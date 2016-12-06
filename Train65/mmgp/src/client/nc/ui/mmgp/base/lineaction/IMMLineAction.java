package nc.ui.mmgp.base.lineaction;

/**
 * 
 * <b> 表体右上的小按钮动作类 </b>
 * <p>
 * </p>
 * 创建日期:2011-5-11
 * @author wangweiu
 * @deprecated
 */
public interface IMMLineAction {
    /**
     * 按钮动作执行类
     * @param type 类型
     * @return 是否执行成功
     * @see nc.ui.pub.bill.action.BillTableLineAction
     */
    boolean doLineAction(int type);
}
