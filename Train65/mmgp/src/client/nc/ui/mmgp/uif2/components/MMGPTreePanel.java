package nc.ui.mmgp.uif2.components;

import java.util.List;

import javax.swing.Action;

import nc.ui.uif2.components.IComponentWithActions;
import nc.ui.uif2.components.TreePanel;
import nc.ui.uif2.model.HierachicalDataAppModel;

@SuppressWarnings("serial")
public class MMGPTreePanel extends TreePanel implements IComponentWithActions {
	private List<Action> actions;

	@Override
	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	@Override
	public void setModel(HierachicalDataAppModel model) {
		super.setModel(model);
	}

}
