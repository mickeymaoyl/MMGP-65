package nc.ui.mmgp.uif2.utils;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.vo.mmgp.util.MMStringUtil;

/**
 * ������صĹ�����
 * 
 * @author wangrra
 */
public class MMGPRefUtils {

    /**
     * ��ͷ--׷�ӵķ�ʽ���ù�������
     * 
     * @param cardPanel
     *        ����ģ��
     * @param itemName
     *        Ҫ���ò��չ����������ֶα���
     * @param condition
     *        Ҫ���������õ�sql����
     */
    public static void setHeadRefConditionInAppend(BillCardPanel cardPanel,
                                                   String itemName,
                                                   String condition) {
        setRefFilterCondition(cardPanel, itemName, true, "", condition, true);
    }

    /**
     * ��ͷ--�滻�ķ�ʽ���ù�������
     * 
     * @param cardPanel
     *        ����ģ��
     * @param itemName
     *        Ҫ���ò��չ����������ֶα���
     * @param condition
     *        Ҫ���������õ�sql����
     */
    public static void setHeadRefConditionInReplace(BillCardPanel cardPanel,
                                                    String itemName,
                                                    String condition) {
        setRefFilterCondition(cardPanel, itemName, true, "", condition, false);
    }

    /**
     * ����--׷�ӵķ�ʽ���ù�������
     * 
     * @param cardPanel
     *        ����ģ��
     * @param itemName
     *        Ҫ���ò��չ����������ֶα���
     * @param tabCode
     *        �����ֶ������ı���ҳǩ���룬���ڱ�ͷʱ�ɲ��䣻���ڱ��壬������Ĭ��Ϊ��һ��ҳǩ
     * @param condition
     *        Ҫ���������õ�sql����
     */
    public static void setBodyRefConditionInAppend(BillCardPanel cardPanel,
                                                   String itemName,
                                                   String tabCode,
                                                   String condition) {
        setRefFilterCondition(cardPanel, itemName, false, tabCode, condition, true);
    }

    /**
     * ����--�滻�ķ�ʽ���ù�������
     * 
     * @param cardPanel
     *        BillCardPanel
     * @param itemName
     *        Ҫ���ò��չ����������ֶα���
     * @param tabCode
     *        �����ֶ������ı���ҳǩ���룬���ڱ�ͷʱ�ɲ��䣻���ڱ��壬������Ĭ��Ϊ��һ��ҳǩ
     * @param condition
     *        Ҫ���������õ�sql����
     */
    public static void setBodyRefConditionInReplace(BillCardPanel cardPanel,
                                                    String itemName,
                                                    String tabCode,
                                                    String condition) {
        setRefFilterCondition(cardPanel, itemName, false, tabCode, condition, false);
    }

    /**
     * ��ͷ--׷�ӵķ�ʽ���ù�������
     * 
     * @param cardPanel
     *        ����ģ��
     * @param itemName
     *        Ҫ���ò��չ����������ֶα���
     * @param tabCode
     *        �����ֶ������ı���ҳǩ���룬���ڱ�ͷʱ�ɲ��䣻���ڱ��壬������Ĭ��Ϊ��һ��ҳǩ
     * @param condition
     *        Ҫ���������õ�sql����
     * @deprecated ����ʹ��setHeadRefConditionInAppend
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
     *        ����ģ��
     * @param itemName
     *        Ҫ���ò��չ����������ֶα���
     * @param isHead
     *        �Ƿ�Ϊ��ͷ��true����ͷ��false������
     * @param tabCode
     *        �����ֶ������ı���ҳǩ���룬���ڱ�ͷʱ�ɲ��䣻���ڱ��壬������Ĭ��Ϊ��һ��ҳǩ
     * @param condition
     *        Ҫ���������õ�sql����
     * @param isAppend
     *        condition�Ƿ�׷�ӣ�true����׷�ӣ� false�����滻
     */
    public static void setRefFilterCondition(BillCardPanel cardPanel,
                                             String itemName,
                                             boolean isHead,
                                             String tabCode,
                                             String condition,
                                             boolean isAppend) {

        if (cardPanel == null || itemName == null || MMStringUtil.isEmpty(condition)) {
            // ��������Ϊ������
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
        // �ֶ����Ƿ���ȷ �ҡ��ֶ������Ƿ�Ϊ����
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
