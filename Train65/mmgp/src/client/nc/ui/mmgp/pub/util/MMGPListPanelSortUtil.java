package nc.ui.mmgp.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillScrollPane;
/**
 * 
 * <b> 列表界面排序工具类 </b>
 * <p>
 *     可以对列表界面进行表头，表体排序
 * </p>
 * <p>
 * 
 * 
 * </p>
 * @since 
 * 创建日期 Jun 6, 2013
 * @author liwsh
 */
public class MMGPListPanelSortUtil {

	// 列表界面
	private BillListPanel listPanel;

	// 表体排序字段 key->页签名 value->排序字段
	private Map<String, List<SortItem>> bodySortItems = new HashMap<String, List<SortItem>>();

	// 表头排序字段
	private List<SortItem> headSortItems = new ArrayList<SortItem>();

	/**
	 * 构造函数
	 * 
	 * @param listPanel
	 */
	public MMGPListPanelSortUtil(BillListPanel listPanel) {
		this.listPanel = listPanel;
	}

	/**
	 * 增加表体待排序字段，默认升序
	 * 
	 * @param key
	 *            字段
	 * @return 自身
	 */
	public MMGPListPanelSortUtil addBodySortItem(String key) {
		return this.addBodySortItem(key, true);
	}
	
	/**
	 * 增加表体待排序字段
	 * 
	 * @param key
	 *            字段
	 * @param asc
	 *            是否升序
	 * @return 返回自身
	 */
	public MMGPListPanelSortUtil addBodySortItem(String key, boolean asc) {
		return this.addBodySortItem(key, asc, ((BillScrollPane) this.listPanel
				.getBodyTabbedPane().getSelectedScrollPane()).getTableCode());
	}
	
	/**
	 * 增加表体待排序字段
	 * 
	 * @param key
	 *            排序字段
	 * @param asc
	 *            是否升序
	 * @param tableCode
	 *            页签
	 * @return 自身
	 */
	public MMGPListPanelSortUtil addBodySortItem(String key, boolean asc,
			String tableCode) {
		// 查找排序字段的序号
		int index = this.listPanel.getBodyBillModel(tableCode).getBodyColByKey(
				key);
		
        if (index == -1) {
            return this;
        }
		
		SortItem item = new SortItem(index, asc);

		// 查找需要排序的页签
		if (!this.bodySortItems.containsKey(tableCode)) {
			this.bodySortItems.put(tableCode, new ArrayList<SortItem>());
		}

		this.bodySortItems.get(tableCode).add(item);
		return this;
	}

	
	/**
	 * 添加表头排序字段，默认升序
	 * 
	 * @param key
	 *            排序字段
	 * @return 自身
	 */
	public MMGPListPanelSortUtil addHeadSortItem(String key) {
		return this.addHeadSortItem(key, true);
	}

	
	/**
	 * 添加表头排序字段
	 * 
	 * @param key
	 *            字段
	 * @param asc
	 *            是否升序
	 * @return 自身
	 */
	public MMGPListPanelSortUtil addHeadSortItem(String key, boolean asc) {
		// 查找排序字段的序号
		int index = this.listPanel.getHeadBillModel().getBodyColByKey(key);
		SortItem item = new SortItem(index, asc);

		this.headSortItems.add(item);
		return this;
	}

	
	/**
	 * 排序表头
	 */
	public void sortHead() {
		this.listPanel.getHeadBillModel().sortByColumns(headSortItems);
	}
	
	
	/**
	 * 表体排序
	 */
	public void sortBody() {
		// 表体排序（多页签）
		for (String tableCode : this.bodySortItems.keySet()) {
			List<SortItem> sortItems = this.bodySortItems.get(tableCode);
			this.listPanel.getBodyBillModel(tableCode).sortByColumns(sortItems);
		}
	}

	public BillListPanel getListPanel() {
		return listPanel;
	}

	public void setListPanel(BillListPanel listPanel) {
		this.listPanel = listPanel;
	}
}
