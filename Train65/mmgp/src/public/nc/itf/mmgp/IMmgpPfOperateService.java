package nc.itf.mmgp;

import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;

/**
 * ���ݲ��������ӿ�
 * @author zhumh
 */
public interface IMmgpPfOperateService {

	/**
	 * ��������
	 * 
	 * @param bills
	 * @return �����VO
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billInsert(T[] bills)
			throws BusinessException;

	/**
	 * �����޸�
	 * 
	 * @param clientFullVOs
	 *            ����ȫVO
	 * @param originBills
	 *            ԭʼVO
	 * @return �޸ĺ�VO
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billUpdate(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * ����ɾ��
	 * 
	 * @param bills
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> void billDelete(T[] bills)
			throws BusinessException;

	/**
	 * �����ύ(����)
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billSendApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * �����ջ�
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billUnSendApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * ��������
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * ��������
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billUnApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

}
