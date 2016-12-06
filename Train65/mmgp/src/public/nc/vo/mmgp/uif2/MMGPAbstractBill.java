package nc.vo.mmgp.uif2;

import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * <b> ��װpubapp��vo </b>
 * <p>
 * ����Ԫ·���Զ���װ�����Զ����ɼ̳б�vo��javaԴ���루uap-studio����Ҫ�򲹶�
 * </p>
 * ��������:2012-11-23
 * 
 * @author wangweiu
 */
public abstract class MMGPAbstractBill extends AbstractBill {

    /**
	 * 
	 */
    private static final long serialVersionUID = -806532374020764712L;

    private transient IBillMeta billMeta;

    @Override
    public IBillMeta getMetaData() {
        /* Dec 3, 2013 wangweir �޸�Ϊ�����أ�����Ч������ Begin */
        if (billMeta == null) {
            this.billMeta = initBillMeta();
        }
        return this.billMeta;
        /* Dec 3, 2013 wangweir End */
    }

    /**
     * @return
     */
    protected IBillMeta initBillMeta() {
        if (MMStringUtil.isEmpty(getMetaFullName())) {
            return null;
        }
        return MMGPBillMetaFactory.getInstance().getBillMeta(this.getMetaFullName());
        // return new MMGPAggBillMeta(getMetaFullName());
    }

    protected abstract String getMetaFullName();

}
