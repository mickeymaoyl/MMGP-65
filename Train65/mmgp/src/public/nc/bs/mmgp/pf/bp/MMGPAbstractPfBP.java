package nc.bs.mmgp.pf.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.log.TimeLog;

/**
 * <b>���������BP����</b>
 * <p>
 * ֧�����������BP��ҵ����չ����.
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
public abstract class MMGPAbstractPfBP<T extends MMGPAbstractBill> {

	/** ҵ����չ������. */
	protected CompareAroundProcesser<T> processer;

	/**
     * ���췽��������BP��չ��.
     *
     * @param point
     *        BP��չ��
     */
	public MMGPAbstractPfBP(IPluginPoint point) {
		super();
		processer = new CompareAroundProcesser<T>(point);
	}

	/**
     * BP����ʵ��.֧��ҵ�������չ
     *
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     * @return ������VO
     * @throws BusinessException
     *         the business exception
     */
	public T[] doAction(T[] clientFullVOs, T[] originBills)
			throws BusinessException {

		// ����ǰ����
		this.addBeforeRule(clientFullVOs, originBills);
		// ���Ӻ����
		this.addAfterRule(clientFullVOs, originBills);
		// ����ǰ����
		TimeLog.logStart();
		T[] returnVOs = processer.before(clientFullVOs, originBills);
		TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0051")/*@res "ҵ����ǰִ��ҵ�����"*/);
		// ����ҵ��
		TimeLog.logStart();
		returnVOs = this.doBusiness(returnVOs, originBills);
		// ���µ����ݿ�
		BillUpdate<T> bo = new BillUpdate<T>();
		returnVOs = bo.update(returnVOs, originBills);
		TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0052")/*@res "ҵ����"*/);
		// ��������
		TimeLog.logStart();
		returnVOs = processer.after(returnVOs, originBills);
		TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0053")/*@res "ҵ�����ִ��ҵ�����"*/);

		return returnVOs;
	}

	/**
     * ҵ����.
     *
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     * @return �����VO
     */
	protected T[] doBusiness(T[] clientFullVOs, T[] originBills) {
		for (T bill : clientFullVOs) {
			bill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		return clientFullVOs;
	}

	/**
     * ����ǰ����.
     *
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     */
	protected void addBeforeRule(T[] clientFullVOs, T[] originBills) {

	}

	/**
     * ���Ӻ����.
     *
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     */
	protected void addAfterRule(T[] clientFullVOs, T[] originBills) {

	}

}