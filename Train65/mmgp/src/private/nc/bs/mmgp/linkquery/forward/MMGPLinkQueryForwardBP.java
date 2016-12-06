package nc.bs.mmgp.linkquery.forward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.mmgp.bill.billsource.MMGPDefaultBillFlow;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pf.pub.PfDataCache;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.linkquery.LinkQueryBillNode;
import nc.vo.mmgp.linkquery.LinkQueryParamVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.forwardbilltype.ForwardBillTypeVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> ��ѯ�������е��� </b>
 * <p>
 * ����ѯ��ֻ�ҵ�����װ�����죬��������Ӧ����������ص��ݣ�����δ֪ģ��Ԫ�������õ�ʵ����̫���淶�ˣ���ʱ��֧��
 * </p>
 * 
 * @since: ��������:Nov 4, 2014
 * @author:gaotx
 */
public class MMGPLinkQueryForwardBP {

    private static final String BILLTYPE_PLO = "YG81";

    /**
     * ��Դ���ݱ���ID�ֶ�������Ϣ��key->�������� value->��Դ���ݱ���id����<br\>
     * ��ԭ�򣬲������е��ݱ����ֶ���Դ���ݱ�����������vsrcbid��������ǣ�ͨ������������������
     */
    private Map<String, String> vsrcbidkeyMap = null;

    /**
     * �������ͷ�Χ��ֻ��ѯ�÷�Χ�ĵ�������������ݣ�Ŀǰֻ�������죬��Ӧ����װ������ȡ�
     */
    private Set<String> billTypeScale = new HashSet<String>();

    public MMGPLinkQueryForwardBP(Map<String, String> vsrcbidkeyMap) {
        if (vsrcbidkeyMap != null) {
            this.vsrcbidkeyMap = vsrcbidkeyMap;
        }
        initBillTypeScale();
    }

    /**
     * ��ʼ���������� Ŀǰֻ��ѯ��棬���ۣ��ɹ���ί�⣬������������װ����ص�������,��ʱֻ�����ã������޶���һЩ����ֹ������<br/>
     * MMPAC����ɢ���������ϣ��������棩<br/>
     * MMSFC(�ɹ��������깤���棩<br/>
     * IC(��⣬������أ�<br/>
     * ί������<br/>
     * װ������<br/>
     * �ɹ����빺������������<br/>
     */
    private void initBillTypeScale() {
        String wherePart =
                " istransaction = 'N' and (systemcode in ('SC')"
                    + " or systemcode like 'YG%'"
                    + " or pk_billtypecode in ('45','46','4X','4E','4A',"
                    + "'4I','4401','4Y','4451','4455','4C','4D',"
                    + "'55C2','55A3','55A4','55D1','55D2','30','20','21','23','5A','5X') ) ";
        try {
            List<BilltypeVO> billTypeList =
                    (List<BilltypeVO>) new BaseDAO().retrieveByClause(
                        BilltypeVO.class,
                        wherePart,
                        new String[]{"pk_billtypecode" });
            if (MMCollectionUtil.isEmpty(billTypeList)) {
                return;
            }
            for (BilltypeVO o : billTypeList) {
                this.billTypeScale.add(o.getPk_billtypecode());
            }
        } catch (DAOException e) {
            ExceptionUtils.wrappException(e);
        }
    }

    public List<LinkQueryBillNode> queryOneTypeForwardBodyVO(String billtype,
                                                             List<LinkQueryParamVO> value) {
        List<LinkQueryBillNode> nodeList = LinkQueryBillNode.convertFromLinkQueryParamVOs(value);
        if (MMCollectionUtil.isEmpty(value)) {
            return nodeList;
        }
        // �ݹ��ѯ���ε���
        recursionQueryForwardBills(billtype, nodeList);
        return nodeList;
    }

    private void recursionQueryForwardBills(String billtype,
                                            List<LinkQueryBillNode> nodeList) {
        String[] forwardBillTypes = getForwardBillTypes(billtype);
        if (MMArrayUtil.isEmpty(forwardBillTypes)) {
            return;
        }
        for (String oneBillType : forwardBillTypes) {
            // ���ڷ�Χ�ڵĵ������Ͳ���ѯ
            if (!this.getBillTypeScale().contains(oneBillType)) {
                continue;
            }
            // ��ȡһ�����ε�������
            String realBillType = PfUtilBaseTools.getRealBilltype(oneBillType);
            // �������ε������ͺ͵�ǰ���ݲ�ѯ���ε���
            List<LinkQueryBillNode> forwardBills = queryForwardBills(realBillType, nodeList);
            // û������ֱ������
            if (MMCollectionUtil.isEmpty(forwardBills)) {
                continue;
            }
            // ���������ι�ϵ
            buildRelations(nodeList, forwardBills);
            // �ݹ��ѯ
            recursionQueryForwardBills(realBillType, forwardBills);
        }
    }

    private void buildRelations(List<LinkQueryBillNode> nodeList,
                                List<LinkQueryBillNode> forwardBills) {
        if (MMCollectionUtil.isEmpty(nodeList) || MMCollectionUtil.isEmpty(forwardBills)) {
            return;
        }
        for (LinkQueryBillNode oneNode : nodeList) {
            for (LinkQueryBillNode oneForward : forwardBills) {
                if (oneNode.buildUniqueKey().equals(oneForward.buildSrcUniqueKey())) {
                    oneNode.addForwardBill(oneForward);
                    oneForward.setParentNode(oneNode);
                }
            }
        }
    }

    /**
     * ��ѯ���ε���
     * 
     * @param realBillType
     * @param nodeList
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<LinkQueryBillNode> queryForwardBills(String realBillType,
                                                      List<LinkQueryBillNode> nodeList) {
        List<LinkQueryBillNode> rtnList = new ArrayList<LinkQueryBillNode>();
        // ��ѯ�ֶ�
        String billidKey = MMGPDefaultBillFlow.getInstance(realBillType).getSubTableForeignKeyFiled();
        String billbidKey = MMGPDefaultBillFlow.getInstance(realBillType).getSubTablePrimarykey();
        // ���bid�ֶθ�id�ֶ�һ�£��������ֱ�idΪ��
        if(MMStringUtil.isNotEmpty(billidKey) && billidKey.equals(billbidKey)){
            billbidKey = null;
        }
        String srcidKey = MMGPDefaultBillFlow.getInstance(realBillType).getSourceIDField();
        String srctypeKey = MMGPDefaultBillFlow.getInstance(realBillType).getSourceTypeField();
        String srcbidKey = this.getVsrcbidKey(realBillType);

        // ��ѯ��Դ����ID
        Set<String> vsrcidSet = getSrcidSet(nodeList);
        // ��ѯ��Դ���ݱ���ID
        Set<String> vsrcbidSet = getSrcbidSet(nodeList);
        // �����ֱ�class
        String subTableName = MMGPDefaultBillFlow.getInstance(realBillType).getSubTableName();
        if (MMStringUtil.isEmpty(billidKey) || MMStringUtil.isEmpty(srcidKey) || MMStringUtil.isEmpty(subTableName)) {
            return rtnList;
        }
        // ������ѯSQL
        StringBuilder sql = new StringBuilder();
        if(MMGlobalConst.BILL_TYPE_WR.equals(realBillType)){
            sql = buildWrSql(vsrcidSet, vsrcbidSet);
        }else{
            sql = buildNormalSql(realBillType, billidKey, billbidKey, srcidKey, srctypeKey, srcbidKey, vsrcidSet, vsrcbidSet, subTableName);    
        }
        
        
        try {
            rtnList =
                    (List<LinkQueryBillNode>) new BaseDAO().executeQuery(sql.toString(), new BeanListProcessor(
                        LinkQueryBillNode.class));
        } catch (DAOException e) {
            Logger.error("-----------" + realBillType);
            Logger.error("-----------" + subTableName);
            ExceptionUtils.wrappException(e);
        }
        return rtnList;
    }
    
    /**
     * ��ѯ�깤�����Ӧ�����ݣ������깤����ӳ�����⣬���Ա������⴦��
     * @param realBillType
     * @param billidKey
     * @param billbidKey
     * @param srcidKey
     * @param srctypeKey
     * @param srcbidKey
     * @param vsrcidSet
     * @param vsrcbidSet
     * @param subTableName
     * @return
     */
    private StringBuilder buildWrSql(Set<String> vsrcidSet,
                                     Set<String> vsrcbidSet) {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" select  mm_wr_product.pk_wr  billid, ");
        sbSql.append("         mm_wr_product.pk_wr_product billbid, ");
        sbSql.append("         mm_wr_quality.pk_wr_quality  realbillbid, ");
        sbSql.append("         '55A4'  billtype, ");
        sbSql.append("         mm_wr_product.vbsrcid  vsrcid, ");
        sbSql.append("         mm_wr_product.cbsrctype  vsrctype, ");
        sbSql.append("         mm_wr_product.vbsrcrowid  vsrcbid ");
        sbSql.append(" from    mm_wr_product ");
        sbSql.append(" left  join  mm_wr_quality  on  mm_wr_product.pk_wr_product  =  mm_wr_quality.pk_wr_product_q "); //��Ϊleft join�����δ�ʼ���������
        sbSql.append(" where  mm_wr_product.dr = 0 ");
        if (MMCollectionUtil.isNotEmpty(vsrcidSet)) {
            sbSql.append(" and ").append(
                new IDExQueryBuilder("tmp_lkf_srcid_").buildSQL(
                    "mm_wr_product.vbsrcid",
                    vsrcidSet.toArray(new String[0])));
        }
        if (MMCollectionUtil.isNotEmpty(vsrcbidSet)) {
            sbSql.append(" and ").append(
                new IDExQueryBuilder("tmp_lkf_srcbid_").buildSQL(
                    "mm_wr_product.vbsrcrowid",
                    vsrcbidSet.toArray(new String[0])));
        }
        return sbSql;
    }

    /**
     * ���������Ϣ������ѯSQL
     * @param realBillType
     * @param billidKey
     * @param billbidKey
     * @param srcidKey
     * @param srctypeKey
     * @param srcbidKey
     * @param vsrcidSet
     * @param vsrcbidSet
     * @param subTableName
     * @return
     */
    private StringBuilder buildNormalSql(String realBillType,
                                         String billidKey,
                                         String billbidKey,
                                         String srcidKey,
                                         String srctypeKey,
                                         String srcbidKey,
                                         Set<String> vsrcidSet,
                                         Set<String> vsrcbidSet,
                                         String subTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" ").append(billidKey).append(" billid ").append(", ");
        if (MMStringUtil.isNotEmpty(billbidKey)) {
            sql.append(" ").append(billbidKey).append(" billbid ").append(", ");
        }
        sql.append(" '").append(realBillType).append("' billtype ").append(", ");
        sql.append(" ").append(srcidKey).append(" vsrcid ").append(", ");
        sql.append(" ").append(srctypeKey).append(" vsrctype ").append(", ");
        if (MMStringUtil.isNotEmpty(srcbidKey)) {
            sql.append(" ").append(srcbidKey).append(" vsrcbid ").append(" ");
        }
        sql.append(" from ").append(subTableName);
        sql.append(" where dr = 0 ");
        if (MMStringUtil.isNotEmpty(srcidKey) && MMCollectionUtil.isNotEmpty(vsrcidSet)) {
            sql.append(" and ").append(
                new IDExQueryBuilder("tmp_lkf_srcid_").buildSQL(srcidKey, vsrcidSet.toArray(new String[0])));
        }
        if (MMCollectionUtil.isNotEmpty(vsrcbidSet)) {
            sql.append(" and ").append(
                new IDExQueryBuilder("tmp_lkf_srcbid_").buildSQL(srcbidKey, vsrcbidSet.toArray(new String[0])));
        }
        return sql;
    }

    private Set<String> getSrcidSet(List<LinkQueryBillNode> nodeList) {
        Set<String> rtnSet = new HashSet<String>();
        for (LinkQueryBillNode aNode : nodeList) {
            if (MMStringUtil.isNotEmpty(aNode.getBillid())) {
                rtnSet.add(aNode.getBillid());
            }
        }
        return rtnSet;
    }

    /**
     * ��ȡ��Դ���ݱ���ID
     * 
     * @param nodeList
     * @return
     */
    private Set<String> getSrcbidSet(List<LinkQueryBillNode> nodeList) {
        Set<String> rtnSet = new HashSet<String>();
        for (LinkQueryBillNode aNode : nodeList) {
            if(aNode.getBilltype().equals(BILLTYPE_PLO)){// YG81-�ƻ��������ƻ�����û���壬���Բ���Ҫ�ӽ���
                continue;
            }
            if (MMStringUtil.isNotEmpty(aNode.getBillbid())) { 
                if(aNode.getBilltype().equals(MMGlobalConst.BILL_TYPE_WR)){ //�깤�������εĵ��ݼ�¼������������������⴦���߼�
                    if(MMStringUtil.isNotEmpty(aNode.getRealbillbid())){ //���û�������ʼ죬���α���֧�ִ���ҵ�������
                        rtnSet.add(aNode.getRealbillbid());    
                    }else{
                        rtnSet.add(aNode.getBillbid());
                    }
                    
                }else{
                    rtnSet.add(aNode.getBillbid());    
                }
            }
        }
        return rtnSet;
    }

    private String getVsrcbidKey(String billtype) {

        String vsrcbidkey = this.vsrcbidkeyMap.get(billtype);

        if (MMStringUtil.isEmpty(vsrcbidkey)) {
            return MMGlobalConst.VSRCBID;
        }

        return vsrcbidkey;
    }

    /**
     * ��ȡ���ε�������
     * 
     * @param billtype
     * @return
     */
    private String[] getForwardBillTypes(String billtype) {
        BilltypeVO billtypevo = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billtype));
        ForwardBillTypeVO[] forwardBillTypeVOs = billtypevo.getForwardBillTypeVOs();
        HashSet<String> set = new HashSet<String>();
        if (MMStringUtil.isEmpty(billtypevo.getForwardbilltype())) {
            return set.toArray(new String[0]);
        }
        String[] forwardbillType = billtypevo.getForwardbilltype().split(",");
        if (MMArrayUtil.isNotEmpty(forwardbillType)) {
            set.addAll(Arrays.asList(forwardbillType));
        }
        if (forwardBillTypeVOs != null) {
            for (ForwardBillTypeVO forwardBillTypeVO : forwardBillTypeVOs) {
                set.add(forwardBillTypeVO.getPk_fwdbilltype());
            }
        }
        return set.toArray(new String[0]);
    }

    public Set<String> getBillTypeScale() {
        return billTypeScale;
    }

    public void setBillTypeScale(Set<String> billTypeScale) {
        this.billTypeScale = billTypeScale;
    }
}
