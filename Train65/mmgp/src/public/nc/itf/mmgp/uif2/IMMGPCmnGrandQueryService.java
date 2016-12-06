package nc.itf.mmgp.uif2;

import java.util.Map;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙查询服务接口
 * </p>
 * 
 * @since: 创建日期:May 7, 2013
 * @author:wangweir
 */
public interface IMMGPCmnGrandQueryService {

    /**
     * @param clazz
     * @param queryScheme
     * @param relationShip
     * @return
     * @throws BusinessException
     */
    Object[] queryBySchemeForGrand(Class< ? extends AbstractBill> clazz,
                                   IQueryScheme queryScheme,
                                   Map<String, CircularlyAccessibleValueObject> relationShip)
            throws BusinessException;

    /**
     * 不是懒加载
     * 
     * @param clazz
     * @param queryScheme
     * @param relationShip
     * @return
     * @throws BusinessException
     */
    Object[] queryBySchemeForGrandWithoutLazyQuery(Class< ? extends AbstractBill> clazz,
                                                   IQueryScheme queryScheme,
                                                   Map<String, CircularlyAccessibleValueObject> relationShip)
            throws BusinessException;

    /**
     * 通过主键查询主子孙VO
     * @param clazz 
     * @param pks
     * @return
     * @throws BusinessException
     */
    Object[] queryByPkForGrandWithoutLazyQuery(Class< ? extends AbstractBill> clazz,
                                               String[] pks) throws BusinessException;
}
