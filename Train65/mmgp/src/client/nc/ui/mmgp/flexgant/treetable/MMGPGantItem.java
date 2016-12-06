package nc.ui.mmgp.flexgant.treetable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import nc.bs.logging.Logger;
import nc.md.model.type.impl.EnumType;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.bd.ref.costomize.RefCustomizedUtil;
import nc.ui.mmgp.flexgant.listener.ITreeTableModelDecimalListener;
import nc.ui.pub.bill.BillItemNumberFormat;
import nc.ui.pub.bill.BillItemPropertyChangeEvent;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillItemConverter;
import nc.ui.pub.bill.IGetBillRelationItemValue;
import nc.ui.pub.bill.MetaDataGetBillRelationItemValue;
import nc.ui.pubapp.gantt.ui.treetable.GantItem;
import nc.vo.bd.ref.RefCustomizedVO;
import nc.vo.bill.pub.BillFormulaReg;
import nc.vo.bill.pub.BillUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.IMetaDataProperty;
import nc.vo.pub.bill.MetaDataPropertyFactory;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-19下午4:55:58
 * @author: tangxya
 */
public class MMGPGantItem extends GantItem implements IBillItem {
    
    private int m_iLength = 0; // 输入长度

    private int m_iDataType = STRING; // 数据类型

    private IMetaDataProperty metaDataProperty = null; // 元数据

    private String metaDataRelation = null; // 元数据关联项

    private String[] m_strLoadFormulas = null; // 加载时处理公式

    private String m_strIDColName = ""; // 关键字名

    private RefCustomizedVO set = null; // 参照类型格式描述

    private String m_strRefType = ""; // 参照类型

    private boolean isDef = false; // 自定义项

    private AbstractRefModel refModel;

    private ArrayList<MMGPGantItem> relationItem = null; // 关联ITEM

    private String metaDataAccessPath = null; // 元数据访问路径

    private IGetBillRelationItemValue getBillRelationItemValue = null;

    private BillItemNumberFormat m_numberFormat = null; // 数字类型格式描述

    private IBillItemConverter converter = null; // 值类型转换器

    private int dateformat = IBillItem.DATE_FORMAT_NORMAL; // 日期格式

    private String nodecode = "";

    private ITreeTableModelDecimalListener decimalListener = null; // 精度接口

    private int maxDecimalDigit = 0;

    public IBillItemConverter getConverter() {
        if (converter == null) converter = MMGPGantItemConverterFactory.getItemConverter(this);
        return converter;
    }

    public void setConverter(IBillItemConverter converter) {
        this.converter = converter;
    }

    /**
		 * 
		 */
    private static final long serialVersionUID = 2194457556332264284L;

    public MMGPGantItem() {
        super();
    }

    public MMGPGantItem(BillTempletBodyVO bVO) {
        super();
        initItem(bVO);
    }

    public void initItem(BillTempletBodyVO bVO) {
        setKey(bVO.getItemkey());

        setShow(bVO.getListshowflag());
        setEdit(bVO.getEditflag());
        setShowOrder(bVO.getShoworder().intValue());
        m_iDataType = bVO.getDatatype() != null ? bVO.getDatatype().intValue() : IBillItem.STRING;

        m_strRefType = bVO.getReftype();

        m_strIDColName = bVO.getIdcolname();
        m_strLoadFormulas = getFormulas(bVO.getLoadformula());
        metaDataProperty = bVO.getMetaDataPropertyAdpter();
        metaDataRelation = bVO.getMetadatarelation();
        metaDataAccessPath = bVO.getMetadatapath();
        
        m_iLength = bVO.getInputlength() != null ? bVO.getInputlength()
            .intValue() : 50;
        if (getMetaDataProperty() != null) {
            m_iDataType = getMetaDataProperty().getDataType();
        }
        if (MMStringUtil.isEmpty(bVO.getDefaultshowname())) {
            if (getMetaDataProperty() != null) {
                setShowName(getMetaDataProperty().getShowName());
                /* m_iDataType = getMetaDataProperty().getDataType(); */
            }
        } else {
            setShowName(bVO.getDefaultshowname());
        }

        if (getDataType() == USERDEFITEM) isDef = true;

        // 设置控件属性
        initAttribByParseReftype();
        initRefModel();
        setValueClassType(getColumnClass(m_iDataType));
    }

    private void initRefModel() {
        if (IBillItem.UFREF == getDataType()) {
            refModel = RefPubUtil.getRefModel(this.getRefTypeSet().getRefNodeName());
        }
    }

    public Class< ? > getColumnClass(int datatype) {
        Class< ? > retclass = null;
        switch (datatype) {
            case IBillItem.DATE:
                retclass = Date.class;
                break;
            case IBillItem.BOOLEAN:
                retclass = UFBoolean.class;
                break;
            case IBillItem.DATETIME:
                retclass = Date.class;
                break;
            case IBillItem.INTEGER:
                retclass = Integer.class;
                break;
            case IBillItem.DECIMAL:
            case IBillItem.MONEY:
                // 2014-6-13 gaotx 原先的Decimal不是标准jar里面的，改为BigDecimal
                retclass = BigDecimal.class;
                break;
            case IBillItem.UFREF:
                retclass = getRefModel().getClass();
                break;
            case IBillItem.COMBO:
                EnumType enumtype = (EnumType) getMetaDataProperty().getAttribute().getDataType();
                retclass = getClassbyFullName(enumtype.getEnumFullClassName());
                break;
            case IBillItem.MULTILANGTEXT:
                retclass = MultiLangText.class;
                break;
            default:
                retclass = String.class;
        }
        return retclass;

    }

    private Class< ? > getClassbyFullName(String fullname) {
        Class< ? > modelClass = null;
        try {
            modelClass = Class.forName(fullname);
        } catch (ClassNotFoundException e) {
            Logger.debug(e.getMessage());
        }
        return modelClass;

    }

    public MMGPGantItem(String key,
                        String showName,
                        boolean isShow,
                        Integer showOrder,
                        Class< ? > valueClassType,
                        boolean isKeyValue) {
        super(key, showName, isShow, showOrder, valueClassType, isKeyValue);
    }

    public IMetaDataProperty getMetaDataProperty() {
        return metaDataProperty;
    }

    public void setMetaDataProperty(IMetaDataProperty metaDataProperty) {
        this.metaDataProperty = metaDataProperty;
        if (metaDataProperty != null) {
            setShowName(metaDataProperty.getShowName());
        }

        setRefType(null);
        set = null;
        setGetBillRelationItemValue(null);

        /*
         * setGetBillRelationItemValue(null); setComponent(null); setRefType(null); set = null;
         */
    }

    public String getMetaDataRelation() {
        return metaDataRelation;
    }

    public String[] getLoadFormula() {
        return m_strLoadFormulas;
    }

    /**
     * 设置加载公式. 创建日期:(01-2-21 9:44:18)
     * 
     * @param newLength
     *        int
     */
    public void setLoadFormula(String[] formulas) {
        m_strLoadFormulas = formulas;
        if (formulas != null) {
            BillFormulaReg.prepareFormulas(getKey(), formulas);
        }
    }

    /**
     * 将多条表达式语句拆分成表达式数组. 创建日期:(01-2-21 9:51:24)
     * 
     * @return int
     */
    protected String[] getFormulas(String formula) {
        if (formula == null) return null;

        String[] formulas = BillUtil.parseFormulas(formula);
        if (formulas != null) {
            BillFormulaReg.prepareFormulas(getKey(), formulas);
        }

        return formulas;
    }

    public void setIDColName(java.lang.String newIDColName) {
        m_strIDColName = newIDColName;
    }

    /**
     * 获得ID列名. 创建日期:(01-2-21 9:51:24)
     * 
     * @return int
     */
    public String getIDColName() {
        return m_strIDColName;
    }

    /**
     * 获得数据类型. 创建日期:(01-2-21 9:46:08)
     * 
     * @return int
     */
    public int getDataType() {
        if (getMetaDataProperty() != null) return getMetaDataProperty().getDataType();
        return m_iDataType;
    }

    /**
     * 设置数据类型. 创建日期:(01-2-21 9:46:08)
     * 
     * @param newDataType
     *        int
     */
    public void setDataType(int newDataType) {
        /*
         * if (m_iDataType != newDataType) setItemEditor(null);
         */

        m_iDataType = newDataType;
    }

    public boolean isRefReturnCode() {

        return getRefTypeSet().isReturnCode();

    }

    public RefCustomizedVO getRefTypeSet() {

        if (getDataType() != IBillItem.UFREF) return null;

        if (set == null) {
            if (getRefType() != null) {

                /*
                 * if (getPos() == BODY) set = RefCustomizedUtil.deCode(getRefType(), true); else
                 */
                set = RefCustomizedUtil.deCode(getRefType(), false);

            } else
                set = new RefCustomizedVO();
        }

        if (getMetaDataProperty() != null && !isIsDef()) {
            set.setDataPowerOperation_Code(getMetaDataProperty().getAttribute().getAccessPowerGroup());
        }

        return set;
    }

    /**
     * 获得参照类型. 创建日期:(01-2-21 9:51:24)
     * 
     * @return int
     */
    public String getRefType() {

        if (getMetaDataProperty() != null && m_strRefType == null) {
            return getMetaDataProperty().getRefType();
        }

        return m_strRefType;
    }

    public void setRefType(String newRefType) {
        m_strRefType = newRefType;
    }

    /**
     * 自定义项 创建日期:(2003-8-20 10:39:41)
     * 
     * @return boolean
     */
    public boolean isIsDef() {
        return isDef;
    }

    public ArrayList<MMGPGantItem> getRelationItem() {
        return relationItem;
    }

    public void addRelationItem(MMGPGantItem item) {
        if (relationItem == null) relationItem = new ArrayList<MMGPGantItem>();

        relationItem.add(item);
    }

    public String getMetaDataAccessPath() {
        return metaDataAccessPath;
    }

    public IGetBillRelationItemValue getGetBillRelationItemValue() {
        // 设置关联项取值器
        if (getMetaDataProperty() != null && getBillRelationItemValue == null)
            getBillRelationItemValue =
                    new MetaDataGetBillRelationItemValue(getMetaDataProperty().getRefBusinessEntity());

        return getBillRelationItemValue;
    }

    public void setGetBillRelationItemValue(IGetBillRelationItemValue getBillRelationItemValue) {
        this.getBillRelationItemValue = getBillRelationItemValue;
    }

    /**
     * 获得小数点位数. 创建日期:(2001-5-14 13:30:36)
     * 
     * @return int
     */
    public int getDecimalDigits() {
        if (getNumberFormat() == null) {
            return -1;
        }
        return getNumberFormat().getDecimalDigits();
    }

    public BillItemNumberFormat getNumberFormat() {
        if (m_numberFormat == null) {
            if (getRefType() != null) {
                switch (getDataType()) {
                    case INTEGER:
                        m_numberFormat = BillUtil.parseIntgerFormat(getRefType());
                        break;
                    case DECIMAL:
                    case MONEY:
                        m_numberFormat = BillUtil.parseDoubleFormat(getRefType());

                    default:
                        break;
                }
            } else {
                m_numberFormat = new BillItemNumberFormat();
            }

        }
        return m_numberFormat;
    }

    /*
     * 解析类型设置
     */
    private void initAttribByParseReftype() {

        if (getRefType() == null || getRefType().trim().length() == 0) {
            return;
        }
        String reftype = getRefType().trim();

        // 自定义类型处理
        if (getDataType() == USERDEFITEM) {
            String[] tokens = BillUtil.getStringTokensWithNullToken(reftype, ":");

            if (tokens.length > 0) {
                try {
                    // 非原数据数据类型
                    if (getMetaDataProperty() != null) {
                        IMetaDataProperty mdp =
                                MetaDataPropertyFactory.creatMetaDataUserDefPropertyByType(
                                    getMetaDataProperty(),
                                    tokens[0]);
                        setMetaDataProperty(mdp);
                    } else {
                        int datatype = Integer.parseInt(tokens[0]);
                        if (datatype <= USERDEFITEM) setDataType(datatype);
                    }
                    if (tokens.length > 1) {
                        setRefType(tokens[1]);
                    } else {
                        setRefType(null);
                        return;
                    }
                } catch (NumberFormatException e) {
                    Logger.warn("数据类型设置错误：" + tokens[0]);
                    setDataType(IBillItem.STRING);
                    setRefType(null);
                    return;
                }
            }
        } else if (getDataType() == IBillItem.DATE) {
            if (getRefType().equals("B")) {
                setDateformat(IBillItem.DATE_FORMAT_BEGIN);
            } else if (getRefType().equals("E")) {
                setDateformat(IBillItem.DATE_FORMAT_END);
            }
        }
    }

    public String getNodecode() {
        return nodecode;
    }

    public void setNodecode(String nodecode) {
        this.nodecode = nodecode;
    }

    public AbstractRefModel getRefModel() {
        if (refModel == null) {
            initRefModel();
        }
        return refModel;
    }

    public void setRefModel(AbstractRefModel refModel) {
        this.refModel = refModel;
    }

    public int getDateformat() {
        return dateformat;
    }

    public void setDateformat(int dateformat) {
        this.dateformat = dateformat;
    }

    public void addDecimalListener(ITreeTableModelDecimalListener bdl) {
        this.decimalListener = bdl;
    }

    public void removeDecimalListener() {
        this.decimalListener = null;
    }

    public ITreeTableModelDecimalListener getDecimalListener() {
        return this.decimalListener;
    }

    private void clearItemConverter() {
        converter = null;
    }

    /**
     * 设置小数位数.
     * 
     * @param iDecimalDigits
     *        int
     */
    public void setDecimalDigits(int iDecimalDigits) {
        // if (getDataType() == DECIMAL) {

        if (iDecimalDigits > maxDecimalDigit) {
            setMaxDecimalDigit(iDecimalDigits);
        }

        if (getNumberFormat().getDecimalDigits() != iDecimalDigits) {

            clearItemConverter();

            getNumberFormat().setDecimalDigits(iDecimalDigits);

            /*
             * fireBillItemPropertyChange(BillItemPropertyChangeEvent.DECIMAL_DIGITS );
             */

        }

    }

    public void setIsDef(boolean newIsDef) {
        isDef = newIsDef;
        clearItemConverter();
    }

    protected void setMaxDecimalDigit(int maxDecimalDigit) {
        this.maxDecimalDigit = maxDecimalDigit;
    }

    public MMGPGantItem copyToGantItem() {
        MMGPGantItem newItem = new MMGPGantItem();
        newItem.setDataType(this.getDataType());
        newItem.setIDColName(this.getIDColName());
        newItem.setKey(this.getKey());
        newItem.setNodecode(this.getNodecode());
        newItem.setEdit(this.isEdit());
        newItem.setDateformat(this.getDecimalDigits());
        newItem.setGetBillRelationItemValue(this.getGetBillRelationItemValue());
        newItem.setMetaDataProperty(this.getMetaDataProperty());
        newItem.setRefModel(this.getRefModel());
        newItem.setLoadFormula(this.getLoadFormula());
        newItem.setRefType(this.getRefType());
        newItem.setShow(this.isShow());
        newItem.setShowName(this.getShowName());
        newItem.setShowOrder(this.getShowOrder());
        newItem.setValueClassType(this.getValueClassType());
        newItem.metaDataRelation = this.getMetaDataRelation();
        newItem.metaDataAccessPath = this.getMetaDataAccessPath();
        newItem.setIsDef(this.isIsDef());
        /*
         * try { StageplanGantItem newItem =(StageplanGantItem)this.clone(); return newItem; } catch
         * (CloneNotSupportedException e) { Logger.error("不支持克隆方法"); }
         */
        return newItem;
    }
    
    /**
     * 返回输入长度. 创建日期:(01-2-21 9:44:18)
     * 
     * @return int
     */
    public int getLength() {
        if (m_iLength == UNSET && getMetaDataProperty() != null)
            return getMetaDataProperty().getInputLength();
        return m_iLength;
    }
    
    /**
     * 设置输入长度. 创建日期:(01-2-21 9:44:18)
     * 
     * @param newLength
     *            int
     */
    public void setLength(int newLength) {
        m_iLength = newLength;
        // if (m_compContent != null) {
        // if (m_compContent instanceof UIRefPane) {
        // ((UIRefPane) m_compContent).setMaxLength(newLength);
        // }
        // }
    }
}
