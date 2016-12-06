package nc.impl.mmgp.uif2;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.pf.bp.MMGPDeleteBP;
import nc.bs.mmgp.pf.bp.MMGPInsertBP;
import nc.bs.mmgp.pf.bp.MMGPUpdateBP;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.uif2.IMMGPCmnOperateBPService;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since 创建日期 Jun 28, 2013
 * @author wangweir
 */
public class MMGPCmnOperateBPService extends MMGPCmnOperateService implements IMMGPCmnOperateBPService {

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnDeleteBillDataFromDB(java.lang.Object)
     */
    @Override
    public void cmnDeleteBillDataFromDB(Object data) throws BusinessException {
        try {
            // 不支持物料删除，直接调用cmnDeleteBillData
            this.cmnDeleteBillData(data);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnDeleteBillData(java.lang.Object)
     */
    @Override
    public void cmnDeleteBillData(Object data) throws BusinessException {
        try {
            checkInput(data);
            // 加锁 + 检查ts
            BillTransferTool<MMGPAbstractBill> transTool =
                    new BillTransferTool<MMGPAbstractBill>(new MMGPAbstractBill[]{(MMGPAbstractBill) data });
            // 补全前台VO
            MMGPAbstractBill[] fullBills = transTool.getClientFullInfoBill();
            MMGPDeleteBP<MMGPAbstractBill> deleteBP = this.getDeleteBP();
            deleteBP.delete(fullBills);
        } catch (Exception ex) {
            ExceptionUtils.marsh(ex);
        }
    }

    /**
     * @return
     */
    protected MMGPDeleteBP<MMGPAbstractBill> getDeleteBP() {
        MMGPDeleteBP<MMGPAbstractBill> deleteBP = new MMGPDeleteBP<MMGPAbstractBill>(this.getDeletePluginPoint());
        return deleteBP;
    }

    /**
     * @return
     */
    protected IPluginPoint getDeletePluginPoint() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnSaveBillData(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T cmnSaveBillData(T data) throws BusinessException {
        try {
            checkInput(data);

            /* Oct 29, 2013 wangweir 查找子类实现的接口 Begin */
            Class<IMMGPCmnOperateBPService> interfaceToCall = IMMGPCmnOperateBPService.class;
            for (Class< ? > oneInterface : this.getClass().getInterfaces()) {
                if (IMMGPCmnOperateBPService.class.isAssignableFrom(oneInterface)) {
                    interfaceToCall = (Class<IMMGPCmnOperateBPService>) oneInterface;
                    break;
                }
            }
            /* Oct 29, 2013 wangweir End */

            if (this.isNewData(data)) {
                return NCLocator.getInstance().lookup(interfaceToCall).cmnInsertBillData(data);
            } else {
                return NCLocator.getInstance().lookup(interfaceToCall).cmnUpdateBillData(data);
            }
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * @param data
     * @return
     * @throws BusinessException
     */
    protected MMGPAbstractBill updateData(MMGPAbstractBill data) throws BusinessException {
        try {
            // 加锁 + 检查ts
            BillTransferTool<MMGPAbstractBill> transTool =
                    new BillTransferTool<MMGPAbstractBill>(new MMGPAbstractBill[]{data });
            // 补全前台VO
            MMGPAbstractBill[] fullBills = transTool.getClientFullInfoBill();
            // 获得修改前vo
            MMGPAbstractBill[] originBills = transTool.getOriginBills();

            MMGPUpdateBP<MMGPAbstractBill> bp = getUpdateBp();
            return bp.update(fullBills, originBills)[0];
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * @return
     */
    protected MMGPUpdateBP<MMGPAbstractBill> getUpdateBp() {
        MMGPUpdateBP<MMGPAbstractBill> updateBP = new MMGPUpdateBP<MMGPAbstractBill>(this.getUpdatePluginPoint());
        return updateBP;
    }

    /**
     * @return
     */
    protected IPluginPoint getUpdatePluginPoint() {
        return null;
    }

    /**
     * 检查是否为主子表并且是MMGPAbstractBill子类
     *
     * @param data
     * @throws BusinessException
     */
    protected <T> void checkInput(T data) throws BusinessException {
        if (!(data instanceof MMGPAbstractBill)) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0066")/*@res "此接口只支持主子表，MMGPAbstractBill子类"*/);
        }
    }

    /**
     * 新增插入数据
     *
     * @param data
     *        数据
     * @return
     * @throws BusinessException
     */
    protected MMGPAbstractBill insertData(MMGPAbstractBill data) throws BusinessException {
        try {
            MMGPInsertBP<MMGPAbstractBill> bp = getInsertBp();
            return bp.insert(MMArrayUtil.toArray(data))[0];
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * @return
     */
    protected MMGPInsertBP<MMGPAbstractBill> getInsertBp() {
        MMGPInsertBP<MMGPAbstractBill> insertBP = new MMGPInsertBP<MMGPAbstractBill>(this.getInsertPluginPoint());
        return insertBP;
    }

    /**
     * @return
     */
    protected IPluginPoint getInsertPluginPoint() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnInsertBillData(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T cmnInsertBillData(T data) throws BusinessException {
        try {
            return (T) this.insertData((MMGPAbstractBill) data);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnUpdateBillData(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T cmnUpdateBillData(T data) throws BusinessException {
        try {
            return (T) this.updateData((MMGPAbstractBill) data);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

}