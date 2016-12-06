package nc.impl.mmgp.uif2.rule.event.cache;

import java.util.List;

import nc.bs.bd.cache.CacheProxy;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.UsePermChangeEvent;
import nc.md.model.IBean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Nov 5, 2013
 * @author wangweir
 */
public class FireDeleteCacheEvent implements IFireCacheEvent {

    /*
     * (non-Javadoc)
     * @see nc.impl.mmgp.uif2.rule.event.cache.IFireCacheEvent#fireCahceEvent(java.lang.String, java.lang.String)
     */
    @Override
    public void fireCahceEvent(IBean bean,
                               String tableName,
                               List<String> pks) {
        CacheProxy.fireDataDeletedBatch(tableName, pks.toArray(new String[0]));
        try {
            EventDispatcher.fireEvent(new UsePermChangeEvent(bean.getID(), IEventType.TYPE_DELETE_AFTER));
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
    }
}
