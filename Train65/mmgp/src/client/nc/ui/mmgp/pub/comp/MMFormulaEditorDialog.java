package nc.ui.mmgp.pub.comp;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.pub.bill.IBillObject;
import nc.ui.pub.bill.IBillObjectEditor;
import nc.ui.pub.formulaedit.FormulaEditorDialog;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.mmgp.pub.MMValueNameBillObject;
import nc.vo.pub.formulaedit.FormulaItem;
import nc.vo.pub.formulaset.FormulaParseFather;

/**
 * 默认公式编辑器
 * <P>
 * 仅仅起到一层封装，别的没有什么
 * </P>
 * 
 * @author zjy
 */
@SuppressWarnings("serial")
public class MMFormulaEditorDialog extends FormulaEditorDialog implements
		IBillObjectEditor {

	protected Map<String, MMTabBuilder> tabBuilderCache = new HashMap<String, MMTabBuilder>();
	

	/**
	 * 公式编辑
	 * 
	 * @param parent
	 *            Container
	 * @param f
	 *            FormulaParseFather
	 */
	public MMFormulaEditorDialog(Container parent, FormulaParseFather f) {
		super(parent, f);
	}

	/**
	 * 公式编辑
	 * 
	 * @param parent
	 * @param f
	 */
	public MMFormulaEditorDialog(Container parent) {
		super(parent, new FormulaParse());
	}

	public void addFormulaItems(List<FormulaItem> lstFormulaItems,
			String tabName) {
		MMTabBuilder tabBuilder = tabBuilderCache.get(tabName);
		if (tabBuilder == null) {
			tabBuilder = createTabBuilder(tabName);
//			tabBuilder.setHSFormulaItems(lstFormulaItems);
//			tabBuilder.initalize();
			tabBuilder.setTabName(tabName);
			tabBuilder.setTabNo(getTabNumber(FormulaEditorDialog.FORMULA_VARIABLE));
//			tabBuilder.addUIControls();
			addCustomTabBuilder(tabBuilder,
					FormulaEditorDialog.FORMULA_VARIABLE);
			// tabBuilder.addHSFormulaItems(lstFormulaItems);

			tabBuilderCache.put(tabName, tabBuilder);
		}
		setFormulaItems(lstFormulaItems, tabName);
		// tabBuilder.initalize();

	}

	protected MMTabBuilder createTabBuilder(String tabName) {
		return new MMTabBuilder(getFormulaWordSorter());
	}

	protected MMTabBuilder getTabBuilder(String tabName) {
		return tabBuilderCache.get(tabName);
	}

	public void setFormulaItems(List<FormulaItem> lstFormulaItems,
			String tabName) {
		MMTabBuilder tabBuilder = tabBuilderCache.get(tabName);
		if (tabBuilder == null) {
			return;
		}
		tabBuilder.setHSFormulaItems(lstFormulaItems);
		tabBuilder.initalize();
		setCustomTabBuilder(tabBuilder.getTabNo(), tabBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
	}

	/**
	 * 画面返回值
	 * <P>
	 * 公式画面上，选择确定时返回的对象
	 * </P>
	 * 
	 * @return 公式内容
	 * @see nc.ui.pub.bill.IBillObjectEditor#getBillObject()
	 */
	public IBillObject getBillObject() {
		return new MMValueNameBillObject(getFormulaDesc(),getFormulaDesc());
	}

	public void setBillObject(IBillObject billObject) {
		setFormulaDesc(billObject == null ? null : billObject.toString());
	}

	public int showEditDialog(Component parent) {
		validate();
		int result = this.showModal();
		if (result == ID_OK) {
			return IBillObjectEditor.OK_OPTION;
		}
		return IBillObjectEditor.CANCEL_OPTION;
	}

}
