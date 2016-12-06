/**
 * 
 */
package nc.ui.train.saleout.aciton;

import java.awt.event.ActionEvent;

import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.AbstractReferenceAction;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.PubShowUpableBillForm;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.train.saleoutdemo.AggSaleOut;

/**
 * @author maoyulong
 *
 */
public class AddSaleOrder extends AbstractReferenceAction {

	private PubShowUpableBillForm editor;


	private BillManageModel model;

	
	public PubShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(PubShowUpableBillForm editor) {
		this.editor = editor;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}

	/**
	 * 
	 */
	public AddSaleOrder() {
		// TODO 自动生成的构造函数存根
	}

	/* （非 Javadoc）
	 * @see nc.ui.uif2.NCAction#doAction(java.awt.event.ActionEvent)
	 */
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		PfUtilClient.childButtonClickedNew(createPfButtonClickContext());
		if (PfUtilClient.isCloseOK()) {
			AggSaleOut[] vos = (AggSaleOut[]) PfUtilClient.getRetVos();
			// 显示到转单界面上
			this.getTransferViewProcessor().processBillTransfer(vos);
//			this.model.setUiState(UIState.ADD);
//			this.editor.showMeUp();
		}
	}



	private PfButtonClickContext createPfButtonClickContext() {
		// TODO 自动生成的方法存根
		PfButtonClickContext context = new PfButtonClickContext();
		context.setSrcBillType(this.getSourceBillType());
		context.setParent(this.getModel().getContext().getEntranceUI());
		context.setPk_group(this.getModel().getContext().getPk_group());
		context.setUserId(this.getModel().getContext().getPk_loginUser());
		context.setCurrBilltype("TR12");
		
		return context;
	}
	
	
}
