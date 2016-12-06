package nc.vo.train.saleorderdemo;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSaleOrderMeta extends AbstractBillMeta{
	
	public AggSaleOrderMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.train.saleorderdemo.SaleOrder.class);
		this.addChildren(nc.vo.train.saleorderdemo.SaleOrderBody.class);
	}
}