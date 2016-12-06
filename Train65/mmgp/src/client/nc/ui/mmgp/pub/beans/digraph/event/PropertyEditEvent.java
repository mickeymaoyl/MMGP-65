package nc.ui.mmgp.pub.beans.digraph.event;

/**
 * <b> ����ͼ��������¼��� </b>
 * <p>
 * ����ͼ��������ϵĿؼ�ֵ�����仯ʱ�ɷ�
 * </p>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
public class PropertyEditEvent {
    private Object source;

    private String propertyKey;

    private Object propertyValue;

    public PropertyEditEvent(Object src) {
        source = src;
    }

    public Object getSource() {
        return source;
    }

    /**
     * @return the propertyKey
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**
     * @param propertyKey
     *        the propertyKey to set
     */
    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * @return the propertyValue
     */
    public Object getPropertyValue() {
        return propertyValue;
    }

    /**
     * @param propertyValue
     *        the propertyValue to set
     */
    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

}
