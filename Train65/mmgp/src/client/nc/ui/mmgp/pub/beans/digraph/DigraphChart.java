/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph;

import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractCellModel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;

/**
 * <b> 对Jgraph的简单封装 </b>
 * <p>
 * 1.显示ToolTip
 * </p>
 * 创建日期:2011-2-21
 * 
 * @author wangweiu
 */
@SuppressWarnings("serial")
public class DigraphChart extends JGraph {

	private UIDigraph uiDigraph = null;

	/**
	 * @param defaultGraphModel
	 */
	public DigraphChart(GraphModel defaultGraphModel, UIDigraph uiDigraph) {
		super(defaultGraphModel);
		this.uiDigraph = uiDigraph;
	}

	/**
	 * @return the uiDigraph
	 */
	public UIDigraph getUiDigraph() {
		return uiDigraph;
	}

	/**
	 * Notification from the <code>UIManager</code> that the L&F has changed.
	 * Replaces the current UI object with the latest version from the
	 * <code>UIManager</code>. Subclassers can override this to support
	 * different GraphUIs.
	 * 
	 * @see JComponent#updateUI
	 */
	@Override
	public void updateUI() {
		setUI(new DigraphUI());
		invalidate();
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		if (e != null) {
			Object cell = getFirstCellForLocation(e.getX(), e.getY());
			if (cell != null && cell instanceof DefaultGraphCell) {
				DefaultGraphCell graphCell = (DefaultGraphCell) cell;
				if (graphCell.getUserObject() instanceof AbstractCellModel) {
					AbstractCellModel cellModel = (AbstractCellModel) graphCell
							.getUserObject();
					return cellModel.getToolTipText();
				}
			}
		}
		return super.getToolTipText(e);
	}
}
