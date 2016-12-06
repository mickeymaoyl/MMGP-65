package nc.ui.mmgp.pub.beans.digraph;

/**
 * <b> 前台使用的标识符生成器 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-2-28
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
