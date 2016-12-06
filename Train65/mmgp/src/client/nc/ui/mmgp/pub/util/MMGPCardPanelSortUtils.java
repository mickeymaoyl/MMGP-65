package nc.ui.mmgp.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.bill.BillCardPanel;

/**
 * <b> ��Ƭ���򹤾� </b>
 * <p>
 * Ϊ�����ṩ�˻����������ܣ�֧�ֱ����ҳǩ
 * </p>
 * <p>
 * ʹ�������� new
 * MMCardPanelSortUtils(cardPanel).addBodySortItem("pk_org",true).addBodySortItem("cmaterialvid",false).sort();
 * </p>
 * ��������:2010-6-13
 * 
 * @author:zhoujuna
 */
public class MMGPCardPanelSortUtils {
    /**
     * ��Ƭ
     */
    private BillCardPanel card;

    /**
     * ��������
     */
    private Map<String, List<SortItem>> bodySortItems = new HashMap<String, List<SortItem>>();

    /**
     * Ĭ�Ϲ��캯��
     * 
     * @param card
     *        ��Ƭ
     */
    public MMGPCardPanelSortUtils(BillCardPanel card) {
        this.card = card;
    }

    /**
     * ���Ӵ������ֶ�
     * 
     * @param key
     *        ����Ĺؼ���
     * @param asc
     *        ����
     * @param tableCode
     *        ҳǩ��
     * @return ��������
     */
    public MMGPCardPanelSortUtils addBodySortItem(String key,
                                                  boolean asc,
                                                  String tableCode) {
        // ���������ֶε����
        int index = this.card.getBillModel(tableCode).getBodyColByKey(key);

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
     * ���Ӵ������ֶ�
     * 
     * @param key
     *        ����Ĺؼ���
     * @param asc
     *        ��������
     * @return ��������
     */
    public MMGPCardPanelSortUtils addBodySortItem(String key,
                                                  boolean asc) {
        return this.addBodySortItem(key, asc, this.card.getCurrentBodyTableCode());
    }

    /**
     * ���Ӵ������ֶΣ�Ĭ������
     * 
     * @param key
     *        ����Ĺؼ���
     * @return ��������
     */
    public MMGPCardPanelSortUtils addBodySortItem(String key) {
        return this.addBodySortItem(key, true);
    }

    /**
     * ����
     */
    public void sort() {
        // �������򣨶�ҳǩ��
        for (String tableCode : this.bodySortItems.keySet()) {
            List<SortItem> sortItems = this.bodySortItems.get(tableCode);
            this.card.getBillModel(tableCode).sortByColumns(sortItems);
        }
    }

}
