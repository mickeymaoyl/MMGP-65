package nc.ui.mmgp.pub.beans.digraph.model;

import nc.vo.pub.SuperVO;

public interface IGraph {
	public String getOid();
	public void setOid(String oid);
	public String getPrimaryKey();
	public void setPrimaryKey(String key);
	public void setStatus(int status);
	public int getStatus();
	public void setAttributeValue(String attributeName, Object value);
	public String getCode();
	public String getName();
	public void setCode(String code);
	public void setName(String name);
	public SuperVO getSuperVO();
	public SuperVO getWrappedVO();
//	public void updateDigraphModel(UIDigraph uiDigraph);
}
