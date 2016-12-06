package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


import javax.swing.AbstractAction;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import javax.swing.tree.TreePath;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.ExTreeNode;
import nc.ui.bd.ref.RefLevelSelectedDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPopupMenu;
import nc.vo.bd.ref.IRefTreeExpandStrategy;

public class MMGPTreeGridNodeListenner implements MouseListener, KeyListener {

    public static final String SELECT_All = "selectAll";
    public static final String SELECT_NONE = "selectNone";
    public static final String SELECT_SUB_NODES = "selectSubNodes";
    public static final String CLEAR_SUB_NODES = "clearSubNodes";
    public static final String CODE = "Code";
    
    private MMGPTreePanel treeComponent;
    private UIPopupMenu popupMenu;
    private ArrayList<Integer> lastSelectedRows = new ArrayList<Integer>();
    private ArrayList<Integer> selectedRow = new ArrayList<Integer>();

    public MMGPTreeGridNodeListenner(MMGPTreePanel treeComp) {
        this.treeComponent = treeComp;
    }

    public void keyReleased(KeyEvent e) {
        if (treeComponent.isEnabled()) {
            if (e.getKeyChar() == ' ') {
                if (treeComponent.isIncludeSubNode()) {
                    doPressSpace(isSelectAction());
                } else {
                    TreePath[] paths = treeComponent.getTree().getSelectionModel().getSelectionPaths();
                    for (int i = 0; i < paths.length; i++) {
                        TreePath path = paths[i];
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                                .getLastPathComponent();
                        if (((ExTreeNode) node).getParent() != null
                                && ((ExTreeNode) node).isCanSelected())
                            checkChanged(node, false);
                    }
                }
            }
            
            //shift+上下键(按着shift+上下键 (松手))
            if (e.isShiftDown()
                    && (e.getKeyCode() == KeyEvent.VK_DOWN || e
                            .getKeyCode() == KeyEvent.VK_UP)) {
                int[] rows = treeComponent.getTree().getSelectionModel().getSelectionRows();
                doMultiSelection(rows);
                return;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_SHIFT )
                reset();
        }
    }


    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        if (!treeComponent.isShowCheckBoxes())
            return;
        if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_SHIFT) {
            getLastSelectedRows();
            return;
        }
        //shift+上下键
        if (e.isShiftDown()
                && (e.getKeyCode() == KeyEvent.VK_DOWN || e
                        .getKeyCode() == KeyEvent.VK_UP)) {
            int[] rows = treeComponent.getTree().getSelectionModel().getSelectionRows();
            doMultiSelection(rows);
            return;
        }
        
//      //上下键(按着shift+上下键不松手)
//      int rowIndex = treeComponent.getTree().getSelectionModel().getLeadSelectionRow();
//      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//          //manager.processKeyUpOrDown(true, rowIndex);
//          processKeyUpOrDown(true, rowIndex);
//      } else if (e.getKeyCode() == KeyEvent.VK_UP) {
//          //manager.processKeyUpOrDown(false, rowIndex);
//          processKeyUpOrDown(false, rowIndex);
//      }
    }
    
    public void processKeyUpOrDown(boolean isDown, int rowIndex) {
        int row = rowIndex;
        if (isDown) {
            row +=1;
        } else {
            row -=1;
        }
        TreePath path = treeComponent.getTree().getPathForRow(row);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                .getLastPathComponent();
        if (node == null)
            return;
        checkChanged(node, treeComponent.isIncludeSubNode());
    }
    
    //判断是进行选取操作还是取消操作
    private boolean isSelectAction(){
        TreePath[] paths = treeComponent.getTree().getSelectionModel().getSelectionPaths();
        for (int i = 0; i < paths.length; i++) {
            TreePath path = paths[i];
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if(!treeComponent.getCheckedNodes().contains(node) && isSelectedEnabled(node, node.isLeaf()))//有一个CheckBox是没选的，则认为是进行选取操作
                return true;
        }
        return false;
    }
    
    private void doPressSpace(boolean isSelect) {
        TreePath[] paths = treeComponent.getTree().getSelectionModel().getSelectionPaths();
        for (int i = 0; i < paths.length; i++) {
            TreePath path = paths[i];
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                    .getLastPathComponent();
            if (isSelectedEnabled(node, node.isLeaf())) {
                if (isSelect) {
                    if (!treeComponent.getCheckedNodes().contains(node)) {
                        checkChanged(node, true);
                    }
                } else {
                    if (treeComponent.getCheckedNodes().contains(node)) {
                        checkChanged(node, true);
                    }
                }
            }
        }
    }

    private void doMultiSelection(int[] rows) {
        if (!treeComponent.isShowCheckBoxes())
            return;
        Vector<Integer> clearedRows = new Vector<Integer>();
        if (rows.length < selectedRow.size()) {
            Iterator<Integer> list = selectedRow.iterator();
            while (list.hasNext()) {
                boolean include = false;
                Integer curRow = list.next();
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i] == curRow.intValue()) {
                        include = true;
                        break;
                    }
                }
                if (!include) {
                    clearedRows.add(curRow);
                }
            }
        }
        
        if (rows.length > 1) {
            for (int i = 0; i < rows.length; i++) {

                TreePath path = treeComponent.getTree().getPathForRow(rows[i]);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (node == null)
                    return;
                if (((ExTreeNode) node).isCanSelected()
                        && !treeComponent.getCheckedNodes().contains(node)) {
                    checkChanged(node, treeComponent.isIncludeSubNode());
                }
            }
        } else if (rows.length == 1) {
            // processMouseClicked();
        }
        
        if (clearedRows.size() > 0) {
            for (int i = 0; i < clearedRows.size(); i++) {
                int clearedRow = (Integer) clearedRows.get(i);
                Iterator<Integer> list = lastSelectedRows.iterator();
                boolean include = false;
                while (list.hasNext()) {
                    Integer curRow = list.next();
                    if (clearedRow == curRow.intValue()) {
                        include = true;
                        break;
                    }
                }
                if (!include){
                    TreePath path = treeComponent.getTree().getPathForRow(clearedRow);
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (node == null)
                        return;
                        checkChanged(node, treeComponent.isIncludeSubNode());
                }
            }
        }
    
        selectedRow.clear();
        for (int i = 0; i < rows.length; i++) {
            selectedRow.add(rows[i]);
        }
    }
    

    
    private void reset() {
        if (!treeComponent.isShowCheckBoxes())
            return;
        selectedRow.clear();
    }
    
    private void getLastSelectedRows() {
        if (!treeComponent.isShowCheckBoxes())
            return;
        lastSelectedRows.clear();   
        HashSet checkedNodes = treeComponent.getCheckedNodes();
        if (checkedNodes != null && checkedNodes.size() > 0) {
            TreePath[] paths = new TreePath[checkedNodes.size()];
            int i = 0;
            for (Object item : checkedNodes) {
                ExTreeNode node = (ExTreeNode) item;
                TreePath path = new TreePath(node.getPath());
                paths[i] = path;
                i++;
            }
            int[] rows= treeComponent.getTree().getSelectionModel().getRowMapper().getRowsForPaths(paths);
            for (int j = 0; j < rows.length; j++) {
                lastSelectedRows.add(rows[j]);
            }
        }
    }




    public void mouseClicked(MouseEvent e) {
        if (treeComponent.isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
            try {
                if (treeComponent.isShowCheckBoxes()) {
                    if (e.isShiftDown()) {
                        doMultiSelection();
                        return;
                    }
                    if (e.isControlDown()) {
                        return;
                    }
                    //processMouseClicked(e);
                }
            } catch (Exception ex) {
                Logger.debug(ex);
            }
        } 
    }
    



    public void mouseEntered(MouseEvent e) {
    }


    public void mouseExited(MouseEvent e) {
    }


    public void mousePressed(MouseEvent e) {
        if (!treeComponent.isShowCheckBoxes())
            return;
        getLastSelectedRows();
    }


    public void mouseReleased(MouseEvent e) {
        if (treeComponent.isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
            if (treeComponent.isShowCheckBoxes()) {
                if (e.isControlDown()){
                    clickWithCtrlDown(e);
                    return;
                }
                processMouseClicked(e);
            }
            
            return;
        }
        if(e.isPopupTrigger()){
            showTreePopupMenu(e);
        }
        
        reset();
    }
    
    private void doMultiSelection() {
        int[] rows = treeComponent.getTree().getSelectionRows();
        if (rows.length > 1) {
            for (int i = 0; i < rows.length; i++) {
                TreePath path = treeComponent.getTree().getPathForRow(rows[i]);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (node == null)
                    return;
                if (((ExTreeNode) node).isCanSelected()&& !treeComponent.getCheckedNodes().contains(node)) {
                    checkChanged(node, treeComponent.isIncludeSubNode());
                }
            }
        } 
    }
    
    private void clickWithCtrlDown(MouseEvent e){
        TreePath path = treeComponent.getTree().getPathForLocation(e.getX(), e.getY());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                .getLastPathComponent();
        if (node == null)
            return;
        if (((ExTreeNode) node).isCanSelected()) {
            checkChanged(node, treeComponent.isIncludeSubNode());
        }
    }
    
    private void processMouseClicked(MouseEvent e){
        TreePath path = treeComponent.getTree().getPathForLocation(e.getX(), e.getY());
        if (path == null)
            return; 
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                .getLastPathComponent();
        if (node == null)
            return;
        
            if (((ExTreeNode)node).isCanSelected() && clickedInCheckBox(path, e.getX(), e.getY())) {
                if (e.getClickCount() == 1) {
                    checkChanged(node, treeComponent.isIncludeSubNode());
                } else if (e.getClickCount() == 2) {
                    checkChangedReverse(node, true);
                }
                treeComponent.getTree().setSelectionPath(path);
            }
    }
    

    private boolean clickedInCheckBox(TreePath path, int x, int y) {
        Point p = treeComponent.getTree().getPathBounds(path).getLocation();
        int fix = 4;
        Rectangle rect = new Rectangle(p.x, p.y, MMGPRefTreeCellRenderer.CHECKBOXSIZE + fix, MMGPRefTreeCellRenderer.CHECKBOXSIZE + fix);
        return rect.contains(x, y);
    }
    
    private void showTreePopupMenu(MouseEvent e) {
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),treeComponent);
        TreePath path = treeComponent.getTree().getPathForLocation(
                e.getX(), e.getY());
        if(path == null){
            getPopupMenu(true).show(treeComponent, pt.x, pt.y);
            e.consume();
            return;
        }
        treeComponent.getTree().setSelectionPath(path);
        Rectangle rec = treeComponent.getTree().getPathBounds(path);
        if (rec.contains(e.getX(), e.getY())) {
            if (path.getLastPathComponent() != null)
                if (path.getLastPathComponent() instanceof ExTreeNode) {
                    if (((ExTreeNode) path.getLastPathComponent()).getChildCount() > 0)
                        getPopupMenu(false).show(treeComponent, pt.x, pt.y);
                    else
                        getPopupMenu(true).show(treeComponent, pt.x, pt.y);
                }
        }
        e.consume();
    }


    public void checkChanged(DefaultMutableTreeNode node,
            boolean isIncludeSubNode) {
        if (treeComponent.getCheckedNodes().contains(node)) {
            treeComponent.getCheckedNodes().remove(node);
            syncTable(node,false);
            if (isIncludeSubNode) {
                updateCheckboxesOnSubTree(node, false);
            }
        } else {
            if (isSelectedEnabled(node, node.isLeaf())) {
                treeComponent.getCheckedNodes().add((ExTreeNode) node);
                syncTable(node, true);
            }
            if (isIncludeSubNode) {
                updateCheckboxesOnSubTree(node, true);
            }
        }
        ItemListener[] ll = treeComponent.getItemListeners();
        for (int i = 0; i < ll.length; i++)
            ll[i].itemStateChanged(new ItemEvent(
                    new UICheckBox(),
                    ItemEvent.ITEM_STATE_CHANGED,
                    node,
                    treeComponent.getCheckedNodes().contains(node) ? ItemEvent.SELECTED
                            : ItemEvent.DESELECTED));
        treeComponent.getTree().repaint();
    }
    
    private boolean isSelectedEnabled(Object value, boolean leaf) {
        if (((DefaultMutableTreeNode) value).getParent() == null)
            return false;
        if(!leaf){
            if(!treeComponent.getRefUI().getRefUIConfig().isNotLeafSelectedEnabled())
                return false;
        }
        if (!(value instanceof ExTreeNode && ((ExTreeNode) value).isCanSelected()))
            return false;
        
        if (!(value instanceof ExTreeNode && ((ExTreeNode) value).isMainClass()))
            return false;
        return true;
    }
    
    private void checkChangedReverse(DefaultMutableTreeNode node,boolean isIncludeSubNode) {
        if (!treeComponent.getCheckedNodes().contains(node)) {
            treeComponent.getCheckedNodes().remove(node);
            syncTable(node,false);
            if (isIncludeSubNode) {
                updateCheckboxesOnSubTree(node, false);
            }
        } else {
            treeComponent.getCheckedNodes().add((ExTreeNode) node);
            syncTable(node,true);
            if (isIncludeSubNode) {
                updateCheckboxesOnSubTree(node, true);
            }
        }
        ItemListener[] ll = treeComponent.getItemListeners();
        for (int i = 0; i < ll.length; i++)
            ll[i].itemStateChanged(new ItemEvent(
                    new UICheckBox(),
                    ItemEvent.ITEM_STATE_CHANGED,
                    node,
                    treeComponent.getCheckedNodes().contains(node) ? ItemEvent.SELECTED
                            : ItemEvent.DESELECTED));
        treeComponent.getTree().repaint();
    }
    
    
    private void syncTable(TreeNode node, boolean isAdd) {
        if (((DefaultMutableTreeNode) node).getUserObject() == null)
            return;
        if(!(((DefaultMutableTreeNode) node).getUserObject() instanceof Vector))
            return;
        Vector record = (Vector) ((DefaultMutableTreeNode) node).getUserObject();
        Vector vDataTable = new Vector();
        vDataTable.add(record);
        if (isAdd)
            treeComponent.getRefUI().addRecordToSelectedTable(vDataTable);
        else
            treeComponent.getRefUI().removeRecordFromSelectedTable(vDataTable);
    }

    private boolean areAllDeselectedCheckBox(TreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (treeComponent.getCheckedNodes().contains(node.getChildAt(i))
                    || node.getChildAt(i).isLeaf()
                    && !treeComponent.isShowCheckBoxes())
                return false;
        }
        return true;
    }
    
    private DefaultMutableTreeNode[] getAllTreeNode(DefaultMutableTreeNode node) {

        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
        Enumeration enumeration = node.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {

            list.add((DefaultMutableTreeNode) enumeration.nextElement());
        }

        return list.toArray((DefaultMutableTreeNode[]) Array.newInstance(
                DefaultMutableTreeNode.class, 0));
    }

    private void updateCheckboxesOnSubTree(TreeNode node, boolean sel) {
        if (sel) {
            if (isSelectedEnabled(node, node.isLeaf())) {
                treeComponent.getCheckedNodes().add((ExTreeNode) node);
                syncTable(node, true);
            }
        }
        
        else{
            treeComponent.getCheckedNodes().remove(node);
            syncTable(node,false);
        }
        for (int i = 0; i < node.getChildCount(); i++)
            updateCheckboxesOnSubTree(node.getChildAt(i), sel);
    }

    private UIPopupMenu getPopupMenu(boolean isLeaf) {
    
        popupMenu = new UIPopupMenu();
        if (treeComponent.isShowCheckBoxes()) {
            if (!isLeaf) {
                popupMenu.add(new SelectAction(SELECT_SUB_NODES));
                popupMenu.add(new SelectAction(CLEAR_SUB_NODES));
                popupMenu.addSeparator();
            }
            popupMenu.add(new SelectAction(SELECT_All));
            popupMenu.add(new SelectAction(SELECT_NONE));

            popupMenu.addSeparator();
            popupMenu.add(new SelectByLevelAction());
            popupMenu.addSeparator();
        }
        popupMenu.add(new ExpandAction());
        popupMenu.add(new CollapseAction());
        if (getTreeDepth() > 0) {

            popupMenu.addSeparator();

            JMenu menu = (JMenu) popupMenu.add(new JMenu(NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0000")/*按级次展开*/));

            populateLevelPopupMenu(menu);
        }
        
        return popupMenu;
    }
    
    private void populateLevelPopupMenu(JMenu menu){
        int depth = getTreeDepth();
        
        for (int i = 0; i < depth; i++) {
            menu.add(new ExpandByLevelAction(i + 1));
        }
    }
    
    private int getTreeDepth() {
        return treeComponent.getRootNode().getDepth();
//      DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeComponent
//              .getRefTreeModel().getTreeModel().getRoot();
//      return rootNode.getDepth();
    }
    
    class ExpandAction extends AbstractAction{
        private static final long serialVersionUID = 1L;

        public ExpandAction() {
            super();
            putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0001")/*全部展开*/);
            putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0001")/*全部展开*/);
            putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0001")/*全部展开*/);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < treeComponent.getTree().getRowCount(); i++) {
                treeComponent.getTree().expandPath(
                        treeComponent.getTree().getPathForRow(i));
            }
        }   
    }
    
    class ExpandByLevelAction extends AbstractAction{
        private static final long serialVersionUID = 1L;
        private int level = 1;
        private String toolTipPre = NCLangRes.getInstance().getStrByID("ref","RefButtonPanelFactory-000011")/* 展开到 */;
        private String toolTipSuf = NCLangRes.getInstance().getStrByID("ref","RefButtonPanelFactory-000012")/* 级 */;
        public ExpandByLevelAction(int level) {
            super();
            this.level = level;
            putValue(CODE, level);
            putValue(AbstractAction.NAME, toolTipPre + level + toolTipSuf);
            putValue(SHORT_DESCRIPTION, toolTipPre + level + toolTipSuf);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //if (level < treeComponent.getRefTreeModel().getExpandLevel()) {
                treeComponent.collapseTreePath();
            //}
            treeComponent.getRefModel().setTreeExpandStrategy(IRefTreeExpandStrategy.LEVEL);
            treeComponent.getRefModel().setExpandLevel(level);
            treeComponent.expandTreePath();
        }   
    }
    
    class CollapseAction extends AbstractAction{
        private static final long serialVersionUID = 1L;
        
        public CollapseAction() {
            super();
            putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0002")/*全部收起*/);
            putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0002")/*全部收起*/);
            putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0002")/*全部收起*/);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = treeComponent.getTree().getRowCount() - 1; i >= 0; i--) {
                treeComponent.getTree().collapsePath(treeComponent.getTree().getPathForRow(i));
            }
        }
    }
    
    class SelectAction extends AbstractAction{
        private static final long serialVersionUID = 1L;
        protected String type;
        
        public SelectAction(String type){
            this.type = type;
            if(type.equalsIgnoreCase(SELECT_All)){
                putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "UPPref-000354")/*全选*/);
                putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "UPPref-000354")/*全选*/);
                putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "UPPref-000354")/*全选*/);
            }else if(type.equalsIgnoreCase(SELECT_NONE)){
                putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0003")/*全消*/);
                putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0003")/*全消*/);
                putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0003")/*全消*/);
            }else if(type.equalsIgnoreCase(SELECT_SUB_NODES)){
                putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0004")/*勾选下级*/);
                putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0004")/*勾选下级*/);
                putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0004")/*勾选下级*/);
            }else if(type.equalsIgnoreCase(CLEAR_SUB_NODES)){
                putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0005")/*取消勾选下级*/);
                putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0005")/*取消勾选下级*/);
                putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0005")/*取消勾选下级*/);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (type.equalsIgnoreCase(SELECT_All)) {
                selectAllNodes();
            } else if (type.equalsIgnoreCase(SELECT_NONE)) {
                clearAllNodes(treeComponent.getRootNode());
            } else if (type.equalsIgnoreCase(SELECT_SUB_NODES)) {
                selectSubNodes();
            } else if (type.equalsIgnoreCase(CLEAR_SUB_NODES)) {
                clearSubNodes();
            }
        }   
        
        private void selectAllNodes(){
            DefaultMutableTreeNode node = treeComponent.getRootNode();
            treeComponent.getCheckedNodes().clear();
            checkChanged(node, true);
        }
        
        private void clearAllNodes(DefaultMutableTreeNode parent){
            DefaultMutableTreeNode[] nodes = getAllTreeNode(parent);
            for (DefaultMutableTreeNode node : nodes) {
                if(!treeComponent.getCheckedNodes().contains(node)){
                    treeComponent.getCheckedNodes().add((ExTreeNode) node);
                }
                checkChanged(node, false);
            }

        }
        
        private void selectSubNodes() {
            DefaultMutableTreeNode node = treeComponent.getSelectedNode();
            if (node != null && !node.isLeaf()) {
                treeComponent.getCheckedNodes().remove(node);
                checkChanged(node, true);
            }
        }
        
        private void clearSubNodes(){
            DefaultMutableTreeNode node = treeComponent.getSelectedNode();
            if (node != null && !node.isLeaf()) {
                clearAllNodes(node);
            }
        }
    }
    
    class SelectByLevelAction extends AbstractAction{
        private static final long serialVersionUID = 1L;
        private RefLevelSelectedDlg levelSelectedDlg = null;
        
        public SelectByLevelAction() {
            super();
            putValue(CODE, NCLangRes.getInstance().getStrByID("ref", "UPPref-000537")/*按级次选择*/);
            putValue(AbstractAction.NAME, NCLangRes.getInstance().getStrByID("ref", "RefTreeNodeRendererListener-0006")/*按级次选择...*/);
            putValue(SHORT_DESCRIPTION, NCLangRes.getInstance().getStrByID("ref", "UPPref-000537")/*按级次选择*/);

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            
            getLevelSelectedDlg().showModal();
            if (getLevelSelectedDlg().getResult() == UIDialog.ID_OK) {
                DefaultMutableTreeNode[] nodes = treeComponent
                        .getLevelTreeNodes(new DefaultMutableTreeNode[] { treeComponent.getRootNode() },
                                getLevelSelectedDlg().getBeginLevelValue(),getLevelSelectedDlg().getEndLevelValue());
                //treeComponent.getCheckedNodes().clear();
                for (DefaultMutableTreeNode node : nodes) {
                    if (!treeComponent.getCheckedNodes().contains(node) && ((ExTreeNode)node).isCanSelected())
                        checkChanged(node, treeComponent.isIncludeSubNode());
                }
            }
        }
        
        private RefLevelSelectedDlg getLevelSelectedDlg() {
            if (levelSelectedDlg == null) {
                levelSelectedDlg = new RefLevelSelectedDlg(treeComponent,treeComponent.getRootNode().getDepth());
            }
            return levelSelectedDlg;
        }
    }
}
