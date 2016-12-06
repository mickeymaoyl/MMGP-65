/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.itf.mmgp;

import java.util.List;

import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-1-21
 * 
 * @author wangweiu
 * @deprecated 2012-11-23 会被删除，改为 @see nc.itf.mmgp.uif2.IMMGPCmnOperateService
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
	 * @param isCheckByPK 检验vo状态的方式，true代表通过检验是否有pk值， false代表通过检验vo的status值
	 * @return
	 * @throws BusinessException
	 */
	<T extends SuperVO> T[] saveMmgpVOs(T[] vos, boolean isCheckByPK) throws BusinessException;

	// public void deleteMmgpVO(Class<SuperVO> clazz,
	// String pk) throws BusinessException;

	/**
	 * MM行业保存BD的方法。
	 * <P>
	 * zjy：copy from IUifService <br>
	 * 保存单据VO(AggreValueObject)数据。返回：ArrayList:第0：主表主键，依次子表主键 创建日期：(2003-5-23
	 * 7:49:37) 樊冠军 2003-05-23 01
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
	 * 保存单表体
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
	 * 删除单据VO(AggreValueObject)数据。
	 * <p>
	 * wangweiu: copy from IUifService<br>
	 * 返回：VOID 创建日期：(2003-5-23 7:49:37) 樊冠军 2003-05-23 01
	 */
	AggregatedValueObject mmdeleteBD(AggregatedValueObject billVo,
			Object userObj) throws UifException;

	<T extends SuperVO> void mmdeleteBDByClause(Class<T> className,
			String wherestr) throws BusinessException;

	/**
	 * 批量处理聚合VO根据vo的status
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
	 * 批量添加聚合VO  注意：此保存方法不支持多子表的聚合vo
	 * @param billVoList
	 * @param lock  :是否对表头进行锁定
	 * @param isHeadVOStatusCheck: 状态是否由表头vo的status来判断
	 *    是：表头vo状态
	 *    否：表头主键
	 * @throws BusinessException
	 */
	public void saveAggregatedValueObjectList(
			List<? extends AggregatedValueObject> billVoList,boolean lock,
			boolean isHeadVOStatusCheck)
			throws BusinessException  ;
	
	
	/****  此保存方法支持多子表的聚合vo的保存
	 * 批量添加聚合VO
	 * @param billVoList
	 * @param lock  :是否对表头进行锁定
	 * @param isCheckByHeadVOStatus: 状态是否由表头vo的status来判断
	 *    是：表头vo状态
	 *    否：通过表头是否拥有主键
	 * @throws BusinessException
	 */
	public void saveExAggregatedValueObjectList(
			List<? extends AggregatedValueObject> billVoList,boolean lock,
			boolean isCheckByHeadVOStatus)
			throws BusinessException  ;
}
