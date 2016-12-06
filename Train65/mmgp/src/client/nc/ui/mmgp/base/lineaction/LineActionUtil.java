package nc.ui.mmgp.base.lineaction;

import java.util.ArrayList;

import javax.swing.Action;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.bill.action.SeparatorAction;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.button.IBillButton;

/**
 * <b> 表体右上按钮动作工具类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-5-11
 * 
 * @author wangweiu
 * @deprecated
 */
public final class LineActionUtil {

	/**
	 * 为卡片的表体注册小按钮
	 * 
	 * @param ui
	 * @param cardpanel
	 */
	public static void initBodyActions(AbstractBillUI ui,
			BillCardPanel cardpanel) {
		if (ui.getButtonManager().getButton(IBillButton.Line) != null) {

			ArrayList<Action> acts = LineActionUtil.getBodyTabActions(ui,
					new MMCommonLineAction(ui));
			cardpanel.addTabAction(IBillItem.BODY, acts);
			// codesync_xg_v01 隐藏表体右键菜单 delete
			// cardpanel.setBodyMenuShow(true);
			cardpanel.setEnabled(false);
		}
	}

	public static ArrayList<Action> getBodyTabActions(IMMLineAction action) {

		ArrayList<Action> acts = new ArrayList<Action>();
		acts.add(MMGPTableLineAction.getInstance(action,
				BillTableLineAction.ADDLINE));
		acts.add(MMGPTableLineAction.getInstance(action,
				BillTableLineAction.INSERTLINE));
		acts.add(MMGPTableLineAction.getInstance(action,
				BillTableLineAction.DELLINE));
		acts.add(MMGPTableLineAction.getInstance(action,
				BillTableLineAction.COPYLINE));
		acts.add(MMGPTableLineAction.getInstance(action,
				BillTableLineAction.PASTELINE));
		acts.add(new SeparatorAction());
		return acts;

	}

	/**
	 * 取得按钮动作
	 * 
	 * @param action
	 *            动作处理类
	 * @return
	 */
	private static ArrayList<Action> getBodyTabActions(AbstractBillUI ui,
			IMMLineAction action) {
		ArrayList<Action> acts = new ArrayList<Action>();
		// codesync_xg_v01 根据行操作下的按钮来决定哪个显示 modify begin
		if (ui.getButtonManager().getButton(IBillButton.AddLine) != null) {
			acts.add(MMGPTableLineAction.getInstance(action,
					BillTableLineAction.ADDLINE));
		}
		if (ui.getButtonManager().getButton(IBillButton.InsLine) != null) {
			acts.add(MMGPTableLineAction.getInstance(action,
					BillTableLineAction.INSERTLINE));
		}
		if (ui.getButtonManager().getButton(IBillButton.DelLine) != null) {
			acts.add(MMGPTableLineAction.getInstance(action,
					BillTableLineAction.DELLINE));
		}
		if (ui.getButtonManager().getButton(IBillButton.CopyLine) != null) {
			acts.add(MMGPTableLineAction.getInstance(action,
					BillTableLineAction.COPYLINE));
		}
		if (ui.getButtonManager().getButton(IBillButton.PasteLine) != null) {
			acts.add(MMGPTableLineAction.getInstance(action,
					BillTableLineAction.PASTELINE));
		}// codesync_xg_v01 modify end
		acts.add(new SeparatorAction());
		return acts;
	}

}
