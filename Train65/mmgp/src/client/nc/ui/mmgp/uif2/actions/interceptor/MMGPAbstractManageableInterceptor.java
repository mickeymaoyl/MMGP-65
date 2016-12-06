package nc.ui.mmgp.uif2.actions.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.ml.NCLangRes;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.uif2.LoginContext;
import nc.vo.util.ManageModeUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Aug 13, 2013
 * @author wangweir
 */
public abstract class MMGPAbstractManageableInterceptor implements ActionInterceptor {

    @Override
    public boolean beforeDoAction(Action action,
                                  ActionEvent e) {
        Object[] objs = getSelectOperaDatas();

        if (MMArrayUtil.isEmpty(objs)) {
            return true;
        }

        for (Object object : objs) {
            if (!ManageModeUtil.manageable(object, getContext())) {
                String msg = ManageModeUtil.getDisManageableMsg(getContext().getNodeType());
                ShowStatusBarMsgUtil.showErrorMsg(
                    NCLangRes.getInstance().getStrByID("uif2", "ExceptionHandlerWithDLG-000000")/* 错误 */,
                    msg,
                    getContext());
                return false;
            }
        }
        return true;

    }

    protected abstract LoginContext getContext();

    protected abstract Object[] getSelectOperaDatas();

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

}
