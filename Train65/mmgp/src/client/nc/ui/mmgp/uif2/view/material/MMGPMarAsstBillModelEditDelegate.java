package nc.ui.mmgp.uif2.view.material;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.view.material.assistant.MarAsstBillModelEditDelegate;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Aug 19, 2013
 * @author wangweir
 */
public class MMGPMarAsstBillModelEditDelegate extends MarAsstBillModelEditDelegate {

    /**
     * �ֶ�λ�ã�Ĭ��Ϊ����
     */
    private int pos = 1;

    /*
     * (non-Javadoc)
     * @see
     * nc.ui.pubapp.uif2app.view.material.assistant.MarAsstBillModelEditDelegate#setEditable(nc.ui.pub.bill.BillCardPanel
     * )
     */
    @Override
    public void setEditable(BillCardPanel billCardPanel) {
        if (this.getPos() == 1) {
            return;
        }
        super.setEditable(billCardPanel);
    }

    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }

    /**
     * @param pos
     *        the pos to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }
    
    /**
     * �жϸ����ֶ��Ƿ������ϸ��������ֶ�(�������ɸ������Ժ͹̶���������)
     * 
     * @param field
     *          ��Ҫ�жϵ��ֶ�����
     * @return ����Ǹ��ϸ��������ֶη���true�����򷵻�false
     */
    protected boolean isAsstField(String field) {
      return super.isAsstField(field);
    }
}
