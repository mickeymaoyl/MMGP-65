package nc.itf.mmgp.bean;

/**
 * <b> 多个字段的重复性校验 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-5-15
 * 
 * @author Administrator
 * @deprecated 使用V6的新方式检查
 */
public interface IMMBDMulUniqueCheckVO extends IMMBDCheckVO {
    /**
     * 获得唯一性校验的字段
     * 
     * @return 唯一性校验的字段
     */
    String[] getUniqueCheckFields();

    String[] getUniqueCheckFieldNames();

}
