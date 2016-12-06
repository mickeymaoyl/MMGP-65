package nc.itf.mmgp;

import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;

/**
 * 单据操作公共接口
 * @author zhumh
 */
public interface IMmgpPfOperateService {

	/**
	 * 单据新增
	 * 
	 * @param bills
	 * @return 保存后VO
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billInsert(T[] bills)
			throws BusinessException;

	/**
	 * 单据修改
	 * 
	 * @param clientFullVOs
	 *            界面全VO
	 * @param originBills
	 *            原始VO
	 * @return 修改后VO
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billUpdate(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * 单据删除
	 * 
	 * @param bills
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> void billDelete(T[] bills)
			throws BusinessException;

	/**
	 * 单据提交(送审)
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billSendApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * 单据收回
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billUnSendApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * 单据审批
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

	/**
	 * 单据弃审
	 * 
	 * @param clientFullVOs
	 * @param originBills
	 * @return
	 * @throws BusinessException
	 */
	<T extends MMGPAbstractBill> T[] billUnApprove(T[] clientFullVOs,
			T[] originBills) throws BusinessException;

}
