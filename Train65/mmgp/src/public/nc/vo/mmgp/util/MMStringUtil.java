/**
 * 
 */
package nc.vo.mmgp.util;

import nc.vo.jcom.lang.StringUtil;

/**
 * �ַ���������
 * 
 * @author Administrator
 */
public class MMStringUtil {

    /**
     * �ж��Ƿ�ΪNC���ݿ��Ĭ�Ͽ�ֵ��"~"��
     * 
     * @param str
     *        ����ֵ
     * @return true if the input value is null or input value is "~"
     */
    public static boolean isDBStrNull(String str) {
        return isEmpty(str) || str.equals("~");
    }

    /**
     * ObjectǿתΪString,Ϊ�ջ����Ͳ�ƥ��ʱ����null
     * 
     * @param ob
     *        Object
     * @return String
     * @author ������ 2011.03.21
     */
    public static String objectToString(Object ob) {
        if (ob == null) {
            return null;
        }
        return ob.toString();
    }

    /**
     * ObjectǿתΪString,��bOptionΪtrueʱ,Ϊ�ջ����Ͳ�ƥ��ʱ���ؿ��ַ��� ��bOptionΪfalseʱ,Ϊ�ջ����Ͳ�ƥ��ʱ����null
     * 
     * @param ob
     *        Object
     * @param bOption
     *        boolean
     * @return String
     * @author ������ 2011.03.21
     */
    public static String objectToString(Object ob,
                                        boolean bOption) {

        if (bOption) {
            if (ob == null) {
                return "";
            }
            return ob.toString();
        }

        return objectToString(ob);
    }

    /**
     * String�ַ���ǿ������,����null,������������Ϊnullʱ,����null
     * 
     * @param strLeft
     *        String
     * @param strRight
     *        String
     * @return String
     * @author ������ 2011.03.21
     */
    public static String strConnectIgnoreNull(String strLeft,
                                              String strRight) {
        String ret = null;
        if (strLeft == null) {
            return strRight;
        }
        if (strRight == null) {
            return strLeft;
        }
        ret = strLeft + strRight;
        return ret;
    }

    /**
     * ת�ַ���Ϊ����,Ϊ�ջ�Ƿ�ʱ���� -1
     * 
     * @param obj
     *        String
     * @return int
     */
    public static int stringToInt(String obj) {
        int ret = -1;
        try {
            if (obj == null) {
                return ret;
            }
            ret = Integer.parseInt(obj);
        } catch (Exception e) {
            ret = -1;
        }
        return ret;
    }

    /**
     * ����ַ������ȣ���ֵΪ0
     * 
     * @param str
     *        String
     * @return int
     * @author ������ 2011.03.23
     */
    public static int getStrLength(String str) {
        if (str == null) {
            return 0;
        }
        return str.length();
    }

    /**
     * �ж�String�ַ����Ƿ�Ϊnull,���Ϊnull,�򷵻����ֵ,���򷵻�ԭֵ
     * 
     * @param toCheck
     * @param toFill
     * @return
     */
    public static String isNull(String toCheck,
                                String toFill) {
        if (toCheck == null) {
            return toFill;
        } else {
            return toCheck;
        }
    }

    /**
     * �ж��ַ����Ƿ�Ϊ��
     * 
     * @param str
     *        input String
     * @return true if the input String is null or ""
     */
    public static boolean isEmpty(String str) {
        return StringUtil.isEmpty(str);
    }

    /**
     * �ж��ַ�������ǰ��հ����Ƿ�Ϊ��
     * 
     * @param str
     *        input String
     * @return true if the input String is null or ""
     */
    public static boolean isEmptyWithTrim(String str) {
        return StringUtil.isEmptyWithTrim(str);
    }

    /**
     * �ж��ַ����Ƿ�Ϊ��Ϊ��
     * 
     * @param str
     *        input String
     * @return true if the input String is not null or ""
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * �ж���������Ƿ�Ϊ��
     * 
     * @param str
     *        input Object
     * @return true if the input Object is null or ""
     */
    public static boolean isObjectStrEmpty(Object str) {
        return StringUtil.isEmpty(objectToString(str));
    }

    /**
     * zjy 20110727 �ַ����Ƚϣ�����null�쳣
     * 
     * @param s1
     * @param s2
     * @return
     * @deprecated spell error by wangweiu
     * @see #isEquals(String,String)
     */
    public static boolean isEqauls(String s1,
                                   String s2) {
        return isEquals(s1, s2);
    }

    /**
     * wangweiu 20121203 �ַ����Ƚϣ�����null�쳣--spell
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isEquals(String s1,
                                   String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }

        if (s1 == null || s2 == null) {
            return false;
        }

        return s1.equals(s2);
    }

    public static String getPYIndexStr(String strChinese) {
        return getPYIndexStr(strChinese, true);
    }

    /**
     * ��ȡ������ֵ�ƴ�����ַ���ɵ��ַ�����
     * <p>
     * <strong>����������</strong>
     * 
     * <pre>
     * getPYIndexStr(&quot;��&quot;, true) = &quot;C&quot;;
     * getPYIndexStr(&quot;����&quot;, ture) = &quot;CX&quot;;
     * </pre>
     * 
     * @param strChinese
     *        Ҫ����ĺ����ַ���
     * @param bUpCase
     *        �������ص���ƴ�ַ��Ƿ��д��trueΪ��д��
     * @return ƴ�����ַ���ɵ��ַ���
     */
    public static String getPYIndexStr(String strChinese,
                                       boolean bUpCase) {
        return StringUtil.getPYIndexStr(strChinese, bUpCase);
    }

    /**
     * �մ�����
     * 
     * @param o
     *        Object
     * @return String
     **/
    public static String null2Empty(Object o) {
        if (o == null) {
            return "";
        }

        return o.toString();
    }
}
