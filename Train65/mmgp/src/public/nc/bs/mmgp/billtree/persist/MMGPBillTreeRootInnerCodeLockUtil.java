package nc.bs.mmgp.billtree.persist;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.pubitf.eaa.InnerCodeUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.util.BDPKLockUtil;

import org.apache.commons.lang.StringUtils;

public class MMGPBillTreeRootInnerCodeLockUtil<T extends IBill> {
    
    private IMMGPBillTreePersistenceUtil<T> treePersistenceUtil = null;

    public MMGPBillTreeRootInnerCodeLockUtil(IMMGPBillTreePersistenceUtil<T> treePersistenceUtil) {
        this.treePersistenceUtil = treePersistenceUtil;
    }

    public void lockBranchRoot(T vo) throws BusinessException {
        if (vo == null) {
            return;
        }
        if (StringUtils.isNotBlank(getInnerCode(vo))) {
            lockInnerCode(getInnerCode(vo));
        } else {
            lockBranchRootByParentInnerCode(vo);
        }
    }

    public void lockBranchRootWhenParentChanged(T vo) throws BusinessException {
        if (vo == null) {
            return;
        }
        lockInnerCode(getInnerCode(vo));
        if (parentChanged(vo)) {
            lockBranchRootByParentInnerCode(vo);
        }
    }

    private void lockBranchRootByParentInnerCode(T vo) throws BusinessException {
        if (getParentPk(vo) != null) {
            lockInnerCode(queryParentInnerCode(vo));
        }
    }

    private void lockInnerCode(String innerCode) throws BusinessException {
        if (StringUtils.isBlank(innerCode)) {
            throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("baseservice",
                            "0baseservice0005")/* @res "待加锁的信息不全（内部码为空）" */);
        }
        String rootCode = innerCode.substring(0, InnerCodeUtil.INNERCODELENGTH);
        lockRootInnerCode(rootCode);
    }

    protected void lockRootInnerCode(String rootCode) throws BusinessException {
        String tableName = getTreePersistenceUtil().getTableName();
        if (StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("baseservice",
                            "0baseservice0006")/* @res "待加锁的信息不全（无法确定数据库表信息）" */);
        }
        BDPKLockUtil.lockString(rootCode + tableName);
    }

    private boolean parentChanged(T vo) throws BusinessException {
        String currentPid = getParentPk(vo);
        String oldPid = queryOldParentPk(vo);
        return !StringUtils.equals(currentPid, oldPid);
    }

    private String queryOldParentPk(T vo) throws BusinessException {
        String pidName = getTreePersistenceUtil().getParentPkFieldName();
        T[] oldVOs = getTreePersistenceUtil().retrieveVOByFields(
                new String[] { vo.getPrimaryKey() }, new String[] { pidName });
        validateVOExists(oldVOs);
        return getParentPk(oldVOs[0]);
    }

    private String queryParentInnerCode(T vo) throws BusinessException {
        T[] parentVOs = getTreePersistenceUtil().retrieveVOByFields(
                new String[] { getParentPk(vo) },
                new String[] { IBaseServiceConst.INNERCODE_FIELD });
        validateVOExists(parentVOs);
        return getInnerCode(parentVOs[0]);
    }

    private void validateVOExists(T[] vos) throws BusinessException {
        if (vos == null || vos.length == 0
                || (vos != null && vos.length != 0 && vos[0] == null)) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("baseservice",
                            "0baseservice0007")/* @res "数据已被他人修改，请刷新后再操作" */);
        }
    }

    private String getInnerCode(T vo) {
        return (String) vo.getParent().getAttributeValue(IBaseServiceConst.INNERCODE_FIELD);
    }

    private String getParentPk(T vo) throws BusinessException {
        String pidName = getTreePersistenceUtil().getParentPkFieldName();
        return (String) vo.getParent().getAttributeValue(pidName);
    }

    private IMMGPBillTreePersistenceUtil<T> getTreePersistenceUtil() {
        return treePersistenceUtil;
    }

}
