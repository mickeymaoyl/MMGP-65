package nc.impl.mmgp.linkquery;

import java.util.Map;

import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> ���鹤���� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:2014-11-1
 * @author:liwsh
 */
public class MMGPLinkQueryUtil {

    /**
     * ����classʵ����һ������
     * 
     * @param billBodyClass
     * @return
     */
    public static SuperVO newInstanceVO(Class< ? extends SuperVO> billBodyClass) {

        try {
            return billBodyClass.newInstance();
        } catch (InstantiationException e) {
            ExceptionUtils.wrappException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtils.wrappException(e);
        }

        return null;
    }

    /**
     * ����classname����class
     * 
     * @param classname
     * @return
     */
    public static Class< ? extends SuperVO> getClass(String classname) {

        try {
            return (Class< ? extends SuperVO>) Class.forName(classname);
        } catch (ClassNotFoundException e) {
            ExceptionUtils.wrappException(e);
        }

        return null;
    }

    /**
     * ���ƶ�key��ֵ��map���Ƴ�
     * 
     * @param map
     *        map
     * @param keys
     *        key����
     */
    public static void removeDataFromMap(Map<String, SuperVO> map,
                                         String[] keys) {

        if (MMArrayUtil.isEmpty(keys)) {
            return;
        }
        for (String key : keys) {
            map.remove(key);
        }
    }

}
