/**
 * 
 */
package nc.vo.mmgp.util;

import nc.vo.pub.lang.UFDouble;

/**
 * 数值工具类
 * 
 * @author Administrator
 */
public class MMNumUtil {
    /**
     * Object转换为Integer
     * 
     * @param obj
     *        input Object
     * @return Object转换为Integer
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
     * Object转换为Long
     * 
     * @param obj
     *        input Object
     * @return Object转换为Long
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
     * Object转换为Integer，如果为空返回给定的默认值
     * 
     * @param obj
     *        input Object
     * @param defaultValue
     *        默认值
     * @return Object转换为Integer，如果为空返回给定的默认值
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
     * Object转换为UFDouble
     * 
     * @param obj
     *        input Object
     * @return Object转换为UFDouble
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
     * Object转换为Integer，如果为空返回给定的默认值
     * 
     * @param obj
     *        input Object
     * @param nullValue
     *        默认值
     * @return Object转换为Integer，如果为空返回给定的默认值
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
     * 转换UFDouble为字符串,空值转化为零长度字符串
     * 
     * @param value
     *        UFDouble 要转化的值
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
     * 转换UFDouble为字符串,空值转化为零长度字符串
     * 
     * @param value
     *        UFDouble 要转化的值
     * @param power
     *        保留小数位数 int
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
     * UFDouble非空化，如果为空返回UFDouble.ZERO_DBL
     * 
     * @param value
     *        input UFDouble
     * @return UFDouble非空化，如果为空返回UFDouble.ZERO_DBL
     */
    public static UFDouble ufDoubleNullIsZero(UFDouble value) {
        if (value == null) {
            return UFDouble.ZERO_DBL;
        }
        return value;
    }

    /***
     * 取得UFDouble类型数据取反操作 <li>当前数据为正数返回该数据的负数，负数返回当前数据的正数<li>
     * 
     * @param num
     *        input UFDouble
     * @return UFDouble类型数据取反操作
     */
    public static UFDouble getNegValue(UFDouble num) {
        if (num == null || UFDouble.ZERO_DBL.equals(num)) {
            return num;
        }
        return new UFDouble(-num.doubleValue());
    }
}
