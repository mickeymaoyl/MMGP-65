/**
 * 
 */
package nc.vo.mmgp.util;

import nc.vo.jcom.lang.StringUtil;

/**
 * 字符串工具类
 * 
 * @author Administrator
 */
public class MMStringUtil {

    /**
     * 判断是否为NC数据库的默认空值（"~"）
     * 
     * @param str
     *        输入值
     * @return true if the input value is null or input value is "~"
     */
    public static boolean isDBStrNull(String str) {
        return isEmpty(str) || str.equals("~");
    }

    /**
     * Object强转为String,为空或类型不匹配时返回null
     * 
     * @param ob
     *        Object
     * @return String
     * @author 代国勇 2011.03.21
     */
    public static String objectToString(Object ob) {
        if (ob == null) {
            return null;
        }
        return ob.toString();
    }

    /**
     * Object强转为String,当bOption为true时,为空或类型不匹配时返回空字符串 当bOption为false时,为空或类型不匹配时返回null
     * 
     * @param ob
     *        Object
     * @param bOption
     *        boolean
     * @return String
     * @author 代国勇 2011.03.21
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
     * String字符串强制连接,忽略null,当两个参数都为null时,返回null
     * 
     * @param strLeft
     *        String
     * @param strRight
     *        String
     * @return String
     * @author 代国勇 2011.03.21
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
     * 转字符串为整数,为空或非法时返回 -1
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
     * 获得字符串长度，空值为0
     * 
     * @param str
     *        String
     * @return int
     * @author 代国勇 2011.03.23
     */
    public static int getStrLength(String str) {
        if (str == null) {
            return 0;
        }
        return str.length();
    }

    /**
     * 判断String字符串是否为null,如果为null,则返回填充值,否则返回原值
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
     * 判断字符串是否为空
     * 
     * @param str
     *        input String
     * @return true if the input String is null or ""
     */
    public static boolean isEmpty(String str) {
        return StringUtil.isEmpty(str);
    }

    /**
     * 判断字符串忽略前后空包后是否为空
     * 
     * @param str
     *        input String
     * @return true if the input String is null or ""
     */
    public static boolean isEmptyWithTrim(String str) {
        return StringUtil.isEmptyWithTrim(str);
    }

    /**
     * 判断字符串是否为不为空
     * 
     * @param str
     *        input String
     * @return true if the input String is not null or ""
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * 判断输入对象是否为空
     * 
     * @param str
     *        input Object
     * @return true if the input Object is null or ""
     */
    public static boolean isObjectStrEmpty(Object str) {
        return StringUtil.isEmpty(objectToString(str));
    }

    /**
     * zjy 20110727 字符串比较，避免null异常
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
     * wangweiu 20121203 字符串比较，避免null异常--spell
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
     * 获取多个汉字的拼音首字符组成的字符串。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * getPYIndexStr(&quot;程&quot;, true) = &quot;C&quot;;
     * getPYIndexStr(&quot;程序&quot;, ture) = &quot;CX&quot;;
     * </pre>
     * 
     * @param strChinese
     *        要处理的汉字字符串
     * @param bUpCase
     *        决定返回的首拼字符是否大写，true为大写。
     * @return 拼音首字符组成的字符串
     */
    public static String getPYIndexStr(String strChinese,
                                       boolean bUpCase) {
        return StringUtil.getPYIndexStr(strChinese, bUpCase);
    }

    /**
     * 空串处理
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
