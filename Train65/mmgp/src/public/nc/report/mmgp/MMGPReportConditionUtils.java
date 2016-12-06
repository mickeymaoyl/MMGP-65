package nc.report.mmgp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import nc.ui.querytemplate.filter.IFilter;
import nc.ui.querytemplate.formular.date.DateInterval;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.ui.querytemplate.value.SystemFunction;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.IQueryConstants;
import nc.vo.pub.query.RefResultVO;
import nc.vo.pubapp.AppContext;

import com.ufida.dataset.IContext;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 *     �����ѯ��������
 * </p>
 * @since 
 * �������� Aug 7, 2013
 * @author wangweir
 */
public class MMGPReportConditionUtils implements Serializable {

    private static final long serialVersionUID = 2532578868446580807L;

    public static final String AsBegin = "_begin";

    public static final String AsEnd = "_end";

    /**
     * �ж��Ƿ����ڳ���������
     * 
     * @param dates
     * @param dayss
     * @return
     */
    public static UFBoolean checkDateDays(String[] dates,
                                          int dayss) {
        UFBoolean bdate = UFBoolean.FALSE;
        if (MMArrayUtil.isEmpty(dates) || dayss <= 0) {
            return bdate;
        }
        UFDate beginDate = null;
        UFDate endDate = null;
        if (!MMStringUtil.isEmpty(dates[0]) && !IOperatorConstants.ISNULL_INCLUDE_SPACE.equalsIgnoreCase(dates[0])) {
            beginDate = new UFDate(dates[0], ICalendar.BASE_TIMEZONE);
        } else {
            bdate = UFBoolean.TRUE;
            return bdate;
        }
        if (dates.length > 1
            && !MMStringUtil.isEmpty(dates[1])
            && !IOperatorConstants.ISNULL_INCLUDE_SPACE.equalsIgnoreCase(dates[1])) {
            endDate = new UFDate(dates[1], ICalendar.BASE_TIMEZONE);
        } else {
            endDate = AppContext.getInstance().getBusiDate();
        }
        int days = endDate.getDaysAfter(beginDate);
        if (days >= dayss) {
            return UFBoolean.TRUE;
        }
        return bdate;
    }

    /**
     * ���ò�ѯ��������ѯ����
     * 
     * @param context
     * @param queryScheme
     */
    public static void setDescription(IContext context,
                                      IQueryScheme queryScheme) {
        // ��ѯ����
        context.setAttribute("#$QUERY_DESCRIPTION#", queryScheme.get(IQueryScheme.KEY_SQL_DESCRIPTION));
        // ��ѯ���� �ͻ���ҵ������
        context.setAttribute("#$QUERY_DATE#", AppContext.getInstance().getBusiDate().toLocalString());
        // ��ѯ������ʾ
        context.setAttribute("#$QUERY_FILTERDESCRIPTION#", getQuerySchemeDescription(queryScheme));
    }

    /**
     * ���ò�ѯ��������ѯ����
     * 
     * @param context
     * @param queryScheme
     */
    public static void setDescription(MMGPReportContextWrapper tranMap,
                                      IQueryScheme queryScheme,
                                      TimeZone timezone) {
        // ��ѯ����
        tranMap.put("#$QUERY_DESCRIPTION#", queryScheme.get(IQueryScheme.KEY_SQL_DESCRIPTION));
        // ��ѯ���� �ͻ���ҵ������(����ִ�л���ʱ��)
        tranMap.put("#$QUERY_DATE#", AppContext.getInstance().getBusiDate().toStdString(timezone));
        // ��ѯ������ʾ
        tranMap.put("#$QUERY_FILTERDESCRIPTION#", getQuerySchemeDescription(queryScheme, timezone));
    }

    /**
     * ���ò�ѯ��������ѯ����
     * 
     * @param context
     * @param queryScheme
     */
    public static void setDescription(MMGPReportContextWrapper tranMap,
                                      IQueryScheme queryScheme) {
        // ��ѯ����
        tranMap.put("#$QUERY_DESCRIPTION#", queryScheme.get(IQueryScheme.KEY_SQL_DESCRIPTION));
        // ��ѯ���� �ͻ���ҵ������
        tranMap.put("#$QUERY_DATE#", AppContext.getInstance().getBusiDate().toLocalString());
        // ��ѯ������ʾ
        tranMap.put("#$QUERY_FILTERDESCRIPTION#", getQuerySchemeDescription(queryScheme));
    }

    public static Map<String, String> getQuerySchemeDescription(IQueryScheme queryScheme) {
        return getQuerySchemeDescription(queryScheme, TimeZone.getDefault());
    }

    /**
     * �ӷ����л�ò�ѯ������ʾ����
     * 
     * @param queryScheme
     * @return
     */
    public static Map<String, String> getQuerySchemeDescription(IQueryScheme queryScheme,
                                                                TimeZone zone) {
        if (zone == null) {
            zone = TimeZone.getDefault();
        }
        IFilter[] filters = (IFilter[]) queryScheme.get(IQueryScheme.KEY_FILTERS);
        if (MMArrayUtil.isEmpty(filters)) {
            return null;
        }
        Map<String, String> condMap = new HashMap<String, String>();
        for (IFilter filter : filters) {
            String fieldCode = filter.getFilterMeta().getFieldCode();
            // ���⴦������
            int datatype = ((FilterMeta) filter.getFilterMeta()).getDataType();
            if (IQueryConstants.DATE == datatype || IQueryConstants.TIME == datatype) {
                String value = dealDateValue(filter, zone, true);
                if (value == null || IOperatorConstants.ISNULL_INCLUDE_SPACE.equalsIgnoreCase(value)) continue;
                String[] dates = value.split(",");
                if (dates.length < 2) {
                    if (IQueryConstants.DATE == datatype) {
                        value = value.substring(0, 10);
                    }
                    condMap.put(fieldCode, value);
                    condMap.put(fieldCode + AsBegin, value);
                    condMap.put(fieldCode + AsEnd, value);
                } else if (dates.length > 1) {
                    if (IQueryConstants.DATE == datatype) {
                        dates[0] =
                                IOperatorConstants.ISNULL_INCLUDE_SPACE.equalsIgnoreCase(dates[0]) ? null : dates[0]
                                    .substring(0, 10);
                        dates[1] =
                                IOperatorConstants.ISNULL_INCLUDE_SPACE.equalsIgnoreCase(dates[1]) ? null : dates[1]
                                    .substring(0, 10);
                    }
                    condMap.put(fieldCode + AsBegin, dates[0]);
                    condMap.put(fieldCode + AsEnd, dates[1]);
                }
                continue;
            }
            if (filter.getFieldValue().getFieldValues().size() < 1) {
                continue;
            }
            StringBuffer sf = new StringBuffer();
            for (IFieldValueElement filterValue : filter.getFieldValue().getFieldValues()) {
                if (filterValue == null) continue;
                Object valueObject = filterValue.getValueObject();
                if (valueObject instanceof SystemFunction) {
                    RefResultVO functionValue = ((SystemFunction) valueObject).getFunctionValue()[0];
                    sf.append(functionValue.getRefName() + ",");
                    continue;
                }
                sf.append(filterValue.getShowString() + ",");
            }
            int len = sf.length();
            if (len > 0) sf.deleteCharAt(len - 1);
            condMap.put(fieldCode, sf.toString());
        }
        return condMap;
    }

    /**
     * ���ݷ������ò�ѯ����CONDITION
     * 
     * @param queryScheme
     * @param tranMap
     */
    public static void buildConditionVO(IQueryScheme queryScheme,
                                        MMGPReportContextWrapper tranMap) {
        IFilter[] filters = (IFilter[]) queryScheme.get(IQueryScheme.KEY_FILTERS);
        if (MMArrayUtil.isEmpty(filters)) {
            return;
        }
        ConditionVO[] selectConditions = null;
        // ��ȡ�߼���ѯ����
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            tranMap.setSelectConditionVOs(selectConditions);
        }
        setGeneralConditionVOTOMap(queryScheme, tranMap);
        tranMap.setConditionVOs(MMArrayUtil.arrayCombine(tranMap.getGeneralConditionVOs(), selectConditions));
        return;
    }

    /**
     * ������ͨ��ѯ����
     * 
     * @param queryScheme
     * @param tranMap
     */
    public static void setGeneralConditionVOTOMap(IQueryScheme queryScheme,
                                                  MMGPReportContextWrapper tranMap) {
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        IFilter[] filters = (IFilter[]) queryScheme.get(IQueryScheme.KEY_FILTERS);
        if (MMArrayUtil.isEmpty(filters)) {
            return;
        }
        Map<String, ConditionVO> condMap = new HashMap<String, ConditionVO>();
        ConditionVO[] selectConditions = null;
        // ��ȡ�߼���ѯ����
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            condMap = dealLogicCondition(selectConditions);
        }
        ConditionVO[] conds = getCondtionVOsFromQueryScheme(queryScheme);
        for (ConditionVO cond : conds) {
            String fieldCode = cond.getFieldCode();
            // ����߼����������
            if (condMap.containsKey(fieldCode)) continue;
            condList.add(cond);
        }
        tranMap.setGeneralConditionVOs(condList.toArray(new ConditionVO[0]));
    }

    /**
     * �Ӳ�ѯ�����л������,�ߵ�Ĭ��ʱ��
     * 
     * @param queryScheme
     * @return
     */
    public static ConditionVO[] getCondtionVOsFromQueryScheme(IQueryScheme queryScheme) {
        return getCondtionVOsFromQueryScheme(queryScheme, TimeZone.getDefault());
    }

    /**
     * �Ӳ�ѯ�����л����������ָ��ʱ��
     * 
     * @param queryScheme
     * @param zone
     * @return
     */
    public static ConditionVO[] getCondtionVOsFromQueryScheme(IQueryScheme queryScheme,
                                                              TimeZone zone) {
        if (zone == null) zone = TimeZone.getDefault();
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        IFilter[] filters = (IFilter[]) queryScheme.get(IQueryScheme.KEY_FILTERS);
        if (MMArrayUtil.isEmpty(filters)) {
            return null;
        }
        FilterMeta meta = null;
        for (IFilter filter : filters) {
            String fieldCode = filter.getFilterMeta().getFieldCode();
            ConditionVO cond = new ConditionVO();
            meta = (FilterMeta) filter.getFilterMeta();
            // �����ѯ�������Զ���������Զ�����Ϊ���ͣ�С����
            // �����������ó�String,�������sql���ʱ�������,��VBDEF10>5����VBDEF10���ַ���
            if (meta.isUserDef()
                && (meta.getDataType() == IQueryConstants.INTEGER
                    || meta.getDataType() == IQueryConstants.DECIMAL
                    || meta.getDataType() == IQueryConstants.BOOLEAN || meta.getDataType() == IQueryConstants.USERCOMBO)) cond
                .setDataType(IQueryConstants.STRING);
            else
                cond.setDataType(meta.getDataType());
            cond.setFieldCode(fieldCode);
            cond.setOperaCode(filter.getOperator().getOperatorCode());
            // ����ֵ��
            dealConditionValue(filter, cond, condList, zone);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * ����filterֵ��
     * 
     * @param filter
     * @param cond
     * @param condList
     */
    private static void dealConditionValue(IFilter filter,
                                           ConditionVO cond,
                                           List<ConditionVO> condList,
                                           TimeZone zone) {
        StringBuffer valuesf = new StringBuffer();
        if (IQueryConstants.DATE == cond.getDataType() || IQueryConstants.TIME == cond.getDataType()) {
            cond.setValue(dealDateValue(filter, zone, false));
            condList.add(cond);
            return;
        }
        // ��ֵ
        if (filter.getFieldValue().getFieldValues().size() < 1) {
            return;
        }
        // ��ֵ
        if (filter.getFieldValue().getFieldValues().size() == 1) {
            if (filter.getFieldValue().getFieldValues() == null) return;
            cond.setValue(filter.getFieldValue().getFieldValues().get(0).getSqlString());
            condList.add(cond);
            return;
        }
        boolean flag = IQueryConstants.UFREF == cond.getDataType() || IQueryConstants.USERCOMBO == cond.getDataType();
        if (flag) {
            cond.setOperaCode(IOperatorConstants.IN);
            valuesf.append(IQueryConstants.LEFT_BRACKET);
        }
        for (IFieldValueElement filterValue : filter.getFieldValue().getFieldValues()) {
            if (filterValue == null) {
                if (IQueryConstants.DATE == cond.getDataType() || IQueryConstants.TIME == cond.getDataType()) {
                    valuesf.append(IOperatorConstants.ISNULL_INCLUDE_SPACE + ",");
                }
                continue;
            }
            if (flag) valuesf.append("'" + filterValue.getSqlString() + "',");
            else
                valuesf.append(filterValue.getSqlString() + ",");
        }
        if (valuesf.length() < 2) return;
        valuesf.deleteCharAt(valuesf.length() - 1);
        if (flag) {
            valuesf.append(IQueryConstants.RIGHT_BRACKET);
        }
        cond.setValue(valuesf.toString());
        condList.add(cond);
    }

    /**
     * ��������ֵ
     * 
     * @param filter
     * @param zone
     *        ʱ��
     * @param isLocalString
     *        �Ƿ񷵻ر������ڴ������򷵻ر�׼ʱ�䴮
     * @return
     */
    public static String dealDateValue(IFilter filter,
                                       TimeZone zone,
                                       boolean isLocalString) {
        if (filter.getFieldValue() == null || MMCollectionUtil.isEmpty(filter.getFieldValue().getFieldValues())) {
            return null;
        }
        StringBuffer valuesf = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int count = filter.getFieldValue().getFieldValues().size();
        for (int i = 0; i < count; i++) {
            IFieldValueElement filterValue = filter.getFieldValue().getFieldValues().get(i);
            if (filterValue == null) {
                valuesf.append(IOperatorConstants.ISNULL_INCLUDE_SPACE + ",");
                continue;
            }
            Object valueObject = filterValue.getValueObject();
            ICalendar calendar = null;
            boolean isBegin = false;
            if (i == 0 && count > 1) isBegin = true;
            UFDate ufDate = null;
            if (valueObject instanceof SystemFunction) {
                RefResultVO functionValue = ((SystemFunction) valueObject).getFunctionValue()[0];
                DateInterval dateInterval = (DateInterval) functionValue.getRefObj();
                if (isBegin) {
                    calendar = dateInterval.getBeginDate();
                    // ת���ɶ�Ӧʱ��
                    ufDate = ((UFDate) calendar).asBegin(zone);
                    if (isLocalString) {
                        // ȡ����ʱ��
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // ȡ��׼ʱ��
                        valuesf.append(ufDate.toString() + ",");
                    }
                } else {
                    calendar = dateInterval.getEndDate();
                    // ת���ɶ�Ӧʱ��
                    ufDate = ((UFDate) calendar).asEnd(zone);
                    if (isLocalString) {
                        // ȡ����ʱ��
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // ȡ��׼ʱ��
                        valuesf.append(ufDate.toString() + ",");
                    }
                }
            } else if (valueObject instanceof ICalendar) {
                calendar = (ICalendar) valueObject;
                ufDate = new UFDate(calendar.getMillis());
                if (isBegin) {
                    ufDate = ufDate.asBegin(zone);
                    if (isLocalString) {
                        // ȡ����ʱ��
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // ȡ��׼ʱ��
                        valuesf.append(ufDate.toString() + ",");
                    }
                } else {
                    ufDate = ufDate.asEnd(zone);
                    if (isLocalString) {
                        // ȡ����ʱ��
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // ȡ��׼ʱ��
                        valuesf.append(ufDate.toString() + ",");
                    }
                }
            }

        }
        if (valuesf.length() > 0) valuesf.deleteCharAt(valuesf.length() - 1);
        return valuesf.toString();
    }

    /**
     * �ӷ����л����ͨ��ѯ����
     * 
     * @param queryScheme
     * @deprecated
     * @return
     */
    @Deprecated
    public static ConditionVO[] getGeneralConditionVO(IQueryScheme queryScheme) {
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        IFilter[] filters = (IFilter[]) queryScheme.get(IQueryScheme.KEY_FILTERS);
        if (MMArrayUtil.isEmpty(filters)) {
            return null;
        }
        Map<String, ConditionVO> condMap = new HashMap<String, ConditionVO>();
        ConditionVO[] selectConditions = null;
        // ��ȡ�߼���ѯ����
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            condMap = dealLogicCondition(selectConditions);
        }
        ConditionVO[] conds = getCondtionVOsFromQueryScheme(queryScheme);
        for (ConditionVO cond : conds) {
            String fieldCode = cond.getFieldCode();
            // ����߼����������
            if (condMap.containsKey(fieldCode)) continue;
            condList.add(cond);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * �ӷ����л����ͨ��ѯ����
     * 
     * @param queryScheme
     * @return
     */
    public ConditionVO[] getGeneralConditionVOFromIQueryScheme(IQueryScheme queryScheme) {
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        IFilter[] filters = (IFilter[]) queryScheme.get(IQueryScheme.KEY_FILTERS);
        if (MMArrayUtil.isEmpty(filters)) {
            return null;
        }
        Map<String, ConditionVO> condMap = new HashMap<String, ConditionVO>();
        ConditionVO[] selectConditions = null;
        // ��ȡ�߼���ѯ����
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            condMap = dealLogicCondition(selectConditions);
        }
        ConditionVO[] conds = getCondtionVOsFromQueryScheme(queryScheme);
        for (ConditionVO cond : conds) {
            String fieldCode = cond.getFieldCode();
            // ����߼����������
            if (condMap.containsKey(fieldCode)) continue;
            condList.add(cond);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    private static Map<String, ConditionVO> dealLogicCondition(ConditionVO[] conds) {
        Map<String, ConditionVO> condMap = new HashMap<String, ConditionVO>();
        for (ConditionVO cond : conds) {
            condMap.put(cond.getFieldCode(), cond);
        }
        return condMap;
    }

    /**
     * �Ƴ���Ӧ�ֶ�ConditionVO
     * 
     * @param conds
     * @param removekey
     * @return
     */
    public ConditionVO[] removeConditionVO(ConditionVO[] conds,
                                           String[] removekey) {
        if (MMArrayUtil.isEmpty(conds)) {
            return conds;
        }
        if (MMArrayUtil.isEmpty(removekey)) {
            return conds;
        }
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        List<String> keyList = new ArrayList<String>();

        for (String removek : removekey) {
            keyList.add(removek);
        }
        for (ConditionVO cond : conds) {
            String fieldcode = cond.getFieldCode();
            if (keyList.contains(fieldcode)) {
                continue;
            }
            condList.add(cond);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * ��ö�Ӧ�ֶ�ConditionVO
     * 
     * @param conds
     * @param removekey
     * @return
     */
    public static ConditionVO[] getConditionVO(ConditionVO[] conds,
                                               String[] keys) {
        if (MMArrayUtil.isEmpty(conds)) {
            return conds;
        }
        if (MMArrayUtil.isEmpty(keys)) {
            return conds;
        }
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        List<String> keyList = new ArrayList<String>();
        for (String key : keys) {
            keyList.add(key);
        }

        for (ConditionVO cond : conds) {
            String fieldcode = cond.getFieldCode();
            if (!keyList.contains(fieldcode)) {
                continue;
            }
            condList.add(cond);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * ��CONDITIONVO�л��ָ�����ֶ�ֵ ֻ���˳���ֵPK�ֶΣ����ڱ������������ѯ����ֵ������PKֵ
     * 
     * @param conds
     * @param fieldcode
     * @return
     */
    public static String getConditionValueID(ConditionVO[] conds,
                                             String fieldcode) {
        if (MMArrayUtil.isEmpty(conds) || MMStringUtil.isEmpty(fieldcode)) {
            return null;
        }
        for (ConditionVO cond : conds) {
            String condcode = cond.getFieldCode();
            if (!fieldcode.equalsIgnoreCase(condcode)) continue;
            String value = cond.getValue();
            if (MMStringUtil.isEmpty(value) || value.indexOf(",") != -1) {
                break;
            }
            return value;
        }
        return null;
    }

    /**
     * ����ConditionVO������
     * 
     * @param conds
     */
    public static void dealConditionVOOperate(ConditionVO[] conds) {
        if (MMArrayUtil.isEmpty(conds)) {
            return;
        }
        for (ConditionVO cond : conds) {
            // ���������,�����
            if (cond.getOperaCode().equalsIgnoreCase("left like")) {
                cond.setOperaCode("like");
                String value = cond.getValue();
                if (!MMStringUtil.isEmpty(value) && !value.endsWith("%")) {
                    cond.setValue(cond.getValue() + "%");
                }
                continue;
            }
            // ���������,�Ұ���
            if (cond.getOperaCode().equalsIgnoreCase("right like")) {
                cond.setOperaCode("like");
                String value = cond.getValue();
                if (!MMStringUtil.isEmpty(value) && !value.startsWith("%")) {
                    cond.setValue("%" + cond.getValue());
                }
                continue;
            }
            // ���������,��ѡ
            if (cond.getOperaCode().equalsIgnoreCase("==")) {
                cond.setOperaCode("=");
                continue;
            }
        }
    }
}
