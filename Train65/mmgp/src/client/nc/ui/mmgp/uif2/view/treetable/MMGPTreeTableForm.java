package nc.ui.mmgp.uif2.view.treetable;

import java.util.Enumeration;

import javax.swing.table.TableColumn;

import nc.md.model.IBean;
import nc.ui.mmgp.pub.bill.treetable.MMGPTreeTableCellRenderer;
import nc.ui.mmgp.uif2.view.MMGPShowUpableBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillTableCellRenderer;
import nc.ui.pub.bill.treetable.IBillTreeCreateStrategy;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.IExceptionHandler;
import nc.ui.uif2.UIState;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.SuperVO;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午11:08:56
 * @author: tangxya
 */
public class MMGPTreeTableForm extends MMGPShowUpableBillForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5193719725131941783L;

	/**
		 * 
		 */
	private IExceptionHandler exceptionHandler;

	private IBillTreeCreateStrategy billTreeCreateStrategy;

	private BillCardPanel tempBillCardPanel;

	public void setTempBillCardPanel(BillCardPanel tempBillCardPanel) {
		this.tempBillCardPanel = tempBillCardPanel;
	}

	private SuperVO currBodyVO;

	public SuperVO getCurrBodyVO() {
		return currBodyVO;
	}

	public void setCurrBodyVO(SuperVO currBodyVO) {
		this.currBodyVO = currBodyVO;
	}

	@Override
	public void initUI() {
		super.initUI();
		this.getBillCardPanel().getBodyPanel()
				.switchTreeTableShow(billTreeCreateStrategy);
		this.getBillCardPanel()
				.getBillData()
				.setBillModel(
						this.getBillCardPanel().getBodyPanel().getTableModel());
		this.getBillCardPanel().getBodyPanel().expandAllTree();

		// gaotx 2014-06-17 gaotx 李思远要求删除这段代码
		// if (getBillOrgPanel() != null) {
		// getBillOrgPanel().setLabelName("所属组织");
		// }

		replaceCellRender();

	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public void setBillTreeCreateStrategy(
			IBillTreeCreateStrategy billTreeCreateStrategy) {
		this.billTreeCreateStrategy = billTreeCreateStrategy;
	}

	/**
	 * 替换上级分类编码和上级分类名称的cellRender
	 */
	@SuppressWarnings("restriction")
	private void replaceCellRender() {

		IBean bean = MMMetaUtils.getBeanByClassFullName(currBodyVO.getClass()
				.getName());
		String pid = MMMetaUtils.getParentPKFieldName(bean);
		String code = MMMetaUtils.getCodeFieldName(bean);
		String name = MMMetaUtils.getNameFieldName(bean);
		String parentCode = pid + "." + code;
		String parentName = pid + "." + name;

		MMGPTreeTableBillCardPanel panle = (MMGPTreeTableBillCardPanel) this
				.getBillCardPanel();
		TableColumn column = null;

		column = panle.getBillScrollPane().getShowCol(parentCode);
		if (column != null) {
			column.setCellRenderer(new MMGPTreeTableCellRenderer(bean));
		}
		column = panle.getBillScrollPane().getShowCol(parentName);
		if (column != null) {
			column.setCellRenderer(new MMGPTreeTableCellRenderer(bean));
		}

	}

	public void handleEvent(AppEvent event) {
		super.handleEvent(event);
		if (OrgChangedEvent.class.getName().equals(event.getType())) {
			// 增加新增状态判断是因为删除行时会先设一次selectedRow为-1，再设置正确selectedRow
			// 而主组织在选择改变时会同步组织数据，当为-1时会设置组织为空，从而导致此处清空界面数据
			// 再切换到卡片界面时数据错误。为避免此问题，在此增加状态判断 by yinyxa 2011-07-27
			if (StringUtils.isBlank(this.getModel().getContext().getPk_org())
					&& this.getModel().getUiState() == UIState.ADD) {
			} else {
				if (this.getModel().getUiState() == UIState.ADD) {
					this.getBillCardPanel().getBodyPanel().getTableModel()
							.setEnabled(true);
					// this.getBillCardPanel().getBillModel()
				} else {
				}
			}
		}
	}

	@Override
	protected void createBillCardPanel() {
		this.billCardPanel = tempBillCardPanel;
		if (this.billCardPanel instanceof nc.ui.pubapp.bill.BillCardPanel) {
			((nc.ui.pubapp.bill.BillCardPanel) this.billCardPanel)
					.setBillForm(this);
		}
	}
}
