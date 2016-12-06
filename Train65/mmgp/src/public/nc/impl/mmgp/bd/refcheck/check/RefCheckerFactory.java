package nc.impl.mmgp.bd.refcheck.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IEventType;
import nc.impl.mmgp.bd.refcheck.FileRefedInfo;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.fi.MaterialFiVO;
import nc.vo.bd.material.marasstframe.MarAsstFrameVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.material.pu.MaterialPuVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 22, 2013
 * @author wangweir
 */
public class RefCheckerFactory {

    private Map<String, IRefChecker> refChecker;

    public RefCheckerFactory() {
        this.refChecker = new HashMap<String, IRefChecker>();
    }

    public IRefChecker getRefCheck(IKeyBuilder keyBuilder,
                                   String eventType,
                                   String voClass) {
        IRefChecker checker = refChecker.get(keyBuilder.buildKey());
        if (checker != null) {
            return checker;
        }

        if (eventType.equals(IEventType.TYPE_DELETE_AFTER) || eventType.equals(IEventType.TYPE_CANCELASSIGN_AFTER)) {
            return new DefaultRefCheckerForDistrOrg();
        }

        if (isAddOrUpdate(eventType) && (isFiMarVO(voClass) || isStockMarVO(voClass) || isPuMarVO(voClass))) {
            return new DefualRefCheckerForAddOrUpdate();
        }

        if (isAddOrUpdate(eventType) && MarAsstFrameVO.class.getName().equals(voClass)) {
            return new MarAsstFrameVOChecher();
        }

        if (isAddOrUpdate(eventType)
            && (isStordocVO(voClass) || isCustomerVO(voClass) || isSupplierVO(voClass) || isMeasdocVO(voClass))) {
            // 仓库、客户、供应商、计量单位
            return new DefaultPkValueRefCheckerForAddOrUpdate();
        }

        if (isAddOrUpdate(eventType) && (isMaterialVO(voClass))) {
            // 不关注其他版本信息, 即：只关注当前版本，若当前版本的档案未被引用，其他版本被引用，则仍可修改
            // 关注其他版本信息, 即：只要有一个版本被引用，该档案就不能被修改
            return new MaterialVOAddOrUpdateRefChecker();
        }

        if (isBatchUpdateBefore(eventType) && (isMaterialVO(voClass) || isMaterialStockVO(voClass))) {
            return new MarBatchUpdateChecker();
        }

        return new RefCheckerFactory.NullRefChecker();
    }

    /**
     * 注册自定义的RefCheck
     * 
     * @param keyBuilder
     * @param checker
     */
    public void registerRefCheck(IKeyBuilder keyBuilder,
                                 IRefChecker checker) {
        this.refChecker.put(keyBuilder.buildKey(), checker);
    }

    private static class NullRefChecker implements IRefChecker {

        /*
         * (non-Javadoc)
         * @see nc.impl.mmgp.bd.refcheck.check.IRefChecker#check(nc.bs.businessevent.IBusinessEvent, java.util.List,
         * nc.vo.pub.SuperVO, nc.vo.pub.SuperVO)
         */
        @Override
        public void check(IBusinessEvent event,
                          List<FileRefedInfo> refedBaseInfos,
                          SuperVO oldValue,
                          SuperVO newValue) throws BusinessException {
            return;
        }
    }

    protected boolean isMaterialStockVO(String voClass) {
        return MaterialStockVO.class.getName().equals(voClass);
    }

    protected boolean isMaterialVO(String voClass) {
        return MaterialVO.class.getName().equals(voClass);
    }

    protected boolean isBatchUpdateBefore(String eventType) {
        return IEventType.TYPE_BATCHUPDATE_CHECK.equals(eventType);
    }

    protected boolean isMeasdocVO(String voClass) {
        return MeasdocVO.class.getName().equals(voClass);
    }

    protected boolean isSupplierVO(String voClass) {
        return SupplierVO.class.getName().equals(voClass);
    }

    protected boolean isCustomerVO(String voClass) {
        return CustomerVO.class.getName().equals(voClass);
    }

    protected boolean isStordocVO(String voClass) {
        return StordocVO.class.getName().equals(voClass);
    }

    protected boolean isPuMarVO(String voClass) {
        return MaterialPuVO.class.getName().equals(voClass);
    }

    protected boolean isStockMarVO(String voClass) {
        return isMaterialStockVO(voClass);
    }

    protected boolean isFiMarVO(String voClass) {
        return MaterialFiVO.class.getName().equals(voClass);
    }

    protected boolean isAddOrUpdate(String eventType) {
        return eventType.equals(IEventType.TYPE_UPDATE_AFTER) || eventType.equals(IEventType.TYPE_INSERT_AFTER);
    }

}
