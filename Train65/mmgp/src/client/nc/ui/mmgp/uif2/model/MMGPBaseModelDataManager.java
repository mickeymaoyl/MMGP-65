package nc.ui.mmgp.uif2.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.mmgp.common.CommonUtils;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.md.model.access.javamap.AggVOStyle;
import nc.md.model.access.javamap.BeanStyleEnum;
import nc.md.model.access.javamap.ExtAggVOStyle;
import nc.md.model.access.javamap.IBeanStyle;
import nc.ui.mmgp.uif2.service.MMGPQueryService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.utils.MMGPMsgUtils;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.pubapp.uif2app.model.IRefreshable;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.uap.sf.SFClientUtil2;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.IAppModelDataManagerEx;
import nc.ui.uif2.model.IQueryAndRefreshManagerEx;
import nc.ui.uif2.model.ModelDataDescriptor;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.pubapp.query2.sql.process.QuerySchemeUtils;
import nc.vo.pubapp.res.Variable;
import nc.vo.uif2.LoginContext;
import nc.vo.util.ConditionBuilder;
import nc.vo.util.VisibleUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * ��չ��UAP��
 * <p>
 * 1.����Ĭ�ϲ�ѯservice<br>
 * 
 * @author wangweiu
 */
public class MMGPBaseModelDataManager implements
    IModelDataManager,
    IAppModelDataManagerEx,
    IRefreshable,
    IQueryAndRefreshManagerEx,
    INeedSeal,
    BeanFactoryAware {

    public static final String ENABLE_DEFAULT = " and (isnull("
        + IBaseServiceConst.ENABLESTATE_FIELD
        + ","
        + IPubEnumConst.ENABLESTATE_ENABLE
        + ") = "
        + IPubEnumConst.ENABLESTATE_ENABLE
        + ")";

    private List<DataPowerBean> dataPowers = new ArrayList<DataPowerBean>();

    private boolean showSealDataFlag = false;

    private Object userObject;

    private boolean needSeal = false;

    private AbstractUIAppModel model;

    private IQueryService service;

    protected String sqlWhere;

    private String billTypeCode;

    public AbstractUIAppModel getModel() {
        return this.model;
    }

    public IQueryService getService() {
        return this.service;
    }

    @Override
    public void initModel() {
        this.getModel().initModel(null);
    }

    /**
     * set select row
     */
    @Override
    public void refresh() {
        this.initModelBySqlWhere(sqlWhere);
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

    @Override
    public void initModelBySqlWhere(IQueryScheme qryScheme) {
        // 2013-12-9 gaotx �����ѯ�ӱ�û�й���dr = 0�����ݣ���ʱ�����Ժ������ѯ��ȫ��ȥ��
        StringBuilder sbWhere = getWhereBuilder(qryScheme);

        initModelBySqlWhere(sbWhere.toString(), qryScheme.getName());
    }
    
    /**
     * ��ȡWhere��ѯ����Builder,����ӱ��ѯ����û����dr = 1���ݵ�����
     * gaotx ��ʱ�����Ժ������ѯ��ȫ��ȥ��
     * @param qryScheme
     * @return
     */
    protected StringBuilder getWhereBuilder(IQueryScheme qryScheme) {
        QuerySchemeProcessor processor = new QuerySchemeProcessor(qryScheme);
        IBean mainBean = QuerySchemeUtils.getMainBean(qryScheme);
        String mainBeanOuter = mainBean.getTable().getName();

        StringBuilder sbWhere = new StringBuilder();
        String mainTableAlias = processor.getMainTableAlias();
        String primaryName = mainBean.getPrimaryKey().getPKColumn().getName();
        sbWhere.append(mainBeanOuter).append(".").append(primaryName);
        sbWhere.append(" in ( select " + mainTableAlias + "." + primaryName + " ").append(
            processor.getFinalFromWhere());
        sbWhere.append(" )");
        return sbWhere;
    }

    protected String toWhereSql(String oldSql) {
        String sqlwhere = oldSql;
        if (MMStringUtil.isEmptyWithTrim(sqlwhere)) {
            sqlwhere = " 1 = 1";
        }
        if (needSeal) {
            if (showSealDataFlag) {
                if (sqlwhere.contains(ENABLE_DEFAULT)) {
                    sqlwhere = sqlwhere.replace(ENABLE_DEFAULT, "");
                }
            } else {
                if (!sqlwhere.contains(ENABLE_DEFAULT)) {
                    sqlwhere = sqlwhere + ENABLE_DEFAULT;
                }
            }
        }
        LoginContext context = getModel().getContext();
        String[] pk_orgs = context.getFuncInfo().getFuncPermissionPkorgs();
        String orgSql = null;
        try {
            IBean bean = MMGPMetaUtils.getIBean(context);
            if (bean.getTagString() != null && bean.getTagString().contains("BDMODE")) {
                orgSql =
                        VisibleUtil.getRefVisibleCondition(
                            context.getPk_group(),
                            pk_orgs,
                            MMGPMetaUtils.getMetaId(context));
            } else {
                // add by jilu 20130520
                // ���ݽڵ㣬��ע��ܿ�ģʽ���Ͳ���VisibleUtil�ˣ�����ΰ���������˴���ʱ����
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
                // end
            }
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        if (MMStringUtil.isNotEmpty(orgSql)) {
            sqlwhere = sqlwhere + " and " + orgSql;
        }
        // wrr add ---begin
        // Ϊ�˽����Ĭ�ϲ�ѯ���������������޸�Ĭ�ϲ�ѯ���������Ĳ�ѯ���������в�ѯʱ����ѯ�������ε�����֯�ֶεķǿ�У�飬��ɲ�ѯ�������е����ݡ�
        else {
            // ֻ������Ȩ�޵Ĺ��ܽڵ� - wangweiu
            String funccode = getModel().getContext().getFuncInfo().getFuncode();
            if (SFClientUtil2.getFuncRegisterVO(funccode) != null) {
                sqlwhere = sqlwhere + " and 1<>1";
            }
        }
        // wrr add ---end

        String powerSql = getDataPowerSql();
        if (MMStringUtil.isNotEmpty(powerSql)) {
            sqlwhere = sqlwhere + " and " + powerSql;
        }
        return sqlwhere;
    }

    /**
     * @return �ܷ�ִ��ˢ�£����û��ִ�й���ѯ����ˢ��û�����壬����false
     */
    @Override
    public boolean refreshable() {
        return !MMStringUtil.isEmptyWithTrim(sqlWhere);
    }

    public void setModel(AbstractUIAppModel model) {
        this.model = model;
    }

    public void setService(IQueryService service) {
        this.service = service;
    }

    @Override
    public void initModelBySqlWhere(String sqlwhere) {
        initModelBySqlWhere(sqlwhere, "");
    }

    protected synchronized void initModelBySqlWhere(String sqlwhere,
                                                    String modelDataDescriptorName) {
        /* Nov 19, 2013 wangweir �޸�ԭ�� Begin */
        if (this.isNeedSeal() && MMStringUtil.isEmpty(sqlwhere)) {
            showQueryResult(modelDataDescriptorName, null);
            return;
        }

        this.sqlWhere = toWhereSql(sqlwhere);
        initDefaultService();
        try {
            Object[] objs = this.service.queryByWhereSql(this.sqlWhere);
            showQueryResult(modelDataDescriptorName, objs);
        } catch (Exception e) {
            throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "pubapp_0",
                "0pubapp-0007")/*
                                * @res "��ѯ���ݷ����쳣"
                                */, e);
        }
    }

    protected void showQueryResult(String modelDataDescriptorName,
                                   Object[] objs) {
        if (objs != null && objs.length == Variable.getMaxQueryCount()) {
            // String hint =
            // "��ѯ���̫��ֻ������ " + Variable.getMaxQueryCount() +
            // " �����ݣ�����С��Χ�ٴβ�ѯ";
            String hint =
                    nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                        "pubapp_0",
                        "0pubapp-0268",
                        null,
                        new String[]{"" + Variable.getMaxQueryCount() })/*
                                                                         * @ res "��ѯ���̫��ֻ������{0}�����ݣ�����С��Χ�ٴβ�ѯ"
                                                                         */;
            MessageDialog.showHintDlg(this.getModel().getContext().getEntranceUI(), null, hint);
        }
        processQueryData(objs);
        ModelDataDescriptor modelDataDescriptor = new ModelDataDescriptor(modelDataDescriptorName);
        this.getModel().initModel(objs, modelDataDescriptor);
        MMGPMsgUtils.showQueryStatusBarMsg(getModel());
    }

    protected String getDataPowerSql() {
        if (MMCollectionUtil.isEmpty(dataPowers)) {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < dataPowers.size(); i++) {
            DataPowerBean dataPower = dataPowers.get(i);
            String powerSql =
                    CommonUtils.getPowerSql(
                        dataPower.getJoinColumn(),
                        dataPower.getResouceCode(),
                        dataPower.getOperatecode());
            if (MMStringUtil.isEmpty(powerSql)) {
                continue;
            }
            if (sql.length() > 0) {
                sql.append(" and ");
            }
            sql.append(powerSql);
        }
        return sql.toString();

    }

    public void showStatusBarMsg(int size) {
        if (size == 0) {
            ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant.getQueryNullInfo(), getModel().getContext());
        } else {
            ShowStatusBarMsgUtil
                .showStatusBarMsg(IShowMsgConstant.getQuerySuccessInfo(size), getModel().getContext());
        }
    }

    protected void processQueryData(Object[] objs) {

    }

    protected void initDefaultService() {
        // ���������ڶ���������⣬�ȸ�Ϊ��������
        // Ĭ��ʹ��ͨ��service
        if (getService() == null) {
            MMGPQueryService service = new MMGPQueryService();
            service.setClassName(getClassFullName());
            setService(service);
        }
    }

    protected String getClassFullName() {
        return MMGPMetaUtils.getClassFullName(getModel().getContext());
    }

    public boolean isShowSealDataFlag() {
        return showSealDataFlag;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public void initModelByQueryScheme(IQueryScheme queryScheme) {
        if (isQueryContainIsApproving(queryScheme) && MMStringUtil.isNotEmpty(this.getBillTypeCode())) {
            queryWithApproving(queryScheme);
        } else {
            initModelBySqlWhere(queryScheme);
        }
    }

    /**
     * �����������Ĳ�ѯ�����߼�
     * 
     * @param queryScheme
     */
    protected void queryWithApproving(IQueryScheme queryScheme) {
        initDefaultService();
        if (this.getService() instanceof MMGPQueryService) {
            Object[] data = null;
            try {
                data =
                        ((MMGPQueryService) this.service).getQueryService().cmnQueryDatasByScheme(
                            this.getClazz(),
                            queryScheme,
                            billTypeCode);
            } catch (BusinessException e) {
                ExceptionUtils.wrappException(e);
            }
            this.showQueryResult(queryScheme.getName(), data);
        } else {
            initModelBySqlWhere(queryScheme);
        }
    }

    @SuppressWarnings("unchecked")
    protected Class< ? extends AbstractBill> getClazz() {
        try {
            IBeanStyle beanStyle =
                    MDBaseQueryFacade.getInstance().getBeanByFullClassName(this.getClassFullName()).getBeanStyle();
            String classFullName = this.getClassFullName();
            if (beanStyle.getStyle().equals(BeanStyleEnum.AGGVO_HEAD)) {
                classFullName = ((AggVOStyle) beanStyle).getAggVOClassName();
            } else if (beanStyle.getStyle().equals(BeanStyleEnum.EXTAGGVO_HEAD)) {
                classFullName = ((ExtAggVOStyle) beanStyle).getExtAggVOClassName();
            }
            return (Class< ? extends AbstractBill>) Class.forName(classFullName);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    /**
     * �Ƿ����������
     * 
     * @param queryScheme
     * @return
     */
    protected boolean isQueryContainIsApproving(IQueryScheme queryScheme) {
        IBusinessEntity entity = null;
        try {
            String fullName = MMGPMetaUtils.getIBean(this.getModel().getContext()).getFullName();
            entity = MDBaseQueryFacade.getInstance().getBusinessEntityByFullName(fullName);
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        if (entity == null || !entity.isImplementBizInterface(IFlowBizItf.class.getName())) {
            return false;
        }

        QuerySchemeProcessor qrySchemeProcessor = new QuerySchemeProcessor(queryScheme);
        QueryCondition condition = qrySchemeProcessor.getQueryCondition(MMGlobalConst.BISAPPROVING);
        return null != condition;
    }

    /**
     * @return the billTypeCode
     */
    public String getBillTypeCode() {
        return billTypeCode;
    }

    /**
     * @param billTypeCode
     *        the billTypeCode to set
     */
    public void setBillTypeCode(String billTypeCode) {
        this.billTypeCode = billTypeCode;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
        Collection<DataPowerBean> datapowerBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(factory, DataPowerBean.class).values();
        if (MMCollectionUtil.isNotEmpty(datapowerBeans)) {
            dataPowers.addAll(datapowerBeans);
        }

    }
}
