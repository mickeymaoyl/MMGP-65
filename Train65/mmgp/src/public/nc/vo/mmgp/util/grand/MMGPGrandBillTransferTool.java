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
 * <b> ��Ҫ�������� </b>
 * <p>
 * �����ﵥ�ݵ��ݼ򻯹���.
 * </p>
 *
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillTransferTool<E extends IBill> {

    /**
     * �ͻ��㴫���ĵ���
     */
    private E[] clientBills;

    /**
     * ���ݿ��д��ڵ�ԭʼ����
     */
    private E[] originBills;

    /**
     * ǰ��̨���ݴ��乤�߹��캯��
     *
     * @param bills
     *        ����ʵ��
     */
    public MMGPGrandBillTransferTool(E[] bills) {
        // Ϊ�˷�ֹ�����жϵ��µ����ظ����ӣ��˴��������ݵ����������Ѿ�����
        if (bills[0].getPrimaryKey() == null || bills[0].getParent().getStatus() == VOStatus.NEW) {
            this.initInserted(bills);
        } else {
            this.initUpdateed(bills);
        }
    }

    /**
     * ��ǰ̨����ʵ�����Ƚϣ���ȡ��Ҫ���ݵ�ǰ̨�ĵ���ʵ�����
     *
     * @param bills
     *        �����ĵ���ʵ��
     * @return ��Ҫ���ݵ�ǰ̨�ĵ���ʵ�����
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    public E[] getBillForToClient(E[] bills) {
        MMGPGrandServerBillToClient clientTransfer = new MMGPGrandServerBillToClient();
        E[] vos = (E[]) clientTransfer.construct(this.clientBills, bills);

        return vos;
    }

    /**
     * ��ȡǰ̨����������ʵ��
     *
     * @return ǰ̨����������ʵ��
     */
    @SuppressWarnings("unchecked")
    public E[] getClientFullInfoBill() {
        // ��¡��Ϊ�˷��㷵��ǰ̨ʱ֪����̨�������ָı���ʲô�ֶε�ֵ
        int length = this.clientBills.length;
        E[] bills = (E[]) Constructor.construct(this.clientBills[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            bills[i] = (E) this.clientBills[i].clone();
        }
        return bills;
    }

    /**
     * ��ȡ���ݿ��д�ŵ�����ԭʼ����ʵ��
     *
     * @return ���ݿ��д�ŵ�����ԭʼ����ʵ��
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
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0069")/*@res "�趨�����ӱ�������"*/); /* -=notranslate=- */
        MMGPGrandBillUtil.getInstance().setGCBillPrimayKey(bills);

        MMGPGrandBillConcurrentTool tool = new MMGPGrandBillConcurrentTool();
        TimeLog.logStart();
        tool.lockBill(bills);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0070")/*@res "���������ӱ��������"*/); /* -=notranslate=- */

        TimeLog.logStart();
        String[] ids = new String[bills.length];
        int length = bills.length;
        for (int i = 0; i < length; i++) {
            ids[i] = bills[i].getPrimaryKey();
        }
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0071")/*@res "��ȡ��������"*/); /* -=notranslate=- */

        TimeLog.logStart();
        MMGPGrandBillQuery query = new MMGPGrandBillQuery(bills[0].getClass());
        this.originBills = (E[]) query.query(ids);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0072")/*@res "��ѯԭʼ����VO"*/); /* -=notranslate=- */
        if (this.originBills == null || this.originBills.length == 0) {
            new VOConcurrentTool().throwUnSynchronizedException();
        }
        TimeLog.logStart();
        E[] vos = this.cloneBill(this.originBills);
        MMGPGrandServerBillCombinClient<E> combineClient = new MMGPGrandServerBillCombinClient<E>();
        combineClient.combine(vos, bills);
        this.clientBills = vos;
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0073")/*@res "ǰ̨����VO��������"*/); /* -=notranslate=- */

        TimeLog.logStart();
        tool.checkTS(bills, this.originBills);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0074")/*@res "���ʱ���"*/); /* -=notranslate=- */
    }

    @SuppressWarnings("unchecked")
    private E[] cloneBill(E[] originbills) {
        // �鼪�ڴ��޸ģ�ȥ�����Ϸ����мӵĴ��룬�ڴ˼����жϣ�
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