package nc.bs.mmgp.pf.action;

import nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.mmgp.uif2.MMGPAbstractBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 24, 2013
 * @author wangweir
 */
@SuppressWarnings("deprecation")
public abstract class N_MMGP_DELETE<T extends MMGPAbstractBill> extends N_MMGP_DETELE<T> {

    @SuppressWarnings("unchecked")
    @Override
    protected CompareAroundProcesser<T> getCompareAroundProcesserWithRules(Object userObj) {
        CompareAroundProcesser<T> innerProcesser = super.getCompareAroundProcesserWithRules(userObj);
        innerProcesser.addBeforeRule(new BillDeleteStatusCheckRule());
        return innerProcesser;
    }
}
