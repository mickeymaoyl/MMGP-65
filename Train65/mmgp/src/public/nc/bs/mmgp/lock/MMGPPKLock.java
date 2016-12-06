package nc.bs.mmgp.lock;

import java.util.List;

import nc.bs.mmgp.common.VoUtils;
import nc.bs.uap.lock.PKLock;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> PK锁封装 </b>
 * <p>
 * 调用UAP的锁，加锁失败抛异常
 * </p>
 * 创建日期:2011-2-26
 * 
 * @author wangweiu
 * @deprecated
 * @see nc.vo.util.BDPKLockUtil
 */
public class MMGPPKLock {

	// NCLangResOnserver.getInstance().getStrByID("40040503",
	// "UPP40040503-000000");
	public static void addDynamicLock(String lock) throws BusinessException {
		boolean success = PKLock.getInstance().addDynamicLock(lock);
		if (!success) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0049")/*
																			 * @res
																			 * "正在进行相关操作，请稍后再试！"
																			 */);
		}
	}

	public static void addBatchDynamicLock(List<String> pks)
			throws BusinessException {
		addBatchDynamicLock(pks.toArray(new String[0]));
	}

	public static void addBatchDynamicLock(CircularlyAccessibleValueObject[] vos)
			throws BusinessException {
		List<String> pks = VoUtils.getPrimaryFiledValues(vos);
		addBatchDynamicLock(pks);
	}

	public static void addBatchDynamicLock(String[] locks)
			throws BusinessException {
		boolean success = PKLock.getInstance().addBatchDynamicLock(locks);
		if (!success) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0049")/*
																			 * @res
																			 * "正在进行相关操作，请稍后再试！"
																			 */);
		}
	}

	private MMGPPKLock() {

	}

}