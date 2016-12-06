package nc.ui.mmgp.uif2.model;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @Description: TODO
    <p>
          œÍœ∏π¶ƒ‹√Ë ˆ
    </p>
 * @data:2014-5-15…œŒÁ10:09:07
 * @author: tangxya
 */
public class MMGPTreeTableModel extends MMGPBillManageModel {
	 private Comparator<CircularlyAccessibleValueObject> comparator;
	    
	    public Comparator<CircularlyAccessibleValueObject> getComparator() {
	        return comparator;
	    }

	    public void setComparator(Comparator<CircularlyAccessibleValueObject> comparator) {
	        this.comparator = comparator;
	    }

	    @SuppressWarnings("rawtypes")
	    @Override
	    public List getData() {
	        
	        List modelList = super.getData();
	       
	        for(Object obj : modelList){
	            
	            AggregatedValueObject vo = (AggregatedValueObject)obj;
	            CircularlyAccessibleValueObject[]  bodyVos = vo.getChildrenVO();
	            if(bodyVos != null){
	                Arrays.sort(bodyVos, comparator);
	                vo.setChildrenVO(bodyVos);
	            }
	           
	        }
	         
	         return modelList;
	    }
	    
	    @Override
	    public Object getSelectedData() {
	        AggregatedValueObject vo = (AggregatedValueObject)super.getSelectedData();
	        if(vo!=null){
	            CircularlyAccessibleValueObject[]  bodyVos = vo.getChildrenVO();
	            if(bodyVos != null){
	                Arrays.sort(bodyVos, comparator);
	                vo.setChildrenVO(bodyVos);
	            }
	        }
	       
	        return vo;
	    }

}
