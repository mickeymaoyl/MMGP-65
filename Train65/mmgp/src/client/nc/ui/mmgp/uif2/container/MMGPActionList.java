package nc.ui.mmgp.uif2.container;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;

public class MMGPActionList extends ArrayList<Action> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8848378067239575503L;

	public MMGPActionList() {

	}

	public MMGPActionList(Collection<Action> c) {
		super(c);
	}

	public void setAddActions(AddActions addAction) {
		this.addAll(addAction.getActions());
	}

	public void setInsertActions(InsertActions insertAction) {

		if (insertAction.getActions() == null
				|| insertAction.getActions().isEmpty()) {
			return;
		}

		if (!(insertAction.getAfter() == null ^ insertAction.getBefore() == null)) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0020")/*@res "before和after必须存在一个!"*/);
		}
		int pos = -100;

		for (int i = 0; i < size(); i++) {
			if (get(i).equals(insertAction.validatedAction())) {
				pos = insertAction.getPos(i);
				break;
			}
		}
		if (pos < 0) {
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0021")/*@res "指定的before/after的Action不存在!"*/);
		}

		addAll(pos, insertAction.getActions());
	}

}