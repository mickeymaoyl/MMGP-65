package nc.vo.mmgp.util.grand;

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
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ҵ��ʵ���ѯ���ߣ��̰߳�ȫ.
 * <p>
 * 1.��ѯSuperVO���ӱ����������.
 * <p>
 * 2.��ȡSuperVO��ʵ�����֣������ռ�.ʵ������.
 * <p>
 * 3.�жϸ����е�ĳһ�����Ƿ�Ϊ�ӱ������.
 * </p>
 * 
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBusiEntityUtil {
    private static MMGPGrandBusiEntityUtil instance = new MMGPGrandBusiEntityUtil();

    private ConcurrentMap<String, IBusinessEntity> enToBeMap = null;

    private ConcurrentMap<String, List<IAttribute>> enToChildAttrMap = null;

    /**
     * ��������С����ʼ����Ϊ50
     */
    private int cacheSize = 50;

    private MMGPGrandBusiEntityUtil() {
    }

    public static MMGPGrandBusiEntityUtil getInstance() {
        return MMGPGrandBusiEntityUtil.instance;
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
    /**
     * ��ѯ����������
     * @param vo ����VO
     * @return ������Set
     */
    public Set<String> queryChildAttrName(ISuperVO vo) {
        String entityName = this.getEntityName(vo);
        return this.queryChildAttrName(entityName);
    }

    public Set<String> queryChildAttrName(SuperVO vo) {
        String entityName = this.getEntityName(vo);
        return this.queryChildAttrName(entityName);
    }
    /**
     * ����ʵ������ѯ��������
     * @param entityName
     * @return
     */
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
        List<IAttribute> attributes = this.getEnToChildAttrMap().get(entityName);
        if (attributes != null) {
            return attributes;
        }

        attributes = new ArrayList<IAttribute>();
        IBusinessEntity be = this.queryBusiEntity(entityName);
        if (be == null) {
            return new ArrayList<IAttribute>();
        }
        List<IAttribute> containAttrs = be.getAttributes();
        for (IAttribute attrTemp : containAttrs) {
            // ���˼�������
            if (this.isChildAttr(attrTemp) && !attrTemp.isCalculation()) {
                attributes.add(attrTemp);
            }
        }

        this.putEntityToAttributes(entityName, attributes);
        return attributes;
    }

    /**
     * @param entityName
     * @param attributes
     */
    private void putEntityToAttributes(String entityName,
                                       List<IAttribute> attributes) {
        if (this.getEnToChildAttrMap().size() >= cacheSize) {
            this.getEnToChildAttrMap().entrySet().iterator().remove();
        }
        this.getEnToChildAttrMap().put(entityName, attributes);
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
     * ��ѯBusinessEntity.
     * 
     * @param entityName
     *        WorkSpace.EntityName
     * @return
     */
    public IBusinessEntity queryBusiEntity(String entityName) {
        IBusinessEntity entity = this.getEnToBeMap().get(entityName);
        if (entity != null) {
            return entity;
        }
        try {
            String[] parts = entityName.split("\\.");
            entity = MDBaseQueryFacade.getInstance().getBusinessEntityByName(parts[0], parts[1]);
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }
        if (entity != null) {
            this.putEntityToBean(entityName, entity);
        }
        return entity;
    }

    /**
     * @param entityName
     * @param entity
     */
    private void putEntityToBean(String entityName,
                                 IBusinessEntity entity) {
        if (this.getEnToBeMap().size() >= 50) {
            this.getEnToBeMap().entrySet().iterator().remove();
        }
        this.getEnToBeMap().put(entityName, entity);
    }

    public List<IAttribute> queryChildAttr(IBean bean) {
        List<IAttribute> attrList = new ArrayList<IAttribute>();
        List<IAssociation> assoList =
                bean.getAssociationsByKind(AssociationKind.Composite, ICardinality.ASS_ONE2MANY);
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

    public static boolean isGrandBill(IBill[] bills) {
        if (MMArrayUtil.isEmpty(bills)) {
            return false;
        }

        IVOMeta[] childrenMetas = bills[0].getMetaData().getChildren();
        for (IVOMeta childrenMeta : childrenMetas) {
            List<IAttribute> attributes =
                    MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(childrenMeta.getEntityName());
            if (attributes.size() > 0) {
                return true;
            }
        }
        return false;
    }
}
