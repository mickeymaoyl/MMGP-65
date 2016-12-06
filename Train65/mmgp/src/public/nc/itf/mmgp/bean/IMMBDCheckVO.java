package nc.itf.mmgp.bean;

/**
 * <b> MM共通检查基本接口 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-5-14
 * 
 * @author wangweiu
 * @deprecated 使用V6的新方式检查
 */
public interface IMMBDCheckVO {
    /**
     * 获得表名
     * 
     * @return 获得表名
     */
    String getTableName();

    /**
     * 获得主键字段
     * 
     * @return 主键字段
     */
    String getPKFieldName();

    Object getAttributeValue(String attributeName);
}
