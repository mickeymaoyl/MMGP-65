package nc.ui.mmgp.uif2.container;

import javax.swing.Action;

public class InsertActions extends AddActions {
	private Action after;
	private Action before;

	public Action getAfter() {
		return after;
	}

	public void setAfter(Action after) {
		this.after = after;
	}

	public Action getBefore() {
		return before;
	}

	public void setBefore(Action before) {
		this.before = before;
	}

	public Action validatedAction() {
		return after == null ? before : after;
	}

	public int getPos(int pos) {
		int newPos = after == null ? pos - 1 : pos + 1;
		if (newPos < 0) {
			newPos = 0;
		}
		return newPos;
	}

}
