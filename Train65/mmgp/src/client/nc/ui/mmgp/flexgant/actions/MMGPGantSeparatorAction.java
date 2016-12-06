package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.swing.action.treetable.AbstractTreeTableAction;

/**
 * @author wangfan3
 * 
 * ¡¡¸ÊÌØÍ¼¡¡·Ö¸ô·û°´Å¥
 *
 */
public class MMGPGantSeparatorAction extends AbstractTreeTableAction {

	public MMGPGantSeparatorAction(AppGantContext context,
			Integer number) {
		super(
				context.getGantchart().getTreeTable(),
				"Separater", null); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
