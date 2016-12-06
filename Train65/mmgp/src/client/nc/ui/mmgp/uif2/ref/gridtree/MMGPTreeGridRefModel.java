package nc.ui.mmgp.uif2.ref.gridtree;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.lang.StringUtils;

import nc.bs.logging.Logger;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.ExTreeNode;
import nc.ui.bd.ref.IFilterCommonDataVec;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.ITreeCellRendererIconPolicy;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.bd.ref.ReftableBO_Client;
import nc.ui.bd.ref.SQLCache;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.ref.IRefTreeExpandStrategy;
import nc.vo.bd.ref.RefInfoVO;
import nc.vo.bd.ref.ReftableVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.VisibleUtil;

public class MMGPTreeGridRefModel extends AbstractRefModel {
    private IBusinessEntity bean;

    private IBusinessEntity tablebean;

    private String[] tableFieldCode;

    private String tableRefCodeField;

    private String tableRefNameField;

    private String tableTableName;

    private String[] tableFieldName;

    private String[] tableHiddenFieldCode;

    private String tableRefNodeName;

    private String tablePkFieldCode;

    private String tableGroupPart;

    protected String tableOrderPart;

    private String tableQuerySql;

    private int tableDefaultFieldCount = 2;

    private int[] tableShownColumns;

    private boolean isTableDataPower = false;

    private boolean isTableAddEnableStateWherePart = false;

    private boolean isTableDisabledDataShow = false;

    private boolean isTableAddEnvWherePart = true;

    protected String tableSqlPatch = "";

    private String tableJoinField;
    //tangxya add 参照查询按钮，是针对左边表还是右边树数据进行过滤
    private boolean isTableQuery=true;
    
    // 树型参照图标策略---------------------------------
    private ITreeCellRendererIconPolicy treeIconPolicy = null;

    private int treeExpandStrategy = IRefTreeExpandStrategy.LEVEL;

    private int m_iExpandLevel = 1;

    private String tableWherePart;

    private String tableAddWherePart;

    protected Hashtable tableCodeIndex = null;

    protected Vector m_vecTableData = null;

    private String tableResouceID;

    private String tableDataPowerField;

    protected String tableJoinValue;

    private String m_strRootName = null;

    private String childField;

    private String fatherField;

    private String codingRule;

    private String codeRuleField;

    private String mark = " ";

    private String treeExpandNodePk;

    private String[] commonDataTableName;

    private String[] commonDataSavePk_doc;

    private IFilterCommonDataVec filterCommonDataVec = null;

    private String docJoinField;

    private static int LEAF = -1;

    private Map hm = new HashMap(); // 判断是否末级使用。

    // 包含没有数据权限控制的全部数据的树Model
    private javax.swing.tree.DefaultTreeModel m_treeModelWithAllData = null;

    // sxj 2003-04-16 add
    private javax.swing.tree.DefaultTreeModel m_treeModel = null;

    //
    Hashtable modelPkToNode = new Hashtable();

    private boolean isRootVisible = true;

    // 对于当前NC－－因为使用PK关联，所以精确匹配，末级展开
    private boolean exactOn = true;

    public MMGPTreeGridRefModel(String refName) {
        super();
        setRefNodeName(refName);
        init(refName);
    }

    protected void init(String refName) {
        RefInfoVO refInfo = RefPubUtil.getRefinfoVO(refName);
        try {
            this.setBean((IBusinessEntity) MDBaseQueryFacade.getInstance().getBeanByFullName(
                refInfo.getModuleName() + "." + refInfo.getMetadataTypeName()));
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return;
        }
        Map<String, String> map = this.getBean().getBizInterfaceMapInfo(IBDObject.class.getName());
        String pkField = this.getBean().getPrimaryKey().getPKColumn().getName();
        String fatherField = map.get("pid");

        String codeField = map.get("code");
        this.setRefCodeField(codeField);
        String nameField = map.get("name");

        this.setRefTitle(getBean().getDisplayName());
        this.setTableName(this.getBean().getTable().getName());
        this.setPkFieldCode(pkField);

        this.setRefNameField(nameField);
        this.setFieldCode(new String[]{codeField, nameField });
        this.setFieldName(new String[]{
            this.getBean().getAttributeByName(codeField).getDisplayName(),
            this.getBean().getAttributeByName(nameField).getDisplayName() });
            this.setHiddenFieldCode(new String[]{pkField, fatherField });
        setFatherField(fatherField);
        setChildField(pkField);

        if (bean.getAttributeByName("enablestate") != null) {
            setAddEnableStateWherePart(true);
        }

        resetFieldName();
    }

    /**
     * 参数2是树的元数据名称（模块.元数据实体名）
     */
    @Override
    public void setPara2(String para2) {
        super.setPara2(para2);
        if (StringUtils.isBlank(para2)) {
            return;
        }
        String[] infos = para2.split(";");
        String tableClass = infos[0].trim();
        String treeRootName = null;
        if (infos.length == 2) {
            treeRootName = infos[1];
        }else if(infos.length == 4){
        	treeRootName = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(infos[2],infos[3]);
        }

        try {
            tablebean = (IBusinessEntity) MDBaseQueryFacade.getInstance().getBeanByFullName(tableClass);

        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return;
        }
        Map<String, String> map = tablebean.getBizInterfaceMapInfo(IBDObject.class.getName());
        String tableTableName = tablebean.getTable().getName();
        String tablePkField = tablebean.getPrimaryKey().getPKColumn().getName();

        String tableCodeField = map.get("code");
        String tableNameField = map.get("name");

        setTableFieldCode(new String[]{tableCodeField, tableNameField

        });
        this.setTableFieldName(new String[]{
            tablebean.getAttributeByName(tableCodeField).getDisplayName(),
            tablebean.getAttributeByName(tableNameField).getDisplayName() });
        setTableJoinField(tableTableName + "." + tablePkField);
        setTableTableName(tableTableName);
        setTableDefaultFieldCount(2);
        this.setTableRefNodeName(tablebean.getDisplayName());
        this.setTableHiddenFieldCode(new String[]{tableTableName + "." + tablePkField, tablePkField });
        String gridTableName = bean.getTable().getName();
        this.setTablePkFieldCode(tablePkField);
        setDocJoinField(gridTableName + "." + tablePkField);
        String[] hideFleids = this.getHiddenFieldCode();
        String[] newhideFleids = new String[hideFleids.length+1];
        for(int i =0 ;i<hideFleids.length;i++){
            newhideFleids[i] = hideFleids[i];
        }
        newhideFleids[hideFleids.length] =tablePkField;
        this.setHiddenFieldCode(newhideFleids);
        this.setRootName(treeRootName);
        this.setTableOrderPart(tableCodeField);
        if (tablebean.getAttributeByName("enablestate") != null) {
            setTableAddEnableStateWherePart(true);
        }
    }

    public String getTableWherePart() {
        String wherePart = tableWherePart;
        if (wherePart == null) {
        	//tangxya modify
            wherePart = " isnull(" + this.getTablebean().getTable().getName() + ".dr,0)=0";
        } else {
        	//tangxya modify
            wherePart += " and isnull(" + this.getTablebean().getTable().getName() + ".dr,0)=0";
        }

        return wherePart;
    }

    public String getEnvTableWherePart() {
        SqlWhereUtil sw = new SqlWhereUtil();
        try {
            sw.and(VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(), this
                .getTablebean()
                .getID()));
        } catch (BusinessException e) {
            Logger.error(e.getMessage());
        }
        return sw.getSQLWhere();
    }

    /*
     * 
     */
    public ReftableVO getTableRefTableVO(String pk_corp) {
        ReftableVO vo = null;
        String key = getTableReftableVOCacheKey(pk_corp);
        // 从缓存读
        Object obj = SQLCache.getSQLCache().getColumn(key);
        if (obj == null) {
            String refNodeName = getTableRefNodeName();

            if (pk_corp == null) {
                pk_corp = getPk_corp();
            }
            try {
                obj = ReftableBO_Client.findByNode(refNodeName, pk_corp);
            } catch (Exception e) {
                Logger.debug(e);
                obj = SQLCache.NULLFLAG;
            }
        }
        if (obj instanceof ReftableVO) {
            vo = (ReftableVO) obj;
        }

        setTableReftableVO2Cache(vo, pk_corp);
        return vo;
    }

    public String getTableReftableVOCacheKey(String pk_org) {
        String tableRefNodeName = getTableRefNodeName();

        // 目前还是按当前登录公司来保存栏目信息,以后有需求可以加入对主体账簿的支持

        String key = tableRefNodeName + pk_org;
        return key;
    }

    /*
     * 栏目信息数据放到缓存.
     */
    public void setTableReftableVO2Cache(ReftableVO vo,
                                         String pk_org) {

        String key = getTableReftableVOCacheKey(pk_org);
        // 放到缓存中。
        if (vo != null) {
            SQLCache.getSQLCache().putColumns(key, vo);
        } else {
            SQLCache.getSQLCache().putColumns(key, SQLCache.NULLFLAG);
        }
    }

    public String getTableFieldShowName(String fieldCode) {
        int index = getTableFieldIndex(fieldCode);
        if (index < 0 || index > getTableFieldName().length - 1) {
            return null;
        } else {
            return getTableFieldName()[index];
        }
    }

    /**
     * 得到一个字段在所有字段中的下标。 创建日期：(2001-8-16 15:39:23)
     * 
     * @return int
     * @param fieldList
     *        java.lang.String[]
     * @param field
     *        java.lang.String
     */
    public int getTableFieldIndex(String field) {

        if (field == null
            || field.trim().length() == 0
            || getTableHtCodeIndex() == null
            || getTableHtCodeIndex().size() == 0) return -1;
        Object o = getTableHtCodeIndex().get(field.trim());
        if (o == null) {
            // 加入动态列
            // int index = tableCodeIndex.size();
            // if (isDynamicCol() && getDynamicFieldNames() != null) {
            // for (int i = 0; i < getDynamicFieldNames().length; i++) {
            // tableCodeIndex.put(getDynamicFieldNames()[i].trim(),Integer.valueOf(index + i));
            // }
            // }
            o = getTableHtCodeIndex().get(field.trim());
        }

        return (o == null) ? -1 : ((Integer) o).intValue();
    }

    /**
     * 此处插入方法说明。 创建日期：(01-6-17 18:35:14)
     * 
     * @return java.util.Hashtable
     */
    public Hashtable getTableHtCodeIndex() {
        if (tableCodeIndex == null || tableCodeIndex.size() == 0) {
            tableCodeIndex = new Hashtable();
            if (getTableFieldCode() != null) for (int i = 0; i < getTableFieldCode().length; i++) {
                tableCodeIndex.put(getTableFieldCode()[i].trim(), Integer.valueOf(i));
            }

            if (getTableHiddenFieldCode() != null) {
                int index = 0;
                if (getTableFieldCode() != null) {
                    index = getTableFieldCode().length;
                }
                for (int i = 0; i < getTableHiddenFieldCode().length; i++) {
                    if (getTableHiddenFieldCode()[i] != null) {
                        tableCodeIndex.put(getTableHiddenFieldCode()[i].trim(), Integer.valueOf(index + i));
                    } else {
                        Logger.debug("Waring: The RefModel has some errors.");
                    }
                }
            }

        }
        return tableCodeIndex;
    }

    /**
     * 获取已经读出的参照数据－－二维Vector。 创建日期：(2001-8-23 18:39:24)
     * 
     * @return java.util.Vector
     */
    public java.util.Vector getTableData() {
        String sql = getTableRefSql();
        Vector v = null;
        if (sql == null) return null;
        // SQLCache cache = SQLCache.getSQLCache();
        if (isCacheEnabled()) {
            /** 从缓存读数据 */
            v = getCacheValue(sql);
        }
        if (v == null) {
            try {
                /** 从数据库读 */
                v = queryMain(getDataSource(), sql);

            } catch (Exception e) {
                Logger.debug(e);
            }
            if (v != null)
            /** 加入到缓存中 */
            // cache.putValue(sql, v);
                setCacheValue(sql, v);
        }
        m_vecTableData = v;
        return m_vecTableData;
    }

    private String getTableRefSql() {
        return getTableSql(
            this.getTableSqlPatch(),
            getTableFieldCode(),
            this.getTableHiddenFieldCode(),
            getTableTableName(),
            getTableWherePart(),
            getTableGroupPart(),
            getTableOrderPart());
    }

    /**
     * 构造SQL语句
     * 
     * @创建日期：(00-10-16 12:27:18)
     * @author wangfan3
     */
    protected String getTableSql(String strPatch,
                                 String[] strFieldCode,
                                 String[] hiddenFields,
                                 String strTableName,
                                 String strWherePart,
                                 String strGroupField,
                                 String strOrderField) {
        if (strTableName == null) return null;

        String basSQL = buildTableBaseSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart);

        StringBuffer sqlBuffer = new StringBuffer(basSQL);
        if (getTableQuerySql() != null) {
            addTableQueryCondition(sqlBuffer);
        }
        // if (getBlurValue() != null && isIncludeBlurPart()) {
        // String blurSql = addBlurWherePart();
        // sqlBuffer.append(blurSql);
        // }

        // 加入Group子句
        if (strGroupField != null) {
            sqlBuffer.append(" group by ").append(strGroupField).toString();
        }
        // 加入ORDER子句
        if (strOrderField != null && strOrderField.trim().length() != 0) {
            sqlBuffer.append(" order by ").append(strOrderField).toString();
        }

        return sqlBuffer.toString();
    }

    protected void addTableQueryCondition(StringBuffer sqlBuffer) {
        sqlBuffer
            .append(" and (")
            .append(getTablePkFieldCode())
            .append(" in (")
            .append(getTableQuerySql())
            .append("))")
            .toString();
    }

    /**
     * 构造基本 SQL
     */
    public String buildTableBaseSql(String patch,
                                    String[] columns,
                                    String[] hiddenColumns,
                                    String tableName,
                                    String whereCondition) {
        StringBuffer whereClause = new StringBuffer();
        StringBuffer sql = new StringBuffer("select ").append(patch).append(" ");
        int columnCount = columns == null ? 0 : columns.length;
        addTableQueryColumn(columnCount, sql, columns, hiddenColumns);
        // 加入FROM子句
        sql.append(" from ").append(tableName);
        // 加入WHERE子句
        if (whereCondition != null && whereCondition.trim().length() != 0) {
            whereClause.append(" where (").append(whereCondition).append(" )");
        } else
            whereClause.append(" where 11=11 ");

        appendTableAddWherePartCondition(whereClause);
         //tangxya modify
        addTableDataPowerCondition(getTablebean().getTable().getName(), whereClause);
        addTableDisabledDataWherePart(whereClause);
        addTableEnvWherePart(whereClause);
        sql.append(" ").append(whereClause.toString());

        return sql.toString();
    }

    /**
     * 
     */
    private void addTableEnvWherePart(StringBuffer whereClause) {

        if (!isTableAddEnvWherePart()) {
            return;
        }

        String wherePart = getEnvTableWherePart();
        if (wherePart != null && wherePart.trim().length() > 0) {

            whereClause.append(" and (").append(wherePart).append(") ");

        }

    }

    protected void addTableDataPowerCondition(String tableName,
                                              StringBuffer whereClause) {
        if (isTableDataPower()) {

            String powerSql = getDataPowerSubSql(tableName, getTableDataPowerField(), getTableResouceID());

            if (powerSql != null) {
                whereClause.append(" and (").append(powerSql).append(")");
            }

        }

    }

    /**
     * 添加启用停用数据条件
     * 
     * @param whereClause
     */
    private void addTableDisabledDataWherePart(StringBuffer whereClause) {

        if (isTableAddEnableStateWherePart()) {

            String wherePart = getDisableDataWherePart(isTableDisabledDataShow());
            if (wherePart != null) {
                whereClause.append(" and (").append(wherePart).append(") ");

            }
        }
    }

    /**
     * 添加列条件
     * 
     * @param iSelectFieldCount
     * @param strSql
     * @param strFieldCode
     * @param hiddenFields
     */
    public void addTableQueryColumn(int iSelectFieldCount,
                                    StringBuffer strSql,
                                    String[] strFieldCode,
                                    String[] hiddenFields) {

        int nameFieldIndex = getTableFieldIndex(this.getTableRefNameField());

        for (int i = 0; i < iSelectFieldCount; i++) {
            if (isMutilLangNameRef()
                && i == nameFieldIndex
                && !strFieldCode[nameFieldIndex].equalsIgnoreCase("null")
                && strFieldCode[nameFieldIndex].equalsIgnoreCase(getTableRefNameField())) {

                strSql.append(getLangNameColume(getTableRefNameField()));

            } else {
                strSql.append(strFieldCode[i]);
            }

            if (i < iSelectFieldCount - 1) strSql.append(",");
        }
        // 加入隐藏字段
        if (hiddenFields != null && hiddenFields.length > 0) {
            for (int k = 0; k < hiddenFields.length; k++) {
                if (hiddenFields[k] != null && hiddenFields[k].trim().length() > 0) {
                    strSql.append(",");
                    strSql.append(hiddenFields[k]);
                }
            }
        }
    }

    private void appendTableAddWherePartCondition(StringBuffer whereClause) {

        if (getTableAddWherePart() == null) {
            return;
        }

        whereClause.append(" ").append(getTableAddWherePart());

    }

    protected String getTableDataPowerSql(boolean isClassSql) {
        String tableDataPowerSql = null;
        //tangxya getTableTableName()
        String strTableName =getTablebean().getTable().getName() ;
        if (isTableDataPower()) {
            // 加入左表数据权限控制
            tableDataPowerSql = getDataPowerSubSql(strTableName, getTableDataPowerField(), getTableResouceID());

        }
        return tableDataPowerSql;
    }

    /*
     * 树型参照界面上是否是末级节点
     */
    public boolean isLeaf(String pk) {
        if (hm.size() == 0) {
            if (m_treeModelWithAllData == null) {
                getTreeModelWithAllData();
            }
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_treeModelWithAllData.getRoot();
            Enumeration enumeration = root.breadthFirstEnumeration();
            {
                while (enumeration.hasMoreElements()) {
                    ExTreeNode node = (ExTreeNode) enumeration.nextElement();
                    // 如果是分类的节点，不做判断
                    if (!node.isMainClass()) {
                        continue;
                    }
                    Object o = node.getUserObject();
                    if (!(o instanceof Vector)) {
                        continue;
                    }
                    Vector v = (Vector) node.getUserObject();
                    String pkValue = v.elementAt(getFieldIndex(getPkFieldCode())).toString();
                    if (node.isLeaf()) {
                        hm.put(pkValue, Integer.valueOf(LEAF));
                    } else {
                        hm.put(pkValue, Integer.valueOf(node.getLevel()));
                    }

                }
            }
        }

        Integer value = ((Integer) hm.get(pk));
        if (value != null) {
            int isLeaf = value.intValue();

            if (isLeaf == LEAF) {
                return true;
            }
            return false;

        }

        return false;

    }

    /**
     * 此处插入方法说明。 创建日期：(2003-4-16 13:35:59)
     * 
     * @return javax.swing.tree.DefaultTreeModel
     */
    private javax.swing.tree.DefaultTreeModel getTreeModelWithAllData() {
        boolean isUseDataPower = isUseDataPower();
        boolean isSealedDataShow = isDisabledDataShow();
        // 不使用数据权限来获得全部数据
        setUseDataPower(false);
        // 显示封存数据
        setDisabledDataShow(true);
        if (m_treeModelWithAllData == null) {
            m_treeModelWithAllData = getCompositeTreeModel();
            hm.clear();
        }
        // 原来是否使用数据权限的开关值
        setUseDataPower(isUseDataPower);
        // 原来是否封存数据显示的标志
        setDisabledDataShow(isSealedDataShow);
        return m_treeModelWithAllData;
    }

    /**
     * 此处插入方法说明。 创建日期：(2003-4-16 13:35:59)
     * 
     * @return javax.swing.tree.DefaultTreeModel
     */
    public javax.swing.tree.DefaultTreeModel getTreeModel() {
        // 父子关系的树暂不支持再加一个分类树。
        if (m_treeModel == null) {
            m_treeModel = getCompositeTreeModel();
        }
        return m_treeModel;
    }

    /**
     * 重新载入数据。 1.不使用缓存时每次都调用 2.使用缓存手动调用如刷新按钮。 创建日期：(2001-8-23 21:14:19)
     * 
     * @return java.util.Vector
     */
    public java.util.Vector reloadTableData() {
        clearTableData();
        return getTableData();
    }

    private void clearTableData() {
        // SQLCache cache = SQLCache.getSQLCache();
        // if (cache != null) {
        // cache.remove(getClassRefSql());
        // }
        removeCacheValue(getTableRefSql());
    }

    /**
     * 支持两种档案构造树 先支持编码规则的档案
     */
    public javax.swing.tree.DefaultTreeModel getCompositeTreeModel() {
        DefaultTreeModel tm = null;
        getModelPkToNode().clear();
        Vector v = null;
        if (this.getTableJoinValue() != null) {
            v = getRefData();
        }
        tm =
                constructTreeModel(
                    v,
                    getCodingRule(),
                    getShownColumns(),
                    true,
                    getFieldIndex(getCodeRuleField()),
                    true,
                    getFieldIndex(getFatherField()),
                    getFieldIndex(getChildField()),
                    getFieldIndex(getPkFieldCode()));

        return tm;
    }

    /**
     * 当Where 条件改变后，清空m_vecData，重新匹配已经设置的pks.
     */
    private void resetSelectedData_WhenDataChanged() {

        String[] selectedPKs = getPkValues();
        // 清数据。
        // clearModelData();
        clearData();

        if (selectedPKs != null && selectedPKs.length > 0) {
            matchPkData(selectedPKs);
        }

        Vector selectedData = getSelectedData();

        setSelectedData(selectedData);
        // 通知UIRefPane,刷新界面
        fireChange();
    }

    /**
     * <p>
     * <strong>最后修改人：sxj</strong>
     * <p>
     * <strong>最后修改日期：2007-5-25</strong>
     * <p>
     * 
     * @param
     * @return DefaultTreeModel
     * @exception BusinessException
     * @since NC5.0
     */
    private DefaultTreeModel constructTreeModel(Vector vRecord,
                                                String codingRule,
                                                int[] showColumns,
                                                boolean isCanSelected,
                                                int codeCol,
                                                boolean isMainClass,
                                                int fatherFieldIndex,
                                                int childFieldIndex,
                                                int pkFieldIndex) {
        DefaultTreeModel treeModel = null;
        if (codingRule != null) {
            treeModel =
                    constructCodingRuleModel(vRecord, codingRule, showColumns, isCanSelected, codeCol, isMainClass);
        } else {
            treeModel =
                    constructParentSonModel(
                        vRecord,
                        isCanSelected,
                        isMainClass,
                        fatherFieldIndex,
                        childFieldIndex,
                        pkFieldIndex);
        }
        return treeModel;
    }

    /**
     * 按编码规则构造树--每级编码最大为9－－形式--"2133"
     */
    protected DefaultTreeModel constructCodingRuleModel(Vector vRecord,
                                                      String codingRule,
                                                      int[] showColumns,
                                                      boolean isCanSelected,
                                                      int codeCol,
                                                      boolean isMainClass) {
        DefaultTreeModel tm = null;
        try {
            ExTreeNode root = null;
            root = new ExTreeNode(getRootName(), true);
            tm = new DefaultTreeModel(root, false);

            if (vRecord == null || vRecord.size() == 0) {
                vRecord = new Vector();
            }

            int[] m_iCodingRule = RefPubUtil.getCodingRule(codingRule);
            Hashtable pkToNode = getModelPkToNode();
            // pkToNode.clear();
            Hashtable hAllNode = new Hashtable();
            HashMap hm = new HashMap();
            Vector vAllTreeNode = new Vector();

            int pkIndex = getFieldIndex(getPkFieldCode());

            for (int i = 0; i < vRecord.size(); i++) {
                Vector row = (Vector) vRecord.elementAt(i);
                ExTreeNode nodepar = new ExTreeNode(row, showColumns, getMark());
                if (!isCanSelect(row)) {
                    nodepar.setIsCanSelected(false);
                } else {
                    nodepar.setIsCanSelected(isCanSelected);
                }
                // 是分类的类还是数据类
                nodepar.setMainClass(isMainClass);
                vAllTreeNode.add(nodepar);
                hm.put(row.elementAt(codeCol).toString(), row);
                hAllNode.put(row.elementAt(codeCol).toString(), nodepar);
                if (pkIndex < row.size()) {
                    pkToNode.put(row.elementAt(pkIndex).toString(), nodepar);
                }

            }
            
            for (int i = 0; i < vRecord.size(); i++) {
                ExTreeNode nodepar = (ExTreeNode) vAllTreeNode.get(i);
                Vector row = (Vector) vRecord.elementAt(i);
                String fatherCode = getFatherCodeValueByCodingRule(row, m_iCodingRule, codeCol);
                if (fatherCode == null || hm.get(fatherCode) == null) {
                    root.insert(nodepar, root.getChildCount());
                } else {
                    ExTreeNode nodeparFather = (ExTreeNode) hAllNode.get(fatherCode);
                    if (nodeparFather == null) {
                        Logger.debug("to find father error:" + fatherCode + ":" + nodepar);
                        // 插入到根节点
                        root.insert(nodepar, root.getChildCount());
                    } else {
                        nodeparFather.insert(nodepar, nodeparFather.getChildCount());
                    }

                }
            }

        } catch (Exception e) {
            Logger.info(e.getMessage());
        }
        return tm;
    }

    /**
     * 2004-12-27 根据编码规则找父节点
     */
    public String getFatherCodeValueByCodingRule(Vector v,
                                                 int[] codingRule,
                                                 int codeCol) {
        int lev = 0;
        int index = 0;
        int len = 0;
        int codeLen = 0;
        String code = v.elementAt(codeCol).toString();
        codeLen = code.length();
        while (true) {
            len += codingRule[index];
            index++;
            if (index == codingRule.length) {
                break;
            }
            if (len == codeLen) {
                lev = index;
                break;
            }
        }
        if (lev == 1) { // 如果是一级，Father为null
            return null;
        }
        int fatherLen = codeLen - codingRule[index - 1];
        // 特殊处理，对于编码不规则的不分级次的档案。
        if (fatherLen <= 0) {
            return null;
        }
        String fatherCode = code.substring(0, fatherLen);
        return fatherCode;
    }

    public boolean isCanSelect(Vector row) {
        return true;
    }

    /**
     * 根据上下级关系构造树
     */
    private DefaultTreeModel constructParentSonModel(Vector vecData,
                                                     boolean isCanSelected,
                                                     boolean isMainClass,
                                                     int fatherFieldIndex,
                                                     int childFieldIndex,
                                                     int pkFieldIndex) {
        ExTreeNode root = null;
        root = new ExTreeNode(getRootName(), true);

        DefaultTreeModel tm = new DefaultTreeModel(root, false);

        if (fatherFieldIndex == -1 || childFieldIndex == -1) {
            return tm;
        }

        if (vecData == null || vecData.size() == 0) {
            vecData = new Vector();
        }
        // sxj 2004-06-23 新构造树方法。

        Hashtable hAllNode = getModelPkToNode();
        hAllNode.put("root", root);
        // hAllNode.clear();
        HashMap hm = new HashMap();
        Vector vAllTreeNode = new Vector();
        // int childFieldIndex= getFieldIndex(getChildField());
        // int pkIndex = getFieldIndex(getPkFieldCode());
        for (int i = 0; i < vecData.size(); i++) {
            Vector row = (Vector) vecData.elementAt(i);
            ExTreeNode nodepar = new ExTreeNode(row, getShownColumns(), getMark());
            nodepar.setMainClass(isMainClass);
            if (!isCanSelect(row)) {
                nodepar.setIsCanSelected(false);
            } else {
                nodepar.setIsCanSelected(isCanSelected);
            }
            vAllTreeNode.add(nodepar);
            hm.put(row.elementAt(childFieldIndex).toString(), row);
            hAllNode.put(row.elementAt(childFieldIndex).toString(), nodepar);
            // 兼容如果childField不是主键列时的定位问题
            hAllNode.put(row.elementAt(pkFieldIndex).toString(), nodepar);

        }
        for (int i = 0; i < vecData.size(); i++) {
            ExTreeNode nodepar = (ExTreeNode) vAllTreeNode.get(i);
            Vector row = (Vector) vecData.elementAt(i);
            String fathreCodeValue = (String) row.elementAt(fatherFieldIndex);
            if (fathreCodeValue == null || fathreCodeValue.trim().length() == 0 || hm.get(fathreCodeValue) == null) {
                root.insert(nodepar, root.getChildCount());
            } else {
                ExTreeNode nodeparFather = (ExTreeNode) hAllNode.get(fathreCodeValue);
                if (nodeparFather == null) {
                    Debug.debug("to find father error:" + fathreCodeValue + ":" + nodepar);
                    // 插入到根节点
                    root.insert(nodepar, root.getChildCount());
                } else {
                    nodeparFather.insert(nodepar, nodeparFather.getChildCount());
                }

            }
        }

        return tm;
    }

    /**
     * @return 返回 modelPkToNode。
     */
    public Hashtable getModelPkToNode() {
        return modelPkToNode;
    }

    /**
     * 
     */
    public void clearTreeModel() {
        m_treeModel = null;
        m_treeModelWithAllData = null;
        hm.clear();
    }

    /**
     * 构造SQL语句
     * 
     * @author:张扬
     */
    protected String getSql(String strPatch,
                            String[] strFieldCode,
                            String[] hiddenFields,
                            String strTableName,
                            String strWherePart,
                            String strGroupPart,
                            String strOrderField) {
        // 树型参照不支持 group by ,这里强制 strGroupPart = null
        // String sql = super.getSql(strPatch, strFieldCode, hiddenFields,
        // strTableName, strWherePart, null, strOrderField);

        return getSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart, strOrderField);
    }

    public String getRefCommSql() {
        return this.getRefSql();
    }

    public String getRefSqlWithoutWhere() {
        String sql = getSql(getStrPatch(), getFieldCode(), getHiddenFieldCode(), getTableName(), "", getOrderPart());
        return sql;
    }

    public Vector getConvertedData(boolean isDataFromCache,
                                   Vector v,
                                   boolean isDefConverted) {

        Vector mapData = RefPubUtil.mapData(v, this);
        return super.getConvertedData(isDataFromCache, mapData, isDefConverted);
    }

    /**
     * 构造SQL语句
     * 
     * @author:张扬 modified by hey
     */
    protected String getSql(String strPatch,
                            String[] strFieldCode,
                            String[] hiddenFields,
                            String strTableName,
                            String strWherePart,
                            String strOrderField) {
        if (strTableName == null || strTableName.trim().length() == 0) return null;

        StringBuffer sqlBuffer =
                new StringBuffer(buildBaseSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart));
        // 高级查询
        if (getQuerySql() != null) {
            addQueryCondition(sqlBuffer);
        }

        // 对自定义参照模糊查询的支持

        if (getBlurValue() != null && getBlurValue().trim().length() > 0 && isIncludeBlurPart()) {
            String blurSql = addBlurWherePart();
            sqlBuffer.append(blurSql);
            addClassAreaCondition(sqlBuffer);

        } else {

            addJoinCondition(sqlBuffer);

        }
        // 查询时要加上分类的数据权限
        if (getTableJoinValue() != null && getTableJoinValue().equals(IRefConst.QUERY)) {
            // addClassDataPowerSql(sqlBuffer);
            addClassAreaCondition(sqlBuffer);

        }

        // 加入ORDER子句
        if (strOrderField != null && strOrderField.trim().length() != 0) {
            sqlBuffer.append(" order by " + strOrderField);
        }
        return sqlBuffer.toString();
    }

    protected void addJoinCondition(StringBuffer sqlBuffer) {
        // 处理关联---但是不加入WherePart
        if (getTableJoinValue() != null && !getTableJoinValue().equals(IRefConst.QUERY)) {

            if (isExactOn()) sqlBuffer.append(" and ( " + getDocJoinField() + " = '" + getTableJoinValue() + "' )");
            else
                sqlBuffer.append(" and ( " + getDocJoinField() + " like '" + getTableJoinValue() + "%' )");

        }

    }

    // 加入左树的条件，包含左树的数据权限
    protected void addClassAreaCondition(StringBuffer sqlBuffer) {
        String sql =
                getTableSql(
                    null,
                    new String[]{getTableJoinField() },
                    null,
                    getTablebean().getTable().getName(),
                    getTableWherePart(),
                    null,
                    null);
        if (sql != null) {
            sqlBuffer.append(" and " + getDocJoinField() + " in (" + sql + ")");
        }
    }

    public String getDocJoinField() {
        if (docJoinField == null || docJoinField.trim().length() == 0) {
            docJoinField = getTableJoinField();
        }
        return docJoinField;
    }

    /**
     * 档案表中和分类连接的字段 创建日期：(2001-8-15 16:38:45)
     */
    public void setDocJoinField(String strDocJoinField) {
        docJoinField = strDocJoinField;
    }

    /**
     * @param treeExpandStrategy
     *        树节点展开策略见 IRefTreeExpandStrategy
     * @param expandLevel
     *        如果是级次展开，展开级次，否则该参数无效
     * @param treeExpandNodePk
     *        如果是节点展开，请输入节点PK
     */
    public void setTreeExpand(int treeExpandStrategy,
                              int expandLevel,
                              String treeExpandNodePk) {
        setTreeExpandStrategy(treeExpandStrategy);
        setExpandLevel(expandLevel);
        setTreeExpandNodePk(treeExpandNodePk);
    }

    public String getTreeExpandNodePk() {
        return treeExpandNodePk;
    }

    public void setTreeExpandNodePk(String treeExpandNodePk) {
        this.treeExpandNodePk = treeExpandNodePk;
    }

    /**
     * 树根名－－为空时取参照名。 创建日期：(2001-8-13 16:21:15)
     * 
     * @return java.lang.String
     */
    public String getRootName() {
        if (m_strRootName != null) {
            return m_strRootName;
        }
        return getRefTitle();

    }

    /**
     * 树根名－－为空不显示树根。 创建日期：(2001-8-13 16:21:15)
     * 
     * @return java.lang.String
     */
    public void setRootName(String strRootName) {
        m_strRootName = strRootName;
    }

    public String getTableOrderPart() {
        return tableOrderPart;
    }

    public void setTableOrderPart(String tableOrderPart) {
        this.tableOrderPart = tableOrderPart;
    }

    public String getTableSqlPatch() {
        return tableSqlPatch;
    }

    public void setTableSqlPatch(String tableSqlPatch) {
        this.tableSqlPatch = tableSqlPatch;
    }

    public boolean isTableDataPower() {
        return isTableDataPower;
    }

    public void setTableDataPower(boolean isTableDataPower) {
        this.isTableDataPower = isTableDataPower;
    }

    public String getTableResouceID() {
        return tableResouceID;
    }

    public void setTableResouceID(String tableResouceID) {
        this.tableResouceID = tableResouceID;
    }

    public String getTableDataPowerField() {
        if (tableDataPowerField == null) {
            return getTableJoinField();
        }
        return tableDataPowerField;
    }

    public String getTableJoinField() {
        return tableJoinField;
    }

    public void setTableJoinField(String tableJoinField) {
        this.tableJoinField = tableJoinField;
    }

    public String getTableJoinValue() {
        return tableJoinValue;
    }

    public void setTableJoinValue(String tableJoinValue) {
        this.tableJoinValue = tableJoinValue;
    }

    public String getTableRefNodeName() {
        return tableRefNodeName;
    }

    public void setTableRefNodeName(String tableRefNodeName) {
        this.tableRefNodeName = tableRefNodeName;
    }

    public IBusinessEntity getTablebean() {
        return tablebean;
    }

    public void setTablebean(IBusinessEntity tablebean) {
        this.tablebean = tablebean;
    }

    public void setTableDefaultFieldCount(int tableDefaultFieldCount) {
        this.tableDefaultFieldCount = tableDefaultFieldCount;
    }

    public void setTreeExpandStrategy(int treeExpandStrategy) {
        this.treeExpandStrategy = treeExpandStrategy;
    }

    public int getTreeExpandStrategy() {
        return treeExpandStrategy;
    }

    public void setTreeIconPolicy(ITreeCellRendererIconPolicy iconPolicy) {
        this.treeIconPolicy = iconPolicy;
    }

    public ITreeCellRendererIconPolicy getTreeIconPolicy() {
        return treeIconPolicy;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-24 14:49:54)
     * 
     * @param iExpandLevel
     *        int
     */
    public void setExpandLevel(int iExpandLevel) {
        m_iExpandLevel = iExpandLevel < 0 ? 0 : iExpandLevel;
    }

    /**
     * 档案展开级次－－0－－根展开 1－－第一级展开 2-- 末级节点一定展开 创建日期：(2001-8-14 19:04:19)
     * 
     * @return int
     */
    public int getExpandLevel() {
        return m_iExpandLevel;
    }

    public void setTableFieldCode(String[] tableFieldCode) {
        this.tableFieldCode = tableFieldCode;
    }

    public String[] getTableFieldCode() {
        return tableFieldCode;
    }

    public void setTableFieldName(String[] tableFieldName) {
        this.tableFieldName = tableFieldName;
    }

    public String[] getTableFieldName() {
        return tableFieldName;
    }

    public void setTableHiddenFieldCode(String[] tableHiddenFieldCode) {
        this.tableHiddenFieldCode = tableHiddenFieldCode;
    }

    public String[] getTableHiddenFieldCode() {
        return tableHiddenFieldCode;
    }

    public void setTableTableName(String tableTableName) {
        this.tableTableName = tableTableName;
    }

    public String getTableTableName() {
        return tableTableName;
    }

    public int getTableDefaultFieldCount() {
        return tableDefaultFieldCount;
    }

    public void setTableWherePart(String tableWherePart) {
        this.tableWherePart = tableWherePart;

        resetSelectedData_WhenDataChanged();
    }

    public String getTablePkFieldCode() {
        return tablePkFieldCode;
    }

    public void setTablePkFieldCode(String tablePkFieldCode) {
        this.tablePkFieldCode = tablePkFieldCode;
    }

    public int[] getTableShownColumns() {
        return tableShownColumns;
    }

    public void setTableShownColumns(int[] tableShownColumns) {
        this.tableShownColumns = tableShownColumns;
    }

    public String getChildField() {
        return childField;
    }

    public void setChildField(String childField) {
        this.childField = childField;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getFatherField() {
        return fatherField;
    }

    public void setFatherField(String fatherField) {
        this.fatherField = fatherField;
    }

    public String[] getCommonDataTableName() {
        return commonDataTableName;
    }

    public void setCommonDataTableName(String[] commonDataTableName) {
        this.commonDataTableName = commonDataTableName;
    }

    public boolean isRootVisible() {
        return isRootVisible;
    }

    public void setRootVisible(boolean isRootVisible) {
        this.isRootVisible = isRootVisible;
    }

    public String[] getCommonDataSavePk_doc() {
        if (commonDataSavePk_doc == null) {
            return new String[]{getPkFieldCode() };
        }
        return commonDataSavePk_doc;
    }

    public void setCommonDataSavePk_doc(String[] commonDataSavePk_doc) {
        this.commonDataSavePk_doc = commonDataSavePk_doc;
    }

    /**
     * 过滤参照值变化事件，子类要覆盖该方法。
     * 
     * @param event
     */
    public void filterValueChanged(ValueChangedEvent changedValue) {
        super.filterValueChanged(changedValue);
        String[] pk_orgs = (String[]) changedValue.getNewValue();
        if (pk_orgs != null && pk_orgs.length > 0) {
            this.setPk_org(pk_orgs[0]);
        }
    }

    public IFilterCommonDataVec getFilterCommonDataVec() {
        return filterCommonDataVec;
    }

    public void setFilterCommonDataVec(IFilterCommonDataVec filterCommonDataVec) {
        this.filterCommonDataVec = filterCommonDataVec;
    }

    public boolean isExactOn() {
        return exactOn;
    }

    public void setExactOn(boolean exactOn) {
        this.exactOn = exactOn;
    }

    public IBusinessEntity getBean() {
        return bean;
    }

    public void setBean(IBusinessEntity bean) {
        this.bean = bean;
    }

    public boolean isTableAddEnableStateWherePart() {
        return isTableAddEnableStateWherePart;
    }

    public void setTableAddEnableStateWherePart(boolean isTableAddEnableStateWherePart) {
        this.isTableAddEnableStateWherePart = isTableAddEnableStateWherePart;
    }

    public String getTableAddWherePart() {
        return tableAddWherePart;
    }

    public void setTableAddWherePart(String tableAddWherePart) {
        this.tableAddWherePart = tableAddWherePart;
    }

    public String getTableRefCodeField() {
        if (tableRefCodeField == null && getTableFieldCode() != null && getTableFieldCode().length > 0)
            tableRefCodeField = getTableFieldCode()[0];
        return tableRefCodeField;
    }

    public void setTableRefCodeField(String tableRefCodeField) {
        this.tableRefCodeField = tableRefCodeField;
    }

    public String getTableRefNameField() {
        if (tableRefNameField == null && getTableFieldCode() != null && getTableFieldCode().length > 1)
            tableRefNameField = getTableFieldCode()[1];
        return tableRefNameField;
    }

    public void setTableRefNameField(String tableRefNameField) {
        this.tableRefNameField = tableRefNameField;
    }

    public boolean isTableDisabledDataShow() {
        return isTableDisabledDataShow;
    }

    public void setTableDisabledDataShow(boolean isTableDisabledDataShow) {
        this.isTableDisabledDataShow = isTableDisabledDataShow;
    }

    public boolean isTableAddEnvWherePart() {
        return isTableAddEnvWherePart;
    }

    public void setTableAddEnvWherePart(boolean isTableAddEnvWherePart) {
        this.isTableAddEnvWherePart = isTableAddEnvWherePart;
    }

    public String getTableGroupPart() {
        return tableGroupPart;
    }

    public void setTableGroupPart(String tableGroupPart) {
        this.tableGroupPart = tableGroupPart;
    }

    public String getTableQuerySql() {
        return tableQuerySql;
    }

    public void setTableQuerySql(String tableQuerySql) {
        this.tableQuerySql = tableQuerySql;
    }

    public String getCodeRuleField() {
        if (codeRuleField == null) {
            return getRefCodeField();
        }
        return codeRuleField;
    }

    public void setCodeRuleField(String codeRuleField) {
        this.codeRuleField = codeRuleField;
    }

    public String getCodingRule() {
        return codingRule;
    }

    public void setCodingRule(String codingRule) {
        this.codingRule = codingRule;
    }

	public boolean isTableQuery() {
		return isTableQuery;
	}

	public void setTableQuery(boolean isTableQuery) {
		this.isTableQuery = isTableQuery;
	}


    
    
}
