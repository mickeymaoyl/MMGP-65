package nc.itf.mmgp.uif2;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public interface IMMGPCmnTreeService {
	<T extends SuperVO> void cmnDeleteTreeVO(T vo) throws BusinessException;

	<T extends SuperVO> T cmnInsertTreeVO(T vo) throws BusinessException;

	<T extends SuperVO> T cmnUpdateTreeVO(T vo) throws BusinessException;

	<T extends SuperVO> T cmnEnableTreeVO(T vo) throws BusinessException;

	<T extends SuperVO> T cmnDisableTreeVO(T vo) throws BusinessException;
}
