package nc.vo.mmgp.pub;


/**
 * 
 * <b> ���������Ĳ������� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * ��������:2011-3-4
 * @author ����
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
