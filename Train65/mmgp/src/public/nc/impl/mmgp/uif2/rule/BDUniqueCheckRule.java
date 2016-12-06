package nc.impl.mmgp.uif2.rule;

import nc.bs.uif2.validation.Validator;
import nc.vo.util.BDUniqueRuleValidate;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 唯一性校验Rule
 * </p>
 * 
 * @since 创建日期 May 14, 2013
 * @author wangweir
 */
public class BDUniqueCheckRule extends AbstractValidatorCheckRule {

    /*
     * (non-Javadoc)
     * @see nc.impl.mmgp.uif2.rule.AbstractValidatorCheckRule#getValidator()
     */
    @Override
    protected Validator[] getValidators() {
        return new Validator[]{new BDUniqueRuleValidate() };
    }
}
