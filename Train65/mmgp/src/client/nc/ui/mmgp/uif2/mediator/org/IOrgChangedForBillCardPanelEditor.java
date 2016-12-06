package nc.ui.mmgp.uif2.mediator.org;

import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.ui.uif2.model.AbstractUIAppModel;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���޸Ľ�����֯�仯����
 * </p>
 * 
 * @since  ��������:Apr 11, 2013
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
