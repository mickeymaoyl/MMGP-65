package nc.ui.mmgp.flexgant.actions;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.swing.action.treetable.SelectAllNodesAction;

/**
 * @author wangfan3
 * 
 * 甘特图　全选按钮
 *
 */
public class MMGPGantSelectAllNodesAction extends SelectAllNodesAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppGantContext context;
	
	private Integer number;

	public MMGPGantSelectAllNodesAction(AppGantContext context,Integer number) {
		super(context.getGantchart().getTreeTable());
		this.context = context;
		this.number = number;
	}
	
	public MMGPGantSelectAllNodesAction(AppGantContext context) {
		super(context.getGantchart().getTreeTable());
		this.context = context;
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
