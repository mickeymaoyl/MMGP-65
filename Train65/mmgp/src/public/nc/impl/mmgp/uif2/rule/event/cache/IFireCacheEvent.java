package nc.impl.mmgp.uif2.rule.event.cache;

import java.util.List;

import nc.md.model.IBean;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Nov 5, 2013
 * @author wangweir
 */
public interface IFireCacheEvent {

    /**
     * 
     */
    void fireCahceEvent(IBean bean,
                        String tableName,
                        List<String> pks);
}
