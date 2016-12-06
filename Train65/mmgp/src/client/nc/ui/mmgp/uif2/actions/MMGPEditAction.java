package nc.ui.mmgp.uif2.actions;

import nc.ui.mmgp.uif2.utils.MMGPUIPermissionUtils;
import nc.ui.pubapp.uif2app.actions.EditAction;
import nc.vo.jcom.lang.StringUtil;

public class MMGPEditAction extends EditAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6360826268663869148L;

	@Override
	protected boolean isActionEnable() {
		return super.isActionEnable();
	}

	@Override
	protected boolean checkDataPermission() {
		if (StringUtil.isEmptyWithTrim(getOperateCode())
				&& StringUtil.isEmptyWithTrim(getMdOperateCode())
				&& StringUtil.isEmptyWithTrim(getResourceCode())) {
			return true;
		}
		if (!MMGPUIPermissionUtils.checkManageable(getModel())) {
			return false;
		}
		return super.checkDataPermission();
	}

}
