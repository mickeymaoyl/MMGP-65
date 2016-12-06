package nc.impl.mmgp.bd.refcheck;

/**
 * ʵ�������Ϣ - ��Զ�汾�ĵ��� ��ע�����汾�Ƿ����ã�����ֻҪ��һ���汾�����ã��õ����Ͳ��ܱ��޸�
 * 
 * @since 6.0
 * @version 2011-6-27 ����04:33:11
 * @author chendb
 */
public class EntityInfoForVersion extends EntityBaseInfo {

    private static final long serialVersionUID = 1L;

    private String srcFieldName;

    /**
     * @param voclassName
     *        ʵ��vo��ȫ��
     * @param fieldName
     *        ʵ���в��յ�ǰ�������°汾���ֶ�����
     * @param srcFieldName
     *        ʵ���в��յ�ǰ����ԭʼ�汾���ֶ�����
     */
    public EntityInfoForVersion(String voclassName,
                                String fieldName,
                                String srcFieldName) {
        super(voclassName, fieldName);
        this.srcFieldName = srcFieldName;
    }

    /**
     * �������֯�������ʱ ������ "���ϻ�����Ϣ" �������ӽṹ�� "���ϲɹ���Ϣ"��"���Ͽ����Ϣ"��
     * 
     * @param voclassName
     *        ʵ��vo��ȫ��
     * @param fieldName
     *        ʵ���в��յ�ǰ�������°汾���ֶ�����
     * @param srcFieldName
     *        ʵ���в��յ�ǰ����ԭʼ�汾���ֶ�����
     * @param orgName
     *        ʵ������֯����
     */
    public EntityInfoForVersion(String voclassName,
                                String fieldName,
                                String srcFieldName,
                                String orgName) {
        super(voclassName, fieldName, orgName);
        this.srcFieldName = srcFieldName;
    }

    public String getSrcFieldName() {
        return this.srcFieldName;
    }

    public void setSrcFieldName(String srcFieldName) {
        this.srcFieldName = srcFieldName;
    }

}
