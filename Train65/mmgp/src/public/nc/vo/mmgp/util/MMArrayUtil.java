package nc.vo.mmgp.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * <b> 数组通用工具类 </b>
 * <p>
 * 数组通用工具类
 * </p>
 * 创建日期:2012-3-20
 * 
 * @author zhuhyc
 */
public class MMArrayUtil {
    /**
     * 是否是空数组
     * 
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 将给定的list转换为数组
     * 
     * @param clazz
     *        给定list元素类型
     * @param result
     *        给定list
     * @return list转换为数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> clazz,
                                      List<T> result) {
        T[] a = (T[]) Array.newInstance(clazz, result.size());
        return (T[]) result.toArray(a);
    }

    /**
     * 合并数组
     * 
     * @param <T>
     * @param clazz
     *        类型
     * @param array1
     *        数组1
     * @param array2
     *        数组2
     * @return 合并结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] arrayCombine(Class<T> clazz,
                                       T[] array1,
                                       T[] array2) {
        T[] combine = (T[]) Array.newInstance(clazz, array1.length + array2.length);
        for (int i = 0; i < array1.length; i++) {
            combine[i] = array1[i];
        }
        for (int i = 0; i < array2.length; i++) {
            combine[array1.length + i] = array2[i];
        }
        return combine;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] arrayCombine(T[]... arrs) {

        if (arrs == null) {
            return null;
        }
        int length = 0;
        int count = 0;
        T[] ret = null;
        for (T[] arr : arrs) {
            if (arr == null || arr.length <= 0) {
                continue;
            }
            if (ret == null) {
                ret = arr;
            }
            count++;
            length += arr.length;
        }
        if (length == 0 || count == 1 || ret == null) {
            return ret;
        }
        ret = (T[]) Array.newInstance(ret[0].getClass(), length);
        int ipos = 0;
        for (Object[] arr : arrs) {
            if (arr == null || arr.length <= 0) {
                continue;
            }
            System.arraycopy(arr, 0, ret, ipos, arr.length);
            ipos += arr.length;
        }
        return ret;

    }

    /**
     * Collection转换成数组T[]
     * 
     * @param <T>
     * @param c
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> c,
                                  Class<T> clazz) {
        if (MMCollectionUtil.isEmpty(c)) {
            return null;
        }
        T[] result = (T[]) Array.newInstance(clazz, c.size());
        return c.toArray(result);
    }

    /**
     * 将单个元素转换为数组
     * 
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(T data) {
        T[] results = (T[]) Array.newInstance(data.getClass(), 1);
        results[0] = data;
        return results;
    }
}
