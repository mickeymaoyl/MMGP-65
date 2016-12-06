package nc.bs.mmgp.pf.bp;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;

/**
 * <b>�����ύ��BP</b>
 * <p>
 * ʵ�������ύ��������BP�࣬Ŀǰֻ�Ǹ��±���VO������״̬�ڽű��������޸ģ�.
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
public class MMGPSendApproveBP<T extends MMGPAbstractBill> extends MMGPAbstractPfBP<T> {

    /**
     * ���췽��.
     * 
     * @param point
     *        ��չ��
     */
    public MMGPSendApproveBP(IPluginPoint point) {
        super(point);
    }

    /**
     * �������.
     * 
     * @param clientFullVOs
     *        �ͻ���ȫVO
     * @param originBills
     *        ԭʼVO
     * @return �����VO
     * @throws BusinessException
     *         the business exception
     */
    public T[] sendApprove(T[] clientFullVOs,
                           T[] originBills) throws BusinessException {

        return super.doAction(clientFullVOs, originBills);
    }

    protected void addBeforeRule(T[] clientFullVOs,
                                 T[] originBills) {
        super.addBeforeRule(clientFullVOs, originBills);
    };

}
