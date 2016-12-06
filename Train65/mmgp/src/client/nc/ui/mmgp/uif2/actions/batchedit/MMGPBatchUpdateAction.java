package nc.ui.mmgp.uif2.actions.batchedit;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.CheckDataPermissionUtil;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.IMultiRowSelectModel;
import nc.uif2.annoations.MethodType;
import nc.uif2.annoations.ModelMethod;
import nc.uif2.annoations.ModelType;
import nc.vo.pub.BusinessException;

public class MMGPBatchUpdateAction extends NCAction {

    // 以下实现数据权限需要
    private String mdOperateCode = null; // 元数据操作编码

    private String operateCode = null; // 资源对象操作编码，以上两者注入其一，都不注入，则不进行数据权限控制。

    private String resourceCode = null; // 业务实体资源编码

    /** 批改按钮 */
    public static final String BTN_BATCHEDIT = "batchUpdate";

    /**
     *
     */
    private static final long serialVersionUID = -6681207249528400285L;

    private final String btnName = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000085")/*@res "批改"*/;

    private BillManageModel model;

    private BillForm billForm;

    private MMGPBatchUpdateContext updateContext;

    public MMGPBatchUpdateContext getUpdateContext() {
        return updateContext;
    }

    public void setUpdateContext(MMGPBatchUpdateContext updateContext) {
        this.updateContext = updateContext;
    }

    public MMGPBatchUpdateAction() {
        super();
        setCode(BTN_BATCHEDIT);
        setBtnName(btnName);
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        checkDataPermission();

        MMGPBatchUpdateContext batchcontext = getUpdateContext();
        batchcontext.setFuncode(this.getModel().getContext().getNodeCode());
        batchcontext.setPk_org(this.getModel().getContext().getPk_org());
        batchcontext.setUserid(this.getModel().getContext().getPk_loginUser());
        batchcontext.setPk_group(this.getModel().getContext().getPk_group());
        batchcontext.setNodekey(getBillForm().getNodekey());
        batchcontext.setModel(getModel());

        MMGPBatchUpdateDlg updateDlg =
                new MMGPBatchUpdateDlg(
                    this.getModel().getContext().getEntranceUI(),
                    getBillForm().getTemplateContainer(),
                    batchcontext);
        batchcontext.setDatas(getSelectVOs());
        beforeShowdlg();
        updateDlg.showModal();

    }

    protected void checkDataPermission() throws Exception {
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
        if (getModel() instanceof IMultiRowSelectModel) {
            Object[] objs = ((IMultiRowSelectModel) getModel()).getSelectedOperaDatas();
            if (objs != null && objs.length > 0) {
                return objs;
            }
        }
        return getModel().getSelectedData();
    }

    protected void beforeShowdlg() throws BusinessException {

    }

    @ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.SETTER)
    public void setModel(BillManageModel model) {
        this.model = model;
        model.addAppEventListener(this);
    }

    @ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.GETTER)
    public BillManageModel getModel() {
        return model;
    }

    protected Object[] getSelectVOs() throws BusinessException {
        Object[] selectVOs = getModel().getSelectedOperaDatas();
        if (selectVOs == null || selectVOs.length == 0) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0008")/*@res "请先选择批改数据！"*/);
        }
        return selectVOs;
    }

    protected boolean isActionEnable() {
        return this.getModel().getUiState() == UIState.NOT_EDIT && this.getModel().getSelectedData() != null;
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

    public BillForm getBillForm() {
        return billForm;
    }

    public void setBillForm(BillForm billForm) {
        this.billForm = billForm;
    }

}