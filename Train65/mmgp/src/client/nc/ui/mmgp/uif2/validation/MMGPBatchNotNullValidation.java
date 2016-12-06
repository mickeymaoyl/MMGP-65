package nc.ui.mmgp.uif2.validation;

import java.util.List;

import nc.bs.uif2.validation.IBatchValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.ui.uif2.editor.BatchBillTable;

public class MMGPBatchNotNullValidation implements IBatchValidationService {
	private BatchBillTable billForm;

	@Override
	public void validate(Object obj) throws ValidationException {
		try {
			this.getBillForm().getBillCardPanel().dataNotNullValidate();
		} catch (nc.vo.pub.ValidationException e) {
			ValidationException exp = new ValidationException();
			exp.addValidationFailure(new ValidationFailure(e.getMessage()));
			throw exp;
		}

	}

	@Override
	public int[] unNecessaryData(List<Object> rows) {
		return null;
	}

	public BatchBillTable getBillForm() {
		return billForm;
	}

	public void setBillForm(BatchBillTable billForm) {
		this.billForm = billForm;
	}

}
