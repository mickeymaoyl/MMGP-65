package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.bs.businessevent.IBusinessEvent;
import nc.itf.bd.pub.IBDMetaDataIDConst;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 客户检查抽象类
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
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
