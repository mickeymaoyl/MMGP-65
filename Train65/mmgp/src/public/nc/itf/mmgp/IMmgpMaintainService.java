/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.itf.mmgp;

import java.util.List;

import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
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
 * @deprecated 2012-11-23 �ᱻɾ������Ϊ @see nc.itf.mmgp.uif2.IMMGPCmnOperateService
 */
public interface IMmgpMaintainService {
	// public <H extends SuperVO,B extends SuperVO> AggregatedValueObject
	// saveMmgpAggVO(AggregatedValueObject vo,
	// Class<H> headerVO,
	// Class<B> bodyVO) throws BusinessException;
	//
	// public SuperVO saveMmgpVO(SuperVO vo) throws BusinessException;

	<T extends SuperVO> T[] saveMmgpVOs(T[] vos) throws BusinessException;
	
	/**
	 * 
	 * @param <T>
	 * @param vos
	 * @param isCheckByPK ����vo״̬�ķ�ʽ��true����ͨ�������Ƿ���pkֵ�� false����ͨ������vo��statusֵ
	 * @return
	 * @throws BusinessException
	 */
	<T extends SuperVO> T[] saveMmgpVOs(T[] vos, boolean isCheckByPK) throws BusinessException;

	// public void deleteMmgpVO(Class<SuperVO> clazz,
	// String pk) throws BusinessException;

	/**
	 * MM��ҵ����BD�ķ�����
	 * <P>
	 * zjy��copy from IUifService <br>
	 * ���浥��VO(AggreValueObject)���ݡ����أ�ArrayList:��0�����������������ӱ����� �������ڣ�(2003-5-23
	 * 7:49:37) ���ھ� 2003-05-23 01
	 * </P>
	 * 
	 * @param billVo
	 * @param userObj
	 * @return
	 * @throws UifException
	 */
	AggregatedValueObject mmsaveBD(AggregatedValueObject billVo, Object userObj)
			throws UifException;

	/**
	 * ���浥����
	 * 
	 * @param <T>
	 * @param billVo
	 * @param userObj
	 * @param checkVo
	 * @param t
	 * @return
	 * @throws UifException
	 */
	<T extends SuperVO> AggregatedValueObject mmsaveSingleBD(
			AggregatedValueObject billVo, Object userObj,
			AggregatedValueObject checkVo, T t) throws UifException;

	/**
	 * ɾ������VO(AggreValueObject)���ݡ�
	 * <p>
	 * wangweiu: copy from IUifService<br>
	 * ���أ�VOID �������ڣ�(2003-5-23 7:49:37) ���ھ� 2003-05-23 01
	 */
	AggregatedValueObject mmdeleteBD(AggregatedValueObject billVo,
			Object userObj) throws UifException;

	<T extends SuperVO> void mmdeleteBDByClause(Class<T> className,
			String wherestr) throws BusinessException;

	/**
	 * ��������ۺ�VO����vo��status
	 * 
	 * @param billVo
	 * @param billVoList
	 * @param userObj
	 * @throws UifException
	 */
	void mmsaveBDLists(AggregatedValueObject billVo,
			List<? extends AggregatedValueObject> billVoList, Object userObj)
			throws BusinessException;
	
	/****
	 * ������Ӿۺ�VO  ע�⣺�˱��淽����֧�ֶ��ӱ�ľۺ�vo
	 * @param billVoList
	 * @param lock  :�Ƿ�Ա�ͷ��������
	 * @param isHeadVOStatusCheck: ״̬�Ƿ��ɱ�ͷvo��status���ж�
	 *    �ǣ���ͷvo״̬
	 *    �񣺱�ͷ����
	 * @throws BusinessException
	 */
	public void saveAggregatedValueObjectList(
			List<? extends AggregatedValueObject> billVoList,boolean lock,
			boolean isHeadVOStatusCheck)
			throws BusinessException  ;
	
	
	/****  �˱��淽��֧�ֶ��ӱ�ľۺ�vo�ı���
	 * ������Ӿۺ�VO
	 * @param billVoList
	 * @param lock  :�Ƿ�Ա�ͷ��������
	 * @param isCheckByHeadVOStatus: ״̬�Ƿ��ɱ�ͷvo��status���ж�
	 *    �ǣ���ͷvo״̬
	 *    ��ͨ����ͷ�Ƿ�ӵ������
	 * @throws BusinessException
	 */
	public void saveExAggregatedValueObjectList(
			List<? extends AggregatedValueObject> billVoList,boolean lock,
			boolean isCheckByHeadVOStatus)
			throws BusinessException  ;
}
