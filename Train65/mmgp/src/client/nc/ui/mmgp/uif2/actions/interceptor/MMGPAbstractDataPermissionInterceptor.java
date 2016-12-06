package nc.ui.mmgp.uif2.actions.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.ml.NCLangRes;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jul 16, 2013
 * @author wangweir
 */
public abstract class MMGPAbstractDataPermissionInterceptor implements ActionInterceptor {

    private String mdOperateCode = null;

    private String operateCode = null;

    private String resourceCode = null;

    /**
     * 
     */
    public MMGPAbstractDataPermissionInterceptor() {
        super();
    }

    @Override
    public boolean beforeDoAction(Action action,
                                  ActionEvent e) {
        if (hasNoPermissionData()) {
            ShowStatusBarMsgUtil.showErrorMsg(
                NCLangRes.getInstance().getStrByID("uif2", "ExceptionHandlerWithDLG-000000")/* 错误 */,
                IShowMsgConstant.getHasNoPermissionDataInfo(),
                getContext());
            return false;
        }
        return true;
    }

    /**
     * @return
     */
    protected boolean hasNoPermissionData() {
        if ((MMStringUtil.isEmptyWithTrim(getOperateCode()) && MMStringUtil.isEmptyWithTrim(getMdOperateCode()))
            || MMStringUtil.isEmptyWithTrim(getResourceCode())) {
            return false;
        }

        Object obj = getSelectedOperaDatas();
        if (obj == null) {
            return false;
        }

        Object[] unDataPermissionData =
                CheckDataPermissionUtil.getUnDataPermissionData(
                    operateCode,
                    mdOperateCode,
                    resourceCode,
                    getContext(),
                    obj);
        return !MMArrayUtil.isEmpty(unDataPermissionData);
    }

    protected abstract LoginContext getContext();

    protected abstract Object[] getSelectedOperaDatas();

    @Override
    public void afterDoActionSuccessed(Action action,
                                       ActionEvent e) {

    }

    @Override
    public boolean afterDoActionFailed(Action action,
                                       ActionEvent e,
                                       Throwable ex) {
        return true;
    }

    /**
     * @return the mdOperateCode
     */
    public String getMdOperateCode() {
        return mdOperateCode;
    }

    /**
     * @param mdOperateCode
     *        the mdOperateCode to set
     */
    public void setMdOperateCode(String mdOperateCode) {
        this.mdOperateCode = mdOperateCode;
    }

    /**
     * @return the operateCode
     */
    public String getOperateCode() {
        return operateCode;
    }

    /**
     * @param operateCode
     *        the operateCode to set
     */
    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    /**
     * @return the resourceCode
     */
    public String getResourceCode() {
        return resourceCode;
    }

    /**
     * @param resourceCode
     *        the resourceCode to set
     */
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

}
