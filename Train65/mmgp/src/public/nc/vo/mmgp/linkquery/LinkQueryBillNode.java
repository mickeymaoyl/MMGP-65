package nc.vo.mmgp.linkquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.util.mmf.framework.base.MMCollectionUtil;
import nc.util.mmf.framework.base.MMStringUtil;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMGPMapList;

/**
 * 
 * <b> 用于递归查询下游单据 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since: 
 * 创建日期:Nov 4, 2014
 * @author:gaotx
 */
public class LinkQueryBillNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 689647088689161914L;

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
     * 真实的单据表体ID（主要用于解决完工报告特殊问题）
     */
    private String realbillbid;

    /**
     * 单据号
     */
    private String billcode;
    
    /**
     * 来源单据
     */
    private String vsrcid;
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
     * 父项
     */
    private LinkQueryBillNode parentNode;
    
    
    /**
     * 下游单据<下游单据类型，下游单据List>
     */
    private MMGPMapList<String, LinkQueryBillNode> fowardBills = new MMGPMapList<String, LinkQueryBillNode>();
    
    /**
     * 将paramVO转换为Node
     * @param params
     * @return
     */
    public static List<LinkQueryBillNode> convertFromLinkQueryParamVOs(List<LinkQueryParamVO> params){
        List<LinkQueryBillNode> rtnList = new ArrayList<LinkQueryBillNode>();
        if(MMCollectionUtil.isEmpty(params)){
            return rtnList;
        }
        for(LinkQueryParamVO aParam : params){
            LinkQueryBillNode aNode = new LinkQueryBillNode();
            aNode.setBillid(aParam.getBillid());
            aNode.setBillbid(aParam.getBillbid());
            aNode.setBillcode(aParam.getBillcode());
            aNode.setBilltype(aParam.getBilltype());
            rtnList.add(aNode);
        }
        return rtnList;
    }
    
    /**
     * 添加下游单据
     * @param node
     */
    public void addForwardBill(LinkQueryBillNode node){
        this.fowardBills.put(node.getBilltype(), node);
    }
    
    /**
     * 批量添加下游单据
     * @param nodeList
     */
    public void addAllForwardBills(List<LinkQueryBillNode> nodeList){
        if(MMCollectionUtil.isEmpty(nodeList)){
            return;
        }
        for(LinkQueryBillNode aNode : nodeList){
            this.fowardBills.put(aNode.getBilltype(), aNode);
        }
    }
    
    /**
     * 节点唯一标识 单据类型+单据ID+单据表体ID
     * @return
     */
    public String buildUniqueKey(){
        // 完工报告特殊处理
        if(this.billtype.equals(MMGlobalConst.BILL_TYPE_WR)){
            return this.billtype + this.billid + this.realbillbid;
        }
        if(MMStringUtil.isEmpty(this.billbid)){
            return this.billtype + this.billid + this.billid;
        }
        return this.billtype + this.billid + this.billbid;
    }
    
    /**
     * 节点来源key，用于匹配
     * @return
     */
    public String buildSrcUniqueKey(){
        if(MMStringUtil.isEmpty(this.vsrcbid)){
            return this.vsrctype + this.vsrcid + this.vsrcid;
        }
        return this.vsrctype + this.vsrcid + this.vsrcbid;
    }
    
    /**
     * 根据指定单据类型获取所有下游单据
     * @param billType
     * @return
     */
    public List<LinkQueryBillNode> getNodesByBillType(String billType){
        List<LinkQueryBillNode> rtnList = new ArrayList<LinkQueryBillNode>();
        if(MMCollectionUtil.isEmpty(this.fowardBills.values())){
            return rtnList;
        }
        List<LinkQueryBillNode> searchList = new ArrayList<LinkQueryBillNode>();
        searchList.addAll(this.fowardBills.values());
        while(searchList.size() > 0){
            LinkQueryBillNode aNode = searchList.remove(0);
            if(MMCollectionUtil.isNotEmpty(aNode.getFowardBills().values())){
                searchList.addAll(aNode.getFowardBills().values());
            }
            if(aNode.getBilltype().equals(billType)){
                rtnList.add(aNode);
            }
        }
        return rtnList;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getBillbid() {
        return billbid;
    }

    public void setBillbid(String billbid) {
        this.billbid = billbid;
    }

    public String getBillcode() {
        return billcode;
    }

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public MMGPMapList<String, LinkQueryBillNode> getFowardBills() {
        return fowardBills;
    }

    public void setFowardBills(MMGPMapList<String, LinkQueryBillNode> fowardBills) {
        this.fowardBills = fowardBills;
    }

    public String getVsrcid() {
        return vsrcid;
    }

    public void setVsrcid(String vsrcid) {
        this.vsrcid = vsrcid;
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

    public LinkQueryBillNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(LinkQueryBillNode parentNode) {
        this.parentNode = parentNode;
    }

    public String getRealbillbid() {
        return realbillbid;
    }

    public void setRealbillbid(String realbillbid) {
        this.realbillbid = realbillbid;
    }
}
