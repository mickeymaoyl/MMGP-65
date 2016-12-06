package nc.ui.mmgp.uif2.actions.grand;

import java.awt.event.ActionEvent;

import nc.bs.uif2.BusinessExceptionAdapter;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.ui.pubapp.uif2app.actions.DeleteAction;
import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 21, 2013
 * @author wangweir
 */
@SuppressWarnings("serial")
public class MMGPGrandDeleteAction extends DeleteAction {

    private MainGrandModel mainGrandModel = null;

    private IValidationService validationService;

    public MainGrandModel getMainGrandModel() {
        return this.mainGrandModel;
    }

    public void setMainGrandModel(MainGrandModel mainGrandModel) {
        this.mainGrandModel = mainGrandModel;
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        Object[] vos = this.getMainGrandModel().getSelectedOperaDatas();
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }

        this.validate(vos);

        if (vos.length > 1) {
            this.getMultiBillTaskRunner().setOperateObjs(vos);
            this.getMultiBillTaskRunner().setTitle(
                nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0127")/*
                                                                                                  * @res "单据删除"
                                                                                                  */);
            this.getMultiBillTaskRunner().setMultiReturnObjProcessor(this);
            this.getMultiBillTaskRunner().runTask();
        } else {
            this.getSingleBillService().operateBill(vos[0]);
            this.model.delete();
            this.showSuccessInfo();
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.actions.DeleteAction#checkDataPermission(java.lang.Object)
     */
    @Override
    protected void checkDataPermission(Object obj) throws Exception {
        if (this.needCheckPermission()) {
            return;
        }
        super.checkDataPermission(obj);
    }

    protected boolean needCheckPermission() {
        return (MMStringUtil.isEmptyWithTrim(getOperateCode()) && MMStringUtil.isEmptyWithTrim(getMdOperateCode()))
            || MMStringUtil.isEmptyWithTrim(getResourceCode());
    }

    protected void validate(Object value) {
        if (validationService != null) {
            try {
                validationService.validate(value);
            } catch (ValidationException e) {
                throw new BusinessExceptionAdapter(e);
            }
        }

    }

    /**
     * @return the validationService
     */
    public IValidationService getValidationService() {
        return validationService;
    }

    /**
     * @param validationService
     *        the validationService to set
     */
    public void setValidationService(IValidationService validationService) {
        this.validationService = validationService;
    }

}
