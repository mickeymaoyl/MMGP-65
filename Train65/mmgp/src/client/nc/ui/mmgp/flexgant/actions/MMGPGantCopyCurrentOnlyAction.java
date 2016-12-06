package nc.ui.mmgp.flexgant.actions;

import javax.swing.tree.TreePath;

import com.dlsc.flexgantt.swing.treetable.TreeTable;

import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.gantt.model.AppGantContext;

/**
 * @author wangfan3
 * 
 * ����-����ǰ
 */
public class MMGPGantCopyCurrentOnlyAction extends MMGPGantCopyAction{

	public MMGPGantCopyCurrentOnlyAction(AppGantContext context, Integer number) {
		super(context,NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0117")/*����ǰ*/, number);
	}
	
	protected void doCopy(TreeTable table, TreePath[] paths) {
		this.getCopyStrategy().setCopyPara(ICopyStrategy.CURRENTONLY);
		super.doCopy(table, paths);
	}

}
