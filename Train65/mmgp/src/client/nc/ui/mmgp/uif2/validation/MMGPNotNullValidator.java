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
 * <b> 前台非空校验（基于模板必输项）
 * <p>
 * 必须挂到 @see nc.bs.uif2.validation.DefaultValidationService 中 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2012-11-23
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
		// 按照模板的配置检验记录的必选项是否都输入了
		if (getNullValidator().validate(obj) != null) {
			return getNullValidator().validate(obj);
		}

		// 接着检验表体项是否为空
		return isBodyNull(obj);
	}

	public ValidationFailure isBodyNull(Object obj) {

		// 获得单据模板中所有的页签
		String[] allTabCodes = getBillCardPanel().getBillData()
				.getBodyTableCodes();

		// 首先判断当前是否为单子表(多子表)，如果都不是，则不须校验表体是否为空，直接结束
		if (!(obj instanceof AggregatedValueObject)
				|| MMArrayUtil.isEmpty(allTabCodes)) {
			return null;
		}

		if (MMListUtil.isEmpty(notNullTabCodes)) {
			// 没有配置不能为空的页签页，返回
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
				// 页签编码拼写错误
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
		// 只有一个页签时 wenjl add 2013-6-26
		if (1 == allTabCodes.length) {
			return new ValidationFailure(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0093")/*表体为空*/);
		}

		StringBuilder sbBuilder = new StringBuilder();
		sbBuilder.append(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0094")/*页签*/);

		for (String nullTab : nullTabCodes) {
			// 获得页签名字
			String tabName = getBillCardPanel().getBillData().getBodyTableName(
					nullTab);
			sbBuilder.append(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0095", null, new String[]{tabName})/*【{0}】，*/);
		}
		// 去掉最后一个逗号
		String result = sbBuilder.substring(0, sbBuilder.length() - 1);
		return new ValidationFailure(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0096", null, new String[]{result})/*{0}为空*/);
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
