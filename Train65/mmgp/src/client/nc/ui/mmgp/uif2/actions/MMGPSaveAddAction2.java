package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.NCAction;
import nc.ui.uif2.actions.SaveAddAction;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ����������ť��ʹ��ע��SaveAction�ķ�ʽʵ�֡���֧�ֶ����ű���saveAction����֧�ַǶ����ű���saveAction
 * </p>
 * 
 * @since �������� Sep 11, 2013
 * @author wangweir
 */
public class MMGPSaveAddAction2 extends SaveAddAction {

    private NCAction saveAction;

    public MMGPSaveAddAction2() {
        super();
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        this.getSaveAction().doAction(e);
        // ����ִ�гɹ����������
        this.getAddAction().doAction(e);
    }

    public NCAction getSaveAction() {
        return this.saveAction;
    }

    public void setSaveAction(NCAction saveAction) {
        this.saveAction = saveAction;
    }
}
