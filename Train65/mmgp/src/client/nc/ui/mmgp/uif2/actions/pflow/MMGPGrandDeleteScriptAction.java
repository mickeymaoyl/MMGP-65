package nc.ui.mmgp.uif2.actions.pflow;

import java.awt.event.ActionEvent;

import nc.bs.pubapp.pf.util.ApproveFlowUtil;
import nc.bs.uif2.IActionCode;
import nc.md.data.access.NCObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.pf.BillStatusEnum;

@SuppressWarnings("serial")
public class MMGPGrandDeleteScriptAction extends MMGPGrandScriptPFlowAction {

    public MMGPGrandDeleteScriptAction() {
        super();
        ActionInitializer.initializeAction(this, IActionCode.DELETE);
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        if (UIDialog.ID_YES == CommonConfirmDialogUtils.showConfirmDeleteDialog(this
            .getModel()
            .getContext()
            .getEntranceUI())) {
            super.doAction(e);
        }

    }

    protected void doSuperAction(ActionEvent e) throws Exception {
        super.doAction(e);
    }

    @Override
    protected boolean isActionEnable() {
        Object[] objs = this.getMainGrandModel().getSelectedOperaDatas();
        if (objs != null && objs.length > 1) {
            return true;
        }

        boolean isEnable =
                this.model.getUiState() == UIState.NOT_EDIT && this.getMainGrandModel().getSelectedData() != null;
        if (isEnable && this.getMainGrandModel().getSelectedData() != null) {
            NCObject obj = NCObject.newInstance(this.getMainGrandModel().getSelectedData());
            // 自由的可以删除
            if (obj != null) {
                Integer fstatusflag = ApproveFlowUtil.getBillStatus(obj);
                if (this.tryMakeFlow(fstatusflag)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param 审批不通过和审批通过在业务上只表示一种审批意见
     *        ，对单据的维护要求是一致的
     * @return
     */
    protected boolean tryMakeFlow(Integer fstatusflag) {
        return BillStatusEnum.FREE.value().equals(fstatusflag);
    }

    @Override
    protected void processReturnObj(Object[] returnObj) throws Exception {
        Object[] retObj = returnObj;
        if (retObj == null || retObj.length == 0) {
            retObj = this.getFlowContext().getBillVos();
        }
        if (PfUtilClient.isSuccess() && retObj[0] instanceof AggregatedValueObject) {
            Object[] deleteObjs = retObj;
            if (retObj.length > 1) {
                deleteObjs = this.getMultibillScriptRunner().getOldSuccessfulObj();
            }
            deleteObjs = this.getFullBills(deleteObjs);
            this.model.directlyDelete(deleteObjs);
        }
        if (this.getMultibillScriptRunner().isTaskSuccessful()) {
            this.showSuccessInfo();
        }
        if (!this.getMultibillScriptRunner().isTaskSuccessful() && retObj.length > 1) {
            this.showFailedInfo();
        }

    }
}
