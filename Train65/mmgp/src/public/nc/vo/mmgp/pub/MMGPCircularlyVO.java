package nc.vo.mmgp.pub;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;

/**
 * <b> 动态vo，支持动态属性的模板 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-3-22
 * 
 * @author wangweiu
 */
@SuppressWarnings("serial")
public class MMGPCircularlyVO extends CircularlyAccessibleValueObject {

    private String primaryKey = null;

    private Map<String, Object> valueMap = new HashMap<String, Object>();

    public MMGPCircularlyVO() {

    }

    @Override
    public void setPrimaryKey(String key) throws BusinessException {
        primaryKey = key;
    }

    @Override
    public String getPrimaryKey() throws BusinessException {
        return primaryKey;
    }

    public MMGPCircularlyVO(Map<String, Object> map) {
        if (map != null) {
            valueMap = map;
        }
    }

    /**
     * 简要说明
     * 
     * @see nc.vo.pub.CircularlyAccessibleValueObject#getAttributeNames()
     */
    @Override
    public String[] getAttributeNames() {
        return valueMap.keySet().toArray(new String[0]);
    }

    /**
     * 简要说明
     * 
     * @see nc.vo.pub.CircularlyAccessibleValueObject#getAttributeValue(java.lang.String)
     */
    @Override
    public Object getAttributeValue(String attributeName) {
        return valueMap.get(attributeName);
    }

    public void removeAttribute(String attributeName) {
        valueMap.remove(attributeName);
    }

    /**
     * 简要说明
     * 
     * @see nc.vo.pub.CircularlyAccessibleValueObject#setAttributeValue(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttributeValue(String name,
                                  Object value) {
        valueMap.put(name, value);

    }

    /**
     * 简要说明
     * 
     * @see nc.vo.pub.ValueObject#getEntityName()
     */
    @Override
    public String getEntityName() {
        return null;
    }

    /**
     * 简要说明
     * 
     * @see nc.vo.pub.ValueObject#validate()
     */
    @Override
    public void validate() throws ValidationException {

    }

//    public Object clone() {
//        MMGPCircularlyVO newObj = new MMGPCircularlyVO();
//        for (String key : getAttributeNames()) {
//            Object value = getAttributeValue(key);
//            newObj.setAttributeValue(key, value);
//        }
//        return newObj;
//    }
}
