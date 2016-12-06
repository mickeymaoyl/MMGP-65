package nc.ui.mmgp.uif2.actions.batch.interceptor;

import nc.ui.mmgp.uif2.actions.interceptor.MMGPAbstractManageableInterceptor;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.uif2.LoginContext;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ά�� ��֯Ȩ�޿��ƣ�����UE�淶��������̸�Ի�����ʾ
 * </p>
 * 
 * @since �������� May 16, 2013
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
