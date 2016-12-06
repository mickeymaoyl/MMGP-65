package nc.ui.mmgp.flexgant.actions;

import javax.swing.tree.TreePath;

import com.dlsc.flexgantt.swing.treetable.TreeTable;

import nc.ui.mmgp.flexgant.view.MMGPGantChart;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.vo.pub.BusinessException;

/**
 * @author wangfan3
 *  ¸´ÖÆ½Ó¿Ú
 *
 *
 */
public interface ICopyStrategy {

	public static int CURRENTONLY = 1;

	public static int CURRENTLOW = 2;

	public static int LOWONLY = 3;

	public static int PASTEASLEVEL = 0;

	public static int PASTEASCHILD = 1;

	public void setCopyPara(int copypara);

	public int getCopyPara();

	public Object doCopy(TreePath[] selectpath, TreeTable table);

	public void insertPasthNode(MMGPGanttChartNode insertNode,
			MMGPGantChart gantChart);

	public boolean isCanCopy(TreePath[] copypath, TreeTable table);

	public boolean isCanPaste(TreePath[] selectpaths);

	public void validateCopy(TreePath[] selectpath, TreeTable table)
			throws BusinessException;

	public void validatePaste(TreePath[] selectpath, TreeTable table)
			throws BusinessException;

	public int getState_whencopy();

	public void setState_whencopy(int state_whencopy);
}
