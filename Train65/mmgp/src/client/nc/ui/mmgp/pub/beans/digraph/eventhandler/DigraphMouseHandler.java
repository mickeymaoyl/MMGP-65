package nc.ui.mmgp.pub.beans.digraph.eventhandler;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import nc.ui.mmgp.pub.beans.digraph.DigraphUI;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class DigraphMouseHandler extends MouseAdapter implements
		MouseMotionListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8097913721522794887L;

	/* The cell under the mousepointer. */
	protected CellView cell;

	/* The object that handles mouse operations. */
	protected Object handler;

	protected transient Cursor previousCursor = null;

	protected JGraph graph;

	protected DigraphUI graphUI;

	protected CellView lastFocus;

	protected CellView focus;

//	protected CellHandle handle;

//	protected BasicMarqueeHandler marquee;

	protected GraphModel graphModel;

	protected GraphLayoutCache graphLayoutCache;

	public DigraphMouseHandler(DigraphUI digraphUI) {
		graphUI = digraphUI;
		graph = digraphUI.getDiGraph();
		lastFocus = graphUI.getLastFocus();
		focus = graphUI.getFocus();
//		handle = graphUI.getCellHandle();
//		marquee = graphUI.getMarqueeHandler();
		graphModel = graphUI.getGraphModel();
		graphLayoutCache = graphUI.getGraphLayoutCache();
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
		handler = null;
		if (!e.isConsumed() && graph.isEnabled() && !e.isPopupTrigger()) {
			graph.requestFocus();
			int s = graph.getTolerance();
			Rectangle2D r = graph.fromScreen(new Rectangle2D.Double(e.getX()
					- s, e.getY() - s, 2 * s, 2 * s));
			lastFocus = focus;
			focus = (focus != null && focus.intersects(graph, r)) ? focus
					: null;
			cell = graph.getNextSelectableViewAt(focus, e.getX(), e.getY());

			if (focus == null)
				focus = cell;

			graphUI.completeEditing();
			boolean isForceMarquee = graphUI.isForceMarqueeEvent(e);
			boolean isEditable = graph.isGroupsEditable()
					|| (focus != null && focus.isLeaf());
			if (!isForceMarquee) {
				if (e.getClickCount() == graph.getEditClickCount()
						&& focus != null && isEditable
						&& focus.getParentView() == null
						//&& graph.isCellEditable(focus.getCell())
						&& handleEditTrigger(cell.getCell(), e)) {
					e.consume();
					cell = null;
				} else if (!graphUI.isToggleSelectionEvent(e)) {
					if (graphUI.getCellHandle() != null) {
						graphUI.getCellHandle().mousePressed(e);
						handler = graphUI.getCellHandle();
					}
					// Immediate Selection
					if (!e.isConsumed() && cell != null
							&& !graph.isCellSelected(cell.getCell())) {
						graphUI.selectCellForEvent(cell.getCell(), e);
						focus = cell;
						if (graphUI.getCellHandle() != null) {
							graphUI.getCellHandle().mousePressed(e);
							handler = graphUI.getCellHandle();
						}
						e.consume();
						cell = null;
					}

					afterMousePressed();

				}
			}

			// Marquee Selection
			if (!e.isConsumed()
					&& graphUI.getMarqueeHandler() != null
					&& (!graphUI.isToggleSelectionEvent(e) || focus == null || isForceMarquee)) {
				graphUI.getMarqueeHandler().mousePressed(e);
				handler = graphUI.getMarqueeHandler();
			}
		}
	}

	protected void afterMousePressed() {}

	/**
	 * Handles edit trigger by starting the edit and return true if the editing
	 * has already started.
	 * 
	 * @param cell
	 *            the cell being edited
	 * @param e
	 *            the mouse event triggering the edit
	 * @return <code>true</code> if the editing has already started
	 */
	protected boolean handleEditTrigger(Object cell, MouseEvent e) {
		graph.scrollCellToVisible(cell);
		if (cell != null)
			startEditing(cell, e);
		return graph.isEditing();
	}

	private void startEditing(Object cell2, MouseEvent e) {
		graphUI.startEditing(cell2, e);
	}

	public void mouseDragged(MouseEvent e) {
		int mods = e.getModifiers();
		if ((mods & InputEvent.BUTTON3_MASK) != 0) {
			// 右键不响应拖拽
			return;
		}

		DigraphUI.autoscroll(graph, e.getPoint());
		if (graph.isEnabled()) {
			if (handler != null && handler == graphUI.getMarqueeHandler())
				graphUI.getMarqueeHandler().mouseDragged(e);
			else if (handler == null && !graphUI.isEditing(graph)
					&& focus != null) {
				if (!graph.isCellSelected(focus.getCell())) {
					graphUI.selectCellForEvent(focus.getCell(), e);
					cell = null;
				}
				if (graphUI.getCellHandle() != null)
					graphUI.getCellHandle().mousePressed(e);
				handler = graphUI.getCellHandle();
			}
			if (graphUI.getCellHandle() != null && handler == graphUI.getCellHandle()) {

				graphUI.getCellHandle().mouseDragged(e);
				//
				//					
				//					
				// int s = graph.getTolerance();
				// Rectangle2D r = graph.fromScreen(new Rectangle2D.Double(e
				// .getX()
				// - s, e.getY() - s, 2 * s, 2 * s));
				//					
				//					
				// CellView newFocus = (focus != null &&
				// focus.intersects(graph, r)) ? focus
				// : null;
				// CellView newCellView =
				// graph.getNextSelectableViewAt(newFocus, e.getX(),
				// e.getY());
				//					
				//					
				// UIDigraph uiDigraph = getUiDigraph();
				// Set<String> vertexIds = uiDigraph.getAllVertexIDs();
				// DefaultGraphCell newCell =
				// (DefaultGraphCell)newCellView.getCell();
				// AbstractVertexModel vertextID =
				// (AbstractVertexModel)newCell.getUserObject();
				// if(vertexIds.contains(vertextID.getVertexId())){
				// handle.mouseDragged(e);
				// }else {
				// return;
				// }
			}
		}
	}

	/**
	 * Invoked when the mouse pointer has been moved on a component (with no
	 * buttons down).
	 */
	public void mouseMoved(MouseEvent e) {
		if (previousCursor == null) {
			previousCursor = graph.getCursor();
		}
		if (graph != null && graph.isEnabled()) {
			if (graphUI.getMarqueeHandler() != null)
				graphUI.getMarqueeHandler().mouseMoved(e);
			if (graphUI.getCellHandle() != null)
				graphUI.getCellHandle().mouseMoved(e);
			if (!e.isConsumed() && previousCursor != null) {
				Cursor currentCursor = graph.getCursor();
				if (currentCursor != previousCursor) {
					graph.setCursor(previousCursor);
				}
				previousCursor = null;
			}
		}
	}

	// Event may be null when called to cancel the current operation.
	public void mouseReleased(MouseEvent e) {

		try {
			if (e != null && !e.isConsumed() && graph != null
					&& graph.isEnabled()) {
				if (handler == graphUI.getMarqueeHandler() && graphUI.getMarqueeHandler() != null)
					graphUI.getMarqueeHandler().mouseReleased(e);
				else if (handler == graphUI.getCellHandle() && graphUI.getCellHandle() != null)
					graphUI.getCellHandle().mouseReleased(e);
				if (isDescendant(cell, focus) && e.getModifiers() != 0) {
					// Do not switch to parent if Special Selection
					cell = focus;

					// TODO 设置当前操作的cell，　 wrr 修改，为了显示选中的节点属性201201
					// if (cell != null) {
					// DefaultGraphCell graphCell = (DefaultGraphCell) cell
					// .getCell();
					// if (graphCell != null) {
					// // 选中节点
					// getUiDigraph().setSelectedCell(graphCell,
					// GraphOperator.MouseRelease);
					// }
					// }

				}
				if (!e.isConsumed() && cell != null) {
					Object tmp = cell.getCell();
					boolean wasSelected = graph.isCellSelected(tmp);
					if (e.isPopupTrigger() || !wasSelected) {
						// if (!wasSelected) {
						graphUI.selectCellForEvent(tmp, e);
						focus = cell;
						postProcessSelection(e, tmp, wasSelected);
					}
				}
			}
		} finally {
			handler = null;
			cell = null;
		}
	}

	/**
	 * Invoked after a cell has been selected in the mouseReleased method. This
	 * can be used to do something interesting if the cell was already selected,
	 * in which case this implementation selects the parent. Override if you
	 * want different behaviour, such as start editing.
	 */
	protected void postProcessSelection(MouseEvent e, Object cell,
			boolean wasSelected) {
		if (wasSelected && graph.isCellSelected(cell) && e.getModifiers() != 0) {
			Object parent = cell;
			Object nextParent = null;
			while (((nextParent = graphModel.getParent(parent)) != null)
					&& graphLayoutCache.isVisible(nextParent))
				parent = nextParent;
			graphUI.selectCellForEvent(parent, e);
			lastFocus = focus;
			focus = graphLayoutCache.getMapping(parent, false);
		}
	}

	protected boolean isDescendant(CellView parentView, CellView childView) {
		if (parentView == null || childView == null) {
			return false;
		}

		Object parent = parentView.getCell();
		Object child = childView.getCell();
		Object ancestor = child;

		do {
			if (ancestor == parent)
				return true;
		} while ((ancestor = graphModel.getParent(ancestor)) != null);

		return false;
	}

}
