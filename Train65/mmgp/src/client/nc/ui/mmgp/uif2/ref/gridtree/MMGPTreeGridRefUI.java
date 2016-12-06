package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IDynamicColumn;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefQueryDlg;
import nc.ui.bd.ref.IRefQueryDlg2;
import nc.ui.bd.ref.IRefUI60;
import nc.ui.bd.ref.RefActions;
import nc.ui.bd.ref.RefButtonPanelFactory;
import nc.ui.bd.ref.RefFocusTraversalPolicy;
import nc.ui.bd.ref.RefInitializeCondition;
import nc.ui.bd.ref.RefNCDataTableModle;
import nc.ui.bd.ref.RefRecentRecordsUtil;
import nc.ui.bd.ref.RefUIConfig;
import nc.ui.bd.ref.RefUtil;
import nc.ui.bd.ref.RefValueVO;
import nc.ui.bd.ref.UFRefColumnsDlg;
import nc.ui.bd.ref.event.NCEventObject;
import nc.ui.bd.ref.event.NCEventSource;
import nc.ui.bd.ref.event.RefEventConstant;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;
import nc.ui.bd.ref.minipanel.IMiniPanelOpener;
import nc.ui.bd.ref.minipanel.MiniPanel;
import nc.ui.bd.ref.minipanel.SplitpanelInfo;
import nc.ui.dbcache.CommonDocCacheFacade;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.ActionsBar;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.toolbar.ToolBarPanel;
import nc.ui.pub.style.Style;
import nc.vo.bd.ref.RefcolumnVO;
import nc.vo.bd.ref.ReftableVO;
import nc.vo.logging.Debug;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;

public class MMGPTreeGridRefUI extends nc.ui.pub.beans.UIDialog implements
    IRefUI60,
    ValueChangedListener,
    ChangeListener {

    int width = 658;

    int height = 390;

    private MMGPTreeGridRefModel refmodel;

    private RefUIConfig refUIConfig;

    /**
     * 是否显示选择表窗口
     */
    private boolean isShowSeletedDataTable = false;

    /**
     * 主体内容Pane
     */
    private javax.swing.JPanel uIDialogContentPane = null;

    private UIPanel pnl_center = null;

    private UITabbedPane tbPane = null;

    private UIPanel commDataPanel = null;

    private MMGPRefGridData tbP_commonData = null;

    private Map<String, Vector> pkRecordCommDataMap = null;

    /**
     * 常用表的缓存PK->行
     */
    Hashtable htPkToRow = new Hashtable();

    /**
     * 左表的缓存JoinValue->行
     */
    Hashtable datePkToRow = new Hashtable();

    /**
     * 多选时区分数据窗口与选中表split
     */
    private nc.ui.pub.beans.UISplitPane splpane_Data = null;

    /**
     * 全部数据
     */
    private nc.ui.pub.beans.UISplitPane splPane_all = null;

    private UIPanel pnl_Data = null;

    /**
     * 装载全部数据中左表
     */
    // private nc.ui.pub.beans.UIPanel pnl_Data = null;

    private nc.ui.pub.beans.UIPanel pnlTable_data = null;

    private UIPanel pnlTree_data = null;

    private UIPanel pnl_Tree_btn = null;

    /**
     * 装载选中表的窗口
     */
    private nc.ui.pub.beans.UIPanel pnl_SelectedTable = null;

    private MMGPTreePanel treePanel;

    /**
     * 数据选中表
     */
    private MMGPRefGridSelectedData tbP_selectedData = null;

    private MiniPanel pnl_miniPanel1 = null;

    private MiniPanel pnl_miniPanel2 = null;

    private RefButtonPanelFactory buttonPanelFactory = null;

    private MMGPRefGridData tbP_data = null;

    private Hashtable<String, Vector> htselectedVector = new Hashtable<String, Vector>();

    private AbstractButton[] addonsButtons = null;

    // sxj 2003-10-28
    // 动态列
    IDynamicColumn dynamicColClass;

    int sourceColNum = 0;

    int tablesourceColNum = 0;

    private Vector<String> m_vecAllColumnNames = null;

    private Vector<String> vecAllTableColumnNames = null;

    private RefcolumnVO[] columnVOs = null;

    private RefcolumnVO[] tableColumnVOs = null;

    private Hashtable htLocate = new Hashtable();

    private boolean isMultiCorpRef;

    private boolean isCommonDataMatchPk = false;

    protected NCEventSource eventSource = new NCEventSource(this);

    private EventHandler eventHandler = new EventHandler();

    private RefFocusTraversalPolicy focusPolicy = null;

    private CommonDocCacheFacade facade = null;

    // 是否包含下级控件是否显示
    private boolean isIncludeSubShow = false;

    /**
     * @return 返回 chkSubNode。
     */
    private UICheckBox getChkIncludeSubNode() {

        return getButtonPanelFactory().getChkIncludeSubNode();
    }

    /**
	 * 
	 */
    public boolean isIncludeSubNode() {
        return isIncludeSubShow() && getChkIncludeSubNode().isSelected();
    }

    /**
     * UFRefTreeGridUI 构造子注解。
     * 
     * @param parent
     *        java.awt.Container
     * @param title
     *        java.lang.String
     */
    public MMGPTreeGridRefUI(java.awt.Container parent,
                             AbstractRefModel refModel,
                             RefUIConfig refUIConfig) {
        super(parent, refModel.getRefTitle());
        this.refUIConfig = refUIConfig;
        setRefModel(refModel);
        initialize();
    }

    /**
     * 初始化类。
     */
    /* 警告：此方法将重新生成。 */
    protected void initialize() {
        setName("EMPMTreeGridRefUI");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setDefaultSize();
        setResizable(true);

        setShowSelectedDataTable();

        setContentPane(getUIDialogContentPane());

        getDataTable().setRefModel(getRefModel());

        initListener();

        if (isMultiSelectedRef() && isShowSeletedDataTable) {
            getPnl_miniPanel1().getSplitPnlInfo().getOpener().doResetAction();
        }
        if (this.getRefUIConfig().isIncludeSubShow()) {
            this.getChkIncludeSubNode().setVisible(true);
        }

    }

    private void initListener() {

        getDataTable().getTable().getSelectionModel().addListSelectionListener(tableSelcetionAdapter);
        if (this.isMultiSelectedRef()) {
            addListener(RefEventConstant.DEL_SELECTED_ROWS, treePanel.delTreeSelectedRowHandler);
            tbP_selectedData.addListener(RefEventConstant.DEL_SELECTED_ROWS, delSelectedRowHandler);
        }
        getRefCorp().addValueChangedListener(this);
        this.getUIButtonOK().addActionListener(eventHandler);
        this.getUIButtonExit().addActionListener(eventHandler);
        getActions().addListener(RefEventConstant.BUTTON_CLICK_ACTION, buttonClickActionHandler);
        getTFLocate().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                if (!getTFLocate().hasFocus()) return;
                String strInput = getTFLocate().getText();
                locate("");
                locate(strInput);
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {

                if (!getTFLocate().hasFocus()) return;
                String strInput = getTFLocate().getText();
                locate("");
                locate(strInput);

            };

            @Override
            public void changedUpdate(DocumentEvent arg0) {
            }

            private void locate(String strInput) {
                int col = -1;
                if (getUIComboBoxColumn().getSelectedIndex() >= 0
                    && getUIComboBoxColumn().getSelectedIndex() < getRefModel().getShownColumns().length)
                    col = getLocateColumn();
                if (col < 0) {
                    return;
                }

                // if (strInput.equals("")) {
                // getUIbtnLocQuery().setVisible(true);
                // getUIbtnLocQuery().setEnabled(false);
                // } else {
                // getUIbtnLocQuery().setVisible(true);
                // getUIbtnLocQuery().setEnabled(true);
                // }
                getDataTable().blurInputValue(getUIComboBoxColumn().getSelectedItem().toString(), strInput);
            }
        });
    }

    private IEventListener buttonClickActionHandler = new IEventListener() {
        public void invoke(Object sender,
                           NCEventObject evt) {
            if (evt.getProperty(RefEventConstant.ACTION_BUTTON_NAME) != null) {
                String actionBtnName = (String) evt.getProperty(RefEventConstant.ACTION_BUTTON_NAME);
                if (actionBtnName == RefActions.BUTTON_REFRESH) {
                    onRefresh();
                } else if (actionBtnName == RefActions.BUTTON_SETCOLUMN) {
                    onColumn();
                } else if (actionBtnName == RefActions.BUTTON_SAVECOMMDATA) {
                    doSave();
                } else if (actionBtnName == RefActions.BUTTON_REFRESHCOMMDATA) {
                    resetCommdata(true);
                } else if (actionBtnName == RefActions.BUTTON_DELETECOMMDATA) {
                    doDelete();
                } else if (actionBtnName == RefActions.BUTTON_QUERY) {
                    onQuery();
                }
            }
        }
    };

    private void doDelete() {
        Vector vec = getSelectedCommData(true);

        if (vec == null || vec.size() == 0) {
            return;
        }

        String[] tablenames = getEMRefModel().getCommonDataTableName();
        String[] tablepks = getEMRefModel().getCommonDataSavePk_doc();
        for (int i = 0; i < tablenames.length; i++) {
            getCommonDocFacade().deleteCommDoc(
                tablenames[i],
                getPk_Commondoc(vec, getEMRefModel().getFieldIndex(tablepks[i])));
        }

        resetCommdata(false);
    }

    private void doSave() {
        Vector vec = this.getTreePanel().getSelectData();

        Vector filterVec = new Vector();

        int newlySize = getPkRecordCommDataMap().size();

        if (newlySize > 0) {
            for (int i = 0; i < vec.size(); i++) {
                Vector record = (Vector) vec.get(i);
                Object pk = record.get(getPKIndex());
                if (getPkRecordCommDataMap().containsKey(pk)) {
                    continue;
                } else {
                    filterVec.add(record);
                }

            }
        } else {
            filterVec = vec;
        }

        if (filterVec == null || filterVec.size() == 0) {
            return;
        }

        int addSize = filterVec.size();

        int nowSize = newlySize + addSize;

        if (nowSize > IRefConst.COMMONDATASIZE) {
            MessageDialog.showHintDlg(
                this,
                NCLangRes.getInstance().getStrByID("ref", "UFRefGridTreeCommDataUI-000002")/* 提示信息 */,
                NCLangRes.getInstance().getStrByID("ref", "UFRefGridTreeCommDataUI-000003")/* 常用数据超过了最大容量 */
                    + (nowSize - IRefConst.COMMONDATASIZE)
                    + NCLangRes.getInstance().getStrByID("ref", "UFRefGridTreeCommDataUI-000004")/*
                                                                                                  * 条数据， 请先删除部分常用数据后再添加
                                                                                                  * , 常用数据最大容量为 ：
                                                                                                  */
                    + IRefConst.COMMONDATASIZE
                    + NCLangRes.getInstance().getStrByID("ref", "UFRefGridTreeCommDataUI-000005")/* 条数据 */);
            return;
        }
        String[] tablenames = getEMRefModel().getCommonDataTableName();
        String[] tablepks = getEMRefModel().getCommonDataSavePk_doc();
        for (int i = 0; i < tablenames.length; i++) {
            getCommonDocFacade().insertCommDoc(
                tablenames[i],
                getPk_Commondoc(filterVec, getEMRefModel().getFieldIndex(tablepks[i])),
                getRefModel().getPk_org());
        }

        resetCommdata(false);

    }

    private void resetCommdata(boolean isrefresh) {
        Vector vCommData = getCommonData(isrefresh);

        getTbP_commonData().setData(vCommData);
        getTbP_commonData().setVDataAll(vCommData);
        
        getTbP_commonData().setMatchedPKs(getSelectedPKs());

        initPkRecordMap(vCommData);
        htLocate.clear();
        getButtonPanelFactory().refreshTfCommonDataLocate();
    }

    private void initPkRecordMap(Vector vCommData) {
        if (vCommData == null) {
            return;
        }
        getPkRecordCommDataMap().clear();
        for (int i = 0; i < vCommData.size(); i++) {
            Vector record = (Vector) vCommData.get(i);
            String pk = (String) record.get(getPKIndex());
            getPkRecordCommDataMap().put(pk, record);
        }
    }

    private Map<String, Vector> getPkRecordCommDataMap() {
        if (pkRecordCommDataMap == null) {
            pkRecordCommDataMap = new HashMap<String, Vector>();
        }
        return pkRecordCommDataMap;
    }

    private List<String> getPk_Commondoc(Vector vec,
                                         int fieldIndex) {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < vec.size(); i++) {
            Vector record = (Vector) vec.get(i);
            String pk = null;

            pk = (String) record.get(fieldIndex);

            list.add(pk);
        }
        return list;
    }

    /**
     * Comment
     */
    private void onColumn() {

        UFRefColumnsDlg refColumnsDlg =
                new UFRefColumnsDlg(
                    this,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("ref", "UPPref-000340")/* @res "栏目选择" */,
                    getRefModel());
        if (refColumnsDlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
            // 栏目变化，带公式的数据要转换为Name。这里可以改进提高效率，如果栏目变化且包含有公式，才进行公式执行
            // 考虑用户的实际应用，变化栏目属于小概率事件，这里简单处理。
            getRefModel().setShownColumns(refColumnsDlg.getSelectedColumns());
            getRefModel().reloadData();

            setColumnVOs(null);
            if (isMultiSelectedRef()) {
                setNewColumnSequence(getTbP_selectedData().getTable());
            }
            if (this.isCommonDataRef()) {
                setNewColumnSequence(getTbP_commonData().getTable());
            }
            setComboBoxData(getColumnVOs());

        }

        refColumnsDlg.destroy();
        this.onRefresh();
    }

    class EventHandler implements java.awt.event.ActionListener, java.awt.event.KeyListener {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == getUIButtonOK()) {
                onButtonOK();
            } else if (e.getSource() == getUIButtonExit()) {
                onButtonExit();
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

        }
    }

    // /* 警告：此方法将重新生成。 */
    // private nc.ui.pub.beans.UIButton getUIbtnLocQuery() {
    // return getButtonPanelFactory().getBtnSimpleQuery();
    // }
    //

    /**
     * Invoked when an action occurs.
     */
    private int getLocateColumn() {
        int col = -1;
        if (getUIComboBoxColumn().getSelectedIndex() >= 0
            && getUIComboBoxColumn().getSelectedIndex() < getRefModel().getShownColumns().length) {
            int colCount = getDataTable().getTable().getModel().getColumnCount();
            String colName;
            for (int i = 0; i < colCount; i++) {
                colName = getDataTable().getTable().getModel().getColumnName(i);
                if (colName.equals(getUIComboBoxColumn().getSelectedItem().toString())) {
                    col = i;
                }
            }
        }

        return col;
    }

    /**
     * 返回 UIRefCorp 特性值。
     * 
     * @return nc.ui.pub.beans.UIRefPane
     */
    /* 警告：此方法将重新生成。 */
    public nc.ui.pub.beans.UIRefPane getRefCorp() {

        return getButtonPanelFactory().getRefCorp();
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-29 14:30:52)
     * 
     * @param iColumns
     *        int[]
     */
    private void setComboBoxData(RefcolumnVO[] items) {
        if (items == null) return;
        getUIComboBoxColumn().removeAllItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getIscolumnshow().booleanValue()) {
                getUIComboBoxColumn().addItem(items[i]);
            }
        }

    }

    /**
     * 返回 UIComboBox1 特性值。
     * 
     * @return nc.ui.pub.beans.UIComboBox
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UIComboBox getUIComboBoxColumn() {

        return getButtonPanelFactory().getCbbColumn();
    }

    ListSelectionListener tableSelcetionAdapter = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if ((e.getSource() == getDataTable().getTable().getSelectionModel())) {
                processTableSelect(e);
            }
        }
    };

    protected void processTableSelect(ListSelectionEvent e) {
        UITable table = (UITable) this.getDataTable().getTable();
        int row = table.getSelectedRow();
        Vector vRecord = new Vector();
        String key = null;
        for (int j = 0; j < tablesourceColNum; j++) {
            Object value = getDataTable().getTable().getModel().getValueAt(row, j);
            vRecord.addElement(value);
        }
        String joinvalue = this.getTableJoinValue(vRecord);
        this.getEMRefModel().setTableJoinValue(joinvalue);
        Vector v = this.getEMRefModel().getData();
        this.getTreePanel().setTreeModel(v);
        this.getTreePanel().setMatchedPKs(this.getSelectedPKs());
        if (!this.isMultiSelectedRef()) {
            if (this.getTreePanel().getSelectData() == null) {
                this.getUIButtonOK().setEnabled(false);
            }
        }
    }

    private String[] getSelectedPKs() {
        if (getHtselectedVector() != null) {
            Object[] objKeys = getHtselectedVector().keySet().toArray();
            if (objKeys.length > 0) {
                String[] pks = new String[objKeys.length];
                for (int i = 0; i < objKeys.length; i++) {
                    pks[i] = objKeys[i].toString();
                }
                return pks;
            }
            return null;
        }
        return null;
    }

    /**
     * <p>
     * <strong>最后修改人：sxj</strong>
     * <p>
     * <strong>最后修改日期：2006-11-9</strong>
     * <p>
     * 
     * @param
     * @return String
     * @exception BusinessException
     * @since NC5.0
     */
    private String getTableJoinValue(Vector vSelected) {
        String strJoinValue = null;
        if (vSelected == null) {
            return null;
        }

        if (vSelected != null) {
            int col = refmodel.getTableFieldIndex(refmodel.getTableJoinField());
            if (col >= 0 && col < vSelected.size()) {
                strJoinValue = (String) vSelected.elementAt(col);
            }
        }
        return strJoinValue;
    }

    public int showModal() {
        htselectedVector.clear();

        if (getRefModel() == null) {
            return -1;
        }

        this.getEMRefModel().setTableJoinValue(null);
        setUserSize();

        if (getRefUIConfig().isFilterRef()) {
            setFilterRefSize();
        }

        if (getRefUIConfig().getIBeforeRefDlgShow() != null) {
            getButtonPanelFactory().getPnl_addonButton().setVisible(true);
            setAddonsButtons(getRefUIConfig().getIBeforeRefDlgShow().addButtons(this));

            addAddonButtons();
        }

        // 封存数据是否显示按钮
        getChkSealedDataShow().setVisible(
            getRefUIConfig().isDisabledDataButtonShow() && getRefModel().isAddEnableStateWherePart());
        if (getRefUIConfig().isDisabledDataButtonShow()) {
            getRefModel().setDisabledDataShow(getChkSealedDataShow().isSelected());
        }

        // 注册了查询类的参照，查询按钮enable
        String queryClassName = getRefModel().getRefQueryDlgClaseName();

        if (queryClassName == null) {
            getToolBarPnl().getToolBar().removeAction(getActions().getQueryAction());
        }

        // 是否分类支持多选择
        if (isMultiSelectedRef()) {
            // TODO
            // getUIButAdd().removeActionListener(this);
            // getUIButDel().removeActionListener(this);
            // getUIButAdd().addActionListener(this);
            // getUIButDel().addActionListener(this);

        }

        // sxj 2003-10-28
        // 是否为动态列参照
        if (getRefModel().isDynamicCol()) {
            dynamicColClass = initDynamicColClass();
            String[][] dynamicFieldNames = dynamicColClass.getDynaminColNameAndLoc();
            if (dynamicFieldNames != null) {
                String[] strNames = new String[dynamicFieldNames.length];
                for (int i = 0; i < strNames.length; i++) {
                    strNames[i] = dynamicFieldNames[i][0];
                }
                // 设置动态列到model
                getRefModel().setDynamicFieldNames(strNames);
                m_vecAllColumnNames = null;

            }

        }

        initTable(true);

        setShowIndexToModel();

        setComboBoxData(getColumnVOs());

        Vector v = getEMRefModel().getTableData();

        setTableData(v);

        Vector selectedData = null;
        if (getRefModel().getSelectedData() != null) {

            selectedData = (Vector) getRefModel().getSelectedData().clone();
        }
        this.getTreePanel().clearTreeData();
        if (isCommonDataRef()) {
            initCommDataTable();
            getToolBarPnl().getToolBar().addAction(getActions().getSaveCommDataAction(), 0);
            getToolBarPnl().getToolBar().addAction(new ActionsBar.ActionsBarSeparator(4, 20), 1);
        }

        // 维护按钮是否显示
        if (getRefModel().getRefMaintenanceHandler() == null) {
            getBtnAddDoc().setVisible(false);// 维护按钮是否显示
        }

        if (isMultiCorpRef) {
            getButtonPanelFactory().addRefFilterValueChangeListener(this);
            setRefFilterConditions();

        } else {
            getButtonPanelFactory().getPnl_North().setVisible(isMultiCorpRef);
        }

        if (!this.isMultiSelectedRef()) {
            this.getUIButtonOK().setEnabled(false);
        } else {
            if (getHtselectedVector() == null || getHtselectedVector().size() == 0) {
                this.getUIButtonOK().setEnabled(false);
            }
        }

        // 如果有选中的数据，定位。
        if (!isCommonDataMatchPk) {
            setMatchedDataToUI(selectedData);
            // setClassLocatePKToUI();
        } else {
            if (isMultiSelectedRef() && selectedData != null && selectedData.size() > 0) {
                addRecordToSelectedTable(selectedData);
                getTbP_commonData().setMatchedPKs(getSelectedPKs());
            }
        }

        setUserColumnWidth();

        setPnl_dataCtlVisible();

        setFocusTraversalPolicy();

        int iResult = super.showModal();

        getTFLocate().setText("");

        // 持久化参照的尺寸
        RefUtil.putRefSize(getRefModel().getRefNodeName(), new Dimension(getWidth(), getHeight()));

        // 保存参照的列宽
        RefUtil.putRefColumnSize(getEMRefModel().getTableRefNodeName(), getDataTable().getTableColumnsWidth());
        if (this.isMultiSelectedRef()) {
            RefUtil.putRefColumnSize(getEMRefModel().getRefNodeName(), getTbP_selectedData().getTableColumnsWidth());
        } else if (this.isCommonDataRef()) {
            RefUtil.putRefColumnSize(getEMRefModel().getRefNodeName(), getTbP_commonData().getTableColumnsWidth());
        }

        // 持久化是否显示已选数据表
        if (isMultiSelectedRef()) {
            RefUtil.putRefIsShowSelectedDataTable(getRefModel().getRefNodeName(), isShowSeletedDataTable);
        }

        return iResult;
    }

    /**
     * <p>
     * <strong>最后修改人：sxj</strong>
     * <p>
     * <strong>最后修改日期：2006-8-3</strong>
     * <p>
     * 
     * @param
     * @return void
     * @exception BusinessException
     * @since NC5.0
     */
    private void setMatchedDataToUI(Vector selectedData) {
        if (selectedData != null && selectedData.size() > 0) {

            if (isMultiSelectedRef()) {
                addRecordToSelectedTable(selectedData);
            } else {
                Vector firstMatchRecode = (Vector) selectedData.get(0);
                int colIndex = getEMRefModel().getFieldIndex(getEMRefModel().getDocJoinField());
                if (colIndex > -1) {
                    Object obj = firstMatchRecode.get(colIndex);
                    Object tableJoinValue = null;
                    if (obj instanceof RefValueVO) {
                        tableJoinValue = ((RefValueVO) obj).getOriginValue();
                    } else {
                        tableJoinValue = obj;
                    }
                    if (tableJoinValue != null) {
                        Integer row = (Integer) getDatePkToRow().get(tableJoinValue);
                        if (row != null)
                            this.getDataTable().getTable().getSelectionModel().setSelectionInterval(row, row);
                    }

                    setNodeSelected();

                }
            }
        }
    }

    private void setNodeSelected() {
        String[] selectedDatas = getRefModel().getPkValues();
        this.getTreePanel().setMatchedPKs(selectedDatas);
    }

    private void setUserColumnWidth() {
        HashMap<String, Integer> map = RefUtil.getRefColumnSize(getRefModel().getRefNodeName());
        HashMap<String, Integer> tablemap = RefUtil.getRefColumnSize(getEMRefModel().getTableRefNodeName());
        getDataTable().setTableColumnsWidth(tablemap);
        if (isMultiSelectedRef()) {
            getTbP_selectedData().setTableColumnsWidth(map);
        }
        if (this.isCommonDataRef()) {
            getTbP_commonData().setTableColumnsWidth(map);
        }
    }

    private void setPnl_dataCtlVisible() {
        boolean isShow = false;
        for (int i = 0; i < getButtonPanelFactory().getPnl_dataCtl().getComponentCount(); i++) {
            if (getButtonPanelFactory().getPnl_dataCtl().getComponent(i).isVisible()) {
                isShow = true;
                break;
            }
        }
        if (!isShow) getButtonPanelFactory().getPnl_dataCtl().setVisible(false);
        else
            getButtonPanelFactory().getPnl_dataCtl().setVisible(true);
    }

    protected void setFocusTraversalPolicy() {
        getUIDialogContentPane().setFocusTraversalPolicyProvider(true);
        Vector<Component> order = getFocusOrderByTab();
        if (focusPolicy == null) {
            focusPolicy = new RefFocusTraversalPolicy(order);
            getUIDialogContentPane().setFocusTraversalPolicy(focusPolicy);
        } else {
            focusPolicy.setOrder(order);
        }
    }

    private Vector<Component> getFocusOrderByTab() {
        if (this.isCommonDataRef() && getTbPane().getSelectedIndex() == 0) {
            return getCommonDataTabFocusOrder();
        } else {
            return getDataTabFocusOrder();
        }
    }

    private Vector<Component> getCommonDataTabFocusOrder() {
        Vector<Component> order = new Vector<Component>();

        if (getRefUIConfig().isMultiCorpRef()) {
            RefInitializeCondition[] conditions = getRefUIConfig().getRefFilterInitconds();
            if (conditions != null) {
                for (int i = 0; i < conditions.length; i++) {
                    if (conditions[i].getRefNodeName() == null) {
                        continue;
                    }
                    UIRefPane ref = getButtonPanelFactory().getRefFilter(conditions[i].getRefNodeName());
                    order.add(ref.getUITextField());
                }
            }
        }
        order.add(getButtonPanelFactory().getTfCommonDataLocate());// 常用数据上的过滤文本框

        order.add(getTbP_commonData().getTable());
        if (getBtnAddDoc().isVisible()) order.add(getBtnAddDoc());
        if (getButtonPanelFactory().getChkIncludeSubNode().isVisible())
            order.add(getButtonPanelFactory().getChkIncludeSubNode());
        if (getButtonPanelFactory().getChkSealedDataShow().isVisible())
            order.add(getButtonPanelFactory().getChkSealedDataShow());
        if (getButtonPanelFactory().getChkHistoryVersion().isVisible())
            order.add(getButtonPanelFactory().getChkHistoryVersion());
        order.add(getUIButtonOK());
        order.add(getUIButtonExit());
        return order;
    }

    private Vector<Component> getDataTabFocusOrder() {
        Vector<Component> order = new Vector<Component>();

        if (getRefUIConfig().isMultiCorpRef()) {
            RefInitializeCondition[] conditions = getRefUIConfig().getRefFilterInitconds();
            if (conditions != null) {
                for (int i = 0; i < conditions.length; i++) {
                    if (conditions[i].getRefNodeName() == null) {
                        continue;
                    }
                    UIRefPane ref = getButtonPanelFactory().getRefFilter(conditions[i].getRefNodeName());
                    order.add(ref.getUITextField());
                }
            }
        }
        order.add(this.getTreePanel().getFilterHandler().getFilterTextField().getComponent(0));// 树上的过滤文本框
        order.add(this.getTreePanel().getTree());
        order.add(getButtonPanelFactory().getTfLocate(false));
        order.add(getDataTable().getTable());
        order.add(getButtonPanelFactory().getBtnPreviousPage());
        order.add(getButtonPanelFactory().getBtnNextPage());
        order.add(getBtnAddDoc());
        if (getButtonPanelFactory().getChkIncludeSubNode().isVisible())
            order.add(getButtonPanelFactory().getChkIncludeSubNode());
        if (getButtonPanelFactory().getChkSealedDataShow().isVisible())
            order.add(getButtonPanelFactory().getChkSealedDataShow());
        if (getButtonPanelFactory().getChkHistoryVersion().isVisible())
            order.add(getButtonPanelFactory().getChkHistoryVersion());

        order.add(getUIButtonOK());
        order.add(getUIButtonExit());
        return order;
    }

    /* 警告：此方法将重新生成。 */
    public nc.ui.pub.beans.UITextField getTFLocate() {

        return getButtonPanelFactory().getTfLocate(true);
    }

    private nc.ui.pub.beans.UIPanel getPnl_RefOrg() {

        return getButtonPanelFactory().getPnl_refCorp();
    }

    private void setRefFilterConditions() {
        if (getPnl_RefOrg().isVisible()) {
            RefInitializeCondition[] conditions = getRefUIConfig().getRefFilterInitconds();
            if (conditions != null) {
                for (int i = 0; i < conditions.length; i++) {
                    if (conditions[i].getRefNodeName() == null) {
                        continue;
                    }
                    UIRefPane ref = getButtonPanelFactory().getRefFilter(conditions[i].getRefNodeName());
                    ref.setDisabledDataButtonShow(conditions[i].isDisabledDataButtonShow());
                    if (conditions[i].getDataPowerOperation_code() != null) {
                        ref.getRefModel().setDataPowerOperation_code(conditions[i].getDataPowerOperation_code());
                    }
                    if (conditions[i].getWherePart() != null)
                        ref.getRefModel().setWherePart(conditions[i].getWherePart());
                    if (conditions[i].getAddWherePart() != null)
                        ref.getRefModel().addWherePart(conditions[i].getAddWherePart());
                    ref.getRefModel().setFilterPks(conditions[i].getFilterPKs());
                    if (conditions[i].getDefaultPk() != null) {
                        ref.setPK(conditions[i].getDefaultPk());
                        valueChanged(new ValueChangedEvent(ref, new String[]{conditions[i].getDefaultPk() }) {

                        });
                    } else {
                        ref.setPK(getRefModel().getPk_org());
                        if (getRefModel().getPk_org() != null)
                            valueChanged(new ValueChangedEvent(ref, new String[]{getRefModel().getPk_org() }) {

                            });
                    }
                }
            }
        }
    }

    /**
     * @return 返回 btnAddDoc。
     */
    private UIButton getBtnAddDoc() {

        return getButtonPanelFactory().getBtnMaintenanceDoc();
    }

    private void setTableData(Vector vDataAll) {
        if (vDataAll == null) vDataAll = new Vector();
        getDataTable().setDataVector(vDataAll);

        // 根据BlurValue定位
        initDataPkToRow(vDataAll);
        if (getDataTable().getTable().getModel().getRowCount() > 0
            && getRefModel().getBlurValue() != null
            && getRefModel().getBlurValue().trim().length() > 0
            && getRefModel().getBlurValue().indexOf("*") == -1
            && getRefModel().getBlurValue().indexOf("%") == -1
            && getRefModel().getBlurValue().indexOf("?") == -1) {
            int col = -1;
            int iGridIndex = -1;
            col = getRefModel().getFieldIndex(getRefModel().getBlurFields()[0]);
            if (col >= 0) iGridIndex = findMatchRow(getRefModel().getBlurValue(), col);
            if (iGridIndex >= 0) {
                getDataTable().getTable().setRowSelectionInterval(iGridIndex, iGridIndex);
                getDataTable().getTable().scrollRectToVisible(
                    getDataTable().getTable().getCellRect(iGridIndex, col, false));
            } else {
                getDataTable().getTable().clearSelection();
            }
        }

        getDataTable().getTable().clearSelection();
        // 清空模糊匹配的数据
        htLocate.clear();

    }

    private void initDataPkToRow(Vector<Vector<Object>> vDataAll) {
        this.getDatePkToRow().clear();
        for (int i = 0; i < vDataAll.size(); i++) {
            Vector<Object> o = vDataAll.get(i);
            String tablejoinField = this.getEMRefModel().getTableJoinField();
            int tablejoinFieldIndex = this.getEMRefModel().getTableFieldIndex(tablejoinField);
            String tablejoinValue = (String) o.get(tablejoinFieldIndex);
            getDatePkToRow().put(tablejoinValue, Integer.valueOf(i));
        }
    }

    /**
     * 表model中查找定位
     * 
     * @author 张扬
     */
    private int findMatchRow(String strInput,
                             int iSelectedIndex) {
        int iGridIndex = -1;
        if (strInput != null
            && !strInput.equals("")
            && getDataTable().getTable().getModel().getRowCount() > 0
            && iSelectedIndex >= 0
            && iSelectedIndex < getDataTable().getTable().getModel().getColumnCount()) {
            for (int i = 0; i < getDataTable().getTable().getModel().getRowCount(); i++) {
                Object o = getDataTable().getTable().getModel().getValueAt(i, iSelectedIndex);
                if (o == null) continue;
                String strCell = o.toString().trim();
                if (strInput.length() > strCell.length()) continue;
                if (strCell.startsWith(strInput)) {
                    iGridIndex = i;
                    break;
                }
            }
        }
        return iGridIndex;
    }

    private void initTable(boolean clearSelectionTable) {

        // 初始化参数表
        initTableTableEnv(getDataTable(), true);

        if (this.isCommonDataRef()) {
            // 初始化常有数据表
            initTableEnv(getTbP_commonData(), false);
        }

        if (isMultiSelectedRef() && clearSelectionTable) {
            initTableEnv(getTbP_selectedData(), true);

        }

    }

    /**
     * 取得当前栏目VO
     */
    private nc.vo.bd.ref.ReftableVO getVO(String pk_corp) {

        return getRefModel().getRefTableVO(pk_corp);
    }

    private void setShowIndexToModel() {
        ReftableVO vo = getVO(getRefModel().getPk_corp());
        if (vo != null && vo.getColumnVOs() != null) {

            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < vo.getColumnVOs().length; i++) {
                if (vo.getColumnVOs()[i].getIscolumnshow().booleanValue()) {
                    list.add(vo.getColumnVOs()[i].getColumnshowindex());
                }

            }
            if (list.size() > 0) {
                int[] showIndex = new int[list.size()];
                for (int i = 0; i < showIndex.length; i++) {
                    showIndex[i] = ((Integer) list.get(i)).intValue();
                }
                getRefModel().setShownColumns(showIndex);
            }

        }
        ReftableVO tablevo = getEMRefModel().getTableRefTableVO(getRefModel().getPk_corp());
        if (tablevo != null && tablevo.getColumnVOs() != null) {

            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < tablevo.getColumnVOs().length; i++) {
                if (tablevo.getColumnVOs()[i].getIscolumnshow().booleanValue()) {
                    list.add(tablevo.getColumnVOs()[i].getColumnshowindex());
                }

            }
            if (list.size() > 0) {
                int[] showIndex = new int[list.size()];
                for (int i = 0; i < showIndex.length; i++) {
                    showIndex[i] = ((Integer) list.get(i)).intValue();
                }
                getEMRefModel().setTableShownColumns(showIndex);
            }

        }
    }

    private void initTableEnv(MMGPRefTablePane tablePane,
                              boolean isDynamic) {
        UITable table = tablePane.getTable();
        initTableModel(table);
        if (isDynamic) {
            MMGPRefTableColumnModel cm = (MMGPRefTableColumnModel) table.getColumnModel();
            cm.setDynamicColumnNames(getRefModel().getDynamicFieldNames());
        }
        if (!isMultiSelectedRef()) table.addSortListener();
        setNewColumnSequence(table);
        tablePane.initRowHeader();
        if (isMultiSelectedRef()) table
            .setSelectionMode(javax.swing.DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        else
            table.setSelectionMode(javax.swing.DefaultListSelectionModel.SINGLE_SELECTION);

    }

    /*
     * 设置定位Combox,表的栏目的显示顺序
     */
    private void setNewColumnSequence(UITable UITable) {

        boolean isDynamicCol = getRefModel().isDynamicCol();

        getTableColumnModel(UITable).setColumnVOs(getColumnVOs());
        getTableColumnModel(UITable).adjustColumnShowSequence();
        setTableColumn(UITable, isDynamicCol);

    }

    /**
     * 
     */
    private void initTableModel(UITable table) {

        Vector vDataAll = new Vector();
        nc.ui.pub.beans.table.NCTableModel tm = new RefNCDataTableModle(vDataAll, getAllColumnNames());

        tm.setAndEditable(true);
        table.setModel(tm);
        table.setColumnModel(new MMGPRefTableColumnModel(getRefModel()));
        table.createDefaultColumnsFromModel();

    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-25 18:29:08)
     * 
     * @return java.util.Vector
     */
    private Vector getAllColumnNames() {

        if (m_vecAllColumnNames == null || m_vecAllColumnNames.size() == 0) {
            m_vecAllColumnNames = new Vector();
            if (getRefModel().getFieldCode() != null && getRefModel().getFieldCode().length > 0) {
                if (getRefModel().getFieldName() == null || getRefModel().getFieldName().length == 0) {
                    for (int i = 0; i < getRefModel().getFieldCode().length; i++) {
                        m_vecAllColumnNames.addElement(getRefModel().getFieldCode()[i]);
                    }
                } else {
                    if (getRefModel().getFieldName().length >= getRefModel().getFieldCode().length) {
                        for (int i = 0; i < getRefModel().getFieldCode().length; i++) {
                            m_vecAllColumnNames.addElement(getRefModel().getFieldName()[i]);
                        }
                    } else {
                        for (int i = 0; i < getRefModel().getFieldName().length; i++) {
                            m_vecAllColumnNames.addElement(getRefModel().getFieldName()[i]);
                        }
                        for (int i = getRefModel().getFieldName().length; i < getRefModel().getFieldCode().length; i++) {
                            m_vecAllColumnNames.addElement(getRefModel().getFieldCode()[i]);
                        }
                    }
                }

            }

            // 加入隐藏列
            if (getRefModel().getHiddenFieldCode() != null) {
                for (int i = 0; i < getRefModel().getHiddenFieldCode().length; i++) {
                    m_vecAllColumnNames.addElement(getRefModel().getHiddenFieldCode()[i]);
                }
            }
            sourceColNum = m_vecAllColumnNames.size();

            // 加入动态列
            if (getRefModel().isDynamicCol()) {
                // String[][] dynamicColName =
                // dynamicColClass.getDynaminColNameAndLoc();
                String[] dynamicColNames = getRefModel().getDynamicFieldNames();
                if (getRefModel().getDynamicFieldNames() != null) {

                    for (int i = 0; i < dynamicColNames.length; i++) {

                        // 加入到显示的列名中
                        m_vecAllColumnNames.addElement(dynamicColNames[i]);
                    }
                }
            }
        }
        return m_vecAllColumnNames;
    }

    private void initTableTableEnv(MMGPRefTablePane tablePane,
                                   boolean isDynamic) {
        UITable table = tablePane.getTable();
        initTableTableModel(table);
        if (isDynamic) {
            MMGPRefTableColumnModel cm = (MMGPRefTableColumnModel) table.getColumnModel();
            cm.setDynamicColumnNames(getRefModel().getDynamicFieldNames());
        }
        if (!isMultiSelectedRef()) table.addSortListener();
        setNewTableColumnSequence(table);
        tablePane.initRowHeader();
        table.setSelectionMode(javax.swing.DefaultListSelectionModel.SINGLE_SELECTION);

    }

    /*
     * 设置定位Combox,表的栏目的显示顺序
     */
    private void setNewTableColumnSequence(UITable UITable) {

        boolean isDynamicCol = getRefModel().isDynamicCol();

        getTableColumnModel(UITable).setColumnVOs(getTableColumnVOs());
        getTableColumnModel(UITable).adjustColumnShowSequence();
        setTableColumn(UITable, isDynamicCol);

    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-27 8:51:50)
     */
    private void setTableColumn(UITable table,
                                boolean isDynamicCol) {

        if (table.getColumnCount() < 4) {
            table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else {
            int[] colWidth = new int[table.getColumnCount()];
            int tableWidth = setSpecialTableColumnWidth(table, isDynamicCol, colWidth);
            fillBlankSpace(table, tableWidth, colWidth);
            table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            table.setColumnWidth(colWidth);
        }
        table.sizeColumnsToFit(-1);
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
    private void fillBlankSpace(UITable table,
                                int tableWidth,
                                int[] colWidth) {
        // 填充空白位置
        int dialogWidth = (int) getSplpane_Data().getPreferredSize().getWidth();
        int talbleTotalWidth = tableWidth + MMGPRefTablePane.ROWHEADERWIDTH + 10;
        if (talbleTotalWidth < dialogWidth) {

            for (int i = 0; i < table.getColumnCount(); i++) {
                colWidth[i] += (dialogWidth - talbleTotalWidth) / table.getColumnCount();

            }
        }
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
    private int setSpecialTableColumnWidth(UITable table,
                                           boolean isDynamicCol,
                                           int[] colWidth) {

        String nameField = getRefModel().getFieldShowName(getRefModel().getRefNameField());
        String columnName = "";
        String name_lang = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001155");
        /** @res "名称" */
        int tableWidth = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {

            // 有动态列，减少各列的宽度。
            columnName = table.getColumnName(i).toString();
            if (isDynamicCol) {
                if (columnName.indexOf(name_lang) > -1 || columnName.equals(nameField)) {
                    colWidth[i] = 100;

                } else {
                    colWidth[i] = 60;
                }
            } else {
                // 加大名称列默认长度105->200
                // sxj 2003-02-24
                if (columnName.indexOf(name_lang) > -1 || columnName.equals(nameField)) {
                    colWidth[i] = 140;// 200;

                } else {
                    colWidth[i] = 100;
                }
            }
            tableWidth += colWidth[i];
        }
        return tableWidth;
    }

    private MMGPRefTableColumnModel getTableColumnModel(UITable table) {
        return (MMGPRefTableColumnModel) table.getColumnModel();
    }

    /**
     * @return 返回 columnVOs。
     */
    private RefcolumnVO[] getTableColumnVOs() {

        if (tableColumnVOs == null) {
            tableColumnVOs = MMGPRefPubUtil.getTableColumnSequences(getRefModel());
            // if (columnVOs != null) {
            // setDefRefName();
            // }

        }
        return tableColumnVOs;
    }

    /**
     * @return 返回 columnVOs。
     */
    private RefcolumnVO[] getColumnVOs() {

        if (columnVOs == null) {
            columnVOs = MMGPRefPubUtil.getColumnSequences(getRefModel());
            // if (columnVOs != null) {
            // setDefRefName();
            // }

        }
        return columnVOs;
    }

    /**
     * @param columnVOs
     *        要设置的 columnVOs。
     */
    private void setColumnVOs(RefcolumnVO[] columnVOs) {
        this.columnVOs = columnVOs;
    }

    /**
     * 
     */
    private void initTableTableModel(UITable table) {

        Vector vDataAll = new Vector();
        nc.ui.pub.beans.table.NCTableModel tm = new RefNCDataTableModle(vDataAll, getAllTableColumnNames());

        tm.setAndEditable(true);
        table.setModel(tm);
        table.setColumnModel(new MMGPRefTableColumnModel(getRefModel()));
        table.createDefaultColumnsFromModel();

    }

    public Vector<String> getAllTableColumnNames() {

        if (vecAllTableColumnNames != null) {
            return vecAllTableColumnNames;
        }

        vecAllTableColumnNames = new Vector<String>();
        if (getEMRefModel().getTableFieldCode() != null && getEMRefModel().getTableFieldCode().length > 0) {
            if (getEMRefModel().getTableFieldName() == null || getEMRefModel().getTableFieldName().length == 0) {
                for (int i = 0; i < getEMRefModel().getTableFieldCode().length; i++) {
                    vecAllTableColumnNames.addElement(getEMRefModel().getTableFieldCode()[i]);
                }
            } else {
                if (getEMRefModel().getTableFieldName().length >= getEMRefModel().getTableFieldCode().length) {
                    for (int i = 0; i < getEMRefModel().getTableFieldCode().length; i++) {
                        vecAllTableColumnNames.addElement(getEMRefModel().getTableFieldName()[i]);
                    }
                } else {
                    for (int i = 0; i < getEMRefModel().getTableFieldName().length; i++) {
                        vecAllTableColumnNames.addElement(getEMRefModel().getTableFieldName()[i]);
                    }
                    for (int i = getEMRefModel().getTableFieldName().length; i < getEMRefModel().getTableFieldCode().length; i++) {
                        vecAllTableColumnNames.addElement(getEMRefModel().getTableFieldCode()[i]);
                    }
                }
            }

        }

        if (getEMRefModel().getTableHiddenFieldCode() != null)
            for (int i = 0; i < getEMRefModel().getTableHiddenFieldCode().length; i++) {
                vecAllTableColumnNames.addElement(getEMRefModel().getTableHiddenFieldCode()[i]);
            }

        tablesourceColNum = vecAllTableColumnNames.size();

        // 加入动态列
        // if (isDynamicCol()) {
        //
        // String[] dynamicColNames = getDynamicFieldNames();
        // if (getDynamicFieldNames() != null) {
        //
        // for (int i = 0; i < dynamicColNames.length; i++) {
        //
        // // 加入到显示的列名中
        // vecAllColumnNames.addElement(dynamicColNames[i]);
        // }
        // }
        // }
        return vecAllTableColumnNames;
    }

    /**
     * 
     */
    private IDynamicColumn initDynamicColClass() {

        String className = getRefModel().getDynamicColClassName();
        // 是否实现接口检查
        IDynamicColumn newDynamicClass = null;
        try {
            newDynamicClass = (IDynamicColumn) Class.forName(className).newInstance();
        } catch (Exception e) {
            return null;

        }

        return newDynamicClass;
    }

    /**
     * 返回 UIButAdd 特性值。
     * 
     * @return nc.ui.pub.beans.UIButton
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UIButton getUIButAdd() {
        return getButtonPanelFactory().getBtnAdd();
    }

    private ToolBarPanel getToolBarPnl() {
        return getButtonPanelFactory().getPnl_btn(true, true, true);
    }

    private RefActions getActions() {
        return getButtonPanelFactory().getActions();
    }

    private UICheckBox getChkSealedDataShow() {

        return getButtonPanelFactory().getChkSealedDataShow();
    }

    public void setAddonsButtons(AbstractButton[] addonsButtons) {
        this.addonsButtons = addonsButtons;
    }

    public AbstractButton[] getAddonsButtons() {
        return addonsButtons;
    }

    private void addAddonButtons() {
        if (getAddonsButtons() != null) {
            AbstractButton btn = null;
            for (int i = 0; i < getAddonsButtons().length; i++) {
                btn = getAddonsButtons()[i];
                btn.setPreferredSize(new java.awt.Dimension(70, 20));
                btn.setOpaque(true);
                getPnl_addonButton().add(btn);
            }
        }
    }

    private UIPanel getPnl_addonButton() {
        return getButtonPanelFactory().getPnl_addonButton();
    }

    private void setFilterRefSize() {
        // 设置参照大小
        Dimension dim =
                RefUtil.getRefSize(getRefModel().getRefNodeName(), new Dimension(width * 7 / 10, height * 7 / 10));
        width = (int) dim.getWidth();
        height = (int) dim.getHeight();
        // setSize(width, height);
        setSizeNoChange(width, height);
    }

    /**
     * 返回 UIDialogContentPane 特性值。
     * 
     * @return javax.swing.JPanel
     */
    /* 警告：此方法将重新生成。 */
    protected javax.swing.JPanel getUIDialogContentPane() {
        if (uIDialogContentPane == null) {

            uIDialogContentPane = new UIPanel();
            uIDialogContentPane.setName("UIDialogContentPane");
            uIDialogContentPane.setBorder(new EmptyBorder(5, 5, 0, 5));
            uIDialogContentPane.setLayout(new java.awt.BorderLayout());

            uIDialogContentPane.add(getButtonPanelFactory().getPnl_North(), BorderLayout.NORTH);

            uIDialogContentPane.add(getPnl_center(), BorderLayout.CENTER);

            uIDialogContentPane.add(getPnl_south(), BorderLayout.SOUTH);
        }
        return uIDialogContentPane;
    }

    private UIPanel getPnl_center() {
        if (pnl_center == null) {
            pnl_center = new UIPanel();
            pnl_center.setLayout(new BorderLayout());
            if (isMultiSelectedRef()) {
                pnl_center.add(getPnl_miniPanel1(), BorderLayout.SOUTH);
                if (getEMRefModel().getCommonDataTableName() == null) {
                    pnl_center.add(getSplPane_all(), "Center");
                } else {
                    pnl_center.add(getTbPane(), "Center");
                }
                // // 把已选去掉
                // pnl_Data.removeAll();
                // pnl_Data.add(getPnl_locate_btn(), BorderLayout.NORTH);
                // pnl_Data.add(getPnl_TableData(), "Center");

            } else {
                if (getEMRefModel().getCommonDataTableName() == null) {
                    pnl_center.add(getSplPane_all(), "Center");
                } else {
                    pnl_center.add(getTbPane(), "Center");
                }
            }
        }
        return pnl_center;
    }

    /**
     * 返回 UISplpane2 特性值。
     * 
     * @return nc.ui.pub.beans.UISplitPane
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UISplitPane getSplpane_Data() {
        if (splpane_Data == null) {
            splpane_Data = new nc.ui.pub.beans.UISplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
            splpane_Data.setName("splpane_Data");
            splpane_Data.setPreferredSize(new java.awt.Dimension(width * 76 / 100, height - 90));
            splpane_Data.setDividerLocation((height - 90) / 2 + 50);
            splpane_Data.add(getTbPane(), "top");
            splpane_Data.add(getPnlSelectedTable(), "bottom");
            ((javax.swing.plaf.basic.BasicSplitPaneUI) splpane_Data.getUI()).getDivider().setBorder(null);
            splpane_Data.setBorder(null);
        }
        return splpane_Data;
    }

    private MiniPanel getPnl_miniPanel1() {
        if (pnl_miniPanel1 == null) {
            SplitpanelInfo info = new SplitpanelInfo();
            info.setOrientation(2);// 纵向垂直分割
            info.setSplietPane(getSplpane_Data());
            info.setMiniOrientation(SplitpanelInfo.MINI_BOTTOM);// 默认为居于底部
            info.setOpener(new IMiniPanelOpener() {

                @Override
                public void doResetAction() {
                    pnl_center.removeAll();

                    splpane_Data = new nc.ui.pub.beans.UISplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
                    splpane_Data.setName("splpane_Data");
                    splpane_Data.setPreferredSize(new java.awt.Dimension(width * 76 / 100, height - 90));
                    splpane_Data.setDividerLocation((height - 90) / 2 + 50);
                    if (getEMRefModel().getCommonDataTableName() == null) {
                        splpane_Data.add(getSplPane_all(), "top");
                    } else {
                        splpane_Data.add(getTbPane(), "top");
                    }
                    splpane_Data.add(getPnlSelectedTable(), "bottom");
                    ((javax.swing.plaf.basic.BasicSplitPaneUI) splpane_Data.getUI()).getDivider().setBorder(null);
                    splpane_Data.setBorder(null);
                    isShowSeletedDataTable = true;

                    pnl_SelectedTable.removeAll();
                    getPnlSelectedTable().add(getPnl_miniPanel2(), BorderLayout.NORTH);
                    getPnlSelectedTable().add(getTbP_selectedData(), BorderLayout.CENTER);
                    pnl_center.add(splpane_Data, "Center");
                    pnl_center.revalidate();
                    pnl_center.repaint();
                }

                @Override
                public void doMiniIconAction() {
                }
            });
            pnl_miniPanel1 = new MiniPanel(info);
            pnl_miniPanel1.initUI();

        }
        return pnl_miniPanel1;
    }

    public void addRecordToSelectedTable(Vector vDataTable) {
        if (vDataTable == null) {
            return;
        }
        int pkIndex = getPKIndex();
        if (vDataTable != null && vDataTable.size() > 0) {

            Vector record = null;
            for (int i = 0; i < vDataTable.size(); i++) {

                record = (Vector) vDataTable.get(i);
                if (pkIndex < record.size()
                    && record.get(pkIndex) != null
                    && getHtselectedVector().get(record.get(pkIndex).toString()) == null) {

                    getTbP_selectedData().addRow(record);
                    getHtselectedVector().put(record.get(pkIndex).toString(), record);
                }
            }

        }
        if (this.getUIButtonOK().isEnabled() == false) {
            this.getUIButtonOK().setEnabled(true);
        }
    }

    public void removeRecordFromSelectedTable(Vector vDataTable) {
        if (vDataTable == null) {
            return;
        }
        int pkIndex = getPKIndex();
        if (vDataTable.size() > 0) {

            Vector record = null;
            for (int i = 0; i < vDataTable.size(); i++) {
                record = (Vector) vDataTable.get(i);
                if (pkIndex < record.size()) {
                    Object selectedPK = record.get(pkIndex);
                    for (int j = 0; j < getTbP_selectedData().getTable().getModel().getRowCount(); j++) {
                        Object value = getTbP_selectedData().getTable().getModel().getValueAt(j, pkIndex);
                        if (value.toString().equalsIgnoreCase(selectedPK.toString())) {
                            getTbP_selectedData().removeRow(j);
                            getHtselectedVector().remove(selectedPK.toString());
                            break;
                        }
                    }
                }
            }
        }
        if (getHtselectedVector() == null || getHtselectedVector().size() == 0) {
            this.getUIButtonOK().setEnabled(false);
        }
    }

    private int getPKIndex() {

        return getRefModel().getFieldIndex(getRefModel().getPkFieldCode());
    }

    private MiniPanel getPnl_miniPanel2() {
        if (pnl_miniPanel2 == null) {
            SplitpanelInfo info = new SplitpanelInfo();
            info.setOrientation(2);// 纵向垂直分割
            info.setMiniOrientation(SplitpanelInfo.MINI_MIDDLE);// 默认为居中
            info.setOpener(new IMiniPanelOpener() {

                @Override
                public void doResetAction() {
                }

                @Override
                public void doMiniIconAction() {
                    pnl_center.removeAll();
                    pnl_center.add(getPnl_miniPanel1(), BorderLayout.SOUTH);
                    if (getEMRefModel().getCommonDataTableName() == null) {
                        pnl_center.add(getSplPane_all(), "Center");
                    } else {
                        pnl_center.add(getTbPane(), "Center");
                    }
                    isShowSeletedDataTable = false;
                    // 把已选去掉
                    // getPnlData().remove(getSplpane_Data());
                    // getPnlData().add(getPnl_TableData(), "Center");
                    pnl_center.revalidate();
                    pnl_center.repaint();
                }
            });
            pnl_miniPanel2 = new MiniPanel(info);
            pnl_miniPanel2.initUI();
        }
        return pnl_miniPanel2;
    }

    /*
     * 返回 UIPnlSelectTable 特性值。
     * @return nc.ui.pub.beans.UIPanel
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UIPanel getPnlSelectedTable() {

        if (pnl_SelectedTable == null) {

            pnl_SelectedTable = new nc.ui.pub.beans.UIPanel();
            pnl_SelectedTable.setBorder(new EmptyBorder(1, 2, 1, 1));
            pnl_SelectedTable.setName("pnl_SelectedTable");
            pnl_SelectedTable.setLayout(new java.awt.BorderLayout());
            pnl_SelectedTable.add(getTbP_selectedData(), "Center");

        }
        return pnl_SelectedTable;
    }

    private UITabbedPane getTbPane() {
        if (tbPane == null) {
            tbPane = new UITabbedPane();
            tbPane.add(getCommDataPanel(), NCLangRes
                .getInstance()
                .getStrByID("ref", "UFRefGridTreeCommDataUI-000008")/* 我的常用 */);
            tbPane
                .add(getSplPane_all(), NCLangRes.getInstance().getStrByID("ref", "UFRefGridTreeCommDataUI-000009")/* 全部数据 */);
            tbPane.addChangeListener(this);
        }
        return tbPane;
    }

    public void stateChanged(ChangeEvent e) {
        if (getTbPane().getSelectedIndex() == 0) {
            // getRefModel().getRefQueryVO().setPage(false);
            getButtonPanelFactory().getTfCommonDataLocate().requestFocus();
        } else {
            this.getTreePanel().getTree().requestFocus();
        }

        setFocusTraversalPolicy();
    }

    private UIPanel getCommDataPanel() {
        if (commDataPanel == null) {
            commDataPanel = new UIPanel();
            commDataPanel.setLayout(new BorderLayout());
            commDataPanel.add(getTbP_commonData(), BorderLayout.CENTER);
            commDataPanel.add(getButtonPanelFactory().getPnl_btnDel(), BorderLayout.NORTH);

        }
        return commDataPanel;
    }

    private MMGPRefGridData getTbP_commonData() {
        if (tbP_commonData == null) {
            tbP_commonData = new MMGPRefGridData(this, isMultiSelectedRef());
            tbP_commonData.setRefModel(getRefModel());
            tbP_commonData.setName("commonData");
            tbP_commonData.addListener(RefEventConstant.DATAROW_CHECKED_CHANGED, rowCheckedChangedHandler);
            addListener(RefEventConstant.DEL_SELECTED_ROWS, tbP_commonData.delSelectedRowHandler);
            tbP_commonData.setBorder(new EmptyBorder(1, 1, 1, 1));
        }
        return tbP_commonData;
    }

    // 待选table行CheckBox发生勾选操作时
    private IEventListener rowCheckedChangedHandler = new IEventListener() {
        public void invoke(Object sender,
                           NCEventObject evt) {
            if (evt.getProperty(RefEventConstant.SELECTED_DATA) != null) {
                Vector vec = (Vector) evt.getProperty(RefEventConstant.SELECTED_DATA);
                if (evt.getProperty(RefEventConstant.IS_ADD_ROWS) != null) {
                    if (Boolean.TRUE == evt.getProperty(RefEventConstant.IS_ADD_ROWS)) {
                        // getDynamicColData(vec);
                        addRecordToSelectedTable(vec);
                    } else {
                        removeRecordFromSelectedTable(vec);
                        getTbP_commonData().setCheckBoxNoChecked(getSelectedPKs(vec));
                    }
                    if (sender == tbP_commonData) {
                        getTreePanel().setMatchedPKs(getSelectedPKs());
                    } else if (sender == tbP_data) {
                        getTbP_commonData().setMatchedPKs(getSelectedPKs());
                    }
                }
            }
        }
    };

    private String[] getSelectedPKs(Vector matchVec) {
        if (matchVec != null) {
            int pkIndex = getPKIndex();
            String[] pks = new String[matchVec.size()];
            for (int i = 0; i < matchVec.size(); i++) {
                Object rec = matchVec.get(i);
                pks[i] = (String) ((Vector) rec).get(pkIndex);
            }
            return pks;
        }
        return null;
    }

    /**
     * 返回 UITablePane1 特性值。
     * 
     * @return nc.ui.pub.beans.UITablePane
     */
    /* 警告：此方法将重新生成。 */
    protected MMGPRefGridSelectedData getTbP_selectedData() {
        if (tbP_selectedData == null) {
            tbP_selectedData = new MMGPRefGridSelectedData();
            tbP_selectedData.setBorder(null);
        }
        return tbP_selectedData;
    }

    // 已选table删除行时
    private IEventListener delSelectedRowHandler = new IEventListener() {
        public void invoke(Object sender,
                           NCEventObject evt) {
            if (evt.getProperty(RefEventConstant.SELECTED_ROWS) != null) {
                onDelFromSelected((int[]) evt.getProperty(RefEventConstant.SELECTED_ROWS));
                getTreePanel().getTree().repaint();
            }
        }
    };

    protected void onDelFromSelected(int[] rows) {
        int pkIndex = getPKIndex();

        if (rows != null && rows.length > 0) {
            ArrayList<String> list = new ArrayList<String>();
            for (int i = rows.length - 1; i >= 0; i--) {

                Object value = getTbP_selectedData().getTable().getModel().getValueAt(rows[i], pkIndex);
                if (value != null) {
                    // 被选择的表中已经有了该记录
                    getHtselectedVector().remove(value.toString());
                    list.add(value.toString());
                }

                getTbP_selectedData().removeRow(rows[i]);

            }
            eventSource.fireEvent(new NCEventObject(
                RefEventConstant.DEL_SELECTED_ROWS,
                RefEventConstant.SELECTED_PKS,
                list.toArray(new String[list.size()])));
        }
        getTbP_selectedData().getTable().requestFocus();
        if (getHtselectedVector() == null || getHtselectedVector().size() == 0) {
            this.getUIButtonOK().setEnabled(false);
        }
    }

    /**
     * 返回 UIPanel2 特性值。
     * 
     * @return nc.ui.pub.beans.UIPanel
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UIPanel getPnlData() {
        if (pnl_Data == null) {

            pnl_Data = new nc.ui.pub.beans.UIPanel();
            pnl_Data.setName("pnl_Data");
            pnl_Data.setLayout(new java.awt.BorderLayout());
            if (isMultiSelectedRef()) {
                pnl_Data.add(getPnl_miniPanel1(), BorderLayout.SOUTH);
            }

            pnl_Data.add(getSplPane_all(), BorderLayout.CENTER);
        }
        return pnl_Data;
    }

    /**
     * 返回 UISplitPane1 特性值。
     * 
     * @return nc.ui.pub.beans.UISplitPane
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UISplitPane getSplPane_all() {
        if (splPane_all == null) {

            splPane_all = new nc.ui.pub.beans.UISplitPane(1);
            splPane_all.setName("UISplitPane1");
            splPane_all.setPreferredSize(new java.awt.Dimension(width, 0));
            splPane_all.setDividerLocation(width * 24 / 100);
            splPane_all.setResizeWeight(0.6);
            // splPane_all.add(getSclPane(), "left");
            // 调整加载顺序
            splPane_all.add(getPnl_TableData(), "left");
            splPane_all.add(getTreePanel_data(), "right");

            if (getEMRefModel().getCommonDataTableName() != null) {
                splPane_all.setBorder(null);
            } else {
                splPane_all.setBorder(BorderFactory.createLineBorder(Style.getNCBorderColor()));
                // ((javax.swing.plaf.basic.BasicSplitPaneUI)
                // splPane_all.getUI()).getDivider().setBorder(null);
                // splPane_all.setBorder(null);
            }

        }
        return splPane_all;
    }

    private UIPanel getTreePanel_data() {
        if (pnlTree_data == null) {
            pnlTree_data = new nc.ui.pub.beans.UIPanel();
            pnlTree_data.setName("pnlTree_data");
            pnlTree_data.setLayout(new java.awt.BorderLayout());
            pnlTree_data.add(getPnl_Tree_btn(), BorderLayout.NORTH);
            pnlTree_data.add(getTreePanel(), BorderLayout.CENTER);

        }
        return pnlTree_data;
    }

    /**
     * @return 返回 pnl_locate_btn。
     */
    public UIPanel getPnl_Tree_btn() {
        if (pnl_Tree_btn == null) {
            pnl_Tree_btn = new UIPanel();
            pnl_Tree_btn.setName("pnl_locate_btn");
            pnl_Tree_btn.setLayout(new BorderLayout());
            pnl_Tree_btn.add(getPnl_btn(), "East");

            pnl_Tree_btn.setBackground(Style.getDlgBgColor());
        }

        return pnl_Tree_btn;
    }

    private ToolBarPanel getPnl_btn() {
        return getButtonPanelFactory().getPnl_btn(false, true, false);
    }

    /**
     * @return 返回 pnl_locate_btn。
     */
    private UIPanel getPnl_locate_btn() {

        return getButtonPanelFactory().getPnl_locate(true);
    }

    private MMGPTreePanel getTreePanel() {
        if (treePanel == null) {
            treePanel =
                    new MMGPTreePanel(
                        new java.awt.Dimension(width * 24 / 100, 0),
                        (MMGPTreeGridRefModel) this.getRefModel(),
                        this);

        }
        return treePanel;
    }

    public void addListener(String eventName,
                            IEventListener listener) {
        eventSource.addListener(eventName, listener);
    }

    /*
     * 返回 UIPnlTblAndLocate 特性值。
     * @return nc.ui.pub.beans.UIPanel
     */
    /* 警告：此方法将重新生成。 */
    private nc.ui.pub.beans.UIPanel getPnl_TableData() {
        if (pnlTable_data == null) {

            pnlTable_data = new nc.ui.pub.beans.UIPanel();
            pnlTable_data.setBorder(new EmptyBorder(1, 2, 1, 1));
            pnlTable_data.setName("pnlTable_data");
            pnlTable_data.setPreferredSize(new java.awt.Dimension(0, (height - 90) / 2));
            pnlTable_data.setLayout(new java.awt.BorderLayout());
            pnlTable_data.add(getPnl_locate_btn(), BorderLayout.NORTH);
            pnlTable_data.add(getDataTable(), "Center");

        }
        return pnlTable_data;
    }

    private MMGPRefGridData getDataTable() {
        if (tbP_data == null) {
            tbP_data = new MMGPRefGridData(this, false);
        }
        return tbP_data;
    }

    private nc.ui.pub.beans.UIPanel getPnl_south() {

        return getButtonPanelFactory().getPnl_south(false);
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
    private void setDefaultSize() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        if (size.height < 769) {
            width = size.width * 78 / 100;
            height = size.height * 70 / 100;
        } else {
            width = size.width * 64 / 100;
            height = size.height * 56 / 100;
        }

        // setSize(width, height);
        setSizeNoChange(width, height);
    }

    private void setUserSize() {
        // 设置参照大小
        Dimension dim = RefUtil.getRefSize(getRefModel().getRefNodeName(), new Dimension(width, height));
        width = (int) dim.getWidth();
        height = (int) dim.getHeight();
        // setSize(width, height);
        setSizeNoChange(width, height);
    }

    private void setShowSelectedDataTable() {
        // 从持久化设置是否显示已选数据表
        if (isMultiSelectedRef()) {
            isShowSeletedDataTable = RefUtil.getRefIsShowSelectedDataTable(getRefModel().getRefNodeName());
        }
    }

    protected boolean isMultiSelectedRef() {
        return getRefUIConfig().isMutilSelected()
            || getRefUIConfig().isTreeGridNodeMultiSelected()
            || isMultiOrgSelected();
    }

    /**
     * @return 返回 buttonPanelFactory。
     */
    protected RefButtonPanelFactory getButtonPanelFactory() {
        if (buttonPanelFactory == null) {
            buttonPanelFactory = new RefButtonPanelFactory(getRefModel(), this);
        }
        return buttonPanelFactory;
    }

    @Override
    public void setRefUIConfig(RefUIConfig refUIConfig) {
        this.refUIConfig = refUIConfig;

    }

    public MMGPTreeGridRefModel getEMRefModel() {
        return refmodel;
    }

    @Override
    public AbstractRefModel getRefModel() {
        return refmodel;
    }

    /**
	 * 
	 */
    private boolean isIncludeSubShow() {
        return isIncludeSubShow;
    }

    @Override
    public void setIncludeSubShow(boolean newIncludeSubShow) {
        isIncludeSubShow = newIncludeSubShow;
    }

    @Override
    public void setMultiCorpRef(boolean isMultiCorpRef) {
        this.isMultiCorpRef = isMultiCorpRef;
    }

    @Override
    public void setMultiSelectedEnabled(boolean isMultiSelectedEnabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNotLeafSelectedEnabled(boolean newNotLeafSelectedEnabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRefModel(AbstractRefModel refModel) {
        this.refmodel = (MMGPTreeGridRefModel) refModel;
    }

    @Override
    public void setTreeGridNodeMultiSelected(boolean isMulti) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFilterDlgShow(boolean isFilterDlgShow) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVersionButtonShow(boolean isVersionButtonShow) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object[] getUISelectedPKs() {
        return getRefModel().getValues(getRefModel().getPkFieldCode(), this.getTreePanel().getSelectData());
    }

    @Override
    public RefcolumnVO[] getRefcolumnVOs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMultiOrgSelected() {
        return getRefUIConfig().isMultiCorpRef() && getRefUIConfig().isMultiOrgSelected();
    }

    @Override
    public RefUIConfig getRefUIConfig() {
        return refUIConfig;
    }

    @Override
    public void onAdd() {

        // 添加到选择表中
        addRecordToSelectedTable(this.getTreePanel().getSelectData());
        getTbP_selectedData().getTable().requestFocus();
        return;

    }

    @Override
    public void onButtonOK() {
        if (isMultiSelectedRef() && !isSelectedData()) {
            processSingleNodeSelected();// 在点确定时，如果没有已选数据，若Table中行有焦点，则认为也是选中该行
        }
        // tangxya add 包含下级时获取下级的的值
        // gaotx 2014-11-27 改为只从选中中处理
        if (isMultiSelectedRef()) {
            UITable table = getTbP_selectedData().getTable();
            int rowCount = table.getRowCount();
            int[] selectedRows = null;
            if (rowCount > 0) {
                selectedRows = new int[rowCount];
                for (int i = 0; i < selectedRows.length; i++) {
                    selectedRows[i] = i;
                }
            }
            getRefModel().setSelectedData(getSelectedVector(table, selectedRows));

        } else {
            if (this.isCommonDataRef() && getTbPane().getSelectedIndex() == 0) getRefModel().setSelectedData(
                getSelectedCommData(false));
            else
                getRefModel().setSelectedData(this.getTreePanel().getSelectData());
        }
        closeOK();
    }

    private Vector getSelectedCommData(boolean isCommDataModify) {

        UITable table = getTbP_commonData().getTable();

        int[] selectedRows = getSelectRows(table, isCommDataModify);

        return getSelectedVector(table, selectedRows);

    }

    // isCommDataModify 是否是常用数据增加或删除数据
    private int[] getSelectRows(UITable table,
                                boolean isCommDataModify) {
        // 如果是常用数据增加或删除数据，返回多选数据
        if (isCommDataModify) {
            return table.getSelectedRows();
        }

        int[] selectedRows = null;

        if (isMultiSelectedRef()) {
            selectedRows = table.getSelectedRows();
        } else {

            int index = -1;
            // index = table.getSelectionModel().getAnchorSelectionIndex();
            index = table.getSelectionModel().getLeadSelectionIndex();
            boolean isSelectIndex = table.getSelectionModel().isSelectedIndex(index);

            if (!isSelectIndex) {
                index = table.getSelectionModel().getMinSelectionIndex();
            }
            if (index != -1) {
                selectedRows = new int[]{index };
            }
        }
        return selectedRows;
    }

    private Vector getSelectedVector(UITable table,
                                     int[] selectedRows) {
        Vector vSelectedData = null;

        if (selectedRows != null && selectedRows.length > 0) {
            vSelectedData = new Vector();
            for (int i = 0; i < selectedRows.length; i++) {
                Vector vRecord = new Vector();
                for (int j = 0; j < table.getModel().getColumnCount(); j++) {
                    vRecord.addElement(table.getModel().getValueAt(selectedRows[i], j));
                }
                vSelectedData.addElement(vRecord);
            }
        }
        return vSelectedData;
    }

    private void processSingleNodeSelected() {
        this.getTreePanel().dealSelectionOnButtonOK();
    }

    protected boolean isSelectedData() {
        if (getHtselectedVector() != null && getHtselectedVector().size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onButtonExit() {
        closeCancel();
        return;
    }

    @Override
    public void valueChanged(ValueChangedEvent event) {
        getRefModel().filterValueChanged(event);
        getDataTable().getTable().clearSelection();
        if (isCommonDataRef()) {
            initCommDataTable();
        }
        onRefresh();
        this.getTreePanel().clearTreeData();
    }

    /**
     * Comment
     */
    public void onRefresh() {
        refreshTfLoacate();
        if (getRefModel() == null) return;

        UITable table = (UITable) this.getDataTable().getTable();
        int row = table.getSelectedRow();
        Vector vRecord = new Vector();
        String key = null;
        for (int j = 0; j < tablesourceColNum; j++) {
            Object value = getDataTable().getTable().getModel().getValueAt(row, j);
            vRecord.addElement(value);
        }
        String joinvalue = this.getTableJoinValue(vRecord);

        getRefModel().reloadData1();
        Vector vDataTree = this.getEMRefModel().reloadTableData();
        if (vDataTree == null) {
            vDataTree = new Vector();
        }
        this.getDataTable().setData(vDataTree);
        if (MMStringUtil.isNotEmpty(joinvalue)) {
            this.getDataTable().setMatchedPKs(new String[]{joinvalue }, true);
        } else {
            this.getTreePanel().clearTreeData();
        }
        RefRecentRecordsUtil.clear(getRefModel());
    }

    public void onQuery() {
        MMGPTreeGridRefModel refModel = (MMGPTreeGridRefModel) getRefModel();
        String className = getRefModel().getRefQueryDlgClaseName();
        Object interfaceClass = null;
        IRefQueryDlg queryDlg = null;
        // 是否实现接口检查
        try {
            Class modelClass = Class.forName(className);
            java.lang.reflect.Constructor cs = null;
            try { // 用公司做构造子
                cs = modelClass.getConstructor(new Class[]{Container.class });
                interfaceClass = cs.newInstance(new Object[]{this });
            } catch (NoSuchMethodException ee) { // 缺省构造
                interfaceClass = modelClass.newInstance();
            }
        } catch (Exception e) {
            Debug.error(e.getMessage(), e);
            return;
        }
        // 类型转换
        if (interfaceClass == null) {
            return;
        }
        if (interfaceClass instanceof IRefQueryDlg) {
            queryDlg = (IRefQueryDlg) interfaceClass;
            if (interfaceClass instanceof IRefQueryDlg2) {
                ((IRefQueryDlg2) queryDlg).setRefModel(getRefModel());
            }
        } else {
            MessageDialog.showErrorDlg(
                this,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("ref", "UPPref-000341")/*
                                                                                    * @res "错误"
                                                                                    */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("ref", "UPPref-000366")/*
                                                                                    * @res
                                                                                    * "未实现IRefQueryDlg或IRefQueryDlg2接口"
                                                                                    */);
            return;
        }
        // 显示对话框
        queryDlg.setParent(this);
        queryDlg.setPk_corp(getRefModel().getPk_corp());

        if (queryDlg.showModal() == UIDialog.ID_OK) {
            RefRecentRecordsUtil.clear(getRefModel());
            if (refModel.isTableQuery()) {
                refModel.setTableQuerySql(null);
            } else {
                refModel.setQuerySql(null);
            }
            if (refModel.isTableQuery()) {
                refModel.setTableQuerySql(queryDlg.getConditionSql());
                Vector vDataAll = refModel.getTableData();
                if (vDataAll == null) {
                    vDataAll = new Vector();
                }
                //
                this.getDataTable().setData(vDataAll);
            } else {
                refModel.setQuerySql(queryDlg.getConditionSql());
                getEMRefModel().getCompositeTreeModel();
            }
        }

    }

    private void refreshTfLoacate() {
        getButtonPanelFactory().refreshTfLoacate(true);
    }

    private void initCommDataTable() {
        Vector vCommData = getCommonData(false);
        // 如果数据为空，设置档案页签为当前页
        if (vCommData == null || vCommData.size() == 0) {
            getTbPane().setSelectedIndex(1);
            isCommonDataMatchPk = false;
            getTbP_commonData().setData(vCommData);
            getTbP_commonData().setVDataAll(vCommData);
            return;
        } else {
            getTbPane().setSelectedIndex(0);
        }
        getTbP_commonData().setData(vCommData);
        getTbP_commonData().setVDataAll(vCommData);

        // 如果有选中的数据，定位。
        int[] rowIndexes = getRecordIndexes(vCommData);
        setSelectionRows(getTbP_commonData().getTable(), rowIndexes);

        isCommonDataMatchPk = true;

        initPkRecordMap(vCommData);
    }

    private void setSelectionRows(UITable table,
                                  int[] rowIndex) {
        if (rowIndex != null && rowIndex.length > 0) {
            int iGridIndex = -1;
            for (int i = 0; i < rowIndex.length; i++) {
                iGridIndex = rowIndex[i];
                if (iGridIndex >= 0 && iGridIndex < table.getRowCount()) {
                    table.getSelectionModel().addSelectionInterval(iGridIndex, iGridIndex);
                    table.scrollRectToVisible(table.getCellRect(iGridIndex, 0, false));
                } else {
                    table.clearSelection();
                }
            }

        }

    }

    /**
     * @return 返回 htPkToRow。
     */
    private Hashtable getHtPkToRow() {
        return htPkToRow;
    }

    private int[] getRecordIndexes(Vector vDataAll) {
        int[] indexes = null;
        if (vDataAll != null && vDataAll.size() > 0) {
            getHtPkToRow().clear();
            String pk = null;
            for (int i = 0; i < vDataAll.size(); i++) {
                Vector record = (Vector) vDataAll.get(i);
                String pkField = getRefModel().getPkFieldCode();
                if (pkField != null) {
                    int pkIndex = getRefModel().getFieldIndex(pkField);
                    if (pkIndex >= 0 && pkIndex < record.size()) {
                        pk = record.get(pkIndex).toString();
                        getHtPkToRow().put(pk, Integer.valueOf(i));
                    }
                }

            }
        }

        String[] selectedDatas = getRefModel().getPkValues();
        ArrayList al = new ArrayList();
        if (selectedDatas != null && selectedDatas.length > 0) {

            for (int i = 0; i < selectedDatas.length; i++) {
                Integer rowNumber = null;
                if (selectedDatas[i] != null) {
                    rowNumber = ((Integer) getHtPkToRow().get(selectedDatas[i]));
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

    private Vector getCommonData(boolean isRefresh) {
        CommonDocCacheFacade facade = getCommonDocFacade();
        if (isRefresh) {
            refrashCommDataTable();
        }
        boolean isUsetDataPower = getRefModel().isUseDataPower();
        getRefModel().setUseDataPower(false);
        String joinValue = getEMRefModel().getTableJoinValue();
        getEMRefModel().setTableJoinValue(null);
        Vector vec = null;
        if (getEMRefModel().getFilterCommonDataVec() == null) {
            vec =
                    (Vector) facade.runCommDocQuery(
                        getEMRefModel().getRefCommSql(),
                        (SQLParameter) null,
                        new RefProcessor());
        } else {
            vec =
                    (Vector) facade.runCommDocQuery(
                        getEMRefModel().getRefSqlWithoutWhere(),
                        (SQLParameter) null,
                        new RefProcessor());
            getEMRefModel().getFilterCommonDataVec().filterCommonDataVec(vec);
        }
        getEMRefModel().setUseDataPower(isUsetDataPower);
        getEMRefModel().setTableJoinValue(joinValue);
        vec = getEMRefModel().getConvertedData(false, vec, true);
        return vec;
    }

    class RefProcessor extends BaseProcessor {

        public Object processResultSet(ResultSet rs) throws SQLException {
            Vector vecs = new Vector();
            ResultSetMetaData rsmd = rs.getMetaData();
            int nColumnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Vector tmpV = new Vector();
                for (int j = 1; j <= nColumnCount; j++) {
                    Object o = null;
                    String strObj = null;
                    Object value = null;
                    if (rsmd.getColumnType(j) == Types.CHAR || rsmd.getColumnType(j) == Types.VARCHAR) {
                        o = rs.getString(j);
                        if (o != null) {
                            strObj = o.toString().trim();
                            value = strObj;
                        }
                    } else {
                        o = rs.getObject(j);
                        value = o;
                    }
                    tmpV.addElement(value);
                }
                vecs.addElement(tmpV);
            }
            return vecs;
        }

    }

    private void refrashCommDataTable() {
        String[] mandocTables = getEMRefModel().getCommonDataTableName();
        for (String mandocTable : mandocTables)
            getCommonDocFacade().refreshCommDoc(mandocTable);
    }

    private CommonDocCacheFacade getCommonDocFacade() {
        if (facade == null) {
            facade = new CommonDocCacheFacade();
        }
        return facade;
    }

    private boolean isCommonDataRef() {
        return getEMRefModel().getCommonDataTableName() != null;
    }

    public Hashtable<String, Vector> getHtselectedVector() {
        return htselectedVector;
    }

    public void setHtselectedVector(Hashtable htselectedVector) {
        this.htselectedVector = htselectedVector;
    }

    /**
     * 返回 UIButtonOK 特性值。
     * 
     * @return nc.ui.pub.beans.UIButton
     */
    /* 警告：此方法将重新生成。 */
    public nc.ui.pub.beans.UIButton getUIButtonOK() {
        return getButtonPanelFactory().getBtnOK();
    }

    /**
     * 返回 UIButtonOK 特性值。
     * 
     * @return nc.ui.pub.beans.UIButton
     */
    /* 警告：此方法将重新生成。 */
    public nc.ui.pub.beans.UIButton getUIButtonExit() {
        return getButtonPanelFactory().getBtnExit();
    }

    public Hashtable getDatePkToRow() {
        return datePkToRow;
    }

}
