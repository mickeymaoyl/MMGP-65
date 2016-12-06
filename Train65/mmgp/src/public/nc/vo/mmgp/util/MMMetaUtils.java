package nc.vo.mmgp.util;

import java.util.Map;

import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.vo.bd.meta.IBDObject;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 元数据工具类 </b> </p> 创建日期:2012-11-23
 * 
 * @author wangweiu
 */
public class MMMetaUtils {
    public static final String PID = "pid";

    public static final String ID = "id";

    public static final String CODE = "code";

    public static final String NAME = "name";

    /**
     * 根据元数据id查询Bean
     * 
     * @param mdid
     *        元数据id
     * @return 根据元数据id查询Bean
     */
    public static IBean getBeanByMetaID(String mdid) {
        try {
            IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(mdid);
            return bean;
        } catch (MetaDataException e) {
            throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0108", null, new String[]{mdid,e.getMessage()})/*查询元数据[{0}]出现错误:{1}*/);
        }
    }

    /**
     * 根据类名（全路径）查询Bean
     * 
     * @param classFullName
     *        类名
     * @return 根据类名查询Bean
     */
    public static IBean getBeanByClassFullName(String classFullName) {
        try {
            IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(classFullName);
            return bean;
        } catch (MetaDataException e) {
            throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0108", null, new String[]{classFullName,e.getMessage()})/*查询元数据[{0}]出现错误:{1}*/);
        }
    }

    /**
     * 获取code映射的字段
     * 
     * @param bean
     *        input Bean
     * @return code映射的字段
     */
    public static String getCodeFieldName(IBean bean) {
        return getNamePathValue(bean, CODE);
    }

    /**
     * 获取name映射的字段
     * 
     * @param bean
     *        input Bean
     * @return name映射的字段
     */
    public static String getNameFieldName(IBean bean) {
        return getNamePathValue(bean, NAME);
    }

    /**
     * 获取 pid映射的字段
     * 
     * @param bean
     *        input Bean
     * @return pid映射的字段
     */
    public static String getParentPKFieldName(IBean bean) {
        return getNamePathValue(bean, PID);
    }

    /**
     * 返回Bean主键映射的字段
     * 
     * @param bean
     *        input Bean
     * @return 主键的映射字段
     */
    public static String getPKFieldName(IBean bean) {
        return getNamePathValue(bean, ID);
    }

    /**
     * 获取IBDObject映射的所有属性值
     * 
     * @param bean
     *        input Bean
     * @return IBDObject映射的所有属性值
     */
    public static Map<String, String> getNamePathMapForBDObject(IBean bean) {
        Map<String, String> name_path_map =
                ((IBusinessEntity) bean).getBizInterfaceMapInfo(IBDObject.class.getName());
        return name_path_map;
    }

    /**
     * 根据IBDObject映射的属性值 返回输入属性的映射值
     * 
     * @param bean
     *        Bean
     * @param key
     *        input attribute name
     * @return IBusinessEntity映射的属性值
     */
    public static String getNamePathValue(IBean bean,
                                          String key) {
        Map<String, String> name_path_map =
                ((IBusinessEntity) bean).getBizInterfaceMapInfo(IBDObject.class.getName());
        return name_path_map.get(key);
    }

    /**
     * 获取映射属性
     * 
     * @param interfaceName
     *        业务接口名称
     * @param vo
     *        VO
     * @param key
     *        属性
     * @return 映射属性
     */
    public static String getBizInterfaceMapInfo(String interfaceName,
                                                AggregatedValueObject vo,
                                                String key) {
        IBusinessEntity entity = null;
        try {
            entity =
                    MDBaseQueryFacade.getInstance().getBusinessEntityByFullName(
                        ((SuperVO) vo.getParentVO()).getMetaData().getEntityName());
        } catch (MetaDataException ex) {
            ExceptionUtils.wrappException(ex);
        }

        return getBizInterfaceMapInfo(interfaceName, entity, key);
    }

    public static String getBizInterfaceMapInfo(String interfaceName,
                                                IBusinessEntity entity,
                                                String key) {
        if (null == entity) {
            return null;
        }
        Map<String, String> map = entity.getBizInterfaceMapInfo(interfaceName);
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 获取审计信息相关接口
     * 
     * @param vo
     *        聚合VO
     * @param key
     *        审计信息属性
     * @return 映射属性
     */
    public static String getIFlowBizItfMapKey(AggregatedValueObject vo,
                                              String key) {
        return getBizInterfaceMapInfo(IFlowBizItf.class.getName(), vo, key);
    }

    /**
     * 获取流程平台业务接口属性
     * 
     * @param vo
     *        IBill
     * @param key
     *        流程平台业务接口属性
     * @return 映射属性
     */
    public static String getIFlowBizItfMapKey4IBill(IBill vo,
                                              String key) {
        return getBizInterfaceMapInfo4IBill(IFlowBizItf.class.getName(), vo, key);
    }

    /**
     * 获取映射属性
     * 
     * @param interfaceName
     *        业务接口名称
     * @param vo
     *        VO
     * @param key
     *        属性
     * @return 映射属性
     */
    public static String getBizInterfaceMapInfo4IBill(String interfaceName,
                                                IBill vo,
                                                String key) {
        IBusinessEntity entity = null;
        try {
            entity =
                    MDBaseQueryFacade.getInstance().getBusinessEntityByFullName(
                        vo.getParent().getMetaData().getEntityName());
        } catch (MetaDataException ex) {
            ExceptionUtils.wrappException(ex);
        }

        return getBizInterfaceMapInfo(interfaceName, entity, key);
    }

}
