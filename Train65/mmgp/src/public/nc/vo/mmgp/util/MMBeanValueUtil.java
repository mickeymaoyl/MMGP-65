package nc.vo.mmgp.util;

import java.lang.reflect.Method;

import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> 简要描述功能 </b>
 * <p>
 *     VO值的转换工具类
 * </p>
 * @since 
 * 创建日期:May 6, 2013
 * @author wangweir
 */
public class MMBeanValueUtil {

    /***
     * 对值进行转换
     * 
     * @param attributeName
     *        : vo的字段名
     * @param vo
     *        ：vo
     * @param value
     *        ：zhi
     * @return
     */
    public static Object getBeanValue(String attributeName,
                                      CircularlyAccessibleValueObject vo,
                                      Object value) {
        if (value == null) {
            return value;
        }
        // 返回值的类型
        Class< ? > valuetype = getAttributeNameType(attributeName, vo);

        if (valuetype == null) {
            return value;
        }

        try {
            if (UFDouble.class.equals(valuetype) || double.class.equals(valuetype)) {
                return switchObjToUFDouble(value);
            } else if (Integer.class.equals(valuetype) || int.class.equals(valuetype)) {
                return switchObjToInteger(value);
            } else if (UFDate.class.equals(valuetype)) {
                return switchObjToUFDate(value);
            } else if (UFDateTime.class.equals(valuetype)) {
                return switchObjToUFDateTime(value);
            } else if (UFBoolean.class.equals(valuetype) || boolean.class.equals(valuetype)) {
                return switchObjToUFBoolean(value);
            } else if (java.lang.String.class.equals(valuetype)) {
                return switchObjToString(value);
            }
        } catch (Exception e) {
            String errStr = "Failed to get property: " + attributeName;
            throw new RuntimeException(errStr, e);
        }
        return value;
    }

    /***
     * 得到vo中attributeName字段的返回值type
     * 
     * @param attributeName
     *        属性名称
     * @param vo
     *        输入的VO
     * @return 属性值
     */
    public static Class< ? > getAttributeNameType(String attributeName,
                                                  CircularlyAccessibleValueObject vo) {
        if (attributeName == null || "".equals(attributeName) || vo == null) {
            return null;
        }

        Method[] methods = BeanHelper.getInstance().getAllGetMethod(vo.getClass(), new String[]{attributeName });

        if (methods == null || methods.length != 1 || methods[0] == null) {
            return null;
        }
        return methods[0].getReturnType();
    }

    /**
     * 将VO获取的值转换为Integer
     * @param param VO值
     * @return 转换后的值
     */
    static public Integer switchObjToInteger(Object param) {
        Integer integer = null;
        if (param != null) {
            if (param instanceof Integer) integer = (Integer) param;
            else {
                String sTrimValue = param.toString().trim();
                if (sTrimValue.length() > 0) {
                    try {
                        integer = new Integer(sTrimValue);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return integer;
    }


    /**
     * 将VO获取的值转换为String
     * @param param VO值
     * @return 转换后的值
     */
    static public String switchObjToString(Object param) {
        String str = null;
        if (param != null) {
            if (param instanceof String) str = (String) param;
            else if (param instanceof DefaultConstEnum) {
                str = ((DefaultConstEnum) param).getValue().toString();
            } else
                str = param.toString().trim();

        }
        return str;
    }

    /**
     * 将VO获取的值转换为UFBoolean
     * @param param VO值
     * @return 转换后的值
     */
    static public nc.vo.pub.lang.UFBoolean switchObjToUFBoolean(Object param) {
        UFBoolean ufbool = null;
        if (param != null) {
            if (param instanceof UFBoolean) ufbool = (UFBoolean) param;
            else {
                String sTrimValue = param.toString().trim();
                if (sTrimValue.length() > 0) {
                    try {
                        ufbool = new UFBoolean(sTrimValue);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return ufbool;
    }

    /**
     * 将VO获取的值转换为UFDate
     * @param param VO值
     * @return 转换后的值
     */
    static public nc.vo.pub.lang.UFDate switchObjToUFDate(Object param) {
        UFDate ufdate = null;
        if (param != null) {
            if (param instanceof UFDate) ufdate = (UFDate) param;
            else {
                String sTrimValue = param.toString().trim();
                if (sTrimValue.length() > 0) {
                    try {
                        ufdate = new UFDate(sTrimValue, false);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return ufdate;
    }

    /**
     * 将VO获取的值转换为UFDateTime
     * @param param VO值
     * @return 转换后的值
     */
    public static UFDateTime switchObjToUFDateTime(Object param) {
        UFDateTime ufdatetime = null;
        if (param != null) {
            if (param instanceof UFDateTime) ufdatetime = (UFDateTime) param;
            else {
                String sTrimValue = param.toString().trim();
                if (sTrimValue.length() > 0) {
                    try {
                        ufdatetime = new UFDateTime(sTrimValue);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return ufdatetime;
    }

    /**
     * 将VO获取的值转换为UFDouble
     * @param param VO值
     * @return 转换后的值
     */
    static public nc.vo.pub.lang.UFDouble switchObjToUFDouble(Object param) {
        UFDouble ufd = null;
        if (param != null) {
            if (param instanceof UFDouble) ufd = (UFDouble) param;
            else {
                String sTrimValue = param.toString().trim();
                if (sTrimValue.length() > 0) {
                    try {
                        ufd = new UFDouble(sTrimValue);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return ufd;
    }

    /**
     * 将VO获取的值转换为String
     * @param param VO值
     * @return 转换后的值
     */
    static public String switchUserDefToString(Object param) {
        String str = null;
        if (param != null) {
            if (param instanceof String) str = (String) param;
            else
                str = param.toString().trim();

        }
        return str;
    }

    /**
     * 将VO获取的值转换为Integer，如果是Boolean值则“Y”对应1，其它对应0
     * @param param VO值
     * @return 转换后的值
     */
    static public Integer switchYNToInteger(Object param) {
        Integer integer = null;
        if (param != null) {
            if (param instanceof Integer) integer = (Integer) param;
            else {
                String sTrimValue = param.toString().trim();
                if (sTrimValue.length() > 0) {
                    try {
                        integer =
                                Integer.valueOf((param.toString().trim().toUpperCase().equals("Y") || param
                                    .toString()
                                    .trim()
                                    .equals("1")) ? 1 : 0);
                    } catch (Exception e) {
                    }
                }
            }
        }

        return integer;
    }
}
