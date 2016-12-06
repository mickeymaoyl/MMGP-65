package nc.impl.mmgp.uif2.grand.bp;

import nc.impl.mmgp.uif2.template.MMGPDeleteBPTemplate;
import nc.impl.mmgp.uif2.template.grand.MMGPGrandBillDeleteOperator;
import nc.impl.pubapp.pattern.rule.IFilterRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� May 21, 2013
 * @author wangweir
 */
public class GrandBillDeleteBP {

    private MMGPDeleteBPTemplate<IBill> template;

    /**
     * �Ƿ�ʹ���ڲ�Ԥ�ù���Ĭ��ʹ��
     */
    private boolean enableInnerRule = true;

    /**
     * 
     */
    public GrandBillDeleteBP(IPluginPoint iPluginPoint) {
        this.template = new MMGPDeleteBPTemplate<IBill>(iPluginPoint, new MMGPGrandBillDeleteOperator<IBill>());
    }

    /**
     * @param fullBills
     */
    public void delete(IBill[] fullBills) {
        if (this.isEnableInnerRule()) {
            // ����ִ��ǰ����
            this.addBeforeRuleInner();
            // ����ִ�к�ҵ�����
            this.addAfterRuleInner();
        }
        this.template.delete(fullBills);
    }

    /**
     * 
     */
    protected void addAfterRuleInner() {

    }

    /**
     * 
     */
    protected void addBeforeRuleInner() {

    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    public void addBeforeRule(Object... rules) {
        if (rules == null) {
            return;
        }

        for (Object rule : rules) {
            if (rule == null) {
                continue;
            }

            if (rule instanceof IRule) {
                this.template.getAroundProcesser().addBeforeRule((IRule) rule);
            } else if (rule instanceof IFilterRule) {
                this.template.getAroundProcesser().addBeforeRule((IFilterRule) rule);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    public void addAfterRule(Object... rules) {
        if (rules == null) {
            return;
        }

        for (Object rule : rules) {
            if (rule == null) {
                continue;
            }

            if (rule instanceof IRule) {
                this.template.getAroundProcesser().addAfterRule((IRule) rule);
            } else if (rule instanceof IFilterRule) {
                this.template.getAroundProcesser().addAfterRule((IFilterRule) rule);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    public void addBeforeFinalRule(Object... rules) {
        if (rules == null) {
            return;
        }

        for (Object rule : rules) {
            if (rule == null) {
                continue;
            }

            if (rule instanceof IRule) {
                this.template.getAroundProcesser().addBeforeFinalRule((IRule) rule);
            } else if (rule instanceof IFilterRule) {
                this.template.getAroundProcesser().addBeforeFinalRule((IFilterRule) rule);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    public void addAfterFinalRule(Object... rules) {
        if (rules == null) {
            return;
        }

        for (Object rule : rules) {
            if (rule == null) {
                continue;
            }

            if (rule instanceof IRule) {
                this.template.getAroundProcesser().addAfterFinalRule((IRule) rule);
            } else if (rule instanceof IFilterRule) {
                this.template.getAroundProcesser().addAfterFinalRule((IFilterRule) rule);
            }
        }
    }

    /**
     * @return the enableInnerRule
     */
    public boolean isEnableInnerRule() {
        return enableInnerRule;
    }

    /**
     * @param enableInnerRule
     *        the enableInnerRule to set
     */
    public void setEnableInnerRule(boolean enableInnerRule) {
        this.enableInnerRule = enableInnerRule;
    }

}
