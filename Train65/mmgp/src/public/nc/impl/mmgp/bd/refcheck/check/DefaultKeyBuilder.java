package nc.impl.mmgp.bd.refcheck.check;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 23, 2013
 * @author wangweir
 */
public class DefaultKeyBuilder implements IKeyBuilder {

    private String eventType;

    private String clazzName;

    /**
     * 
     */
    public DefaultKeyBuilder(String eventType,
                             String clazzName) {
        this.eventType = eventType;
        this.clazzName = clazzName;
    }

    /*
     * (non-Javadoc)
     * @see nc.impl.mmgp.bd.refcheck.check.IKeyBuilder#buildKey()
     */
    @Override
    public String buildKey() {
        return eventType + clazzName;
    }

}
