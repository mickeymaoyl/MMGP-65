package nc.ui.mmgp.uif2.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.uif2.validation.ValidationFailure;
import nc.bs.uif2.validation.Validator;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMListUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.AggregatedValueObject;

/**
 * ����ǰ̨Ψһ��У��
 * @author wangrra
 *
 */
public class MMGPBodyUniqueValidator implements Validator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5532978913355682917L;

	private AbstractAppModel model;

	private BillForm editor;

	private List<String> bodyUniqueCfg;

	@Override
	public ValidationFailure validate(Object obj) {

		if (!(obj instanceof AggregatedValueObject)
				|| MMListUtil.isEmpty(bodyUniqueCfg)) {
			return null;
		}

		StringBuilder sbBuilder = new StringBuilder();
		for (String tagCfg : bodyUniqueCfg) {
			if (tagCfg == null || tagCfg.trim().length() == 0) {
				continue;
			}
			String[] params = tagCfg.split(":");
			String item = null;
			String table = null;
			if (params.length == 2) {
				if (params[0] != null
						&& !MMStringUtil.isEmpty(params[0].trim())) {
					// ��һ��ܿ�
					item = params[0];
					table = params[1];
				} else {
					continue;
				}
			} else if (params.length == 1) {
				if (tagCfg.indexOf(":") < 0) {
					// ������":"
					item = params[0];
				} else {
					continue;
				}
			} else {
				continue;
			}

			String checkResult = checkBodyUnique(item, table);
			if (!MMStringUtil.isEmpty(checkResult)) {
				sbBuilder.append(checkResult);
				sbBuilder.append("\n");
			}
		}

		if (sbBuilder.length() > 0) {
			return new ValidationFailure(sbBuilder.toString());
		}
		return null;
	}

	public List<String> getBodyUniqueCfg() {
		return bodyUniqueCfg;
	}

	public void setBodyUniqueCfg(List<String> bodyUniqueCfg) {
		this.bodyUniqueCfg = bodyUniqueCfg;
	}

	private String checkBodyUnique(String itemCode, String tabCode) {

		if (MMStringUtil.isEmpty(itemCode)) {
			return null;
		}

		String[] tabCodes = getBillCardPanel().getBillData()
				.getBodyTableCodes();
		if (MMArrayUtil.isEmpty(tabCodes)) {
			return null;
		}

		if (MMStringUtil.isEmpty(tabCode)) {
			tabCode = tabCodes[0];
		}

		BillItem billItem = getBillCardPanel().getBillData().getBodyItem(
				tabCode, itemCode);

		// �ֶ����Ƿ���ȷ �ҡ��ֶ������Ƿ�Ϊ���ջ����ַ�
		if (billItem == null
				|| (IBillItem.UFREF != billItem.getDataType() && (IBillItem.STRING != billItem
						.getDataType() && IBillItem.MULTILANGTEXT != billItem.getDataType()))) {
			return null;
		}

		int rowCount = getBillCardPanel().getBillModel(tabCode).getRowCount();

		if (rowCount <= 0) {
			return null;
		}

		List<String> pkValue = new ArrayList<String>();
		int emptyCount = 0;
		for (int i = 0; i < rowCount; i++) {
			Object pkObj = getBillCardPanel().getBillModel(tabCode)
					.getValueObjectAt(i, itemCode);
			String pk = MMStringUtil.objectToString(pkObj);
			if (MMStringUtil.isEmpty(pk)) {
				emptyCount++;
			} else {
				pkValue.add(pk);
			}
		}

		String tabName = getBillCardPanel().getBillData().getBodyTableName(
				tabCode);
		String hintTableName = null;
		if (tabCodes.length > 1) {
			hintTableName = NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0102", null, new String[]{tabName})/*ҳǩ��{0}��*/;
		} else {
			hintTableName = NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0103")/*����*/;
		}

		// ����ǿ��������������֤ʧ��
		String displayName = billItem.getName();

		StringBuilder sBuilder = new StringBuilder();
		if (emptyCount > 1) {
			sBuilder.append(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0104", null, new String[]{hintTableName,displayName})/*{0}��{1}���ж���1����ֵ*/);
			sBuilder.append("\n");
		}

		Set<String> repeatedValueList = findRepeat(pkValue);
		if (repeatedValueList == null || repeatedValueList.size() == 0) {
			return sBuilder.toString();
		}

		sBuilder.append(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0105", null, new String[]{hintTableName,displayName})/*{0}��{1}�����������ظ�*/);
		for (String repeatedValue : repeatedValueList) {
			sBuilder.append(" [" + repeatedValue + "]");
		}
		return sBuilder.toString();

	}

	private Set<String> findRepeat(List<String> pkValues) {
		Set<String> repeatedSet = new HashSet<String>();
		for (int i = 0; i < pkValues.size() - 1; i++) {
			for (int j = i + 1; j < pkValues.size(); j++) {
				if (pkValues.get(i).equals(pkValues.get(j))) {
					repeatedSet.add(pkValues.get(i));
					continue;
				}
			}
		}
		return repeatedSet;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BillCardPanel getBillCardPanel() {
		if (editor != null) {
			return editor.getBillCardPanel();
		}
		return null;
	}

}
