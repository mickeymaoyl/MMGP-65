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
                                                                                                                     * "�ջ�"
                                                                                                                     */);
    }

    @Override
    protected String getErrorMsg() {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0341")/*
                                                                                                 * @res "�ջ�ʧ�ܣ�"
                                                                                                 */;
    }

    @Override
    protected boolean isActionEnable() {
        // һ��Ҫ�ӣ�Ч���������
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
        // ���ݹ�������2011.6.25
        // ������״̬��������Ϊ�յĵ��ݿ����ջ� ���������ύ̬
        boolean isEnable =
                (selectedData != null && status == ApproveStatus.APPROVING && StringUtils.isEmpty(approver))
                    || ApproveStatus.COMMIT == status;
        return isEnable;
    }
}
