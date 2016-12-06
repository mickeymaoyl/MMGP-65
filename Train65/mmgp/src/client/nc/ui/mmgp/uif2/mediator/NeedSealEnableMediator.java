package nc.ui.mmgp.uif2.mediator;

import nc.ui.mmgp.uif2.model.INeedSeal;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ֧������/ͣ�� ��DataManager�����ࡣ��ʵ����INeedSeal�ӿڵ�DataManagerע�뼴�ɡ���Ҫ����/ͣ�ù��ܵĽڵ���Ӵ�Mediator
 * </p>
 * 
 * @since  ��������:Apr 2, 2013
 * @author wangweir
 */
public class NeedSealEnableMediator {

    private INeedSeal needSeal;

    /**
     * @return the needSeal
     */
    public INeedSeal getNeedSeal() {
        return needSeal;
    }

    /**
     * @param needSeal
     *        the needSeal to set
     */
    public void setNeedSeal(INeedSeal needSeal) {
        this.needSeal = needSeal;
        this.needSeal.setNeedSeal(true);
    }

}
