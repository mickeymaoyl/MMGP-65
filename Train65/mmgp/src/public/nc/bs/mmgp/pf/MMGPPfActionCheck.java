package nc.bs.mmgp.pf;

import nc.bs.pub.pf.CheckStatusCallbackContext;
import nc.bs.pub.pf.ICheckStatusCallback;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 审批流回调处理类
 * </p>
 * 
 * @since 创建日期 Aug 2, 2013
 * @author wangweir
 */
public class MMGPPfActionCheck implements ICheckStatusCallback {

    @Override
    public void callCheckStatus(CheckStatusCallbackContext cscc) throws BusinessException {
        if (cscc.getBillVo() == null || !(cscc.getBillVo() instanceof IBill)) {
            return;
        }

        IBill bill = (IBill) cscc.getBillVo();
        String fstatusflag = MMMetaUtils.getIFlowBizItfMapKey4IBill(bill, IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
        String approver = MMMetaUtils.getIFlowBizItfMapKey4IBill(bill, IFlowBizItf.ATTRIBUTE_APPROVER);
        String tauditime = MMMetaUtils.getIFlowBizItfMapKey4IBill(bill, IFlowBizItf.ATTRIBUTE_APPROVEDATE);

        ISuperVO parent = bill.getParent();

        if (cscc.getApproveDate() != null && cscc.getApproveId() != null) {
            parent.setAttributeValue(tauditime, UFDate.fromPersisted(cscc.getApproveDate()));
            parent.setAttributeValue(approver, cscc.getApproveId());
        } else {
            parent.setAttributeValue(tauditime, null);
            parent.setAttributeValue(approver, null);
        }
        // 更新表头
        String[] names = new String[]{fstatusflag, approver, tauditime };
        VOUpdate<ISuperVO> bo = new VOUpdate<ISuperVO>();
        bo.update(new ISuperVO[]{parent }, names);

    }
}
