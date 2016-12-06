package nc.impl.mmgp.uif2.rule;

import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationFrameworkUtil;
import nc.bs.uif2.validation.Validator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 15, 2013
 * @author wangweir
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractValidatorCheckRule implements IRule {

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.IRule#process(E[])
     */
    @Override
    public void process(Object[] vos) {
        Validator[] validators = this.getValidators();
        IValidationService validationService = ValidationFrameworkUtil.createValidationService(validators);
        try {
            validationService.validate(vos);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
    }

    /**
     * 
     */
    protected abstract Validator[] getValidators();

}
