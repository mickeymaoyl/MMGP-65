package nc.ui.mmgp.uif2.view.value;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

//20120222 ���µ�UE�������з�ѡ������ʾ�����󡢾�����Ϣ����ʾ�ڴ����������ܶԻ�����ʾ���޸�����ĵ��õڶ�������Ϊfalse
public class MMGPCardPanelDefaultValueSetter {

	public MMGPCardPanelDefaultValueSetter() {
		super();
	}

	public void setDefaultValue(LoginContext context, BillCardPanel cardPanel) {

		// ����������֯�Լ���������Ĭ��ֵ
		setBillItemValue(cardPanel, "pk_group", context.getPk_group());
		setBillItemValue(cardPanel, "pk_org", context.getPk_org());
		setBillItemValue(cardPanel, IBaseServiceConst.ENABLESTATE_FIELD,
				IPubEnumConst.ENABLESTATE_ENABLE);
		/* Apr 11, 2013 wangweir ִ�б༭��ʽ Begin */
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
