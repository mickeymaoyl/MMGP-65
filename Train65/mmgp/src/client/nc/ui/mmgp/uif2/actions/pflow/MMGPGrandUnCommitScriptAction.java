package nc.ui.mmgp.uif2.actions.pflow;

import javax.swing.Action;

import nc.funcnode.ui.action.INCAction;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveStatus;
import nc.vo.pub.AggregatedValueObject;

import org.apache.commons.lang.StringUtils;

public class MMGPGrandUnCommitScriptAction extends MMGPGrandScriptPFlowAction {

    /**
   *
   */
    private static final long serialVersionUID = 2729125366482308927L;

    public MMGPGrandUnCommitScriptAction() {
        super();
        this.putValue(INCAction.CODE, "UnCommit");
        this
            .putValue(Action.NAME, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0118")/*
                                                                                                                     * @res
                                                                                                                     * "收回"
                                                                                                                     */);
    }

    @Override
    protected String getErrorMsg() {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0341")/*
                                                                                                 * @res "收回失败！"
                                                                                                 */;
    }

    @Override
    protected boolean isActionEnable() {
        // 一定要加，效率问题否则
        Object[] seldatas = this.getMainGrandModel().getSelectedOperaDatas();
        if (this.model.getAppUiState() == AppUiState.NOT_EDIT && null != seldatas && seldatas.length > 1) {
            return true;
        }

        AggregatedValueObject selectedData = (AggregatedValueObject) this.getMainGrandModel().getSelectedData();
        int status = ApproveStatus.FREE;
        String approver = null;
        if (selectedData != null) {
            status = this.extractApproveStatus(selectedData).intValue();
            approver = this.extractApprover(selectedData);
        }
        // 根据公共需求2011.6.25
        // 审批中状态、审批人为空的单据可以收回 或者是已提交态
        boolean isEnable =
                (selectedData != null && status == ApproveStatus.APPROVING && StringUtils.isEmpty(approver))
                    || ApproveStatus.COMMIT == status;
        return isEnable;
    }
}
