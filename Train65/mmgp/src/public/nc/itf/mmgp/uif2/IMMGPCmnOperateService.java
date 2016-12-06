package nc.itf.mmgp.uif2;

import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;
import nc.vo.pub.BusinessException;

public interface IMMGPCmnOperateService {

    /***
     * 删除（物理删除）
     * 
     * @param data
     * @throws BusinessException
     */
    void cmnDeleteBillDataFromDB(Object data) throws BusinessException;

    void cmnDeleteBillData(Object data) throws BusinessException;

    <T> T cmnSaveBillData(T data) throws BusinessException;

    <T> T cmnDisableBillData(T data) throws BusinessException;

    <T> T cmnEnableBillData(T data) throws BusinessException;

    /***
     * @param data
     * @return
     * @throws BusinessException
     */
    <T> T cmnSaveBillData_RequiresNew(T data) throws BusinessException;

    /**
     * 插入数据
     * 
     * @param data
     *        数据
     * @return 插入后的数据
     * @throws BusinessException
     */
    <T> T cmnInsertBillData(T data) throws BusinessException;

    /**
     * 更新数据
     * 
     * @param data
     *        数据
     * @return 更新后的数据
     * @throws BusinessException
     */
    <T> T cmnUpdateBillData(T data) throws BusinessException;
    
    /**
     * 支持主子表的批量启用
     * @param datas
     * @return
     * @throws BusinessException
     */
    <T> MMGPValueObjWithErrLog cmnManageEnableBillData(T[] datas) throws BusinessException;
    /**
     * 支持主子表的批量停用
     * @param datas
     * @return
     * @throws BusinessException
     */
    <T> MMGPValueObjWithErrLog cmnManageDisableBillData(T[] datas) throws BusinessException;

}
