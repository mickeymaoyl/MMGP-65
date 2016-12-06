/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph.event;

import java.util.HashMap;
import java.util.Map;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public class VertexEvent {

    /**
     * 参数主键-X坐标
     */
    public static final String KEY_MOVE_X = "KEY_MOVE_X";

    /**
     * 参数主键-Y坐标
     */
    public static final String KEY_MOVE_Y = "KEY_MOVE_Y";

    private AbstractVertexModel source = null;

    private GraphOperator operator = GraphOperator.Add;

    private Map<String, Object> parameters = new HashMap<String, Object>();

    /**
     * 构造函数
     * 
     * @param src
     *        源
     * @param op
     *        操作类型
     */
    public VertexEvent(AbstractVertexModel src,
                       GraphOperator op) {
        source = src;
        operator = op;
    }

    /**
     * 设置参数值
     * 
     * @param key
     *        主键
     * @param value
     *        值
     */
    public void setParameter(String key,
                             Object value) {
        parameters.put(key, value);
    }

    /**
     * 取得参数值
     * 
     * @param key
     *        主键
     * @return 参数值
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
