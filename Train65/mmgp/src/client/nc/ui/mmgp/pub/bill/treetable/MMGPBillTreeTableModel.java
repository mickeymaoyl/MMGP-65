package nc.ui.mmgp.pub.bill.treetable;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import nc.ui.pub.bill.RowAttribute;
import nc.ui.pub.bill.treetable.BillTreeTableModel;

/**
 * @Description: TODO
 *               <p>
 *               œÍœ∏π¶ƒ‹√Ë ˆ
 *               </p>
 * @data:2014-5-15…œŒÁ10:48:40
 * @author: tangxya
 */
public class MMGPBillTreeTableModel extends BillTreeTableModel {

	public MMGPBillTreeTableModel(TreeNode root, int showcol) {
		super(root, showcol);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DefaultMutableTreeNode createBillTableTreeNode(Vector<?> vector,
			RowAttribute att) {

		return MMGPBillTreeTableTools.createBillTableTreeNode(vector, att,
				super.getShowcol());
	}
}
