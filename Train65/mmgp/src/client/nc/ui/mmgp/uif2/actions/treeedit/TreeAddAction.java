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

public class TreeAddAction extends NCAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 4882804770728326108L;
	private String treeAddImagePath = "themeres/ui/toolbaricons/add-child.png";

	private HierachicalDataAppModel model;

	private TreeDataEditDlg treeDataEditDlg;

	public TreeAddAction() {
		super();
		ActionInitializer.initializeAction(this, IActionCode.ADD);
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.ALT_MASK));
		putValue(Action.SHORT_DESCRIPTION, nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("common", "UC001-0000108")/*
																	 * @res "ÐÂÔö"
																	 */
				+ "(Alt+I)");
		putValue(Action.SMALL_ICON,
				ThemeResourceCenter.getInstance().getImage(treeAddImagePath));
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		UIState old = model.getUiState();
		try {
			model.setUiState(UIState.ADD);
			treeDataEditDlg.showModal();
		} finally {
			model.setUiState(old);
		}
	}

	@Override
	protected boolean isActionEnable() {
		return true;
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
