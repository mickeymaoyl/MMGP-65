package nc.bs.pub.action;

import nc.bs.mmgp.pf.action.N_MMGP_SAVE;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleorderdemo.AggSaleOrder;


/**
 * 
 * <b> �ɹ��嵥�ύ�����ű� </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since 
 * �������� May 27, 2013
 * @author liwsh
 */
public class N_TR11_SAVE extends N_MMGP_SAVE<AggSaleOrder> {

	@Override
	protected Class<IMmgpPfOperateService> getOperateServiceClass() {
		return IMmgpPfOperateService.class;
	}

	@Override
	protected IPluginPoint getScriptPoint() {
		return SalePoint.SEND_APPROVE;
	}

}
