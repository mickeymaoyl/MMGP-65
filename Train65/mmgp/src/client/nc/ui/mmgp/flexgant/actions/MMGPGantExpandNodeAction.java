package nc.ui.mmgp.flexgant.actions;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.swing.action.treetable.ExpandNodeAction;

/**
 * @author wangfan3
 * 
 * ¸ÊÌØÍ¼Õ¹¿ª
 *
 */
public class MMGPGantExpandNodeAction extends ExpandNodeAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppGantContext context;
	
	private Integer number;

	public MMGPGantExpandNodeAction(AppGantContext context,Integer number) {
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
