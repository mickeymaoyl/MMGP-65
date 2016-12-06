package nc.impl.mmgp.uif2.rule;

import nc.bs.uif2.validation.Validator;
import nc.vo.util.BDUniqueRuleValidate;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * Ψһ��У��Rule
 * </p>
 * 
 * @since �������� May 14, 2013
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
