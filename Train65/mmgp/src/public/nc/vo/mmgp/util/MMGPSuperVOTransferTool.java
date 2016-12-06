package nc.vo.mmgp.util;

import java.util.Set;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.mddb.constant.ElementConstant;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;
import nc.vo.pubapp.pattern.model.tool.VOTool;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 *
 * @since �������� Jul 15, 2013
 * @author wangweir
 */
public class MMGPSuperVOTransferTool<E extends SuperVO> {
    /**
     * �ͻ��㴫���ĵ���
     */
    private E[] clientVOs;

    /**
     * ���ݿ��д��ڵ�ԭʼ����
     */
    private E[] originVOs;

    /**
     * ǰ��̨���ݴ��乤�߹��캯��
     *
     * @param bills
     *        ����ʵ��
     */
    public MMGPSuperVOTransferTool(E[] bills) {
        // Ϊ�˷�ֹ�����жϵ��µ����ظ����ӣ��˴��������ݵ����������Ѿ�����
        if ((bills[0].getPrimaryKey() == null) || (bills[0].getStatus() == VOStatus.NEW)) {
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
    @SuppressWarnings({"unchecked" })
    public E[] getBillForToClient(E[] bills) {
        E[] newBills = (E[]) Constructor.construct(bills[0].getClass(), bills.length);
        for (int i = 0; i < bills.length; i++) {
            newBills[i] = this.contruct(this.clientVOs[i], bills[i]);
        }

        return newBills;
    }

    @SuppressWarnings("unchecked")
    protected E contruct(E clientVO,
                         E vo) {
        if (clientVO == null) {
            return null;
        }
        E newVO = (E) Constructor.construct(clientVO.getClass());
        VOTool tool = new VOTool();
        Set<String> set = tool.getDifferentFieldForDynamic(vo, clientVO);
        this.appendMandatoryKey(set, vo);
        for (String name : set) {
            Object value = vo.getAttributeValue(name);
            newVO.setAttributeValue(name, value);
        }
        String name = PseudoColumnAttribute.PSEUDOCOLUMN;
        Object value = vo.getAttributeValue(name);
        newVO.setAttributeValue(name, value);
        return newVO;
    }

    protected void appendMandatoryKey(Set<String> set,
                                      ISuperVO vo) {
        IAttributeMeta keyMeta = vo.getMetaData().getPrimaryAttribute();
        set.add(keyMeta.getName());
        set.add(ElementConstant.KEY_TS);
    }

    /**
     * ��ȡǰ̨����������ʵ��
     *
     * @return ǰ̨����������ʵ��
     */
    @SuppressWarnings("unchecked")
    public E[] getClientFullInfoBill() {
        // ��¡��Ϊ�˷��㷵��ǰ̨ʱ֪����̨�������ָı���ʲô�ֶε�ֵ
        int length = this.clientVOs.length;
        E[] bills = (E[]) Constructor.construct(this.clientVOs[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            bills[i] = (E) this.clientVOs[i].clone();
        }
        return bills;
    }

    /**
     * ��ȡ���ݿ��д�ŵ�����ԭʼ����ʵ��
     *
     * @return ���ݿ��д�ŵ�����ԭʼ����ʵ��
     */
    public E[] getOriginBills() {
        return this.originVOs;
    }

    @SuppressWarnings("unchecked")
    private void initInserted(E[] bills) {
        int size = bills.length;
        E[] vos = (E[]) Constructor.construct(bills[0].getClass(), size);
        for (int i = 0; i < size; i++) {
            vos[i] = (E) bills[i].clone();
        }
        this.originVOs = vos;
        this.clientVOs = vos;
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    private void initUpdateed(E[] bills) {
        VOConcurrentTool tool = new VOConcurrentTool();
        TimeLog.logStart();
        tool.lock(bills);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0077")/*@res "������ͷ����������"*/); /* -=notranslate=- */

        TimeLog.logStart();
        String[] ids = new String[bills.length];
        int length = bills.length;
        for (int i = 0; i < length; i++) {
            ids[i] = bills[i].getPrimaryKey();
        }
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0071")/*@res "��ȡ��������"*/); /* -=notranslate=- */

        TimeLog.logStart();
        VOQuery query = new VOQuery(bills[0].getClass());
        this.originVOs = (E[]) query.query(ids);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0072")/*@res "��ѯԭʼ����VO"*/); /* -=notranslate=- */

        TimeLog.logStart();
        length = this.originVOs.length;
        E[] vos = (E[]) Constructor.construct(bills[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            vos[i] = (E) this.originVOs[i].clone();
        }
        VOTool combineClient = new VOTool();
        for (int i = 0; i < length; i++) {
            combineClient.combine(vos[i], bills[i]);
        }
        this.clientVOs = vos;
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0073")/*@res "ǰ̨����VO��������"*/); /* -=notranslate=- */

        TimeLog.logStart();
        tool.checkTS(bills, this.originVOs);
        TimeLog.info(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0074")/*@res "���ʱ���"*/); /* -=notranslate=- */
    }
}