package nc.vo.mmgp.util;

import java.util.List;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 *     List������
 * </p>
 * @since 
 * �������� May 10, 2013
 * @author wangweir
 */
public class MMListUtil {
    
	/**
	 * �ж�list�Ƿ�Ϊ�ա�����ֱ��ʹ��MMCollectionUtil.isEmpty
	 * @param list input List
	 * @return true if list is empty
	 */
	public static boolean isEmpty(List<?> list) {
		return MMCollectionUtil.isEmpty(list);
	}
}
