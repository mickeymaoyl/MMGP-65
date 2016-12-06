package nc.ui.mmgp.flexgant.view.treetable.assistant;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.pub.IGantTableData;
import nc.ui.mmgp.flexgant.view.treetable.userdefitem.MMGPGantMarAsstEditDelegate;
import nc.ui.uif2.userdefitem.UserDefItemContainer;
import nc.vo.bd.userdefrule.UserdefitemVO;

/**
 * @Description: ����ͼ���treetable���ϸ������Դ����� ����ֱ���������ļ���ʹ��
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-6-3����9:32:43
 * @author: tangxya
 */
public class MMGPGantMarAssPrepator implements IGantTableData {

	private UserDefItemContainer container;

	private MMGPGantMarAsstEditDelegate editDelegate = null;

	public UserDefItemContainer getContainer() {
		return this.container;
	}

	public void setContainer(UserDefItemContainer container) {
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.mmgp.flexgant.pub.IGantTableData#prepareGantTableData()
	 */
	@Override
	public void prepareGantTableData(MMGPGantChartModel chartModel) {
		if (this.container != null) {
			UserdefitemVO[] userdefitemVOs = this.container
					.getUserdefitemVOsByUserdefruleCode(MMGPGantMarAssistantUtils.MARASSISTANTRULECODE);
			MMGPGantMarAssistantUtils.updateMarAssistantGantItem(chartModel,
					this.getMarAsstEditDelegate().getPrefix(), userdefitemVOs);
		}

	}

	public MMGPGantMarAsstEditDelegate getMarAsstEditDelegate() {

		if (this.editDelegate == null)
			editDelegate = new MMGPGantMarAsstEditDelegate();
		return this.editDelegate;
	}

	public void setPrefix(String prefix) {
		this.getMarAsstEditDelegate().setPrefix(prefix);
	}

	/**
	 * @param productorField
	 *            ���������ֶ�
	 */
	public void setProductorField(String productorField) {
		this.getMarAsstEditDelegate().setProductorField(productorField);
	}

	/**
	 * @param projectField
	 *            ��Ŀ�ֶ�
	 */
	public void setProjectField(String projectField) {
		this.getMarAsstEditDelegate().setProjectField(projectField);
	}

	/**
	 * @param qualityLevelField
	 *            ���״̬�ֶ�
	 */
	public void setStoreStateField(String storeStateField) {
		this.getMarAsstEditDelegate().setStoreStateField(storeStateField);
	}

	/**
	 * @param supplierField
	 *            ��Ӧ���ֶ�
	 */
	public void setSupplierField(String supplierField) {
		this.getMarAsstEditDelegate().setSupplierField(supplierField);
	}

	/**
	 * @param customerField
	 *            �ͻ��ֶ�
	 */
	public void setCustomerField(String customerField) {
		this.getMarAsstEditDelegate().setCustomerField(customerField);
	}

	/**
	 * @param materialField
	 *            �����ֶ�
	 */
	public void setMaterialField(String materialField) {
		this.getMarAsstEditDelegate().setMaterialField(materialField);
	}

}
