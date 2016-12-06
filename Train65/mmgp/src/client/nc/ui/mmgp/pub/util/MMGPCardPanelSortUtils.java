package nc.ui.mmgp.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.bill.BillCardPanel;

/**
 * <b> 卡片排序工具 </b>
 * <p>
 * 为表体提供了基本的排序功能，支持表体多页签
 * </p>
 * <p>
 * 使用样例： new
 * MMCardPanelSortUtils(cardPanel).addBodySortItem("pk_org",true).addBodySortItem("cmaterialvid",false).sort();
 * </p>
 * 创建日期:2010-6-13
 * 
 * @author:zhoujuna
 */
public class MMGPCardPanelSortUtils {
    /**
     * 卡片
     */
    private BillCardPanel card;

    /**
     * 表体排序
     */
    private Map<String, List<SortItem>> bodySortItems = new HashMap<String, List<SortItem>>();

    /**
     * 默认构造函数
     * 
     * @param card
     *        卡片
     */
    public MMGPCardPanelSortUtils(BillCardPanel card) {
        this.card = card;
    }

    /**
     * 增加待排序字段
     * 
     * @param key
     *        排序的关键字
     * @param asc
     *        正序
     * @param tableCode
     *        页签名
     * @return 返回自身
     */
    public MMGPCardPanelSortUtils addBodySortItem(String key,
                                                  boolean asc,
                                                  String tableCode) {
        // 查找排序字段的序号
        int index = this.card.getBillModel(tableCode).getBodyColByKey(key);

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
     * 增加待排序字段
     * 
     * @param key
     *        排序的关键字
     * @param asc
     *        升序排列
     * @return 返回自身
     */
    public MMGPCardPanelSortUtils addBodySortItem(String key,
                                                  boolean asc) {
        return this.addBodySortItem(key, asc, this.card.getCurrentBodyTableCode());
    }

    /**
     * 增加待排序字段，默认升序
     * 
     * @param key
     *        排序的关键字
     * @return 返回自身
     */
    public MMGPCardPanelSortUtils addBodySortItem(String key) {
        return this.addBodySortItem(key, true);
    }

    /**
     * 排序
     */
    public void sort() {
        // 表体排序（多页签）
        for (String tableCode : this.bodySortItems.keySet()) {
            List<SortItem> sortItems = this.bodySortItems.get(tableCode);
            this.card.getBillModel(tableCode).sortByColumns(sortItems);
        }
    }

}
