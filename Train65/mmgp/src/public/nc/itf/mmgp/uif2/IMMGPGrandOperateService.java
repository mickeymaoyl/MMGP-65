package nc.itf.mmgp.uif2;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙持久化接口
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public interface IMMGPGrandOperateService {

    /**
     * @param value
     * @return
     * @throws BusinessException
     */
    public IBill[] insert(IBill[] value) throws BusinessException;

    /**
     * @param value
     * @return
     * @throws BusinessException
     */
    public IBill[] update(IBill[] value) throws BusinessException;

    /**
     * @param value
     * @throws BusinessException
     */
    public void delete(IBill[] value) throws BusinessException;
}
