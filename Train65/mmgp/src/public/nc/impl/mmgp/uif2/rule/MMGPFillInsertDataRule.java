package nc.impl.mmgp.uif2.rule;

import nc.bs.mmgp.common.CommonUtils;
import nc.bs.pubapp.pub.rule.FillInsertDataRule;
import nc.vo.mmgp.MMGlobalConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 添加制单日期处理
 * </p>
 * 
 * @since 创建日期 Jun 27, 2013
 * @author wangweir
 */
public class MMGPFillInsertDataRule extends FillInsertDataRule {
    /*
     * (non-Javadoc)
     * @see nc.bs.pubapp.pub.rule.FillInsertDataRule#process(java.lang.Object[])
     */
    @Override
    public void process(Object[] bills) {
        super.process(bills);
        this.setDmakeDate((IBill[]) bills);
        this.setBillDate((IBill[]) bills);
    }

    /**
     * @param bills
     */
    protected void setBillDate(IBill[] bills) {
        if (MMArrayUtil.isEmpty(bills)) {
            return;
        }

        SuperVO head = (SuperVO) bills[0].getParent();
        if (!hasBillDateItem(head)) {
            return;
        }

        for (IBill bill : bills) {
            head = (SuperVO) bill.getParent();
            if (head.getAttributeValue(MMGlobalConst.DBILLDATE) != null) {
                continue;
            }
            head.setAttributeValue(MMGlobalConst.DBILLDATE, AppContext.getInstance().getBusiDate());
        }
    }

    protected boolean hasBillDateItem(SuperVO head) {
        for (String itemkey : head.getAttributeNames()) {
            if (MMGlobalConst.DBILLDATE.equals(itemkey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置制单日期
     * 
     * @param bills
     *        单据
     */
    protected void setDmakeDate(IBill[] bills) {
        for (IBill bill : bills) {
            ISuperVO head = bill.getParent();
            CommonUtils.setBillmakerAndDate(head);
        }
    }
}
