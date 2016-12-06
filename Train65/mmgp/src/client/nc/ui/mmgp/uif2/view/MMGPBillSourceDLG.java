package nc.ui.mmgp.uif2.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IPFConfig;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.pf.AbstractBillSourceDLG;
import nc.ui.pub.pf.BillSourceVar;
import nc.ui.querytemplate.IBillReferQuery;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletVO;

/**
 * 来源单据显示对话框
 * 
 * @author zhangyhk 2013-6-7 实现按钮可更改
 * @see nc.ui.pub.pf.BillSourceDLG
 */

public class MMGPBillSourceDLG extends AbstractBillSourceDLG implements
		ActionListener, BillEditListener, BillTableMouseListener,
		ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3775438975602848552L;

	protected BillListPanel ivjbillListPanel = null;

	protected JPanel ivjUIDialogContentPane = null;

	protected UIButton ivjbtnCancel = null;

	protected UIButton ivjbtnOk = null;

	protected UIButton ivjbtnQuery = null;

	protected UIPanel ivjPanlCmd = null;

	// 单据vo,主表vo,子表vo
	protected String m_billVo = null;

	protected String m_billHeadVo = null;

	protected String m_billBodyVo = null;

	// 查询条件语句
	protected String m_whereStr = null;

	// 返回的集合Vo
	protected AggregatedValueObject retBillVo = null;

	// 返回集合VO数组
	protected AggregatedValueObject[] retBillVos = null;

	protected boolean isRelationCorp = true;

	// 主计量数量小数位数
	protected Integer m_BD501 = null;

	// 辅计量数量小数位数
	protected Integer m_BD502 = null;

	// 单价小数位数
	protected Integer m_BD505 = null;

	// 换算率小数位数
	protected Integer m_BD503 = null;

	private class HeadRowStateListener implements
			IBillModelRowStateChangeEventListener {

		public void valueChanged(RowStateChangeEvent e) {
			if (e.getRow() != getbillListPanel().getHeadTable()
					.getSelectedRow()) {
				headRowChange(e.getRow());
			}

			BillModel model = getbillListPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model
					.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();

			if (e.isSelectState()) {
				getbillListPanel().getChildListPanel().selectAllTableRow();
			} else {
				getbillListPanel().getChildListPanel()
						.cancelSelectAllTableRow();
			}
			model.addRowStateChangeEventListener(l);

			getbillListPanel().updateUI();
		}

	}

	/**
	 * 根据类名称，where语句构造参照界面
	 * 
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 */
	public MMGPBillSourceDLG(Container parent, BillSourceVar bsVar) {
		super(parent, bsVar);
		m_whereStr = getBillSourceVar().getWhereStr();
		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see event.ActionListener#actionPerformed(event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getbtnOk()) {
			onOk();
		} else if (e.getSource() == getbtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getbtnQuery()) {
			onQuery();
		}
	}

	/**
	 * 增加单据模版 <li>该方法被PfUtilClient.childButtonClicked()调用
	 */
	public void addBillUI() {
		// 增加模版调用
		getUIDialogContentPane().add(getbillListPanel(), BorderLayout.CENTER);
		// 增加对控件监听
		addListenerEvent();
	}

	/**
	 * 事件侦听
	 */
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbtnQuery().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);
		getbillListPanel().getHeadBillModel().addRowStateChangeEventListener(
				new HeadRowStateListener());

		// 表头列表 行切换事件处理器
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BillEditListener#afterEdit(BillEditEvent)
	 */
	public void afterEdit(BillEditEvent e) {
	}

	/**
	 * 只对表头进行处理 <li>行切换 事件 <li>双击 事件 <li>WARN::行切换事件发生在双击事件之前
	 * 
	 * @param iNewRow
	 */
	protected synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow,
				getBillSourceVar().getPkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				// 1.初次载入表体数据
				loadBodyData(iNewRow);
				// 2.备份到模型中
				getbillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getbillListPanel().repaint();
	}

	public void bodyRowChange(BillEditEvent e) {
	}

	/**
	 * @return
	 */
	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				// 获的显示位数值
				String billtype = getBillSourceVar().getBillType();// 交易类型
				BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(billtype,
						null, getBillSourceVar().getUserId(),
						getBillSourceVar().getPk_group(), getBillSourceVar()
								.getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				// 更改主表的显示位数
				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				// 更改子表的显示位数
				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				// 进行主子隐藏列的判断
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				ivjbillListPanel.setMultiSelect(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * 获得单据类型VO类信息
	 * 
	 * <li>数组[0]=单据聚合Vo;数组[1]=单据主表Vo;数组[2]=单据子表Vo;
	 */
	public void getBillVO() {
		try {
			String[] retString = PfUtilBaseTools
					.getStrBillVo(getBillSourceVar().getBillType());
			// MatchTableBO_Client.querybillVo(getBillType());
			// 0--单据vo;1-主表Vo;2-子表Vo;
			m_billVo = retString[0];
			m_billHeadVo = retString[1];
			m_billBodyVo = retString[2];
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 子表条件语句
	 * 
	 * @return
	 */
	public String getBodyCondition() {
		return null;
	}

	/**
	 * 子表隐藏字段
	 * 
	 * @return
	 */
	public String[] getBodyHideCol() {
		return null;
	}

	/**
	 * 返回子表二维数组，第一维为必须为2。第二维不限， 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}} 注意:必须保证二行的长度相等。否则系统按默认值2取。
	 * 创建日期：(2001-12-25 10:19:03)
	 * 
	 * @return java.lang.String[][]
	 */
	public String[][] getBodyShowNum() {
		return null;
	}

	protected UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/* @res "取消" */);
		}
		return ivjbtnCancel;
	}

	protected UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "确定" */);
		}
		return ivjbtnOk;
	}

	protected UIButton getbtnQuery() {
		if (ivjbtnQuery == null) {

			ivjbtnQuery = new UIButton();
			ivjbtnQuery.setName("btnQuery");
			ivjbtnQuery.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006")/* @res "查询" */);
		}
		return ivjbtnQuery;
	}

	/**
	 * 主表查询条件
	 * 
	 * @return
	 */
	public String getHeadCondition() {
		return null;
	}

	/**
	 * 主表隐藏字段
	 * 
	 * @return
	 */
	public String[] getHeadHideCol() {
		return null;
	}

	/**
	 * 返回主表二维数组，第一维为必须为2。第二维不限， 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}} 注意:必须保证二行的长度相等。否则系统按默认值2取。
	 * 创建日期：(2001-12-25 10:19:03)
	 * 
	 * @return java.lang.String[][]
	 */
	public String[][] getHeadShowNum() {
		return null;
	}

	protected UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout(FlowLayout.RIGHT));
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			ivjPanlCmd.add(getbtnQuery(), getbtnQuery().getName());
		}
		return ivjPanlCmd;
	}

	public AggregatedValueObject getRetVo() {
		return retBillVo;
	}

	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			// 2003-05-12平台进行显示调用
			// getUIDialogContentPane().add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	protected void initialize() {

		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle(NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000114")/* @res "单据的参照界面" */);
		setContentPane(getUIDialogContentPane());

		// 获取单据对应的单据vo名称
		getBillVO();
	}

	/**
	 * 根据主表获取子表数据
	 * 
	 * @param row
	 *            选中的表头行
	 */
	public void loadBodyData(int row) {
		try {
			// 获得主表ID
			String id = getbillListPanel().getHeadBillModel()
					.getValueAt(row, getBillSourceVar().getPkField())
					.toString();
			// 查询子表VO数组
			CircularlyAccessibleValueObject[] tmpBodyVo = NCLocator
					.getInstance()
					.lookup(IPFConfig.class)
					.queryBodyAllData(getBillSourceVar().getBillType(), id,
							getBodyCondition());

			if (getbillListPanel().getBillListData().isMeataDataTemplate())
				getbillListPanel().getBillListData()
						.setBodyValueObjectByMetaData(tmpBodyVo);
			else
				getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

	}

	public void loadHeadData() {
		try {
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and ("
							+ getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}

			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(
					IPFConfig.class.getName());
			CircularlyAccessibleValueObject[] tmpHeadVo = pfConfig
					.queryHeadAllData(getBillSourceVar().getBillType(),
							tmpWhere);

			if (getbillListPanel().getBillListData().isMeataDataTemplate())
				getbillListPanel().getBillListData()
						.setHeaderValueObjectByMetaData(tmpHeadVo);
			else
				getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			// lj+ 2005-4-5
			// selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(
					this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000237")/* @res "错误" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000490")/* @res "数据加载失败！" */);
		}
	}

	/**
	 * "确定"按钮的响应，从界面获取被选单据VO
	 */
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
		}
		this.closeOK();
	}

	/**
	 * 在该界面上进行再次查询
	 */
	public void onQuery() {
		IBillReferQuery queryCondition = getQueyDlg();
		if (queryCondition.showModal() == UIDialog.ID_OK) {
			// 返回查询条件
			m_whereStr = queryCondition.getWhereSQL();
			loadHeadData();
			// fgj@ 2001-11-06 修改置空表体数据
			getbillListPanel().setBodyValueVO(null);
			// hxr@ 2005-3-31 初始选中一行
			JTable table = getbillListPanel().getParentListPanel().getTable();
			int iRowCount = table.getRowCount();
			if (iRowCount > 0 && table.getSelectedRow() < 0) {
				table.changeSelection(0, 0, false, false);
			}
		}
	}

	/**
	 * 更改主表的显示位数（根据产品的返回）
	 * 
	 * @param billDataVo
	 * @param strShow
	 * @throws Exception
	 */
	public void setVoDecimalDigitsBody(BillListData billDataVo,
			String[][] strShow) throws Exception {
		if (strShow.length < 2)
			return;

		if (strShow[0].length != strShow[1].length)
			throw new Exception(NCLangRes.getInstance().getStrByID("102220",
					"UPP102220-000115")/* @res "显示位数组第一、二行不匹配" */);

		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getBodyItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/**
	 * 更改主表的显示位数（根据产品的返回）
	 * 
	 * @param billDataVo
	 * @param strShow
	 * @throws Exception
	 */
	public void setVoDecimalDigitsHead(BillListData billDataVo,
			String[][] strShow) throws Exception {
		if (strShow.length < 2) {
			return;
		}
		if (strShow[0].length != strShow[1].length)
			throw new Exception(NCLangRes.getInstance().getStrByID("102220",
					"UPP102220-000115")/* @res "显示位数组第一、二行不匹配" */);

		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getHeadItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int headRow = ((ListSelectionModel) e.getSource())
					.getAnchorSelectionIndex();
			if (headRow >= 0) {
				headRowChange(headRow);
			}
		}
	}

	@Override
	public void mouse_doubleclick(BillMouseEnent e) {
		// if (e.getPos() == BillItem.HEAD) {
		// //XXX::只对表头的双击事件进行响应,表体的双击事件在BillListPanel.BodyMouseListener中响应
		// final int headRow = e.getRow();
		//
		// // leijun 2006-7-4 耗时操作，显示等待对话框
		// new Thread(new Runnable() {
		// public void run() {
		// BannerDialog dialog = new BannerDialog(BillSourceDLG.this);
		// dialog.setStartText(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000491")/*@res "获取数据中，请稍等..."*/);
		// try {
		// dialog.start();
		//
		// headRowDoubleClicked(headRow);
		// } finally {
		// dialog.end();
		// }
		// }
		//
		// }).start();
		//
		// }
	}

}

