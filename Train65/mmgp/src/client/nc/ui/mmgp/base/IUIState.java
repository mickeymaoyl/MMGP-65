package nc.ui.mmgp.base;

/**
 * ���칦�ܵ����״̬�ӿڣ����е���������ڵ�Ľ��涼Ӧ��ͳһʹ�ô˽ӿڶ���Ľ���״̬�� <br>
 * ���ҵ��ڵ�������״̬��Ҫ���䣬�����״ֵ̬Ӧ�ô���200�� <br>
 * �������ܹ�����ϵͳ״̬�����䣬����״̬����ʱ������ҵ��ڵ������״̬�г�ͻ
 * 
 * @author wangweiu
 * @deprecated by wangweiu ��ʹ���ˣ��ο�ui����2�ķ�ʽ����״̬
 */
public interface IUIState {
    /**
     * �����ʼ��״̬
     */
    int STATE_INIT = 0;

    /**
     * �б�״̬
     */
    int STATE_LIST = 1;

    /**
     * ��ѯ״̬
     */
    int STATE_QUERY = 2;

    /**
     * ���״̬
     */
    int STATE_CARD_BROWSE = 3;

    /**
     * ����״̬
     */
    int STATE_ADD = 4;

    /**
     * �״�����״̬
     */
    int STATE_ADD_FIRST = 5;

    /**
     * ��������״̬
     */
    int STATE_ADD_ETC = 6;

    /**
     * �޸�״̬
     */
    int STATE_MODIFY = 7;

    /**
     * �����޸�״̬
     */
    int STATE_MODIFY_ETC = 8;

    /**
     * ��������״̬--���б��뿨Ƭ����������������
     */
    int STATE_SPECIAL = 9;

    /**
     * �쳣״̬
     */
    int STATE_ERROR = 99;
}
