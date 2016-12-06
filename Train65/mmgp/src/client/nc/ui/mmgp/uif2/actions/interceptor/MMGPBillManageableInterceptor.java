package nc.ui.mmgp.uif2.actions.interceptor;

import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 组织权限控制，满足UE规范，不是用谈对话框提示
 * </p>
 * 
 * @since 2013.7.26
 * @author wangfan3
 */
public class MMGPBillManageableInterceptor extends MMGPAbstractManageableInterceptor {

    private BillManageModel model;

    /**
     * @return the model
     */
    public BillManageModel getModel() {
        return model;
    }

    /**
     * @param model
     *        the model to set
     */
    public void setModel(BillManageModel model) {
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractManageableInterceptor#getContext()
     */
    @Override
    protected LoginContext getContext() {
        return this.getModel().getContext();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractManageableInterceptor#getSelectOperaDatas()
     */
    @Override
    protected Object[] getSelectOperaDatas() {
        return this.getModel().getSelectedOperaDatas();
    }
}
