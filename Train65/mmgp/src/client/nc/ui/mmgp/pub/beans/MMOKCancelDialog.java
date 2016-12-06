package nc.ui.mmgp.pub.beans;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;

/**
 * 
 * <b> ����ȷ����ȡ����ť�ĶԻ����� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * ��������:2011-6-20
 * @author Administrator
 */
@SuppressWarnings("serial")
public abstract class MMOKCancelDialog extends UIDialog implements ActionListener {
    /**
     *ȷ�ϰ�ť
     */
    private UIButton okBtn = null;

    /**
     * ȡ����ť
     */
    private UIButton canelBtn = null;

    private UIPanel centerPanel;

    public MMOKCancelDialog(Container parent) {
        super(parent);

        this.setLayout(new BorderLayout());
        setName("MMOKCancelDialog");

        this.add(createSouthPanel(), BorderLayout.SOUTH);
        centerPanel = createCenterPanel();
        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * ��ȡλ��center��panel����Ҫ����ȥʵ��
     * 
     * @return UIPanel ���������UIʵ��
     */
    protected abstract UIPanel createCenterPanel();

    /**
     * ��ȡλ��south��panel
     * 
     * @return UIPanel ��Ű�ť��UIʵ��
     */
    protected UIPanel createSouthPanel() {
        UIPanel southPanel = new UIPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(getOKButton());
        southPanel.add(getCanelButton());
        return southPanel;
    }

    protected Component getComponentByName(String name) {
        for (Component c : centerPanel.getComponents()) {
            if (name.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    /**
     * ��ȡȷ����ť
     * 
     * @return UIButton ȷ����ťʵ��
     */
    protected UIButton getOKButton() {

        if (okBtn == null) {
            okBtn = new UIButton(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0111")/*ȷ��*/);
            okBtn.addActionListener(this);
        }
        return okBtn;
    }

    /**
     * ��ȡȡ����ť
     * 
     * @return UIButton ȡ����ťʵ��
     */
    protected UIButton getCanelButton() {

        if (canelBtn == null) {
            canelBtn = new UIButton(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0112")/*ȡ��*/);
            canelBtn.addActionListener(this);
        }
        return canelBtn;
    }

    public void actionPerformed(ActionEvent e) {
        setResult(UIDialog.ID_CANCEL);
        if (e.getSource() == getOKButton()) {
            if (!beforeOK()) {
                return;
            }
            setResult(UIDialog.ID_OK);
        }
        this.close();

    }

    protected boolean beforeOK() {
        return true;
    }
}
