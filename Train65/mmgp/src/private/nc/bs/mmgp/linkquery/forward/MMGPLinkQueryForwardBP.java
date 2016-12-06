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
 * <b> 查询下游所有单据 </b>
 * <p>
 * 本查询类只找到所有装备制造，生产，供应链到入库的相关单据，其他未知模块元数据设置的实在是太不规范了，暂时不支持
 * </p>
 * 
 * @since: 创建日期:Nov 4, 2014
 * @author:gaotx
 */
public class MMGPLinkQueryForwardBP {

    private static final String BILLTYPE_PLO = "YG81";

    /**
     * 来源单据表体ID字段名称信息，key->单据类型 value->来源单据表体id名称<br\>
     * （原因，不是所有单据表体字段来源单据表体主键都叫vsrcbid，如果不是，通过这个参数传入进来）
     */
    private Map<String, String> vsrcbidkeyMap = null;

    /**
     * 单据类型范围，只查询该范围的单据类型相关数据，目前只包含制造，供应链，装备制造等。
     */
    private Set<String> billTypeScale = new HashSet<String>();

    public MMGPLinkQueryForwardBP(Map<String, String> vsrcbidkeyMap) {
        if (vsrcbidkeyMap != null) {
            this.vsrcbidkeyMap = vsrcbidkeyMap;
        }
        initBillTypeScale();
    }

    /**
     * 初始化单据类型 目前只查询库存，销售，采购，委外，调拨，生产，装备相关单据类型,暂时只有我用，所以限定死一些，防止报错误<br/>
     * MMPAC（离散生产，备料，生产报告）<br/>
     * MMSFC(派工，供需完工报告）<br/>
     * IC(入库，出库相关）<br/>
     * 委外所有<br/>
     * 装备所有<br/>
     * 采购，请购，调拨，销售<br/>
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
        // 递归查询下游单据
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
            // 不在范围内的单据类型不查询
            if (!this.getBillTypeScale().contains(oneBillType)) {
                continue;
            }
            // 获取一个下游单据类型
            String realBillType = PfUtilBaseTools.getRealBilltype(oneBillType);
            // 根据下游单据类型和当前单据查询下游单据
            List<LinkQueryBillNode> forwardBills = queryForwardBills(realBillType, nodeList);
            // 没有下游直接跳过
            if (MMCollectionUtil.isEmpty(forwardBills)) {
                continue;
            }
            // 构建上下游关系
            buildRelations(nodeList, forwardBills);
            // 递归查询
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
     * 查询下游单据
     * 
     * @param realBillType
     * @param nodeList
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<LinkQueryBillNode> queryForwardBills(String realBillType,
                                                      List<LinkQueryBillNode> nodeList) {
        List<LinkQueryBillNode> rtnList = new ArrayList<LinkQueryBillNode>();
        // 查询字段
        String billidKey = MMGPDefaultBillFlow.getInstance(realBillType).getSubTableForeignKeyFiled();
        String billbidKey = MMGPDefaultBillFlow.getInstance(realBillType).getSubTablePrimarykey();
        // 如果bid字段跟id字段一致，则设置字表id为空
        if(MMStringUtil.isNotEmpty(billidKey) && billidKey.equals(billbidKey)){
            billbidKey = null;
        }
        String srcidKey = MMGPDefaultBillFlow.getInstance(realBillType).getSourceIDField();
        String srctypeKey = MMGPDefaultBillFlow.getInstance(realBillType).getSourceTypeField();
        String srcbidKey = this.getVsrcbidKey(realBillType);

        // 查询来源单据ID
        Set<String> vsrcidSet = getSrcidSet(nodeList);
        // 查询来源单据表体ID
        Set<String> vsrcbidSet = getSrcbidSet(nodeList);
        // 下游字表class
        String subTableName = MMGPDefaultBillFlow.getInstance(realBillType).getSubTableName();
        if (MMStringUtil.isEmpty(billidKey) || MMStringUtil.isEmpty(srcidKey) || MMStringUtil.isEmpty(subTableName)) {
            return rtnList;
        }
        // 创建查询SQL
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
     * 查询完工报告对应的数据，由于完工报告映射问题，所以必须特殊处理
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
        sbSql.append(" left  join  mm_wr_quality  on  mm_wr_product.pk_wr_product  =  mm_wr_quality.pk_wr_product_q "); //改为left join，解决未质检入库的问题
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
     * 根据相关信息创建查询SQL
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
     * 获取来源单据表体ID
     * 
     * @param nodeList
     * @return
     */
    private Set<String> getSrcbidSet(List<LinkQueryBillNode> nodeList) {
        Set<String> rtnSet = new HashSet<String>();
        for (LinkQueryBillNode aNode : nodeList) {
            if(aNode.getBilltype().equals(BILLTYPE_PLO)){// YG81-计划订单，计划订单没表体，所以不需要加进来
                continue;
            }
            if (MMStringUtil.isNotEmpty(aNode.getBillbid())) { 
                if(aNode.getBilltype().equals(MMGlobalConst.BILL_TYPE_WR)){ //完工报告下游的单据记录的是孙表，所以增加特殊处理逻辑
                    if(MMStringUtil.isNotEmpty(aNode.getRealbillbid())){ //解决没有生成质检，下游报不支持此种业务的问题
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
     * 获取下游单据类型
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
