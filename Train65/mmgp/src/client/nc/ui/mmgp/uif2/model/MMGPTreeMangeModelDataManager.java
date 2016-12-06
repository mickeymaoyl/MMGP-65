package nc.ui.mmgp.uif2.model;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.mmgp.common.CommonUtils;
import nc.ui.mmgp.uif2.service.MMGPQueryService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.ui.uif2.model.IAppModelDataManagerEx;
import nc.vo.bd.access.tree.ITreeCreateStrategy;
import nc.vo.bd.meta.BDObjectTreeCreateStrategy;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uif2.LoginContext;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.VisibleUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class MMGPTreeMangeModelDataManager implements IAppModelDataManagerEx,
		AppEventListener {
	public static final String ENABLE_DEFAULT = "(isnull("
		+ IBaseServiceConst.ENABLESTATE_FIELD + ","
		+ IPubEnumConst.ENABLESTATE_ENABLE + ") = "
		+ IPubEnumConst.ENABLESTATE_ENABLE + ")";
	private IQueryService queryService = null;

	private BillManageModel model;

	private HierachicalDataAppModel treeModel = null;
	//zhaoli 2013-4-19 ֧���Ҳ�������ġ���ʾͣ�á���ť
	private boolean isShowSeal = false;

	private String parentFieldName;

	// private String sqlWhere = null;

	public MMGPTreeMangeModelDataManager() {
	}

	public void initModel() {
		initDefaultService();
		initManageModelBySelectNode(null);
	}

	/**
	 * 
	 * @param condition
	 */
	public void initManageModelBySelectNode(final String pk_node) {

		Object[] data = null;

		String condition = null;
		if (pk_node != null) {
			condition = getParentFieldName() + " = '" + pk_node + "'";
			data = queryByCondition(condition);

		}
		getModel().initModel(data);

	}

	@Override
	public void initModelBySqlWhere(String sqlWhere) {
		// this.sqlWhere = sqlWhere;
		// LoginContext context = getModel().getContext();
		// String[] data = queryPsndocByOrg(sqlWhere, orgs);
		// // ������ݲ�ѯ����û�в�ѯ������ʱ����Ҫ����״̬������Ϣ��ʾ
		// if (data == null || data.length == 0) {
		// ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant
		// .getQueryNullInfo(), getModel().getContext());
		// } else
		// ShowStatusBarMsgUtil.showStatusBarMsg(
		// IShowMsgConstant.getQuerySuccessInfo(data.length),
		// getModel().getContext());
		// TODO
	}

	@Override
	public void refresh() {
		// LoginContext context = getModel().getContext();
		// List<OrgVO> orgvos = context.getOrgvos();
		// String[] orgs = null;
		// if (orgvos != null && orgvos.size() > 0) {
		// orgs = new String[orgvos.size()];
		// for (int i = 0; i < orgvos.size(); i++) {
		// orgs[i] = orgvos.get(i).getPk_org();
		// }
		//
		// }
		// String[] data = queryPsndocByOrg(sqlWhere, orgs);
		// if (data == null || data.length == 0) {
		// ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant
		// .getQueryNullInfo(), getModel().getContext());
		// } else
		// ShowStatusBarMsgUtil.showStatusBarMsg(
		// IShowMsgConstant.getQuerySuccessInfo(data.length),
		// getModel().getContext());

	}

	/**
	 * ����������ѯ����
	 * 
	 * @param sqlWhere
	 * @return
	 */
	private Object[] queryByCondition(String sqlWhere) {
		Object[] data = null;
		// // ��¼��ѯ���
		// this.sqlWhere = sqlWhere;

		// ����Ϊnullʱ������null
		if (StringUtils.isBlank(sqlWhere)) {
			return null;
		}
		SqlWhereUtil swu = new SqlWhereUtil();
		// �����Ƿ���˷�棬���ӷ������ ���� 2013-4-19
		if (!isShowSeal)
			swu.s(ENABLE_DEFAULT);
		//end zhaoli
		if (!StringUtils.isBlank(sqlWhere))
			swu.and(sqlWhere);

		// // Ĭ�������ֶα��롢���ƣ����������������δ�ṩ��
		// String defaultOrderPart = " order by code, name";
        LoginContext context = getModel().getContext();
        //���� 2013-4-23 ֻ��Ҫ�Ե�ǰ��֯���ˣ�����Ҫ���еĿɼ���֯��
        String[] pk_orgs = { context.getPk_org() };// context.getFuncInfo().getFuncPermissionPkorgs();
        //end
        String orgSql = null;
		try {
			orgSql = VisibleUtil.getRefVisibleCondition(context.getPk_group(),
					pk_orgs, MMGPMetaUtils.getMetaId(context));
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		swu.and(orgSql);
		try {
			data = getQueryService().queryByWhereSql(swu.getSQLWhere());
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return data;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;

	}

	public IQueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(IQueryService queryService) {
		this.queryService = queryService;
	}

	protected void initDefaultService() {
		// ���������ڶ���������⣬�ȸ�Ϊ��������
		// Ĭ��ʹ��ͨ��service
		if (getQueryService() == null) {
			MMGPQueryService service = new MMGPQueryService();
			service.setClassName(getClassFullName());
//			if (getModel() instanceof BillManageModel) {
//				BillManageModel billMangeModel = (BillManageModel) getModel();
//				service.setSupportLazilyLoad(billMangeModel
//						.isSupportLazilyLoad());
//			}
			setQueryService(service);

		}
	}

	protected String getClassFullName() {
		return MMGPMetaUtils.getClassFullName(getModel().getContext());
	}

	public HierachicalDataAppModel getTreeModel() {
		return treeModel;
	}

	// private void setPsndocToPaginationModel(String[] pks) {
	// try {
	// paginationModel.setObjectPks(pks);
	// } catch (BusinessException e) {
	// Logger.error(e.getMessage());
	// throw new BusinessExceptionAdapter(e);
	// }
	//
	// }
	//
	// @Override
	// public void onDataReady() {
	// getDelegator().onDataReady();
	//
	// }
	//
	// @Override
	// public void onStructChanged() {
	// }
	//
	// public BillManagePaginationDelegator getDelegator() {
	// if (delegator == null) {
	// delegator = new BillManagePaginationDelegator(getModel(),
	// getPaginationModel());
	// }
	// return delegator;
	// }
	//
	// public PaginationModel getPaginationModel() {
	// return paginationModel;
	// }

	public String getParentFieldName() {
		if (MMStringUtil.isEmpty(parentFieldName)) {
			ITreeCreateStrategy strategy = getTreeModel()
					.getTreeCreateStrategy();
			if (strategy instanceof BDObjectTreeCreateStrategy) {
				String className = ((BDObjectTreeCreateStrategy) strategy)
						.getClassName();
				parentFieldName = CommonUtils.getPKFieldName(className);
			}
		}
		return parentFieldName;
	}

	public void setParentFieldName(String parentFieldName) {
		this.parentFieldName = parentFieldName;
	}

	@Override
	public void handleEvent(AppEvent event) {

	}

	@Override
	public void setShowSealDataFlag(boolean showSealDataFlag) {
		this.isShowSeal = showSealDataFlag;
	}

}
