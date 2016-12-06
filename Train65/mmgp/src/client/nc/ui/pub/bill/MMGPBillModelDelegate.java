package nc.ui.pub.bill;

import nc.md.data.access.DASFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBusinessEntity;
import nc.md.model.access.javamap.BeanStyleEnum;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.bill.BillTabVO;

/**
 * 为了解决billmodel对setBodyObjectByMetaData的访问限制
 * 
 * @author wangrra
 * 
 */

public class MMGPBillModelDelegate {
	public static void setBodyObjectByMetaDataWithNCObject(BillData billData,
			BillTabVO tabVO, NCObject[] bodys) {

		if (billData == null) {
			return;
		}

		String tabCode = null;

		if (tabVO != null) {
			tabCode = tabVO.getTabcode();
		}
		BillModel billModel = billData.getBillModel(tabCode);

		billModel.setBodyObjectByMetaData(bodys);
	}

	public static void setBodyObjectByMetaData(BillData billData,
			BillTabVO tabVO, Object o) {

		if (billData == null) {
			return;
		}

		IBusinessEntity be = billData.getBillTempletVO().getHeadVO()
				.getBillMetaDataBusinessEntity();

		NCObject ncobject = null;

		if (be.getBeanStyle().getStyle() == BeanStyleEnum.AGGVO_HEAD)
			ncobject = DASFacade.newInstanceWithContainedObject(be, o);
		else if (be.getBeanStyle().getStyle() == BeanStyleEnum.NCVO
				|| be.getBeanStyle().getStyle() == BeanStyleEnum.POJO) {
			if (o instanceof AggregatedValueObject) {
				o = ((AggregatedValueObject) o).getParentVO();
				ncobject = DASFacade.newInstanceWithContainedObject(be, o);
			} else {
				ncobject = DASFacade.newInstanceWithContainedObject(be, o);
			}
		}

		String tabCode = null;

		if (tabVO != null) {
			tabCode = tabVO.getTabcode();
		}
		BillModel billModel = billData.getBillModel(tabCode);

		NCObject[] ncos = (NCObject[]) ncobject.getAttributeValue(tabVO
				.getMetadatapath());

		billModel.setBodyObjectByMetaData(ncos);
	}
}
