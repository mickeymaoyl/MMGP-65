package nc.impl.mmgp.uif2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnGrandQueryService;
import nc.itf.pubapp.uif2app.components.grand.IGrandAggVosQueryService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBillQuery;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:May 7, 2013
 * @author:wangweir
 */
public class MMGPCmnGrandQueryService implements IMMGPCmnGrandQueryService {

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPGrandQueryService#queryBySchemeForBom(java.lang.Class,
     * nc.ui.querytemplate.querytree.IQueryScheme, java.util.Map)
     */
    @Override
    public Object[] queryBySchemeForGrand(Class< ? extends AbstractBill> clazz,
                                          IQueryScheme queryScheme,
                                          Map<String, CircularlyAccessibleValueObject> relationShip)
            throws BusinessException {

        try {
            QuerySchemeProcessor processer = new QuerySchemeProcessor(queryScheme);
            // 固定查询条件
            processer.appendCurrentGroup();
            // 查询有权限的组织
            processer.appendFuncPermissionOrgSql();
            // 为了解决数据丢失问题
            IGrandAggVosQueryService query = NCLocator.getInstance().lookup(IGrandAggVosQueryService.class);

            AbstractBill[] result =
                    query.query(
                        queryScheme,
                        this.getOrderString(clazz, queryScheme),
                        (HashMap<String, CircularlyAccessibleValueObject>) relationShip,
                        clazz);

            return filterByApprove(queryScheme, result);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return new AbstractBill[0];
        }
    }

    protected Object[] filterByApprove(IQueryScheme queryScheme,
                                       AbstractBill[] result) {
        if (MMArrayUtil.isEmpty(result)) {
            return null;
        }

        String billTypeCodeItemKey = MMMetaUtils.getIFlowBizItfMapKey(result[0], IFlowBizItf.ATTRIBUTE_BILLTYPE);

        if (MMStringUtil.isEmpty(billTypeCodeItemKey)) {
            return result;
        }

        String billTypeCode = (String) result[0].getParent().getAttributeValue(billTypeCodeItemKey);

        if (MMStringUtil.isEmpty(billTypeCode)) {
            return result;
        }

        // 过滤状态
        return MMGPApproveFilter.filterForApprove(queryScheme, result, billTypeCode);
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Object[] queryBySchemeForGrandWithoutLazyQuery(Class< ? extends AbstractBill> clazz,
                                                          IQueryScheme queryScheme,
                                                          Map<String, CircularlyAccessibleValueObject> relationShip)
            throws BusinessException {
        try {
            AbstractBill[] lazyResults =
                    (AbstractBill[]) this.queryBySchemeForGrand(clazz, queryScheme, relationShip);
            if (MMArrayUtil.isEmpty(lazyResults) || lazyResults.length == 1) {
                return lazyResults;
            }

            List<String> ids = new ArrayList<String>();
            for (AbstractBill lazyAbstractBill : lazyResults) {
                ids.add(lazyAbstractBill.getPrimaryKey());
            }

            MMGPGrandBillQuery query = new MMGPGrandBillQuery(clazz);
            AbstractBill[] result = (AbstractBill[]) query.query(ids.toArray(new String[0]));

            return filterByApprove(queryScheme, result);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return new Object[0];
    }

    /*
     * (non-Javadoc)
     * @see nc.itf.mmgp.uif2.IMMGPCmnGrandQueryService#queryByPkForGrandWithoutLazyQuery(java.lang.Class,
     * java.lang.String[])
     */
    @Override
    public Object[] queryByPkForGrandWithoutLazyQuery(Class< ? extends AbstractBill> clazz,
                                                      String[] pks) throws BusinessException {
        try {
            @SuppressWarnings({"rawtypes", "unchecked" })
            MMGPGrandBillQuery query = new MMGPGrandBillQuery(clazz);
            return query.query(pks);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    protected String getOrderString(Class< ? > clazz,
                                    IQueryScheme queryScheme) throws MetaDataException {
        IBusinessEntity bean =
                (IBusinessEntity) MDQueryService.lookupMDQueryService().getBeanByFullClassName(clazz.getName());

        String billNo =
                MMMetaUtils.getBizInterfaceMapInfo(IFlowBizItf.class.getName(), bean, IFlowBizItf.ATTRIBUTE_BILLNO);

        QuerySchemeProcessor qrySchemeProcessor = new QuerySchemeProcessor(queryScheme);
        String mainTableAlias = qrySchemeProcessor.getMainTableAlias();
        SqlBuilder orderBy = new SqlBuilder();
        orderBy.append(" order by " + mainTableAlias + "." + MMGlobalConst.PK_ORG);
        if (MMStringUtil.isNotEmpty(billNo)) {
            orderBy.append(", " + mainTableAlias + "." + billNo);
        }
        return orderBy.toString();
    }

}
