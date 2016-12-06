package nc.bs.mmgp.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * VO工具类
 * </p>
 * 
 * @since 创建日期 May 10, 2013
 * @author wangweir
 */
public class MMGPVoUtils {
    public static final int ASC = 1;

    public static final int DESC = -1;

    /**
     * 设置表头值
     * 
     * @param voObj
     *        VO （可以为SuperVO或者AggVO）
     * @param field
     *        字段
     * @param value
     *        值
     * @throws BusinessException
     */
    public static void setHeadVOFieldValue(Object voObj,
                                           String field,
                                           Object value) throws BusinessException {
        if (voObj == null) {
            return;
        }
        CircularlyAccessibleValueObject headerVO = getHeadVO(voObj);

        if (headerVO != null) {
            headerVO.setAttributeValue(field, value);
        }
    }

    /**
     * 设置表头VO状态
     * 
     * @param object
     *        VO（可以为SuperVO或者AggVO）
     * @param status
     *        vo状态
     * @throws BusinessException
     */
    public static void setHeadVOStatus(Object object,
                                       int status) throws BusinessException {
        if (object == null) {
            return;
        }
        CircularlyAccessibleValueObject vo = getHeadVO(object);

        if (vo != null) {
            vo.setStatus(status);
        }
    }

    /**
     * 返回表头VO状态
     * 
     * @param object
     *        VO（可以为SuperVO或者AggVO）
     * @return VO状态
     * @throws BusinessException
     */
    public static int getHeadVOStatus(Object object) throws BusinessException {
        if (object == null) {
            return -1;
        }
        CircularlyAccessibleValueObject vo = getHeadVO(object);

        if (vo != null) {
            return vo.getStatus();
        }
        return -1;
    }

    /**
     * 返回表头VO
     * 
     * @param object
     *        VO（可以为SuperVO或者AggVO）
     * @return 表头VO
     */
    private static CircularlyAccessibleValueObject getHeadVO(Object object) {
        // 欲设置状态的vo
        CircularlyAccessibleValueObject vo = null;

        if (object instanceof AggregatedValueObject) {
            vo = ((AggregatedValueObject) object).getParentVO();

        } else if (object instanceof CircularlyAccessibleValueObject) {
            vo = (CircularlyAccessibleValueObject) object;
        }
        return vo;
    }

    public static void arraySort(CircularlyAccessibleValueObject[] vos,
                                 final String[] fields,
                                 final int[] ascFlags,
                                 final boolean nullLast) {
        VOUtil.sort(vos, fields, ascFlags, nullLast);
    }

    public static void arraySort(CircularlyAccessibleValueObject[] vos,
                                 final String[] fields,
                                 final int[] ascFlags) {
        arraySort(vos, fields, ascFlags, false);
    }

    public static <T extends CircularlyAccessibleValueObject> void descSort(T[] vos,
                                                                            String... fields) {
        if (vos == null) return;
        if (fields == null || fields.length == 0) return;
        int[] ascFlags = new int[fields.length];
        Arrays.fill(ascFlags, VOUtil.DESC);
        arraySort(vos, fields, ascFlags);
    }

    public static <T extends CircularlyAccessibleValueObject> void ascSort(T[] vos,
                                                                           String... fields) {
        if (vos == null) return;
        if (fields == null || fields.length == 0) return;
        int[] ascFlags = new int[fields.length];
        Arrays.fill(ascFlags, VOUtil.ASC);
        arraySort(vos, fields, ascFlags);
    }

    public static <T extends CircularlyAccessibleValueObject> void listSort(List<T> vos,
                                                                            final String[] fields,
                                                                            final int[] ascFlags,
                                                                            final boolean nullLast) {
        if (vos == null) return;
        if (fields == null || fields.length == 0) return;
        if (ascFlags == null) {
            throw new IllegalArgumentException("VOUtil.sort ascFlags cann't be null");
        } else if (fields.length != ascFlags.length) {
            throw new IllegalArgumentException("VOUtil.sort length of fields not equal with that of ascFlags");
        }
        for (int i = 0; i < ascFlags.length; i++) {
            if (ascFlags[i] != ASC && ascFlags[i] != DESC)
                throw new IllegalArgumentException("VOUtil.sort Illegal Value of ascFlag i="
                    + i
                    + " value= "
                    + ascFlags[i]);
        }
        Comparator<T> c = new Comparator<T>() {
            @SuppressWarnings({"unchecked", "rawtypes" })
            public int compare(T vo1,
                               T vo2) {
                int Greater = 1;
                int Less = -1;
                int Equal = 0;

                for (int i = 0; i < fields.length; i++) {
                    Object v1 = vo1.getAttributeValue(fields[i]);
                    Object v2 = vo2.getAttributeValue(fields[i]);

                    if (v1 == null && v2 == null) {
                        continue;
                    }
                    if (v1 == null && v2 != null) if (ascFlags[i] == ASC && nullLast) {
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
                        if (u1.compareTo(u2) == 0) continue;
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
        Collections.sort(vos, c);

    }

    public static <T extends CircularlyAccessibleValueObject> void listSort(List<T> vos,
                                                                            final String[] fields,
                                                                            final int[] ascFlags) {
        listSort(vos, fields, ascFlags, false);
    }

    public static <T extends CircularlyAccessibleValueObject> void descSort(List<T> vos,
                                                                            String... fields) {
        if (vos == null) return;
        if (fields == null || fields.length == 0) return;
        int[] ascFlags = new int[fields.length];
        Arrays.fill(ascFlags, VOUtil.DESC);
        listSort(vos, fields, ascFlags);
    }

    public static <T extends CircularlyAccessibleValueObject> void ascSort(List<T> vos,
                                                                           String... fields) {
        if (vos == null) return;
        if (fields == null || fields.length == 0) return;
        int[] ascFlags = new int[fields.length];
        Arrays.fill(ascFlags, VOUtil.ASC);
        listSort(vos, fields, ascFlags);
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
	
}
