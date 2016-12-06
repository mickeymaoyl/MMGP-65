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
 * <b> 简要描述功能 </b>
 * <p>
 *     报表查询条件工具
 * </p>
 * @since 
 * 创建日期 Aug 7, 2013
 * @author wangweir
 */
public class MMGPReportConditionUtils implements Serializable {

    private static final long serialVersionUID = 2532578868446580807L;

    public static final String AsBegin = "_begin";

    public static final String AsEnd = "_end";

    /**
     * 判断是否日期超出天数据
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
     * 设置查询描述及查询日期
     * 
     * @param context
     * @param queryScheme
     */
    public static void setDescription(IContext context,
                                      IQueryScheme queryScheme) {
        // 查询描述
        context.setAttribute("#$QUERY_DESCRIPTION#", queryScheme.get(IQueryScheme.KEY_SQL_DESCRIPTION));
        // 查询日期 客户端业务日期
        context.setAttribute("#$QUERY_DATE#", AppContext.getInstance().getBusiDate().toLocalString());
        // 查询条件显示
        context.setAttribute("#$QUERY_FILTERDESCRIPTION#", getQuerySchemeDescription(queryScheme));
    }

    /**
     * 设置查询描述及查询日期
     * 
     * @param context
     * @param queryScheme
     */
    public static void setDescription(MMGPReportContextWrapper tranMap,
                                      IQueryScheme queryScheme,
                                      TimeZone timezone) {
        // 查询描述
        tranMap.put("#$QUERY_DESCRIPTION#", queryScheme.get(IQueryScheme.KEY_SQL_DESCRIPTION));
        // 查询日期 客户端业务日期(根据执行环境时区)
        tranMap.put("#$QUERY_DATE#", AppContext.getInstance().getBusiDate().toStdString(timezone));
        // 查询条件显示
        tranMap.put("#$QUERY_FILTERDESCRIPTION#", getQuerySchemeDescription(queryScheme, timezone));
    }

    /**
     * 设置查询描述及查询日期
     * 
     * @param context
     * @param queryScheme
     */
    public static void setDescription(MMGPReportContextWrapper tranMap,
                                      IQueryScheme queryScheme) {
        // 查询描述
        tranMap.put("#$QUERY_DESCRIPTION#", queryScheme.get(IQueryScheme.KEY_SQL_DESCRIPTION));
        // 查询日期 客户端业务日期
        tranMap.put("#$QUERY_DATE#", AppContext.getInstance().getBusiDate().toLocalString());
        // 查询条件显示
        tranMap.put("#$QUERY_FILTERDESCRIPTION#", getQuerySchemeDescription(queryScheme));
    }

    public static Map<String, String> getQuerySchemeDescription(IQueryScheme queryScheme) {
        return getQuerySchemeDescription(queryScheme, TimeZone.getDefault());
    }

    /**
     * 从方案中获得查询条件显示名称
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
            // 特殊处理日期
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
     * 根据方案设置查询条件CONDITION
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
        // 获取逻辑查询条件
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            tranMap.setSelectConditionVOs(selectConditions);
        }
        setGeneralConditionVOTOMap(queryScheme, tranMap);
        tranMap.setConditionVOs(MMArrayUtil.arrayCombine(tranMap.getGeneralConditionVOs(), selectConditions));
        return;
    }

    /**
     * 设置普通查询条件
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
        // 获取逻辑查询条件
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            condMap = dealLogicCondition(selectConditions);
        }
        ConditionVO[] conds = getCondtionVOsFromQueryScheme(queryScheme);
        for (ConditionVO cond : conds) {
            String fieldCode = cond.getFieldCode();
            // 如果逻辑条件则过滤
            if (condMap.containsKey(fieldCode)) continue;
            condList.add(cond);
        }
        tranMap.setGeneralConditionVOs(condList.toArray(new ConditionVO[0]));
    }

    /**
     * 从查询方案中获得条件,走当默认时区
     * 
     * @param queryScheme
     * @return
     */
    public static ConditionVO[] getCondtionVOsFromQueryScheme(IQueryScheme queryScheme) {
        return getCondtionVOsFromQueryScheme(queryScheme, TimeZone.getDefault());
    }

    /**
     * 从查询方案中获得条件，走指定时区
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
            // 如果查询条件是自定义项，并且自定义项为整型，小数，
            // 数据类型设置成String,否则翻译成sql语句时会出问题,如VBDEF10>5，而VBDEF10是字符串
            if (meta.isUserDef()
                && (meta.getDataType() == IQueryConstants.INTEGER
                    || meta.getDataType() == IQueryConstants.DECIMAL
                    || meta.getDataType() == IQueryConstants.BOOLEAN || meta.getDataType() == IQueryConstants.USERCOMBO)) cond
                .setDataType(IQueryConstants.STRING);
            else
                cond.setDataType(meta.getDataType());
            cond.setFieldCode(fieldCode);
            cond.setOperaCode(filter.getOperator().getOperatorCode());
            // 处理值集
            dealConditionValue(filter, cond, condList, zone);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * 处理filter值集
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
        // 无值
        if (filter.getFieldValue().getFieldValues().size() < 1) {
            return;
        }
        // 单值
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
     * 处理日期值
     * 
     * @param filter
     * @param zone
     *        时区
     * @param isLocalString
     *        是否返回本地日期串，否则返回标准时间串
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
                    // 转换成对应时区
                    ufDate = ((UFDate) calendar).asBegin(zone);
                    if (isLocalString) {
                        // 取本地时区
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // 取标准时区
                        valuesf.append(ufDate.toString() + ",");
                    }
                } else {
                    calendar = dateInterval.getEndDate();
                    // 转换成对应时区
                    ufDate = ((UFDate) calendar).asEnd(zone);
                    if (isLocalString) {
                        // 取本地时区
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // 取标准时区
                        valuesf.append(ufDate.toString() + ",");
                    }
                }
            } else if (valueObject instanceof ICalendar) {
                calendar = (ICalendar) valueObject;
                ufDate = new UFDate(calendar.getMillis());
                if (isBegin) {
                    ufDate = ufDate.asBegin(zone);
                    if (isLocalString) {
                        // 取本地时区
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // 取标准时区
                        valuesf.append(ufDate.toString() + ",");
                    }
                } else {
                    ufDate = ufDate.asEnd(zone);
                    if (isLocalString) {
                        // 取本地时区
                        valuesf.append(ufDate.toString(zone, format) + ",");
                    } else {
                        // 取标准时区
                        valuesf.append(ufDate.toString() + ",");
                    }
                }
            }

        }
        if (valuesf.length() > 0) valuesf.deleteCharAt(valuesf.length() - 1);
        return valuesf.toString();
    }

    /**
     * 从方案中获得普通查询条件
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
        // 获取逻辑查询条件
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            condMap = dealLogicCondition(selectConditions);
        }
        ConditionVO[] conds = getCondtionVOsFromQueryScheme(queryScheme);
        for (ConditionVO cond : conds) {
            String fieldCode = cond.getFieldCode();
            // 如果逻辑条件则过滤
            if (condMap.containsKey(fieldCode)) continue;
            condList.add(cond);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * 从方案中获得普通查询条件
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
        // 获取逻辑查询条件
        if (queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION) != null) {
            selectConditions = (ConditionVO[]) queryScheme.get(IQueryScheme.KEY_LOGICAL_CONDITION);
            condMap = dealLogicCondition(selectConditions);
        }
        ConditionVO[] conds = getCondtionVOsFromQueryScheme(queryScheme);
        for (ConditionVO cond : conds) {
            String fieldCode = cond.getFieldCode();
            // 如果逻辑条件则过滤
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
     * 移除对应字段ConditionVO
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
     * 获得对应字段ConditionVO
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
     * 从CONDITIONVO中获得指定的字段值 只过滤出单值PK字段，用于报表变量解析查询条件值，单个PK值
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
     * 处理ConditionVO操作符
     * 
     * @param conds
     */
    public static void dealConditionVOOperate(ConditionVO[] conds) {
        if (MMArrayUtil.isEmpty(conds)) {
            return;
        }
        for (ConditionVO cond : conds) {
            // 处理操作符,左包含
            if (cond.getOperaCode().equalsIgnoreCase("left like")) {
                cond.setOperaCode("like");
                String value = cond.getValue();
                if (!MMStringUtil.isEmpty(value) && !value.endsWith("%")) {
                    cond.setValue(cond.getValue() + "%");
                }
                continue;
            }
            // 处理操作符,右包含
            if (cond.getOperaCode().equalsIgnoreCase("right like")) {
                cond.setOperaCode("like");
                String value = cond.getValue();
                if (!MMStringUtil.isEmpty(value) && !value.startsWith("%")) {
                    cond.setValue("%" + cond.getValue());
                }
                continue;
            }
            // 处理操作符,单选
            if (cond.getOperaCode().equalsIgnoreCase("==")) {
                cond.setOperaCode("=");
                continue;
            }
        }
    }
}
