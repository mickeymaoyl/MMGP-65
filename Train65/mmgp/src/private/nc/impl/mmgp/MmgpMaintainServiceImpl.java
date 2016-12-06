/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.impl.mmgp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mmgp.check.MMBDUniqueCheck;
import nc.bs.mmgp.check.MMGPCheckTsUtil;
import nc.bs.mmgp.common.CommonUtils;
import nc.bs.mmgp.dao.MMGPCmnDAO;
import nc.bs.mmgp.lock.MMGPPKLock;
import nc.bs.pub.pflock.IPfBusinessLock;
import nc.bs.pub.pflock.PfBusinessLock;
import nc.bs.trade.comdelete.BillDelete;
import nc.bs.trade.comsave.BillSave;
import nc.bs.trade.lock.BDConsistenceCheck;
import nc.bs.trade.lock.BDLockData;
import nc.bs.uap.lock.PKLock;
import nc.itf.mmgp.IMmgpMaintainService;
import nc.itf.mmgp.bean.IMMBDMulUniqueCheckVO;
import nc.itf.mmgp.bean.IMMBDReferenceCheckVO;
import nc.itf.mmgp.bean.IMMBDReferenceNotPrimaryKey;
import nc.itf.mmgp.bean.IMMBDUniqueCheckVO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.pub.HYBillVO;

/**
 * <b> MMGP档案默认服务</b>
 * <p>
 * 实现对档案的保存，删除等基本操作
 * </p>
 * 创建日期:2011-1-21.
 *
 * @author wangweiu
 * @deprecated 由 nc.impl.mmgp.uif2.MMGPCmnOperateService 替换
 * @see nc.impl.mmgp.uif2.MMGPCmnOperateService
 */
public class MmgpMaintainServiceImpl implements IMmgpMaintainService {

    /**
     * Checks if is insert.
     *
     * @param vo
     *        the vo
     * @param isCheckByPK
     *        the is check by pk
     * @return true, if is insert
     */
    private boolean isInsert(SuperVO vo,
                             boolean isCheckByPK) {
        if (isCheckByPK) {
            // 通过主键
            return vo.getPrimaryKey() == null || vo.getPrimaryKey().length() == 0;
        } else {
            // 通过表头
            return vo.getStatus() == VOStatus.NEW;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends SuperVO> T[] saveMmgpVOs(T[] vos,
                                               boolean isCheckByPK) throws BusinessException {
        try {
            if (vos == null || vos.length == 0) {
                return null;
            }
            Class<T> clazz = (Class<T>) vos[0].getClass();
            MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
            List<String> updatepks = new ArrayList<String>();
            List<SuperVO> insertVOs = new ArrayList<SuperVO>();
            List<SuperVO> updateVOs = new ArrayList<SuperVO>();
            List<String> allPks = new ArrayList<String>();
            for (int i = 0; i < vos.length; i++) {
                if (isInsert(vos[i], isCheckByPK)) {
                    insertVOs.add(vos[i]);
                } else {
                    updatepks.add(vos[i].getPrimaryKey());
                    updateVOs.add(vos[i]);
                }
            }
            allPks.addAll(updatepks);
            if (!insertVOs.isEmpty()) {
                String[] insertPk = null;
                if (isCheckByPK) {
                    insertPk = cmnDAO.insertVOList(insertVOs);
                } else {
                    insertPk = cmnDAO.insertVOListWithPK(insertVOs);
                }
                allPks.addAll(Arrays.asList(insertPk));
            }

            if (!updateVOs.isEmpty()) {
                PKLock.getInstance().addBatchDynamicLock(updatepks.toArray(new String[0]));
                MMGPCheckTsUtil.checkTimeStamp(clazz, vos);
                cmnDAO.updateVOs(updateVOs);
            }
            List<T> result = cmnDAO.retrieveByPKs(clazz, allPks.toArray(new String[0]));
            return CommonUtils.createArray(clazz, result);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return null;
        }
    }

    /**
     * 简要说明.
     *
     * @param <T>
     *        the generic type
     * @param vos
     *        the vos
     * @return the t[]
     * @throws BusinessException
     *         the business exception
     * @see nc.itf.mmgp.IMmgpMaintainService#saveMmgpVOs(nc.vo.pub.SuperVO[])
     */
    public <T extends SuperVO> T[] saveMmgpVOs(T[] vos) throws BusinessException {
        try {
            return saveMmgpVOs(vos, true);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
            return null;
        }
    }

    /**
     * 如果isKeepHeadPK 为ture，则用vo自身的主键覆盖掉pks相对应的值.
     *
     * @param pks
     *        新生成的一组主键值
     * @param addHYBillVOList
     *        vo状态为new的聚合vo列表
     * @param isKeepHeadPK
     *        是否需要保留主键的pk值
     * @return the string[]
     * @throws BusinessException
     *         the business exception
     */
    private String[] processPk(String[] pks,
                               List<AggregatedValueObject> addHYBillVOList,
                               boolean isKeepHeadPK) throws BusinessException {
        if (!isKeepHeadPK) {
            // 不保留主键值
            return pks;
        }
        List<String> newPkList = new ArrayList<String>();

        // 保留主键值
        for (int i = 0; i < pks.length; i++) {
            String pk = addHYBillVOList.get(i).getParentVO().getPrimaryKey();
            if (MMStringUtil.isEmpty(pk)) {
                newPkList.add(pks[i]);
            } else {
                newPkList.add(pk);
            }
        }
        return newPkList.toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     */
    public void mmsaveBDLists(AggregatedValueObject billVo,
                              List< ? extends AggregatedValueObject> billVoList,
                              Object userObj) throws BusinessException {
        try {
            IPfBusinessLock bdLock = new PfBusinessLock();
            bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));
            // zjy 2011-03-12 关联性校验
            referenceCheckOnSave(billVo);
            // 检查唯一性
            singleUniqueCheck(billVo);
            multiUniqueCheck(billVo);

            saveAggregatedValueObjectList(billVoList, false, false);

        } catch (Exception be) {
            ExceptionUtils.marsh(be);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void saveAggregatedValueObjectList(List< ? extends AggregatedValueObject> billVoList,
                                              boolean lock,
                                              boolean isHeadVOStatusCheck) throws BusinessException {
        try {
            if (billVoList == null || billVoList.size() == 0) {
                return;
            }

            MMGPCmnDAO dao = new MMGPCmnDAO();

            List<SuperVO> updateParentList = new ArrayList<SuperVO>();
            List<SuperVO> updateChildList = new ArrayList<SuperVO>();

            List<SuperVO> addParentList = new ArrayList<SuperVO>();
            List<SuperVO> addChildList = new ArrayList<SuperVO>();

            Set<String> lockPKSet = new HashSet<String>();
            // 子表为新增，主表已经存在
            List<SuperVO> addOtherChildList = new ArrayList<SuperVO>();

            List<AggregatedValueObject> addHYBillVOList = new ArrayList<AggregatedValueObject>();

            for (AggregatedValueObject obj : billVoList) {
                // 如果根据vo状态来判断是否新增或修改或删除，则是否根据pk值来设置创建或修改信息为false
                setAuditData(obj, !isHeadVOStatusCheck);
                SuperVO parentVO = (SuperVO) obj.getParentVO();
                SuperVO[] childrenVOS = (SuperVO[]) obj.getChildrenVO();
                List<SuperVO> childrenList = new ArrayList<SuperVO>(Arrays.asList(childrenVOS));
                if (parentVO.getStatus() == VOStatus.DELETED) {
                    // wrr update 由数据库直接删除，修改为置dr 为 1
                    parentVO.setAttributeValue("dr", 1);
                    updateParentList.add(parentVO);

                    for (SuperVO childVO : childrenVOS) {
                        childVO.setAttributeValue("dr", 1);
                        updateChildList.add(parentVO);
                    }

                    lockPKSet.add(parentVO.getPrimaryKey());
                }

                switch (parentVO.getStatus()) {

                    case nc.vo.pub.VOStatus.NEW: {
                        addHYBillVOList.add(obj);
                        break;
                    }
                    case nc.vo.pub.VOStatus.UPDATED: {

                        for (SuperVO childVO : childrenList) {
                            if (MMStringUtil.isEmpty(childVO.getPrimaryKey())) {
                                childVO.setAttributeValue(childVO.getParentPKFieldName(), parentVO.getPrimaryKey());
                                addOtherChildList.add(childVO);
                            } else {
                                if (childVO.getStatus() == VOStatus.DELETED) {
                                    childVO.setAttributeValue("dr", 1);
                                }
                                updateChildList.add(childVO);
                            }
                        }

                        // 只需要表头加锁
                        lockPKSet.add(parentVO.getPrimaryKey());
                        updateParentList.add(parentVO);

                        break;
                    }
                    default:
                }

            }

            if (lockPKSet != null && !lockPKSet.isEmpty() && lock) {
                MMGPPKLock.addBatchDynamicLock(lockPKSet.toArray(new String[0]));
            }

            String[] pks = new SequenceGenerator().generate(addHYBillVOList.size());

            pks = processPk(pks, addHYBillVOList, isHeadVOStatusCheck);

            for (int i = 0; i < addHYBillVOList.size(); i++) {
                AggregatedValueObject addVO = addHYBillVOList.get(i);
                SuperVO parentVO = (SuperVO) addVO.getParentVO();
                parentVO.setPrimaryKey(pks[i]);

                addParentList.add(parentVO);

                SuperVO[] childrenVOS = (SuperVO[]) addVO.getChildrenVO();
                List<SuperVO> childVOList = new ArrayList<SuperVO>(Arrays.asList(childrenVOS));
                for (SuperVO childVO : childVOList) {
                    childVO.setAttributeValue(childVO.getParentPKFieldName(), pks[i]);
                    addChildList.add(childVO);
                }

            }

            dao.insertVOListWithPK(addParentList);
            dao.insertVOList(addChildList);

            dao.insertVOList(addOtherChildList);

            dao.updateVOs(updateParentList);
            dao.updateVOs(updateChildList);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public AggregatedValueObject mmsaveBD(AggregatedValueObject billVo,
                                          Object userObj) throws UifException {

        IPfBusinessLock bdLock = new PfBusinessLock();
        try {
            bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));

            // zjy 2011-03-12 关联性校验
            referenceCheckOnSave(billVo);

            // 检查唯一性
            singleUniqueCheck(billVo);

            multiUniqueCheck(billVo);

            BillSave billsave = new BillSave();
            setAuditData(billVo, true);

            return billsave.saveBD(billVo, userObj);
        } catch (BusinessException be) {
            Logger.error(be.getMessage(), be);
            throw new UifException(be.getMessage());
        } catch (Exception ex) {
            Logger.error(ex.getMessage(), ex);
            throw new UifException(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T extends SuperVO> AggregatedValueObject mmsaveSingleBD(AggregatedValueObject billVo,
                                                                    Object userObj,
                                                                    AggregatedValueObject checkVo,
                                                                    T t) throws UifException {

        IPfBusinessLock bdLock = new PfBusinessLock();
        try {
            bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));

            // zjy 2011-03-12 关联性校验
            referenceCheckOnSave(billVo);
            // 检查唯一性
            singleUniqueCheck(billVo);
            multiUniqueCheck(billVo);
            MMGPCmnDAO dao = new MMGPCmnDAO();

            List<SuperVO> insertList = new ArrayList<SuperVO>();
            List<SuperVO> updateList = new ArrayList<SuperVO>();
            List<SuperVO> delList = new ArrayList<SuperVO>();

            SuperVO[] billVos = (SuperVO[]) billVo.getChildrenVO();

            for (SuperVO vo : billVos) {
                if (VOStatus.DELETED == vo.getStatus()) delList.add(vo);
            }
            SuperVO[] checkVos = (SuperVO[]) checkVo.getChildrenVO();
            List<SuperVO> viewVosSuperVOs = new ArrayList<SuperVO>();

            for (SuperVO vo : checkVos) {
                switch (vo.getStatus()) {
                    case nc.vo.pub.VOStatus.NEW:
                        insertList.add(vo);
                        break;

                    case nc.vo.pub.VOStatus.UPDATED:
                        updateList.add(vo);
                        break;

                    default:
                        viewVosSuperVOs.add(vo);

                }
            }

            setListAuditData(insertList);
            setListAuditData(updateList);
            setListAuditData(delList);
            dao.insertVOList(insertList);
            dao.deleteVOList(delList);
            dao.updateVOs(updateList);
            viewVosSuperVOs.addAll(insertList);
            viewVosSuperVOs.addAll(updateList);
            checkVo.setChildrenVO(viewVosSuperVOs.toArray(new SuperVO[0]));
            return checkVo;
        } catch (BusinessException be) {
            Logger.error(be.getMessage(), be);
            throw new UifException(be.getMessage());
        } catch (Exception ex) {
            Logger.error(ex.getMessage(), ex);
            throw new UifException(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public AggregatedValueObject mmdeleteBD(AggregatedValueObject billVo,
                                            Object userObj) throws UifException {
        IPfBusinessLock bdLock = new PfBusinessLock();
        try {
            bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));
            // 关联性校验
            referenceCheckOnDelete(billVo);

            BillDelete billdelete = new BillDelete();
            return billdelete.deleteBD(billVo, userObj);
        } catch (BusinessException be) {
            Logger.error(be.getMessage(), be);
            throw new UifException(be.getMessage());
        } catch (Exception ex) {
            Logger.error(ex.getMessage(), ex);
            throw new UifException(ex.getMessage(), ex);
        }
    }

    /**
     * Reference check on delete.
     *
     * @param billVo
     *        the bill vo
     * @throws BusinessException
     *         the business exception
     */
    private void referenceCheckOnDelete(AggregatedValueObject billVo) throws BusinessException {
        String tableName = null;
        List<String> keyList = new ArrayList<String>();
        if (billVo.getParentVO() == null) {
            // 插入子表
            CircularlyAccessibleValueObject[] vos = billVo.getChildrenVO();
            if (isArrayEmpty(vos)) {
                return;
            }

            if (!IMMBDReferenceCheckVO.class.isAssignableFrom(vos[0].getClass())) {
                return;
            }

            IMMBDReferenceCheckVO[] bdvos = (IMMBDReferenceCheckVO[]) vos;
            tableName = bdvos[0].getTableName();

            for (int i = 0; i < bdvos.length; i++) {
                CircularlyAccessibleValueObject vo = vos[i];
                // codesync_xg_v01 非主键被删除检查引用 modify begin
                keyList.add(getReferenceKey(vo));
                // codesync_xg_v01 非主键被删除检查引用 modify end
            }

        } else {
            CircularlyAccessibleValueObject vo = billVo.getParentVO();

            if (!IMMBDReferenceCheckVO.class.isAssignableFrom(vo.getClass())) {
                return;
            }
            IMMBDReferenceCheckVO uniqueCheckVO = (IMMBDReferenceCheckVO) vo;
            tableName = uniqueCheckVO.getTableName();
            // codesync_xg_v01 非主键被删除检查引用 modify begin
            keyList.add(getReferenceKey(vo));
            // codesync_xg_v01 非主键被删除检查引用 modify end

        }

        if (keyList.isEmpty()) {
            return;
        }

        checkReference(tableName, keyList);

    }

    /**
     * 保存时检查子表删除项是否被引用.
     *
     * @param billVo
     *        被检查的单据
     * @throws BusinessException
     *         BusinessException
     * @author zjy
     */
    private void referenceCheckOnSave(AggregatedValueObject billVo) throws BusinessException {
        CircularlyAccessibleValueObject[] vos = billVo.getChildrenVO();
        if (isArrayEmpty(vos)) {
            return;
        }

        if (!IMMBDReferenceCheckVO.class.isAssignableFrom(vos[0].getClass())) {
            return;
        }

        IMMBDReferenceCheckVO[] bdvos = (IMMBDReferenceCheckVO[]) vos;
        String tableName = bdvos[0].getTableName();
        List<String> keyList = new ArrayList<String>();

        for (int i = 0; i < bdvos.length; i++) {
            if (vos[i].getStatus() == VOStatus.DELETED) {
                // codesync_xg_v01 非主键被删除检查引用 modify begin
                keyList.add(getReferenceKey(vos[i]));
                // codesync_xg_v01 非主键被删除检查引用 modify end
            }
        }

        checkReference(tableName, keyList);
    }

    // codesync_xg_v01 非主键被删除检查引用 add begin
    /**
     * Gets the reference key.
     *
     * @param vo
     *        the vo
     * @return the reference key
     */
    private String getReferenceKey(CircularlyAccessibleValueObject vo) {

        String field = null;
        if (IMMBDReferenceNotPrimaryKey.class.isAssignableFrom(vo.getClass())) {
            field = ((IMMBDReferenceNotPrimaryKey) vo).getReferenceField();
        } else {
            field = ((IMMBDReferenceCheckVO) vo).getPKFieldName();
        }
        return MMStringUtil.objectToString(vo.getAttributeValue(field));
    }

    // codesync_xg_v01 非主键被删除检查引用 add end

    /**
     * Check reference.
     *
     * @param tableName
     *        the table name
     * @param keyList
     *        the key list
     * @throws BusinessException
     *         the business exception
     */
    private void checkReference(String tableName,
                                List<String> keyList) throws BusinessException {
        // String[] keys = keyList.toArray(new String[0]);
        // IReferenceCheck check = NCLocator.getInstance().lookup(
        // IReferenceCheck.class);
        // String[] refKeys = check.getReferencedKeys(tableName, keys);
        // if (refKeys != null && refKeys.length > 0) {
        // throw new BusinessException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE());
        // }
    }

    /**
     * 检查唯一性.
     *
     * @param billVo
     *        被检查的单据
     * @throws BusinessException
     *         BusinessException
     */
    private void singleUniqueCheck(AggregatedValueObject billVo) throws BusinessException {

        String tableName = null;
        Map<String, IMMBDUniqueCheckVO> keyMap = new HashMap<String, IMMBDUniqueCheckVO>();
        IMMBDUniqueCheckVO uniqueCheckVO = null;

        if (billVo.getParentVO() == null) {
            // 插入子表
            CircularlyAccessibleValueObject[] vos = billVo.getChildrenVO();
            if (isArrayEmpty(vos)) {
                return;
            }

            if (!IMMBDUniqueCheckVO.class.isAssignableFrom(vos[0].getClass())) {
                return;
            }

            IMMBDUniqueCheckVO[] bdvos = (IMMBDUniqueCheckVO[]) vos;
            uniqueCheckVO = bdvos[0];
            tableName = bdvos[0].getTableName();

            for (int i = 0; i < bdvos.length; i++) {
                CircularlyAccessibleValueObject vo = vos[i];
                IMMBDUniqueCheckVO bdvo = bdvos[i];
                if (vo.getStatus() != nc.vo.pub.VOStatus.DELETED) {
                    // codesync_uf_v01
                    // huangtao,原来写的代码中如果检查的字段类型不是String的话会报类型转换异常
                    Object value = vo.getAttributeValue(uniqueCheckVO.getUniqueCheckField());
                    if (value != null) {
                        keyMap.put(value.toString(), bdvo);
                    }
                    // String attributeValue = (String)
                    // vo.getAttributeValue(bdvo
                    // .getUniqueCheckField());
                    // if (attributeValue != null) {
                    // keyMap.put(attributeValue, bdvo);
                    // }
                    // end by huangtao
                }
            }

        } else {
            CircularlyAccessibleValueObject vo = billVo.getParentVO();

            if (!IMMBDUniqueCheckVO.class.isAssignableFrom(vo.getClass())) {
                return;
            }
            uniqueCheckVO = (IMMBDUniqueCheckVO) vo;
            tableName = uniqueCheckVO.getTableName();
            // codesync_uf_v01 huangtao,原来写的代码中如果检查的字段类型不是String的话会报类型转换异常
            Object value = vo.getAttributeValue(uniqueCheckVO.getUniqueCheckField());
            if (value != null) {
                keyMap.put(value.toString(), uniqueCheckVO);
            }
            // String attributeValue = (String)
            // vo.getAttributeValue(uniqueCheckVO
            // .getUniqueCheckField());
            // if (attributeValue != null) {
            // keyMap.put(attributeValue, uniqueCheckVO);
            // }
            // keyMap.put(attributeValue, uniqueCheckVO);
            // end by huangtao,

        }

        if (keyMap.isEmpty()) {
            return;
        }

        checkWithDataBase(tableName, keyMap, uniqueCheckVO);
    }

    /**
     * Checks if is array empty.
     *
     * @param vos
     *        the vos
     * @return true, if is array empty
     */
    private boolean isArrayEmpty(CircularlyAccessibleValueObject[] vos) {
        return vos == null || vos.length == 0;
    }

    /**
     * Check with data base.
     *
     * @param tableName
     *        the table name
     * @param keyMap
     *        the key map
     * @param uniqueCheckVO
     *        the unique check vo
     * @throws BusinessException
     *         the business exception
     */
    @SuppressWarnings("unchecked")
    private void checkWithDataBase(String tableName,
                                   Map<String, IMMBDUniqueCheckVO> keyMap,
                                   IMMBDUniqueCheckVO uniqueCheckVO) throws BusinessException {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(uniqueCheckVO.getUniqueCheckField());
        sql.append(" , ");
        sql.append(uniqueCheckVO.getPKFieldName());
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(uniqueCheckVO.getUniqueCheckField());
        sql.append(" in ( ");
        sql.append(toArrayString(keyMap.keySet(), true));
        sql.append(" ) and isnull(dr,0) = 0 ");
        IUAPQueryBS queryBs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
        List<Object[]> existValues = (List<Object[]>) queryBs.executeQuery(sql.toString(), new ArrayListProcessor());
        Set<String> repeatValue = new HashSet<String>();
        List<String> repeatShowName = new ArrayList<String>();
        if (existValues != null && existValues.size() > 0) {
            for (Object[] objs : existValues) {

                IMMBDUniqueCheckVO vo = keyMap.get(objs[0]);

                if (!objs[1].equals(vo.getAttributeValue(vo.getPKFieldName()))) {
                    repeatValue.add((String) objs[0]);
                    repeatShowName.add(vo.getDisplayMessage());
                }
            }
            if (!repeatValue.isEmpty()) {
                throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("mmgp001_0", "0mmgp001-0098", null, new String[]{uniqueCheckVO.getUniqueCheckFieldName(),toArrayString(repeatShowName,false)})/*{0}【{1}】已经存在，不能保存！*/);
            }
        }
    }

    /**
     * 检查唯一性.
     *
     * @param billVo
     *        被检查的单据
     * @throws BusinessException
     *         BusinessException
     */
    private void multiUniqueCheck(AggregatedValueObject billVo) throws BusinessException {

        IMMBDMulUniqueCheckVO multiUniqueCheckVO = null;

        if (billVo.getParentVO() == null) {
            // 插入子表
            CircularlyAccessibleValueObject[] vos = billVo.getChildrenVO();
            if (isArrayEmpty(vos)) {
                return;
            }

            if (!IMMBDMulUniqueCheckVO.class.isAssignableFrom(vos[0].getClass())) {
                return;
            }

            IMMBDMulUniqueCheckVO[] bdvos = (IMMBDMulUniqueCheckVO[]) vos;
            multiUniqueCheckVO = bdvos[0];

        } else {
            CircularlyAccessibleValueObject vo = billVo.getParentVO();

            if (!IMMBDMulUniqueCheckVO.class.isAssignableFrom(vo.getClass())) {
                return;
            }
            multiUniqueCheckVO = (IMMBDMulUniqueCheckVO) vo;
        }
        if (!(billVo instanceof HYBillVO)) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0040")/*@res "聚合VO"*/ + billVo.getClass().getName() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0041")/*@res " 必须是HYBillVO（或子类）"*/);
        }
        // codesync_uf_v01 修改唯一性验证提示 modify -begin
        // 不与型钢代码同步
        MMBDUniqueCheck check = new MMBDUniqueCheck();
        try {
            check.checkBDisUnique((HYBillVO) billVo, multiUniqueCheckVO);
        } catch (Exception e) {
            if (null != multiUniqueCheckVO) {
                StringBuffer msg = new StringBuffer(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0042")/*@res "系统中已存在"*/);
                for (int i = 0; i < multiUniqueCheckVO.getUniqueCheckFieldNames().length; i++) {
                    if (0 != i) {
                        msg.append(",");
                    }
                    msg.append(" [");
                    msg.append(multiUniqueCheckVO.getUniqueCheckFieldNames()[i]);
                    msg.append("] ");
                }
                msg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0043")/*@res "相同的记录!"*/);
                throw new ValidationException(msg.toString());
            }
        }

        // codesync_uf_v01 modify -end
    }

    /**
     * To array string.
     *
     * @param repeatValues
     *        the repeat values
     * @param haveQuote
     *        the have quote
     * @return the string
     */
    @SuppressWarnings("rawtypes")
    private String toArrayString(Collection repeatValues,
                                 boolean haveQuote) {
        StringBuilder toString = new StringBuilder();
        for (Object object : repeatValues) {
            if (haveQuote) {
                toString.append("'");
            }
            toString.append(object.toString());
            if (haveQuote) {
                toString.append("'");
            }
            toString.append(",");
        }
        toString.deleteCharAt(toString.length() - 1);
        return toString.toString();
    }

    /**
     * Sets the audit data.
     *
     * @param billVo
     *        the bill vo
     * @param isCheckStatusWithPk
     *        状态是否由表头的PK值来判断,true代表通过pk值确定状态, false表示通过表头vo状态来判断
     */
    private void setAuditData(AggregatedValueObject billVo,
                              boolean isCheckStatusWithPk) {

        /**
         * CircularlyAccessibleValueObject[] beSetAuditVos = null; if (billVo.getParentVO() == null) { // 插入子表
         * beSetAuditVos = billVo.getChildrenVO(); } else { beSetAuditVos = new CircularlyAccessibleValueObject[] {
         * billVo .getParentVO() }; } UFDateTime time = getTime(); String userID = getUserID(); for
         * (CircularlyAccessibleValueObject vo : beSetAuditVos) { String pk = null; try { pk = vo.getPrimaryKey(); }
         * catch (BusinessException e) { Logger.error(e); } if (isPkStatusCheck) { if (StringUtil.isEmpty(pk)) { // 新增
         * vo.setAttributeValue("creationtime", time); vo.setAttributeValue("creator", userID); // codesync_uf_v01 add
         * ljj 20110916 if (MMStringUtil.isObjectStrEmpty(vo .getAttributeValue("pk_corp"))) {
         * vo.setAttributeValue("pk_corp", InvocationInfoProxy .getInstance().getCorpCode()); } } else { // 修改
         * vo.setAttributeValue("modifiedtime", time); vo.setAttributeValue("modifier", userID); } } else { if
         * (vo.getStatus() == VOStatus.NEW) { vo.setAttributeValue("creationtime", time);
         * vo.setAttributeValue("creator", userID); // codesync_uf_v01 add ljj 20110916 if
         * (MMStringUtil.isObjectStrEmpty(vo .getAttributeValue("pk_corp"))) { vo.setAttributeValue("pk_corp",
         * InvocationInfoProxy .getInstance().getCorpCode()); } } else { vo.setAttributeValue("modifiedtime", time);
         * vo.setAttributeValue("modifier", userID); } } }
         **/
    }

    /**
     * 设置单表体审计信息.
     *
     * @param voList
     *        the new list audit data
     */
    private void setListAuditData(List<SuperVO> voList) {

        /**
         * UFDateTime time = getTime(); String userID = getUserID(); for (SuperVO vo : voList) { String pk = null; pk =
         * vo.getPrimaryKey(); if (StringUtil.isEmpty(pk)) { // 新增 vo.setAttributeValue("creationtime", time); //
         * vo.setAttributeValue("ts", time); vo.setAttributeValue("creator", userID); vo.setAttributeValue("pk_corp",
         * InvocationInfoProxy .getInstance().getCorpCode()); } else { // 修改 vo.setAttributeValue("modifiedtime", time);
         * vo.setAttributeValue("modifier", userID); // vo.setAttributeValue("ts", time); } }
         **/
    }

    // private String getUserID() {
    // return InvocationInfoProxy.getInstance().getUserCode();
    // }
    //
    // private UFDateTime getTime() {
    // return new UFDateTime(new Date(TimeService.getInstance().getTime()));
    // }

    /**
     * {@inheritDoc}
     */
    public <T extends SuperVO> void mmdeleteBDByClause(Class<T> className,
                                                       String wherestr) throws DAOException {
        MMGPCmnDAO basDAO = new MMGPCmnDAO();

        basDAO.deleteByClause(className, wherestr);

    }

    /**
     * {@inheritDoc}
     */
    public void saveExAggregatedValueObjectList(List< ? extends AggregatedValueObject> billVoList,
                                                boolean lock,
                                                boolean isCheckByHeadVOStatus) throws BusinessException {
        try {
            List<AggregatedValueObject> toSave = new ArrayList<AggregatedValueObject>();
            List<AggregatedValueObject> toDelete = new ArrayList<AggregatedValueObject>();

            if (billVoList == null || billVoList.size() == 0) {
                return;
            }

            Set<String> lockPKSet = new HashSet<String>();

            for (AggregatedValueObject obj : billVoList) {
                // 设置创建人创建时间修改人修改时间
                setAuditData(obj, !isCheckByHeadVOStatus);
                SuperVO parentVO = (SuperVO) obj.getParentVO();
                SuperVO[] childrenVOS = (SuperVO[]) obj.getChildrenVO();
                List<SuperVO> childrenList = new ArrayList<SuperVO>(Arrays.asList(childrenVOS));
                if (parentVO.getStatus() == VOStatus.DELETED) {
                    // 表头另锁；加入到待删除列表中
                    lockPKSet.add(parentVO.getPrimaryKey());
                    toDelete.add(obj);
                    continue;
                }

                toSave.add(obj);

                if (parentVO.getStatus() == VOStatus.UPDATED) {
                    for (SuperVO childVO : childrenList) {
                        if (MMStringUtil.isEmpty(childVO.getPrimaryKey())) {
                            // 设置子表的主表主键
                            childVO.setAttributeValue(childVO.getParentPKFieldName(), parentVO.getPrimaryKey());
                        }
                    }
                    lockPKSet.add(parentVO.getPrimaryKey());
                }
            }

            if (lockPKSet != null && !lockPKSet.isEmpty() && lock) {
                MMGPPKLock.addBatchDynamicLock(lockPKSet.toArray(new String[0]));
            }

            BillDelete billDelete = new BillDelete();
            for (AggregatedValueObject agg : toDelete) {
                billDelete.deleteBD(agg, null);
            }

            MMBillSave save = new MMBillSave();
            save.saveBDs(toSave.toArray(new AggregatedValueObject[0]), null, isCheckByHeadVOStatus);
        } catch (Exception e) {
            ExceptionUtils.marsh(e);
        }

    }

    // /**
    // * 简要说明
    // *
    // * @see nc.itf.mmgp.IMmgpMaintainService#deleteMmgpVO(java.lang.Class,
    // java.lang.String)
    // */
    // public void deleteMmgpVO(Class<SuperVO> clazz,
    // String pk) throws BusinessException {
    // PKLock.getInstance().addDynamicLock(pk);
    // MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
    // cmnDAO.deleteByPK(clazz, pk);
    // }

    // /**
    // * 简要说明
    // *
    // * @see
    // nc.itf.mmgp.IMmgpMaintainService#saveMmgpAggVO(nc.vo.pub.AggregatedValueObject)
    // */
    // @SuppressWarnings("unchecked")
    // public <H extends SuperVO, B extends SuperVO> AggregatedValueObject
    // saveMmgpAggVO(AggregatedValueObject vo,
    // Class<H> headerVO,
    // Class<B> bodyVO)
    // throws BusinessException {
    //
    // MMGPAggDAO aggDao = new MMGPAggDAO();
    // String pk = null;
    //
    // if (isInsert((SuperVO) vo.getParentVO())) {
    // pk = aggDao.insertAggVOs(vo)[0];
    // } else {
    // MMGPPKLock.addDynamicLock(vo.getParentVO().getPrimaryKey());
    // MMGPCheckTsUtil.checkTimeStamp(headerVO, (H) vo.getParentVO());
    // aggDao.updateAggVO(vo);
    // pk = vo.getParentVO().getPrimaryKey();
    // }
    // return aggDao.queryAggVOByPk(vo.getClass(), headerVO, bodyVO, pk);
    // }

    // /**
    // * 简要说明
    // *
    // * @see nc.itf.mmgp.IMmgpMaintainService#saveMmgpVO(nc.vo.pub.SuperVO)
    // */
    // @SuppressWarnings("unchecked")
    // public SuperVO saveMmgpVO(SuperVO vo) throws BusinessException {
    // MMGPCmnDAO cmnDAO = new MMGPCmnDAO();
    // String pk = null;
    // if (isInsert(vo)) {
    // pk = cmnDAO.insertVO(vo);
    // } else {
    // PKLock.getInstance().addDynamicLock(vo.getPrimaryKey());
    // MMGPCheckTsUtil.checkTimeStamp((Class<SuperVO>) vo.getClass(), vo);
    // cmnDAO.updateVO(vo);
    // pk = vo.getPrimaryKey();
    // }
    // return cmnDAO.retrieveByPK(vo.getClass(), pk);
    // }

}