
package nc.ui.mmgp.pub.beans.digraph.event;

/**
 * <b> 有向图连线事件处理类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public interface IEdgeListener {
    /**
     * 有向图连线事件处理
     * @param e
     */
    void onEdgeChange(EdgeEvent e);
}
