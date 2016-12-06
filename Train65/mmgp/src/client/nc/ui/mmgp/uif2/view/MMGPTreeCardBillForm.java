package nc.ui.mmgp.uif2.view;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.view.value.MMGPCardPanelDefaultValueSetter;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.vo.pub.BeanHelper;

public class MMGPTreeCardBillForm extends BillForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2220632620703958100L;

	private String pkFieldName;

	private String parentPKFieldName;

	private MMGPCardPanelDefaultValueSetter defaultValueSetter ;
	

	@Override
	public void initUI() {
		super.initUI();
		defaultValueSetter = new MMGPCardPanelDefaultValueSetter();
	}

	protected void setBillItemValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null) {
			getBillCardPanel().getHeadItem(key).setValue(value);
		}
	}
	
	@Override
	public void setValue(Object object) {
		super.setValue(object);
		if(getModel().getUiState() == UIState.ADD){
			setDefaultValue();
		}
	}

	@Override
	protected void setDefaultValue() {
		super.setDefaultValue();
		defaultValueSetter.setDefaultValue(getModel().getContext(),getBillCardPanel());
		Object selObj = getModel().getSelectedData();
		if (selObj == null) {
			return;
		}
		getBillCardPanel().getHeadItem(getParentPKFieldName()).setValue(
				BeanHelper.getProperty(selObj, getPkFieldName()));
	}

	public String getPkFieldName() {
		if (pkFieldName == null) {
			pkFieldName = MMGPMetaUtils.getPKFieldName(getModel().getContext());
		}
		return pkFieldName;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public String getParentPKFieldName() {
		if (parentPKFieldName == null) {
			parentPKFieldName = MMGPMetaUtils.getParentPKFieldName(getModel()
					.getContext());
		}
		return parentPKFieldName;
	}

	public void setParentPKFieldName(String parentPKFieldName) {
		this.parentPKFieldName = parentPKFieldName;
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