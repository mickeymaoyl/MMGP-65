package nc.ui.mmgp.uif2.actions.batch.interceptor;

import nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractManageableInterceptor;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 批维护 组织权限控制，满足UE规范，不是用谈对话框提示
 * </p>
 * 
 * @since 创建日期 May 16, 2013
 * @author wangweir
 */
public class MMGPBatchManageableInterceptor extends MMGPAbstractManageableInterceptor {

    private BatchBillTableModel model;

    /**
     * @return the model
     */
    public BatchBillTableModel getModel() {
        return model;
    }

    /**
     * @param model
     *        the model to set
     */
    public void setModel(BatchBillTableModel model) {
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
