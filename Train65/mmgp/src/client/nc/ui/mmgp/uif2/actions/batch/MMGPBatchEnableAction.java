package nc.ui.mmgp.uif2.actions.batch;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.bs.uif2.IActionCode;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.vo.bd.pub.IPubEnumConst;

public class MMGPBatchEnableAction extends MMGPBatchSealActionBase {

    private static final long serialVersionUID = 283994281412077261L;

    public MMGPBatchEnableAction() {
        super();
        ActionInitializer.initializeAction(this, IActionCode.ENABLE);
    }

    @Override
    protected boolean isActionEnable() {
        boolean actionEnable = true;

        if (this.getSelectedDataStatus() == MMGPBatchSealActionBase.ALL_ENABLE) {
            actionEnable = false;
        }

        // 未启用或已停用的数据的启用按钮可用
        return getModel().getUiState() == UIState.NOT_EDIT && getModel().getSelectedData() != null && actionEnable;
    }

    @Override
    protected int showConfirmDialog() {
        return CommonConfirmDialogUtils.showConfirmEnableDialog(getModel().getContext().getEntranceUI());
    }

    @Override
    protected void setEnableState(Object obj) throws Exception {
        MMGPVoUtils.setHeadVOFieldValue(obj, IBaseServiceConst.ENABLESTATE_FIELD, IPubEnumConst.ENABLESTATE_ENABLE);
    }

}
