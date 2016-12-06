package nc.bs.pub.action;


import nc.bs.mmgp.pf.action.N_MMGP_UNSAVE;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleorderdemo.AggSaleOrder;

/**
 * 
 * <b> �ɹ��嵥�ջض����ű�</b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since 
 * �������� May 27, 2013
 * @author liwsh
 */
public class N_TR11_UNSAVE extends N_MMGP_UNSAVE<AggSaleOrder> {

	@Override
	protected Class<IMmgpPfOperateService> getOperateServiceClass() {
		return IMmgpPfOperateService.class;
	}

	@Override
	protected IPluginPoint getScriptPoint() {
		return SalePoint.UNSEND_APPROVE;
	}

}
