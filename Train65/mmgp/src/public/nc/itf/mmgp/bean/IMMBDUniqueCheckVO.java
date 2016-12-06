/**
 * 
 */
package nc.itf.mmgp.bean;

/**
 * 唯一校验VO
 * <P>
 * 单表类型的基本档案，做的这个主要目的为了 <U>
 * <li>唯一性校验</li>
 * </P>
 * 
 * @author zjy
 * @deprecated 使用V6的新方式检查
 */
public interface IMMBDUniqueCheckVO extends IMMBDCheckVO {
    /**
     * 获得唯一性校验的字段
     * 
     * @return 唯一性校验的字段
     */
    String getUniqueCheckField();

    /**
     * 获得出错时提示信息
     * 
     * @return
     */
    String getDisplayMessage();

    /**
     * 出错时提示信息:
     * <p>
     * getUniqueCheckFieldName() 【getDisplayMessage()】已经存在，不能保存
     * 
     * @return
     */
    String getUniqueCheckFieldName();

}
