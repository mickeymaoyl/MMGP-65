package nc.ui.mmgp.uif2.mediator.org;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙组织改变事件处理
 * </p>
 * 
 * @since 创建日期 May 12, 2013
 * @author wangweir
 */
public class OrgChangedHandlerForGrand implements IAppEventHandler<OrgChangedEvent> {

    private BillForm billForm;

    private BillForm grandBillForm;

    private BillManageModel model;

    /**
     * 组织改变处理类z
     */
    private IOrgChangedForBillCardPanelEditor orgChangedImpl = new OrgChangedForBillCardPanelEditor();

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.event.IAppEventHandler#handleAppEvent(nc.ui.uif2.AppEvent)
     */
    @Override
    public void handleAppEvent(OrgChangedEvent e) {
        this.orgChangedImpl.orgChanged(this.getBillForm(), model);
        this.orgChangedImpl.orgChanged(this.getGrandBillForm(), model);

        // 清空孙表数据
        if (this.getModel().getAppUiState().getUiState() == UIState.ADD) {
            this.getGrandBillForm().getBillCardPanel().addNew();
        }
    }

    /**
     * @return the orgChangedImpl
     */
    public IOrgChangedForBillCardPanelEditor getOrgChangedImpl() {
        return orgChangedImpl;
    }

    /**
     * @param orgChangedImpl
     *        the orgChangedImpl to set
     */
    public void setOrgChangedImpl(IOrgChangedForBillCardPanelEditor orgChangedImpl) {
        this.orgChangedImpl = orgChangedImpl;
    }

    /**
     * @return the billForm
     */
    public BillForm getBillForm() {
        return billForm;
    }

    /**
     * @param billForm
     *        the billForm to set
     */
    public void setBillForm(BillForm billForm) {
        this.billForm = billForm;
    }

    /**
     * @return the grandBillForm
     */
    public BillForm getGrandBillForm() {
        return grandBillForm;
    }

    /**
     * @param grandBillForm
     *        the grandBillForm to set
     */
    public void setGrandBillForm(BillForm grandBillForm) {
        this.grandBillForm = grandBillForm;
    }

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
        if (null != this.model) {
            this.model.addAppEventListener(OrgChangedEvent.class, this);
        }
    }

}
