package nc.ui.mmgp.uif2.actions;

import java.awt.Container;

import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;

/**
 * <b> 多页签主子表，打印模板nodekey 生成工厂 </b>
 * <p>
 * 根据当前界面选择的子表页签编码，动态选择不同nodekey对应的打印模板
 * </p>
 * 
 * @since: 创建日期:Jul 23, 2014
 * @author:liwsh
 */
public class MMGPPrintNodeKeyFactory {

    /**
     * 是否卡片界面
     */
    private boolean card;

    /**
     * 界面容器
     */
    private Container parent;

    /**
     * 获取打印模板nodekey
     * 
     * @param card
     *        是否卡片界面
     * @param parent
     *        界面容器
     * @return
     */
    public String getNodeKey() {

        String tabcode = "";

        if (this.isCard()) {
            BillForm editor = (BillForm) this.getParent();
            tabcode = editor.getBillCardPanel().getBodyTabbedPane().getSelectedTableCode();
        } else {
            BillListView listView = (BillListView) this.getParent();
            tabcode = listView.getBillListPanel().getBodyTabbedPane().getSelectedTableCode();
        }
        return tabcode;
    }

    public boolean isCard() {
        return card;
    }

    public void setCard(boolean card) {
        this.card = card;
    }

    public Container getParent() {
        return parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

}
