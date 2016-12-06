/**
 * 
 */
package nc.vo.mmgp.util;

import nc.vo.pub.lang.UFDouble;

/**
 * ��ֵ������
 * 
 * @author Administrator
 */
public class MMNumUtil {
    /**
     * Objectת��ΪInteger
     * 
     * @param obj
     *        input Object
     * @return Objectת��ΪInteger
     */
    public static Integer objectToInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return Integer.parseInt(obj.toString());
    }

    /**
     * Objectת��ΪLong
     * 
     * @param obj
     *        input Object
     * @return Objectת��ΪLong
     */
    public static Long objectToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        return Long.parseLong(obj.toString());
    }

    /**
     * Objectת��ΪInteger�����Ϊ�շ��ظ�����Ĭ��ֵ
     * 
     * @param obj
     *        input Object
     * @param defaultValue
     *        Ĭ��ֵ
     * @return Objectת��ΪInteger�����Ϊ�շ��ظ�����Ĭ��ֵ
     */
    public static Integer objectToInteger(Object obj,
                                          Integer defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return Integer.parseInt(obj.toString());
    }

    /**
     * Objectת��ΪUFDouble
     * 
     * @param obj
     *        input Object
     * @return Objectת��ΪUFDouble
     */
    public static UFDouble objToUFDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof UFDouble) {
            return (UFDouble) obj;
        }
        return new UFDouble(obj.toString());
    }

    /**
     * Objectת��ΪInteger�����Ϊ�շ��ظ�����Ĭ��ֵ
     * 
     * @param obj
     *        input Object
     * @param nullValue
     *        Ĭ��ֵ
     * @return Objectת��ΪInteger�����Ϊ�շ��ظ�����Ĭ��ֵ
     */
    public static UFDouble objToUFDouble(Object obj,
                                         UFDouble nullValue) {
        if (obj == null || obj.toString().length() == 0) {
            return nullValue;
        }
        if (obj instanceof UFDouble) {
            return (UFDouble) obj;
        }
        return new UFDouble(obj.toString());
    }

    /**
     * ת��UFDoubleΪ�ַ���,��ֵת��Ϊ�㳤���ַ���
     * 
     * @param value
     *        UFDouble Ҫת����ֵ
     * @return String
     */
    public static String ufDoubleToString(UFDouble value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    /**
     * ת��UFDoubleΪ�ַ���,��ֵת��Ϊ�㳤���ַ���
     * 
     * @param value
     *        UFDouble Ҫת����ֵ
     * @param power
     *        ����С��λ�� int
     * @return UFDouble
     */
    public static String ufDoubleToString(UFDouble value,
                                          int power) {
        if (value == null) {
            return "";
        } else {
            value = value.setScale(power, UFDouble.ROUND_HALF_UP);
            return value.toString();
        }
    }

    /**
     * UFDouble�ǿջ������Ϊ�շ���UFDouble.ZERO_DBL
     * 
     * @param value
     *        input UFDouble
     * @return UFDouble�ǿջ������Ϊ�շ���UFDouble.ZERO_DBL
     */
    public static UFDouble ufDoubleNullIsZero(UFDouble value) {
        if (value == null) {
            return UFDouble.ZERO_DBL;
        }
        return value;
    }

    /***
     * ȡ��UFDouble��������ȡ������ <li>��ǰ����Ϊ�������ظ����ݵĸ������������ص�ǰ���ݵ�����<li>
     * 
     * @param num
     *        input UFDouble
     * @return UFDouble��������ȡ������
     */
    public static UFDouble getNegValue(UFDouble num) {
        if (num == null || UFDouble.ZERO_DBL.equals(num)) {
            return num;
        }
        return new UFDouble(-num.doubleValue());
    }
}
