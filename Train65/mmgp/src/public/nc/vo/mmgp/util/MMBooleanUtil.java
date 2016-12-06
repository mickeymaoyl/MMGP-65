package nc.vo.mmgp.util;

import nc.vo.pub.lang.UFBoolean;

/**
 * <b> 布尔工具类 </b>
 * <p>
 * UFBoolean转为java.lang.Boolean
 * </p>
 * 创建日期:2011-6-20
 * 
 * @author Administrator
 */
public class MMBooleanUtil {

    /**
     * UFBoolean 转Boolean
     * @param ufBoolean UFBoolean
     * @return boolean value
     */
    public static boolean booleanValue(UFBoolean ufBoolean) {
        return null == ufBoolean ? false : ufBoolean.booleanValue();
    }

    /**
     * Object 转 boolean
     * @param obj Object
     * @return boolean value
     */
    public static boolean objToBooleanValue(Object obj) {
        if (obj == null) {
            return false;
        }
        return booleanValue(new UFBoolean(obj.toString()));
    }

    /**
     * Object 转 UFBoolean
     * @param obj Object
     * @return UFBoolean
     */
    public static UFBoolean objToUFBoolean(Object obj) {

        if (null == obj) {
            return UFBoolean.FALSE;
        }
        return new UFBoolean(obj.toString());
    }
}
