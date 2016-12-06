package nc.vo.mmgp.batch;

import nc.vo.bd.meta.IBDObject;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class MMGPBatchVOUtil {
	
	IBDObject bdInfo;
	
	public MMGPBatchVOUtil(){}
	
	public static SuperVO getMainSuperVO(Object obj){
		SuperVO vo = null;
		if (obj instanceof SuperVO) {
			vo = (SuperVO) obj;
		}
		if (obj instanceof AggregatedValueObject) {
			vo = (SuperVO) ((AggregatedValueObject) obj).getParentVO();
		}
		return vo;
	}
	
}
