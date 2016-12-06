package nc.report.mmgp;

/**
 * MMGP报表存储KEY常量
 *  <li>公共缓存变量，增加Key值时，请使用前缀PUBCONTEXTKEY_PREFIX</li>
 */
public class MMGPReportConstantKey {

    // 公共缓存KEY前缀
    public static final String PUBCONTEXTKEY_PREFIX = "#$MMGP";

    // 供应链通用查询说明
    public static final String SCMQUERY_DESCRIPTION = PUBCONTEXTKEY_PREFIX + "QUERY_DESCRIPTION#";

    // 供应链通用查询描述
    public static final String SCMQUERY_DATE = PUBCONTEXTKEY_PREFIX + "QUERY_DATE#";

    // 供应链通用查询条件显示内容
    public static final String SCMQUERY_FILTERDESCRIPTION = PUBCONTEXTKEY_PREFIX + "QUERY_FILTERDESCRIPTION#";

    // 供应链通用查询过滤器
    public static final String SCMREPORT_DESC = PUBCONTEXTKEY_PREFIX + "REPORT_DESC#";

    // 供应链通用查询报表上下文对象
    public static final String SCMREPORT_CONTEXT = PUBCONTEXTKEY_PREFIX + "REPORT_CONTEXT#";

    // 供应链通用查询报表所有查询条件
    public static final String SCMREPORT_CONDITION = PUBCONTEXTKEY_PREFIX + "REPORT_CONDITION#";

    // 供应链通用查询报表通用查询条件
    public static final String SCMREPORT_GENERALCONDITION = PUBCONTEXTKEY_PREFIX + "REPORT_GENERALCONDITION#";

    // 供应链通用查询报表逻辑查询条件
    public static final String SCMREPORT_SELECTCONDITION = PUBCONTEXTKEY_PREFIX + "REPORT_SELECTCONDITION#";

    // 供应链通用查询默认查询条件
    public static final String SCMREPORT_WHEREPART = PUBCONTEXTKEY_PREFIX + "REPORT_WHEREPART#";

    // 供应链通用查询默认查询方案
    public static final String SCMREPORT_QUERYSCHEME = PUBCONTEXTKEY_PREFIX + "REPORT_QUERYSCHEME#";

    // 供应链通用查询条件
    public static final String SCMREPORT_WHERESQL = PUBCONTEXTKEY_PREFIX + "REPORT_WHERESQL#";

    // 供应链通用当地时区
    public static final String SCMREPORT_LOCALTIMEZONE = PUBCONTEXTKEY_PREFIX + "REPORT_LOCALTIMEZONE#";

    // 供应链通用联查条件
    public static final String SCMREPORT_LINKQUERYPARAMS = PUBCONTEXTKEY_PREFIX + "REPORT_LINKQUERYPARAMS#";

    // 供应链通用联查行信息
    public static final String SCMREPORT_LINKQUERYROWDATA = PUBCONTEXTKEY_PREFIX + "REPORT_LINKQUERYROWDATA#";

    // 供应链通用功能节点权限组织ID
    public static final String SCMREPORT_FUNCPERMISSION = PUBCONTEXTKEY_PREFIX + "REPORT_FUNCPERMISSION#";

    // 供应链通用语义提供者编码
    public static final String SCMREPORT_SMARTTABLECODE = PUBCONTEXTKEY_PREFIX + "REPORT_SMARTTABLECODE#";

    // 供应链语义模型工厂类
    public static final String SCMREPORT_SMART_SCMSMARTVO = PUBCONTEXTKEY_PREFIX + "REPORT_SMART_SCMSMARTVO#";

    /** 报表变量处理 */
    // 查询日期
    public static final String SCM_DEFAULT_REP_VAR_DATE = PUBCONTEXTKEY_PREFIX + "_var_date#";

    // 查询条件描述
    public static final String SCM_DEFAULT_REP_VAR_DESCRIPTION = PUBCONTEXTKEY_PREFIX + "_var_description#";

    // 查询条件
    public static final String SCM_DEFAULT_REP_VAR_FILTERDESCRIPTION = PUBCONTEXTKEY_PREFIX + "CONDITIONVAR#";

    // 自由辅助性名称
    public static final String SCM_DEFAULT_REP_VAR_VFREECAPTION = PUBCONTEXTKEY_PREFIX + "VFREECAPTION#";

    // 自由项名称
    public static final String SCM_DEFAULT_REP_VAR_DEFCAPTION = PUBCONTEXTKEY_PREFIX + "DEFCAPTION#";

    // 自由项参数
    public static final String SCM_DEFAULT_REP_VAR_DEFCAPTIONPARAM = PUBCONTEXTKEY_PREFIX + "DEFCAPTIONPARAM#";

    // 分隔标识
    public static final String FLAG = "#";

    /** 变量处理解析档案值 函数名 */
    // 物料解析函数
    public static final String SCM_DEFAULT_REP_VAR_MATERIL = PUBCONTEXTKEY_PREFIX + "parse_material#";

}
