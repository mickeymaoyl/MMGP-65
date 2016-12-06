package nc.impl.mmgp.uif2.grand.bp;

import nc.bs.pubapp.pub.rule.CheckNotNullRule;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFieldLengthCheckRule;
import nc.impl.mmgp.uif2.rule.MMGPFillUpdateDataRule;
import nc.impl.mmgp.uif2.template.MMGPUpdateBPTemplate;
import nc.impl.mmgp.uif2.template.grand.MMGPGrandBillUpdateOperator;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IFilterRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 21, 2013
 * @author wangweir
 */
public class GrandBillUpdateBP {

    private MMGPUpdateBPTemplate<IBill> template;

    /**
     * 是否使用内部预置规则，默认使用
     */
    private boolean enableInnerRule = true;

    /**
     * 
     */
    public GrandBillUpdateBP(IPluginPoint iPluginPoint) {
        this.template = new MMGPUpdateBPTemplate<IBill>(iPluginPoint, new MMGPGrandBillUpdateOperator<IBill>());
    }

    /**
     * @param fullBills
     * @param originBills
     * @return
     */
    public IBill[] update(IBill[] fullBills,
                          IBill[] originBills) {
        for (IBill vo : fullBills) {
            if (vo.getParent().getStatus() != VOStatus.UPDATED) {
                vo.getParent().setStatus(VOStatus.UPDATED);
            }
        }

        if (this.isEnableInnerRule()) {
            // 执行前规则
            this.addBeforeRuleInner();
            // 执行后规则
            this.addAfterRuleInner();
        }

        return template.update(fullBills, originBills);
    }

    /**
     * 
     */
    protected void addAfterRuleInner() {

    }

    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    protected void addBeforeRuleInner() {

        // 组织停用校验
        IRule rule = new OrgDisabledCheckRule(MMGlobalConst.PK_ORG, null);
        this.addBeforeRule(rule);

        // 单据是否存在表体数据的规则
        this.addBeforeRule(new CheckNotNullRule());

        // 长度检查
        rule = new MMGPFieldLengthCheckRule();
        this.addBeforeRule(rule);

        // 设置审计信息
        rule = new MMGPFillUpdateDataRule();
        this.addBeforeRule(rule);

        // 物料检查自由项
        rule = new MarAssistantSaveRule();
        this.addBeforeRule(rule);

        // TODO 检查自定义项
        // String[] prefixs = new String[]{"hvdef", "vdef", "vdef", "vdef", "vdef" };
        // Class< ? >[] voClasses =
        // new Class< ? >[]{
        // nc.vo.bd.bom.bom0202.entity.BomVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomItemVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomActivityVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomWipVO.class,
        // nc.vo.bd.bom.bom0202.entity.BomPosVO.class };
        // rule = new nc.impl.pubapp.bd.userdef.UserDefSaveRule<AggBomVO>(prefixs, voClasses);
        // this.addBeforeRule(rule);

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
            } else if (rule instanceof ICompareRule) {
                this.template.getAroundProcesser().addBeforeRule((ICompareRule) rule);
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
            } else if (rule instanceof ICompareRule) {
                this.template.getAroundProcesser().addAfterRule((ICompareRule) rule);
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
            } else if (rule instanceof ICompareRule) {
                this.template.getAroundProcesser().addBeforeFinalRule((ICompareRule) rule);
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
            } else if (rule instanceof ICompareRule) {
                this.template.getAroundProcesser().addAfterFinalRule((ICompareRule) rule);
            }
        }
    }

}
