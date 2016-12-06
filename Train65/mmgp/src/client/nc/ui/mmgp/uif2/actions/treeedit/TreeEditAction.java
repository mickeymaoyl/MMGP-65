package nc.ui.mmgp.uif2.actions.treeedit;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import nc.bs.uif2.IActionCode;
import nc.ui.mmgp.uif2.view.treeedit.TreeDataEditDlg;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.uitheme.ui.ThemeResourceCenter;

public class TreeEditAction extends NCAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 4882804770728326108L;
	private String treeEditImagePath = "themeres/ui/toolbaricons/edit.png";
	private HierachicalDataAppModel model;
	private TreeDataEditDlg treeDataEditDlg;

	public TreeEditAction() {
		super();
		ActionInitializer.initializeAction(this, IActionCode.EDIT);
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.ALT_MASK));
		putValue(Action.SHORT_DESCRIPTION, nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("common", "UC001-0000045")/*
																	 * @res "ÐÞ¸Ä"
																	 */
				+ "(Alt+E)");
		putValue(Action.SMALL_ICON,
				ThemeResourceCenter.getInstance().getImage(treeEditImagePath));
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// showDialog
		UIState old = model.getUiState();
		model.setUiState(UIState.EDIT);
		treeDataEditDlg.showModal();
		model.setUiState(old);
	}

	@Override
	protected boolean isActionEnable() {
		return model.getSelectedData() != null;
	}

	public HierachicalDataAppModel getModel() {
		return model;
	}

	public void setModel(HierachicalDataAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public TreeDataEditDlg getTreeDataEditDlg() {
		return treeDataEditDlg;
	}

	public void setTreeDataEditDlg(TreeDataEditDlg treeDataEditDlg) {
		this.treeDataEditDlg = treeDataEditDlg;
	}

}
