package nc.bs.mmgp.pf.bp;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;

/**
 * <b>�ջ�BP</b>
 * <p>
 * ʵ���ջز�����BP�࣬Ŀǰֻ�Ǹ��±���VO������״̬�ڽű��������޸ģ�.
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
public class MMGPUnSendApproveBP<T extends MMGPAbstractBill> extends MMGPAbstractPfBP<T> {

    /**
     * ���췽��.
     * 
     * @param point
     *        ��չ��
     */
    public MMGPUnSendApproveBP(IPluginPoint point) {
        super(point);
    }

    /**
     * �ջ�.
     * 
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     * @return �����VO
     * @throws BusinessException
     *         the business exception
     */
    public T[] unSendApprove(T[] clientFullVOs,
                             T[] originBills) throws BusinessException {

        return super.doAction(clientFullVOs, originBills);
    }

    /*
     * (non-Javadoc)
     * @see
     * nc.bs.mmgp.pf.bp.MMGPAbstractPfBP#addBeforeRule(nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser,
     * T[], T[])
     */
    @Override
    protected void addBeforeRule(T[] clientFullVOs,
                                 T[] originBills) {
        super.addBeforeRule(clientFullVOs, originBills);
    }

}
