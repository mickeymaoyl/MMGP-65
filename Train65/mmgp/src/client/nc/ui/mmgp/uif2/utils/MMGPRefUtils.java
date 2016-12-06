package nc.ui.mmgp.uif2.utils;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * 参照相关的工具类
 * 
 * @author wangrra
 */
public class MMGPRefUtils {

    /**
     * 表头--追加的方式设置过滤条件
     * 
     * @param cardPanel
     *        单据模板
     * @param itemName
     *        要设置参照过滤条件的字段编码
     * @param condition
     *        要给参照设置的sql条件
     */
    public static void setHeadRefConditionInAppend(BillCardPanel cardPanel,
                                                   String itemName,
                                                   String condition) {
        setRefFilterCondition(cardPanel, itemName, true, "", condition, true);
    }

    /**
     * 表头--替换的方式设置过滤条件
     * 
     * @param cardPanel
     *        单据模板
     * @param itemName
     *        要设置参照过滤条件的字段编码
     * @param condition
     *        要给参照设置的sql条件
     */
    public static void setHeadRefConditionInReplace(BillCardPanel cardPanel,
                                                    String itemName,
                                                    String condition) {
        setRefFilterCondition(cardPanel, itemName, true, "", condition, false);
    }

    /**
     * 表体--追加的方式设置过滤条件
     * 
     * @param cardPanel
     *        单据模板
     * @param itemName
     *        要设置参照过滤条件的字段编码
     * @param tabCode
     *        参照字段所属的表体页签编码，当在表头时可不配；如在表体，不配则默认为第一个页签
     * @param condition
     *        要给参照设置的sql条件
     */
    public static void setBodyRefConditionInAppend(BillCardPanel cardPanel,
                                                   String itemName,
                                                   String tabCode,
                                                   String condition) {
        setRefFilterCondition(cardPanel, itemName, false, tabCode, condition, true);
    }

    /**
     * 表体--替换的方式设置过滤条件
     * 
     * @param cardPanel
     *        BillCardPanel
     * @param itemName
     *        要设置参照过滤条件的字段编码
     * @param tabCode
     *        参照字段所属的表体页签编码，当在表头时可不配；如在表体，不配则默认为第一个页签
     * @param condition
     *        要给参照设置的sql条件
     */
    public static void setBodyRefConditionInReplace(BillCardPanel cardPanel,
                                                    String itemName,
                                                    String tabCode,
                                                    String condition) {
        setRefFilterCondition(cardPanel, itemName, false, tabCode, condition, false);
    }

    /**
     * 表头--追加的方式设置过滤条件
     * 
     * @param cardPanel
     *        单据模板
     * @param itemName
     *        要设置参照过滤条件的字段编码
     * @param tabCode
     *        参照字段所属的表体页签编码，当在表头时可不配；如在表体，不配则默认为第一个页签
     * @param condition
     *        要给参照设置的sql条件
     * @deprecated 建议使用setHeadRefConditionInAppend
     */
    @Deprecated
    public static void setRefFilterCondition(BillCardPanel cardPanel,
                                             String itemName,
                                             String tabCode,
                                             String condition) {
        setRefFilterCondition(cardPanel, itemName, true, tabCode, condition, true);
    }

    /**
     * @param cardPanel
     *        单据模板
     * @param itemName
     *        要设置参照过滤条件的字段编码
     * @param isHead
     *        是否为表头，true：表头，false：表体
     * @param tabCode
     *        参照字段所属的表体页签编码，当在表头时可不配；如在表体，不配则默认为第一个页签
     * @param condition
     *        要给参照设置的sql条件
     * @param isAppend
     *        condition是否追加，true代表追加， false代表替换
     */
    public static void setRefFilterCondition(BillCardPanel cardPanel,
                                             String itemName,
                                             boolean isHead,
                                             String tabCode,
                                             String condition,
                                             boolean isAppend) {

        if (cardPanel == null || itemName == null || MMStringUtil.isEmpty(condition)) {
            // 以上三项为必输项
            return;
        }

        BillItem billItem = null;

        if (isHead) {
            billItem = cardPanel.getHeadItem(itemName);
        } else {
            if (MMStringUtil.isEmpty(tabCode)) {
                billItem = cardPanel.getBillData().getBodyItem(itemName);
            } else {
                billItem = cardPanel.getBillData().getBodyItem(tabCode, itemName);
            }
        }
        // 字段名是否正确 且　字段类型是否为参照
        if (billItem == null || IBillItem.UFREF != billItem.getDataType()) {
            return;
        }

        UIRefPane refPane = (UIRefPane) billItem.getComponent();

        if (isAppend) {
            String oldWherePart = refPane.getRefModel().getWherePart();
            if (MMStringUtil.isEmpty(oldWherePart) || "null".equals(oldWherePart)) {
                refPane.getRefModel().setWherePart(condition);
            } else {
                refPane.getRefModel().addWherePart(" and " + condition);
            }
        } else {
            refPane.getRefModel().setWherePart(condition);
        }

    }

}
