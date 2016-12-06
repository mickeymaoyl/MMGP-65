/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.event;

import java.util.HashMap;
import java.util.Map;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
public class VertexEvent {

    /**
     * ��������-X����
     */
    public static final String KEY_MOVE_X = "KEY_MOVE_X";

    /**
     * ��������-Y����
     */
    public static final String KEY_MOVE_Y = "KEY_MOVE_Y";

    private AbstractVertexModel source = null;

    private GraphOperator operator = GraphOperator.Add;

    private Map<String, Object> parameters = new HashMap<String, Object>();

    /**
     * ���캯��
     * 
     * @param src
     *        Դ
     * @param op
     *        ��������
     */
    public VertexEvent(AbstractVertexModel src,
                       GraphOperator op) {
        source = src;
        operator = op;
    }

    /**
     * ���ò���ֵ
     * 
     * @param key
     *        ����
     * @param value
     *        ֵ
     */
    public void setParameter(String key,
                             Object value) {
        parameters.put(key, value);
    }

    /**
     * ȡ�ò���ֵ
     * 
     * @param key
     *        ����
     * @return ����ֵ
     */
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * @return the source
     */
    public AbstractVertexModel getSource() {
        return source;
    }

    /**
     * @return the operator
     */
    public GraphOperator getOperator() {
        return operator;
    }

}
