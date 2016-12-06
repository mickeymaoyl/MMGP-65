package nc.impl.mmgp.uif2.template.grand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.impl.mmgp.uif2.grand.util.GCBillUtil;
import nc.impl.pubapp.pattern.data.vo.VODelete;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.tool.BillHelper;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since 创建日期 May 21, 2013
 * @author wangweir
 */
public class MMGPGrandBillDeleteOperator<E extends IBill> implements IOperator<E> {

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.template.IOperator#operate(E[])
     */
    @Override
    public E[] operate(E[] vos) {
        TimeLog.logStart();
        this.setStatus(vos);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0063")/*@res "单据设置删除状态"*/); /* -=notranslate=- */

        TimeLog.logStart();
        this.persistent(vos);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0064")/*@res "单据保存到数据库"*/); /* -=notranslate=- */

        return vos;
    }

    private void setStatus(E[] bills) {
        BillHelper helper = new BillHelper(bills);
        List<ISuperVO> parentList = helper.getParentList();
        for (ISuperVO vo : parentList) {
            vo.setStatus(VOStatus.DELETED);
        }
        Map<IVOMeta, List<ISuperVO>> itemIndex = helper.getItemIndex().toMap();
        List<ISuperVO> allGrandChildVOList = new ArrayList<ISuperVO>();
        for (List<ISuperVO> list : itemIndex.values()) {
            for (ISuperVO vo : list) {
                vo.setStatus(VOStatus.DELETED);
                GCBillUtil.getInstance().getAllChildVOs(vo, allGrandChildVOList);
            }
        }
        // 设定孙表VO状态.
        for (ISuperVO gcVOTemp : allGrandChildVOList) {
            gcVOTemp.setStatus(VOStatus.DELETED);
        }
    }

    private void persistent(E[] bills) {
        BillHelper helper = new BillHelper(bills);
        List<ISuperVO> parentList = helper.getParentList();
        this.deleteVO(parentList);

        Map<IVOMeta, List<ISuperVO>> itemIndex = helper.getItemIndex().toMap();
        for (List<ISuperVO> list : itemIndex.values()) {
            this.deleteVO(list);
            // 删除孙表.
            this.deleteGCVO(list);
        }
    }

    private void deleteGCVO(List<ISuperVO> list) {
        Map<String, List<ISuperVO>> childVOMap = GCBillUtil.getInstance().getAllChildVOMap(list);
        for (Map.Entry<String, List<ISuperVO>> childVOMapEntry : childVOMap.entrySet()) {
            List<ISuperVO> childVOList = childVOMapEntry.getValue();
            this.deleteVO(childVOList);
            this.deleteGCVO(childVOList);
        }
    }

    private void deleteVO(List<ISuperVO> list) {
        VODelete<ISuperVO> bo = new VODelete<ISuperVO>();
        int length = list.size();
        if (length > 0) {
            ISuperVO[] vos = new ISuperVO[length];
            vos = list.toArray(vos);
            bo.delete(vos);
        }
    }

}