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
//	    	//����UE�淶��Ҫ��δ�ҵ�ƥ���������ĵ���Ҳ�����ύ�����������������̬���ݲ�����������
//	        //status == ApproveStatus.FREE || 
//	        status == ApproveStatus.APPROVING
//	            || status == IPfRetCheckInfo.COMMIT;
//	    isEnable = isEnable && flag;
//
//	    return isEnable;
//	  }

}
