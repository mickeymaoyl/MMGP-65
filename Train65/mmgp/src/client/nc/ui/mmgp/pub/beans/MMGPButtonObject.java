/**
 * 
 */
package nc.ui.mmgp.pub.beans;

import java.util.Arrays;

import nc.ui.pub.ButtonObject;

/**
 * MMGPͨ�ð�ť��
 * 
 * @author wangweiu
 * @deprecated by wangweiu :ȫ��ʹ��NCAction���а�ť��ʾ
 * @see nc.ui.uif2.NCAction
 */
public class MMGPButtonObject extends ButtonObject {

	/**
	 * ����״̬ <br>
	 * //�б�״̬ int LIST_STATE = 0; <br>
	 * //���״̬ int BROWER_STATE = 1; <br>
	 * //����״̬ int ADD_STATE = 2; <br>
	 * //�޸�״̬ int MODIFY_STATE = 3; <br>
	 * //����ɹ��˳� int SAVE_SUCCESS_QUIT = 4; <br>
	 * //����ɹ����˳� int SAVE_SUCCESS_NOT_QUIT = 5; <br>
	 * //����ʧ���˳� int SAVE_FAILURE_QUIT = 6; <br>
	 * //����ʧ�ܲ��˳� int SAVE_FAILURE_NOT_QUIT = 7; <br>
	 * //�����ʼ��״̬ int INIT_STATE = 8; <br>
	 * //�״�����״̬ int ADD_FIRST_STATE = 9; <br>
	 * //��������״̬ int ADD_ETC_STATE = 10; <br>
	 * //�����޸�״̬ int MODIFY_ETC_STATE = 11;
	 */
	private int[] oprateStatus;

	// /**
	// * ҵ��״̬ //����δͨ�� int NOPASS = 0; //����ͨ�� int CHECKPASS = 1; //���������� int
	// CHECKGOING = 2; //�ύ״̬ int COMMIT = 3;
	// //����״̬
	// * int DELETE = 4; //����״̬ int CX = 5; //��ֹ(���㣩̬ int ENDED = 6; //����״̬ int
	// FREEZE = 7; //����̬ int FREE = 8; //�ƻ�̬
	// int
	// * PLAN = 20; //ȷ��̬ int CONFIRM = 21; //Ͷ��̬ int LAUNCH = 22;
	// */
	// private int[] businessStatus;

	/**
	 * �Ƿ���״̬����, Ĭ�Ͽ���
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
	 * ����״̬ //�б�״̬ int LIST_STATE = 0; //���״̬ int BROWER_STATE = 1; //����״̬ int
	 * ADD_STATE = 2; //�޸�״̬ int MODIFY_STATE = 3; //����ɹ��˳� int
	 * SAVE_SUCCESS_QUIT = 4; //����ɹ����˳� int SAVE_SUCCESS_NOT_QUIT = 5; //����ʧ���˳�
	 * int SAVE_FAILURE_QUIT = 6; //����ʧ�ܲ��˳� int SAVE_FAILURE_NOT_QUIT = 7;
	 * //�����ʼ��״̬ int INIT_STATE = 8; //�״�����״̬ int ADD_FIRST_STATE = 9; //��������״̬
	 * int ADD_ETC_STATE = 10; //�����޸�״̬ int MODIFY_ETC_STATE = 11;
	 */
	public int[] getOprateStatus() {
		return oprateStatus;
	}

	/**
	 * ����״̬ //�б�״̬ int LIST_STATE = 0; //���״̬ int BROWER_STATE = 1; //����״̬ int
	 * ADD_STATE = 2; //�޸�״̬ int MODIFY_STATE = 3; //����ɹ��˳� int
	 * SAVE_SUCCESS_QUIT = 4; //����ɹ����˳� int SAVE_SUCCESS_NOT_QUIT = 5; //����ʧ���˳�
	 * int SAVE_FAILURE_QUIT = 6; //����ʧ�ܲ��˳� int SAVE_FAILURE_NOT_QUIT = 7;
	 * //�����ʼ��״̬ int INIT_STATE = 8; //�״�����״̬ int ADD_FIRST_STATE = 9; //��������״̬
	 * int ADD_ETC_STATE = 10; //�����޸�״̬ int MODIFY_ETC_STATE = 11;
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
	 * ʹ��״̬���п��ư�ť
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
	 * ����״̬
	 * 
	 * @param intArrs
	 * @param intValue
	 * @return
	 */
	private boolean isContains(int[] intArrs, int intValue) {
		// ����Ĭ��ʲô�������ã���ô����ʲô�����¶�����
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
