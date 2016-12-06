package nc.ui.mmgp.uif2.actions;

import java.awt.Container;

import nc.ui.mmgp.uif2.actions.batch.MMGPAbstractDirectPrintAction;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.uif2.editor.BillForm;
import nc.vo.pub.bill.BillTabVO;

@SuppressWarnings("serial")
public class MMGPBillCardDirectPrintAction extends MMGPAbstractDirectPrintAction {
	private BillForm editor;

	@Override
	protected BillItem[] getHeadShowItems() {
		return editor.getBillCardPanel().getHeadShowItems();
	}

	@Override
	protected BillTable[] getBodyTables() {
		BillTabVO[] tabVos = editor.getBillCardPanel().getBillData()
				.getAllTabVos();
		BillTable[] bodyTables = new BillTable[tabVos.length];
		for (int i = 0; i < tabVos.length; i++) {
			BillTabVO tabVo = tabVos[i];
			bodyTables[i] = (BillTable) editor.getBillCardPanel().getBillTable(
					tabVo.getTabcode());
		}
		return bodyTables;
	}

	@Override
	protected Container getPrintDialogContainer() {
		return getModel().getContext().getEntranceUI();
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}
}
