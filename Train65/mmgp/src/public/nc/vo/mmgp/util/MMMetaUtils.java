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
 * <b> Ԫ���ݹ����� </b> </p> ��������:2012-11-23
 * 
 * @author wangweiu
 */
public class MMMetaUtils {
    public static final String PID = "pid";

    public static final String ID = "id";

    public static final String CODE = "code";

    public static final String NAME = "name";

    /**
     * ����Ԫ����id��ѯBean
     * 
     * @param mdid
     *        Ԫ����id
     * @return ����Ԫ����id��ѯBean
     */
    public static IBean getBeanByMetaID(String mdid) {
        try {
            IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(mdid);
            return bean;
        } catch (MetaDataException e) {
            throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0108", null, new String[]{mdid,e.getMessage()})/*��ѯԪ����[{0}]���ִ���:{1}*/);
        }
    }

    /**
     * ����������ȫ·������ѯBean
     * 
     * @param classFullName
     *        ����
     * @return ����������ѯBean
     */
    public static IBean getBeanByClassFullName(String classFullName) {
        try {
            IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(classFullName);
            return bean;
        } catch (MetaDataException e) {
            throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0108", null, new String[]{classFullName,e.getMessage()})/*��ѯԪ����[{0}]���ִ���:{1}*/);
        }
    }

    /**
     * ��ȡcodeӳ����ֶ�
     * 
     * @param bean
     *        input Bean
     * @return codeӳ����ֶ�
     */
    public static String getCodeFieldName(IBean bean) {
        return getNamePathValue(bean, CODE);
    }

    /**
     * ��ȡnameӳ����ֶ�
     * 
     * @param bean
     *        input Bean
     * @return nameӳ����ֶ�
     */
    public static String getNameFieldName(IBean bean) {
        return getNamePathValue(bean, NAME);
    }

    /**
     * ��ȡ pidӳ����ֶ�
     * 
     * @param bean
     *        input Bean
     * @return pidӳ����ֶ�
     */
    public static String getParentPKFieldName(IBean bean) {
        return getNamePathValue(bean, PID);
    }

    /**
     * ����Bean����ӳ����ֶ�
     * 
     * @param bean
     *        input Bean
     * @return ������ӳ���ֶ�
     */
    public static String getPKFieldName(IBean bean) {
        return getNamePathValue(bean, ID);
    }

    /**
     * ��ȡIBDObjectӳ�����������ֵ
     * 
     * @param bean
     *        input Bean
     * @return IBDObjectӳ�����������ֵ
     */
    public static Map<String, String> getNamePathMapForBDObject(IBean bean) {
        Map<String, String> name_path_map =
                ((IBusinessEntity) bean).getBizInterfaceMapInfo(IBDObject.class.getName());
        return name_path_map;
    }

    /**
     * ����IBDObjectӳ�������ֵ �����������Ե�ӳ��ֵ
     * 
     * @param bean
     *        Bean
     * @param key
     *        input attribute name
     * @return IBusinessEntityӳ�������ֵ
     */
    public static String getNamePathValue(IBean bean,
                                          String key) {
        Map<String, String> name_path_map =
                ((IBusinessEntity) bean).getBizInterfaceMapInfo(IBDObject.class.getName());
        return name_path_map.get(key);
    }

    /**
     * ��ȡӳ������
     * 
     * @param interfaceName
     *        ҵ��ӿ�����
     * @param vo
     *        VO
     * @param key
     *        ����
     * @return ӳ������
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
     * ��ȡ�����Ϣ��ؽӿ�
     * 
     * @param vo
     *        �ۺ�VO
     * @param key
     *        �����Ϣ����
     * @return ӳ������
     */
    public static String getIFlowBizItfMapKey(AggregatedValueObject vo,
                                              String key) {
        return getBizInterfaceMapInfo(IFlowBizItf.class.getName(), vo, key);
    }

    /**
     * ��ȡ����ƽ̨ҵ��ӿ�����
     * 
     * @param vo
     *        IBill
     * @param key
     *        ����ƽ̨ҵ��ӿ�����
     * @return ӳ������
     */
    public static String getIFlowBizItfMapKey4IBill(IBill vo,
                                              String key) {
        return getBizInterfaceMapInfo4IBill(IFlowBizItf.class.getName(), vo, key);
    }

    /**
     * ��ȡӳ������
     * 
     * @param interfaceName
     *        ҵ��ӿ�����
     * @param vo
     *        VO
     * @param key
     *        ����
     * @return ӳ������
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
