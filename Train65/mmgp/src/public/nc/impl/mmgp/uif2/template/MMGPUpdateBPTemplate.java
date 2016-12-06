package nc.impl.mmgp.uif2.template;

import nc.impl.pubapp.pattern.data.bill.template.BillUpdateOperator;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CompareOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.ICompareOperator;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 单据修改BP的模板类,支持传入具体的ICompareOperator
 * </p>
 * 
 * @see UpdateBPTemplate
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPUpdateBPTemplate<E extends IBill> {
    /**
     * 单据修改规则处理器
     */
    private CompareOperatorTemplate<E> template;

    public MMGPUpdateBPTemplate(IPluginPoint point,
                                ICompareOperator<E> operator) {
        this.template = new CompareOperatorTemplate<E>(point, operator);
    }

    /**
     * 单据修改BP的模板类构造函数
     * 
     * @param point
     *        单据修改BP插入点
     */
    public MMGPUpdateBPTemplate(IPluginPoint point) {
        ICompareOperator<E> operator = new BillUpdateOperator<E>();
        this.template = new CompareOperatorTemplate<E>(point, operator);
    }

    /**
     * 获取单据修改规则处理器。调用者可以据此增加前、后规则
     * 
     * @return 单据修改规则处理器
     */
    public CompareAroundProcesser<E> getAroundProcesser() {
        return this.template.getAroundProcesser();
    }

    /**
     * 修改单据，会执行相关修改前、修改后规则 将新旧单据进行对比后，将改变值保存到数据库中
     * 
     * @param bills
     *        要修改的单据VO
     * @param originBills
     *        修改前的单据VO
     * @return 保存到数据库后的单据VO
     */
    public E[] update(E[] bills,
                      E[] originBills) {
        return this.template.operate(bills, originBills);
    }
}
