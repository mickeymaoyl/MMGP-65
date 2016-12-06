package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.tree.TreePath;

import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.swing.action.treetable.IndentNodeAction;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

/**
 * @author wangfan3
 * 
 *  ¸ÊÌØÍ¼×óÒÆ°´Å¥
 *
 */
public class MMGPGantIndentNodeAction extends IndentNodeAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppGantContext context;
	
	private Integer number;

	public MMGPGantIndentNodeAction(AppGantContext context,Integer number) {
		super(context.getGantchart().getTreeTable());
		this.context = context;
		this.number = number;
	}

	@Override
	public boolean isEnabled() {
		boolean actionStateOfContext = true;
		if(context != null){
			actionStateOfContext = context.getActionStateOfContext(number);
		} 
		return actionStateOfContext && super.isEnabled();
	}
	
	public void actionPerformed(ActionEvent e) {
		TreeTable table = getTreeTable();
		TreePath[] paths = table.getSelectionPaths();
		sortPaths(paths);
		table.indentNodes(paths);
	}

	private void sortPaths(TreePath[] paths) {
		Arrays.sort(paths, new Comparator<TreePath>() {

			@Override
			public int compare(TreePath o1, TreePath o2) {
				MMGPGanttChartNode node1 = (MMGPGanttChartNode) o1
						.getLastPathComponent();
				MMGPGanttChartNode node2 = (MMGPGanttChartNode) o2
						.getLastPathComponent();
				int index1 = node1.getParent().getIndex(node1);
				int index2 = node2.getParent().getIndex(node2);
				return index1 - index2;
			}
		});

	}
}
