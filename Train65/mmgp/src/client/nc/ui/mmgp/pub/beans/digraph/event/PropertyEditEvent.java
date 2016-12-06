package nc.ui.mmgp.pub.beans.digraph.event;

/**
 * <b> 有向图属性面板事件类 </b>
 * <p>
 * 有向图属性面板上的控件值发生变化时派发
 * </p>
 * 创建日期:2011-2-16
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
