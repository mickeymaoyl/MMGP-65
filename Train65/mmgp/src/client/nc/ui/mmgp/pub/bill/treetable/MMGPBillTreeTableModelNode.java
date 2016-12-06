package nc.ui.mmgp.pub.bill.treetable;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.bill.RowAttribute;
import nc.ui.pub.bill.treetable.BillTreeTableModelNode;
import nc.ui.pub.bill.treetable.IBillTreeTableNode;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午10:42:38
 * @author: tangxya
 */
public class MMGPBillTreeTableModelNode extends DefaultMutableTreeNode
		implements IBillTreeTableNode {

	private static final long serialVersionUID = 4235228933020018901L;
	// 数据Vector
	private Vector<Object> data;

	private RowAttribute rowAttribute;

	private int showitemcol = -1;

	// // 节点标识
	// private Object m_nodeID;
	// // 父节点标识
	// private Object m_parentnodeID;
	// // 编码
	// private String m_code;

	public MMGPBillTreeTableModelNode(Object userObject) {
		super(userObject);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-11 22:49:42)
	 * 
	 * @param userObject
	 *            java.lang.Object
	 */
	public MMGPBillTreeTableModelNode(int showitemcol) {
		super();
		this.showitemcol = showitemcol;
	}

	public String toString() {

		Object o = "";

		if (showitemcol > -1
		// &&(data.size()>showitemcol-1)
		)
			if (data.get(showitemcol) != null)
				o = data.get(showitemcol).toString();

		return o.toString();
	}

	/**
	 * 取得节点中的数据（Vector）。 创建日期：(2003-9-11 22:50:36)
	 * 
	 * @return java.util.Vector
	 */
	public Vector<Object> getData() {
		return data;
	}

	/**
	 * 设置数据。 创建日期：(2003-9-11 22:50:36)
	 * 
	 * @param newData
	 *            java.util.Vector
	 */
	public void setData(Vector newData) {
		data = newData;
	}

	/**
	 * 得到某列的值。 创建日期：(2003-9-18 11:09:21)
	 * 
	 * @return java.lang.Object
	 * @param column
	 *            int
	 */
	public Object getValueAt(int column) {
		if (data == null) {
			return null;
		}
		return data.elementAt(column);
	}

	/**
	 * 设置某列的值。 创建日期：(2003-9-18 11:16:24)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param column
	 *            int
	 */
	public void setValueAt(Object value, int column) {
		if (data != null) {
			data.setElementAt(value, column);
		}
	}

	public void setRowAttribute(RowAttribute rowAttribute) {
		this.rowAttribute = rowAttribute;
	}

	public RowAttribute getRowAttribute() {
		return rowAttribute;
	}

	/**
	 * 本函数可以把beInsertNode节点复制到target节点中，
	 * 使beInsertNode成为target的子节点，如果withChildren为真， 递归复制beInsertNode子节点。
	 * 创建日期：(2002-5-28 15:55:29)
	 * 
	 * @return nc.ui.pub.beans.tree.ExTreeNode
	 */
	public BillTreeTableModelNode getCloneIncludeChildren() {
		BillTreeTableModelNode targetNode = (BillTreeTableModelNode) this
				.clone();
		for (int i = 0; i < this.getChildCount(); i++) {
			BillTreeTableModelNode sourceNodeChild = (BillTreeTableModelNode) this
					.getChildAt(i);
			BillTreeTableModelNode targetNodeChild = sourceNodeChild
					.getCloneIncludeChildren();
			targetNode.add(targetNodeChild);
		}
		return targetNode;
	}

}
