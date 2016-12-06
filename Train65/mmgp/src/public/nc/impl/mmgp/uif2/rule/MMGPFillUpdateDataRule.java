package nc.impl.mmgp.uif2.rule;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pub.rule.FillUpdateDataRule;
import nc.pubitf.org.IOrgUnitPubService;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Aug 28, 2013
 * @author wangweir
 */
public class MMGPFillUpdateDataRule extends FillUpdateDataRule {

    /*
     * (non-Javadoc)
     * @see nc.bs.pubapp.pub.rule.FillUpdateDataRule#process(java.lang.Object[])
     */
    @Override
    public void process(Object[] vos) {
        super.process(vos);
        this.fillItemOrgs((IBill[]) vos);
    }

    private void fillItemOrgs(IBill[] bills) {
        for (IBill bill : bills) {
            ISuperVO head = bill.getParent();

            String pk_group = (String) head.getAttributeValue(MMGlobalConst.PK_GROUP);
            String pk_org = (String) head.getAttributeValue(MMGlobalConst.PK_ORG);
            String pk_org_v = (String) head.getAttributeValue(MMGlobalConst.PK_ORG_V);
            if (pk_org_v == null) {
                // 组织的最新版本
                Map<String, String> map;
                try {
                    UFDate busiDate = AppContext.getInstance().getBusiDate();
                    map =
                            NCLocator
                                .getInstance()
                                .lookup(IOrgUnitPubService.class)
                                .getNewVIDSByOrgIDSAndDate(new String[]{pk_org }, busiDate);
                    pk_org_v = map.get(pk_org);
                    head.setAttributeValue(MMGlobalConst.PK_ORG_V, pk_org_v);
                } catch (BusinessException e) {
                    ExceptionUtils.wrappException(e);
                }
            }

            for (IVOMeta meta : bill.getMetaData().getChildren()) {
                for (int i = 0; bill.getChildren(meta) != null && i < bill.getChildren(meta).length; i++) {
                    ISuperVO childvo = bill.getChildren(meta)[i];
                    if (childvo.getAttributeValue(MMGlobalConst.PK_GROUP) == null) {
                        childvo.setAttributeValue(MMGlobalConst.PK_GROUP, pk_group);
                    }
                    if (childvo.getAttributeValue(MMGlobalConst.PK_ORG) == null) {
                        childvo.setAttributeValue(MMGlobalConst.PK_ORG, pk_org);
                    }
                    if (childvo.getAttributeValue(MMGlobalConst.PK_ORG_V) == null) {
                        childvo.setAttributeValue(MMGlobalConst.PK_ORG_V, pk_org_v);
                    }
                }
            }
        }
    }

}
