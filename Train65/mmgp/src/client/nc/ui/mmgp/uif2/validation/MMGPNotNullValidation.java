package nc.ui.mmgp.uif2.validation;

import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.ui.uif2.editor.BillForm;
/**
 * 
 * <b> 前台非空校验（基于模板必输项） </b>
 * <p>
 *     详细描述功能
 * </p>
 * 创建日期:2012-11-23
 * @author wangweiu
 */
public class MMGPNotNullValidation implements IValidationService {

	private BillForm billForm;

	public BillForm getBillForm() {
		return this.billForm;
	}

	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}

	@Override
	public void validate(Object obj) throws ValidationException {
		MMGPOrgValidateUtil.validateOrgNotNull(getBillForm(), obj);
		try {
			this.getBillForm().getBillCardPanel().dataNotNullValidate();
		} catch (nc.vo.pub.ValidationException e) {
			ValidationException exp = new ValidationException();
			exp.addValidationFailure(new ValidationFailure(e.getMessage()));
			throw exp;
		}
	}

}