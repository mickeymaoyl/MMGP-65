package nc.bs.pub.action;


import nc.bs.mmgp.pf.action.N_MMGP_WRITE;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleorderdemo.AggSaleOrder;

/**
 * 
 * <b> �ɹ��嵥���涯���ű� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since 
 * �������� May 27, 2013
 * @author liwsh
 */
public class N_TR11_WRITE extends N_MMGP_WRITE<AggSaleOrder> {

	@Override
	protected IPluginPoint getInsertPoint() {
		return SalePoint.INSERT;
	}

	@Override
	protected IPluginPoint getUpdatePoint() {
		return SalePoint.UPDATE;
	}

	@Override
	protected Class<IMmgpPfOperateService> getOperateServiceClass() {
		return IMmgpPfOperateService.class;
	}

}
