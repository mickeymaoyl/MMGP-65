package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.view.MMGPShowUpableBillListView;
import nc.ui.pubapp.uif2app.components.grand.mediator.GrandMouseClickShowPanelMediator;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���ǻ�ȡhyperlink�ķ�������nc.ui.mmgp.uif2.view.MMGPShowUpableBillListView�����¼��ķ���һ�¡�
 * </p>
 * 
 * @see  MMGPShowUpableBillListView
 * @since �������� Jun 24, 2013
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
