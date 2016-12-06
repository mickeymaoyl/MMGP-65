package nc.ui.mmgp.uif2.actions.batchedit;


import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.vo.pub.ISuperVO;
/**
 * 
 * <b> ��Ҫ�������� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since 
 * �������� 2013-6-25
 * @author tangxya
 */
public abstract class MMGPBatchUpdateContext{
    /**
     * �޸ı�ͷ
     */
    public final static int BATCH_UPDATE_HEAD=0;
    /**
     * �޸ı�ͷ
     */
    public final static int BATCH_UPDATE_BODY=1;
     /**
      * �޸ı�ͷ������
      */
    public final static int BATCH_UPDATE_All=2;
    
    
    private String pk_org;
    
    /**
     * �޸ı����ֶΣ���Ϊ���ӱ�ָ���޸ľ��嵥�ݽ�������tablecode
     */
    private String tablecode;
    /**
     * �޸ı����ֶΣ���Ϊ���ӱ�ָ���޸ľ����ĸ�����
     */
    private Class< ? extends ISuperVO> childClass;

    /**
     * ���޸ĵ�����
     */
    private Object[] datas;
    
    
    private Object userobj;
    
    
    private String funcode;
    
    private String userid;
    
    private String pk_group;
    /**
     * �Ƿ���µ����ݿ�
     */
    private boolean isPersistent=true;
    /**
     * ����ģ��ʱ��nodekey
     */
    private String nodekey;
    
    private BillManageModel model;
    
    private IMMGPBatchService  batchUpdateService;
    
   
    public String getNodekey() {
        return nodekey;
    }

    public void setNodekey(String nodekey) {
        this.nodekey = nodekey;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean isPersistent) {
        this.isPersistent = isPersistent;
    }
    
    public String getTablecode() {
        return tablecode;
    }

    public void setTablecode(String tablecode) {
        this.tablecode = tablecode;
    }

    public BillManageModel getModel() {
        return model;
    }

    public void setModel(BillManageModel model) {
        this.model = model;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPk_group() {
        return pk_group;
    }

    public void setPk_group(String pk_group) {
        this.pk_group = pk_group;
    }

    public String getFuncode() {
        return funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
    }

    public String getPk_org() {
        return pk_org;
    }

    public void setPk_org(String pk_org) {
        this.pk_org = pk_org;
    }

    public Object getUserobj() {
        return userobj;
    }

    public void setUserobj(Object userobj) {
        this.userobj = userobj;
    }

    /**
     * �޸ĵ��ݱ�ͷ���߱��������
     * @return
     */
    public int getupdatePos(){
        return BATCH_UPDATE_HEAD;
    }

    public Object[] getDatas() {
        return datas;
    }

    public void setDatas(Object[] datas) {
        this.datas = datas;
    }
    /**
     * 
            ��Ҫ�������ĵ��ֶ�key   new String []{"tneedtime", "")}
     */
    public abstract String[] getUpdatefields();
    
    /**
     * ���ķ���
     * @return
     */
    protected  IMMGPBatchService getBatchUpdateService(){
        if(batchUpdateService==null){
            batchUpdateService=new MMGPBatchUpdateService();
        }
        return batchUpdateService;
    }
    
    public void setBatchUpdateService(IMMGPBatchService batchUpdateService) {
        this.batchUpdateService = batchUpdateService;
    }

    public Class< ? extends ISuperVO> getChildClass() {
        return childClass;
    }

    public void setChildClass(Class< ? extends ISuperVO> childClass) {
        this.childClass = childClass;
    };  
}
