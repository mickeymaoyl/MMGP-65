package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;

import nc.ui.mmgp.flexgant.view.MMGPGantForm;
import nc.ui.mmgp.uif2.actions.MMGPBodyAddLineAction;
import nc.ui.pubapp.uif2app.event.card.BodyRowEditType;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;

/**
 * @author wangfan3
 * ∏ ÃÿÕº‘ˆ––
 */
public class MMGPGantAddLineAction extends MMGPBodyAddLineAction {
    private MMGPGantForm ganteditor; 
    
    @Override
    public void doAction(ActionEvent arg0) throws Exception {
        getGanteditor().getGantChart().stopEditing();
        getGanteditor().getGantChart().addLine();
        
       // this.fireEvent();
    }

    public MMGPGantForm getGanteditor() {
        return ganteditor;
    }

    public void setGanteditor(MMGPGantForm ganteditor) {
        this.ganteditor = ganteditor;
    }
    
    protected void fireEvent() {
        int[] rows = new int[]{ this.getCardPanel().getRowCount()-1 };
        this.getModel().fireEvent(
            new CardBodyAfterRowEditEvent(this.getCardPanel(),
                BodyRowEditType.ADDLINE, rows));
      }
      
}
