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
 * <b> ��Ҫ�������� </b>
 * <p>
 *     VOֵ��ת��������
 * </p>
 * @since 
 * ��������:May 6, 2013
 * @author wangweir
 */
public class MMBeanValueUtil {

    /***
     * ��ֵ����ת��
     * 
     * @param attributeName
     *        : vo���ֶ���
     * @param vo
     *        ��vo
     * @param value
     *        ��zhi
     * @return
     */
    public static Object getBeanValue(String attributeName,
                                      CircularlyAccessibleValueObject vo,
                                      Object value) {
        if (value == null) {
            return value;
        }
        // ����ֵ������
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
     * �õ�vo��attributeName�ֶεķ���ֵtype
     * 
     * @param attributeName
     *        ��������
     * @param vo
     *        �����VO
     * @return ����ֵ
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
     * ��VO��ȡ��ֵת��ΪInteger
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪString
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪUFBoolean
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪUFDate
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪUFDateTime
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪUFDouble
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪString
     * @param param VOֵ
     * @return ת�����ֵ
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
     * ��VO��ȡ��ֵת��ΪInteger�������Booleanֵ��Y����Ӧ1��������Ӧ0
     * @param param VOֵ
     * @return ת�����ֵ
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
