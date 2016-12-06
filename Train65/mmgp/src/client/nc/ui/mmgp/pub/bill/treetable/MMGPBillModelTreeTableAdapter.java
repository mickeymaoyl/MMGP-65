package nc.ui.mmgp.pub.bill.treetable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.md.data.access.DASFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.RowAttribute;
import nc.ui.pub.bill.treetable.IBillTreeTableModel;
import nc.ui.pub.bill.treetable.IBillTreeTableNode;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����10:20:56
 * @author: tangxya
 */
public class MMGPBillModelTreeTableAdapter extends BillModel implements TreeExpansionListener, TreeModelListener {

    protected ListableBeanFactory factory;

    private IBillTreeTableModel treeTableModel = null;

    private Comparator<CircularlyAccessibleValueObject> comparator;

    public IBillTreeTableModel getTreeTableModel() {
        return treeTableModel;
    }

    protected JTree tree;

    public MMGPBillModelTreeTableAdapter(IBillTreeTableModel treeTableModel,
                                         JTree tree,
                                         Comparator<CircularlyAccessibleValueObject> comparator) {
        super();
        this.treeTableModel = treeTableModel;
        this.tree = tree;
        this.comparator = comparator;

        resetLineNumHM();

        setBodyItems(treeTableModel.getItems());

    }

    private HashMap<TreeNode, Integer> lineNumHM = new HashMap<TreeNode, Integer>();

    private transient int index = 0;

    public void resetLineNumHM() {
        lineNumHM.clear();
        index = 0;
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        initLineNumHM(root);
        getRowNOTableModel().setNumRows(getRowCount());
    }

    private void initLineNumHM(TreeNode node) {
        lineNumHM.put(node, index);
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            TreeNode cNode = node.getChildAt(i);
            index++;
            initLineNumHM(cNode);
        }

    }

    protected String getRowNO(int row) {
        TreePath path = tree.getPathForRow(row);
        if (path != null) {
            TreeNode node = (TreeNode) path.getLastPathComponent();
            int lineNum = lineNumHM.get(node);
            return lineNum + "";
        }

        return "E";
    }

    @Override
    public int getRowCount() {
        if (tree == null) return 0;
        return tree.getRowCount();
    }

    private Object getNode(int row) {
        TreePath path = tree.getPathForRow(row);
        if (path != null) {
            return path.getLastPathComponent();
        }
        return null;
    }

    /**
     * @Description:���ض�Ӧ����λ��
     * @param: @param node ���ڵ�
     * @return:int ���ڵ�Ӹ��ڵ���ȱ�����ȫ·��������ĵڼ��У�
     */
    public int getRow(MMGPBillTreeTableModelNode node) {
        int row = this.tree.getRowForPath(new TreePath(node.getPath()));
        return row;
    }

    @Override
    public Object getValueAt(int rowIndex,
                             int columnIndex) {
        Object node = getNode(rowIndex);
        if (node == null) return null;
        else
            return treeTableModel.getValueAt(node, rowIndex, columnIndex);

    }

    /**
     * ��ʼ�����Ԫ��. ��������:(01-2-21 10:08:48)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void setDataVector(Vector newData) {
        super.setDataVector(newData);
        getRowNOTableModel().setNumRows(getRowCount());
    }

    @Override
    public void setValueAt(Object value,
                           int rowIndex,
                           int columnIndex) {
        Object node = getNode(rowIndex);
        if (node != null) {
            Object aValue = value;

            BillItem item = m_biBodyItems[columnIndex];
            int datatype = item.getDataType();

            // �Ƚ���Ϊ�յ��ж�
            if (aValue != null) {

                // �޸�С������
                if ((item.getDataType() == IBillItem.DECIMAL || item.getDataType() == IBillItem.MONEY)) {
                    // -----Ϊ���þ������ӵĴ��룬����С�����������¼���------------------
                    setBillItemDecimalByNode(item, (IBillTreeTableNode) node);
                }

                aValue = item.getConverter().convertToBillItem(datatype, aValue);

            }
            treeTableModel.setValueAt(aValue, node, rowIndex, columnIndex);

            fireTableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex));

        }
        // if(treeTableModel.getShowcol() > -1){
        // TreePath path = tree.getPathForRow(rowIndex);
        // if (path != null) {
        // tree.getModel().valueForPathChanged(path, getValueAt(rowIndex,
        // treeTableModel.getShowcol()));
        // }
        // }
    }

    // //////////////////////////////
    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
        TreePath path = event.getPath();
        int row = tree.getRowForPath(path);
        if (row < tree.getRowCount()) {
            fireTableRowsDeleted(row + 1, row + 1);
            getRowNOTableModel().setNumRows(tree.getRowCount());
        }

    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath path = event.getPath();
        int row = tree.getRowForPath(path);
        if (row < tree.getRowCount()) {
            fireTableRowsInserted(row + 1, row + 1);
            getRowNOTableModel().setNumRows(tree.getRowCount());
        }

    }

    // ///////////////////////
    private void fireTreeTableDataChange() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        };
        SwingUtilities.invokeLater(run);
    }

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                fireTableRowsUpdated(0, tree.getRowCount() - 1);
            }
        };
        SwingUtilities.invokeLater(run);
    }

    @Override
    public void treeNodesInserted(final TreeModelEvent e) {
        // Runnable run = new Runnable() {
        // @Override
        // public void run() {
        // TreePath path = e.getTreePath();
        // int [] cis = e.getChildIndices();
        // int max = 0;
        // int min = Integer.MAX_VALUE;
        // int count = cis == null ? 0 : cis.length;
        // for (int i = 0; i < count; i++) {
        // if(max < cis[i]+1){
        // max = cis[i]+1;
        // }
        // if(min > cis[i]+1){
        // min = cis[i]+1;
        // }
        // }
        // int row = tree.getRowForPath(path);
        // fireTableRowsInserted(row+min, row+max);
        // }
        // };
        // SwingUtilities.invokeLater(run);
        //
    }

    @Override
    public void treeNodesRemoved(final TreeModelEvent e) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                TreePath path = e.getTreePath();
                int[] cis = e.getChildIndices();
                int max = 0;
                int min = Integer.MAX_VALUE;
                int count = cis == null ? 0 : cis.length;
                for (int i = 0; i < count; i++) {
                    if (max < cis[i] + 1) {
                        max = cis[i] + 1;
                    }
                    if (min > cis[i] + 1) {
                        min = cis[i] + 1;
                    }
                }
                int row = tree.getRowForPath(path);
                fireTableRowsDeleted(row + min, row + max);
            }
        };
        SwingUtilities.invokeLater(run);

    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
        fireTreeTableDataChange();

    }

    @Override
    public RowAttribute getRowAttribute(int row) {

        Object node = getNode(row);
        return treeTableModel.getRowAttribute(node);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addRow(Vector rowData) {
        insertRow(getDataVector().size(), rowData);
        // dataVector.add(rowData);
    }

    @Override
    public void addLine(int selectrow) {

        MutableTreeNode parent = getTreeNode(selectrow);

        addLine(parent);

        // resetLineNumHM();
    }

    public MutableTreeNode addLine(MutableTreeNode parent) {

        if (m_biBodyItems == null) return null;

        if (parent == null) parent = getRoot();

        // ����������
        // int lastRow = dataVector.size() - 1;
        int inertrow = this.getinsertRow(parent, parent.getChildCount());
        Vector<Object> vNewRow = createRowVector(inertrow);
        RowAttribute ra = createRowAttribute();
        vRowAttrib.add(ra);

        // ������з�������dataVector�������һ�У��������� ��һ�����ڱ�������һ����
        // addRow(vNewRow);
        insertRow(inertrow, vNewRow);

        MutableTreeNode newChild = treeTableModel.createBillTableTreeNode(vNewRow, ra);

        insertNodeIntoParent(parent, newChild, -1);

        resetLineNumHM();

        return newChild;

    }

    public MutableTreeNode addLine(MutableTreeNode parent,
                                   CircularlyAccessibleValueObject bodyRowVO) {

        MutableTreeNode newChild = addLine(parent);

        if (newChild != null) {
            // setNodeDataByRowVO((IBillTreeTableNode)newChild,bodyRowVO);
            TreePath path = new TreePath(((DefaultTreeModel) treeTableModel).getPathToRoot(newChild));
            int row = tree.getRowForPath(path);

            setBodyRowVO(bodyRowVO, row);

            execLoadFormulaByRow(row);
        }

        return newChild;

    }

    public MutableTreeNode addLineByMetaData(MutableTreeNode parent,
                                             Object o) {

        MutableTreeNode newChild = addLine(parent);

        setNodeDataByMetaData((IBillTreeTableNode) newChild, o);

        return newChild;

    }

    public void setNodeDataByMetaData(IBillTreeTableNode node,
                                      Object o) {

        if (o == null || node == null) return;

        IBusinessEntity be = getTabvo().getBillMetaDataBusinessEntity();

        NCObject ncobject = DASFacade.newInstanceWithContainedObject(be, o);

        BillItem[] items = getBodyItems();

        Object aValue;

        for (int col = 0; col < items.length; col++) {
            BillItem item = items[col];

            if (item.getMetaDataProperty() != null && item.getIDColName() == null) {
                aValue = ncobject.getAttributeValue(item.getMetaDataProperty().getAttribute());

                setValueAt(aValue, node, col);
            }
        }

        loadLoadRelationItemValue(node);

        fireTableChanged(new TableModelEvent(this));
    }

    private void loadLoadRelationItemValue(IBillTreeTableNode node) {
        if (getRowCount() < 0) return;

        for (int col = 0; col < getBodyItems().length; col++) {
            BillItem item = getBodyItems()[col];

            if (item.getDataType() == IBillItem.UFREF && item.getMetaDataProperty() != null) {

                ArrayList<IConstEnum> relationitem = getMetaDataRelationItems(item, false);

                if (relationitem != null) {

                    String id = null;
                    Object obj = node.getValueAt(col);
                    if (obj != null && obj instanceof IConstEnum) id = (String) ((IConstEnum) obj).getValue();

                    IConstEnum[] o =
                            item.getGetBillRelationItemValue().getRelationItemValue(relationitem, new String[]{id });

                    if (o != null) {
                        for (int i = 0; i < o.length; i++) {
                            if (o[i].getValue() != null) {
                                Object[] v = (Object[]) o[i].getValue();
                                int icol = getBodyColByKey(o[i].getName());
                                setValueAt(v[0], node, icol);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param: @param parent ���ڵ�
     * @param: @param node �ӽڵ�
     * @param: @param bodyRowVO ����VO
     */
    public void setNodeDataByRowVO(IBillTreeTableNode parent,
                                   IBillTreeTableNode node,
                                   CircularlyAccessibleValueObject bodyRowVO) {
        if (bodyRowVO == null) return;
        Object aValue;
        BillItem item;
        String key;
        IBean bean = MMMetaUtils.getBeanByClassFullName(bodyRowVO.getClass().getName());
        String pid = MMMetaUtils.getParentPKFieldName(bean);
        String valueName = MMMetaUtils.getNameFieldName(bean);
        valueName = pid + "." + valueName;
        BillItem[] items = getBodyItems();
        for (int i = 0; i < items.length; i++) {
            item = items[i];
            key = item.getKey();
            if (key.equals(pid)) {
                aValue = parent.toString();
            }
//            else if(key.equals(valueName)){
//                aValue = parent.getValueAt(this.getBodyColByKey(key));
//            }
            else {
                aValue = bodyRowVO.getAttributeValue(key);
            }
            switch (item.getDataType()) {
                case IBillItem.DECIMAL:
                case IBillItem.MONEY:
                    setBillItemDecimalByVO(item, -1, bodyRowVO);
                    break;
                case IBillItem.UFREF:
                    if (aValue == null || aValue.toString().length() == 0) aValue = null;

                    Object o = node.getValueAt(i);
                    if (o != null && o instanceof IConstEnum) if (aValue != null) aValue =
                            new DefaultConstEnum(((IConstEnum) o).getValue(), aValue.toString());
                    else
                        aValue = new DefaultConstEnum(((IConstEnum) o).getValue(), null);
                    else if (aValue != null) aValue = new DefaultConstEnum(aValue, aValue.toString());
                    break;
                case IBillItem.MULTILANGTEXT:
                    if(key.equals(valueName)){
                        break;
                    }
                    String name = (String) bodyRowVO.getAttributeValue(key);
                    String name2 = (String) bodyRowVO.getAttributeValue(key + "2");
                    String name3 = (String) bodyRowVO.getAttributeValue(key + "3");
                    String name4 = (String) bodyRowVO.getAttributeValue(key + "4");
                    String name5 = (String) bodyRowVO.getAttributeValue(key + "5");
                    String name6 = (String) bodyRowVO.getAttributeValue(key + "6");

                    if (name != null
                        || name2 != null
                        || name3 != null
                        || name4 != null
                        || name5 != null
                        || name6 != null) {
                        MultiLangText mlt = new MultiLangText();

                        mlt.setText(name);
                        mlt.setText2(name2);
                        mlt.setText3(name3);
                        mlt.setText4(name4);
                        mlt.setText5(name5);
                        mlt.setText6(name6);

                        aValue = mlt;
                    }
                default: 
                    break;
            }

            // -------------
            setValueAt(aValue, node, i);
            setItemRelationValueAt(aValue, node, item);
        }

        DefaultTreeModel treeModel = (DefaultTreeModel) treeTableModel;
        TreePath path = new TreePath(treeModel.getPathToRoot((TreeNode) node));
        TreeModelEvent e = new TreeModelEvent(tree, path);
        treeNodesChanged(e);
    }

    private void setValueAt(Object value,
                            IBillTreeTableNode node,
                            int columnIndex) {
        if (node != null) {
            Object aValue = value;

            BillItem item = m_biBodyItems[columnIndex];
            int datatype = item.getDataType();

            // �Ƚ���Ϊ�յ��ж�
            if (aValue != null) {

                // �޸�С������
                if ((item.getDataType() == IBillItem.DECIMAL || item.getDataType() == IBillItem.MONEY)) {
                    // -----Ϊ���þ������ӵĴ��룬����С�����������¼���------------------
                    setBillItemDecimalByNode(item, node);
                }

                aValue = item.getConverter().convertToBillItem(datatype, aValue);

            }
            node.setValueAt(aValue, columnIndex);

            if (treeTableModel.getShowcol() == columnIndex) {
                TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());

                if (path != null) {
                    tree.getModel().valueForPathChanged(path, node.getValueAt(treeTableModel.getShowcol()));
                }
            }
        }
    }

    // ͬ�����¹�����ID��
    private void setItemRelationValueAt(Object aValue,
                                        IBillTreeTableNode node,
                                        BillItem item) {

        if (aValue == null || item.getRelationItem() == null) return;

        for (int j = 0; j < item.getRelationItem().size(); j++) {
            BillItem ritem = item.getRelationItem().get(j);

            if (ritem.getDataType() == IBillItem.UFREF) {
                int column = getBodyColByKey(item.getRelationItem().get(j).getKey());

                if (column >= 0) {
                    Object o = node.getValueAt(column);

                    if (aValue != null) {
                        if (aValue instanceof IConstEnum) aValue = ((IConstEnum) aValue).getValue();

                        if (aValue != null && aValue.toString().length() > 0) {
                            if (o != null && o instanceof IConstEnum) aValue =
                                    new DefaultConstEnum(aValue, ((IConstEnum) o).getName());
                            else
                                aValue = new DefaultConstEnum(aValue, (String) aValue);
                        } else {
                            aValue = null;
                        }
                    } else
                        aValue = null;

                    setValueAt(aValue, node, column);
                }
            }
        }
    }

    private void setBillItemDecimalByNode(BillItem item,
                                          IBillTreeTableNode node) {
        if (item.getDecimalListener() != null) {
            int colIndex = getBodyColByKey(item.getDecimalListener().getSource());
            Object id = node.getValueAt(colIndex);
            if (id != null && id instanceof IConstEnum) id = ((IConstEnum) id).getValue();
            if (id != null) item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(-1, id));
        }
    }

    /**
     * ���ö�Ӧ�е�Item�ľ���
     */
    private void setBillItemDecimalByVO(BillItem item,
                                        int row,
                                        CircularlyAccessibleValueObject bodyRowVO) {
        if (item.getDecimalListener() != null) {
            String source = item.getDecimalListener().getSource();
            Object pkValue = bodyRowVO.getAttributeValue(source);
            if (pkValue != null) item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(row, pkValue));
        }
    }

    /**
     * ������ ��������:(01-2-28 11:16:53)
     */
    public void insertRow(int row) {
        insertRowModel(row);
    }

    /**
     * ������ ��������:(01-2-28 11:16:53)
     */
    private void insertRowModel(int row) {
        if (row < 0) return;
        Vector<Object> vNewRow = createRowVector(row);

        // ������з�������dataVector�������һ�У��������� ��һ�����ڱ�������һ����
        // addRow(vNewRow);
        insertRow(row, vNewRow);
        // ������״̬
        RowAttribute ra = createRowAttribute();
        vRowAttrib.add(ra);

        MutableTreeNode child = treeTableModel.createBillTableTreeNode(vNewRow, ra);

        MutableTreeNode currchild = getTreeNode(row);
        MutableTreeNode parent = (MutableTreeNode) currchild.getParent();

        insertNodeIntoParent(parent, child, parent.getIndex(currchild));

        resetLineNumHM();
        // fireTableChanged(new TableModelEvent(this));
    }

    /**
     * ɾ������.
     */
    @SuppressWarnings("unchecked")
    public void delLine(int[] array) {

        if (array == null || array.length <= 0) return;

        // �ȶ�Ҫɾ�����кŽ�������,��ȷ���Ӻ���ǰɾ����
        int[] row = (int[]) array.clone();
        for (int i = 0; i < row.length; i++) {
            MMGPBillTreeTableModelNode node = getTreeNode(row[row.length - 1 - i]);

            delLine(node);
        }

    }

    public void delLine(MMGPBillTreeTableModelNode node) {

        if (vDeleteRow == null) vDeleteRow = new Vector<Vector< ? >>();

        delTableLine(node);

        removeNodeFromParent(node);

        // ����ϼ�
        reCalcurateAll();

        resetLineNumHM();
    }

    private void delTableLine(MMGPBillTreeTableModelNode node) {

        Vector<Object> vRow = node.getData();

        dataVector.remove(vRow);
        vRowAttrib.remove(node.getRowAttribute());

        if (node.getRowAttribute().getRowState() == NORMAL || node.getRowAttribute().getRowState() == MODIFICATION)
            vDeleteRow.add(vRow);

        for (int i = node.getChildCount(); i > 0; i--) {
            MMGPBillTreeTableModelNode child = (MMGPBillTreeTableModelNode) node.getChildAt(i - 1);
            delLine(child);
        }

    }

    private void removeNodeFromParent(MutableTreeNode node) {
        TreeModel model = treeTableModel;
        if (model instanceof DefaultTreeModel) {
            DefaultTreeModel treeModel = (DefaultTreeModel) model;
            treeModel.removeNodeFromParent(node);
        }
    }

    /**
     * ��ȡinserRowOnly����Ӱ��ԭ���߼�
     * @param: @param parent ���ڵ�
     * @param: @param bodyRowVO ����VO
     * @param: @param index ���ڵ�Ĳ���λ
     * @return:MutableTreeNode ����������
     */
	public MutableTreeNode insertRow(MutableTreeNode parent, CircularlyAccessibleValueObject bodyRowVO, int index) {
		MutableTreeNode node = this.insertRowOnly(parent, bodyRowVO, index);
		resetLineNumHM();
		return node;
	}
	
	/**
	 * �Ż���InsertRow��ֻ���в��룬�����кŴ�������������������ֹ������кŴ������ܴ������11%
	 * TODO���������ռ��ΪinsertNodeIntoParent����ź�ʱռ��60%-70%��û���뵽�ر�õķ�ʽ�Ż����������
	 * @author gaotx
	 * @param parent
	 * @param bodyRowVO
	 * @param index
	 * @return
	 */
	public MutableTreeNode insertRowOnly(MutableTreeNode parent, CircularlyAccessibleValueObject bodyRowVO, int index) {
		Vector<Object> vNewRow = createRowVector(-1);
		int inertrow = getinsertRow(parent, index);
		insertRow(inertrow, vNewRow);
		// ������״̬
		RowAttribute ra = createRowAttribute();
		vRowAttrib.add(ra);
		MutableTreeNode newChild = treeTableModel.createBillTableTreeNode(vNewRow, ra);
		setNodeDataByRowVO((IBillTreeTableNode) parent, (IBillTreeTableNode) newChild, bodyRowVO);
		insertNodeIntoParent(parent, newChild, index);
		return newChild;
	}

    /**
     * @Description:TODO
     * @param: @param parent ���ڵ�
     * @param: @param index ���ڵ����λ
     * @return:int ��ṹ����λ
     */
    private int getinsertRow(MutableTreeNode parent,
                             int index) {
        int inertrow = 0;
        MMGPBillTreeTableModelNode prenode = null;
        if (index == 0) prenode = (MMGPBillTreeTableModelNode) parent;
        else {
            prenode = (MMGPBillTreeTableModelNode) ((MMGPBillTreeTableModelNode) parent.getChildAt(index - 1));
            while (!prenode.isLeaf()) {
                prenode = (MMGPBillTreeTableModelNode) prenode.getLastLeaf();
            }
        }
        //inertrow = this.getRow(prenode);
        inertrow = this.dataVector.indexOf(prenode.getData());
        return inertrow + 1;
    }
    public int getDataRow(MutableTreeNode node){
        
        return this.dataVector.indexOf(((MMGPBillTreeTableModelNode)node).getData());
    }

    public void addRow(Vector rowData,
                       int index) {
        insertRow(getDataVector().size(), rowData);
    }

    @SuppressWarnings("rawtypes")
    public void insertNodeIntoParent(MutableTreeNode parent,
                                     MutableTreeNode newChild,
                                     int index) {
        if (index == -1) index = parent.getChildCount();

        TreeModel model = treeTableModel;
        if (model instanceof DefaultTreeModel) {
            DefaultTreeModel treeModel = (DefaultTreeModel) model;
            Vector v = (Vector) this.getDataVector().get(index);

            // if( v.size()<((BillTreeTableModel)treeModel).getShowcol()+1){
            // BillTreeTableModelNode node= (BillTreeTableModelNode) newChild;
            // node.gets
            // treeModel.insertNodeInto(newChild, parent, index);
            // }
            treeModel.insertNodeInto(newChild, parent, index);
            TreePath path = new TreePath(treeModel.getPathToRoot(parent));
            // TreePath selectpath = tree.getSelectionPath();
            // ����ڵ��Ѿ�չ����expandPath���ᷢ���¼�֪ͨ��������Ҫ���ⷢ�¼�
            if (tree.isExpanded(path)) {
                int row = tree.getRowForPath(new TreePath(treeModel.getPathToRoot(newChild)));
                fireTableRowsInserted(row, row);
                // getRowNOTableModel().setNumRows(tree.getRowCount());
            } else {
                tree.expandPath(path);
            }

            // tree.setSelectionPath(selectpath);

        }
    }

    public final MMGPBillTreeTableModelNode getTreeNode(int row) {

        MMGPBillTreeTableModelNode node = null;
        if (row < 0) node = (MMGPBillTreeTableModelNode) getRoot();
        else
            node = (MMGPBillTreeTableModelNode) getNode(row);

        return node;
    }

    public final MutableTreeNode getRoot() {
        return (MutableTreeNode) treeTableModel.getRoot();
    }

    /**
     * �кű�ģʽ. ��������:(01-2-21 10:08:48)
     */
    protected DefaultTableModel createDefaultRowNumberModel() {
        return new RowNumberModel();
    }

    @Override
    public CircularlyAccessibleValueObject[] getBodyValueVOs(String bodyVOName) {
        try {
            Class< ? > bodyVOClass = Class.forName(bodyVOName);
            int length = super.getRowCount();
            CircularlyAccessibleValueObject[] bodyVOs =
                    (CircularlyAccessibleValueObject[]) Array.newInstance(bodyVOClass, length);

            for (int i = 0; i < length; i++) {
                bodyVOs[i] = getBodyValueRowVO(i, bodyVOName);
            }
            return bodyVOs;
        } catch (ClassNotFoundException e) {

        }
        return null;
    }

    /*
     * �õ���VO
     */

    public CircularlyAccessibleValueObject getBodyValueRowVO(int row,
                                                             String bodyVOName) {
        try {
            CircularlyAccessibleValueObject bodyRowVO =
                    (CircularlyAccessibleValueObject) Class.forName(bodyVOName).newInstance();
            // for (int j = 0; j < getBodyItems().length; j++) {
            // BillItem item = getBodyItems()[j];
            // Object aValue = getValueAt(row, j);
            // aValue = item.converType(aValue);
            // bodyRowVO.setAttributeValue(item.getKey(), aValue);
            // }
            getBodyRowVO(row, bodyRowVO);
            // ����״̬
            switch (getRowStateS(row)) {
                case ADD:
                    bodyRowVO.setStatus(nc.vo.pub.VOStatus.NEW);
                    break;
                case MODIFICATION:
                    bodyRowVO.setStatus(nc.vo.pub.VOStatus.UPDATED);
                    break;
            }
            return bodyRowVO;
        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        }
        return null;
    }

    public int getRowStateS(int row) {

        if (row < 0) return UNSTATE;

        if (dataVector == null) return UNSTATE;
        if (super.getRowCount() <= row) return UNSTATE;

        return vRowAttrib.get(row).getRowState();

    }

    protected void getBodyRowVO(int row,
                                CircularlyAccessibleValueObject bodyVO) {
        for (int j = 0; j < getBodyItems().length; j++) {
            BillItem item = getBodyItems()[j];
            Object aValue = getValueAtModel(row, j);
            aValue = item.converType(aValue);
            setValue2BodyVO(bodyVO, item, aValue);
        }
    }

    /**
     * �õ���Ԫֵ.
     */
    public Object getValueAtModel(int row,
                                  int column) {

        Vector< ? > rowVector = getRowVectorAtData(row);
        if (column >= 0 && rowVector != null && rowVector.size() > column) {
            Object aValue = rowVector.elementAt(column);
            String sv;
            if (getBodyItems()[column].getDataType() == BillItem.BOOLEAN)
                if (aValue != null && aValue instanceof UFBoolean) {
                    aValue = Boolean.valueOf(((UFBoolean) aValue).booleanValue());
                }
            if (aValue instanceof String) {
                sv = ((String) aValue).trim();
                if (sv.length() == 0) {
                    aValue = null;
                }
            }
            return aValue;
        }
        return null;

    }

    protected Vector< ? > getRowVectorAtData(int row) {
        if (row < 0) return null;
        if (dataVector == null || super.getRowCount() <= row) return null;
        Vector< ? > rowVector = (Vector< ? >) dataVector.elementAt(row);
        return rowVector;
    }

    protected void setValue2BodyVO(CircularlyAccessibleValueObject bodyVO,
                                   BillItem item,
                                   Object aValue) {
        if (aValue != null && item.getDataType() == IBillItem.MULTILANGTEXT && aValue instanceof MultiLangText) {
            MultiLangText mlt = (MultiLangText) aValue;

            bodyVO.setAttributeValue(item.getKey(), mlt.getText());
            bodyVO.setAttributeValue(item.getKey() + "2", mlt.getText2());
            bodyVO.setAttributeValue(item.getKey() + "3", mlt.getText3());
            bodyVO.setAttributeValue(item.getKey() + "4", mlt.getText4());
            bodyVO.setAttributeValue(item.getKey() + "5", mlt.getText5());
            bodyVO.setAttributeValue(item.getKey() + "6", mlt.getText6());

        } else {
            bodyVO.setAttributeValue(item.getKey(), aValue);
        }
    }

    @SuppressWarnings("unused")
    private void getAllChildNum(TreeNode node,
                                List<TreeNode> list) {

        Enumeration<MutableTreeNode> enu = node.children();
        while (enu.hasMoreElements()) {

            MMGPBillTreeTableModelNode treenode = (MMGPBillTreeTableModelNode) enu.nextElement();

            list.add(treenode);

            if (!treenode.isLeaf()) {

                this.getAllChildNum(treenode, list);
            }
        }
    }

    public void clearBodyData() {
        super.clearBodyData();
        int count = this.getRoot().getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            MutableTreeNode node = (MutableTreeNode) this.getRoot().getChildAt(i);
            removeNodeFromParent(node);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked" })
    public void insertRow(int row,
                          Vector rowData) {
        this.dataVector.insertElementAt(rowData, row);
        justifyRowss(row, row + 1);
        fireTableRowsInserted(row, row);
    }

    private void justifyRowss(int from,
                              int to) {
        this.dataVector.setSize(super.getRowCount());

        for (int i = from; i < to; i++) {
            if (this.dataVector.elementAt(i) == null) {
                this.dataVector.setElementAt(new Vector(), i);
            }
            ((Vector) this.dataVector.elementAt(i)).setSize(getColumnCount());
        }

    }

    /**
     * ���ñ�������. ��������:(01-2-23 14:22:07)
     */
    public void setBodyDataVO(CircularlyAccessibleValueObject[] bodyVOs) {

        clearBodyData();
        if (bodyVOs == null || bodyVOs.length == 0) return;
        boolean needCalculate = isNeedCalculate();
        setNeedCalculate(false);
        DefaultMutableTreeNode root = null;
        try {
            root = constructTreeModel(bodyVOs);
            /* insertTree(root); */

            int count = root.getChildCount();
            for (int i = 0; i < count; i++) {
            	//2015-5-19 gaotx EHP1 �Ż��������ܣ���loadRelation�ŵ�ѭ������
				MMGPBillTreeTableTools.insertTreeOnly((DefaultMutableTreeNode) root.getChildAt(i), (DefaultMutableTreeNode) this.getRoot(), this
						.getRoot().getChildCount(), this);
            }
            //2015-5-19 gaotx EHP1 �Ż��������ܣ���resetLineNumHM,loadRelation�ŵ�ѭ������
            this.resetLineNumHM();
            this.loadLoadRelationItemValue();
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }

        setNeedCalculate(needCalculate);

        fireTableChanged(new TableModelEvent(this));
    }

    /*
     * private void insertTree(DefaultMutableTreeNode insertRoot) throws BusinessException {
     * Stack<DefaultMutableTreeNode> stack = new Stack<DefaultMutableTreeNode>(); stack.push(insertRoot); Map<String,
     * MutableTreeNode> pk2TreeTableNodeMap = new HashMap<String, MutableTreeNode>(); IBean bean = null; String pid =
     * null; while (!stack.isEmpty()) { DefaultMutableTreeNode curnode = stack.pop(); if (!curnode.equals(insertRoot)) {
     * DefaultMutableTreeNode parent = null; if (curnode.getParent().equals(insertRoot)) { parent =
     * (DefaultMutableTreeNode) this.getRoot(); } else { CircularlyAccessibleValueObject curbodyvo =
     * (CircularlyAccessibleValueObject) curnode .getUserObject(); if(bean==null){ bean =
     * MMMetaUtils.getBeanByClassFullName(curbodyvo.getClass().getName()); pid = MMMetaUtils.getParentPKFieldName(bean);
     * } parent = (DefaultMutableTreeNode) pk2TreeTableNodeMap .get(curbodyvo.getAttributeValue(pid)); } MutableTreeNode
     * newchild = this.insertRow(parent, (CircularlyAccessibleValueObject) curnode .getUserObject(),
     * parent.getChildCount()); String key = null; key = ((CircularlyAccessibleValueObject) curnode
     * .getUserObject()).getPrimaryKey(); pk2TreeTableNodeMap.put(key, newchild); } int count = curnode.getChildCount();
     * for (int i = count; i > 0; i--) { stack.push((DefaultMutableTreeNode) curnode.getChildAt(i - 1)); } } }
     */

    /**
     * ����PID �������νṹ
     * 
     * @param bodyVOs
     *        ������VOs
     * @return
     * @throws BusinessException
     */
    private DefaultMutableTreeNode constructTreeModel(CircularlyAccessibleValueObject[] bodyVOs)
            throws BusinessException {
        Map<String, List<CircularlyAccessibleValueObject>> parentpk2voMap =
                new HashMap<String, List<CircularlyAccessibleValueObject>>();
        IBean bean = null;
        String pid = null;
        for (CircularlyAccessibleValueObject body : bodyVOs) {
            if (bean == null) {
                bean = MMMetaUtils.getBeanByClassFullName(body.getClass().getName());
                pid = MMMetaUtils.getParentPKFieldName(bean);
            }
            String cparentid = (String) body.getAttributeValue(pid);
            List<CircularlyAccessibleValueObject> childlist = parentpk2voMap.get(cparentid);
            if (childlist == null) {
                childlist = new ArrayList<CircularlyAccessibleValueObject>();
                parentpk2voMap.put(cparentid, childlist);
            }
            childlist.add(body);
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        Stack<DefaultMutableTreeNode> stack = new Stack<DefaultMutableTreeNode>();
        stack.push(root);
        while (!stack.isEmpty()) {
            DefaultMutableTreeNode curnode = stack.pop();
            Object obj = curnode.getUserObject();
            String pk = null;
            if (obj != null) {
                pk = ((CircularlyAccessibleValueObject) obj).getPrimaryKey();
            }
            List<CircularlyAccessibleValueObject> childlist = parentpk2voMap.get(pk);
            if (childlist == null) {
                continue;
            }
            Collections.sort(childlist, comparator);
            for (int i = 0; i < childlist.size(); i++) {
                CircularlyAccessibleValueObject body = childlist.get(i);
                DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(body);
                curnode.add(childnode);
                stack.push(childnode);
            }
        }
        return root;
    }

}