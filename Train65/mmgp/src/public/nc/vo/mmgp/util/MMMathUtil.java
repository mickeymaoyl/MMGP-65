package nc.vo.mmgp.util;

import java.util.Arrays;
import java.util.List;

import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * <b> UFDouble 数学工具类 </b>
 * <p>
 * UFDouble 数学工具类
 * </p>
 * 
 * @since 创建日期 May 10, 2013
 * @author wangweir
 */
public class MMMathUtil {

    /**
     * 平方根
     * 
     * @param value
     *        input UFDouble
     * @return 平方根
     */
    public static UFDouble sqrt(UFDouble value) {
        if (value == null) {
            return UFDouble.ZERO_DBL;
        }
        double sqrtV = Math.sqrt(value.doubleValue());
        return new UFDouble(sqrtV);
    }

    /**
     * 计算平方
     * 
     * @param value
     *        input UFdouble
     * @return 平方值
     */
    public static UFDouble square(UFDouble value) {
        return SafeCompute.multiply(value, value);
    }

    /**
     * 绝对值计算
     * 
     * @param value
     *        input UFdouble
     * @return 绝对值
     */
    public static UFDouble abs(UFDouble value) {
        if (value == null) {
            return UFDouble.ZERO_DBL;
        }
        double absV = Math.abs(value.doubleValue());
        return new UFDouble(absV);
    }

    /**
     * 计算平均值
     * 
     * @param values
     *        input values
     * @return 平均值
     */
    public static UFDouble average(UFDouble... values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        return average(Arrays.asList(values));
    }

    /**
     * 计算平均值
     * 
     * @param values
     *        input values
     * @return 平均值
     */
    public static UFDouble average(List<UFDouble> values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        return SafeCompute.div(total(values), new UFDouble(values.size()));
    }

    /**
     * 计算合计值
     * 
     * @param values
     *        input values
     * @return 合计值
     */
    public static UFDouble total(UFDouble... values) {
        if (values == null) {
            return UFDouble.ZERO_DBL;
        }
        return total(Arrays.asList(values));
    }

    /**
     * 计算合计值
     * 
     * @param values
     *        input values
     * @return 合计值
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
