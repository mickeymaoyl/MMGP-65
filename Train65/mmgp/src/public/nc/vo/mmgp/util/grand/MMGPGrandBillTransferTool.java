package nc.vo.mmgp.util.grand;

import java.util.List;

import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.md.model.IAttribute;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙单据单据简化工具.
 * </p>
 *
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillTransferTool<E extends IBill> {

    /**
     * 客户算传来的单据
     */
    private E[] clientBills;

    /**
     * 数据库中存在的原始单据
     */
    private E[] originBills;

    /**
     * 前后台单据传输工具构造函数
     *
     * @param bills
     *        单据实体
     */
    public MMGPGrandBillTransferTool(E[] bills) {
        // 为了防止网络中断导致单据重复增加，此处新增单据的主键可能已经设置
        if (bills[0].getPrimaryKey() == null || bills[0].getParent().getStatus() == VOStatus.NEW) {
            this.initInserted(bills);
        } else {
            this.initUpdateed(bills);
        }
    }

    /**
     * 与前台单据实体作比较，获取需要传递到前台的单据实体快照
     *
     * @param bills
     *        完整的单据实体
     * @return 需要传递到前台的单据实体快照
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    public E[] getBillForToClient(E[] bills) {
        MMGPGrandServerBillToClient clientTransfer = new MMGPGrandServerBillToClient();
        E[] vos = (E[]) clientTransfer.construct(this.clientBills, bills);

        return vos;
    }

    /**
     * 获取前台的完整单据实体
     *
     * @return 前台的完整单据实体
     */
    @SuppressWarnings("unchecked")
    public E[] getClientFullInfoBill() {
        // 克隆是为了方便返回前台时知道后台保存中又改变了什么字段的值
        int length = this.clientBills.length;
        E[] bills = (E[]) Constructor.construct(this.clientBills[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            bills[i] = (E) this.clientBills[i].clone();
        }
        return bills;
    }

    /**
     * 获取数据库中存放的完整原始单据实体
     *
     * @return 数据库中存放的完整原始单据实体
     */
    public E[] getOriginBills() {
        return this.originBills;
    }

    @SuppressWarnings("unchecked")
    private void initInserted(E[] bills) {
        int size = bills.length;
        E[] vos = (E[]) Constructor.construct(bills[0].getClass(), size);
        for (int i = 0; i < size; i++) {
            vos[i] = (E) bills[i].clone();
        }
        this.originBills = vos;
        this.clientBills = vos;
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    private void initUpdateed(E[] bills) {
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0069")/*@res "设定主表，子表，孙表外键"*/); /* -=notranslate=- */
        MMGPGrandBillUtil.getInstance().setGCBillPrimayKey(bills);

        MMGPGrandBillConcurrentTool tool = new MMGPGrandBillConcurrentTool();
        TimeLog.logStart();
        tool.lockBill(bills);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0070")/*@res "锁定主表，子表，孙表主健"*/); /* -=notranslate=- */

        TimeLog.logStart();
        String[] ids = new String[bills.length];
        int length = bills.length;
        for (int i = 0; i < length; i++) {
            ids[i] = bills[i].getPrimaryKey();
        }
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0071")/*@res "获取单据主健"*/); /* -=notranslate=- */

        TimeLog.logStart();
        MMGPGrandBillQuery query = new MMGPGrandBillQuery(bills[0].getClass());
        this.originBills = (E[]) query.query(ids);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0072")/*@res "查询原始单据VO"*/); /* -=notranslate=- */
        if (this.originBills == null || this.originBills.length == 0) {
            new VOConcurrentTool().throwUnSynchronizedException();
        }
        TimeLog.logStart();
        E[] vos = this.cloneBill(this.originBills);
        MMGPGrandServerBillCombinClient<E> combineClient = new MMGPGrandServerBillCombinClient<E>();
        combineClient.combine(vos, bills);
        this.clientBills = vos;
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0073")/*@res "前台单据VO补充完整"*/); /* -=notranslate=- */

        TimeLog.logStart();
        tool.checkTS(bills, this.originBills);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0074")/*@res "检查时间戳"*/); /* -=notranslate=- */
    }

    @SuppressWarnings("unchecked")
    private E[] cloneBill(E[] originbills) {
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