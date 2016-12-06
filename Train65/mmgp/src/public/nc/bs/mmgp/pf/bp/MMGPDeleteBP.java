package nc.bs.mmgp.pf.bp;

import nc.bs.pubapp.pub.rule.ReturnBillCodeRule;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b>ɾ��BP</b>
 * <p>
 * ʵ��ɾ��������BP�࣬Ĭ�����������¹���</br> ǰ����
 * <ul>
 * <li></li>
 * </ul>
 * �����
 * <ul>
 * <li>���ݺŻ��˹���</li>
 * </ul>
 * </p>
 * <p>
 * ��������:2013-5-9
 * </p>
 *
 * @param <T>
 *            VO����
 * @since NC V6.3
 * @author zhumh
 */
public class MMGPDeleteBP<T extends MMGPAbstractBill> {

	/** ɾ������. */
	protected DeleteBPTemplate<T> bpTmplate;

	/**
	 * Instantiates a new mMGP delete bp.
	 *
	 * @param point
	 *            the point
	 */
	public MMGPDeleteBP(IPluginPoint point) {
		super();
		bpTmplate = new DeleteBPTemplate<T>(point);
	}

	/**
	 * Delete.
	 *
	 * @param bills
	 *            the bills
	 * @throws BusinessException
	 *             the business exception
	 */
	public void delete(T[] bills) throws BusinessException {
		addBeforeRule(bpTmplate.getAroundProcesser(), bills);
		addAfterRule(bpTmplate.getAroundProcesser(), bills);
		bpTmplate.delete(bills);
	}

	/**
	 * Adds the before rule.
	 *
	 * @param processer
	 *            the processer
	 * @param bills
	 *            the bills
	 */
	protected void addBeforeRule(AroundProcesser<T> processer, T[] bills) {
		if (!MMArrayUtil.isEmpty(bills)) {
			IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(
					bills[0].getParent(), IFlowBizItf.class);
			Integer approveStatus = flowInfo.getApproveStatus();
			String billcode = flowInfo.getBillNo();
			if (BillStatusEnum.FREE.toIntValue() != approveStatus) {
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0054")/*@res "������̬�ĵ��ݲ���ɾ�������µ���ɾ��ʧ��"*/
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0055")/*@res "��"*/ + billcode + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0056")/*@res "��"*/);
			}
		}

	}

	/**
	 * Adds the after rule.
	 *
	 * @param processer
	 *            the processer
	 * @param bills
	 *            the bills
	 */
	@SuppressWarnings("unchecked")
	protected void addAfterRule(AroundProcesser<T> processer, T[] bills) {

		if (!MMArrayUtil.isEmpty(bills)) {
			// ��ȡ����������ֶ���Ϣ
			IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(
					bills[0].getParent(), IFlowBizItf.class);
			String billtype = flowInfo.getBilltype();
			String vbillcode = flowInfo
					.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
			// ���ݺŻ��˹���
			IRule<T> rule = new ReturnBillCodeRule(billtype, vbillcode,
					MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
			processer.addAfterRule(rule);
		}
	}
}