package nc.vo.train.billtest;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggQlfxMeta extends AbstractBillMeta{
	
	public AggQlfxMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.train.billtest.Qlfx.class);
		this.addChildren(nc.vo.train.billtest.QlxfBodyVO.class);
	}
}