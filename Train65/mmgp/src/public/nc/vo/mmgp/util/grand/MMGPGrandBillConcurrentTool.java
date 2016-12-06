package nc.vo.mmgp.util.grand;

import java.util.List;

import nc.impl.pubapp.pattern.data.bill.tool.BillConcurrentTool;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙加锁校验TS.
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillConcurrentTool extends BillConcurrentTool {

    @Override
    public void checkTS(IBill[] bills,
                        IBill[] originBills) {
        // 校验主子表ts.
        super.checkTS(bills, originBills);
        // 校验孙表ts.
        this.checkGrandChildTS(bills, originBills);
    }

    @Override
    public void lockBill(IBill[] bills) {
        // 锁定主子.
        super.lockBill(bills);
        // 锁定孙表.
        this.lockGrandChildVOs(bills);
    }

    public void checkGrandChildTS(IBill[] bills,
                                  IBill[] originBills) {
        List<ISuperVO> billGCVOList = MMGPGrandBillUtil.getInstance().getAllGrandChildVOs(bills);
        ISuperVO[] billGCVOs = billGCVOList.toArray(new ISuperVO[0]);
        MMGPGrandBillIndex index = new MMGPGrandBillIndex(originBills);
        for (ISuperVO voTemp : billGCVOs) {
            this.checkTS(voTemp, index);
        }
    }

    private void checkTS(ISuperVO vo,
                         MMGPGrandBillIndex index) {
        String key = vo.getPrimaryKey();
        if (key == null) {
            return;
        }
        // 新增行，但是前台界面已经设置上pK
        else if (vo.getStatus() == VOStatus.NEW) {
            return;
        }
        ISuperVO originVO = index.get(vo.getMetaData(), key);
        if (originVO == null) {
            this.throwUnSynchronizedException();
        }
        ISuperVO[] vos = new ISuperVO[]{vo };
        ISuperVO[] originVOs = new ISuperVO[]{originVO };

        VOConcurrentTool bo = new VOConcurrentTool();
        bo.checkTS(vos, originVOs);
    }

    private void lockGrandChildVOs(IBill[] bills) {
        for (IBill billTemp : bills) {
            this.lockGrandChildVO(billTemp);
        }
    }

    private void lockGrandChildVO(IBill bills) {
        List<ISuperVO> vos = MMGPGrandBillUtil.getInstance().getAllGrandChildVOs(bills);
        VOConcurrentTool bo = new VOConcurrentTool();
        if (MMCollectionUtil.isEmpty(vos)) {
            return;
        }
        bo.lock(vos.toArray(new ISuperVO[0]));
    }

    private void throwUnSynchronizedException() {
        String tip = null;
        String message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0177")/*
                                                                                                           * @res
                                                                                                           * "出现并发，请重新查询"
                                                                                                           */;
        ExceptionUtils.wrappBusinessException(message, tip);
    }

}
