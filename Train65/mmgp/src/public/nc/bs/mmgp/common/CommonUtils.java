package nc.bs.mmgp.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.pubapp.pub.bill.IMakeTime;
import nc.itf.uap.rbac.datapermission.IDataPermissionInnerQueryService;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.rbac.core.dataperm.DataPermSplitTableConfig;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.MultiLangContext;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.CarrierRuntimeException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-3-30
 *
 * @author wangweiu
 */
public class CommonUtils {
    /**
     * @param <T>
     * @param clazz
     * @param result
     * @return
     * @deprecated
     * @see nc.vo.mmgp.util.MMArrayUtil#createArray(Class, List)
     */
    public static <T> T[] createArray(Class<T> clazz,
                                      List<T> result) {
        return MMArrayUtil.createArray(clazz, result);
    }

    @SuppressWarnings("deprecation")
    public static String getTableName(Class< ? extends SuperVO> clazz) {
        return newObject(clazz).getTableName();
    }

    @SuppressWarnings("deprecation")
    public static String getPKFieldName(Class< ? extends SuperVO> clazz) {
        return newObject(clazz).getPKFieldName();
    }

    @SuppressWarnings("unchecked")
    public static String getPKFieldName(String superVOclassName) {
        try {
            return getPKFieldName((Class< ? extends SuperVO>) Class.forName(superVOclassName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T newObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] convertObjArray(Object[] objs,
                                          Class<T> clazz) {
        if (objs == null) {
            return null;
        }
        List<T> result = new ArrayList<T>();
        for (Object obj : objs) {
            result.add((T) obj);
        }
        return MMArrayUtil.createArray(clazz, result);
    }

    /**
     * 返回环境对应的多语文本
     *
     * @return 环境对应的多语文本
     */
    public static String getLangStrSuffix() {
        String suffix = "";
        int langSeq = MultiLangContext.getInstance().getCurrentLangSeq().intValue();
        if (langSeq != 1) {
            suffix = String.valueOf(langSeq);
        }
        return suffix;
    }

    public static void setPk_org_v(SuperVO vo) {
        // 如果有pk_org没有pk_org_v,自动查一下，给设置上
        String pk_org = MMStringUtil.objectToString(vo.getAttributeValue("pk_org"));
        if (MMStringUtil.isNotEmpty(pk_org)) {
            String pk_org_v = MMStringUtil.objectToString(vo.getAttributeValue("pk_org_v"));
            if (MMStringUtil.isEmpty(pk_org_v)) {
                try {
                    String orgVidByOid = getPk_org_v(pk_org);
                    vo.setAttributeValue("pk_org_v", orgVidByOid);
                } catch (Exception e) {
                    // 设不上默认值也让保存
                    Logger.error(e, e);
                }
            }
        }
    }

    /**
     * 设置制单日期、制单人
     *
     * @param vos
     */
    public static void setBillmakerAndDate(ISuperVO... vos) {
        for (ISuperVO vo : vos) {
            String billmaker = MMStringUtil.objectToString(vo.getAttributeValue(MMGlobalConst.VBILLMAKER));
            if (MMStringUtil.isEmptyWithTrim(billmaker)) {
                vo.setAttributeValue(MMGlobalConst.VBILLMAKER, AppContext.getInstance().getPkUser());
            }

            String dmakedateFiled = getMakeTimeColumn(vo);

            if (MMStringUtil.isEmpty(dmakedateFiled)) {
                continue;
            }

            String dmakedate = MMStringUtil.objectToString(vo.getAttributeValue(dmakedateFiled));
            if (MMStringUtil.isEmptyWithTrim(dmakedate)) {
                vo.setAttributeValue(dmakedateFiled, AppContext.getInstance().getBusiDate());
            }
        }
    }

    /**
     * 获取制单日期字段
     *
     * @param bill
     * @return
     */
    private static String getMakeTimeColumn(ISuperVO bill) {
        Map<String, String> makeTimeMap = null;
        String entityName = bill.getMetaData().getEntityName();
        IBusinessEntity entity = null;
        try {
            entity = MDBaseQueryFacade.getInstance().getBusinessEntityByFullName(entityName);
            if (null != entity) {
                makeTimeMap = entity.getBizInterfaceMapInfo(IMakeTime.class.getName());
            }
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }

        if (MMMapUtil.isEmpty(makeTimeMap)) {
            return null;
        }
        String dmakeDate = makeTimeMap.get(IMakeTime.NAME_MAKETIME);
        return dmakeDate == null ? MMGlobalConst.DMAKEDATE : dmakeDate;
    }

    public static String getPk_org_v(String pk_org) throws BusinessException {
        IOrgUnitQryService orgService = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
        String orgVidByOid = orgService.getOrg(pk_org).getPk_vid();
        return orgVidByOid;
    }

    public static String getPowerSql(String joinColumn,
                                     String resouceCode,
                                     String operatecode){

    	if(MMStringUtil.isEmpty(joinColumn)){
    		throw new CarrierRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0045")/*@res " 连接字段名不能为空"*/);
    	}
        String powerSql = null;
        String pk_user = AppContext.getInstance().getPkUser();
        String pk_group = AppContext.getInstance().getPkGroup();

        if (resouceCode == null) {
            return null;
        }
        try {
            DataPermSplitTableConfig config = null;
            IDataPermissionInnerQueryService queryService =
                    NCLocator.getInstance().lookup(IDataPermissionInnerQueryService.class);
            try {
                config = queryService.getDataPermSplitConfig(pk_user, pk_group);
            } catch (BusinessException be) {
                Logger.error(be.getMessage(), be);
            }

            String dataPermProfileTableName =
                    config.getDataPermSplitTableNameByResourceCode(resouceCode, operatecode);

            if (!StringUtil.isEmptyWithTrim(dataPermProfileTableName)) {
                powerSql = joinColumn + " in ( select pk_doc from " + dataPermProfileTableName + ")";

            }
            Logger.debug("the datapower sql  is " + powerSql);

        } catch (Exception e) {
            Logger.error(e.getMessage(), e);

        }
        return powerSql;
    }

}