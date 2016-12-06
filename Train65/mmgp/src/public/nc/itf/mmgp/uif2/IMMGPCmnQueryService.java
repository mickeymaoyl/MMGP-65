package nc.itf.mmgp.uif2;

import java.util.List;
import java.util.Map;


import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.lazilyload.BillLazilyLoaderVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public interface IMMGPCmnQueryService {

    Object[] cmnQueryAllDatas(Class< ? > clazz) throws BusinessException;

    Object[] cmnQueryDatasByCondition(Class< ? > clazz,
                                      String con) throws BusinessException;

    Object cmnQueryDatasByPk(Class< ? > clazz,
                             String pk) throws BusinessException;

    /**
     * @param clazz
     * @param pks
     * @return
     * @throws BusinessException
     */
    Object[] cmnQueryDatasByPks(Class< ? > clazz,
                                String[] pks) throws BusinessException;

    Object[] cmnQueryDatasByConditionWithDataPermission(Class< ? > clazz,
                                                        String con,
                                                        String resouceCode,
                                                        String opratecode) throws BusinessException;

    Object[] cmnQueryDatasByConditionLazyLoad(Class< ? > clazz,
                                              String con,
                                              boolean isLazyLoad) throws BusinessException;

    IBill cmnQueryChildrenByParentPK(Class< ? > clazz,
                                     String parentPk) throws BusinessException;

    Map<String, Map<Class< ? extends ISuperVO>, SuperVO[]>> cmnQueryChildrenByParent(Map<BillLazilyLoaderVO, List<Class< ? extends ISuperVO>>> map)
            throws BusinessException;

    /**
     * ͨ����ѯ������ѯ��֧����������������
     * 
     * @param clazz
     * @param queryScheme
     *        IQueryScheme
     * @param billTypeCode
     *        ��������
     * @return ��ѯ���
     * @throws BusinessException
     *         �쳣
     */
    Object[] cmnQueryDatasByScheme(Class< ? extends AbstractBill> clazz,
                                   IQueryScheme queryScheme,
                                   String billTypeCode) throws BusinessException;

    /**
     * ��������PK��ѯ���ݣ�����ʵ�ʵ�VO���飬��parentPks˳���Ӧ���鲻������null����ָ����Ҫ���ص��ӱ�
     * 
     * @param clazz  AggVO��Ӧ��class
     * @param parentPks
     * @param subEntityName
     *        ��Ҫ���ص��ӱ���nullʱ�������ӱ������������ӱ�
     * @return
     * @throws BusinessException
     * @author zhangyyp
     */
    Object[] cmnQuerySpecChildrenByParentPK(Class< ? > clazz,
                                            String[] parentPks,
                                            String[] subEntityName) throws BusinessException;

    /**
     * �����ص��õĸ��ݸ����ѯָ������ķ���
     * 
     * @param map
     * @param subEntityName
     *        ָ��Ҫ��ѯ���ӱ�����
     * @return
     * @throws BusinessException
     * @author zhangyyp
     */
    Map<String, Map<Class< ? extends ISuperVO>, SuperVO[]>> cmnQuerySpecChildrenByParent(Map<BillLazilyLoaderVO, List<Class< ? extends ISuperVO>>> map,
                                                                                     String[] subEntityName)
            throws BusinessException;

    /**
     * ����������ѯ���ݣ�������ֻ��ѯ��һ������ָ�����ӱ�
     * 
     * @param clazz
     * @param con
     * @param isLazyLoad
     * @param subEntityName
     *        ָ��Ҫ��ѯ���ӱ�����
     * @return
     * @throws BusinessException
     * @author zhangyyp
     */
    Object[] cmnQuerySpecDatasByConditionLazyLoad(Class< ? > clazz,
                                              String con,
                                              boolean isLazyLoad,
                                              String[] subEntityName) throws BusinessException;

    /**
     * ͨ����ѯ������ѯ���ݣ�����ͨ����������������
     * bLazyLoad Ϊtrue  �������ӱ�����
     * @param clazz
     * @param con
     *        ��ѯ����
     * @param bLazyLoad
     *        �Ƿ�������
     * @return List<Object> ����vo
     * @throws BusinessException
     * @author zhangyyp
     */
    public Object[] cmnQueryDatasByConditionLazyLoad2(Class< ? > clazz,
                                                 String con,
                                                 Boolean bLazyLoad) throws BusinessException;
}
