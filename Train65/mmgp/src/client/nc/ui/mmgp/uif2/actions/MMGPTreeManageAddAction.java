package nc.ui.mmgp.uif2.actions;

import nc.ui.uif2.UIState;
import nc.ui.uif2.model.HierachicalDataAppModel;

@SuppressWarnings("serial")
public class MMGPTreeManageAddAction extends MMGPAddAction {
	private HierachicalDataAppModel treeModel = null;

	@Override
	protected boolean isActionEnable() {
		return model.getUiState() == UIState.NOT_EDIT
				&& treeModel.getSelectedData() != null;
	}

	public HierachicalDataAppModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}

}
