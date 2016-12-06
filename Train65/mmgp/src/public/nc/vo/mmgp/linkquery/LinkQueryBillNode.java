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
 * <b> ���ڵݹ��ѯ���ε��� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since: 
 * ��������:Nov 4, 2014
 * @author:gaotx
 */
public class LinkQueryBillNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 689647088689161914L;

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
     * ��ʵ�ĵ��ݱ���ID����Ҫ���ڽ���깤�����������⣩
     */
    private String realbillbid;

    /**
     * ���ݺ�
     */
    private String billcode;
    
    /**
     * ��Դ����
     */
    private String vsrcid;
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
     * ����
     */
    private LinkQueryBillNode parentNode;
    
    
    /**
     * ���ε���<���ε������ͣ����ε���List>
     */
    private MMGPMapList<String, LinkQueryBillNode> fowardBills = new MMGPMapList<String, LinkQueryBillNode>();
    
    /**
     * ��paramVOת��ΪNode
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
     * ������ε���
     * @param node
     */
    public void addForwardBill(LinkQueryBillNode node){
        this.fowardBills.put(node.getBilltype(), node);
    }
    
    /**
     * ����������ε���
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
     * �ڵ�Ψһ��ʶ ��������+����ID+���ݱ���ID
     * @return
     */
    public String buildUniqueKey(){
        // �깤�������⴦��
        if(this.billtype.equals(MMGlobalConst.BILL_TYPE_WR)){
            return this.billtype + this.billid + this.realbillbid;
        }
        if(MMStringUtil.isEmpty(this.billbid)){
            return this.billtype + this.billid + this.billid;
        }
        return this.billtype + this.billid + this.billbid;
    }
    
    /**
     * �ڵ���Դkey������ƥ��
     * @return
     */
    public String buildSrcUniqueKey(){
        if(MMStringUtil.isEmpty(this.vsrcbid)){
            return this.vsrctype + this.vsrcid + this.vsrcid;
        }
        return this.vsrctype + this.vsrcid + this.vsrcbid;
    }
    
    /**
     * ����ָ���������ͻ�ȡ�������ε���
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
