/**
 * 
 */
package nc.itf.mmgp.bean;

/**
 * ΨһУ��VO
 * <P>
 * �������͵Ļ������������������ҪĿ��Ϊ�� <U>
 * <li>Ψһ��У��</li>
 * </P>
 * 
 * @author zjy
 * @deprecated ʹ��V6���·�ʽ���
 */
public interface IMMBDUniqueCheckVO extends IMMBDCheckVO {
    /**
     * ���Ψһ��У����ֶ�
     * 
     * @return Ψһ��У����ֶ�
     */
    String getUniqueCheckField();

    /**
     * ��ó���ʱ��ʾ��Ϣ
     * 
     * @return
     */
    String getDisplayMessage();

    /**
     * ����ʱ��ʾ��Ϣ:
     * <p>
     * getUniqueCheckFieldName() ��getDisplayMessage()���Ѿ����ڣ����ܱ���
     * 
     * @return
     */
    String getUniqueCheckFieldName();

}
