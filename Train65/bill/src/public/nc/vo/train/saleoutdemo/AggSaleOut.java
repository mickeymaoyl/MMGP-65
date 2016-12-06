package nc.vo.train.saleoutdemo;

import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.train.saleoutdemo.SaleOut")

public class AggSaleOut extends MMGPAbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSaleOutMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SaleOut getParentVO(){
	  	return (SaleOut)this.getParent();
	  }

	@Override
	protected String getMetaFullName() {
		// TODO 自动生成的方法存根
		return "train.SaleOut";
	}
	  
}