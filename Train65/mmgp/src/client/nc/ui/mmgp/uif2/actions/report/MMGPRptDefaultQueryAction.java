package nc.ui.mmgp.uif2.actions.report;

import java.awt.Container;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nc.bcmanage.vo.ReportDBVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.report.ReportPermissionUtils;
import nc.bs.sm.accountmanage.IReportDBService;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.iufo.freereport.extend.IBusiFormat;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.itf.iufo.freereport.extend.IReportAdjustor;
import nc.pub.smart.context.ICommContextKey;
import nc.pub.smart.context.ISmartContextKey;
import nc.report.mmgp.MMGPReportConditionUtils;
import nc.report.mmgp.MMGPReportContextWrapper;
import nc.ui.iufo.freereport.extend.DefaultQueryAction;
import nc.ui.pubapp.uif2app.query2.IQueryConditionDLGInitializer;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.totalvo.MarAssistantDealer;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.filter.IFilter;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.querytemplate.querytree.QueryScheme;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.IQueryConstants;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.querytemplate.QueryTemplateConvertor;
import nc.vo.querytemplate.TemplateInfo;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreePrivateContextKey;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.FreeReportDrillParam;
import com.ufida.report.anareport.model.AbsAnaReportModel;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 报表通用查询注册类
 * </p>
 * 
 * @since 创建日期 Aug 7, 2013
 * @author wangweir
 */
public class MMGPRptDefaultQueryAction extends DefaultQueryAction {

	private MMGPMainOrgPowerUtil mainOrgUitl;

	// 是否启用权限
	private UFBoolean powerEnable = UFBoolean.FALSE;

	protected QueryConditionDLGDelegator qryCondDLGDelegator;

	/**
	 * IQueryConditionDLGInitializer
	 */
	private IQueryConditionDLGInitializer qryCondDLGInitializer;

	private AbsAnaReportModel rptModel;

	private MMGPReportContextWrapper reportContextWrapper;

	/**
	 * 穿透接口
	 */
	@Override
	public IQueryCondition doQueryByDrill(Container parent, IContext context,
			AbsAnaReportModel reportModel, FreeReportDrillParam drillParam) {
		this.rptModel = reportModel;
		// 默认实现，直接使用源报表的查询条件
		if (drillParam == null) {
			return new BaseQueryCondition(false);
		}
		// 处理联查精度
		BaseQueryCondition condition = (BaseQueryCondition) drillParam
				.getSrcCondition();
		BaseQueryCondition rptcond = new BaseQueryCondition(true);
		rptcond.setDescriptors(condition.getDescriptors());
		IBusiFormat busiformat = this.getIBusiFormat(context);
		if (busiformat != null) {
			rptcond.setBusiFormat(busiformat);
		}
		rptcond.setUserObject(condition.getUserObject());
		return rptcond;
	}

	/**
	 * 处理查询方案查询
	 */
	@Override
	public IQueryCondition doQueryByScheme(Container parent, IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		this.rptModel = reportModel;
		this.dealDataPermission(context);
		super.doQueryByScheme(parent, context, reportModel, queryScheme);
		if (queryScheme == null) {
			return new BaseQueryCondition(false);
		}
		QueryScheme newqueryScheme = (QueryScheme) queryScheme;
		IQueryCondition condition = this.createQueryCondition(context);
		BaseQueryCondition result = (BaseQueryCondition) condition;
		// 构建返回对象
		if (this.mainOrgUitl == null) {
			this.mainOrgUitl = new MMGPMainOrgPowerUtil(context);
		}
		this.buildTranMapByScheme(newqueryScheme);
		MMGPReportConditionUtils.setDescription(context, newqueryScheme);
		// 处理物料相关条件
		this.dealMaterialCondition();
		this.dealConditionVO();
		this.doBusinessProcess(result, context);

		// 设置报表调节器
		result.setRoportAdjustor(this.getAdjustor(result, context));
		MMGPReportConditionUtils.setDescription(this.reportContextWrapper,
				newqueryScheme);

		// 放到UserObject，带到后台
		result.setUserObject(this.reportContextWrapper);
		// 双引擎处理
		this.dealDoubleEngine(context);
		return condition;
	}

	public UFBoolean getPowerEnable() {
		return this.powerEnable;
	}

	public AbsAnaReportModel getReportModel() {
		return this.rptModel;
	}

	public void setPowerEnable(UFBoolean powerEnable) {
		this.powerEnable = powerEnable;
	}

	/**
	 * 精度處理
	 */
	@Override
	protected IQueryCondition createQueryCondition(IContext context) {
		BaseQueryCondition condition = new BaseQueryCondition(true);
		IBusiFormat busiformat = this.getIBusiFormat(context);
		if (busiformat != null) {
			condition.setBusiFormat(busiformat);
		}
		return condition;
	}

	/**
	 * 构建查询面版
	 */
	@Override
	protected QueryConditionDLG createQueryDlg(Container parent,
			TemplateInfo ti, IContext context, IQueryCondition oldCondition) {
		if (this.qryCondDLGDelegator == null) {
			// 返回供应链的查询对话框
			this.qryCondDLGDelegator = this.createQryDLGDelegator(parent, ti);
			this.qryCondDLGDelegator.getQce().getQueryContext()
					.setReloadQuickAreaValue(isReloadQuickAreaValue());

			this.initQueryConditionDLGDelegator();

			if (this.powerEnable.booleanValue()) {
				this.qryCondDLGDelegator.registerRefPowerFilterInfo(
						this.getMainClass(), this.getColumnMapping());
			}

			this.mainOrgUitl = new MMGPMainOrgPowerUtil(context);
			this.mainOrgUitl.setMainOrgPower(this.getQryCondDLGDelegator(),
					this.getMainPkOrgField());

			this.qryCondDLGDelegator
					.addQueryCondVODealer(new MarAssistantDealer());
		}

		return this.qryCondDLGDelegator.getQueryConditionDLG();
	}

	/**
	 * @return
	 */
	private boolean isReloadQuickAreaValue() {
		return true;
	}

	/**
	 * @param parent
	 * @param ti
	 * @return
	 */
	protected QueryConditionDLGDelegator createQryDLGDelegator(
			Container parent, TemplateInfo ti) {
		QueryConditionDLGDelegator dlgDelegator;
		dlgDelegator = new QueryConditionDLGDelegator(parent, ti);
		this.initQueryConditionDLG(dlgDelegator);
		return dlgDelegator;
	}

	/**
	 * @param dlgDelegator
	 */
	protected void initQueryConditionDLG(QueryConditionDLGDelegator dlgDelegator) {
		if (this.getQryCondDLGInitializer() != null) {
			this.getQryCondDLGInitializer().initQueryConditionDLG(dlgDelegator);
		}
	}

	protected void dealConditionVO() {
		ConditionVO[] conds = this.reportContextWrapper
				.getGeneralConditionVOs();
		for (ConditionVO cond : conds) {
			// 处理多值
			this.dealValues(cond);
			// 处理操作符
			if (cond.getOperaCode().equalsIgnoreCase("left like")) {
				cond.setOperaCode("like");
				String value = cond.getValue();
				if (!MMStringUtil.isEmpty(value) && !value.endsWith("%")) {
					cond.setValue(cond.getValue() + "%");
				}
				continue;
			}
			if (cond.getOperaCode().equalsIgnoreCase("==")) {
				cond.setOperaCode("=");
				continue;
			}
		}
	}

	/**
	 * 指定主实体，用于语义模型权限过滤
	 * 
	 * @param context
	 */
	protected void dealDataPermission(IContext context) {
		if (this.powerEnable.booleanValue() && this.getMainClasses() != null) {
			ReportPermissionUtils utils = new ReportPermissionUtils(context);
			utils.setMainBeanClass(this.getMainClasses());
			return;
		}
		if (this.powerEnable.booleanValue() && this.getMainClass() != null) {
			ReportPermissionUtils utils = new ReportPermissionUtils(context);
			utils.setMainBeanClass(this.getMainClass());
		}
	}

	/**
	 * 处理双引擎
	 */
	protected void dealDoubleEngine(IContext context) {
		if (this.isNeedDoubleEngine()) {
			String doubleds = this.getDoubleEngineDsName();
			if (!PubAppTool.isNull(doubleds)) {
				context.setAttribute(ISmartContextKey.KEY_DS_EXEC, doubleds);
			}
		} else {
			context.removeAttribute(ICommContextKey.KEY_DS_EXEC);
		}
	}

	/**
	 * @return
	 */
	protected String getDoubleEngineDsName() {
		if (this.getModuleCode() == null) {
			return null;
		}

		try {
			ReportDBVO dbVO = NCLocator.getInstance()
					.lookup(IReportDBService.class)
					.getReportDBVOByCode(this.getModuleCode());
			if (dbVO != null) {
				return dbVO.getDsname();
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return null;
	}

	/**
	 * @return
	 */
	protected String getModuleCode() {
		return null;
	}

	/**
	 * 处理非元数据物料相关的查询条件
	 */
	protected void dealMaterialCondition() {
		return;
	}

	/**
	 * 处理CONDITIONVO的值，当IN操作符时，处理一下多值
	 * 
	 * @param cond
	 */
	protected void dealValues(ConditionVO cond) {
		return;
	}

	/**
	 * @param condition
	 * @param context
	 */
	protected void doBusinessProcess(IQueryCondition condition, IContext context) {
		return;
	}

	/**
	 * 获得设计报表调试器（用于动态列控制),可复写
	 * 
	 * @param condition
	 * @param context
	 * @return
	 */
	protected IReportAdjustor getAdjustor(BaseQueryCondition condition,
			IContext context) {
		return null;
	}

	/**
	 * 获得启用权限的字段 Map<String, String>字段映射，key：查询模板field code，value：元数据全路径
	 * 默认启用权限字段物料、客户、供应商
	 * 
	 * @return
	 */
	protected Map<String, String> getColumnMapping() {
		Map<String, String> columnMapping = new HashMap<String, String>();
		columnMapping.put("cmaterialoid", "cmaterialoid");
		columnMapping.put("cmaterialvid", "cmaterialvid");
		return columnMapping;
	}

	/**
	 * 格式化工具
	 * 
	 * @param context
	 * @return
	 */
	protected IBusiFormat getIBusiFormat(IContext context) {
		// ICRptDefaultScaleStrategy icScaleStrategy = new
		// ICRptDefaultScaleStrategy();
		// return icScaleStrategy.getReportScaleProcess();
		return null;
	}

	/**
	 * 获得查询权限校验主实体元数据类
	 * 
	 * @return
	 */
	protected Class<? extends ISuperVO> getMainClass() {
		return null;
	}

	/**
	 * 获得权限校验主实体元数据类
	 * 
	 * @return
	 */
	protected Class<? extends ISuperVO>[] getMainClasses() {
		return null;
	}

	/**
	 * 主组织权限字段
	 * 
	 * @return
	 */
	protected String getMainPkOrgField() {
		return MMGlobalConst.PK_ORG;
	}

	/**
	 * 初始化查询代理（如：校验类注册）
	 */
	protected void initQueryConditionDLGDelegator() {
		return;
	}

	/**
	 * 是否需要双引擎
	 * 
	 * @return
	 */
	protected boolean isNeedDoubleEngine() {
		return false;
	}

	/**
	 * 处理查询模板查询
	 */
	@Override
	protected IQueryCondition setQueryResult(IQueryCondition condition,
			QueryConditionDLG queryDlg, AbsAnaReportModel reportModel,
			IContext context) {
		this.rptModel = reportModel;
		this.dealDataPermission(context);
        QueryScheme newqueryScheme = (QueryScheme) queryDlg.getQueryScheme();
		MMGPReportConditionUtils.setDescription(context, newqueryScheme);
		// 无查询模板则返回
		if (condition == null || !(condition instanceof BaseQueryCondition)) {
			return condition;
		}
		String where = newqueryScheme.getWhereSQLOnly();
		BaseQueryCondition result = (BaseQueryCondition) condition;
		this.buildTranMap(where);
		// 处理相关条件
		this.dealMaterialCondition();
		this.dealConditionVO();
		this.doBusinessProcess(result, context);
		MMGPReportConditionUtils.setDescription(this.reportContextWrapper,
				newqueryScheme);
		// 设置报表调节器
		result.setRoportAdjustor(this.getAdjustor(result, context));
		// 放到UserObject，带到后台
		result.setUserObject(this.reportContextWrapper);
		// 双引擎处理
		this.dealDoubleEngine(context);
		return condition;
	}

	/**
	 * 构建传递缓存类
	 * 
	 * @param where
	 */
	private void buildTranMap(String where) {
		this.buildTranMapByScheme(this.getQryCondDLGDelegator()
				.getQueryConditionDLG().getQueryScheme());
		// 处理Where
		this.reportContextWrapper.setWherePart(where);
	}

	/**
	 * 构建传递缓存类
	 * 
	 * @param where
	 */
	private void buildTranMapByScheme(IQueryScheme queryScheme) {
		// 构建返回对象
		this.reportContextWrapper = new MMGPReportContextWrapper();
		this.reportContextWrapper.setNewQuery(true);
		MMGPReportConditionUtils.buildConditionVO(queryScheme,
				this.reportContextWrapper);
		this.reportContextWrapper.setQueryScheme(queryScheme);
		this.combinCondition();
	}

	/**
	 * 合并其它固定条件
	 */
	private void combinCondition() {
		ConditionVO pk_orgContdition = this.mainOrgUitl
				.getMainOrgCondition(this.getMainPkOrgField());
		// 处理查询条件
		this.reportContextWrapper.setConditionVOs(MMArrayUtil.arrayCombine(
				this.reportContextWrapper.getConditionVOs(),
				this.reportContextWrapper.getSelectConditionVOs(),
				new ConditionVO[] { this.groupCondition(), pk_orgContdition }));
		this.reportContextWrapper
				.setSelectConditionVOs(this.reportContextWrapper
						.getSelectConditionVOs());
		this.reportContextWrapper.setGeneralConditionVOs(MMArrayUtil
				.arrayCombine(
						this.reportContextWrapper.getGeneralConditionVOs(),
						new ConditionVO[] { this.groupCondition(),
								pk_orgContdition }));
	}

	/**
	 * 构造集团条件
	 * 
	 * @return
	 */
	private ConditionVO groupCondition() {
		ConditionVO cond = new ConditionVO();
		cond.setDataType(IQueryConstants.UFREF);
		cond.setFieldCode(MMGlobalConst.PK_GROUP);
		cond.setOperaCode(IOperatorConstants.EQ);
		cond.setValue(WorkbenchEnvironment.getInstance().getGroupVO()
				.getPk_group());
		return cond;
	}

	/**
	 * 转换查询条件，去掉不必要对象
	 * 
	 * @param queryScheme
	 * @return
	 */
//	private QueryScheme reBuildQueryScheme(IQueryScheme queryScheme) {
//		QueryScheme newqueryScheme = new QueryScheme();
//		newqueryScheme.putWhereSQLOnly(queryScheme.getWhereSQLOnly());
//		newqueryScheme.putSQLDescription((String) queryScheme
//				.get(IQueryScheme.KEY_SQL_DESCRIPTION));
//		newqueryScheme.putNormalCondition(queryScheme
//				.get(IQueryScheme.KEY_NORMAL_CONDITION));
//		newqueryScheme.putLogicalCondition(queryScheme
//				.get(IQueryScheme.KEY_LOGICAL_CONDITION));
//		newqueryScheme.put(IQueryScheme.KEY_FILTERS,
//				queryScheme.get(IQueryScheme.KEY_FILTERS));
//		return newqueryScheme;
//	}

	/**
	 * 适配平台主组织权限，供查询框使用。
	 * 
	 * @param context
	 */
	protected void setCondOrgAttribute(IContext context) {
		this.setCondOrgAttribute(context, this.getQryCondDLGDelegator()
				.getGeneralCondtionVOs());
	}

	/**
	 * 适配平台主组织权限，供快速查询使用。
	 * 
	 * @param context
	 */
	protected void setCondOrgAttribute(IContext context,
			IQueryScheme queryScheme) {
		IFilter[] filters = (IFilter[]) queryScheme
				.get(IQueryScheme.KEY_FILTERS);
		ConditionVO[] conds = QueryTemplateConvertor
				.convertIFilter2ConditionVO(Arrays.asList(filters));
		this.setCondOrgAttribute(context, conds);
	}

	/**
	 * 为了适配平台的主组织权限，报表必须查出组织字段（可以不显示），对于汇总报表，取查询条件中第一个组织写死在SQL里。
	 * 此方法的作用就是将查询条件中的主组织条件的第一个PK值存入上下文，供后台拼SQL使用。
	 */
	private void setCondOrgAttribute(IContext context, ConditionVO[] conds) {
		String pk_org_cond = "''";
		for (ConditionVO condVo : conds) {
			if (this.getMainPkOrgField().equals(condVo.getFieldCode())) {
				String[] temp = condVo.getValue().split(",");
				if (temp.length == 1) {
					pk_org_cond = "'" + temp[0] + "'";
				} else {
					pk_org_cond = StringUtils.remove(temp[0], "(");
				}
			}
		}
		context.setAttribute(FreePrivateContextKey.KEY_USER_PK_ORG, pk_org_cond);
	}

	/**
	 * @return the qryCondDLGInitializer
	 */
	public IQueryConditionDLGInitializer getQryCondDLGInitializer() {
		return qryCondDLGInitializer;
	}

	/**
	 * @param qryCondDLGInitializer
	 *            the qryCondDLGInitializer to set
	 */
	public void setQryCondDLGInitializer(
			IQueryConditionDLGInitializer qryCondDLGInitializer) {
		this.qryCondDLGInitializer = qryCondDLGInitializer;
	}

	/**
	 * @return the qryCondDLGDelegator
	 */
	public QueryConditionDLGDelegator getQryCondDLGDelegator() {
		return qryCondDLGDelegator;
	}

	/**
	 * @param qryCondDLGDelegator
	 *            the qryCondDLGDelegator to set
	 */
	public void setQryCondDLGDelegator(
			QueryConditionDLGDelegator qryCondDLGDelegator) {
		this.qryCondDLGDelegator = qryCondDLGDelegator;
	}

	public MMGPReportContextWrapper getTranMap() {
		return this.reportContextWrapper;
	}

}
