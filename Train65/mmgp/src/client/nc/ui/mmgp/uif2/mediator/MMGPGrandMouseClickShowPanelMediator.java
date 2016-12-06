package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.view.MMGPShowUpableBillListView;
import nc.ui.pubapp.uif2app.components.grand.mediator.GrandMouseClickShowPanelMediator;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 覆盖获取hyperlink的方法，与nc.ui.mmgp.uif2.view.MMGPShowUpableBillListView发送事件的方法一致。
 * </p>
 * 
 * @see  MMGPShowUpableBillListView
 * @since 创建日期 Jun 24, 2013
 * @author wangweir
 */
public class MMGPGrandMouseClickShowPanelMediator extends GrandMouseClickShowPanelMediator {

    private BillManageModel model = null;

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.view.MouseClickShowPanelMediator#getHyperLinkColumn()
     */
    @Override
    public String getHyperLinkColumn() {
        if (MMStringUtil.isNotEmpty(super.getHyperLinkColumn())) {
            return super.getHyperLinkColumn();
        }

        if (this.getModel() == null) {
            return null;
        }
        
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
        return hyperLinkFiledName;
    }

    /**
     * @return the model
     */
    public BillManageModel getModel() {
        return model;
    }

    /**
     * @param model
     *        the model to set
     */
    public void setModel(BillManageModel model) {
        this.model = model;
    }

}
