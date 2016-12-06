package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.bs.businessevent.IBusinessEvent;
import nc.itf.bd.pub.IBDMetaDataIDConst;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * �ͻ���������
 * </p>
 * 
 * @since �������� Nov 18, 2013
 * @author wangweir
 */
public abstract class CustomerAssignRefChecker extends EMAbstractAssignRefChecker {

    /*
     * (non-Javadoc)
     * @see
     * nc.impl.embd.pub.assigncheck.EMAbstractAssignRefCheck#isEventSourceIDMatch(nc.bs.businessevent.IBusinessEvent)
     */
    @Override
    protected boolean isEventSourceIDMatch(IBusinessEvent e) {
        return IBDMetaDataIDConst.CUSTORG.equals(e.getSourceID());
    }

}
