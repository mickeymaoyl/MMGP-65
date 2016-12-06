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
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since: 创建日期:2014-10-29
 * @author:liwsh
 */
public class MMGPLinkQueryServiceImpl implements IMMGPLinkQueryService {

    /**
     * 目的单据类型
     */
    private String destBillType;

    /**
     * 来源单据表体ID字段名称信息，key->单据类型 value->来源单据表体id名称<br\>
     * 单据回溯过程中遇到的所有单据，起两个作用：<br\>
     * 1. 限定回溯范围，避免无谓的搜索。回溯过程如果遇到不是这个map中的单据，则终止搜索<br\>
     * 2. 定义单据表体来源单据bid的key值，因为无法通过其他任何办法获取<br\>
     */
    private Map<String, String> vsrcbidkeyMap;

    /**
     * 根据单据类型和单据表体id等，查询对应的来源单据表体（目的单据类型指定哪一个类型表体）<br\>
     * 比如，采购入库单->采购到货单->采购订单->请购单->作业。 我们需要根据采购入库单的表体id，找到这行对应的作业表体信息<br\>
     * 我们就可以传入由采购入库单表体id和采购入库单单据类型构造的paramvos和作业的单据类型作为的destBillType<br\>
     * 如果我们要查对应的采购订单是哪一行，destBillType换成采购订单的单据类型即可。
     */
    @Override
    public Map<String, SuperVO> querySrcBodyVO(LinkQueryParamVO[] paramvos,
                                               String destBillType,
                                               Map<String, String> vsrcbidkeyMap) throws BusinessException {

        if (MMArrayUtil.isEmpty(paramvos) || MMStringUtil.isEmpty(destBillType)) {
            return Collections.emptyMap();
        }

        // 初始化数据
        initData(destBillType, vsrcbidkeyMap);

        // 按单据类型分组
        MMGPMapList<String, LinkQueryParamVO> billBodyMap = splitByBilltype(paramvos);

        // 遍历分组后的单据表体
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
     * 初始化数据
     *
     * @param destBillType
     *        目的单据类型
     * @param vsrcbidkeyMap
     *        来源单据表体ID字段 key信息
     */
    private void initData(String destBillType,
                          Map<String, String> vsrcbidkeyMap) {
        this.destBillType = destBillType;
        this.vsrcbidkeyMap = vsrcbidkeyMap;

    }

    /**
     * 获取某一种单据类型的单据表体数据对应的来源单据表体（具体哪一种来源单据表体，由目的单据类型指定）
     *
     * @param billtype
     *        单据类型
     * @param billbodyList
     *        单据表体VO队列
     * @return key-> 单据表体id， value->目的单据类型对应的来源单据表体VO
     * @throws BusinessException
     */
    private Map<String, SuperVO> querySrcBodyVO(String billtype,
                                                List<LinkQueryParamVO> billbodyList) throws BusinessException {

        // 根据单据表体源头单据类型是否为目的单据类型分为2组
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

        // 当源头单据类型就是目的单据类型时，查询目的单据类型对应的单据表体（直接根据源头表体id查询需要返回的数据）
        if (MMCollectionUtil.isNotEmpty(firstIsDestBodyList)) {
            destBodyMap.putAll(querySrcBodyForSpecial(firstIsDestBodyList));
        }

        // 当源头单据类型不是目的单据类型时，查询目的单据类型对应的单据表体(需要根据来源单据一级一级往上回溯，直到找到目的单据类型对应的单据表体)
        if (MMCollectionUtil.isNotEmpty(normalBodyList)) {
            destBodyMap.putAll(querySrcBodyForNormal(billtype, normalBodyList));
        }

        return destBodyMap;
    }

    /**
     * 当源头单据类型就是目的单据类型时，查询目的单据类型对应的单据表体
     *
     * @param billtype
     *        单据类型
     * @param firstIsDestBodyList
     *        单据表体VO队列
     * @return key-> 单据表体id， value->目的单据类型对应的来源单据表体VO
     */
    private Map<String, SuperVO> querySrcBodyForSpecial(List<LinkQueryParamVO> firstIsDestBodyList)
            throws BusinessException {

        // 源头表体id集合
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

        // 查询源头单据表体
        Map<String, SuperVO> billbodyMap =
                MMGPLinkQueryDAO.getInstance().queryVOByPks(destBillType, firstbidSet.toArray(new String[0]), null);

        if (MMMapUtil.isEmpty(billbodyMap)) {
            billbodyMap =
                    MMGPLinkQueryDAO.getInstance().queryVOFromAllChild(
                        destBillType,
                        firstbidSet.toArray(new String[0]));
        }

        // 单据表体id与源头表体id映射
        Map<String, String> bidToFstbidMap = new HashMap<String, String>();
        for (LinkQueryParamVO billbodyVO : firstIsDestBodyList) {
            String firstbid = billbodyVO.getVfirstbid();
            String billbid = billbodyVO.getBillbid();
            bidToFstbidMap.put(billbid, firstbid);
        }

        // 返回结果
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
     * 当源头单据类型不是目的单据类型时，查询目的单据类型对应的单据表体(需要根据来源单据一级一级往上回溯，直到找到目的单据类型对应的单据表体)
     *
     * @param billtype
     *        单据类型
     * @param normalBodyList
     *        单据表体VO队列
     * @return key-> 单据表体id， value->目的单据类型对应的来源单据表体VO
     * @throws BusinessException
     */
    private Map<String, SuperVO> querySrcBodyForNormal(String billtype,
                                                       List<LinkQueryParamVO> normalBodyList)
            throws BusinessException {

        String realBillType = PfUtilBaseTools.getRealBilltype(billtype);
        Class< ? extends SuperVO> subTableClass = MMGPDefaultBillFlow.getInstance(realBillType).getSubTableClass();

        if (subTableClass == null) {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0038")/*@res "目的单据类型"*/ + billtype + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0039")/*@res " 对应的单据元数据没有在单据流程信息获取回写接口里面定义来源单据映射信息"*/);
        }

        /**
         * 单据表体VO ID与单据表体VO映射<br\>
         * value的值会变，每一次都变成来源单据对应的表体，直到来源单据变成目的单据类型为止。<br\>
         * 当截止的时候，该map就是需要返回的值
         */
        Map<String, SuperVO> bidTOBodyVOMap = buildBodyVOMap(normalBodyList, subTableClass);

        this.querySourceBodyVO(bidTOBodyVOMap);

        return bidTOBodyVOMap;
    }

    /**
     * 构建表体VO MAP
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

            // 单据类型
            billbodyVO.setAttributeValue(MMGlobalConst.VBILLTYPECODE, linkqueryVO.getBilltype());
            // 单据子表id
            billbodyVO.setAttributeValue(MMGlobalConst.BILLBID, linkqueryVO.getBillbid());
            // 来源单据类型
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCTYPE, linkqueryVO.getVsrctype());
            // 来源单据表体id
            billbodyVO.setAttributeValue(MMGlobalConst.VSRCBID, linkqueryVO.getVsrcbid());

            // 源头单据类型
            billbodyVO.setAttributeValue(MMGlobalConst.VFIRSTTYPE, linkqueryVO.getVfirsttype());
            // 源头单据表体id
            billbodyVO.setAttributeValue(MMGlobalConst.VFIRSTBID, linkqueryVO.getVfirstbid());

            bidTOBodyVOMap.put(linkqueryVO.getBillbid(), billbodyVO);
        }
        return bidTOBodyVOMap;
    }

    /**
     * 寻找来源单据表体VO
     *
     * @param billBodyMap
     *        key-> 回溯起点单据表体id value->当前单据表体vo
     * @param destBilltype
     *        目的单据类型
     * @throws BusinessException
     */
    private void querySourceBodyVO(Map<String, SuperVO> billBodyMap) throws BusinessException {

        // 过滤出当前单据类型不是目的单据类型的数据，并从原map中移除
        Map<String, SuperVO> filterMap = filterBillBody(billBodyMap);

        if (MMMapUtil.isEmpty(filterMap)) {
            return;
        }

        // 根据来源单据类型分组
        Map<String, Map<String, SuperVO>> srcType2BillbodyMap = this.splitByVsrctype(filterMap);

        // 来源单据类型为空，说明当前单据是源头单据，但还不是目的单据，移除掉
        if (MMMapUtil.isEmpty(srcType2BillbodyMap)) {
            MMGPLinkQueryUtil.removeDataFromMap(billBodyMap, filterMap.keySet().toArray(new String[0]));
            return;
        }

        // 分组查询来源单据，并将map中的当前单据替换为来源单据
        Map<String, SuperVO> replacedMap = divideQuerySrcBody(srcType2BillbodyMap);
        billBodyMap.putAll(replacedMap);

        // 递归调用
        querySourceBodyVO(billBodyMap);
    }

    /**
     * 分组查询来源单据，并将map中的当前单据替换为来源单据
     *
     * @param srcType2BillbodyMap
     *        key-> 来源单据类型 value->单据表体信息
     * @return
     * @throws BusinessException
     *         业务异常
     */
    private Map<String, SuperVO> divideQuerySrcBody(Map<String, Map<String, SuperVO>> srcType2BillbodyMap)
            throws BusinessException {

        Set<Entry<String, Map<String, SuperVO>>> set = srcType2BillbodyMap.entrySet();
        Iterator<Entry<String, Map<String, SuperVO>>> iterator = set.iterator();
        while (iterator.hasNext()) {

            Entry<String, Map<String, SuperVO>> entry = iterator.next();

            // 如果来源单据类型不是我们自己的节点，并且不再传入MAP中，就不再继续查找了 gaotx 2015-4-9
            String vsrcbilltype = entry.getKey();
            if (!vsrcbidkeyMap.keySet().contains(vsrcbilltype) && !vsrcbilltype.startsWith("YG")) {
                iterator.remove();
                continue;
            }
            // 查询字段
            String[] selectfields = this.buildSelectFields(vsrcbilltype);

            // 如果来源单据为空，则不进行寻源
            if (selectfields != null && MMStringUtil.isEmpty(selectfields[3])) {
                iterator.remove();
                continue;
            }

            // 如果来源单据类型是备料计划，不继续向上寻源。因为已经没有意义
            if (selectfields != null && MMGlobalConst.BILL_TYPE_PICKM.equalsIgnoreCase(vsrcbilltype)) {
                iterator.remove();
                continue;
            }

            // 来源单据表体id
            Map<String, SuperVO> currentBodyMap = entry.getValue();
            Set<String> vsrcbidSet = getVsrcbidSet(currentBodyMap);

            // 查询来源单据
            Map<String, SuperVO> srcbodyMap =
                    MMGPLinkQueryDAO.getInstance().queryVOByPks(
                        vsrcbilltype,
                        vsrcbidSet.toArray(new String[0]),
                        selectfields);

            if (MMMapUtil.isEmpty(srcbodyMap)) {

                /**
                 * 因为完工报告推出的入库单，入库单来源单据表体id记录的是完工报告的孙表。<br\>
                 * 但是完工报告在单据流程信息获取回写接口里面定义来源单据映射为完工报告子表，无法根据来源信息查找到完工报告。<br\>
                 * 所有直接根据源头单据表体id和源头单据类型（从生产报告上取，因为产成品入库单上的源头可能会存在问题，存在问题情况如下）查找，这过程可能会漏掉数据，目前没办法。<br\>
                 * 产成品入库单上的源头信息错误的场景如下：<br\>
                 * 1. 走工序完工报告生成生产报告，合格品入库的时候，保存的产成品入库单源头id和源头表体id都记录的是工序完工报告的表头id。<br\>
                 * 2. 交接单接收的时候，推出的产成品入库单的源头信息都为空<br\>
                 */
                if (MMGlobalConst.BILL_TYPE_WR.equalsIgnoreCase(vsrcbilltype)) {
                    boolean isFind = querySrcForStockInBill(vsrcbilltype, currentBodyMap);
                    if (!isFind) {
                        iterator.remove();
                        continue;
                    }
                } else {
                    // 防止验证的时候报错，寻源不到直接下一个
                    Logger.error("无法找到单据类型为：" + vsrcbilltype + "对应的来源单据表体。");
                    iterator.remove();
                    continue;
                    // ExceptionUtils.wrappBusinessException("无法找到单据类型为：" + vsrcbilltype + "对应的来源单据表体。");
                }
            } else {
                // 用来源单据表体 替换 当前单据表体
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
     * 为产成品入库单寻源的时候，特殊处理，从源头开始查找。
     *
     * @param vsrcbilltype
     *        来源单据类型
     * @param currentBodyMap
     * @throws BusinessException
     */
    private boolean querySrcForStockInBill(String vsrcbilltype,
                                           Map<String, SuperVO> currentBodyMap) throws BusinessException {

        // 从生产报告表体取生产订单信息作为来源信息。
        // 1. 来源单据表体id为生产报告孙表pk
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
        // Logger.error("无法找到单据类型为：" + vsrcbilltype + "对应的来源单据表体。");
        // return false;
        // // ExceptionUtils.wrappBusinessException("无法找到单据类型为：" + vsrcbilltype + "对应的来源单据表体。");
        // }
        //
        // replaceBodyByFirstBody(currentBodyMap, firstBodyMap, vfirstbilltype);
        return true;
    }

    /**
     * 用来源单据表体替换当前单据表体
     *
     * @param currentBodyMap
     *        当前单据表体信息
     * @param srcbodyMap
     *        来源单据表体信息
     */
    private void replaceBodyBySrcBody(Map<String, SuperVO> currentBodyMap,
                                      Map<String, SuperVO> srcbodyMap) {

        // 单据bid与来源单据bid映射
        Map<String, String> bid2SrcBidMap = getBid2SrcBidMap(currentBodyMap);

        // 将当前单据替换为来源单据
        Set<Entry<String, String>> bid2SrcbidSet = bid2SrcBidMap.entrySet();
        Iterator<Entry<String, String>> bid2SrcbidIter = bid2SrcbidSet.iterator();

        while (bid2SrcbidIter.hasNext()) {

            // 来源单据表体
            Entry<String, String> bid2SrcbidEntry = bid2SrcbidIter.next();
            String srcbid = bid2SrcbidEntry.getValue();
            SuperVO srcbodyVO = srcbodyMap.get(srcbid);

            // 替换单据
            currentBodyMap.put(bid2SrcbidEntry.getKey(), srcbodyVO);
        }
    }

    /**
     * 构建单据bid与来源单据bid映射
     *
     * @param currentBodyMap
     *        当前单据信息
     * @return key-> 最原始单据表体id value-> 当前单据表体来源id
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
     * 构建查询字段
     *
     * @param srcbilltype
     *        来源单据类型
     * @return 查询字段
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
     * 获取来源单据表体id集合
     *
     * @param currentBodyMap
     *        单据表体信息
     * @return 来源单据表体id
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
     * 过滤出当前单据类型不是目的单据类型的单据表体，并从原map中移除
     *
     * @param billBodyMap
     *        单据表体信息，key->最开始的单据表体id value->对应的当前单据表体id（有可能是key对应的单据表体的来源，也有可能是来源的来源等等）
     * @return 当前单据不是目的单据类型的单据表体
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
     * 根据来源单据类型分组
     *
     * @param billBodyMap
     *        单据表体信息
     * @return key-> 来源单据类型， value->单据表体信息
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
     * 获取来源单据表体id KEY
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
     * 根据单据类型分组
     *
     * @param paramvos
     *        单据表体VO
     * @return 分组之后的单据表体VO
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
        // 初始化数据
        initData(destBillType, vsrcbidkeyMap);
        // 按单据类型分组
        MMGPMapList<String, LinkQueryParamVO> billBodyMap = splitByBilltype(paramvos);

        Set<Entry<String, List<LinkQueryParamVO>>> billBodySet = billBodyMap.entrySet();
        Iterator<Entry<String, List<LinkQueryParamVO>>> iterator = billBodySet.iterator();
        MMGPLinkQueryForwardBP linkQueryForwardBP = new MMGPLinkQueryForwardBP(vsrcbidkeyMap);
        while (iterator.hasNext()) {
            Entry<String, List<LinkQueryParamVO>> entry = iterator.next();
            // 递归查询所有下游节点
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
        // 初始化数据
        initData(destBillType, vsrcbidkeyMap);
        // 按单据类型分组
        MMGPMapList<String, LinkQueryParamVO> billBodyMap = splitByBilltype(paramvos);

        Set<Entry<String, List<LinkQueryParamVO>>> billBodySet = billBodyMap.entrySet();
        Iterator<Entry<String, List<LinkQueryParamVO>>> iterator = billBodySet.iterator();
        MMGPLinkQueryForwardBP linkQueryForwardBP = new MMGPLinkQueryForwardBP(vsrcbidkeyMap);
        while (iterator.hasNext()) {
            Entry<String, List<LinkQueryParamVO>> entry = iterator.next();
            // 递归查询所有下游节点
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