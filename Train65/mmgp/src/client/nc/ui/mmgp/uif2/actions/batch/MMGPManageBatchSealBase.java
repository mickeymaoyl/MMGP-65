package nc.ui.mmgp.uif2.actions.batch;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.validation.IBatchValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.itf.mmgp.uif2.IMMGPCmnOperateService;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.ui.uif2.model.BillManageModel;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.ui.uif2.model.IMultiRowSelectModel;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.batch.MMGPBatchVOUtil;
import nc.vo.mmgp.batch.MMGPBatchValidationFailure;
import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;
import nc.vo.mmgp.meta.MMGPObjectAdpaterFactory;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.ManageModeUtil;

public abstract class MMGPManageBatchSealBase extends NCAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -5858102117520274479L;

	private AbstractUIAppModel model;

	// 校验对象需要在此处注射，不要在模型中注射，否则将可能导致问题发生。
	private IBatchValidationService validationService = null;

	// 以下实现数据权限需要
	private String mdOperateCode = null; // 元数据操作编码

	private String operateCode = null; // 资源对象操作编码，以上两者注入其一，都不注入，则不进行数据权限控制。

	private String resourceCode = null; // 业务实体资源编码

	public static final int ENABLE_DISABLE = 3;

	public static final int ALL_ENABLE = 2;

	public static final int ALL_DISABLE = 1;

	private MMGPValueObjWithErrLog operData;

	private IMMGPCmnOperateService operateService;

	private MMGPObjectAdpaterFactory bdObjectFactory = null;

	public MMGPManageBatchSealBase() {
		super();
	}

	protected IMMGPCmnOperateService getOperateService() {
		if (operateService == null) {
			operateService = NCLocator.getInstance().lookup(
					IMMGPCmnOperateService.class);
		}

		return operateService;
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
		checkSelectDataNull();

		buildValueObjWithErrLog();

		checkData();

		validate();

		boolean isoperate=doOperate();
		if(!isoperate){
		    return;
		}

		updateModel();

		showErrorLog();

		clearOperData();
	}

	private void clearOperData() {
		this.setOperData(null);
	}

	protected abstract void showStatusMsg();

	protected void showErrorLog() {
		Map<Object, List<String>> value2ErrMap = this.getOperData()
				.getValue2ErrMap();
		if (value2ErrMap == null || value2ErrMap.size() == 0) {
			showStatusMsg();
			return;
		}
		StringBuilder errMsg = new StringBuilder(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0004")/*@res "以下数据操作失败："*/ + "\n");
		for (Map.Entry<Object, List<String>> errEntry : value2ErrMap.entrySet()) {
			Object obj = errEntry.getKey();
			List<String> errList = errEntry.getValue();
			String voErrmsg = getVOErrmsg(obj, errList);
			errMsg.append(voErrmsg + "\n");
		}
		ShowStatusBarMsgUtil.showErrorMsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0005")/*@res "操作失败"*/, errMsg.toString(), getModel()
				.getContext());

	}

	protected String getVOErrmsg(Object obj, List<String> errList) {
		SuperVO vo = MMGPBatchVOUtil.getMainSuperVO(obj);
		if (vo == null) {
			return null;
		}
		String code = (String) getIBDObjetct(vo).getCode();
		String name = getIBDObjetct(vo).getName().toString();
		StringBuilder errMsg = new StringBuilder(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0090", null, new String[]{code,name})/*编码[{0}]，名称[{1}]:\n*/);
		errMsg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0006")/*@res "失败原因："*/);

		for (int i = 0; i < errList.size(); i++) {
			errMsg.append(i + 1).append(",").append(errList.get(i)).append(" ");
		}
		return errMsg.toString();
	}

	protected void checkSelectDataNull() throws BusinessException {
		Object[] selectedObjs = getSelectedOperaDatas();
		if (selectedObjs == null || selectedObjs.length == 0) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0007")/*@res "请选择待操作的数据."*/);
		}
	}

	public Object[] getSelectedOperaDatas() {
		if (model instanceof IMultiRowSelectModel) {
			return ((IMultiRowSelectModel) model).getSelectedOperaDatas();
		} else if (model instanceof BatchBillTableModel) {
			return ((BatchBillTableModel) model).getSelectedOperaDatas();
		} else if (getModel() instanceof HierachicalDataAppModel) {
			return ((HierachicalDataAppModel) getModel()).getSelectedDatas();
		}
		return new Object[] { model.getSelectedData() };
	}

	public Integer[] getSelectedOperaRows() {
		if (model instanceof IMultiRowSelectModel) {
			return ((IMultiRowSelectModel) model).getSelectedOperaRows();
		}
		if (model instanceof BatchBillTableModel) {
			return ((BatchBillTableModel) model).getSelectedOperaRows();
		}
		return null;
	}

	private void updateModel() {
		if (model instanceof BillManageModel) {
			((BillManageModel) model).directlyUpdate(this.getOperData()
					.getVos());
		}
	}

	protected boolean isDisabled(Object obj) {
		SuperVO vo = MMGPBatchVOUtil.getMainSuperVO(obj);
		if (vo != null) {
			Integer enablestate = (Integer) vo
					.getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD);
			if (enablestate != null
					&& enablestate.equals(IPubEnumConst.ENABLESTATE_DISABLE))
				return true;
		}
		return false;
	}

	private void checkData() {
		checkStateEnable();

		checkManagerModel();

		checkDataHasPermission();

	}

	protected boolean doOperate() throws Exception {
	    int isOperate = showConfirmDialog();
		if (UIDialog.ID_CANCEL == isOperate
				|| UIDialog.ID_NO == isOperate) {
			return false;
		}
		Map<Object, List<String>> allErr = new HashMap<Object, List<String>>();
		allErr.putAll(this.getOperData().getValue2ErrMap());
		if (this.getOperData().getVos() != null
				&& this.getOperData().getVos().length > 0) {
			MMGPValueObjWithErrLog voErr = doOperate(getOperData().getVos());
			if (voErr != null) {
				this.getOperData().getValue2ErrMap()
						.putAll(voErr.getValue2ErrMap());
				this.getOperData().setVos(voErr.getVos());
			}
		}
		return true;
	}

	/**
	 * 解决删除时抛出异常“Index: 0, Size: 0”
	 *
	 * @return true if the select data is legal
	 */
	private boolean isSelectDataLegal() {
		Integer[] rows = getSelectedOperaRows();
		int rowCount = getRowCount();
		for (Integer row : rows) {
			if (row >= rowCount) {
				return false;
			}
		}
		return true;
	}

	private int getRowCount() {
		if (model instanceof IMultiRowSelectModel) {
			return ((IMultiRowSelectModel) model).getRowCount();
		}
		if (model instanceof BatchBillTableModel) {
			return ((BatchBillTableModel) model).getRowCount();
		}
		return -1;
	}

	/**
	 * 获取当前选中数据的状态
	 *
	 * @return -1 表示当前没有选中数据
	 */
	public int getSelectedDataStatus() {
		// 返回当前选择数据的启用标识.如果全为启用返回True
		boolean enableFlag = false;
		boolean disableFlag = false;

		// 解决删除时抛出异常“Index: 0, Size: 0”
		if (!isSelectDataLegal()) {
			return ENABLE_DISABLE;
		}

		Object[] vos = getSelectedOperaDatas();

		// 如果没有选择数据，提示
		if (vos == null || vos != null && vos.length == 0) {
			return -1;
		}

		// 对选择的数据状态进行分类
		for (Object vo : vos) {
			if (!isDisabled(vo)) {
				enableFlag = true;
			} else {
				disableFlag = true;
			}

			if (enableFlag && disableFlag) {
				return ENABLE_DISABLE;
			}
		}

		if (enableFlag) {
			return ALL_ENABLE;
		}

		return ALL_DISABLE;
	}

	protected abstract int showConfirmDialog();

	protected abstract MMGPValueObjWithErrLog doOperate(Object[] vo)
			throws Exception;

	protected void validate() {
		validate(this.getCheckData());
	}

	// ??
	private Object getCheckData() {
		return getSelectedOperaDatas();
	}

	protected void validate(Object value) {
		if (validationService != null) {
			try {
				validationService.validate(value);
			} catch (ValidationException e) {
				List<ValidationFailure> failureList = e.getFailures();
				for (ValidationFailure failur : failureList) {
					if (failur instanceof MMGPBatchValidationFailure) {
						MMGPBatchValidationFailure batFail = (MMGPBatchValidationFailure) failur;
						String errmsg = batFail.getMessage();
						Object obj = batFail.getUesrObject();
						this.getOperData().addErrLogMessage(obj, errmsg);
					} else {
						ExceptionUtils.wrappException(e);
					}
				}
			}
		}

	}

	protected Object[] getUnDataPermissionData() {
		Object obj = getSelectedOperaDatas();
		return CheckDataPermissionUtil.getUnDataPermissionData(operateCode,
				mdOperateCode, resourceCode, model.getContext(), obj);
	}

	private void checkDataHasPermission() {
		if (needCheckPermission()) {
			return;
		}
		Object[] objs = getUnDataPermissionData();
		if (objs == null || objs.length == 0) {
			return;
		}
		String errmsg = IShowMsgConstant.getDataPermissionInfo();
		for (Object obj : objs) {
			this.getOperData().addErrLogMessage(obj, errmsg);
		}
	}

	/**
	 * @return
	 */
	protected boolean needCheckPermission() {
		return (MMStringUtil.isEmptyWithTrim(getOperateCode()) && MMStringUtil
				.isEmptyWithTrim(getMdOperateCode()))
				|| MMStringUtil.isEmptyWithTrim(getResourceCode());
	}

	private void checkManagerModel() {
		for (Object obj : getSelectedOperaDatas()) {
			if (isNotManageable(obj)) {
				String errmsg = ManageModeUtil.getDisManageableMsg(getModel()
						.getContext().getNodeType());
				this.getOperData().addErrLogMessage(obj, errmsg);
			}
		}
	}

	protected boolean isNotManageable(Object obj) {
		return !ManageModeUtil.manageable(obj, getModel().getContext());
	}

	protected abstract void checkStateEnable();

	protected void buildValueObjWithErrLog() {
		MMGPValueObjWithErrLog value = new MMGPValueObjWithErrLog(
				getSelectedOperaDatas());
		setOperData(value);
	}

	public AbstractUIAppModel getModel() {
		return model;
	}

	public void setModel(AbstractUIAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public MMGPValueObjWithErrLog getOperData() {
		return operData;
	}

	public void setOperData(MMGPValueObjWithErrLog operData) {
		this.operData = operData;
	}

	public String getMdOperateCode() {
		return mdOperateCode;
	}

	public void setMdOperateCode(String mdOperateCode) {
		this.mdOperateCode = mdOperateCode;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public IBDObject getIBDObjetct(SuperVO vo) {
		if (bdObjectFactory == null) {
			bdObjectFactory = new MMGPObjectAdpaterFactory(vo.getClass()
					.getName());
		}
		return bdObjectFactory.createBDObject(vo);

	}

}