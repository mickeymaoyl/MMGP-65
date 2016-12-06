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
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-15����1:30:26
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
				|| BodyRowEditType.INSERTLINE == rowType) { // ����
			// ����
			String pk_group = (String) card.getBillData().getHeadItem(PK_GROUP)
					.getValueObject();
			// ҵ��Ԫ
			String pk_org = (String) card.getBillData().getHeadItem(PK_ORG)
					.getValueObject();
			// ҵ��Ԫ��汾
			String pk_org_v = (String) card.getBillData().getHeadItem(PK_ORG_V)
					.getValueObject();

			for (int i = 0; i < rows; i++) {
				// ��ֵ
				billModelTreeAdapter.setValueAt(pk_group, i, PK_GROUP);
				billModelTreeAdapter.setValueAt(pk_org, i, PK_ORG);
				billModelTreeAdapter.setValueAt(pk_org_v, i, PK_ORG_V);
			}
		}
        //���innercode������
        int inercodeIndex = MMGPBillTreeTableTools.getInercodeIndex(billModelTreeAdapter, innercodeKey);
        MMGPBillTreeTableTools.setBodyInnercode(billModelTreeAdapter.getRoot(),inercodeIndex);

		// ��ʾ��������
        billModelTreeAdapter.loadLoadRelationItemValue();
	}

}
