package nc.ui.mmgp.uif2.pf.action;

import nc.ui.pubapp.uif2app.actions.pflow.DeleteScriptAction;

/**
 * @author wenjl
 * @date 2013-6-27
 */
public class MMGPDeleteScriptAction extends DeleteScriptAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3308592314138946791L;

//	@Override
//	protected boolean isActionEnable() {
//
//		boolean isEnable = this.model.getUiState() == UIState.NOT_EDIT
//				&& this.model.getSelectedData() != null;
//		if (isEnable) {
//			NCObject obj = NCObject.newInstance(this.getModel()
//					.getSelectedData());
//			// 自由的可以删除
//			if (obj != null) {
//				Integer fstatusflag = ApproveFlowUtil.getBillStatus(obj);
//				if (this.tryMakeFlow(fstatusflag)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

}
