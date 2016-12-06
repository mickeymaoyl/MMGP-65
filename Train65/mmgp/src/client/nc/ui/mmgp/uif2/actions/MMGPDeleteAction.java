package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.ui.mmgp.uif2.utils.MMGPUIPermissionUtils;
import nc.ui.pubapp.uif2app.actions.DeleteAction;
import nc.ui.uif2.model.IMultiRowSelectModel;
import nc.vo.jcom.lang.StringUtil;

public class MMGPDeleteAction extends DeleteAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8223180975614827L;
	private IValidationService validationService;

	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object tempData = null;
		if ((this.getSingleBillView() != null)
				&& this.getSingleBillView().isComponentVisible()) {
			// 卡片显示时，只删除当前选中的一行数据
			if (null != this.getModel().getSelectedData()) {
				tempData = this.getModel().getSelectedData();
			}
		} else if (this.getModel() instanceof IMultiRowSelectModel) {
			tempData = ((IMultiRowSelectModel) this.getModel())
					.getSelectedOperaDatas();
		}
		validate(tempData);
		super.doAction(e);
	}

	@Override
	protected void checkDataPermission() throws Exception {
		if (StringUtil.isEmptyWithTrim(getOperateCode())
				&& StringUtil.isEmptyWithTrim(getMdOperateCode())
				&& StringUtil.isEmptyWithTrim(getResourceCode())) {
			return;
		}
		MMGPUIPermissionUtils.checkManageableWithException(getModel());
		super.checkDataPermission();
	}

	/**
	 * 
	 * @param value
	 * @throws ValidationException
	 */
	protected void validate(Object value) throws ValidationException {
		if (validationService != null) {
			validationService.validate(value);
		}
	}

	public IValidationService getValidationService() {
		return validationService;
	}

	public void setValidationService(IValidationService validationService) {
		this.validationService = validationService;
	}

}
