/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.itf.mmgp;

import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-1-21
 * 
 * @author wangweiu
 * @deprecated 2012-11-23 �ᱻɾ������Ϊ @see nc.itf.mmgp.uif2.IMMGPCmnQueryService
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
     * ȡ��������
     * @return
     */
    String getOID();
    // public SuperVO[] queryMmgpSupperVO(Class<SuperVO> clazz,
    // Map<String,Object> condition);

}
