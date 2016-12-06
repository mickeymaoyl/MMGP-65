package nc.bs.pub.action;

import nc.bs.mmgp.pf.action.N_MMGP_UNAPPROVE;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleorderdemo.AggSaleOrder;


/**
 * <b> 采购清单弃审动作脚本 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 27, 2013
 * @author liwsh
 */
public class N_TR11_UNAPPROVE extends N_MMGP_UNAPPROVE<AggSaleOrder> {

    @Override
    protected Class<IMmgpPfOperateService> getOperateServiceClass() {
        return IMmgpPfOperateService.class;
    }

    @Override
    protected IPluginPoint getScriptPoint() {
        return SalePoint.UNAPPROVE;
    }




}
