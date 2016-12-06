/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.impl.mmgp;

import java.util.List;

import nc.bs.mmgp.common.CommonUtils;
import nc.bs.mmgp.dao.MMGPCmnDAO;
import nc.bs.uap.oid.OidGenerator;
import nc.itf.mmgp.IMmgpQueryService;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-1-21
 * 
 * @author wangweiu
 * @deprecated 由nc.impl.mmgp.uif2.MMGPCmnQueryService替换
 * @see nc.impl.mmgp.uif2.MMGPCmnQueryService
 */
public class MmgpQueryServiceImpl implements IMmgpQueryService {

    /**
     * 简要说明
     * 
     * @see nc.itf.mmgp.IMmgpQueryService#queryMmgpAllVO(java.lang.Class)
     */
    public <T extends SuperVO> T[] queryMmgpAllVO(Class<T> clazz) throws BusinessException {
        try {
            MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
            List<T> result = cmnDAO.retrieveAll(clazz);
            if (result == null || result.isEmpty()) {
                return null;
            }
            return CommonUtils.createArray(clazz, result);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 简要说明
     * 
     * @see nc.itf.mmgp.IMmgpQueryService#queryMmgpVO(java.lang.Class, java.lang.String)
     */
    public <T extends SuperVO> T[] queryMmgpVO(Class<T> clazz,
                                               String where) throws BusinessException {
        try {
            MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
            List<T> result = cmnDAO.retrieveByClause(clazz, where);
            if (result == null || result.isEmpty()) {
                return null;
            }
            return CommonUtils.createArray(clazz, result);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return null;
        }
    }

    public <T extends SuperVO> T[] queryMmgpVO(Class<T> clazz,
                                               String where,
                                               SQLParameter params) throws BusinessException {
        try {
            MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
            List<T> result = cmnDAO.retrieveByClause(clazz, where, params);
            if (result == null || result.isEmpty()) {
                return null;
            }
            return CommonUtils.createArray(clazz, result);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return null;
        }
    }

    /**
     * 简要说明
     * 
     * @see nc.itf.mmgp.IMmgpQueryService#getOID()
     */
    public String getOID() {
        return OidGenerator.getInstance().nextOid();
    }

    // /**
    // * 简要说明
    // * @see nc.itf.mmgp.IMmgpQueryService#queryAllMmgpAggVO(java.lang.Class, java.lang.Class, java.lang.Class)
    // */
    // public AggregatedValueObject[] queryAllMmgpAggVO(Class<AggregatedValueObject> vo,
    // Class< ? extends SuperVO> headerVO,
    // Class< ? extends SuperVO> bodyVO) {
    // return null;
    // }
    //
    // /**
    // * 简要说明
    // * @see nc.itf.mmgp.IMmgpQueryService#queryMmgpAggVO(java.lang.Class, java.lang.Class, java.lang.Class,
    // java.lang.String)
    // */
    // public AggregatedValueObject[] queryMmgpAggVO(Class<AggregatedValueObject> vo,
    // Class<SuperVO> headerVO,
    // Class<SuperVO> bodyVO,
    // return null;
    // }

}
