package nc.ui.mmgp.uif2.actions;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.itf.mmgp.uif2.IMMGPCmnOperateService;
import nc.ui.bd.pub.actions.BDDisableAction;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;

public class MMGPDisableAction extends BDDisableAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1470975058381071571L;

	@Override
	public Object doDisable(Object object) throws Exception {
		MMGPVoUtils.setHeadVOStatus(object, VOStatus.UPDATED);
		return NCLocator.getInstance().lookup(IMMGPCmnOperateService.class)
				.cmnDisableBillData(object);
	}

	/**
	 * 当前选择数据是否出于"启用"状态，只有启用状态的数据才能进行停用
	 * 
	 * @return
	 */
	@Override
	public boolean isCurrentDataEnable() {
		// 返回当前选择数据的封存标记
		Object selObj = getModel().getSelectedData();
		CircularlyAccessibleValueObject vo;
		if (selObj instanceof AggregatedValueObject) {
			vo = ((AggregatedValueObject) selObj).getParentVO();
		} else {
			vo = (CircularlyAccessibleValueObject) getModel().getSelectedData();
		}
		Object enableStatus = vo
				.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD);
		return enableStatus == null
				|| ((Integer) enableStatus == IPubEnumConst.ENABLESTATE_ENABLE);
	}

	protected void doDisable() throws Exception {
		if (UIDialog.ID_YES == CommonConfirmDialogUtils
				.showConfirmDisableDialog(getModel().getContext()
						.getEntranceUI())) {
			Object newobj = doDisable(getModel().getSelectedData());
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
