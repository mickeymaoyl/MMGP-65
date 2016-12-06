package nc.ui.mmgp.uif2.actions.treeedit;

import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import nc.bs.uif2.IActionCode;
import nc.ui.mmgp.uif2.actions.MMGPDeleteAction;
import nc.ui.uif2.actions.ActionInitializer;
import nc.uitheme.ui.ThemeResourceCenter;

public class TreeDelAction extends MMGPDeleteAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 4882804770728326108L;
	private String treeDeleteImagePath = "themeres/ui/toolbaricons/delete.png";

	public TreeDelAction() {
		super();
		ActionInitializer.initializeAction(this, IActionCode.DEL);
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.ALT_MASK));
		putValue(Action.SHORT_DESCRIPTION, nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("common", "UC001-0000039")/* @res "É¾³ý" */
				+ "(Alt+D)");
		putValue(Action.SMALL_ICON,
				ThemeResourceCenter.getInstance().getImage(treeDeleteImagePath));
	}
}
