package nc.impl.mmgp.uif2.template;

import nc.impl.pubapp.pattern.data.bill.template.BillDeleteOperator;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CommonOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 单据行删除BP的模板类,支持传入具体的IOperator
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPDeleteBPTemplate<E extends IBill> {
    /**
     * 业务处理简单规则模板类
     */
    private CommonOperatorTemplate<E> template;
    
    
    /**
     * 
     */
    public MMGPDeleteBPTemplate(IPluginPoint point, IOperator<E> operator) {
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * 单据行删除BP的模板类构造函数
     * 
     * @param point
     *        单据行删除BP插入点
     */
    public MMGPDeleteBPTemplate(IPluginPoint point) {
        IOperator<E> operator = new BillDeleteOperator<E>();
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * 删除单据，会执行相关删除前、删除后规则
     * 
     * @param bills
     *        要删除的单据VO
     */
    public void delete(E[] bills) {
        this.template.operate(bills);
    }

    /**
     * 获取单据删除规则处理器。调用者可以据此增加前、后规则
     * 
     * @return 单据删除规则处理器
     */
    public AroundProcesser<E> getAroundProcesser() {
        return this.template.getAroundProcesser();
    }

}
