package nc.impl.mmgp.bd.refcheck.assigncheck;

import nc.bs.businessevent.IBusinessEvent;
import nc.itf.bd.pub.IBDMetaDataIDConst;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 物料引用校验
 * </p>
 * 
 * @since 创建日期 Nov 18, 2013
 * @author wangweir
 */
public abstract class MaterialAssignRefChecker extends EMAbstractAssignRefChecker {
    /**
     * 事件源ID是否需要检查
     * 
     * @param e
     * @return
     */
    protected boolean isEventSourceIDMatch(IBusinessEvent e) {
        return IBDMetaDataIDConst.MARORG.equals(e.getSourceID());
    }
}
