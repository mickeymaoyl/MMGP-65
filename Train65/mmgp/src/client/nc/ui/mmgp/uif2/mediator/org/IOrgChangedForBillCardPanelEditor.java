package nc.ui.mmgp.uif2.mediator.org;

import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.ui.uif2.model.AbstractUIAppModel;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 批修改界面组织变化处理
 * </p>
 * 
 * @since  创建日期:Apr 11, 2013
 * @author wangweir
 */
public interface IOrgChangedForBillCardPanelEditor {

    /**
     * @param billCardPanelEditor
     *        IBillCardPanelEditor
     * @param model
     *        AbstractUIAppModel
     */
    void orgChanged(IBillCardPanelEditor billCardPanelEditor,
                    AbstractUIAppModel model);
}
