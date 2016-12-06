/**
 * 
 */
package nc.ui.mmgp.pub.beans;

import java.util.Arrays;

import nc.ui.pub.ButtonObject;

/**
 * MMGP通用按钮类
 * 
 * @author wangweiu
 * @deprecated by wangweiu :全部使用NCAction进行按钮显示
 * @see nc.ui.uif2.NCAction
 */
public class MMGPButtonObject extends ButtonObject {

	/**
	 * 操作状态 <br>
	 * //列表状态 int LIST_STATE = 0; <br>
	 * //浏览状态 int BROWER_STATE = 1; <br>
	 * //增加状态 int ADD_STATE = 2; <br>
	 * //修改状态 int MODIFY_STATE = 3; <br>
	 * //保存成功退出 int SAVE_SUCCESS_QUIT = 4; <br>
	 * //保存成功不退出 int SAVE_SUCCESS_NOT_QUIT = 5; <br>
	 * //保存失败退出 int SAVE_FAILURE_QUIT = 6; <br>
	 * //保存失败不退出 int SAVE_FAILURE_NOT_QUIT = 7; <br>
	 * //界面初始化状态 int INIT_STATE = 8; <br>
	 * //首次增加状态 int ADD_FIRST_STATE = 9; <br>
	 * //其他增加状态 int ADD_ETC_STATE = 10; <br>
	 * //其他修改状态 int MODIFY_ETC_STATE = 11;
	 */
	private int[] oprateStatus;

	// /**
	// * 业务状态 //审批未通过 int NOPASS = 0; //审批通过 int CHECKPASS = 1; //审批进行中 int
	// CHECKGOING = 2; //提交状态 int COMMIT = 3;
	// //作废状态
	// * int DELETE = 4; //冲销状态 int CX = 5; //终止(结算）态 int ENDED = 6; //冻结状态 int
	// FREEZE = 7; //自由态 int FREE = 8; //计划态
	// int
	// * PLAN = 20; //确认态 int CONFIRM = 21; //投放态 int LAUNCH = 22;
	// */
	// private int[] businessStatus;

	/**
	 * 是否受状态控制, 默认控制
	 */
	private boolean statusControl = true;

	public MMGPButtonObject(String name, String hint, int power, String code) {
		super(name, hint, power, code);
	}

	public MMGPButtonObject(String name, String hint, String code) {
		super(name, hint, code);
	}

	// public int[] getBusinessStatus() {
	// return businessStatus;
	// }
	//
	// public void setBusinessStatus(int[] businessStatus) {
	// this.businessStatus = businessStatus;
	// if (this.businessStatus != null) {
	// Arrays.sort(this.businessStatus);
	// }
	// }

	/**
	 * 操作状态 //列表状态 int LIST_STATE = 0; //浏览状态 int BROWER_STATE = 1; //增加状态 int
	 * ADD_STATE = 2; //修改状态 int MODIFY_STATE = 3; //保存成功退出 int
	 * SAVE_SUCCESS_QUIT = 4; //保存成功不退出 int SAVE_SUCCESS_NOT_QUIT = 5; //保存失败退出
	 * int SAVE_FAILURE_QUIT = 6; //保存失败不退出 int SAVE_FAILURE_NOT_QUIT = 7;
	 * //界面初始化状态 int INIT_STATE = 8; //首次增加状态 int ADD_FIRST_STATE = 9; //其他增加状态
	 * int ADD_ETC_STATE = 10; //其他修改状态 int MODIFY_ETC_STATE = 11;
	 */
	public int[] getOprateStatus() {
		return oprateStatus;
	}

	/**
	 * 操作状态 //列表状态 int LIST_STATE = 0; //浏览状态 int BROWER_STATE = 1; //增加状态 int
	 * ADD_STATE = 2; //修改状态 int MODIFY_STATE = 3; //保存成功退出 int
	 * SAVE_SUCCESS_QUIT = 4; //保存成功不退出 int SAVE_SUCCESS_NOT_QUIT = 5; //保存失败退出
	 * int SAVE_FAILURE_QUIT = 6; //保存失败不退出 int SAVE_FAILURE_NOT_QUIT = 7;
	 * //界面初始化状态 int INIT_STATE = 8; //首次增加状态 int ADD_FIRST_STATE = 9; //其他增加状态
	 * int ADD_ETC_STATE = 10; //其他修改状态 int MODIFY_ETC_STATE = 11;
	 */
	public void setOprateStatus(int[] oprateStatus) {
		this.oprateStatus = oprateStatus;
		if (this.oprateStatus != null) {
			Arrays.sort(this.oprateStatus);
		}
	}

	public void change2OprateStatus(int oprateState) {
		controlWithState(this.oprateStatus, oprateState);
	}

	/**
	 * 使用状态进行控制按钮
	 * 
	 * @param state
	 */
	private void controlWithState(int[] status, int state) {
		if (!statusControl) {
			return;
		}
		boolean isEnable = this.isContains(status, state);
		this.setEnabled(isEnable);
	}

	// public void doAction(MMToftPanel mmtp) {
	//
	// }

	// public void change2BusinessStatus(int businessState) {
	// controlWithState(this.businessStatus, businessState);
	// }

	/**
	 * 包含状态
	 * 
	 * @param intArrs
	 * @param intValue
	 * @return
	 */
	private boolean isContains(int[] intArrs, int intValue) {
		// 现在默认什么都不设置，那么就是什么条件下都符合
		if (intArrs == null) {
			return true;
		}
		return Arrays.binarySearch(intArrs, intValue) >= 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MMGPButtonObject) {
			return this.getCode().equals(((MMGPButtonObject) obj).getCode());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getCode().hashCode();
	}

	public boolean isStatusControl() {
		return statusControl;
	}

	public void setStatusControl(boolean statusControl) {
		this.statusControl = statusControl;
	}

}
