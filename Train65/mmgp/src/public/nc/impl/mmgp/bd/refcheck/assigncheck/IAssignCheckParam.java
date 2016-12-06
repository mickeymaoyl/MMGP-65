package nc.impl.mmgp.bd.refcheck.assigncheck;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 物料、供应商、客户三大档案“取消分配前校验”和“参数改为强分配校验”前校验参数接口
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
 * @author wangweir
 */
public interface IAssignCheckParam {

    /**
     * 集团字段
     * 
     * @return
     */
    String getGroupField();

    /**
     * 组织字段
     * 
     * @return
     */
    String getOrgField();

    /**
     * 档案字段
     * 
     * @return
     */
    String[] getDocField();

    /**
     * 引用档案名称/单据名称。用于错误提示信息
     * 
     * @return
     */
    String getErrorMsg();

    /**
     * 获取表名
     * 
     * @return
     */
    String getTableName();
}
