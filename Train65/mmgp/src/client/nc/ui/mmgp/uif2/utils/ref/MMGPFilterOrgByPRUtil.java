package nc.ui.mmgp.uif2.utils.ref;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 根据计划委托关系过滤库存组织参照
 * </p>
 * 
 * @since 创建日期 Jul 9, 2013
 * @author wangweir
 */
public class MMGPFilterOrgByPRUtil {

    /**
     * 
     */
    private UIRefPane orgRefPane;

    private String pk_org;

    /**
     * 
     */
    public MMGPFilterOrgByPRUtil(UIRefPane orgRefPane,
                                 String pk_org) {
        this.orgRefPane = orgRefPane;
        this.pk_org = pk_org;
    }

    /**
     * 
     */
    public MMGPFilterOrgByPRUtil(BillItem orgBillItem,
                                 String pk_org) {
        this.orgRefPane = (UIRefPane) orgBillItem.getComponent();
        this.pk_org = pk_org;
    }

    public MMGPFilterOrgByPRUtil filterByPlanRelation() {
        StringBuilder sql = new StringBuilder();
        sql.append(" and org_stockorg.pk_stockorg in (");

        sql.append("select pd_pr_stockorg.cstockorgid");
        sql.append("  from pd_pr_stockorg");
        sql.append(" inner join pd_pr");
        sql.append("    on pd_pr.pk_pr = pd_pr_stockorg.pk_pr");
        sql.append(" where pd_pr.dr = 0");
        sql.append("   and pd_pr_stockorg.dr = 0");
        sql.append("   and pd_pr.relationtype = 1 ");//RelationTypeEnum.PLAN_RELATION.value();
        sql.append("   and pd_pr.pk_org = '").append(this.pk_org).append("') ");

        this.orgRefPane.getRefModel().addWherePart(sql.toString());
        return this;
    }

}
