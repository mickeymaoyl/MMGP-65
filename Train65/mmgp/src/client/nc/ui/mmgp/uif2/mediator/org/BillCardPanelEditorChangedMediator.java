package nc.ui.mmgp.uif2.mediator.org;

import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.ui.uif2.model.AbstractUIAppModel;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 批修改组织修改后事件Mediator.目前主要设置了参照的组织过滤条件.
 * </p>
 * 
 * @since  创建日期:Apr 11, 2013
 * @author wangweir
 */
public class BillCardPanelEditorChangedMediator implements AppEventListener {

    /**
     * BatchBillTable
     */
    private IBillCardPanelEditor billCardPanelEditor;

    /**
     * Model
     */
    private AbstractUIAppModel model;

    /**
     * 组织改变处理类z
     */
    private IOrgChangedForBillCardPanelEditor orgChangedImpl = new OrgChangedForBillCardPanelEditor();

    /**
     * 更新参照
     */
    @Override
    public void handleEvent(AppEvent event) {
        if ((null != this.getOrgChangedImpl()) && (event instanceof OrgChangedEvent)) {
            this.getOrgChangedImpl().orgChanged(this.getBillCardPanelEditor(), this.getModel());
        }
    }

    public void setModel(AbstractUIAppModel model) {
        this.model = model;
        if (null != this.model) {
            this.model.addAppEventListener(this);
        }
    }

    /**
     * @return
     */
    public AbstractUIAppModel getModel() {
        return this.model;
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
     * @return the billCardPanelEditor
     */
    public IBillCardPanelEditor getBillCardPanelEditor() {
        return billCardPanelEditor;
    }

    /**
     * @param billCardPanelEditor
     *        the billCardPanelEditor to set
     */
    public void setBillCardPanelEditor(IBillCardPanelEditor billCardPanelEditor) {
        this.billCardPanelEditor = billCardPanelEditor;
    }

}
