package nc.impl.mmgp.uif2.rule.event;

import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 15, 2013
 * @author wangweir
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractEventRule implements IRule {

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.IRule#process(E[])
     */
    @Override
    public void process(Object[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }
        try {
            BDCommonEventUtil eventUtil = initBDCommonEventUtil(vos);
            fireEvent(vos, eventUtil);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
    }

    /**
     * @param vos
     * @param eventUtil
     * @throws BusinessException
     */
    protected abstract void fireEvent(Object[] vos,
                                      BDCommonEventUtil eventUtil) throws BusinessException;

    /**
     * @param vos
     * @return
     * @throws BusinessException
     */
    protected BDCommonEventUtil initBDCommonEventUtil(Object[] vos) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(this.getBeanID(vos));
        return eventUtil;
    }

    /**
     * Gets the bean id.
     * 
     * @param obj
     *        the obj
     * @return the bean id
     * @throws BusinessException
     *         the business exception
     */
    protected String getBeanID(Object[] vos) throws BusinessException {
        IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(vos[0].getClass().getName());
        return bean.getID();
    }

}
