package nc.ui.mmgp.uif2.actions;

import nc.ui.pubapp.uif2app.actions.BodyAddLineAction;

public class MMGPBodyAddLineAction extends BodyAddLineAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2987341253314119845L;
	
	/* (non-Javadoc)
	 * @see nc.ui.pubapp.uif2app.actions.BodyAddLineAction#doAction()
	 */
	@Override
	public void doAction() {
	    /* Oct 15, 2013 wangweir �޸Ĳ��ն�ѡ�����л�����ʱֱ�ӵ�����а�ť�������к�bug Begin */
	    this.getCardPanel().stopEditing();
	    /* Oct 15, 2013 wangweir End */
	    super.doAction();
	}

}
