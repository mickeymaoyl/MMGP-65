package nc.ui.mmgp.uif2.container;

import java.util.List;

import javax.swing.Action;

import nc.ui.uif2.TangramContainer;
import nc.vo.mmgp.util.MMSystemUtil;

/**
 * 扩展pubapp的界面容器，增量增加按钮的配置，详细请参考billmanage.xml模板的配置
 * @author wangweiu
 */
public class MMGPTangramContainer extends TangramContainer {

	/**
	 *
	 */
	private static final long serialVersionUID = -5373596327253127090L;

	private InsertActions initInsertAction;
	private AddActions initAddAction;
	private List<Action> actionsOnDevelopMode;

	// private InitInsertAction editInsertAction;
	// private InitAddAction editAddAction;

	public InsertActions getInsertActions() {
		return initInsertAction;
	}

	public void setInsertActions(InsertActions initInsertAction) {
		this.initInsertAction = initInsertAction;
		if (initInsertAction.getActions() == null
				|| initInsertAction.getActions().isEmpty()) {
			return;
		}

		if (!(initInsertAction.getAfter() == null ^ initInsertAction
				.getBefore() == null)) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0020")/*@res "before和after必须存在一个!"*/);
		}
		int pos = -100;

		for (int i = 0; i < getActions().size(); i++) {
			if (getActions().get(i).equals(initInsertAction.validatedAction())) {
				pos = initInsertAction.getPos(i);
				break;
			}
		}
		if (pos < 0) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0021")/*@res "指定的before/after的Action不存在!"*/);
		}

		getActions().addAll(pos, initInsertAction.getActions());

		resetActions();
	}

	public AddActions getAddActions() {
		return initAddAction;
	}

	public void setAddActions(AddActions initAddAction) {
		this.initAddAction = initAddAction;
		if (initAddAction.getActions() == null
				|| initAddAction.getActions().isEmpty()) {
			return;
		}
		getActions().addAll(initAddAction.getActions());
		resetActions();
	}

	public List<Action> getActionsOnDevelopMode() {
		return actionsOnDevelopMode;
	}

	public void setActionsOnDevelopMode(List<Action> actionsOnDevelopMode) {
		this.actionsOnDevelopMode = actionsOnDevelopMode;
		if (actionsOnDevelopMode != null && !actionsOnDevelopMode.isEmpty() && !MMSystemUtil.isDevelopMode()) {
			getActions().removeAll(actionsOnDevelopMode);
			resetActions();
		}
	}

	protected void resetActions() {
		setActions(getActions());
	}

}