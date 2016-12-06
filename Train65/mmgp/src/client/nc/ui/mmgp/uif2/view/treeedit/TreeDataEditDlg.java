package nc.ui.mmgp.uif2.view.treeedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import nc.ui.uap.rbac.RbacCommonOkCancelDlg;
import nc.ui.uif2.DefaultExceptionHanler;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;

public class TreeDataEditDlg extends RbacCommonOkCancelDlg {

	/**
	 *
	 */
	private static final long serialVersionUID = -1538037119662787478L;
	private BillForm editorPane;
	private AbstractAppModel model;
	private NCAction saveAction;
	private NCAction cancelAction;
	private DefaultExceptionHanler handler = null;

	public TreeDataEditDlg(Container parent) {
		super(parent);
		setSize(new Dimension(650, 400));

	}

	public void initUI() {
		if (handler == null) {
			handler = new DefaultExceptionHanler();
			handler.setContext(model.getContext());
		}
		// TODO title
		setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0035")/*@res "±à¼­Ê÷"*/);
		getUIPanelMain().setLayout(new BorderLayout());
		getUIPanelMain().add(getEditorPane(), BorderLayout.CENTER);

	}

	public BillForm getEditorPane() {
		return editorPane;
	}

	public void setEditorPane(BillForm editorPane) {
		this.editorPane = editorPane;
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

	@Override
	public void closeCancel() {

		if (cancelAction != null) {
			try {
				cancelAction.doAction(null);
			} catch (Exception e) {
				getHandler().handlerExeption(e);
			}
		}
		try {
			super.closeCancel();
		} catch (Exception e) {
			getHandler().handlerExeption(e);
		}

	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.closeCancel();
		} else {
			super.processWindowEvent(e);
		}
	}

	@Override
	public void closeOK() {
		try {
			if (saveAction != null)
				saveAction.doAction(null);
			super.closeOK();
		} catch (Exception e) {
			getHandler().handlerExeption(e);
		}
	}

	public void setCancelAction(NCAction cancelAction) {
		this.cancelAction = cancelAction;
	}

	public DefaultExceptionHanler getHandler() {
		return handler;
	}

	public void setHandler(DefaultExceptionHanler handler) {
		this.handler = handler;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

}