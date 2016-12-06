package nc.ui.mmgp.uif2.actions.pflow;

import nc.bs.pubapp.pf.util.ApproveFlowUtil;
import nc.bs.uif2.IActionCode;
import nc.md.data.access.NCObject;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.IActionExecutable;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveStatus;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.uif2.actions.ActionInitializer;

public class MMGPGrandCommitScriptAction extends MMGPGrandScriptPFlowAction implements IActionExecutable {

    private static final long serialVersionUID = 1L;

    public MMGPGrandCommitScriptAction() {
        ActionInitializer.initializeAction(this, IActionCode.COMMIT);
        this.setActionName(IPFACTION.COMMIT);
        this
            .setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0116")/* @res "单据送审" */);
    }

    @Override
    public void doBeforAction() {
        if (this.model.getAppUiState() == AppUiState.NOT_EDIT) {
            this.setComposite(false);
        } else {
            this.setComposite(true);
        }

    }

    @Override
    public boolean isExecuted() {
        return this.getIsPreActionSuccesful();
    }

    @Override
    protected String getErrorMsg() {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0340")/*
                                                                                                 * @res "提交失败！"
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
        boolean isEnable = selectedData != null && status == ApproveStatus.FREE;

        return isEnable;
    }

}
