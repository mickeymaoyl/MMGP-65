package nc.vo.mmgp.util.grand;

import java.util.List;

import nc.md.model.IAttribute;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.pub.Constructor;
/**
 * 主子孙克隆工具类
 * @author gaotx
 *
 * @param <E> 继承IBill的VO
 */
public class MMGPGrandBillCloneTool <E extends IBill> {    
    /**
     * 克隆主子孙聚合VO
     * 
     * @author gaotx 2014-6-12
     * @param originBill
     * @return
     */
    public E cloneBill(E originBill) {
        if (originBill == null) {
            return originBill;
        }
        E[] arrayBill = (E[]) Constructor.construct(originBill.getClass(), 1);
        arrayBill[0] = originBill;
        E[] rtnBill = this.cloneBill(arrayBill);
        if (MMArrayUtil.isNotEmpty(rtnBill)) {
            return rtnBill[0];
        }
        return originBill;
    }

    @SuppressWarnings("unchecked")
    public E[] cloneBill(E[] originbills) {
        // 洪吉在此修改（去掉以上方法中加的代码，在此加入判断）
        if (originbills == null || originbills.length == 0) {
            return originbills;
        }
        int length = originbills.length;
        E[] vos = (E[]) Constructor.construct(originbills[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            vos[i] = (E) originbills[i].clone();
            this.cloneGCVOs(vos[i]);
        }
        return vos;
    }

    private void cloneGCVOs(E bill) {
        IBillMeta billMeta = bill.getMetaData();
        IVOMeta[] childMeta = billMeta.getChildren();
        if (childMeta == null || childMeta.length == 0) {
            return;
        }
        for (IVOMeta voMeta : childMeta) {
            ISuperVO[] childVOs = bill.getChildren(voMeta);
            if (childVOs == null || childVOs.length == 0) {
                continue;
            }
            bill.setChildren(voMeta, this.cloneVOs(childVOs));
        }
    }

    private ISuperVO[] cloneVOs(ISuperVO[] vos) {
        List<IAttribute> childAttrList = MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(vos[0]);
        if (childAttrList.isEmpty()) {
            return vos;
        }
        for (IAttribute childAttr : childAttrList) {
            for (ISuperVO voTemp : vos) {
                ISuperVO[] childVOs = (ISuperVO[]) childAttr.getAccessStrategy().getValue(voTemp, childAttr);
                if (childVOs == null || childVOs.length == 0) {
                    continue;
                }
                ISuperVO[] newChildVOs = this.batchClone(childVOs);
                childAttr.getAccessStrategy().setValue(voTemp, childAttr, newChildVOs);

            }
        }
        return vos;
    }

    private ISuperVO[] batchClone(ISuperVO[] vos) {
        int length = vos.length;
        ISuperVO[] newVOs = Constructor.construct(vos[0].getClass(), length);
        int i = 0;
        for (ISuperVO voTemp : vos) {
            newVOs[i++] = (ISuperVO) voTemp.clone();
        }
        this.cloneVOs(newVOs);
        return newVOs;
    }

}
