package nc.ui.mmgp.uif2.actions.batch;

import nc.bs.uif2.IActionCode;
import nc.ui.ml.NCLangRes;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;

public class MMGPManageBatchDisableAction extends MMGPManageBatchSealBase{

	/**
	 *
	 */
	private static final long serialVersionUID = 7315295927947699266L;

	public MMGPManageBatchDisableAction() {
		super();
		initializeAction();
	}

	protected void initializeAction(){
		ActionInitializer.initializeAction(this, IActionCode.DISABLE);
	}

	@Override
	protected int showConfirmDialog() {
	   return CommonConfirmDialogUtils.showConfirmDisableDialog(getModel().getContext().getEntranceUI());
	}

	@Override
	protected MMGPValueObjWithErrLog doOperate(Object[] vos) throws Exception {
		MMGPValueObjWithErrLog log = getOperateService().cmnManageDisableBillData(vos);
		return log;
	}

	@Override
	protected void checkStateEnable() {
		String errmsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0002")/*@res "该数据已停用！"*/;
		for (Object obj : getSelectedOperaDatas()) {
			if (isDisabled(obj)) {
				this.getOperData().addErrLogMessage(obj, errmsg);
			}
		}

	}

	@Override
	protected boolean isActionEnable() {
		boolean actionEnable = true;

		if (getSelectedDataStatus() == ALL_DISABLE) {
			actionEnable = false;
		}
		// 未启用或已停用的数据的启用按钮可用
		return getModel().getUiState() == UIState.NOT_EDIT
				&& getModel().getSelectedData() != null && actionEnable;
	}

	@Override
	protected void showStatusMsg() {
		//maoyl
		String msg =NCLangRes.getInstance().getStrByID("uif2", "IShowMsgConstant-000015");
		
		ShowStatusBarMsgUtil.showStatusBarMsg(msg, getModel().getContext());

	}



}