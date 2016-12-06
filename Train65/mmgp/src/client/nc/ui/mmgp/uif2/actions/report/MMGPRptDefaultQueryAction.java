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
 * <b> ��Ҫ�������� </b>
 * <p>
 * ����ͨ�ò�ѯע����
 * </p>
 * 
 * @since �������� Aug 7, 2013
 * @author wangweir
 */
public class MMGPRptDefaultQueryAction extends DefaultQueryAction {

	private MMGPMainOrgPowerUtil mainOrgUitl;

	// �Ƿ�����Ȩ��
	private UFBoolean powerEnable = UFBoolean.FALSE;

	protected QueryConditionDLGDelegator qryCondDLGDelegator;

	/**
	 * IQueryConditionDLGInitializer
	 */
	private IQueryConditionDLGInitializer qryCondDLGInitializer;

	private AbsAnaReportModel rptModel;

	private MMGPReportContextWrapper reportContextWrapper;

	/**
	 * ��͸�ӿ�
	 */
	@Override
	public IQueryCondition doQueryByDrill(Container parent, IContext context,
			AbsAnaReportModel reportModel, FreeReportDrillParam drillParam) {
		this.rptModel = reportModel;
		// Ĭ��ʵ�֣�ֱ��ʹ��Դ����Ĳ�ѯ����
		if (drillParam == null) {
			return new BaseQueryCondition(false);
		}
		// �������龫��
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
	 * �����ѯ������ѯ
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
		// �������ض���
		if (this.mainOrgUitl == null) {
			this.mainOrgUitl = new MMGPMainOrgPowerUtil(context);
		}
		this.buildTranMapByScheme(newqueryScheme);
		MMGPReportConditionUtils.setDescription(context, newqueryScheme);
		// ���������������
		this.dealMaterialCondition();
		this.dealConditionVO();
		this.doBusinessProcess(result, context);

		// ���ñ��������
		result.setRoportAdjustor(this.getAdjustor(result, context));
		MMGPReportConditionUtils.setDescription(this.reportContextWrapper,
				newqueryScheme);

		// �ŵ�UserObject��������̨
		result.setUserObject(this.reportContextWrapper);
		// ˫���洦��
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
	 * ����̎��
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
	 * ������ѯ���
	 */
	@Override
	protected QueryConditionDLG createQueryDlg(Container parent,
			TemplateInfo ti, IContext context, IQueryCondition oldCondition) {
		if (this.qryCondDLGDelegator == null) {
			// ���ع�Ӧ���Ĳ�ѯ�Ի���
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
			// �����ֵ
			this.dealValues(cond);
			// ���������
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
	 * ָ����ʵ�壬��������ģ��Ȩ�޹���
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
	 * ����˫����
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
	 * �����Ԫ����������صĲ�ѯ����
	 */
	protected void dealMaterialCondition() {
		return;
	}

	/**
	 * ����CONDITIONVO��ֵ����IN������ʱ������һ�¶�ֵ
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
	 * �����Ʊ�������������ڶ�̬�п���),�ɸ�д
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
	 * �������Ȩ�޵��ֶ� Map<String, String>�ֶ�ӳ�䣬key����ѯģ��field code��value��Ԫ����ȫ·��
	 * Ĭ������Ȩ���ֶ����ϡ��ͻ�����Ӧ��
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
	 * ��ʽ������
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
	 * ��ò�ѯȨ��У����ʵ��Ԫ������
	 * 
	 * @return
	 */
	protected Class<? extends ISuperVO> getMainClass() {
		return null;
	}

	/**
	 * ���Ȩ��У����ʵ��Ԫ������
	 * 
	 * @return
	 */
	protected Class<? extends ISuperVO>[] getMainClasses() {
		return null;
	}

	/**
	 * ����֯Ȩ���ֶ�
	 * 
	 * @return
	 */
	protected String getMainPkOrgField() {
		return MMGlobalConst.PK_ORG;
	}

	/**
	 * ��ʼ����ѯ�����磺У����ע�ᣩ
	 */
	protected void initQueryConditionDLGDelegator() {
		return;
	}

	/**
	 * �Ƿ���Ҫ˫����
	 * 
	 * @return
	 */
	protected boolean isNeedDoubleEngine() {
		return false;
	}

	/**
	 * �����ѯģ���ѯ
	 */
	@Override
	protected IQueryCondition setQueryResult(IQueryCondition condition,
			QueryConditionDLG queryDlg, AbsAnaReportModel reportModel,
			IContext context) {
		this.rptModel = reportModel;
		this.dealDataPermission(context);
        QueryScheme newqueryScheme = (QueryScheme) queryDlg.getQueryScheme();
		MMGPReportConditionUtils.setDescription(context, newqueryScheme);
		// �޲�ѯģ���򷵻�
		if (condition == null || !(condition instanceof BaseQueryCondition)) {
			return condition;
		}
		String where = newqueryScheme.getWhereSQLOnly();
		BaseQueryCondition result = (BaseQueryCondition) condition;
		this.buildTranMap(where);
		// �����������
		this.dealMaterialCondition();
		this.dealConditionVO();
		this.doBusinessProcess(result, context);
		MMGPReportConditionUtils.setDescription(this.reportContextWrapper,
				newqueryScheme);
		// ���ñ��������
		result.setRoportAdjustor(this.getAdjustor(result, context));
		// �ŵ�UserObject��������̨
		result.setUserObject(this.reportContextWrapper);
		// ˫���洦��
		this.dealDoubleEngine(context);
		return condition;
	}

	/**
	 * �������ݻ�����
	 * 
	 * @param where
	 */
	private void buildTranMap(String where) {
		this.buildTranMapByScheme(this.getQryCondDLGDelegator()
				.getQueryConditionDLG().getQueryScheme());
		// ����Where
		this.reportContextWrapper.setWherePart(where);
	}

	/**
	 * �������ݻ�����
	 * 
	 * @param where
	 */
	private void buildTranMapByScheme(IQueryScheme queryScheme) {
		// �������ض���
		this.reportContextWrapper = new MMGPReportContextWrapper();
		this.reportContextWrapper.setNewQuery(true);
		MMGPReportConditionUtils.buildConditionVO(queryScheme,
				this.reportContextWrapper);
		this.reportContextWrapper.setQueryScheme(queryScheme);
		this.combinCondition();
	}

	/**
	 * �ϲ������̶�����
	 */
	private void combinCondition() {
		ConditionVO pk_orgContdition = this.mainOrgUitl
				.getMainOrgCondition(this.getMainPkOrgField());
		// �����ѯ����
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
	 * ���켯������
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
	 * ת����ѯ������ȥ������Ҫ����
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
	 * ����ƽ̨����֯Ȩ�ޣ�����ѯ��ʹ�á�
	 * 
	 * @param context
	 */
	protected void setCondOrgAttribute(IContext context) {
		this.setCondOrgAttribute(context, this.getQryCondDLGDelegator()
				.getGeneralCondtionVOs());
	}

	/**
	 * ����ƽ̨����֯Ȩ�ޣ������ٲ�ѯʹ�á�
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
	 * Ϊ������ƽ̨������֯Ȩ�ޣ������������֯�ֶΣ����Բ���ʾ�������ڻ��ܱ���ȡ��ѯ�����е�һ����֯д����SQL�
	 * �˷��������þ��ǽ���ѯ�����е�����֯�����ĵ�һ��PKֵ���������ģ�����̨ƴSQLʹ�á�
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
