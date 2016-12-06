package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;

import nc.ui.mmgp.flexgant.view.MMGPGantForm;
import nc.ui.mmgp.uif2.actions.MMGPBodyDelLineAction;

/**
 * @author wangfan3
 *
 * ∏ ÃÿÕº…æ––∞¥≈•
 *
 */
public class MMGPGantDelLineAction extends MMGPBodyDelLineAction {
    private MMGPGantForm ganteditor;

    @Override
    public void doAction(ActionEvent arg0) throws Exception {
        getGanteditor().getGantChart().stopEditing();
        getGanteditor().getGantChart().deleteSelNode();

        // this.fireEvent();
    }

    public MMGPGantForm getGanteditor() {
        return ganteditor;
    }

    public void setGanteditor(MMGPGantForm ganteditor) {
        this.ganteditor = ganteditor;
    }
}
