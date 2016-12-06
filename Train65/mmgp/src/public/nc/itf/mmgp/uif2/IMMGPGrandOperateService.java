package nc.itf.mmgp.uif2;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ������־û��ӿ�
 * </p>
 * 
 * @since �������� May 20, 2013
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
