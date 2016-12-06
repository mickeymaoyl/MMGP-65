package nc.impl.mmgp.linkquery;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.mmgp.bill.billsource.MMGPDefaultBillFlow;
import nc.impl.pubapp.pattern.database.IDQueryBuilder;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.md.model.IBean;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.mmpac.dmo.entity.DmoVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 单据联查数据库操作类 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since: 创建日期:2014-11-1
 * @author:liwsh
 */
public class MMGPLinkQueryDAO {

    /**
     * 单例
     */
    private static MMGPLinkQueryDAO linkdao = null;

    /**
     * 数据库操作单例
     */
    private static BaseDAO dao = null;

    /**
     * 获取单例
     *
     * @return 数据库操作对象
     */
    public static MMGPLinkQueryDAO getInstance() {
        if (linkdao == null) {
            linkdao = new MMGPLinkQueryDAO();
        }
        return linkdao;
    }

    /**
     * 获取数据库操作dAO
     *
     * @return 数据库操作dAO
     */
    private static BaseDAO getBaseDAO() {
        if (dao == null) {
            dao = new BaseDAO();
        }
        return dao;
    }

    /**
     * 根据单据类型和单据表体id，查询单据表体VO
     *
     * @param billtype
     *        单据类型
     * @param billbids
     *        单据表体id
     * @return 单据表体VO key-> 单据表体主键 value-> 单据表体VO
     */
    public Map<String, SuperVO> queryVOByPks(String billtype,
                                             String[] billbids,
                                             String[] selectFields) throws BusinessException {

        // 获取子表class
        String realDestBillType = PfUtilBaseTools.getRealBilltype(billtype);
        Class< ? extends SuperVO> destClass = MMGPDefaultBillFlow.getInstance(realDestBillType).getSubTableClass();
        if (destClass == null) {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0038")/*@res "目的单据类型"*/ + billtype + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0039")/*@res " 对应的单据元数据没有在单据流程信息获取回写接口里面定义来源单据映射信息"*/);
        }

        // 构造where条件
        String subTablePk = MMGPDefaultBillFlow.getInstance(realDestBillType).getSubTablePrimarykey();
        String pkCondition = buildWhereCond(billbids, subTablePk);

        // 查询
        Collection<SuperVO> billbodyVOCollctn =
                this.getBaseDAO().retrieveByClause(destClass, pkCondition, selectFields);

        // 构造返回结果
        if (MMCollectionUtil.isEmpty(billbodyVOCollctn)) {
            return Collections.emptyMap();
        }
        Map<String, SuperVO> billBodyMap = buildBillBodyMap(billbodyVOCollctn, realDestBillType, selectFields);

        return billBodyMap;
    }

    /**
     * 根据完工报告孙表id查询对应的生产订单
     *
     * @param wrqualityIds
     *        完工报告孙表id集合
     * @return key-> 完工报告孙表pk value-> 生产订单
     * @throws BusinessException
     */
    public Map<String, SuperVO> queryDmoForStockin(String[] wrqualityIds) throws BusinessException {

        if (MMArrayUtil.isEmpty(wrqualityIds)) {
            return Collections.emptyMap();
        }

        IDQueryBuilder idbuilder = new IDQueryBuilder();
        String idcond = idbuilder.buildSQL("wrquo.pk_wr_quality", wrqualityIds);

        StringBuilder sql = new StringBuilder();
        sql.append("select wrquo.pk_wr_quality,");
        sql.append("       dmo.vbilltype,");
        sql.append("       dmo.pk_dmo,");
        sql.append("       dmo.vsrctype,");
        sql.append("       dmo.vsrcbid,dmo.vsrcid");
        sql.append("  from mm_wr_quality wrquo");
        sql.append(" inner join mm_wr_product wrpro on wrpro.pk_wr_product =");
        sql.append("                                   wrquo.pk_wr_product_q");
        sql.append("                               and wrquo.dr = 0");
        sql.append("                               and wrpro.dr = 0");
        sql.append(" inner join mm_dmo dmo on dmo.pk_dmo = wrpro.cbmoid");
        sql.append("                      and dmo.dr = 0");
        sql.append("");
        sql.append(" where ");
        sql.append(idcond);

        List<Object[]> result =
                (List<Object[]>) (new BaseDAO()).executeQuery(sql.toString(), new ArrayListProcessor());

        if (MMCollectionUtil.isEmpty(result)) {
            return Collections.emptyMap();
        }

        Map<String, SuperVO> dmoMap = new HashMap<String, SuperVO>();
        for (Object[] objs : result) {
            DmoVO dmovo = new DmoVO();
            dmovo.setAttributeValue(MMGlobalConst.VBILLTYPECODE, MMGlobalConst.BILL_TYPE_PRODUCE);
            dmovo.setAttributeValue(MMGlobalConst.BILLBID, (String)objs[2]);
            dmovo.setAttributeValue(MMGlobalConst.VSRCTYPE, (String)objs[3]);
            String vsrcbid = (String) objs[4];
            if (MMStringUtil.isEmpty(vsrcbid)) {
                vsrcbid = (String) objs[5];
            }
            dmovo.setAttributeValue(MMGlobalConst.VSRCBID, vsrcbid);

            dmoMap.put((String) objs[0], dmovo);
        }

        return dmoMap;
    }

    /**
     * 构建单据表体MAP
     *
     * @param billbodyVOCollctn
     *        单据表体信息
     * @param realDestBillType
     * @return key-> 单据表体主键 value-> 单据表体
     */
    private Map<String, SuperVO> buildBillBodyMap(Collection<SuperVO> billbodyVOCollctn,
                                                  String realDestBillType, String[] selectFields) {

        Map<String, SuperVO> billBodyMap = new HashMap<String, SuperVO>();

        Iterator<SuperVO> iterator = billbodyVOCollctn.iterator();
        while (iterator.hasNext()) {
            SuperVO billbodyVO = iterator.next();

            //设置单据类型
            billbodyVO.setAttributeValue(MMGlobalConst.VBILLTYPECODE, realDestBillType);

            if (MMArrayUtil.isEmpty(selectFields)) {
                billBodyMap.put(billbodyVO.getPrimaryKey(), billbodyVO);
                continue;
            }

            //冗余单据表体id
            billbodyVO.setAttributeValue(MMGlobalConst.BILLBID, billbodyVO.getAttributeValue(selectFields[0]));
            //冗余来源单据类型
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCTYPE, billbodyVO.getAttributeValue(selectFields[1]));
            //冗余来源单据bid
            String vsrcbid = (String) billbodyVO.getAttributeValue(selectFields[2]);
            if (MMStringUtil.isEmpty(vsrcbid)) {
                vsrcbid = (String) billbodyVO.getAttributeValue(selectFields[3]);
            }
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCBID, vsrcbid);

            billBodyMap.put(billbodyVO.getPrimaryKey(), billbodyVO);
        }
        return billBodyMap;
    }


    /**
     * 构建查询条件
     *
     * @param billbids
     *        单据表体id值
     * @param subTablePk
     *        单据表体id名称
     * @return 查询条件
     */
    private String buildWhereCond(String[] billbids,
                                  String subTablePk) {
        IDQueryBuilder billbidBuilder = new IDQueryBuilder();
        String pkCondition = billbidBuilder.buildSQL(subTablePk, billbids);
        // modify by liwsh 2014-11-19 不考虑是否删除，因为这个过程中可能会遇到删除的单据
        // pkCondition = pkCondition + " and dr = 0 ";
        return pkCondition;
    }

    /**
     * 根据单据类型和单据表体id，从所有的表体中查询单据表体VO
     *
     * @param billtype
     *        单据类型
     * @param billbids
     *        单据表体id
     * @return key-> 单据表体id value-> 单据表体vo
     * @throws BusinessException
     */
    public Map<String, SuperVO> queryVOFromAllChild(String billtype,
                                                    String[] billbids) throws BusinessException {

        // 获取子表class
        String realDestBillType = PfUtilBaseTools.getRealBilltype(billtype);
        List<IBean> subBeanList = MMGPDefaultBillFlow.getInstance(realDestBillType).getAllSubBean();

        if (MMCollectionUtil.isEmpty(subBeanList)) {
            return Collections.emptyMap();
        }

        // 遍历所有的子表
        for (IBean subBean : subBeanList) {
            String primaryKey = subBean.getPrimaryKey().getPKColumn().getName();

            String subclassname = subBean.getFullClassName();
            Class< ? extends SuperVO> subclass = MMGPLinkQueryUtil.getClass(subclassname);

            String whereCond = this.buildWhereCond(billbids, primaryKey);

            // 查询
            Collection<SuperVO> billbodyVOCollctn = this.getBaseDAO().retrieveByClause(subclass, whereCond);

            if (MMCollectionUtil.isEmpty(billbodyVOCollctn)) {
                continue;
            }

            // 构造返回结果
            return this.buildBillBodyMap(billbodyVOCollctn, realDestBillType, null);

        }
        return Collections.emptyMap();
    }

}