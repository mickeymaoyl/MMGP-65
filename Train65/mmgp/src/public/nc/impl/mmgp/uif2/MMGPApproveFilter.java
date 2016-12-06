package nc.impl.mmgp.uif2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.mmgp.util.grand.MMGPGrandBillQuery;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Aug 1, 2013
 * @author wangweir
 */
public class MMGPApproveFilter {
    /**
     * ��ѯʱ����������
     * 
     * @param <T>
     * @param queryScheme
     *        ��ѯ����
     * @param vos
     *        ��Ҫ���˵ľۺ�VO����
     * @param billType
     *        ��������
     * @return ���˺�ľۺ�VO����
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    public static <T extends AbstractBill> T[] filterForApprove(IQueryScheme queryScheme,
                                                                T[] vos,
                                                                String billType) {
        if (ArrayUtils.isEmpty(vos)) {
            return null;
        }
        QuerySchemeProcessor qrySchemeProcessor = new QuerySchemeProcessor(queryScheme);
        QueryCondition condition = qrySchemeProcessor.getQueryCondition(MMGlobalConst.BISAPPROVING);
        if (null == condition) {
            return vos;
        }
        Object[] values = condition.getValues();
        if (!UFBoolean.valueOf(values[0].toString()).booleanValue()) {
            return vos;
        }
        String userId = AppContext.getInstance().getPkUser();
        String trantypeName = MMMetaUtils.getIFlowBizItfMapKey(vos[0], IFlowBizItf.ATTRIBUTE_TRANSTYPE);

        /* Dec 6, 2013 wangweir ʹ��ר�ýӿڣ����Ч������ Begin */
        // T[] filterResults =
        // PfServiceUtil.filterForApprove(vos, billType, userId, null == trantypeName ? "null" : trantypeName);
        T[] filterResults = filterForApprove(vos, billType, userId, null == trantypeName ? "null" : trantypeName);
        /* Dec 6, 2013 wangweir End */

        if (MMArrayUtil.isEmpty(filterResults)) {
            return filterResults;
        }

        List refreshVos = Arrays.asList(filterResults);
        // �п��ܵ�1�������ص������Ѿ����˵������Ҫ����ˢ�µ�1�����ݱ���
        refreshFirstVO(refreshVos, filterResults[0].getClass());

        return (T[]) new ListToArrayTool(filterResults[0].getClass()).convertToArray(refreshVos);
    }

    /**
     * �Ӹ�����ҵ��VO�����й��˳������û��Ĵ�������ҵ��VO
     * 
     * @param vos
     *        ���д����˵�ҵ��ۺ�VO
     * @param billType
     *        ��������
     * @param userId
     *        �û�ID
     * @param tstypeattributename
     *        �������ͱ����ֶ�����
     * @return ���д�������ҵ��VO
     */
    public static <T extends AbstractBill> T[] filterForApprove(T[] vos,
                                                                String billType,
                                                                String userId,
                                                                String tstypeattributename) {

        // ���ս������ͷ���
        MapList<Object, T> index = new MapList<Object, T>();

        // ��������Ϊ�յļ���
        List<T> voList = new ArrayList<T>();
        // ���ս������ͷ���
        for (T vo : vos) {
            Object value = vo.getParentVO().getAttributeValue(tstypeattributename);
            if (value != null) {
                index.put(value, vo);
            } else {// ��������Ϊ��
                voList.add(vo);
            }
        }
        return filterForApprove0(billType, userId, index, voList);
    }

    /**
     * �Ӱ��ս������ͷ����ļ����й��˳������û��Ĵ�������ҵ��VO
     * 
     * @param billType
     *        ��������
     * @param userId
     *        �û�ID
     * @param index
     *        ���ս������ͷ�����Map<K, List<V>>
     * @param voList
     *        ��������Ϊ�ռ���
     * @return ���д�������ҵ��VO
     * @author ��־��
     * @since 2011-4-02
     */
    private static <T> T[] filterForApprove0(String billType,
                                             String userId,
                                             MapList<Object, T> index,
                                             List<T> voList) {
        // ���˺�ļ���
        List<T> filterList = new ArrayList<T>();
        ListToArrayTool<T> tool = new ListToArrayTool<T>();
        for (Object key : index.keySet()) {
            List<T> list = index.get(key);
            T[] filterVos = filterForApprove0(tool.convertToArray(list), key.toString(), userId);
            for (T filterVo : filterVos) {
                filterList.add(filterVo);
            }
        }
        // ���ڽ�������Ϊ�յ�vo�����뵥�����ͽ��й���
        if (voList.size() > 0) {
            T[] filters = filterForApprove0(tool.convertToArray(voList), billType, userId);
            for (T vo : filters) {
                filterList.add(vo);
            }
        }
        if (filterList.size() == 0) {
            return null;
        }
        return tool.convertToArray(filterList);
    }

    /**
     * �Ӹ�����ҵ��VO�����й��˳������û��Ĵ�������ҵ��VO
     * 
     * @param vos
     *        ���д����˵�ҵ��VO
     * @param billType
     *        ��������
     * @param userId
     *        �û�ID
     * @return ���д�������ҵ��VO
     * @author ��ǿ��
     * @throws BusinessException
     * @since 2008-8-15
     */
    @SuppressWarnings({"unchecked" })
    private static <T> T[] filterForApprove0(T[] vos,
                                             String billType,
                                             String userId) {
        Map<String, T> vosMap = new HashMap<String, T>();
        for (T vo : vos) {
            IFlowBizItf flowbizitf = PfMetadataTools.getBizItfImpl(vo, IFlowBizItf.class);
            if (flowbizitf.getApproveStatus() != null) {
                if (flowbizitf.getApproveStatus().intValue() == IPfRetCheckInfo.PASSING) {
                    // ����Ѿ�����
                    continue;
                }
            }
            try {
                vosMap.put(getPrimaryKey(vo), vo);
            } catch (BusinessException e) {
                ExceptionUtils.wrappException(e);
            }
        }

        if (MMMapUtil.isEmpty(vosMap)) {
            return vos;
        }

        String[] filterIds = null;
        try {
            String[] pks = vosMap.keySet().toArray(new String[0]);
            filterIds =
                    NCLocator
                        .getInstance()
                        .lookup(IPFWorkflowQry.class).isCheckmanAry(pks, billType, userId);
//                        .isCheckmanAryIgnoreInstance(pks, billType, userId);
            // filterIds = isCheckmanAryIgnoreInstance(vosMap.values(), billType, userId);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        if (null == filterIds) {
            filterIds = new String[0];
        }
        List<T> filterVos = new ArrayList<T>();
        for (String id : filterIds) {
            filterVos.add(vosMap.get(id));
        }
        T[] rs = (T[]) Array.newInstance(vos.getClass().getComponentType(), filterVos.size());
        return filterVos.toArray(rs);
    }

    /**
     * ��ȡ���������
     * 
     * @param obj
     *        Ҫ��ȡ�����Ķ���
     * @return ���������
     * @throws BusinessException
     * @author ��ǿ��
     * @since 2008-9-2
     */
    private static String getPrimaryKey(Object obj) throws BusinessException {
        if (obj instanceof ValueObject) {
            return ((ValueObject) obj).getPrimaryKey();
        } else if (obj instanceof AggregatedValueObject) {
            return ((AggregatedValueObject) obj).getParentVO().getPrimaryKey();
        } else {
            throw new IllegalArgumentException("unsupported type : " + obj.getClass());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked" })
    protected static void refreshFirstVO(List vos,
                                         Class< ? extends AbstractBill> clazz) {
        if (CollectionUtils.isEmpty(vos)
            || !ArrayUtils.isEmpty(((AbstractBill) vos.get(0)).getChildrenVO())
            && null != ((AbstractBill) vos.get(0)).getChildrenVO()[0]) {
            return;
        }
        String[] refreshPKs = new String[]{((AbstractBill) vos.get(0)).getPrimaryKey() };
        MMGPGrandBillQuery query = new MMGPGrandBillQuery(clazz);
        AbstractBill[] refreshBills = (AbstractBill[]) query.query(refreshPKs);
        // �����ʱ����������ԭ���ĵ�һ�����ݱ�ɾ������ݹ鴦��
        if (ArrayUtils.isEmpty(refreshBills)) {
            vos.remove(0);
            refreshFirstVO(vos, clazz);
        }
        vos.set(0, refreshBills[0]);
    }
}
