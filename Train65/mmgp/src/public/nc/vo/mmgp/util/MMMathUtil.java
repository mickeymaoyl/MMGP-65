package nc.vo.mmgp.util;

import java.util.Arrays;
import java.util.List;

import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * <b> UFDouble ��ѧ������ </b>
 * <p>
 * UFDouble ��ѧ������
 * </p>
 * 
 * @since �������� May 10, 2013
 * @author wangweir
 */
public class MMMathUtil {

    /**
     * ƽ����
     * 
     * @param value
     *        input UFDouble
     * @return ƽ����
     */
    public static UFDouble sqrt(UFDouble value) {
        if (value == null) {
            return UFDouble.ZERO_DBL;
        }
        double sqrtV = Math.sqrt(value.doubleValue());
        return new UFDouble(sqrtV);
    }

    /**
     * ����ƽ��
     * 
     * @param value
     *        input UFdouble
     * @return ƽ��ֵ
     */
    public static UFDouble square(UFDouble value) {
        return SafeCompute.multiply(value, value);
    }

    /**
     * ����ֵ����
     * 
     * @param value
     *        input UFdouble
     * @return ����ֵ
     */
    public static UFDouble abs(UFDouble value) {
        if (value == null) {
            return UFDouble.ZERO_DBL;
        }
        double absV = Math.abs(value.doubleValue());
        return new UFDouble(absV);
    }

    /**
     * ����ƽ��ֵ
     * 
     * @param values
     *        input values
     * @return ƽ��ֵ
     */
    public static UFDouble average(UFDouble... values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        return average(Arrays.asList(values));
    }

    /**
     * ����ƽ��ֵ
     * 
     * @param values
     *        input values
     * @return ƽ��ֵ
     */
    public static UFDouble average(List<UFDouble> values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        return SafeCompute.div(total(values), new UFDouble(values.size()));
    }

    /**
     * ����ϼ�ֵ
     * 
     * @param values
     *        input values
     * @return �ϼ�ֵ
     */
    public static UFDouble total(UFDouble... values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        return total(Arrays.asList(values));
    }

    /**
     * ����ϼ�ֵ
     * 
     * @param values
     *        input values
     * @return �ϼ�ֵ
     */
    public static UFDouble total(List<UFDouble> values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        UFDouble result = UFDouble.ZERO_DBL;
        for (UFDouble value : values) {
            result = SafeCompute.add(result, value);
        }

        return result;
    }
}
