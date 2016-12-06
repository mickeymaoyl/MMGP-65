package nc.impl.mmgp.uif2.rule;

import java.util.List;
import java.util.Map;

import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.RowDataUniqueCheckUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 表体唯一性校验
 * </p>
 * 
 * @since 创建日期 Jul 2, 2013
 * @author wangweir
 */
public class BodyRowUniqueCheckRule implements IRule<AbstractBill> {

    private Map<String, List<String>> uniqueCfgs;

    public BodyRowUniqueCheckRule(Map<String, List<String>> uniqueCfg) {
        this.uniqueCfgs = uniqueCfg;
    }

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.IRule#process(E[])
     */
    @Override
    public void process(AbstractBill[] bills) {
        List<ValidationFailure> failures = RowDataUniqueCheckUtil.check(bills, this.uniqueCfgs);
        if (MMCollectionUtil.isEmpty(failures)) {
            return;
        }

        ExceptionUtils.wrappException(new ValidationException(failures));
    }

}
