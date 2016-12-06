package nc.bs.mmgp.billtree.persist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.bd.baseservice.BDUniqueFieldTrimUtil;
import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.bd.baseservice.busilog.BDBusiLogUtil;
import nc.bs.bd.baseservice.busilog.IBDBusiLogUtil;
import nc.bs.bd.baseservice.busilog.IBusiOperateConst;
import nc.bs.bd.baseservice.md.ITreeUpdateWithChildren;
import nc.bs.bd.baseservice.validator.BDTreeEnableValidator;
import nc.bs.bd.baseservice.validator.BDTreeParentEnableValidator;
import nc.bs.bd.baseservice.validator.BDTreeUpdateLoopValidator;
import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationFrameworkUtil;
import nc.bs.uif2.validation.Validator;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.vo.bd.pub.DistributedAddBaseValidator;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.pub.SingleDistributedDeleteValidator;
import nc.vo.bd.pub.SingleDistributedUpdateValidator;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.util.AuditInfoUtil;
import nc.vo.util.BDReferenceChecker;
import nc.vo.util.BDUniqueRuleValidate;
import nc.vo.util.BDVersionValidationUtil;
import nc.vo.util.bizlock.BizlockDataUtil;

import org.apache.commons.lang.ArrayUtils;

/**
 * <b> 左树右单据后台服务类 </b>
 * <p>
 * 左边树形结构，右边主子表形式的节点后台服务类
 * </p>
 *
 * @since: 创建日期:Sep 15, 2014
 * @author:liwsh
 */
public class MMGPBillTreeBaseService<T extends IBill> {

    private String MDId;

    private IBDBusiLogUtil busiLogUtil = null;

    private IMMGPBillTreePersistenceUtil<IBill> persistenceUtil;

    private MMGPBillTreeRootInnerCodeLockUtil<IBill> rootLockUtil = null;

    public MMGPBillTreeBaseService(String mdID) {
        this(mdID, null);
    }

    public MMGPBillTreeBaseService(String mdID,
                                   IMMGPBillTreePersistenceUtil<IBill> persistenceUtil) {
        this.MDId = mdID;
        this.persistenceUtil = persistenceUtil;
    }

    public String getMDId() {
        return MDId;
    }

    protected IMMGPBillTreePersistenceUtil<IBill> getPersistenceUtil() {
        if (persistenceUtil == null) {
            persistenceUtil = new MMGPBillTreePersistenceUtil<IBill>(getMDId());
        }
        return persistenceUtil;
    }

    protected MMGPBillTreeRootInnerCodeLockUtil<IBill> getRootLockUtil() {
        if (rootLockUtil == null) {
            rootLockUtil = new MMGPBillTreeRootInnerCodeLockUtil<IBill>(getPersistenceUtil());
        }
        return rootLockUtil;
    }

    protected void setBusiLogUtil(IBDBusiLogUtil busiLogUtil) {
        this.busiLogUtil = busiLogUtil;
    }

    protected IBDBusiLogUtil getBusiLogUtil() {
        if (busiLogUtil == null) {
            busiLogUtil = new BDBusiLogUtil(getMDId());
        }
        return busiLogUtil;
    }

    /** *********************** 删除操作 ***************************** */
    public void deleteVO(T vo,
                         boolean deleteFromDB) throws BusinessException {
        if (vo == null) return;

        // 加锁操作：为所在分支的根编码加锁
        deleteLockOperate(vo);

        // 版本校验
        deleteVersionValidate(vo);

        // 逻辑校验
        deleteValidateVO(vo);

        // 事件前通知
        fireBeforeDeleteEvent(vo);

        // 缓存通知
        notifyVersionChangeWhenDataDeleted(vo);

        // DB操作: 删除业务VO, 更新内部编码
        dbDeleteVO(vo, deleteFromDB);

        // 事件后通知
        fireAfterDeleteEvent(vo);

        // 业务日志
        writeDeletedBusiLog(vo);
    }

    protected void deleteLockOperate(T vo) throws BusinessException {
        getRootLockUtil().lockBranchRoot(vo);
    }

    protected void deleteVersionValidate(T vo) throws BusinessException {
        BDVersionValidationUtil.validateSuperVO((SuperVO) vo.getParent());
    }

    protected void deleteValidateVO(T vo) throws BusinessException {
        IValidationService validateService = ValidationFrameworkUtil.createValidationService(getDeleteValidator());
        validateService.validate(vo.getParent());
    }

    protected Validator[] getDeleteValidator() {
        Validator[] validators =
                new Validator[]{BDReferenceChecker.getInstance(), new SingleDistributedDeleteValidator() };
        return validators;
    }

    protected void fireBeforeDeleteEvent(T vo) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.dispatchDeleteBeforeEvent(vo);
    }

    protected void fireAfterDeleteEvent(T vo) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.dispatchDeleteAfterEvent(vo);
    }

    protected void dbDeleteVO(T vo,
                              boolean deleteFromDB) throws BusinessException {
        getPersistenceUtil().deleteVO(deleteFromDB, vo);
    }

    protected void notifyVersionChangeWhenDataDeleted(T vo) throws BusinessException {
        getPersistenceUtil().notifyVersionChangeWhenDataDeleted(vo);
    }

    protected void writeDeletedBusiLog(T vo) throws BusinessException {
        getBusiLogUtil().writeBusiLog(IBusiOperateConst.DELETE, null, (SuperVO) vo.getParent());
    }

    /** *********************** 新增操作 ***************************** */
    public T insertVO(T vo) throws BusinessException {
        if (vo == null) return null;

        // 对唯一性字段进行trim处理
        getUniqueFieldTrimUtil().trimUniqueFields((SuperVO) vo.getParent());

        // 加锁操作：为所在分支的根编码加主键锁； 为自己加业务锁；
        insertLockOperate(convertToVOArray(vo));

        // 逻辑校验
        insertValidateVO(vo);

        // 添加审计信息
        setInsertAuditInfo(convertToVOArray(vo));

        // 事件前通知
        fireBeforeInsertEvent(convertToVOArray(vo));

        // DB操作：保存新增VO; 更新内部编码; 重新检索新VO;
        String pk = dbInsertVO(vo);

        vo = retrieveVO(pk);

        // 缓存通知
        notifyVersionChangeWhenDataInserted(convertToVOArray(vo));

        // 事件后通知
        fireAfterInsertEvent(convertToVOArray(vo));

        // 业务日志
        writeInsertBusiLog(convertToVOArray(vo));

        // 返回新VO
        return vo;
    }

    public void insertVOs(T[] vos) throws BusinessException {
        if (vos == null || vos.length == 0) return;

        // 对唯一性字段进行trim处理
        SuperVO[] parents = this.getParentVOs(vos);
        getUniqueFieldTrimUtil().trimUniqueFields(parents);

        // 加锁操作：为所在分支的根编码加主键锁； 为自己加业务锁；
        insertLockOperate(vos);

        // 逻辑校验
        insertValidateVO(vos);

        // 添加审计信息
        setInsertAuditInfo(vos);

        // 事件前通知
        fireBeforeInsertEvent(vos);

        // DB操作：保存新增VO; 更新内部编码;
        String[] pks = dbInsertVOs(vos);

        vos = retrieveVO(pks);

        // 缓存通知
        notifyVersionChangeWhenDataInserted(vos);

        // 事件后通知
        fireAfterInsertEvent(vos);

        // 业务日志
        writeInsertBusiLog(vos);
    }

    protected void insertLockOperate(T... vos) throws BusinessException {
        List<T> needToLockRoot = getParentNotExistsVOs(vos);
        for (T vo : needToLockRoot) {
            getRootLockUtil().lockBranchRoot(vo);
        }
        BizlockDataUtil.lockDataByBizlock(vos);
    }

    private List<T> getParentNotExistsVOs(T... vos) throws BusinessException {
        Set<String> pkSet = new HashSet<String>();
        for (int i = 0; i < vos.length; i++) {
            pkSet.add(vos[i].getPrimaryKey());
        }
        List<T> parentNotInPkSetVOs = new ArrayList<T>();
        String parentField = getPersistenceUtil().getParentPkFieldName();
        for (int i = 0; i < vos.length; i++) {
            if (!pkSet.contains(vos[i].getParent().getAttributeValue(parentField))) {
                parentNotInPkSetVOs.add(vos[i]);
            }
        }
        return parentNotInPkSetVOs;
    }

    protected void insertValidateVO(T vo) throws BusinessException {
        IValidationService validationService = ValidationFrameworkUtil.createValidationService(getInsertValidator());
        validationService.validate(vo.getParent());
    }

    protected void insertValidateVO(T[] vos) throws BusinessException {
        IValidationService validationService = ValidationFrameworkUtil.createValidationService(getInsertValidator());
        validationService.validate(this.getParentVOs(vos));
    }

    protected Validator[] getInsertValidator() {
        // 上级停用校验
        BDTreeParentEnableValidator parentValidator = new BDTreeParentEnableValidator(null);

        IBean bean = null;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        Validator[] validators;
        if (bean != null && bean.getAttributeByName(IBaseServiceConst.ENABLESTATE_FIELD) != null) {
            validators =
                    new Validator[]{new BDUniqueRuleValidate(), new DistributedAddBaseValidator(), parentValidator };
        } else {
            validators = new Validator[]{new BDUniqueRuleValidate(), new DistributedAddBaseValidator() };
        }
        return validators;
    }

    protected void setInsertAuditInfo(T... vos) throws BusinessException {
        this.updateInsertInfo(vos);
    }

    protected void fireBeforeInsertEvent(T... vos) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.dispatchInsertBeforeEvent((Object[]) vos);
    }

    protected void fireAfterInsertEvent(T... vos) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.dispatchInsertAfterEvent((Object[]) vos);
    }

    protected String dbInsertVO(T vo) throws BusinessException {
        String[] pks = getPersistenceUtil().insertVOWithInnerCode(convertToVOArray(vo));
        return (pks != null && pks.length > 0) ? pks[0] : null;
    }

    protected String[] dbInsertVOs(T... vos) throws BusinessException {
        return getPersistenceUtil().insertVOWithInnerCode(vos);
    }

    protected void notifyVersionChangeWhenDataInserted(T... vos) throws BusinessException {
        getPersistenceUtil().notifyVersionChangeWhenDataInserted(vos);
    }

    protected void writeInsertBusiLog(T... vos) throws BusinessException {

        SuperVO[] parentVOs = getParentVOs(vos);

        getBusiLogUtil().writeBusiLog(IBusiOperateConst.ADD, null, parentVOs);
    }

    private SuperVO[] getParentVOs(T... vos) {
        SuperVO[] parentVOs = new SuperVO[vos.length];
        for (int i = 0; i < vos.length; i++) {
            parentVOs[i] = (SuperVO) vos[i].getParent();
        }
        return parentVOs;
    }

    /** *********************** 修改操作 ***************************** */
    public T updateVO(T vo,
                      T oldVO) throws BusinessException {
        if (vo == null) return null;

        // 对唯一性字段进行trim处理
        getUniqueFieldTrimUtil().trimUniqueFields((SuperVO) vo.getParent());

        // 加锁操作：为所在分支的根编码加主键锁；如果修改到另一分支，还要为新分支的根主键加锁； 为自己加业务锁；(加锁放后面，因为前台传入的是差异VO，导致加锁出错)
        updateLockOperate(vo);

        // 逻辑校验
        updateValidateVO(oldVO, vo);

        // 更新审计信息
        setUpdateAuditInfo(convertToVOArray(vo));

        // 事件前通知
        fireBeforeUpdateEvent(oldVO, vo);

        // DB操作：保存修改的VO; 更新内部编码; 重新检索新VO;
        dbUpdateVO(oldVO, vo);

        vo = retrieveVO(vo.getPrimaryKey());

        // 缓存通知
        notifyVersionChangeWhenDataUpdated(convertToVOArray(vo));

        // 事件后通知
        fireAfterUpdateEvent(oldVO, vo);

        // 业务日志
        writeUpdatedBusiLog(convertToVOArray(oldVO), convertToVOArray(vo));

        // 返回新VO
        return vo;
    }

    /**
     * 用户编码树，直接修改innercode从而更新所有下级的innercode所提供的服务，本版本暂不支持。<br\>
     * 另外就是主键树了，主键树innercode的修改只可能所属父节点发生变动。<br\>
     * 如果属于这种情况，它的所有下级的子节点的innercode也要跟着修改。在updateVO方法里面有所体现<br\>
     * 后续如果要实现，可模仿TreeBaseService类
     *
     * @param vo
     * @param updatWithChildren
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("rawtypes")
    public T updateVOWithSub(T vo,
                             ITreeUpdateWithChildren updatWithChildren) throws BusinessException {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0044")/*@res "暂不支持此项业务"*/);
        return null;
    }

    protected void updateLockOperate(T vo) throws BusinessException {
        getRootLockUtil().lockBranchRootWhenParentChanged(vo);
        BizlockDataUtil.lockDataByBizlock(vo);
    }

    protected void updateValidateVO(T oldVO,
                                    T vo) throws BusinessException {
        IValidationService validationService =
                ValidationFrameworkUtil.createValidationService(getUpdateValidator(oldVO));
        validationService.validate(vo.getParent());
    }

    protected Validator[] getUpdateValidator(T oldVO) {
        // 上下级循环引用校验
        BDTreeUpdateLoopValidator loopValidator = new BDTreeUpdateLoopValidator();
        // 上级停用校验
        BDTreeParentEnableValidator parentValidator = new BDTreeParentEnableValidator((SuperVO) oldVO.getParent());

        IBean bean = null;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        Validator[] validators;
        if (bean != null && bean.getAttributeByName(IBaseServiceConst.ENABLESTATE_FIELD) != null) {
            validators =
                    new Validator[]{
                        new BDUniqueRuleValidate(),
                        new SingleDistributedUpdateValidator(),
                        loopValidator,
                        parentValidator };
        } else {
            validators =
                    new Validator[]{new BDUniqueRuleValidate(), new SingleDistributedUpdateValidator(), loopValidator };
        }

        return validators;
    }

    protected void setUpdateAuditInfo(T... vo) {
        updateModifyInfo(vo);
    }

    protected void fireBeforeUpdateEvent(T oldVO,
                                         T vo) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.setOldObjs(new Object[]{oldVO });
        eventUtil.dispatchUpdateBeforeEvent(vo);
    }

    protected void fireAfterUpdateEvent(T oldVO,
                                        T vo) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.setOldObjs(new Object[]{oldVO });
        eventUtil.dispatchUpdateAfterEvent(vo);
    }

    protected void dbUpdateVO(T oldVO,
                              T vo) throws BusinessException {
        getPersistenceUtil().updateVOWithInnerCode(vo, oldVO);
    }

    protected void notifyVersionChangeWhenDataUpdated(T... vos) throws BusinessException {
        getPersistenceUtil().notifyVersionChangeWhenDataInserted(vos);
    }

    protected void writeUpdatedBusiLog(T[] oldVOs,
                                       T[] vos) throws BusinessException {

        SuperVO[] parentVOs = getParentVOs(vos);
        SuperVO[] oldparentVOs = getParentVOs(oldVOs);
        getBusiLogUtil().writeModefyBusiLog(IBusiOperateConst.EDIT, parentVOs, oldparentVOs);
    }

    /** *********************** 启用操作 ***************************** */
    public T enableVO(T vo) throws BusinessException {
        if (vo == null) return null;

        // 为所在分支的根加主键锁
        enableLockOperate(vo);

        // 版本校验
        enableVersionValidate(vo);

        if (isDataEnabled(vo)) {
            return vo;
        }

        // 逻辑校验
        enableValidateVO(vo);

        // 更新审计信息
        setEnableAuditInfo(vo);

        // 设置启用标志
        setEnableFlag(vo);

        T oldVO = retrieveVOByFields(new String[]{IBaseServiceConst.ENABLESTATE_FIELD }, vo.getPrimaryKey());

        // 事件前通知
        fireBeforeEnableEvent(oldVO, vo);

        // DB操作：保存更新；重新检索出启用数据；
        dbEnableVO(vo);

        vo = retrieveVO(vo.getPrimaryKey());

        // 缓存通知
        notifyVersionChangeWhenDataUpdated(convertToVOArray(vo));

        // 事件后通知
        fireAfterEnableEvent(oldVO, vo);

        // 业务日志
        writeEnableBusiLog(vo);

        // 返回启用的VO
        return vo;
    }

    protected void enableLockOperate(T vo) throws BusinessException {
        getRootLockUtil().lockBranchRoot(vo);
    }

    protected void enableVersionValidate(T vo) throws BusinessException {
        BDVersionValidationUtil.validateSuperVO((SuperVO) vo.getParent());
    }

    protected boolean isDataEnabled(T vo) {
        Integer enable_state = (Integer) vo.getParent().getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD);
        return IPubEnumConst.ENABLESTATE_ENABLE == enable_state;
    }

    protected void enableValidateVO(T vo) throws BusinessException {
        IValidationService validateService = ValidationFrameworkUtil.createValidationService(getEnableValidator());
        validateService.validate(vo.getParent());
    }

    protected Validator[] getEnableValidator() throws BusinessException {
        return new Validator[]{new BDTreeEnableValidator(), new SingleDistributedUpdateValidator() };
    }

    protected void setEnableAuditInfo(T vo) {
        AuditInfoUtil.updateData(vo.getParent());
    }

    protected void setEnableFlag(T vo) {
        vo.getParent().setAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD, IPubEnumConst.ENABLESTATE_ENABLE);
    }

    protected void fireBeforeEnableEvent(T oldVO,
                                         T vo) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        if (IPubEnumConst.ENABLESTATE_DISABLE == (Integer) oldVO.getParent().getAttributeValue(
            IBaseServiceConst.ENABLESTATE_FIELD)) {
            eventUtil.dispatchDisableToEnableBeforeEvent(vo);
        } else {
            eventUtil.dispatchInitToEnableBeforeEvent(vo);
        }
    }

    protected void fireAfterEnableEvent(T oldVO,
                                        T vo) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        if (IPubEnumConst.ENABLESTATE_DISABLE == (Integer) oldVO.getParent().getAttributeValue(
            IBaseServiceConst.ENABLESTATE_FIELD)) {
            eventUtil.dispatchDisableToEnableAfterEvent(vo);
        } else {
            eventUtil.dispatchInitToEnableAfterEvent(vo);
        }
    }

    protected void dbEnableVO(T vo) throws BusinessException {
        String[] updateFields =
                new String[]{
                    IBaseServiceConst.ENABLESTATE_FIELD,
                    IBaseServiceConst.MODIFIER_FIELD,
                    IBaseServiceConst.MODIFIEDTIME_FIELD };
        getPersistenceUtil().updateVOWithAttrs(updateFields, convertToVOArray(vo));
    }

    protected void writeEnableBusiLog(T vo) throws BusinessException {
        getBusiLogUtil().writeBusiLog(IBusiOperateConst.ENABLE, null, (SuperVO) vo.getParent());
    }

    /** *********************** 启用操作 ***************************** */
    public T disableVO(T vo) throws BusinessException {
        if (vo == null) return null;

        // 加锁
        disableLockOperate(vo);

        // 版本校验
        disableVersionValidate(vo);

        if (isDataDisabled(vo)) {
            return vo;
        }

        // 业务校验
        disableValidateVO(vo);

        T[] needDisableVOs = retrieveSubVOsWithSelf(vo);

        // 审计信息
        setDisableAuditInfo(needDisableVOs);

        // 设置停用标志
        setDisableFlag(needDisableVOs);

        // 事前通知
        fireBeforeDisableEvent(needDisableVOs);

        // DB操作：保存更新；重新检索被启用的VO
        dbDisable(needDisableVOs);

        needDisableVOs = retrieveVO(getPrimaryKeyArray(needDisableVOs));

        // 缓存通知
        notifyVersionChangeWhenDataUpdated(needDisableVOs);

        // 事件后通知
        fireAfterDisableEvent(needDisableVOs);

        // 业务日志
        writeDisableBusiLog(needDisableVOs);

        return needDisableVOs[0];
    }

    /**
     * 获取表头主键
     *
     * @param vos
     *        数据
     * @return
     */
    private String[] getPrimaryKeyArray(T[] vos) {

        if (MMArrayUtil.isEmpty(vos)) {
            return null;
        }

        String[] pks = new String[vos.length];
        for (int i = 0; i < vos.length; i++) {
            pks[i] = vos[i].getPrimaryKey();
        }
        return pks;
    }

    protected void disableLockOperate(T vo) throws BusinessException {
        getRootLockUtil().lockBranchRoot(vo);
    }

    protected void disableVersionValidate(T vo) throws BusinessException {
        BDVersionValidationUtil.validateSuperVO((SuperVO) vo.getParent());
    }

    protected boolean isDataDisabled(T vo) {
        Integer enable_state = (Integer) vo.getParent().getAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD);
        return IPubEnumConst.ENABLESTATE_DISABLE == enable_state;
    }

    protected void disableValidateVO(T vo) throws BusinessException {
        IValidationService validateService = ValidationFrameworkUtil.createValidationService(getDisableValidator());
        validateService.validate(vo.getParent());
    }

    protected Validator[] getDisableValidator() throws BusinessException {
        return new Validator[]{new SingleDistributedUpdateValidator() };
    }

    protected void setDisableAuditInfo(T[] vos) {

        updateModifyInfo(vos);
    }

    /**
     * 更新修改人修改时间
     *
     * @param vos
     */
    private void updateModifyInfo(T[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }

        ISuperVO[] parentvos = new ISuperVO[vos.length];
        for (int i = 0; i < vos.length; i++) {
            parentvos[i] = vos[i].getParent();
        }

        AuditInfoUtil.updateData(parentvos);
    }


    /**
     * 更新创建人创建时间
     *
     * @param vos
     */
    private void updateInsertInfo(T[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }

        ISuperVO[] parentvos = new ISuperVO[vos.length];
        for (int i = 0; i < vos.length; i++) {
            parentvos[i] = vos[i].getParent();
        }

        AuditInfoUtil.addData(parentvos);
    }


    protected void setDisableFlag(T[] vos) {
        for (T vo : vos) {
            vo.getParent().setAttributeValue(IBaseServiceConst.ENABLESTATE_FIELD, IPubEnumConst.ENABLESTATE_DISABLE);
        }
    }

    protected void fireBeforeDisableEvent(T[] vos) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.dispatchEnableToDisableBeforeEvent((Object[]) vos);
    }

    protected void fireAfterDisableEvent(T[] vos) throws BusinessException {
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(getMDId());
        eventUtil.dispatchEnableToDisableAfterEvent((Object[]) vos);
    }

    protected void dbDisable(T[] vos) throws BusinessException {
        String[] updateFields =
                new String[]{
                    IBaseServiceConst.ENABLESTATE_FIELD,
                    IBaseServiceConst.MODIFIER_FIELD,
                    IBaseServiceConst.MODIFIEDTIME_FIELD };
        getPersistenceUtil().updateVOWithAttrs(updateFields, vos);
    }

    protected void writeDisableBusiLog(T... vos) throws BusinessException {

        SuperVO[] parentVOs = getParentVOs(vos);
        getBusiLogUtil().writeBusiLog(IBusiOperateConst.DISABLE, null, parentVOs);
    }

    /** ************************* 检索VO操作 ****************************** */
    protected T retrieveVO(String pk) throws BusinessException {
        T[] vos = retrieveVO(new String[]{pk });
        return ArrayUtils.isEmpty(vos) ? null : vos[0];
    }

    protected T[] retrieveVO(String[] pks) throws BusinessException {
        return (T[]) getPersistenceUtil().retrieveVO(pks);
    }

    protected T retrieveVOByFields(String[] fields,
                                   String pk) throws BusinessException {
        T[] vos = (T[]) getPersistenceUtil().retrieveVOByFields(new String[]{pk }, fields);
        return ArrayUtils.isEmpty(vos) ? null : vos[0];
    }

    protected T[] retrieveSubVOsWithSelf(T vo) throws BusinessException {
        return (T[]) getPersistenceUtil().retrieveSubVOsWithSelf(vo.getPrimaryKey());
    }

    protected T[] convertToVOArray(T obj) throws BusinessException {

        T[] vos = (T[]) Array.newInstance(obj.getClass(), 1);
        vos[0] = obj;
        return vos;
    }

    private BDUniqueFieldTrimUtil uniqueFieldTrimUtil;

    /**
     * 获取唯一性字段trim工具
     *
     * @return
     * @date 2013-1-31 上午10:28:00
     * @since NC6.1
     */
    protected BDUniqueFieldTrimUtil getUniqueFieldTrimUtil() {
        if (this.uniqueFieldTrimUtil == null) {
            this.uniqueFieldTrimUtil = new BDUniqueFieldTrimUtil(this.getMDId());
        }
        return this.uniqueFieldTrimUtil;
    }

}