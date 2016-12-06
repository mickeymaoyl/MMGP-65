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
 * <b> 带有确定、取消按钮的对话框父类 </b>
 * <p>
 *     详细描述功能
 * </p>
 * 创建日期:2011-6-20
 * @author Administrator
 */
@SuppressWarnings("serial")
public abstract class MMOKCancelDialog extends UIDialog implements ActionListener {
    /**
     *确认按钮
     */
    private UIButton okBtn = null;

    /**
     * 取消按钮
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
     * 获取位于center的panel，需要子类去实现
     * 
     * @return UIPanel 存放输入框的UI实例
     */
    protected abstract UIPanel createCenterPanel();

    /**
     * 获取位于south的panel
     * 
     * @return UIPanel 存放按钮的UI实例
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
     * 获取确定按钮
     * 
     * @return UIButton 确定按钮实例
     */
    protected UIButton getOKButton() {

        if (okBtn == null) {
            okBtn = new UIButton(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0111")/*确定*/);
            okBtn.addActionListener(this);
        }
        return okBtn;
    }

    /**
     * 获取取消按钮
     * 
     * @return UIButton 取消按钮实例
     */
    protected UIButton getCanelButton() {

        if (canelBtn == null) {
            canelBtn = new UIButton(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0112")/*取消*/);
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
