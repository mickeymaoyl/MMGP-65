package nc.ui.mmgp.uif2.validation;

import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.view.BillOrgPanel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.editor.BillForm;

/**
 * 工厂必须项校验
 * 
 * @author yangwm
 * 
 */
public class MMGPOrgValidateUtil {

	public static void validateOrgNotNull(BillForm editor, Object obj)
			throws ValidationException {
		if (editor instanceof ShowUpableBillForm) {
			if (null != ((ShowUpableBillForm) editor).getBillOrgPanel()) {
				ShowUpableBillForm form = (ShowUpableBillForm) editor;
				BillOrgPanel orgPanel = form.getBillOrgPanel();
				Object pk_org = orgPanel.getOrgGetter().getPkOrg(obj);
				if (form.isShowOrgPanel() && null == pk_org) {
					ValidationException exp = new ValidationException();
					exp.addValidationFailure(new ValidationFailure(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0097", null, new String[]{orgPanel.getLabelName()})/*{0}不能为空!*/));
					throw exp;
				}
			}
		}
	}
}
