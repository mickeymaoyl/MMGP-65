package nc.vo.mmgp.util;

import java.util.Map;

import nc.bs.pub.formulaparse.FormulaParse;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ����UFDouble�ĸ��Ӽ���
 * <p>
 * eg: MMComputeUtil.compute ("a *b + a + 100" , new UFDouble(10), new UFDouble(2))
 * </p>
 * 
 * @author wangweiu
 */
public final class MMComputeUtil {
    /**
     * ��ʽ������
     */
    private static FormulaParseFather f = new FormulaParse();

    /**
     * UFDouble��ʽ����
     * 
     * @param express
     *        ��ʽ���ʽ
     * @param args
     *        ��ʽ�����б� UFDouble ����
     * @return ������
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
     * UFDouble��ʽ����
     * 
     * @param express
     *        ��ʽ���ʽ
     * @param args
     *        ��ʽ�����б�,double ����
     * @return ������
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
     * UFDouble��ʽ����
     * 
     * @param express
     *        ��ʽ���ʽ
     * @param varValues
     *        ��ʽ�����б�,Map�洢��ʽkey������ֵ
     * @return ����
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
