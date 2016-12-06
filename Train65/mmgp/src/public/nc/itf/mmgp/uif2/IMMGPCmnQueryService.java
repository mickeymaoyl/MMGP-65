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
     * 通过查询方案查询，支持审批流特殊需求
     * 
     * @param clazz
     * @param queryScheme
     *        IQueryScheme
     * @param billTypeCode
     *        单据类型
     * @return 查询结果
     * @throws BusinessException
     *         异常
     */
    Object[] cmnQueryDatasByScheme(Class< ? extends AbstractBill> clazz,
                                   IQueryScheme queryScheme,
                                   String billTypeCode) throws BusinessException;

    /**
     * 根据批量PK查询单据，返回实际的VO数组，与parentPks顺序对应，查不到返回null，可指定需要加载的子表
     * 
     * @param clazz  AggVO对应的class
     * @param parentPks
     * @param subEntityName
     *        需要加载的子表，传null时不过滤子表，即加载所有子表
     * @return
     * @throws BusinessException
     * @author zhangyyp
     */
    Object[] cmnQuerySpecChildrenByParentPK(Class< ? > clazz,
                                            String[] parentPks,
                                            String[] subEntityName) throws BusinessException;

    /**
     * 懒加载调用的根据父项查询指定子项的方法
     * 
     * @param map
     * @param subEntityName
     *        指定要查询的子表名称
     * @return
     * @throws BusinessException
     * @author zhangyyp
     */
    Map<String, Map<Class< ? extends ISuperVO>, SuperVO[]>> cmnQuerySpecChildrenByParent(Map<BillLazilyLoaderVO, List<Class< ? extends ISuperVO>>> map,
                                                                                     String[] subEntityName)
            throws BusinessException;

    /**
     * 根据条件查询数据，懒加载只查询第一行数据指定的子表
     * 
     * @param clazz
     * @param con
     * @param isLazyLoad
     * @param subEntityName
     *        指定要查询的子表名称
     * @return
     * @throws BusinessException
     * @author zhangyyp
     */
    Object[] cmnQuerySpecDatasByConditionLazyLoad(Class< ? > clazz,
                                              String con,
                                              boolean isLazyLoad,
                                              String[] subEntityName) throws BusinessException;

    /**
     * 通过查询条件查询数据，可以通过参数控制懒加载
     * bLazyLoad 为true  不加载子表数据
     * @param clazz
     * @param con
     *        查询条件
     * @param bLazyLoad
     *        是否懒加载
     * @return List<Object> 返回vo
     * @throws BusinessException
     * @author zhangyyp
     */
    public Object[] cmnQueryDatasByConditionLazyLoad2(Class< ? > clazz,
                                                 String con,
                                                 Boolean bLazyLoad) throws BusinessException;
}
