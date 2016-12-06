package nc.ui.mmgp.uif2.validation;

import nc.bs.uif2.validation.IPerformMode;
import nc.bs.uif2.validation.Validator;

/**
 * 
 * <b> ͨ�õ�validator </b>
 * <p>
 * Ĭ��ʵ����IPerformMode������false��������У�鲻ִ���ˡ�<br>
 * ����ҵ� @see nc.bs.uif2.validation.DefaultValidationService �� 
 * </p>
 * ��������:2012-11-23
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