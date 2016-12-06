package nc.ui.mmgp.pub.bill.treetable;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.bill.treetable.RowDataAndAttribute;
import nc.vo.bd.access.tree.ITreeCreateStrategy;
import nc.vo.jcom.tree.INodeFilter;

/**
 * @Description: TODO
 *               <p>
 *               œÍœ∏π¶ƒ‹√Ë ˆ
 *               </p>
 * @data:2014-5-15…œŒÁ10:54:24
 * @author: tangxya
 */
public class MMGPBillTableTreeStrategy implements ITreeCreateStrategy {

	private int parentCol = -1;
	private int col = -1;
	private int showcol = -1;

	private String codeRule = null;

	public MMGPBillTableTreeStrategy(int parentCol, int col, int showcol,
			String codeRule) {
		super();
		this.parentCol = parentCol;
		this.col = col;
		this.showcol = showcol;
		this.codeRule = codeRule;
	}

	public Object getParentNodeId(Object userObj) {

		RowDataAndAttribute record = (RowDataAndAttribute) userObj;
		return record.getValueAt(parentCol);
	}

	public Object getNodeId(Object userObj) {
		RowDataAndAttribute record = (RowDataAndAttribute) userObj;
		return record.getValueAt(col);
	}

	public DefaultMutableTreeNode getOtherTreeNode() {
		return null;
	}

	public Object getCodeValue(Object userObj) {
		RowDataAndAttribute record = (RowDataAndAttribute) userObj;
		return record.getValueAt(col);
	}

	public String getCodeRule() {
		return codeRule;
	}

	public String getCircularRule() {
		return null;
	}

	public DefaultMutableTreeNode createTreeNode(Object userObj) {

		RowDataAndAttribute record = (RowDataAndAttribute) userObj;

		DefaultMutableTreeNode node = MMGPBillTreeTableTools
				.createBillTableTreeNode(record, showcol);

		return node;

	}

	public DefaultMutableTreeNode createDefaultTreeNodeForLoseNode(
			Object codeValue) {
		return null;
	}

	public boolean isCodeTree() {
		if (getCodeRule() != null)
			return true;

		return false;
	}

	public DefaultMutableTreeNode getRootNode() {
		return new MMGPBillTreeTableModelNode("root");
	}

	public INodeFilter getNodeFileter() {
		return null;
	}

	public int getInsertType() {
		return 0;
	}

}
