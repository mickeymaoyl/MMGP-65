package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.bs.businessevent.IBusinessEvent;
import nc.itf.bd.pub.IBDMetaDataIDConst;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��Ӧ�̼�������
 * </p>
 * 
 * @since �������� Nov 18, 2013
 * @author wangweir
 */
public abstract class VendorAssignRefChecker extends EMAbstractAssignRefChecker {

    @Override
    protected boolean isEventSourceIDMatch(IBusinessEvent e) {
        return IBDMetaDataIDConst.SUPORG.equals(e.getSourceID());
    }

}
