package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;

import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.icons.IconId;
import com.dlsc.flexgantt.icons.IconRegistry;
import com.dlsc.flexgantt.swing.action.treetable.AbstractTreeTableAction;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

/**
 * @author wangfan3
 * 
 * 甘特图下移按钮
 *
 */
public class MMGPGantDownMoveAction extends AbstractTreeTableAction {

	private AppGantContext context;

	private Integer number;

	public MMGPGantDownMoveAction(TreeTable treeTable, AppGantContext context,
			Integer number) {
		super(
				treeTable,
				NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0119")/*下移行*/, IconRegistry.getIcon(IconId.FOCUS_ARROW_DOWN)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}
	
	public MMGPGantDownMoveAction(AppGantContext context,
			Integer number) {
		super(
				context.getGantchart().getTreeTable(),
				NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0119")/*下移行*/, IconRegistry.getIcon(IconId.FOCUS_ARROW_DOWN)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TreeTable table = getTreeTable();
		TreePath[] paths = table.getSelectionPaths();
		if(paths.length!=1){
			return;
		}
		((MMGPGantTreetable) table).downMoveNode(paths[0]);
	}

	@Override
	public boolean isEnabled() {
		boolean actionStateOfContext = true;
		if (context != null) {
			actionStateOfContext = context.getActionStateOfContext(number);
		}
		return actionStateOfContext && treeTable.getSelectionCount() > 0 && super.isEnabled();
	}
}
