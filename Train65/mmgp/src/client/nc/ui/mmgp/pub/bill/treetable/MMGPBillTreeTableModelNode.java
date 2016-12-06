package nc.ui.mmgp.pub.bill.treetable;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.bill.RowAttribute;
import nc.ui.pub.bill.treetable.BillTreeTableModelNode;
import nc.ui.pub.bill.treetable.IBillTreeTableNode;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����10:42:38
 * @author: tangxya
 */
public class MMGPBillTreeTableModelNode extends DefaultMutableTreeNode
		implements IBillTreeTableNode {

	private static final long serialVersionUID = 4235228933020018901L;
	// ����Vector
	private Vector<Object> data;

	private RowAttribute rowAttribute;

	private int showitemcol = -1;

	// // �ڵ��ʶ
	// private Object m_nodeID;
	// // ���ڵ��ʶ
	// private Object m_parentnodeID;
	// // ����
	// private String m_code;

	public MMGPBillTreeTableModelNode(Object userObject) {
		super(userObject);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-11 22:49:42)
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
	 * ȡ�ýڵ��е����ݣ�Vector���� �������ڣ�(2003-9-11 22:50:36)
	 * 
	 * @return java.util.Vector
	 */
	public Vector<Object> getData() {
		return data;
	}

	/**
	 * �������ݡ� �������ڣ�(2003-9-11 22:50:36)
	 * 
	 * @param newData
	 *            java.util.Vector
	 */
	public void setData(Vector newData) {
		data = newData;
	}

	/**
	 * �õ�ĳ�е�ֵ�� �������ڣ�(2003-9-18 11:09:21)
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
	 * ����ĳ�е�ֵ�� �������ڣ�(2003-9-18 11:16:24)
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
	 * ���������԰�beInsertNode�ڵ㸴�Ƶ�target�ڵ��У�
	 * ʹbeInsertNode��Ϊtarget���ӽڵ㣬���withChildrenΪ�棬 �ݹ鸴��beInsertNode�ӽڵ㡣
	 * �������ڣ�(2002-5-28 15:55:29)
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
