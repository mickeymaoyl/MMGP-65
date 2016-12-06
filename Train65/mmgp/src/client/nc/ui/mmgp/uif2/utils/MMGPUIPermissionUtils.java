package nc.ui.mmgp.uif2.utils;

import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.pub.BusinessException;
import nc.vo.util.ManageModeUtil;

public class MMGPUIPermissionUtils {
	public static boolean checkManageable(AbstractAppModel model) {
		if (!ManageModeUtil.manageable(model.getSelectedData(),
				model.getContext())) {
			String msg = getDisManageableMsg(model);
			if (model != null && model.getContext() != null)
				ShowStatusBarMsgUtil.showErrorMsg(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("bdpub", "0bdpub0007")/*
																		 * @res
																		 * "´íÎó"
																		 */,
						msg, model.getContext());
			return false;
		}
		return true;
	}

	public static void checkManageableWithException(AbstractAppModel model)
			throws BusinessException {
		if (!ManageModeUtil.manageable(model.getSelectedData(),
				model.getContext())) {
			String msg = getDisManageableMsg(model);
			throw new BusinessException(msg);
		}
	}

	protected static String getDisManageableMsg(AbstractAppModel model) {
		return ManageModeUtil.getDisManageableMsg(model.getContext()
				.getNodeType());
	}
}
