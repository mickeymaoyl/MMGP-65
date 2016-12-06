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
 * ���������Ժ��Զ������������֯������
 * 
 * @since 6.0
 * @version 2012-6-6 ����05:49:18
 * @author muxh
 */
public class QMarAndDefFilter {

    /**
     * ��ѯģ���ϵ�����֯ �����ڹ��˴󲿷ֵĲ��գ�
     */
    private String basekey = "pk_org";

    /**
     * ��ѯ�Ի������
     */

    private QueryConditionDLGDelegator delegator = null;

    private Map<String, Object> filtermaps = new HashMap<String, Object>();

    /**
     * �Զ������������� ǰ׺
     */
    private String[] itemPrefixs = new String[] {
        "vfree", "vbfree", "vdef", "vbdef"
    };

    /**
     * ���췽��
     * 
     * @param delegator ��ѯ�Ի������
     */
    public QMarAndDefFilter(QueryConditionDLGDelegator delegator) {
        this.delegator = delegator;
    }

    /**
     * ���췽��
     * 
     * @param delegator ��ѯ�Ի������
     * @param basekey ���չ��˵��ֶ�
     */
    public QMarAndDefFilter(QueryConditionDLGDelegator delegator, String basekey) {
        this.delegator = delegator;
        this.basekey = basekey;
    }

    /**
     * һ����ѯģ��ֻ��Ҫ��һ��
     */
    public void addFilterMapsListeners() {
        this.addFilterMaps();
        Set<Entry<String, Object>> entrys = this.filtermaps.entrySet();
        for (Entry<String, Object> entry : entrys) {
            this.delegator.registerCriteriaEditorListener((ICriteriaChangedListener) entry.getValue());
        }
        this.filtermaps.clear(); // ���

    }

    public String[] getItemPrefixs() {
        return this.itemPrefixs;
    }

    /**
     * ĳЩ���ղ���Ҫ���� ���� ��֯����Ա �����������Ƴ�
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
     * ��ӹ��˼�������ѯģ���ϵ����в����ֶΣ� �˷������� �ᵼ�º���������ֶ�Ҳ�����������
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
            // ����Ǹ������Ի������Զ�����
            if (this.isStartWithPrefix(fieldcode)) {
                CommonQueryFilter lis = new CommonQueryFilter(this.delegator);
                lis.setFatherPath(this.basekey);
                lis.setChildPath(fieldcode);
                this.filtermaps.put(fieldcode, lis);
            }
        }
    }

    /**
     * �ж��ֶ��Ƿ����Զ�����ǰ׺��ͷ
     * 
     * @param fieldcode ��Ҫ�жϵ��ֶ�
     * @return true���ǣ�false������
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
