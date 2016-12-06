package nc.ui.mmgp.flexgant.view;

import java.awt.Component;

import nc.ui.mmgp.flexgant.view.treetable.MMGPGantColumnGroup;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGroupableTreeTableHeader;
import nc.vo.mmgp.util.MMArrayUtil;

import com.dlsc.flexgantt.swing.AbstractGanttChart;
import com.dlsc.flexgantt.swing.GanttChart;
import com.dlsc.flexgantt.swing.layer.LayerContainerScrollPane;
import com.dlsc.flexgantt.swing.print.PrintContext;
import com.dlsc.flexgantt.swing.print.PrintGanttChart;
import com.dlsc.flexgantt.swing.treetable.TreeTableHeader;
import com.dlsc.flexgantt.swing.treetable.TreeTableScrollPane;

/**
 * @Description: 甘特图打印的gantchart
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2015-3-17上午10:43:43
 * @author: tangxya
 */
public class MMGPPrintGantChart extends PrintGanttChart {
	private GanttChart gantchart;

	private PrintContext ctx;

	/**
	 * @param ctx
	 */
	public MMGPPrintGantChart(PrintContext ctx) {

		super(ctx);
		this.ctx = ctx;
	}

	@Override
	public TreeTableScrollPane[] getTreeTableScrollPanes() {
		if (ctx == null) {
			return null;
		}
		// tangxya
		return ((MMGPGantChart) ctx.getGanttChart()).getTreeTableScrollPanes();

	}

	@Override
	public LayerContainerScrollPane[] getLayerContainerScrollPanes() {
		if (ctx == null) {
			return null;
		}
		return ((MMGPGantChart) ctx.getGanttChart())
				.getLayerContainerScrollPanes();
	}

	protected void copySettings(AbstractGanttChart from, AbstractGanttChart to,
			boolean initialCopy) {
		gantchart = (GanttChart) from;
		super.copySettings(from, to, initialCopy);
	}

	public GanttChart getGantchart() {
		return gantchart;
	}

	public void setGantchart(GanttChart gantchart) {
		this.gantchart = gantchart;
	}

	@Override
	public void updateState() {
		setSize(getPreferredSize());
		for (Component comp : getComponents()) {
			comp.invalidate();
			comp.validate();
		}
		// Exception: This function should be called while holding treeLock
		// 适应JDK1.7新特性（最主要是防止1.7下报错。。。）。by dongyshd
		synchronized (getTreeLock()) {
			validateTree();
		}
		if (getPreviewDialog() != null
				&& getPreviewDialog().getScrollPane() != null) {
			getPreviewDialog().getScrollPane().repaint();
		}
	}

	@Override
	public void tearDown() {
		if (this.getPreviewDialog() != null) {
			this.getPreviewDialog().dispose();
		}
		super.tearDown();
	}

	/**
	 * 打印时将多表头的信息复制过来
	 */
	protected void copySettings(TreeTableHeader from, TreeTableHeader to,
			boolean initialCopy) {
		if (from instanceof MMGPGroupableTreeTableHeader) {
			MMGPGroupableTreeTableHeader header = (MMGPGroupableTreeTableHeader) from;
			MMGPGroupableTreeTableHeader printheader = (MMGPGroupableTreeTableHeader) to;
			MMGPGantColumnGroup[] groups = header.getColumnGroups();
			if (MMArrayUtil.isNotEmpty(groups)) {
				for (MMGPGantColumnGroup group : groups) {
					printheader.addColumnGroup(group);
				}
			}
		}
		super.copySettings(from, to, initialCopy);
	}
}
