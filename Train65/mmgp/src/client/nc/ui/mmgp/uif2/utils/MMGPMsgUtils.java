package nc.ui.mmgp.uif2.utils;

import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.ui.uif2.model.BillManageModel;

public class MMGPMsgUtils {
	public static void showQueryStatusBarMsg(AbstractUIAppModel model) {
		int size = 0;

		if (model instanceof BillManageModel) {
			size = ((BillManageModel) model).getData().size();

		} else if (model instanceof BatchBillTableModel) {
			size = ((BatchBillTableModel) model).getRows().size();
		} else {
			return;
		}
		if (size == 0) {
			ShowStatusBarMsgUtil.showStatusBarMsg(
					IShowMsgConstant.getQueryNullInfo(), model.getContext());
		} else {
			ShowStatusBarMsgUtil.showStatusBarMsg(
					IShowMsgConstant.getQuerySuccessInfo(size),
					model.getContext());
		}
	}
}
