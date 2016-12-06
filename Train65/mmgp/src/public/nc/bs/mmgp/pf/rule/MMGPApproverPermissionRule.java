package nc.bs.mmgp.pf.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pub.power.BillPowerChecker;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * �����Ȩ��У��
 * </p>
 *
 * @since �������� Oct 17, 2013
 * @author wangweir
 */
public class MMGPApproverPermissionRule<E extends AbstractBill> implements IRule<E> {

    private String resourceCode;

    private String vbillcode = MMGlobalConst.VBILLCODE;

    public MMGPApproverPermissionRule(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public MMGPApproverPermissionRule(String resourceCode,
                                      String vbillcode) {
        this.resourceCode = resourceCode;
        this.vbillcode = vbillcode;
    }

    @Override
    public void process(E[] vos) {
        if (MMStringUtil.isEmpty(resourceCode)) {
            return;
        }
        for (E vo : vos) {
            if (!BillPowerChecker.hasApproverPermission(vo, resourceCode)) {
                ExceptionUtils.wrappBusinessException(String.format(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0057")/*@res "��ǰ�û��Ե��ݲ�����������Ȩ�ޡ����ݺţ�%s"*/, new Object[]{vo
                    .getParent()
                    .getAttributeValue(this.vbillcode) }));
            }
        }
    }

}