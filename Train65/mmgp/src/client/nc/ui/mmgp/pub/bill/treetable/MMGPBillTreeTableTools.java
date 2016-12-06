package nc.ui.mmgp.pub.bill.treetable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import nc.md.model.IBean;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.RowAttribute;
import nc.ui.pub.bill.treetable.BillTreeTableModel;
import nc.ui.pub.bill.treetable.BillTreeTableModelNode;
import nc.ui.pub.bill.treetable.IBillTreeCreateStrategy;
import nc.ui.pub.bill.treetable.IBillTreeTableModel;
import nc.ui.pub.bill.treetable.RowDataAndAttribute;
import nc.vo.bd.access.tree.ITreeCreateStrategy;
import nc.vo.bd.access.tree.TreeCreateException;
import nc.vo.jcom.tree.ICodeNodeProvider;
import nc.vo.jcom.tree.IPKNodeProvider;
import nc.vo.jcom.tree.TreeCreator;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����10:50:49
 * @author: tangxya
 */
public class MMGPBillTreeTableTools {

	public static DefaultMutableTreeNode createBillTableTreeNode(
			Vector<?> vector, RowAttribute att, int showitemcol) {

		RowDataAndAttribute record = creatRowDataAndAttribute(vector, att);

		return createBillTableTreeNode(record, showitemcol);
	}

	static DefaultMutableTreeNode createBillTableTreeNode(
			RowDataAndAttribute record, int showitemcol) {
		// Object obj = record.getRowData();
		// if(showitemcol > -1)
		// obj = record.getRowData().get(showitemcol);

		MMGPBillTreeTableModelNode node = new MMGPBillTreeTableModelNode(
				showitemcol);
		node.setData(record.getRowData());
		node.setRowAttribute(record.getAttribute());

		return node;
	}

	private static RowDataAndAttribute creatRowDataAndAttribute(
			Vector<?> vector, RowAttribute att) {

		return new RowDataAndAttribute(vector, att);
	}

	public static IBillTreeTableModel creatTreeModelByStrategy(
			BillModel billmodel, IBillTreeCreateStrategy createStrategy) {

		RowDataAndAttribute[] datas = getRowDataAndAttriutes(billmodel);

		int col = billmodel.getBodyColByKey(createStrategy.getItemKey());

		int parentcol = billmodel.getBodyColByKey(createStrategy
				.getParentItemKey());

		int showcol = billmodel
				.getBodyColByKey(createStrategy.getShowItemKey());

		MMGPBillTableTreeStrategy createTreeStrategy = new MMGPBillTableTreeStrategy(
				parentcol, col, showcol, createStrategy.getCodeRule());

		MMGPBillTreeTableModel treeModel = createTree(datas,
				createTreeStrategy, showcol);

		treeModel.setItems(billmodel.getBodyItems());

		return treeModel;

	}

	public static IBillTreeTableModel createTreeModelByDataTreeModel(
			BillModel billmodel, DefaultTreeModel dataTreeModel,
			String showColName) {

		RowDataAndAttribute[] datas = getRowDataAndAttriutes(billmodel);

		int showcol = billmodel.getBodyColByKey(showColName);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) dataTreeModel
				.getRoot();

		DefaultMutableTreeNode newroot = new BillTreeTableModelNode("root");

		int nodeIndex = 0;
		doCreateTree(root, newroot, datas, nodeIndex, showcol);

		BillTreeTableModel model = new BillTreeTableModel(newroot, showcol);

		model.setItems(billmodel.getBodyItems());

		return model;

	}

	private static int doCreateTree(DefaultMutableTreeNode root,
			DefaultMutableTreeNode newroot, RowDataAndAttribute[] datas,
			int nodeIndex, int showcol) {
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode newnode = createBillTableTreeNode(
					datas[nodeIndex++], showcol);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			nodeIndex = doCreateTree(node, newnode, datas, nodeIndex, showcol);
			newroot.add(newnode);
		}
		return nodeIndex;
	}

	private static RowDataAndAttribute[] getRowDataAndAttriutes(
			BillModel billmodel) {

		Vector dataVector = billmodel.getDataVector();

		RowAttribute[] attributes = new RowAttribute[dataVector.size()];
		for (int i = 0; i < dataVector.size(); i++) {
			attributes[i] = billmodel.getRowAttribute(i);
		}

		RowDataAndAttribute[] datas = new RowDataAndAttribute[dataVector.size()];
		for (int i = 0; i < dataVector.size(); i++) {
			datas[i] = creatRowDataAndAttribute((Vector) dataVector.get(i),
					attributes[i]);
		}
		return datas;
	}

	private static MMGPBillTreeTableModel createTree(Object[] userObjs,
			ITreeCreateStrategy strategy, int showcol) {
		try {
			DefaultMutableTreeNode root = strategy.getRootNode();
			if (strategy.isCodeTree())
				TreeCreator.createCodeTree(root, userObjs,
						new CodeNodeProviderAdapter(strategy),
						strategy.getInsertType(), strategy.getNodeFileter());
			else
				TreeCreator.createPKTree(root, userObjs,
						new PKNodeProviderAdapter(strategy),
						strategy.getInsertType(), strategy.getNodeFileter());
			return new MMGPBillTreeTableModel(root, showcol);
		} catch (Exception e) {
			throw new TreeCreateException("create tree failed. "
					+ e.getMessage(), e);
		}

	}

	private static class CodeNodeProviderAdapter implements ICodeNodeProvider {
		private ITreeCreateStrategy adaptee;

		public CodeNodeProviderAdapter(ITreeCreateStrategy adaptee) {
			super();
			if (!adaptee.isCodeTree())
				throw new IllegalArgumentException(
						"adaptee must be a code tree provider");
			this.adaptee = adaptee;
		}

		public DefaultMutableTreeNode createTreeNode(Object userObj) {
			return adaptee.createTreeNode(userObj);
		}

		public Object getCodeValue(Object userObj) {
			return adaptee.getCodeValue(userObj);
		}

		public String getCodeRule() {
			return adaptee.getCodeRule();
		}

		public String getCircularRule() {
			return null;
		}

		public DefaultMutableTreeNode createDefaultTreeNodeForLoseNode(
				Object codeValue) {
			return null;
		}

		public DefaultMutableTreeNode getOtherTreeNode() {
			return null;
		}

	}

	private static class PKNodeProviderAdapter implements IPKNodeProvider {
		private ITreeCreateStrategy adaptee;

		public PKNodeProviderAdapter(ITreeCreateStrategy adaptee) {
			super();
			if (adaptee.isCodeTree())
				throw new IllegalArgumentException(
						"adaptee must be a pk tree provider");
			this.adaptee = adaptee;
		}

		public DefaultMutableTreeNode createTreeNode(Object obj) {

			return adaptee.createTreeNode(obj);
		}

		public Object getNodeId(Object obj) {
			return adaptee.getNodeId(obj);
		}

		public Object getParentNodeId(Object obj) {
			return adaptee.getParentNodeId(obj);
		}

		public DefaultMutableTreeNode getOtherTreeNode() {
			return null;
		}

	}

	/**
	 * 
	 * @param node
	 *            �����ĸ��ڵ�
	 * @param pCode
	 *            ���ڵ��innercode
	 * @param treeModel
	 */
	@SuppressWarnings("unchecked")
	public static void setBodyInnercode(MutableTreeNode node, int inercodeIndex) {

		Enumeration<MutableTreeNode> enu = node.children();

		int sameLeveIndex = 0;// ͬһ���ӽڵ��С��������
		String pCode = (String) ((MMGPBillTreeTableModelNode) node)
				.getValueAt(inercodeIndex);
		while (enu.hasMoreElements()) {

			sameLeveIndex++;

			MMGPBillTreeTableModelNode treenode = (MMGPBillTreeTableModelNode) enu
					.nextElement();

			// ����innercode
			String randomCode = null;
			if (pCode == null) {
				randomCode = sameLeveIndex + "";
			} else {
				randomCode = pCode + "." + sameLeveIndex;
			}
			treenode.setValueAt(randomCode, inercodeIndex);

			// �ȱ���Ҷ�ڵ㣬�������
			if (!treenode.isLeaf()) {

				setBodyInnercode(treenode, inercodeIndex);
			}

		}
	}

	/**
	 * 
	 * @param billModel
	 * @param key
	 * @return innercode��Ԫ����������
	 * 
	 */
	public static int getInercodeIndex(BillModel billModel, String key) {
		return billModel.getBodyColByKey(key);
	}
	
	/**
	 * ֻ����TreeNode�����������������Ż������嵥������Ŀbom���գ���������������ʱ�����������������Դ�ﵽ80%��
	 * ���������������ͳһ�ڽڵ���м�����������,ע�⣺insertreeOnlyҲ���������кţ�������Ҫ�ֹ�����
	 * @param insertRoot
	 * @param parent
	 * @param index
	 * @param billModelTreeAdapter
	 * @throws BusinessException
	 */
	public static void insertTreeOnly(DefaultMutableTreeNode insertRoot,
			DefaultMutableTreeNode parent, int index,
			MMGPBillModelTreeTableAdapter billModelTreeAdapter) throws BusinessException{
		Stack<DefaultMutableTreeNode> stack = new Stack<DefaultMutableTreeNode>();
		stack.push(insertRoot);
		Map<String, MutableTreeNode> pk2TreeTableNodeMap = new HashMap<String, MutableTreeNode>();
		while (!stack.isEmpty()) {
			
			DefaultMutableTreeNode curnode = stack.pop();

			DefaultMutableTreeNode curparent = null;
			int curindex = -1;
			if (curnode.equals(insertRoot)) {//����ڵ��ǲ������ĸ��ڵ����
				curparent = parent;
				curindex = index;
			} else {
				CircularlyAccessibleValueObject curbodyvo = (CircularlyAccessibleValueObject) curnode.getUserObject();
				IBean bean = MMMetaUtils.getBeanByClassFullName(curbodyvo.getClass().getName());
				String pid = MMMetaUtils.getParentPKFieldName(bean);
				curparent = (DefaultMutableTreeNode) pk2TreeTableNodeMap.get(curbodyvo.getAttributeValue(pid));
				curindex = curparent.getChildCount();
			}
			MutableTreeNode newchild = billModelTreeAdapter.insertRowOnly(curparent, (CircularlyAccessibleValueObject) curnode.getUserObject(), curindex);
			String key = null;
			key = ((CircularlyAccessibleValueObject) curnode.getUserObject()).getPrimaryKey();
			pk2TreeTableNodeMap.put(key, newchild);

			int count = curnode.getChildCount();
			for (int i = count; i > 0; i--) {//�ӽڵ㵹����ջ����֤�������ջ����������
				stack.push((DefaultMutableTreeNode) curnode.getChildAt(i - 1));
			}
		}
	}
	
	/**
	 * ��ȡ���뵽insertTreeOnly
	 * @param: @param insertRoot �������ĸ��ڵ�
	 * @param: @param parent ���ڵ�
	 * @param: @param index ����λ
	 * @param: @param billModelTreeAdapter ����ģ��
	 * @param: @throws BusinessException
	 *
	 */
	public static void insertTree(DefaultMutableTreeNode insertRoot,
			DefaultMutableTreeNode parent, int index,
			MMGPBillModelTreeTableAdapter billModelTreeAdapter)
			throws BusinessException {
		insertTreeOnly(insertRoot, parent, index, billModelTreeAdapter);
		//�����кţ���ΪinserTreeOnlyҲ�������к�
		billModelTreeAdapter.resetLineNumHM();
		billModelTreeAdapter.loadLoadRelationItemValue();
	}
}
