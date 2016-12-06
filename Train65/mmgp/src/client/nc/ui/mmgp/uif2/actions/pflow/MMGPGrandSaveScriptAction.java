package nc.ui.mmgp.uif2.actions.pflow;

import nc.bs.uif2.IActionCode;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 22, 2013
 * @author wangweir
 */
public class MMGPGrandSaveScriptAction extends MMGPGrandScriptPFlowAction {

    private static final long serialVersionUID = 1L;

    public MMGPGrandSaveScriptAction() {
        ActionInitializer.initializeAction(this, IActionCode.SAVE);
        this.setActionName(IPFACTION.SAVE);
    }

    @Override
    public void doBeforAction() {
        
    }

    @Override
    protected boolean isActionEnable() {
        return this.model.getAppUiState().getUiState() == UIState.EDIT
            || this.model.getAppUiState().getUiState() == UIState.ADD;
    }

}
