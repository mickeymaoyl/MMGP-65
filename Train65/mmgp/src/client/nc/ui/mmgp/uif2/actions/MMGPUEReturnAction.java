package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.actions.UEReturnAction;
import nc.ui.uif2.AppEvent;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 卡片界面返回列表界面按钮,返回后发事件，触发按钮刷新状态
 * </p>
 * 
 * @since 创建日期 Jun 20, 2013
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
