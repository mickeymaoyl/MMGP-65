package nc.bs.mmgp.pf.action;

import nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b>��˽ű�����</b>
 * <p>
 * ɾ���ű������ýӿڵ�ɾ������.
 * </p>
 * <p>
 * ��������:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO����
 * @since NC V6.3
 * @author zhumh
 * @deprecated ������������ʹ��N_MMGP_DELETE
 */
@Deprecated 
public abstract class N_MMGP_DETELE<T extends MMGPAbstractBill> extends MMGPGrandAbstractPfAction<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected T[] processBP(Object userObj,
                            T[] clientFullVOs,
                            T[] originBills) {

        try {
            IMmgpPfOperateService service = getOperateService();
            service.billDelete(clientFullVOs);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return clientFullVOs;
    }

}
