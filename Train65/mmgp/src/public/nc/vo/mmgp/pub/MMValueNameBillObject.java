package nc.vo.mmgp.pub;

import java.io.Serializable;

import nc.ui.pub.bill.IBillObject;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2013-5-29
 * 
 * @author wangweiu
 */
@SuppressWarnings("serial")
public class MMValueNameBillObject implements IBillObject, Serializable {

	private Object value = null;
	private String name = null;

	public MMValueNameBillObject(Object inValue, String inName) {
		value = inValue;
		name = inName;
	}

	public Object getValue() {
		return value;
	}

	/**
	 * 简要说明
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compareTo(Object o) {
		if(value == null && o==null){
			return IBillObject.EQUAL_OPTION;
		}
		if (o == null) {
			return IBillObject.GREATTHAN_OPTION;
		}
		MMValueNameBillObject other = (MMValueNameBillObject) o;
		if (value instanceof Comparable
				&& other.getValue() instanceof Comparable) {
			if (value == null || other.getValue() == null) {
				return IBillObject.GREATTHAN_OPTION;
			}
			return ((Comparable) value).compareTo(other.getValue());
		}
		return IBillObject.GREATTHAN_OPTION;
	}

	@Override
	public String toString() {
		return MMStringUtil.objectToString(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MMValueNameBillObject)) {
			return false;

		}
		MMValueNameBillObject newObj = (MMValueNameBillObject) obj;
		if (newObj.getValue() == null) {
			return false;
		}
		return value.equals(newObj.getValue());
	}

}
