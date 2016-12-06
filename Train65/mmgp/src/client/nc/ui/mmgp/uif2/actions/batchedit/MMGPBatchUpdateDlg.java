package nc.ui.mmgp.uif2.actions.batchedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.RefInitializeCondition;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.itemeditors.IBillItemEditor;
import nc.ui.pub.bill.itemeditors.IBillItemEditorWrapper;
import nc.ui.uif2.ExceptionHandlerWithDLG;
import nc.ui.uif2.IExceptionHandler;
import nc.ui.uif2.editor.TemplateContainer;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.bill.BillTempletVO;

import org.apache.commons.lang.StringUtils;

/**
 * <b> 批改对话框</b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since 创建日期 2013-6-26
 * @author tangxya
 */
public class MMGPBatchUpdateDlg extends UIDialog {

    /**
     *
     */
    private static final long serialVersionUID = 87191137470316465L;

    private UIButton cancelBtn;

    private UIButton batchUpdateBtn;

    private UIPanel attrSelectPanel;

    private UILabel attrSelectLabel;

    private UIComboBox attrSelectComboBox;

    private UIPanel cardPanel;

    private BillCardPanel billcardpanel;

    private TemplateContainer tContainer;

    private List<String> updatepkList;

    private MMGPBatchUpdateExcutor batchUpdateExcutor;

    private IExceptionHandler exHandler;

    private MMGPBatchUpdateContext batchcontext;

    private List<BillItem> billItems = new ArrayList<BillItem>();

    public MMGPBatchUpdateDlg(Container parent,
                          TemplateContainer tContainer,
                          MMGPBatchUpdateContext batchcontext) {
        super(parent);
        this.batchcontext = batchcontext;
        this.tContainer = tContainer;
        this.init();

    }

    private class BatchUpdateExcutorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == MMGPBatchUpdateDlg.this.getBatchUpdateBtn()) {
                MMGPBatchUpdateDlg.this.onBtnBatchUpdate();
            } else if (e.getSource() == MMGPBatchUpdateDlg.this.getCancelBtn()) {
                MMGPBatchUpdateDlg.this.onBtnCancel();
            }
        }

    }

    public void init() {
        this.setSize(400, 350);
        this.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000085")/*@res "批改"*/);

        this.batchUpdateExcutor = new MMGPBatchUpdateExcutor(this, batchcontext);
        this.exHandler = new ExceptionHandlerWithDLG(this);

        UIPanel content = new UIPanel(new BorderLayout());
        content.add(getAttrSelectPanel(), BorderLayout.NORTH);

        content.add(this.getCardPanel(), BorderLayout.CENTER);

        UIPanel buttonPanel = new UIPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        buttonPanel.add(this.getBatchUpdateBtn());
        buttonPanel.add(this.getCancelBtn());
        content.add(buttonPanel, BorderLayout.SOUTH);
        this.setContentPane(content);

        BatchUpdateExcutorListener excutor = new BatchUpdateExcutorListener();
        this.getBatchUpdateBtn().addActionListener(excutor);
        this.getCancelBtn().addActionListener(excutor);
    }

    /**
     * 加载卡片单据模板
     *
     * @return
     */
    protected BillCardPanel getbillCardPanel() {
        if (billcardpanel == null) {
            billcardpanel = new BillCardPanel();

            BillTempletVO template = null;
            if (tContainer == null) {
                template =
                        billcardpanel.getDefaultTemplet(
                            this.batchcontext.getFuncode(),
                            null,
                            this.batchcontext.getUserid(),
                            this.batchcontext.getPk_group(),
                            this.batchcontext.getNodekey(),
                            null);
            } else {
                template = tContainer.getTemplate(this.batchcontext.getNodekey(), null, null);
            }
            billcardpanel.setBillData(new BillData(template));
        }
        return billcardpanel;
    }

    public List<String> getUpdatepkList() {
        return updatepkList;
    }

    public void setUpdatepkList(List<String> updatepkList) {
        this.updatepkList = updatepkList;
    }

    public UIPanel getAttrSelectPanel() {
        if (this.attrSelectPanel == null) {
            this.attrSelectPanel = new UIPanel();
            this.attrSelectPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            this.attrSelectPanel.add(this.getAttrSelectLabel());
            this.attrSelectPanel.add(this.getAttrSelectComBox());
        }
        return this.attrSelectPanel;
    }

    private UIButton getBatchUpdateBtn() {
        if (this.batchUpdateBtn == null) {
            String msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000085")/*@res "批改"*/;
            this.batchUpdateBtn = new UIButton(msg);
            this.batchUpdateBtn.setToolTipText(msg);
        }
        return this.batchUpdateBtn;
    }

    private UIButton getCancelBtn() {
        if (this.cancelBtn == null) {
            String msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0009")/*@res "关闭"*/;
            this.cancelBtn = new UIButton(msg);
            this.cancelBtn.setToolTipText(msg);
        }
        return this.cancelBtn;
    }

    public UILabel getAttrSelectLabel() {
        if (this.attrSelectLabel == null) {
            this.attrSelectLabel = new UILabel();
            this.attrSelectLabel.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0010")/*@res "批改属性"*/);
        }
        return this.attrSelectLabel;
    }

    private UIComboBox getAttrSelectComBox() {
        if (this.attrSelectComboBox == null) {
            this.attrSelectComboBox = new UIComboBox();

            this.attrSelectComboBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String attrkey = (String) ((DefaultConstEnum) e.getItem()).getValue();
                        MMGPBatchUpdateDlg.this.changeAttrComboBox(attrkey);
                    }
                }
            });
            this.attrSelectComboBox.addItems(loadUpdateItems(batchcontext.getUpdatefields()));

        }
        return this.attrSelectComboBox;
    }

    protected DefaultConstEnum[] loadUpdateItems(String[] updateattrs) {

        this.billItems.clear();
        if (MMArrayUtil.isEmpty(updateattrs)) {
            return null;
        }
        List<DefaultConstEnum> list = new ArrayList<DefaultConstEnum>();
        BillItem[] items;
        if (batchcontext.getupdatePos() == IBillItem.BODY) {
            items = this.getbillCardPanel().getBodyItems();
        } else {
            items = this.getbillCardPanel().getHeadItems();
        }
        for (String attrkey : updateattrs) {
            for (BillItem billItem : items) {
                if (attrkey.equals(billItem.getKey())) {
                    billItem.setEnabled(true);
                    billItem.setEdit(true);
                    billItem.setShow(true);

                    billItems.add(billItem);
                    list.add(new DefaultConstEnum(billItem.getKey(), billItem.getName()));
                }
            }
        }
        return list.toArray(new DefaultConstEnum[0]);
    }

    private void changeAttrComboBox(String attrkey) {
        if (StringUtils.isEmpty(attrkey)) {
            this.billItems.clear();
            this.resetComponents(new BillItem[0]);
            return;
        }
        BillItem showItem = null;

        for (BillItem billItem : billItems) {
            if (attrkey.equals(billItem.getKey())) {
                billItem.setEnabled(true);
                billItem.setEdit(true);
                billItem.setShow(true);
                /* billItems.add(billItem); */
                showItem = billItem;
            }
        }
        this.resetComponents(new BillItem[]{showItem });
        afterInit(attrkey);
    }

    protected void afterInit(String attr) {
        setOrgRef();

    }

    /***
     * 参照组织过滤
     */
    protected void setOrgRef() {
        if (batchcontext.getPk_org() != null) {
            for (BillItem item : this.billItems) {
                if (item.getDataType() == IBillItem.UFREF) {
                    UIRefPane refpanel = (UIRefPane) item.getComponent();
                    if (item.getRefType().contains(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0004064")/*@res "部门"*/)||item.getRefType().contains(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0000129")/*@res "人员"*/)) {// 部门档案、人员档案不通过组织过滤，显示组织选择框
                        refpanel.setMultiCorpRef(true);
                        refpanel.getRefModel().setFilterRefNodeName(new String[]{nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0011")/*@res "业务单元"*/ });
                        RefInitializeCondition refInitCon = refpanel.getRefUIConfig().getRefFilterInitconds()[0];
                        refInitCon.setDefaultPk(batchcontext.getPk_org());
                        refpanel.getRefUIConfig().setRefFilterInitconds(new RefInitializeCondition[]{refInitCon });
                    } else {
                        refpanel.getRefModel().setPk_org(batchcontext.getPk_org());
                    }

                }
            }
        }
    }

    /**
     * 重新设置批改属性编辑控件 .
     *
     * @param showItems
     */
    private void resetComponents(BillItem[] showItems) {
        this.getCardPanel().removeAll();

        if (showItems != null && showItems.length > 0) {
            for (int i = 0; i < showItems.length; i++) {
                this.getCardPanel().add(showItems[i].getCaptionLabel());
                JComponent com = showItems[i].getComponent();
                IBillItemEditor editor = showItems[i].getItemEditor();
                if (editor instanceof IBillItemEditorWrapper) {
                    com = ((IBillItemEditorWrapper) editor).getWrapperComponent();
                } else {
                    com = editor.getComponent();
                }
                editor.setValue(null);
                this.getCardPanel().add(com);
            }
            // 布局管理
            this.getCardPanel().setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        }
        this.getCardPanel().validate();
        this.getCardPanel().repaint();
    }

    private UIPanel getCardPanel() {
        if (this.cardPanel == null) {
            this.cardPanel = new UIPanel();
        }
        return this.cardPanel;
    }

    private void onBtnBatchUpdate() {
        try {
            this.batchUpdateExcutor.doBatchUpdate(getValue());
        } catch (Exception e) {
            this.handleException(e);
        }

    }

    /**
     * 返回批改字段名+批改值
     *
     * @return
     */
    public Map<String, Object> getValue() {
        DefaultConstEnum item = (DefaultConstEnum) this.getAttrSelectComBox().getModel().getSelectedItem();
        String attr = (String) item.getValue();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(attr)) {
            if (batchcontext.getupdatePos() == IBillItem.BODY) {
                BillItem billitem = getbillCardPanel().getBodyItem(attr);
                map.put(attr, billitem.getValueObject());
            } else {
                BillItem billitem = getbillCardPanel().getHeadItem(attr);
                map.put(attr, billitem.getValueObject());
            }
        }
        return map;
    }

    private void onBtnCancel() {
        this.closeCancel();
    }

    private void handleException(Exception e) {
        Logger.error(e.getMessage(), e);
        this.exHandler.handlerExeption(e);
    }
}