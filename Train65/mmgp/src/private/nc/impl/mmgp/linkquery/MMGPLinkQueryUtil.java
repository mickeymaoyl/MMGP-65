package nc.impl.mmgp.linkquery;

import java.util.Map;

import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 联查工具类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:2014-11-1
 * @author:liwsh
 */
public class MMGPLinkQueryUtil {

    /**
     * 根据class实例化一个对象
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
     * 根据classname构造class
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
     * 将制定key的值从map中移除
     * 
     * @param map
     *        map
     * @param keys
     *        key数组
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
