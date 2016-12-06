package nc.ui.mmgp.uif2.view.material;

import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardPanelLoadEvent;
import nc.ui.pubapp.uif2app.event.list.ListBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.list.ListBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.model.IAppModelEx;
import nc.ui.pubapp.uif2app.view.material.assistant.MarAsstBillModelEditDelegate;
import nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator;
import nc.vo.bd.userdefrule.UserdefitemVO;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Aug 17, 2013
 * @author wangweir
 */
public class MMGPMarAsstPreparator extends MarAsstPreparator {

    private int pos = 1;

    private IAppModelEx modelInMMGP;

    private MarAsstBillModelEditDelegate editDelegate = null;

    public void init() {
        this.addListener();
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
        ((MMGPMarAsstBillModelEditDelegate) this.getMarAsstEditDelegate()).setPos(pos);
    }

    public void setModel(IAppModelEx model) {
        this.setModelInMMGP(model);
    }

    @Override
    public void prepareBillData(BillData bd) {
        if (this.getContainer() != null) {
            UserdefitemVO[] userdefitemVOs =
                    this
                        .getContainer()
                        .getUserdefitemVOsByUserdefruleCode(MMGPMarAssistantUtils.MARASSISTANTRULECODE);
            MMGPMarAssistantUtils.updateMarAssistantBillItem(
                bd,
                this.getMarAsstEditDelegate().getPrefix(),
                userdefitemVOs);
        }
    }

    @Override
    public void prepareBillListData(BillListData bld) {
        if (this.getContainer() != null) {
            UserdefitemVO[] userdefitemVOs =
                    this
                        .getContainer()
                        .getUserdefitemVOsByUserdefruleCode(MMGPMarAssistantUtils.MARASSISTANTRULECODE);
            MMGPMarAssistantUtils.updateMarAssistantBillItem(
                bld,
                this.getMarAsstEditDelegate().getPrefix(),
                userdefitemVOs);
        }
    }

	protected void beforEdit(CardBodyBeforeEditEvent e) {
		if (this.modelInMMGP.getAppUiState() == AppUiState.NOT_EDIT
				&& ((MMGPMarAsstBillModelEditDelegate) this
						.getMarAsstEditDelegate()).isAsstField(e.getKey())) {
			e.setReturnValue(false);
			return;
		}

		boolean editable = this.getMarAsstEditDelegate().beforEdit(
				e.getBillCardPanel().getBillModel(), e.getRow(), e.getKey(),
				e.getReturnValue());
		e.setReturnValue(Boolean.valueOf(editable));
		return;
	}

    protected void beforEdit(ListBodyBeforeEditEvent e) {
        boolean editable =
                this.getMarAsstEditDelegate().beforEdit(
                    e.getBillListPanel().getBodyBillModel(),
                    e.getRow(),
                    e.getKey(),
                    e.getReturnValue());
        e.setReturnValue(Boolean.valueOf(editable));
        return;
    }

    protected void beforEdit(CardHeadTailBeforeEditEvent e) {
        boolean editable =
                this.getMarAsstEditDelegate().beforEdit(e.getBillCardPanel(), e.getKey(), e.getReturnValue());
        e.setReturnValue(Boolean.valueOf(editable));
        return;
    }

    // 重构卡片表头编辑前状态
    protected void beforEditRefactor(CardHeadTailBeforeEditEvent e) {
        if (this.modelInMMGP.getAppUiState() == AppUiState.NOT_EDIT) {
            e.setReturnValue(false);
            return;
        }

        boolean editable =
                this.getMarAsstEditDelegate().beforEditRefactor(
                    e.getBillCardPanel(),
                    e.getKey(),
                    e.getReturnValue(),
                    e);
        e.setReturnValue(Boolean.valueOf(editable));
        return;
    }

    protected void isEditable(CardPanelLoadEvent e) {
        if (this.modelInMMGP.getAppUiState() == AppUiState.EDIT) return;

        this.getMarAsstEditDelegate().setEditable(e.getBillCardPanel());
    }

    protected void addListener() {
        // 2012-4-12 add
        // 卡片界面打开，置为编辑态后（如点击修改按钮），需设置卡片界面的物料辅助属性的可编辑性，在此事件中做修改可编辑性的处理
        if (this.getPos() == IBillItem.HEAD) {
            this.modelInMMGP.addAppEventListener(
                CardPanelLoadEvent.class,
                new IAppEventHandler<CardPanelLoadEvent>() {
                    @Override
                    public void handleAppEvent(CardPanelLoadEvent e) {
                        MMGPMarAsstPreparator.this.isEditable(e);
                    }
                });

            this.modelInMMGP.addAppEventListener(
                CardHeadTailBeforeEditEvent.class,
                new IAppEventHandler<CardHeadTailBeforeEditEvent>() {
                    @Override
                    public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
                        MMGPMarAsstPreparator.this.beforEdit(e);
                    }
                });
            this.modelInMMGP.addAppEventListener(
                CardHeadTailAfterEditEvent.class,
                new IAppEventHandler<CardHeadTailAfterEditEvent>() {
                    @Override
                    public void handleAppEvent(CardHeadTailAfterEditEvent e) {
                        MMGPMarAsstPreparator.this.afterEdit(e);
                    }
                });

        }

        if (this.getPos() == IBillItem.BODY) {
            this.modelInMMGP.addAppEventListener(
                ListBodyBeforeEditEvent.class,
                new IAppEventHandler<ListBodyBeforeEditEvent>() {
                    @Override
                    public void handleAppEvent(ListBodyBeforeEditEvent e) {
                        MMGPMarAsstPreparator.this.beforEdit(e);
                    }
                });

            this.modelInMMGP.addAppEventListener(
                ListBodyAfterEditEvent.class,
                new IAppEventHandler<ListBodyAfterEditEvent>() {
                    @Override
                    public void handleAppEvent(ListBodyAfterEditEvent e) {
                        MMGPMarAsstPreparator.this.afterEdit(e);
                    }
                });
            this.modelInMMGP.addAppEventListener(
                CardBodyBeforeEditEvent.class,
                new IAppEventHandler<CardBodyBeforeEditEvent>() {
                    @Override
                    public void handleAppEvent(CardBodyBeforeEditEvent e) {
                        MMGPMarAsstPreparator.this.beforEdit(e);
                    }
                });
            this.modelInMMGP.addAppEventListener(
                CardBodyAfterEditEvent.class,
                new IAppEventHandler<CardBodyAfterEditEvent>() {
                    @Override
                    public void handleAppEvent(CardBodyAfterEditEvent e) {
                        MMGPMarAsstPreparator.this.afterEdit(e);
                    }
                });
        }
    }

    /**
     * @return the modelInMMGP
     */
    public IAppModelEx getModelInMMGP() {
        return modelInMMGP;
    }

    /**
     * @param modelInMMGP
     *        the modelInMMGP to set
     */
    public void setModelInMMGP(IAppModelEx modelInMMGP) {
        this.modelInMMGP = modelInMMGP;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator#getMarAsstEditDelegate()
     */
    @Override
    public MarAsstBillModelEditDelegate getMarAsstEditDelegate() {
        if (this.editDelegate == null) {
            editDelegate = new MMGPMarAsstBillModelEditDelegate();
            ((MMGPMarAsstBillModelEditDelegate) editDelegate).setPos(pos);
        }
        return this.editDelegate;
    }

    public void setEditDelegate(MarAsstBillModelEditDelegate editDelegate) {
        this.editDelegate = editDelegate;
    }

}
