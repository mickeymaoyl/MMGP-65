package nc.ui.mmgp.flexgant.actions;

import javax.swing.tree.TreePath;

import com.dlsc.flexgantt.swing.treetable.TreeTable;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 *
 * 复制-包含下级
 *
 */
public class MMGPGantCopyCurrentLowAction extends MMGPGantCopyAction{

	public MMGPGantCopyCurrentLowAction(AppGantContext context, Integer number) {
		super(context,NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0116")/*包含下级*/, number);
	}
	
	protected void doCopy(TreeTable table, TreePath[] paths) {
		this.getCopyStrategy().setCopyPara(ICopyStrategy.CURRENTLOW);
		super.doCopy(table, paths);
	}

}
