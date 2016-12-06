package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.RefreshAction;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.ui.uif2.editor.BillForm;

public class MMGPRefreshAction extends RefreshAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2261091978862386934L;
	protected BillForm editor;
	RefreshSingleAction singleRefresh = null;

	public MMGPRefreshAction() {

	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		if (editor != null && editor.isShowing()) {
			singleRefresh.doAction(e);
		} else {
			super.doAction(e);
		}
	}

	@Override
	protected boolean isActionEnable() {
		if (editor != null && editor.isShowing()) {
			return getModel().getSelectedData() != null;
		} else {
			return super.isActionEnable();
		}
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public RefreshSingleAction getSingleRefresh() {
		return singleRefresh;
	}

	public void setSingleRefresh(RefreshSingleAction singleRefresh) {
		this.singleRefresh = singleRefresh;
	}

}
