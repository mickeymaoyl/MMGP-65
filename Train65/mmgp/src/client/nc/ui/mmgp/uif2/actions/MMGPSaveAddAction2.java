package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.NCAction;
import nc.ui.uif2.actions.SaveAddAction;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 保存新增按钮，使用注入SaveAction的方式实现。既支持动作脚本的saveAction，又支持非动作脚本的saveAction
 * </p>
 * 
 * @since 创建日期 Sep 11, 2013
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
        // 保存执行成功则继续新增
        this.getAddAction().doAction(e);
    }

    public NCAction getSaveAction() {
        return this.saveAction;
    }

    public void setSaveAction(NCAction saveAction) {
        this.saveAction = saveAction;
    }
}
