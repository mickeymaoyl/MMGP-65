package nc.ui.mmgp.pub.beans.digraph;

/**
 * <b> ǰ̨ʹ�õı�ʶ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-2-28
 * 
 * @author wangweiu
 */
public class DigraphIdGenerator {
    private static long bogusIndex = 0;

    public static void clear() {
        bogusIndex = 0;
    }

    public static String nextId() {
        bogusIndex++;
        return bogusIndex + "";
    }
}
