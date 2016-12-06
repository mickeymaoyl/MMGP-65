package nc.ui.mmgp.flexgant.actions;

import java.awt.event.ActionEvent;

import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.flexgant.view.MMGPGantChart;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.uif2.IExceptionHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import com.dlsc.flexgantt.icons.IconId;
import com.dlsc.flexgantt.icons.IconRegistry;
import com.dlsc.flexgantt.swing.action.treetable.AbstractTreeTableAction;

/**
 * @author wangfan3
 * 
 *  ¸ÊÌØÍ¼ Õ³Ìù °´Å¥
 *
 */
public class MMGPGantPasterAction extends AbstractTreeTableAction {

	private AppGantContext context;

	private Integer number;

	private ICopyStrategy copyStrategy;

	private IExceptionHandler exceptionHandler;

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public MMGPGantPasterAction(AppGantContext context, Integer number) {
		super(context.getGantchart().getTreeTable(),
				NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0122")/*Õ³Ìù*/, IconRegistry.getIcon(IconId.GRID)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}

	public MMGPGantPasterAction(AppGantContext context, String text,
			Integer number) {
		super(context.getGantchart().getTreeTable(), text, IconRegistry
				.getIcon(IconId.GRID)); //$NON-NLS-1$
		this.context = context;
		this.number = number;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (copyStrategy == null) {
			ExceptionUtils
					.wrappBusinessException(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0123")/*MMGPGantPasterActionÖÐÎ´×¢Èë¸´ÖÆ²ßÂÔcopyStrtegy*/);
		}
		try {
			validate();
			doAction();
		} catch (BusinessException e1) {
			exceptionHandler.handlerExeption(e1);
		}
		
	}

	protected void validate() throws BusinessException {
		copyStrategy.validatePaste(this.getTreeTable().getSelectionPaths(),
				this.getTreeTable());
	}

	protected void doAction() {
		MMGPGantTreetable treetable = (MMGPGantTreetable) this.getTreeTable();
		for (TreePath path : treetable.getSelectionModel().getSelectionPaths()) {
			MMGPGanttChartNode parentNode = getParentNode(path);
			copyStrategy.insertPasthNode(parentNode,
					(MMGPGantChart) context.getGantchart());
		}
	}

	protected MMGPGanttChartNode getParentNode(TreePath selectpath) {
		return (MMGPGanttChartNode) selectpath.getLastPathComponent();
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
}
