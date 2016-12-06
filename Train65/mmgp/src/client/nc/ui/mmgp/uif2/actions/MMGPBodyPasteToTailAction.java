package nc.ui.mmgp.uif2.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.bill.BillTabVO;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 表体Copy至表
 * </p>
 * 
 * @since 创建日期 May 12, 2013
 * @author wangweir
 */
@SuppressWarnings("serial")
public class MMGPBodyPasteToTailAction extends BodyPasteToTailAction {

    @Override
    public Collection<String> getClearItems() {
        if (MMCollectionUtil.isEmpty(super.getClearItems())) {
            BillTabVO[] bodyTabvos = getCardPanel().getBillData().getBillBaseTabVOsByPosition(IBillItem.BODY);
            if (bodyTabvos != null) {
                List<String> keys = new ArrayList<String>();
                for (BillTabVO bodyTab : bodyTabvos) {
                    String pk_field = bodyTab.getBillMetaDataBusinessEntity().getPrimaryKey().getPKColumn().getName();
                    keys.add(bodyTab.getTabcode() + ":" + pk_field);
                }
                setClearItems(keys);
            }
        }
        return super.getClearItems();
    }
}
