package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.QueryAction;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.uif2.actions.IQueryDelegator;

/**
 * @author wangweiu
 * 
 */
public class MMGPQueryAction extends QueryAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3999251498601096391L;

	@Override
	public void doAction(ActionEvent e) throws Exception {
		super.doAction(e);
	}

	@Override
	protected void executeQuery(String sqlWhere) {
		super.executeQuery(sqlWhere);
	}

	@Override
	public void setQueryDelegator(IQueryDelegator queryDelegator) {
		super.setQueryDelegator(queryDelegator);
	}

	@Override
	protected void showQueryInfo() {

	}

	/**
	 * 取得查询对话框
	 * <p>
	 * 由于pubapp把uap的这块重写了，导致查询对话框的查询方案和快速查询区的查询方案不能及时同步；所以改成了uap的方式。
	 * <p>
	 * 不建议重写，这块很麻烦,到处都是坑。如果要重写，请务必不要拷贝代码，而采用super.getQueryDlg()，来获得dailog，
	 * 然后在自己处理； <br>
	 * 本来想弄成final的，但考虑到很可能要对查询条件的参照进行一些自定义过滤处理，就暂时没改。
	 * <p>
	 * 另外不能在这个方法里调用getQueryDelegator()方法，会重现死循环。
	 */
	@Override
	public QueryConditionDLG getQueryDlg() {
		if (getQueryCoinditionDLG() != null) {
			return getQueryCoinditionDLG().createQCDByIQCD(
					getQueryCoinditionDLG());
		}
		return super.getQueryDlg();
	}

}
