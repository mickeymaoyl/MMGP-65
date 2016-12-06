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
//		// 根据公共需求2011.6.25
//		// 审批中状态、审批人为空的单据可以收回 或者是已提交态
//		boolean isEnable = (selectedData != null && status == ApproveStatus.APPROVING && StringUtils.isEmpty(approver))
//			|| ApproveStatus.COMMIT == status;
//		return isEnable;
//	}

}
