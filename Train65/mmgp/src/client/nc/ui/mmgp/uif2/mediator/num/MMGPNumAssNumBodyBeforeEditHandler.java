package nc.ui.mmgp.uif2.mediator.num;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主辅计量工具类编辑前处理。
 * </p>
 * 
 * @since 创建日期 Jun 13, 2013
 * @author wangweir
 */
public class MMGPNumAssNumBodyBeforeEditHandler implements IAppEventHandler<CardBodyBeforeEditEvent> {

    private MMGPNumAssNumMediator mmgpNumAssNumMediator;

    public MMGPNumAssNumBodyBeforeEditHandler(MMGPNumAssNumMediator mmgpNumAssNumMediator) {
        this.mmgpNumAssNumMediator = mmgpNumAssNumMediator;
    }

    @Override
    public void handleAppEvent(CardBodyBeforeEditEvent e) {
        if (e.getKey().equals(mmgpNumAssNumMediator.getCastunitidKey())) {
            this.filterAstUnitRef(e);
            e.setReturnValue(true);
        } else if (e.getKey().equals(mmgpNumAssNumMediator.getCmaterialvid())) {
            // 物料参照设置多选
            ((UIRefPane) e.getBillCardPanel().getBodyItem(e.getTableCode(), e.getKey()).getComponent())
                .setMultiSelectedEnabled(mmgpNumAssNumMediator.isMaterialMultSelect());
            e.setReturnValue(true);
        } else if (e.getKey().equals(mmgpNumAssNumMediator.getNchangerateKey())) {
            this.changeRateBeforeEdit(e);
        } else {
            e.setReturnValue(true);
        }
    }

    /**
     * 换算率修改前
     * 
     * @param e
     * @return
     */
    protected void changeRateBeforeEdit(CardBodyBeforeEditEvent e) {
        String pk_material = (String) getBodyValue(e, this.mmgpNumAssNumMediator.getCmaterialvid());

        String astUnitId = (String) getBodyValue(e, this.mmgpNumAssNumMediator.getCastunitidKey());

        String mainUnitId = (String) this.getBodyValue(e, this.mmgpNumAssNumMediator.getCunitidKey());

        boolean isEditable =
                MMGPNumAssNumInnerUtils.isChangeRateEditable(mmgpNumAssNumMediator, pk_material, mainUnitId, astUnitId);
        e.setReturnValue(isEditable);
    }

    protected Object getBodyValue(CardBodyBeforeEditEvent e,
                                  String itemkey) {
        return e.getBillCardPanel().getBodyValueAt(e.getRow(), itemkey);
    }

    /**
     * 辅计量编辑前添加过滤
     * 
     * @param e
     */
    protected void filterAstUnitRef(CardBodyBeforeEditEvent e) {
        BillItem astUnitItem = e.getBillCardPanel().getBodyItem(e.getTableCode(), e.getKey());
        String materialid =
                (String) e.getBillCardPanel().getBodyValueAt(e.getRow(), mmgpNumAssNumMediator.getCmaterialvid());

        MMGPNumAssNumInnerUtils.filterAstUnitRef(astUnitItem, materialid);
    }

}
