package nc.impl.mmgp.uif2.rule.event;

import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.vo.pub.BusinessException;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� May 15, 2013
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
