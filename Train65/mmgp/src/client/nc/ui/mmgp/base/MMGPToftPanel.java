package nc.ui.mmgp.base;

import nc.ui.mmgp.pub.beans.MMGPButtonObject;
import nc.ui.pub.ButtonObject;

/**
 * 按照生产制造的需求对ToftPanel进行了封装 所有的生产制造功能都应该继承 实现的功能: 定义一些公用方法.
 * 
 * @author：wangweiu
 * @deprecated by wangweiu 不使用了，参考ui工厂2
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
	// * 模块编码
	// */
	// private String moduleCode;
	//
	// /**
	// * ClientEnvironment
	// */
	// private ClientEnvironment cle = ClientEnvironment.getInstance();
	//
	/**
	 * 通用的界面状态
	 */
	private int nState = IUIState.STATE_ERROR;

	//
	// /**
	// * 多语言翻译对象
	// */
	// protected NCLangRes langRes = NCLangRes.getInstance();
	//
	// /**
	// * 退出编辑界面需要提示是需要的接口
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
	// * BDToftPanel 构造子注解。
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
	// * 增加双击表头全选的功能 需要的画面可以调用该方法 前提是实现getBillListPanel()方法
	// */
	// public void addAllSelectListener() {
	// // 增加双击表头全选的功能
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
	// // 增加双击表头全选的功能
	// MouseListener[] listeners =
	// getBillListPanel().getParentListPanel().getRowNOTable().getTableHeader().getMouseListeners();
	// for (MouseListener listener : listeners) {
	// getBillListPanel().getParentListPanel().getRowNOTable().getTableHeader().removeMouseListener(listener);
	// }
	// }
	//
	// /**
	// * 设置推出编辑界面时调用的接口
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
	// * 编辑界面时调用的接口
	// *
	// * @return IClose
	// */
	// public IClose getIClose() {
	// return this.m_iclose;
	// }
	//
	// /**
	// * 得到客户端系统日期
	// *
	// * @return 客户端系统日期
	// */
	// public String getClientDate() {
	// return (new UFDate(new Date())).toString();
	// }
	//
	// // 获得公司级系统参数
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
	// * 得到登录公司的VO
	// */
	// public nc.vo.bd.CorpVO getCorpVO() {
	// return cle.getCorporation();
	// }
	//
	// /**
	// * 得到登录业务日期
	// */
	// public UFDate getLogDate() {
	// return cle.getBusinessDate();
	// }
	//
	// /**
	// * 取得模块编号
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
	// * 得到查询开始日期
	// */
	// public String getQueryStartDate() {
	// return cle.getBusinessDate().toString();
	// }
	//
	// /**
	// * 得到服务端系统时间
	// */
	// public UFDateTime getServerDateTime() {
	// return ClientEnvironment.getServerTime();
	// }
	//
	// /**
	// * 得到登录公司的ID
	// */
	// public String getUnitCode() {
	// return cle.getCorporation().getPk_corp();
	// }
	//
	// /**
	// * 当前登录的用户。
	// *
	// * @return User
	// */
	// public synchronized UserVO getUser() {
	//
	// return cle.getUser();
	// }
	//
	// /**
	// * 此处插入方法说明。
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
	// * 按钮m_boDirectPrint点击时执行的动作,如有必要，请覆盖.
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
	// * 按刷新按钮后的处理方法。
	// */
	// public void onRefresh() {
	// }
	//
	// public void reportException(Exception e) {
	// super.reportException(e);
	// }
	//
	// /**
	// * 设置模块编号
	// *
	// * @param newMODULE
	// * String
	// */
	// public void setModuleCode(String newMODULE) {
	// moduleCode = newMODULE;
	// }
	//
	// /**
	// * 此处插入方法说明。
	// */
	// @Override
	// public boolean onClosing() {
	// if (m_iclose == null) {
	// return true;
	// }
	// if (m_iclose.getCurrentUIState() == IUIState.STATE_ADD) {
	//
	// // 提示是否需要保存
	// return closeOnAdd();
	// } else if (m_iclose.getCurrentUIState() == IUIState.STATE_MODIFY) {
	// return closeOnEdit();
	// } else {
	// return true;
	// }
	// }
	//
	// private boolean closeOnEdit() {
	// // 数据是否发生改变，发生改变提示，不发生改变不提示
	// // 提示是否需要保存
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
	// // 解锁
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
	// // 解锁
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
	 * 设置界面状态
	 * 
	 * @param st
	 * @param para
	 *            业务节点可通过该参数传递一些特殊的信息
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
	// * 是否需要自动按钮排序
	// */
	// public boolean isOrderButton() {
	// return true;
	// }
	//
	// // @Override
	// // protected final void setButtons(ButtonObject[] buttons) {
	// // throw new
	// UnsupportedOperationException("请使用setActions(IButtonAction[])");
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
	// // // 只有末级按钮有触发事件;
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
	// * 根据当前界面状态更新按钮的操作状态
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
	// // * 根据当前的业务状态更新按钮的操作状态
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
	// * 根据当前的界面状态更新按钮的状态
	// *
	// * @param bo
	// */
	protected void changeButtonStateByUIState(MMGPButtonObject bo) {

		// 更新自身的状态
		bo.change2OprateStatus(getCurrentUIState());

		// 更新子按钮的状态
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
	// // * 根据当前的业务状态更新按钮的状态
	// // * @param bo
	// // */
	// // protected void changeButtonStateByBusinessState(MMGPButtonObject bo) {
	// //
	// // //更新自身的状态
	// // bo.change2BusinessStatus(getCurrentBusinessState());
	// //
	// // //更新子按钮的状态
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
	// Logger.debug("自动刷新:节点号:" + getModuleCode() + "\n标题:" + getTitle()/*
	// -=notranslate=- */
	// + "\n用户:" + getClientEnvironment().getUser().getUserName()); /*
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
	 * 设置界面状态
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
	// // * 上一页
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
	// "UPP1009-000336")/* @res""翻页错误"" */);
	// // }
	// // }
	// //
	// // /**
	// // * 下一页
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
	// "UPP1009-000336")/* @res""翻页错误"" */);
	// // }
	// // }
	// //
	// // /**
	// // * 首页
	// // */
	// // public void onPageBegin() {
	// // try {
	// // int rowcount = getBillListPanel().getHeadTable().getRowCount();
	// // if (rowcount > 0) changeRowToRow(0, 0);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // MessageDialog.showErrorDlg(this, null, langRes.getStrByID("1009",
	// "UPP1009-000336")/* @res""翻页错误"" */);
	// // }
	// // }
	// //
	// // /**
	// // * 末页
	// // */
	// // public void onPageEnd() {
	// // try {
	// // int rowcount = getBillListPanel().getHeadTable().getRowCount();
	// // if (rowcount > 0) changeRowToRow(rowcount - 1, rowcount - 1);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // MessageDialog.showErrorDlg(this, null, langRes.getStrByID("1009",
	// "UPP1009-000336")/* @res""翻页错误"" */);
	// // }
	// // }
	// //
	// // /**
	// // * 表头行变更
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
	// // // 设置原来的行状态为无状态
	// // if (e.getOldRow() > -1 && e.getOldRow() <
	// getBillListPanel().getHeadBillModel().getRowCount()) {
	// // getBillListPanel().getHeadBillModel().setRowState(e.getOldRow(),
	// BillModel.UNSTATE);
	// // }
	// // if (e.getRow() > -1) {
	// // // 设置行状态为选择
	// // getBillListPanel().getHeadBillModel().setRowState(e.getRow(),
	// BillModel.SELECTED);
	// // // 当前浏览的VO,即当前选中的行的VO
	// // Object obj = getCurrentViewData();
	// // if (obj == null) throw new
	// BusinessException(langRes.getStrByID("1009", "UPP1009-000337")/*
	// // * @res""调用getCurrentViewData
	// // * ()错误,结果为空!""
	// // */);
	// // // 如果返回的是聚合VO，则设置卡片界面
	// // if (obj instanceof nc.vo.pub.AggregatedValueObject) {
	// // vo = (nc.vo.pub.AggregatedValueObject) obj;
	// // // 表头
	// // getBillCardPanel().getBillData().setHeaderValueVO(vo.getParentVO());
	// // // 表体
	// // getBillCardPanel().getBillData().getBillModel().clearBodyData();
	// // getBillCardPanel().getBillData().setBodyValueVO(vo.getChildrenVO());
	// // getBillCardPanel().updateValue();
	// // // 状态为浏览
	// // setUIState(IUIState.STATE_CARD_BROWSE);
	// // }
	// // }
	// // }
	//
	// /**
	// * 返回模板的nodekey
	// *
	// * @return nodekey
	// */
	// public String getNodeKey() {
	// return nodeKey;
	// }
	//
	// /**
	// * 设置nodekey
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
	// // * 返回业务类型
	// // * @return
	// // */
	// // public String getBusinessType() {
	// // return BusinessType;
	// // }
	// // /**
	// // * 设置业务类型
	// // * @param businessType
	// // */
	// // public void setBusinessType(String businessType) {
	// // BusinessType = businessType;
	// // }

}
