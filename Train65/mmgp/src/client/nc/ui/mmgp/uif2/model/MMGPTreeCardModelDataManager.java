package nc.ui.mmgp.uif2.model;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnQueryService;
import nc.md.model.IBean;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.bd.meta.BDObjectTreeCreateStrategy;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;
import nc.vo.util.ConditionBuilder;
import nc.vo.util.VisibleUtil;

/**
 * 扩展自uap，增加默认查询。
 * 
 * @author wangweiu
 * 
 */
public class MMGPTreeCardModelDataManager extends MMGPBaseModelDataManager implements AppEventListener {
	public static final String ENABLE_DEFAULT = " and (isnull("
			+ IBaseServiceConst.ENABLESTATE_FIELD + ","
			+ IPubEnumConst.ENABLESTATE_ENABLE + ") = "
			+ IPubEnumConst.ENABLESTATE_ENABLE + ")";

	private IMMGPCmnQueryService qryServicer;

	private String orderField;



	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	private String mmgpTreeWhereSql;

	public String getMmgpTreeWhereSql() {
		return mmgpTreeWhereSql;
	}

	public void setMmgpTreeWhereSql(String mmgpTreeWhereSql) {
		this.mmgpTreeWhereSql = mmgpTreeWhereSql;
	}
	@Override
	public void setModel(AbstractUIAppModel model) {
		super.setModel(model);
		model.addAppEventListener(this);
	}
	@Override
	public void refresh() {
	    //Modify by wangfan3 20130530 刷新后焦点回到树形根节点
	//	Object obj = getModel().getSelectedData();
		initModel();
//		try {
//			getTreeModel()
//					.setSelectedNode(getTreeModel().findNodeByBusinessObject(obj));
//		} catch (Exception e) {
//			getTreeModel().setSelectedNode(null);
//		}
	}

	@Override
	public boolean refreshable() {
		return true;
	}

	@Override
	public void initModel() {

		Object[] docVos = null;
		try {
			String className;
			if (getTreeModel().getTreeCreateStrategy() instanceof BDObjectTreeCreateStrategy) {
				BDObjectTreeCreateStrategy treeStategy = (BDObjectTreeCreateStrategy) getTreeModel()
						.getTreeCreateStrategy();
				className = treeStategy.getClassName();
			} else {
				throw new RuntimeException("property[className] not found");
			}
			//2014-4-17 gaotx 扩展元数据查询中，dr应该制定别名
			LoginContext context = getModel().getContext();
			IBean bean = MMGPMetaUtils.getIBean(context);
			Class<?> clazz = Class.forName(className);
			String con = getMmgpTreeWhereSql() == null ? "isnull("+bean.getTable().getName()+".dr,0)=0"
					: getMmgpTreeWhereSql();
			
			if (isNeedSeal() && !isShowSealDataFlag()) {
				con += ENABLE_DEFAULT;
			}
			
			if (MMStringUtil.isEmpty(orderField)) {

				orderField = MMMetaUtils.getCodeFieldName(MMMetaUtils
						.getBeanByClassFullName(className));
			}
			
			/* Apr 11, 2013 wangweir 通过Context的组织过滤 Begin */
	        // String[] pk_orgs = context.getFuncInfo().getFuncPermissionPkorgs();
			/* Apr 11, 2013 wangweir End */
			// 2013-04-19 wangweiu 修改 
			if (MMStringUtil.isNotEmpty(context.getPk_org())) {
				String[] pk_orgs = { context.getPk_org() };
				
                String orgSql = null;
                if (bean.getTagString() != null && bean.getTagString().contains("BDMODE")) {
                    orgSql =
                            VisibleUtil.getRefVisibleCondition(
                                context.getPk_group(),
                                pk_orgs,
                                MMGPMetaUtils.getMetaId(context));
                } else {
                    // add by jilu 20130520 单据节点，不注册管控模式，就不走VisibleUtil了，等王伟出补丁，此处暂时处理
                    if (pk_orgs != null && pk_orgs.length > 0) {
                        StringBuilder pkorg = new StringBuilder();
                        for (String pk_org : pk_orgs) {
                            pkorg.append("'").append(pk_org).append("', ");
                        }
                        pkorg.delete(pkorg.length() - 2, pkorg.length());
                        ConditionBuilder conditionBuilder =
                                new ConditionBuilder(
                                    "{tablename.}{pk_org_fieldname} in (" + pkorg.toString() + ")",
                                    VisibleUtil.CONDITIONSTRUTYPE_SQL);
                        conditionBuilder.setMdclassid(bean.getID());
                        orgSql = conditionBuilder.getCondition(bean.getTable().getName());
                    }
                }
				if (MMStringUtil.isNotEmpty(orgSql)) {
					con = con + " and " + orgSql;
				}
				con = con + "  order by " + orderField;
				IMDPersistenceQueryService queryService = NCLocator.getInstance()
						.lookup(IMDPersistenceQueryService.class);
				docVos = queryService.queryBillOfVOByCond(clazz, con, false, false)
						.toArray();
				/* Apr 15, 2013 wangweir 修改原因 Begin */
				// docVos = this.getQryServicer().cmnQueryDatasByCondition(clazz,
				// con);
				/* Apr 15, 2013 wangweir End */
				// docVos = getQryServicer().cmnQueryDatasByCondition(clazz, con);
				getModel().initModel(docVos);
			} else {
				getModel().initModel(null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	private HierachicalDataAppModel getTreeModel() {
		return (HierachicalDataAppModel) super.getModel();
	}

	protected IMMGPCmnQueryService getQryServicer() {
		if (qryServicer == null) {
			qryServicer = NCLocator.getInstance().lookup(
					IMMGPCmnQueryService.class);
		}
		return qryServicer;
	}

    /**
     * @param qryServicer the qryServicer to set
     */
    public void setQryServicer(IMMGPCmnQueryService qryServicer) {
        this.qryServicer = qryServicer;
    }

	@Override
	public void handleEvent(AppEvent event) {
		// 这块不需要了，orgpanel的change时间会自动调用initModel()
//		if (OrgChangedEvent.class.getName().equals(event.getType())) {
//			initModel();
//		}
	}



}
