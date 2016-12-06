package nc.ui.mmgp.pub.beans.digraph;

/**
 * <b> JGraph的前台表现类 </b>
 * <p>
 * 处理鼠标和键盘事件
 * </p>
 * 创建日期:2011-2-21
 * 
 * @author wangweiu
 */

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import nc.bs.logging.Logger;
import nc.ui.mmgp.pub.beans.digraph.eventhandler.DefaultGraphMouseHandler;
import nc.ui.mmgp.pub.beans.digraph.eventhandler.DefaultGraphRootHandler;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.plaf.basic.BasicGraphUI;

/**
 * 该类及其内部类控制鼠标行为和剪切板。 <li>1.控制图元的编辑行为 <li>2.控制图元的选取行为 <li>3.实现图元重叠、图元移动后调整合适的参与者
 * <li>4.实现正确的图元粘贴复制功能
 */
@SuppressWarnings("serial")
public class DigraphUI extends BasicGraphUI {

	/**
	 * @return the uiDigraph
	 */
	public DigraphChart getDiGraph() {
		return (DigraphChart) graph;
	}

	public UIDigraph getUiDigraph() {
		return getDiGraph().getUiDigraph();
	}

	/**
	 * 重载了父类的方法<br>
	 * NOTE::该方法被EditAction类调用，同时如果双击Cell或按下F2也会调用该方法
	 */
	@Override
	public boolean startEditing(Object cell, MouseEvent event) {
		if (cell instanceof DefaultEdge) {
			return false;
		} else if (cell instanceof DefaultGraphCell) {
			ActionEvent e = new ActionEvent(cell, 0, "DoubleClick");
			getUiDigraph().fireVertexDoubleClickEvent(e);  // 双击事件处理已删除　20120809
			event.consume();// TODO
		}
		return false;
	}

	@Override
	/*
	 * * Creates the listener responsible for calling the correct handlers based
	 * on mouse events, and to select invidual cells.
	 */
	protected MouseListener createMouseListener() {
		return new DefaultGraphMouseHandler(this);
	}

	@Override
	public CellHandle createHandle(GraphContext context) {

		if (context != null && !context.isEmpty() && graph.isEnabled()) {
			try {
				return new DefaultGraphRootHandler(context, this);
			} catch (HeadlessException e) {
				Logger.info(e.getMessage());
			} catch (RuntimeException e) {
				throw e;
			}
		}
		return null;
	}

	@Override
	protected KeyListener createKeyListener() {
		return new KeyHandler() {
			public void keyPressed(KeyEvent e) {

				if (!isProcessKey(e.getKeyCode())) {
					super.keyPressed(e);
					return;
				}
				Object[] selectedCells = graph.getSelectionCells();
				if (selectedCells == null || selectedCells.length == 0) {
					return;
				}
				for (Object selectedObj : selectedCells) {
					if (selectedObj == null
							|| !(selectedObj instanceof DefaultGraphCell)) {
						return;
					}
					DefaultGraphCell selectedCell = (DefaultGraphCell) selectedObj;
					Object model = selectedCell.getUserObject();
					if (model == null) {
						continue;
					}
					processKeyEvent(e, model);
				}
				e.consume();
				// }
			}

			private boolean isProcessKey(int keycode) {
				boolean graphEditable = graph != null && graph.hasFocus()
						&& graph.isEnabled() && graph.isEditable();
				Integer[] processKeys = new Integer[] { KeyEvent.VK_DELETE,
						KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT };
				return graphEditable
						&& Arrays.asList(processKeys).contains(keycode);
			}

		};
	}

	private void processKeyEvent(KeyEvent e, Object model) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			if (model instanceof AbstractVertexModel) {
				getUiDigraph().getDigraphModel().deleteVertex(
						(AbstractVertexModel) model);
			} else if (model instanceof AbstractEdgeModel) {
				getUiDigraph().getDigraphModel().deleteEdge(
						(AbstractEdgeModel) model);
			}
			break;
		case KeyEvent.VK_UP:
			move(model, 0, -1);
			break;
		case KeyEvent.VK_DOWN:
			move(model, 0, 1);
			break;
		case KeyEvent.VK_LEFT:
			move(model, -1, 0);
			break;
		case KeyEvent.VK_RIGHT:
			move(model, 1, 0);
			break;
		default:
			break;
		}
	}

	private void move(Object model, int xLength, double yLength) {
		if (model instanceof AbstractVertexModel) {
			AbstractVertexModel vertex = (AbstractVertexModel) model;
			double x = vertex.getX();
			double y = vertex.getY();
			vertex.setX(x + xLength);
			vertex.setY(y + yLength);
			getUiDigraph().getDigraphModel().updateVertex(vertex);
		}
	}


	// ***********************************
	// wrr 为了重构添加 begin

	public CellView getFocus() {
		return focus;
	}

	public void setFocus(CellView newFocus) {
		focus = newFocus;
	}

	public CellView getLastFocus() {
		return lastFocus;
	}

	public void setLastFocus(CellView newLastFocus) {
		lastFocus = newLastFocus;
	}

	public void completeEditing() {
		super.completeEditing();
	}

	public CellHandle getCellHandle() {
		return handle;
	}

	public BasicMarqueeHandler getMarqueeHandler() {
		return marquee;
	}

	public GraphModel getGraphModel() {
		return graphModel;
	}

	public GraphLayoutCache getGraphLayoutCache() {
		return graphLayoutCache;
	}

	public boolean getSnapSelectedView() {
		return snapSelectedView;
	}

	public Dimension getPreferredSize() {
		return preferredSize;
	}

	public int getScrollBorder() {
		return SCROLLBORDER;
	}

	// end
	// ***********************************
}
