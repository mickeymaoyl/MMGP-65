package nc.ui.mmgp.uif2.ref;

import java.util.Arrays;

import nc.md.model.IBusinessEntity;

import org.apache.commons.lang.StringUtils;

/**
 * 通用参照。
 * <p>
 * 仅支持有元数据实体的数据--且改实体需要实现IBDObject。<br>
 * bd_refinfo里的 para1字段代表要显示的参照字段。<br>
 * 默认dr=0
 * 
 * @author wangweiu
 * 
 */
public class MMGPMetaDataRefModel extends AbstractMMGPMetaDataRefModel {

	public MMGPMetaDataRefModel(String refName) {
		super(refName);
	}

	@Override
	protected void initByMetaData(IBusinessEntity bean) {
		this.setOrderPart(getRefCodeField());
	}

	@Override
	public void setPara1(String para1) {
		super.setPara1(para1);
		if (StringUtils.isBlank(para1)) {
			return;
		}
		String[] fields = para1.split(";");
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] = this.getBean().getAttributeByName(fields[i])
					.getDisplayName();
		}
		this.setFieldCode(fields);
		this.setFieldName(fieldNames);
		String pkField = this.getBean().getPrimaryKey().getPKColumn().getName();
		if (Arrays.asList(fields).contains(pkField)) {
			this.setHiddenFieldCode(new String[] {});
		}

		setDefaultFieldCount(fields.length);
	}

	@Override
	public void setPara3(String para3) {

		super.setPara3(para3);
		if (StringUtils.isBlank(para3)) {
			return;
		}
		String[] fields = para3.split(";");

		if (fields == null || fields.length == 0) {
			return;
		}

		setResourceID(fields[0]);
		if (fields.length > 1) {
			setDataPowerOperation_code(fields[1]);
		}
		if (fields.length > 2) {
			setDataPowerColumn(fields[2]);
		}else{
			String pkField = this.getBean().getPrimaryKey().getPKColumn().getName();
			setDataPowerColumn(pkField);
		}
	}
}
