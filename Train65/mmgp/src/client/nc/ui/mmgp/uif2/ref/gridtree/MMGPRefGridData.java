package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.FocusManager;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefDataComponent;
import nc.ui.bd.ref.IRefGridDataState;
import nc.ui.bd.ref.IRefUI60;
import nc.ui.bd.ref.RefNCDataTableModle;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.bd.ref.event.NCEventObject;
import nc.ui.bd.ref.event.RefEventConstant;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.bd.ref.RefcolumnVO;
import nc.vo.pub.BusinessException;

/**
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class MMGPRefGridData extends MMGPRefTablePane implements IRefDataComponent {

    public static HashSet<AWTKeyStroke> focusTraversalKeyHashSet = new HashSet<AWTKeyStroke>();

    private static final long serialVersionUID = 1L;

    Vector vecAllColumnNames = null;

    String[] columnNames = null;

    AbstractRefModel refModel = null;

    IRefUI60 refDialog = null;

    Vector vDataAll = null;

    private boolean isShowCheckBox = true;

    protected Point translate = new Point(0, 0);

    // 过滤数据缓存
    private Hashtable htLocate = new Hashtable();

    private MMGPRefTableCheckboxManager manager = null;

    private ArrayList<Integer> selectedRow = new ArrayList<Integer>();

    private ArrayList<Integer> lastSelectedRows = new ArrayList<Integer>();

    private UITable headerColumn = null;

    private IRefGridDataState gridDataState = null;

    private TableCellRenderer colHeaderTableCellRenderer = null;

    public IEventListener delSelectedRowHandler = new IEventListener() {
        public void invoke(Object sender,
                           NCEventObject evt) {
            if (evt.getProperty(RefEventConstant.SELECTED_PKS) != null) {
                String[] pks = (String[]) evt.getProperty(RefEventConstant.SELECTED_PKS);
                int[] indexs = getRecordIndexes(pks);
                manager.setNotSelected(indexs);
            }
        }
    };

    public IEventListener rowCheckedChangedHandler = new IEventListener() {
        public void invoke(Object sender,
                           NCEventObject evt) {
            if (evt.getProperty(RefEventConstant.SELECTED_ROWS) != null) {
                Vector vec = getSelectData((int[]) evt.getProperty("selectedRows"));
                eventSource.fireEvent(new NCEventObject(
                    RefEventConstant.DATAROW_CHECKED_CHANGED,
                    RefEventConstant.SELECTED_DATA,
                    vec,
                    RefEventConstant.IS_ADD_ROWS,
                    evt.getProperty(RefEventConstant.IS_ADD_ROWS)));
                getTable().repaint();
                headerColumn.repaint();
            }
        }
    };

    public MMGPRefGridData(IRefUI60 refDialog,
                           boolean isShowCheckBox) {
        this.refDialog = refDialog;
        this.isShowCheckBox = isShowCheckBox;
        initialize();
    }

    class GridDataTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private Color unselectedBackground = Color.WHITE;

        private Color selectedBackground = new Color(229, 243, 252);

        private Color focusCelldBackground = new Color(0xfff1e4);// new Color(255, 241, 241);

        private boolean isShowCheckBox = false;

        public GridDataTableCellRenderer(boolean isShowCheckBox) {
            this.isShowCheckBox = isShowCheckBox;
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            boolean leadColumn = false;
            if (table.getColumnModel().getSelectionModel().getLeadSelectionIndex() == column) {
                leadColumn = true;
            }
            if (isShowCheckBox) {
                if (manager != null && manager.getTableCheck()[row] == 0) {
                    setBackground(unselectedBackground);
                } else {
                    setBackground(selectedBackground);
                }
            }

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isShowCheckBox) {
                if (isSelected && leadColumn && table.hasFocus()) {
                    setBackground(focusCelldBackground);
                } else if (isSelected && manager != null && manager.getTableCheck()[row] == 1) {
                    setBackground(selectedBackground);
                }
            } else {
                if (isSelected && hasFocus) {
                    setBackground(focusCelldBackground);
                } else
                    setBackground(unselectedBackground);
            }

            return c;
        }

    }

    class GridDataRowHeaderCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private MMGPRefTableCheckboxManager manager = null;

        private Color unselectedBackground = new Color(242, 242, 242);

        private Color selectedBackground = new Color(229, 243, 252);

        private Color focusCelldBackground = new Color(0xfff1e4);// new Color(245, 229, 230);

        private JTable dataTable = null;

        private boolean isShowCheckBox = false;

        public GridDataRowHeaderCellRenderer(MMGPRefTableCheckboxManager manager,
                                             JTable table,
                                             boolean isShowCheckBox) {
            this.manager = manager;
            dataTable = table;
            this.isShowCheckBox = isShowCheckBox;
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            int leadSelectionIndex = dataTable.getSelectionModel().getLeadSelectionIndex();
            int selectedRowCount = dataTable.getSelectedRowCount();

            int columnSelIndex = getTable().getColumnModel().getSelectionModel().getLeadSelectionIndex();

            if (isShowCheckBox) {

                if (selectedRowCount > 1 && leadSelectionIndex == row) {
                    setBackground(focusCelldBackground);
                } else {
                    if (manager != null && manager.getTableCheck()[row] == 0) {
                        setBackground(unselectedBackground);
                    } else {
                        setBackground(selectedBackground);
                    }
                }
            }

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isShowCheckBox) {
                if (selectedRowCount > 0 && isSelectedRow(row) && dataTable.hasFocus()) {
                    setBackground(focusCelldBackground);
                } else {
                    if (manager != null && manager.getTableCheck()[row] == 0) {
                        setBackground(unselectedBackground);
                    } else {
                        setBackground(selectedBackground);
                    }
                }
            } else {
                if (leadSelectionIndex == row && dataTable.hasFocus()) setBackground(focusCelldBackground);
                else
                    setBackground(unselectedBackground);
            }

            return c;
        }

        private boolean isSelectedRow(int row) {
            int[] rows = dataTable.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                if (rows[i] == row) return true;
            }
            return false;
        }
    }

    class GridDataColumnHeaderCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private Color focusCelldBackground = new Color(0xfff1e4);// new Color(255, 241, 241);

        private JTable dataTable = null;

        public GridDataColumnHeaderCellRenderer(JTable table) {

            dataTable = table;
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            boolean leadColumn = false;

            if (table.getColumnModel().getSelectionModel().getLeadSelectionIndex() == column) {
                leadColumn = true;
            }

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (leadColumn && table.hasFocus() && table.getRowCount() > 0) {
                setBackground(focusCelldBackground);
                setBorder(null);
            } else {
                setBackground(table.getTableHeader().getBackground());
                setBorder(null);
            }

            return c;
        }

    }

    private void initialize() {

        getTable().setFocusTraversalKeysEnabled(false);
        // // 关闭。否则会吃掉TabbedPane Ctrl+Tab快捷键
        getTable().setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, focusTraversalKeyHashSet);
        getTable().setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, focusTraversalKeyHashSet);
        registerKey();

        getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) e.getSource();
                if (selectionModel.getMaxSelectionIndex() - selectionModel.getMinSelectionIndex() > 0) {
                    if (isShowCheckBox) {
                        if (selectionModel.getAnchorSelectionIndex() != -1) {
                            // manager.clickTableRow(selectionModel.getAnchorSelectionIndex());
                        }
                    }
                }
                if (MMGPRefGridData.this.getName() != null && MMGPRefGridData.this.getName().equals("commonData")) {
                    if (selectionModel.getMaxSelectionIndex() == -1 && selectionModel.getMinSelectionIndex() == -1) {
                        ((MMGPTreeGridRefUI) getRefDialog()).getUIButtonOK().setEnabled(false);
                    } else {
                        ((MMGPTreeGridRefUI) getRefDialog()).getUIButtonOK().setEnabled(true);
                    }
                }
                headerColumn.repaint();

            }
        });
        getTable().addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                // headerColumn.repaint();
                // getTable().getTableHeader().repaint();
                // getTable().repaint();
            }

            @Override
            public void focusGained(FocusEvent e) {
                // 以下处理解决需求：grid有焦点行时，就算不勾选记录点确定也表示选中该记录
                // 带来的问题是当焦点点到别处时，Grid行依然被选（虽然没有了焦点状态），点确定是会选中该行
                if (getRefDialog() instanceof MMGPTreeGridRefUI) {
                    if (!((MMGPTreeGridRefUI) getRefDialog()).isMultiSelectedRef()) {
                        if (((MMGPTreeGridRefUI) getRefDialog()).getUISelectedPKs() == null)
                            ((MMGPTreeGridRefUI) getRefDialog()).getUIButtonOK().setEnabled(false);
                    }
                }
                if (MMGPRefGridData.this.getName() != null && MMGPRefGridData.this.getName().equals("commonData")) {
                    DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) getTable().getSelectionModel();
                    if (selectionModel.getMaxSelectionIndex() == -1 && selectionModel.getMinSelectionIndex() == -1) {
                        ((MMGPTreeGridRefUI) getRefDialog()).getUIButtonOK().setEnabled(false);
                    } else {
                        ((MMGPTreeGridRefUI) getRefDialog()).getUIButtonOK().setEnabled(true);
                    }
                }
                if (getTable().getRowCount() > 0
                    && (getTable().getSelectedRow() == -1 || getTable().getSelectedColumn() == -1)
                    || getTable().getSelectedRow() >= getTable().getRowCount()) {
                    getTable().getSelectionModel().setSelectionInterval(0, 0);
                    getTable().getColumnModel().getSelectionModel().setSelectionInterval(0, 0);
                    headerColumn.repaint();
                    getTable().getTableHeader().repaint();
                    return;
                }
            }
        });

        getTable().setSelectionBackground(Color.WHITE);
        if (isShowCheckBox) {
            getTable().setSortEnabled(false);
            getTable().removeSortListener();
            getTable().addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    if (keyCode == KeyEvent.VK_TAB) {
                        if (e.isShiftDown()) {
                            if (getTable().getSelectedRow() == 0 && getTable().getSelectedColumn() == 0) {
                                focusPreviousComponent();
                                return;
                            }
                        } else {
                            if (getTable().getSelectedRow() == (getTable().getRowCount() - 1)
                                && getTable().getSelectedColumn() == (getTable().getColumnCount() - 1)) {
                                focusNextComponent();
                                return;
                            }
                        }
                    }
                    if (keyCode == KeyEvent.VK_F6) {
                        if (e.isShiftDown()) {
                            focusPreviousComponent();
                        } else {
                            focusNextComponent();
                        }
                        return;
                    }
                    if (!isShowCheckBox) return;
                    if (e.isShiftDown() && keyCode == KeyEvent.VK_SHIFT) {
                        getLastSelectedRows();
                        return;
                    }

                    // shift+上下键
                    if (e.isShiftDown() && (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP)) {
                        int[] rows = ((UITable) e.getSource()).getSelectedRows();
                        doMultiSelection(rows);
                        return;
                    }

                    // 上下键(按着shift+上下键不松手)
                    int rowIndex = ((UITable) e.getSource()).getSelectionModel().getLeadSelectionIndex();
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        manager.processKeyUpOrDown(true, rowIndex);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        manager.processKeyUpOrDown(false, rowIndex);
                    }

                }

                public void keyReleased(KeyEvent e) {
                    getTable().getTableHeader().repaint();
                    int[] rows = ((UITable) e.getSource()).getSelectedRows();
                    if (e.getKeyChar() == ' ') {
                        if (rows.length > 0) {
                            for (int i = 0; i < rows.length; i++) {
                                manager.clickCheckBoxAndCheckedChanged(rows[i]);
                                if (manager.getTableCheckedCount() > 0) manager.setMultiSelected(true);
                            }
                        }
                    }
                    // shift+上下键(按着shift+上下键 (松手))
                    if (e.isShiftDown() && (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP)) {
                        // int[] rows = ((UITable) e.getSource()).getSelectedRows();
                        doMultiSelection(rows);
                        return;
                    }

                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) reset();
                }
            });
        } else {
            getTable().addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // TODO Auto-generated method stub
                    getTable().getTableHeader().repaint();
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        if (e.isShiftDown()) {
                            if (getTable().getSelectedRow() == 0 && getTable().getSelectedColumn() == 0) {
                                focusPreviousComponent();
                                return;
                            }
                        } else {
                            if (getTable().getSelectedRow() == (getTable().getRowCount() - 1)
                                && getTable().getSelectedColumn() == (getTable().getColumnCount() - 1)) {
                                focusNextComponent();
                                return;
                            }
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_F6) {
                        if (e.isShiftDown()) {
                            focusPreviousComponent();
                        } else {
                            focusNextComponent();
                        }
                        return;
                    }
                }
            });
        }
    }

    private void focusNextComponent() {
        FocusManager focusManager = FocusManager.getCurrentManager();
        focusManager.focusNextComponent();
    }

    private void focusPreviousComponent() {
        FocusManager focusManager = FocusManager.getCurrentManager();
        focusManager.focusPreviousComponent();
    }

    public void setData(Vector data) {
        setVDataAll(data);
        resetTableData(data);
        for (int i = 0; i < getTable().getColumnModel().getColumnCount(); i++) {
            setHeaderRendererForeground(getTable().getColumnModel().getColumn(i));
        }
    }

    public void setDataVector(int baseNumber,
                              Vector data) {
        super.setDataVector(baseNumber, data);
        setVDataAll(data);

        for (int i = 0; i < getTable().getColumnModel().getColumnCount(); i++) {
            setHeaderRendererForeground(getTable().getColumnModel().getColumn(i));
        }
    }

    public void setCheckBoxChecked(Object[] pks) {
        if (pks == null) return;
        String[] strPKs = new String[pks.length];
        for (int i = 0; i < pks.length; i++) {
            strPKs[i] = (String) pks[i];
        }
        int[] indexs = getRecordIndexes(strPKs);
        manager.setSelected(indexs);
        if (indexs != null && indexs.length == 1) {// 如果打开界面时，上次选中的是一条记录，则依然是单选状态
            manager.setMultiSelected(false);
        }
    }

    public void setCheckBoxNoChecked(String[] pks) {
        if (pks == null) return;
        int[] indexs = getRecordIndexes(pks);
        manager.setNotSelected(indexs);
    }

    public void setRefModel(AbstractRefModel refModel) {
        this.refModel = refModel;
        initTableModel();
    }

    public void setRefDialog(MMGPTreeGridRefUI refDialog) {
        this.refDialog = refDialog;

    }

    public HashMap<String, Integer> getTableColumnsWidth() {
        int colCount = getTable().getColumnModel().getColumnCount();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < colCount; i++) {
            String columnName = getTable().getColumnName(i);
            int columnWidth = getTable().getColumnModel().getColumn(i).getWidth();
            map.put(columnName, columnWidth);
        }
        return map;
    }

    public void setTableColumnsWidth(HashMap<String, Integer> map) {
        if (map == null) return;
        int colCount = getTable().getColumnModel().getColumnCount();
        for (int i = 0; i < colCount; i++) {
            String columnName = getTable().getColumnName(i);
            if (map.get(columnName) != null) {
                int width = (int) map.get(columnName);
                getTable().getColumnModel().getColumn(i).setPreferredWidth(width);
            }
        }
        getTable().doLayout();
    }

    public void initRowHeader() {

        NCTableModel model = (NCTableModel) getTable().getModel();

        if (model == null) {
            return;
        }
        if (isShowCheckBox) {
            getRefNCtableModel().setShowCheckBoxColumn(true);
        }

        headerColumn = new UITable(refNCtableModel);
        headerColumn.setFocusable(false);

        headerColumn.createDefaultColumnsFromModel();

        headerColumn.setBackground(IRefConst.REFTABLEROWHEADER);
        headerColumn.setColumnSelectionAllowed(false);
        headerColumn.setCellSelectionEnabled(false);

        if (isShowCheckBox) {
            manager = new MMGPRefTableCheckboxManager(headerColumn);
            manager.addListener(RefEventConstant.DATAROW_CHECKED_CHANGED, rowCheckedChangedHandler);
        }

        headerColumn.setDefaultRenderer(Object.class, new GridDataRowHeaderCellRenderer(
            manager,
            getTable(),
            isShowCheckBox));

        JViewport jv = new JViewport();
        jv.setView(headerColumn);
        setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn.getTableHeader());

        if (isShowCheckBox) jv.setPreferredSize(new Dimension(ROWHEADERWIDTH + 27, 0));
        else
            jv.setPreferredSize(new Dimension(ROWHEADERWIDTH, 0));

        setRowHeader(jv);

        if (isShowCheckBox) initConn(headerColumn);
    }

    private void setHeaderRendererForeground(TableColumn tc) {
        TableCellRenderer renderer = tc.getHeaderRenderer();
        if (renderer == null || !(renderer instanceof GridDataColumnHeaderCellRenderer)) {
            renderer = getDefaultTableCellRenderer();

            tc.setHeaderRenderer(renderer);
        }
    }

    private TableCellRenderer getDefaultTableCellRenderer() {
        if (colHeaderTableCellRenderer == null) colHeaderTableCellRenderer = creatDefaultTableCellRenderer();

        return colHeaderTableCellRenderer;
    }

    protected TableCellRenderer creatDefaultTableCellRenderer() {
        return new GridDataColumnHeaderCellRenderer(getTable());
    }

    private void initConn(final JTable rowTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int columnIndex = rowTable.columnAtPoint(e.getPoint());
                if (columnIndex == 1) {
                    Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
                    if (cursor != null) {
                        rowTable.setCursor(cursor);
                        e.consume();
                    }
                } else {
                    rowTable.setCursor(null);
                }
            }
        };
        rowTable.addMouseListener(mouseAdapter);
        rowTable.addMouseMotionListener(mouseAdapter);
    }

    private boolean isMultiSelectedRef() {
        return false;

    }

    private boolean isMultiOrgSelected() {
        return getRefDialog().getRefUIConfig().isMultiCorpRef()
            && getRefDialog().getRefUIConfig().isMultiOrgSelected();
    }

    private void initTableModel() {

        initConn();
        if (isMultiSelectedRef()) {
            getTable().setSelectionMode(javax.swing.DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            getTable().setSelectionMode(javax.swing.DefaultListSelectionModel.SINGLE_SELECTION);
        }

        nc.ui.pub.beans.table.NCTableModel tm =
                new RefNCDataTableModle(new Vector(), RefPubUtil.getAllColumnNames(getRefModel()));
        tm.setAndEditable(true);
        getTable().setModel(tm);
        // getTable()
        // .setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        MMGPRefTableColumnModel cm = new MMGPRefTableColumnModel(getRefModel());
        getTable().setColumnModel(cm);
        // 动态列信息
        cm.setDynamicColumnNames(getRefModel().getDynamicFieldNames());
        getTable().createDefaultColumnsFromModel();
        initRowHeader();
        // initTableModel(new Vector(),
        // RefUtil.getAllColumnNames(getRefModel()));
        setNewColumnSequence();
        // setShowIndexToModel();

        getTable().setDefaultRenderer(Object.class, new GridDataTableCellRenderer(isShowCheckBox));
        if (isMultiSelectedRef()) {
            Vector v = getRefModel().getSelectedData();
            if (v != null && v.size() > 1) {
                manager.setMultiSelected(true);
            } else {
                manager.setMultiSelected(false);
            }
        }
    }

    private void getLastSelectedRows() {
        if (!isShowCheckBox) return;
        lastSelectedRows.clear();
        Set<Integer> tempSet = manager.getSelectedIndexSet();
        Iterator<Integer> list = tempSet.iterator();
        while (list.hasNext()) {
            lastSelectedRows.add(list.next());
        }
    }

    private void doMultiSelectionWithCtrlDown(int[] rows) {
        if (!isShowCheckBox) return;

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
                Iterator<Integer> list = selectedRow.iterator();
                boolean include = false;
                while (list.hasNext()) {
                    Integer curRow = list.next();
                    if (rows[i] == curRow.intValue()) {
                        include = true;
                        break;
                    }
                }
                if (!include) manager.clickTableRowWithCtrlDown(rows[i]);
            }

        } else if (rows.length == 1) {
            manager.clickTableRowWithCtrlDown(rows[0]);
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
                if (!include) manager.clickTableRowWithCtrlDown(clearedRow);
            }
        }

        selectedRow.clear();
        for (int i = 0; i < rows.length; i++) {
            selectedRow.add(rows[i]);
        }

    }

    private void doMultiSelection(int[] rows) {
        if (!isShowCheckBox) return;
        Vector<Integer> clearedRows = new Vector<Integer>();
        // System.out.println(rows.length);
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
                manager.clickTableRow(rows[i], true);
            }
        } else if (rows.length == 1) {
            processMouseClicked();
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
                if (!include) {
                    manager.clickCheckBoxAndCheckedChanged(clearedRow);
                }
            }
        }

        selectedRow.clear();
        for (int i = 0; i < rows.length; i++) {
            selectedRow.add(rows[i]);
        }
    }

    private void reset() {
        if (!isShowCheckBox) return;
        selectedRow.clear();
    }

    /**
     * <p>
     * <strong>最后修改人：sxj</strong>
     * <p>
     * <strong>最后修改日期：2006-5-9</strong>
     * <p>
     * 
     * @param
     * @return void
     * @exception BusinessException
     * @since NC5.0
     */
    private void initConn() {
        // getTable().addSortListener();
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (isShowCheckBox) {
                    int[] rows = getTable().getSelectedRows();
                    if (e.isShiftDown()) {
                        doMultiSelection(rows);
                        return;
                    }
                    if (e.isControlDown()) {
                        manager.clickTableRowWithCtrlDown(getTable().getSelectionModel().getLeadSelectionIndex());
                        e.consume();
                        return;
                    }
                    processMouseClicked();
                }

                if (e.getSource() == getTable()
                    && e.getClickCount() == 2
                    && MMGPRefGridData.this.getName() != null
                    && MMGPRefGridData.this.getName().equals("commonData")) processMouseDoubleClicked(e);
            }

            public void mouseMoved(MouseEvent e) {
                Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
                if (cursor != null) {
                    getTable().setCursor(cursor);
                    e.consume();
                }
            }

            public void mousePressed(MouseEvent e) {
                getTable().getTableHeader().repaint();
                if (!isShowCheckBox) return;
                getLastSelectedRows();
            }

            public void mouseDragged(MouseEvent e) {
                if (!isShowCheckBox) return;
                int[] rows = getTable().getSelectedRows();

                // if (e.isControlDown()) {
                // doMultiSelectionWithCtrlDown(rows);
                // e.consume();
                // return;
                // }
                doMultiSelection(rows);
                e.consume();

            }

            public void mouseReleased(MouseEvent e) {
                reset();
            }

        };
        getTable().addMouseListener(mouseAdapter);
        getTable().addMouseMotionListener(mouseAdapter);
    }

    /**
     * 表格行双击事件响应
     */
    private void processMouseDoubleClicked(java.awt.event.MouseEvent mouseEvent) {
        if (getTable().getSelectedRow() != -1) {
            if (isShowCheckBox) {
                // if (manager.isMultiSelected() && manager.getTableCheckedCount() > 0 &&
                // !manager.isChecked(getTable().getSelectedRow())) {
                if (!manager.isChecked(getTable().getSelectedRow())) {
                    manager.clickCheckBoxAndCheckedChanged(getTable().getSelectedRow());
                }
            }
            getRefDialog().onButtonOK();
        }
        return;
    }

    private void processMouseClicked() {
        if (isShowCheckBox) {
            if (getTable().getSelectedRow() != -1) {
                if (getGridDataState() != null && !manager.isMultiSelected()) {
                    manager.setMultiSelected(getGridDataState().isMultiSelected());
                }
                manager.clickTableRow(getTable().getSelectedRow(), false);
            }
        }
    }

    /**
     * @return 返回 refModel。
     */
    public AbstractRefModel getRefModel() {
        return refModel;
    }

    /*
     * 设置定位Combox,表的栏目的显示顺序
     */
    public void setNewColumnSequence() {
        RefcolumnVO[] vos = getRefDialog().getRefcolumnVOs();
        // setComboBoxData(colNames);
        getTableColumnModel().setColumnVOs(vos);
        getTableColumnModel().adjustColumnShowSequence();
        setTableColumn();
    }

    private MMGPRefTableColumnModel getTableColumnModel() {
        return (MMGPRefTableColumnModel) getTable().getColumnModel();
    }

    private void setTableColumn() {

        if (getTable().getColumnCount() < 4) {
            getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else {

            int[] colWidth = new int[getTable().getColumnCount()];

            int tableWidth = setSpecialTableColumnWidth(colWidth);
            fillBlankSpace(tableWidth, colWidth);

            getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            getTable().setColumnWidth(colWidth);
        }
        getTable().sizeColumnsToFit(-1);
    }

    /**
     * <p>
     * <strong>最后修改人：sxj</strong>
     * <p>
     * <strong>最后修改日期：2006-8-24</strong>
     * <p>
     * 
     * @param
     * @return int
     * @exception BusinessException
     * @since NC5.0
     */
    private int setSpecialTableColumnWidth(int[] colWidth) {

        String nameField = getRefModel().getFieldShowName(getRefModel().getRefNameField());
        String columnName = "";
        String name_lang = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001155");
        /** @res "名称" */
        int tableWidth = 0;
        for (int i = 0; i < getTable().getColumnCount(); i++) {
            // 加大名称列默认长度
            // sxj 2003-02-24
            // model 中的nameField也加大 2008-12-03
            columnName = getTable().getColumnName(i).toString();
            if (columnName.indexOf(name_lang) > -1 || columnName.equals(nameField)) {
                colWidth[i] = 150;// 200;

            } else {
                colWidth[i] = 80;// 112;
            }

            tableWidth += colWidth[i];

        }
        return tableWidth;
    }

    /**
     * <p>
     * <strong>最后修改人：sxj</strong>
     * <p>
     * <strong>最后修改日期：2006-8-24</strong>
     * <p>
     * 
     * @param
     * @return void
     * @exception BusinessException
     * @since NC5.0
     */
    private void fillBlankSpace(int tableWidth,
                                int[] colWidth) {
        // 填充空白位置
        int dialogWidth = getRefDialog().getWidth();
        int talbleTotalWidth = tableWidth + ROWHEADERWIDTH + 10;
        if (talbleTotalWidth < dialogWidth) {

            for (int i = 0; i < getTable().getColumnCount(); i++) {
                colWidth[i] += (dialogWidth - talbleTotalWidth) / getTable().getColumnCount();

            }
        }
    }

    private void resetTableData(Vector data) {

        setDataVector(data);
        setTableColumn();
        getTable().clearSelection();
        repaint();

    }

    /**
     * @return 返回 refDialog。
     */
    private IRefUI60 getRefDialog() {
        return refDialog;
    }

    public void blurInputValue(String columnName,
                               String strInput) {
        if (getRefModel().isFilterByLucene() && !nc.vo.jcom.lang.StringUtil.isEmpty(strInput)) blurInputValueByServer(strInput);
        else {
            blurInputValueByClient("");
            blurInputValueByClient(strInput);
        }

        getRefModel().setBlur(false);
    }

    private void blurInputValueByServer(String strInput) {
        Vector vTablePKs = getTablePKs();
        if (vTablePKs == null) return;
        getRefModel().setBlurValue(strInput);
        Vector matchedPks = getRefModel().getMatchPksByFilter(vTablePKs);
        getRefModel().setBlurValue(null);

        Vector matchVec = new Vector();
        Vector modelVec = getRefModel().getData();
        for (int i = 0; i < modelVec.size(); i++) {
            Vector vecRecord = (Vector) modelVec.get(i);
            Object pk = vecRecord.get(getPKIndex());
            if (matchedPks.contains(pk)) {
                matchVec.add(vecRecord);
            }
        }
        setData(matchVec);
    }

    private int getPKIndex() {

        return getRefModel().getFieldIndex(getRefModel().getPkFieldCode());
    }

    private Vector getTablePKs() {
        Object[] pks = getRefModel().getValues(getRefModel().getPkFieldCode(), getRefModel().getData());
        if (pks == null || !(pks.length > 0)) return null;
        Vector v = new Vector();
        for (int i = 0; i < pks.length; i++) {
            v.add(pks[i]);
        }
        return v;
    }

    private void blurInputValueByClient(String strInput) {
        if (strInput == null || strInput.trim().length() == 0) {
            htLocate.clear();
            // 原来的数据
            setData(((MMGPTreeGridRefModel) getRefModel()).getTableData());
            return;
        }
        // 表格中的列位置

        Vector matchVec = new Vector();
        // 从缓存取
        if (htLocate.get(strInput) != null) {
            matchVec = (Vector) htLocate.get(strInput);
        } else {
            NCTableModel tm = ((NCTableModel) getTable().getModel());
            Vector vData = tm.getDataVector();
            if (vData == null || vData.size() == 0) {
                return;
            }
            // 比较
            for (int i = 0; i < getTable().getRowCount(); i++) {

                for (int j = 0; j < getTable().getColumnCount(); j++) {
                    Object o = getTable().getValueAt(i, j);
                    String strCell = (o == null ? "" : o.toString().trim());
                    if (isIndexMatch(strInput, strCell)
                        || (refModel.isSupportPY() && RefPubUtil.isMatch(strInput, strCell))) {
                        Vector v = (Vector) vData.elementAt(i);
                        matchVec.add(v);
                        break;
                    }
                }

            }
            htLocate.put(strInput, matchVec);
        }

        if (matchVec != null) {
            setData(matchVec);
        }
    }

    private boolean isIndexMatch(String strInput,
                                 String strCell) {
        return RefPubUtil.toLowerCaseStr(getRefModel(), strCell).indexOf(
            RefPubUtil.toLowerCaseStr(getRefModel(), strInput)) >= 0;
    }

    /**
	 * 
	 */
    private int getLocateColumn(String columnName) {
        int col = -1;

        int colCount = getTable().getModel().getColumnCount();
        String colName;
        for (int i = 0; i < colCount; i++) {
            colName = getTable().getModel().getColumnName(i);
            if (colName.equals(columnName)) {
                col = i;
                break;
            }
        }

        return col;
    }

    /**
     * 按确认按钮，得到数据
     */
    public Vector getSelectData() {
        if (getTable().getRowCount() == 0) return null;
        int[] selectedRows = getTable().getSelectedRows();
        return getSelectData(selectedRows);
    }

    private Vector getSelectData(int[] selectedRows) {
        Vector vSelectedData = null;
        if (selectedRows != null && selectedRows.length > 0) {
            vSelectedData = new Vector();
            for (int i = 0; i < selectedRows.length; i++) {
                Vector vRecord = new Vector();
                for (int j = 0; j < getTable().getModel().getColumnCount(); j++) {
                    vRecord.addElement(getTable().getModel().getValueAt(selectedRows[i], j));
                }
                vSelectedData.addElement((Vector) vRecord);
            }
        }
        return vSelectedData;
    }

    private int[] getRecordIndexes(String[] pks) {
        int[] indexes = null;

        String[] selectedDatas = pks;
        // Vector vDataAll = getRefModel().getData();
        if (selectedDatas == null || vDataAll == null || vDataAll.size() == 0) {
            return null;
        }
        // 行号和记录的对应。
        HashMap hm = new HashMap();
        String pk = null;
        for (int i = 0; i < getVDataAll().size(); i++) {
            Vector record = (Vector) getVDataAll().get(i);
            String pkField = getRefModel().getPkFieldCode();
            if (pkField != null) {
                int pkIndex = getRefModel().getFieldIndex(pkField);
                if (pkIndex >= 0 && pkIndex < record.size()) {
                    pk = record.get(pkIndex).toString();
                    hm.put(pk, Integer.valueOf(i));
                }

            }
        }
        // 找到匹配数据
        ArrayList al = new ArrayList();
        if (selectedDatas != null && selectedDatas.length > 0) {

            for (int i = 0; i < selectedDatas.length; i++) {
                Integer rowNumber = null;
                if (selectedDatas[i] != null) {
                    rowNumber = ((Integer) hm.get(selectedDatas[i]));
                }
                if (rowNumber != null) {
                    al.add(rowNumber);
                }

            }
            if (al.size() > 0) {
                indexes = new int[al.size()];
                for (int i = 0; i < indexes.length; i++) {
                    indexes[i] = ((Integer) al.get(i)).intValue();
                }
            }
        }
        return indexes;
    }

    public void setMatchedPKs(String[] pks) {
        if (pks == null) return;

        if (isShowCheckBox) {
            setCheckBoxChecked(pks);
        }

        setTableSelect(pks);

    }

    public void setMatchedPKs(String[] pks,
                              boolean isSetSelect) {
        if (pks == null) return;

        if (isShowCheckBox) {
            setCheckBoxChecked(pks);
        }

        if (isSetSelect) setTableSelect(pks);
    }

    private void setTableSelect(String[] pks) {
        int[] rowIndex = getRecordIndexes(pks);

        if (rowIndex == null) {
            // 默认定位到第一行
            if (getTable().getModel().getRowCount() > 0) {
                getTable().setRowSelectionInterval(0, 0);
            }
            return;
        }
        int iGridIndex = -1;
        for (int i = 0; i < rowIndex.length; i++) {
            iGridIndex = rowIndex[i];
            if (iGridIndex >= 0 && iGridIndex < getTable().getRowCount()) {

                getTable().requestFocusInWindow();
                getTable().getSelectionModel().setSelectionInterval(iGridIndex, iGridIndex);
                getTable().getColumnModel().getSelectionModel().setSelectionInterval(0, 0);
                if (!isShowCheckBox) getTable().scrollRectToVisible(getTable().getCellRect(iGridIndex, 0, false));
            } else {
                continue;
            }
        }
        if (isShowCheckBox) {
            getTable().getSelectionModel().setSelectionInterval(0, 0);
        }
    }

    /*
     * （非 Javadoc）
     * @see nc.ui.bd.ref.IRefDataComponent#getRefType()
     */
    public int getRefType() {
        // TODO 自动生成方法存根
        return IRefConst.GRID;
    }

    /*
     * （非 Javadoc）
     * @see nc.ui.bd.ref.IRefDataComponent#matchTreeNode(java.lang.String, java.lang.String)
     */
    public void matchTreeNode(String clumnName,
                              String matchValue) {
        // TODO 自动生成方法存根

    }

    /*
     * （非 Javadoc）
     * @see nc.ui.bd.ref.IRefDataComponent#getTreeRootNode()
     */
    public DefaultMutableTreeNode getTreeRootNode() {
        // TODO 自动生成方法存根
        return null;
    }

    private void registerKey() {
        // Enter key
        InputMap map = getTable().getInputMap();
        ActionMap am = getTable().getActionMap();

        // ESCAPE
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), ShortCutKeyAction.KEY_REFTABLE_ESCAPE);
        am.put(ShortCutKeyAction.KEY_REFTABLE_ESCAPE, new ShortCutKeyAction(ShortCutKeyAction.VK_ESCAPE));
        // A
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), ShortCutKeyAction.KEY_REFTABLE_ENTER);
        am.put(ShortCutKeyAction.KEY_REFTABLE_ENTER, new ShortCutKeyAction(ShortCutKeyAction.VK_ENTER));

    }

    class ShortCutKeyAction extends AbstractAction {

        int keycode = -1;

        final static int VK_ESCAPE = KeyEvent.VK_ESCAPE;

        final static int VK_ENTER = KeyEvent.VK_ENTER;

        final static String KEY_REFTABLE_ESCAPE = "Reftable_ESCAPE";

        final static String KEY_REFTABLE_ENTER = "Reftable_ENTER";

        // final static String KEY_REFTABLE_ENTER = "Reftable_ENTER";

        public ShortCutKeyAction(int keycode) {
            this.keycode = keycode;
        }

        public void actionPerformed(ActionEvent e) {

            switch (keycode) {
                case VK_ESCAPE:
                    getRefDialog().onButtonExit();
                    break;
                case VK_ENTER:
                    getRefDialog().onButtonOK();
                    break;
            }
        }
    }

    public void requestComponentFocus() {
        getTable().requestFocus();

    }

    /**
     * @return 返回 vDataAll。
     */
    public Vector getVDataAll() {
        if (vDataAll == null) {
            vDataAll = new Vector();
        }
        return vDataAll;
    }

    /**
     * @param dataAll
     *        要设置的 vDataAll。
     */
    public void setVDataAll(Vector dataAll) {
        vDataAll = dataAll;
    }

    public void setGridDataState(IRefGridDataState gridDataState) {
        this.gridDataState = gridDataState;
    }

    public IRefGridDataState getGridDataState() {
        return gridDataState;
    }

}
