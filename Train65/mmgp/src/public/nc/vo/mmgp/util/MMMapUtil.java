package nc.vo.mmgp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> Map������ </b>
 * <p>
 * ��Ҫ�з�����ϼ������
 * </p>
 * ��������:2011-4-25
 * 
 * @author zhaoxbc
 */
public class MMMapUtil {

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * ������Ķ�λ���鰴�յ�һ���ֶν��з���
	 * 
	 * @param objList
	 *            �б��еĶ����������������ڵ���2
	 * @return Map<String,List<String>> ��object[0]Ϊ��ֵ����object[1]���б�Ϊ��ֵ
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
	 * ��������ַ�����ֵ�԰��ռ�����
	 * 
	 * @param objList
	 *            ������ַ�����ֵ��
	 * @return ʹ�ü������Ľ��
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
	 * ���������VO ���ո������ֶη���
	 * 
	 * @param vos
	 *            �����VO
	 * @param groupByField
	 *            �����ķ����ֶ�
	 * @return �����Ľ��
	 */
	public static <T extends CircularlyAccessibleValueObject> Map<String, List<T>> voGroupBy(
			List<T> vos, String groupByField) {
		return voGroupBy(vos, new String[] { groupByField });
	}

	/**
	 * ��VO�������ֶν��з���
	 * 
	 * @param vos
	 *            VO����
	 * @param groupByField
	 *            �����ֶ�
	 * @return ������
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
	 * ��VO�������ֶκϼ�
	 * 
	 * @param vos
	 *            VO����
	 * @param sumField
	 *            �ϼ��ֶ�
	 * @param groupByFields
	 *            �����ֶ�
	 * @return �ϼ�ֵ
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
	 * �ж�Map�Ƿ�Ϊ��
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
