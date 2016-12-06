package nc.ui.mmgp.uif2.view.treetable;

import javax.swing.tree.TreeModel;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableTools;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.treetable.IBillTreeCreateStrategy;
import nc.ui.pub.bill.treetable.IBillTreeTableModel;
import nc.ui.pub.bill.treetable.ITableTreeFactory;
import nc.ui.pub.bill.treetable.TableTreeFactory;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午11:15:52
 * @author: tangxya
 */
public class MMGPTreeTableBillScrollPane extends BillScrollPane {

	@SuppressWarnings("unused")
	private final int TABLESHOWMODE = 0;
	private final int TREETABLESHOWMODE = 1;

	private ITableTreeFactory tabletreefactory;

	public ITableTreeFactory getTabletreefactory() {
		return tabletreefactory;
	}

	public void setTabletreefactory(ITableTreeFactory tabletreefactory) {
		this.tabletreefactory = tabletreefactory;
	}

	/**
	 * BillScrollPane 构造子注解.
	 * 
	 * @param vsbPolicy
	 *            int
	 * @param hsbPolicy
	 *            int
	 */
	public MMGPTreeTableBillScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	protected static MMGPTreeTableBillScrollPane createDefaultBillScrollPane() {
		return new MMGPTreeTableBillScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED,
				HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public void switchTreeTableShow(
			IBillTreeCreateStrategy billTreeCreateStrategy) {
		ITableTreeFactory tabletreefactory = this.tabletreefactory;

		switch (billTreeCreateStrategy.getTreeMode()) {
		case IBillTreeCreateStrategy.TREETABLE:
			tabletreefactory = this.tabletreefactory;
			break;
		case IBillTreeCreateStrategy.TABLETREE:
			tabletreefactory = new TableTreeFactory();
			break;

		default:
			break;
		}

		initTableTree(billTreeCreateStrategy, tabletreefactory);

	}

	private TreeModel initTableTree(
			IBillTreeCreateStrategy billTreeCreateStrategy,
			ITableTreeFactory tabletreefactory) {

		IBillTreeTableModel treeTableModel = MMGPBillTreeTableTools
				.creatTreeModelByStrategy(getTableModel(),
						billTreeCreateStrategy);

		tree = tabletreefactory.creatTreeTable(this, treeTableModel).getTree();

		mode = TREETABLESHOWMODE;

		return treeTableModel;
	}

	/**
	 * 设置右键菜单不可见
	 */
	@Override
	public void addTableBodyMenu() {

		setBBodyMenuShow(false);
	}

}
