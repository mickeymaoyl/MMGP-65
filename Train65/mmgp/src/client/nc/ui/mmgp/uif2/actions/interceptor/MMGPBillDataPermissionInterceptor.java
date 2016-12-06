package nc.ui.mmgp.uif2.actions.interceptor;

import nc.ui.uif2.model.BillManageModel;
import nc.vo.uif2.LoginContext;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ����Ȩ�޿���������
 * </p>
 * 
 * @since �������� May 16, 2013
 * @author wangweir
 */
public class MMGPBillDataPermissionInterceptor extends MMGPAbstractDataPermissionInterceptor {

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
