package nc.ui.mmgp.uif2.actions.print;

import java.util.ArrayList;
import java.util.List;

import nc.ui.mmgp.uif2.scale.MMGPScaleBean;
import nc.ui.mmgp.uif2.scale.NumScaleUtil;
import nc.ui.mmgp.uif2.scale.processor.MMGPBillVOScaleProcessor;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class PrintNumScaleUtils {
	public static void setPrintScale(Object[] objs, String pk_group,
			List<? extends MMGPScaleBean> scaleBeans) {
		setPrintScale(objs, pk_group, scaleBeans, false);
	}
	public static void setPrintScale(Object[] objs, String pk_group,
			List<? extends MMGPScaleBean> scaleBeans, boolean isSingbleBody) {
		if(MMCollectionUtil.isEmpty(scaleBeans)){
			return;
		}
		AggregatedValueObject[] aggVOs = null;
		if (objs[0] instanceof AggregatedValueObject) {
			List<AggregatedValueObject> vos = new ArrayList<AggregatedValueObject>();
			for (Object obj : objs) {
				vos.add((AggregatedValueObject) obj);
			}
			aggVOs = vos.toArray(new AggregatedValueObject[0]);
		} else if (objs[0] instanceof CircularlyAccessibleValueObject) {
			List<HYBillVO> vos = new ArrayList<HYBillVO>();
			if (isSingbleBody) {
				List<CircularlyAccessibleValueObject> children = new ArrayList<CircularlyAccessibleValueObject>();
				for (Object obj : objs) {
					children.add((CircularlyAccessibleValueObject) obj);
				}
				HYBillVO billVO = new HYBillVO();
				billVO.setChildrenVO(children
						.toArray(new CircularlyAccessibleValueObject[0]));
				vos.add(billVO);
			} else {
				for (Object obj : objs) {
					HYBillVO billVO = new HYBillVO();
					billVO.setParentVO((CircularlyAccessibleValueObject) obj);
					vos.add(billVO);
				}
			}
			aggVOs = vos.toArray(new AggregatedValueObject[0]);
		} else {
			throw new IllegalArgumentException("unknow type:"
					+ objs[0].getClass() + ",need aggVO");
		}
		setAggVoScale(pk_group, scaleBeans, aggVOs);
	}

	private static void setAggVoScale(String pk_group,
			List<? extends MMGPScaleBean> scaleBeans,
			AggregatedValueObject[] aggVOs) {
		MMGPBillVOScaleProcessor voScaleProcessor = new MMGPBillVOScaleProcessor(
				pk_group, aggVOs);
		NumScaleUtil.setScale(voScaleProcessor,
				scaleBeans.toArray(new MMGPScaleBean[0]));
	}
}
