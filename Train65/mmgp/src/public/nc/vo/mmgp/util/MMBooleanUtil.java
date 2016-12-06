package nc.vo.mmgp.util;

import nc.vo.pub.lang.UFBoolean;

/**
 * <b> ���������� </b>
 * <p>
 * UFBooleanתΪjava.lang.Boolean
 * </p>
 * ��������:2011-6-20
 * 
 * @author Administrator
 */
public class MMBooleanUtil {

    /**
     * UFBoolean תBoolean
     * @param ufBoolean UFBoolean
     * @return boolean value
     */
    public static boolean booleanValue(UFBoolean ufBoolean) {
        return null == ufBoolean ? false : ufBoolean.booleanValue();
    }

    /**
     * Object ת boolean
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
     * Object ת UFBoolean
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
