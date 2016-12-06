package nc.ui.mmgp.flexgant.view.treetable.assistant;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.pub.IGantTableData;
import nc.ui.mmgp.flexgant.view.treetable.userdefitem.MMGPGantMarAsstEditDelegate;
import nc.ui.uif2.userdefitem.UserDefItemContainer;
import nc.vo.bd.userdefrule.UserdefitemVO;

/**
 * @Description: 甘特图左边treetable物料辅助属性处理器 可以直接在配置文件中使用
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-6-3上午9:32:43
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
	 *            生产厂商字段
	 */
	public void setProductorField(String productorField) {
		this.getMarAsstEditDelegate().setProductorField(productorField);
	}

	/**
	 * @param projectField
	 *            项目字段
	 */
	public void setProjectField(String projectField) {
		this.getMarAsstEditDelegate().setProjectField(projectField);
	}

	/**
	 * @param qualityLevelField
	 *            库存状态字段
	 */
	public void setStoreStateField(String storeStateField) {
		this.getMarAsstEditDelegate().setStoreStateField(storeStateField);
	}

	/**
	 * @param supplierField
	 *            供应商字段
	 */
	public void setSupplierField(String supplierField) {
		this.getMarAsstEditDelegate().setSupplierField(supplierField);
	}

	/**
	 * @param customerField
	 *            客户字段
	 */
	public void setCustomerField(String customerField) {
		this.getMarAsstEditDelegate().setCustomerField(customerField);
	}

	/**
	 * @param materialField
	 *            辅料字段
	 */
	public void setMaterialField(String materialField) {
		this.getMarAsstEditDelegate().setMaterialField(materialField);
	}

}
