package nc.ui.mmgp.uif2.query;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.org.IOrgConst;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.AppUiContext;
import nc.ui.pubapp.uif2app.query2.IQueryConditionDLGInitializer;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;
import nc.ui.pubapp.uif2app.query2.totalvo.MarAssistantDealer;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uap.rbac.FuncSubInfo;
import nc.vo.uap.rbac.profile.FunctionPermProfileManager;

/**
 * 该类的作用是，为查询模版中的参照类型的查询条件添加参照过滤
 */

public class MMGPCommonQueryConditionInitializer implements IQueryConditionDLGInitializer {

    private List<String> orgFilterList;

    private MMGPCommonQueryRefFilterManager refFilterManager;

    private Boolean isOrgFilteredByPermission = true;

    private String orgFieldCode = "pk_org";

    /* Jul 9, 2013 wangweir 是否支持多组织参照处理；装备制造查询目前使用 Begin */
    /**
     * 是否支持多组织参照处理；装备制造查询目前使用
     */
    private boolean supportMultiCorpRef = false;

    /* Sep 17, 2013 wangweir 物料固定辅助属性及自由辅助属性相关处理的参数bean，注入该参数bean则处理物料辅助属性 Begin */
    private MaterialQueryCondParam materialQueryParam;

    /* Sep 17, 2013 wangweir End */

    @Override
    public void initQueryConditionDLG(QueryConditionDLGDelegator condDLGDelegator) {
        processOrgField(condDLGDelegator);

        processOrgFilter(condDLGDelegator);

        processRefField(condDLGDelegator);

        // 物料固定辅助属性及自由辅助属性过滤
        processMarAss(condDLGDelegator);
    }

    /**
     * @param condDLGDelegator
     */
    private void processMarAss(QueryConditionDLGDelegator condDLGDelegator) {
        if (this.getMaterialQueryParam() == null) {
            return;
        }

        // 自定义项
        String prefix = this.getMaterialQueryParam().getPrefix();

        if (MMStringUtil.isNotEmpty(prefix)) {
            QMarAndDefFilter marAndDefQueryFilter = new QMarAndDefFilter(condDLGDelegator, this.getOrgFieldCode());
            marAndDefQueryFilter.setItemPrefixs(new String[]{prefix });
            marAndDefQueryFilter.addFilterMapsListeners();

            // 自由辅助属性的支持
            MarAssistantDealer marAssistantDealer = new MarAssistantDealer();
            marAssistantDealer.setPrefix(prefix);
            condDLGDelegator.addQueryCondVODealer(marAssistantDealer);
        }

        List<String> marFixFields = this.getMaterialQueryParam().getMarFixFields();

        if (MMCollectionUtil.isNotEmpty(marFixFields)) {
            MMGPOrgFilterWithMultiCorp orgFilter =
                    new MMGPOrgFilterWithMultiCorp(condDLGDelegator, this.getOrgFieldCode());
            orgFilter.setChildPaths(marFixFields);
        }
    }

    protected void processRefField(QueryConditionDLGDelegator condDLGDelegator) {
        if (getRefFilterManager() != null) {
            Collection<MMGPCommonQueryRefFilter> refFilters = getRefFilterManager().getRefFilters();
            if (MMCollectionUtil.isNotEmpty(refFilters)) {
                for (final MMGPCommonQueryRefFilter refFilter : refFilters) {
                    if (refFilter.getFilter() != null) {
                        condDLGDelegator.setRefFilter(refFilter.getFieldCode(), refFilter.getFilter());
                    } else {
                        IRefFilter filter = new IRefFilter() {
                            @Override
                            public void doFilter(UIRefPane refPane) {
                                refPane.getRefModel().addWherePart(" and " + refFilter.getWhereSql());
                            }
                        };
                        condDLGDelegator.setRefFilter(refFilter.getFieldCode(), filter);
                    }
                }
            }
        }
    }

    protected void processOrgFilter(QueryConditionDLGDelegator condDLGDelegator) {
        if (MMCollectionUtil.isEmpty(orgFilterList)) {
            return;
        }

        if (this.isSupportMultiCorpRef()) {
            MMGPOrgFilterWithMultiCorp orgFilter =
                    new MMGPOrgFilterWithMultiCorp(condDLGDelegator, this.getOrgFieldCode());
            orgFilter.setChildPaths(orgFilterList);
        } else {
            for (String orgFilterFiled : orgFilterList) {
                MMGPOrgFilter orgFilter = new MMGPOrgFilter(condDLGDelegator, orgFilterFiled);
                orgFilter.addEditorListener();
            }
        }
    }

    protected void processOrgField(QueryConditionDLGDelegator condDLGDelegator) {
        // 注册需要按用户已分配组织进行过滤的组织条件，该条件必须是组织条件
        if (getIsOrgFilteredByPermission()) {
            condDLGDelegator.registerNeedPermissionOrgFieldCode(
                this.getOrgFieldCode(),
                getFuncPermissionPkorgs(condDLGDelegator));
        }
    }

    protected String[] getFuncPermissionPkorgs(QueryConditionDLGDelegator condDLGDelegator) {
        List<String> funcPermissionPkorgs = new ArrayList<String>();
        String[] funcPermissionPkorgsInner = getFuncPermissionPkorgsInner(condDLGDelegator);
        if(MMArrayUtil.isNotEmpty(funcPermissionPkorgsInner)){
            funcPermissionPkorgs.addAll(Arrays.asList(funcPermissionPkorgsInner));
        }
        
        funcPermissionPkorgs.add(getGroup(condDLGDelegator));
        funcPermissionPkorgs.add(IOrgConst.GLOBEORG);
        return funcPermissionPkorgs.toArray(new String[0]);
    }

    protected String getGroup(QueryConditionDLGDelegator condDLGDelegator) {
        if (condDLGDelegator.getLogincontext() != null) {
            return condDLGDelegator.getLogincontext().getPk_group();
        }

        return AppUiContext.getInstance().getPkGroup();
    }

    protected String[] getFuncPermissionPkorgsInner(QueryConditionDLGDelegator condDLGDelegator) {
        FuncSubInfo funcSubInfo = null;
        // 转单时无法直接拿到login context，只能通过ToftPanelAdaptor来取被分配的组织
        if (condDLGDelegator.getLogincontext() != null) {
            funcSubInfo = condDLGDelegator.getLogincontext().getFuncInfo();
        } else {
            Container parent = condDLGDelegator.getQueryConditionDLG().getParent();
            if (parent != null && parent instanceof ToftPanelAdaptor) {
                funcSubInfo = ((ToftPanelAdaptor) parent).getFuncletContext().getFuncSubInfo();
            }
            // else {
            // throw new RuntimeException(
            // "转单时请把ToftPanelAdaptor传给流程平台，通过设置PfButtonClickContext的parent属性！");/*-=notranslate=-*/
            // }
        }
        if (funcSubInfo != null) {
            return funcSubInfo.getFuncPermissionPkorgs();
        }
        String funcode = condDLGDelegator.getQueryConditionDLG().getTempInfo().getFunNode();
        return FunctionPermProfileManager
            .getInstance()
            .getProfile(WorkbenchEnvironment.getInstance().getLoginUser().getUser_code())
            .getFuncSubInfo(funcode)
            .getFuncPermissionPkorgs();
    }

    public List<String> getOrgFilterList() {
        return orgFilterList;
    }

    public void setOrgFilterList(List<String> orgFilterList) {
        this.orgFilterList = orgFilterList;
    }

    public MMGPCommonQueryRefFilterManager getRefFilterManager() {
        return refFilterManager;
    }

    public void setRefFilterManager(MMGPCommonQueryRefFilterManager refFilterManager) {
        this.refFilterManager = refFilterManager;
    }

    public Boolean getIsOrgFilteredByPermission() {
        return isOrgFilteredByPermission;
    }

    public void setIsOrgFilteredByPermission(Boolean isOrgFilteredByPermission) {
        this.isOrgFilteredByPermission = isOrgFilteredByPermission;
    }

    /**
     * @return the supportMultiCorpRef
     */
    public boolean isSupportMultiCorpRef() {
        return supportMultiCorpRef;
    }

    /**
     * @param supportMultiCorpRef
     *        the supportMultiCorpRef to set
     */
    public void setSupportMultiCorpRef(boolean supportMultiCorpRef) {
        this.supportMultiCorpRef = supportMultiCorpRef;
    }

    /**
     * @return the orgFieldCode
     */
    public String getOrgFieldCode() {
        return orgFieldCode;
    }

    /**
     * @param orgFieldCode
     *        the orgFieldCode to set
     */
    public void setOrgFieldCode(String orgFieldCode) {
        this.orgFieldCode = orgFieldCode;
    }

    /**
     * @return the materialQueryParam
     */
    public MaterialQueryCondParam getMaterialQueryParam() {
        return materialQueryParam;
    }

    /**
     * @param materialQueryParam
     *        the materialQueryParam to set
     */
    public void setMaterialQueryParam(MaterialQueryCondParam materialQueryParam) {
        this.materialQueryParam = materialQueryParam;
    }

}
