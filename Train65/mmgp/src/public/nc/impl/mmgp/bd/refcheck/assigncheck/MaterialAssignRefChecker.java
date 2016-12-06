package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.bs.businessevent.IBusinessEvent;
import nc.itf.bd.pub.IBDMetaDataIDConst;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��������У��
 * </p>
 * 
 * @since �������� Nov 18, 2013
 * @author wangweir
 */
public abstract class MaterialAssignRefChecker extends EMAbstractAssignRefChecker {
    /**
     * �¼�ԴID�Ƿ���Ҫ���
     * 
     * @param e
     * @return
     */
    protected boolean isEventSourceIDMatch(IBusinessEvent e) {
        return IBDMetaDataIDConst.MARORG.equals(e.getSourceID());
    }
}
