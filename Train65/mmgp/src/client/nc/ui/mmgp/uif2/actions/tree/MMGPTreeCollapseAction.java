package nc.ui.mmgp.uif2.actions.tree;

import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.ui.uif2.NCAction;
import nc.ui.uif2.components.TreePanel;

/**
 *
 * @Description:树节点收起功能
 * @author: tangxya
 * @data:2014-5-9上午9:37:58
 */
@SuppressWarnings("serial")
public class MMGPTreeCollapseAction extends NCAction{
	  // 树模型
    private TreePanel treePanel;

    public static final String COLLAPSE = "Collapse";

    public MMGPTreeCollapseAction() {
        setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0014")/*@res "收起"*/);
        setCode(COLLAPSE);
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        JTree tree = this.treePanel.getTree();

        if (tree.getSelectionCount() == 0) {
            for (int i = tree.getRowCount() - 1; i >= 0; i--)
                tree.collapseRow(i);
        } else {
            int[] rows = tree.getSelectionRows();
            for (int i = 0; i < rows.length; i++) {
                doNext(tree, tree.getPathForRow(rows[i]), false);
                tree.collapseRow(rows[i]);
            }
        }

    }

    /**
     * 收起下一层
     * @param tree
     * @param path
     * @param expand
     */
    private void doNext(JTree tree,
                        TreePath path,
                        boolean expand) {
        if (path == null) {
            return;
        }
        TreeNode node = (TreeNode) path.getLastPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            TreePath childpath = path.pathByAddingChild(node.getChildAt(i));
            if (expand) tree.expandPath(childpath);
            doNext(tree, childpath, expand);
            if (!expand) tree.collapsePath(childpath);
        }
    }

    public TreePanel getTreePanel() {
        return treePanel;
    }

    public void setTreePanel(TreePanel treePanel) {
        this.treePanel = treePanel;
    }

}