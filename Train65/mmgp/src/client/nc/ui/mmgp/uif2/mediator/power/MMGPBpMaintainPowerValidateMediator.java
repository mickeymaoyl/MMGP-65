package nc.ui.mmgp.uif2.mediator.power;

import java.util.ArrayList;
import java.util.List;

import nc.bs.uif2.validation.IValidationService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.pub.power.PowerValidateService;
import nc.ui.pubapp.uif2app.actions.pflow.AbstractScriptExcAction;
import nc.ui.pubapp.uif2app.validation.CompositeValidation;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ҵ������ʹ�õ�Ȩ��У��
 * </p>
 * 
 * @since �������� Aug 12, 2013
 * @author wangweir
 */
public class MMGPBpMaintainPowerValidateMediator {

    private AbstractScriptExcAction action;

    /**
     * �Ƿ���Ȩ�޿���
     */
    private boolean enable = true;

    // ����ʵ������Ȩ����Ҫ
    private String mdOperateCode = null; // Ԫ���ݲ�������

    private String operateCode = null; // ��Դ����������룬��������ע����һ������ע�룬�򲻽�������Ȩ�޿��ơ�

    private String resourceCode = null; // ҵ��ʵ����Դ����

    private LoginContext context;

    public void init() {
        if (!this.isEnable()) {
            return;
        }

        if (StringUtil.isEmptyWithTrim(getOperateCode())
            && StringUtil.isEmptyWithTrim(getMdOperateCode())
            || StringUtil.isEmptyWithTrim(getResourceCode())) {
            return;
        }

        if (this.getAction() == null) {
            return;
        }

        if (this.getAction().getValidationService() == null) {
            this.getAction().setValidationService(getPowerValidateService());
        } else {
            List<IValidationService> validationServices = new ArrayList<IValidationService>();
            validationServices.add(this.getPowerValidateService());
            validationServices.add(this.getAction().getValidationService());
            CompositeValidation compositeValidation = new CompositeValidation();
            compositeValidation.setValidators(validationServices);
            this.getAction().setValidationService(compositeValidation);
        }
    }

    /**
     * @return the action
     */
    public AbstractScriptExcAction getAction() {
        return action;
    }

    /**
     * @param action
     *        the action to set
     */
    public void setAction(AbstractScriptExcAction action) {
        this.action = action;
    }

    protected PowerValidateService getPowerValidateService() {
        PowerValidateService powerValidateService = new PowerValidateService();

        String opCode = null;
        if (MMStringUtil.isEmptyWithTrim(this.getMdOperateCode())) {
            opCode = this.getOperateCode();
        } else {
            opCode = this.getMdOperateCode();
        }

        powerValidateService.setActionCode(opCode);
        powerValidateService.setPermissionCode(this.getResourceCode());
        powerValidateService.setBillCodeFiledName(this.getBillNo());

        return powerValidateService;
    }

    /**
     * @return
     */
    private String getBillNo() {
        if (this.getContext() == null) {
            return null;
        }
        return MMGPMetaUtils.getBillNoFieldName(this.getContext());
    }

    /**
     * @return the mdOperateCode
     */
    public String getMdOperateCode() {
        return mdOperateCode;
    }

    /**
     * @param mdOperateCode
     *        the mdOperateCode to set
     */
    public void setMdOperateCode(String mdOperateCode) {
        this.mdOperateCode = mdOperateCode;
    }

    /**
     * @return the operateCode
     */
    public String getOperateCode() {
        return operateCode;
    }

    /**
     * @param operateCode
     *        the operateCode to set
     */
    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    /**
     * @return the resourceCode
     */
    public String getResourceCode() {
        return resourceCode;
    }

    /**
     * @param resourceCode
     *        the resourceCode to set
     */
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    /**
     * @return the context
     */
    public LoginContext getContext() {
        return context;
    }

    /**
     * @param context
     *        the context to set
     */
    public void setContext(LoginContext context) {
        this.context = context;
    }

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable
     *        the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
