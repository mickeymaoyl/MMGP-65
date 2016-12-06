package nc.ui.mmgp.uif2.view.value;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

//20120222 最新的UE规则：所有非选择型提示、错误、警告信息均显示在错误区，不能对话框显示，修改下面的调用第二个参数为false
public class MMGPCardPanelDefaultValueSetter {

	public MMGPCardPanelDefaultValueSetter() {
		super();
	}

	public void setDefaultValue(LoginContext context, BillCardPanel cardPanel) {

		// 设置所属组织以及所属集团默认值
		setBillItemValue(cardPanel, "pk_group", context.getPk_group());
		setBillItemValue(cardPanel, "pk_org", context.getPk_org());
		setBillItemValue(cardPanel, IBaseServiceConst.ENABLESTATE_FIELD,
				IPubEnumConst.ENABLESTATE_ENABLE);
		/* Apr 11, 2013 wangweir 执行编辑公式 Begin */
		cardPanel.execHeadEditFormulas();
		/* Apr 11, 2013 wangweir End */
	}

	protected void setBillItemValue(BillCardPanel cardPanel, String key,
			Object value) {
		BillItem headItem = cardPanel.getHeadItem(key);
		if (headItem != null) {
			headItem.setDefaultValue(MMStringUtil.objectToString(value));
			headItem.setValue(value);
			cardPanel.getBillData().loadEditHeadRelation(key);
		}
	}
}
