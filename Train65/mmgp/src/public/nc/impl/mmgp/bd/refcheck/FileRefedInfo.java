package nc.impl.mmgp.bd.refcheck;

import java.io.Serializable;

/**
 * �������ú󲻿��޸�ע����Ϣ
 * 
 * @since 6.0
 * @version 2011-6-7 ����02:47:19
 * @author chendb
 */
public class FileRefedInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private EntityBaseInfo[] entityBaseInfos;

    private String errInfo;

    private String modulecode;

    private String[] noEditFileds;

    /**
     * @param modulecode
     *        ģ�����
     * @param entityInfos
     *        ʵ�������Ϣ�飺ָ�������У����յ�ǰ�������ֶΣ�����ʵ��Ļ�����Ϣ���ɸ�����Ҫ����������� �����ɹ������У�"�ɹ�������ϸ" �� " �����ƻ�" �о��������� "���ϵ���" �����ϣ�������� "�ɹ�������ϸ"
     *        ���������Ϻ����ϵ��������޸Ľ��п��ƣ���ֻ����"�ɹ�������ϸ"ʵ����Ϣ���� ������ { EntityBaseInfo("ʵ��vo��ȫ��"," ʵ���в��յ�ǰ�������ֶ�����","ʵ������֯����") }
     * @param noEditFileds
     *        �����У����ú����޸ĵ��ֶ����Ƽ�
     * @param errInfo
     *        ������ʾ��Ϣ
     */
    public FileRefedInfo(String modulecode,
                         EntityBaseInfo[] entityInfos,
                         String[] noEditFileds,
                         String errInfo) {
        this.modulecode = modulecode;
        this.entityBaseInfos = entityInfos;
        this.noEditFileds = noEditFileds;
        this.errInfo = errInfo;
    }

    /**
     * �����ṩ�Ļ�����Ϣ
     * 
     * @return
     */
    public EntityBaseInfo[] getEntityBaseInfos() {
        return this.entityBaseInfos;
    }

    /**
     * ������ʾ��Ϣ
     * 
     * @return
     */
    public String getErrInfo() {
        return this.errInfo;
    }

    /**
     * ģ�����
     * 
     * @return
     */
    public String getModulecode() {
        return this.modulecode;
    }

    /**
     * �����У����ú����޸ĵ��ֶ����Ƽ�
     * 
     * @return
     */
    public String[] getnoEditFileds() {
        return this.noEditFileds;
    }

    /**
     * @return the noEditFileds
     */
    public String[] getNoEditFileds() {
        return noEditFileds;
    }

    /**
     * @param noEditFileds
     *        the noEditFileds to set
     */
    public void setNoEditFileds(String[] noEditFileds) {
        this.noEditFileds = noEditFileds;
    }

    /**
     * @param entityBaseInfos
     *        the entityBaseInfos to set
     */
    public void setEntityBaseInfos(EntityBaseInfo[] entityBaseInfos) {
        this.entityBaseInfos = entityBaseInfos;
    }

    /**
     * @param errInfo
     *        the errInfo to set
     */
    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    /**
     * @param modulecode
     *        the modulecode to set
     */
    public void setModulecode(String modulecode) {
        this.modulecode = modulecode;
    }

}
