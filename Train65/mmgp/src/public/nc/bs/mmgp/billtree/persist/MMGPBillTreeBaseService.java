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
 * <b> �����ҵ��ݺ�̨������ </b>
 * <p>
 * ������νṹ���ұ����ӱ���ʽ�Ľڵ��̨������
 * </p>
 *
 * @since: ��������:Sep 15, 2014
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

    /** *********************** ɾ������ ***************************** */
    public void deleteVO(T vo,
                         boolean deleteFromDB) throws BusinessException {
        if (vo == null) return;

        // ����������Ϊ���ڷ�֧�ĸ��������
        deleteLockOperate(vo);

        // �汾У��
        deleteVersionValidate(vo);

        // �߼�У��
        deleteValidateVO(vo);

        // �¼�ǰ֪ͨ
        fireBeforeDeleteEvent(vo);

        // ����֪ͨ
        notifyVersionChangeWhenDataDeleted(vo);

        // DB����: ɾ��ҵ��VO, �����ڲ�����
        dbDeleteVO(vo, deleteFromDB);

        // �¼���֪ͨ
        fireAfterDeleteEvent(vo);

        // ҵ����־
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

    /** *********************** �������� ***************************** */
    public T insertVO(T vo) throws BusinessException {
        if (vo == null) return null;

        // ��Ψһ���ֶν���trim����
        getUniqueFieldTrimUtil().trimUniqueFields((SuperVO) vo.getParent());

        // ����������Ϊ���ڷ�֧�ĸ�������������� Ϊ�Լ���ҵ������
        insertLockOperate(convertToVOArray(vo));

        // �߼�У��
        insertValidateVO(vo);

        // ��������Ϣ
        setInsertAuditInfo(convertToVOArray(vo));

        // �¼�ǰ֪ͨ
        fireBeforeInsertEvent(convertToVOArray(vo));

        // DB��������������VO; �����ڲ�����; ���¼�����VO;
        String pk = dbInsertVO(vo);

        vo = retrieveVO(pk);

        // ����֪ͨ
        notifyVersionChangeWhenDataInserted(convertToVOArray(vo));

        // �¼���֪ͨ
        fireAfterInsertEvent(convertToVOArray(vo));

        // ҵ����־
        writeInsertBusiLog(convertToVOArray(vo));

        // ������VO
        return vo;
    }

    public void insertVOs(T[] vos) throws BusinessException {
        if (vos == null || vos.length == 0) return;

        // ��Ψһ���ֶν���trim����
        SuperVO[] parents = this.getParentVOs(vos);
        getUniqueFieldTrimUtil().trimUniqueFields(parents);

        // ����������Ϊ���ڷ�֧�ĸ�������������� Ϊ�Լ���ҵ������
        insertLockOperate(vos);

        // �߼�У��
        insertValidateVO(vos);

        // ��������Ϣ
        setInsertAuditInfo(vos);

        // �¼�ǰ֪ͨ
        fireBeforeInsertEvent(vos);

        // DB��������������VO; �����ڲ�����;
        String[] pks = dbInsertVOs(vos);

        vos = retrieveVO(pks);

        // ����֪ͨ
        notifyVersionChangeWhenDataInserted(vos);

        // �¼���֪ͨ
        fireAfterInsertEvent(vos);

        // ҵ����־
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
        // �ϼ�ͣ��У��
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

    /** *********************** �޸Ĳ��� ***************************** */
    public T updateVO(T vo,
                      T oldVO) throws BusinessException {
        if (vo == null) return null;

        // ��Ψһ���ֶν���trim����
        getUniqueFieldTrimUtil().trimUniqueFields((SuperVO) vo.getParent());

        // ����������Ϊ���ڷ�֧�ĸ������������������޸ĵ���һ��֧����ҪΪ�·�֧�ĸ����������� Ϊ�Լ���ҵ������(�����ź��棬��Ϊǰ̨������ǲ���VO�����¼�������)
        updateLockOperate(vo);

        // �߼�У��
        updateValidateVO(oldVO, vo);

        // ���������Ϣ
        setUpdateAuditInfo(convertToVOArray(vo));

        // �¼�ǰ֪ͨ
        fireBeforeUpdateEvent(oldVO, vo);

        // DB�����������޸ĵ�VO; �����ڲ�����; ���¼�����VO;
        dbUpdateVO(oldVO, vo);

        vo = retrieveVO(vo.getPrimaryKey());

        // ����֪ͨ
        notifyVersionChangeWhenDataUpdated(convertToVOArray(vo));

        // �¼���֪ͨ
        fireAfterUpdateEvent(oldVO, vo);

        // ҵ����־
        writeUpdatedBusiLog(convertToVOArray(oldVO), convertToVOArray(vo));

        // ������VO
        return vo;
    }

    /**
     * �û���������ֱ���޸�innercode�Ӷ����������¼���innercode���ṩ�ķ��񣬱��汾�ݲ�֧�֡�<br\>
     * ��������������ˣ�������innercode���޸�ֻ�����������ڵ㷢���䶯��<br\>
     * �������������������������¼����ӽڵ��innercodeҲҪ�����޸ġ���updateVO����������������<br\>
     * �������Ҫʵ�֣���ģ��TreeBaseService��
     *
     * @param vo
     * @param updatWithChildren
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("rawtypes")
    public T updateVOWithSub(T vo,
                             ITreeUpdateWithChildren updatWithChildren) throws BusinessException {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0044")/*@res "�ݲ�֧�ִ���ҵ��"*/);
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
        // ���¼�ѭ������У��
        BDTreeUpdateLoopValidator loopValidator = new BDTreeUpdateLoopValidator();
        // �ϼ�ͣ��У��
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

    /** *********************** ���ò��� ***************************** */
    public T enableVO(T vo) throws BusinessException {
        if (vo == null) return null;

        // Ϊ���ڷ�֧�ĸ���������
        enableLockOperate(vo);

        // �汾У��
        enableVersionValidate(vo);

        if (isDataEnabled(vo)) {
            return vo;
        }

        // �߼�У��
        enableValidateVO(vo);

        // ���������Ϣ
        setEnableAuditInfo(vo);

        // �������ñ�־
        setEnableFlag(vo);

        T oldVO = retrieveVOByFields(new String[]{IBaseServiceConst.ENABLESTATE_FIELD }, vo.getPrimaryKey());

        // �¼�ǰ֪ͨ
        fireBeforeEnableEvent(oldVO, vo);

        // DB������������£����¼������������ݣ�
        dbEnableVO(vo);

        vo = retrieveVO(vo.getPrimaryKey());

        // ����֪ͨ
        notifyVersionChangeWhenDataUpdated(convertToVOArray(vo));

        // �¼���֪ͨ
        fireAfterEnableEvent(oldVO, vo);

        // ҵ����־
        writeEnableBusiLog(vo);

        // �������õ�VO
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

    /** *********************** ���ò��� ***************************** */
    public T disableVO(T vo) throws BusinessException {
        if (vo == null) return null;

        // ����
        disableLockOperate(vo);

        // �汾У��
        disableVersionValidate(vo);

        if (isDataDisabled(vo)) {
            return vo;
        }

        // ҵ��У��
        disableValidateVO(vo);

        T[] needDisableVOs = retrieveSubVOsWithSelf(vo);

        // �����Ϣ
        setDisableAuditInfo(needDisableVOs);

        // ����ͣ�ñ�־
        setDisableFlag(needDisableVOs);

        // ��ǰ֪ͨ
        fireBeforeDisableEvent(needDisableVOs);

        // DB������������£����¼��������õ�VO
        dbDisable(needDisableVOs);

        needDisableVOs = retrieveVO(getPrimaryKeyArray(needDisableVOs));

        // ����֪ͨ
        notifyVersionChangeWhenDataUpdated(needDisableVOs);

        // �¼���֪ͨ
        fireAfterDisableEvent(needDisableVOs);

        // ҵ����־
        writeDisableBusiLog(needDisableVOs);

        return needDisableVOs[0];
    }

    /**
     * ��ȡ��ͷ����
     *
     * @param vos
     *        ����
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
     * �����޸����޸�ʱ��
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
     * ���´����˴���ʱ��
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

    /** ************************* ����VO���� ****************************** */
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
     * ��ȡΨһ���ֶ�trim����
     *
     * @return
     * @date 2013-1-31 ����10:28:00
     * @since NC6.1
     */
    protected BDUniqueFieldTrimUtil getUniqueFieldTrimUtil() {
        if (this.uniqueFieldTrimUtil == null) {
            this.uniqueFieldTrimUtil = new BDUniqueFieldTrimUtil(this.getMDId());
        }
        return this.uniqueFieldTrimUtil;
    }

}