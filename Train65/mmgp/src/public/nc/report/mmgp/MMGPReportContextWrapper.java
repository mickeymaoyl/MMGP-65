package nc.report.mmgp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import nc.bs.framework.common.RuntimeEnv;
import nc.pub.smart.data.IRowData;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.lang.Calendars;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportFilterUtil;
import nc.vo.pubapp.report.ReportQueryConUtil;

import com.ufida.dataset.IContext;

/**
 * 供应链报表前后台传递对象
 * 
 * @since 6.0
 * @version 2011-09-17 下午08:50:53
 * @author jinjya
 */
public class MMGPReportContextWrapper implements Serializable {

    public MMGPReportContextWrapper() {
        // nothing
    }

    private static final long serialVersionUID = -6818313537742245524L;

    private HashMap<String, Object> tranMap = new HashMap<String, Object>();

    /**
     * 供应链工具类
     */
    private transient ReportQueryConUtil qryconutil = null;

    private transient ReportFilterUtil filterutil = null;

    private transient IContext context = null;

    private transient MMGPReportContextWrapper scmTranMap = null;

    // 报表行数据
    private transient IRowData linkQueryRowData;

    // 报表行临时属性
    private transient HashMap<String, Object> linkQueryTempRowData = new HashMap<String, Object>();

    // 允许在联查界面查询
    private transient boolean allowQuery = false;

    // 是否重新查询
    private boolean isNewQuery = false;

    // 是否是联查
    private boolean isLinkQuery = false;

    // 是否启用权限
    private boolean isEnablePower = false;

    protected HashMap<String, Object> getCacheMap() {
        return this.tranMap;
    }

    protected MMGPReportContextWrapper getCacheTransMap() {
        return this.scmTranMap;
    }

    /**
     * 过滤ConditionVO中Value为空的对象 除操作符为ISNULL或ISNOTNULL以外， 正常情况下Value为空值是非法条件，应过滤掉
     * 
     * @param vos
     * @return
     */
    protected ConditionVO[] filterNullValue(ConditionVO[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return vos;
        }
        List<ConditionVO> condList = new ArrayList<ConditionVO>();
        for (ConditionVO vo : vos) {
            String opercode = vo.getOperaCode();
            if (IOperatorConstants.ISNULL.equalsIgnoreCase(opercode)
                || IOperatorConstants.ISNOTNULL.equalsIgnoreCase(opercode)) {
                condList.add(vo);
                continue;
            }
            if (MMStringUtil.isEmptyWithTrim(vo.getValue())) {
                continue;
            }
            condList.add(vo);
        }
        return condList.toArray(new ConditionVO[0]);
    }

    /**
     * 获取对象时构造子 该构造方式只能在后台使用和Adjustor里获取此对象
     * 
     * @param context
     */
    public MMGPReportContextWrapper(IContext context) {
        if (!RuntimeEnv.getInstance().isRunningInServer()) {
            // ExceptionUtils
            // .wrappBusinessException(SCMReportRes.getReportSmartTransMapError());

        }
        this.qryconutil = new ReportQueryConUtil(context);
        this.filterutil = new ReportFilterUtil(context);
        this.context = context;
        if (!this.qryconutil.isNull() && this.qryconutil.getUserObject() instanceof MMGPReportContextWrapper) {
            this.scmTranMap = (MMGPReportContextWrapper) this.qryconutil.getUserObject();
        }
    }

    /**
     * 获取UserObject对象是否为空
     * 
     * @return
     */
    public boolean getUserObjectIsNull() {
        return this.scmTranMap == null;
    }

    public void put(String key,
                    Object valueObj) {
        if (null != this.scmTranMap) {
            this.scmTranMap.put(key, valueObj);
            return;
        }
        this.tranMap.put(key, valueObj);
    }

    public Object get(String key) {
        if (null != this.scmTranMap) {
            return this.scmTranMap.get(key);
        }
        return this.tranMap.get(key);
    }

    public void remove(String key) {
        if (null != this.scmTranMap) {
            this.scmTranMap.remove(key);
        }
        this.tranMap.remove(key);
    }

    /**
     * 获得缓存其它Key的Map
     * 
     * @return
     */
    public Map<String, Object> getOtherAttributeMap() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getOtherAttributeMap();
        }
        Map<String, Object> otherAttributeMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : this.tranMap.entrySet()) {
            if (entry.getKey().startsWith(MMGPReportConstantKey.PUBCONTEXTKEY_PREFIX)) {
                continue;
            }
            otherAttributeMap.put(entry.getKey(), entry.getValue());
        }
        return otherAttributeMap;
    }

    /**
     * 获取字符串
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getString(key);
        }
        if (null == this.tranMap.get(key) || !(this.tranMap.get(key) instanceof String)) {
            return null;
        }
        return (String) this.tranMap.get(key);
    }

    /**
     * 获取数值型
     * 
     * @param key
     * @return
     */
    public UFDouble getUFDouble(String key) {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getUFDouble(key);
        }
        if (null == this.tranMap.get(key) || !(this.tranMap.get(key) instanceof UFDouble)) {
            return UFDouble.ZERO_DBL;
        }
        return (UFDouble) this.tranMap.get(key);
    }

    /**
     * 获取布尔值
     * 
     * @param key
     * @return
     */
    public UFBoolean getUFBoolean(String key) {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getUFBoolean(key);
        }
        if (null == this.tranMap.get(key)) {
            return UFBoolean.FALSE;
        }
        if (this.tranMap.get(key) instanceof String) {
            String flag = (String) this.tranMap.get(key);
            if ("Y".equalsIgnoreCase(flag)) {
                return UFBoolean.TRUE;
            }
            return UFBoolean.FALSE;
        } else if (!(this.tranMap.get(key) instanceof UFBoolean)) {
            return UFBoolean.FALSE;
        }
        return (UFBoolean) this.tranMap.get(key);
    }

    /**
     * 获得上下文对象
     * 
     * @return
     */
    public IContext getIContext() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getIContext();
        }
        return this.context;
    }

    /**
     * 获得筛选器
     * 
     * @param key
     * @return
     */

    public Descriptor[] getDescriptors(String key) {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getDescriptors(key);
        }
        if (this.tranMap.get(key) != null && this.tranMap.get(key) instanceof Descriptor[]) {
            return (Descriptor[]) this.tranMap.get(key);
        }
        return null;
    }

    /**
     * 获得筛选器
     * 
     * @return
     */

    public Descriptor[] getDescriptor() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getDescriptor();
        }
        if (this.tranMap.get(MMGPReportConstantKey.SCMREPORT_DESC) != null
            && this.tranMap.get(MMGPReportConstantKey.SCMREPORT_DESC) instanceof Descriptor[]) {
            return (Descriptor[]) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_DESC);
        }
        return null;
    }

    /**
     * 获得普通查询条件
     * 
     * @return
     */
    public ConditionVO[] getGeneralConditionVOs() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getGeneralConditionVOs();
        }
        if (this.tranMap.get(MMGPReportConstantKey.SCMREPORT_GENERALCONDITION) != null
            && this.tranMap.get(MMGPReportConstantKey.SCMREPORT_GENERALCONDITION) instanceof ConditionVO[]) {
            return (ConditionVO[]) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_GENERALCONDITION);
        }
        return null;
    }

    /**
     * 获得逻辑查询条件
     * 
     * @return
     */
    public ConditionVO[] getSelectConditionVOs() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getSelectConditionVOs();
        }
        if (this.tranMap.get(MMGPReportConstantKey.SCMREPORT_SELECTCONDITION) != null
            && this.tranMap.get(MMGPReportConstantKey.SCMREPORT_SELECTCONDITION) instanceof ConditionVO[]) {
            return (ConditionVO[]) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_SELECTCONDITION);
        }
        return null;
    }

    /**
     * 获得查询条件
     * 
     * @return
     */
    public ConditionVO[] getConditionVOs() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getConditionVOs();
        }
        if (this.tranMap.get(MMGPReportConstantKey.SCMREPORT_CONDITION) != null
            && this.tranMap.get(MMGPReportConstantKey.SCMREPORT_CONDITION) instanceof ConditionVO[]) {
            return (ConditionVO[]) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_CONDITION);
        }
        return null;
    }

    /**
     * 根据查询条件Key值，获得指定的查询条件
     * 
     * @return
     */
    public ConditionVO getConditionVO(String conditionkey) {
        if (MMStringUtil.isEmptyWithTrim(conditionkey)) {
            return null;
        }
        ConditionVO[] conds = this.getConditionVOs();
        if (MMArrayUtil.isEmpty(conds)) {
            return null;
        }
        ConditionVO reCond = null;
        for (ConditionVO cond : conds) {
            if (!conditionkey.equalsIgnoreCase(cond.getFieldCode())) {
                continue;
            }
            reCond = cond;
            break;
        }
        return reCond;
    }

    /**
     * 获得查询条件
     * 
     * @param key
     * @return
     */
    public String getWherePart() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getWherePart();
        }
        if (this.tranMap.get(MMGPReportConstantKey.SCMREPORT_WHEREPART) != null
            && this.tranMap.get(MMGPReportConstantKey.SCMREPORT_WHEREPART) instanceof String) {
            return (String) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_WHEREPART);
        }
        return null;
    }

    public ReportFilterUtil getFilterutil() {
        return this.filterutil;
    }

    /**
     * 设置查询条件
     * 
     * @param vos
     */
    public void setConditionVOs(ConditionVO[] vos) {
        ConditionVO[] voss = this.filterNullValue(vos);
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_CONDITION, voss);
    }

    /**
     * 设置查询条件
     * 
     * @param vos
     */
    public void setGeneralConditionVOs(ConditionVO[] vos) {
        ConditionVO[] voss = this.filterNullValue(vos);
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_GENERALCONDITION, voss);
    }

    /**
     * 设置查询条件
     * 
     * @param vos
     */
    public void setSelectConditionVOs(ConditionVO[] vos) {
        ConditionVO[] voss = this.filterNullValue(vos);
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_SELECTCONDITION, voss);
    }

    /**
     * 设置查询方案
     * 
     * @param queryscheme
     */
    public void setQueryScheme(IQueryScheme queryscheme) {
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_QUERYSCHEME, queryscheme);
    }

    /**
     * 获取查询方案
     * 
     * @return
     */
    public IQueryScheme getQueryScheme() {
        if (null != this.scmTranMap) {
            return this.scmTranMap.getQueryScheme();
        }
        return (IQueryScheme) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_QUERYSCHEME);
    }

    /**
     * 设置描述器
     * 
     * @param des
     */
    public void setDescriptors(Descriptor[] des) {
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_DESC, des);
    }

    /**
     * 设置默认查询条件
     * 
     * @param sqlwhere
     */
    public void setWherePart(String sqlwhere) {
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_WHEREPART, sqlwhere);
    }

    /**
     * 设置客户端或订阅执行时区
     * 
     * @param timezone
     */
    public void setLocalTimeZone(TimeZone timezone) {
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_LOCALTIMEZONE, timezone);
    }

    /**
     * 获取客户端或订阅执行时区
     * 
     * @return
     */
    public TimeZone getLocalTimeZone() {
        if (null != this.scmTranMap) return this.scmTranMap.getLocalTimeZone();
        if (this.tranMap.containsKey(MMGPReportConstantKey.SCMREPORT_LOCALTIMEZONE)) {
            return (TimeZone) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_LOCALTIMEZONE);
        }
        return Calendars.getGMTDefault();
    }

    /**
     * 设置联查行数据（仅前台使用）
     * 
     * @param rowData
     */
    public void setLinkQueryRowData(IRowData rowData) {
        this.linkQueryRowData = rowData;
    }

    /**
     * 设置联查行临时属性
     * 
     * @param field
     * @param value
     */
    public void setLinkQueryTempRowData(String field,
                                        Object value) {
        if (this.linkQueryTempRowData == null) this.linkQueryTempRowData = new HashMap<String, Object>();
        this.linkQueryTempRowData.put(field, value);
    }

    /**
     * 获取联查行数据（仅前台使用）
     * 
     * @return
     */
    public IRowData getLinkQueryRowData() {
        return this.linkQueryRowData;
    }

    /**
     * 获取联查行数据
     * 
     * @param fieldname
     * @return
     */
    public Object getLinkQueryRowDataValue(String fieldname) {
        // 优先临时属性，再取行属性
        if (this.linkQueryTempRowData != null && this.linkQueryTempRowData.containsKey(fieldname))
            return this.linkQueryTempRowData.get(fieldname);
        if (this.linkQueryRowData == null) return null;
        return this.linkQueryRowData.getData(fieldname);
    }

    /**
     * 设置功能节点主组织权限组织PK
     * 
     * @param pkorgs
     */
    public void setFuncPermissionPkorgs(String[] pkorgs) {
        this.tranMap.put(MMGPReportConstantKey.SCMREPORT_FUNCPERMISSION, pkorgs);
    }

    /**
     * 获取功能节点主组织权限组织PK
     * 
     * @return
     */
    public String[] getFuncPermissionPkorgs() {
        if (null != this.scmTranMap) return this.scmTranMap.getFuncPermissionPkorgs();
        if (this.tranMap.containsKey(MMGPReportConstantKey.SCMREPORT_FUNCPERMISSION)) {
            return (String[]) this.tranMap.get(MMGPReportConstantKey.SCMREPORT_FUNCPERMISSION);
        }
        return null;
    }

    public boolean isNewQuery() {
        if (null != this.scmTranMap) return this.scmTranMap.isNewQuery();
        return this.isNewQuery;
    }

    public void setNewQuery(boolean isNewQuery) {
        this.isNewQuery = isNewQuery;
    }

    public boolean isLinkQuery() {
        if (null != this.scmTranMap) return this.scmTranMap.isLinkQuery();
        return this.isLinkQuery;
    }

    public void setLinkQuery(boolean isLinkQuery) {
        this.isLinkQuery = isLinkQuery;
    }

    public boolean isEnablePower() {
        if (null != this.scmTranMap) return this.scmTranMap.isEnablePower();
        return this.isEnablePower;
    }

    public void setEnablePower(boolean isEnablePower) {
        this.isEnablePower = isEnablePower;
    }

    public boolean isAllowQuery() {
        return this.allowQuery;
    }

    public void setAllowQuery(boolean allowQuery) {
        this.allowQuery = allowQuery;
    }
}
