package nc.impl.mmgp.uif2.rule.event;

import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.vo.pub.BusinessException;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 15, 2013
 * @author wangweir
 */
public class UpdateBeforeEventRule extends AbstractEventRule {

    /*
     * (non-Javadoc)
     * @see nc.impl.mmgp.uif2.rule.event.AbstractEventRule#fireEvent(java.lang.Object[],
     * nc.bs.businessevent.bd.BDCommonEventUtil)
     */
    @Override
    protected void fireEvent(Object[] vos,
                             BDCommonEventUtil eventUtil) throws BusinessException {
        eventUtil.dispatchUpdateBeforeEvent(vos);
    }

}
