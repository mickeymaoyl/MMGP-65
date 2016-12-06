package nc.ui.train.saleout.aciton;

import nc.ui.pubapp.billref.dest.ITransferBillDataLogic;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.PubShowUpableBillForm;

public class SaleOutTrans implements ITransferBillDataLogic {

	
	private PubShowUpableBillForm billForm;
	public SaleOutTrans() {
		// TODO 自动生成的构造函数存根
	}

	public PubShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(PubShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

	@Override
	public void doTransferAddLogic(Object selectedData) {
		// TODO 自动生成的方法存根
//            this.billForm.showMeUp();
		    this.billForm.setValue(selectedData);
		    // 在设置完值之后再调用这个方法的目的是避免漏掉一些控制逻辑
		    this.billForm.setEditable(true);
		    this.billForm.showMeUp();
	}

}
