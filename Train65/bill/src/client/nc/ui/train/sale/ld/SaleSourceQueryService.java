/**
 * 
 */
package nc.ui.train.sale.ld;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.mmgp.uif2.IMMGPCmnQueryService;
import nc.ui.pubapp.uif2app.query2.model.IRefQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.train.saleorderdemo.AggSaleOrder;

/**
 * @author maoyulong
 *
 */
public class SaleSourceQueryService implements IRefQueryService {

	/**
	 * 
	 */
	public SaleSourceQueryService() {
		// TODO 自动生成的构造函数存根
	}

	/* （非 Javadoc）
	 * @see nc.ui.pubapp.uif2app.model.IQueryService#queryByWhereSql(java.lang.String)
	 */
	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.ui.pubapp.uif2app.query2.model.IRefQueryService#queryByQueryScheme(nc.ui.querytemplate.querytree.IQueryScheme)
	 */
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		// TODO 自动生成的方法存根
		IMMGPCmnQueryService  query =(IMMGPCmnQueryService) NCLocator.getInstance().lookup(IMMGPCmnQueryService.class.getName());
		AggSaleOrder [] vos =(AggSaleOrder[]) query.cmnQueryDatasByCondition(AggSaleOrder.class, queryScheme.getWhereSQLOnly());
		return vos;
	}

}
