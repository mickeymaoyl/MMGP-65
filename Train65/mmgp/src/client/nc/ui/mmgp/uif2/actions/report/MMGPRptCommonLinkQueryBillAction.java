package nc.ui.mmgp.uif2.actions.report;

import nc.pub.smart.data.IRowData;

/**
 * <b> 报表联查单条单据 </b>
 * <p>
 * 提供了默认实现方式，如需要子类可重写getSingleBillPk方法。
 * </p>
 * 
 * @since 创建日期 2014-1-13
 * @author dongchx
 */
public class MMGPRptCommonLinkQueryBillAction extends MMGPRptLinkQueryBillBaseAction {

    /**
     * 保存主表主键的字段名称
     */
    private String pkAttributeName;

    public MMGPRptCommonLinkQueryBillAction(String actionCode,
                                            String actionName,
                                            String nodeCode) {
        super(actionCode, actionName, nodeCode);
    }

    public MMGPRptCommonLinkQueryBillAction(String actionCode,
                                            String actionName,
                                            String nodeCode,
                                            String pkAttributeName) {
        this(actionCode, actionName, nodeCode);
        this.pkAttributeName = pkAttributeName;
    }

    public String getPkAttributeName() {
        return pkAttributeName;
    }

    public void setPkAttributeName(String pkAttributeName) {
        this.pkAttributeName = pkAttributeName;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.mmgp.uif2.actions.report.MMGPRptLinkQueryBillBaseAction#getBillPks(nc.pub.smart.data.IRowData)
     */
    @Override
    protected String[] getLinkBillPks(IRowData rowData) {
        // 取得选中行单据主键
        Object value = rowData.getData(this.getPkAttributeName());
        String billPk = value == null ? null : value.toString();
        return new String[]{billPk };
    }

}
