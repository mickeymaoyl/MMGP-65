package nc.vo.mmgp.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * <b> ����ͨ�ù����� </b>
 * <p>
 * ����ͨ�ù�����
 * </p>
 * ��������:2012-3-20
 * 
 * @author zhuhyc
 */
public class MMArrayUtil {
    /**
     * �Ƿ��ǿ�����
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
     * ��������listת��Ϊ����
     * 
     * @param clazz
     *        ����listԪ������
     * @param result
     *        ����list
     * @return listת��Ϊ����
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> clazz,
                                      List<T> result) {
        T[] a = (T[]) Array.newInstance(clazz, result.size());
        return (T[]) result.toArray(a);
    }

    /**
     * �ϲ�����
     * 
     * @param <T>
     * @param clazz
     *        ����
     * @param array1
     *        ����1
     * @param array2
     *        ����2
     * @return �ϲ����
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
     * Collectionת��������T[]
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
     * ������Ԫ��ת��Ϊ����
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
