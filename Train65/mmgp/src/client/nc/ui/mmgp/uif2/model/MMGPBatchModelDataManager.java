package nc.ui.mmgp.uif2.model;

import nc.md.model.IBean;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.utils.MMGPMsgUtils;
import nc.ui.pubapp.pub.smart.SmartBatchAppModelService;
import nc.ui.pubapp.uif2app.model.BatchModelDataManager;
import nc.ui.pubapp.uif2app.model.IRefreshable;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.uif2.model.AbstractBatchAppModel;
import nc.vo.bd.pub.NODE_TYPE;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uif2.LoginContext;
import nc.vo.util.ConditionBuilder;
import nc.vo.util.VisibleUtil;

public class MMGPBatchModelDataManager extends BatchModelDataManager implements
    IModelDataManager,
    INeedSeal,
    IRefreshable {
    private boolean qryWhenInit;

    private boolean needSeal = false;

    private boolean showSealDataFlag = false;

    @Override
    public AbstractBatchAppModel getModel() {
        return super.getModel();
    }

    @Override
    public SmartBatchAppModelService getService() {
        return super.getService();
    }

    @Override
    public void initModel() {
        if (qryWhenInit) {
            // if (needSeal) {
            // try {
            // getService().queryByDataVisibilitySetting(
            // getModel().getContext());
            // } catch (Exception e) {
            // Logger.error(e.getMessage(), e);
            // }
            // }
            initModelBySqlWhere(null);
        } else {
            this.model.initModel(null);
        }
    }

    @Override
    public void initModelBySqlWhere(String sqlwhere) {
        if (qryWhenInit && this.getModel().getContext().getNodeType() == NODE_TYPE.ORG_NODE) {
            // 组织级节点,组织为空时不显示任何数据，选中组织时显示数据。与供应链“车型定义”节点类似
            if (this.getModel().getContext().getPk_org() == null) {
                this.model.initModel(null);
                return;
            }
        }
        this.setSqlWhere(toWhereSql(sqlwhere));

        super.initModelBySqlWhere(this.getSqlWhere());
        MMGPMsgUtils.showQueryStatusBarMsg(getModel());
    }

    protected String toWhereSql(String oldSql) {
        String sqlwhere = oldSql;
        if (MMStringUtil.isEmptyWithTrim(sqlwhere)) {
            sqlwhere = " 1 = 1";
        }
        if (needSeal) {
            if (showSealDataFlag) {
                if (sqlwhere.contains(MMGPBaseModelDataManager.ENABLE_DEFAULT)) {
                    sqlwhere = sqlwhere.replace(MMGPBaseModelDataManager.ENABLE_DEFAULT, "");
                }
            } else {
                if (!sqlwhere.contains(MMGPBaseModelDataManager.ENABLE_DEFAULT)) {
                    sqlwhere = sqlwhere + MMGPBaseModelDataManager.ENABLE_DEFAULT;
                }
            }

        }
        LoginContext context = getModel().getContext();
        String[] pk_orgs;
        if (qryWhenInit) {
            /* Apr 11, 2013 wangweir 通过Context的组织过滤 Begin */
            // String[] pk_orgs =
            // context.getFuncInfo().getFuncPermissionPkorgs();
            pk_orgs = new String[]{context.getPk_org() };
            /* Apr 11, 2013 wangweir End */

        } else {
            pk_orgs = context.getFuncInfo().getFuncPermissionPkorgs();
        }
        String orgSql = null;

        IBean bean = MMGPMetaUtils.getIBean(context);

        try {
            // 检查元数据是否使用了管控模式的标签
            if (bean.getTagString().contains("BDMODE")) {
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

                if (MMStringUtil.isNotEmpty(AppContext.getInstance().getPkGroup())) {
                    ConditionBuilder conditionBuilder =
                            new ConditionBuilder(
                                "{tablename.}{pk_group_fieldname} = '{pk_group}'",
                                VisibleUtil.CONDITIONSTRUTYPE_SQL);
                    conditionBuilder.setMdclassid(bean.getID());
                    conditionBuilder.setPk_group(AppContext.getInstance().getPkGroup());

                    if (MMStringUtil.isEmpty(orgSql)) {
                        orgSql = conditionBuilder.getCondition(bean.getTable().getName());
                    } else {
                        orgSql = orgSql + " and " + conditionBuilder.getCondition(bean.getTable().getName());
                    }
                }
                // end
            }
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        if (MMStringUtil.isNotEmpty(orgSql)) {
            sqlwhere = sqlwhere + " and " + orgSql;
        }
        return sqlwhere;
    }

    public boolean isQryWhenInit() {
        return qryWhenInit;
    }

    public void setQryWhenInit(boolean qryWhenInit) {
        this.qryWhenInit = qryWhenInit;
    }

    public boolean isShowSealDataFlag() {
        return showSealDataFlag;
    }

    @Override
    public void setShowSealDataFlag(boolean showSealDataFlag) {
        this.showSealDataFlag = showSealDataFlag;
    }

    public boolean isNeedSeal() {
        return needSeal;
    }

    public void setNeedSeal(boolean needSeal) {
        this.needSeal = needSeal;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.model.IRefreshable#refreshable()
     */
    @Override
    public boolean refreshable() {
        return !MMStringUtil.isEmptyWithTrim(this.getSqlWhere());
    }

    @Override
    public void initModelByQueryScheme(IQueryScheme queryScheme) {
        initModelBySqlWhere(queryScheme.getWhereSQLOnly());
    }

}
