package nc.impl.mmgp.uif2;

import nc.impl.mmgp.uif2.grand.bp.GrandBillDeleteBP;
import nc.impl.mmgp.uif2.grand.bp.GrandBillInsertBP;
import nc.impl.mmgp.uif2.grand.bp.GrandBillUpdateBP;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.uif2.IMMGPGrandOperateService;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBillTransferTool;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ������־û�ʵ����
 * </p>
 * 
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandOperateService implements IMMGPGrandOperateService {

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.IDataOperationService#insert(nc.vo.pubapp.pattern.model.entity.bill.IBill[])
     */
    @Override
    public IBill[] insert(IBill[] value) throws BusinessException {

        try {
            if (MMArrayUtil.isEmpty(value)) {
                return new IBill[0];
            }

            GrandBillInsertBP<IBill> bp = getGrandInsertBp();

            return bp.insert(value);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return new IBill[0];
    }

    /**
     * @return
     */
    protected GrandBillInsertBP<IBill> getGrandInsertBp() {
        GrandBillInsertBP<IBill> insertBP = new GrandBillInsertBP<IBill>(this.getInsertPluginPoint());
        this.initInsertRule(insertBP);
        return insertBP;
    }

    /**
     * ��ʼ�����������Rule
     * 
     * @param insertBP
     */
    protected void initInsertRule(GrandBillInsertBP<IBill> insertBP) {
        
    }

    /**
     * ����������չ�����Ϊ��
     * 
     * @return IPluginPoint
     */
    protected IPluginPoint getInsertPluginPoint() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.IDataOperationService#update(nc.vo.pubapp.pattern.model.entity.bill.IBill[])
     */
    @Override
    public IBill[] update(IBill[] value) throws BusinessException {

        try {
            if (MMArrayUtil.isEmpty(value)) {
                return null;
            }
            // ���� + ���ts
            MMGPGrandBillTransferTool<IBill> transTool = new MMGPGrandBillTransferTool<IBill>(value);
            // ��ȫǰ̨VO
            IBill[] fullBills = transTool.getClientFullInfoBill();
            // ����޸�ǰvo
            IBill[] originBills = transTool.getOriginBills();

            GrandBillUpdateBP bp = getGrandUpdateBp();
            return bp.update(fullBills, originBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * @return
     */
    protected GrandBillUpdateBP getGrandUpdateBp() {
        GrandBillUpdateBP insertBP = new GrandBillUpdateBP(this.getUpdatePluginPoint());
        this.initUpdateRule(insertBP);
        return insertBP;
    }

    /**
     * ��ʼ�����²�����Rule
     * 
     * @param updateBP
     */
    protected void initUpdateRule(GrandBillUpdateBP updateBP) {

    }

    /**
     * ����������չ�����Ϊ��
     * 
     * @return ������������Ϊ��
     */
    protected IPluginPoint getUpdatePluginPoint() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.IDataOperationService#delete(nc.vo.pubapp.pattern.model.entity.bill.IBill[])
     */
    @Override
    public void delete(IBill[] value) throws BusinessException {
        try {
            if (MMArrayUtil.isEmpty(value)) {
                return;
            }
            // ���� + ���ts
            MMGPGrandBillTransferTool<IBill> transTool = new MMGPGrandBillTransferTool<IBill>(value);
            // ��ȫǰ̨VO
            IBill[] fullBills = transTool.getClientFullInfoBill();
            GrandBillDeleteBP deleteBP = this.getGrandDeleteBP();
            deleteBP.delete(fullBills);
        } catch (Exception ex) {
            ExceptionUtils.marsh(ex);
        }
    }

    /**
     * @return
     */
    protected GrandBillDeleteBP getGrandDeleteBP() {
        GrandBillDeleteBP deleteBP = new GrandBillDeleteBP(this.getDeletePluginPoint());
        this.initDeleteRule(deleteBP);
        return deleteBP;
    }

    /**
     * ��ʼ��ɾ������
     * 
     * @param deleteBP
     */
    protected void initDeleteRule(GrandBillDeleteBP deleteBP) {

    }

    /**
     * ����ɾ����չ�����Ϊ��
     * 
     * @return
     */
    protected IPluginPoint getDeletePluginPoint() {
        return null;
    }

}
