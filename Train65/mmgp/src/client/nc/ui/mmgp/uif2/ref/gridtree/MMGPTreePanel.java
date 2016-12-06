package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.ExTreeNode;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.bd.ref.event.NCEventObject;
import nc.ui.bd.ref.event.RefEventConstant;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.tree.IFilterByText;
import nc.ui.pub.beans.tree.TreeFilterHandler;
import nc.vo.bd.ref.IRefTreeExpandStrategy;

public class MMGPTreePanel extends UIPanel implements TreeSelectionListener {

    private TreeFilterHandler filterHandler = new TreeFilterHandler();

    private MMGPTreeGridRefModel refModel;

    private UIScrollPane SclPane_tree = null;

    private nc.ui.pub.beans.UITree ivjUITree1 = null;

    private MMGPTreeGridRefUI refUI;

    private boolean isIncludeSubShow = true;

    private boolean showCheckBoxes;

    private HashSet<ExTreeNode> checkedNodes = new HashSet<ExTreeNode>();

    // Hashtable<String, ExTreeNode> pkToNode = new Hashtable<String,
    // ExTreeNode>();

    private RefTreeNodeFilter refTreeNOdeFilter = new RefTreeNodeFilter();

    private ArrayList<ItemListener> itemListeners = new ArrayList<ItemListener>();

    public final HashSet<ExTreeNode> getCheckedNodes() {
        return checkedNodes;
    }

    public MMGPTreePanel(Dimension dimension,
                         MMGPTreeGridRefModel refModel,
                         MMGPTreeGridRefUI ui) {
        super();
        this.setLayout(new BorderLayout());
        this.setPreferredSize(dimension);
        this.setRefModel(refModel);
        this.setRefUI(ui);
        init();
    }

    private void init() {
        JComponent comp = getToolPanelComponent();
        if (comp != null) {
            // treePanel.add(filterHandler.getFilterTextInputComponent(),
            // BorderLayout.NORTH);
            UIPanel topPanel = new UIPanel();
            topPanel.setLayout(new BorderLayout());
            topPanel.add(comp, BorderLayout.SOUTH);
            topPanel.add(filterHandler.getFilterTextInputComponent(), BorderLayout.NORTH);
            this.add(topPanel, BorderLayout.NORTH);
        } else {
            this.add(filterHandler.getFilterTextInputComponent(), BorderLayout.NORTH);
        }
        if (isMultiSelectedRef()) {
            setShowCheckBoxes(true);
            // 允许不连续选取树节点
            getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        } else {
            setShowCheckBoxes(false);
            getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        }
        this.add(getSclPane());
        filterHandler.setTree(getTree());
        filterHandler.setIFilterByText(refTreeNOdeFilter);
    }

    MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getSource() == getTree() && e.getClickCount() == 2 && !(e.isControlDown() || e.isShiftDown()))
                processMouseDoubleClicked(e);
        }
    };

    /**
     * 此处插入方法说明。 创建日期：(02-11-21 10:06:28)
     */
    private void processMouseDoubleClicked(java.awt.event.MouseEvent e) {

        TreePath tp = getTree().getSelectionPath();
        if (tp == null) {
            return;
        }
        ExTreeNode selectedNode = (ExTreeNode) tp.getLastPathComponent();
        if ((!isMultiSelectedRef() && selectedNode.isCanSelected())
            || (isMultiSelectedRef() && !clickedInCheckBox(tp, e.getX(), e.getY()))
            && selectedNode.isCanSelected())// 双击checkbox之外
        {
            dealwithSelectionAction();
        }

    }

    boolean isCanSelectedNodeDoubleClicked = true;

    private void dealwithSelectionAction() {
        if (isCanSelectedNodeDoubleClicked) {
            TreePath tp = getTree().getSelectionPath();
            ExTreeNode selectedNode = (ExTreeNode) tp.getLastPathComponent();
            if (selectedNode.getParent() == null) {
                return;
            }
            if (isShowCheckBoxes()) {
                if (!getCheckedNodes().contains(selectedNode)) {
                    CheckedNode(selectedNode);
                }
                getRefUI().onAdd();
            }
            getRefUI().onButtonOK();
        }
    }

    public void dealSelectionOnButtonOK() {
        // 在点确定时，如果没有已选数据，若节点有焦点，则认为也是选中节点
        TreePath tp = getTree().getSelectionPath();
        if (tp == null) return;
        ExTreeNode selectedNode = (ExTreeNode) tp.getLastPathComponent();
        if (selectedNode.getParent() == null) {
            return;
        }
        if (isShowCheckBoxes()) {
            if (!getCheckedNodes().contains(selectedNode)) {
                CheckedNode(selectedNode);
            }
            getRefUI().onAdd();
        }
    }

    public Vector getSelectData() {
        if (isShowCheckBoxes()) {
            return getSelectedVecDataByCheckBox();
        }
        return getSelectedVecData();
    }

    private Vector getSelectedVecData() {

        Vector vSelectedData = null;

        TreePath[] treePaths = getTree().getSelectionPaths();

        if (treePaths != null && treePaths.length > 0) {

            Object[] retPKs = null;
            Object[] records = null;

            retPKs = getSelectedPKs(treePaths, true);
            records = getSelectedPKs(treePaths, false);

            // if (getRefDialog().isIncludeSubNode()) {
            // RefPubUtil.saveTempData(getRefModel(), retPKs);
            // }

            // 设置RefTempDataWherePart到Model
            // getRefModel().setIncludeSub(getRefUI().isIncludeSubNode());

            // 处理多选出来的记录
            vSelectedData = new Vector();
            if (records != null && records.length > 0) {
                for (int i = 0; i < records.length; i++) {

                    vSelectedData.addElement(records[i]);
                }
            }

        }
        return vSelectedData;
    }

    private void setSelectedToCollection(ArrayList al,
                                         Vector vecRecord,
                                         ExTreeNode node) {
        Object obj = node.getUserObject();
        Vector vec = null;

        if (obj instanceof Vector) {
            vec = (Vector) obj;

        } else {
            return;
        }

        if (node.isCanSelected()) {
            String pk = (vec.get(getRefModel().getFieldIndex(getRefModel().getPkFieldCode()))).toString();
            al.add(pk);
            vecRecord.add(vec);

        }
    }

    private Object[] getSelectedPKs(TreePath[] treePaths,
                                    boolean isPk) {

        Object[] objs = null;

        ArrayList al = new ArrayList();
        Vector vecRecord = new Vector();
        Map map = new HashMap();

        for (int i = 0; i < treePaths.length; i++) {
            ExTreeNode m_nSelectedNode = (ExTreeNode) treePaths[i].getLastPathComponent();

            // 如果该节点已经处理过，不处理。
            if (getRefUI().isIncludeSubNode()) {
                Enumeration enumeration = m_nSelectedNode.breadthFirstEnumeration();
                while (enumeration.hasMoreElements()) {
                    ExTreeNode node = (ExTreeNode) enumeration.nextElement();

                    // 如果是根节点或者已经处理过的节点，下一个
                    if (node.isRoot() || map.containsKey(node)) {
                        continue;
                    }
                    setSelectedToCollection(al, vecRecord, node);
                    map.put(node, node);
                }
                // 返回所有PK,不用继续循环了。
                if (m_nSelectedNode.isRoot()) {

                    break;
                }
            } else {
                setSelectedToCollection(al, vecRecord, m_nSelectedNode);
            }

        }

        if (isPk) {
            // 得到所有的pks
            String[] pks = null;
            if (al.size() > 0) {
                pks = new String[al.size()];
                al.toArray(pks);
            }
            objs = pks;

        } else {
            // 得到所有的记录
            if (vecRecord.size() > 0) {
                objs = new Object[vecRecord.size()];
                vecRecord.copyInto(objs);
            }
        }

        return objs;

    }

    private Vector getSelectedDataByCheckBoxIncludeSub() {
        ArrayList al = new ArrayList();
        Vector vecRecord = new Vector();
        Map map = new HashMap();
        for (Object item : checkedNodes) {
            ExTreeNode selnode = (ExTreeNode) item;
            Enumeration enumeration = selnode.breadthFirstEnumeration();
            while (enumeration.hasMoreElements()) {
                ExTreeNode node = (ExTreeNode) enumeration.nextElement();

                // 如果是根节点或者已经处理过的节点，下一个
                if (node.isRoot() || map.containsKey(node)) {
                    continue;
                }
                setSelectedToCollection(al, vecRecord, node);
                map.put(node, node);
            }
            // 返回所有PK,不用继续循环了。
            if (selnode.isRoot()) {
                break;
            }
        }
        return vecRecord;

    }

    private Vector getSelectedVecDataByCheckBox() {
        // tangxya add
        if (getRefUI().isIncludeSubNode()) {
            return getSelectedDataByCheckBoxIncludeSub();
        }
        // tangxya end
        if (checkedNodes.size() > 0) {
            ArrayList al = new ArrayList();
            Vector vecRecord = new Vector();
            for (Object item : checkedNodes) {
                ExTreeNode node = (ExTreeNode) item;
                Object obj = node.getUserObject();
                Vector vec = null;

                if (obj instanceof Vector) {
                    vec = (Vector) obj;
                    vecRecord.add(vec);
                } else {
                    continue;
                }
            }
            // getRefModel().setIncludeSub(getRefUI().isIncludeSubNode());
            return vecRecord;
        }
        return null;
    }

    private void CheckedNode(DefaultMutableTreeNode node) {
        MMGPTreeGridNodeListenner l = getRenderListener();
        if (l != null) l.checkChanged(node, isIncludeSubNode());
    }

    private MMGPTreeGridNodeListenner getRenderListener() {
        if (getTree().getCellRenderer() instanceof MMGPRefTreeCellRenderer) {
            MMGPTreeGridNodeListenner l =
                    ((MMGPRefTreeCellRenderer) getTree().getCellRenderer()).getTreeNodeRenderListener();
            return l;
        }
        return null;
    }

    private boolean isDBLeaf(ExTreeNode tnSelected) {
        boolean isDBLeaf = false;

        if (tnSelected == null) {
            return isDBLeaf;
        }

        if (tnSelected.isRoot()) {
            return isDBLeaf;
        }
        Vector vec = (Vector) tnSelected.getUserObject();
        if (tnSelected.isMainClass()) {
            String pk = vec.elementAt(getRefModel().getFieldIndex(getRefModel().getPkFieldCode())).toString();
            if (pk != null) {
                isDBLeaf = getRefModel().isLeaf(pk);
            }
        }
        return isDBLeaf;
    }

    private boolean clickedInCheckBox(TreePath path,
                                      int x,
                                      int y) {
        Point p = getTree().getPathBounds(path).getLocation();
        int fix = 4;
        Rectangle rect =
                new Rectangle(
                    p.x,
                    p.y,
                    MMGPRefTreeCellRenderer.CHECKBOXSIZE + fix,
                    MMGPRefTreeCellRenderer.CHECKBOXSIZE + fix);
        return rect.contains(x, y);
    }

    /**
     * 返回 UIScrollPane1 特性值。
     * 
     * @return nc.ui.pub.beans.UIScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UIScrollPane getSclPane() {
        if (SclPane_tree == null) {

            SclPane_tree = new nc.ui.pub.beans.UIScrollPane();
            SclPane_tree.setName("UIScrollPane1");
            SclPane_tree.setViewportView(getTree());
            SclPane_tree.setBorder(null);

        }
        return SclPane_tree;
    }

    public void clearTreeData() {
        this.setTreeModel(new Vector());
        this.getTree().clearSelection();
        this.getRefModel().setTableJoinValue(null);
    }

    class RefTreeNodeFilter implements IFilterByText {

        @Override
        public boolean accept(DefaultMutableTreeNode node,
                              String filterText) {
            if (node.isRoot()) {
                return true;
            }
            Vector record = (Vector) node.getUserObject();

            int codeFieldIndex = getRefModel().getFieldIndex(refModel.getRefCodeField());
            int nameFieldIndex = getRefModel().getFieldIndex(refModel.getRefNameField());

            Object code = record.get(codeFieldIndex);
            Object name = record.get(nameFieldIndex);

            if (code != null
                && (isMatch(filterText, code.toString()) || (getRefModel().isSupportPY() && RefPubUtil.isMatch(
                    filterText,
                    code.toString())))) {
                return true;
            }
            if (name != null
                && (isMatch(filterText, name.toString()) || (getRefModel().isSupportPY() && RefPubUtil.isMatch(
                    filterText,
                    name.toString())))) {
                return true;
            }

            return false;
        }

        @Override
        public DefaultMutableTreeNode cloneMatchedNode(DefaultMutableTreeNode matchedNode) {
            return (DefaultMutableTreeNode) matchedNode.clone();
        }

    }

    public TreeFilterHandler getFilterHandler() {
        return filterHandler;
    }

    private boolean isMatch(String strInput,
                            String strCell) {
        return RefPubUtil.toLowerCaseStr(getRefModel(), strCell).indexOf(
            RefPubUtil.toLowerCaseStr(getRefModel(), strInput)) >= 0;
    }

    /**
     * 返回 UITree1 特性值。
     * 
     * @return nc.ui.pub.beans.UITree
     */
    /* 警告：此方法将重新生成。 */
    public nc.ui.pub.beans.UITree getTree() {
        if (ivjUITree1 == null) {
            ivjUITree1 = new nc.ui.pub.beans.UITree(){
                @Override
                public void collapsePath(TreePath path) {
                    
                    if (isMouseClick && !clickedInToggle)
                        return;
                    super.collapsePath(path);
                    isMouseClick = false;
                    clickedInToggle = false;
                }
                
                private boolean clickedInToggle = false;//好象点在+-号上时path为空，用此来判断，
                private boolean isMouseClick = false;
                @Override
                protected void processMouseEvent(MouseEvent e) {
                    int id = e.getID();
                    if (id == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(e)) {
                        TreePath path = getPathForLocation(e.getX(),e.getY());
                        if (isShowCheckBoxes()) {//此处为了点CheckBox方框时不展开树节点
                            if (path != null && clickedInCheckBox(path, e.getX(),e.getY())) {
                                return;
                            }
                        }
                        if (path == null)
                            clickedInToggle = true;
                        else
                            clickedInToggle = false;
                        isMouseClick = true;
                    } else if (!SwingUtilities.isLeftMouseButton(e)) {//对于右键等，为了不影响菜单上的collapsePath操作，此处设置为true
                        clickedInToggle = false;
                        isMouseClick = false;
                    }
                    super.processMouseEvent(e);
                }
                
                @Override
                public boolean getShowsRootHandles()
                {
                    if(!this.isRootVisible()){
                        return true;
                    }
                    return showsRootHandles;
                }
            };
            ivjUITree1.setName("UITree1");
            ivjUITree1.setBorder(new EmptyBorder(11, 11, 11, 12));
            ivjUITree1.putClientProperty("JTree.lineStyle", "Angled");

            ivjUITree1.setCellRenderer(new MMGPRefTreeCellRenderer(this, refModel));
            ivjUITree1.setToggleClickCount(1);
            ivjUITree1.addFocusListener(new FocusListener() {
                @Override
                public void focusLost(FocusEvent e) {

                    TreePath treePath = ivjUITree1.getSelectionPath();
                    if (treePath == null) {
                        if (!showCheckBoxes) {
                            MMGPTreePanel.this.getRefUI().getUIButtonOK().setEnabled(false);
                        }
                        return;
                    }
                    ExTreeNode tnSelected = (ExTreeNode) treePath.getLastPathComponent();
                    if (!showCheckBoxes)
                        MMGPTreePanel.this
                            .getRefUI()
                            .getUIButtonOK()
                            .setEnabled(isSelectedEnabled(tnSelected, isDBLeaf(tnSelected)));
                }

                @Override
                public void focusGained(FocusEvent e) {
                    if (ivjUITree1.getSelectionCount() == 0 && ivjUITree1.getRowCount() > 0) {
                        ivjUITree1.setSelectionRow(0);
                    }
                }
                
              
            });
            getTree().addMouseListener(mouseAdapter);
            getTree().addTreeSelectionListener(this);
        }
        return ivjUITree1;
    }

    private boolean isMultiSelectedRef() {
        return getRefUI().getRefUIConfig().isMutilSelected() || isMultiOrgSelected();
    }

    private boolean isMultiOrgSelected() {
        return getRefUI().getRefUIConfig().isMultiCorpRef() && getRefUI().getRefUIConfig().isMultiOrgSelected();
    }

    private JComponent getToolPanelComponent() {
        String compName = getRefModel().getUiControlComponentClassName();
        if (compName == null) {
            return null;
        }
        JComponent comp = null;
        Class cl;
        try {
            cl = Class.forName(compName);
            Constructor c = cl.getConstructor(new Class[]{MMGPTreeGridRefUI.class });
            comp = (JComponent) c.newInstance(new Object[]{this.getRefUI() });
        } catch (ClassNotFoundException e) {
            Logger.debug(e);
        } catch (SecurityException e) {
            Logger.debug(e);
        } catch (NoSuchMethodException e) {
            Logger.debug(e);
        } catch (IllegalArgumentException e) {
            Logger.debug(e);
        } catch (InstantiationException e) {
            Logger.debug(e);
        } catch (IllegalAccessException e) {
            Logger.debug(e);
        } catch (InvocationTargetException e) {
            Logger.debug(e);
        }
        return comp;

    }

    /*
     * （非 Javadoc）
     * @see nc.ui.bd.ref.IRefDataComponent#setMatchedPKs(java.lang.String[])
     */
    public void setMatchedPKs(String[] pks) {
        getTree().clearSelection();
        if (pks != null) {
            TreePath[] treePaths = new TreePath[pks.length];
            ExTreeNode node = null;
            for (int i = 0; i < pks.length; i++) {
                if (pks[i] == null) {
                    continue;
                }
                node = (ExTreeNode) this.getPkToNode().get(pks[i]);
                if (node != null) {
                    treePaths[i] = new TreePath(node.getPath());
                    if (isShowCheckBoxes()) getCheckedNodes().add(node);
                }

            }
            if (!isShowCheckBoxes()) getTree().addSelectionPaths(treePaths);
            selectAndExpandToNode(node);
        }

    }

    private void selectAndExpandToNode(ExTreeNode node) {
        if (node == null) {
            return;
        }
        TreePath treePath = new TreePath(node.getPath());
        getTree().expandPath(treePath);
        // getTree().setSelectionPath(treePath);
        getTree().makeVisible(treePath);
        getTree().scrollPathToVisible(treePath);
    }

    /**
     * 按照上下级关系生成树 创建日期：(2001-8-27 22:06:42)
     */
    public void setTreeModel(Vector vTree) {

        getRefModel().clearTreeModel();
        DefaultTreeModel tm = getRefModel().getTreeModel();
        getTree().setModel(tm);
        try {
            if (checkedNodes != null) {
                if (!getRefUI().isIncludeSubNode()) {// tangxya
                    checkedNodes.clear();
                }

            }
        } catch (Exception ex) {
        }
        boolean isRootVisible = getRefModel().isRootVisible();
        // 如果现实跟节点并且没有数据，就不显示跟节点
        if (isRootVisible && (tm.getChildCount((DefaultMutableTreeNode) tm.getRoot()) == 0)) {
            isRootVisible = false;
        }
        getTree().setRootVisible(isRootVisible);

        expandTreePath();
        getTree().updateUI();

    }

    // /*
    // * *
    // * 按照上下级关系生成树 创建日期：(2001-8-27 22:06:42)
    // */
    // public void setTreeModel_dep(Vector<Vector> vTree) {
    // ExTreeNode root = null;
    // root = new ExTreeNode(refModel.getRootName(), true);
    // root.setIsCanSelected(false);
    // DefaultTreeModel tm = new DefaultTreeModel(root, false);
    // getTree().setModel(tm);
    // Vector<Vector> m_vecVOs = vTree;
    // // sxj 2004-06-23 新构造树方法。
    //
    // Hashtable<String, ExTreeNode> hAllNode = getPkToNode();
    // hAllNode.clear();
    //
    // hAllNode.put("root", root);
    // HashMap<Object,Vector> hm = new HashMap<Object,Vector>();
    // Vector<ExTreeNode> vAllTreeNode = new Vector<ExTreeNode>();
    //
    // String childField = refModel.getChildField();
    //
    // if (childField == null) {
    // childField = refModel.getPkFieldCode();
    // }
    //
    // int chileFieldIndex = refModel.getFieldIndex(childField);
    //
    // for (int i = 0; i < m_vecVOs.size(); i++) {
    // Vector row = (Vector) m_vecVOs.elementAt(i);
    // ExTreeNode nodepar = new ExTreeNode(row, getTreeColumn(),
    // refModel.getMark());
    //
    // if (!refModel.isCanSelect(row)) {
    // nodepar.setIsCanSelected(false);
    // }
    // vAllTreeNode.add(nodepar);
    // hm.put(row.elementAt(chileFieldIndex), row);
    // hAllNode.put((String) row.elementAt(chileFieldIndex), nodepar);
    //
    // }
    // int fatherFieldIndex = 0;
    //
    // if (refModel.getFatherField() != null) {
    // fatherFieldIndex = refModel.getFieldIndex(refModel.getFatherField());
    // }
    //
    // for (int i = 0; i < m_vecVOs.size(); i++) {
    // ExTreeNode nodepar = (ExTreeNode) vAllTreeNode.get(i);
    // Vector row = (Vector) m_vecVOs.elementAt(i);
    //
    // String fatherCodeValue = null;
    //
    // if (fatherFieldIndex >= 0 && fatherFieldIndex < row.size()) {
    // fatherCodeValue = (String) row.get(fatherFieldIndex);
    // }
    //
    // if (fatherCodeValue == null || fatherCodeValue.trim().length() == 0 ||
    // hm.get(fatherCodeValue) == null) {
    // root.insert(nodepar, root.getChildCount());
    // getTree().makeVisible(new TreePath(nodepar.getPath()));
    // } else {
    //
    // ExTreeNode nodeparFather = (ExTreeNode) hAllNode.get(fatherCodeValue);
    // if (nodeparFather == null) {
    // Debug.debug("to find father error:" + fatherCodeValue + ":" + nodepar);
    // // 插入到根节点
    // root.insert(nodepar, root.getChildCount());
    // } else {
    // nodeparFather.insert(nodepar, nodeparFather.getChildCount());
    // }
    //
    // }
    // }
    // expandTreePath();
    // getTree().updateUI();
    // // return tm;
    // }

    public void expandTreePath() {

        int treeExpandStrategy = this.getRefModel().getTreeExpandStrategy();
        switch (treeExpandStrategy) {
            case IRefTreeExpandStrategy.LEVEL:
                DefaultMutableTreeNode root = getRootNode();
                DefaultMutableTreeNode[] nodes =
                        getLevelTreeNodes(new DefaultMutableTreeNode[]{root }, 0, getRefModel().getExpandLevel());
                makeVisible(nodes);

                break;
            case IRefTreeExpandStrategy.ALL:
                makeVisible(getAllTreeNode());
                break;
            case IRefTreeExpandStrategy.NODE:
                String nodepk = this.getRefModel().getTreeExpandNodePk();
                if (nodepk == null) {
                    nodepk = "root";
                    this.getRefModel().setTreeExpandNodePk(nodepk);
                }
                DefaultMutableTreeNode node = this.getPkToNode().get(nodepk);
                if (node != null) {
                    makeVisible(new DefaultMutableTreeNode[]{node });
                }
                break;

            default:
                break;
        }

    }

    protected DefaultMutableTreeNode getRootNode() {
        return (DefaultMutableTreeNode) this.getTree().getModel().getRoot();
    }

    public final ItemListener[] getItemListeners() {
        return (ItemListener[]) itemListeners.toArray(new ItemListener[itemListeners.size()]);
    }

    public final void addItemListener(ItemListener listener) {
        itemListeners.add(listener);
    }

    public final void removeItemListener(ItemListener listener) {
        itemListeners.remove(listener);
    }

    public void collapseTreePath() {
        int rowCount = getTree().getRowCount();
        for (int i = rowCount - 1; i > 0; i--) {
            getTree().collapseRow(i);
        }

    }

    public final DefaultMutableTreeNode getSelectedNode() {
        try {
            javax.swing.tree.TreePath selPath = getTree().getSelectionPath();
            if (selPath != null) return (DefaultMutableTreeNode) (selPath
                .getPathComponent(selPath.getPathCount() - 1));
            else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public DefaultMutableTreeNode[] getLevelTreeNodes(DefaultMutableTreeNode[] selectedTreeNodes,
                                                      int beginLevel,
                                                      int endLevel) {
        if (selectedTreeNodes == null) {
            return null;
        }

        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
        for (int i = 0; i < selectedTreeNodes.length; i++) {
            // int level = selectedTreeNodes[i].getLevel();

            Enumeration enumeration = selectedTreeNodes[i].breadthFirstEnumeration();
            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                if (node.isRoot()) {
                    continue;
                }
                if (node.getLevel() >= beginLevel && node.getLevel() <= endLevel) {
                    if (!list.contains(node)) {
                        list.add(node);
                    }

                }
            }
        }
        return list.toArray((DefaultMutableTreeNode[]) Array.newInstance(DefaultMutableTreeNode.class, 0));

    }

    private void makeVisible(DefaultMutableTreeNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            // 展开
            getTree().makeVisible(new TreePath(nodes[i].getPath()));

        }
    }

    private DefaultMutableTreeNode[] getAllTreeNode() {

        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
        DefaultMutableTreeNode root = getRootNode();
        Enumeration enumeration = root.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {

            list.add((DefaultMutableTreeNode) enumeration.nextElement());
        }

        return list.toArray((DefaultMutableTreeNode[]) Array.newInstance(DefaultMutableTreeNode.class, 0));
    }

    public IEventListener delTreeSelectedRowHandler = new IEventListener() {
        public void invoke(Object sender,
                           NCEventObject evt) {
            if (evt.getProperty(RefEventConstant.SELECTED_PKS) != null) {
                String[] pks = (String[]) evt.getProperty(RefEventConstant.SELECTED_PKS);
                checkedTreeNodeByPKs(pks, false);
            }
        }
    };

    private void checkedTreeNodeByPKs(String[] pks,
                                      Boolean isChecked) {
        for (int i = 0; i < pks.length; i++) {
            Object pk = pks[i];
            if (pk == null) continue;
            ExTreeNode node = null;
            node = (ExTreeNode) this.getPkToNode().get(pk);
            if (node != null && !isChecked) {
                if (isShowCheckBoxes()) getCheckedNodes().remove(node);
            } else if (node != null && isChecked) {
                if (isShowCheckBoxes()) getCheckedNodes().add(node);
            }

        }
        getTree().repaint();
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-27 19:31:43)
     * 
     * @return int[]
     */
    private int[] getTreeColumn() {
        int[] cols = null;
        if (refModel != null && refModel.getDefaultFieldCount() > 0) {
            cols = new int[refModel.getDefaultFieldCount()];
            for (int i = 0; i < refModel.getDefaultFieldCount(); i++) {
                cols[i] = i;
            }
        }
        return cols;
    }

    /**
     * @return 返回 pkToNode。
     */
    private Hashtable<String, ExTreeNode> getPkToNode() {
        return this.getRefModel().getModelPkToNode();
    }

    /**
     * 
     */
    public boolean isIncludeSubNode() {
        return isIncludeSubShow() && getChkIncludeSubNode().isSelected();
    }

    /**
     * @return 返回 chkSubNode。
     */
    private UICheckBox getChkIncludeSubNode() {

        return getRefUI().getButtonPanelFactory().getChkIncludeSubNode();
    }

    public MMGPTreeGridRefModel getRefModel() {
        return refModel;
    }

    public void setRefModel(MMGPTreeGridRefModel refModel) {
        this.refModel = refModel;
    }

    public MMGPTreeGridRefUI getRefUI() {
        return refUI;
    }

    public void setRefUI(MMGPTreeGridRefUI refUI) {
        this.refUI = refUI;
    }

    public boolean isIncludeSubShow() {
        return isIncludeSubShow;
    }

    public void setIncludeSubShow(boolean isIncludeSubShow) {
        this.isIncludeSubShow = isIncludeSubShow;
    }

    public boolean isShowCheckBoxes() {
        return showCheckBoxes;
    }

    public void setShowCheckBoxes(boolean showCheckBoxes) {
        this.showCheckBoxes = showCheckBoxes;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if ((e.getSource() == getTree())) {
            actionUITree(e);
        }

    }

    private void actionUITree(javax.swing.event.TreeSelectionEvent treeSelectionEvent) {
        // Object[] pks = getSelectedPKs(treeSelectionEvent.getPaths(), true);
        // if (pks != null) {
        // this.getRefUI().setRefCardInfoPk(pks[0] == null ? null : pks[0].toString());
        // } else {
        // getRefDialog().setRefCardInfoPk(null);
        // }

        if (treeSelectionEvent.getNewLeadSelectionPath() == null) {
            if (!showCheckBoxes) this.getRefUI().getUIButtonOK().setEnabled(false);
            return;
        }
        TreePath treePath = treeSelectionEvent.getPath();
        ExTreeNode tnSelected = (ExTreeNode) treePath.getLastPathComponent();
        // 单选的情形
        if (!showCheckBoxes)
            this.getRefUI().getUIButtonOK().setEnabled(isSelectedEnabled(tnSelected, isDBLeaf(tnSelected)));

    }

    private boolean isSelectedEnabled(Object value,
                                      boolean leaf) {
        if (((DefaultMutableTreeNode) value).getParent() == null) return false;
        if (!leaf) {
            if (!this.getRefUI().getRefUIConfig().isNotLeafSelectedEnabled()) return false;
        }
        if (!(value instanceof ExTreeNode && ((ExTreeNode) value).isCanSelected())) return false;
        if (!(value instanceof ExTreeNode && ((ExTreeNode) value).isMainClass())) return false;
        return true;
    }
}
