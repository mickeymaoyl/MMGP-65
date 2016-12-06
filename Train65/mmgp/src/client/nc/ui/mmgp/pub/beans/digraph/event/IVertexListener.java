/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph.event;

/**
 * <b> 有向图节点事件处理类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public interface IVertexListener {
    /**
     * 有向图节点事件处理
     * 
     * @param e
     */
    void onVertexChange(VertexEvent e);
}
