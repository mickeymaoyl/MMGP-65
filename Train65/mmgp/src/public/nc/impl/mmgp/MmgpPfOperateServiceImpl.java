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
 * <b> ���ݺ�̨Ĭ�ϻ��� </b>
 * <p>
 * ʵ�ֶԵ��ݵ��������޸ġ����桢�ύ���ջء���˺�����ͨ�÷�����</br> ����ʵ�ֽӿڼ̳д�������û�������ҵ��ֻ��Ҫʵ�ֻ�ȡ����������չ�㷽����</br> ���������ҵ������д��</br>
 * </p>
 * .
 * 
 * @since NC V6.3 ��������:2013-5-7
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
     * ��������BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
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
     * �޸Ĳ���BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
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
     * ɾ������BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
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
     * �������BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
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
     * �ջز���BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
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
     * ��������BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
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
     * �������BP����չ��.
     * 
     * @return IPluginPoint ҵ�������չ��
     */
    protected abstract IPluginPoint getBpUnApprovePoint();

}
