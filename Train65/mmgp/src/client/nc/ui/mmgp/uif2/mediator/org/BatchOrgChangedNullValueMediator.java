package nc.ui.mmgp.uif2.mediator.org;

import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 30, 2013
 * @author wangweir
 */
public class BatchOrgChangedNullValueMediator implements AppEventListener {
    /**
     * Model
     */
    private AbstractUIAppModel model;

    /**
     * @return the model
     */
    public AbstractUIAppModel getModel() {
        return model;
    }

    /**
     * @param model
     *        the model to set
     */
    public void setModel(AbstractUIAppModel model) {
        this.model = model;
        if (null != this.model) {
            this.model.addAppEventListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.AppEventListener#handleEvent(nc.ui.uif2.AppEvent)
     */
    @Override
    public void handleEvent(AppEvent event) {
        String pkOrg = this.getModel().getContext().getPk_org();
        if ((event instanceof OrgChangedEvent) && MMStringUtil.isEmpty(pkOrg)) {
            this.getModel().initModel(null);
        }
    }
}
