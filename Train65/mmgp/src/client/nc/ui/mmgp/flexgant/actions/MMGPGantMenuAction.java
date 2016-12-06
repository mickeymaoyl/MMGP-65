package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.icons.IconId;
import com.dlsc.flexgantt.icons.IconRegistry;
import com.dlsc.flexgantt.swing.action.treetable.AbstractTreeTableAction;

/**
 * @author wangfan3
 * 
 *  甘特图左表 右键菜单 子菜单按钮
 *
 */
public class MMGPGantMenuAction extends AbstractTreeTableAction{
	
	private AppGantContext context;

	private Integer number;
	
	private List<AbstractTreeTableAction> child;


	public MMGPGantMenuAction(String text,AppGantContext context,
			Integer number) {
		super(context.getGantchart().getTreeTable(), text, IconRegistry.getIcon(IconId.RECTANGLE)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public AppGantContext getContext() {
		return context;
	}

	public void setContext(AppGantContext context) {
		this.context = context;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public List<AbstractTreeTableAction> getChild() {
		return child;
	}

	public void setChild(List<AbstractTreeTableAction> child) {
		this.child = child;
	}


}
