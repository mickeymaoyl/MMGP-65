package nc.bs.mmgp.billtree.persist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.md.model.access.javamap.AggVOStyle;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.IMDPersistenceService;
import nc.md.persist.framework.MDPersistenceService;
import nc.md.util.MDUtil;
import nc.pubitf.eaa.InnerCodeUtil;
import nc.vo.bd.access.tree.AbastractTreeCreateStrategy;
import nc.vo.bd.access.tree.BDTreeCreator;
import nc.vo.bd.meta.IBDObject;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.uif2.CodeRuleInvalidException;
import nc.vo.uif2.CodeRuleUtil;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.UniqueConditionUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <b> 左树右单据后台持久化实现类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:Sep 15, 2014
 * @author:liwsh
 */
public class MMGPBillTreePersistenceUtil<T extends IBill> implements IMMGPBillTreePersistenceUtil<T> {

    /**
     * 父节点字段名称
     */
    private String parentPkFieldName = null;

    /**
     * 元数据bean id
     */
    private String MDId;

    /**
     * 元数据
     */
    private IBean bean = null;

    /**
     * 构造函数
     * 
     * @param MDId
     *        元数据id
     * @param subAttributeNames
     */
    public MMGPBillTreePersistenceUtil(String MDId) {
        this.MDId = MDId;
    }

    @Override
    public String[] insertVOWithInnerCode(T... vos) throws BusinessException {

        NCObject[] ncobjs = new NCObject[vos.length];
        for (int i = 0; i < vos.length; i++) {
            vos[i].getParent().setStatus(VOStatus.NEW);
            ncobjs[i] = getNCObject(vos[i]);
        }
        String[] pks = getMDService().saveBill(ncobjs);
        List<T> sortVOs = sortByTreePreorder(vos);
        for (T vo : sortVOs) {
            InnerCodeUtil.generateInnerCodeAfterInsert((SuperVO) vo.getParent());
        }
        return pks;
    }

    @Override
    public void updateVOWithInnerCode(T vo,
                                      T oldVO) throws BusinessException {

        vo.getParent().setStatus(VOStatus.UPDATED);

        IBill[] originBills = new IBill[]{oldVO };
        IBill[] bills = new IBill[]{vo };
        
        BillUpdate<IBill> billUpdateAction = new BillUpdate<IBill>();
        billUpdateAction.update(bills, originBills);

        if (parentIDChanged(vo, oldVO)) {
            InnerCodeUtil.generateInnerCodeAfterChangePosition((SuperVO) vo.getParent());
        }

    }

    private boolean parentIDChanged(T vo,
                                    T oldVO) throws BusinessException {
        String newParentId = (String) vo.getParent().getAttributeValue(getParentPkFieldName());
        String oldParentId = (String) oldVO.getParent().getAttributeValue(getParentPkFieldName());
        return !StringUtils.equals(newParentId, oldParentId);
    }

    @Override
    public void updateVOWithAttrs(String[] fields,
                                  T... vos) throws BusinessException {
        getMDService().updateBillWithAttrs(vos, fields);
    }

    @Override
    public void deleteVO(boolean deleteFromDB,
                         T... vos) throws BusinessException {

        NCObject[] ncObjs = getNCObject(vos);

        if (deleteFromDB) {
            getMDService().deleteBillFromDB(ncObjs);
        } else {
            getMDService().deleteBill(ncObjs);
        }

    }

    @Override
    public T[] retrieveVO(String[] pks) throws BusinessException {
        if (pks == null || pks.length == 0) return null;
        Object[] objs = getMDQueryService().queryBillOfVOByPKsWithOrder(getEntityClass(), pks, false);

        return convertToVOArray(getEntityClass(), objs);
    }

    @Override
    public T[] retrieveVOByFields(String[] pks,
                                  String[] fields) throws BusinessException {
        if (pks == null || pks.length == 0) return null;
        NCObject[] objs = getMDQueryService().queryBillOfNCObjectByPKs(getEntityClass(), pks, fields, false);
        return convertToVOArray(getEntityClass(), objs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] retrieveSubVOsWithSelf(String pk) throws BusinessException {
        String condition = IBaseServiceConst.INNERCODE_FIELD + " like '" + getOldInnerCode(pk) + "%'";
        Collection<T> col =
                getMDQueryService().queryBillOfVOByCondWithOrder(
                    getEntityClass(),
                    condition,
                    false,
                    false,
                    new String[]{IBaseServiceConst.INNERCODE_FIELD });
        return col.isEmpty() ? null : convertToVOArray(getEntityClass(), col.toArray());
    }

    private String getOldInnerCode(String pk) throws BusinessException {
        T[] vos = retrieveVOByFields(new String[]{pk }, new String[]{IBaseServiceConst.INNERCODE_FIELD });
        return vos[0] == null ? null : (String) vos[0].getParent().getAttributeValue(
            IBaseServiceConst.INNERCODE_FIELD);
    }

    @Override
    public String retrieveParentPk(T vo,
                                   CodeRuleUtil codeRuleUtil) throws BusinessException {
        String parentCode = getParentCode(vo, codeRuleUtil);
        if (StringUtils.isNotBlank(parentCode)) {
            String uniqueCondition = UniqueConditionUtil.getUniqueCondition((SuperVO) vo.getParent());
            String parentPK =
                    queryPkByUniqueConditionAndCode(uniqueCondition, parentCode, vo.getParent().getClass().getName());
            if (StringUtils.isBlank(parentPK) || parentPK.equals(vo.getPrimaryKey())) {
                throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                    "baseservice",
                    "0baseservice0004")/* @res "上级节点不存在。" */);
            }
            return parentPK;
        }
        return null;
    }

    private String getParentCode(T vo,
                                 CodeRuleUtil codeRuleUtil) throws CodeRuleInvalidException {
        String code = (String) vo.getParent().getAttributeValue(IBaseServiceConst.CODE_FIELD);
        codeRuleUtil.validateCode(code);
        String parentCode = codeRuleUtil.getParentCode(code);
        return parentCode;
    }

    private String queryPkByUniqueConditionAndCode(String uniqueCondition,
                                                   String code,
                                                   String parentClassName) throws BusinessException,
            MetaDataException, DAOException {

        IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(parentClassName);
        // IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
        String sql =
                "select "
                    + bean.getTable().getPrimaryKeyName()
                    + " from "
                    + bean.getTable().getName()
                    + " where "
                    + new SqlWhereUtil(uniqueCondition).and(IBaseServiceConst.CODE_FIELD + " = ?").getSQLWhere();
        SQLParameter parameter = new SQLParameter();
        parameter.addParam(code);
        String parentPK = (String) new BaseDAO().executeQuery(sql, parameter, new ColumnProcessor());
        return parentPK;
    }

    @Override
    public void notifyVersionChangeWhenDataDeleted(T... vos) throws BusinessException {
        if (ArrayUtils.isEmpty(vos)) {
            return;
        }

        List<String> pkList = new ArrayList<String>();
        for (T vo : vos) {
            pkList.add(vo.getParent().getPrimaryKey());
        }
        String[] pks = pkList.toArray(new String[0]);

        IBean parentBean =
                MDBaseQueryFacade.getInstance().getBeanByFullClassName(vos[0].getParent().getClass().getName());
        for (String tableName : getTableNamesByBean(parentBean)) {
            CacheProxy.fireDataDeletedBatch(tableName, pks);
        }

        IVOMeta[] childMetas = vos[0].getMetaData().getChildren();

        if (MMArrayUtil.isEmpty(childMetas)) {
            return;
        }

        for (IVOMeta childMeta : childMetas) {

            List<String> childPkList = new ArrayList<String>();
            for (T vo : vos) {
                ISuperVO[] childrens = vo.getChildren(childMeta);
                if (MMArrayUtil.isEmpty(childrens)) {
                    continue;
                }
                for (ISuperVO supervo : childrens) {
                    childPkList.add(supervo.getPrimaryKey());
                }
            }

            if (MMCollectionUtil.isEmpty(childPkList)) {
                continue;
            }

            IBean childBean =
                    MDBaseQueryFacade.getInstance().getBeanByFullClassName(
                        vos[0].getChildren(childMeta)[0].getClass().getName());

            for (String tableName : getTableNamesByBean(childBean)) {
                CacheProxy.fireDataDeletedBatch(tableName, childPkList.toArray(new String[0]));
            }
        }
    }

    @Override
    public void notifyVersionChangeWhenDataInserted(T... vos) throws BusinessException {
        if (ArrayUtils.isEmpty(vos)) {
            return;
        }

        IBean parentBean =
                MDBaseQueryFacade.getInstance().getBeanByFullClassName(vos[0].getParent().getClass().getName());
        for (String tableName : getTableNamesByBean(parentBean)) {
            CacheProxy.fireDataInserted(tableName);
        }

        IVOMeta[] childMetas = vos[0].getMetaData().getChildren();
        if (MMArrayUtil.isEmpty(childMetas)) {
            return;
        }

        for (IVOMeta childMeta : childMetas) {

            ISuperVO[] children = vos[0].getChildren(childMeta);
            if (MMArrayUtil.isEmpty(children)) {
                continue;
            }

            IBean childBean =
                    MDBaseQueryFacade.getInstance().getBeanByFullClassName(children[0].getClass().getName());

            for (String tableName : getTableNamesByBean(childBean)) {
                CacheProxy.fireDataInserted(tableName);
            }

        }
    }

    @Override
    public void notifyVersionChangeWhenDataUpdated(T... vos) throws BusinessException {
        if (ArrayUtils.isEmpty(vos)) {
            return;
        }

        IBean parentBean =
                MDBaseQueryFacade.getInstance().getBeanByFullClassName(vos[0].getParent().getClass().getName());
        for (String tableName : getTableNamesByBean(parentBean)) {
            CacheProxy.fireDataUpdated(tableName);
        }

        IVOMeta[] childMetas = vos[0].getMetaData().getChildren();

        if (MMArrayUtil.isEmpty(childMetas)) {
            return;
        }

        for (IVOMeta childMeta : childMetas) {

            Set<String> deletePkSet = new HashSet<String>();
            boolean isChanged = false;
            for (T vo : vos) {
                ISuperVO[] childrens = vo.getChildren(childMeta);

                if (MMArrayUtil.isEmpty(childrens)) {
                    continue;
                }
                for (ISuperVO supervo : childrens) {
                    if (supervo.getStatus() != VOStatus.UNCHANGED) {
                        isChanged = true;
                    }
                    if (supervo.getStatus() == VOStatus.DELETED) {
                        deletePkSet.add(supervo.getPrimaryKey());
                    }
                }
            }
            IBean childBean =
                    MDBaseQueryFacade.getInstance().getBeanByFullClassName(
                        vos[0].getChildren(childMeta)[0].getClass().getName());

            if (MMCollectionUtil.isNotEmpty(deletePkSet)) {
                for (String tableName : getTableNamesByBean(childBean)) {
                    CacheProxy.fireDataDeletedBatch(tableName, deletePkSet.toArray(new String[0]));
                }
            } else {
                if (isChanged) {
                    for (String tableName : getTableNamesByBean(childBean)) {
                        CacheProxy.fireDataUpdated(tableName);
                    }
                }
            }
        }

    }

    @Override
    public Class<T> getEntityClass() throws BusinessException {
        try {
            String aggvoClassName = ((AggVOStyle) this.getBean().getBeanStyle()).getAggVOClassName();
            return (Class<T>) Class.forName(aggvoClassName);
        } catch (ClassNotFoundException e) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "baseservice",
                "0baseservice0003")/* @res "元数据对应的VO不存在。" */);
        }
    }

    protected IBean getBean() throws BusinessException {
        if (bean == null) {
            bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
        }
        return bean;
    }

    @Override
    public String getParentPkFieldName() throws BusinessException {
        if (parentPkFieldName == null) {
            IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(getMDId());
            Map<String, String> name_path_map =
                    ((IBusinessEntity) bean).getBizInterfaceMapInfo(IBDObject.class.getName());
            parentPkFieldName = name_path_map.get("pid");
        }
        return parentPkFieldName;
    }

    protected String getMDId() {
        return MDId;
    }

    @SuppressWarnings("unchecked")
    private List<T> sortByTreePreorder(T... vos) {
        List<T> sortVOs = new ArrayList<T>();
        if (vos.length > 1) {
            DefaultTreeModel tree = BDTreeCreator.createTree(vos, new sortVOTreeCreateStrategy());
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getRoot();
            Enumeration<DefaultMutableTreeNode> e = root.preorderEnumeration();
            e.nextElement();
            while (e.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                sortVOs.add((T) node.getUserObject());
            }
        } else {
            sortVOs.add(vos[0]);
        }
        return sortVOs;
    }

    @SuppressWarnings("unchecked")
    private class sortVOTreeCreateStrategy extends AbastractTreeCreateStrategy {
        public Object getNodeId(Object obj) {
            return ((T) obj).getParent().getPrimaryKey();
        }

        public Object getParentNodeId(Object obj) {
            try {
                return ((T) obj).getParent().getAttributeValue(getParentPkFieldName());
            } catch (BusinessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public boolean isCodeTree() {
            return false;
        }
    }

    protected static IMDPersistenceService getMDService() {
        return MDPersistenceService.lookupPersistenceService();
    }

    protected static IMDPersistenceQueryService getMDQueryService() {
        return MDPersistenceService.lookupPersistenceQueryService();
    }

    public T[] convertToVOArray(Class<T> clazz,
                                Object... objs) {
        if (objs == null || objs.length == 0) {
            return null;
        }
        T[] vos = (T[]) Array.newInstance(clazz, objs.length);
        for (int i = 0; i < objs.length; i++) {

            if (objs[i] instanceof NCObject) {
                vos[i] = (T) ((NCObject) objs[i]).getContainmentObject();
            } else {
                vos[i] = (T) objs[i];
            }
        }
        return vos;
    }

    private Set<String> getTableNamesByBean(IBean bean) {
        Set<String> tableNames = new HashSet<String>();
        tableNames.add(bean.getTable().getName());
        return tableNames;
    }

    public String getTableName() throws BusinessException {
        return this.getBean().getTable().getName();
    }

    
    public NCObject getNCObject(Object billVo) throws MetaDataException {
        NCObject ncObj = NCObject.newInstance(billVo);
        if (ncObj == null)
            throw new MetaDataException(NCLangResOnserver.getInstance().getStrByID("mdbusi", "mdPersistUtil-0000")/*要保存的SuperVO没有设置元数据，无法进行持久化,SuperVo:*/
                    + billVo.getClass().getName());
        if (!MDUtil.isEntityType(ncObj.getRelatedBean()))
            throw new MetaDataException(NCLangResOnserver.getInstance().getStrByID("mdbusi", "mdPersistUtil-0001")/*要保存的SuperVO对应的元数据不是实体类型，请检查模型！beanName:*/
                    + ncObj.getRelatedBean().getName());
        return ncObj;
    }

    public NCObject[] getNCObject(Object[] billVos) throws MetaDataException {
        if (billVos == null || billVos.length == 0)
            return null;
        NCObject[] ncObjs = new NCObject[billVos.length];
        IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(
                billVos[0].getClass().getName());
        if (!MDUtil.isEntityType(bean))
            throw new MetaDataException(NCLangResOnserver.getInstance().getStrByID("mdbusi", "mdPersistUtil-0001")/*要保存的SuperVO对应的元数据不是实体类型，请检查模型！beanName:*/ + bean.getFullName());
        for (int i = 0; i < billVos.length; i++) {
            ncObjs[i] = NCObject.newInstance(bean, billVos[i]);
        }
        return ncObjs;
    }
}
