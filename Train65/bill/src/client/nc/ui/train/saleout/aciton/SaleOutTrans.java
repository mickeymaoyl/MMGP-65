package nc.ui.train.saleout.aciton;

import nc.ui.pubapp.billref.dest.ITransferBillDataLogic;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.PubShowUpableBillForm;

public class SaleOutTrans implements ITransferBillDataLogic {

	
	private PubShowUpableBillForm billForm;
	public SaleOutTrans() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public PubShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(PubShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

	@Override
	public void doTransferAddLogic(Object selectedData) {
		// TODO �Զ����ɵķ������
//            this.billForm.showMeUp();
		    this.billForm.setValue(selectedData);
		    // ��������ֵ֮���ٵ������������Ŀ���Ǳ���©��һЩ�����߼�
		    this.billForm.setEditable(true);
		    this.billForm.showMeUp();
	}

}
