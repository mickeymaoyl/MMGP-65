/**
 * 
 */
package nc.ui.train.sale.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

/**
 * @author maoyulong
 *
 */
public class AceBodyAfterEditHandler implements IAppEventHandler<CardBodyAfterEditEvent> {

	/**
	 * 
	 */
	public AceBodyAfterEditHandler() {
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO �Զ����ɵķ������
		System.out.println(e);
	}

}
