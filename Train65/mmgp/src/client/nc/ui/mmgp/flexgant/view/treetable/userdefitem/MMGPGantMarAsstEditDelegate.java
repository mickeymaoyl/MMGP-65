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
 *               ����MarAsstBillModelEditDelegate�࣬�������
 *               </p>
 * @data:2014-6-3����9:40:24
 * @author: tangxya
 */
public class MMGPGantMarAsstEditDelegate {

	private Map<String, IMarAssistant> assistantCache = new HashMap<String, IMarAssistant>();

	/** 5 ���̶��������Ե��ֶ����ƣ�Ϊ��ת�����㣬����Ϊ����6 */
	private String[] fixedItemField = new String[6];

	private String materialField;

	private String prefix;

	/**
	 * @param materialField
	 *            �����ֶ�
	 */
	public void setMaterialField(String materialField) {
		this.materialField = materialField;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param productorField
	 *            ���������ֶ�
	 */
	public void setProductorField(String productorField) {
		this.fixedItemField[4] = productorField;
	}

	/**
	 * @param projectField
	 *            ��Ŀ�ֶ�
	 */
	public void setProjectField(String projectField) {
		this.fixedItemField[2] = projectField;
	}

	/**
	 * @param qualityLevelField
	 *            ���״̬�ֶ�
	 */
	public void setStoreStateField(String storeStateField) {
		this.fixedItemField[1] = storeStateField;
	}

	/**
	 * @param supplierField
	 *            ��Ӧ���ֶ�
	 */
	public void setSupplierField(String supplierField) {
		this.fixedItemField[3] = supplierField;
	}

	/**
	 * @param customerField
	 *            �ͻ��ֶ�
	 */
	public void setCustomerField(String customerField) {
		this.fixedItemField[5] = customerField;
	}

	/**
	 * �Ӹ������������ֶλ�ø���������ţ���Ӧƽ̨�����Զ�����ı��룬��1��15
	 * 
	 * @param field
	 *            ���������ֶ�
	 * @return �����������
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
			// �����Ӧ����������Ϊ�ղ������Զ�̵��ã����cache�а������������Զ�Ӧ��ֵ�Լ�����
			return this.assistantCache.get(pkMaterial);
		} else {
			IMarAssistant assistant = null;
			String pkMarasstframe = this
					.getAssistantFrameByMaterial(pkMaterial);

			try {
				assistant = MarAssistantFactory.getInstance()
						.getMarAssistantByMarasstFrame(pkMarasstframe);
				// �������ݽ�������ƽ̨���ñ��벻һ�µ�wrapper
				// assistant = MarAssistantUtils.createWrapper(assistant);
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
			this.assistantCache.put(pkMaterial, assistant);
			return assistant;
		}

	}

	/**
	 * ��������pk��ȡ�������Խṹpk
	 * 
	 * @param pkMaterial
	 *            ����pk
	 * @return �������Խṹpk
	 */
	private String getAssistantFrameByMaterial(String pkMaterial) {
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pkMaterial);
		String sql = "select pk_marasstframe from bd_material where pk_material = ?";
		return (String) ((Object[]) DBCacheFacade.runQuery(sql, parameter,
				new ArrayProcessor()))[0];
	}

}
