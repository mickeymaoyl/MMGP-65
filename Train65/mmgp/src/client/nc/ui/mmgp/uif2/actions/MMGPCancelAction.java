package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.actions.CancelAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.EventFromClosingHandlerJudger;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.ui.uif2.editor.IBillCardPanelEditor;

public class MMGPCancelAction extends CancelAction {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -5561454220575934008L;

	  /**
	   * �Ƿ�����ȡ��
	   */
	  protected boolean canceled = false;

	  @Override
	  public void doAction(ActionEvent e) throws Exception {
	    // ����Ǵ�ClosingHandler������CancelAction����ѯ�ʣ�ֱ��ȡ��
	    // ����ڶ�ҳǩ������������ġ� ����һ��Ľڵ㣬���ֱ�ӹر��ˣ��Ƿ����setUiState��ʵ�޹ؽ�Ҫ
	    if (EventFromClosingHandlerJudger.isFromClosingHandler(e)
	        || UIDialog.ID_YES == CommonConfirmDialogUtils
	            .showConfirmCancelDialog(this.getModel().getContext()
	                .getEntranceUI()))

	    {

	      this.doBeforeCancel();
	      this.getModel().setUiState(UIState.NOT_EDIT);
	      // ȡ��ʱ��������״̬�仯�⣬����Ҫ��������ѡ������
	      this.doResetSelectedData();
	      ShowStatusBarMsgUtil.showStatusBarMsg(
	          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0",
	              "0pubapp-0125")/*@res "��ȡ��"*/, this.getModel().getContext());
	      this.canceled = true;
	      if(getEditor() !=null &&getEditor() instanceof IBillCardPanelEditor){
	         ((IBillCardPanelEditor) getEditor()).getBillCardPanel().getHeadTabbedPane().clearShowWarning();
	          ((IBillCardPanelEditor) getEditor()).getBillCardPanel().getBillData().clearShowWarning();
	      }
	    }
	    else {
	      this.canceled = false;
	    }
	  }

	  public boolean isCanceled() {
	    return this.canceled;
	  }
}
