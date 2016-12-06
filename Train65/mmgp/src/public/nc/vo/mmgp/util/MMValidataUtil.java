package nc.vo.mmgp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;

/***
 * @author xiegg
 */
public final class MMValidataUtil {

    /***
     * @param ipAddresssObject
     * @throws ValidationException
     * @deprecated
     * @see #valdateIpAddress(String)
     */
    public static void valdataqIpAddress(String ipAddresssObject) throws ValidationException {
        valdateIpAddress(ipAddresssObject);
    }

    /**
     * ip��ַУ��
     *
     * @param ipAddresssObject
     *        ip��ַ
     * @throws ValidationException
     *         ip��ַ��֤���Ϸ��쳣��Ϣ
     */
    public static void valdateIpAddress(String ipAddresssObject) throws ValidationException {
        // IP��ַ������ʽ
        String ipPattern = "^([1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(0|[1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(ipAddresssObject.toString());

        if (!matcher.find()) {
            ValidationException exp = new ValidationException();
            exp.addValidationFailure(new ValidationFailure(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0033")/*@res "ip��ַ��֤���Ϸ�!"*/));
            throw exp;
        }
    }
}