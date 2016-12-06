package nc.ui.mmgp.uif2.utils.ref;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���Ϲ��˹�����,Ŀǰ֧�ְ����������͹���
 * </p>
 * 
 * @since �������� Jul 1, 2013
 * @author wangweir
 */
public class MMGPFilterMaterialRefUtil {

    /**
     * ����
     */
    private UIRefPane materialRef;

    /**
     * ��֯
     */
    private String pk_org;

    /**
     * 
     */
    public MMGPFilterMaterialRefUtil(UIRefPane materialRef,
                                     String pk_org) {
        this(materialRef);
        this.pk_org = pk_org;
    }

    /**
     * 
     */
    public MMGPFilterMaterialRefUtil(BillItem materialItem,
                                     String pk_org) {
        this(materialItem);
        this.pk_org = pk_org;
    }

    /**
     * 
     */
    public MMGPFilterMaterialRefUtil(UIRefPane materialRef) {
        this.materialRef = materialRef;
        initPkOrg();
    }

    /**
     * 
     */
    public MMGPFilterMaterialRefUtil(BillItem materialItem) {
        if (materialItem == null) {
            return;
        }
        this.materialRef = (UIRefPane) materialItem.getComponent();
        initPkOrg();
    }

    protected void initPkOrg() {
        if (this.materialRef != null) {
            this.pk_org = materialRef.getRefModel().getPk_org();
        }
    }

    /**
     * @return the materialRef
     */
    protected UIRefPane getMaterialRef() {
        return materialRef;
    }

    /**
     * ���������
     * 
     * @return
     */
    public MMGPFilterMaterialRefUtil filterMR() {
        return filterByMarType(IMaterialEnumConst.MATERTYPE_MR);
    }

    /**
     * ����ί���
     * 
     * @return
     */
    public MMGPFilterMaterialRefUtil filterOT() {
        return filterByMarType(IMaterialEnumConst.MATERTYPE_OT);
    }

    /**
     * �������������ί���
     * 
     * @return
     */
    public MMGPFilterMaterialRefUtil filterMROrOT() {
        return this.filterByMarType(new String[]{IMaterialEnumConst.MATERTYPE_MR, IMaterialEnumConst.MATERTYPE_OT });
    }

    /**
     * ���˸������Ͽ����Ϣ-��������
     * 
     * @param filterTypes
     *        ���Ͽ����Ϣ-��������
     * @return
     */
    public MMGPFilterMaterialRefUtil filterByMarType(String... filterTypes) {
        if (MMStringUtil.isEmpty(pk_org) || this.materialRef == null) {
            return this;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder
            .append(" and bd_material.pk_material in (select pk_material from bd_materialstock stock ")
            .append(" where stock.dr = 0 and stock.pk_org = '")
            .append(this.pk_org)
            .append("'");

        if (MMArrayUtil.isEmpty(filterTypes)) {
            return this;
        }

        sqlBuilder.append(" and (stock.martype='").append(filterTypes[0]).append("'");

        for (int i = 1; i < filterTypes.length; i++) {
            sqlBuilder.append(" or stock.martype='").append(filterTypes[i]).append("'");
        }

        sqlBuilder.append(" )) ");

        this.materialRef.getRefModel().addWherePart(sqlBuilder.toString());
        return this;
    }

}
