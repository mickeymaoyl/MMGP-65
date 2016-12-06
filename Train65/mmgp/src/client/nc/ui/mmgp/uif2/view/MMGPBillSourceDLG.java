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
 * ��Դ������ʾ�Ի���
 * 
 * @author zhangyhk 2013-6-7 ʵ�ְ�ť�ɸ���
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

	// ����vo,����vo,�ӱ�vo
	protected String m_billVo = null;

	protected String m_billHeadVo = null;

	protected String m_billBodyVo = null;

	// ��ѯ�������
	protected String m_whereStr = null;

	// ���صļ���Vo
	protected AggregatedValueObject retBillVo = null;

	// ���ؼ���VO����
	protected AggregatedValueObject[] retBillVos = null;

	protected boolean isRelationCorp = true;

	// ����������С��λ��
	protected Integer m_BD501 = null;

	// ����������С��λ��
	protected Integer m_BD502 = null;

	// ����С��λ��
	protected Integer m_BD505 = null;

	// ������С��λ��
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
	 * ���������ƣ�where��乹����ս���
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
	 * ���ӵ���ģ�� <li>�÷�����PfUtilClient.childButtonClicked()����
	 */
	public void addBillUI() {
		// ����ģ�����
		getUIDialogContentPane().add(getbillListPanel(), BorderLayout.CENTER);
		// ���ӶԿؼ�����
		addListenerEvent();
	}

	/**
	 * �¼�����
	 */
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbtnQuery().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);
		getbillListPanel().getHeadBillModel().addRowStateChangeEventListener(
				new HeadRowStateListener());

		// ��ͷ�б� ���л��¼�������
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
	 * ֻ�Ա�ͷ���д��� <li>���л� �¼� <li>˫�� �¼� <li>WARN::���л��¼�������˫���¼�֮ǰ
	 * 
	 * @param iNewRow
	 */
	protected synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow,
				getBillSourceVar().getPkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				// 1.���������������
				loadBodyData(iNewRow);
				// 2.���ݵ�ģ����
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
				// �����ʾλ��ֵ
				String billtype = getBillSourceVar().getBillType();// ��������
				BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(billtype,
						null, getBillSourceVar().getUserId(),
						getBillSourceVar().getPk_group(), getBillSourceVar()
								.getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				// �����������ʾλ��
				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				// �����ӱ����ʾλ��
				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				// �������������е��ж�
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
	 * ��õ�������VO����Ϣ
	 * 
	 * <li>����[0]=���ݾۺ�Vo;����[1]=��������Vo;����[2]=�����ӱ�Vo;
	 */
	public void getBillVO() {
		try {
			String[] retString = PfUtilBaseTools
					.getStrBillVo(getBillSourceVar().getBillType());
			// MatchTableBO_Client.querybillVo(getBillType());
			// 0--����vo;1-����Vo;2-�ӱ�Vo;
			m_billVo = retString[0];
			m_billHeadVo = retString[1];
			m_billBodyVo = retString[2];
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * �ӱ��������
	 * 
	 * @return
	 */
	public String getBodyCondition() {
		return null;
	}

	/**
	 * �ӱ������ֶ�
	 * 
	 * @return
	 */
	public String[] getBodyHideCol() {
		return null;
	}

	/**
	 * �����ӱ��ά���飬��һάΪ����Ϊ2���ڶ�ά���ޣ� ��һ��Ϊ�ֶ����ԣ��ڶ���Ϊ��ʾ��λ����
	 * {{"����A","����B","����C","����D"},{"3","4","5","6"}} ע��:���뱣֤���еĳ�����ȡ�����ϵͳ��Ĭ��ֵ2ȡ��
	 * �������ڣ�(2001-12-25 10:19:03)
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
					"UC001-0000008")/* @res "ȡ��" */);
		}
		return ivjbtnCancel;
	}

	protected UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "ȷ��" */);
		}
		return ivjbtnOk;
	}

	protected UIButton getbtnQuery() {
		if (ivjbtnQuery == null) {

			ivjbtnQuery = new UIButton();
			ivjbtnQuery.setName("btnQuery");
			ivjbtnQuery.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006")/* @res "��ѯ" */);
		}
		return ivjbtnQuery;
	}

	/**
	 * �����ѯ����
	 * 
	 * @return
	 */
	public String getHeadCondition() {
		return null;
	}

	/**
	 * ���������ֶ�
	 * 
	 * @return
	 */
	public String[] getHeadHideCol() {
		return null;
	}

	/**
	 * ���������ά���飬��һάΪ����Ϊ2���ڶ�ά���ޣ� ��һ��Ϊ�ֶ����ԣ��ڶ���Ϊ��ʾ��λ����
	 * {{"����A","����B","����C","����D"},{"3","4","5","6"}} ע��:���뱣֤���еĳ�����ȡ�����ϵͳ��Ĭ��ֵ2ȡ��
	 * �������ڣ�(2001-12-25 10:19:03)
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
			// 2003-05-12ƽ̨������ʾ����
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
				"UPP102220-000114")/* @res "���ݵĲ��ս���" */);
		setContentPane(getUIDialogContentPane());

		// ��ȡ���ݶ�Ӧ�ĵ���vo����
		getBillVO();
	}

	/**
	 * ���������ȡ�ӱ�����
	 * 
	 * @param row
	 *            ѡ�еı�ͷ��
	 */
	public void loadBodyData(int row) {
		try {
			// �������ID
			String id = getbillListPanel().getHeadBillModel()
					.getValueAt(row, getBillSourceVar().getPkField())
					.toString();
			// ��ѯ�ӱ�VO����
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
			// ���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
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
							"UPPpfworkflow-000237")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000490")/* @res "���ݼ���ʧ�ܣ�" */);
		}
	}

	/**
	 * "ȷ��"��ť����Ӧ���ӽ����ȡ��ѡ����VO
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
	 * �ڸý����Ͻ����ٴβ�ѯ
	 */
	public void onQuery() {
		IBillReferQuery queryCondition = getQueyDlg();
		if (queryCondition.showModal() == UIDialog.ID_OK) {
			// ���ز�ѯ����
			m_whereStr = queryCondition.getWhereSQL();
			loadHeadData();
			// fgj@ 2001-11-06 �޸��ÿձ�������
			getbillListPanel().setBodyValueVO(null);
			// hxr@ 2005-3-31 ��ʼѡ��һ��
			JTable table = getbillListPanel().getParentListPanel().getTable();
			int iRowCount = table.getRowCount();
			if (iRowCount > 0 && table.getSelectedRow() < 0) {
				table.changeSelection(0, 0, false, false);
			}
		}
	}

	/**
	 * �����������ʾλ�������ݲ�Ʒ�ķ��أ�
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
					"UPP102220-000115")/* @res "��ʾλ�����һ�����в�ƥ��" */);

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
	 * �����������ʾλ�������ݲ�Ʒ�ķ��أ�
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
					"UPP102220-000115")/* @res "��ʾλ�����һ�����в�ƥ��" */);

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
		// //XXX::ֻ�Ա�ͷ��˫���¼�������Ӧ,�����˫���¼���BillListPanel.BodyMouseListener����Ӧ
		// final int headRow = e.getRow();
		//
		// // leijun 2006-7-4 ��ʱ��������ʾ�ȴ��Ի���
		// new Thread(new Runnable() {
		// public void run() {
		// BannerDialog dialog = new BannerDialog(BillSourceDLG.this);
		// dialog.setStartText(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000491")/*@res "��ȡ�����У����Ե�..."*/);
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

