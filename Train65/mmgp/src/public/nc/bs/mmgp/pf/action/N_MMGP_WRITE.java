package nc.bs.mmgp.pf.action;

import nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction;
import nc.bs.pubapp.pub.rule.BillUpdateStatusCheckRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b>保存脚本父类</b>
 * <p>
 * 保存脚本，调用接口的保存操作.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO类型
 * @since NC V6.3
 * @author zhumh
 */
public abstract class N_MMGP_WRITE<T extends MMGPAbstractBill> extends MMGPGrandAbstractPfAction<T> {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected CompareAroundProcesser<T> getCompareAroundProcesserWithRules(Object userObj) {

        CompareAroundProcesser<T> processer = null;
        T[] clientFullVOs = (T[]) this.getVos();
        if (StringUtil.isEmptyWithTrim(clientFullVOs[0].getParent().getPrimaryKey())) {
            processer = new CompareAroundProcesser<T>(getInsertPoint());
        } else {
            processer = new CompareAroundProcesser<T>(getUpdatePoint());
            processer.addBeforeRule(new BillUpdateStatusCheckRule());
        }
        return processer;
    }

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
            if (StringUtil.isEmptyWithTrim(clientFullVOs[0].getParent().getPrimaryKey())) {
                bills = (T[]) service.billInsert(clientFullVOs);
            } else {
                bills = (T[]) service.billUpdate(clientFullVOs, originBills);
            }
            return bills;
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return bills;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IPluginPoint getScriptPoint() {
        return null;
    }

    /**
     * 获取新增脚本扩展点.
     * 
     * @return IPluginPoint 新增脚本扩展点
     */
    protected abstract IPluginPoint getInsertPoint();

    /**
     * 获取修改脚本扩展点.
     * 
     * @return IPluginPoint 修改脚本扩展点
     */
    protected abstract IPluginPoint getUpdatePoint();

    /*
     * (non-Javadoc)
     * @see nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction#getOperateServiceClass()
     */
    @Override
    protected Class< ? extends IMmgpPfOperateService> getOperateServiceClass() {
        // TODO Auto-generated method stub
        return null;
    }

}
