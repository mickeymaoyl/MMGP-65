package nc.ui.mmgp.uif2.actions.batch.interceptor;

import nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractDataPermissionInterceptor;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 数据权限控制拦截器
 * </p>
 * 
 * @since 创建日期 May 16, 2013
 * @author wangweir
 */
public class MMGPBatchDataPermissionInterceptor extends MMGPAbstractDataPermissionInterceptor {

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
     * @see nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractDataPermissionInterceptor#getContext()
     */
    @Override
    protected LoginContext getContext() {
        return this.getModel().getContext();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractDataPermissionInterceptor#getSelectedOperaDatas()
     */
    @Override
    protected Object[] getSelectedOperaDatas() {
        return this.getModel().getSelectedOperaDatas();
    }
}
