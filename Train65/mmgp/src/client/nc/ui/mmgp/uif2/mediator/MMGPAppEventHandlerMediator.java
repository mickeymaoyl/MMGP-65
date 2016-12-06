package nc.ui.mmgp.uif2.mediator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.ui.pubapp.uif2app.event.EventHandlerDelegate;
import nc.ui.pubapp.uif2app.event.EventHandlerGroup;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.model.IAppModelEx;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;

public class MMGPAppEventHandlerMediator {
	private List<EventHandlerGroup> handlerGroup;

	private Map<String, Collection<IAppEventHandler<?>>> handlerMap;

	private IAppModelEx model;

	public List<EventHandlerGroup> getHandlerGroup() {
		return this.handlerGroup;
	}

	public Map<String, Collection<IAppEventHandler<?>>> getHandlerMap() {
		return this.handlerMap;
	}

	public void setHandlerGroup(List<EventHandlerGroup> handlerGroups) {
		this.handlerGroup = handlerGroups;
		this.matchHandlerGroup();
	}

	public void setHandlerMap(
			Map<String, Collection<IAppEventHandler<?>>> handlerMap) {
		this.handlerMap = handlerMap;
		this.matchHandlerMap();
	}

	public void setModel(IAppModelEx model) {
		this.model = model;
		this.matchHandlerMap();
		this.matchHandlerGroup();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void matchHandlerGroup() {
		if (null == this.model || null == this.handlerGroup) {
			return;
		}

		for (EventHandlerGroup group : this.handlerGroup) {
			EventHandlerDelegate eventHandler = new EventHandlerDelegate();
			eventHandler.setPicky(group.getPicky());
			eventHandler.setHandler(group.getHandler());
			String eventType = group.getEvent();
			addEventListener(eventType, eventHandler);
		}
	}

	protected void matchHandlerMap() {
		if (null == this.model || null == this.handlerMap) {
			return;
		}

		for (String eventType : this.handlerMap.keySet()) {
			for (IAppEventHandler<?> handler : this.handlerMap.get(eventType)) {
				addEventListener(eventType, handler);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addEventListener(final String eventType,
			final IAppEventHandler handler) {
		try {
			this.model.addAppEventListener(
					(Class<? extends AppEvent>) Class.forName(eventType),
					handler);
		} catch (ClassNotFoundException e) {
			this.model.addAppEventListener(new AppEventListener() {
				@Override
				public void handleEvent(AppEvent event) {
					if (event.getType().equals(eventType)) {
						handler.handleAppEvent(event);
					}
				}
			});
//			ExceptionUtils.wrappException(e);
		}
	}

}
