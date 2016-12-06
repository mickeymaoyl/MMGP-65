package nc.bs.mmgp.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.BusinessRuntimeException;

/**
 * <b> ��ʽת���� </b>
 * <p>
 * ͨ���ݹ飬�ѹ�ʽ���õĹ�ʽ��Ŀת��Ϊ��ĩ������Ŀ����������
 * </p>
 * ��������:2011-3-22
 *
 * @author wangweiu
 */
public class MMFormulaConvertor {
    public static final char SIGN_LEFT = '@';

    public static final char SIGN_RIGHT = '@';

    private Map<String, String> mapFormulas = null;

    public MMFormulaConvertor(Map<String, String> allFormulas) {
        mapFormulas = allFormulas;
    }

    public MMFormulaConvertor() {
        mapFormulas = new HashMap<String, String>();
    }

    public void addFormula(String key,
                           String formula) {
        mapFormulas.put(key, formula);
    }

    /**
     * @return the mapFormulas
     */
    public Map<String, String> getMapFormulas() {
        return mapFormulas;
    }

    public String convertFormula(String formula) {
        if (mapFormulas == null) {
            return formula;
        }
        List<String> vars = new ArrayList<String>();
        StringBuffer singleVar = null;
        boolean start = false;
        for (char c : formula.toCharArray()) {
            if (!start && c == SIGN_LEFT) {
                singleVar = new StringBuffer();
                start = true;
            } else if (start && c == SIGN_RIGHT) {
                singleVar.append(c);
                vars.add(singleVar.toString());
                start = false;
            }

            if (start) {
                singleVar.append(c);
            }
        }

        return convertByVars(formula, vars);
    }

    private String convertByVars(String formula,
                                 List<String> vars) {
        String tmpFormula = formula;
        for (String var : vars) {
            if (mapFormulas.containsKey(var)) {
                String cacheFormula = mapFormulas.get(var);
                if (cacheFormula.contains(var)) {
                    throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0048")/*@res "��ʽ����ѭ����"*/ + var + "=" + cacheFormula);
                }
                tmpFormula = tmpFormula.replace(var, "("+cacheFormula+")");
                tmpFormula = convertFormula(tmpFormula);
            }
        }
        return tmpFormula;
    }

    /**
     * @return
     */
    public static String getVariableSign(String str) {
        return SIGN_LEFT + str + SIGN_RIGHT;

    }
}