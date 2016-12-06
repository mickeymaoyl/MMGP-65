package nc.bs.mmgp.pf.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.pf.IWorkflowAdmin;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.wfengine.definition.WorkflowTypeEnum;

/**
 * <b>�ջؽű�����</b>
 * <p>
 * �ջؽű������ýӿڵ��ջز���.
 * </p>
 * <p>
 * ��������:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO����
 * @since NC V6.3
 * @author zhumh
 */
public abstract class N_MMGP_UNSAVE<T extends MMGPAbstractBill> extends MMGPGrandAbstractPfAction<T> {

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
            bills = (T[]) service.billUnSendApprove(clientFullVOs, originBills);
            return bills;
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return bills;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected CompareAroundProcesser<T> getCompareAroundProcesserWithRules(Object userObj) {
        CompareAroundProcesser<T> innerProcesser = super.getCompareAroundProcesserWithRules(userObj);
        innerProcesser.addBeforeRule(new UncommitStatusCheckRule());
        return innerProcesser;
    }

    /*
     * (non-Javadoc)
     * @see nc.bs.mmgp.pf.action.grand.MMGPGrandAbstractPfAction#beforeProcess(nc.vo.pub.compiler.PfParameterVO)
     */
    @Override
    protected void beforeProcess(PfParameterVO paraVo) {
        super.beforeProcess(paraVo);

        if (hasApproveFlowStartup(paraVo)) {
            checkApproveFlowRunning(paraVo);
        }
    }

    /**
     * �������ʵ���Ƿ񱻹���
     * 
     * @param paraVo
     */
    protected void checkApproveFlowRunning(PfParameterVO paraVo) {
        UFBoolean hasRunningInstance =
                NCLocator
                    .getInstance()
                    .lookup(IWorkflowAdmin.class)
                    .hasRunningProcess(
                        paraVo.getM_billVersionPK(),
                        paraVo.m_billType,
                        WorkflowTypeEnum.Approveflow.getStrValue());
        if (hasRunningInstance != null && !hasRunningInstance.booleanValue()) {
            ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "pfworkflow",
                "EngineService-0004")/*
                                      * �õ�������������ʵ���ѱ����� ��
                                      */);
        }
    }

    /**
     * �жϸõ����Ƿ����������ʵ��
     * 
     * @param paraVo
     * @return
     */
    protected boolean hasApproveFlowStartup(PfParameterVO paraVo) {
        boolean hasApproveFlowStartup = false;
        try {
            hasApproveFlowStartup =
                    NCLocator
                        .getInstance()
                        .lookup(IPFWorkflowQry.class)
                        .isApproveFlowStartup(paraVo.getM_billVersionPK(), paraVo.m_billType);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return hasApproveFlowStartup;
    }
}
