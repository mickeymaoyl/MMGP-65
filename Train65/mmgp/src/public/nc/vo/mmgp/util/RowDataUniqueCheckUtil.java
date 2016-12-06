package nc.vo.mmgp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.uif2.validation.ValidationFailure;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.vo.ModelAttribute;
import nc.vo.pubapp.pattern.model.meta.entity.vo.ModelVOMeta;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 表体唯一性校验工具类
 * </p>
 * 
 * @since 创建日期 Jul 2, 2013
 * @author wangweir
 */
public class RowDataUniqueCheckUtil {

    /**
     * @param bills
     * @param uniqueCfgs
     * @return
     */
    public static List<ValidationFailure> check(AbstractBill[] bills,
                                                Map<String, List<String>> uniqueCfgs) {
        List<ValidationFailure> failures = new ArrayList<ValidationFailure>();
        for (AbstractBill bill : bills) {
            failures.addAll(check(bill, uniqueCfgs));
        }
        return failures;
    }

    /**
     * @param bill
     * @param uniqueCfgs
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<ValidationFailure> check(AbstractBill bill,
                                                Map<String, List<String>> uniqueCfgs) {
        if (MMMapUtil.isEmpty(uniqueCfgs)) {
            return Collections.EMPTY_LIST;
        }

        Map<String, Set<Integer>> errorRows = new HashMap<String, Set<Integer>>();
        for (Entry<String, List<String>> uniqueCfg : uniqueCfgs.entrySet()) {
            String clazzName = uniqueCfg.getKey();
            List<String> uniqueItemkeys = uniqueCfg.getValue();
            if (MMCollectionUtil.isEmpty(uniqueItemkeys)) {
                continue;
            }

            Class< ? extends ISuperVO> clazz = (Class< ? extends ISuperVO>) getClazz(clazzName);
            ISuperVO[] bodyVOs = bill.getChildren(clazz);
            if (MMArrayUtil.isEmpty(bodyVOs)) {
                continue;
            }
            errorRows.put(buildErrorMsgFormat(uniqueItemkeys, bodyVOs[0]), new HashSet<Integer>());

            checkSuperVOs(bodyVOs, uniqueItemkeys, errorRows);
        }

        return buildFailures(errorRows);
    }

    @SuppressWarnings("unchecked")
    protected static List<ValidationFailure> buildFailures(Map<String, Set<Integer>> errorRows) {
        if (MMMapUtil.isEmpty(errorRows)) {
            return Collections.EMPTY_LIST;
        }

        List<ValidationFailure> failures = new ArrayList<ValidationFailure>();
        for (Entry<String, Set<Integer>> errorRow : errorRows.entrySet()) {
            String msg = errorRow.getKey();
            if (MMCollectionUtil.isEmpty(errorRow.getValue())) {
                continue;
            }
            failures.add(new ValidationFailure(String.format(msg, errorRow.getValue().toString())));
        }

        return failures;
    }

    /**
     * @param vos
     *        SuperVO
     * @param uniqueItemkeys
     *        唯一性字段
     * @param errorPrefix
     *        提示信息前缀
     * @return
     */
    public static List<ValidationFailure> check(SuperVO[] vos,
                                                List<String> uniqueItemkeys,
                                                String errorPrefix) {
        if (MMArrayUtil.isEmpty(vos)) {
            return null;
        }
        Map<String, Set<Integer>> errorRows = new HashMap<String, Set<Integer>>();
        errorRows.put(errorPrefix + buildErrorMsgFormat(uniqueItemkeys, vos[0]), new HashSet<Integer>());

        checkSuperVOs(vos, uniqueItemkeys, errorRows);

        return buildFailures(errorRows);
    }

    protected static Class< ? > getClazz(String clazzName) {
        try {
            return Class.forName(clazzName).getClass();
        } catch (ClassNotFoundException e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    /**
     * @param errorRows
     * @param uniqueItemkeys
     * @param bodyVOs
     */
    protected static void checkSuperVOs(ISuperVO[] bodyVOs,
                                        List<String> uniqueItemkeys,
                                        Map<String, Set<Integer>> errorRows) {
        List<String> uniqueKeys = new ArrayList<String>();
        int row = 0;
        for (ISuperVO bodyVO : bodyVOs) {
            if (VOStatus.DELETED == bodyVO.getStatus()) {
                continue;
            }
            row++;

            String uniqueKey = buildUniqueKey(uniqueItemkeys, bodyVO);

            int indexOfUniqueKey = uniqueKeys.indexOf(uniqueKey);

            if (indexOfUniqueKey != -1) {
                buildErrorMsg(errorRows, uniqueItemkeys, row, bodyVO, indexOfUniqueKey);
            }

            uniqueKeys.add(uniqueKey);
        }
    }

    protected static void buildErrorMsg(Map<String, Set<Integer>> errorRows,
                                        List<String> uniqueItemkeys,
                                        int row,
                                        ISuperVO bodyVO,
                                        int indexOfUniqueKey) {
        String ErrMsg = buildErrorMsgFormat(uniqueItemkeys, bodyVO);
        Set<Integer> rows = errorRows.get(ErrMsg);
        if (rows == null) {
            rows = new HashSet<Integer>();
            rows.add(indexOfUniqueKey + 1);
            errorRows.put(ErrMsg, rows);
        }
        rows.add(row);
    }

    protected static String buildErrorMsgFormat(List<String> uniqueItemkeys,
                                                ISuperVO bodyVO) {
        List<String> showNames = new ArrayList<String>();

        ModelVOMeta meta = (ModelVOMeta) bodyVO.getMetaData();
        for (String uniqueItemkey : uniqueItemkeys) {
            showNames.add(((ModelAttribute) meta.getAttribute(uniqueItemkey)).getLabel());
        }

        String ErrMsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0", "0mmgp001-0109", null, new String[]{meta.getLabel(),showNames.toString()})/*{0}: 第%s行字段{1}不唯一*/;
        return ErrMsg;
    }

    protected static String buildUniqueKey(List<String> uniqueItemkeys,
                                           ISuperVO bodyVO) {
        StringBuilder uniqueKey = new StringBuilder();
        for (String uniqueItemkey : uniqueItemkeys) {
            uniqueKey.append(bodyVO.getAttributeValue(uniqueItemkey));
        }
        return uniqueKey.toString();
    }

}
