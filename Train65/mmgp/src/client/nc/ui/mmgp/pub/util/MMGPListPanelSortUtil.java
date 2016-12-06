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
 * <b> �б�������򹤾��� </b>
 * <p>
 *     ���Զ��б������б�ͷ����������
 * </p>
 * <p>
 * 
 * 
 * </p>
 * @since 
 * �������� Jun 6, 2013
 * @author liwsh
 */
public class MMGPListPanelSortUtil {

	// �б����
	private BillListPanel listPanel;

	// ���������ֶ� key->ҳǩ�� value->�����ֶ�
	private Map<String, List<SortItem>> bodySortItems = new HashMap<String, List<SortItem>>();

	// ��ͷ�����ֶ�
	private List<SortItem> headSortItems = new ArrayList<SortItem>();

	/**
	 * ���캯��
	 * 
	 * @param listPanel
	 */
	public MMGPListPanelSortUtil(BillListPanel listPanel) {
		this.listPanel = listPanel;
	}

	/**
	 * ���ӱ���������ֶΣ�Ĭ������
	 * 
	 * @param key
	 *            �ֶ�
	 * @return ����
	 */
	public MMGPListPanelSortUtil addBodySortItem(String key) {
		return this.addBodySortItem(key, true);
	}
	
	/**
	 * ���ӱ���������ֶ�
	 * 
	 * @param key
	 *            �ֶ�
	 * @param asc
	 *            �Ƿ�����
	 * @return ��������
	 */
	public MMGPListPanelSortUtil addBodySortItem(String key, boolean asc) {
		return this.addBodySortItem(key, asc, ((BillScrollPane) this.listPanel
				.getBodyTabbedPane().getSelectedScrollPane()).getTableCode());
	}
	
	/**
	 * ���ӱ���������ֶ�
	 * 
	 * @param key
	 *            �����ֶ�
	 * @param asc
	 *            �Ƿ�����
	 * @param tableCode
	 *            ҳǩ
	 * @return ����
	 */
	public MMGPListPanelSortUtil addBodySortItem(String key, boolean asc,
			String tableCode) {
		// ���������ֶε����
		int index = this.listPanel.getBodyBillModel(tableCode).getBodyColByKey(
				key);
		
        if (index == -1) {
            return this;
        }
		
		SortItem item = new SortItem(index, asc);

		// ������Ҫ�����ҳǩ
		if (!this.bodySortItems.containsKey(tableCode)) {
			this.bodySortItems.put(tableCode, new ArrayList<SortItem>());
		}

		this.bodySortItems.get(tableCode).add(item);
		return this;
	}

	
	/**
	 * ��ӱ�ͷ�����ֶΣ�Ĭ������
	 * 
	 * @param key
	 *            �����ֶ�
	 * @return ����
	 */
	public MMGPListPanelSortUtil addHeadSortItem(String key) {
		return this.addHeadSortItem(key, true);
	}

	
	/**
	 * ��ӱ�ͷ�����ֶ�
	 * 
	 * @param key
	 *            �ֶ�
	 * @param asc
	 *            �Ƿ�����
	 * @return ����
	 */
	public MMGPListPanelSortUtil addHeadSortItem(String key, boolean asc) {
		// ���������ֶε����
		int index = this.listPanel.getHeadBillModel().getBodyColByKey(key);
		SortItem item = new SortItem(index, asc);

		this.headSortItems.add(item);
		return this;
	}

	
	/**
	 * �����ͷ
	 */
	public void sortHead() {
		this.listPanel.getHeadBillModel().sortByColumns(headSortItems);
	}
	
	
	/**
	 * ��������
	 */
	public void sortBody() {
		// �������򣨶�ҳǩ��
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
