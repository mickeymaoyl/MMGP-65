package nc.ui.mmgp.uif2.actions.grand;

import java.awt.event.ActionEvent;

import nc.impl.mmgp.uif2.grand.util.GCClientBillCombinServer;
import nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction;
import nc.ui.pubapp.uif2app.actions.IDataOperationService;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.ui.uif2.UIState;
import nc.vo.mmgp.util.grand.MMGPGrandClientBillCombinServer;
import nc.vo.mmgp.util.grand.MMGPGrandClientBillToServer;
import nc.vo.mmgp.util.grand.MMGPGrandPseudoColUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
@SuppressWarnings("serial")
public class MMGPGrandSaveAction extends DifferentVOSaveAction {

    private CardGrandPanelComposite cardGrandPanel;

    private MainGrandModel mainGrandModel;

    @Override
    public void doAction(ActionEvent e) throws Exception {

        Object value = this.getCardGrandPanel().getValue();
        if (value == null) {
            return;
        }

        this.validate(value);

        IBill bill = (IBill) value;
        // TODO 如果支持复制新增，需要处理 wangweir
        // if (MMStringUtil.isNotEmpty(bill.getParent().getPrimaryKey()) && this.getModel().getUiState() == UIState.ADD)
        // {
        // String headPkField = bill.getParent().getMetaData().getPrimaryAttribute().getColumn().getName();
        // bill.getParent().setAttributeValue(headPkField, null);
        // bill.getParent().setStatus(VOStatus.NEW);
        // }

        if (this.getModel().getUiState() == UIState.ADD) {
            MMGPGrandPseudoColUtil.setPseudoColInfo(value);
            this.doAddSave(bill);
            this.getMainGrandModel().clearBufferData();
        } else if (this.getModel().getUiState() == UIState.EDIT) {
            MMGPGrandPseudoColUtil.setPseudoColInfo(value);
            this.doEditSave(bill);
            this.getMainGrandModel().clearBufferData();
        }

        this.showSuccessInfo();
    }

    /**
     * 方法功能描述：进行新增保存
     *
     * @param value
     *        Object型参数，要保存的数据
     * @throws Exception
     *         保存失败，则抛出异常
     */
    @Override
    protected void doAddSave(Object value) throws Exception {

        // 取界面vo数据
        IBill[] clientVOs = new IBill[]{(IBill) value };

        MMGPGrandClientBillToServer<IBill> tool = new MMGPGrandClientBillToServer<IBill>();

        // 取得差异VO，并把差异vo传到后台
        IBill[] lightVOs = tool.constructInsert(clientVOs);

        if (this.getService() == null) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0013")/*@res "service不能为空。"*/);/* -=notranslate=- */
        }
        IBill[] afterUpdateVOs = this.getService().insert(lightVOs);

        // clientVOs为界面上的数据，afterUpdateVOs为后台返回的差异数据，取全vo数据
        new MMGPGrandClientBillCombinServer<IBill>().combine(clientVOs, afterUpdateVOs);

        // TODO 如果是复制新增，并且原单据是默认的，还需要刷新一遍原单据. wangweir
        // if (MMValueCheck.isNotEmpty(this.cbomid) && MMValueCheck.isNotEmpty(this.isDefault)
        // && this.isDefault.booleanValue()) {
        // IBomBillQueryService queryService = NCLocator.getInstance().lookup(IBomBillQueryService.class);
        // AggBomVO[] originalvos = queryService.queryAggBomByBomID(new String[] {
        // this.cbomid
        // });
        // // 刷新Model
        // this.getModel().setUiState(UIState.NOT_EDIT);
        // // this.getMainGrandModel().directlyAdd(originalvos[0]);
        // if (MMArrayUtil.isNotEmpty(originalvos)) {
        // this.getMainGrandModel().directlyUpdate(originalvos[0]);
        //
        // }
        // }

        this.getModel().setUiState(UIState.NOT_EDIT);
        this.getMainGrandModel().directlyAdd(clientVOs[0]);
    }

    /**
     * 方法功能描述：进行修改保存
     *
     * @param value
     *        Object型参数，要保存的数据
     * @throws Exception
     *         保存失败，则抛出异常
     */
    @Override
    protected void doEditSave(Object value) throws Exception {
        // 取界面vo数据
        IBill[] clientVOs = new IBill[]{(IBill) value };

        IBill oldVO = (IBill) this.getMainGrandModel().getSelectedData();
        IBill[] oldVOs = new IBill[]{oldVO };

        // 取得轻量级VO
        MMGPGrandClientBillToServer<IBill> tool = new MMGPGrandClientBillToServer<IBill>();
        IBill[] lightVOs = tool.construct(oldVOs, clientVOs);

        if (this.getService() == null) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0013")/*@res "service不能为空。"*/);
        }
        IBill[] afterUpdateVOs = this.getService().update(lightVOs);

        // clientVOs为界面上的数据，afterUpdateVOs为后台返回的差异数据
        new GCClientBillCombinServer<IBill>().combine(clientVOs, afterUpdateVOs);

        this.getModel().setUiState(UIState.NOT_EDIT);
        this.getMainGrandModel().directlyUpdate(clientVOs[0]);
    }



    /**
     * @return the cardGrandPanel
     */
    public CardGrandPanelComposite getCardGrandPanel() {
        return cardGrandPanel;
    }

    /**
     * @param cardGrandPanel
     *        the cardGrandPanel to set
     */
    public void setCardGrandPanel(CardGrandPanelComposite cardGrandPanel) {
        this.cardGrandPanel = cardGrandPanel;
    }

    /**
     * @return the mainGrandModel
     */
    public MainGrandModel getMainGrandModel() {
        return mainGrandModel;
    }

    /**
     * @param mainGrandModel
     *        the mainGrandModel to set
     */
    public void setMainGrandModel(MainGrandModel mainGrandModel) {
        this.mainGrandModel = mainGrandModel;
    }

    /* (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction#getService()
     */
    @Override
    public IDataOperationService getService() {
        return super.getService();
    }

}