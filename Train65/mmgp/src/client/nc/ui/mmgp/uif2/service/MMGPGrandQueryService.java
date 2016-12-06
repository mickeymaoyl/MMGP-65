package nc.ui.mmgp.uif2.service;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnGrandQueryService;
import nc.md.model.IBean;
import nc.md.model.access.javamap.AggVOStyle;
import nc.md.model.access.javamap.BeanStyleEnum;
import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.components.grand.model.AbstractGrandQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙前台查询服务层
 * </p>
 *
 * @since: 创建日期:May 7, 2013
 * @author:wangweir
 */
public class MMGPGrandQueryService extends AbstractGrandQueryService {

    /**
     * 返回数据的VO类型，需要AggVO
     */
    private Class< ? extends AbstractBill> clazz = null;

    /**
     * 自定义查询服务接口
     */
    private String queryServiceItf;

    /**
     * 查询服务
     */
    private IMMGPCmnGrandQueryService queryService;

    /**
     * LoginContext
     */
    private LoginContext context;

    /*
     * (non-Javadoc)
     * @see
     * nc.ui.pubapp.uif2app.query2.model.IQueryService#queryByQueryScheme(nc.ui.querytemplate.querytree.IQueryScheme)
     */
    @Override
    public Object[] queryByQueryScheme(IQueryScheme queryScheme) throws Exception {
        Map<String, CircularlyAccessibleValueObject> relationShip = this.getGrandTabAndVOMap();
        return this.getQueryService().queryBySchemeForGrand(this.getClazz(), queryScheme, relationShip);
    }

    /**
     * @return the queryServiceItf
     */
    public String getQueryServiceItf() {
        return queryServiceItf;
    }

    /**
     * @param queryServiceItf
     *        the queryServiceItf to set
     */
    public void setQueryServiceItf(String queryServiceItf) {
        try {
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName(queryServiceItf);
            if (!IMMGPCmnGrandQueryService.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0029")/*@res "后台接口必须继承nc.itf.mmgp.uif2.IMMGPGrandQueryService"*/);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0092", null, new String[]{queryServiceItf})/*配置的后台接口[{0}]不存在*/);
        }
        this.queryServiceItf = queryServiceItf;
    }

    /**
     * 获取查询服务
     *
     * @return the queryService
     */
    public IMMGPCmnGrandQueryService getQueryService() {
        if (queryService == null) {
            if (MMStringUtil.isNotEmpty(queryServiceItf)) {
                queryService = (IMMGPCmnGrandQueryService) NCLocator.getInstance().lookup(queryServiceItf);
            } else {
                queryService = NCLocator.getInstance().lookup(IMMGPCmnGrandQueryService.class);
            }
        }
        return queryService;
    }

    /**
     * @return the context
     */
    public LoginContext getContext() {
        return context;
    }

    /**
     * @param context
     *        the context to set
     */
    public void setContext(LoginContext context) {
        this.context = context;
    }

    /**
     * @return the clazz
     */
    @SuppressWarnings("unchecked")
    public Class< ? extends AbstractBill> getClazz() {
        if (this.clazz == null) {
            IBean bean = MMGPMetaUtils.getIBean(this.getContext());
            if (BeanStyleEnum.AGGVO_HEAD.equals(bean.getBeanStyle().getStyle())) {
                String className = ((AggVOStyle) bean.getBeanStyle()).getAggVOClassName();
                try {
                    this.clazz = (Class< ? extends AbstractBill>) Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            }
        }
        return clazz;
    }

}