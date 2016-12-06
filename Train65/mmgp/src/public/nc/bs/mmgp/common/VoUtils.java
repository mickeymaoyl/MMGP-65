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
 * VO���ù��ߣ���Ҫ����VO���Եķ��䡣 ���ù�����ʽ������VO���Ե���չ�Ͳ��������ڼ̳л�����ϡ� �˹����ǻ�������һ����ʵ��VO������Ҫ�����ݿ�����Ͻ�����չ �������ڣ�(2003-4-8 12:37:43)
 * 
 * @author����ռһ
 * 
 * ����reflectorʵ����getAttributeNames()��
 * getAttributeValue()��setAttributeValue()��getFields()�� ����������������ĸСд�������ݿ��Ӧ��
 * @deprecated ��Ҫ�Ļ��Լ���д ��Ҫ�������
 */
@SuppressWarnings({"unchecked","rawtypes"})
public final class VoUtils { 
	/**
	 * VOToolkit ������ע�⡣
	 */
	private VoUtils() {
		super();
	}

	/**
	 * ������(��������)ת��Ϊ��Ӧ�ķ��ʷ������� �������Ϊ"wlbm"���򷵻ض�Ӧ�ķ��ʷ�����"Wlbm"��
	 * ������������Ƕ�ȡ������ǰ�����"get"����Ϊ���������"set"�� �������ڣ�(2002-6-3 11:28:26)
	 * 
	 * @return java.lang.String ���ʷ�����
	 * @param name
	 *            java.lang.String ��������
	 */
	private static String convertNameToMethod(String name) {
		if (name != null) {
			String firstChar = name.substring(0, 1);
			name = firstChar.toUpperCase() + name.substring(1);
		}
		return name;

	}

	/**
	 * ��������������ת��Ϊ������. �������Ϊ"wlbm"���򷵻ض�Ӧ��������"m_wlbm"�� �������ڣ�(2002-6-3 11:27:31)
	 * 
	 * @return java.lang.String ������
	 * @param name
	 *            java.lang.String ��������
	 */
	private static String convertNameToAttribute(String name) {
		return "m_" + name;
	}

	/**
	 * ������ת��Ϊ����. �������ΪFiled��"m_wlbmField"���򷵻ض�Ӧ������"wlbm" �����������null
	 * ע�⣺���ﰵ��һ�����򣬼�VO���������������Ӧ��field��
	 * ����һ������Ϊwlbm��������һ����Ӧ������m_wlbm��һ����Ӧ��m_wlbmField��
	 * ���ȱ��m_wlbmField������Ϊ��ֻ��һ����ͨ�����ԣ���getAttributeNames()���� �ķ���ֵ���ǲ�����wlbm�ġ�
	 * �������������Ĺ��򣬿���֪���˷�������һ�����ã������ж�һ�������Ƿ�field����getFields()���� ���õ���������ܡ�
	 * �������ڣ�(2002-6-3 11:27:31)
	 * 
	 * @return java.lang.String ������"wlbm"
	 * @param name
	 *            java.lang.String
	 */

	private static String convertFieldNameToName(String fieldName) {
		// "m_?Field"����8λ
		if (fieldName.length() < 8) {
			return null;
		}
		// ����"m_"��ͷ
		if (!fieldName.substring(0, 2).equals("m_")) {
			return null;
		}
		// ���5λ����"Field"����
		if (!fieldName.substring(fieldName.length() - 5).equals("Field")) {
			return null;
		}
		return fieldName.substring(2, fieldName.length() - 5);
	}

	/**
	 * �õ������νṹ�ϻ�����Ͻṹ��ָ���������ࡣ �������ڣ�(2003-4-8 10:08:18)
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
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0099", null, new String[]{className})/*class:{0} ������*/);
		} catch (Exception e) {
		    ExceptionUtils.wrappException(e);
			return null;
		}
	}

	/**
	 * <p>
	 * �������Ե��������飬��Ҫ��һ��ѭ���з��ʡ�
	 * �ȵõ����ж�������ԣ��������ΪField������֮�ɵ���������������m_wlbmField��֪����wlbm����
	 * 
	 * @return java.lang.String[] ������������
	 */

	public static String[] getAttributeNames(String className) {
		java.util.Vector<String> vFields = new java.util.Vector<String>();
		try {
			// �õ����ж�����ֶ�
			java.lang.reflect.Field[] fs = getClass(className)
					.getDeclaredFields();
			String str = null;
			for (int i = 0; i < fs.length; i++) {
				// ���Ž��ֶ�ת��Ϊ����
				str = convertFieldNameToName(fs[i].getName());
				// ���ת���ɹ����򽫽��������������
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
	 *�ϲ�FieldObject���� ��ΪJava��ʱ��֧�ַ��ͣ����ﻹҪʵ�����Ƶķ���mergeFieldObjectArray()��
	 */
	public static String[] mergeStringArray(String[] arr1, String[] arr2) {
		String[] result = new String[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, result, 0, arr1.length);
		System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
		return result;

	}

	/**
	 * ��ֵǰ���� ����DMO���ȡ����ʱ�������͵�ת���� �������ڣ�(2003-4-14 13:50:02)
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
	 * ����һ�����������ַ����õ������Ե�ֵ��
	 * <p>
	 * �ɲ������������ɹ���������Եķ��ʷ���������wlbm��֪ ���ʷ�����ΪgetWlbm��Ȼ��ִ�д˷������� �������ڣ�(2002-5-24)
	 * 
	 * @param key
	 *            java.lang.String
	 * @postcondition �����Ӧ�����Բ����ڣ�����null��
	 */
	public static Object getAttributeValue(String name, Object thisClass) {
		return getAttributeValue(name, thisClass.getClass().getName(),
				thisClass);
	}

	/**
	 * <p>
	 * ����һ�����������ַ����õ������Ե�ֵ��
	 * <p>
	 * �ɲ������������ɹ���������Եķ��ʷ���������wlbm��֪ ���ʷ�����ΪgetWlbm��Ȼ��ִ�д˷������� �������ڣ�(2002-5-24)
	 * 
	 * @param key
	 *            java.lang.String
	 * @postcondition �����Ӧ�����Բ����ڣ�����null��
	 */
	public static Object getAttributeValue(String name, String className,
			Object thisClass) {
		try {
			// ����������������ת��Ϊ��Ӧ�ķ��ʷ�����
			String mmName = "get" + convertNameToMethod(name);

			// �õ����ʷ���ӳ��
			java.lang.reflect.Method mm = getClass(className).getMethod(mmName,
					new Class[] {});
			if (mm == null) {
				return null;
			} else {
				// ִ��
				return mm.invoke(thisClass, new Object[] {});
			}
		} catch (Exception e) {
			// ����չVO����ʱ����������쳣��������ClassCastException������������
			// e.printStackTrace();
			Logger.debug(e.getMessage());
			return null;
		}
	}

	/**
	 * �������ValueObject�������FieldObject����ļ��ϡ� �ȵõ����ж�������ԣ��������ΪFieldObject��
	 * ����ÿ��Field���Կɵõ�FieldObject���� �������ڣ�(2002-5-24)
	 * 
	 * @return nc.vo.pub.FieldObject[]
	 */
	public static FieldObject[] getFields(String className, Object thisClass) {
		java.util.Vector<FieldObject> vFields = new java.util.Vector<FieldObject>();
		try {
			// �õ����������ֶ�
			java.lang.reflect.Field[] fs = getClass(className)
					.getDeclaredFields();
			FieldObject fObj = null;
			String str = null;
			for (int i = 0; i < fs.length; i++) {
				// �ж��Ƿ�FieldObject
				str = convertFieldNameToName(fs[i].getName());
				if (str != null) {
					// �����FieldObject���󣬵õ��˶���
					fObj = (FieldObject) fs[i].get(thisClass);
					if (fObj == null) {
						// �������Ϊ�գ��ȹ���һ������ʵ��
						fObj = (FieldObject) fs[i].getType().newInstance();
						// ���ø�ʵ��������
						fObj.setName(str);
						fObj.setLabel(null);
						// ����ʵ������VO
						fs[i].set(thisClass, fObj);
						// ����ʵ�����뷵������
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
	 * �ж϶���ֵ�Ƿ�Ϊ�ա� �������ڣ�(2003-4-17 15:48:31)
	 * 
	 * @return boolean
	 * @param object
	 *            java.lang.Object
	 */
	public static boolean isNULL(Object o) {
		return o == null || o.toString().trim().length() == 0;
	}

	/**
	 *�ϲ�FieldObject���� ��ΪJava��ʱ��֧�ַ��ͣ����ﻹҪʵ�����Ƶķ���mergeStringArray()��
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
	 * �Բ���name��Ӧ����������ֵ��
	 * <p>
	 * �������ڣ�(2003-4-8 10:07:33)
	 * 
	 * @param name
	 *            java.lang.String
	 * @param value
	 *            java.lang.Object
	 * @param className
	 *            java.lang.String
	 * @postcondition �����Ӧ�����ԣ����ء�
	 */
	public static void setAttributeValue(String name, Object value,
			String className, Object thisClass) {
		try {
			// �õ�������
			String ffName = convertNameToAttribute(name);
			// �õ����ԵĶ����ֶ�
			java.lang.reflect.Field ff = getClass(className).getDeclaredField(
					ffName);
			//
			value = beforeSetValue(value, ff);
			// �õ����Ե�Setter������
			String mmName = "set" + convertNameToMethod(name);
			// �õ����Ե�Setter����
			java.lang.reflect.Method mm = thisClass.getClass().getMethod(
					mmName, new Class[] { ff.getType() });
			if (mm == null) {
				return;
			}
			mm.invoke(thisClass, new Object[] { value });
		} catch (ClassCastException e) {
			throw new ClassCastException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0100", null, new String[]{name,(String)value})/*setAttributeValue������Ϊ {0} ��ֵʱ����ת�����󣡣�ֵ��{1}��*/);
		} catch (Exception e) {
			// ����չVO����ʱ����������쳣��������ClassCastException������������
			// e.printStackTrace();
		}

	}

//	/**
//	 * ת����������VOΪnull
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

	// vos:��panel�ϵõ���VO���飨������VO��
	// classname��VO������
	// nullitemkey�������Щ�ֶε�ֵ��Ϊ������Ϊ���VOΪ��VO������ɾ��
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
	 * VO�б����
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param voList
	 *            Ҫ�����vo�б�
	 * @param splitKey
	 *            ����ؼ���
	 * @return ������maplist
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
	 * Map�б����
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param mapList
	 *            Ҫ�����Map�б�
	 * @param splitKey
	 *            ����ؼ���
	 * @return ������maplist
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
	 * ��voListת��Ϊmap,��ֵΪvo����
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param voList
	 *            Ҫ�����vo�б�
	 * @return ������map
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
	 * ��voListת��Ϊmap
	 * 
	 * @param <T>
	 *            SuperVO
	 * @param voList
	 *            Ҫ�����vo�б�
	 * @param splitKey
	 *            ָ�������
	 * @return ������map
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
	 * ���ݷ����ֶκͷ���ֵ��÷�������vo�б�
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
