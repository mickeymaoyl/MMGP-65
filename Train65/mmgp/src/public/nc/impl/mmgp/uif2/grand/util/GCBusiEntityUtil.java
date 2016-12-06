package nc.impl.mmgp.uif2.grand.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import nc.md.MDBaseQueryFacade;
import nc.md.common.AssociationKind;
import nc.md.model.IAssociation;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.ICardinality;
import nc.md.model.MetaDataException;
import nc.md.model.type.IType;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 业务实体查询工具，线程安全.
 * <p>
 * 1.查询SuperVO中子表的属性名字.
 * <p>
 * 2.获取SuperVO的实体名字，命名空间.实体名字.
 * <p>
 * 3.判断父表中的某一属性是否为子表的属性.
 * 
 * @since 6.3
 * @version 2012-6-14 下午02:38:33
 * @author zhaoshb
 */
public class GCBusiEntityUtil {
    private static GCBusiEntityUtil instance = new GCBusiEntityUtil();

    private ConcurrentMap<String, IBusinessEntity> enToBeMap = null;

    private ConcurrentMap<String, List<IAttribute>> enToChildAttrMap = null;

    private GCBusiEntityUtil() {
    }

    public static GCBusiEntityUtil getInstance() {
        return GCBusiEntityUtil.instance;
    }

    public List<IBusinessEntity> queryChildBusiEntity(ISuperVO vo) {
        IBusinessEntity parentBusiEntity = this.queryBusiEntity(vo);
        return this.queryChildBusiEntity(parentBusiEntity);
    }

    public List<IBusinessEntity> queryChildBusiEntity(IBusinessEntity parentBusiEntity) {
        List<IBusinessEntity> busiEntityList = new ArrayList<IBusinessEntity>();
        List<IAttribute> childAttrList = parentBusiEntity.getAttributes();
        for (IAttribute attrTemp : childAttrList) {
            boolean isChildAttr = this.isChildAttr(attrTemp);
            if (isChildAttr) {
                IBusinessEntity childBusiEntity = (IBusinessEntity) attrTemp.getAssociation().getEndBean();
                busiEntityList.add(childBusiEntity);
            }
        }
        return busiEntityList;
    }

    public Set<String> queryChildAttrName(IVOMeta meta) {
        String entityName = meta.getEntityName();
        return this.queryChildAttrName(entityName);
    }

    public Set<String> queryChildAttrName(ISuperVO vo) {
        String entityName = this.getEntityName(vo);
        return this.queryChildAttrName(entityName);
    }

    public Set<String> queryChildAttrName(SuperVO vo) {
        String entityName = this.getEntityName(vo);
        return this.queryChildAttrName(entityName);
    }

    public Set<String> queryChildAttrName(String entityName) {
        Set<String> childAttrNameSet = new HashSet<String>();
        List<IAttribute> childAttrList = this.queryChildAttr(entityName);
        for (IAttribute attrTemp : childAttrList) {
            childAttrNameSet.add(attrTemp.getName());
        }
        return childAttrNameSet;
    }

    public List<IAttribute> queryChildAttr(ISuperVO vo) {
        return this.queryChildAttr((SuperVO) vo);
    }

    public List<IAttribute> queryChildAttr(SuperVO vo) {
        String entityName = this.getEntityName(vo);
        return this.queryChildAttr(entityName);
    }

    public String getParentAttrName(String entityName) {
        IAttribute parAttr = this.getParentAttr(entityName);
        if (parAttr == null) {
            return null;
        }
        return parAttr.getName();
    }

    public IAttribute getParentAttr(String entityName) {
        IBusinessEntity busiEntity = this.queryBusiEntity(entityName);
        IBusinessEntity parentEntity = busiEntity.getParentEntity();
        if (parentEntity == null) {
            return null;
        }
        List<IAttribute> childAttr = this.queryChildAttr(parentEntity);
        String startBeanName = busiEntity.getName();
        for (IAttribute attrTemp : childAttr) {
            IBean bean = attrTemp.getAssociation().getEndBean();
            String endBeanName = bean.getName();
            if (startBeanName.equals(endBeanName)) {
                return attrTemp;
            }
        }
        return null;
    }

    public List<IAttribute> queryChildAttr(String entityName) {
        if (this.getEnToChildAttrMap().containsKey(entityName)) {
            return this.getEnToChildAttrMap().get(entityName);
        }
        List<IAttribute> childAttrList = new ArrayList<IAttribute>();
        IBusinessEntity be = this.queryBusiEntity(entityName);
        if (be == null) {
            return new ArrayList<IAttribute>();
        }
        List<IAttribute> containAttrs = be.getAttributes();
        for (IAttribute attrTemp : containAttrs) {
            if (this.isChildAttr(attrTemp)) {
                childAttrList.add(attrTemp);
            }
        }
        this.getEnToChildAttrMap().put(entityName, childAttrList);
        return childAttrList;
    }

    public Map<String, String> querychildAttrToCol(ISuperVO vo) {
        Map<String, String> childAttrToColMap = new HashMap<String, String>();
        List<IAttribute> childAttrList = this.queryChildAttr(vo);
        for (IAttribute attrTemp : childAttrList) {
            String attrName = attrTemp.getName();
            String colName = attrTemp.getColumn().getName();
            childAttrToColMap.put(attrName, colName);
        }
        return childAttrToColMap;
    }

    public String getEntityName(SuperVO vo) {
        IVOMeta voMeta = vo.getMetaData();
        return voMeta.getEntityName();
    }

    public String getEntityName(ISuperVO vo) {
        return vo.getMetaData().getEntityName();
    }

    public boolean isChildAttr(IAttribute attr) {
        if (attr.getDataType().getTypeType() == IType.COLLECTION) {
            if (attr.getAssociation().getEndBean() != null) {
                return true;
            }
        }
        return false;
    }

    public IBusinessEntity queryBusiEntity(ISuperVO vo) {
        String entityName = vo.getMetaData().getEntityName();
        return this.queryBusiEntity(entityName);
    }

    /**
     * 查询BusinessEntity.
     * 
     * @param entityName
     *            WorkSpace.EntityName
     * @return
     */
    public IBusinessEntity queryBusiEntity(String entityName) {
        if (this.getEnToBeMap().containsKey(entityName)) {
            return this.enToBeMap.get(entityName);
        }
        IBusinessEntity be = null;
        try {
            String[] parts = entityName.split("\\.");
            be = MDBaseQueryFacade.getInstance().getBusinessEntityByName(parts[0], parts[1]);
        }
        catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        if (be != null) {
            this.getEnToBeMap().put(entityName, be);
        }
        return be;
    }

    public List<IAttribute> queryChildAttr(IBean bean) {
        List<IAttribute> attrList = new ArrayList<IAttribute>();
        List<IAssociation> assoList = bean.getAssociationsByKind(AssociationKind.Composite, ICardinality.ASS_ONE2MANY);
        if (assoList == null || assoList.isEmpty()) {
            return attrList;
        }
        for (IAssociation assoTemp : assoList) {
            IAttribute attrTemp = assoTemp.getStartAttribute();
            attrList.add(attrTemp);
        }
        return attrList;
    }

    public Map<String, IBusinessEntity> getEnToBeMap() {
        if (this.enToBeMap == null) {
            this.enToBeMap = new ConcurrentHashMap<String, IBusinessEntity>();
        }
        return this.enToBeMap;
    }

    public Map<String, List<IAttribute>> getEnToChildAttrMap() {
        if (this.enToChildAttrMap == null) {
            this.enToChildAttrMap = new ConcurrentHashMap<String, List<IAttribute>>();
        }
        return this.enToChildAttrMap;
    }
}
