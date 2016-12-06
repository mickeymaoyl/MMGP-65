package nc.bs.mmgp.billtree.persist;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.uif2.CodeRuleUtil;
/**
 * 
 * <b> �����ҵ��ݺ�̨�־û��ӿ� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since: 
 * ��������:Sep 15, 2014
 * @author:liwsh
 */
public interface IMMGPBillTreePersistenceUtil<T extends IBill> {
    
    public String getTableName() throws BusinessException;
    
    public Class<T> getEntityClass() throws BusinessException;

    public String getParentPkFieldName() throws BusinessException;

    public String[] insertVOWithInnerCode(T... vos) throws BusinessException;

    public void updateVOWithInnerCode(T vo, T oldVO) throws BusinessException;

    public void updateVOWithAttrs(String[] fields, T... vos)
            throws BusinessException;

    public void deleteVO(boolean deleteFromDB, T... vos ) throws BusinessException;

    public T[] retrieveVO(String[] pks) throws BusinessException;

    public T[] retrieveVOByFields(String[] pks, String[] fields)
            throws BusinessException;

    public T[] retrieveSubVOsWithSelf(String pk) throws BusinessException;

    public String retrieveParentPk(T vo, CodeRuleUtil codeRuleUtil)
            throws BusinessException;

    public void notifyVersionChangeWhenDataDeleted(T... vos)
            throws BusinessException;

    public void notifyVersionChangeWhenDataInserted(T... vos)
            throws BusinessException;

    public void notifyVersionChangeWhenDataUpdated(T... vos)
            throws BusinessException;

}
