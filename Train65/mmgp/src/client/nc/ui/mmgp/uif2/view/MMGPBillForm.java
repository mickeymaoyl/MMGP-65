package nc.ui.mmgp.uif2.view;

import nc.ui.mmgp.uif2.view.value.MMGPCardPanelDefaultValueSetter;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.AppEvent;

/**
 * 
 * <b>基于pubapp，增加了事件处理 </b>
 * <p>
 *     详细描述功能
 * </p>
 * 创建日期:2012-11-23
 * @author wangweiu
 */
public class MMGPBillForm extends BillForm {

	/** */
	private static final long serialVersionUID = 8672334670018033056L;

	private MMGPCardPanelDefaultValueSetter defaultValueSetter ;

	@Override
	public void initUI() {
		super.initUI();
		defaultValueSetter = new MMGPCardPanelDefaultValueSetter();
	}

	@Override
	protected void setDefaultValue() {
		defaultValueSetter.setDefaultValue(getModel().getContext(),getBillCardPanel());
	}

	@Override
	public void handleEvent(AppEvent event) {
		super.handleEvent(event);
		if(OrgChangedEvent.class.getName().equals(event.getType())){
			OrgChangedEvent orgChangedEvent = (OrgChangedEvent) event;
			String pk_org = orgChangedEvent.getNewPkOrg();
			getModel().getContext().setPk_org(pk_org);
			getBillCardPanel().addNew();
			setDefaultValue();
		}
	}
}
