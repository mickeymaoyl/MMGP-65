package nc.ui.mmgp.uif2.lazilyload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnQueryService;
import nc.ui.pubapp.uif2app.lazilyload.IBillLazilyLoader;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.lazilyload.BillLazilyLoaderVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * MMGP¿¡º”‘ÿƒ¨»œ≤È—Ø
 * 
 */
public class MMGPBillLazilyLoader implements IBillLazilyLoader {

	@SuppressWarnings("unchecked")
	@Override
	public void loadChildrenByClass(
			Map<IBill, List<Class<? extends ISuperVO>>> needLoadChildrenMap)
			throws Exception {
		try {
			Map<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>> map = new HashMap<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>>();
			for (Entry<IBill, List<Class<? extends ISuperVO>>> entry : needLoadChildrenMap
					.entrySet()) {
				String pk = entry.getKey().getParent().getPrimaryKey();
				String ts = entry.getKey().getParent().getAttributeValue("ts")
						.toString();
				Class<? extends IBill> billClass = entry.getKey().getClass();
				Class<SuperVO> parentClass = (Class<SuperVO>) entry.getKey()
						.getParent().getClass();

				BillLazilyLoaderVO loaderVO = new BillLazilyLoaderVO();
				loaderVO.setPk(pk);
				loaderVO.setTs(ts);
				loaderVO.setBillClass(billClass);
				loaderVO.setParentClass(parentClass);

				map.put(loaderVO, entry.getValue());
			}
			Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> resultMap = getResultMap(map);

			for (Entry<IBill, List<Class<? extends ISuperVO>>> entry : needLoadChildrenMap
					.entrySet()) {
				for (Entry<String, Map<Class<? extends ISuperVO>, SuperVO[]>> resultEntry : resultMap
						.entrySet()) {
					IBill bill = entry.getKey();
					if (bill.getPrimaryKey().equals(resultEntry.getKey())) {
						fillBill(bill, entry.getValue(), resultEntry.getValue());
					}
				}
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
	}

	protected Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> getResultMap(
			Map<BillLazilyLoaderVO, List<Class<? extends ISuperVO>>> map)
			throws BusinessException {
		IMMGPCmnQueryService service = NCLocator.getInstance().lookup(
				IMMGPCmnQueryService.class);
		Map<String, Map<Class<? extends ISuperVO>, SuperVO[]>> resultMap = service
				.cmnQueryChildrenByParent(map);
		return resultMap;
	}

	protected void fillBill(IBill bill,
			List<Class<? extends ISuperVO>> needLoadChildrenList,
			Map<Class<? extends ISuperVO>, SuperVO[]> resultMap) {
		for (Class<? extends ISuperVO> childrenClass : needLoadChildrenList) {
			SuperVO[] itemVOs = resultMap.get(childrenClass);
			bill.setChildren(childrenClass, itemVOs);
		}
	}

}
