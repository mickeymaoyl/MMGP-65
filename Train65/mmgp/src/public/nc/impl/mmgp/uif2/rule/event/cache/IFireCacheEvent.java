package nc.impl.mmgp.uif2.rule.event.cache;

import java.util.List;

import nc.md.model.IBean;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Nov 5, 2013
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
