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
public class InsertAfterEventRule extends AbstractEventRule {

    /**
     * @param vos
     * @param eventUtil
     * @throws BusinessException
     */
    protected void fireEvent(Object[] vos,
                             BDCommonEventUtil eventUtil) throws BusinessException {
        eventUtil.dispatchInsertAfterEvent(vos);
    }

}
