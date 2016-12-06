package nc.ui.pubapp.billref.dest;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import nc.bs.pf.pub.PfDataCache;
import nc.bs.uif2.IActionCode;
import nc.funcnode.ui.action.INCAction;
import nc.funcnode.ui.action.SeparatorAction;
import nc.itf.pubapp.pub.bill.IRowNo;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.common.AssociationKind;
import nc.md.data.access.NCObject;
import nc.md.model.IAssociation;
import nc.md.model.IBusinessEntity;
import nc.md.model.ICardinality;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.CancelAction;
import nc.ui.pubapp.uif2app.actions.IActionExecutable;
import nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor;
import nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeFuncUtils;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell;
import nc.ui.pubapp.uif2app.view.PubShowUpableBillForm;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.TangramContainer;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.AbstractToftPanelActionContainer;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.actions.IActionContributor;
import nc.ui.uif2.model.ModelDataDescriptor;
import nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.util.VORowNoUtils;

/**
 * ת����������ʾ���洦������UE�淶������ת�������ݴ�ѡ���棬�����˼�����������飺
 * <p>
 * <li>����к�
 * <li>����Ŀ�Ľ������� ҵ����ǰ̨�����ļ������������ã�
 * 
 * <pre>
 * &lt;!-- ת���󹫹����봦�� -- &gt;
 * &lt;bean id=&quot;transferViewProcessor&quot;
 * class=&quot;nc.ui.pubapp.billref.dest.TransferViewProcessor&quot; &gt;
 * &lt;property name=&quot;list&quot; ref=&quot;listView&quot; /&gt;
 * &lt;!-- �б�ť���� -- &gt;
 * &lt;property name=&quot;actionContainer&quot; ref=&quot;actionsOfList&quot;
 * /&gt;
 * &lt;!-- ��Ƭ��ť���� -- &gt;
 * &lt;property name=&quot;cardActionContainer&quot;
 * ref=&quot;actionsOfCard&quot; /&gt;
 * &lt;!-- ���ݱ��水ť -- &gt;
 * &lt;property name=&quot;saveAction&quot; ref=&quot;saveAction&quot; /&gt;
 * &lt;!-- �����ύ��ť -- &gt;
 * &lt;property name=&quot;commitAction&quot; ref=&quot;sendApproveAction&quot;
 * / &gt;
 * &lt;!-- ����ȡ����ť -- &gt;
 * &lt;property name=&quot;cancelAction&quot; ref=&quot;cancelAction&quot; /&gt;
 * &lt;property name=&quot;billForm&quot; ref=&quot;billFormEditor&quot; /&gt;
 * &lt;property name=&quot;transferLogic&quot; &gt;
 * &lt;bean class=&quot;nc.ui.pubapp.billref.dest.DefaultBillDataLogic&quot;
 * &gt;
 * &lt;property name=&quot;billForm&quot; ref=&quot;billFormEditor&quot; /&gt;
 * &lt;/bean &gt;
 * &lt;/property &gt;
 * &lt;/bean &gt;
 * 
 * </pre>
 * 
 * Ȼ���transferViewProcessor beanע�뵽
 * {@link nc.ui.pubapp.uif2app.actions.AbstractReferenceAction}��
 * <code>transferViewProcessor</code>�� ��
 * {@link nc.ui.pubapp.uif2app.actions.AbstractReferenceAction#doAction(ActionEvent)}
 * �е���
 * 
 * <pre>
 * getTransferViewProcessor().processBillTransfer(objs);
 * 
 * <pre>
 * 
 * @since 6.0
 * @version 2011-3-24 ����11:15:34
 * @author ������
 */
public class TransferViewProcessor {
	
	private EditAction editAction = null;

	class MouseListenerHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			TransferViewProcessor.this.selectedTransferDataRow = TransferViewProcessor.this.transferListView
					.getBillListPanel().getHeadTable().getSelectedRow();
			// if (e.getClickCount() == 1) {
			// TransferViewProcessor.this.processRowChange();
			// }
			if (e.getClickCount() > 1) {
				TransferViewProcessor.this.fillTranferDataIntoCard();
			}
		}

	}

	private class CancelActionInterceptor implements ActionInterceptor {

		CancelActionInterceptor() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean afterDoActionFailed(Action action, ActionEvent e,
				Throwable ex) {
			return true;
		}

		@Override
		public void afterDoActionSuccessed(Action action, ActionEvent e) {
			if (action instanceof CancelAction) {
				boolean isCancel = ((CancelAction) action).isCanceled();
				// �������ȡ����ִ�и��������߼�
				if (!isCancel) {
					return;
				}
			}
			TransferViewProcessor.this.processedBillNum++;
			TransferViewProcessor.this.fillNextDataInfoCard();
		}

		@Override
		public boolean beforeDoAction(Action action, ActionEvent e) {
			return true;
		}

	}

	private class CardExitAction extends NCAction {

		private static final long serialVersionUID = 9220834076623042059L;

		CardExitAction() {
			super();
			this.putValue(INCAction.CODE, "CardExit");
			this.putValue(Action.NAME, nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0005")/*
																		 * @res
																		 * "�˳�ת��"
																		 */);
		}

		@Override
		public void doAction(ActionEvent e) throws Exception {
			// TransferViewProcessor.this.billForm
			// .setValue(TransferViewProcessor.this.lastSuccTranferBill);
			TransferViewProcessor.this.getTransferLogic().doTransferAddLogic(
					TransferViewProcessor.this.lastSuccTranferBill);
//			TransferViewProcessor.this.restoreOrigView0();
			
			//��Ƭ�¡��˳�ת���������б��������
			TransferViewProcessor.this.restoreOrigView();
			ShowStatusBarMsgUtil.showStatusBarMsg(null,
					TransferViewProcessor.this.list.getModel().getContext());
		}

	}

	private class CommitActionInterceptor implements ActionInterceptor {

		CommitActionInterceptor() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean afterDoActionFailed(Action action, ActionEvent e,
				Throwable ex) {
			if (action instanceof IActionExecutable) {
				boolean executed = ((IActionExecutable) action).isExecuted();
				if (!executed) {
					return true;
				}
			}
			TransferViewProcessor.this.processedBillNum++;
			BillManageModel model = (BillManageModel) TransferViewProcessor.this.list
					.getModel();
			TransferViewProcessor.this.lastSuccTranferBill = model
					.getSelectedData();
			TransferViewProcessor.this.fillNextDataInfoCard();
			return true;
		}

		@Override
		public void afterDoActionSuccessed(Action action, ActionEvent e) {
			TransferViewProcessor.this.processedBillNum++;
			BillManageModel model = (BillManageModel) TransferViewProcessor.this.list
					.getModel();
			TransferViewProcessor.this.lastSuccTranferBill = model
					.getSelectedData();
			TransferViewProcessor.this.fillNextDataInfoCard();
		}

		@Override
		public boolean beforeDoAction(Action action, ActionEvent e) {
			return true;
		}

	}

	private class DeleteAction extends NCAction {

		private static final long serialVersionUID = 3582911094652088721L;

		DeleteAction() {
			super();
			ActionInitializer.initializeAction(this, IActionCode.DELETE);
		}

		@Override
		public void doAction(ActionEvent e) throws Exception {
			TransferBillModel transferModel = (TransferBillModel) TransferViewProcessor.this.transferListView
					.getModel();
			transferModel.delete();
		}

	}

	/**
	 * �޸�
	 * 
	 * @author chenyyb
	 */
	private class EditAction extends NCAction {

		/**
     *
     */
		private static final long serialVersionUID = -2721468766469126728L;

		private BillManageModel model = null;

		EditAction() {
			super();
			ActionInitializer.initializeAction(this, IActionCode.EDIT);
		}

		@Override
		public void doAction(ActionEvent e) throws Exception {
			TransferViewProcessor.this.selectedTransferDataRow = this
					.getModel().getSelectedRow();
			TransferViewProcessor.this.fillTranferDataIntoCard();
		}

		@Override
		protected boolean isActionEnable() {
			List<?> data = this.getModel().getData();
			return !(data == null || data.size() <= 0);
		}

		BillManageModel getModel() {
			return this.model;
		}

		void setModel(BillManageModel model) {
			this.model = model;
			this.model.addAppEventListener(this);
		}

	}

	private class ListExitAction extends NCAction {

		private static final long serialVersionUID = 4582192637948763592L;

		ListExitAction() {
			super();
			this.putValue(INCAction.CODE, "ListExit");
			this.putValue(Action.NAME, nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0005")/*
																		 * @res
																		 * "�˳�ת��"
																		 */);
		}

		@Override
		public void doAction(ActionEvent e) throws Exception {
			TransferViewProcessor.this.switchFromTransferView();
		}

	}

	private class SaveActionInterceptor implements ActionInterceptor {

		SaveActionInterceptor() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean afterDoActionFailed(Action action, ActionEvent e,
				Throwable ex) {
			return true;
		}

		@Override
		public void afterDoActionSuccessed(Action action, ActionEvent e) {
			if (action instanceof IActionExecutable) {
				boolean executed = ((IActionExecutable) action).isExecuted();
				if (!executed) {
					return;
				}
			}
			
			if (TransferViewProcessor.this.commitAction != null && e.getSource() != null
					&& StringUtils.equals(e.getSource().toString(),"SaveAndCommitScriptAction")) {
				TransferBillModel transferModel = (TransferBillModel) transferListView.getModel();
				List<Object> datas = transferModel.getData();
				if (datas.size() >= 2) {
					TransferViewProcessor.this.commitAction.actionPerformed(e);
					return;
				}
			}
			TransferViewProcessor.this.processedBillNum++;
			BillManageModel model = (BillManageModel) TransferViewProcessor.this.list
					.getModel();
			TransferViewProcessor.this.lastSuccTranferBill = model
					.getSelectedData();
			TransferViewProcessor.this.fillNextDataInfoCard();
		}

		@Override
		public boolean beforeDoAction(Action action, ActionEvent e) {
			return true;
		}

	}

	PubShowUpableBillForm billForm;

	Object lastSuccTranferBill;

	ShowUpableBillListView list;

	int processedBillNum = 0;

	int selectedTransferDataRow;

	int totalBillNum = 0;

	TransferBillView transferListView;

	private IActionContributor actionContainer;

	private NCAction cancelAction;

	private AbstractToftPanelActionContainer cardActionContainer;

	private List<Action> cardOrgiEditActions;

	private NCAction commitAction;

	private boolean isProcessPanelOnly;

	private List<Action> listOrgiActions;

	private ITransferListViewProcessor listProcessor;

	private ITransferListRowChangeProcessor listRowChangeProcessor;

	private ActionInterceptor orgiCancelActionInterceptor;

	private ActionInterceptor orgiCommitActionInterceptor;

	private ActionInterceptor orgiSaveActionInterceptor;

	private UEQueryAreaShell queryAreaShell;

	private CardLayoutToolbarPanel queryInfoToolbarPanel;

	private NCAction saveAction;

	private ITransferBillDataLogic transferLogic;

	public TransferViewProcessor() {
		super();
	}

	public IActionContributor getActionContainer() {
		return this.actionContainer;
	}

	public PubShowUpableBillForm getBillForm() {
		return this.billForm;
	}

	public NCAction getCancelAction() {
		return this.cancelAction;
	}

	public AbstractToftPanelActionContainer getCardActionContainer() {
		return this.cardActionContainer;
	}

	public NCAction getCommitAction() {
		return this.commitAction;
	}

	public ITransferListViewProcessor getListProcessor() {
		return this.listProcessor;
	}

	public ITransferListRowChangeProcessor getListRowChangeProcessor() {
		return this.listRowChangeProcessor;
	}

	public TransferBillView getListView() {
		return this.transferListView;
	}

	public TransferBillView getinitializedListView() {
		  if(this.transferListView==null){
		   this.transferListView = this.createTransferListView();
		  }
		  return this.transferListView;
	}

	
	public UEQueryAreaShell getQueryAreaShell() {
		return this.queryAreaShell;
	}

	public CardLayoutToolbarPanel getQueryInfoToolbarPanel() {
		return this.queryInfoToolbarPanel;
	}

	public NCAction getSaveAction() {
		return this.saveAction;
	}

	public ITransferBillDataLogic getTransferLogic() {
		if (this.transferLogic == null) {
			this.transferLogic = new DefaultBillDataLogic();
		}
		return this.transferLogic;
	}

	public Object[] getViewDatas() {
		if (this.transferListView == null) {
			return null;
		}
		return this.transferListView.getModel().getData().toArray();
	}

	public boolean isProcessPanelOnly() {
		return this.isProcessPanelOnly;
	}

	/**
	 * �����������ݴ���
	 * 
	 * @param billVOs
	 *            ���ε���������VO
	 */
	public void processBillTransfer(Object[] pbillVOs) {
		Object[] billVOs = pbillVOs;
		if (billVOs == null || billVOs.length == 0) {
			return;
		}
		// this.restoreActionInterceptors();
		this.totalBillNum = billVOs.length;
		// PfServiceUtil.processDestBillTranType(billVOs,
		// ((BillManageModel) this.list.getModel()).getBillType(), this.list
		// .getModel().getContext().getPk_group());
		// String trantype =
		// TrantypeFuncUtils.getTrantype(this.list.getModel().getContext());
		String trantype = TrantypeFuncUtils.getTrantype(this.list.getModel()
				.getContext());
		String pk_trantype = null;
		// String pk_trantype =
		// TrantypeFuncUtils.getTrantypePk(this.list.getModel().getContext());
		BilltypeVO vo = PfDataCache.getBillType(trantype);
		if (vo != null) {
			pk_trantype = vo.getPk_billtypeid();
		}
		billVOs = this.filterBillByTrantype(pk_trantype, billVOs,
				billVOs[0].getClass());
		
		// ���˺���������Ϊ��
		if (billVOs == null || billVOs.length == 0) {
			ShowStatusBarMsgUtil.showStatusBarMsg(NCLangRes.getInstance().getStrByID("pubapp_0", "TransferViewProcessor-0000")/*���˺���������Ϊ��, �˳�ת��!*/, this.list.getModel().getContext());
			return;
		}
		this.addRowNO(billVOs);
		this.copyOrgiActions();
		this.copyOrgiActionInterceptors();
		this.addActionInterceptors();
		this.showDataOfBill(billVOs);
		// this.firstCall = false;
	}

	public void setActionContainer(IActionContributor actionContainer) {
		this.actionContainer = actionContainer;
	}

	public void setBillForm(PubShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

	public void setCancelAction(NCAction cancelAction) {
		this.cancelAction = cancelAction;
	}

	public void setCardActionContainer(
			AbstractToftPanelActionContainer cardActionContainer) {
		this.cardActionContainer = cardActionContainer;
	}

	public void setCommitAction(NCAction commitAction) {
		this.commitAction = commitAction;
	}

	public void setList(ShowUpableBillListView list) {
		this.list = list;
	}

	public void setListProcessor(ITransferListViewProcessor listProcessor) {
		this.listProcessor = listProcessor;
	}

	public void setListRowChangeProcessor(
			ITransferListRowChangeProcessor listRowChangeProcessor) {
		this.listRowChangeProcessor = listRowChangeProcessor;
	}

	public void setProcessPanelOnly(boolean processPanelOnly) {
		this.isProcessPanelOnly = processPanelOnly;
	}

	public void setQueryAreaShell(UEQueryAreaShell queryAreaShell) {
		this.queryAreaShell = queryAreaShell;
	}

	public void setQueryInfoToolbarPanel(
			CardLayoutToolbarPanel queryInfoToolbarPanel) {
		this.queryInfoToolbarPanel = queryInfoToolbarPanel;
	}

	public void setSaveAction(NCAction saveAction) {
		this.saveAction = saveAction;
	}

	public void setTransferLogic(ITransferBillDataLogic transferLogic) {
		this.transferLogic = transferLogic;
	}

	public void showDataOfBill(Object[] billVOs) {
		if (billVOs == null || billVOs.length == 0) {
			return;
		}
		// ����ÿ�δ�����������ڴ�й©
		if (this.transferListView == null) {
			this.transferListView = this.createTransferListView();
		}
		this.transferListView.dataAlreadySyschForList = false;
		this.transferListView.needLoad = billVOs.length == 1 ? false : true;
		if (this.getQueryInfoToolbarPanel() != null) {
			this.getQueryInfoToolbarPanel().setModel(
					this.transferListView.getModel());
		}
		// this.transferListView = this.createTransferListView();
		// if (this.getListProcessor() != null) {
		// this.getListProcessor().processBefore(this.transferListView,
		// billVOs);
		// }
		ModelDataDescriptor descriptor = new ModelDataDescriptor(
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"pubapp_0", "0pubapp-0002")/* @res "������" */);
		this.transferListView.getModel().initModel(billVOs, descriptor);
		// if (this.getListProcessor() != null) {
		// this.getListProcessor().processAfter(this.transferListView, billVOs);
		// }
		this.switchToTransferView();
		if (billVOs.length == 1) {
			this.transferListView.needLoad = false;
			// ѡ�����¼��ڳ�ʼ��ģ�͵�ʱ���Ѿ��������ˣ������ٴ���һ��
			// this.transferListView.getModel().setSelectedRow(0);
			this.fillTranferDataIntoCard(billVOs[0]);
			// return;
		}
		// this.transferListView.getModel().initModel(billVOs);
		// this.switchToTransferView();
	}

	/**
	 * ת��������淵�ص�ԭʼ�б����
	 */
	public void switchFromTransferView() {
		if (!this.isProcessPanelOnly) {
			this.restoreOrigView();
			this.showListButtons();
		}
	}

	/**
	 * ����кţ�Ŀǰ֧��{@link nc.vo.pub.AggregatedValueObject}��
	 * {@link nc.vo.pub.CircularlyAccessibleValueObject}��������VO
	 * 
	 * @param billVOs
	 */
	protected void addRowNO(Object[] billVOs) {
		if (billVOs == null || billVOs.length == 0) {
			return;
		}

		if (billVOs instanceof AggregatedValueObject[]) {
			//���ӱ����ݣ��ڷ�������ֱ�ȡ��ͬ�ӱ���к��ֶν�������
			VORowNoUtils.setVOsRowNoByRule((AggregatedValueObject[]) billVOs);
		} else if (billVOs instanceof CircularlyAccessibleValueObject[]) {
			//�������ݣ�ȡ���к��ֶ�ֱ����ֵ
			String sRowNOKey = this.getRowNoKey(billVOs[0]);
			if (sRowNOKey == null) {
				return;
			}
			VORowNoUtils.setVOsRowNoByRule(
					(CircularlyAccessibleValueObject[]) billVOs, sRowNOKey);
		}
	}

	@SuppressWarnings("unchecked")
	void fillNextDataInfoCard() {
		TransferBillModel transferModel = (TransferBillModel) this.transferListView
				.getModel();
		List<Object> datas = transferModel.getData();
		this.totalBillNum--;
		if (datas.size() >= 2) {
			int nextRow = (this.selectedTransferDataRow + 1) % datas.size();
			this.fillTranferDataIntoCard(datas.get(nextRow));
			transferModel.delete();
			this.selectedTransferDataRow = this.selectedTransferDataRow
					% datas.size();
			// transferModel.setSelectedOperaRows(new int[] {
			// this.selectedTransferDataRow
			// });
			// ͬ��ѡ����
			transferModel.setSelectedRow(this.selectedTransferDataRow);

		} else {
			// this.getTransferLogic().doTransferAddLogic(
			// TransferViewProcessor.this.lastSuccTranferBill);
			// this.switchFromTransferView();
			// �����ĵ��������һ�ŵ���ʱ�л�ԭʼ�Ľ���
			transferModel.delete();
			this.restoreOrigView0();
		}
	}

	void fillTranferDataIntoCard() {
		TransferBillModel transferModel = (TransferBillModel) this.transferListView
				.getModel();
		Object selectedData = transferModel.getSelectedData();
		this.fillTranferDataIntoCard(selectedData);
	}

	// void processRowChange() {
	// TransferBillModel transferModel =
	// (TransferBillModel) this.transferListView.getModel();
	// Object selectedData = transferModel.getSelectedData();
	// if (this.getListRowChangeProcessor() != null) {
	// this.getListRowChangeProcessor().processRowChange(this.transferListView,
	// this.selectedTransferDataRow, selectedData);
	// }
	// }

	void restoreOrigView() {
		this.restoreOrigView0();
		this.list.showMeUp();
	}

	void restoreOrigView0() {
		this.switchToPanel(this.list.getBillListPanel());
		this.switchListButtons(this.listOrgiActions);
		this.switchCardEditButtons(this.cardOrgiEditActions);
		this.restoreActionInterceptors();
		// ��ԭ��ѯ��Ϣ��������model
		if (this.getQueryInfoToolbarPanel() != null) {
			// ���Ƴ�ת��֮ǰ�ѱ������еı�����Ϣ����
			ModelDataDescriptor descriptor = new ModelDataDescriptor(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"pubapp_0", "0pubapp-0002")/* @res "������" */);
			this.transferListView.getModel().initModel(null, descriptor);
			this.getQueryInfoToolbarPanel().setModel(this.list.getModel());
		}
		// if (this.getQueryAreaShell() != null) {
		// this.getQueryAreaShell().showMe();
		// }
		this.list.getModel().setUiState(UIState.NOT_EDIT);
		// this.getBillForm().showMeUp();
	}

	public void addActionInterceptor(NCAction action,
			ActionInterceptor actionInterceptor) {
		if (action == null) {
			return;
		}
		ActionInterceptor orgiActionInterceptor = action.getInterceptor();
		if (orgiActionInterceptor == null) {
			action.setInterceptor(actionInterceptor);
			return;
		}
		CompositeActionInterceptor compositeActionInterceptor = new CompositeActionInterceptor();
		compositeActionInterceptor.addInterceptor(orgiActionInterceptor);
		compositeActionInterceptor.addInterceptor(actionInterceptor);
		action.setInterceptor(compositeActionInterceptor);
	}

	private void addActionInterceptors() {
		this.addActionInterceptor(this.saveAction, new SaveActionInterceptor());
		this.addActionInterceptor(this.cancelAction,
				new CancelActionInterceptor());
		this.addActionInterceptor(this.commitAction,
				new CommitActionInterceptor());
	}

	private void copyListOrgiActions() {
		this.listOrgiActions = this.actionContainer.getActions();
	}

	private void copyOrgiActionInterceptors() {
		this.orgiCancelActionInterceptor = this.cancelAction.getInterceptor();
		this.orgiSaveActionInterceptor = this.saveAction.getInterceptor();
		// �еĵ��ݿ���û���ύ��ť
		if (this.commitAction != null) {
			this.orgiCommitActionInterceptor = this.commitAction
					.getInterceptor();
		}
	}

	private void copyOrgiActions() {
		this.cardOrgiEditActions = this.cardActionContainer.getEditActions();
	}

	private TransferBillView createTransferListView() {
		TransferBillView listView = new TransferBillView();
		TransferBillModel transferModel = this.createTransferModel();
		listView.setModel(transferModel);
		listView.setNodekey(this.list.getNodekey());
		listView.setPos(this.list.getPos());
		listView.setTabCode(this.list.getTabCode());
		listView.setTemplateContainer(this.list.getTemplateContainer());
		listView.setUserdefitemListPreparator(this.list
				.getUserdefitemListPreparator());
		listView.setListRowChangeProcessor(this.getListRowChangeProcessor());
		listView.setListProcessor(this.getListProcessor());
		listView.initUI();

		MouseListenerHandler l = new MouseListenerHandler();
		listView.getBillListPanel().getHeadTable().addMouseListener(l);

		return listView;
	}

	private TransferBillModel createTransferModel() {
		TransferBillModel transferModel = new TransferBillModel();
		transferModel.setContext(this.list.getModel().getContext());
		transferModel.setBusinessObjectAdapterFactory(this.list.getModel()
				.getBusinessObjectAdapterFactory());
		// ����δ����Ƶ�showDataOfBill�����У���Ϊmodel����ÿ�ζ�����
		// if (this.getQueryInfoToolbarPanel() != null) {
		// this.getQueryInfoToolbarPanel().setModel(transferModel);
		// }
		return transferModel;
	}

	private void fillTranferDataIntoCard(Object data) {
		// �滻��ť�����л�����֮ǰ���У���������ϵİ�ť���ܲ������°�ť
		List<Action> actions = new ArrayList<Action>(this.cardOrgiEditActions);
		if (this.needAddCardExitAction()) {
			actions.add(new SeparatorAction());
			actions.add(new CardExitAction());
			actions.add(new SeparatorAction());
		}
		this.switchCardEditButtons(actions);

		((BillManageModel) this.list.getModel())
				.setAppUiState(AppUiState.TRANSFERBILL_ADD);
		this.setVOStatusToNew((AggregatedValueObject) data);
		this.getTransferLogic().doTransferAddLogic(data);
		// ShowStatusBarMsgUtil.showStatusBarMsg("�����޸ĵ�" +
		// (this.processedBillNum +
		// 1)
		// + "/" + this.totalBillNum, this.list.getModel().getContext());
		int currRow = this.selectedTransferDataRow % this.totalBillNum + 1;
		// ShowStatusBarMsgUtil.showStatusBarMsg("�����޸ĵ�" + currRow + "/"
		// + this.totalBillNum, this.list.getModel().getContext());
		ShowStatusBarMsgUtil
				.showStatusBarMsg(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"pubapp_0",
								"0pubapp-0246",
								null,
								new String[] { "" + currRow,
										"" + this.totalBillNum })/*
																 * @res
																 * "�����޸ĵ�{0}/{1}"
																 */, this.list
								.getModel().getContext());
	}

	/**
	 * �������ε��ݵĽ������͹������ݣ���Ҫ����Խڵ��ǽ������ͷ������ɵ����
	 * 
	 * @param pk_trantype
	 *            �����ڵ��Դ��������
	 * @param billVOs
	 *            ��ʼ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T[] filterBillByTrantype(String pk_trantype, Object[] billVOs,
			Class<T> billClass) {
		if (pk_trantype == null) {
			return (T[]) billVOs;
		}
		List<T> llist = new ArrayList<T>();
		for (Object billVO : billVOs) {
			IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(billVO,
					IFlowBizItf.class);
			if (fbi.getTranstypePk() == null
					|| PubAppTool.isEqual(pk_trantype, fbi.getTranstypePk())) {
				llist.add((T) billVO);
				if (fbi.getTranstypePk() == null) {
					fbi.setTranstypePk(pk_trantype);
				}
			}
		}
		return llist.toArray((T[]) Array.newInstance(billClass, llist.size()));
	}

	private String getRowNoKey(Object billVO) {
		NCObject ncobj = NCObject.newInstance(billVO);
		IBusinessEntity entity = (IBusinessEntity) ncobj.getRelatedBean();
		if (null != entity) {
			List<IAssociation> associations = entity.getAssociationsByKind(
					AssociationKind.Composite, ICardinality.ASS_ALL);
			for (IAssociation association : associations) {
				IBusinessEntity endEntity = (IBusinessEntity) association
						.getEndBean();
				Map<String, String> map = endEntity
						.getBizInterfaceMapInfo(IRowNo.class.getName());
				if (map != null && map.get("rowno") != null) {
					return map.get("rowno");
				}
			}
		}
		return null;
	}

	/**
	 * ����Ѿ���ӹ���ť�Ͳ�������ˣ����������ֶ�����˳�ת������ť
	 * 
	 * @return
	 */
	private boolean needAddCardExitAction() {
		List<Action> actions = new ArrayList<Action>(this.cardOrgiEditActions);
		if (this.transferListView.getModel().getData().size() < 2) {
			return false;
		}
		
		for (Action action : actions) {
			if (action instanceof CardExitAction) {
				return false;
			}
		}
		return true;
	}

	private void restoreActionInterceptors() {
		this.saveAction.setInterceptor(this.orgiSaveActionInterceptor);
		this.cancelAction.setInterceptor(this.orgiCancelActionInterceptor);
		// �еĵ��ݿ���û���ύ��ť
		if (this.commitAction != null) {
			this.commitAction.setInterceptor(this.orgiCommitActionInterceptor);
		}
	}

	private void setVOStatusToNew(AggregatedValueObject aggVO) {
		CircularlyAccessibleValueObject parentVO = aggVO.getParentVO();
		if (parentVO != null) {
			parentVO.setStatus(VOStatus.NEW);
		}
		CircularlyAccessibleValueObject[] childrenVO = aggVO.getChildrenVO();
		if (childrenVO != null) {
			for (CircularlyAccessibleValueObject childVO : childrenVO) {
				childVO.setStatus(VOStatus.NEW);
			}
		}
	}

	//�����б�container��ʾ����ͬʱ���ÿ�Ƭcontainer���أ��������ְ�ť�ظ� 20130321 modified by guoting
    private void showListButtons() {
        if (!(this.actionContainer instanceof TangramContainer)) {
            this.actionContainer.setActived(false);
            this.actionContainer.setActived(true);
            this.cardActionContainer.setActived(false);
        }
        else {
            this.actionContainer.setActived(true);
            this.actionContainer.setActived(false);
            this.cardActionContainer.setActived(true);
        }
    }

  //���ÿ�Ƭcontainer��ʾ����ͬʱ�����б�container���أ��������ְ�ť�ظ� 20130321 modified by guoting
    private void switchCardEditButtons(List<Action> editActions) {
        if (this.isProcessPanelOnly) {
            return;
        }
        this.cardActionContainer.setEditActions(editActions);
        this.actionContainer.setActived(false);
        // this.cardActionContainer.setActived(false);
        // this.cardActionContainer.setActived(true);
    }

	/**
	 * ����Ƭ������б���水ť��һ��ʱ�л�������ť
	 * 
	 * @param actions
	 */
	public void switchListButtons(List<Action> actions) {
		// if (this.isProcessPanelOnly) {
		// return;
		// }
		this.actionContainer.setActions(actions);
		// this.showListButtons();
	}

	private void switchToPanel(JPanel targetPanel) {
		this.list.removeAll();
		//��panel�ָ�
		this.list.add(targetPanel);
		//��ҳpanel�ָ�
		if(this.list.getSouth() != null) {
			this.list.add(this.list.getSouth(), BorderLayout.SOUTH);
		}
		if(this.list.getNorth() != null) {
			this.list.add(this.list.getNorth(), BorderLayout.NORTH);
		}
		this.list.updateUI();
	}

	/**
	 * �л���ת���������
	 */
	public void switchToTransferView() {
		this.list.addPropertyChangeListener(this.transferListView);
		this.switchToPanel(this.transferListView);
		
		
		List<Action> list = this.actionContainer.getActions();
		boolean isTransferView = false;
		for (Action action : list) {
			if (action.getValue(INCAction.CODE) !=null
					&& action.getValue(INCAction.CODE).equals("ListExit")) {
				isTransferView = true;
			}
		}
		
		// this.hideQuickArea();
		if (!this.isProcessPanelOnly && !isTransferView) {
			// this.listOrgiActions = this.actionContainer.getActions();
			
			this.copyListOrgiActions();
			List<Action> actions = new ArrayList<Action>();
			
			if (this.editAction == null) {
				this.editAction = new EditAction();
				editAction.setModel((BillManageModel) this.transferListView.getModel());
			}

			actions.add(editAction);
			actions.add(new DeleteAction());
			actions.add(new SeparatorAction());
			actions.add(new ListExitAction());
			this.switchListButtons(actions);
			this.showListButtons();
		}
		if (this.getQueryAreaShell() != null) {
			this.getQueryAreaShell().hideMe();
		}
		this.list.showMeUp();
	}
}
