package nc.ui.mmgp.uif2.query;

import java.util.ArrayList;
import java.util.List;

import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 物料负责属性查询条件处理用的参数bean
 * </p>
 * 
 * @since 创建日期 Sep 17, 2013
 * @author wangweir
 */
public class MaterialQueryCondParam {

    /**
     * 物料vid
     */
    private String materialidfiled;

    /**
     * 物料oid
     */
    private String materialoidfield;

    /**
     * 生产厂商
     */
    private String productorField;

    /**
     * 项目
     */
    private String projectField;

    /**
     * 库存状态
     */
    private String storeStateField;

    /**
     * 供应商
     */
    private String supplierField;

    /**
     * 客户字段
     */
    private String customerField;

    /**
     * 物料自由辅助属性前缀
     */
    private String prefix;

    /**
     * @return the materialidfiled
     */
    public String getMaterialidfiled() {
        return materialidfiled;
    }

    /**
     * @param materialidfiled
     *        the materialidfiled to set
     */
    public void setMaterialidfiled(String materialidfiled) {
        this.materialidfiled = materialidfiled;
    }

    /**
     * @return the materialoidfield
     */
    public String getMaterialoidfield() {
        return materialoidfield;
    }

    /**
     * @param materialoidfield
     *        the materialoidfield to set
     */
    public void setMaterialoidfield(String materialoidfield) {
        this.materialoidfield = materialoidfield;
    }

    /**
     * @return the productorField
     */
    public String getProductorField() {
        return productorField;
    }

    /**
     * @param productorField
     *        the productorField to set
     */
    public void setProductorField(String productorField) {
        this.productorField = productorField;
    }

    /**
     * @return the projectField
     */
    public String getProjectField() {
        return projectField;
    }

    /**
     * @param projectField
     *        the projectField to set
     */
    public void setProjectField(String projectField) {
        this.projectField = projectField;
    }

    /**
     * @return the storeStateField
     */
    public String getStoreStateField() {
        return storeStateField;
    }

    /**
     * @param storeStateField
     *        the storeStateField to set
     */
    public void setStoreStateField(String storeStateField) {
        this.storeStateField = storeStateField;
    }

    /**
     * @return the supplierField
     */
    public String getSupplierField() {
        return supplierField;
    }

    /**
     * @param supplierField
     *        the supplierField to set
     */
    public void setSupplierField(String supplierField) {
        this.supplierField = supplierField;
    }

    /**
     * @return the customerField
     */
    public String getCustomerField() {
        return customerField;
    }

    /**
     * @param customerField
     *        the customerField to set
     */
    public void setCustomerField(String customerField) {
        this.customerField = customerField;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *        the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String> getMarFixFields() {
        List<String> fixFieldCodes = new ArrayList<String>();
        if (MMStringUtil.isNotEmpty(this.getCustomerField())) {
            fixFieldCodes.add(this.getCustomerField());
        }

        if (MMStringUtil.isNotEmpty(this.getProductorField())) {
            fixFieldCodes.add(this.getProductorField());
        }

        if (MMStringUtil.isNotEmpty(this.getProjectField())) {
            fixFieldCodes.add(this.getProjectField());
        }

        if (MMStringUtil.isNotEmpty(this.getSupplierField())) {
            fixFieldCodes.add(this.getSupplierField());
        }

        if (MMStringUtil.isNotEmpty(this.getStoreStateField())) {
            fixFieldCodes.add(this.getStoreStateField());
        }

        return fixFieldCodes;
    }
}
