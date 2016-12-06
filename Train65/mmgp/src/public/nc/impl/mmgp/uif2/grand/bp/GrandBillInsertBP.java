package nc.impl.mmgp.uif2.grand.bp;

import nc.bs.pubapp.pub.rule.CheckNotNullRule;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFieldLengthCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFillInsertDataRule;
import nc.impl.mmgp.uif2.template.MMGPInsertBPTemplate;
import nc.impl.mmgp.uif2.template.grand.MMGPGrandBillInsertOperator;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.pattern.rule.IFilterRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class GrandBillInsertBP<E extends IBill> {

    private MMGPInsertBPTemplate<E> template;

    /**
     * 是否使用内部预置规则，默认使用
     */
    private boolean enableInnerRule = true;

    /**
     * @param iPluginPoint
     */
    public GrandBillInsertBP(IPluginPoint iPluginPoint) {
        this.template = new MMGPInsertBPTemplate<E>(iPluginPoint, new MMGPGrandBillInsertOperator<E>());
    }

    /**
     * @param fullBills
     * @return
     */
    public E[] insert(E[] fullBills) {

        if (this.isEnableInnerRule()) {
            // 执行前规则
            this.addBeforeRuleInner();
            // 执行后规则
            this.addAfterRuleInner();
        }

        E[] returnvo = template.insert(fullBills);
        return returnvo;
    }

    /**
     * 
     */
    protected void addAfterRuleInner() {
        
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
     * 新增前规则
     * 
     * @param processor
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    protected void addBeforeRuleInner() {
        // 补充默认值规则
        IRule<IBill> rule = new MMGPFillInsertDataRule();
        this.addBeforeRule(rule);
        
        //表头为空校验规则
        this.addBeforeRule(new CheckNotNullRule());
        
        // 组织停用校验
        rule = new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null);
        this.addBeforeRule(rule);

        // 长度检查
        rule = new MMGPFieldLengthCheckRule();
        this.addBeforeRule(rule);

        // 自定义项检查
        // String[] prefixs = new String[]{"hvdef", "vdef", "vdef", "vdef", "vdef" };
        // Class< ? >[] voClasses =
        // new Class< ? >[]{
        // nc.vo.bd.bom.bom0202.entity.BomVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomItemVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomActivityVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomWipVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomPosVO.class };
        // rule = new nc.impl.pubapp.bd.userdef.UserDefSaveRule<AggBomVO>(prefixs, voClasses);
        // aroundProcesser.addBeforeRule(rule);

        // 检查物料自由项
        rule = new MarAssistantSaveRule();
        this.addBeforeRule(rule);
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
