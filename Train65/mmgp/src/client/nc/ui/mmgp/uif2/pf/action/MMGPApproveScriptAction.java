package nc.ui.mmgp.uif2.pf.action;

import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;

/**
 * @author wenjl
 * @date 2013-6-27
 */
public class MMGPApproveScriptAction extends ApproveScriptAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 594156094898518373L;
	
//	@Override
//	  protected boolean isActionEnable() {
//	    Object selectedData = this.model.getSelectedData();
//	    int status = ApproveStatus.FREE;
//	    if (selectedData != null) {
//	      NCObject obj = NCObject.newInstance(selectedData);
//	      if (obj != null) {
//	        status = ApproveFlowUtil.getBillStatus(obj).intValue();
//	      }
//	    }
//	    boolean isEnable =
//	        this.model.getAppUiState() == AppUiState.NOT_EDIT
//	            && selectedData != null;
//	    boolean flag =
//	    	//最新UE规范：要求“未找到匹配审批流的单据也必须提交后才能审批，即自由态单据不能审批。”
//	        //status == ApproveStatus.FREE || 
//	        status == ApproveStatus.APPROVING
//	            || status == IPfRetCheckInfo.COMMIT;
//	    isEnable = isEnable && flag;
//
//	    return isEnable;
//	  }

}
