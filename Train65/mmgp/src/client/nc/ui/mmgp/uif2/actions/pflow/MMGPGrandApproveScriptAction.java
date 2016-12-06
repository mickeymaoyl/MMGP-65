package nc.ui.mmgp.uif2.actions.pflow;

import java.util.Map;

import nc.bs.pubapp.pf.util.ApproveFlowUtil;
import nc.bs.uif2.IActionCode;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveStatus;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.uif2.actions.ActionInitializer;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class MMGPGrandApproveScriptAction extends MMGPGrandScriptPFlowAction {

    private static final long serialVersionUID = 1L;

    public MMGPGrandApproveScriptAction() {
        ActionInitializer.initializeAction(this, IActionCode.APPROVE);
        this.setActionName(IPFACTION.APPROVE);
        this
            .setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0115")/* @res "单据审批" */);
    }

    @Override
    public void doBeforAction() {
        super.doBeforAction();
        this.getFlowContext().setUserId(this.model.getContext().getPk_loginUser());

    }

    /*
     * 流程平台处理需要制单人和主组织信息
     */
    @Override
    protected void fillBusitypeAfterLight(AbstractBill[] lightVOs) {
        super.fillBusitypeAfterLight(lightVOs);
        String pk_orgkey = this.getIFlowBizItfMapKey(lightVOs[0], IFlowBizItf.ATTRIBUTE_PKORG);
        String billMakerKey = this.getIFlowBizItfMapKey(lightVOs[0], IFlowBizItf.ATTRIBUTE_BILLMAKER);
        Map<String, AbstractBill> fullVOMap = this.createfullVOMap();
        for (AbstractBill bill : lightVOs) {
            String pk = bill.getPrimaryKey();
            if (StringUtil.isEmpty(pk)) {
                continue;
            }
            AbstractBill fullBill = fullVOMap.get(pk);
            if (null == fullBill) {
                continue;
            }
            if (pk_orgkey != null) {
                String pk_org = (String) fullBill.getParentVO().getAttributeValue(pk_orgkey);
                bill.getParentVO().setAttributeValue(pk_orgkey, pk_org);
            }
            if (billMakerKey != null) {
                String billMaker = (String) fullBill.getParentVO().getAttributeValue(billMakerKey);
                bill.getParentVO().setAttributeValue(billMakerKey, billMaker);
            }
        }
    }

    @Override
    protected String getErrorMsg() {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0338")/*
                                                                                                 * @res "审核失败！"
                                                                                                 */;
    }

    @Override
    protected boolean isActionEnable() {
        Object[] objs = this.getMainGrandModel().getSelectedOperaDatas();
        if (objs != null && objs.length > 1) {
            return true;
        }

        Object selectedData = this.getMainGrandModel().getSelectedData();
        int status = ApproveStatus.FREE;
        if (selectedData != null) {
            NCObject obj = NCObject.newInstance(selectedData);
            if (obj != null) {
                status = ApproveFlowUtil.getBillStatus(obj).intValue();
            }
        }
        boolean isEnable = this.model.getAppUiState() == AppUiState.NOT_EDIT && selectedData != null;
        boolean flag = status == ApproveStatus.APPROVING || status == IPfRetCheckInfo.COMMIT;
        // 最新UE规范：要求“未找到匹配审批流的单据也必须提交后才能审批，即自由态单据不能审批。”
        // status == ApproveStatus.FREE ||
        isEnable = isEnable && flag;

        return isEnable;
    }

}
