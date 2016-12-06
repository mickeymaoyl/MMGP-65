package nc.vo.mmgp.linkquery;

/**
 * 
 * <b> �������VO </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since: 
 * ��������:2014-10-29
 * @author:liwsh
 */
public class LinkQueryParamVO  implements java.io.Serializable, java.lang.Cloneable {

    private static final long serialVersionUID = -8168143864605633825L;
    
    /**
     * �������ͱ�����߽������ͱ��루���û�е������ͣ�
     */
    private String billtype;
    
    /**
     * ����id����������Ҫ��
     */
    private String billid;
    /**
     * ���ݱ���bid
     */
    private String billbid;
    
    /**
     * ���ݺ�
     */
    private String billcode;
    
    /**
     * ��Դ����
     */
    private String vsrcid;
    /**
     * ��Դ���ݺ�
     */
    private String vsrccode;
    /**
     * ��Դ�������ͱ���
     */
    private String vsrctype;
    /**
     * ��Դ�������ͱ���
     */
    private String vsrctrantype;
    /**
     * ��Դ������ϸ
     */
    private String vsrcbid;
    
    
    /**
     * Դͷ����
     */
    private String vfirstid;
    /**
     * Դͷ���ݺ�
     */
    private String vfirstcode;
    /**
     * Դͷ�������ͱ���
     */
    private String vfirsttype;
    /**
     * Դͷ�������ͱ���
     */
    private String vfirsttrantype;
    /**
     * Դͷ������ϸ
     */
    private String vfirstbid;
    
    public String getBillid() {
        return billid;
    }
    public void setBillid(String billid) {
        this.billid = billid;
    }
    public String getBillcode() {
        return billcode;
    }
    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getBilltype() {
        return billtype;
    }
    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }
    public String getBillbid() {
        return billbid;
    }
    public void setBillbid(String billbid) {
        this.billbid = billbid;
    }
    public String getVsrcid() {
        return vsrcid;
    }
    public void setVsrcid(String vsrcid) {
        this.vsrcid = vsrcid;
    }
    public String getVsrccode() {
        return vsrccode;
    }
    public void setVsrccode(String vsrccode) {
        this.vsrccode = vsrccode;
    }
    public String getVsrctype() {
        return vsrctype;
    }
    public void setVsrctype(String vsrctype) {
        this.vsrctype = vsrctype;
    }
    public String getVsrctrantype() {
        return vsrctrantype;
    }
    public void setVsrctrantype(String vsrctrantype) {
        this.vsrctrantype = vsrctrantype;
    }
    public String getVsrcbid() {
        return vsrcbid;
    }
    public void setVsrcbid(String vsrcbid) {
        this.vsrcbid = vsrcbid;
    }
    public String getVfirstid() {
        return vfirstid;
    }
    public void setVfirstid(String vfirstid) {
        this.vfirstid = vfirstid;
    }
    public String getVfirstcode() {
        return vfirstcode;
    }
    public void setVfirstcode(String vfirstcode) {
        this.vfirstcode = vfirstcode;
    }
    public String getVfirsttype() {
        return vfirsttype;
    }
    public void setVfirsttype(String vfirsttype) {
        this.vfirsttype = vfirsttype;
    }
    public String getVfirsttrantype() {
        return vfirsttrantype;
    }
    public void setVfirsttrantype(String vfirsttrantype) {
        this.vfirsttrantype = vfirsttrantype;
    }
    public String getVfirstbid() {
        return vfirstbid;
    }
    public void setVfirstbid(String vfirstbid) {
        this.vfirstbid = vfirstbid;
    }

}
