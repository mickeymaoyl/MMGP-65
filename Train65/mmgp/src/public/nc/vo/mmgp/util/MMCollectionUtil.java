package nc.vo.mmgp.util;

import java.util.Collection;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * Collection工具类
 * </p>
 * 
 * @since 创建日期 May 10, 2013
 * @author wangweir
 */
public class MMCollectionUtil {

    /**
     * true if collection is empty
     * 
     * @param collection
     *        input collection
     * @return true if collection is empty
     */
    public static boolean isEmpty(Collection< ? > collection) {
        return ((collection == null) || (collection.isEmpty()));
    }

    /**
     * true if collection is not empty
     * 
     * @param collection
     *        input collection
     * @return true if collection is not empty
     */
    public static boolean isNotEmpty(Collection< ? > collection) {
        return !isEmpty(collection);
    }
}
