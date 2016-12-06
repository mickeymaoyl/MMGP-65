package nc.impl.mmgp.bd.refcheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.businessevent.CheckEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.framework.common.NCLocator;
import nc.impl.mmgp.bd.refcheck.check.DefaultKeyBuilder;
import nc.impl.mmgp.bd.refcheck.check.IKeyBuilder;
import nc.impl.mmgp.bd.refcheck.check.IRefChecker;
import nc.impl.mmgp.bd.refcheck.check.RefCheckerFactory;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.vo.bd.material.MaterialVO;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.tool.VOTool;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 校验：若某档案被引用，档案中某些字段不可修改
 * </p>
 * 
 * @since 创建日期 Sep 23, 2013
 * @author wangweir
 */
public abstract class FileRefedListener implements IBusinessListener {

    protected IBusinessEvent event;

    private Map<String, List<FileRefedInfo>> fileRefedBaseInfos;

    private RefCheckerFactory refCheckerFactory;

    public FileRefedListener() {
        this.refCheckerFactory = new RefCheckerFactory();
        // 注册事件监听校验信息
        this.register();
    }

    @Override
    public void doAction(IBusinessEvent businessevent) throws BusinessException {
        this.event = businessevent;

        // 后事件监听
        if (this.event instanceof BDCommonEvent) {
            // "修改后"监听
            this.updateListener();
            // 物料"创建新版本"监听
            this.marCreateNewVsListener();
            // 物料"取消分配后"监听
            this.marCancelDistriListener();
            // "删除后"监听
            this.delAfterListener();
        }
        // 前校验监听
        else if (this.event instanceof CheckEvent) {
            // 物料"批改前校验"监听
            this.marBatchUpdateListener();
        }
    }

    /**
     * "删除后"监听
     * 
     * @throws BusinessException
     */
    protected void delAfterListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_DELETE_AFTER)) {
            this.distrOrg();
        }
    }

    /**
     * 取消分配后监听
     * 
     * @throws BusinessException
     */
    protected void distrOrg() throws BusinessException {
        Object[] newObjects = ((BDCommonEvent) this.event).getNewObjs();

        if (MMArrayUtil.isEmpty(newObjects)) {
            return;
        }

        for (int i = 0; i < newObjects.length; i++) {
            if (!(newObjects[i] instanceof SuperVO)) {
                continue;
            }

            SuperVO newVO = (SuperVO) newObjects[i];
            Object orgObj = newVO.getAttributeValue(MMGlobalConst.PK_ORG);
            if (MMStringUtil.isEmptyWithTrim((String) orgObj)) {
                continue;
            }

            IKeyBuilder keyBuilder = new DefaultKeyBuilder(IEventType.TYPE_DELETE_AFTER, newVO.getClass().getName());

            String key = keyBuilder.buildKey();
            List<FileRefedInfo> refedBaseInfos = this.getOneFileRefedBaseInfos(key);

            if (MMCollectionUtil.isEmpty(refedBaseInfos)) {
                continue;
            }

            IRefChecker checker =
                    this.refCheckerFactory.getRefCheck(keyBuilder, IEventType.TYPE_DELETE_AFTER, newVO
                        .getClass()
                        .getName());
            checker.check(event, refedBaseInfos, null, newVO);
        }

    }

    /**
     * 被指定单据引用后不可修改
     * 
     * @param eventtype
     */
    protected void disUpdateAfterRefed(String eventtype) throws BusinessException {
        Object[] newObjects = ((BDCommonEvent) this.event).getNewObjs();
        Object[] oldObjects = null;
        // 创建新版本
        if (eventtype.equals(IEventType.TYPE_INSERT_AFTER)) {
            MaterialVO newObject = (MaterialVO) newObjects[0];
            String pk_source = newObject.getPk_source();
            int version = newObject.getVersion().intValue();
            if (pk_source != null && version > 1) {
                String condition =
                        MaterialVO.PK_SOURCE
                            + " = '"
                            + pk_source
                            + "' and "
                            + MaterialVO.VERSION
                            + " = "
                            + (version - 1);
                String[] materilids = this.getMarterialService().queryMaterialPksByCondition(null, condition);
                oldObjects = this.getMarterialService().queryDataByPks(materilids);
            }
        } else {
            // 其他
            oldObjects = ((BDCommonEvent) this.event).getOldObjs();
        }

        if (MMArrayUtil.isEmpty(newObjects) || MMArrayUtil.isEmpty(oldObjects)) {
            return;
        }

        for (int i = 0; i < oldObjects.length; i++) {
            if (!(oldObjects[i] instanceof SuperVO) || !(newObjects[i] instanceof SuperVO)) {
                continue;
            }

            SuperVO oldVO = (SuperVO) oldObjects[i];
            SuperVO newVO = (SuperVO) newObjects[i];

            // 档案编辑时已修改的字段集
            VOTool votool = new VOTool();
            Set<String> editedSet = votool.getDifferentField(oldVO, newVO);

            if (MMCollectionUtil.isEmpty(editedSet)) {
                continue;
            }

            IKeyBuilder builder = new DefaultKeyBuilder(eventtype, newVO.getClass().getName());
            List<FileRefedInfo> refedBaseInfos = this.getOneFileRefedBaseInfos(builder.buildKey());

            if (MMCollectionUtil.isEmpty(refedBaseInfos)) {
                continue;
            }

            IRefChecker checker = this.refCheckerFactory.getRefCheck(builder, eventtype, newVO.getClass().getName());
            checker.check(event, refedBaseInfos, oldVO, newVO);
        }
    }

    protected IMaterialBaseInfoQueryService getMarterialService() {
        return NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
    }

    /**
     * 物料批改前监听
     * 
     * @param chkutil
     * @throws BusinessException
     */
    protected void marBatchUpdateListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_BATCHUPDATE_CHECK)) {
            CheckEvent checkevent = (CheckEvent) this.event;
            Object[] vos = checkevent.getInputVOs();
            if (MMArrayUtil.isEmpty(vos)) {
                return;
            }

            IKeyBuilder keyBuilder =
                    new DefaultKeyBuilder(IEventType.TYPE_BATCHUPDATE_CHECK, vos[0].getClass().getName());
            List<FileRefedInfo> refedBaseInfos = this.getOneFileRefedBaseInfos(keyBuilder.buildKey());

            if (MMCollectionUtil.isEmpty(refedBaseInfos)) {
                return;
            }

            IRefChecker checker =
                    this.refCheckerFactory.getRefCheck(keyBuilder, IEventType.TYPE_BATCHUPDATE_CHECK, vos[0]
                        .getClass()
                        .getName());
            checker.check(event, refedBaseInfos, null, null);
        }
    }

    /**
     * 物料"取消分配后"监听
     * 
     * @throws BusinessException
     */
    protected void marCancelDistriListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_CANCELASSIGN_AFTER)) {
            this.distrOrg();
        }

    }

    /**
     * 物料"创建新版本"监听
     * 
     * @throws BusinessException
     */
    protected void marCreateNewVsListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_INSERT_AFTER)) {
            Object[] newObjects = ((BDCommonEvent) this.event).getNewObjs();
            if (!MMArrayUtil.isEmpty(newObjects)) {
                SuperVO newObject = (SuperVO) newObjects[0];
                if (newObject instanceof MaterialVO
                    && ((MaterialVO) newObject).getVersion() != null
                    && ((MaterialVO) newObject).getVersion().intValue() > 1) {
                    if (this.event.getEventType().equals(IEventType.TYPE_INSERT_AFTER)) {
                        this.disUpdateAfterRefed(IEventType.TYPE_INSERT_AFTER);
                    }
                }
            }
        }
    }

    /**
     * "修改后"监听
     * 
     * @throws BusinessException
     */
    protected void updateListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_UPDATE_AFTER)) {
            this.disUpdateAfterRefed(IEventType.TYPE_UPDATE_AFTER);
        }
    }

    /**
     * 注册监听信息
     */
    public abstract void register();

    /**
     * 注册校验信息
     * 
     * @param eventType
     * @param clazzName
     * @param refedBaseInfo
     */
    public void registerRefedBaseInfo(String eventType,
                                      String clazzName,
                                      FileRefedInfo[] refedBaseInfo) {
        IKeyBuilder keyBuilder = new DefaultKeyBuilder(eventType, clazzName);
        this.registerRefedBaseInfo(keyBuilder, refedBaseInfo);
    }

    /**
     * 注册校验类
     * 
     * @param eventType
     * @param clazzName
     * @param refChecker
     */
    public void registerRefChecker(String eventType,
                                   String clazzName,
                                   IRefChecker refChecker) {
        IKeyBuilder keyBuilder = new DefaultKeyBuilder(eventType, clazzName);
        this.refCheckerFactory.registerRefCheck(keyBuilder, refChecker);
    }

    /**
     * 注册校验类
     * 
     * @param keyBuilder
     *        检验唯一性标识构造方法
     * @param refChecker
     *        校验类
     */
    public void registerRefChecker(IKeyBuilder keyBuilder,
                                   IRefChecker refChecker) {
        this.refCheckerFactory.registerRefCheck(keyBuilder, refChecker);
    }

    /**
     * 注册检验信息
     * 
     * @param keyBuilder
     *        检验唯一性标识构造方法
     * @param refedBaseInfo
     *        校验信息
     */
    public void registerRefedBaseInfo(IKeyBuilder keyBuilder,
                                      FileRefedInfo[] refedBaseInfo) {
        if (MMArrayUtil.isEmpty(refedBaseInfo)) {
            return;
        }

        List<FileRefedInfo> oneFileRefedBaseInfos = this.getOneFileRefedBaseInfos(keyBuilder.buildKey());
        if (oneFileRefedBaseInfos == null) {
            oneFileRefedBaseInfos = new ArrayList<FileRefedInfo>();
            this.getFileRefedBaseInfos().put(keyBuilder.buildKey(), oneFileRefedBaseInfos);
        }
        oneFileRefedBaseInfos.addAll(Arrays.asList(refedBaseInfo));
    }

    public List<FileRefedInfo> getOneFileRefedBaseInfos(String key) {
        return this.getFileRefedBaseInfos().get(key);
    }

    /**
     * @return the fileRefedBaseInfos
     */
    public Map<String, List<FileRefedInfo>> getFileRefedBaseInfos() {
        if (this.fileRefedBaseInfos == null) {
            this.fileRefedBaseInfos = new HashMap<String, List<FileRefedInfo>>();
        }
        return fileRefedBaseInfos;
    }

}
