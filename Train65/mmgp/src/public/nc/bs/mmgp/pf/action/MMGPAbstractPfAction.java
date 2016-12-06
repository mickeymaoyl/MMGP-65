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
 * <b>�����ű�����</b>
 * <p>
 * ��ȡ�ű���չ��ͽӿڷ���ʵ��. �ӿڷ�����Ҫ�̳�nc.itf.mmgp.IMmgpPfOperateService�ӿ�
 * </p>
 * <p>
 * ��������:2013-5-9
 * </p>
 * 
 * @param <T>
 *        �ۺ�VO
 * @since NC V6.3
 * @author zhumh
 * @see nc.itf.mmgp.IMmgpPfOperateService
 * @see MMGPGrandAbstractPfAction ֧�������������ӱ��Ժ�ʹ�����
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
     * ��ȡ�ű������ӿڷ���.
     * 
     * @return IMmgpPfOperateService �ű������ӿڷ���
     */
    protected IMmgpPfOperateService getOperateService() {
        return NCLocator.getInstance().lookup(getOperateServiceClass());
    }

    /**
     * ��ȡ�ű������ӿڷ���ʵ�ֽӿ�����.
     * 
     * @param <E>
     *        the element type
     * @return Class<E extends IMmgpPfOperateService> �ӿ���
     */
    protected abstract <E extends IMmgpPfOperateService> Class<E> getOperateServiceClass();

    /**
     * ��ȡ�ű�������չ��.
     * 
     * @return IPluginPoint �ű���չ��
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
