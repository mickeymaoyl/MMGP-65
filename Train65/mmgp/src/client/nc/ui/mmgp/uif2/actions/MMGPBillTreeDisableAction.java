package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.bs.uif2.IActionCode;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.ui.bd.pub.actions.ManageModeActionInterceptor;
import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.uif2.service.MMGPDocModelService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.actions.CompositeActionInterceptor;
import nc.ui.uif2.actions.RefreshAction;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> �����ӱ����ͽڵ�ͣ�ð�ť</b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:Sep 19, 2014
 * @author:liwsh
 */
public class MMGPBillTreeDisableAction extends NCAction {

    private HierachicalDataAppModel treeModel;

    private IValidationService validationService;

    private RefreshAction refreshAction;

    // ����ʵ������Ȩ����Ҫ
    private String mdOperateCode = null; // Ԫ���ݲ�������

    private String operateCode = null; // ��Դ����������룬��������ע����һ������ע�룬�򲻽�������Ȩ�޿��ơ�

    private String resourceCode = null; // ҵ��ʵ����Դ����

    /**
     * ����
     */
    private MMGPDocModelService manageModelService;

    private ManageModeActionInterceptor manageModeActionInterceptor = new ManageModeActionInterceptor();

    private CompositeActionInterceptor interceptor;

    public MMGPBillTreeDisableAction() {
        super();
        ActionInitializer.initializeAction(this, IActionCode.DISABLE);
        interceptor = new CompositeActionInterceptor();
        super.setInterceptor(interceptor);
        interceptor.add(manageModeActionInterceptor);
        setInterceptor(manageModeActionInterceptor);
    }

    @Override
    public void setInterceptor(ActionInterceptor interceptor) {
        this.interceptor.add(interceptor);
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
        this.treeModel.addAppEventListener(this);
        manageModeActionInterceptor.setModel(treeModel);
    }

    @Override
    protected boolean isActionEnable() {
        return ((getTreeModel().getUiState() == UIState.NOT_EDIT || getTreeModel().getUiState() == UIState.INIT)
            && (!isSelectedNodeNull()) && (isSelectedNodeEnable()));
    }

    private boolean isSelectedNodeNull() {
        return (getTreeModel().getSelectedData() == null);
    }

    // �˷�����ǰ���ǣ�ѡ���˷ǿռ�¼
    private boolean isSelectedNodeEnable() {
        IBill billvo = (IBill) getTreeModel().getSelectedData();
        Object enableFlag = billvo.getParent().getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD);
        boolean isEnable = false;
        if (enableFlag != null) {
            isEnable = IPubEnumConst.ENABLESTATE_ENABLE == (Integer) enableFlag;
        }

        return isEnable;
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        DefaultMutableTreeNode selectedNode = treeModel.getSelectedNode();
        if (selectedNode == null) {
            MessageDialog.showHintDlg(
                treeModel.getContext().getEntranceUI(),
                NCLangRes.getInstance().getStrByID("smcomm", "UPPuserauth-000005")/*
                                                                                   * @res "��ʾ"
                                                                                   */,
                NCLangRes.getInstance().getStrByID("smcomm", "UPPuserauth-000006")/*
                                                                                   * @res "����ѡ��һ����¼��"
                                                                                   */);
            return;
        }

        Object[] objs = getUnDataPermissionData();
        if (objs != null && objs.length > 0) {
            // TODO showErrorInfo
            JComponent parent = getTreeModel().getContext().getEntranceUI();
            ShowStatusBarMsgUtil.showDataPermissionDlg(
                parent,
                NCLangRes.getInstance().getStrByID("uif2", "ExceptionHandlerWithDLG-000000")/* ���� */,
                IShowMsgConstant.getHasNoPermissionDataInfo());
            return;
        }

        IBill billvo = (IBill) getTreeModel().getSelectedData();

        // Object oldSealFlag = selectedVO.getAttributeValue(SEAL_FLAG_FIELD);
        validate(billvo);
        if (UIDialog.ID_OK == MessageDialog.showOkCancelDlg(
            getTreeModel().getContext().getEntranceUI(),
            NCLangRes.getInstance().getStrByID("uif2", "TreeDisableAction-000000")/* ��ʾ */,
            NCLangRes.getInstance().getStrByID("uif2", "TreeDisableAction-000001")/* ��ȷ��Ҫͣ����ѡ���ݼ��������¼������� */)) {
            Object sealResult = null;
            sealResult = doDisable(billvo);

            if (selectedNode.isLeaf()) {
                getTreeModel().directlyUpdate(sealResult);
            } else {
                getRefreshAction().doAction(e);
            }
        }
    }

    public IBill doDisable(IBill billvo) throws BusinessException {
        
        billvo.getParent().setStatus(VOStatus.UPDATED);
        
        return this.getManageModelService().getManageService().cmnDisableBillData(billvo);
    }

    protected Object[] getUnDataPermissionData() {
        Object obj = getCheckData();
        return CheckDataPermissionUtil.getUnDataPermissionData(
            operateCode,
            mdOperateCode,
            resourceCode,
            getTreeModel().getContext(),
            obj);
    }

    protected Object[] getCheckData() {
        List<Object> checkData = new ArrayList<Object>();
        DefaultMutableTreeNode selectedNode = getTreeModel().getSelectedNode();
        addChildren2CheckData(selectedNode, checkData);
        return checkData.toArray();
    }

    private void addChildren2CheckData(DefaultMutableTreeNode selectedNode,
                                       List<Object> checkData) {
        Enumeration children = selectedNode.preorderEnumeration();
        if (children == null || !children.hasMoreElements()) {
            return;
        }
        while (children.hasMoreElements()) {
            Object child = children.nextElement();
            if (child instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode childx = (DefaultMutableTreeNode) child;
                checkData.add(childx.getUserObject());
            }
        }
    }

    public IValidationService getValidationService() {
        return validationService;
    }

    public void setValidationService(IValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * ͣ��У��
     */
    protected void validate(Object value) {
        if (validationService != null) {
            try {
                validationService.validate(value);
            } catch (ValidationException e) {
                throw new BusinessExceptionAdapter(e);
            }
        }

    }

    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public RefreshAction getRefreshAction() {
        return refreshAction;
    }

    public void setRefreshAction(RefreshAction refreshAction) {
        this.refreshAction = refreshAction;
    }

    public String getMdOperateCode() {
        return mdOperateCode;
    }

    public void setMdOperateCode(String mdOperateCode) {
        this.mdOperateCode = mdOperateCode;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public MMGPDocModelService getManageModelService() {
        return manageModelService;
    }

    public void setManageModelService(MMGPDocModelService manageModelService) {
        this.manageModelService = manageModelService;
    }

}
