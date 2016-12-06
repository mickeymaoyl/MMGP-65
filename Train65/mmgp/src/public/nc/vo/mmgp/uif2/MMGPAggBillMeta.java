package nc.vo.mmgp.uif2;

import java.util.List;

import nc.md.MDBaseQueryFacade;
import nc.md.common.AssociationKind;
import nc.md.model.IAssociation;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.ICardinality;
import nc.md.model.IComponent;
import nc.md.model.MetaDataException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.exception.CarrierRuntimeException;
import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.ModelVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> ������ʵ������ƹ���billmeata </b>
 * <p>
 * �����һ����Ԫ���ݵ���ʵ�����ƶ���������Ԫ��������
 * </p>
 * ��������:2012-11-23
 * 
 * @author wangweiu
 */
public class MMGPAggBillMeta extends AbstractBillMeta {
    /**
     * 
     */
    private static final String BODYOFAGGVOACCESSOR = "nc.md.model.access.BodyOfAggVOAccessor";

    /**
     * @param modlue
     *        .metadataname
     */
    public MMGPAggBillMeta(String metaName) {
        init(metaName);
    }

    protected void init(String metaName) {
        IBean bean;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByFullName(metaName);
        } catch (MetaDataException e) {
            throw new CarrierRuntimeException(e);
        }
        IComponent component = (IComponent) bean.getContainer();
        List<IBusinessEntity> entities = component.getBusinessEntities();
        /* May 19, 2013 wangweir �޸�ԭ��: 1�� ֧������ֶ����������������ֶβ�һ�£�2���������������p Begin */
        IBusinessEntity primaryEntity = null;
        AssociationKind kind = AssociationKind.Composite;
        int nCardinalityType = ICardinality.ASS_ALL;
        for (IBusinessEntity entity : entities) {
            if (entity.isPrimary()) {
                primaryEntity = entity;
                setParent(getClassByName(entity.getFullClassName()));
                List<IAssociation> associations = entity.getAssociationsByKind(kind, nCardinalityType);
                if (associations != null) {
                    for (IAssociation attr : associations) {
                        setAttrnameMetaRelation(
                            attr.getStartAttribute().getName(),
                            new ModelVOMeta(attr.getEndBean()));
                    }
                }
                break;
            }
        }

        // �����ʵ������ʵ��������ϵ
        if (primaryEntity == null) {
            return;
        }
        List<IAssociation> associations = primaryEntity.getAssociationsByKind(kind, nCardinalityType);
        for (IAssociation association : associations) {
            if (association.getStartAttribute().getAccessStrategy() == null) {
                continue;
            }

            // ���ʲ��Բ���BodyOfAggVOAccessor
            if (!association.getStartAttribute().getAccessStrategy().getClass().getName().equals(BODYOFAGGVOACCESSOR)) {
                continue;
            }

            String foregnkeyName = association.getStartAttribute().getColumn().getName();
            IVOMeta itemVOMeta = VOMetaFactory.getInstance().getVOMeta(association.getEndBean().getFullName());

            this.addChildren(this.getClassByName(association.getEndBean().getFullClassName()));
            this.addChildForeignKey(itemVOMeta.getAttribute(foregnkeyName));
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<ISuperVO> getClassByName(String classFullName) {
        try {
            return (Class<ISuperVO>) Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new CarrierRuntimeException(e);
        }
    }
}
