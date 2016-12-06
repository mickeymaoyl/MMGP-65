/**
 * 
 */
package nc.ui.mmgp.pub.comp;

import java.util.HashMap;
import java.util.List;

import nc.ui.pub.formulaedit.AbstractTabBuilder;
import nc.ui.pub.formulaedit.FormulaWordSorter;
import nc.vo.pub.formulaedit.FormulaItem;

/**
 * 公式自定义页签
 * @author Administrator
 */
public class MMTabBuilder extends AbstractTabBuilder {

    private String tabName = "TODO";
    
    private int tabNo = -1;

    HashMap<String, FormulaItem> hsFormulaItems = new HashMap<String, FormulaItem>();

    /**
     * 构造函数
     * 
     * @param fws
     *        FormulaWordSorter
     */
    public MMTabBuilder(FormulaWordSorter fws) {
        super(fws);
    }

    public void addHSFormulaItems(List<FormulaItem> lstItems) {
        if (lstItems == null) {
            return;
        }
        for (FormulaItem item : lstItems) {
            hsFormulaItems.put(item.getDisplayName(), item);
        }
    }

    void setHSFormulaItems(List<FormulaItem> lstItems) {
        hsFormulaItems.clear();
        if (lstItems == null) {
            return;
        }
        for (FormulaItem item : lstItems) {
            hsFormulaItems.put(item.getDisplayName().toLowerCase(), item);
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pub.formulaedit.AbstractTabBuilder#getHSFormulaItems()
     */
    @SuppressWarnings("rawtypes")
	@Override
    public HashMap getHSFormulaItems() {
        return hsFormulaItems;
    }

    /**
     * 设置Tab页的名称
     * 
     * @param name
     *        Tabe页名称
     */
    public void setTabName(String name) {
        tabName = name;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pub.formulaedit.TabBuilder#getTabName()
     */
    @Override
    public String getTabName() {
        return this.tabName;
    }

	public int getTabNo() {
		return tabNo;
	}

	public void setTabNo(int tabNo) {
		this.tabNo = tabNo;
	}

}
