package nc.ui.mmgp.uif2.actions;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.itf.mmgp.uif2.IMMGPCmnOperateService;
import nc.ui.bd.pub.actions.BDEnableAction;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;

public class MMGPEnableAction extends BDEnableAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1470975058381071571L;

	@Override
	public Object doEnable(Object obj) throws Exception {
		MMGPVoUtils.setHeadVOStatus(obj, VOStatus.UPDATED);
		return NCLocator.getInstance().lookup(IMMGPCmnOperateService.class)
				.cmnEnableBillData(obj);
	}

	@Override
	public boolean isCurrentDataEnable() {
		// 返回当前选择数据的是否启用标记
		Object selObj = getModel().getSelectedData();
		CircularlyAccessibleValueObject vo;
		if (selObj instanceof AggregatedValueObject) {
			vo = ((AggregatedValueObject) selObj).getParentVO();
		} else {
			vo = (CircularlyAccessibleValueObject) getModel().getSelectedData();
		}
		return vo.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD) == null
				|| (IPubEnumConst.ENABLESTATE_ENABLE == (Integer) vo
						.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD));
	}

	@Override
	protected void doEnable() throws Exception {
		if (UIDialog.ID_YES == CommonConfirmDialogUtils
				.showConfirmEnableDialog(getModel().getContext()
						.getEntranceUI())) {
			Object newobj = doEnable(getModel().getSelectedData());
			if (newobj instanceof Object[]) {
				getModel().directlyUpdate((Object[]) newobj);
			} else {
				getModel().directlyUpdate(newobj);
			}
		}
	}

	@Override
	protected void validate() {
		validate(getModel().getSelectedData());
	}

}
