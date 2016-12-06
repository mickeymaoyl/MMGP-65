package nc.ui.mmgp.uif2.mediator.org;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.ui.uif2.model.AbstractUIAppModel;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 批修改界面组织变化处理，目前处理了参照的过滤。 如果pk_org字段有变化，请注入。
 * </p>
 * 
 * @since 创建日期:Apr 11, 2013
 * @author wangweir
 */
public class OrgChangedForBillCardPanelEditor implements
		IOrgChangedForBillCardPanelEditor {

	private String orgColumnName = "pk_org";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.ui.mmgp.uif2.mediator.org.IOrgChangedForBatchTable#orgChanged(nc.ui
	 * .pubapp.uif2app.view.BatchBillTable)
	 */
	@Override
	public void orgChanged(IBillCardPanelEditor billCardPanelEditor,
			AbstractUIAppModel model) {
		BillCardPanel billCardPanel = billCardPanelEditor.getBillCardPanel();
		if (null != billCardPanel) {
			// 设置参照过滤
			BillPanelUtils.setOrgForAllRef(billCardPanel, model.getContext());
			if (billCardPanel.getHeadItem(orgColumnName) != null) {
				billCardPanel.getBillData().loadEditHeadRelation(orgColumnName);
			}
			billCardPanel.getBillData().loadLoadHeadRelation();
		}
	}

	/**
	 * @return the orgColumnName
	 */
	public String getOrgColumnName() {
		return orgColumnName;
	}

	/**
	 * @param orgColumnName
	 *            the orgColumnName to set
	 */
	public void setOrgColumnName(String orgColumnName) {
		this.orgColumnName = orgColumnName;
	}

}
