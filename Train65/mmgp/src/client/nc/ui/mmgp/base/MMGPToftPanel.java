package nc.ui.mmgp.base;

import nc.ui.mmgp.pub.beans.MMGPButtonObject;
import nc.ui.pub.ButtonObject;

/**
 * ������������������ToftPanel�����˷�װ ���е��������칦�ܶ�Ӧ�ü̳� ʵ�ֵĹ���: ����һЩ���÷���.
 * 
 * @author��wangweiu
 * @deprecated by wangweiu ��ʹ���ˣ��ο�ui����2
 */
public abstract class MMGPToftPanel extends nc.ui.pub.ToftPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	// /**
	// * ģ�����
	// */
	// private String moduleCode;
	//
	// /**
	// * ClientEnvironment
	// */
	// private ClientEnvironment cle = ClientEnvironment.getInstance();
	//
	/**
	 * ͨ�õĽ���״̬
	 */
	private int nState = IUIState.STATE_ERROR;

	//
	// /**
	// * �����Է������
	// */
	// protected NCLangRes langRes = NCLangRes.getInstance();
	//
	// /**
	// * �˳��༭������Ҫ��ʾ����Ҫ�Ľӿ�
	// */
	// private IClose m_iclose;
	//
	// private String nodeKey = null;
	//
	// protected boolean initSuccess = true;
	//
	// // private String BusinessType = null; //
	//
	// /**
	// * BDToftPanel ������ע�⡣
	// */
	//
	// public MMGPToftPanel() {
	// super();
	// }
	//
	// @Override
	// public String checkPrerequisite() {
	// if (!isInitSuccess()) {
	// return IFuncWindow.DONTSHOWFRAME;
	// }
	// return null;
	// }
	//
	// protected void setInitSuccess(boolean newInitSuccess) {
	// initSuccess = newInitSuccess;
	// }
	//
	// protected boolean isInitSuccess() {
	// return initSuccess;
	// }
	//
	// @Override
	// public String getTitle() {
	// return null;
	// }
	//
	// /**
	// * ����˫����ͷȫѡ�Ĺ��� ��Ҫ�Ļ�����Ե��ø÷��� ǰ����ʵ��getBillListPanel()����
	// */
	// public void addAllSelectListener() {
	// // ����˫����ͷȫѡ�Ĺ���
	// getBillListPanel().getParentListPanel().getRowNOTable().getTableHeader().addMouseListener(new
	// MouseAdapter() {
	//
	// public void mouseClicked(MouseEvent e) {
	// if (e.getClickCount() < 2) {
	// return;
	// }
	// BillModel bm = getBillListPanel().getHeadBillModel();
	// boolean hasSelectAll = true;
	// for (int i = bm.getRowCount() - 1; i >= 0; i--) {
	// if (bm.getRowState(i) != BillModel.SELECTED) {
	// hasSelectAll = false;
	// break;
	// }
	// }
	//
	// if (hasSelectAll) {
	// getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	// } else {
	// getBillListPanel().getParentListPanel().selectAllTableRow();
	// }
	// updateUI();
	// }
	//
	// });
	// }
	//
	// /**
	// * removeMouseListener
	// */
	// public void removeMouseListener() {
	// // ����˫����ͷȫѡ�Ĺ���
	// MouseListener[] listeners =
	// getBillListPanel().getParentListPanel().getRowNOTable().getTableHeader().getMouseListeners();
	// for (MouseListener listener : listeners) {
	// getBillListPanel().getParentListPanel().getRowNOTable().getTableHeader().removeMouseListener(listener);
	// }
	// }
	//
	// /**
	// * �����Ƴ��༭����ʱ���õĽӿ�
	// *
	// * @param iclose
	// * IClose
	// */
	// public void addIClose(IClose iclose) {
	// this.m_iclose = iclose;
	//
	// }
	//
	// /**
	// * �༭����ʱ���õĽӿ�
	// *
	// * @return IClose
	// */
	// public IClose getIClose() {
	// return this.m_iclose;
	// }
	//
	// /**
	// * �õ��ͻ���ϵͳ����
	// *
	// * @return �ͻ���ϵͳ����
	// */
	// public String getClientDate() {
	// return (new UFDate(new Date())).toString();
	// }
	//
	// // ��ù�˾��ϵͳ����
	// public SysInitVO getCorpPara(String pk_corp,
	// String paracode) {
	// try {
	// return SysInitBO_Client.queryByParaCode(pk_corp, paracode);
	// } catch (Exception e) {
	// Logger.error(e);
	// return null;
	// }
	// }
	//
	// /**
	// * �õ���¼��˾��VO
	// */
	// public nc.vo.bd.CorpVO getCorpVO() {
	// return cle.getCorporation();
	// }
	//
	// /**
	// * �õ���¼ҵ������
	// */
	// public UFDate getLogDate() {
	// return cle.getBusinessDate();
	// }
	//
	// /**
	// * ȡ��ģ����
	// *
	// * @return String
	// */
	// public String getModuleCode() {
	// String code = null;
	// try {
	// code = super.getModuleCode();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (code == null) {
	// return moduleCode;
	// } else {
	// return code;
	// }
	//
	// }
	//
	// /**
	// * �õ���ѯ��ʼ����
	// */
	// public String getQueryStartDate() {
	// return cle.getBusinessDate().toString();
	// }
	//
	// /**
	// * �õ������ϵͳʱ��
	// */
	// public UFDateTime getServerDateTime() {
	// return ClientEnvironment.getServerTime();
	// }
	//
	// /**
	// * �õ���¼��˾��ID
	// */
	// public String getUnitCode() {
	// return cle.getCorporation().getPk_corp();
	// }
	//
	// /**
	// * ��ǰ��¼���û���
	// *
	// * @return User
	// */
	// public synchronized UserVO getUser() {
	//
	// return cle.getUser();
	// }
	//
	// /**
	// * �˴����뷽��˵����
	// */
	// public void onPrint() {
	// if (getCurrentUIState() == IUIState.STATE_CARD_BROWSE) {
	// MMGPCardBillPrintTool.onPrint(this);
	// } else if (getCurrentUIState() == IUIState.STATE_LIST) {
	// MMGPListBillPrintTool.onPrint(this);
	// }
	// }
	//
	// /**
	// * ��ťm_boDirectPrint���ʱִ�еĶ���,���б�Ҫ���븲��.
	// */
	// public void onDirectPrint() {
	// if (getCurrentUIState() == IUIState.STATE_CARD_BROWSE) {
	// MMGPCardBillPrintTool.onDirectPrint(this.getBillCardPanel(),
	// this.getTitle());
	// } else if (getCurrentUIState() == IUIState.STATE_LIST) {
	// MMGPListBillPrintTool.onDirectPrint(this);
	// }
	// }
	//
	// /**
	// * ��ˢ�°�ť��Ĵ�������
	// */
	// public void onRefresh() {
	// }
	//
	// public void reportException(Exception e) {
	// super.reportException(e);
	// }
	//
	// /**
	// * ����ģ����
	// *
	// * @param newMODULE
	// * String
	// */
	// public void setModuleCode(String newMODULE) {
	// moduleCode = newMODULE;
	// }
	//
	// /**
	// * �˴����뷽��˵����
	// */
	// @Override
	// public boolean onClosing() {
	// if (m_iclose == null) {
	// return true;
	// }
	// if (m_iclose.getCurrentUIState() == IUIState.STATE_ADD) {
	//
	// // ��ʾ�Ƿ���Ҫ����
	// return closeOnAdd();
	// } else if (m_iclose.getCurrentUIState() == IUIState.STATE_MODIFY) {
	// return closeOnEdit();
	// } else {
	// return true;
	// }
	// }
	//
	// private boolean closeOnEdit() {
	// // �����Ƿ����ı䣬�����ı���ʾ���������ı䲻��ʾ
	// // ��ʾ�Ƿ���Ҫ����
	// int ret =
	// MessageDialog.showYesNoCancelDlg(this, null,
	// NCLangRes.getInstance().getStrByID(
	// "common",
	// "MT3",
	// null,
	// new String[]{NCLangRes.getInstance().getStrByID("common",
	// "UC001-0000001") }));
	// if (ret == UIDialog.ID_CANCEL) {
	// return false;
	// } else if (ret == UIDialog.ID_NO) {
	// // ����
	// try {
	// m_iclose.freeLock();
	// return true;
	// } catch (Exception e) {
	// Logger.error(e.getMessage(), e);
	// return false;
	// }
	// } else if (ret == UIDialog.ID_YES) {
	// int ret_save = m_iclose.save();
	// if (ret_save == IClose.SAVE_SUCCESS_QUIT) {
	// // ����
	// try {
	// m_iclose.freeLock();
	// return true;
	// } catch (Exception e) {
	// Logger.error(e.getMessage(), e);
	// return false;
	// }
	// } else if (ret_save == IClose.SAVE_FAILURE_NOT_QUIT) {
	// return false;
	// } else {
	// return true;
	// }
	// } else {
	// return true;
	// }
	// }
	//
	// private boolean closeOnAdd() {
	// int ret =
	// MessageDialog.showYesNoCancelDlg(this, null,
	// NCLangRes.getInstance().getStrByID(
	// "common",
	// "MT3",
	// null,
	// new String[]{NCLangRes.getInstance().getStrByID("common",
	// "UC001-0000001") }));
	// if (ret == UIDialog.ID_CANCEL) {
	// return false;
	// } else if (ret == UIDialog.ID_NO) {
	// return true;
	// } else if (ret == UIDialog.ID_YES) {
	// int ret_save = m_iclose.save();
	// if (ret_save == IClose.SAVE_SUCCESS_QUIT) {
	// return true;
	// } else if (ret_save == IClose.SAVE_FAILURE_NOT_QUIT) {
	// return false;
	// } else {
	// return true;
	// }
	// } else {
	// return true;
	// }
	// }
	//
	/**
	 * ���ý���״̬
	 * 
	 * @param st
	 * @param para
	 *            ҵ��ڵ��ͨ���ò�������һЩ�������Ϣ
	 * @see nc.ui.mm.pub.IUIState
	 */
	public void setState(int st, String para) {
		this.nState = st;
		updateMMButtons();
	}

	//
	// private ButtonObject[] adjustButtonsOrder(ButtonObject[] buttons) {
	// return ButtonUtils.sortButtons(buttons);
	// }
	//
	// /**
	// * �Ƿ���Ҫ�Զ���ť����
	// */
	// public boolean isOrderButton() {
	// return true;
	// }
	//
	// // @Override
	// // protected final void setButtons(ButtonObject[] buttons) {
	// // throw new
	// UnsupportedOperationException("��ʹ��setActions(IButtonAction[])");
	// // }
	//
	// protected void setMMButtons(ButtonObject[] buttons) {
	// if (isOrderButton()) {
	// super.setButtons(adjustButtonsOrder(buttons));
	// } else {
	// super.setButtons(buttons);
	// }
	//
	// }
	//
	// // protected void setActions(IButtonAction[] actions) {
	// // List<MMGPButtonObject> buttons = new ArrayList<MMGPButtonObject>();
	// // for (IButtonAction action : actions) {
	// // processAction(null, action, buttons);
	// // }
	// // setMMButtons(buttons.toArray(new MMGPButtonObject[0]));
	// // updateButtons();
	// // }
	// //
	// // private void processAction(IButtonAction parenActon,
	// // IButtonAction action,
	// // List<MMGPButtonObject> buttons) {
	// //
	// // if (parenActon == null) {
	// // buttons.add(action.getMMButton());
	// // } else {
	// //
	// // parenActon.getMMButton().addChildButton(action.getMMButton());
	// // }
	// // if (action.getChildrenActions() == null ||
	// action.getChildrenActions().length == 0) {
	// // // ֻ��ĩ����ť�д����¼�;
	// // buttonConfig.put(action.getMMButton(), action);
	// // return;
	// // }
	// //
	// // for (IButtonAction childAction : action.getChildrenActions()) {
	// // processAction(action, childAction, buttons);
	// // }
	// // }
	//
	// /**
	// * ���ݵ�ǰ����״̬���°�ť�Ĳ���״̬
	// */
	public void updateMMButtons() {

		if (super.getButtons() == null) {
			return;
		}

		for (ButtonObject bo : super.getButtons()) {
			if (bo instanceof MMGPButtonObject) {
				changeButtonStateByUIState((MMGPButtonObject) bo);
			}
		}
		updateButtons();
	}

	//
	// // /**
	// // * ���ݵ�ǰ��ҵ��״̬���°�ť�Ĳ���״̬
	// // */
	// // public void updateMMButtonsByBusinessState() {
	// //
	// // if(super.getButtons() == null) {
	// // return;
	// // }
	// //
	// // for (ButtonObject bo : super.getButtons()) {
	// // if (bo instanceof MMGPButtonObject) {
	// // changeButtonStateByBusinessState((MMGPButtonObject)bo);
	// // }
	// // }
	// // updateButtons();
	// // }
	//
	// /**
	// * ���ݵ�ǰ�Ľ���״̬���°�ť��״̬
	// *
	// * @param bo
	// */
	protected void changeButtonStateByUIState(MMGPButtonObject bo) {

		// ���������״̬
		bo.change2OprateStatus(getCurrentUIState());

		// �����Ӱ�ť��״̬
		if (bo.getChildCount() > 0) {
			ButtonObject[] childButtons = bo.getChildButtonGroup();
			for (ButtonObject childButton : childButtons) {
				if (childButton instanceof MMGPButtonObject) {
					changeButtonStateByUIState((MMGPButtonObject) childButton);
				}
			}
		}
	}

	//
	// // /**
	// // * ���ݵ�ǰ��ҵ��״̬���°�ť��״̬
	// // * @param bo
	// // */
	// // protected void changeButtonStateByBusinessState(MMGPButtonObject bo) {
	// //
	// // //���������״̬
	// // bo.change2BusinessStatus(getCurrentBusinessState());
	// //
	// // //�����Ӱ�ť��״̬
	// // if(bo.getChildCount() > 0) {
	// // ButtonObject[] childButtons = bo.getChildButtonGroup();
	// // for(ButtonObject childButton : childButtons) {
	// // if(childButton instanceof MMGPButtonObject) {
	// // changeButtonStateByBusinessState((MMGPButtonObject)childButton);
	// // }
	// // }
	// // }
	// // }
	//
	// public NCLangRes getLangRes() {
	// return NCLangRes.getInstance();
	// }
	//
	// public void onAutoRefresh() {
	//
	// Logger.debug("�Զ�ˢ��:�ڵ��:" + getModuleCode() + "\n����:" + getTitle()/*
	// -=notranslate=- */
	// + "\n�û�:" + getClientEnvironment().getUser().getUserName()); /*
	// -=notranslate=- */
	// onRefresh();
	// }
	//
	// public abstract BillCardPanel getBillCardPanel();
	//
	// public abstract BillListPanel getBillListPanel();
	//
	// // public int getCurrentBusinessState() {
	// // return 0;
	// // }
	//
	public int getCurrentUIState() {
		return nState;
	}

	//
	// public Object getCurrentViewData() {
	// return null;
	// }
	//
	/**
	 * ���ý���״̬
	 * 
	 * @param nState
	 * @see nc.ui.mm.pub.IUIState
	 */
	public void setUIState(int nState) {
		this.nState = nState;
		updateMMButtons();
		setDataByState(nState);
	}

	/**
	 * @param state
	 */
	protected void setDataByState(int state) {

	}
	//
	// // /**
	// // * ��һҳ
	// // */
	// // public void onPagePre() {
	// // try {
	// // int rowcount = getBillListPanel().getHeadTable().getRowCount();
	// // if (rowcount < 0) return;
	// // int row = getBillListPanel().getHeadTable().getSelectedRow();
	// // if (row == 0) return;
	// // changeRowToRow(row, row - 1);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // MessageDialog.showErrorDlg(this, null, langRes.getStrByID("1009",
	// "UPP1009-000336")/* @res""��ҳ����"" */);
	// // }
	// // }
	// //
	// // /**
	// // * ��һҳ
	// // */
	// // public void onPageNext() {
	// // try {
	// // int rowcount = getBillListPanel().getHeadTable().getRowCount();
	// // if (rowcount < 0) return;
	// // int row = getBillListPanel().getHeadTable().getSelectedRow();
	// // if (row >= rowcount - 1) return;
	// // changeRowToRow(row, row + 1);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // MessageDialog.showErrorDlg(this, null, langRes.getStrByID("1009",
	// "UPP1009-000336")/* @res""��ҳ����"" */);
	// // }
	// // }
	// //
	// // /**
	// // * ��ҳ
	// // */
	// // public void onPageBegin() {
	// // try {
	// // int rowcount = getBillListPanel().getHeadTable().getRowCount();
	// // if (rowcount > 0) changeRowToRow(0, 0);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // MessageDialog.showErrorDlg(this, null, langRes.getStrByID("1009",
	// "UPP1009-000336")/* @res""��ҳ����"" */);
	// // }
	// // }
	// //
	// // /**
	// // * ĩҳ
	// // */
	// // public void onPageEnd() {
	// // try {
	// // int rowcount = getBillListPanel().getHeadTable().getRowCount();
	// // if (rowcount > 0) changeRowToRow(rowcount - 1, rowcount - 1);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // MessageDialog.showErrorDlg(this, null, langRes.getStrByID("1009",
	// "UPP1009-000336")/* @res""��ҳ����"" */);
	// // }
	// // }
	// //
	// // /**
	// // * ��ͷ�б��
	// // *
	// // * @param oldrow
	// // * @param newrow
	// // * @throws Exception
	// // */
	// // public void changeRowToRow(int oldrow,
	// // int newrow) throws Exception {
	// // getBillListPanel().getHeadTable().setRowSelectionInterval(newrow,
	// newrow);
	// // nc.ui.pub.bill.BillEditEvent e =
	// // new nc.ui.pub.bill.BillEditEvent(getBillListPanel().getHeadTable(),
	// oldrow, newrow);
	// // nc.vo.pub.AggregatedValueObject vo = null;
	// // // ����ԭ������״̬Ϊ��״̬
	// // if (e.getOldRow() > -1 && e.getOldRow() <
	// getBillListPanel().getHeadBillModel().getRowCount()) {
	// // getBillListPanel().getHeadBillModel().setRowState(e.getOldRow(),
	// BillModel.UNSTATE);
	// // }
	// // if (e.getRow() > -1) {
	// // // ������״̬Ϊѡ��
	// // getBillListPanel().getHeadBillModel().setRowState(e.getRow(),
	// BillModel.SELECTED);
	// // // ��ǰ�����VO,����ǰѡ�е��е�VO
	// // Object obj = getCurrentViewData();
	// // if (obj == null) throw new
	// BusinessException(langRes.getStrByID("1009", "UPP1009-000337")/*
	// // * @res""����getCurrentViewData
	// // * ()����,���Ϊ��!""
	// // */);
	// // // ������ص��Ǿۺ�VO�������ÿ�Ƭ����
	// // if (obj instanceof nc.vo.pub.AggregatedValueObject) {
	// // vo = (nc.vo.pub.AggregatedValueObject) obj;
	// // // ��ͷ
	// // getBillCardPanel().getBillData().setHeaderValueVO(vo.getParentVO());
	// // // ����
	// // getBillCardPanel().getBillData().getBillModel().clearBodyData();
	// // getBillCardPanel().getBillData().setBodyValueVO(vo.getChildrenVO());
	// // getBillCardPanel().updateValue();
	// // // ״̬Ϊ���
	// // setUIState(IUIState.STATE_CARD_BROWSE);
	// // }
	// // }
	// // }
	//
	// /**
	// * ����ģ���nodekey
	// *
	// * @return nodekey
	// */
	// public String getNodeKey() {
	// return nodeKey;
	// }
	//
	// /**
	// * ����nodekey
	// *
	// * @param nodeKey
	// * nodeKey
	// */
	// public void setNodeKey(String nodeKey) {
	// this.nodeKey = nodeKey;
	// }
	//
	// // protected void registButton(String name,
	// // String hint,
	// // String code) {
	// // registButton(name, hint, code, 1);
	// // }
	// //
	// // protected void registButton(String name,
	// // String hint,
	// // String code,
	// // int power) {
	// // }
	// // /**
	// // * ����ҵ������
	// // * @return
	// // */
	// // public String getBusinessType() {
	// // return BusinessType;
	// // }
	// // /**
	// // * ����ҵ������
	// // * @param businessType
	// // */
	// // public void setBusinessType(String businessType) {
	// // BusinessType = businessType;
	// // }

}
