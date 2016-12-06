package nc.vo.mmgp.batch;

import nc.bs.uif2.validation.ValidationFailure;

public class MMGPBatchValidationFailure extends ValidationFailure {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3793839241173669606L;
	private Object uesrObject ;

	public Object getUesrObject() {
		return uesrObject;
	}

	public void setUesrObject(Object uesrObject) {
		this.uesrObject = uesrObject;
	}

}
