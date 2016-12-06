package nc.bs.mmgp.pf.action;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b>动作脚本基类</b>
 * <p>
 * 获取脚本扩展点和接口服务实现. 接口服务需要继承nc.itf.mmgp.IMmgpPfOperateService接口
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @param <T>
 *        聚合VO
 * @since NC V6.3
 * @author zhumh
 * @see nc.itf.mmgp.IMmgpPfOperateService
 * @see MMGPGrandAbstractPfAction 支持主子孙表和主子表，以后使用这个
 */
@Deprecated
public abstract class MMGPAbstractPfAction<T extends IBill> extends AbstractPfAction<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected CompareAroundProcesser<T> getCompareAroundProcesserWithRules(Object userObj) {
        return new CompareAroundProcesser<T>(getScriptPoint());
    }

    /**
     * 获取脚本操作接口服务.
     * 
     * @return IMmgpPfOperateService 脚本操作接口服务
     */
    protected IMmgpPfOperateService getOperateService() {
        return NCLocator.getInstance().lookup(getOperateServiceClass());
    }

    /**
     * 获取脚本操作接口服务实现接口类型.
     * 
     * @param <E>
     *        the element type
     * @return Class<E extends IMmgpPfOperateService> 接口类
     */
    protected abstract <E extends IMmgpPfOperateService> Class<E> getOperateServiceClass();

    /**
     * 获取脚本操作扩展点.
     * 
     * @return IPluginPoint 脚本扩展点
     */
    protected abstract IPluginPoint getScriptPoint();

    /*
     * (non-Javadoc)
     * @see nc.bs.pub.compiler.AbstractCompiler#runClass(java.lang.String, java.lang.String, java.lang.String,
     * nc.vo.pub.compiler.PfParameterVO, java.util.Hashtable)
     */
    @Override
    public Object runClass(String className,
                           String method,
                           String parameter,
                           PfParameterVO paraVo,
                           @SuppressWarnings("rawtypes") Hashtable keyHas) throws BusinessException {
        try {
            return super.runClass(className, method, parameter, paraVo, keyHas);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return null;
        }
    }

}
