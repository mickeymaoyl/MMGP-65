package nc.vo.mmgp;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 全局常量
 * </p>
 * 
 * @since 创建日期 May 10, 2013
 * @author wangweir
 */
public class MMGlobalConst {
    private static final String BILL_TYPE_PURBILLLIST = "YG21";

    private static final String BILL_TYPE_ECC1 = "ECC1";

    private static final String BILL_TYPE_PREORDER = "38";

    private static final String BILL_TYPE_BORROWOUT = "4H";

    /**
     * 物资需求申请
     */
    private static final String BILL_TYPE_STOREREQ = "422X";

    /**
     * 借入单
     */
    private static final String BILL_TYPE_BORROWIN = "49";

    /**
     * 销售报价单
     */
    private static final String BILL_TYPE_SALEQUOTATION = "4310";

    /**
     * 销售合同
     */
    private static final String BILL_TYPE_CT = "Z3";

    /**
     * 采购结算
     */
    private static final String BILL_TYPE_POBALANCE = "27";

    /**
     * 附件入库
     */
    private static final String BILL_TYPE_ASSIN = "YG45";

    /**
     * 转库单
     */
    public static final String BILL_TYPE_WHTRANS = "4K";

    public static final String BILL_TYPE_SALEORDER = "30";

    /**
     * 
     */
    public static final String UMES_FILESYS = "80";

    /** 组织 */
    public static final String PK_ORG = "pk_org";

    /** 组织版本 */
    public static final String PK_ORG_V = "pk_org_v";

    /** 集团 */
    public static final String PK_GROUP = "pk_group";

    /** 单据类型 */
    public static final String VBILLTYPECODE = "vbilltypecode";

    /** 单据号 */
    public static final String VBILLCODE = "vbillcode";

    /** 单据日期 */
    public static final String DBILLDATE = "dbilldate";

    /** 单据状态 */
    public static final String FSTATUSFLAG = "fstatusflag";

    /** 制单人 */
    public static final String VBILLMAKER = "vbillmaker";

    /** 制单时间 */
    public static final String DMAKEDATE = "dmakedate";

    /** 创建人 */
    public static final String CREATOR = "creator";

    /** 创建时间 */
    public static final String CREATIONTIME = "creationtime";

    /**
     * 待审批;用在查询待审批
     */
    public static final String BISAPPROVING = "bisapproving";

    /**
     * 来源单据表体id
     */
    public static final String VSRCBID = "vsrcbid";

    /**
     * 来源单据类型
     */
    public static final String VSRCTYPE = "vsrctype";

    /**
     * 单据子表主键
     */
    public static final String BILLBID = "billbid";

    /**
     * 源头单据类型
     */
    public static final String VFIRSTTYPE = "vfirsttype";

    /**
     * 源头单据表体id
     */
    public static final String VFIRSTBID = "vfirstbid";

    /**
     * 单据类型：应收单
     */
    public static final String BILL_TYPE_RECEIVABLE = "F0";

    /**
     * 单据类型：请购单
     */
    public static final String BILL_TYPE_PRAY = "20";

    /**
     * 单据类型：采购订单
     */
    public static final String BILL_TYPE_ORDER = "21";

    /**
     * 单据类型：到货单
     */
    public static final String BILL_TYPE_ARRIVE = "23";

    /**
     * 单据类型：项目采购计划
     */
    public static final String BILL_TYPE_PROCPLAN = "YG23";

    /** 作业单据类型 */
    public static final String BILL_TYPE_TASK = "YG65";

    /**
     * 调入申请
     */
    public static final String BILL_TYPE_TRANSINREQ = "5A";

    /**
     * 作业申请
     */
    public static final String BILL_TYPE_TASKREQ = "55A8";

    /**
     * 投放计划
     */
    public static final String BILL_TYPE_PUTPLAN = "55C7";

    /**
     * 调拨订单
     */
    public static final String BILL_TYPE_TO = "5X";

    /**
     * 部件预投
     */
    public static final String BILL_TYPE_COMPONENT = "YG72";

    /**
     * 生产订单
     */
    public static final String BILL_TYPE_PRODUCE = "55C2";

    /**
     * 完工报告
     */
    public static final String BILL_TYPE_WR = "55A4";

    /**
     * 备料计划
     */
    public static final String BILL_TYPE_PICKM = "55A3";

    /**
     * 询报价单
     */
    public static final String BILL_TYPE_ASKBILL = "29";

    /**
     * 委托加工入库
     */
    public static final String BILL_TYPE_SUBCONTIN = "47";

    /**
     * 采购发票
     */
    public static final String BILL_TYPE_INVOICE = "25";

    /**
     * 采购合同
     */
    public static final String BILL_TYPE_CTPU = "Z2";

    /**
     * 价格审批
     */
    public static final String BILL_TYPE_PRICEAUDIT = "28";

    /**
     * 委外订单
     */
    public static final String BILL_TYPE_SCORDER = "61";

    /**
     * 收款单
     */
    public static final String BILL_TYPE_GATHERINGBILL_F2 = "F2";

    /**
     * 收款单
     */
    public static final String BILL_TYPE_GATHERINGBILL_D2 = "D2";

    /**
     * 产成品入
     */
    public static final String BILL_TYPE_PRODIN = "46";

    /**
     * 采购入
     */
    public static final String BILL_TYPE_PURCHASEIN = "45";

    /**
     * 设备卡片
     */
    public static final String BILL_TYPE_EQUIP = "4A00";

    /**
     * 其他出
     */
    public static final String BILL_TYPE_OTHREROUT = "4I";

    /**
     * 设备入
     */
    private static final String BILL_TYPE_EQUIPIN = "4401";

    /**
     * 生产报废入
     */
    private static final String BILL_TYPE_PRODSCRBIN = "4X";

    /**
     * 调拨入
     */
    private static final String BILL_TYPE_TRANSIN = "4E";

    /**
     * 其他入
     */
    private static final String BILL_TYPE_OTHERIN = "4A";

    /**
     * 调拨出
     */
    private static final String BILL_TYPE_TRANSOUT = "4Y";

    /**
     * 出库申请
     */
    private static final String BILL_TYPE_OUTREQ = "4451";

    /**
     * 设备出
     */
    private static final String BILL_TYPE_EQUIPOUT = "4455";

    /**
     * 销售出
     */
    public static final String BILL_TYPE_SALEOUT = "4C";

    /**
     * 材料出
     */
    public static final String BILL_TYPE_MATROUT = "4D";

    /**
     * 寻源来源表体ID默认参数Map<单据类型,表体ID字段>
     */
    public static final Map<String, String> TASK_TYPE_VSRCBID_MAP = new HashMap<String, String>();

    static {
        // 请购单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRAY, "csourcebid");
        // 采购订单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ORDER, "csourcebid");
        // 到货单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ARRIVE, "csourcebid");
        // 委外订单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SCORDER, "csrcbid");
        // 价格审批单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRICEAUDIT, "csrcbid");
        // 采购合同
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_CTPU, "csrcbid");
        // 采购发票
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_INVOICE, "csourcebid");
        // 委托加工入库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SUBCONTIN, "csourcebillbid");
        // 询报价单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ASKBILL, "csrcbid");
        // 备料计划
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PICKM, "csourcebillrowid");
        // 生产报告
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_WR, "vbsrcrowid");
        // 调拨订单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TO, "csrcbid");
        // 离散投放计划
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PUTPLAN, "vsrcrowid");
        // 作业申报
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TASKREQ, "csrcrowid");
        // 调入申请
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TRANSINREQ, "csrcbid");
        // 采购入库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PURCHASEIN, "csourcebillbid");
        // 产成品入库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRODIN, "csourcebillbid");
        // 收款单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_GATHERINGBILL_D2, "src_itemid");
        // 收款单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_GATHERINGBILL_F2, "src_itemid");
        // 设备卡片
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_EQUIP, "pk_bill_b_src");
        // 其他出
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_OTHREROUT, "csourcebillbid");
        // 材料出库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_MATROUT, "csourcebillbid");
        // 销售出库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SALEOUT, "csourcebillbid");
        // 设备出库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_EQUIPOUT, "csourcebillbid");
        // 出库申请
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_OUTREQ, "csourcebillbid");
        // 库存调拨出库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TRANSOUT, "csourcebillbid");
        // 其他入
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_OTHERIN, "csourcebillbid");
        // 调拨入
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TRANSIN, "csourcebillbid");
        // 生产报废入
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRODSCRBIN, "csourcebillbid");
        // 设备入
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_EQUIPIN, "csourcebillbid");
        // 采购结算
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_POBALANCE, "pk_stock_b");
        // 销售
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SALEORDER, "csrcbid");

        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_WHTRANS, "csourcebillbid");
        // 生产订单跟默认一致
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PRODUCE, "vsrcbid");
        // 附件入库
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_ASSIN, "csourcebillbid");
        // 销售合同
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_CT, "csrcbid");
        // 销售报价单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_SALEQUOTATION, "csrcbid");
        // 物资需求申请
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_STOREREQ, "csourcebid");
        // 库存借入单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_BORROWIN, "csourcebillbid");
        // 库存借出单
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_BORROWOUT, "csourcebillbid");

        // 定额单
        TASK_TYPE_VSRCBID_MAP.put("YG41", "vsrcbid");
        // 定额变更单
        TASK_TYPE_VSRCBID_MAP.put("YG42", "vsrcbid");
        // 项目库存转移单
        TASK_TYPE_VSRCBID_MAP.put("YG44", "vsrcbid");

        // 采购清单变更单
        TASK_TYPE_VSRCBID_MAP.put("YG22", "vsrcbid");
        // 项目采购计划
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_PROCPLAN, "vsrcbid");
        // 项目采购替代申请
        TASK_TYPE_VSRCBID_MAP.put("YG24", "vsrcbid");

        // 集配计划
        TASK_TYPE_VSRCBID_MAP.put("YG31", "vsrcbid");
        // 配送单
        TASK_TYPE_VSRCBID_MAP.put("YG32", "vsrcbid");
        // 集配替代申请
        TASK_TYPE_VSRCBID_MAP.put("YG33", "vsrcbid");
        // 自制件加工计划
        TASK_TYPE_VSRCBID_MAP.put("YG35", "vsrcbid");

        // 立项单
        TASK_TYPE_VSRCBID_MAP.put("YG61", "vsrcbid");
        // 作业进度申报单
        TASK_TYPE_VSRCBID_MAP.put("YG63", "vsrcbid");
        // 作业
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_TASK, "csrcbid");
        // WBS
        TASK_TYPE_VSRCBID_MAP.put("YG66", "csrcbid");
        // 计划模板-WBS
        TASK_TYPE_VSRCBID_MAP.put("YG68", "vsrcbid");
        // 计划模板-作业
        TASK_TYPE_VSRCBID_MAP.put("YG69", "vsrcbid");

        // 项目主生产计划
        TASK_TYPE_VSRCBID_MAP.put("YG71", "vsrcbid");
        // 部件预投
        TASK_TYPE_VSRCBID_MAP.put(BILL_TYPE_COMPONENT, "vsrcbid");

        // 项目计划订单
        TASK_TYPE_VSRCBID_MAP.put("YG81", "vsrcbid");

    }
}
