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
 * <b> �����������ݿ������ </b>
 * <p>
 * ��ϸ��������
 * </p>
 *
 * @since: ��������:2014-11-1
 * @author:liwsh
 */
public class MMGPLinkQueryDAO {

    /**
     * ����
     */
    private static MMGPLinkQueryDAO linkdao = null;

    /**
     * ���ݿ��������
     */
    private static BaseDAO dao = null;

    /**
     * ��ȡ����
     *
     * @return ���ݿ��������
     */
    public static MMGPLinkQueryDAO getInstance() {
        if (linkdao == null) {
            linkdao = new MMGPLinkQueryDAO();
        }
        return linkdao;
    }

    /**
     * ��ȡ���ݿ����dAO
     *
     * @return ���ݿ����dAO
     */
    private static BaseDAO getBaseDAO() {
        if (dao == null) {
            dao = new BaseDAO();
        }
        return dao;
    }

    /**
     * ���ݵ������ͺ͵��ݱ���id����ѯ���ݱ���VO
     *
     * @param billtype
     *        ��������
     * @param billbids
     *        ���ݱ���id
     * @return ���ݱ���VO key-> ���ݱ������� value-> ���ݱ���VO
     */
    public Map<String, SuperVO> queryVOByPks(String billtype,
                                             String[] billbids,
                                             String[] selectFields) throws BusinessException {

        // ��ȡ�ӱ�class
        String realDestBillType = PfUtilBaseTools.getRealBilltype(billtype);
        Class< ? extends SuperVO> destClass = MMGPDefaultBillFlow.getInstance(realDestBillType).getSubTableClass();
        if (destClass == null) {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0038")/*@res "Ŀ�ĵ�������"*/ + billtype + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0039")/*@res " ��Ӧ�ĵ���Ԫ����û���ڵ���������Ϣ��ȡ��д�ӿ����涨����Դ����ӳ����Ϣ"*/);
        }

        // ����where����
        String subTablePk = MMGPDefaultBillFlow.getInstance(realDestBillType).getSubTablePrimarykey();
        String pkCondition = buildWhereCond(billbids, subTablePk);

        // ��ѯ
        Collection<SuperVO> billbodyVOCollctn =
                this.getBaseDAO().retrieveByClause(destClass, pkCondition, selectFields);

        // ���췵�ؽ��
        if (MMCollectionUtil.isEmpty(billbodyVOCollctn)) {
            return Collections.emptyMap();
        }
        Map<String, SuperVO> billBodyMap = buildBillBodyMap(billbodyVOCollctn, realDestBillType, selectFields);

        return billBodyMap;
    }

    /**
     * �����깤�������id��ѯ��Ӧ����������
     *
     * @param wrqualityIds
     *        �깤�������id����
     * @return key-> �깤�������pk value-> ��������
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
     * �������ݱ���MAP
     *
     * @param billbodyVOCollctn
     *        ���ݱ�����Ϣ
     * @param realDestBillType
     * @return key-> ���ݱ������� value-> ���ݱ���
     */
    private Map<String, SuperVO> buildBillBodyMap(Collection<SuperVO> billbodyVOCollctn,
                                                  String realDestBillType, String[] selectFields) {

        Map<String, SuperVO> billBodyMap = new HashMap<String, SuperVO>();

        Iterator<SuperVO> iterator = billbodyVOCollctn.iterator();
        while (iterator.hasNext()) {
            SuperVO billbodyVO = iterator.next();

            //���õ�������
            billbodyVO.setAttributeValue(MMGlobalConst.VBILLTYPECODE, realDestBillType);

            if (MMArrayUtil.isEmpty(selectFields)) {
                billBodyMap.put(billbodyVO.getPrimaryKey(), billbodyVO);
                continue;
            }

            //���൥�ݱ���id
            billbodyVO.setAttributeValue(MMGlobalConst.BILLBID, billbodyVO.getAttributeValue(selectFields[0]));
            //������Դ��������
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCTYPE, billbodyVO.getAttributeValue(selectFields[1]));
            //������Դ����bid
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
     * ������ѯ����
     *
     * @param billbids
     *        ���ݱ���idֵ
     * @param subTablePk
     *        ���ݱ���id����
     * @return ��ѯ����
     */
    private String buildWhereCond(String[] billbids,
                                  String subTablePk) {
        IDQueryBuilder billbidBuilder = new IDQueryBuilder();
        String pkCondition = billbidBuilder.buildSQL(subTablePk, billbids);
        // modify by liwsh 2014-11-19 �������Ƿ�ɾ������Ϊ��������п��ܻ�����ɾ���ĵ���
        // pkCondition = pkCondition + " and dr = 0 ";
        return pkCondition;
    }

    /**
     * ���ݵ������ͺ͵��ݱ���id�������еı����в�ѯ���ݱ���VO
     *
     * @param billtype
     *        ��������
     * @param billbids
     *        ���ݱ���id
     * @return key-> ���ݱ���id value-> ���ݱ���vo
     * @throws BusinessException
     */
    public Map<String, SuperVO> queryVOFromAllChild(String billtype,
                                                    String[] billbids) throws BusinessException {

        // ��ȡ�ӱ�class
        String realDestBillType = PfUtilBaseTools.getRealBilltype(billtype);
        List<IBean> subBeanList = MMGPDefaultBillFlow.getInstance(realDestBillType).getAllSubBean();

        if (MMCollectionUtil.isEmpty(subBeanList)) {
            return Collections.emptyMap();
        }

        // �������е��ӱ�
        for (IBean subBean : subBeanList) {
            String primaryKey = subBean.getPrimaryKey().getPKColumn().getName();

            String subclassname = subBean.getFullClassName();
            Class< ? extends SuperVO> subclass = MMGPLinkQueryUtil.getClass(subclassname);

            String whereCond = this.buildWhereCond(billbids, primaryKey);

            // ��ѯ
            Collection<SuperVO> billbodyVOCollctn = this.getBaseDAO().retrieveByClause(subclass, whereCond);

            if (MMCollectionUtil.isEmpty(billbodyVOCollctn)) {
                continue;
            }

            // ���췵�ؽ��
            return this.buildBillBodyMap(billbodyVOCollctn, realDestBillType, null);

        }
        return Collections.emptyMap();
    }

}