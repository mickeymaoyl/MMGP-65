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
 * <b> �������̽ӿڹ��� </b>
 * <p>
 * ���Ը���Ԫ��������ĵ������̽ӿڶ�����ֶ�ӳ���ҵ�Ԫ�����ж�Ӧ���ֶΣ������ȵ�
 * </p>
 * 
 * @since: ��������:2014-10-29
 * @author:liwsh
 */
public class MMGPDefaultBillFlow implements IBillFlow {

    private static final String TAG_SRC_TYPE = "srctype";

    /**
     * Ԫ����ʵ��
     */
    private IBusinessEntity bean = null;

    /**
     * ���̵��ݽӿ��ֶ�ӳ���ϵ
     */
    private Map<String, String> dataMap = null;

    /**
     * ���̵��� ���ֶνӿ�ӳ���ϵ
     */
    private Map<String, String> mutiInterMap = null;

    /**
     * ����ƽ̨Ĭ�ϵĹ���
     */
    private DefaultBillFlow defaultBillFlow = null;


    /**
     * ����ƽ̨Ĭ�϶���Դ�Ĺ���
     */
    private SrcGroupSuportBillFlow[] srcgroupBillFlow = null;

    /**
     * ���캯��
     * 
     * @param billType
     *        ��������
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
     * ����
     * 
     * @param billType
     *        ��������
     * @return
     */
    public static MMGPDefaultBillFlow getInstance(String billType) {
        return new MMGPDefaultBillFlow(billType);
    }

    /**
     * ��˾
     */
    @Override
    public String getBillCorp() {
        return defaultBillFlow.getBillCorp();
    }

    /**
     * ���ݺ�
     */
    @Override
    public String getBillNOField() {
        return defaultBillFlow.getBillNOField();
    }

    /**
     * ��������
     */
    @Override
    public String getBillTypeField() {
        return defaultBillFlow.getBillTypeField();
    }

    /**
     * ��ʵ�����
     */
    @Override
    public String getMainTableName() {
        return defaultBillFlow.getMainTableName();
    }

    /**
     * ��ʵ��������
     */
    @Override
    public String getMainTablePrimaryKeyFiled() {
        return defaultBillFlow.getMainTablePrimaryKeyFiled();
    }

    /**
     * ��Դ����id��Ŀǰ�ݲ�֧�ֶ���Դ���
     */
    @Override
    public String getSourceIDField() {
        String srcIdField = defaultBillFlow.getSourceIDField();

        if (MMStringUtil.isNotEmpty(srcIdField)) {
            return srcIdField;
        }

        // ���ֶνӿڲ���
        srcIdField = getSrcIdFieldFromSrcGroup();
        return srcIdField;
    }


    /**
     * ��ȡ��Դ����id���������ֶνӿ�
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
     * ��Դ������id
     */
    @Override
    public String getSourceRowField() {
        return defaultBillFlow.getSourceRowField();
    }

    /**
     * ��Դ��������
     */
    @Override
    public String getSourceTypeField() {

        String srcTypeField = defaultBillFlow.getSourceTypeField();

        if (MMStringUtil.isNotEmpty(srcTypeField)) {
            return srcTypeField;
        }

        // ���ֶνӿڲ���
        srcTypeField = getSrcTypeFieldFromSrcGroup();
        return srcTypeField;
    }
    

    /**
     * �������ֶνӿڻ�ȡ��Դ��������
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
     * �ӱ����
     */
    @Override
    public String getSubTableForeignKeyFiled() {
        String subForeignKey = defaultBillFlow.getSubTableForeignKeyFiled();
        if (MMStringUtil.isNotEmpty(subForeignKey)) {
            return subForeignKey;
        }

        // ���ֶνӿڲ���
        return getSubFrgnKeyFromSrcGroup();

    }


    /**
     * �����ֶνӿڻ�ȡ�ӱ����
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
     * �ӱ�
     */
    @Override
    public String getSubTableName() {

        String subtableName = defaultBillFlow.getSubTableName();

        if (MMStringUtil.isNotEmpty(subtableName)) {
            return subtableName;
        }

        // ���ֶνӿڲ���
        return getSubTableNameFromSrcGroup();
    }


    private String getSubTableNameFromSrcGroup() {
        if (MMArrayUtil.isEmpty(srcgroupBillFlow)) {
            return null;
        }
        return srcgroupBillFlow[0].getSubTableName();
    }

    /**
     * �ӱ��к�
     */
    @Override
    public String getSubTableRowNoField() {
        return defaultBillFlow.getSubTableRowNoField();
    }

    /**
     * ��������
     */
    @Override
    public String getTransTypeField() {
        return defaultBillFlow.getTransTypeField();
    }

    /**
     * ��������pk
     */
    @Override
    public String getTransTypePkField() {
        return defaultBillFlow.getTransTypePkField();
    }

    /**
     * ��ȡ�ӱ�����
     * 
     * @return
     */
    public String getSubTableClassName() {

        String srcid = this.dataMap.get(IFlowBizItf.ATTRIBUTE_SRCBILLID);
        IAttribute attr = this.bean.getAttributeByPath(srcid);

        if (attr != null) {
            return attr.getOwnerBean().getFullClassName();
        }

        // �����ֶνӿڲ���
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
     * ��ȡ�ӱ�Class
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
     * ����ӱ�������
     * 
     * @return ������
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
     * ��ȡ�����ӱ�Bean
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
     * ��ȡ��������
     * 
     * @return ����
     */
    public String getMainTableClassName() {
        return this.bean.getFullClassName();
    }

}
