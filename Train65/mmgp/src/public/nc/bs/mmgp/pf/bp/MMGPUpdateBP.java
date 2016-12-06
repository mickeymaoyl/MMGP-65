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
 * <b>�����޸Ĳ���BP</b>
 * <p>
 * ʵ�ֵ����޸Ĳ�����BP�࣬Ĭ�����������¹���</br> ǰ����
 * <ul>
 * <li>����Ĭ��ֵ�Ĺ���</li>
 * <li>�����ֶγ��ȼ�����</li>
 * <li>�����Ƿ���ڱ������ݵĹ���</li>
 * </ul>
 * �����
 * <ul>
 * <li>���ݺ�У�����</li>
 * </ul>
 * </p>
 * <p>
 * ��������:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO����
 * @since NC V6.3
 * @author zhumh
 */
public class MMGPUpdateBP<T extends MMGPAbstractBill> {

    /** �޸Ĳ���ʵ��. */
    protected UpdateBPTemplate<T> bpTmplate;

    /**
     * ���췽��.
     * 
     * @param point
     *        ��չ��
     */
    public MMGPUpdateBP(IPluginPoint point) {
        super();
        bpTmplate = new UpdateBPTemplate<T>(point);
    }

    /**
     * �޸Ĳ���.
     * 
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     * @return �޸ĺ�VO
     * @throws BusinessException
     *         the business exception
     */
    public T[] update(T[] clientFullVOs,
                      T[] originBills) throws BusinessException {
        /* Jun 18, 2013 wangweir ����ͷVO����Ϊ����̬����������±�ͷ�޸��ˣ��޸�ʱ���bug Begin */
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
     * ����ǰ����.
     * 
     * @param processer
     *        ҵ�������չ
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     */
    @SuppressWarnings("unchecked")
    protected void addBeforeRule(CompareAroundProcesser<T> processer,
                                 T[] clientFullVOs,
                                 T[] originBills) {
        // ����Ĭ��ֵ�Ĺ���
        IRule<T> rule = new MMGPFillUpdateDataRule();
        processer.addBeforeRule(rule);
        // �����ֶγ��ȼ�����
        rule = new MMGPFieldLengthCheckRule();
        processer.addBeforeRule(rule);
        // �����Ƿ���ڱ������ݵĹ���
        rule = new CheckNotNullRule();
        processer.addBeforeRule(rule);
        // ��֯ͣ��У��
        rule = new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null);
        processer.addBeforeRule(rule);
        // ���ȼ��
        rule = new MMGPFieldLengthCheckRule();
        processer.addBeforeRule(rule);

        if (!MMArrayUtil.isEmpty(clientFullVOs)) {
            // ��ȡ����������ֶ���Ϣ
            IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(clientFullVOs[0].getParent(), IFlowBizItf.class);
            String billtype = flowInfo.getBilltype();
            String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
            // ���µ��ݺŵĹ���
            ICompareRule<T> compareRule =
                    new UpdateBillCodeRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
            processer.addBeforeRule(compareRule);
        }

    }

    /**
     * ���Ӻ����.
     * 
     * @param processer
     *        ҵ�������չ
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     */
    @SuppressWarnings("unchecked")
    protected void addAfterRule(CompareAroundProcesser<T> processer,
                                T[] clientFullVOs,
                                T[] originBills) {

        if (!MMArrayUtil.isEmpty(clientFullVOs)) {
            // ��ȡ����������ֶ���Ϣ
            IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(clientFullVOs[0].getParent(), IFlowBizItf.class);
            String billtype = flowInfo.getBilltype();
            String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
            // ���ݺ�У�����
            IRule<T> rule = new BillCodeCheckRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
            processer.addAfterRule(rule);
        }
    }

}
