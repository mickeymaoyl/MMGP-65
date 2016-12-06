package nc.ui.mmgp.flexgant.view.treetable.userdefitem;

import java.util.HashMap;
import java.util.Map;

import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.pubitf.uapbd.assistant.IMarAssistant;
import nc.pubitf.uapbd.assistant.MarAssistantFactory;
import nc.ui.dbcache.DBCacheFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * @Description: TODO
 *               <p>
 *               仿照MarAsstBillModelEditDelegate类，后续添加
 *               </p>
 * @data:2014-6-3上午9:40:24
 * @author: tangxya
 */
public class MMGPGantMarAsstEditDelegate {

	private Map<String, IMarAssistant> assistantCache = new HashMap<String, IMarAssistant>();

	/** 5 个固定辅助属性的字段名称，为了转换方便，设置为长度6 */
	private String[] fixedItemField = new String[6];

	private String materialField;

	private String prefix;

	/**
	 * @param materialField
	 *            辅料字段
	 */
	public void setMaterialField(String materialField) {
		this.materialField = materialField;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param productorField
	 *            生产厂商字段
	 */
	public void setProductorField(String productorField) {
		this.fixedItemField[4] = productorField;
	}

	/**
	 * @param projectField
	 *            项目字段
	 */
	public void setProjectField(String projectField) {
		this.fixedItemField[2] = projectField;
	}

	/**
	 * @param qualityLevelField
	 *            库存状态字段
	 */
	public void setStoreStateField(String storeStateField) {
		this.fixedItemField[1] = storeStateField;
	}

	/**
	 * @param supplierField
	 *            供应商字段
	 */
	public void setSupplierField(String supplierField) {
		this.fixedItemField[3] = supplierField;
	}

	/**
	 * @param customerField
	 *            客户字段
	 */
	public void setCustomerField(String customerField) {
		this.fixedItemField[5] = customerField;
	}

	/**
	 * 从给定辅助属性字段获得辅助属性序号，对应平台设置自定义项的编码，从1到15
	 * 
	 * @param field
	 *            辅助属性字段
	 * @return 辅助属性序号
	 */
	protected int getSeqFromField(String field) {
		for (int i = 0; i < this.fixedItemField.length; i++) {
			if (field.equals(this.fixedItemField[i])) {
				return i;
			}
		}
		return Integer.parseInt(field.substring(this.getPrefix().length())) + 5;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getMaterialField() {
		return this.materialField;
	}

	private IMarAssistant getAssistant(String pkMaterial) {
		if (this.assistantCache.containsKey(pkMaterial)) {
			// 避免对应辅助属性组为空产生多次远程调用，如果cache中包含该物料属性对应键值对即返回
			return this.assistantCache.get(pkMaterial);
		} else {
			IMarAssistant assistant = null;
			String pkMarasstframe = this
					.getAssistantFrameByMaterial(pkMaterial);

			try {
				assistant = MarAssistantFactory.getInstance()
						.getMarAssistantByMarasstFrame(pkMarasstframe);
				// 修正单据界面编码和平台设置编码不一致的wrapper
				// assistant = MarAssistantUtils.createWrapper(assistant);
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
			this.assistantCache.put(pkMaterial, assistant);
			return assistant;
		}

	}

	/**
	 * 根据物料pk获取辅助属性结构pk
	 * 
	 * @param pkMaterial
	 *            物料pk
	 * @return 辅助属性结构pk
	 */
	private String getAssistantFrameByMaterial(String pkMaterial) {
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pkMaterial);
		String sql = "select pk_marasstframe from bd_material where pk_material = ?";
		return (String) ((Object[]) DBCacheFacade.runQuery(sql, parameter,
				new ArrayProcessor()))[0];
	}

}
