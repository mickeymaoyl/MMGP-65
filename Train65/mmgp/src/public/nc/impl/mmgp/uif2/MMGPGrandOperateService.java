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
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙持久化实现类
 * </p>
 * 
 * @since 创建日期 May 20, 2013
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
     * 初始化插入操作的Rule
     * 
     * @param insertBP
     */
    protected void initInsertRule(GrandBillInsertBP<IBill> insertBP) {
        
    }

    /**
     * 给定插入扩展点可以为空
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
            // 加锁 + 检查ts
            MMGPGrandBillTransferTool<IBill> transTool = new MMGPGrandBillTransferTool<IBill>(value);
            // 补全前台VO
            IBill[] fullBills = transTool.getClientFullInfoBill();
            // 获得修改前vo
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
     * 初始化更新操作的Rule
     * 
     * @param updateBP
     */
    protected void initUpdateRule(GrandBillUpdateBP updateBP) {

    }

    /**
     * 给定更新扩展点可以为空
     * 
     * @return 给定插入点可以为空
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
            // 加锁 + 检查ts
            MMGPGrandBillTransferTool<IBill> transTool = new MMGPGrandBillTransferTool<IBill>(value);
            // 补全前台VO
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
     * 初始化删除规则
     * 
     * @param deleteBP
     */
    protected void initDeleteRule(GrandBillDeleteBP deleteBP) {

    }

    /**
     * 给定删除扩展点可以为空
     * 
     * @return
     */
    protected IPluginPoint getDeletePluginPoint() {
        return null;
    }

}
