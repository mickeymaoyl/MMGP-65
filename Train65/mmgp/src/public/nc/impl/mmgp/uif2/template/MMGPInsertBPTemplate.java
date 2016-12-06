package nc.impl.mmgp.uif2.template;

import nc.impl.pubapp.pattern.data.bill.template.BillInsertOperator;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CommonOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ����InsertBPTemplate,֧�ִ�������IOperator
 * </p>
 * 
 * @see InsertBPTemplate
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPInsertBPTemplate<E extends IBill> {

    /**
     * ����������������
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
     * ��������BP��ģ���๹�캯��
     * 
     * @param point
     *        ��������BP�����
     * @return
     */
    public MMGPInsertBPTemplate(IPluginPoint point) {
        IOperator<E> operator = new BillInsertOperator<E>();
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * �������ݣ���ִ���������ǰ�����������
     * 
     * @param bills
     *        Ҫ�����ĵ���VO
     * @return ���뵽���ݿ���VO
     */
    public E[] insert(E[] bills) {
        return this.template.operate(bills);
    }

    /**
     * ��ȡ�����������������������߿��Ծݴ�����ǰ�������
     * 
     * @return ����������������
     */
    public AroundProcesser<E> getAroundProcesser() {
        return this.template.getAroundProcesser();
    }
}
