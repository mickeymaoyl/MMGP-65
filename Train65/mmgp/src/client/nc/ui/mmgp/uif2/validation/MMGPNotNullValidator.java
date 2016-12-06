package nc.ui.mmgp.uif2.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.validation.BillNotNullValidator;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMListUtil;
import nc.vo.pub.AggregatedValueObject;

/**
 * 
 * <b> ǰ̨�ǿ�У�飨����ģ������
 * <p>
 * ����ҵ� @see nc.bs.uif2.validation.DefaultValidationService �� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2012-11-23
 * 
 * @author wangweiu
 */
public class MMGPNotNullValidator extends AbsractMMGPValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1098569167126150300L;

	private BillForm editor = null;
	private BillNotNullValidator templateNullValidator;

	private List<String> notNullTabCodes;

	@Override
	public ValidationFailure validate(Object obj) {
		try {
			MMGPOrgValidateUtil.validateOrgNotNull(editor, obj);
		} catch (ValidationException e) {
			return new ValidationFailure(e.getMessage());
		}
		// ����ģ������ü����¼�ı�ѡ���Ƿ�������
		if (getNullValidator().validate(obj) != null) {
			return getNullValidator().validate(obj);
		}

		// ���ż���������Ƿ�Ϊ��
		return isBodyNull(obj);
	}

	public ValidationFailure isBodyNull(Object obj) {

		// ��õ���ģ�������е�ҳǩ
		String[] allTabCodes = getBillCardPanel().getBillData()
				.getBodyTableCodes();

		// �����жϵ�ǰ�Ƿ�Ϊ���ӱ�(���ӱ�)����������ǣ�����У������Ƿ�Ϊ�գ�ֱ�ӽ���
		if (!(obj instanceof AggregatedValueObject)
				|| MMArrayUtil.isEmpty(allTabCodes)) {
			return null;
		}

		if (MMListUtil.isEmpty(notNullTabCodes)) {
			// û�����ò���Ϊ�յ�ҳǩҳ������
			return null;
		}

		Set<String> notNullTabCodeSet = new HashSet<String>();

		for (String tabCode : notNullTabCodes) {
			if ("#all#".equals(tabCode)) {
				for (String tab : allTabCodes) {
					notNullTabCodeSet.add(tab);
				}
				break;
			} else if ("#default#".equals(tabCode)) {
				notNullTabCodeSet.add(allTabCodes[0]);
			} else {
				notNullTabCodeSet.add(tabCode);
			}
		}

		List<String> nullTabCodes = new ArrayList<String>();

		for (String tabCode : notNullTabCodeSet) {
			BillModel billModel = getBillCardPanel().getBillModel(tabCode);
			if (billModel == null) {
				// ҳǩ����ƴд����
				continue;
			}
			int rowCount = billModel.getRowCount();
			if (rowCount <= 0) {
				nullTabCodes.add(tabCode);
			}
		}

		if (MMListUtil.isEmpty(nullTabCodes)) {
			return null;
		}
		// ֻ��һ��ҳǩʱ wenjl add 2013-6-26
		if (1 == allTabCodes.length) {
			return new ValidationFailure(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0093")/*����Ϊ��*/);
		}

		StringBuilder sbBuilder = new StringBuilder();
		sbBuilder.append(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0094")/*ҳǩ*/);

		for (String nullTab : nullTabCodes) {
			// ���ҳǩ����
			String tabName = getBillCardPanel().getBillData().getBodyTableName(
					nullTab);
			sbBuilder.append(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0095", null, new String[]{tabName})/*��{0}����*/);
		}
		// ȥ�����һ������
		String result = sbBuilder.substring(0, sbBuilder.length() - 1);
		return new ValidationFailure(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0096", null, new String[]{result})/*{0}Ϊ��*/);
	}

	public BillNotNullValidator getNullValidator() {
		if (templateNullValidator == null) {
			templateNullValidator = new BillNotNullValidator(getEditor()
					.getBillCardPanel());
		}
		return templateNullValidator;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BillForm getEditor() {
		return editor;
	}

	public BillCardPanel getBillCardPanel() {
		if (editor != null) {
			return editor.getBillCardPanel();
		}
		return null;
	}

	public List<String> getNotNullTabCodes() {
		return notNullTabCodes;
	}

	public void setNotNullTabCodes(List<String> notNullTabCodes) {
		this.notNullTabCodes = notNullTabCodes;
	}
}
