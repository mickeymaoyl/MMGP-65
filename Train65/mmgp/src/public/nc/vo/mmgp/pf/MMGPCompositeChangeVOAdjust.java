package nc.vo.mmgp.pf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 组合VO交换处理后类
 * </p>
 * 
 * @since 创建日期 Oct 29, 2013
 * @author wangweir
 */
public abstract class MMGPCompositeChangeVOAdjust implements IChangeVOAdjust {

    private List<IChangeVOAdjust> changeVOAdjustList;

    /**
     * 
     */
    public MMGPCompositeChangeVOAdjust() {
        init();
    }

    /**
     * 
     */
    protected void init() {
        this.registerChangeVOAdjust(this.getChangeVOAdjusts());
    }

    /**
     * 子类提供组合的VO交换规则处理类，主要为了支持多个VO交换规则处理类
     * 
     * @return
     */
    public abstract IChangeVOAdjust[] getChangeVOAdjusts();

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#adjustBeforeChange(nc.vo.pub.AggregatedValueObject,
     * nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject adjustBeforeChange(AggregatedValueObject srcVO,
                                                    ChangeVOAdjustContext adjustContext) throws BusinessException {
        AggregatedValueObject currentSrcVO = srcVO;
        for (IChangeVOAdjust changeVOAdjust : this.getChangeVOAdjustList()) {
            currentSrcVO = changeVOAdjust.adjustBeforeChange(currentSrcVO, adjustContext);
        }
        return currentSrcVO;
    }

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#adjustAfterChange(nc.vo.pub.AggregatedValueObject,
     * nc.vo.pub.AggregatedValueObject, nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject adjustAfterChange(AggregatedValueObject srcVO,
                                                   AggregatedValueObject destVO,
                                                   ChangeVOAdjustContext adjustContext) throws BusinessException {
        AggregatedValueObject currentSrcVO = srcVO;
        AggregatedValueObject currentDestVO = destVO;
        for (IChangeVOAdjust changeVOAdjust : this.getChangeVOAdjustList()) {
            currentDestVO = changeVOAdjust.adjustAfterChange(currentSrcVO, currentDestVO, adjustContext);
        }
        return currentDestVO;
    }

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#batchAdjustBeforeChange(nc.vo.pub.AggregatedValueObject[],
     * nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject[] batchAdjustBeforeChange(AggregatedValueObject[] srcVOs,
                                                           ChangeVOAdjustContext adjustContext)
            throws BusinessException {
        AggregatedValueObject[] currentSrcVOs = srcVOs;
        for (IChangeVOAdjust changeVOAdjust : this.getChangeVOAdjustList()) {
            currentSrcVOs = changeVOAdjust.batchAdjustBeforeChange(currentSrcVOs, adjustContext);
        }
        return currentSrcVOs;
    }

    /*
     * (non-Javadoc)
     * @see nc.vo.pf.change.IChangeVOAdjust#batchAdjustAfterChange(nc.vo.pub.AggregatedValueObject[],
     * nc.vo.pub.AggregatedValueObject[], nc.vo.pf.change.ChangeVOAdjustContext)
     */
    @Override
    public AggregatedValueObject[] batchAdjustAfterChange(AggregatedValueObject[] srcVOs,
                                                          AggregatedValueObject[] destVOs,
                                                          ChangeVOAdjustContext adjustContext)
            throws BusinessException {
        AggregatedValueObject[] currentSrcVOs = srcVOs;
        AggregatedValueObject[] currentDestVOs = destVOs;
        for (IChangeVOAdjust changeVOAdjust : this.getChangeVOAdjustList()) {
            currentDestVOs = changeVOAdjust.batchAdjustAfterChange(currentSrcVOs, currentDestVOs, adjustContext);
        }
        return currentDestVOs;
    }

    /**
     * @return the changeVOAdjustList
     */
    public List<IChangeVOAdjust> getChangeVOAdjustList() {
        if (this.changeVOAdjustList == null) {
            this.changeVOAdjustList = new ArrayList<IChangeVOAdjust>();
        }
        return changeVOAdjustList;
    }

    /**
     * 注册VO交换规则
     * 
     * @param voAdjusts
     */
    protected void registerChangeVOAdjust(IChangeVOAdjust[] voAdjusts) {
        if (MMArrayUtil.isEmpty(voAdjusts)) {
            return;
        }

        this.getChangeVOAdjustList().addAll(Arrays.asList(voAdjusts));
    }

    /**
     * 注册VO交换规则
     * 
     * @param voAdjust
     */
    protected void registerChangeVOAdjust(IChangeVOAdjust voAdjust) {
        if (voAdjust == null) {
            return;
        }

        this.registerChangeVOAdjust(new IChangeVOAdjust[]{voAdjust });
    }
}
