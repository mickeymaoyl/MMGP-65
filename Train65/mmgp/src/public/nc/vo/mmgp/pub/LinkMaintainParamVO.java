package nc.vo.mmgp.pub;


/**
 * 
 * <b> 联动操作的参数对象 </b>
 * <p>
 *     详细描述功能
 * </p>
 * 创建日期:2011-3-4
 * @author 李岩
 */
public class LinkMaintainParamVO {
    
    private String pk;
    private int operator=0;
    private Object objVO;
   
    public String getPk() {
        return pk;
    }
    public void setPk(String pk) {
        this.pk = pk;
    }
    public int getOperator() {
        return operator;
    }
    public void setOperator(int operator) {
        this.operator = operator;
    }
	public Object getObjVO() {
		return objVO;
	}
	public void setObjVO(Object objVO) {
		this.objVO = objVO;
	}
   
    
}
