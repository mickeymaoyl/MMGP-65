package nc.ui.mmgp.uif2.actions.batch;

import java.awt.Container;

import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.uif2.editor.BatchBillTable;

public class MMGPBatchDirectPrintAction extends MMGPAbstractDirectPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BatchBillTable editor;

	@Override
	protected BillItem[] getHeadShowItems() {
		// return editor.getBillCardPanel().getBodyShowItems();
		return null;
	}

	@Override
	protected BillTable[] getBodyTables() {
		BillTable[] bodyTables = new BillTable[1];
		bodyTables[0] = (BillTable) editor.getBillCardPanel().getBillTable();
		return bodyTables;
	}

	@Override
	protected Container getPrintDialogContainer() {
		return getModel().getContext().getEntranceUI();
	}

	public BatchBillTable getEditor() {
		return editor;
	}

	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}
}
