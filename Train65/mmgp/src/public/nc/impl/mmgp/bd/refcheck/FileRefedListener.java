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
 * <b> ��Ҫ�������� </b>
 * <p>
 * У�飺��ĳ���������ã�������ĳЩ�ֶβ����޸�
 * </p>
 * 
 * @since �������� Sep 23, 2013
 * @author wangweir
 */
public abstract class FileRefedListener implements IBusinessListener {

    protected IBusinessEvent event;

    private Map<String, List<FileRefedInfo>> fileRefedBaseInfos;

    private RefCheckerFactory refCheckerFactory;

    public FileRefedListener() {
        this.refCheckerFactory = new RefCheckerFactory();
        // ע���¼�����У����Ϣ
        this.register();
    }

    @Override
    public void doAction(IBusinessEvent businessevent) throws BusinessException {
        this.event = businessevent;

        // ���¼�����
        if (this.event instanceof BDCommonEvent) {
            // "�޸ĺ�"����
            this.updateListener();
            // ����"�����°汾"����
            this.marCreateNewVsListener();
            // ����"ȡ�������"����
            this.marCancelDistriListener();
            // "ɾ����"����
            this.delAfterListener();
        }
        // ǰУ�����
        else if (this.event instanceof CheckEvent) {
            // ����"����ǰУ��"����
            this.marBatchUpdateListener();
        }
    }

    /**
     * "ɾ����"����
     * 
     * @throws BusinessException
     */
    protected void delAfterListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_DELETE_AFTER)) {
            this.distrOrg();
        }
    }

    /**
     * ȡ����������
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
     * ��ָ���������ú󲻿��޸�
     * 
     * @param eventtype
     */
    protected void disUpdateAfterRefed(String eventtype) throws BusinessException {
        Object[] newObjects = ((BDCommonEvent) this.event).getNewObjs();
        Object[] oldObjects = null;
        // �����°汾
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
            // ����
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

            // �����༭ʱ���޸ĵ��ֶμ�
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
     * ��������ǰ����
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
     * ����"ȡ�������"����
     * 
     * @throws BusinessException
     */
    protected void marCancelDistriListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_CANCELASSIGN_AFTER)) {
            this.distrOrg();
        }

    }

    /**
     * ����"�����°汾"����
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
     * "�޸ĺ�"����
     * 
     * @throws BusinessException
     */
    protected void updateListener() throws BusinessException {
        if (this.event.getEventType().equals(IEventType.TYPE_UPDATE_AFTER)) {
            this.disUpdateAfterRefed(IEventType.TYPE_UPDATE_AFTER);
        }
    }

    /**
     * ע�������Ϣ
     */
    public abstract void register();

    /**
     * ע��У����Ϣ
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
     * ע��У����
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
     * ע��У����
     * 
     * @param keyBuilder
     *        ����Ψһ�Ա�ʶ���췽��
     * @param refChecker
     *        У����
     */
    public void registerRefChecker(IKeyBuilder keyBuilder,
                                   IRefChecker refChecker) {
        this.refCheckerFactory.registerRefCheck(keyBuilder, refChecker);
    }

    /**
     * ע�������Ϣ
     * 
     * @param keyBuilder
     *        ����Ψһ�Ա�ʶ���췽��
     * @param refedBaseInfo
     *        У����Ϣ
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
