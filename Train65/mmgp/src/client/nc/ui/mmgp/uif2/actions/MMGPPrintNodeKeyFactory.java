package nc.ui.mmgp.uif2.actions;

import java.awt.Container;

import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;

/**
 * <b> ��ҳǩ���ӱ���ӡģ��nodekey ���ɹ��� </b>
 * <p>
 * ���ݵ�ǰ����ѡ����ӱ�ҳǩ���룬��̬ѡ��ͬnodekey��Ӧ�Ĵ�ӡģ��
 * </p>
 * 
 * @since: ��������:Jul 23, 2014
 * @author:liwsh
 */
public class MMGPPrintNodeKeyFactory {

    /**
     * �Ƿ�Ƭ����
     */
    private boolean card;

    /**
     * ��������
     */
    private Container parent;

    /**
     * ��ȡ��ӡģ��nodekey
     * 
     * @param card
     *        �Ƿ�Ƭ����
     * @param parent
     *        ��������
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
