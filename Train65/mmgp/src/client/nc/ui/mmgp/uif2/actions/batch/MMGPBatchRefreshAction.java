package nc.ui.mmgp.uif2.actions.batch;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.util.ActionEnableCheckUtils;
import nc.ui.pubapp.uif2app.model.IRefreshable;
import nc.ui.uif2.actions.RefreshAction;
import nc.ui.uif2.model.AbstractUIAppModel;

public class MMGPBatchRefreshAction extends RefreshAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9077117062750571907L;

    @Override
    public void doAction(ActionEvent e) throws Exception {
        // ����ѡ������֯��飬ֻ�����֯���ڵ�
        ActionEnableCheckUtils.checkActionEnableInOrgNode(this.getModel().getContext());
        super.doAction(e);
    }

    @Override
    public AbstractUIAppModel getModel() {
        return super.getModel();
    }

    @Override
    public void setModel(AbstractUIAppModel model) {
        super.setModel(model);
    }

    @Override
    protected boolean isActionEnable() {
        /* May 27, 2013 wangweir ������ѯ����ť�Ľڵ㣬��ˢ�¡���ť Begin */
        boolean refreshable = true;
        if (this.getDataManager() instanceof IRefreshable) {
            refreshable = ((IRefreshable) this.getDataManager()).refreshable();
        }
        return super.isActionEnable() && refreshable;
        /* May 27, 2013 wangweir End */
    }

}
