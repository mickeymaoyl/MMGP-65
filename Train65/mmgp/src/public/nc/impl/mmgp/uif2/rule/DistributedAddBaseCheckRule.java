package nc.impl.mmgp.uif2.rule;

import nc.bs.uif2.validation.Validator;
import nc.vo.bd.pub.DistributedAddBaseValidator;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� May 14, 2013
 * @author wangweir
 */
public class DistributedAddBaseCheckRule extends AbstractValidatorCheckRule {

    /*
     * (non-Javadoc)
     * @see nc.impl.mmgp.uif2.rule.AbstractValidatorCheckRule#getValidator()
     */
    @Override
    protected Validator[] getValidators() {
        return new Validator[]{new DistributedAddBaseValidator()};
    }

}
