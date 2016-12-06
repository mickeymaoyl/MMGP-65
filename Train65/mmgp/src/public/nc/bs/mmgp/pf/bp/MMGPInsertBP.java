package nc.bs.mmgp.pf.bp;

import nc.bs.pubapp.pub.rule.BillCodeCheckRule;
import nc.bs.pubapp.pub.rule.CheckNotNullRule;
import nc.bs.pubapp.pub.rule.CreateBillCodeRule;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFieldLengthCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFillInsertDataRule;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;

/**
 * <b>������������BP</b>
 * <p>
 * ʵ�ֵ�������������BP�࣬Ĭ�����������¹���</br>
 * ǰ����
 * <ul>
 * <li>����Ĭ��ֵ�Ĺ���</li>
 * <li>�����ֶγ��ȼ�����</li>
 * <li>�����Ƿ���ڱ������ݵĹ���</li>
 * <li>�������ݺŹ���</li>
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
 * @since  NC V6.3
 * @author zhumh
 */
public class MMGPInsertBP<T extends MMGPAbstractBill> {

	/** The bp tmplate. */
	protected InsertBPTemplate<T> bpTmplate;

	/**
     * Instantiates a new mMGP insert bp.
     * 
     * @param point
     *        the point
     */
	public MMGPInsertBP(IPluginPoint point) {
		super();
		bpTmplate = new InsertBPTemplate<T>(point);
	}

	/**
     * Insert.
     * 
     * @param bills
     *        the bills
     * @return the t[]
     * @throws BusinessException
     *         the business exception
     */
	public T[] insert(T[] bills) throws BusinessException {

		addBeforeRule(bpTmplate.getAroundProcesser(), bills);
		addAfterRule(bpTmplate.getAroundProcesser(), bills);
		return bpTmplate.insert(bills);
	}

	/**
     * Adds the before rule.
     * 
     * @param processer
     *        the processer
     * @param bills
     *        the bills
     */
	@SuppressWarnings("unchecked")
	protected void addBeforeRule(AroundProcesser<T> processer, T[] bills) {

		// ����Ĭ��ֵ�Ĺ���
		IRule<T> rule = new MMGPFillInsertDataRule();
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
        
		if (!MMArrayUtil.isEmpty(bills)) {
			// ��ȡ����������ֶ���Ϣ
			IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(
					bills[0].getParent(), IFlowBizItf.class);
			String billtype = flowInfo.getBilltype();
			String vbillcode = flowInfo
					.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
			// �������ݺŵĹ���
			rule = new CreateBillCodeRule(billtype, vbillcode,
					MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
			processer.addBeforeRule(rule);
		}
	}

	/**
     * Adds the after rule.
     * 
     * @param processer
     *        the processer
     * @param bills
     *        the bills
     */
	protected void addAfterRule(AroundProcesser<T> processer, T[] bills) {

		if (!MMArrayUtil.isEmpty(bills)) {
			// ��ȡ����������ֶ���Ϣ
			IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(
					bills[0].getParent(), IFlowBizItf.class);
			String billtype = flowInfo.getBilltype();
			String vbillcode = flowInfo
					.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
			// ���ݺ�У�����
			@SuppressWarnings("unchecked")
			IRule<T> rule = new BillCodeCheckRule(billtype, vbillcode,
					MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
			processer.addAfterRule(rule);
		}
	}

}
