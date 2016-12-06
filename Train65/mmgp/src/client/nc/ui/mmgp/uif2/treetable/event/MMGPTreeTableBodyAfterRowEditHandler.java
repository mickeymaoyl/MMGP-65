package nc.ui.mmgp.uif2.treetable.event;

import nc.ui.mmgp.pub.bill.treetable.MMGPBillModelTreeTableAdapter;
import nc.ui.mmgp.pub.bill.treetable.MMGPBillTreeTableTools;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.BodyRowEditType;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15下午1:30:26
 * @author: tangxya
 */
@SuppressWarnings("restriction")
public class MMGPTreeTableBodyAfterRowEditHandler implements
		IAppEventHandler<CardBodyAfterRowEditEvent> {

	private static final String PK_GROUP = "pk_group";
	private static final String PK_ORG = "pk_org";
	private static final String PK_ORG_V = "pk_org_v";
	private String innercodeKey = "innercode";
    
    public String getInnercodeKey() {
        return innercodeKey;
    }
    public void setInnercodeKey(String innercodeKey) {
        this.innercodeKey = innercodeKey;
    }

	public void handleAppEvent(CardBodyAfterRowEditEvent e) {

		BodyRowEditType rowType = e.getRowEditType();
		BillCardPanel card = e.getBillCardPanel();
		MMGPBillModelTreeTableAdapter billModelTreeAdapter = (MMGPBillModelTreeTableAdapter) card.getBodyPanel().getTableModel();
		int rows = billModelTreeAdapter.getRowCount();

		if (BodyRowEditType.ADDLINE == rowType
				|| BodyRowEditType.INSERTLINE == rowType) { // 增行
			// 集团
			String pk_group = (String) card.getBillData().getHeadItem(PK_GROUP)
					.getValueObject();
			// 业务单元
			String pk_org = (String) card.getBillData().getHeadItem(PK_ORG)
					.getValueObject();
			// 业务单元多版本
			String pk_org_v = (String) card.getBillData().getHeadItem(PK_ORG_V)
					.getValueObject();

			for (int i = 0; i < rows; i++) {
				// 赋值
				billModelTreeAdapter.setValueAt(pk_group, i, PK_GROUP);
				billModelTreeAdapter.setValueAt(pk_org, i, PK_ORG);
				billModelTreeAdapter.setValueAt(pk_org_v, i, PK_ORG_V);
			}
		}
        //获得innercode的列数
        int inercodeIndex = MMGPBillTreeTableTools.getInercodeIndex(billModelTreeAdapter, innercodeKey);
        MMGPBillTreeTableTools.setBodyInnercode(billModelTreeAdapter.getRoot(),inercodeIndex);

		// 显示参照数据
        billModelTreeAdapter.loadLoadRelationItemValue();
	}

}
