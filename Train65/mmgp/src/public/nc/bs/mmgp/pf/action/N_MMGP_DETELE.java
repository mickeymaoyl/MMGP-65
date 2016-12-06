package nc.bs.mmgp.pf.action;

import nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b>审核脚本父类</b>
 * <p>
 * 删除脚本，调用接口的删除操作.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO类型
 * @since NC V6.3
 * @author zhumh
 * @deprecated 命名错误，请大家使用N_MMGP_DELETE
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
