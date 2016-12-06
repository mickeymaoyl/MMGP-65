package nc.ui.mmgp.uif2.actions.batch;

import nc.bs.uif2.IActionCode;
import nc.ui.ml.NCLangRes;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;

public class MMGPManageBatchEnableAction extends MMGPManageBatchSealBase {



	/**
	 *
	 */
	private static final long serialVersionUID = 7315295927947699266L;

	public MMGPManageBatchEnableAction() {
		super();
		initializeAction();
	}

	protected void initializeAction() {
		ActionInitializer.initializeAction(this, IActionCode.ENABLE);
	}

	@Override
	protected int showConfirmDialog() {
		return CommonConfirmDialogUtils.showConfirmEnableDialog(getModel()
				.getContext().getEntranceUI());
	}

	@Override
	protected MMGPValueObjWithErrLog doOperate(Object[] datas) throws Exception {
		MMGPValueObjWithErrLog log = getOperateService().cmnManageEnableBillData(
				datas);
		return log;
	}

	@Override
	protected void checkStateEnable() {
		String errmsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0003")/*@res "该数据已启用！"*/;
		for (Object obj : getSelectedOperaDatas()) {
			if (!isDisabled(obj)) {
				this.getOperData().addErrLogMessage(obj, errmsg);
			}
		}

	}

	@Override
    protected boolean isActionEnable() {
        boolean actionEnable = true;

        if (getSelectedDataStatus() == ALL_ENABLE) {
            actionEnable = false;
        }
        // 未启用或已停用的数据的启用按钮可用
        return getModel().getUiState() == UIState.NOT_EDIT && getModel().getSelectedData() != null && actionEnable;
    }

	@Override
	protected void showStatusMsg() {
		 //maoyl   
        String msg =NCLangRes.getInstance().getStrByID("uif2", "IShowMsgConstant-000014");//原63中/*启用成功。*/;
        ShowStatusBarMsgUtil.showStatusBarMsg(msg, getModel().getContext());
//		ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getEnableSuccessInfo(), getModel().getContext());

	}

}