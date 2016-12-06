package nc.vo.mmgp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> Map工具类 </b>
 * <p>
 * 主要有分组域合计两项功能
 * </p>
 * 创建日期:2011-4-25
 * 
 * @author zhaoxbc
 */
public class MMMapUtil {

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * 对输入的二位数组按照第一个字段进行分组
	 * 
	 * @param objList
	 *            列表中的对象数组个数必须大于等于2
	 * @return Map<String,List<String>> 以object[0]为键值，以object[1]的列表为键值
	 */
	public static Map<String, List<String>> getMapFromArrayList(
			List<Object[]> objList) {
		List<Map.Entry<String, String>> newObjList = new ArrayList<Map.Entry<String, String>>();
		for (Object[] obj : objList) {
			Map.Entry<String, String> entry = new MMEntry<String, String>(
					obj[0].toString(), obj[1].toString());
			newObjList.add(entry);
		}
		return groupBy(newObjList);
	}

	/**
	 * 对输入的字符串键值对按照键分组
	 * 
	 * @param objList
	 *            输入的字符串键值对
	 * @return 使用键分组后的结果
	 */
	public static Map<String, List<String>> groupBy(
			List<Map.Entry<String, String>> objList) {
		Map<String, List<String>> keyAndValuelistMap = new HashMap<String, List<String>>();
		for (Map.Entry<String, String> row : objList) {
			String key = row.getKey();
			if (!keyAndValuelistMap.containsKey(key)) {
				keyAndValuelistMap.put(key, new ArrayList<String>());
			}
			keyAndValuelistMap.get(key).add(row.getValue());
		}
		return keyAndValuelistMap;
	}

	/**
	 * 对于输入的VO 按照给定的字段分组
	 * 
	 * @param vos
	 *            输入的VO
	 * @param groupByField
	 *            给定的分组字段
	 * @return 分组后的结果
	 */
	public static <T extends CircularlyAccessibleValueObject> Map<String, List<T>> voGroupBy(
			List<T> vos, String groupByField) {
		return voGroupBy(vos, new String[] { groupByField });
	}

	/**
	 * 对VO按分组字段进行分组
	 * 
	 * @param vos
	 *            VO数组
	 * @param groupByField
	 *            分组字段
	 * @return 分组结果
	 */
	public static <T extends CircularlyAccessibleValueObject> Map<String, List<T>> voGroupBy(
			List<T> vos, String... groupByField) {
		Map<String, List<T>> keyAndValuelistMap = new HashMap<String, List<T>>();
		for (T vo : vos) {
			String key = buildGroupKey(vo, groupByField);

			List<T> aGroupList = keyAndValuelistMap.get(key);
			if (aGroupList == null) {
				aGroupList = new ArrayList<T>();
				keyAndValuelistMap.put(key, aGroupList);
			}
			aGroupList.add(vo);
		}
		return keyAndValuelistMap;
	}

	/**
	 * 对VO按分组字段合计
	 * 
	 * @param vos
	 *            VO数组
	 * @param sumField
	 *            合计字段
	 * @param groupByFields
	 *            分组字段
	 * @return 合计值
	 */
	public static <T extends CircularlyAccessibleValueObject> Map<String, UFDouble> sumVosByGroup(
			List<T> vos, String sumField, String... groupByFields) {
		Map<String, UFDouble> results = new HashMap<String, UFDouble>();
		for (T vo : vos) {
			String key = buildGroupKey(vo, groupByFields);
			UFDouble num = MMNumUtil.objToUFDouble(
					vo.getAttributeValue(sumField), UFDouble.ZERO_DBL);
			UFDouble sumnum = results.get(key);
			if (sumnum == null) {
				sumnum = UFDouble.ZERO_DBL;
			}
			results.put(key, MMComputeUtil.compute("a+b", sumnum, num));
		}

		return results;
	}

	/**
	 * create group key
	 * 
	 * @param vo
	 *            VO
	 * @param groupByFields
	 *            group key field
	 * @return group key
	 */
	private static <T extends CircularlyAccessibleValueObject> String buildGroupKey(
			T vo, String... groupByFields) {
		StringBuilder key = new StringBuilder();
		for (String field : groupByFields) {
			key.append(MMStringUtil.objectToString(vo.getAttributeValue(field)));
		}
		return key.toString();
	}

	/**
	 * 判断Map是否为空
	 * 
	 * @param map
	 *            input map
	 * @return true if the map is empty
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Map map) {
		return ((map == null) || (map.isEmpty()));
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
}
