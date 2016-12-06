package nc.ui.mmgp.base.lineaction;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.button.IBillButton;
/**
 * 
 * <b> 简要描述功能 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @author wangweiu
 * @deprecated
 */
public class MMCommonLineAction implements IMMLineAction {
    private AbstractBillUI billUI = null;

    public MMCommonLineAction(AbstractBillUI ui) {
        billUI = ui;
    }

    /**
     * 
     * 根据类型，调用eventhandler里对应的行操作方法
     * @see nc.ui.mmgp.base.lineaction.IMMLineAction#doLineAction(int)
     */
    public boolean doLineAction(int type) {
        int btnNo = -1;
        switch (type) {
            case BillTableLineAction.ADDLINE:
                btnNo = IBillButton.AddLine;
                break;
            case BillTableLineAction.DELLINE:
                btnNo = IBillButton.DelLine;
                break;
            case BillTableLineAction.INSERTLINE:
                btnNo = IBillButton.InsLine;
                break;
            case BillTableLineAction.COPYLINE:
                btnNo = IBillButton.CopyLine;
                break;
            case BillTableLineAction.PASTELINE:
                btnNo = IBillButton.PasteLine;
                break;
            case BillTableLineAction.PASTELINETOTAIL:
                btnNo = IBillButton.PasteLinetoTail;
                break;
            default:
                break;
        }
        ButtonObject bo = billUI.getButtonManager().getButton(btnNo);
        if (bo != null && bo.isEnabled() && bo.isVisible()) {
            billUI.onButtonClicked(bo);
        }
        return true;
    }

}
