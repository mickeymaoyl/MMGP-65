package nc.ui.mmgp.uif2.actions;

import java.awt.Container;

import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.uif2.actions.AbstractDirectPrintAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;
import nc.vo.pub.bill.BillTabVO;

@SuppressWarnings("serial")
public class MMGPBillManageDirectPrintAction extends AbstractDirectPrintAction {
	private BillForm editor;
	private BillListView list;

	@Override
	protected BillItem[] getHeadShowItems() {
		if (editor.isShowing()) {
			return editor.getBillCardPanel().getHeadShowItems();
		} else {
			return null;
		}
	}

	@Override
	protected BillTable[] getBodyTables() {
		BillTable[] bodyTables;
		if (editor.isShowing()) {
			BillTabVO[] tabVos = editor.getBillCardPanel().getBillData()
					.getAllTabVos();
			if (tabVos == null || tabVos.length == 0) {
				return null;
			}
			bodyTables = new BillTable[tabVos.length];
			for (int i = 0; i < tabVos.length; i++) {
				BillTabVO tabVo = tabVos[i];
				bodyTables[i] = (BillTable) editor.getBillCardPanel()
						.getBillTable(tabVo.getTabcode());
			}
		} else {
			bodyTables = new BillTable[] { (BillTable) list.getBillListPanel()
					.getHeadTable() };
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

	public BillListView getList() {
		return list;
	}

	public void setList(BillListView list) {
		this.list = list;
	}

}
