package nc.ui.mmgp.uif2.view.treetable;

import nc.ui.pubapp.bill.BillCardPanel;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����11:14:50
 * @author: tangxya
 */
public class MMGPTreeTableBillCardPanel extends BillCardPanel {

	private MMGPTreeTableBillScrollPane billScrollPane;

	public MMGPTreeTableBillScrollPane getBillScrollPane() {
		return billScrollPane;
	}

	public void setBillScrollPane(MMGPTreeTableBillScrollPane billScrollPane) {
		this.billScrollPane = billScrollPane;
	}

	protected MMGPTreeTableBillScrollPane createDefaultBillScrollPane() {
		return billScrollPane;
	}
}
