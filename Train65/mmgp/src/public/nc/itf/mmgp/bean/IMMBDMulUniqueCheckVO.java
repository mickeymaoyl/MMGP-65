package nc.itf.mmgp.bean;

/**
 * <b> ����ֶε��ظ���У�� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-5-15
 * 
 * @author Administrator
 * @deprecated ʹ��V6���·�ʽ���
 */
public interface IMMBDMulUniqueCheckVO extends IMMBDCheckVO {
    /**
     * ���Ψһ��У����ֶ�
     * 
     * @return Ψһ��У����ֶ�
     */
    String[] getUniqueCheckFields();

    String[] getUniqueCheckFieldNames();

}
