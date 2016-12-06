package nc.impl.mmgp.uif2.template;

import nc.impl.pubapp.pattern.data.bill.template.BillUpdateOperator;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CompareOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.ICompareOperator;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * �����޸�BP��ģ����,֧�ִ�������ICompareOperator
 * </p>
 * 
 * @see UpdateBPTemplate
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPUpdateBPTemplate<E extends IBill> {
    /**
     * �����޸Ĺ�������
     */
    private CompareOperatorTemplate<E> template;

    public MMGPUpdateBPTemplate(IPluginPoint point,
                                ICompareOperator<E> operator) {
        this.template = new CompareOperatorTemplate<E>(point, operator);
    }

    /**
     * �����޸�BP��ģ���๹�캯��
     * 
     * @param point
     *        �����޸�BP�����
     */
    public MMGPUpdateBPTemplate(IPluginPoint point) {
        ICompareOperator<E> operator = new BillUpdateOperator<E>();
        this.template = new CompareOperatorTemplate<E>(point, operator);
    }

    /**
     * ��ȡ�����޸Ĺ��������������߿��Ծݴ�����ǰ�������
     * 
     * @return �����޸Ĺ�������
     */
    public CompareAroundProcesser<E> getAroundProcesser() {
        return this.template.getAroundProcesser();
    }

    /**
     * �޸ĵ��ݣ���ִ������޸�ǰ���޸ĺ���� ���¾ɵ��ݽ��жԱȺ󣬽��ı�ֵ���浽���ݿ���
     * 
     * @param bills
     *        Ҫ�޸ĵĵ���VO
     * @param originBills
     *        �޸�ǰ�ĵ���VO
     * @return ���浽���ݿ��ĵ���VO
     */
    public E[] update(E[] bills,
                      E[] originBills) {
        return this.template.operate(bills, originBills);
    }
}
