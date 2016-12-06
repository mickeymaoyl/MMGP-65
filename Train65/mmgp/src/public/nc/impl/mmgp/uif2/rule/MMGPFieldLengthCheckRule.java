package nc.impl.mmgp.uif2.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mmgp.util.MMGPVOFieldLengthChecker;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Oct 28, 2013
 * @author wangweir
 */
@SuppressWarnings("rawtypes")
public class MMGPFieldLengthCheckRule implements IRule {

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.IRule#process(E[])
     */
    @Override
    public void process(Object[] vos) {
        MMGPVOFieldLengthChecker.checkVOFieldsLength((IBill[]) vos);
    }

}
