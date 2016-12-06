package nc.vo.mmgp.util;

import java.util.Collection;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * Collection������
 * </p>
 * 
 * @since �������� May 10, 2013
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
