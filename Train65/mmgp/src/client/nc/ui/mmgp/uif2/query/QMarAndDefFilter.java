package nc.ui.mmgp.uif2.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.querytemplate.ICriteriaChangedListener;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.QueryTempletTotalVO;

/**
 * 处理辅助属性和自定义项关联主组织联动。
 * 
 * @since 6.0
 * @version 2012-6-6 下午05:49:18
 * @author muxh
 */
public class QMarAndDefFilter {

    /**
     * 查询模板上的主组织 （用于过滤大部分的参照）
     */
    private String basekey = "pk_org";

    /**
     * 查询对话框代理
     */

    private QueryConditionDLGDelegator delegator = null;

    private Map<String, Object> filtermaps = new HashMap<String, Object>();

    /**
     * 自定义项与自由项 前缀
     */
    private String[] itemPrefixs = new String[] {
        "vfree", "vbfree", "vdef", "vbdef"
    };

    /**
     * 构造方法
     * 
     * @param delegator 查询对话框代理
     */
    public QMarAndDefFilter(QueryConditionDLGDelegator delegator) {
        this.delegator = delegator;
    }

    /**
     * 构造方法
     * 
     * @param delegator 查询对话框代理
     * @param basekey 依照过滤的字段
     */
    public QMarAndDefFilter(QueryConditionDLGDelegator delegator, String basekey) {
        this.delegator = delegator;
        this.basekey = basekey;
    }

    /**
     * 一个查询模板只需要调一次
     */
    public void addFilterMapsListeners() {
        this.addFilterMaps();
        Set<Entry<String, Object>> entrys = this.filtermaps.entrySet();
        for (Entry<String, Object> entry : entrys) {
            this.delegator.registerCriteriaEditorListener((ICriteriaChangedListener) entry.getValue());
        }
        this.filtermaps.clear(); // 清空

    }

    public String[] getItemPrefixs() {
        return this.itemPrefixs;
    }

    /**
     * 某些参照不需要过滤 比如 组织、人员 可以在这里移除
     * 
     * @param keys
     */
    public void removeFilterMaps(String[] keys) {
        for (String key : keys) {
            this.filtermaps.remove(key);
        }
    }

    public void setItemPrefixs(String[] itemPrefixs) {
        this.itemPrefixs = itemPrefixs;
    }

    /**
     * 添加过滤监听（查询模板上的所有参照字段） 此方法慎用 会导致后来设过的字段也会有这个监听
     */
    private void addFilterMaps() {

        QueryTempletTotalVO totalvo = this.delegator.getTotalVO();
        if (totalvo == null) {
            return;
        }
        QueryConditionVO[] conds = null;

        conds = totalvo.getConditionVOs();
        if (conds == null || conds.length == 0) {

            return;
        }
        for (QueryConditionVO cond : conds) {
            String fieldcode = cond.getFieldCode();
            // 如果是辅助属性或者是自定义项
            if (this.isStartWithPrefix(fieldcode)) {
                CommonQueryFilter lis = new CommonQueryFilter(this.delegator);
                lis.setFatherPath(this.basekey);
                lis.setChildPath(fieldcode);
                this.filtermaps.put(fieldcode, lis);
            }
        }
    }

    /**
     * 判断字段是否以自定义项前缀开头
     * 
     * @param fieldcode 需要判断的字段
     * @return true：是；false：不是
     */
    private boolean isStartWithPrefix(String fieldcode) {

        for (String prefix : this.getItemPrefixs()) {
            if (fieldcode.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}
