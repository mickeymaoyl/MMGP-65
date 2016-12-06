package nc.ui.mmgp.uif2.scale;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Oct 14, 2013
 * @author wangweir
 */
public enum ScaleBeanType {
    /**
     * 数量辅数量
     */
    NumAssNum,

    /**
     * 换算率
     */
    HSL,
    /**
     * 重量精度
     */
    WEIGHT,
    /**
     * 存货成本单价，对应精度参数"SCM01"
     */
    COSTPRICE_SCALE,
    /**
     * 销售/采购价格精度，对应精度参数"NC004"
     */
    PS_PRICESCALE,
    /**
     * 税率精度
     */
    TAXRATE,
    /**
     * 集团本位币金额
     */
    GROUP_LOCAL_MNY,
    /**
     * 全局本币金额
     */
    GLOBAL_LOCAL_MNY,
    /**
     * 币钟精度
     */
    MONEY,
    /**
     * 组织汇率精度
     */
    ORGEXCHG,
    /**
     * 集团汇率精度
     */
    GROUPEXCHG,
    /**
     * 全局汇率精度
     */
    GLOBAEXCHG,
    /**
     * 业务参数--配置的是小数位数
     */
    Param,

    /**
     * 业务参数--配置的是计量单位
     */
    Param_MeasDoc,
}
