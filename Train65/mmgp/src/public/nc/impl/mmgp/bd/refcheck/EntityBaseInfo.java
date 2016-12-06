package nc.impl.mmgp.bd.refcheck;

import java.io.Serializable;

/**
 * ʵ�������Ϣ
 * 
 * @since 6.0
 * @version 2011-6-9 ����08:45:31
 * @author chendb
 */
public class EntityBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName;

    private String orgName;

    private String voclassName;

    /**
     * @param voclassName
     *        ʵ��vo��ȫ��
     * @param fieldName
     *        ʵ���в��յ�ǰ�������ֶ�����
     */
    public EntityBaseInfo(String voclassName,
                          String fieldName) {
        this.voclassName = voclassName;
        this.fieldName = fieldName;
    }

    /**
     * �������֯�������ʱ ������ "���ϻ�����Ϣ" �������ӽṹ�� "���ϲɹ���Ϣ"��"���Ͽ����Ϣ"��
     * 
     * @param voclassName
     *        ʵ��vo��ȫ��
     * @param fieldName
     *        ʵ���в��յ�ǰ�������ֶ�����
     * @param orgName
     *        ʵ������֯����
     */
    public EntityBaseInfo(String voclassName,
                          String fieldName,
                          String orgName) {
        this.voclassName = voclassName;
        this.fieldName = fieldName;
        this.orgName = orgName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getVoclassName() {
        return this.voclassName;
    }
}
