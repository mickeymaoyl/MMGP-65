package nc.ui.mmgp.uif2.actions.pflow;

import nc.bs.pubapp.pf.util.ApproveFlowUtil;
import nc.bs.uif2.IActionCode;
import nc.md.data.access.NCObject;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveStatus;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.uif2.actions.ActionInitializer;

public class MMGPGrandUnApproveScriptAction extends MMGPGrandScriptPFlowAction {
    private static final long serialVersionUID = 1L;

    public MMGPGrandUnApproveScriptAction() {
        ActionInitializer.initializeAction(this, IActionCode.UNAPPROVE);
        this.setActionName(IPFACTION.UNAPPROVE);
    }

    @Override
    public void doBeforAction() {
        super.doBeforAction();
        this.getFlowContext().setUserId(this.model.getContext().getPk_loginUser());

    }

    @Override
    protected String getErrorMsg() {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0339")/*
                                                                                                 * @res "弃审失败！"
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

        // 审批中或审批通过时，取消审批可用
        boolean isEnable =
                this.model.getAppUiState() == AppUiState.NOT_EDIT
                    && selectedData != null
                    && (status == ApproveStatus.APPROVED || status == ApproveStatus.APPROVING || status == ApproveStatus.NOPASS);

        return isEnable;
    }

}
