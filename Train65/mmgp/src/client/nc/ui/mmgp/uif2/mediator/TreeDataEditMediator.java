package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.view.treeedit.TreeDataEditDlg;
import nc.ui.uif2.DefaultExceptionHanler;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;

public class TreeDataEditMediator {

	private BillForm editorPane;
	private AbstractAppModel model;
	private TreeDataEditDlg dlg;
	private NCAction saveAction;
	private NCAction cancelAction;
	private DefaultExceptionHanler exeptionHandler;

	public void initDlg() {
		dlg = new TreeDataEditDlg(model.getContext().getEntranceUI());
		dlg.setEditorPane(editorPane);
		dlg.setSaveAction(saveAction);
		dlg.setCancelAction(cancelAction);
		dlg.setHandler(exeptionHandler);
		dlg.initUI();
	}

	public void showDlg() {
		dlg.showModal();
	}

	public BillForm getEditorPane() {
		return editorPane;
	}

	public void setEditorPane(BillForm editorPane) {
		this.editorPane = editorPane;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	public NCAction getSaveAction() {
		return saveAction;
	}

	public void setSaveAction(NCAction saveAction) {
		this.saveAction = saveAction;
	}

	public NCAction getCancelAction() {
		return cancelAction;
	}

	public void setCancelAction(NCAction cancelAction) {
		this.cancelAction = cancelAction;
	}

	public DefaultExceptionHanler getExeptionHandler() {
		return exeptionHandler;
	}

	public void setExeptionHandler(DefaultExceptionHanler exeptionHandler) {
		this.exeptionHandler = exeptionHandler;
	}

}
