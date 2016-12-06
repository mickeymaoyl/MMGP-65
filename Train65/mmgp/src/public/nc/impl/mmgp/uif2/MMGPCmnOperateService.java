package nc.impl.mmgp.uif2;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.bd.baseservice.busilog.BDBusiLogUtil;
import nc.bs.bd.baseservice.busilog.IBusiOperateConst;
import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mmgp.common.CommonUtils;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFrameworkUtil;
import nc.bs.uif2.validation.Validator;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.itf.mmgp.uif2.IMMGPCmnOperateService;
import nc.itf.pubapp.pub.smart.IBillMaintainService;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.IMDPersistenceService;
import nc.md.util.MDUtil;
import nc.vo.bd.pub.DistributedAddBaseValidator;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.pub.SingleDistributedDeleteValidator;
import nc.vo.mmgp.batch.MMGPBatchVOUtil;
import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.util.AuditInfoUtil;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.BDReferenceChecker;
import nc.vo.util.BDUniqueRuleValidate;
import nc.vo.util.BDVersionValidationUtil;
import nc.vo.util.bizlock.BizlockDataUtil;

import org.apache.commons.lang.ArrayUtils;

/**
 * <b>基本档案默认操作基类</b>
 * <p>
 * 实现对档案的保存、启用停用、删除等通用方法.</br> 支持各操作的业务事件.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @since NC V6.3
 * @author wangweiu
 */
public class MMGPCmnOperateService implements IMMGPCmnOperateService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cmnDeleteBillData(Object data) throws BusinessException {
		try {
			lockData(data);
			checkTs(data);
			deleteValidate(data);

			fireBeforeDeleteEvent(data);

			deleteImpl(data);

			fireAfterDeleteEvent(data);
			// business log
			writeDeletedBusiLog(data);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}

	}

	/**
	 * 删除校验.
	 * 
	 * @param data
	 *            the data
	 * @throws ValidationException
	 *             the validation exception
	 */
	protected void deleteValidate(Object data) throws ValidationException {
		IValidationService validationService = ValidationFrameworkUtil
				.createValidationService(createDeleteValidator());
		validateImpl(data, validationService);
	}

	/**
	 * 创建删除校验类.
	 * 
	 * @return the validator[]
	 */
	protected Validator[] createDeleteValidator() {
		Validator[] validators = new Validator[] {
				BDReferenceChecker.getInstance(),
				new SingleDistributedDeleteValidator() };
		return validators;
	}

	/**
	 * 删除实现方法.
	 * 
	 * @param data
	 *            the data
	 * @throws MetaDataException
	 *             the meta data exception
	 */
	protected void deleteImpl(Object data) throws MetaDataException {
		IMDPersistenceService service = NCLocator.getInstance().lookup(
				IMDPersistenceService.class);
		service.deleteBill(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T cmnSaveBillData(T data) throws BusinessException {
		try {

			/* Oct 29, 2013 wangweir 查找子类实现的接口 Begin */
			Class<IMMGPCmnOperateService> interfaceToCall = IMMGPCmnOperateService.class;
			for (Class<?> oneInterface : this.getClass().getInterfaces()) {
				if (IMMGPCmnOperateService.class.isAssignableFrom(oneInterface)) {
					interfaceToCall = (Class<IMMGPCmnOperateService>) oneInterface;
					break;
				}
			}
			/* Oct 29, 2013 wangweir End */

			if (this.isNewData(data)) {
				return NCLocator.getInstance().lookup(interfaceToCall)
						.cmnInsertBillData(data);
			} else {
				return NCLocator.getInstance().lookup(interfaceToCall)
						.cmnUpdateBillData(data);
			}
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	/**
	 * 保存的内部方法，此处主要是为了记录业务日志。
	 * 
	 * @param data
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	protected <T> T saveDataInner(T data) throws BusinessException {
		Object savedData = null;

		if (data instanceof IBill) {
			/* May 24, 2013 wangweir 由于前台传递的差异VO此处改为补全前台差异VO再持久化 Begin */
			IBill[] fullBills = null;
			if (this.isNewData(data)) {
				BizlockDataUtil.lockDataByBizlock(data);
				fullBills = (IBill[]) MMArrayUtil.toArray(data);
			} else {
				// 加锁 + 检查ts
				BillTransferTool<IBill> transTool = new BillTransferTool<IBill>(
						(IBill[]) MMArrayUtil.toArray(data));
				// 补全前台VO
				fullBills = transTool.getClientFullInfoBill();
				BizlockDataUtil.lockDataByBizlock((Object[]) fullBills);
			}

			saveValidate(fullBills[0]);
			setDefaultData(fullBills[0]);

			fireBeforeSaveEvent(fullBills[0]);

			if (this.isNewData(fullBills[0])) {
				savedData = saveImpl(fullBills[0]);
			} else {
				savedData = NCLocator.getInstance()
						.lookup(IBillMaintainService.class)
						.update(fullBills[0]);
			}
		} else {
			lockData(data);
			checkTs(data);
			saveValidate(data);
			setDefaultData(data);
			fireBeforeSaveEvent(data);
			savedData = saveImpl(data);
		}

		fireAfterSaveEvent(savedData);
		// business log
		writeSaveBusiLog(savedData);

		return (T) savedData;
	}

	/**
	 * TS校验.
	 * 
	 * @param data
	 *            the data
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void checkTs(Object data) throws BusinessException {
		BDVersionValidationUtil.validateObject(data);
	}

	/**
	 * 保存校验.
	 * 
	 * @param data
	 *            the data
	 * @throws ValidationException
	 *             the validation exception
	 */
	protected void saveValidate(Object data) throws ValidationException {
		IValidationService validationService = ValidationFrameworkUtil
				.createValidationService(createSaveValidator());

		validateImpl(data, validationService);
	}

	/**
	 * Validate impl.
	 * 
	 * @param data
	 *            the data
	 * @param validationService
	 *            the validation service
	 * @throws ValidationException
	 *             the validation exception
	 */
	protected void validateImpl(Object data,
			IValidationService validationService) throws ValidationException {
		if (data instanceof AggregatedValueObject) {
			AggregatedValueObject aggVO = (AggregatedValueObject) data;
			if (aggVO.getParentVO() != null) {
				validationService.validate(aggVO.getParentVO());
			} else {
				validationService.validate(aggVO.getChildrenVO());
			}
		} else {
			validationService.validate(data);
		}
	}

	/**
	 * Lock data.
	 * 
	 * @param data
	 *            the data
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void lockData(Object data) throws BusinessException {
		if (isNewData(data)) {
			BizlockDataUtil.lockDataByBizlock(data);
			return;
		}
		BDPKLockUtil.lockObject(data);

		BizlockDataUtil.lockDataByBizlock(data);
	}

	/**
	 * Save impl.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param data
	 *            the data
	 * @return the t
	 * @throws MetaDataException
	 *             the meta data exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> T saveImpl(T data) throws MetaDataException {
		IMDPersistenceService service = NCLocator.getInstance().lookup(
				IMDPersistenceService.class);
		String pk = service.saveBill(data);
		IMDPersistenceQueryService queryService = NCLocator.getInstance()
				.lookup(IMDPersistenceQueryService.class);
		NCObject ncobj = queryService.queryBillOfNCObjectByPKWithDR(
				data.getClass(), pk, true);
		return (T) (ncobj == null ? null : ncobj.getContainmentObject());
	}

	/**
	 * Sets the default data.
	 * 
	 * @param data
	 *            the new default data
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void setDefaultData(Object data) throws BusinessException {
		SuperVO vo = getValiVO(data);
		if (isNewData(data)) {
			AuditInfoUtil.addData(vo);
			CommonUtils.setPk_org_v(vo);
			/* Jun 27, 2013 wangweir 设置创建人、创建日期 Begin */
			CommonUtils.setBillmakerAndDate(vo);
			/* Jun 27, 2013 wangweir End */
		} else {
			AuditInfoUtil.updateData(vo);
		}
	}

	/**
	 * Checks if is new data.
	 * 
	 * @param data
	 *            the data
	 * @return true, if is new data
	 * @throws MetaDataException
	 *             the meta data exception
	 */
	protected boolean isNewData(Object data) throws MetaDataException {
		IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(
				data.getClass().getName());
		String pkFieldName = bean.getTable().getPrimaryKeyName();
		NCObject ncObj = NCObject.newInstance(data);
		Object pkObj = ncObj.getAttributeValue(bean
				.getAttributeByName(pkFieldName));
		return pkObj == null;
	}

	/**
	 * Creates the save validator.
	 * 
	 * @return the validator[]
	 */
	protected Validator[] createSaveValidator() {
		Validator[] validators = new Validator[] { new BDUniqueRuleValidate(),
				new DistributedAddBaseValidator() };
		return validators;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cmnDeleteBillDataFromDB(Object data) throws BusinessException {
		try {
			lockData(data);
			checkTs(data);
			deleteValidate(data);
			fireBeforeDeleteEvent(data);
			deleteFromDBImpl(data);
			fireAfterDeleteEvent(data);
			writeDeletedBusiLog(data);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	/**
	 * Delete from db impl.
	 * 
	 * @param data
	 *            the data
	 * @throws MetaDataException
	 *             the meta data exception
	 */
	protected void deleteFromDBImpl(Object data) throws MetaDataException {
		IMDPersistenceService service = NCLocator.getInstance().lookup(
				IMDPersistenceService.class);
		service.deleteBillFromDB(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T cmnDisableBillData(T data) throws BusinessException {
		try {
			lockData(data);
			checkTs(data);

			fireBeforeDisableEvent(data);
			MMGPVoUtils.setHeadVOFieldValue(data,
					IBaseServiceConst.ENABLESTATE_FIELD,
					IPubEnumConst.ENABLESTATE_DISABLE);
			T savedData = saveImpl(data);

			fireAfterDisableEvent(savedData);
			// business log
			writeDisableBusiLog(savedData);

			return savedData;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnEnableBillData(java.lang.Object
	 * )
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T cmnEnableBillData(T data) throws BusinessException {
		try {
			lockData(data);
			checkTs(data);

			fireBeforeEnableEvent(data);
			MMGPVoUtils.setHeadVOFieldValue(data,
					IBaseServiceConst.ENABLESTATE_FIELD,
					IPubEnumConst.ENABLESTATE_ENABLE);
			T savedData = saveImpl(data);

			fireAfterEnableEvent(savedData);
			// business log
			writeEnableBusiLog(savedData);

			return savedData;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ===================================================================================
	/**
	 * Fire before save event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireBeforeSaveEvent(Object obj) throws BusinessException {
		if (isNewData(obj)) {
			this.fireBeforeInsertEvent(obj);
		} else {
			this.fireBeforeUpdateEvent(obj);
		}
	}

	/**
	 * Fire after save event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireAfterSaveEvent(Object obj) throws BusinessException {
		if (obj == null) {
			return;
		}
		if (isNewData(obj)) {
			this.fireAfterInsertEvent(obj);
		} else {
			this.fireAfterUpdateEvent(obj);
		}
	}

	/**
	 * Write save busi log.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void writeSaveBusiLog(Object obj) throws BusinessException {
		if (isNewData(obj)) {
			this.writeInsertBusiLog(obj);
		} else {
			this.writeUpdateBusiLog(obj);
		}
	}

	/**
	 * Fire before insert event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireBeforeInsertEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchInsertBeforeEvent(obj);
	}

	/**
	 * Fire after insert event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireAfterInsertEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchInsertAfterEvent(obj);
	}

	/**
	 * Write insert busi log.
	 * 
	 * @param vo
	 *            the vo
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void writeInsertBusiLog(Object vo) throws BusinessException {
		BDBusiLogUtil busiLogUtil = new BDBusiLogUtil(getBeanID(vo));
		busiLogUtil.writeBusiLog(IBusiOperateConst.ADD, null,
				convertToSuperVOArray(vo));
	}

	/**
	 * Fire before update event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireBeforeUpdateEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchUpdateBeforeEvent(obj);
	}

	/**
	 * Fire after update event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireAfterUpdateEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchUpdateAfterEvent(obj);
	}

	/**
	 * Write update busi log.
	 * 
	 * @param vo
	 *            the vo
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void writeUpdateBusiLog(Object vo) throws BusinessException {
		BDBusiLogUtil busiLogUtil = new BDBusiLogUtil(getBeanID(vo));
		busiLogUtil.writeBusiLog(IBusiOperateConst.EDIT, null,
				convertToSuperVOArray(vo));
	}

	/**
	 * Fire before delete event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireBeforeDeleteEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchDeleteBeforeEvent(obj);
	}

	/**
	 * Fire after delete event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireAfterDeleteEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchDeleteAfterEvent(obj);
	}

	/**
	 * Write deleted busi log.
	 * 
	 * @param vo
	 *            the vo
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void writeDeletedBusiLog(Object vo) throws BusinessException {
		BDBusiLogUtil busiLogUtil = new BDBusiLogUtil(getBeanID(vo));
		busiLogUtil.writeBusiLog(IBusiOperateConst.DELETE, null,
				convertToSuperVOArray(vo));
	}

	// 启用-停用
	/**
	 * Fire before enable event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireBeforeEnableEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchDisableToEnableBeforeEvent(obj);
	}

	/**
	 * Fire after enable event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireAfterEnableEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchDisableToEnableAfterEvent(obj);
	}

	/**
	 * Write enable busi log.
	 * 
	 * @param vo
	 *            the vo
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void writeEnableBusiLog(Object vo) throws BusinessException {
		BDBusiLogUtil busiLogUtil = new BDBusiLogUtil(getBeanID(vo));
		busiLogUtil.writeBusiLog(IBusiOperateConst.ENABLE, null,
				convertToSuperVOArray(vo));
	}

	/**
	 * Fire before disable event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireBeforeDisableEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchEnableToDisableBeforeEvent(obj);
	}

	/**
	 * Fire after disable event.
	 * 
	 * @param obj
	 *            the obj
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void fireAfterDisableEvent(Object obj) throws BusinessException {
		BDCommonEventUtil eventUtil = new BDCommonEventUtil(getBeanID(obj));
		eventUtil.dispatchEnableToDisableAfterEvent(obj);
	}

	/**
	 * Write disable busi log.
	 * 
	 * @param vo
	 *            the vo
	 * @throws BusinessException
	 *             the business exception
	 */
	protected void writeDisableBusiLog(Object vo) throws BusinessException {
		BDBusiLogUtil busiLogUtil = new BDBusiLogUtil(getBeanID(vo));
		busiLogUtil.writeBusiLog(IBusiOperateConst.DISABLE, null,
				convertToSuperVOArray(vo));
	}

	/**
	 * Convert to super vo array.
	 * 
	 * @param obj
	 *            the obj
	 * @return the super v o[]
	 * @throws BusinessException
	 *             the business exception
	 */
	protected SuperVO[] convertToSuperVOArray(Object obj)
			throws BusinessException {
		SuperVO[] superVOs;
		if (obj.getClass().isArray()) {
			Object[] objs = (Object[]) obj;
			superVOs = new SuperVO[objs.length];
			for (int i = 0; i < objs.length; i++) {
				superVOs[i] = getValiVO(objs[i]);
			}
		} else {
			superVOs = new SuperVO[] { getValiVO(getValiVO(obj)) };
		}
		return superVOs;
	}

	/**
	 * Gets the vali vo.
	 * 
	 * @param obj
	 *            the obj
	 * @return the vali vo
	 * @throws BusinessException
	 *             the business exception
	 */
	private SuperVO getValiVO(Object obj) throws BusinessException {
		if (obj.getClass().isArray()) {
			throw new BusinessException();
		}
		if (obj instanceof AggregatedValueObject) {
			return (SuperVO) ((AggregatedValueObject) obj).getParentVO();
		} else {
			return (SuperVO) obj;
		}
	}

	/**
	 * Gets the bean id.
	 * 
	 * @param obj
	 *            the obj
	 * @return the bean id
	 * @throws BusinessException
	 *             the business exception
	 */
	protected String getBeanID(Object obj) throws BusinessException {
		if (obj.getClass().isArray()) {
			Object[] objs = (Object[]) obj;
			return getIbean(objs[0]).getID();
		} else {
			return getIbean(obj).getID();
		}
	}

	/**
	 * Gets the ibean.
	 * 
	 * @param obj
	 *            the obj
	 * @return the ibean
	 * @throws BusinessException
	 *             the business exception
	 */
	protected IBean getIbean(Object obj) throws BusinessException {
		return MDBaseQueryFacade.getInstance().getBeanByFullClassName(
				obj.getClass().getName());
	}

	@Override
	public <T> T cmnSaveBillData_RequiresNew(T data) throws BusinessException {
		try {
			return this.cmnSaveBillData(data);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnInsertBillData(java.lang.Object
	 * )
	 */
	@Override
	public <T> T cmnInsertBillData(T data) throws BusinessException {
		try {
			return this.saveDataInner(data);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.itf.mmgp.uif2.IMMGPCmnOperateService#cmnUpdateBillData(java.lang.Object
	 * )
	 */
	@Override
	public <T> T cmnUpdateBillData(T data) throws BusinessException {
		try {
			return this.saveDataInner(data);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	@Override
	public <T> MMGPValueObjWithErrLog cmnManageEnableBillData(T[] datas)
			throws BusinessException {
		BDPKLockUtil.lockObject(datas);
		BDVersionValidationUtil.validateObject(datas);
		MMGPValueObjWithErrLog returnvalue = sealValidate(datas);
		Object[] enabledVOs = returnvalue.getVos();
		if (!ArrayUtils.isEmpty(enabledVOs)) {
			// 设置 VO 状态
			setVOStatus(enabledVOs);
			// 设置启用标志
			/* setEnableFlag(enabledVOs); */
			setSealFlag(enabledVOs, IPubEnumConst.ENABLESTATE_ENABLE);
			// 设置启用的审计信息
			setAuditInfo(enabledVOs);
			// 通知
			fireBeforeEnableEvent(enabledVOs);
			// 数据保存到数据库
			enabledVOs = savebatchImpl(enabledVOs);
			/*
			 * // 缓存通知 notifyVersionChangeWhenDataUpdated(enabledVOs);
			 */
			// 事件后通知
			fireAfterEnableEvent(enabledVOs);
			// 业务日志
			writeDisableBusiLog(enabledVOs);
			// 拼装反馈信息
			returnvalue.setVos(enabledVOs);

		}
		return returnvalue;
	}

	protected <T> void setAuditInfo(T[] vos) {
		for (T t : vos) {
			AuditInfoUtil.updateData(MMGPBatchVOUtil.getMainSuperVO(t));
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T[] savebatchImpl(T[] data) throws MetaDataException {
		IMDPersistenceService service = NCLocator.getInstance().lookup(
				IMDPersistenceService.class);
		NCObject[] ncobjs = getNCObject(data);
		String[] pks = service.saveBill(ncobjs);
		IMDPersistenceQueryService queryService = NCLocator.getInstance()
				.lookup(IMDPersistenceQueryService.class);
		T[] rsncobjs = (T[]) queryService.queryBillOfVOByPKs(
				data[0].getClass(), pks, false).toArray();
		return (rsncobjs == null ? null : rsncobjs);
	}

	@Override
	public <T> MMGPValueObjWithErrLog cmnManageDisableBillData(T[] datas)
			throws BusinessException {
		BDPKLockUtil.lockObject(datas);
		BDVersionValidationUtil.validateObject(datas);
		MMGPValueObjWithErrLog returnvalue = sealValidate(datas);
		Object[] disabledVOs = returnvalue.getVos();
		if (!ArrayUtils.isEmpty(disabledVOs)) {
			// 设置 VO 状态
			setVOStatus(disabledVOs);
			// 设置停用标志
			setSealFlag(disabledVOs, IPubEnumConst.ENABLESTATE_DISABLE);
			// 设置停用的审计信息
			setAuditInfo(disabledVOs);
			// 通知
			fireBeforeDisableEvent(disabledVOs);
			// 数据保存到数据库
			disabledVOs = savebatchImpl(disabledVOs);
			/*
			 * // 缓存通知 notifyVersionChangeWhenDataUpdated(disabledVOs);
			 */
			// 事件后通知
			fireAfterDisableEvent(disabledVOs);
			// 业务日志
			writeDisableBusiLog(disabledVOs);
			// 拼装反馈信息
			returnvalue.setVos(disabledVOs);

		}
		return returnvalue;

	}

	protected <T> MMGPValueObjWithErrLog sealValidate(T[] data) {
		MMGPValueObjWithErrLog valueLog = new MMGPValueObjWithErrLog();
		valueLog.setVos(data);
		return valueLog;
	}

	protected <T> void setVOStatus(T[] disabledVOs) throws BusinessException {
		for (T t : disabledVOs) {
			MMGPVoUtils.setHeadVOStatus(t, VOStatus.UPDATED);
		}

	}

	protected static NCObject[] getNCObject(Object[] billVos)
			throws MetaDataException {
		if (billVos == null || billVos.length == 0)
			return null;
		NCObject[] ncObjs = new NCObject[billVos.length];
		IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(
				billVos[0].getClass().getName());
		if (!MDUtil.isEntityType(bean))
			throw new MetaDataException(NCLangResOnserver.getInstance()
					.getStrByID("mdbusi", "mdPersistUtil-0001")/*
																 * 要保存的SuperVO对应的元数据不是实体类型
																 * ，
																 * 请检查模型！beanName
																 * :
																 */
					+ bean.getFullName());
		for (int i = 0; i < billVos.length; i++) {
			ncObjs[i] = NCObject.newInstance(bean, billVos[i]);
		}
		return ncObjs;
	}

	protected <T> void setSealFlag(T[] vos, int flag) throws BusinessException {
		for (T t : vos) {
			MMGPVoUtils.setHeadVOFieldValue(t,
					IBaseServiceConst.ENABLESTATE_FIELD,flag);
		}
	}

}
