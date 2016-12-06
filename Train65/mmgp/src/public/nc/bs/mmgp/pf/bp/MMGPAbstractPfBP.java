package nc.bs.mmgp.pf.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.log.TimeLog;

/**
 * <b>审批流相关BP父类</b>
 * <p>
 * 支持审批流相关BP的业务扩展机制.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 *
 * @param <T>
 *        VO类型
 * @since  NC V6.3
 * @author zhumh
 */
public abstract class MMGPAbstractPfBP<T extends MMGPAbstractBill> {

	/** 业务扩展处理类. */
	protected CompareAroundProcesser<T> processer;

	/**
     * 构造方法，传入BP扩展点.
     *
     * @param point
     *        BP扩展点
     */
	public MMGPAbstractPfBP(IPluginPoint point) {
		super();
		processer = new CompareAroundProcesser<T>(point);
	}

	/**
     * BP操作实现.支持业务规则扩展
     *
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     * @return 操作后VO
     * @throws BusinessException
     *         the business exception
     */
	public T[] doAction(T[] clientFullVOs, T[] originBills)
			throws BusinessException {

		// 增加前规则
		this.addBeforeRule(clientFullVOs, originBills);
		// 增加后规则
		this.addAfterRule(clientFullVOs, originBills);
		// 处理前规则
		TimeLog.logStart();
		T[] returnVOs = processer.before(clientFullVOs, originBills);
		TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0051")/*@res "业务处理前执行业务规则"*/);
		// 处理业务
		TimeLog.logStart();
		returnVOs = this.doBusiness(returnVOs, originBills);
		// 更新到数据库
		BillUpdate<T> bo = new BillUpdate<T>();
		returnVOs = bo.update(returnVOs, originBills);
		TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0052")/*@res "业务处理"*/);
		// 处理后规则
		TimeLog.logStart();
		returnVOs = processer.after(returnVOs, originBills);
		TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0053")/*@res "业务处理后执行业务规则"*/);

		return returnVOs;
	}

	/**
     * 业务处理.
     *
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     * @return 处理后VO
     */
	protected T[] doBusiness(T[] clientFullVOs, T[] originBills) {
		for (T bill : clientFullVOs) {
			bill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		return clientFullVOs;
	}

	/**
     * 增加前规则.
     *
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     */
	protected void addBeforeRule(T[] clientFullVOs, T[] originBills) {

	}

	/**
     * 增加后规则.
     *
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     */
	protected void addAfterRule(T[] clientFullVOs, T[] originBills) {

	}

}