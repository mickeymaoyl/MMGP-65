package nc.impl.mmgp.uif2;

import nc.bs.mmgp.common.CommonUtils;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFrameworkUtil;
import nc.bs.uif2.validation.Validator;
import nc.impl.mmgp.smart.MMGPBatchSaveAction;
import nc.impl.mmgp.uif2.rule.BDUniqueCheckRule;
import nc.impl.mmgp.uif2.rule.event.DeleteAfterEventRule;
import nc.impl.mmgp.uif2.rule.event.DeleteBeforeEventRule;
import nc.impl.mmgp.uif2.rule.event.InsertAfterEventRule;
import nc.impl.mmgp.uif2.rule.event.InsertBeforeEventRule;
import nc.impl.mmgp.uif2.rule.event.UpdateAfterEventRule;
import nc.impl.mmgp.uif2.rule.event.UpdateBeforeEventRule;
import nc.impl.mmgp.uif2.rule.event.cache.CacheEventRule;
import nc.impl.mmgp.uif2.rule.event.cache.FireDeleteCacheEvent;
import nc.impl.mmgp.uif2.rule.event.cache.FireInsertCacheEvent;
import nc.impl.mmgp.uif2.rule.event.cache.FireUpdateCacheEvent;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pub.smart.SmartServiceImpl;
import nc.itf.mmgp.uif2.IMMGPSmartService;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bd.pub.DistributedAddBaseValidator;
import nc.vo.bd.pub.SingleDistributedDeleteValidator;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.AuditInfoUtil;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.BDReferenceChecker;
import nc.vo.util.BDVersionValidationUtil;
import nc.vo.util.bizlock.BizlockDataUtil;

/**
 * <b>MMGP smart服务，多用于批量操作</b>
 * <p>
 * 对原pubapp功能进行增强，如保存时处理审计信息等.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @since NC V6.3
 * @author wangweiu
 */
public class MMGPSmartService extends SmartServiceImpl implements IMMGPSmartService {

    /**
     * {@inheritDoc}
     * <p>
     * 2013-06-25 wangwei:父类异常修改为BusinessException，导致这里报错，只能也改了（使用06-20的nchome）
     */
    @Override
    public BatchOperateVO batchSave(BatchOperateVO batchVO) throws BusinessException {
        try {
            if (isNotEmpty(batchVO.getDelObjs())) {
                SuperVO[] vos = convertToSuperVO(batchVO.getDelObjs());
                lockData(vos);
                checkTs(vos);
                deleteValidate(vos);
            }
            if (isNotEmpty(batchVO.getUpdObjs())) {
                SuperVO[] vos = convertToSuperVO(batchVO.getUpdObjs());
                lockData(vos);
                checkTs(vos);
                saveValidate(vos);
                // 2013-5-8 gaotx 处理审计信息
                setDefaultData(vos, false);
            }
            if (isNotEmpty(batchVO.getAddObjs())) {
                SuperVO[] vos = convertToSuperVO(batchVO.getAddObjs());
                BizlockDataUtil.lockDataByBizlock(vos);
                for (SuperVO vo : vos) {
                    vo.setAttributeValue("pk_group", AppContext.getInstance().getPkGroup());
                    CommonUtils.setPk_org_v(vo);
                }
                saveValidate(vos);
                // 2013-5-8 gaotx 处理审计信息
                setDefaultData(vos, true);
            }

            MMGPBatchSaveAction<ISuperVO> batchSaveAction = new MMGPBatchSaveAction<ISuperVO>(this.getPluginPoint());
            addInsertRule(batchSaveAction, batchVO);

            addDeleteRule(batchSaveAction, batchVO);

            addUpdateRule(batchSaveAction, batchVO);

            return batchSaveAction.batchSave(batchVO);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 支持提供扩展点,默认返回空
     * 
     * @return
     */
    protected IPluginPoint getPluginPoint() {
        return null;
    }

    /**
     * @param batchSaveAction
     * @param batchVO
     */
    @SuppressWarnings("unchecked")
    protected void addUpdateRule(MMGPBatchSaveAction<ISuperVO> batchSaveAction,
                                 BatchOperateVO batchVO) {
        batchSaveAction.getUpdateProcesser().addBeforeRule(new BDUniqueCheckRule());
        batchSaveAction.getUpdateProcesser().addBeforeRule(new UpdateBeforeEventRule());
        batchSaveAction.getUpdateProcesser().addAfterRule(new UpdateAfterEventRule());

        batchSaveAction.getUpdateProcesser().addAfterRule(new CacheEventRule(new FireUpdateCacheEvent()));
    }

    /**
     * @param batchSaveAction
     * @param batchVO
     */
    @SuppressWarnings("unchecked")
    protected void addDeleteRule(MMGPBatchSaveAction<ISuperVO> batchSaveAction,
                                 BatchOperateVO batchVO) {
        batchSaveAction.getDeleteProcesser().addBeforeRule(new DeleteBeforeEventRule());
        batchSaveAction.getDeleteProcesser().addAfterRule(new DeleteAfterEventRule());

        batchSaveAction.getDeleteProcesser().addAfterRule(new CacheEventRule(new FireDeleteCacheEvent()));
    }

    /**
     * @param batchSaveAction
     * @param batchVO
     */
    @SuppressWarnings("unchecked")
    protected void addInsertRule(MMGPBatchSaveAction<ISuperVO> batchSaveAction,
                                 BatchOperateVO batchVO) {
        batchSaveAction.getInsertProcesser().addBeforeRule(new BDUniqueCheckRule());
        batchSaveAction.getInsertProcesser().addBeforeRule(new InsertBeforeEventRule());
        batchSaveAction.getInsertProcesser().addAfterRule(new InsertAfterEventRule());

        batchSaveAction.getInsertProcesser().addAfterRule(new CacheEventRule(new FireInsertCacheEvent()));
    }

    /**
     * 设置默认值，审计信息等.
     * 
     * @param datas
     *        the datas
     * @param isNew
     *        是否新增
     * @throws BusinessException
     *         the business exception
     * @author gaotx
     */
    protected void setDefaultData(Object[] datas,
                                  boolean isNew) throws BusinessException {
        if (isNew) {
            AuditInfoUtil.addData(datas);
        } else {
            AuditInfoUtil.updateData(datas);
        }
    }

    /**
     * Lock data.
     * 
     * @param data
     *        the data
     * @throws BusinessException
     *         the business exception
     */
    protected void lockData(Object[] data) throws BusinessException {
        BDPKLockUtil.lockObject(data);
        BizlockDataUtil.lockDataByBizlock(data);
    }

    /**
     * Checks if is not empty.
     * 
     * @param objs
     *        the objs
     * @return true, if is not empty
     */
    private boolean isNotEmpty(Object[] objs) {
        return objs != null && objs.length > 0;
    }

    /**
     * Save validate.
     * 
     * @param data
     *        the data
     * @throws ValidationException
     *         the validation exception
     */
    protected void saveValidate(Object[] data) throws ValidationException {
        Validator[] validators = new Validator[]{new DistributedAddBaseValidator() };
        IValidationService validationService = ValidationFrameworkUtil.createValidationService(validators);

        validationService.validate(data);
    }

    /**
     * Delete validate.
     * 
     * @param data
     *        the data
     * @throws ValidationException
     *         the validation exception
     */
    protected void deleteValidate(Object[] data) throws ValidationException {
        Validator[] validators =
                new Validator[]{BDReferenceChecker.getInstance(), new SingleDistributedDeleteValidator() };
        IValidationService validationService = ValidationFrameworkUtil.createValidationService(validators);
        validationService.validate(data);
    }

    /**
     * Convert to super vo.
     * 
     * @param data
     *        the data
     * @return the super v o[]
     */
    protected SuperVO[] convertToSuperVO(Object[] data) {
        SuperVO[] vos = new SuperVO[data.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i] instanceof AggregatedValueObject) {
                AggregatedValueObject aggVO = (AggregatedValueObject) data[i];
                vos[i] = (SuperVO) aggVO.getParentVO();
            } else {
                vos[i] = (SuperVO) data[i];
            }
        }
        return vos;
    }

    /**
     * Check ts.
     * 
     * @param data
     *        the data
     * @throws BusinessException
     *         the business exception
     */
    protected void checkTs(Object[] data) throws BusinessException {
        BDVersionValidationUtil.validateObject(data);
    }

}
