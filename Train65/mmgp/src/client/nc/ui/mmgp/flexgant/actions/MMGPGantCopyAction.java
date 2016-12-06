package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;

import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.uif2.IExceptionHandler;
import nc.vo.pub.BusinessException;

import com.dlsc.flexgantt.icons.IconId;
import com.dlsc.flexgantt.icons.IconRegistry;
import com.dlsc.flexgantt.swing.action.treetable.AbstractTreeTableAction;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

/**
 * @author wangfan3
 *
 *¸´ÖÆ°´Å¥
 *
 */
public class MMGPGantCopyAction extends AbstractTreeTableAction {

	private AppGantContext context;

	private Integer number;

	protected ICopyStrategy copyStrategy;

	private IExceptionHandler exceptionHandler;

	public MMGPGantCopyAction(AppGantContext context, Integer number) {
		super(context.getGantchart().getTreeTable(),
				NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0115")/*¸´ÖÆ*/, IconRegistry.getIcon(IconId.RECTANGLE)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}

	public MMGPGantCopyAction(AppGantContext context, String text,
			Integer number) {
		super(context.getGantchart().getTreeTable(), text, IconRegistry
				.getIcon(IconId.RECTANGLE)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TreeTable table = getTreeTable();
		TreePath[] paths = table.getSelectionPaths();
		try {
			validate();
		} catch (BusinessException e1) {
			exceptionHandler.handlerExeption(e1);
		}
		doCopy(table, paths);
	}

	protected void validate() throws BusinessException {
		copyStrategy.validateCopy(this.getTreeTable().getSelectionPaths(),
				this.getTreeTable());
	}

	protected boolean isCanCopy(TreeTable table, TreePath[] paths) {
		return copyStrategy.isCanCopy(paths, table);
	}

	protected void doCopy(TreeTable table, TreePath[] paths) {
		copyStrategy.doCopy(paths, treeTable);
	}

	@Override
	public boolean isEnabled() {
		boolean actionStateOfContext = true;
		if (context != null) {
			actionStateOfContext = context.getActionStateOfContext(number);
		}
		return actionStateOfContext && treeTable.getSelectionCount() > 0
				&& super.isEnabled();
	}

	public ICopyStrategy getCopyStrategy() {
		return copyStrategy;
	}

	public void setCopyStrategy(ICopyStrategy copyStrategy) {
		this.copyStrategy = copyStrategy;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
