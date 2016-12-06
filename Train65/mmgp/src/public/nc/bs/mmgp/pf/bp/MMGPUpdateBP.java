package nc.bs.mmgp.pf.bp;

import nc.bs.pubapp.pub.rule.BillCodeCheckRule;
import nc.bs.pubapp.pub.rule.CheckNotNullRule;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.bs.pubapp.pub.rule.UpdateBillCodeRule;
import nc.impl.mmgp.uif2.rule.MMGPFieldLengthCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFillUpdateDataRule;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b>单据修改操作BP</b>
 * <p>
 * 实现单据修改操作的BP类，默认增加了以下规则：</br> 前规则：
 * <ul>
 * <li>补充默认值的规则</li>
 * <li>单据字段长度检查规则</li>
 * <li>单据是否存在表体数据的规则</li>
 * </ul>
 * 后规则：
 * <ul>
 * <li>单据号校验规则</li>
 * </ul>
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO类型
 * @since NC V6.3
 * @author zhumh
 */
public class MMGPUpdateBP<T extends MMGPAbstractBill> {

    /** 修改操作实现. */
    protected UpdateBPTemplate<T> bpTmplate;

    /**
     * 构造方法.
     * 
     * @param point
     *        扩展点
     */
    public MMGPUpdateBP(IPluginPoint point) {
        super();
        bpTmplate = new UpdateBPTemplate<T>(point);
    }

    /**
     * 修改操作.
     * 
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     * @return 修改后VO
     * @throws BusinessException
     *         the business exception
     */
    public T[] update(T[] clientFullVOs,
                      T[] originBills) throws BusinessException {
        /* Jun 18, 2013 wangweir 将表头VO设置为更新态，解决不更新表头修改人，修改时间的bug Begin */
        for (IBill vo : clientFullVOs) {
            if (vo.getParent().getStatus() != VOStatus.UPDATED) {
                vo.getParent().setStatus(VOStatus.UPDATED);
            }
        }
        /* Jun 18, 2013 wangweir End */

        addBeforeRule(bpTmplate.getAroundProcesser(), clientFullVOs, originBills);
        addAfterRule(bpTmplate.getAroundProcesser(), clientFullVOs, originBills);
        return bpTmplate.update(clientFullVOs, originBills);
    }

    /**
     * 增加前规则.
     * 
     * @param processer
     *        业务规则扩展
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     */
    @SuppressWarnings("unchecked")
    protected void addBeforeRule(CompareAroundProcesser<T> processer,
                                 T[] clientFullVOs,
                                 T[] originBills) {
        // 补充默认值的规则
        IRule<T> rule = new MMGPFillUpdateDataRule();
        processer.addBeforeRule(rule);
        // 单据字段长度检查规则
        rule = new MMGPFieldLengthCheckRule();
        processer.addBeforeRule(rule);
        // 单据是否存在表体数据的规则
        rule = new CheckNotNullRule();
        processer.addBeforeRule(rule);
        // 组织停用校验
        rule = new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null);
        processer.addBeforeRule(rule);
        // 长度检查
        rule = new MMGPFieldLengthCheckRule();
        processer.addBeforeRule(rule);

        if (!MMArrayUtil.isEmpty(clientFullVOs)) {
            // 获取审批流相关字段信息
            IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(clientFullVOs[0].getParent(), IFlowBizItf.class);
            String billtype = flowInfo.getBilltype();
            String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
            // 更新单据号的规则
            ICompareRule<T> compareRule =
                    new UpdateBillCodeRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
            processer.addBeforeRule(compareRule);
        }

    }

    /**
     * 增加后规则.
     * 
     * @param processer
     *        业务规则扩展
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     */
    @SuppressWarnings("unchecked")
    protected void addAfterRule(CompareAroundProcesser<T> processer,
                                T[] clientFullVOs,
                                T[] originBills) {

        if (!MMArrayUtil.isEmpty(clientFullVOs)) {
            // 获取审批流相关字段信息
            IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(clientFullVOs[0].getParent(), IFlowBizItf.class);
            String billtype = flowInfo.getBilltype();
            String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
            // 单据号校验规则
            IRule<T> rule = new BillCodeCheckRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
            processer.addAfterRule(rule);
        }
    }

}
