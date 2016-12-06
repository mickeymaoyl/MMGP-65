/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * <b> �ڵ���ʽ���� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-2-17
 * 
 * @author wangweiu
 */
public class VertexStyle {
    private Color background = Color.BLUE;

    private boolean opaque = true;

    private boolean autoResize = false;

    private Border border = BorderFactory.createRaisedBevelBorder();

    /**
     * ȡ�ñ���ɫ
     * 
     * @return ����ɫ
     */
    public Color getBackground() {
        return background;
    }

    /**
     * ���ñ���ɫ
     * 
     * @param background
     *        the background to set
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     * ȡ��opaque
     * 
     * @return the opaque
     */
    public boolean isOpaque() {
        return opaque;
    }

    /**
     * ����opaque
     * 
     * @param opaque
     *        the opaque to set
     */
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    /**
     * ȡ�ñ߿�
     * 
     * @return the �߿�
     */
    public Border getBorder() {
        return border;
    }

    /**
     * ���ñ߿�
     * 
     * @param border
     *        the border to set
     */
    public void setBorder(Border border) {
        this.border = border;
    }

    /**
     * �Ƿ�����Ӧ��С
     * 
     * @return
     */
    public boolean isAutoResize() {
        return autoResize;
    }

    /**
     * �����Ƿ�����Ӧ��С
     * 
     * @param autoResize
     */
    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

}
