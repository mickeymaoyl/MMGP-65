package nc.bs.mmgp.bill.billsource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.trade.billsource.DefaultBillFlow;
import nc.bs.trade.billsource.IBillFlow;
import nc.impl.pubapp.linkquery.SrcGroupSuportBillFlow;
import nc.itf.pubapp.pub.bill.ISrcGroupSuportBillFlow;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.common.AssociationKind;
import nc.md.model.IAssociation;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 单据流程接口工具 </b>
 * <p>
 * 可以根据元数据里面的单据流程接口定义的字段映射找到元数据中对应的字段，表名等等
 * </p>
 * 
 * @since: 创建日期:2014-10-29
 * @author:liwsh
 */
public class MMGPDefaultBillFlow implements IBillFlow {

    private static final String TAG_SRC_TYPE = "srctype";

    /**
     * 元数据实体
     */
    private IBusinessEntity bean = null;

    /**
     * 流程单据接口字段映射关系
     */
    private Map<String, String> dataMap = null;

    /**
     * 流程单据 两字段接口映射关系
     */
    private Map<String, String> mutiInterMap = null;

    /**
     * 流程平台默认的工具
     */
    private DefaultBillFlow defaultBillFlow = null;


    /**
     * 流程平台默认多来源的工具
     */
    private SrcGroupSuportBillFlow[] srcgroupBillFlow = null;

    /**
     * 构造函数
     * 
     * @param billType
     *        单据类型
     */
    private MMGPDefaultBillFlow(String billType) {
        super();

        defaultBillFlow = DefaultBillFlow.getInstance(billType);
        srcgroupBillFlow = SrcGroupSuportBillFlow.createByBillType(billType);

        try {
            this.bean = PfMetadataTools.queryMetaOfBilltype(billType);
            this.dataMap = this.bean.getBizInterfaceMapInfo(IFlowBizItf.class.getName());
            this.mutiInterMap = this.bean.getBizInterfaceMapInfo(ISrcGroupSuportBillFlow.class.getName());

        } catch (BusinessException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 单例
     * 
     * @param billType
     *        单据类型
     * @return
     */
    public static MMGPDefaultBillFlow getInstance(String billType) {
        return new MMGPDefaultBillFlow(billType);
    }

    /**
     * 公司
     */
    @Override
    public String getBillCorp() {
        return defaultBillFlow.getBillCorp();
    }

    /**
     * 单据号
     */
    @Override
    public String getBillNOField() {
        return defaultBillFlow.getBillNOField();
    }

    /**
     * 单据类型
     */
    @Override
    public String getBillTypeField() {
        return defaultBillFlow.getBillTypeField();
    }

    /**
     * 主实体表名
     */
    @Override
    public String getMainTableName() {
        return defaultBillFlow.getMainTableName();
    }

    /**
     * 主实体主键名
     */
    @Override
    public String getMainTablePrimaryKeyFiled() {
        return defaultBillFlow.getMainTablePrimaryKeyFiled();
    }

    /**
     * 来源单据id，目前暂不支持多来源情况
     */
    @Override
    public String getSourceIDField() {
        String srcIdField = defaultBillFlow.getSourceIDField();

        if (MMStringUtil.isNotEmpty(srcIdField)) {
            return srcIdField;
        }

        // 两字段接口查找
        srcIdField = getSrcIdFieldFromSrcGroup();
        return srcIdField;
    }


    /**
     * 获取来源单据id，从两个字段接口
     * 
     * @return
     */
    private String getSrcIdFieldFromSrcGroup() {
        if (MMArrayUtil.isEmpty(srcgroupBillFlow)) {
            return null;
        }
        return srcgroupBillFlow[0].getSourceIDField();
    }

    /**
     * 来源单据行id
     */
    @Override
    public String getSourceRowField() {
        return defaultBillFlow.getSourceRowField();
    }

    /**
     * 来源单据类型
     */
    @Override
    public String getSourceTypeField() {

        String srcTypeField = defaultBillFlow.getSourceTypeField();

        if (MMStringUtil.isNotEmpty(srcTypeField)) {
            return srcTypeField;
        }

        // 两字段接口查找
        srcTypeField = getSrcTypeFieldFromSrcGroup();
        return srcTypeField;
    }
    

    /**
     * 从两个字段接口获取来源单据类型
     * 
     * @return
     */
    private String getSrcTypeFieldFromSrcGroup() {
        if (MMArrayUtil.isEmpty(srcgroupBillFlow)) {
            return null;
        }
        return srcgroupBillFlow[0].getSourceTypeField();
    }


    /**
     * 子表外键
     */
    @Override
    public String getSubTableForeignKeyFiled() {
        String subForeignKey = defaultBillFlow.getSubTableForeignKeyFiled();
        if (MMStringUtil.isNotEmpty(subForeignKey)) {
            return subForeignKey;
        }

        // 两字段接口查找
        return getSubFrgnKeyFromSrcGroup();

    }


    /**
     * 从两字段接口获取子表外键
     * 
     * @return
     */
    private String getSubFrgnKeyFromSrcGroup() {
        if (MMArrayUtil.isEmpty(srcgroupBillFlow)) {
            return null;
        }
        return srcgroupBillFlow[0].getSubTableForeignKeyFiled();
    }

    /**
     * 子表
     */
    @Override
    public String getSubTableName() {

        String subtableName = defaultBillFlow.getSubTableName();

        if (MMStringUtil.isNotEmpty(subtableName)) {
            return subtableName;
        }

        // 两字段接口查找
        return getSubTableNameFromSrcGroup();
    }


    private String getSubTableNameFromSrcGroup() {
        if (MMArrayUtil.isEmpty(srcgroupBillFlow)) {
            return null;
        }
        return srcgroupBillFlow[0].getSubTableName();
    }

    /**
     * 子表行号
     */
    @Override
    public String getSubTableRowNoField() {
        return defaultBillFlow.getSubTableRowNoField();
    }

    /**
     * 交易类型
     */
    @Override
    public String getTransTypeField() {
        return defaultBillFlow.getTransTypeField();
    }

    /**
     * 交易类型pk
     */
    @Override
    public String getTransTypePkField() {
        return defaultBillFlow.getTransTypePkField();
    }

    /**
     * 获取子表类名
     * 
     * @return
     */
    public String getSubTableClassName() {

        String srcid = this.dataMap.get(IFlowBizItf.ATTRIBUTE_SRCBILLID);
        IAttribute attr = this.bean.getAttributeByPath(srcid);

        if (attr != null) {
            return attr.getOwnerBean().getFullClassName();
        }

        // 从两字段接口查找
        return getSubClassNameFromMuti();

    }

    private String getSubClassNameFromMuti() {
        if (MMMapUtil.isEmpty(mutiInterMap)) {
            return null;
        }

        for (String srctype : mutiInterMap.keySet()) {
            if (srctype.indexOf(TAG_SRC_TYPE) > -1) {
                return this.bean.getAttributeByPath(mutiInterMap.get(srctype)).getOwnerBean().getFullClassName();
            }
        }

        return null;
    }

    /**
     * 获取子表Class
     * 
     * @return
     */
    public Class< ? extends SuperVO> getSubTableClass() {

        String subTableClasName = this.getSubTableClassName();
        if (subTableClasName == null) {
            return null;
        }

        try {
            return (Class< ? extends SuperVO>) Class.forName(subTableClasName);
        } catch (ClassNotFoundException e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    /**
     * 获得子表主键名
     * 
     * @return 主键名
     */
    public String getSubTablePrimarykey() {


        String srctype = this.dataMap.get(IFlowBizItf.ATTRIBUTE_SRCBILLTYPE);
        IAttribute attr = this.bean.getAttributeByPath(srctype);
        if (attr != null) {
            return attr.getOwnerBean().getPrimaryKey().getPKColumn().getName();
        }

        return this.getsubPrimarykeyFromMuti();

    }


    private String getsubPrimarykeyFromMuti() {
        if (MMMapUtil.isEmpty(mutiInterMap)) {
            return null;
        }

        for (String srctype : mutiInterMap.keySet()) {
            if (srctype.indexOf(TAG_SRC_TYPE) > -1) {
                return this.bean
                    .getAttributeByPath(mutiInterMap.get(srctype))
                    .getOwnerBean()
                    .getPrimaryKey()
                    .getPKColumn()
                    .getName();
            }
        }

        return null;
    }

    /**
     * 获取所有子表Bean
     * 
     * @return
     */
    public List<IBean> getAllSubBean() {

        List<IBean> allSubBeanList = new ArrayList<IBean>();

        List<IAssociation> associationList = this.bean.getAssociations();
        if (MMCollectionUtil.isEmpty(associationList)) {
            return allSubBeanList;
        }

        for (IAssociation association : associationList) {
            if (association.getType() == AssociationKind.Composite) {
                allSubBeanList.add(association.getEndBean());
            }
        }

        return allSubBeanList;
    }

    /**
     * 获取主表类名
     * 
     * @return 类名
     */
    public String getMainTableClassName() {
        return this.bean.getFullClassName();
    }

}
