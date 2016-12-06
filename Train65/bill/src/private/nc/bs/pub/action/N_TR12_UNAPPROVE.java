package nc.bs.pub.action;

import nc.bs.mmgp.pf.action.N_MMGP_UNAPPROVE;
import nc.bs.pub.point.SaleOutPoint;
import nc.bs.pub.point.SalePoint;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.mmgp.IMmgpPfOperateService;
import nc.vo.train.saleoutdemo.AggSaleOut;


/**
 * <b> �ɹ��嵥�������ű� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� May 27, 2013
 * @author liwsh
 */
public class N_TR12_UNAPPROVE extends N_MMGP_UNAPPROVE<AggSaleOut> {

    @Override
    protected Class<IMmgpPfOperateService> getOperateServiceClass() {
        return IMmgpPfOperateService.class;
    }

    @Override
    protected IPluginPoint getScriptPoint() {
        return SaleOutPoint.UNAPPROVE;
    }




}
