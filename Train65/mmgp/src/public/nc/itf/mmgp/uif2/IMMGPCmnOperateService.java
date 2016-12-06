package nc.itf.mmgp.uif2;

import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;
import nc.vo.pub.BusinessException;

public interface IMMGPCmnOperateService {

    /***
     * ɾ��������ɾ����
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
     * ��������
     * 
     * @param data
     *        ����
     * @return ����������
     * @throws BusinessException
     */
    <T> T cmnInsertBillData(T data) throws BusinessException;

    /**
     * ��������
     * 
     * @param data
     *        ����
     * @return ���º������
     * @throws BusinessException
     */
    <T> T cmnUpdateBillData(T data) throws BusinessException;
    
    /**
     * ֧�����ӱ����������
     * @param datas
     * @return
     * @throws BusinessException
     */
    <T> MMGPValueObjWithErrLog cmnManageEnableBillData(T[] datas) throws BusinessException;
    /**
     * ֧�����ӱ������ͣ��
     * @param datas
     * @return
     * @throws BusinessException
     */
    <T> MMGPValueObjWithErrLog cmnManageDisableBillData(T[] datas) throws BusinessException;

}
