package nc.vo.mmgp.util;

import java.util.List;

/**
 * <b> 简要描述功能 </b>
 * <p>
 *     List工具类
 * </p>
 * @since 
 * 创建日期 May 10, 2013
 * @author wangweir
 */
public class MMListUtil {
    
	/**
	 * 判断list是否为空。建议直接使用MMCollectionUtil.isEmpty
	 * @param list input List
	 * @return true if list is empty
	 */
	public static boolean isEmpty(List<?> list) {
		return MMCollectionUtil.isEmpty(list);
	}
}
