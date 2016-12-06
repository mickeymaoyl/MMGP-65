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
	 * ȡ�ò�ѯ�Ի���
	 * <p>
	 * ����pubapp��uap�������д�ˣ����²�ѯ�Ի���Ĳ�ѯ�����Ϳ��ٲ�ѯ���Ĳ�ѯ�������ܼ�ʱͬ�������Ըĳ���uap�ķ�ʽ��
	 * <p>
	 * ��������д�������鷳,�������ǿӡ����Ҫ��д������ز�Ҫ�������룬������super.getQueryDlg()�������dailog��
	 * Ȼ�����Լ����� <br>
	 * ������Ū��final�ģ������ǵ��ܿ���Ҫ�Բ�ѯ�����Ĳ��ս���һЩ�Զ�����˴�������ʱû�ġ�
	 * <p>
	 * ���ⲻ����������������getQueryDelegator()��������������ѭ����
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
