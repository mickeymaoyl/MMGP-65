package nc.ui.mmgp.pub.beans.digraph.eventhandler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Map;

import javax.swing.JViewport;
import javax.swing.TransferHandler;

import nc.bs.logging.Logger;
import nc.ui.mmgp.pub.beans.digraph.DigraphUI;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.jgraph.JGraph;
import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.PortView;

public class DigraphRootHandler implements CellHandle, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7425362306445883014L;

	// x and y offset from the mouse press event to the left/top corner of a
	// view that is returned by a findViewForPoint().
	// These are used only when the isSnapSelectedView mode is enabled.
	protected transient double _mouseToViewDelta_x = 0;

	protected transient double _mouseToViewDelta_y = 0;

	protected transient boolean firstDrag = true;

	/* Temporary views for the cells. */
	protected transient CellView[] views;

	protected transient CellView[] contextViews;

	protected transient CellView[] portViews;

	protected transient CellView targetGroup, ignoreTargetGroup;

	/* Bounds of the cells. Non-null if too many cells. */
	protected transient Rectangle2D cachedBounds;

	/* Initial top left corner of the selection */
	protected transient Point2D initialLocation;

	/* Child handles. Null if too many handles. */
	protected transient CellHandle[] handles;

	/* The point where the mouse was pressed. */
	protected transient Point2D start = null, last, snapStart, snapLast;

	/** Reference to graph off screen graphics */
	protected transient Graphics offgraphics;

	/**
	 * Indicates whether this handle is currently moving cells. Start may be
	 * non-null and isMoving false while the minimum movement has not been
	 * reached.
	 */
	protected boolean isMoving = false;

	/**
	 * Indicates whether this handle has started drag and drop. Note: isDragging
	 * => isMoving.
	 */
	protected boolean isDragging = false;

	/** The handle that consumed the last mousePressedEvent. Initially null. */
	protected transient CellHandle activeHandle = null;

	/* The current selection context, responsible for cloning the cells. */
	protected transient GraphContext context;

	/*
	 * True after the graph was repainted to block xor-ed painting of
	 * background.
	 */
	protected boolean isContextVisible = true;

	protected boolean blockPaint = false;

	protected Point2D current;

	/* Defines the Disconnection if DisconnectOnMove is True */
	protected transient ConnectionSet disconnect = null;

	protected JGraph graph;

	protected DigraphUI graphUI;

	public static int MAXCELLS;

	public static int MAXHANDLES;

	public static final boolean DNDPREVIEW = DigraphUI.DNDPREVIEW;

	protected GraphModel graphModel;

	protected GraphLayoutCache graphLayoutCache;

	protected boolean snapSelectedView;

	protected Dimension preferredSize;

	public static int SCROLLBORDER;

	/**
	 * Creates a root handle which contains handles for the given cells. The
	 * root handle and all its childs point to the specified JGraph instance.
	 * The root handle is responsible for dragging the selection.
	 */
	public DigraphRootHandler(GraphContext ctx, DigraphUI digraphUI) {
		graphUI = digraphUI;
		graph = digraphUI.getDiGraph();
		MAXCELLS = DigraphUI.MAXCELLS;
		MAXHANDLES = DigraphUI.MAXHANDLES;
		graphModel = graphUI.getGraphModel();
		graphLayoutCache = graphUI.getGraphLayoutCache();
		snapSelectedView = graphUI.getSnapSelectedView();
		preferredSize = graphUI.getPreferredSize();
		SCROLLBORDER = graphUI.getScrollBorder();

		this.context = ctx;
		if (!ctx.isEmpty()) {
			// Temporary cells
			views = ctx.createTemporaryCellViews();
			Rectangle2D tmpBounds = graph.toScreen(graph.getCellBounds(ctx
					.getCells()));
			if (ctx.getDescendantCount() < MAXCELLS) {
				contextViews = ctx.createTemporaryContextViews();
				initialLocation = graph.toScreen(getInitialLocation(ctx
						.getCells()));
			} else
				cachedBounds = tmpBounds;
			if (initialLocation == null && tmpBounds != null) {
				initialLocation = new Point2D.Double(tmpBounds.getX(),
						tmpBounds.getY());
			}
			// Sub-Handles
			Object[] cells = ctx.getCells();
			if (cells.length < MAXHANDLES) {
				handles = new CellHandle[views.length];
				for (int i = 0; i < views.length; i++)
					handles[i] = views[i].getHandle(ctx);
				// PortView Preview
				portViews = ctx.createTemporaryPortViews();
			}
		}
	}

	/**
	 * Returns the initial location, which is the top left corner of the
	 * selection, ignoring all connected endpoints of edges.
	 */
	protected Point2D getInitialLocation(Object[] cells) {
		if (cells != null && cells.length > 0) {
			Rectangle2D ret = null;
			for (int i = 0; i < cells.length; i++) {
				if (graphModel.isEdge(cells[i])) {
					CellView cellView = graphLayoutCache.getMapping(cells[i],
							false);
					if (cellView instanceof EdgeView) {
						EdgeView edgeView = (EdgeView) cellView;
						if (edgeView.getSource() == null) {
							Point2D pt = edgeView.getPoint(0);
							if (pt != null) {
								if (ret == null)
									ret = new Rectangle2D.Double(pt.getX(),
											pt.getY(), 0, 0);
								else
									Rectangle2D.union(ret,
											new Rectangle2D.Double(pt.getX(),
													pt.getY(), 0, 0), ret);
							}
						}
						if (edgeView.getTarget() == null) {
							Point2D pt = edgeView.getPoint(edgeView
									.getPointCount() - 1);
							if (pt != null) {
								if (ret == null)
									ret = new Rectangle2D.Double(pt.getX(),
											pt.getY(), 0, 0);
								else
									Rectangle2D.union(ret,
											new Rectangle2D.Double(pt.getX(),
													pt.getY(), 0, 0), ret);
							}
						}
					}
				} else {
					Rectangle2D r = graph.getCellBounds(cells[i]);
					if (r != null) {
						if (ret == null)
							ret = (Rectangle2D) r.clone();
						Rectangle2D.union(ret, r, ret);
					}
				}
			}
			if (ret != null)
				return new Point2D.Double(ret.getX(), ret.getY());
		}
		return null;
	}

	/* Returns the context of this root handle. */
	public GraphContext getContext() {
		return context;
	}

	/* Paint the handles. Use overlay to paint the current state. */
	public void paint(Graphics g) {
		if (handles != null && handles.length < MAXHANDLES)
			for (int i = 0; i < handles.length; i++)
				if (handles[i] != null)
					handles[i].paint(g);
		blockPaint = true;
		if (!graph.isXorEnabled() && current != null) {
			double dx = current.getX() - start.getX();
			double dy = current.getY() - start.getY();
			if (dx != 0 || dy != 0) {
				overlay(g);
			}
		} else {
			blockPaint = true;
		}
	}

	public void overlay(Graphics g) {
		if (isDragging && !DNDPREVIEW) // BUG IN 1.4.0 (FREEZE)
			return;
		if (cachedBounds != null) { // Paint Cached Bounds
			g.setColor(Color.black);
			g.drawRect((int) cachedBounds.getX(), (int) cachedBounds.getY(),
					(int) cachedBounds.getWidth() - 2,
					(int) cachedBounds.getHeight() - 2);

		} else {
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform oldTransform = g2.getTransform();
			g2.scale(graph.getScale(), graph.getScale());
			if (views != null) { // Paint Temporary Views
				for (int i = 0; i < views.length; i++)
					graphUI.paintCell(g, views[i], views[i].getBounds(), true);
			}
			// Paint temporary context
			if (contextViews != null && isContextVisible) {
				for (int i = 0; i < contextViews.length; i++) {
					graphUI.paintCell(g, contextViews[i],
							contextViews[i].getBounds(), true);
				}
			}
			if (!graph.isPortsScaled())
				g2.setTransform(oldTransform);
			if (portViews != null && graph.isPortsVisible())
				graphUI.paintPorts(g, portViews);
			g2.setTransform(oldTransform);
		}

		// Paints the target group to move into
		if (targetGroup != null) {
			Rectangle2D b = graph.toScreen((Rectangle2D) targetGroup
					.getBounds().clone());
			g.setColor(graph.getHandleColor());
			g.fillRect((int) b.getX() - 1, (int) b.getY() - 1,
					(int) b.getWidth() + 2, (int) b.getHeight() + 2);
			g.setColor(graph.getMarqueeColor());
			g.draw3DRect((int) b.getX() - 2, (int) b.getY() - 2,
					(int) b.getWidth() + 3, (int) b.getHeight() + 3, true);
		}
	}

	/**
	 * Invoked when the mouse pointer has been moved on a component (with no
	 * buttons down).
	 */
	public void mouseMoved(MouseEvent event) {
		if (!event.isConsumed() && handles != null) {
			for (int i = handles.length - 1; i >= 0 && !event.isConsumed(); i--)
				if (handles[i] != null)
					handles[i].mouseMoved(event);
		}
	}

	public void mousePressed(MouseEvent event) {
		if (!event.isConsumed() && graph.isMoveable()) {
			if (handles != null) { // Find Handle
				for (int i = handles.length - 1; i >= 0; i--) {
					if (handles[i] != null) {
						handles[i].mousePressed(event);
						if (event.isConsumed()) {
							activeHandle = handles[i];
							return;
						}
					}
				}
			}
			if (views != null) { // Start Move if over cell
				Point2D screenPoint = event.getPoint();
				Point2D pt = graph.fromScreen((Point2D) screenPoint.clone());
				CellView view = findViewForPoint(pt);
				if (view != null) {
					if (snapSelectedView) {
						Rectangle2D bounds = view.getBounds();
						start = graph.toScreen(new Point2D.Double(
								bounds.getX(), bounds.getY()));
						snapStart = graph.snap((Point2D) start.clone());
						_mouseToViewDelta_x = screenPoint.getX() - start.getX();
						_mouseToViewDelta_y = screenPoint.getY() - start.getY();
					} else { // this is the original RootHandle's mode.
						snapStart = graph.snap((Point2D) screenPoint.clone());
						_mouseToViewDelta_x = snapStart.getX()
								- screenPoint.getX();
						_mouseToViewDelta_y = snapStart.getY()
								- screenPoint.getY();
						start = (Point2D) snapStart.clone();
					}
					last = (Point2D) start.clone();
					snapLast = (Point2D) snapStart.clone();
					isContextVisible = contextViews != null
							&& contextViews.length < MAXCELLS
							&& (!event.isControlDown() || !graph.isCloneable());
					event.consume();
				}
			}
			// Analyze for common parent
			if (graph.isMoveIntoGroups() || graph.isMoveOutOfGroups()) {
				Object[] cells = context.getCells();
				Object ignoreGroup = graph.getModel().getParent(cells[0]);
				for (int i = 1; i < cells.length; i++) {
					Object tmp = graph.getModel().getParent(cells[i]);
					if (ignoreGroup != tmp) {
						ignoreGroup = null;
						break;
					}
				}
				if (ignoreGroup != null)
					ignoreTargetGroup = graph.getGraphLayoutCache().getMapping(
							ignoreGroup, false);
			}
		}
	}

	/**
	 * Hook for subclassers to return a different view for a mouse click at
	 * <code>pt</code>. For example, this can be used to return a leaf cell
	 * instead of a group.
	 */
	protected CellView findViewForPoint(Point2D pt) {
		double snap = graph.getTolerance();
		Rectangle2D r = new Rectangle2D.Double(pt.getX() - snap, pt.getY()
				- snap, 2 * snap, 2 * snap);
		for (int i = 0; i < views.length; i++)
			if (views[i].intersects(graph, r))
				return views[i];
		return null;
	}

	/**
	 * Used for move into group to find the target group.
	 */
	protected CellView findUnselectedInnermostGroup(double x, double y) {
		Object[] cells = graph.getDescendants(graph.getRoots());
		for (int i = cells.length - 1; i >= 0; i--) {
			CellView view = graph.getGraphLayoutCache().getMapping(cells[i],
					false);
			if (view != null && !view.isLeaf()
					&& !context.contains(view.getCell())
					&& view.getBounds().contains(x, y))
				return view;
		}
		return null;
	}

	protected void startDragging(MouseEvent event) {
		isDragging = true;
		if (graph.isDragEnabled()) {
			int action = (event.isControlDown() && graph.isCloneable()) ? TransferHandler.COPY
					: TransferHandler.MOVE;
			TransferHandler th = graph.getTransferHandler();
			graphUI.setInsertionLocation(event.getPoint());
			try {
				th.exportAsDrag(graph, event, action);
			} catch (Exception ex) {
				// Ignore
			}
		}
	}

	/**
	 * @return Returns the parent graph scrollpane for the specified graph.
	 */
	public Component getFirstOpaqueParent(Component component) {
		if (component != null) {
			Component parent = component;
			while (parent != null) {
				if (parent.isOpaque() && !(parent instanceof JViewport))
					return parent;
				parent = parent.getParent();
			}
		}
		return component;
	}

	protected void initOffscreen() {
		if (!graph.isXorEnabled()) {
			return;
		}
		try {
			offgraphics = graph.getOffgraphics();
		} catch (Exception e) {
			offgraphics = null;
		} catch (Error e) {
			offgraphics = null;
		}
	}

	/** Process mouse dragged event. */
	public void mouseDragged(MouseEvent event) {
		boolean constrained = graphUI.isConstrainedMoveEvent(event);
		boolean spread = false;
		Rectangle2D dirty = null;
		if (firstDrag && graph.isDoubleBuffered() && cachedBounds == null) {
			initOffscreen();
			firstDrag = false;
		}
		if (event != null && !event.isConsumed()) {
			if (activeHandle != null) // Paint Active Handle
				activeHandle.mouseDragged(event);
			// Invoke Mouse Dragged
			else if (start != null) { // Move Cells
				Graphics g = (offgraphics != null) ? offgraphics : graph
						.getGraphics();
				Point ep = event.getPoint();
				Point2D point = new Point2D.Double(ep.getX()
						- _mouseToViewDelta_x, ep.getY() - _mouseToViewDelta_y);
				Point2D snapCurrent = graph.snap(point);
				current = snapCurrent;
				int thresh = graph.getMinimumMove();
				double dx = current.getX() - start.getX();
				double dy = current.getY() - start.getY();
				if (isMoving || Math.abs(dx) > thresh || Math.abs(dy) > thresh) {
					boolean overlayed = false;
					isMoving = true;
					if (disconnect == null && graph.isDisconnectOnMove())
						disconnect = context.disconnect(graphLayoutCache
								.getAllDescendants(views));
					// Constrained movement
					double totDx = current.getX() - start.getX();
					double totDy = current.getY() - start.getY();
					dx = current.getX() - last.getX();
					dy = current.getY() - last.getY();
					Point2D constrainedPosition = constrainDrag(event, totDx,
							totDy, dx, dy);
					if (constrainedPosition != null) {
						dx = constrainedPosition.getX();
						dy = constrainedPosition.getY();
					}
					double scale = graph.getScale();
					dx = dx / scale;
					dy = dy / scale;
					// Start Drag and Drop
					if (graph.isDragEnabled() && !isDragging)
						startDragging(event);
					if (dx != 0 || dy != 0) {
						if (offgraphics != null || !graph.isXorEnabled()) {
							dirty = graph.toScreen(AbstractCellView
									.getBounds(views));
							Rectangle2D t = graph.toScreen(AbstractCellView
									.getBounds(contextViews));
							if (t != null)
								dirty.add(t);
						}
						if (graph.isXorEnabled()) {
							g.setColor(graph.getForeground());

							// use 'darker' to force XOR to distinguish
							// between
							// existing background elements during drag
							// http://sourceforge.net/tracker/index.php?func=detail&aid=677743&group_id=43118&atid=435210
							g.setXORMode(graph.getBackground().darker());
						}
						if (!snapLast.equals(snapStart)
								&& (offgraphics != null || !blockPaint)) {
							if (graph.isXorEnabled()) {
								overlay(g);
							}
							overlayed = true;
						}
						isContextVisible = (!event.isControlDown() || !graph
								.isCloneable())
								&& contextViews != null
								&& (contextViews.length < MAXCELLS);
						blockPaint = false;
						if (constrained && cachedBounds == null) {
							// Reset Initial Positions
							CellView[] all = graphLayoutCache
									.getAllDescendants(views);
							for (int i = 0; i < all.length; i++) {
								CellView orig = graphLayoutCache.getMapping(
										all[i].getCell(), false);
								AttributeMap attr = orig.getAllAttributes();
								all[i].changeAttributes(
										graph.getGraphLayoutCache(),
										(AttributeMap) attr.clone());
								all[i].refresh(graph.getGraphLayoutCache(),
										context, false);
							}
						}
						if (cachedBounds != null) {
							if (dirty != null) {
								dirty.add(cachedBounds);
							}
							cachedBounds.setFrame(cachedBounds.getX() + dx
									* scale, cachedBounds.getY() + dy * scale,
									cachedBounds.getWidth(),
									cachedBounds.getHeight());
							if (dirty != null) {
								dirty.add(cachedBounds);
							}
						} else {
							// Translate
							GraphLayoutCache.translateViews(views, dx, dy);
							if (views != null)
								graphLayoutCache.update(views);
							if (contextViews != null)
								graphLayoutCache.update(contextViews);
						}
						// Change preferred size of graph
						if (graph.isAutoResizeGraph()
								&& (event.getX() > graph.getWidth()
										- SCROLLBORDER || event.getY() > graph
										.getHeight() - SCROLLBORDER)) {

							int SPREADSTEP = 25;
							Rectangle view = null;
							if (graph.getParent() instanceof JViewport)
								view = ((JViewport) graph.getParent())
										.getViewRect();
							if (view != null) {
								if (view.contains(event.getPoint())) {
									if (view.x + view.width
											- event.getPoint().x < SCROLLBORDER) {
										preferredSize.width = Math.max(
												preferredSize.width,
												(int) view.getWidth())
												+ SPREADSTEP;
										spread = true;
									}
									if (view.y + view.height
											- event.getPoint().y < SCROLLBORDER) {
										preferredSize.height = Math.max(
												preferredSize.height,
												(int) view.getHeight())
												+ SPREADSTEP;
										spread = true;
									}
									if (spread) {
										graph.revalidate();
										DigraphUI.autoscroll(graph,
												event.getPoint());
										if (graph.isDoubleBuffered())
											initOffscreen();
									}
								}
							}
						}

						// Move into groups
						Rectangle2D ignoredRegion = (ignoreTargetGroup != null) ? (Rectangle2D) ignoreTargetGroup
								.getBounds().clone() : null;
						if (targetGroup != null) {
							Rectangle2D tmp = graph
									.toScreen((Rectangle2D) targetGroup
											.getBounds().clone());
							if (dirty != null)
								dirty.add(tmp);
							else
								dirty = tmp;
						}
						targetGroup = null;
						if (graph.isMoveIntoGroups()
								&& (ignoredRegion == null || !ignoredRegion
										.intersects(AbstractCellView
												.getBounds(views)))) {
							targetGroup = (event.isControlDown()) ? null
									: findUnselectedInnermostGroup(
											snapCurrent.getX() / scale,
											snapCurrent.getY() / scale);
							if (targetGroup == ignoreTargetGroup)
								targetGroup = null;
						}
						if (!snapCurrent.equals(snapStart)
								&& (offgraphics != null || !blockPaint)
								&& !spread) {
							if (graph.isXorEnabled()) {
								overlay(g);
							}
							overlayed = true;
						}
						if (constrained)
							last = (Point2D) start.clone();
						last.setLocation(last.getX() + dx * scale, last.getY()
								+ dy * scale);
						// It is better to translate <code>last<code> by a
						// scaled dx/dy
						// instead of making it to be the
						// <code>current<code> (as in prev version),
						// so that the view would be catching up with a
						// mouse pointer
						snapLast = snapCurrent;
						if (overlayed
								&& (offgraphics != null || !graph
										.isXorEnabled())) {
							if (dirty == null) {
								dirty = new Rectangle2D.Double();
							}
							dirty.add(graph.toScreen(AbstractCellView
									.getBounds(views)));
							Rectangle2D t = graph.toScreen(AbstractCellView
									.getBounds(contextViews));
							if (t != null)
								dirty.add(t);
							// TODO: Should use real ports if portsVisible
							// and check if ports are scaled
							int border = PortView.SIZE + 4;
							if (graph.isPortsScaled())
								border = (int) (graph.getScale() * border);
							int border2 = border / 2;
							dirty.setFrame(dirty.getX() - border2, dirty.getY()
									- border2, dirty.getWidth() + border,
									dirty.getHeight() + border);
							double sx1 = Math.max(0, dirty.getX());
							double sy1 = Math.max(0, dirty.getY());
							double sx2 = sx1 + dirty.getWidth();
							double sy2 = sy1 + dirty.getHeight();
							if (isDragging && !DNDPREVIEW) // BUG IN 1.4.0
								// (FREEZE)
								return;
							if (offgraphics != null) {
								graph.drawImage((int) sx1, (int) sy1,
										(int) sx2, (int) sy2, (int) sx1,
										(int) sy1, (int) sx2, (int) sy2);
							} else {
								graph.repaint((int) dirty.getX(),
										(int) dirty.getY(),
										(int) dirty.getWidth() + 1,
										(int) dirty.getHeight() + 1);
							}
						}
					}
				} // end if (isMoving or ...)
			} // end if (start != null)
		} else if (event == null)
			graph.repaint();
	}

	/**
	 * Hook method to constrain a mouse drag
	 * 
	 * @param event
	 * @param totDx
	 * @param totDy
	 * @param dx
	 * @param dy
	 * @return a point describing any position constraining applied
	 */
	protected Point2D constrainDrag(MouseEvent event, double totDx,
			double totDy, double dx, double dy) {
		boolean constrained = graphUI.isConstrainedMoveEvent(event);

		if (constrained && cachedBounds == null) {
			if (Math.abs(totDx) < Math.abs(totDy)) {
				dx = 0;
				dy = totDy;
			} else {
				dx = totDx;
				dy = 0;
			}
		} else {
			if (!graph.isMoveBelowZero() && last != null
					&& initialLocation != null && start != null) {
				if (initialLocation.getX() + totDx < 0) {
					// TODO isn't dx always just 0?
					dx = start.getX() - last.getX() - initialLocation.getX();
				}
				if (initialLocation.getY() + totDy < 0) {
					// TODO isn't dy always just 0?
					dy = start.getY() - last.getY() - initialLocation.getY();
				}
			}
			if (!graph.isMoveBeyondGraphBounds() && last != null
					&& initialLocation != null && start != null) {
				Rectangle2D graphBounds = graph.getBounds();
				Rectangle2D viewBounds = AbstractCellView.getBounds(views);
				if (initialLocation.getX() + totDx + viewBounds.getWidth() > graphBounds
						.getWidth())
					dx = 0;
				if (initialLocation.getY() + totDy + viewBounds.getHeight() > graphBounds
						.getHeight())
					dy = 0;
			}
		}

		return new Point2D.Double(dx, dy);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void mouseReleased(MouseEvent event) {
		try {
			if (event == null || event.isConsumed()) {
				return;
			}

			if (activeHandle != null) {
				activeHandle.mouseReleased(event);
				activeHandle = null;
			} else if (isMoving && !event.getPoint().equals(start)) {
				if (cachedBounds != null) {

					Point ep = event.getPoint();
					Point2D point = new Point2D.Double(ep.getX()
							- _mouseToViewDelta_x, ep.getY()
							- _mouseToViewDelta_y);
					Point2D snapCurrent = graph.snap(point);

					double dx = snapCurrent.getX() - start.getX();
					double dy = snapCurrent.getY() - start.getY();

					if (!graph.isMoveBelowZero()
							&& initialLocation.getX() + dx < 0)
						dx = -1 * initialLocation.getX();
					if (!graph.isMoveBelowZero()
							&& initialLocation.getY() + dy < 0)
						dy = -1 * initialLocation.getY();

					Point2D tmp = graph.fromScreen(new Point2D.Double(dx, dy));
					GraphLayoutCache.translateViews(views, tmp.getX(),
							tmp.getY());
				}
				CellView[] all = graphLayoutCache.getAllDescendants(views);
				Map attributes = GraphConstants.createAttributes(all, null);
				if (event.isControlDown() && graph.isCloneable()) { // Clone
					// Cells
					Object[] cells = graph.getDescendants(graph.order(context
							.getCells()));
					// Include properties from hidden cells
					Map hiddenMapping = graphLayoutCache.getHiddenMapping();
					for (int i = 0; i < cells.length; i++) {
						Object witness = attributes.get(cells[i]);
						if (witness == null) {
							CellView view = (CellView) hiddenMapping
									.get(cells[i]);
							if (view != null
									&& !graphModel.isPort(view.getCell())) {
								// TODO: Clone required? Same in
								// GraphConstants.
								AttributeMap attrs = (AttributeMap) view
										.getAllAttributes().clone();
								// Maybe translate?
								// attrs.translate(dx, dy);
								attributes.put(cells[i], attrs.clone());
							}
						}
					}
					ConnectionSet cs = ConnectionSet.create(graphModel, cells,
							false);
					ParentMap pm = ParentMap.create(graphModel, cells, false,
							true);
					cells = graphLayoutCache.insertClones(cells,
							graph.cloneCells(cells), attributes, cs, pm, 0, 0);
				} else if (graph.isMoveable()) { // Move Cells
					ParentMap pm = null;

					// Moves into group
					if (targetGroup != null) {
						// 判断是否能加入到这个组，注意要判断整个组
						boolean checkResult = canMoveIntoGroup(
								context.getCells(), targetGroup.getCell());

						if (checkResult) {
							pm = new ParentMap(context.getCells(),
									targetGroup.getCell());
						}

					} else if (graph.isMoveOutOfGroups()
							&& (ignoreTargetGroup != null && !ignoreTargetGroup
									.getBounds().intersects(
											AbstractCellView.getBounds(views)))) {

						// 判断是否能移出这个组，注意要判断整个组
						// boolean checkResult = canMoveOutGroup(context
						// .getCells(), targetGroup.getCell());
						// if (checkResult) {
						// pm = new ParentMap(context.getCells(), null);
						// }
						pm = new ParentMap(context.getCells(), null);

					}
					graph.getGraphLayoutCache().edit(attributes, disconnect,
							pm, null);
				}
				event.consume();
			}

			postprocessMouseRelease(event);

		} catch (Exception e) {
		    ExceptionUtils.wrappException(e);
		} finally {
			ignoreTargetGroup = null;
			targetGroup = null;
			isDragging = false;
			disconnect = null;
			firstDrag = true;
			current = null;
			start = null;
		}
	}

	protected void postprocessMouseRelease(MouseEvent event) {

	}

	protected boolean canMoveIntoGroup(Object[] cells, Object cell) {
		Logger.info("wrr-类 .DigraphRootHandler.执行了canMoveIntoGroup");
		return true;
	}

	protected boolean canMoveOutGroup(Object[] cells, Object cell) {
		return true;
	}
}