package nc.bs.pub.action;


import nc.bs.mmgp.pf.action.N_MMGP_APPROVE;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleorderdemo.AggSaleOrder;
/**
 * 
 * <b> 采购清单审核动作脚本 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since 
 * 创建日期 May 27, 2013
 * @author liwsh
 */
public class N_TR11_APPROVE extends N_MMGP_APPROVE<AggSaleOrder> {

	@Override
	protected Class<IMmgpPfOperateService> getOperateServiceClass() {
		return IMmgpPfOperateService.class;
	}

	@Override
	protected IPluginPoint getScriptPoint() {
		return SalePoint.APPROVE;
	}

}
