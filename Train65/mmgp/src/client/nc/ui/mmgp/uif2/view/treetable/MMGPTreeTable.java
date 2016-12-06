package nc.ui.mmgp.uif2.view.treetable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillModelTreeTableAdapter;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.treetable.IBillTreeTableModel;
import nc.ui.pub.bill.treetable.ITreeTable;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午11:11:41
 * @author: tangxya
 */
public class MMGPTreeTable implements ITreeTable {

	public MMGPTreeTable(IBillTreeTableModel ttm, BillScrollPane sp,
			Comparator<CircularlyAccessibleValueObject> comparator) {
		this.treeTableModel = ttm;
		this.sp = sp;
		this.comparator = comparator;
		initialize();
	}

	private class KnobIcon implements Icon {
		private String text = "";

		private FontMetrics fm = null;

		private Insets insets = new Insets(2, 2, 2, 2);

		private Point local = new Point();

		private int iconW = -1;

		public KnobIcon(String text, FontMetrics fm) {
			super();
			this.fm = fm;
			this.text = text;
		}

		public void setLocal(Point local) {
			this.local = local;
		}

		public Point getLocal() {
			return local;
		}

		public boolean containPoint(Point p) {
			Rectangle rect = new Rectangle(local, new Dimension(getIconWidth(),
					getIconHeight()));
			return rect.contains(p);
		}

		@Override
		public int getIconHeight() {
			return 13;
		}

		@Override
		public int getIconWidth() {
			if (iconW == -1) {
				int strW = fm.stringWidth(text);
				int width = strW + insets.left + insets.right;
				iconW = width < 13 ? 13 : width;
			}
			return iconW;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.translate(x, y);
			Color oldC = g.getColor();
			g.setColor(Color.white);
			g.fillRect(0, 0, getIconWidth() - 1, getIconHeight() - 1);
			g.setColor(oldC);
			int strX = (getIconWidth() - fm.stringWidth(text)) / 2;
			int stry = (getIconHeight() - fm.getHeight()) / 2 + fm.getAscent();
			g.drawString(text, strX, stry);
			g.drawRect(0, 0, getIconWidth() - 1, getIconHeight() - 1);
			g.translate(-x, -y);

		}

	}

	private class QuickExpandTreePanel extends JPanel {
		private static final long serialVersionUID = 5121705994552348634L;

		private int hgap = 2;

		private Insets insets = new Insets(1, 1, 1, 1);

		transient List<KnobIcon> iconList = new ArrayList<KnobIcon>();

		public QuickExpandTreePanel() {
			super();

			setBorder(BorderFactory.createLineBorder(UIManager
					.getColor("TableHeader.lineColor")));

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					expandTree(e);
				}
			});
		}

		@Override
		public synchronized void doLayout() {
			int lineNumeWidth = 0;
			if (showLineNum) {
				lineNumeWidth = calulateLineNumWidth();
			}
			iconList.clear();
			FontMetrics fm = getFontMetrics(getFont());
			Dimension size = getSize();
			int x = insets.left;
			int y = size.height - knobSize.height - insets.bottom;
			int count = 0;
			while (x + knobSize.width < size.width - lineNumeWidth) {
				count++;
				KnobIcon icon = new KnobIcon("" + count, fm);
				icon.setLocal(new Point(x, y));
				iconList.add(icon);
				x += knobSize.width + hgap;
			}

		}

		protected synchronized void expandTree(MouseEvent e) {
			Point p = e.getPoint();
			for (int i = 0; i < iconList.size(); i++) {
				KnobIcon icon = iconList.get(i);
				if (icon.containPoint(p)) {
					((CTree) tree).expandTree(i + 1);
					break;
				}
			}

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			paintKnob(g);
		}

		private synchronized void paintKnob(Graphics g) {
			for (int i = 0; i < iconList.size(); i++) {
				KnobIcon icon = iconList.get(i);
				Point p = icon.getLocal();
				iconList.get(i).paintIcon(this, g, p.x, p.y);
			}

		}

	}

	private class TreeCtrlIcon implements Icon {
		private boolean expand = true;

		public TreeCtrlIcon(boolean expand) {
			super();
			this.expand = expand;
		}

		@Override
		public int getIconHeight() {
			return 13;
		}

		@Override
		public int getIconWidth() {
			return 13;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color oldC = g.getColor();
			g.setColor(Color.black);
			g.translate(x, y);
			g.drawRect(0, 0, getIconWidth() - 1, getIconHeight() - 1);
			g.drawLine(2, getIconHeight() / 2, getIconWidth() - 3,
					getIconHeight() / 2);
			if (!expand) {
				g.drawLine(getIconWidth() / 2, 2, getIconWidth() / 2,
						getIconHeight() - 3);
			}
			g.translate(-x, -y);
			g.setColor(oldC);
		}

	}

	private class CTreeRender extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -4208728225258912643L;

		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			d.width = 0;
			return d;
		}

	}

	private class CTreeUI extends BasicTreeUI {

		/**
		 * 
		 * Expands path if it is not expanded, or collapses row if it is
		 * expanded.
		 * 
		 * If expanding a path and JTree scrolls on expand, ensureRowsAreVisible
		 * 
		 * is invoked to scroll as many of the children to visible as possible
		 * 
		 * (tries to scroll to last visible descendant of path).
		 */

		protected void toggleExpandState(TreePath path) {

			if (table.getCellEditor() != null) {

				table.getCellEditor().stopCellEditing();

			}

			super.toggleExpandState(path);

		}

		@Override
		public void paint(Graphics g, JComponent c) {
			lineNumHM.clear();
			index = 0;
			TreeNode root = (TreeNode) tree.getModel().getRoot();
			initLineNumHM(root);
			super.paint(g, c);
			g.setColor(table.getGridColor());
			if (table.getHeight() > 0) {
				g.drawLine(tree.getWidth() - 1, 0, tree.getWidth() - 1,
						table.getHeight() - 1);
				g.drawLine(0, table.getHeight() - 1, tree.getWidth() - 1,
						table.getHeight() - 1);
			}
		}

		Icon expandedIcon = new TreeCtrlIcon(true);

		Icon collapsedIcon = new TreeCtrlIcon(false);

		@Override
		protected void installDefaults() {
			super.installDefaults();
			setRightChildIndent(10);

		}

		@Override
		public Icon getExpandedIcon() {
			return expandedIcon;
		}

		@Override
		public Icon getCollapsedIcon() {
			return collapsedIcon;
		}

		protected void paintVerticalLine(Graphics g, JComponent c, int x,
				int top, int bottom) {
			Color oldC = g.getColor();
			g.setColor(new Color(122, 152, 182));
			super.paintVerticalLine(g, c, x - totalChildIndent, top - 4, bottom);
			g.setColor(oldC);
		}

		@Override
		protected void paintHorizontalLine(Graphics g, JComponent c, int y,
				int left, int right) {
			int with = c.getWidth();
			Color oldC = g.getColor();
			g.setColor(new Color(122, 152, 182));
			super.paintHorizontalLine(g, c, y, left - totalChildIndent, with);
			g.setColor(oldC);
		}

		@Override
		protected void paintHorizontalPartOfLeg(Graphics g,
				Rectangle clipBounds, Insets insets, Rectangle bounds,
				TreePath path, int row, boolean isExpanded,
				boolean hasBeenExpanded, boolean isLeaf) {
			boolean draw = true;
			TreePath pPath = path.getParentPath();
			if (pPath != null) {
				Object parent = pPath.getLastPathComponent();
				int index = treeModel.getIndexOfChild(parent,
						path.getLastPathComponent());
				if (index < treeModel.getChildCount(parent) - 1) {
					draw = false;
				}
			}
			// add by lisyd 2015 1.16 注释掉了下面这句话
			// if (draw)
			// add by wangfan3 2015 1.16
			if (path.getPathCount() > 2)
				super.paintHorizontalPartOfLeg(g, clipBounds, insets, bounds,
						path, row, isExpanded, hasBeenExpanded, isLeaf);
		}

		@Override
		public Dimension getPreferredSize(JComponent c) {
			Dimension dim = super.getPreferredSize(c);
			if (showLineNum) {
				int dw = calulateLineNumWidth();
				dim.width += dw;
			}
			return dim;
		}

		private HashMap<TreeNode, Integer> lineNumHM = new HashMap<TreeNode, Integer>();

		private transient int index = 0;

		private void initLineNumHM(TreeNode node) {
			lineNumHM.put(node, index);
			int count = node.getChildCount();
			for (int i = 0; i < count; i++) {
				TreeNode cNode = node.getChildAt(i);
				index++;
				initLineNumHM(cNode);
			}

		}

		private String convertRow(int row) {
			TreePath path = tree.getPathForRow(row);
			TreeNode node = (TreeNode) path.getLastPathComponent();
			int lineNum = lineNumHM.get(node);
			return lineNum + "";
		}

		@Override
		protected void paintRow(Graphics g, Rectangle clipBounds,
				Insets insets, Rectangle bounds, TreePath path, int row,
				boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
			super.paintRow(g, clipBounds, insets, bounds, path, row,
					isExpanded, hasBeenExpanded, isLeaf);
			if (showLineNum) {
				Dimension size = getPreferredSize(tree);
				FontMetrics fm = tree.getFontMetrics(tree.getFont());
				String text = convertRow(row);
				int textW = fm.stringWidth(text);
				int w = calulateLineNumWidth();
				Rectangle rect = new Rectangle(size.width - w, bounds.y, w,
						bounds.height);
				Color oldC = g.getColor();
				if (tree.isRowSelected(row)) {
					g.setColor(Color.yellow);
				} else {
					g.setColor(Color.white);
				}
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
				g.setColor(table.getGridColor());
				g.drawLine(rect.x, rect.y, rect.x, rect.y + rect.height - 1);
				// g.drawLine(rect.x+rect.width-1, rect.y, rect.x+rect.width-1,
				// rect.y+rect.height);
				int numx = rect.x + (rect.width - textW) - 2;
				int numy = rect.y + (rect.height - fm.getHeight()) / 2
						+ fm.getAscent();
				g.setColor(table.getForeground());
				g.drawString(text, numx, numy);
				g.setColor(oldC);
			}
		}
	}

	private class CTree extends JTree {
		private static final long serialVersionUID = 4686974324274452161L;

		public CTree() {
			super();
			setFocusable(false);
			setRequestFocusEnabled(false);
			setCellRenderer(new CTreeRender());
			setRootVisible(false);
			setShowsRootHandles(true);
			setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		}

		public TreeNode[] getPathToRoot(TreeNode aNode) {
			return getPathToRoot(aNode, 0);
		}

		protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
			TreeNode[] retNodes;
			if (aNode == null) {
				if (depth == 0)
					return null;
				else
					retNodes = new TreeNode[depth];
			} else {
				depth++;
				if (aNode == getModel().getRoot())
					retNodes = new TreeNode[depth];
				else
					retNodes = getPathToRoot(aNode.getParent(), depth);
				retNodes[retNodes.length - depth] = aNode;
			}
			return retNodes;
		}

		private boolean isExpand(int depth) {
			TreeModel model = getModel();
			TreeNode root = (TreeNode) model.getRoot();
			return isExpand(root, depth);
		}

		private boolean isExpand(TreeNode node, int depth) {
			if (depth == 1 && !node.isLeaf()) {
				TreePath path = new TreePath(getPathToRoot(node));
				return isExpanded(path);
			} else {
				int count = node.getChildCount();
				boolean b = true;
				for (int i = 0; i < count; i++) {
					TreeNode child = node.getChildAt(i);
					b = isExpand(child, depth - 1);
					if (!b) {
						break;
					}
				}
				return b;
			}

		}

		public void expandTree(int depth) {
			if (!isRootVisible()) {
				depth += 1;
			}
			boolean isExpand = isExpand(depth);
			TreeModel model = getModel();
			TreeNode root = (TreeNode) model.getRoot();
			expandTree(root, !isExpand, depth);
		}

		private void expandTree(TreeNode node, boolean expand, int depth) {
			if (depth == 0)
				return;
			TreePath path = new TreePath(getPathToRoot(node));
			if (expand) {
				expandPath(path);
			} else {
				if (depth == 1)
					collapsePath(path);
			}
			int count = node.getChildCount();
			for (int i = 0; i < count; i++) {
				TreeNode child = node.getChildAt(i);
				expandTree(child, expand, depth - 1);
			}
		}

		@Override
		public void updateUI() {
			setUI(new CTreeUI());
		}

	}

	private class SelectionModelAdapter extends DefaultTreeSelectionModel
			implements ListSelectionListener {
		private static final long serialVersionUID = 4103331087476323907L;

		protected boolean updatingListSelectionModel;

		public SelectionModelAdapter() {
			super();
			getListSelectionModel().addListSelectionListener(this);
		}

		public ListSelectionModel getListSelectionModel() {
			return super.listSelectionModel;
		}

		@Override
		public void resetRowSelection() {
			if (!updatingListSelectionModel) {
				try {
					if (table.getCellEditor() != null) {
						table.getCellEditor().stopCellEditing();
					}
					updatingListSelectionModel = true;
					super.resetRowSelection();
				} finally {
					updatingListSelectionModel = false;
				}
			}

		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!updatingListSelectionModel) {
				try {
					/**
					 * add by lisyd 2015 1.20 注释掉下面这段代码,引起的多语文本框选中后抛空指针
					 */
					// if (table.getCellEditor() != null) {
					// table.getCellEditor().stopCellEditing();
					// }
					updatingListSelectionModel = true;
					int min = listSelectionModel.getMinSelectionIndex();
					int max = listSelectionModel.getMaxSelectionIndex();
					// clearSelection();
					for (int i = min; i <= max; i++) {
						if (listSelectionModel.isSelectedIndex(i)) {
							TreePath path = tree.getPathForRow(i);
							if (path != null) {
								addSelectionPath(path);
							}
						}
					}
					tree.repaint();
				} finally {
					updatingListSelectionModel = false;
				}
			}
		}

	}

	// private class CTableUI extends BasicTableUI {
	//
	// @Override
	// public void paint(Graphics g, JComponent c) {
	// super.paint(g, c);
	// if(!showLineNum)
	// g.drawLine(0, 0, 0, table.getHeight());
	// }
	//
	// }

	private IBillTreeTableModel treeTableModel = null;

	private CTree tree = null;

	private QuickExpandTreePanel qetp = null;

	private Dimension knobSize = new Dimension(13, 13);

	private boolean showLineNum = true;

	private JTable table = null;

	private BillScrollPane sp = null;

	private Comparator<CircularlyAccessibleValueObject> comparator;

	private void initialize() {
		tree = new CTree();
		tree.setModel(treeTableModel);
		qetp = new QuickExpandTreePanel();
		MMGPBillModelTreeTableAdapter tma = new MMGPBillModelTreeTableAdapter(
				treeTableModel, tree, comparator);

		tma.setBodyItems(sp.getTableModel().getBodyItems());
		tma.setDataVector(sp.getTableModel().getDataVector());
		tma.setTabvo(sp.getTableModel().getTabvo());
		tma.setEnabled(sp.getTableModel().isEnabled());

		// table.setModel(tma);

		sp.setTableModel(tma);

		table = sp.getTable();

		tree.setRowHeight(sp.getTable().getRowHeight());
		tree.addTreeExpansionListener(tma);
		tree.getModel().addTreeModelListener(tma);
		tree.getModel().addTreeModelListener(new TreeModelListener() {
			@Override
			public void treeNodesChanged(TreeModelEvent e) {
				caluateTreeNodeCount();
			}

			@Override
			public void treeNodesInserted(TreeModelEvent e) {
				caluateTreeNodeCount();
			}

			@Override
			public void treeNodesRemoved(TreeModelEvent e) {
				caluateTreeNodeCount();
			}

			@Override
			public void treeStructureChanged(TreeModelEvent e) {
				caluateTreeNodeCount();
			}

		});
		caluateTreeNodeCount();
		SelectionModelAdapter selModel = new SelectionModelAdapter();
		tree.setSelectionModel(selModel);

		DefaultListSelectionModel slm = (DefaultListSelectionModel) table
				.getSelectionModel();

		ListSelectionListener[] ls = slm.getListSelectionListeners();

		for (int i = 0; i < ls.length; i++) {
			selModel.getListSelectionModel().addListSelectionListener(ls[i]);
		}

		sp.getTable().setSelectionModel(selModel.getListSelectionModel());

		// table.setUI(new CTableUI());

		// showLineNum = false;

		// ((BillTable)table).addTableNotify(new TableNotify());
		configureEnclosingScrollPaneRowHeader(sp);
	}

	private void configureEnclosingScrollPaneRowHeader(BillScrollPane gp) {
		BillScrollPane scrollPane = (BillScrollPane) gp;
		tree.setBackground(table.getBackground());
		qetp.setBackground(table.getTableHeader().getBackground());
		scrollPane.setRowNOView(tree);
		JViewport rowNOHeader = new JViewport();
		rowNOHeader.setView(qetp);
		scrollPane.setCorner(BillScrollPane.ROW_NO_HEADER, rowNOHeader);
	}

	@Override
	public JTree getTree() {
		return tree;
	}

	private int treeNodeCount = 0;

	private int caluateTreeNodeCount() {
		treeNodeCount = 0;
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		caluateTreeNodeCount0(root);
		return treeNodeCount;
	}

	private void caluateTreeNodeCount0(TreeNode node) {
		int count = node.getChildCount();
		for (int i = 0; i < count; i++) {
			TreeNode cNode = node.getChildAt(i);
			treeNodeCount++;
			caluateTreeNodeCount0(cNode);
		}
	}

	private int calulateLineNumWidth() {
		int count = treeNodeCount;
		FontMetrics fm = tree.getFontMetrics(tree.getFont());
		int dw = fm.stringWidth("" + count) + 4;
		return dw;
	}

}
