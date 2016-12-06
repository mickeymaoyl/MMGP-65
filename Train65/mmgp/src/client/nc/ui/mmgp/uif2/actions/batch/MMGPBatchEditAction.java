package nc.ui.mmgp.uif2.actions.batch;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.batch.BatchEditAction;
import nc.ui.pubapp.uif2app.actions.util.ActionEnableCheckUtils;
import nc.ui.pubapp.uif2app.view.BaseOrgPanel;

public class MMGPBatchEditAction extends BatchEditAction {

    private static final long serialVersionUID = -5131855730971553842L;

    private BaseOrgPanel orgPanel;

    /**
	 * 
	 */

    @Override
    public void doAction(ActionEvent e) throws Exception {
        /* Jun 5, 2013 wangweir 解决， 删除主组织，点击修改问题 Begin */
        if (this.getOrgPanel() != null) {
            this.getOrgPanel().stopEditing();
        }
        ActionEnableCheckUtils.checkActionEnableInOrgNode(this.getModel().getContext());
        /* Jun 5, 2013 wangweir End */
        super.doAction(e);
    }

    /**
     * @return the orgPanel
     */
    public BaseOrgPanel getOrgPanel() {
        return orgPanel;
    }

    /**
     * @param orgPanel
     *        the orgPanel to set
     */
    public void setOrgPanel(BaseOrgPanel orgPanel) {
        this.orgPanel = orgPanel;
    }

}
