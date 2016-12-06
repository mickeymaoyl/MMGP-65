package nc.ui.mmgp.flexgant.actions;

import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 * 
 * 甘特图 粘贴 粘贴为平级
 *
 */
public class MMGPGantPasteAsLevelAction extends MMGPGantPasterAction {

	public MMGPGantPasteAsLevelAction(AppGantContext context, Integer number) {
		super(context, NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0121")/*粘贴为平级*/, number);
	}

	protected MMGPGanttChartNode getParentNode(TreePath selectpath) {
		return (MMGPGanttChartNode) selectpath.getParentPath()
				.getLastPathComponent();
	}

}
