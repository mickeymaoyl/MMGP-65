package nc.ui.mmgp.uif2.service;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnQueryService;
import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessRuntimeException;

/**
 * <b> 前台通用查询服务(主子表只查询表头（懒加载）) </b>
 * <p>
 * 实现通用查询
 * </p>
 * <p>
 * add 2013-04-16 支持查询接口注入，允许查询继承查询接口添加自己的方法。
 * </p>
 * 创建日期:2012-11-23
 *
 * @author wangweiu
 */
public class MMGPQueryService implements IQueryService {
    private Class< ? > clazz = null;

    private IMMGPCmnQueryService queryService;

    private String queryServiceItf;

    @Override
    public Object[] queryByWhereSql(String whereSql) throws Exception {
        try {
            // 懒加载
            return getQueryService().cmnQueryDatasByConditionLazyLoad(clazz, whereSql, true);
        } catch (Exception e) {
            throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "pubapp_0",
                "0pubapp-0007")/*
                                * @res "查询单据发生异常"
                                */, e);
        }
    }

    public IMMGPCmnQueryService getQueryService() {
        if (queryService == null) {
            if (MMStringUtil.isNotEmpty(queryServiceItf)) {
                queryService = (IMMGPCmnQueryService) NCLocator.getInstance().lookup(queryServiceItf);
            } else {
                queryService = NCLocator.getInstance().lookup(IMMGPCmnQueryService.class);
            }
        }
        return queryService;
    }

    public String getClassName() {
        return clazz.getName();
    }

    public void setClassName(String className) {
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
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
            if (!IMMGPCmnQueryService.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0030")/*@res "后台接口必须继承nc.itf.mmgp.uif2.IMMGPCmnQueryService"*/);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0092", null, new String[]{queryServiceItf})/*配置的后台接口[{0}]不存在*/);
        }
        this.queryServiceItf = queryServiceItf;
    }

}