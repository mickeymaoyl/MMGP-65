package nc.impl.mmgp.uif2.template;

import nc.impl.pubapp.pattern.data.bill.template.BillDeleteOperator;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CommonOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ������ɾ��BP��ģ����,֧�ִ�������IOperator
 * </p>
 * 
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPDeleteBPTemplate<E extends IBill> {
    /**
     * ҵ����򵥹���ģ����
     */
    private CommonOperatorTemplate<E> template;
    
    
    /**
     * 
     */
    public MMGPDeleteBPTemplate(IPluginPoint point, IOperator<E> operator) {
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * ������ɾ��BP��ģ���๹�캯��
     * 
     * @param point
     *        ������ɾ��BP�����
     */
    public MMGPDeleteBPTemplate(IPluginPoint point) {
        IOperator<E> operator = new BillDeleteOperator<E>();
        this.template = new CommonOperatorTemplate<E>(point, operator);
    }

    /**
     * ɾ�����ݣ���ִ�����ɾ��ǰ��ɾ�������
     * 
     * @param bills
     *        Ҫɾ���ĵ���VO
     */
    public void delete(E[] bills) {
        this.template.operate(bills);
    }

    /**
     * ��ȡ����ɾ�����������������߿��Ծݴ�����ǰ�������
     * 
     * @return ����ɾ����������
     */
    public AroundProcesser<E> getAroundProcesser() {
        return this.template.getAroundProcesser();
    }

}
