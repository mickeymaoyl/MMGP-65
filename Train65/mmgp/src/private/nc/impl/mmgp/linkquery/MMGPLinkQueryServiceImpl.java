package nc.impl.mmgp.linkquery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.logging.Logger;
import nc.bs.mmgp.bill.billsource.MMGPDefaultBillFlow;
import nc.bs.mmgp.linkquery.forward.MMGPLinkQueryForwardBP;
import nc.itf.mmgp.linkquery.IMMGPLinkQueryService;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.linkquery.LinkQueryBillNode;
import nc.vo.mmgp.linkquery.LinkQueryParamVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMGPMapList;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 *
 * @since: ��������:2014-10-29
 * @author:liwsh
 */
public class MMGPLinkQueryServiceImpl implements IMMGPLinkQueryService {

    /**
     * Ŀ�ĵ�������
     */
    private String destBillType;

    /**
     * ��Դ���ݱ���ID�ֶ�������Ϣ��key->�������� value->��Դ���ݱ���id����<br\>
     * ���ݻ��ݹ��������������е��ݣ����������ã�<br\>
     * 1. �޶����ݷ�Χ��������ν�����������ݹ�����������������map�еĵ��ݣ�����ֹ����<br\>
     * 2. ���嵥�ݱ�����Դ����bid��keyֵ����Ϊ�޷�ͨ�������κΰ취��ȡ<br\>
     */
    private Map<String, String> vsrcbidkeyMap;

    /**
     * ���ݵ������ͺ͵��ݱ���id�ȣ���ѯ��Ӧ����Դ���ݱ��壨Ŀ�ĵ�������ָ����һ�����ͱ��壩<br\>
     * ���磬�ɹ���ⵥ->�ɹ�������->�ɹ�����->�빺��->��ҵ�� ������Ҫ���ݲɹ���ⵥ�ı���id���ҵ����ж�Ӧ����ҵ������Ϣ<br\>
     * ���ǾͿ��Դ����ɲɹ���ⵥ����id�Ͳɹ���ⵥ�������͹����paramvos����ҵ�ĵ���������Ϊ��destBillType<br\>
     * �������Ҫ���Ӧ�Ĳɹ���������һ�У�destBillType���ɲɹ������ĵ������ͼ��ɡ�
     */
    @Override
    public Map<String, SuperVO> querySrcBodyVO(LinkQueryParamVO[] paramvos,
                                               String destBillType,
                                               Map<String, String> vsrcbidkeyMap) throws BusinessException {

        if (MMArrayUtil.isEmpty(paramvos) || MMStringUtil.isEmpty(destBillType)) {
            return Collections.emptyMap();
        }

        // ��ʼ������
        initData(destBillType, vsrcbidkeyMap);

        // ���������ͷ���
        MMGPMapList<String, LinkQueryParamVO> billBodyMap = splitByBilltype(paramvos);

        // ���������ĵ��ݱ���
        Map<String, SuperVO> srcBillBodyMap = new HashMap<String, SuperVO>();

        Set<Entry<String, List<LinkQueryParamVO>>> billBodySet = billBodyMap.entrySet();
        Iterator<Entry<String, List<LinkQueryParamVO>>> iterator = billBodySet.iterator();
        while (iterator.hasNext()) {
            Entry<String, List<LinkQueryParamVO>> entry = iterator.next();
            Map<String, SuperVO> destBillBodyMap = querySrcBodyVO(entry.getKey(), entry.getValue());
            srcBillBodyMap.putAll(destBillBodyMap);
        }
        return srcBillBodyMap;
    }

    /**
     * ��ʼ������
     *
     * @param destBillType
     *        Ŀ�ĵ�������
     * @param vsrcbidkeyMap
     *        ��Դ���ݱ���ID�ֶ� key��Ϣ
     */
    private void initData(String destBillType,
                          Map<String, String> vsrcbidkeyMap) {
        this.destBillType = destBillType;
        this.vsrcbidkeyMap = vsrcbidkeyMap;

    }

    /**
     * ��ȡĳһ�ֵ������͵ĵ��ݱ������ݶ�Ӧ����Դ���ݱ��壨������һ����Դ���ݱ��壬��Ŀ�ĵ�������ָ����
     *
     * @param billtype
     *        ��������
     * @param billbodyList
     *        ���ݱ���VO����
     * @return key-> ���ݱ���id�� value->Ŀ�ĵ������Ͷ�Ӧ����Դ���ݱ���VO
     * @throws BusinessException
     */
    private Map<String, SuperVO> querySrcBodyVO(String billtype,
                                                List<LinkQueryParamVO> billbodyList) throws BusinessException {

        // ���ݵ��ݱ���Դͷ���������Ƿ�ΪĿ�ĵ������ͷ�Ϊ2��
        List<LinkQueryParamVO> firstIsDestBodyList = new ArrayList<LinkQueryParamVO>();
        List<LinkQueryParamVO> normalBodyList = new ArrayList<LinkQueryParamVO>();

        for (LinkQueryParamVO bodyvo : billbodyList) {
            if (destBillType.equalsIgnoreCase(bodyvo.getVfirsttype())) {
                firstIsDestBodyList.add(bodyvo);
            } else {
                normalBodyList.add(bodyvo);
            }
        }

        Map<String, SuperVO> destBodyMap = new HashMap<String, SuperVO>();

        // ��Դͷ�������;���Ŀ�ĵ�������ʱ����ѯĿ�ĵ������Ͷ�Ӧ�ĵ��ݱ��壨ֱ�Ӹ���Դͷ����id��ѯ��Ҫ���ص����ݣ�
        if (MMCollectionUtil.isNotEmpty(firstIsDestBodyList)) {
            destBodyMap.putAll(querySrcBodyForSpecial(firstIsDestBodyList));
        }

        // ��Դͷ�������Ͳ���Ŀ�ĵ�������ʱ����ѯĿ�ĵ������Ͷ�Ӧ�ĵ��ݱ���(��Ҫ������Դ����һ��һ�����ϻ��ݣ�ֱ���ҵ�Ŀ�ĵ������Ͷ�Ӧ�ĵ��ݱ���)
        if (MMCollectionUtil.isNotEmpty(normalBodyList)) {
            destBodyMap.putAll(querySrcBodyForNormal(billtype, normalBodyList));
        }

        return destBodyMap;
    }

    /**
     * ��Դͷ�������;���Ŀ�ĵ�������ʱ����ѯĿ�ĵ������Ͷ�Ӧ�ĵ��ݱ���
     *
     * @param billtype
     *        ��������
     * @param firstIsDestBodyList
     *        ���ݱ���VO����
     * @return key-> ���ݱ���id�� value->Ŀ�ĵ������Ͷ�Ӧ����Դ���ݱ���VO
     */
    private Map<String, SuperVO> querySrcBodyForSpecial(List<LinkQueryParamVO> firstIsDestBodyList)
            throws BusinessException {

        // Դͷ����id����
        Set<String> firstbidSet = new HashSet<String>();
        for (LinkQueryParamVO billbodyVO : firstIsDestBodyList) {
            String firstbid = billbodyVO.getVfirstbid();
            if (MMStringUtil.isNotEmpty(firstbid)) {
                firstbidSet.add(firstbid);
            }
        }

        if (MMCollectionUtil.isEmpty(firstbidSet)) {
            return Collections.emptyMap();
        }

        // ��ѯԴͷ���ݱ���
        Map<String, SuperVO> billbodyMap =
                MMGPLinkQueryDAO.getInstance().queryVOByPks(destBillType, firstbidSet.toArray(new String[0]), null);

        if (MMMapUtil.isEmpty(billbodyMap)) {
            billbodyMap =
                    MMGPLinkQueryDAO.getInstance().queryVOFromAllChild(
                        destBillType,
                        firstbidSet.toArray(new String[0]));
        }

        // ���ݱ���id��Դͷ����idӳ��
        Map<String, String> bidToFstbidMap = new HashMap<String, String>();
        for (LinkQueryParamVO billbodyVO : firstIsDestBodyList) {
            String firstbid = billbodyVO.getVfirstbid();
            String billbid = billbodyVO.getBillbid();
            bidToFstbidMap.put(billbid, firstbid);
        }

        // ���ؽ��
        Map<String, SuperVO> bidToDestBillbodyMap = new HashMap<String, SuperVO>();

        Set<Entry<String, String>> bidSet = bidToFstbidMap.entrySet();
        Iterator<Entry<String, String>> bidIter = bidSet.iterator();
        while (bidIter.hasNext()) {
            Entry<String, String> entry = bidIter.next();
            String destbid = entry.getValue();
            bidToDestBillbodyMap.put(entry.getKey(), billbodyMap.get(destbid));
        }

        return bidToDestBillbodyMap;
    }

    /**
     * ��Դͷ�������Ͳ���Ŀ�ĵ�������ʱ����ѯĿ�ĵ������Ͷ�Ӧ�ĵ��ݱ���(��Ҫ������Դ����һ��һ�����ϻ��ݣ�ֱ���ҵ�Ŀ�ĵ������Ͷ�Ӧ�ĵ��ݱ���)
     *
     * @param billtype
     *        ��������
     * @param normalBodyList
     *        ���ݱ���VO����
     * @return key-> ���ݱ���id�� value->Ŀ�ĵ������Ͷ�Ӧ����Դ���ݱ���VO
     * @throws BusinessException
     */
    private Map<String, SuperVO> querySrcBodyForNormal(String billtype,
                                                       List<LinkQueryParamVO> normalBodyList)
            throws BusinessException {

        String realBillType = PfUtilBaseTools.getRealBilltype(billtype);
        Class< ? extends SuperVO> subTableClass = MMGPDefaultBillFlow.getInstance(realBillType).getSubTableClass();

        if (subTableClass == null) {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0038")/*@res "Ŀ�ĵ�������"*/ + billtype + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0039")/*@res " ��Ӧ�ĵ���Ԫ����û���ڵ���������Ϣ��ȡ��д�ӿ����涨����Դ����ӳ����Ϣ"*/);
        }

        /**
         * ���ݱ���VO ID�뵥�ݱ���VOӳ��<br\>
         * value��ֵ��䣬ÿһ�ζ������Դ���ݶ�Ӧ�ı��壬ֱ����Դ���ݱ��Ŀ�ĵ�������Ϊֹ��<br\>
         * ����ֹ��ʱ�򣬸�map������Ҫ���ص�ֵ
         */
        Map<String, SuperVO> bidTOBodyVOMap = buildBodyVOMap(normalBodyList, subTableClass);

        this.querySourceBodyVO(bidTOBodyVOMap);

        return bidTOBodyVOMap;
    }

    /**
     * ��������VO MAP
     *
     * @param normalBodyList
     * @param subTableClass
     * @return
     */
    private Map<String, SuperVO> buildBodyVOMap(List<LinkQueryParamVO> normalBodyList,
                                                Class< ? extends SuperVO> subTableClass) {
        Map<String, SuperVO> bidTOBodyVOMap = new HashMap<String, SuperVO>();
        for (LinkQueryParamVO linkqueryVO : normalBodyList) {
            SuperVO billbodyVO = MMGPLinkQueryUtil.newInstanceVO(subTableClass);

            // ��������
            billbodyVO.setAttributeValue(MMGlobalConst.VBILLTYPECODE, linkqueryVO.getBilltype());
            // �����ӱ�id
            billbodyVO.setAttributeValue(MMGlobalConst.BILLBID, linkqueryVO.getBillbid());
            // ��Դ��������
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCTYPE, linkqueryVO.getVsrctype());
            // ��Դ���ݱ���id
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCBID, linkqueryVO.getVsrcbid());

            // Դͷ��������
            billbodyVO.setAttributeValue(MMGlobalConst.VFIRSTTYPE, linkqueryVO.getVfirsttype());
            // Դͷ���ݱ���id
            billbodyVO.setAttributeValue(MMGlobalConst.VFIRSTBID, linkqueryVO.getVfirstbid());

            bidTOBodyVOMap.put(linkqueryVO.getBillbid(), billbodyVO);
        }
        return bidTOBodyVOMap;
    }

    /**
     * Ѱ����Դ���ݱ���VO
     *
     * @param billBodyMap
     *        key-> ������㵥�ݱ���id value->��ǰ���ݱ���vo
     * @param destBilltype
     *        Ŀ�ĵ�������
     * @throws BusinessException
     */
    private void querySourceBodyVO(Map<String, SuperVO> billBodyMap) throws BusinessException {

        // ���˳���ǰ�������Ͳ���Ŀ�ĵ������͵����ݣ�����ԭmap���Ƴ�
        Map<String, SuperVO> filterMap = filterBillBody(billBodyMap);

        if (MMMapUtil.isEmpty(filterMap)) {
            return;
        }

        // ������Դ�������ͷ���
        Map<String, Map<String, SuperVO>> srcType2BillbodyMap = this.splitByVsrctype(filterMap);

        // ��Դ��������Ϊ�գ�˵����ǰ������Դͷ���ݣ���������Ŀ�ĵ��ݣ��Ƴ���
        if (MMMapUtil.isEmpty(srcType2BillbodyMap)) {
            MMGPLinkQueryUtil.removeDataFromMap(billBodyMap, filterMap.keySet().toArray(new String[0]));
            return;
        }

        // �����ѯ��Դ���ݣ�����map�еĵ�ǰ�����滻Ϊ��Դ����
        Map<String, SuperVO> replacedMap = divideQuerySrcBody(srcType2BillbodyMap);
        billBodyMap.putAll(replacedMap);

        // �ݹ����
        querySourceBodyVO(billBodyMap);
    }

    /**
     * �����ѯ��Դ���ݣ�����map�еĵ�ǰ�����滻Ϊ��Դ����
     *
     * @param srcType2BillbodyMap
     *        key-> ��Դ�������� value->���ݱ�����Ϣ
     * @return
     * @throws BusinessException
     *         ҵ���쳣
     */
    private Map<String, SuperVO> divideQuerySrcBody(Map<String, Map<String, SuperVO>> srcType2BillbodyMap)
            throws BusinessException {

        Set<Entry<String, Map<String, SuperVO>>> set = srcType2BillbodyMap.entrySet();
        Iterator<Entry<String, Map<String, SuperVO>>> iterator = set.iterator();
        while (iterator.hasNext()) {

            Entry<String, Map<String, SuperVO>> entry = iterator.next();

            // �����Դ�������Ͳ��������Լ��Ľڵ㣬���Ҳ��ٴ���MAP�У��Ͳ��ټ��������� gaotx 2015-4-9
            String vsrcbilltype = entry.getKey();
            if (!vsrcbidkeyMap.keySet().contains(vsrcbilltype) && !vsrcbilltype.startsWith("YG")) {
                iterator.remove();
                continue;
            }
            // ��ѯ�ֶ�
            String[] selectfields = this.buildSelectFields(vsrcbilltype);

            // �����Դ����Ϊ�գ��򲻽���ѰԴ
            if (selectfields != null && MMStringUtil.isEmpty(selectfields[3])) {
                iterator.remove();
                continue;
            }

            // �����Դ���������Ǳ��ϼƻ�������������ѰԴ����Ϊ�Ѿ�û������
            if (selectfields != null && MMGlobalConst.BILL_TYPE_PICKM.equalsIgnoreCase(vsrcbilltype)) {
                iterator.remove();
                continue;
            }

            // ��Դ���ݱ���id
            Map<String, SuperVO> currentBodyMap = entry.getValue();
            Set<String> vsrcbidSet = getVsrcbidSet(currentBodyMap);

            // ��ѯ��Դ����
            Map<String, SuperVO> srcbodyMap =
                    MMGPLinkQueryDAO.getInstance().queryVOByPks(
                        vsrcbilltype,
                        vsrcbidSet.toArray(new String[0]),
                        selectfields);

            if (MMMapUtil.isEmpty(srcbodyMap)) {

                /**
                 * ��Ϊ�깤�����Ƴ�����ⵥ����ⵥ��Դ���ݱ���id��¼�����깤��������<br\>
                 * �����깤�����ڵ���������Ϣ��ȡ��д�ӿ����涨����Դ����ӳ��Ϊ�깤�����ӱ��޷�������Դ��Ϣ���ҵ��깤���档<br\>
                 * ����ֱ�Ӹ���Դͷ���ݱ���id��Դͷ�������ͣ�������������ȡ����Ϊ����Ʒ��ⵥ�ϵ�Դͷ���ܻ�������⣬��������������£����ң�����̿��ܻ�©�����ݣ�Ŀǰû�취��<br\>
                 * ����Ʒ��ⵥ�ϵ�Դͷ��Ϣ����ĳ������£�<br\>
                 * 1. �߹����깤���������������棬�ϸ�Ʒ����ʱ�򣬱���Ĳ���Ʒ��ⵥԴͷid��Դͷ����id����¼���ǹ����깤����ı�ͷid��<br\>
                 * 2. ���ӵ����յ�ʱ���Ƴ��Ĳ���Ʒ��ⵥ��Դͷ��Ϣ��Ϊ��<br\>
                 */
                if (MMGlobalConst.BILL_TYPE_WR.equalsIgnoreCase(vsrcbilltype)) {
                    boolean isFind = querySrcForStockInBill(vsrcbilltype, currentBodyMap);
                    if (!isFind) {
                        iterator.remove();
                        continue;
                    }
                } else {
                    // ��ֹ��֤��ʱ�򱨴�ѰԴ����ֱ����һ��
                    Logger.error("�޷��ҵ���������Ϊ��" + vsrcbilltype + "��Ӧ����Դ���ݱ��塣");
                    iterator.remove();
                    continue;
                    // ExceptionUtils.wrappBusinessException("�޷��ҵ���������Ϊ��" + vsrcbilltype + "��Ӧ����Դ���ݱ��塣");
                }
            } else {
                // ����Դ���ݱ��� �滻 ��ǰ���ݱ���
                replaceBodyBySrcBody(currentBodyMap, srcbodyMap);
            }

        }

        Map<String, SuperVO> replacedMap = new HashMap<String, SuperVO>();
        Set<Entry<String, Map<String, SuperVO>>> set2 = srcType2BillbodyMap.entrySet();
        Iterator<Entry<String, Map<String, SuperVO>>> iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Entry<String, Map<String, SuperVO>> entry2 = iterator2.next();
            replacedMap.putAll(entry2.getValue());
        }

        return replacedMap;
    }

    /**
     * Ϊ����Ʒ��ⵥѰԴ��ʱ�����⴦����Դͷ��ʼ���ҡ�
     *
     * @param vsrcbilltype
     *        ��Դ��������
     * @param currentBodyMap
     * @throws BusinessException
     */
    private boolean querySrcForStockInBill(String vsrcbilltype,
                                           Map<String, SuperVO> currentBodyMap) throws BusinessException {

        // �������������ȡ����������Ϣ��Ϊ��Դ��Ϣ��
        // 1. ��Դ���ݱ���idΪ�����������pk
        Set<String> vsrcbidSet = getVsrcbidSet(currentBodyMap);
        if (MMCollectionUtil.isEmpty(vsrcbidSet)) {
            return false;
        }

        Map<String, SuperVO> dmoMap =
                MMGPLinkQueryDAO.getInstance().queryDmoForStockin(vsrcbidSet.toArray(new String[0]));

        if (MMMapUtil.isEmpty(dmoMap)) {
            return false;
        }

        Map<String, SuperVO> resultMap = new HashMap<String, SuperVO>();

        Set<Entry<String, SuperVO>> entrySet = currentBodyMap.entrySet();
        Iterator<Entry<String, SuperVO>> entryIter = entrySet.iterator();
        while (entryIter.hasNext()) {
            Entry<String, SuperVO> entry = entryIter.next();
            String srcbid = (String) entry.getValue().getAttributeValue(MMGlobalConst.VSRCBID);
            SuperVO dmovo = dmoMap.get(srcbid);
            if (dmovo != null) {
                resultMap.put(entry.getKey(), dmovo);
            }
        }

        Set<Entry<String, SuperVO>> crrntEntrySet = currentBodyMap.entrySet();
        Iterator<Entry<String, SuperVO>> crrntentryIter = crrntEntrySet.iterator();
        while (crrntentryIter.hasNext()) {

            String key = crrntentryIter.next().getKey();
            if (resultMap.containsKey(key)) {
                currentBodyMap.put(key, resultMap.get(key));
            } else {
                crrntentryIter.remove();
            }
        }

        // String vfirstbilltype = getVfirstbilltype(currentBodyMap);
        // Set<String> vfirstbidSet = getVfirstbidSet(currentBodyMap);
        //
        // String[] selectfields = this.buildSelectFields(vfirstbilltype);
        //
        // Map<String, SuperVO> firstBodyMap =
        // MMGPLinkQueryDAO.getInstance().queryVOByPks(
        // vfirstbilltype,
        // vfirstbidSet.toArray(new String[0]),
        // selectfields);
        //
        // if (MMMapUtil.isEmpty(firstBodyMap)) {
        // Logger.error("�޷��ҵ���������Ϊ��" + vsrcbilltype + "��Ӧ����Դ���ݱ��塣");
        // return false;
        // // ExceptionUtils.wrappBusinessException("�޷��ҵ���������Ϊ��" + vsrcbilltype + "��Ӧ����Դ���ݱ��塣");
        // }
        //
        // replaceBodyByFirstBody(currentBodyMap, firstBodyMap, vfirstbilltype);
        return true;
    }

    /**
     * ����Դ���ݱ����滻��ǰ���ݱ���
     *
     * @param currentBodyMap
     *        ��ǰ���ݱ�����Ϣ
     * @param srcbodyMap
     *        ��Դ���ݱ�����Ϣ
     */
    private void replaceBodyBySrcBody(Map<String, SuperVO> currentBodyMap,
                                      Map<String, SuperVO> srcbodyMap) {

        // ����bid����Դ����bidӳ��
        Map<String, String> bid2SrcBidMap = getBid2SrcBidMap(currentBodyMap);

        // ����ǰ�����滻Ϊ��Դ����
        Set<Entry<String, String>> bid2SrcbidSet = bid2SrcBidMap.entrySet();
        Iterator<Entry<String, String>> bid2SrcbidIter = bid2SrcbidSet.iterator();

        while (bid2SrcbidIter.hasNext()) {

            // ��Դ���ݱ���
            Entry<String, String> bid2SrcbidEntry = bid2SrcbidIter.next();
            String srcbid = bid2SrcbidEntry.getValue();
            SuperVO srcbodyVO = srcbodyMap.get(srcbid);

            // �滻����
            currentBodyMap.put(bid2SrcbidEntry.getKey(), srcbodyVO);
        }
    }

    /**
     * ��������bid����Դ����bidӳ��
     *
     * @param currentBodyMap
     *        ��ǰ������Ϣ
     * @return key-> ��ԭʼ���ݱ���id value-> ��ǰ���ݱ�����Դid
     */
    private Map<String, String> getBid2SrcBidMap(Map<String, SuperVO> currentBodyMap) {

        Map<String, String> bid2SrcBidMap = new HashMap<String, String>();

        Set<Entry<String, SuperVO>> currentBodySet = currentBodyMap.entrySet();
        Iterator<Entry<String, SuperVO>> crrntBodyIter = currentBodySet.iterator();
        while (crrntBodyIter.hasNext()) {
            Entry<String, SuperVO> crrntEntry = crrntBodyIter.next();

            String billbid = crrntEntry.getKey();
            SuperVO crrntBodyVO = crrntEntry.getValue();

            bid2SrcBidMap.put(billbid, (String) crrntBodyVO.getAttributeValue(MMGlobalConst.VSRCBID));
        }
        return bid2SrcBidMap;
    }

    /**
     * ������ѯ�ֶ�
     *
     * @param srcbilltype
     *        ��Դ��������
     * @return ��ѯ�ֶ�
     */
    private String[] buildSelectFields(String srcbilltype) {

        String realsrcType = PfUtilBaseTools.getRealBilltype(srcbilltype);

        if (destBillType.equalsIgnoreCase(realsrcType)) {
            return null;
        }

        String billbidKey = MMGPDefaultBillFlow.getInstance(realsrcType).getSubTablePrimarykey();
        String vsrctypekey = MMGPDefaultBillFlow.getInstance(realsrcType).getSourceTypeField();
        String vsrcbidkey = this.getVsrcbidKey(realsrcType);
        String vsrcidKey = MMGPDefaultBillFlow.getInstance(realsrcType).getSourceIDField();

        String[] selectfields = new String[]{billbidKey, vsrctypekey, vsrcbidkey, vsrcidKey };

        return selectfields;
    }

    /**
     * ��ȡ��Դ���ݱ���id����
     *
     * @param currentBodyMap
     *        ���ݱ�����Ϣ
     * @return ��Դ���ݱ���id
     */
    private Set<String> getVsrcbidSet(Map<String, SuperVO> currentBodyMap) {

        Set<String> vsrcbidSet = new HashSet<String>();

        Set<Entry<String, SuperVO>> currentBodySet = currentBodyMap.entrySet();
        Iterator<Entry<String, SuperVO>> crrntBodyIter = currentBodySet.iterator();

        while (crrntBodyIter.hasNext()) {
            Entry<String, SuperVO> crrntEntry = crrntBodyIter.next();
            SuperVO crrntBodyVO = crrntEntry.getValue();
            vsrcbidSet.add((String) crrntBodyVO.getAttributeValue(MMGlobalConst.VSRCBID));
        }

        return vsrcbidSet;
    }

    /**
     * ���˳���ǰ�������Ͳ���Ŀ�ĵ������͵ĵ��ݱ��壬����ԭmap���Ƴ�
     *
     * @param billBodyMap
     *        ���ݱ�����Ϣ��key->�ʼ�ĵ��ݱ���id value->��Ӧ�ĵ�ǰ���ݱ���id���п�����key��Ӧ�ĵ��ݱ������Դ��Ҳ�п�������Դ����Դ�ȵȣ�
     * @return ��ǰ���ݲ���Ŀ�ĵ������͵ĵ��ݱ���
     */
    private Map<String, SuperVO> filterBillBody(Map<String, SuperVO> billBodyMap) {

        Map<String, SuperVO> filterMap = new HashMap<String, SuperVO>();

        Set<Entry<String, SuperVO>> billbodySet = billBodyMap.entrySet();
        Iterator<Entry<String, SuperVO>> billbodyIter = billbodySet.iterator();
        while (billbodyIter.hasNext()) {
            Entry<String, SuperVO> entry = billbodyIter.next();
            SuperVO billbodyVO = entry.getValue();
            String vbilltype = (String) billbodyVO.getAttributeValue(MMGlobalConst.VBILLTYPECODE);

            if (destBillType.equalsIgnoreCase(vbilltype)) {
                continue;
            }
            filterMap.put(entry.getKey(), billbodyVO);

            billbodyIter.remove();
        }
        return filterMap;
    }

    /**
     * ������Դ�������ͷ���
     *
     * @param billBodyMap
     *        ���ݱ�����Ϣ
     * @return key-> ��Դ�������ͣ� value->���ݱ�����Ϣ
     */
    private Map<String, Map<String, SuperVO>> splitByVsrctype(Map<String, SuperVO> billBodyMap) {

        if (MMMapUtil.isEmpty(billBodyMap)) {
            return null;
        }

        Map<String, Map<String, SuperVO>> srcType2BillbodyMap = new HashMap<String, Map<String, SuperVO>>();

        Set<Entry<String, SuperVO>> billbodySet = billBodyMap.entrySet();
        Iterator<Entry<String, SuperVO>> billbodyIter = billbodySet.iterator();
        while (billbodyIter.hasNext()) {
            Entry<String, SuperVO> entry = billbodyIter.next();
            SuperVO billbodyVO = entry.getValue();
            String vsrctype = (String) billbodyVO.getAttributeValue(MMGlobalConst.VSRCTYPE);

            if (MMStringUtil.isEmpty(vsrctype)) {
                continue;
            }

            Map<String, SuperVO> value = srcType2BillbodyMap.get(vsrctype);
            if (value == null) {
                value = new HashMap<String, SuperVO>();
                srcType2BillbodyMap.put(vsrctype, value);
            }
            value.put(entry.getKey(), billbodyVO);
        }

        return srcType2BillbodyMap;
    }

    /**
     * ��ȡ��Դ���ݱ���id KEY
     *
     * @param billtype
     * @return
     */
    private String getVsrcbidKey(String billtype) {

        String vsrcbidkey = this.vsrcbidkeyMap.get(billtype);

        if (MMStringUtil.isEmpty(vsrcbidkey)) {
            return MMGlobalConst.VSRCBID;
        }

        return vsrcbidkey;
    }

    /**
     * ���ݵ������ͷ���
     *
     * @param paramvos
     *        ���ݱ���VO
     * @return ����֮��ĵ��ݱ���VO
     */
    private MMGPMapList<String, LinkQueryParamVO> splitByBilltype(LinkQueryParamVO[] paramvos) {
        MMGPMapList<String, LinkQueryParamVO> maplist = new MMGPMapList<String, LinkQueryParamVO>();
        for (LinkQueryParamVO paramvo : paramvos) {
            maplist.put(paramvo.getBilltype(), paramvo);
        }
        return maplist;
    }

    @Override
    public MMGPMapList<String, String> queryForwardBodyVOs(LinkQueryParamVO[] paramvos,
                                                           String destBillType,
                                                           Map<String, String> vsrcbidkeyMap)
            throws BusinessException {

        MMGPMapList<String, String> rtnList = new MMGPMapList<String, String>();
        if (MMArrayUtil.isEmpty(paramvos) || MMStringUtil.isEmpty(destBillType)) {
            return rtnList;
        }
        // ��ʼ������
        initData(destBillType, vsrcbidkeyMap);
        // ���������ͷ���
        MMGPMapList<String, LinkQueryParamVO> billBodyMap = splitByBilltype(paramvos);

        Set<Entry<String, List<LinkQueryParamVO>>> billBodySet = billBodyMap.entrySet();
        Iterator<Entry<String, List<LinkQueryParamVO>>> iterator = billBodySet.iterator();
        MMGPLinkQueryForwardBP linkQueryForwardBP = new MMGPLinkQueryForwardBP(vsrcbidkeyMap);
        while (iterator.hasNext()) {
            Entry<String, List<LinkQueryParamVO>> entry = iterator.next();
            // �ݹ��ѯ�������νڵ�
            List<LinkQueryBillNode> nodeList =
                    linkQueryForwardBP.queryOneTypeForwardBodyVO(entry.getKey(), entry.getValue());
            getDestBillTypeVOs(rtnList, nodeList);
        }

        return rtnList;
    }

    private void getDestBillTypeVOs(MMGPMapList<String, String> rtnList,
                                    List<LinkQueryBillNode> nodeList) {
        if (MMCollectionUtil.isEmpty(nodeList)) {
            return;
        }
        for (LinkQueryBillNode aNode : nodeList) {
            List<LinkQueryBillNode> oneTypeNodeList = aNode.getNodesByBillType(destBillType);
            if (MMCollectionUtil.isNotEmpty(oneTypeNodeList)) {
                for (LinkQueryBillNode aForwardBill : oneTypeNodeList) {
                    String key = MMStringUtil.isEmpty(aNode.getBillbid()) ? aNode.getBillid() : aNode.getBillbid();
                    String value =
                            MMStringUtil.isEmpty(aForwardBill.getBillbid()) ? aForwardBill.getBillid() : aForwardBill
                                .getBillbid();
                    rtnList.put(key, value);
                }
            }
        }
    }

    @Override
    public Map<String, LinkQueryBillNode> queryForwardBodyVOs(LinkQueryParamVO[] paramvos,
                                                              Map<String, String> vsrcbidkeyMap)
            throws BusinessException {
        Map<String, LinkQueryBillNode> rtnList = new HashMap<String, LinkQueryBillNode>();
        if (MMArrayUtil.isEmpty(paramvos) || MMStringUtil.isEmpty(destBillType)) {
            return rtnList;
        }
        // ��ʼ������
        initData(destBillType, vsrcbidkeyMap);
        // ���������ͷ���
        MMGPMapList<String, LinkQueryParamVO> billBodyMap = splitByBilltype(paramvos);

        Set<Entry<String, List<LinkQueryParamVO>>> billBodySet = billBodyMap.entrySet();
        Iterator<Entry<String, List<LinkQueryParamVO>>> iterator = billBodySet.iterator();
        MMGPLinkQueryForwardBP linkQueryForwardBP = new MMGPLinkQueryForwardBP(vsrcbidkeyMap);
        while (iterator.hasNext()) {
            Entry<String, List<LinkQueryParamVO>> entry = iterator.next();
            // �ݹ��ѯ�������νڵ�
            List<LinkQueryBillNode> nodeList =
                    linkQueryForwardBP.queryOneTypeForwardBodyVO(entry.getKey(), entry.getValue());
            if (MMCollectionUtil.isEmpty(nodeList)) {
                continue;
            }
            for (LinkQueryBillNode aNode : nodeList) {
                String key = MMStringUtil.isEmpty(aNode.getBillbid()) ? aNode.getBillid() : aNode.getBillbid();
                rtnList.put(key, aNode);
            }
        }
        return rtnList;
    }

}