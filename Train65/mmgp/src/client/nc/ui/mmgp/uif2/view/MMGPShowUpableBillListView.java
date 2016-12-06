package nc.ui.mmgp.uif2.view;

import nc.bs.logging.Logger;
import nc.md.model.IBean;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemHyperlinkEvent;
import nc.ui.pub.bill.BillItemHyperlinkListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.list.ListBillItemHyperlinkEvent;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.util.ManageModeUtil;

public class MMGPShowUpableBillListView extends ShowUpableBillListView {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3175182422743650548L;

    @Override
    public void initUI() {
        super.initUI();

        String hyperLinkFiledName = null;
        String billNo = MMGPMetaUtils.getBillNoFieldName(getModel().getContext());
        if (MMStringUtil.isNotEmpty(billNo)) {
            hyperLinkFiledName = billNo;
        } else {
            String codeField = MMGPMetaUtils.getCodeFieldName(getModel().getContext());
            if (MMStringUtil.isNotEmpty(codeField)) {
                hyperLinkFiledName = codeField;
            }
        }

        if (MMStringUtil.isNotEmpty(hyperLinkFiledName)) {
            BillItem codeitem = getBillListPanel().getHeadItem(hyperLinkFiledName);
            if (codeitem != null) {
                codeitem.setListHyperlink(true);
                codeitem.addBillItemHyperlinkListener(new BillItemHyperlinkListener() {
                    @Override
                    public void hyperlink(BillItemHyperlinkEvent event) {
                        getModel().fireShowEditorEvent();
                        /* Jun 24, 2013 wangweir 发送ListBillItemHyperlinkEvent，支持主子孙切换。不影响主子表 Begin */
                        getModel()
                            .fireEvent(
                                new ListBillItemHyperlinkEvent(event, MMGPShowUpableBillListView.this
                                    .getBillListPanel()));
                        /* Jun 24, 2013 wangweir End */
                    }
                });
                // getBillListPanel().setListData(getBillListPanel().getBillListData());
            }
        }
    }

    @Override
    protected void synchronizeDataFromModel() {
        super.synchronizeDataFromModel();
        renderTable();
    }

    protected void renderTable() {
        try {
            BillManageModel manageModel = getModel();
            // manageModel.get
            IBean bean = MMGPMetaUtils.getIBean(getModel().getContext());
            BillModel model = getBillListPanel().getHeadBillModel();
            int colCount = model.getColumnCount();
            for (int i = 0; i < model.getRowCount(); i++) {
                Object vo = model.getBodyValueRowVO(i, bean.getFullClassName());
                if (!ManageModeUtil.manageable(
                    vo,
                    getModel().getContext(),
                    manageModel.getBusinessObjectAdapterFactory())) {
                    for (int j = 0; j < colCount; j++) {
                        model.setBackground(ManageModeUtil.NE_BG_COLOR, i, j);
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void showMeUp() {
        super.showMeUp();
    }

    //
    //
    // @Override
    // protected void setValueSetter() {
    // // 如果没有注射进来，则使用默认的Setter
    // if (getBillListPanelValueSetter() == null) {
    // if (billListPanel.getBillListData().isMeataDataTemplate()) {
    // setBillListPanelValueSetter(new MDBillListPanelValueSetter() {
    // @Override
    // public void setBodyData(BillListPanel listPanel,
    // Object selectedData) {
    //
    // String[] tabcodes = listPanel.getBillListData()
    // .getBodyTableCodes();
    // if (tabcodes == null || tabcodes.length == 0) {
    // return;
    // }
    // listPanel.getBillListData()
    // .setBodyValueObjectByMetaData(selectedData);
    // for (String tabcode : tabcodes) {
    // listPanel.getBillListData()
    // .getBodyBillModel(tabcode)
    // .execLoadFormula();
    // }
    //
    // }
    // });
    // } else {
    // setBillListPanelValueSetter(new VOBillListPanelValueSetter());
    // }
    // }
    // }

}
