package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.bs.uif2.IActionCode;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.ui.bd.pub.actions.ManageModeActionInterceptor;
import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.uif2.service.MMGPDocModelService;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.actions.CompositeActionInterceptor;
import nc.ui.uif2.components.CommonConfirmDialogUtils;
import nc.ui.uif2.editor.IEditor;
import nc.ui.uif2.model.AbstractAppModel;
import nc.uif2.annoations.MethodType;
import nc.uif2.annoations.ModelMethod;
import nc.uif2.annoations.ModelType;
import nc.uif2.annoations.ViewMethod;
import nc.uif2.annoations.ViewType;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 树主子表档案型节点按钮 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeEnableAction extends NCAction {

    private AbstractAppModel model;

    private IEditor editor;

    private IValidationService validationService;

    // 以下实现数据权限需要
    private String mdOperateCode = null; // 元数据操作编码

    private String operateCode = null; // 资源对象操作编码，以上两者注入其一，都不注入，则不进行数据权限控制。

    private String resourceCode = null; // 业务实体资源编码

    /**
     * 服务
     */
    private MMGPDocModelService manageModelService;

    private ManageModeActionInterceptor manageModeActionInterceptor = new ManageModeActionInterceptor();

    private CompositeActionInterceptor interceptor;

    public MMGPBillTreeEnableAction() {
        super();
        ActionInitializer.initializeAction(this, IActionCode.ENABLE);

        interceptor = new CompositeActionInterceptor();
        super.setInterceptor(interceptor);
        interceptor.add(manageModeActionInterceptor);
        setInterceptor(manageModeActionInterceptor);

    }

    @Override
    public void setInterceptor(ActionInterceptor interceptor) {
        this.interceptor.add(interceptor);

    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        checkPermission();

        validate();

        doEnable();

        //maoyl   
        String msg =NCLangRes.getInstance().getStrByID("uif2", "IShowMsgConstant-000014");//原63中/*启用成功。*/;
        ShowStatusBarMsgUtil.showStatusBarMsg(msg, model.getContext());
    }

    protected void doEnable() throws Exception {

        IBill vo = (IBill) getModel().getSelectedData();

        if (UIDialog.ID_YES == CommonConfirmDialogUtils.showConfirmEnableDialog(getModel()
            .getContext()
            .getEntranceUI())) {
            Object newobj = doEnable(vo);
            if (newobj instanceof Object[]) {
                model.directlyUpdate((Object[]) newobj);
            } else {
                model.directlyUpdate(newobj);
            }
        }
    }

    /**
     * @param vo
     * @return
     * @throws BusinessException
     */
    private Object doEnable(IBill vo) throws BusinessException {

        vo.getParent().setStatus(VOStatus.UPDATED);

        return this.getManageModelService().getManageService().cmnEnableBillData(vo);

    }

    protected void validate() {
        IBill vo = (IBill) getModel().getSelectedData();
        validate(vo);
    }

    protected void checkPermission() throws Exception {
        Object[] objs = getUnDataPermissionData();
        if (objs != null && objs.length > 0) {
            throw new BusinessException(IShowMsgConstant.getDataPermissionInfo());
        }
    }

    protected Object[] getUnDataPermissionData() {
        Object obj = getCheckData();
        return CheckDataPermissionUtil.getUnDataPermissionData(
            operateCode,
            mdOperateCode,
            resourceCode,
            model.getContext(),
            obj);
    }

    protected Object getCheckData() {
        return getModel().getSelectedData();
    }

    @Override
    protected boolean isActionEnable() {
        // 未启用或已停用的数据的启用按钮可用
        return getModel().getUiState() == UIState.NOT_EDIT
            && model.getSelectedData() != null
            && !isCurrentDataEnable();
    }

    @ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.GETTER)
    public AbstractAppModel getModel() {
        return model;
    }

    @ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.SETTER)
    public void setModel(AbstractAppModel model) {
        this.model = model;
        model.addAppEventListener(this);

        manageModeActionInterceptor.setModel(model);
    }

    @ViewMethod(viewType = ViewType.IEditor, methodType = MethodType.GETTER)
    public IEditor getEditor() {
        return editor;
    }

    @ViewMethod(viewType = ViewType.IEditor, methodType = MethodType.SETTER)
    public void setEditor(IEditor editor) {
        this.editor = editor;
    }

    public boolean isCurrentDataEnable() {
        // 返回当前选择数据的是否启用标记
        IBill vo = (IBill) getModel().getSelectedData();
        return vo.getParent().getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD) != null
            && (IPubEnumConst.ENABLESTATE_ENABLE == (Integer) vo.getParent().getAttributeValue(
                IBaseServiceConst.ENABLESTATE_FIELD));
    }

    public IValidationService getValidationService() {
        return validationService;
    }

    public void setValidationService(IValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * 启用校验
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
