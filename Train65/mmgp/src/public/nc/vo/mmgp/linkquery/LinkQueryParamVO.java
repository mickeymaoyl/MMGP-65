package nc.vo.mmgp.linkquery;

/**
 * 
 * <b> 联查参数VO </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since: 
 * 创建日期:2014-10-29
 * @author:liwsh
 */
public class LinkQueryParamVO  implements java.io.Serializable, java.lang.Cloneable {

    private static final long serialVersionUID = -8168143864605633825L;
    
    /**
     * 单据类型编码或者交易类型编码（如果没有单据类型）
     */
    private String billtype;
    
    /**
     * 单据id（查下游需要）
     */
    private String billid;
    /**
     * 单据表体bid
     */
    private String billbid;
    
    /**
     * 单据号
     */
    private String billcode;
    
    /**
     * 来源单据
     */
    private String vsrcid;
    /**
     * 来源单据号
     */
    private String vsrccode;
    /**
     * 来源单据类型编码
     */
    private String vsrctype;
    /**
     * 来源交易类型编码
     */
    private String vsrctrantype;
    /**
     * 来源单据明细
     */
    private String vsrcbid;
    
    
    /**
     * 源头单据
     */
    private String vfirstid;
    /**
     * 源头单据号
     */
    private String vfirstcode;
    /**
     * 源头单据类型编码
     */
    private String vfirsttype;
    /**
     * 源头交易类型编码
     */
    private String vfirsttrantype;
    /**
     * 源头单据明细
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
