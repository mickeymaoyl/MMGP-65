package nc.bs.pub.action;

import nc.bs.mmgp.pf.action.N_MMGP_DELETE;
import nc.bs.pub.point.SaleOutPoint;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleorderdemo.AggSaleOrder;
import nc.vo.train.saleoutdemo.AggSaleOut;
/**
 * 
 * <b> 采购清单删除动作脚本 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since 
 * 创建日期 May 27, 2013
 * @author liwsh
 */
public class N_TR12_DELETE extends N_MMGP_DELETE<AggSaleOut> {

	@Override
	protected Class<IMmgpPfOperateService> getOperateServiceClass() {
		return IMmgpPfOperateService.class;
	}

	@Override
	protected IPluginPoint getScriptPoint() {
		return SaleOutPoint.DELETE;
	}

}
