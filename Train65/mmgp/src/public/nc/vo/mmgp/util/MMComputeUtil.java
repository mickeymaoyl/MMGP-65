package nc.vo.mmgp.util;

import java.util.Map;

import nc.bs.pub.formulaparse.FormulaParse;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 处理UFDouble的复杂计算
 * <p>
 * eg: MMComputeUtil.compute ("a *b + a + 100" , new UFDouble(10), new UFDouble(2))
 * </p>
 * 
 * @author wangweiu
 */
public final class MMComputeUtil {
    /**
     * 公式解析器
     */
    private static FormulaParseFather f = new FormulaParse();

    /**
     * UFDouble公式计算
     * 
     * @param express
     *        公式表达式
     * @param args
     *        公式参数列表 UFDouble 类型
     * @return 计算结果
     */
    public static UFDouble compute(String express,
                                   UFDouble... args) {
        f.setExpress(express);
        VarryVO varVO = f.getVarry();
        String[] vars = varVO.getVarry();
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                f.addVariable(vars[i], args[i]);
            }
        }
        return new UFDouble(f.getValue());
    }

    /**
     * UFDouble公式计算
     * 
     * @param express
     *        公式表达式
     * @param args
     *        公式参数列表,double 类型
     * @return 计算结果
     */
    public static UFDouble compute(String express,
                                   Double... args) {
        f.setExpress(express);
        VarryVO varVO = f.getVarry();
        String[] vars = varVO.getVarry();
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                f.addVariable(vars[i], args[i]);
            }
        }
        return new UFDouble(f.getValue());
    }

    /**
     * UFDouble公式计算
     * 
     * @param express
     *        公式表达式
     * @param varValues
     *        公式参数列表,Map存储公式key及参数值
     * @return 计算
     */
    public static UFDouble compute(String express,
                                   Map<String, UFDouble> varValues) {
        f.setExpress(express);
        if (varValues != null) {
            for (Map.Entry<String, UFDouble> entity : varValues.entrySet()) {
                f.addVariable(entity.getKey(), entity.getValue());
            }
        }
        return new UFDouble(f.getValue());
    }

    public static void main(String[] args) {
        UFDouble value = MMComputeUtil.compute("a *b + a + 100", new UFDouble(10), new UFDouble(2));
        System.out.println(value);
    }

    private MMComputeUtil() {

    }
}
