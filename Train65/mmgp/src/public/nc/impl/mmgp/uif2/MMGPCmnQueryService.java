package nc.impl.mmgp.uif2;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.database.IDQueryBuilder;
import nc.itf.mmgp.uif2.IMMGPCmnQueryService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pubitf.rbac.IDataPermissionPubService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.IBDObject;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.lazilyload.BillLazilyLoaderVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;

/**
 * <b>通用查询服务</b>
 * <p>
 * MMGP提供的公共查询服务，对元数据查询方式做了包装.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @since NC V6.3
 * @author wangweiu
 */
public class MMGPCmnQueryService implements IMMGPCmnQueryService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] cmnQueryAllDatas(Class<?> clazz) throws BusinessException {
		try {
			return queryDatasByConditionInner(clazz, "dr=0");
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] cmnQueryDatasByCondition(Class<?> clazz, String con)
			throws BusinessException {
		try {
			return queryDatasByConditionInner(clazz, con);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	/**
	 * Query datas by condition inner.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param con
	 *            the con
	 * @return the object[]
	 * @throws MetaDataException
	 *             the meta data exception
	 */
	private Object[] queryDatasByConditionInner(Class<?> clazz, String con)
			throws MetaDataException {
		if (MMStringUtil.isEmpty(con)) {
			con = " 1 = 1 ";
		}
		Collection<Object> objs = queryObjects(clazz, con, true, false);
		return collectionToArray(objs, clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	public IBill cmnQueryChildrenByParentPK(Class<?> clazz, String parentPk)
			throws BusinessException {
		try {
			IMDPersistenceQueryService queryService = NCLocator.getInstance()
					.lookup(IMDPersistenceQueryService.class);
			IBill aggVO = (IBill) queryService.queryBillOfNCObjectByPKWithDR(
					clazz, parentPk, true).getContainmentObject();

			return aggVO;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> cmnQueryChildrenByParent(
			Map<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>> map)
			throws BusinessException {

		Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> resultMap = new HashMap<String, Map<Class<? extends ISuperVO>, SuperVO[]>>();
		try {
			for (Entry<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>> entry : map
					.entrySet()) {
				BillLazilyLoaderVO loaderVO = entry.getKey();

				Class<? extends IBill> billClz = loaderVO.getBillClass();
				String parentID = loaderVO.getPk();
				Map<Class<? extends ISuperVO>, SuperVO[]> billMap = new HashMap<Class<? extends ISuperVO>, SuperVO[]>();
				IBill aggVO = this
						.cmnQueryChildrenByParentPK(billClz, parentID);
				for (Class<? extends ISuperVO> childClazz : entry.getValue()) {
					billMap.put(childClazz,
							(SuperVO[]) aggVO.getChildren(childClazz));
				}
				resultMap.put(parentID, billMap);
			}
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return resultMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] cmnQueryDatasByConditionLazyLoad(Class<?> clazz,
			String con, boolean isLazyLoad) throws BusinessException {
		try {
			if (MMStringUtil.isEmpty(con)) {
				con = " 1 = 1 ";
			}
			Collection<Object> objs = queryObjects(clazz, con, true, isLazyLoad);
			return collectionToArray(objs, clazz);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	/**
	 * @param objs
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object[] collectionToArray(Collection objs, Class<?> clazz) {
		if (MMCollectionUtil.isEmpty(objs)) {
			return null;
		}
		return new ListToArrayTool(clazz).convertToArray(new ArrayList(objs));
	}

	/**
	 * Query objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param con
	 *            the con
	 * @param ignoreDrEqual1
	 *            the ignore dr equal1
	 * @param bLazyLoad
	 *            the b lazy load
	 * @return the collection
	 * @throws MetaDataException
	 *             the meta data exception
	 */

	private Collection<Object> queryObjects(Class<?> clazz, String con,
			boolean ignoreDrEqual1, boolean bLazyLoad) throws MetaDataException {
		IMDPersistenceQueryService queryService = NCLocator.getInstance()
				.lookup(IMDPersistenceQueryService.class);
		List<Object> results = this.getResults(clazz, con, bLazyLoad);
		/*
		 * (List<Object>) queryService.queryBillOfVOByCondWithOrder( clazz, con,
		 * true, bLazyLoad, this.getOrderPaths(clazz));
		 */

		/* Oct 12, 2013 wangweir 加载第一行数据的表体，避免懒加载增加连接数 Begin */
		if (bLazyLoad && MMCollectionUtil.isNotEmpty(results)) {
			Object firstObj = results.get(0);
			String pk = null;
			if (firstObj instanceof IBill) {
				pk = ((IBill) firstObj).getPrimaryKey();
			} else if (firstObj instanceof SuperVO) {
				pk = ((SuperVO) firstObj).getPrimaryKey();
			}

			if (MMStringUtil.isNotEmpty(pk)) {
				NCObject firstNCObj = queryService
						.queryBillOfNCObjectByPKWithDR(clazz, pk, true);
				results.set(0, firstNCObj.getContainmentObject());
			}
		}
		/* Oct 12, 2013 wangweir End */
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] cmnQueryDatasByConditionWithDataPermission(Class<?> clazz,
			String con, String resouceCode, String operatecode)
			throws BusinessException {
		try {
			IBusinessEntity bean = (IBusinessEntity) MDQueryService
					.lookupMDQueryService().getBeanByFullClassName(
							clazz.getName());
			String tableName = bean.getTable().getName();
			String pkField = bean.getPrimaryKey().getPKColumn().getName();
			String powerSql = getPowerSql(tableName + "." + pkField,
					resouceCode, operatecode);
			String newCondition;
			if (MMStringUtil.isEmpty(powerSql)) {
				newCondition = con;

			} else {
				if (MMStringUtil.isEmpty(con)) {
					newCondition = powerSql;
				} else {
					newCondition = con + " and " + powerSql;
				}
			}

			return cmnQueryDatasByCondition(clazz, newCondition);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	/**
	 * Gets the power sql.
	 * 
	 * @param joinColumn
	 *            the join column
	 * @param resouceCode
	 *            the resouce code
	 * @param operatecode
	 *            the operatecode
	 * @return the power sql
	 */
	private String getPowerSql(String joinColumn, String resouceCode,
			String operatecode) {
		String powerSql = null;
		String pk_user = AppContext.getInstance().getPkUser();
		String pk_group = AppContext.getInstance().getPkGroup();

		if (resouceCode == null) {
			return null;
		}
		try {
			// DataPermSplitTableConfig config = null;

			IDataPermissionPubService queryService = NCLocator.getInstance()
					.lookup(IDataPermissionPubService.class);
			String sqlWherePart = queryService.getDataPermissionSQLWherePart(
					pk_user, resouceCode, operatecode, pk_group);
			// try {
			// config = queryService.getDataPermSplitConfig(pk_user, pk_group);
			// } catch (BusinessException be) {
			// Logger.error(be.getMessage(), be);
			// }
			//
			// String dataPermProfileTableName = config
			// .getDataPermSplitTableNameByResourceCode(resouceCode,
			// operatecode);
			// TODO
			if (!MMStringUtil.isEmptyWithTrim(sqlWherePart)) {
				powerSql = sqlWherePart;

			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);

		}
		return powerSql;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object cmnQueryDatasByPk(Class<?> clazz, String pk)
			throws BusinessException {
		try {
			IMDPersistenceQueryService queryService = NCLocator.getInstance()
					.lookup(IMDPersistenceQueryService.class);
			NCObject ncObj = queryService.queryBillOfNCObjectByPKWithDR(clazz,
					pk, true);

			if (ncObj == null) {
				return null;
			}
			if (AggregatedValueObject.class.isAssignableFrom(clazz)) {
				return ncObj.getContainmentObject();
			} else {
				return ncObj.getModelConsistObject();
			}
			// return queryService.queryBillOfVOByPK(clazz, pk, false);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object[] cmnQueryDatasByScheme(Class<? extends AbstractBill> clazz,
			IQueryScheme queryScheme, String billTypeCode)
			throws BusinessException {
		try {
			QuerySchemeProcessor qrySchemeProcessor = new QuerySchemeProcessor(
					queryScheme);
			// 固定查询条件
			qrySchemeProcessor.appendCurrentGroup();
			// 查询有权限的组织
			qrySchemeProcessor.appendFuncPermissionOrgSql();

			// 添加排序
			String orderByString = getOrderString(clazz, queryScheme);

			// 查询结果
			AbstractBill[] vos = (AbstractBill[]) new BillLazyQuery(clazz)
					.query(queryScheme, orderByString);

			// 过滤状态
			return MMGPApproveFilter.filterForApprove(queryScheme, vos,
					billTypeCode);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	protected String getOrderString(Class<?> clazz, IQueryScheme queryScheme)
			throws MetaDataException {
		QuerySchemeProcessor qrySchemeProcessor = new QuerySchemeProcessor(
				queryScheme);
		String mainTableAlias = qrySchemeProcessor.getMainTableAlias();

		String[] orderPath = this.getOrderPaths(clazz);

		if (MMArrayUtil.isEmpty(orderPath)) {
			return null;
		}

		SqlBuilder orderBy = new SqlBuilder();
		orderBy.append(" order by ");

		for (int i = 0; i < orderPath.length; i++) {
			if (i == orderPath.length - 1) {
				orderBy.append(mainTableAlias + "." + orderPath[i]);
			} else {
				orderBy.append(mainTableAlias + "." + orderPath[i] + ", ");
			}
		}

		return orderBy.toString();
	}

	/**
	 * @throws MetaDataException
	 */
	protected String[] getOrderPaths(Class<?> clazz) throws MetaDataException {
		IBusinessEntity bean = (IBusinessEntity) MDQueryService
				.lookupMDQueryService().getBeanByFullClassName(clazz.getName());

		List<String> orderPath = new ArrayList<String>();
		String pkOrg = MMMetaUtils.getBizInterfaceMapInfo(
				IBDObject.class.getName(), bean, MMGlobalConst.PK_ORG);
		if (MMStringUtil.isNotEmpty(pkOrg)) {
			orderPath.add(pkOrg);
		}

		String billNo = MMMetaUtils
				.getBizInterfaceMapInfo(IFlowBizItf.class.getName(), bean,
						IFlowBizItf.ATTRIBUTE_BILLNO);
		if (MMStringUtil.isNotEmpty(billNo)) {
			orderPath.add(billNo);
		} else if (MMStringUtil.isNotEmpty(MMMetaUtils.getCodeFieldName(bean))) {
			orderPath.add(MMMetaUtils.getCodeFieldName(bean));
		}
		return orderPath.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.itf.mmgp.uif2.IMMGPCmnQueryService#cmnQueryDatasByPks(java.lang.Class,
	 * java.lang.String[])
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object[] cmnQueryDatasByPks(Class<?> clazz, String[] pks)
			throws BusinessException {
		try {
			IBean bean = MDBaseQueryFacade.getInstance()
					.getBeanByFullClassName(clazz.getName());
			String pkName = bean.getTable().getName() + "."
					+ bean.getTable().getPrimaryKeyName();
			IDQueryBuilder idSqlBuilder = new IDQueryBuilder();
			String sql = idSqlBuilder.buildSQL(pkName, pks);
			Collection result = NCLocator
					.getInstance()
					.lookup(IMDPersistenceQueryService.class)
					.queryBillOfVOByCondWithOrder(clazz, sql, true, false,
							this.getOrderPaths(clazz));
			return this.collectionToArray(result, clazz);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	/*
	 * 根据批量PK查询单据，返回实际的VO数组，与parentPks顺序对应，查不到返回null，可指定需要加载的子表 add by zhangyyp
	 * 注意：该方法返回值不会过滤dr=1的数据，所以表体所有数据
	 */
	public Object[] cmnQuerySpecChildrenByParentPK(Class<?> clazz,
			String[] parentPks, String[] subEntityName)
			throws BusinessException {
		try {

			Object[] aggVOs = getQueryService().queryBillOfVOByPKsWithOrder(
					clazz, parentPks, subEntityName);
			return aggVOs;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	/**
	 * add by zhangyyp 根据条件查询数据，懒加载只查询第一行数据指定的子表
	 * 
	 * @param clazz
	 * @param con
	 * @param isLazyLoad
	 * @param subEntityName
	 * @return
	 * @throws BusinessException
	 */
	public Object[] cmnQuerySpecDatasByConditionLazyLoad(Class<?> clazz,
			String con, boolean isLazyLoad, String[] subEntityName)
			throws BusinessException {
		try {
			if (MMStringUtil.isEmpty(con)) {
				con = " 1 = 1 ";
			}
			Collection<Object> objs = queryObjects(clazz, con, true,
					isLazyLoad, subEntityName);
			return collectionToArray(objs, clazz);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];
	}

	/**
	 * add by zhangyyp
	 * 
	 * @param clazz
	 * @param con
	 * @param ignoreDrEqual1
	 * @param bLazyLoad
	 * @param subEntityName
	 * @return
	 * @throws MetaDataException
	 */
	private Collection<Object> queryObjects(Class<?> clazz, String con,
			boolean ignoreDrEqual1, boolean bLazyLoad, String[] subEntityName)
			throws MetaDataException {
		IMDPersistenceQueryService queryService = getQueryService();
		List<Object> results = getResults(clazz, con, bLazyLoad);
		/* Oct 12, 2013 wangweir 加载第一行数据的表体，避免懒加载增加连接数 Begin */
		if (bLazyLoad && MMCollectionUtil.isNotEmpty(results)) {
			String pk = getFirstPk(results);
			if (MMStringUtil.isNotEmpty(pk)) {
				/* 2014-4-14 add by zhangyyp 换个接口查询指定的子实体,避免多表体查询报错 */
				Object[] firstNCObj = queryService.queryBillOfVOByPKsWithOrder(
						clazz, new String[] { pk }, subEntityName);
				results.set(0, firstNCObj[0]);
			}
		}
		/* Oct 12, 2013 wangweir End */
		return results;
	}

	private IMDPersistenceQueryService getQueryService() {
		return NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
	}

	/**
	 * 根据where条件查询单据，返回实际的VO集合(List)
	 * 
	 * @param clazz
	 * @param con
	 * @param bLazyLoad
	 * @return
	 * @throws MetaDataException
	 */
	@SuppressWarnings("unchecked")
	private List<Object> getResults(Class<?> clazz, String con,
			boolean bLazyLoad) throws MetaDataException {
		return (List<Object>) getQueryService().queryBillOfVOByCondWithOrder(
				clazz, con, true, bLazyLoad, this.getOrderPaths(clazz));
	}

	private String getFirstPk(List<Object> results) {
		if (MMCollectionUtil.isEmpty(results)) {
			return null;
		}
		Object firstObj = results.get(0);
		String pk = null;
		if (firstObj instanceof IBill) {
			pk = ((IBill) firstObj).getPrimaryKey();
		} else if (firstObj instanceof SuperVO) {
			pk = ((SuperVO) firstObj).getPrimaryKey();
		}
		return pk;

	}

	/** 懒加载调用的根据父项pk查询指定子项的方法 - add by zhangyyp **/
	public Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> cmnQuerySpecChildrenByParent(
			Map<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>> map,
			String[] subEntityName) throws BusinessException {
		Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> resultMap = new HashMap<String, Map<Class<? extends ISuperVO>, SuperVO[]>>();
		try {
			for (Entry<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>> entry : map
					.entrySet()) {
				BillLazilyLoaderVO loaderVO = entry.getKey();

				Class<? extends IBill> billClz = loaderVO.getBillClass();
				String parentID = loaderVO.getPk();
				Map<Class<? extends ISuperVO>, SuperVO[]> billMap = new HashMap<Class<? extends ISuperVO>, SuperVO[]>();
				// 此处通过父项pk查询指定子表的数据
				Object[] aggVOs = this.cmnQuerySpecChildrenByParentPK(billClz,
						new String[] { parentID }, subEntityName);
				IBill aggVO = (IBill) aggVOs[0];
				for (Class<? extends ISuperVO> childClazz : entry.getValue()) {
					billMap.put(childClazz,
							(SuperVO[]) aggVO.getChildren(childClazz));
				}
				resultMap.put(parentID, billMap);
			}
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return resultMap;
	}

	/**
	 * 通过查询条件查询数据
	 * bLazyLoad 为true  不加载子表数据
	 * @param clazz
	 * @param con
	 *            查询条件
	 * @param bLazyLoad
	 *            是否懒加载
	 * @return List<Object> 返回vo
	 * @throws BusinessException
	 */
	public Object[] cmnQueryDatasByConditionLazyLoad2(Class<?> clazz, String con,
			Boolean bLazyLoad) throws BusinessException {
		try {
			if (MMStringUtil.isEmpty(con)) {
				con = " 1 = 1 ";
			}
			Collection<Object> objs=this.getResults(clazz, con, bLazyLoad);
			return  collectionToArray(objs, clazz) ;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return new Object[0];

	}
}
