/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.itf.mmgp;

import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-1-21
 * 
 * @author wangweiu
 * @deprecated 2012-11-23 会被删除，改为 @see nc.itf.mmgp.uif2.IMMGPCmnQueryService
 */
public interface IMmgpQueryService {
    // public AggregatedValueObject[] queryAllMmgpAggVO(Class<AggregatedValueObject> vo,
    // Class<SuperVO> headerVO,
    // Class<SuperVO> bodyVO)throws BusinessException;
    //
    // public AggregatedValueObject[] queryMmgpAggVO(Class<AggregatedValueObject> vo,
    // Class<SuperVO> headerVO,
    // Class<SuperVO> bodyVO,
    // String headerWhere)throws BusinessException;

    <T extends SuperVO> T[] queryMmgpAllVO(Class<T> clazz) throws BusinessException;

    <T extends SuperVO> T[] queryMmgpVO(Class<T> clazz,
                                               String where) throws BusinessException;

    public <T extends SuperVO> T[] queryMmgpVO(Class<T> clazz,
    		String where,SQLParameter params) throws BusinessException ;
    /**
     * 取得新主键
     * @return
     */
    String getOID();
    // public SuperVO[] queryMmgpSupperVO(Class<SuperVO> clazz,
    // Map<String,Object> condition);

}
