package nc.ui.mmgp.uif2.actions.report;

import nc.pub.smart.data.IRowData;

/**
 * <b> �������鵥������ </b>
 * <p>
 * �ṩ��Ĭ��ʵ�ַ�ʽ������Ҫ�������дgetSingleBillPk������
 * </p>
 * 
 * @since �������� 2014-1-13
 * @author dongchx
 */
public class MMGPRptCommonLinkQueryBillAction extends MMGPRptLinkQueryBillBaseAction {

    /**
     * ���������������ֶ�����
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
        // ȡ��ѡ���е�������
        Object value = rowData.getData(this.getPkAttributeName());
        String billPk = value == null ? null : value.toString();
        return new String[]{billPk };
    }

}
