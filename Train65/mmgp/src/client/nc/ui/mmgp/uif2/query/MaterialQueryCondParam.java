package nc.ui.mmgp.uif2.query;

import java.util.ArrayList;
import java.util.List;

import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���ϸ������Բ�ѯ���������õĲ���bean
 * </p>
 * 
 * @since �������� Sep 17, 2013
 * @author wangweir
 */
public class MaterialQueryCondParam {

    /**
     * ����vid
     */
    private String materialidfiled;

    /**
     * ����oid
     */
    private String materialoidfield;

    /**
     * ��������
     */
    private String productorField;

    /**
     * ��Ŀ
     */
    private String projectField;

    /**
     * ���״̬
     */
    private String storeStateField;

    /**
     * ��Ӧ��
     */
    private String supplierField;

    /**
     * �ͻ��ֶ�
     */
    private String customerField;

    /**
     * �������ɸ�������ǰ׺
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
