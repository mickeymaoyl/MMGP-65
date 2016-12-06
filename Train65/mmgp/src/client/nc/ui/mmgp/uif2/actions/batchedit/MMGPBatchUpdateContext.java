package nc.ui.mmgp.uif2.actions.batchedit;


import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.vo.pub.ISuperVO;
/**
 * 
 * <b> 简要描述功能 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since 
 * 创建日期 2013-6-25
 * @author tangxya
 */
public abstract class MMGPBatchUpdateContext{
    /**
     * 修改表头
     */
    public final static int BATCH_UPDATE_HEAD=0;
    /**
     * 修改表头
     */
    public final static int BATCH_UPDATE_BODY=1;
     /**
      * 修改表头、表体
      */
    public final static int BATCH_UPDATE_All=2;
    
    
    private String pk_org;
    
    /**
     * 修改表体字段，若为多子表，指明修改具体单据界面表体的tablecode
     */
    private String tablecode;
    /**
     * 修改表体字段，若为多子表，指明修改具体哪个表体
     */
    private Class< ? extends ISuperVO> childClass;

    /**
     * 待修改的数据
     */
    private Object[] datas;
    
    
    private Object userobj;
    
    
    private String funcode;
    
    private String userid;
    
    private String pk_group;
    /**
     * 是否更新到数据库
     */
    private boolean isPersistent=true;
    /**
     * 加载模板时的nodekey
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
     * 修改单据表头或者表体的数据
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
            需要进行批改的字段key   new String []{"tneedtime", "")}
     */
    public abstract String[] getUpdatefields();
    
    /**
     * 批改服务
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
