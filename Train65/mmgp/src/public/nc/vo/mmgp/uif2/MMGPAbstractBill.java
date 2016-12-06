package nc.vo.mmgp.uif2;

import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * <b> 封装pubapp的vo </b>
 * <p>
 * 根据元路径自动组装，另，自动生成继承本vo的java源代码（uap-studio）需要打补丁
 * </p>
 * 创建日期:2012-11-23
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
        /* Dec 3, 2013 wangweir 修改为懒加载，避免效率问题 Begin */
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
