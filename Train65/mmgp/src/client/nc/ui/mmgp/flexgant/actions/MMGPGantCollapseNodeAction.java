package nc.ui.mmgp.flexgant.actions;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.swing.action.treetable.CollapseNodeAction;

/**
 * @author wangfan3
 * ����ͼ ����
 *
 */
public class MMGPGantCollapseNodeAction extends CollapseNodeAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppGantContext context;
	
	private Integer number;

	public MMGPGantCollapseNodeAction(AppGantContext context,Integer number) {
		super(context.getGantchart().getTreeTable());
		this.context = context;
		this.number = number;
	}

	@Override
	public boolean isEnabled() {
		boolean actionStateOfContext = true;
		if(context != null){
			actionStateOfContext = context.getActionStateOfContext(number);
		} 
		return actionStateOfContext && super.isEnabled();
	}
}
