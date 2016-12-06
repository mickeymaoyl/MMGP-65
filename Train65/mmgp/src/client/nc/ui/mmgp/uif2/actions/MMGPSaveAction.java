package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.SaveAction;
import nc.ui.uif2.UIState;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;

public class MMGPSaveAction extends SaveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3135871724828169193L;

	/**
	 * 
	 * �������棨see������ǵġ�����
	 * 
	 * @see nc.ui.scmf.qc.qualitylevel.action.QualityLevelSaveAction
	 */
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object value = this.getEditor().getValue();
		if (!(value instanceof IBill)) {
			super.doAction(e);
			return;
		}

		this.validate(value);

		IBill billVO = (IBill) value;

		ClientBillToServer<IBill> tool = new ClientBillToServer<IBill>();

		IBill[] clientVOs = new IBill[] { billVO };

		// ���Ӱ�ť
		if (UIState.ADD == this.getModel().getUiState()) {

			doAddSave(value);
		}
		// �޸İ�ť
		else {
			IBill[] oldVO = new IBill[] { (IBill) this.getModel()
					.getSelectedData() };
			// ȡ��������VO
			IBill[] lightVOs = tool.construct(oldVO, clientVOs);

			doEditSave(lightVOs[0]);
		}
		showSuccessInfo();
	}

}
