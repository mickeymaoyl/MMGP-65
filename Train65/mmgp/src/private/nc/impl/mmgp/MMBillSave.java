package nc.impl.mmgp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.business.NullBDBusiCheck;
import nc.bs.trade.comsave.ComSave;
import nc.bs.trade.comsave.IBillSave;
import nc.bs.trade.comsave.IQueryAfterSave;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.trade.pub.IBDGetCheckClass;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.pub.IRetCurrentDataAfterSave;
import nc.vo.trade.pub.IServerSideFactory;
/**
 * 
 * <b> 单据VO保存服务 </b>
 * <p>
 *     实现对档案或单据VO的保存。
 * </p>
 * 创建日期:2012-11-23
 * @author wangweiu
 * @deprecated 推荐使用元数据，会被删除
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class MMBillSave extends ComSave implements IBillSave {
	/**
	 * BillSave 构造子注解。
	 */
	public MMBillSave() {
		super();
	}

	/**
     * 处理子表数据（除去删除的)。 创建日期：(2004-2-27 21:27:21).
     * 
     * @param items
     *        nc.vo.pub.SuperVO[]
     * @return the super v o[]
     * @throws BusinessException
     *         the business exception
     */
	private SuperVO[] dealChildVO(SuperVO[] items) throws BusinessException {
		if (items == null)
			return null;
		Vector v = new Vector();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getStatus() != VOStatus.DELETED)
				v.addElement(items[i]);
		}
		if (v.size() > 0) {
			SuperVO[] vos = (SuperVO[]) java.lang.reflect.Array.newInstance(
					items.getClass().getComponentType(), v.size());
			v.copyInto(vos);
			return vos;
		}
		return null;
	}

	private boolean isNew(AggregatedValueObject billVO,
			boolean isHeadVOStatusCheck) throws BusinessException {
		if (isHeadVOStatusCheck) {
			if (billVO.getParentVO().getStatus() == VOStatus.NEW) {
				return true;
			} else {
				return false;
			}
		}
		return billVO.getParentVO().getPrimaryKey() == null
				|| billVO.getParentVO().getPrimaryKey().length() == 0;

	}

	private boolean isNew(AggregatedValueObject billVO) throws BusinessException {
		return billVO.getParentVO().getPrimaryKey() == null
				|| billVO.getParentVO().getPrimaryKey().length() == 0;
	}

	/**
	 * 保存单据VO(AggreValueObject)数据。 返回：ArrayList:第0：主表主键，依次子表主键 创建日期：(2003-5-23
	 * 7:49:37) 樊冠军 2003-05-23 01
	 */
	private AggregatedValueObject saveBaseDoc(AggregatedValueObject billVo,
			boolean isQueryData) throws BusinessException {
		if (billVo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000039")/*
														 * @res
														 * "警告：saveBD得到的参数为null，未能保存任何数据"
														 */);

		if (billVo.getParentVO() == null) {
			// System.out.println("警告：saveBill得到的参数的表头VO未null，未能保存任何数据");
			return saveBodyVOs(billVo, isQueryData);
		}

		return saveBillCom(billVo);
	}
	
	private AggregatedValueObject saveBaseDoc(AggregatedValueObject billVo,
			boolean isQueryData, boolean isHeadVOStatusCheck) throws BusinessException {
		if (billVo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000039")/*
														 * @res
														 * "警告：saveBD得到的参数为null，未能保存任何数据"
														 */);

		if (billVo.getParentVO() == null) {
			// System.out.println("警告：saveBill得到的参数的表头VO未null，未能保存任何数据");
			return saveBodyVOs(billVo, isQueryData);
		}

		return saveBillCom(billVo, isHeadVOStatusCheck);
	}
	

	
	
	public nc.vo.pub.AggregatedValueObject saveBD(
			nc.vo.pub.AggregatedValueObject billVo, java.lang.Object userObj, boolean isHeadVOStatusCheck)
			throws BusinessException{

		try {

			// 如果UserObj实现了新的接口
			if (userObj instanceof IServerSideFactory) {
				return saveBD_new(billVo, userObj);
			}

			if (userObj != null) {
				IBDBusiCheck bdbusi = getBDBusiCheckInstance(userObj);
				// 事件前的处理
				bdbusi.check(IBDACTION.SAVE, billVo, userObj);

				// 事件中处理
				if (userObj instanceof IRetCurrentDataAfterSave)
					billVo = saveBaseDoc(billVo, false);
				else
					billVo = saveBaseDoc(billVo, true);

				// 事件后处理
				bdbusi.dealAfter(IBDACTION.SAVE, billVo, userObj);

				return billVo;
			} else
				return saveBaseDoc(billVo, true, isHeadVOStatusCheck);
		} catch (BusinessException e) {
			throw e;
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000012")/* @res "未知的异常" */
					+ e.getMessage());
		}

		catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000012")/* @res "未知的异常" */, e);
		}

	
	}
	

	/**
	 * saveBD 方法注解。
	 */
	public nc.vo.pub.AggregatedValueObject saveBD(
			nc.vo.pub.AggregatedValueObject billVo, java.lang.Object userObj)
			throws BusinessException {
		try {

			// 如果UserObj实现了新的接口
			if (userObj instanceof IServerSideFactory) {
				return saveBD_new(billVo, userObj);
			}

			if (userObj != null) {
				IBDBusiCheck bdbusi = getBDBusiCheckInstance(userObj);
				// 事件前的处理
				bdbusi.check(IBDACTION.SAVE, billVo, userObj);

				// 事件中处理
				if (userObj instanceof IRetCurrentDataAfterSave)
					billVo = saveBaseDoc(billVo, false);
				else
					billVo = saveBaseDoc(billVo, true);

				// 事件后处理
				bdbusi.dealAfter(IBDACTION.SAVE, billVo, userObj);

				return billVo;
			} else
				return saveBaseDoc(billVo, true);
		} catch (BusinessException e) {
			throw e;
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000012")/* @res "未知的异常" */
					+ e.getMessage());
		}

		catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000012")/* @res "未知的异常" */, e);
		}

	}

	/**
	 * 得到IBDBusiCheck类的实例。如果传入的userObj没有实现
	 * IBDGetCheckClass接口，则返回一个NullBDBusiCheck的实例
	 * 
	 * @param userObj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private IBDBusiCheck getBDBusiCheckInstance(java.lang.Object userObj)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		IBDBusiCheck bdbusi = null;
		// 进行业务校验
		if (userObj instanceof IBDGetCheckClass) {
			IBDGetCheckClass bdchk = (IBDGetCheckClass) userObj;
			String checkClass = bdchk.getCheckClass();
			if (checkClass != null && checkClass.trim().length() != 0) {
				// Class c = Class.forName(checkClass.trim());
				// Object o = c.newInstance();
				Object o = NewObjectService.newInstance(checkClass.trim());
				Logger
						.error("nc.bs.trade.comsave.BillSave hardcode module name as \"uap\"");
				if (o instanceof IBDBusiCheck)
					bdbusi = (IBDBusiCheck) o;
			}
		}
		if (bdbusi == null)
			bdbusi = new NullBDBusiCheck();
		return bdbusi;
	}

	/**
	 * @param billVo
	 * @param userObj
	 * @return
	 * @throws Exception
	 */
	private AggregatedValueObject saveBD_new(AggregatedValueObject billVo,
			Object userObj) throws Exception {
		IServerSideFactory factory = (IServerSideFactory) userObj;

		IBDBusiCheck busiCheck = factory.getBDBusiCheckInstance();

		busiCheck.check(IBDACTION.SAVE, billVo, userObj);

		AggregatedValueObject result = saveBaseDoc_new(billVo, factory);

		busiCheck.dealAfter(IBDACTION.SAVE, result, userObj);

		return result;
	}

	/**
	 * @param billVo
	 * @param factory
	 * @return
	 */
	private AggregatedValueObject saveBaseDoc_new(AggregatedValueObject billVo,
			IServerSideFactory factory) throws Exception {
		if (billVo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000039")/*
														 * @res
														 * "警告：saveBD得到的参数为null，未能保存任何数据"
														 */);

		if (billVo.getParentVO() == null) {
			// System.out.println("警告：saveBill得到的参数的表头VO未null，未能保存任何数据");
			return saveBodyVOs_new(billVo, factory);
		}

		return saveBillCom(billVo);
	}

	/**
	 * @param billVO
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	private AggregatedValueObject saveBodyVOs_new(AggregatedValueObject billVO,
			IServerSideFactory factory) throws BusinessException {
		HYSuperDMO dmo = new HYSuperDMO();

		// 插入子表
		SuperVO[] items = (SuperVO[]) billVO.getChildrenVO();
		if (billVO instanceof IExAggVO)
			items = (SuperVO[]) ((IExAggVO) billVO).getAllChildrenVO();
		// 保存子表数据
		saveItems(dmo, items, null, null);

		AggregatedValueObject retVO = billVO;

		IQueryAfterSave queryAfterSave = factory.getQueryAfterSaveInstance();
		// 重查子表数据
		try {
			retVO.setChildrenVO(queryAfterSave.queryBodyVOsAfterSave());
		} catch (Exception e) {
			Logger.error(e);
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				throw new BusinessException(e.getMessage(), e);

			}
		}

		return retVO;

	}

	/**
	 * 单据VO保存。 创建日期：(2004-2-27 11:15:29)
	 * 
	 * @return ArrayList
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public java.util.ArrayList saveBill(nc.vo.pub.AggregatedValueObject billVo)
			throws BusinessException {
		// try {
		ArrayList retAry = new ArrayList();
		AggregatedValueObject retVo = saveBillCom(billVo);
		retAry.add(retVo.getParentVO().getPrimaryKey());
		retAry.add(retVo);
		return retAry;
		// }
		// catch (Exception e) {
		// Logger.error(e.getMessage(), e);
		// throw new BusinessException(
		// nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000012")/*@res
		// "未知的异常"*/,
		// new
		// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000040")/*@res
		// "公共BO保存单据saveBill::"*/ + e.getMessage()));
		// }
	}

	public AggregatedValueObject saveBillCom(
			nc.vo.pub.AggregatedValueObject billVo) throws BusinessException {

		if (billVo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000041")/*
														 * @res
														 * "警告：saveBill得到的参数为null，未能保存任何数据"
														 */);

		if (billVo.getParentVO() == null) {
		    Logger.error("警告：saveBill得到的参数的表头VO未null，未能保存任何数据");
			return null;
		}
		HYSuperDMO dmo = null;
		AggregatedValueObject retVO = null;
		// try
		// {

		dmo = new HYSuperDMO();
		if (isNew(billVo)) {
			retVO = saveBillWhenAdd(billVo, dmo);
		} else {
			retVO = saveBillWhenEdit(billVo, dmo);
		}
		return retVO;
		// }

		// catch (Exception e)
		// {
		// Logger.error(e.getMessage(), e);
		// throw
		// new
		// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000012")/*@res
		// "未知的异常"*/+nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000040")/*@res
		// "公共BO保存单据saveBill::"*/ + e.getMessage());
		// }

	}

	/**
	 * 单据VO保存。 创建日期：(2004-2-27 11:15:29)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 * 
	 */
	public AggregatedValueObject saveBillCom(
			nc.vo.pub.AggregatedValueObject billVo, boolean isHeadVOStatusCheck)
			throws BusinessException {
		if (billVo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000041")/*
														 * @res
														 * "警告：saveBill得到的参数为null，未能保存任何数据"
														 */);

		if (billVo.getParentVO() == null) {
		    Logger.error("警告：saveBill得到的参数的表头VO未null，未能保存任何数据");
			return null;
		}
		HYSuperDMO dmo = null;
		AggregatedValueObject retVO = null;
		// try
		// {

		dmo = new HYSuperDMO();
		if (isNew(billVo, isHeadVOStatusCheck)) {
			retVO = saveBillWhenAdd(billVo, dmo);
		} else {
			retVO = saveBillWhenEdit(billVo, dmo);
		}
		return retVO;
		// }

		// catch (Exception e)
		// {
		// Logger.error(e.getMessage(), e);
		// throw
		// new
		// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000012")/*@res
		// "未知的异常"*/+nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000040")/*@res
		// "公共BO保存单据saveBill::"*/ + e.getMessage());
		// }
	}
			
			
	private AggregatedValueObject saveBillWhenAdd(AggregatedValueObject vo,
			HYSuperDMO dmo) throws BusinessException {

		// 插入表头：
		SuperVO headVO = (SuperVO) vo.getParentVO();

		// BillCodeGenerater gen = new BillCodeGenerater();
		// gen.generaterBillCode(vo);

		headVO.setAttributeValue("dr", Integer.valueOf(0));
//		String key = dmo.insert(headVO);
		String key = new BaseDAO().insertVOWithPK(headVO);

		// 插入子表
		SuperVO[] items = (SuperVO[]) vo.getChildrenVO();
		if (vo instanceof IExAggVO) {
			items = (SuperVO[]) ((IExAggVO) vo).getAllChildrenVO();
		}
		if (items != null) {
			// 插入表体各项：
			for (int i = 0; i < items.length; i++) {
				items[i].setAttributeValue("dr", Integer.valueOf(0));
				items[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}
			saveItems(dmo, items, headVO.getPKFieldName(), key);
		}

		vo.setParentVO(dmo.queryByPrimaryKey(headVO.getClass(), key));
		return vo;
	}
			
	/**
	 * 向数据库中插入一个VO对象。
	 * 
	 * 创建日期：(2003-9-18)
	 * 
	 * @param sendmny
	 *            nc.vo.testforei.AggregatedValueObject
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * 
	 */
	private AggregatedValueObject saveBillWhenEdit(
			AggregatedValueObject billVO, HYSuperDMO dmo)
			throws BusinessException {

		SuperVO headvo = (SuperVO) billVO.getParentVO();
		String billPK = headvo.getPrimaryKey();
		String mainPKFiledName = headvo.getPKFieldName();
		headvo.setAttributeValue("dr", Integer.valueOf(0));
		dmo.update(headvo);
		boolean isMultiChild = false;
		// 插入子表
		SuperVO[] items = (SuperVO[]) billVO.getChildrenVO();
		if (billVO instanceof IExAggVO) {
			isMultiChild = true;
			items = (SuperVO[]) ((IExAggVO) billVO).getAllChildrenVO();
		}
		if (items != null)
			saveItems(dmo, items, mainPKFiledName, billPK);

		billVO.setParentVO(dmo.queryByPrimaryKey(headvo.getClass(), billPK));
		if (items != null && items.length > 0)
			setChildData(billVO, isMultiChild, dmo);
		return billVO;

	}

	/**
	 * 保存子表数据。 创建日期：(2004-02-04 11:22:19)
	 * 
	 * @return nc.vo.pub.SuperVO[]
	 * @param vos
	 *            nc.vo.pub.SuperVO[]
	 * 
	 */
	private AggregatedValueObject saveBodyVOs(AggregatedValueObject billVO,
			boolean isQueryData) throws BusinessException {
		try {
			HYSuperDMO dmo = new HYSuperDMO();

			// 插入子表
			SuperVO[] items = (SuperVO[]) billVO.getChildrenVO();
			if (billVO instanceof IExAggVO)
				items = (SuperVO[]) ((IExAggVO) billVO).getAllChildrenVO();

			saveItems(dmo, items, null, null);

			AggregatedValueObject retVO = billVO;
			if (isQueryData) {
				retVO = (AggregatedValueObject) billVO.getClass().newInstance();
				if (items.length > 0)
					if (items[0].getParentPKFieldName() != null)
						retVO.setChildrenVO(dmo.queryByWhereClause(items[0]
								.getClass(), items[0].getParentPKFieldName()
								+ "='"
								+ items[0].getAttributeValue(items[0]
										.getParentPKFieldName())
								+ "' and isnull(dr,0)=0"));

					else
						retVO.setChildrenVO(dmo.queryByWhereClause(items[0]
								.getClass(), "isnull(dr,0)=0"));
			}

			return retVO;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(
					"HYPubBO::saveBillWhenEdit(AggregatedValueObject) Exception!"
							+ " " + e.getMessage());
		}
	}

	/**
	 * 设置子表数据。 创建日期：(2004-2-27 20:59:29)
	 * 
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void setChildData(AggregatedValueObject vo, boolean isMultiChild,
			HYSuperDMO dmo) throws BusinessException {
		// 查询子表
		if (isMultiChild) {
			IExAggVO exAggVO = (IExAggVO) vo;
			for (int i = 0; i < exAggVO.getTableCodes().length; i++) {
				String tableCode = exAggVO.getTableCodes()[i];
				SuperVO[] items = (SuperVO[]) exAggVO.getTableVO(tableCode);
				exAggVO.setTableVO(tableCode, dealChildVO(items));
			}
		} else {
			SuperVO[] itemVos = (SuperVO[]) vo.getChildrenVO();
			SuperVO headVo = (SuperVO) vo.getParentVO();
			vo.setChildrenVO(dmo.queryByWhereClause(itemVos[0].getClass(),
					itemVos[0].getParentPKFieldName() + "='"
							+ headVo.getPrimaryKey() + "' and isnull(dr,0)=0"));
		}
	}

	/**
	 * saveBD 方法注解。
	 */
	public nc.vo.pub.AggregatedValueObject[] saveBDs(
			nc.vo.pub.AggregatedValueObject[] billVos, java.lang.Object userObj)
			throws BusinessException {

		if (billVos == null)
			return null;

		nc.vo.pub.AggregatedValueObject[] ret = new nc.vo.pub.AggregatedValueObject[billVos.length];
		for (int i = 0; i < billVos.length; i++) {
			ret[i] = saveBD(billVos[i], userObj);
		}

		return ret;
	}
	
	
	public nc.vo.pub.AggregatedValueObject[] saveBDs(
			nc.vo.pub.AggregatedValueObject[] billVos, java.lang.Object userObj, boolean isHeadVOStatusCheck)
			throws BusinessException {

		if (billVos == null)
			return null;

		nc.vo.pub.AggregatedValueObject[] ret = new nc.vo.pub.AggregatedValueObject[billVos.length];
		for (int i = 0; i < billVos.length; i++) {
			ret[i] = saveBD(billVos[i], userObj, isHeadVOStatusCheck);
		}

		return ret;
	}
	

	/**
	 * 单据VO[]保存,无数据状态的保存。 创建日期：(2004-2-27 11:15:29)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 * 
	 */
	public AggregatedValueObject[] saveBillComVos(
			nc.vo.pub.AggregatedValueObject[] billVos) throws BusinessException {
		if (billVos == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uffactory_hyeaa",
							"UPPuffactory_hyeaa-000042")/*
														 * @res
														 * "警告：saveBillComVos得到的参数为null，未能保存任何数据"
														 */);

		HYSuperDMO dmo = null;

		dmo = new HYSuperDMO();
		// 保存主表
		SuperVO[] headVos = new SuperVO[billVos.length];
		for (int i = 0; i < headVos.length; i++) {
			headVos[i] = (SuperVO) billVos[i].getParentVO();
		}
		String[] keyAry = dmo.insertArray(headVos);

		// 保存子表
		Vector insertVec = new Vector();
		for (int i = 0; i < billVos.length; i++) {
			SuperVO headVo = (SuperVO) billVos[i].getParentVO();
			SuperVO[] vos = (SuperVO[]) billVos[i].getChildrenVO();
			if (vos != null) {
				for (int j = 0; j < vos.length; j++) {
					// 设置外健
					vos[j]
							.setAttributeValue(headVo.getPKFieldName(),
									keyAry[i]);
					vos[j].setAttributeValue("dr", Integer.valueOf(0));
					vos[j].setStatus(VOStatus.NEW);
					insertVec.addElement(vos[j]);
				}
			}
		}
		if (insertVec.size() > 0) {
			Hashtable insertDataHas = new Hashtable();
			SuperVO itemVo = (SuperVO) insertVec.get(0);
			insertDataHas.put(itemVo.getTableName(), insertVec);
			saveItemHas(dmo, insertDataHas, null);
		}
		return billVos;

	}
}