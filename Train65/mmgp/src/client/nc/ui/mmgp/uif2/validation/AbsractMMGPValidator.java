package nc.ui.mmgp.uif2.validation;

import nc.bs.uif2.validation.IPerformMode;
import nc.bs.uif2.validation.Validator;

/**
 * 
 * <b> 通用的validator </b>
 * <p>
 * 默认实现了IPerformMode，返回false代表后面的校验不执行了。<br>
 * 必须挂到 @see nc.bs.uif2.validation.DefaultValidationService 中 
 * </p>
 * 创建日期:2012-11-23
 * 
 * @author wangweiu
 */
public abstract class AbsractMMGPValidator implements Validator, IPerformMode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9036702622845822578L;
	private boolean failedContinue = false;

	public boolean isFailedContinue() {
		return failedContinue;
	}

	public void setFailedContinue(boolean failedContinue) {
		this.failedContinue = failedContinue;
	}

	@Override
	public boolean getPerformModeContinue() {
		return failedContinue;
	}

}