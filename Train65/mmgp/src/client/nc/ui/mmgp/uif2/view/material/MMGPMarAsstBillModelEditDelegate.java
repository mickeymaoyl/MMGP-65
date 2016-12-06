package nc.ui.mmgp.uif2.view.material;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.view.material.assistant.MarAsstBillModelEditDelegate;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Aug 19, 2013
 * @author wangweir
 */
public class MMGPMarAsstBillModelEditDelegate extends MarAsstBillModelEditDelegate {

    /**
     * 字段位置，默认为表体
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
     * 判断给定字段是否是物料辅助属性字段(包括自由辅助属性和固定辅助属性)
     * 
     * @param field
     *          需要判断的字段名称
     * @return 如果是辅料辅助属性字段返回true，否则返回false
     */
    protected boolean isAsstField(String field) {
      return super.isAsstField(field);
    }
}
