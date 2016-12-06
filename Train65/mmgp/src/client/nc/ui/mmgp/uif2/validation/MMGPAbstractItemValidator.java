package nc.ui.mmgp.uif2.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.ui.uif2.editor.BillForm;


public class MMGPAbstractItemValidator implements IValidationService {

	private BillForm billForm;

	public BillForm getBillForm() {
		return this.billForm;
	}

	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}

	public Object getHeadItemValue(String itemKey) {
		return getBillForm().getBillCardPanel().getBillData()
				.getHeadItem(itemKey) == null ? null : getBillForm()
				.getBillCardPanel().getBillData().getHeadItem(itemKey)
				.getValueObject();
	}

	/***
	 * У��IP��ַ�Ϸ�
	 * @param itemFieldName
	 * @throws ValidationException
	 */
	protected void validataIpAddress(String itemFieldName)throws ValidationException{

		Object ipAddresssObject = getHeadItemValue(itemFieldName) ;

		if(ipAddresssObject == null){
			return ;
		}

		//IP��ַ������ʽ
		String ipPattern = "^([1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(0|[1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
		Pattern pattern = Pattern.compile(ipPattern);
		Matcher matcher = pattern.matcher(ipAddresssObject.toString());

		if(matcher.find() == false){
			ValidationException exp = new ValidationException();
			exp.addValidationFailure(new ValidationFailure(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0033")/*@res "ip��ַ��֤���Ϸ�!"*/));
			throw exp;
		}
	}

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
}