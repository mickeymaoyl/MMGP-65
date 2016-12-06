package nc.ui.mmgp.uif2.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.actions.BodyPasteLineAction;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.bill.BillTabVO;

public class MMGPBodyPasteLineAction extends BodyPasteLineAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8564608241325037167L;

	@Override
	public Collection<String> getClearItems() {
		if (MMCollectionUtil.isEmpty(super.getClearItems())) {
			BillTabVO[] bodyTabvos = getCardPanel().getBillData()
					.getBillBaseTabVOsByPosition(IBillItem.BODY);
			if (bodyTabvos != null) {
				List<String> keys = new ArrayList<String>();
				for (BillTabVO bodyTab : bodyTabvos) {
					String pk_field = bodyTab.getBillMetaDataBusinessEntity()
							.getPrimaryKey().getPKColumn().getName();
					keys.add(bodyTab.getTabcode() + ":" + pk_field);
				}
				setClearItems(keys);
			}

			// this.getCardPanel().getBillData().getBodyItem(strKey)
		}
		return super.getClearItems();
	}
}
