package nc.itf.mmgp.bean;

/**
 * <b> MM��ͨ�������ӿ� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-5-14
 * 
 * @author wangweiu
 * @deprecated ʹ��V6���·�ʽ���
 */
public interface IMMBDCheckVO {
    /**
     * ��ñ���
     * 
     * @return ��ñ���
     */
    String getTableName();

    /**
     * ��������ֶ�
     * 
     * @return �����ֶ�
     */
    String getPKFieldName();

    Object getAttributeValue(String attributeName);
}
