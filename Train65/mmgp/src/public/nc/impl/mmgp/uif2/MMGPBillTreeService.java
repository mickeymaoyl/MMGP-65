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
 * <b> 左树右列表节点服务实现类</b>
 * <p>
 * 支持右边非单表的卡片界面（右边主子表型）
 * </p>
 *
 * @since: 创建日期:Sep 16, 2014
 * @author:liwsh
 */
public class MMGPBillTreeService implements IMMGPBillTreeService {

    /**
     * 从数据库中删除数据，物理删除
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
     * 删除数据，逻辑删除，置dr=1
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
    // * 保存或者修改操作
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
     * 保存或者修改操作
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T cmnSaveBillData(T data) throws BusinessException {

        try {
            /* Oct 29, 2013 wangweir 查找子类实现的接口 Begin */
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
     * 停用
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
     * 启用
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
     * 启用新事务保存
     */
    @Override
    public <T> T cmnSaveBillData_RequiresNew(T data) throws BusinessException {
        try {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0065")/*@res "暂不支持此项操作"*/);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 新增操作
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
     * 更新操作
     */
    @Override
    public <T> T cmnUpdateBillData(T data) throws BusinessException {

        try {

            // 补全差异VO，TS校验
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
     * 批生效
     */
    @Override
    public <T> MMGPValueObjWithErrLog cmnManageEnableBillData(T[] datas) throws BusinessException {
        try {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0065")/*@res "暂不支持此项操作"*/);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 批失效
     */
    @Override
    public <T> MMGPValueObjWithErrLog cmnManageDisableBillData(T[] datas) throws BusinessException {
        try {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0065")/*@res "暂不支持此项操作"*/);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 是否为新增数据
     *
     * @param data
     *        数据对象
     * @return true 新增，false 不是新增
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
     * 获得树形列表界面后台持久化服务类
     *
     * @param object
     *        数据对象
     * @return 后台持久化服务
     * @throws BusinessException
     *         业务异常
     */
    protected <T extends IBill> MMGPBillTreeBaseService<T> createTreeBaseService(T object) throws BusinessException {
        String mdID = getIbean(object).getID();
        MMGPBillTreeBaseService<T> treeService = new MMGPBillTreeBaseService<T>(mdID, null);
        return treeService;
    }

    /**
     * 获得元数据实体id
     *
     * @param obj
     *        数据对象
     * @return 元数据实体id
     * @throws BusinessException
     *         业务异常
     */
    protected IBean getIbean(Object obj) throws BusinessException {
        return MDBaseQueryFacade.getInstance().getBeanByFullClassName(obj.getClass().getName());
    }

    /**
     * 预留保存校验
     *
     * @param data
     * @throws ValidationException
     */
    protected void saveValidate(Object data) throws ValidationException {

    }

    /**
     * 预留更新校验
     *
     * @param data
     * @throws ValidationException
     */
    protected void updateValidate(Object data) throws ValidationException {

    }

    /**
     * 预留删除校验
     *
     * @param data
     * @throws ValidationException
     */
    protected void deleteValidate(Object data) throws ValidationException {

    }

    /**
     * 预留停用校验
     *
     * @param data
     * @throws ValidationException
     */
    protected void disableValidate(Object data) throws ValidationException {

    }

    /**
     * 预留启用校验
     *
     * @param data
     * @throws ValidationException
     */
    protected void enableValidate(Object data) throws ValidationException {

    }

}