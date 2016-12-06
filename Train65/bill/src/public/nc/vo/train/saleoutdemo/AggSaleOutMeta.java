package nc.vo.train.saleoutdemo;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSaleOutMeta extends AbstractBillMeta{
	
	public AggSaleOutMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.train.saleoutdemo.SaleOut.class);
		this.addChildren(nc.vo.train.saleoutdemo.SaleOutBody.class);
	}
}