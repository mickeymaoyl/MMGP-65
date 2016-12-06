package nc.vo.train.saleorderdemo;

import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.train.saleorderdemo.SaleOrder")

public class AggSaleOrder extends MMGPAbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSaleOrderMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SaleOrder getParentVO(){
	  	return (SaleOrder)this.getParent();
	  }

	@Override
	protected String getMetaFullName() {
		// TODO 自动生成的方法存根
		return "train.SaleOrder";
	}
	  
}