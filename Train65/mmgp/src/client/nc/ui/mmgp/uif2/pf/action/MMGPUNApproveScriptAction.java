package nc.ui.mmgp.uif2.pf.action;

import nc.ui.pubapp.uif2app.actions.pflow.UNApproveScriptAction;

/**
 * @author wenjl
 * @date 2013-6-27
 */
public class MMGPUNApproveScriptAction extends UNApproveScriptAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8911809501783995342L;
	
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
//	    
//	    //审批中或审批通过时，取消审批可用
//	    boolean isEnable =
//	        this.model.getAppUiState() == AppUiState.NOT_EDIT && 
//	        selectedData != null && 
//	        (status == ApproveStatus.APPROVED || status == ApproveStatus.APPROVING || status == ApproveStatus.NOPASS);
//
//	    return isEnable;
//	  }

}
