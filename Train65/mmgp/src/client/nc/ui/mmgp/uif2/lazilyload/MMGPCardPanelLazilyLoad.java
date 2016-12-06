package nc.ui.mmgp.uif2.lazilyload;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.bill.BillTabVO;

public class MMGPCardPanelLazilyLoad extends CardPanelLazilyLoad {
	 @Override
	  public List<Class<? extends ISuperVO>> getCurrentChildren() {
	    List<Class<? extends ISuperVO>> classList =
	        new ArrayList<Class<? extends ISuperVO>>();
	    
	    if(!this.getBillform().isShowing()) {
	      return classList;
	    }

	    String tableCode =
	        this.getBillform().getBillCardPanel().getCurrentBodyTableCode();
	    if(MMStringUtil.isEmpty(tableCode)){
	    	return classList;
	    }
	    BillTabVO tabVO =
	        this.getBillform().getBillCardPanel().getBillData()
	            .getTabVO(IBillItem.BODY, tableCode);
	    if(tabVO==null){
	    	return classList;
	    }
	    return super.getCurrentChildren();
	 }
}
