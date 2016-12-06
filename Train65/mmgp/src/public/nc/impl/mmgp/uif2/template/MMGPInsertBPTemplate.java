package nc.impl.mmgp.uif2.template;

import nc.impl.pubapp.pattern.data.bill.template.BillInsertOperator;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CommonOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 扩充InsertBPTemplate,支持传入具体的IOperator
 * </p>
 * 
 * @see InsertBPTemplate
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPInsertBPTemplate<E extends IBill> {

    /**
     * 单据新增规则处理器
     */
    private CommonOperatorTemplate<E> template;

    /**
     * 
     */
    public MMGPInsertBPTemplate(IPluginPoint point,
                                IOperator<E> operator) {
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * 单据新增BP的模板类构造函数
     * 
     * @param point
     *        单据新增BP插入点
     * @return
     */
    public MMGPInsertBPTemplate(IPluginPoint point) {
        IOperator<E> operator = new BillInsertOperator<E>();
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * 新增单据，会执行相关新增前、新增后规则
     * 
     * @param bills
     *        要新增的单据VO
     * @return 插入到数据库后的VO
     */
    public E[] insert(E[] bills) {
        return this.template.operate(bills);
    }

    /**
     * 获取单据新增规则处理器。调用者可以据此增加前、后规则
     * 
     * @return 单据新增规则处理器
     */
    public AroundProcesser<E> getAroundProcesser() {
        return this.template.getAroundProcesser();
    }
}
