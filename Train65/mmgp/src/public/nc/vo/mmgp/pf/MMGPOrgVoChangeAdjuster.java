package nc.vo.mmgp.pf;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.org.cache.IOrgUnitPubService_C;
import nc.util.mmf.framework.base.MMCollectionUtil;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * <b> ������֯Ĭ��ֵ������ </b>
 * <p>
 * �ڽ�����������ʱ��������ε���Ϊ��ʷ�汾������������ʱӦ�ø���Ϊ���°汾
 * </p>
 * 
 * @since: ��������:Oct 31, 2013
 * @author:gaotx
 */
public class MMGPOrgVoChangeAdjuster implements IChangeVOAdjust {

    private String pkOrgField = "pk_org";

    private String pkOrgVField = "pk_org_v";

    private String pkOrgBodyField = pkOrgField;

    private String pkOrgVBodyField = pkOrgVField;

    private Map<String, String> orgIdVidCache = null;

    /**
     * ȡĬ���ֶ�
     */
    public MMGPOrgVoChangeAdjuster() {
        super();
    }

    /**
     * ��ͷ������֯�ֶ�һ��
     * 
     * @param pkOrgField
     * @param pkOrgVField
     */
    public MMGPOrgVoChangeAdjuster(String pkOrgField,
                                   String pkOrgVField) {
        this.pkOrgField = pkOrgField;
        this.pkOrgVField = pkOrgVField;
    }

    /**
     * �ֱ����ͷ����org�ֶ�
     * 
     * @param pkOrgField
     * @param pkOrgVField
     * @param pkOrgBodyField
     * @param pkOrgVBodyField
     */
    public MMGPOrgVoChangeAdjuster(String pkOrgField,
                                   String pkOrgVField,
                                   String pkOrgBodyField,
                                   String pkOrgVBodyField) {
        this.pkOrgField = pkOrgField;
        this.pkOrgVField = pkOrgVField;
        this.pkOrgBodyField = pkOrgBodyField;
        this.pkOrgVBodyField = pkOrgVBodyField;
    }

    @Override
    public AggregatedValueObject adjustBeforeChange(AggregatedValueObject srcVO,
                                                    ChangeVOAdjustContext adjustContext) throws BusinessException {
        return srcVO;
    }

    @Override
    public AggregatedValueObject adjustAfterChange(AggregatedValueObject srcVO,
                                                   AggregatedValueObject destVO,
                                                   ChangeVOAdjustContext adjustContext) throws BusinessException {
        AggregatedValueObject[] destVOs =
                this.batchAdjustAfterChange(
                    new AggregatedValueObject[]{srcVO },
                    new AggregatedValueObject[]{destVO },
                    adjustContext);
        if (MMArrayUtil.isEmpty(destVOs)) {
            return null;
        }
        return destVOs[0];
    }

    @Override
    public AggregatedValueObject[] batchAdjustBeforeChange(AggregatedValueObject[] srcVOs,
                                                           ChangeVOAdjustContext adjustContext)
            throws BusinessException {
        return srcVOs;
    }

    @Override
    public AggregatedValueObject[] batchAdjustAfterChange(AggregatedValueObject[] srcVOs,
                                                          AggregatedValueObject[] destVOs,
                                                          ChangeVOAdjustContext adjustContext)
            throws BusinessException {
        if (MMArrayUtil.isEmpty(destVOs)) {
            return destVOs;
        }
        // 0. ��ʼ����֯
        initOrgValue((AbstractBill[]) destVOs);
        // 1. �����ͷ������֯
        procNewOrgInfo((AbstractBill[]) destVOs);
        // 2. ����
        return destVOs;
    }

    /**
     * �����ͷ������֯
     * 
     * @param destVOs
     */
    protected void procNewOrgInfo(AbstractBill[] destVOs) {
        for (AbstractBill o : destVOs) {
            ISuperVO head = o.getParent();
            fillOrgInfo(head, this.pkOrgField, this.pkOrgVField);
            // �������,֧�ֶ����
            IBillMeta billMeta = o.getMetaData();
            IVOMeta[] children = billMeta.getChildren();
            if (children == null) {
                return;
            }
            for (IVOMeta child : children) {
                ISuperVO[] childrenVO = o.getChildren(child);
                if (MMArrayUtil.isEmpty(childrenVO)) {
                    return;
                }
                for (ISuperVO body : childrenVO) {
                    fillOrgInfo(body, this.pkOrgBodyField, this.pkOrgVBodyField);
                }
            }
            afterFillOrgInfo(o);
        }
    }

    /**
     * �������ദ���������������ݵ�Ԥ������
     * 
     * @param o
     */
    protected void afterFillOrgInfo(AggregatedValueObject o) {

    }

    /**
     * ��ʼ����֯����
     * 
     * @param destVOs
     */
    private void initOrgValue(AbstractBill[] destVOs) {
        Set<String> pkOrgSet = getAllPkOrgs(destVOs);
        orgIdVidCache = queryOrgVidByIds(pkOrgSet);
    }

    /**
     * ��ȡ�����ķ��������������ѯ
     * 
     * @param pkOrgSet
     * @return
     */
    protected Map<String, String> queryOrgVidByIds(Collection<String> pkOrgSet) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            if (MMCollectionUtil.isEmpty(pkOrgSet)) {
                return rtnMap;
            }
            rtnMap =
                    NCLocator
                        .getInstance()
                        .lookup(IOrgUnitPubService_C.class)
                        .getNewVIDSByOrgIDS(pkOrgSet.toArray(new String[0]));
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return rtnMap;
    }

    /**
     * �������ӱ�VO����ȡ���е�pk_org
     * 
     * @param destVOs
     * @return
     */
    protected Set<String> getAllPkOrgs(AbstractBill[] destVOs) {
        Set<String> pkOrgSet = new HashSet<String>();
        for (AbstractBill vo : destVOs) {
            getOneBillPkOrg(pkOrgSet, vo);
        }
        return pkOrgSet;
    }

    /**
     * ��ȡ��һ�����ϵ�pk_org
     * 
     * @param pkOrgSet
     * @param vo
     */
    protected void getOneBillPkOrg(Set<String> pkOrgSet,
                                   AbstractBill vo) {
        ISuperVO head = vo.getParent();
        String pk_org = (String) head.getAttributeValue(pkOrgField);
        if (MMStringUtil.isNotEmpty(pk_org)) {
            pkOrgSet.add(pk_org);
        }
        // �������,֧�ֶ����
        IBillMeta billMeta = vo.getMetaData();
        IVOMeta[] children = billMeta.getChildren();
        if (children == null) {
            return;
        }
        for (IVOMeta child : children) {
            ISuperVO[] childrenVO = vo.getChildren(child);
            if (MMArrayUtil.isEmpty(childrenVO)) {
                continue;
            }
            for (ISuperVO body : childrenVO) {
                String bodyOrg = (String) body.getAttributeValue(pkOrgBodyField);
                if (MMStringUtil.isNotEmpty(bodyOrg)) {
                    pkOrgSet.add(bodyOrg);
                }
            }
        }
    }

    /**
     * �����ͷ������֯�汾
     * 
     * @param obj
     * @param pkOrgField
     * @param pkOrgVField
     */
    private void fillOrgInfo(ISuperVO obj,
                             String pkOrgField2,
                             String pkOrgVField2) {
        if (this.orgIdVidCache == null || this.orgIdVidCache.size() == 0) {
            return;
        }
        String newPkOrgV = this.orgIdVidCache.get(obj.getAttributeValue(pkOrgField2));
        if (MMStringUtil.isEmpty(newPkOrgV)) {
            return;
        }
        obj.setAttributeValue(pkOrgVField2, newPkOrgV);
    }

    public String getPkOrgField() {
        return pkOrgField;
    }

    public void setPkOrgField(String pkOrgField) {
        this.pkOrgField = pkOrgField;
    }

    public String getPkOrgVField() {
        return pkOrgVField;
    }

    public void setPkOrgVField(String pkOrgVField) {
        this.pkOrgVField = pkOrgVField;
    }

    public String getPkOrgBodyField() {
        return pkOrgBodyField;
    }

    public void setPkOrgBodyField(String pkOrgBodyField) {
        this.pkOrgBodyField = pkOrgBodyField;
    }

    public String getPkOrgVBodyField() {
        return pkOrgVBodyField;
    }

    public void setPkOrgVBodyField(String pkOrgVBodyField) {
        this.pkOrgVBodyField = pkOrgVBodyField;
    }

    public Map<String, String> getOrgIdVidCache() {
        return orgIdVidCache;
    }

    public void setOrgIdVidCache(Map<String, String> orgIdVidCache) {
        this.orgIdVidCache = orgIdVidCache;
    }

}
