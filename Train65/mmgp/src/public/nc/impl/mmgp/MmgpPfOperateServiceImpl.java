package nc.impl.mmgp;

import nc.bs.mmgp.pf.bp.MMGPApproveBP;
import nc.bs.mmgp.pf.bp.MMGPDeleteBP;
import nc.bs.mmgp.pf.bp.MMGPInsertBP;
import nc.bs.mmgp.pf.bp.MMGPSendApproveBP;
import nc.bs.mmgp.pf.bp.MMGPUnApproveBP;
import nc.bs.mmgp.pf.bp.MMGPUnSendApproveBP;
import nc.bs.mmgp.pf.bp.MMGPUpdateBP;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 单据后台默认基类 </b>
 * <p>
 * 实现对单据的新增、修改、保存、提交、收回、审核和弃审通用方法，</br> 单据实现接口继承此类后，如果没有特殊的业务，只需要实现获取各操作的扩展点方法。</br> 如果有特殊业务，请重写。</br>
 * </p>
 * .
 * 
 * @since NC V6.3 创建日期:2013-5-7
 * @author zhumh
 */
public abstract class MmgpPfOperateServiceImpl implements IMmgpPfOperateService {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> T[] billInsert(T[] bills) throws BusinessException {
        try {
            MMGPInsertBP<T> bp = new MMGPInsertBP<T>(getBpInsertPoint());
            return bp.insert(bills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 新增操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpInsertPoint();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> T[] billUpdate(T[] clientFullVOs,
                                                       T[] originBills) throws BusinessException {
        try {
            MMGPUpdateBP<T> bp = new MMGPUpdateBP<T>(getBpUpdatePoint());
            return bp.update(clientFullVOs, originBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 修改操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpUpdatePoint();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> void billDelete(T[] bills) throws BusinessException {
        try {
            MMGPDeleteBP<T> bp = new MMGPDeleteBP<T>(getBpDeletePoint());
            bp.delete(bills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    /**
     * 删除操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpDeletePoint();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> T[] billSendApprove(T[] clientFullVOs,
                                                            T[] originBills) throws BusinessException {
        try {
            MMGPSendApproveBP<T> bp = new MMGPSendApproveBP<T>(getBpSendApprovePoint());
            return bp.sendApprove(clientFullVOs, originBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 送审操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpSendApprovePoint();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> T[] billUnSendApprove(T[] clientFullVOs,
                                                              T[] originBills) throws BusinessException {
        try {
            MMGPUnSendApproveBP<T> bp = new MMGPUnSendApproveBP<T>(getBpUnSendApprovePoint());
            return bp.unSendApprove(clientFullVOs, originBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 收回操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpUnSendApprovePoint();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> T[] billApprove(T[] clientFullVOs,
                                                        T[] originBills) throws BusinessException {
        try {
            MMGPApproveBP<T> bp = new MMGPApproveBP<T>(getBpApprovePoint());
            return bp.approve(clientFullVOs, originBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 审批操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpApprovePoint();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends MMGPAbstractBill> T[] billUnApprove(T[] clientFullVOs,
                                                          T[] originBills) throws BusinessException {
        try {
            MMGPUnApproveBP<T> bp = new MMGPUnApproveBP<T>(getBpUnApprovePoint());
            return bp.unApprove(clientFullVOs, originBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 弃审操作BP类扩展点.
     * 
     * @return IPluginPoint 业务规则扩展点
     */
    protected abstract IPluginPoint getBpUnApprovePoint();

}
