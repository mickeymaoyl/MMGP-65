/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * <b> 节点样式定义 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-2-17
 * 
 * @author wangweiu
 */
public class VertexStyle {
    private Color background = Color.BLUE;

    private boolean opaque = true;

    private boolean autoResize = false;

    private Border border = BorderFactory.createRaisedBevelBorder();

    /**
     * 取得背景色
     * 
     * @return 背景色
     */
    public Color getBackground() {
        return background;
    }

    /**
     * 设置背景色
     * 
     * @param background
     *        the background to set
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     * 取得opaque
     * 
     * @return the opaque
     */
    public boolean isOpaque() {
        return opaque;
    }

    /**
     * 设置opaque
     * 
     * @param opaque
     *        the opaque to set
     */
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    /**
     * 取得边框
     * 
     * @return the 边框
     */
    public Border getBorder() {
        return border;
    }

    /**
     * 设置边框
     * 
     * @param border
     *        the border to set
     */
    public void setBorder(Border border) {
        this.border = border;
    }

    /**
     * 是否自适应大小
     * 
     * @return
     */
    public boolean isAutoResize() {
        return autoResize;
    }

    /**
     * 设置是否自适应大小
     * 
     * @param autoResize
     */
    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

}
