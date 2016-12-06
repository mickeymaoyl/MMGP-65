package nc.ui.mmgp.uif2.model;

import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;

public class MMGPBatchBillTableModel extends BatchBillTableModel{

	@Override
	public void addAppEventListener(AppEventListener l) {
		super.addAppEventListener(l);
	}

	@Override
	public void addAppEventListener(Class<? extends AppEvent> eventType,
			IAppEventHandler<? extends AppEvent> l) {
		super.addAppEventListener(eventType, l);
	}

	@Override
	public void fireEvent(AppEvent event) {
		super.fireEvent(event);
	}

	@Override
	public AppUiState getAppUiState() {
		return super.getAppUiState();
	}

	@Override
	public UIState getUiState() {
		return super.getUiState();
	}

	@Override
	public void removeAppEventListener(AppEventListener l) {
		super.removeAppEventListener(l);
	}

	@Override
	public void removeAppEventListener(Class<? extends AppEvent> eventType,
			IAppEventHandler<? extends AppEvent> l) {
		super.removeAppEventListener(eventType, l);
	}

	@Override
	public void setAppUiState(AppUiState appUiState) {
		super.setAppUiState(appUiState);
	}

	@Override
	public void setUiState(UIState uiState) {
		super.setUiState(uiState);
	}
	
	
}
