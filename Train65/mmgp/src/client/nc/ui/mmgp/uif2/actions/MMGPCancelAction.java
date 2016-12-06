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
	   * 是否真正取消
	   */
	  protected boolean canceled = false;

	  @Override
	  public void doAction(ActionEvent e) throws Exception {
	    // 如果是从ClosingHandler触发的CancelAction不用询问，直接取消
	    // 这对于多页签档案是有意义的。 对于一般的节点，如果直接关闭了，是否调用setUiState其实无关紧要
	    if (EventFromClosingHandlerJudger.isFromClosingHandler(e)
	        || UIDialog.ID_YES == CommonConfirmDialogUtils
	            .showConfirmCancelDialog(this.getModel().getContext()
	                .getEntranceUI()))

	    {

	      this.doBeforeCancel();
	      this.getModel().setUiState(UIState.NOT_EDIT);
	      // 取消时，除进行状态变化外，还需要重新设置选中数据
	      this.doResetSelectedData();
	      ShowStatusBarMsgUtil.showStatusBarMsg(
	          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0",
	              "0pubapp-0125")/*@res "已取消"*/, this.getModel().getContext());
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
