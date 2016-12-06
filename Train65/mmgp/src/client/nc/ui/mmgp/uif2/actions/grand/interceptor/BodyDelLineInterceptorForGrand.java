package nc.ui.mmgp.uif2.actions.grand.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.ui.uif2.actions.ActionInterceptor;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 29, 2013
 * @author wangweir
 */
public class BodyDelLineInterceptorForGrand implements ActionInterceptor {

    private MainGrandModel mainGrandModel;

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.actions.ActionInterceptor#beforeDoAction(javax.swing.Action, java.awt.event.ActionEvent)
     */
    @Override
    public boolean beforeDoAction(Action action,
                                  ActionEvent e) {
        /* Sep 29, 2013 wangweir 删除行时将空行过滤的标志位设置成false Begin */
        this.getMainGrandModel().setFlag(false);
        /* Sep 29, 2013 wangweir End */
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
     * @return the mainGrandModel
     */
    public MainGrandModel getMainGrandModel() {
        return mainGrandModel;
    }

    /**
     * @param mainGrandModel
     *        the mainGrandModel to set
     */
    public void setMainGrandModel(MainGrandModel mainGrandModel) {
        this.mainGrandModel = mainGrandModel;
    }

}
