package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.bs.businessevent.IBusinessEvent;
import nc.itf.bd.pub.IBDMetaDataIDConst;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 供应商检查抽象类
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
 * @author wangweir
 */
public abstract class VendorAssignRefChecker extends EMAbstractAssignRefChecker {

    @Override
    protected boolean isEventSourceIDMatch(IBusinessEvent e) {
        return IBDMetaDataIDConst.SUPORG.equals(e.getSourceID());
    }

}
