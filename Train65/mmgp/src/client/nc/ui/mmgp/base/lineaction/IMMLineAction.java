package nc.ui.mmgp.base.lineaction;

/**
 * 
 * <b> �������ϵ�С��ť������ </b>
 * <p>
 * </p>
 * ��������:2011-5-11
 * @author wangweiu
 * @deprecated
 */
public interface IMMLineAction {
    /**
     * ��ť����ִ����
     * @param type ����
     * @return �Ƿ�ִ�гɹ�
     * @see nc.ui.pub.bill.action.BillTableLineAction
     */
    boolean doLineAction(int type);
}
