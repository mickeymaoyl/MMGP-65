package nc.ui.mmgp.uif2.actions.batch.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.actions.util.ActionEnableCheckUtils;
import nc.ui.pubapp.uif2app.view.BaseOrgPanel;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.vo.pubapp.pattern.log.Log;
import nc.vo.uif2.LoginContext;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���޸Ľ���У�鰴ť�Ƿ����
 * </p>
 * 
 * @since �������� Jul 24, 2013
 * @author wangweir
 */
public class MMGPBatchCheckActionEnableInOrgNode implements ActionInterceptor {

    private BaseOrgPanel orgPanel;

    private LoginContext context;

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.actions.ActionInterceptor#beforeDoAction(javax.swing.Action, java.awt.event.ActionEvent)
     */
    @Override
    public boolean beforeDoAction(Action action,
                                  ActionEvent e) {
        if (this.getOrgPanel() != null) {
            getOrgPanel().stopEditing();
        }
        try {
            ActionEnableCheckUtils.checkActionEnableInOrgNode(getContext());
        } catch (Exception e1) {
            Log.error(e1);
            ShowStatusBarMsgUtil.showErrorMsg(
                NCLangRes.getInstance().getStrByID("uif2", "ExceptionHandlerWithDLG-000000")/* ���� */,
                e1.getMessage(),
                this.getContext());
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.actions.ActionInterceptor#afterDoActionSuccessed(javax.swing.Action, java.awt.event.ActionEvent)
     */
    @Override
    public void afterDoActionSuccessed(Action action,
                                       ActionEvent e) {

    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.actions.ActionInterceptor#afterDoActionFailed(javax.swing.Action, java.awt.event.ActionEvent,
     * java.lang.Throwable)
     */
    @Override
    public boolean afterDoActionFailed(Action action,
                                       ActionEvent e,
                                       Throwable ex) {
        return true;
    }

    /**
     * @return the orgPanel
     */
    public BaseOrgPanel getOrgPanel() {
        return orgPanel;
    }

    /**
     * @param orgPanel
     *        the orgPanel to set
     */
    public void setOrgPanel(BaseOrgPanel orgPanel) {
        this.orgPanel = orgPanel;
    }

    /**
     * @return the context
     */
    public LoginContext getContext() {
        return context;
    }

    /**
     * @param context
     *        the context to set
     */
    public void setContext(LoginContext context) {
        this.context = context;
    }

}
