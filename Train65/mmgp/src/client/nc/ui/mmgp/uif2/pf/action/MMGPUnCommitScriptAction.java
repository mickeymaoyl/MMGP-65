package nc.ui.mmgp.uif2.pf.action;

import nc.ui.pubapp.uif2app.actions.pflow.UnCommitScriptAction;

/**
 * @author wenjl
 * @date 2013-6-27
 */
public class MMGPUnCommitScriptAction extends UnCommitScriptAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4530562813580967642L;
	
//	@Override
//	protected boolean isActionEnable() {
//
//		AggregatedValueObject selectedData = (AggregatedValueObject) this.model.getSelectedData();
//		int status = ApproveStatus.FREE;
//		String approver = null;
//		if (selectedData != null) {
//			status = this.extractApproveStatus(selectedData).intValue();
//			approver = this.extractApprover(selectedData);
//		}
//		// ���ݹ�������2011.6.25
//		// ������״̬��������Ϊ�յĵ��ݿ����ջ� ���������ύ̬
//		boolean isEnable = (selectedData != null && status == ApproveStatus.APPROVING && StringUtils.isEmpty(approver))
//			|| ApproveStatus.COMMIT == status;
//		return isEnable;
//	}

}
