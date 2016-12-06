package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.UEReturnAction;
import nc.ui.uif2.AppEvent;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��Ƭ���淵���б���水ť,���غ��¼���������ťˢ��״̬
 * </p>
 * 
 * @since �������� Jun 20, 2013
 * @author wangweir
 */
@SuppressWarnings("serial")
public class MMGPUEReturnAction extends UEReturnAction {

    @Override
    public void doAction(ActionEvent e) throws Exception {
        super.doAction(e);
        
        if (this.getModel() != null) {
            this.getModel().fireEvent(new AppEvent(MMGPUEReturnAction.class.getName()));
        }
    }
}
