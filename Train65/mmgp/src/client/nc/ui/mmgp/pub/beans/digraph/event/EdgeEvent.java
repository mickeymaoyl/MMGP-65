/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph.event;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;

/**
 * <b> 节点连线的事件类 </b> <br>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public class EdgeEvent {

    private AbstractEdgeModel source = null;

    private GraphOperator operator = GraphOperator.Add;

    /**
     * 构造函数
     * 
     * @param src
     *        源
     * @param op
     *        操作类型
     */
    public EdgeEvent(AbstractEdgeModel src,
                     GraphOperator op) {
        source = src;
        operator = op;
    }

    /**
     * 返回事件来源
     * 
     * @return the source
     */
    public AbstractEdgeModel getSource() {
        return source;
    }

    /**
     * 返回操作（增加、删除）
     * 
     * @return the operator
     */
    public GraphOperator getOperator() {
        return operator;
    }
}
