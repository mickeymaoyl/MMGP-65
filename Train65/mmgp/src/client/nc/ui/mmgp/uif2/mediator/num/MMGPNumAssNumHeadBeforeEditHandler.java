package nc.ui.mmgp.uif2.mediator.num;

import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ����������ͷ�༭ǰ����
 * </p>
 * 
 * @since �������� Jun 14, 2013
 * @author wangweir
 */
public class MMGPNumAssNumHeadBeforeEditHandler implements IAppEventHandler<CardHeadTailBeforeEditEvent> {

    private MMGPNumAssNumMediator mmgpNumAssNumMediator;

    public MMGPNumAssNumHeadBeforeEditHandler(MMGPNumAssNumMediator mmgpNumAssNumMediator) {
        this.mmgpNumAssNumMediator = mmgpNumAssNumMediator;
    }

    @Override
    public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
        if (e.getKey().equals(mmgpNumAssNumMediator.getCastunitidKey())) {
            this.filterAstUnitRef(e);
            e.setReturnValue(true);
        } else if (e.getKey().equals(mmgpNumAssNumMediator.getNchangerateKey())) {
            this.changeRateBeforeEdit(e);
        } else {
            e.setReturnValue(true);
        }
    }

    /**
     * �������༭ǰ��ӹ���
     * 
     * @param e
     */
    protected void filterAstUnitRef(CardHeadTailBeforeEditEvent e) {
        BillItem astUnitItem = e.getBillCardPanel().getHeadTailItem(e.getKey());
        String materialid =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(mmgpNumAssNumMediator.getCmaterialvid())
                    .getValueObject();

        MMGPNumAssNumInnerUtils.filterAstUnitRef(astUnitItem, materialid);
    }

    /**
     * �������޸�ǰ
     * 
     * @param e
     * @return
     */
    protected void changeRateBeforeEdit(CardHeadTailBeforeEditEvent e) {
        String pk_material =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(this.mmgpNumAssNumMediator.getCmaterialvid())
                    .getValueObject();

        String astUnitId =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(this.mmgpNumAssNumMediator.getCastunitidKey())
                    .getValueObject();

        String mainUnitId =
                (String) e
                    .getBillCardPanel()
                    .getHeadTailItem(this.mmgpNumAssNumMediator.getCunitidKey())
                    .getValueObject();

        boolean editable =
                MMGPNumAssNumInnerUtils.isChangeRateEditable(
                    mmgpNumAssNumMediator,
                    pk_material,
                    mainUnitId,
                    astUnitId);
        e.setReturnValue(editable);
    }

}
