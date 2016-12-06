package nc.impl.mmgp.uif2;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.billtree.persist.MMGPBillTreeBaseService;
import nc.bs.uif2.validation.ValidationException;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.itf.mmgp.uif2.IMMGPBillTreeService;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.vo.mmgp.batch.MMGPValueObjWithErrLog;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> �������б�ڵ����ʵ����</b>
 * <p>
 * ֧���ұ߷ǵ���Ŀ�Ƭ���棨�ұ����ӱ��ͣ�
 * </p>
 *
 * @since: ��������:Sep 16, 2014
 * @author:liwsh
 */
public class MMGPBillTreeService implements IMMGPBillTreeService {

    /**
     * �����ݿ���ɾ�����ݣ�����ɾ��
     */
    @Override
    public void cmnDeleteBillDataFromDB(Object data) throws BusinessException {

        try {
            deleteValidate(data);
            this.createTreeBaseService((IBill) data).deleteVO((IBill) data, true);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    /**
     * ɾ�����ݣ��߼�ɾ������dr=1
     */
    @Override
    public void cmnDeleteBillData(Object data) throws BusinessException {

        try {
            deleteValidate(data);
            this.createTreeBaseService((IBill) data).deleteVO((IBill) data, false);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    // /**
    // * ��������޸Ĳ���
    // */
    // @Override
    // public <T> T cmnSaveBillData(T data) throws BusinessException {
    //
    // try {
    // if (this.isNewData(data)) {
    // return this.cmnInsertBillData(data);
    // } else {
    // return this.cmnUpdateBillData(data);
    // }
    // } catch (Exception e) {
    // ExceptionUtils.marsh(e);
    // return null;
    // }
    // }

    /**
     * ��������޸Ĳ���
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T cmnSaveBillData(T data) throws BusinessException {

        try {
            /* Oct 29, 2013 wangweir ��������ʵ�ֵĽӿ� Begin */
            Class<IMMGPBillTreeService> interfaceToCall = IMMGPBillTreeService.class;
            for (Class< ? > oneInterface : this.getClass().getInterfaces()) {
                if (IMMGPBillTreeService.class.isAssignableFrom(oneInterface)) {
                    interfaceToCall = (Class<IMMGPBillTreeService>) oneInterface;
                    break;
                }
            }
            /* Oct 29, 2013 wangweir End */

            if (this.isNewData(data)) {
                return NCLocator.getInstance().lookup(interfaceToCall).cmnInsertBillData(data);
            } else {
                return NCLocator.getInstance().lookup(interfaceToCall).cmnUpdateBillData(data);
            }
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ͣ��
     */
    @Override
    public <T> T cmnDisableBillData(T data) throws BusinessException {
        try {
            disableValidate(data);
            return (T) this.createTreeBaseService((IBill) data).disableVO((IBill) data);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ����
     */
    @Override
    public <T> T cmnEnableBillData(T data) throws BusinessException {
        try {
            enableValidate(data);
            return (T) this.createTreeBaseService((IBill) data).enableVO((IBill) data);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ���������񱣴�
     */
    @Override
    public <T> T cmnSaveBillData_RequiresNew(T data) throws BusinessException {
        try {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0065")/*@res "�ݲ�֧�ִ������"*/);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ��������
     */
    @Override
    public <T> T cmnInsertBillData(T data) throws BusinessException {

        try {
            saveValidate(data);
            return (T) this.createTreeBaseService((IBill) data).insertVO((IBill) data);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ���²���
     */
    @Override
    public <T> T cmnUpdateBillData(T data) throws BusinessException {

        try {

            // ��ȫ����VO��TSУ��
            BillTransferTool tool = new BillTransferTool((IBill[]) MMArrayUtil.toArray(data));

            data = (T) tool.getClientFullInfoBill()[0];
            IBill orginData = tool.getOriginBills()[0];

            updateValidate(data);
            return (T) this.createTreeBaseService((IBill) data).updateVO((IBill) data, orginData);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ����Ч
     */
    @Override
    public <T> MMGPValueObjWithErrLog cmnManageEnableBillData(T[] datas) throws BusinessException {
        try {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0065")/*@res "�ݲ�֧�ִ������"*/);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * ��ʧЧ
     */
    @Override
    public <T> MMGPValueObjWithErrLog cmnManageDisableBillData(T[] datas) throws BusinessException {
        try {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0065")/*@res "�ݲ�֧�ִ������"*/);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * �Ƿ�Ϊ��������
     *
     * @param data
     *        ���ݶ���
     * @return true ������false ��������
     * @throws MetaDataException
     */
    protected boolean isNewData(Object data) throws MetaDataException {

        IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(data.getClass().getName());

        NCObject ncObj = NCObject.newInstance(data);
        String pkFieldName = bean.getTable().getPrimaryKeyName();

        Object pkObj = ncObj.getAttributeValue(bean.getAttributeByName(pkFieldName));
        return pkObj == null;
    }

    /**
     * ��������б�����̨�־û�������
     *
     * @param object
     *        ���ݶ���
     * @return ��̨�־û�����
     * @throws BusinessException
     *         ҵ���쳣
     */
    protected <T extends IBill> MMGPBillTreeBaseService<T> createTreeBaseService(T object) throws BusinessException {
        String mdID = getIbean(object).getID();
        MMGPBillTreeBaseService<T> treeService = new MMGPBillTreeBaseService<T>(mdID, null);
        return treeService;
    }

    /**
     * ���Ԫ����ʵ��id
     *
     * @param obj
     *        ���ݶ���
     * @return Ԫ����ʵ��id
     * @throws BusinessException
     *         ҵ���쳣
     */
    protected IBean getIbean(Object obj) throws BusinessException {
        return MDBaseQueryFacade.getInstance().getBeanByFullClassName(obj.getClass().getName());
    }

    /**
     * Ԥ������У��
     *
     * @param data
     * @throws ValidationException
     */
    protected void saveValidate(Object data) throws ValidationException {

    }

    /**
     * Ԥ������У��
     *
     * @param data
     * @throws ValidationException
     */
    protected void updateValidate(Object data) throws ValidationException {

    }

    /**
     * Ԥ��ɾ��У��
     *
     * @param data
     * @throws ValidationException
     */
    protected void deleteValidate(Object data) throws ValidationException {

    }

    /**
     * Ԥ��ͣ��У��
     *
     * @param data
     * @throws ValidationException
     */
    protected void disableValidate(Object data) throws ValidationException {

    }

    /**
     * Ԥ������У��
     *
     * @param data
     * @throws ValidationException
     */
    protected void enableValidate(Object data) throws ValidationException {

    }

}