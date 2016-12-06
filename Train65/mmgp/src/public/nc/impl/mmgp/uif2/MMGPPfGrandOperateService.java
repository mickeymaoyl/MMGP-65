package nc.impl.mmgp.uif2;

import nc.bs.pubapp.pub.rule.BillCodeCheckRule;
import nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule;
import nc.bs.pubapp.pub.rule.BillUpdateStatusCheckRule;
import nc.bs.pubapp.pub.rule.CreateBillCodeRule;
import nc.bs.pubapp.pub.rule.ReturnBillCodeRule;
import nc.bs.pubapp.pub.rule.UpdateBillCodeRule;
import nc.impl.mmgp.MmgpPfOperateServiceImpl;
import nc.impl.mmgp.uif2.grand.bp.GrandBillDeleteBP;
import nc.impl.mmgp.uif2.grand.bp.GrandBillInsertBP;
import nc.impl.mmgp.uif2.grand.bp.GrandBillUpdateBP;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.uif2.IMMGPPfGrandOperateService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBillTransferTool;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class MMGPPfGrandOperateService extends MmgpPfOperateServiceImpl implements IMMGPPfGrandOperateService {

    @Override
    public <T extends MMGPAbstractBill> T[] billInsert(T[] bills) throws BusinessException {

        try {
            if (MMArrayUtil.isEmpty(bills)) {
                return null;
            }

            GrandBillInsertBP<T> bp = getGrandInsertBp(bills);
            return bp.insert(bills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * @param fullBills
     * @return
     */
    protected <T extends MMGPAbstractBill> GrandBillInsertBP<T> getGrandInsertBp(T[] fullBills) {
        GrandBillInsertBP<T> insertBP = new GrandBillInsertBP<T>(this.getBpInsertPoint());
        this.initInsertRule(insertBP, fullBills);
        return insertBP;
    }

    /**
     * ��ʼ�����������Rule
     * 
     * @param insertBP
     * @param fullBills
     */
    @SuppressWarnings("rawtypes")
    protected void initInsertRule(GrandBillInsertBP insertBP,
                                  IBill[] fullBills) {
        // ���ݺŹ���Rule
        // ��ȡ����������ֶ���Ϣ
        IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(fullBills[0].getParent(), IFlowBizItf.class);
        String billtype = flowInfo.getBilltype();
        String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);

        // �������ݺŵĹ���
        IRule rule = new CreateBillCodeRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
        insertBP.addBeforeRule(rule);

        // AfterRule****************************************************
        // ���ݺ�У�����
        rule = new BillCodeCheckRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
        insertBP.addAfterRule(rule);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MMGPAbstractBill> T[] billUpdate(T[] clientFullVOs,
                                                       T[] originBills) throws BusinessException {
        try {
            if (MMArrayUtil.isEmpty(clientFullVOs)) {
                return null;
            }
            // ���� + ���ts
            MMGPGrandBillTransferTool<IBill> transTool = new MMGPGrandBillTransferTool<IBill>(clientFullVOs);
            // // ��ȫǰ̨VO
            IBill[] fullBills = transTool.getClientFullInfoBill();
            // // ����޸�ǰvo
            IBill[] newOriginBills = transTool.getOriginBills();

            GrandBillUpdateBP bp = getGrandUpdateBp(fullBills, newOriginBills);
            return (T[]) bp.update(fullBills, newOriginBills);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * @param newOriginBills
     * @param fullBills
     * @return
     */
    protected GrandBillUpdateBP getGrandUpdateBp(IBill[] fullBills,
                                                 IBill[] newOriginBills) {
        GrandBillUpdateBP insertBP = new GrandBillUpdateBP(this.getBpUpdatePoint());
        this.initUpdateRule(insertBP, fullBills, newOriginBills);
        return insertBP;
    }

    /**
     * ��ʼ�����²�����Rule
     * 
     * @param updateBP
     * @param newOriginBills
     * @param fullBills
     */
    @SuppressWarnings("rawtypes")
    protected void initUpdateRule(GrandBillUpdateBP updateBP,
                                  IBill[] fullBills,
                                  IBill[] newOriginBills) {
        // AfterRule****************************************************
        // ��ȡ����������ֶ���Ϣ
        IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(fullBills[0].getParent(), IFlowBizItf.class);
        String billtype = flowInfo.getBilltype();
        String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
        // ���ݺ�У�����
        IRule rule = new BillCodeCheckRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
        updateBP.addAfterRule(rule);

        // ���µ��ݺŵĹ���
        ICompareRule compareRule =
                new UpdateBillCodeRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
        updateBP.addBeforeRule(compareRule);

        // ����״̬У��
        BillUpdateStatusCheckRule billStatusCheckRule = new BillUpdateStatusCheckRule();
        updateBP.addBeforeRule(billStatusCheckRule);
    }

    @Override
    public <T extends MMGPAbstractBill> void billDelete(T[] bills) throws BusinessException {
        try {
            if (MMArrayUtil.isEmpty(bills)) {
                return;
            }
            // ���� + ���ts
            MMGPGrandBillTransferTool<IBill> transTool = new MMGPGrandBillTransferTool<IBill>(bills);
            // ��ȫǰ̨VO
            IBill[] fullBills = transTool.getClientFullInfoBill();
            GrandBillDeleteBP deleteBP = this.getGrandDeleteBP(fullBills);
            deleteBP.delete(fullBills);
        } catch (Exception ex) {
            ExceptionUtils.marsh(ex);
        }
    }

    /**
     * @param fullBills
     * @return
     */
    protected GrandBillDeleteBP getGrandDeleteBP(IBill[] fullBills) {
        GrandBillDeleteBP deleteBP = new GrandBillDeleteBP(this.getBpDeletePoint());
        this.initDeleteRule(deleteBP, fullBills);
        return deleteBP;
    }

    /**
     * ��ʼ��ɾ������
     * 
     * @param deleteBP
     * @param fullBills
     */
    @SuppressWarnings("rawtypes")
    protected void initDeleteRule(GrandBillDeleteBP deleteBP,
                                  IBill[] fullBills) {
        // ��ȡ����������ֶ���Ϣ
        IFlowBizItf flowInfo = PfMetadataTools.getBizItfImpl(fullBills[0].getParent(), IFlowBizItf.class);
        String billtype = flowInfo.getBilltype();
        String vbillcode = flowInfo.getColumnName(IFlowBizItf.ATTRIBUTE_BILLNO);
        // ���ݺŻ��˹���
        IRule rule = new ReturnBillCodeRule(billtype, vbillcode, MMGlobalConst.PK_GROUP, MMGlobalConst.PK_ORG);
        deleteBP.addBeforeRule(rule);

        BillDeleteStatusCheckRule billStatusCheckRule = new BillDeleteStatusCheckRule();
        deleteBP.addBeforeRule(billStatusCheckRule);
    }

    @Override
    protected IPluginPoint getBpInsertPoint() {
        return null;
    }

    @Override
    protected IPluginPoint getBpUpdatePoint() {
        return null;
    }

    @Override
    protected IPluginPoint getBpDeletePoint() {
        return null;
    }

    @Override
    protected IPluginPoint getBpSendApprovePoint() {
        return null;
    }

    @Override
    protected IPluginPoint getBpUnSendApprovePoint() {
        return null;
    }

    @Override
    protected IPluginPoint getBpApprovePoint() {
        return null;
    }

    @Override
    protected IPluginPoint getBpUnApprovePoint() {
        return null;
    }

}
