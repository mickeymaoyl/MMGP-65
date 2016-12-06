package nc.bs.mmgp.pf.action.grand;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pf.action.PfScriptData;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBillTransferTool;
import nc.vo.mmgp.util.grand.MMGPGrandBusiEntityUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * 脚本基类：所有脚本都可以继承此类 修改：增加beforeProcess、changeProcessorAfterRule方法， execPf方法增加返回值 <b> 简要描述功能 </b>
 * <p>
 * 支持孙脚本执行抽象类,主子孙请继承这个
 * </p>
 *
 * @see AbstractPfAction
 * @since 创建日期 May 22, 2013
 * @author wangweir
 */
public abstract class MMGPGrandAbstractPfAction<T extends IBill> extends AbstractCompiler2 {

    @Override
    public final String getCodeRemark() {
        // 显示给用户，不允许在产品中修改脚本
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0050")/*@res "脚本不允许修改，否则后果自负"*/;/* -=notranslate=- */
    }

    @Override
    public final Object runComClass(PfParameterVO paraVo) throws BusinessException {
        try {
            @SuppressWarnings("unused")
            Object obj = paraVo.m_userObj;

            beforeProcess(paraVo);

            super.m_tmpVo = paraVo;

            @SuppressWarnings("unchecked")
            T[] clientFullVOs = (T[]) this.getVos();
            if (MMArrayUtil.isEmpty(clientFullVOs)) {
                return null;
            }

            List<T[]> billTransferResult = billFtransfer(clientFullVOs);

            T[] originBills = billTransferResult.get(0);
            clientFullVOs = billTransferResult.get(1);

            if (clientFullVOs != null && clientFullVOs.length > 0) {
                paraVo.m_preValueVo = (AggregatedValueObject) clientFullVOs[0];
                paraVo.m_preValueVos = (AggregatedValueObject[]) clientFullVOs;
            }

            PfScriptData<T> scriptData = new PfScriptData<T>();
            // scriptData.setPfParameterVO(paraVo);
            scriptData.setOriginBills(originBills);
            scriptData.setClientFullVOs(clientFullVOs);

            CompareAroundProcesser<T> processor = this.getCompareAroundProcesserWithRules(paraVo.m_userObj);

            // 执行持久化前规则
            if (processor != null) {
                processor.before(scriptData.getClientFullVOs(), scriptData.getOriginBills());
            }

            Object execPfReturnObj = this.execPf(paraVo);

            T[] bills = this.processBP(paraVo.m_userObj, scriptData.getClientFullVOs(), scriptData.getOriginBills());

            this.changeProcessorAfterRule(
                processor,
                scriptData.getClientFullVOs(),
                scriptData.getOriginBills(),
                execPfReturnObj);

            T[] returnBills = null;
            if (processor != null) {
                returnBills = processor.after(bills, scriptData.getOriginBills());
            }
            AggregatedValueObject[] aggvos = (AggregatedValueObject[]) returnBills;
            this.setVos(aggvos);
            this.setVo(aggvos == null ? null : (aggvos.length > 0 ? aggvos[0] : null));

            this.flowRelateDispose(paraVo);

            return returnBills;
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return null;
        }
    }

    /**
     * @param clientFullVOs
     * @return
     */
    protected List<T[]> billFtransfer(T[] clientFullVOs) {

        List<T[]> billTransferResult = new ArrayList<T[]>(2);
        if (isGrandBill(clientFullVOs)) {
            MMGPGrandBillTransferTool<T> tool = new MMGPGrandBillTransferTool<T>(clientFullVOs);
            billTransferResult.add(tool.getOriginBills());
            billTransferResult.add(tool.getClientFullInfoBill());
        } else {
            BillTransferTool<T> tool = new BillTransferTool<T>(clientFullVOs);
            billTransferResult.add(tool.getOriginBills());
            billTransferResult.add(tool.getClientFullInfoBill());
        }

        return billTransferResult;
    }

    /**
     * @param clientFullVOs
     */
    protected boolean isGrandBill(T[] clientFullVOs) {
        return MMGPGrandBusiEntityUtil.isGrandBill(clientFullVOs);
    }

    /**
     * 审批操作前做一些检查操作
     *
     * @param paraVo
     */
    protected void beforeProcess(PfParameterVO paraVo) {
    }

    /**
     * 根据业务修改校验规则
     *
     * @param processor
     * @param clientFullVOs
     */
    protected void changeProcessorAfterRule(CompareAroundProcesser<T> processor,
                                            T[] clientFullVOs,
                                            T[] originBills,
                                            Object execPfReturnObj) {
    }

    /**
     * 流程相关的处理
     *
     * @param paraVo
     */
    protected void flowRelateDispose(PfParameterVO paraVo) {
        // nothing
    }

    protected abstract T[] processBP(Object userObj,
                                     T[] clientFullVOs,
                                     T[] originBills);

    private Object execPf(PfParameterVO paraVo) {
        try {
            if (PfUtilBaseTools.isRecallAction(paraVo.m_actionName, paraVo.m_billType)
                || PfUtilBaseTools.isUnSaveAction(paraVo.m_actionName, paraVo.m_billType)) {
                this.procRecallFlow(paraVo);
                return null;
            } else if (PfUtilBaseTools.isUnapproveAction(paraVo.m_actionName, paraVo.m_billType)
                || PfUtilBaseTools.isRollbackAction(paraVo.m_actionName, paraVo.m_billType)) {
                return this.procUnApproveFlow(paraVo);
            } else if (PfUtilBaseTools.isSaveAction(paraVo.m_actionName, paraVo.m_billType)) {
                // 流程平台只有提交的时候不回写状态，所以这里要补上
                // 审批状态回写
                for (AggregatedValueObject aggVO : paraVo.m_preValueVos) {
                    IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(aggVO, IFlowBizItf.class);
                    fbi.setApproveStatus(Integer.valueOf(IPfRetCheckInfo.COMMIT));
                }
                return null;
            } else if (PfUtilBaseTools.isApproveAction(paraVo.m_actionName, paraVo.m_billType)) {
                return this.procFlowBacth(paraVo);
            }
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
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
     * @return Class<? extends IMmgpPfOperateService> 接口类
     */
    protected abstract Class< ? extends IMmgpPfOperateService> getOperateServiceClass();

    /**
     * 获取脚本操作扩展点.
     *
     * @return IPluginPoint 脚本扩展点
     */
    protected abstract IPluginPoint getScriptPoint();

}