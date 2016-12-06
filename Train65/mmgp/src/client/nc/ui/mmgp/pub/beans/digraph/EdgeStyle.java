/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph;

import org.jgraph.graph.GraphConstants;

/**
 * <b> 边的显示样式 </b>
 * <p>
 * 边的显示样式
 * </p>
 * 创建日期:2011-2-17
 * 
 * @author wangweiu
 */
public class EdgeStyle {
    /**
     * 终点样式
     */
    private int lineEnd = GraphConstants.ARROW_CLASSIC;

    private boolean endFill = true;

    /**
     * @return the lineEnd
     */
    public int getLineEnd() {
        return lineEnd;
    }

    /**
     * @param lineEnd
     *        the lineEnd to set
     */
    public void setLineEnd(int lineEnd) {
        this.lineEnd = lineEnd;
    }

    /**
     * @return the endFill
     */
    public boolean isEndFill() {
        return endFill;
    }

    /**
     * @param endFill
     *        the endFill to set
     */
    public void setEndFill(boolean endFill) {
        this.endFill = endFill;
    }

}
