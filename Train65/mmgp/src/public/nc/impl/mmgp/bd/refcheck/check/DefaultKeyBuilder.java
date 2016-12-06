package nc.impl.mmgp.bd.refcheck.check;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Sep 23, 2013
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
