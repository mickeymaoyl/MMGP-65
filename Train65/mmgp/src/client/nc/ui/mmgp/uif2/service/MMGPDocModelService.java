package nc.ui.mmgp.uif2.service;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.itf.mmgp.uif2.IMMGPBillTreeService;
import nc.itf.mmgp.uif2.IMMGPCmnOperateService;
import nc.ui.ml.NCLangRes;
import nc.ui.uif2.model.IAppModelService;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.mmgp.util.MMSystemUtil;
import nc.vo.pub.VOStatus;
import nc.vo.uif2.LoginContext;

/**
 * <b> ͨ��ǰ̨����</b>
 * <p>
 * �ṩͨ�õ����ӡ�ɾ�����޸Ĺ��ܣ���ʵ���������ؽӿ�
 * </p>
 * ��������:2012-11-23
 *
 * @author wangweiu
 */
public class MMGPDocModelService implements IAppModelService {

    private IMMGPCmnOperateService manageService;

    private LoginContext context;

    private String manageServiceItf;

    public String getManageServiceItf() {
        return manageServiceItf;
    }

    public void setManageServiceItf(String itfClassName) {
        try {
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName(itfClassName);
            if (!IMMGPCmnOperateService.class.isAssignableFrom(clazz)
                && !IMMGPBillTreeService.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0027")/*@res "��̨�ӿڱ���̳�nc.itf.mmgp.uif2.IMMGPCmnOperateService"*/);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0092", null, new String[]{itfClassName})/*���õĺ�̨�ӿ�[{0}]������*/);
        }
        this.manageServiceItf = itfClassName;
    }

    public void delete(Object object) throws Exception {
        MMGPVoUtils.setHeadVOStatus(object, VOStatus.DELETED);
        if (MMSystemUtil.isDevelopMode()) {
            getManageService().cmnDeleteBillDataFromDB(object);
        } else {
            getManageService().cmnDeleteBillData(object);
        }
    }

    public Object insert(Object object) throws Exception {
        MMGPVoUtils.setHeadVOStatus(object, VOStatus.NEW);
        return getManageService().cmnSaveBillData(object);
    }

    public Object[] queryByDataVisibilitySetting(LoginContext context) throws Exception {
        return null;
    }

    public Object update(Object object) throws Exception {
        MMGPVoUtils.setHeadVOStatus(object, VOStatus.UPDATED);
        return getManageService().cmnSaveBillData(object);
    }

    public IMMGPCmnOperateService getManageService() {
        if (manageService == null) {
            if (MMStringUtil.isNotEmpty(manageServiceItf)) {
                manageService = (IMMGPCmnOperateService) NCLocator.getInstance().lookup(manageServiceItf);
            } else {
                manageService = NCLocator.getInstance().lookup(IMMGPCmnOperateService.class);
            }
        }
        return manageService;
    }

    public LoginContext getContext() {
        return context;
    }

    public void setContext(LoginContext context) {
        this.context = context;
    }

}