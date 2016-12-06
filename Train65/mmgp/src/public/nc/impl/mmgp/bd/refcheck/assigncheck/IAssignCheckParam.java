package nc.impl.mmgp.bd.refcheck.assigncheck;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���ϡ���Ӧ�̡��ͻ����󵵰���ȡ������ǰУ�顱�͡�������Ϊǿ����У�顱ǰУ������ӿ�
 * </p>
 * 
 * @since �������� Nov 18, 2013
 * @author wangweir
 */
public interface IAssignCheckParam {

    /**
     * �����ֶ�
     * 
     * @return
     */
    String getGroupField();

    /**
     * ��֯�ֶ�
     * 
     * @return
     */
    String getOrgField();

    /**
     * �����ֶ�
     * 
     * @return
     */
    String[] getDocField();

    /**
     * ���õ�������/�������ơ����ڴ�����ʾ��Ϣ
     * 
     * @return
     */
    String getErrorMsg();

    /**
     * ��ȡ����
     * 
     * @return
     */
    String getTableName();
}
