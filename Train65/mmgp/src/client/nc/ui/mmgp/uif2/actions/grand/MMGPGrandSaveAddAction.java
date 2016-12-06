package nc.ui.mmgp.uif2.actions.grand;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.SaveAction;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.AddAction;
import nc.ui.uif2.actions.SaveAddAction;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� May 21, 2013
 * @author wangweir
 */
@SuppressWarnings("serial")
public class MMGPGrandSaveAddAction extends SaveAddAction {

    private AddAction addAction;

    private SaveAction saveAction;

    /**
     * BOM�ۺϿ�Ƭ����
     */
    private CardGrandPanelComposite cardGrandPanel;

    public MMGPGrandSaveAddAction() {
        super();
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        this.getSaveAction().doAction(e);

        // ����ִ�гɹ����������
        this.getAddAction().doAction(e);
    }

    @Override
    protected boolean isActionEnable() {
        return this.getModel().getUiState() == UIState.ADD;
    }

    @Override
    public AddAction getAddAction() {
        return this.addAction;
    }

    @Override
    public void setAddAction(AddAction addAction) {
        this.addAction = addAction;
    }

    public SaveAction getSaveAction() {
        return this.saveAction;
    }

    public void setSaveAction(SaveAction saveAction) {
        this.saveAction = saveAction;
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

}
