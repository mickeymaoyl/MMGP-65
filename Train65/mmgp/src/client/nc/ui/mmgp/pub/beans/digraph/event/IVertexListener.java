/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.event;

/**
 * <b> ����ͼ�ڵ��¼������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
public interface IVertexListener {
    /**
     * ����ͼ�ڵ��¼�����
     * 
     * @param e
     */
    void onVertexChange(VertexEvent e);
}
