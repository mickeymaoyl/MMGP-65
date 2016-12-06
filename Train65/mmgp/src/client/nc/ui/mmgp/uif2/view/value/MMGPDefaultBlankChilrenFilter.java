package nc.ui.mmgp.uif2.view.value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.view.value.IBlankChildrenFilter;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class MMGPDefaultBlankChilrenFilter implements IBlankChildrenFilter {

	/**
	 * 过滤空子表VO，获取属于空行的子VO索引
	 * 
	 * @param 需要过滤空行的cardpanel
	 *            ，待过滤空子表的VO
	 * @return 返回没JB用 不用返回，把传进来的obj直接改了就行
	 * 
	 * @see nc.ui.pubapp.uif2app.view.value.IBlankChildrenFilter#filter(nc.ui.pub.bill.BillCardPanel,
	 *      java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Integer[]> filter(BillCardPanel cardPanel, Object obj) {
		Map<String, Integer[]> nullRowMap = new HashMap<String, Integer[]>();
		if (!(obj instanceof IBill)) {
			return nullRowMap;
		}
		BillTabVO[] tabVos = cardPanel.getBillData().getAllTabVos();
		for (BillTabVO tabVO : tabVos) {
//			List<Integer> nullRowList = new ArrayList<Integer>();

			if (tabVO.getPos() != BillItem.BODY) {
				continue;
			}
			if (tabVO.getBasetab() != null) {
				BillTabVO tempVO = cardPanel.getBillData().getTabVO(
						IBillItem.BODY, tabVO.getBasetab());
				if (tempVO != null) {
					tabVO = tempVO;
				}
			}
			Class<? extends ISuperVO> childClz = null;
			try {

				childClz = (Class<? extends ISuperVO>) Class.forName(tabVO
						.getBillMetaDataBusinessEntity().getFullClassName());
			} catch (ClassNotFoundException e) {
				Logger.error(e.getMessage(), e);
				continue;
			}
			ISuperVO[] bodyVos = ((IBill) obj).getChildren(childClz);
			List<ISuperVO> newChildren = new ArrayList<ISuperVO>();
			if (bodyVos != null && bodyVos.length > 0) {
				List<Integer> deleteRows = new ArrayList<Integer>();
				for (int row = 0; row < bodyVos.length; row++) {
					boolean isRowNull = true;
					BillItem[] showItems = cardPanel.getBillData()
							.getBodyShowItems(tabVO.getTabcode());
					for (BillItem columnItem : showItems) {
						Object value = bodyVos[row]
								.getAttributeValue(columnItem.getKey());
						if (!(null == value || "".equals(value.toString()
								.trim()))) {
							isRowNull = false;
							break;
						}
					}
					if (isRowNull) {
						deleteRows.add(row);
//						nullRowList.add(Integer.valueOf(row));
						continue;
					}

					newChildren.add(bodyVos[row]);
				}
				((IBill) obj).setChildren(childClz, newChildren
						.toArray((ISuperVO[]) Array.newInstance(
								bodyVos[0].getClass(), 0)));

				// 还要把画面上的空白行业给删了
				int[] delRows = new int[deleteRows.size()];
				for (int i = 0; i < deleteRows.size(); i++) {
					delRows[i] = deleteRows.get(i);
				}
				
				/* May 22, 2013 wangweir 有需要删除的行时再调用删行。否则即使没有删行也会触发删行事件。nc.ui.pub.bill.BillScrollPane.delLine(int[])的Bug Begin */
                if (delRows != null && delRows.length != 0) {
                    BillScrollPane bsp = cardPanel.getBodyPanel(tabVO.getTabcode());
                    bsp.delLine(delRows);
                }
				/* May 22, 2013 wangweir End */

			}
		}
		return nullRowMap;
	}

}
