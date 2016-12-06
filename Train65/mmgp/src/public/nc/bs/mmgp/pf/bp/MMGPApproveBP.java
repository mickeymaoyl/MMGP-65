package nc.bs.mmgp.pf.bp;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pub.BusinessException;

/**
 * <b>审批BP</b>
 * <p>
 * 实现审批操作的BP类，目前只是更新保存VO（单据状态在脚本基类中修改）.
 * </p>
 * <p>
 * 创建日期:2013-5-9
 * </p>
 * 
 * @param <T>
 *        VO类型
 * @since NC V6.3
 * @author zhumh
 */
public class MMGPApproveBP<T extends MMGPAbstractBill> extends MMGPAbstractPfBP<T> {

    /**
     * 构造方法.
     * 
     * @param point
     *        扩展点
     */
    public MMGPApproveBP(IPluginPoint point) {
        super(point);
    }

    /**
     * 审批操作实现.
     * 
     * @param clientFullVOs
     *        客户端全VO
     * @param originBills
     *        原始VO
     * @return 处理后VO
     * @throws BusinessException
     *         the business exception
     */
    public T[] approve(T[] clientFullVOs,
                       T[] originBills) throws BusinessException {

        return super.doAction(clientFullVOs, originBills);
    }

    /*
     * (non-Javadoc)
     * @see
     * nc.bs.mmgp.pf.bp.MMGPAbstractPfBP#addBeforeRule(nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser,
     * T[], T[])
     */
    @Override
    protected void addBeforeRule(T[] clientFullVOs,
                                 T[] originBills) {
        super.addBeforeRule(clientFullVOs, originBills);
    }

}
