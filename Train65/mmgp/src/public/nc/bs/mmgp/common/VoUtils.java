package nc.bs.mmgp.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;
//import nc.vo.mm.pub.FreeItemVOTookKit;
//import nc.vo.mm.pub.MMCircularlyAccessibleValueObject;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.FieldObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.VOUtil;

/**
 * VO常用工具，主要用于VO属性的反射。 采用工具形式，这样VO属性的扩展就不会受限于继承或者组合。 此工具是基于这样一个事实：VO属性需要在数据库基础上进行扩展 创建日期：(2003-4-8 12:37:43)
 * 
 * @author：王占一
 * 
 * 利用reflector实现了getAttributeNames()，
 * getAttributeValue()，setAttributeValue()和getFields()。 参数命名：所有字母小写（和数据库对应）
 * @deprecated 需要的话自己重写 不要用这个类
 */
@SuppressWarnings({"unchecked","rawtypes"})
public final class VoUtils { 
	/**
	 * VOToolkit 构造子注解。
	 */
	private VoUtils() {
		super();
	}

	/**
	 * 将参数(属性域名)转化为对应的访问方法名。 参数如果为"wlbm"，则返回对应的访问方法名"Wlbm"。
	 * 后续处理：如果是读取，则在前面加上"get"，如为设置则加上"set"。 创建日期：(2002-6-3 11:28:26)
	 * 
	 * @return java.lang.String 访问方法名
	 * @param name
	 *            java.lang.String 属性域名
	 */
	private static String convertNameToMethod(String name) {
		if (name != null) {
			String firstChar = name.substring(0, 1);
			name = firstChar.toUpperCase() + name.substring(1);
		}
		return name;

	}

	/**
	 * 将参数（域名）转化为属性名. 参数如果为"wlbm"，则返回对应的属性名"m_wlbm"。 创建日期：(2002-6-3 11:27:31)
	 * 
	 * @return java.lang.String 属性名
	 * @param name
	 *            java.lang.String 属性域名
	 */
	private static String convertNameToAttribute(String name) {
		return "m_" + name;
	}

	/**
	 * 将参数转化为域名. 参数如果为Filed如"m_wlbmField"，则返回对应的域名"wlbm" 其他情况返回null
	 * 注意：这里暗含一个规则，即VO的域名必须有其对应的field，
	 * 如有一个域名为wlbm，它将有一个对应的属性m_wlbm和一个对应的m_wlbmField，
	 * 如果缺少m_wlbmField，则认为它只是一个普通的属性，在getAttributeNames()方法 的返回值里是不包括wlbm的。
	 * 基于上述暗含的规则，可以知道此方法还有一个作用，就是判断一个属性是否field，在getFields()方法 里用到了这个功能。
	 * 创建日期：(2002-6-3 11:27:31)
	 * 
	 * @return java.lang.String 域名如"wlbm"
	 * @param name
	 *            java.lang.String
	 */

	private static String convertFieldNameToName(String fieldName) {
		// "m_?Field"至少8位
		if (fieldName.length() < 8) {
			return null;
		}
		// 必须"m_"开头
		if (!fieldName.substring(0, 2).equals("m_")) {
			return null;
		}
		// 最后5位不是"Field"返回
		if (!fieldName.substring(fieldName.length() - 5).equals("Field")) {
			return null;
		}
		return fieldName.substring(2, fieldName.length() - 5);
	}

	/**
	 * 得到在类层次结构上或者组合结构上指定类名的类。 创建日期：(2003-4-8 10:08:18)
	 * 
	 * @return java.lang.Class
	 * @param className
	 *            java.lang.String
	 */
	private static Class getClass(String className) {
		try {
			if (className != null) {
				return Class.forName(className);
			} else {
				throw new ClassNotFoundException("null");
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0099", null, new String[]{className})/*class:{0} 不存在*/);
		} catch (Exception e) {
		    ExceptionUtils.wrappException(e);
			return null;
		}
	}

	/**
	 * <p>
	 * 返回属性的域名数组，需要在一个循环中访问。
	 * 先得到所有定义的属性，如果属性为Field，解析之可得属性域名，如由m_wlbmField可知存在wlbm属性
	 * 
	 * @return java.lang.String[] 属性域名数组
	 */

	public static String[] getAttributeNames(String className) {
		java.util.Vector<String> vFields = new java.util.Vector<String>();
		try {
			// 得到所有定义的字段
			java.lang.reflect.Field[] fs = getClass(className)
					.getDeclaredFields();
			String str = null;
			for (int i = 0; i < fs.length; i++) {
				// 试着将字段转化为域名
				str = convertFieldNameToName(fs[i].getName());
				// 如果转化成功，则将结果加入属性数组
				if (str != null) {
					vFields.add(str);
				}
			}
		} catch (Exception e) {
		    ExceptionUtils.wrappException(e);
		}
		String[] results = new String[vFields.size()];
		vFields.copyInto(results);
		return results;
	}

	/**
	 *合并FieldObject数组 因为Java暂时不支持泛型，这里还要实现类似的方法mergeFieldObjectArray()。
	 */
	public static String[] mergeStringArray(String[] arr1, String[] arr2) {
		String[] result = new String[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, result, 0, arr1.length);
		System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
		return result;

	}

	/**
	 * 赋值前处理。 用于DMO里读取数据时数据类型的转换。 创建日期：(2003-4-14 13:50:02)
	 * 
	 * @return java.lang.Object
	 * @param value
	 *            java.lang.Object
	 * @param field
	 *            java.lang.reflect.Field
	 */
	private static Object beforeSetValue(Object value,
			java.lang.reflect.Field field) {
		if (field.getType() == UFDateTime.class && value != null
				&& !(value instanceof UFDateTime)) {
			value = new UFDateTime(value.toString());
		} else if (field.getType() == UFDate.class && value != null
				&& !(value instanceof UFDate)) {
			value = new UFDate(value.toString());
		} else if (field.getType() == UFDouble.class && value != null
				&& !(value instanceof UFDouble)) {
			value = new UFDouble(value.toString());
		} else if (field.getType() == UFBoolean.class && value != null
				&& !(value instanceof UFBoolean)) {
			value = new UFBoolean(value.toString());
		}
		return value;
	}

	/**
	 * <p>
	 * 根据一个属性名称字符串得到该属性的值。
	 * <p>
	 * 由参数属性域名可构造出此属性的访问方法，如由wlbm可知 访问方法名为getWlbm，然后执行此方法即可 创建日期：(2002-5-24)
	 * 
	 * @param key
	 *            java.lang.String
	 * @postcondition 如果对应的属性不存在，返回null。
	 */
	public static Object getAttributeValue(String name, Object thisClass) {
		return getAttributeValue(name, thisClass.getClass().getName(),
				thisClass);
	}

	/**
	 * <p>
	 * 根据一个属性名称字符串得到该属性的值。
	 * <p>
	 * 由参数属性域名可构造出此属性的访问方法，如由wlbm可知 访问方法名为getWlbm，然后执行此方法即可 创建日期：(2002-5-24)
	 * 
	 * @param key
	 *            java.lang.String
	 * @postcondition 如果对应的属性不存在，返回null。
	 */
	public static Object getAttributeValue(String name, String className,
			Object thisClass) {
		try {
			// 将参数即属性域名转化为对应的访问方法名
			String mmName = "get" + convertNameToMethod(name);

			// 得到访问方法映射
			java.lang.reflect.Method mm = getClass(className).getMethod(mmName,
					new Class[] {});
			if (mm == null) {
				return null;
			} else {
				// 执行
				return mm.invoke(thisClass, new Object[] {});
			}
		} catch (Exception e) {
			// 在扩展VO属性时，这里出现异常（不包括ClassCastException）是正常现象
			// e.printStackTrace();
			Logger.debug(e.getMessage());
			return null;
		}
	}

	/**
	 * 返回这个ValueObject类的所有FieldObject对象的集合。 先得到所有定义的属性，如果属性为FieldObject，
	 * 解析每个Field属性可得到FieldObject对象， 创建日期：(2002-5-24)
	 * 
	 * @return nc.vo.pub.FieldObject[]
	 */
	public static FieldObject[] getFields(String className, Object thisClass) {
		java.util.Vector<FieldObject> vFields = new java.util.Vector<FieldObject>();
		try {
			// 得到所有属性字段
			java.lang.reflect.Field[] fs = getClass(className)
					.getDeclaredFields();
			FieldObject fObj = null;
			String str = null;
			for (int i = 0; i < fs.length; i++) {
				// 判断是否FieldObject
				str = convertFieldNameToName(fs[i].getName());
				if (str != null) {
					// 如果是FieldObject对象，得到此对象
					fObj = (FieldObject) fs[i].get(thisClass);
					if (fObj == null) {
						// 如果对象为空，先构造一个对象实例
						fObj = (FieldObject) fs[i].getType().newInstance();
						// 设置该实例的域名
						fObj.setName(str);
						fObj.setLabel(null);
						// 将此实例赋给VO
						fs[i].set(thisClass, fObj);
						// 将此实例加入返回数组
						vFields.add(fObj);
					}
				} // if
			} // for
		} catch (Exception e) {
		    ExceptionUtils.wrappException(e);
			return null;
		}
		FieldObject[] results = new FieldObject[vFields.size()];
		vFields.copyInto(results);
		return results;

	}

	/**
	 * 判断对象值是否为空。 创建日期：(2003-4-17 15:48:31)
	 * 
	 * @return boolean
	 * @param object
	 *            java.lang.Object
	 */
	public static boolean isNULL(Object o) {
		return o == null || o.toString().trim().length() == 0;
	}

	/**
	 *合并FieldObject数组 因为Java暂时不支持泛型，这里还要实现类似的方法mergeStringArray()。
	 */
	public static FieldObject[] mergeFieldObjectArray(FieldObject[] arr1,
			FieldObject[] arr2) {
		FieldObject[] result = new FieldObject[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, result, 0, arr1.length);
		System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
		return result;

	}

	/**
	 * <p>
	 * 对参数name对应的属性设置值。
	 * <p>
	 * 创建日期：(2003-4-8 10:07:33)
	 * 
	 * @param name
	 *            java.lang.String
	 * @param value
	 *            java.lang.Object
	 * @param className
	 *            java.lang.String
	 * @postcondition 如果对应的属性，返回。
	 */
	public static void setAttributeValue(String name, Object value,
			String className, Object thisClass) {
		try {
			// 得到属性名
			String ffName = convertNameToAttribute(name);
			// 得到属性的定义字段
			java.lang.reflect.Field ff = getClass(className).getDeclaredField(
					ffName);
			//
			value = beforeSetValue(value, ff);
			// 得到属性的Setter方法名
			String mmName = "set" + convertNameToMethod(name);
			// 得到属性的Setter方法
			java.lang.reflect.Method mm = thisClass.getClass().getMethod(
					mmName, new Class[] { ff.getType() });
			if (mm == null) {
				return;
			}
			mm.invoke(thisClass, new Object[] { value });
		} catch (ClassCastException e) {
			throw new ClassCastException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0100", null, new String[]{name,(String)value})/*setAttributeValue方法中为 {0} 赋值时类型转换错误！（值：{1}）*/);
		} catch (Exception e) {
			// 在扩展VO属性时，这里出现异常（不包括ClassCastException）是正常现象
			// e.printStackTrace();
		}

	}

//	/**
//	 * 转换空自由项VO为null
//	 */
//	public static MMCircularlyAccessibleValueObject convertNullFreeitemVO2NULL(
//			MMCircularlyAccessibleValueObject vo) {
//		if (vo.m_freeitemvo == null) {
//			return vo;
//		}
//
//		for (int i = 0; i < FreeItemVOTookKit.FREE_COUNT; i++) {
//			String value = (String) vo.m_freeitemvo
//					.getAttributeValue(FreeItemVOTookKit.FREE_FIELD_VALUE
//							+ (i + 1));
//			if (value != null && value.trim().length() > 0) {
//				return vo;
//			}
//		}
//
//		vo.m_freeitemvo = null;
//		return vo;
//	}

	// vos:在panel上得到的VO数组（包括空VO）
	// classname：VO的名称
	// nullitemkey：如果这些字段的值都为空则认为这个VO为空VO，将被删除
	public static CircularlyAccessibleValueObject[] delNullVos(
			CircularlyAccessibleValueObject[] vos, String classname,
			String[] nullitemkey) {
		if (vos == null || vos.length == 0)
			return vos;
		if (nullitemkey == null || nullitemkey.length == 0)
			return vos;
		Vector<CircularlyAccessibleValueObject> v = new Vector<CircularlyAccessibleValueObject>();
		CircularlyAccessibleValueObject[] os = null;
		try {

			Class c1 = Class.forName(classname);
			for (int i = 0; i < vos.length; i++) {
				boolean flag = false;
				for (int j = 0; j < nullitemkey.length; j++) {
					Object o = vos[i].getAttributeValue(nullitemkey[j]);
					if (o != null && o.toString().trim().length() > 0)
						flag = true;
				}
				if (flag)
					v.addElement(vos[i]);
			}
			os = (CircularlyAccessibleValueObject[]) Array.newInstance(c1, v
					.size());
		} catch (Exception e) {
		    ExceptionUtils.wrappException(e);
		}

		v.copyInto(os);
		return os;
	}

	public static final boolean isEmpty(Object str) {
		return str == null || "".equals(str.toString());
	}

	public static final boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static final boolean isEmpty(UFDouble value) {
		return value == null || value.doubleValue() == 0.0;
	}

	public static final boolean isNotEmpty(UFDouble value) {
		return !isEmpty(value);
	}

	public static boolean isEmpty(UFDate value) {
		return value == null || value.toString().trim().length() == 0;
	}

	public static boolean isNotEmpty(UFDate value) {
		return !isEmpty(value);
	}

	public static boolean isEmpty(Object[] array) {
		return array != null && array.length > 0;
	}

	public static final int ASC = 1;

	public static final int DESC = -1;

	public static void arraySort(CircularlyAccessibleValueObject[] vos,
			final String[] fields, final int[] ascFlags, final boolean nullLast) {
		VOUtil.sort(vos, fields, ascFlags, nullLast);
	}

	public static void arraySort(CircularlyAccessibleValueObject[] vos,
			final String[] fields, final int[] ascFlags) {
		arraySort(vos, fields, ascFlags, false);
	}

	public static <T extends CircularlyAccessibleValueObject> void descSort(
			T[] vos, String[] fields) {
		if (vos == null)
			return;
		if (fields == null || fields.length == 0)
			return;
		int[] ascFlags = new int[fields.length];
		java.util.Arrays.fill(ascFlags, VOUtil.DESC);
		arraySort(vos, fields, ascFlags);
	}

	public static <T extends CircularlyAccessibleValueObject> void ascSort(
			T[] vos, String[] fields) {
		if (vos == null)
			return;
		if (fields == null || fields.length == 0)
			return;
		int[] ascFlags = new int[fields.length];
		java.util.Arrays.fill(ascFlags, VOUtil.ASC);
		arraySort(vos, fields, ascFlags);
	}

	public static <T extends CircularlyAccessibleValueObject> void listSort(
			List<T> vos, final String[] fields, final int[] ascFlags,
			final boolean nullLast) {
		if (vos == null)
			return;
		if (fields == null || fields.length == 0)
			return;
		if (ascFlags == null) {
			throw new IllegalArgumentException(
					"VOUtil.sort ascFlags cann't be null");
		} else if (fields.length != ascFlags.length) {
			throw new IllegalArgumentException(
					"VOUtil.sort length of fields not equal with that of ascFlags");
		}
		for (int i = 0; i < ascFlags.length; i++) {
			if (ascFlags[i] != ASC && ascFlags[i] != DESC)
				throw new IllegalArgumentException(
						"VOUtil.sort Illegal Value of ascFlag i=" + i
								+ " value= " + ascFlags[i]);
		}
		java.util.Comparator c = new java.util.Comparator() {
			public int compare(Object o1, Object o2) {
				CircularlyAccessibleValueObject vo1 = (CircularlyAccessibleValueObject) o1;
				CircularlyAccessibleValueObject vo2 = (CircularlyAccessibleValueObject) o2;

				int Greater = 1;
				int Less = -1;
				int Equal = 0;

				for (int i = 0; i < fields.length; i++) {
					Object v1 = vo1.getAttributeValue(fields[i]);
					Object v2 = vo2.getAttributeValue(fields[i]);

					if (v1 == null && v2 == null) {
						continue;
					}
					if (v1 == null && v2 != null)
						if (ascFlags[i] == ASC && nullLast) {
							return ascFlags[i] * Greater;
						} else {
							return ascFlags[i] * Less;
						}
					if (v1 != null && v2 == null) {
						if (ascFlags[i] == ASC && nullLast) {
							return ascFlags[i] * Less;
						} else {
							return ascFlags[i] * Greater;
						}
					}

					Comparable c1 = null;
					Comparable c2 = null;

					if (v1 instanceof Comparable && v2 instanceof Comparable) {
						c1 = (Comparable) v1;
						c2 = (Comparable) v2;
					} else if (v1 instanceof UFDouble && v2 instanceof UFDouble) {
						UFDouble u1 = (UFDouble) v1;
						UFDouble u2 = (UFDouble) v2;
						if (u1.compareTo(u2) == 0)
							continue;
						else
							return u1.compareTo(u2) * ascFlags[i];

					} else {
						c1 = ("" + v1);
						c2 = ("" + v2);
					}

					if (c1.compareTo(c2) == 0) {
						continue;
					} else {
						return c1.compareTo(c2) * ascFlags[i];
					}
				}
				return Equal;
			}
		};
		java.util.Collections.sort(vos, c);

	}

	public static <T extends CircularlyAccessibleValueObject> void listSort(
			List<T> vos, final String[] fields, final int[] ascFlags) {
		listSort(vos, fields, ascFlags, false);
	}

	public static <T extends CircularlyAccessibleValueObject> void descSort(
			List<T> vos, String[] fields) {
		if (vos == null)
			return;
		if (fields == null || fields.length == 0)
			return;
		int[] ascFlags = new int[fields.length];
		java.util.Arrays.fill(ascFlags, VOUtil.DESC);
		listSort(vos, fields, ascFlags);
	}

	public static <T extends CircularlyAccessibleValueObject> void ascSort(
			List<T> vos, String[] fields) {
		if (vos == null)
			return;
		if (fields == null || fields.length == 0)
			return;
		int[] ascFlags = new int[fields.length];
		java.util.Arrays.fill(ascFlags, VOUtil.ASC);
		listSort(vos, fields, ascFlags);
	}

	/**
	 * VO列表分组
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param voList
	 *            要分组的vo列表
	 * @param splitKey
	 *            分组关键字
	 * @return 分组后的maplist
	 */
	public static <T extends SuperVO> Map<String, List<T>> splitSuperVOList(
			List<T> voList, String splitKey) {
		Map<String, List<T>> retMap = new HashMap<String, List<T>>();
		if (voList == null || voList.isEmpty() || splitKey == null
				|| "".equals(splitKey)) {
			return retMap;
		}
		for (T vo : voList) {
			String key = MMStringUtil.objectToString(vo
					.getAttributeValue(splitKey));
			if (key == null) {
				continue;
			}
			List<T> tmpList = retMap.get(key);
			if (tmpList == null || tmpList.isEmpty()) {
				tmpList = new ArrayList<T>();
			}
			tmpList.add(vo);
			retMap.put(key, tmpList);
		}
		return retMap;
	}

	/**
	 * Map列表分组
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param mapList
	 *            要分组的Map列表
	 * @param splitKey
	 *            分组关键字
	 * @return 分组后的maplist
	 */
	public static Map<String, List<Map>> splitMapList(List<Map> mapList,
			String splitKey) {
		Map<String, List<Map>> retMap = new HashMap<String, List<Map>>();
		if (mapList == null || mapList.isEmpty() || splitKey == null
				|| "".equals(splitKey)) {
			return retMap;
		}
		for (Map map : mapList) {
			String key = MMStringUtil.objectToString(map.get(splitKey));
			if (key == null) {
				continue;
			}
			List<Map> tmpList = retMap.get(key);
			if (tmpList == null || tmpList.isEmpty()) {
				tmpList = new ArrayList<Map>();
			}
			tmpList.add(map);
			retMap.put(key, tmpList);
		}
		return retMap;
	}

	/**
	 * 将voList转换为map,键值为vo主键
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param voList
	 *            要分组的vo列表
	 * @return 分组后的map
	 */
	public static <T extends SuperVO> Map<String, T> superVOListToMap(
			List<T> voList) {
		Map<String, T> retMap = new HashMap<String, T>();
		if (voList == null || voList.isEmpty()) {
			return retMap;
		}

		for (T vo : voList) {
			String key = MMStringUtil.objectToString(vo.getAttributeValue(vo
					.getPKFieldName()));
			if (key == null) {
				continue;
			}
			retMap.put(key, vo);
		}
		return retMap;
	}

	/**
	 * 将voList转换为map
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param voList
	 *            要分组的vo列表
	 * @param splitKey
	 *            指定分组键
	 * @return 分组后的map
	 */
	public static <T extends SuperVO> Map<String, T> superVOListToMap(
			List<T> voList, String splitKey) {
		Map<String, T> retMap = new HashMap<String, T>();
		if (voList == null || voList.isEmpty()) {
			return retMap;
		}

		for (T vo : voList) {
			String key = MMStringUtil.objectToString(vo
					.getAttributeValue(splitKey));
			if (key == null) {
				continue;
			}
			retMap.put(key, vo);
		}
		return retMap;
	}

	/**
	 * 根据分组字段和分组值获得分组后的子vo列表
	 * 
	 * @param <T>
	 * @param voList
	 * @param keyName
	 * @param keyValue
	 * @return
	 */
	public static <T extends SuperVO> List<T> getChildListByKey(List<T> voList,
			String keyName, String keyValue) {
		List<T> ret = new ArrayList<T>();
		if (voList.isEmpty() || MMStringUtil.isEmpty(keyName)
				|| MMStringUtil.isEmpty(keyValue)) {
			return ret;
		}
		Map<String, List<T>> map = splitSuperVOList(voList, keyName);
		return map.get(keyValue);
	}

	public static List<String> getStringFiledValues(
			CircularlyAccessibleValueObject[] vos, String field) {
		return getFiledValues(vos, field, String.class);
	}

	public static List<String> getPrimaryFiledValues(
			CircularlyAccessibleValueObject[] vos) {
		List<String> list = new ArrayList<String>();
		for (CircularlyAccessibleValueObject vo : vos) {
			try {
				list.add(vo.getPrimaryKey());
			} catch (BusinessException e) {
				throw new IllegalArgumentException(e);
			}
		}
		return list;
	}

	public static List<UFDouble> getUFDoubleFiledValues(
			CircularlyAccessibleValueObject[] vos, String field) {
		return getFiledValues(vos, field, UFDouble.class);
	}

	public static <T> List<T> getFiledValues(
			CircularlyAccessibleValueObject[] vos, String field,
			Class<T> returnType) {
		List<T> list = new ArrayList<T>();
		for (CircularlyAccessibleValueObject vo : vos) {
			list.add((T) getAttributeValue(field, vo));
		}
		return list;
	}

}
