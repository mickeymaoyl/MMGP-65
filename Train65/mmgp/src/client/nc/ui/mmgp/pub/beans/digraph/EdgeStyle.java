/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph;

import org.jgraph.graph.GraphConstants;

/**
 * <b> �ߵ���ʾ��ʽ </b>
 * <p>
 * �ߵ���ʾ��ʽ
 * </p>
 * ��������:2011-2-17
 * 
 * @author wangweiu
 */
public class EdgeStyle {
    /**
     * �յ���ʽ
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
