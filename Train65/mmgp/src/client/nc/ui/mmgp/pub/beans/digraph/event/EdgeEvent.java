/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.event;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;

/**
 * <b> �ڵ����ߵ��¼��� </b> <br>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
public class EdgeEvent {

    private AbstractEdgeModel source = null;

    private GraphOperator operator = GraphOperator.Add;

    /**
     * ���캯��
     * 
     * @param src
     *        Դ
     * @param op
     *        ��������
     */
    public EdgeEvent(AbstractEdgeModel src,
                     GraphOperator op) {
        source = src;
        operator = op;
    }

    /**
     * �����¼���Դ
     * 
     * @return the source
     */
    public AbstractEdgeModel getSource() {
        return source;
    }

    /**
     * ���ز��������ӡ�ɾ����
     * 
     * @return the operator
     */
    public GraphOperator getOperator() {
        return operator;
    }
}
