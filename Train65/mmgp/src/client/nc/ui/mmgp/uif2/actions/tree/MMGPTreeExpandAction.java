package nc.ui.mmgp.uif2.actions.tree;

import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.ui.uif2.NCAction;
import nc.ui.uif2.components.TreePanel;

/**
 * @Description:树节点展开
 * @author: tangxya
 * @data:2014-5-9上午9:34:30
 */
@SuppressWarnings("serial")
public class MMGPTreeExpandAction extends NCAction {
	  // 树模型
    private TreePanel treePanel;

    public static final String EXPAND = "Expand";

    public MMGPTreeExpandAction() {
            setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0015")/*@res "展开"*/);
            setCode(EXPAND);
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {


        JTree tree = this.treePanel.getTree();
        int selectCount = tree.getSelectionCount();

        if (selectCount == 0) {
            for (int i = 0; i < tree.getRowCount(); i++)
                tree.expandRow(i);
        } else {
            int[] rows = tree.getSelectionRows();
            for (int i = 0; i < rows.length; i++) {
                tree.expandRow(rows[i]);
                doNext(tree, tree.getPathForRow(rows[i]), true);
            }
        }

    }

    /**
     * 展开下一层
     * @param tree
     * @param path
     * @param expand
     */
    private void doNext(JTree tree,TreePath path, boolean expand) {
        if (path == null) {
            return;
        }

        TreeNode node = (TreeNode) path.getLastPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            TreePath childpath = path.pathByAddingChild(node.getChildAt(i));
            if (expand)
                tree.expandPath(childpath);
            doNext(tree, childpath, expand);
            if (!expand)
                tree.collapsePath(childpath);
        }
    }


    public TreePanel getTreePanel() {
        return treePanel;
    }

    public void setTreePanel(TreePanel treePanel) {
        this.treePanel = treePanel;
    }


}