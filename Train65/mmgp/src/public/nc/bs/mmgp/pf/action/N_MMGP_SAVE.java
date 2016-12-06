package nc.bs.mmgp.pf.action;

import nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b>�ύ�ű�����</b>
 * <p>
 * �ύ�ű������ýӿڵ��ύ����.
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
public abstract class N_MMGP_SAVE<T extends MMGPAbstractBill> extends MMGPGrandAbstractPfAction<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected T[] processBP(Object userObj,
                            T[] clientFullVOs,
                            T[] originBills) {

        T[] bills = null;
        try {
            IMmgpPfOperateService service = getOperateService();
            bills = (T[]) service.billSendApprove(clientFullVOs, originBills);
            return bills;
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return bills;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected CompareAroundProcesser<T> getCompareAroundProcesserWithRules(Object userObj) {
        CompareAroundProcesser<T> innerProcesser = super.getCompareAroundProcesserWithRules(userObj);
        innerProcesser.addBeforeRule(new CommitStatusCheckRule());
        return innerProcesser;
    }

}
