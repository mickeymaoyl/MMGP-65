package nc.ui.mmgp.pub.beans.digraph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import nc.ui.mmgp.pub.beans.digraph.event.EdgeEvent;
import nc.ui.mmgp.pub.beans.digraph.event.GraphOperator;
import nc.ui.mmgp.pub.beans.digraph.event.HierachBuildEvent;
import nc.ui.mmgp.pub.beans.digraph.event.IEdgeListener;
import nc.ui.mmgp.pub.beans.digraph.event.IVertexListener;
import nc.ui.mmgp.pub.beans.digraph.event.VertexEvent;
import nc.ui.mmgp.pub.beans.digraph.eventhandler.DigraphMarqueeHandler;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractCellModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;
import nc.ui.mmgp.pub.beans.digraph.model.impl.DefaultDigraphModel;
import nc.ui.mmgp.pub.beans.digraph.routed.RoutingConst;
import nc.ui.pub.beans.UIPanel;
import nc.vo.mmgp.util.MMStringUtil;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphSelectionModel;

/**
 * ����ͼ������
 *
 * ʵ�ֱ�����
 *
 * @author wangweiu
 */
@SuppressWarnings("serial")
public class UIDigraph extends UIPanel implements Observer {

	/**
	 * �����
	 */
	private UIPanel pnlMain = null;

	/**
	 * JGraphͼ�ν���(����)
	 */
	protected JGraph graph = null;

	/**
	 * �ٴη�װ��һ��ģ�ͣ���JGraph��model������ͼģ��
	 */
	private AbstractDigraphModel digraphModel = null;

	/**
	 * �ߵ���ʽ
	 */
	private EdgeStyle edgeStyle = new EdgeStyle();

	/**
	 * ��ͬ���͵Ľڵ����ʽ����
	 */
	private Map<String, VertexStyle> vertexStyles = new HashMap<String, VertexStyle>();

	/**
	 * ���滭���ϵĽڵ㡣keyΪ���������ɵ�����
	 */
	private Map<String, DefaultGraphCell> allVertex = new HashMap<String, DefaultGraphCell>();

	/**
	 * ���滭���ϵıߡ�keyΪ���������ɵ�����
	 */
	private Map<String, DefaultEdge> allEdge = new HashMap<String, DefaultEdge>();

	/**
	 * �ڵ���ⲿ����
	 */
	private List<IVertexListener> vertextListeners = new ArrayList<IVertexListener>();

	/**
	 * �ߵ��ⲿ����
	 */
	private List<IEdgeListener> edgetListeners = new ArrayList<IEdgeListener>();

	private ActionListener vertextDoubleClick = null;

	/**
	 * �Ƿ����ߵĿ��
	 */
	private boolean customerLineWidth = false;

	/**
	 * ���캯��.
	 */
	public UIDigraph() {
		initalize();
	}

	/**
     *
     */
	protected void initalize() {
		digraphModel = createDigraphModel();
		digraphModel.addObserver(this);

		// ��ʼ������
		setLayout(new BorderLayout());
		setName("uidigraph");

		pnlMain = new UIPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setName("pnlMain");
		pnlMain.setBackground(Color.BLACK);
		UIPanel cmdPanel = createCmdPanel();
		if (cmdPanel != null) {
			pnlMain.add(cmdPanel, BorderLayout.NORTH);
		}
		JScrollPane jscrollpane = new JScrollPane(getJGraph());
		jscrollpane.setName("jscrollpane");
		jscrollpane.setEnabled(true);
		jscrollpane.setBorder(BorderFactory.createLineBorder(new Color(199,199,199)));
		pnlMain.add(jscrollpane, BorderLayout.CENTER);
		add(pnlMain, BorderLayout.CENTER);
	}

	/**
	 * Ĭ�Ϸ���DefaultDigraphModel, #�޷�����ɳ��󷽷�
	 *
	 * @return
	 */
	protected AbstractDigraphModel createDigraphModel() {
		return new DefaultDigraphModel();
	}

	/**
	 * ȡ������ͼģ��
	 */
	public AbstractDigraphModel getDigraphModel() {
		return digraphModel;
	}

	/**
	 * ��ͼ�Ͻ��ж�λ
	 *
	 * @param content
	 */
	public void findNode(String content) {
		for (AbstractVertexModel vertexModel : getDigraphModel()
				.getAllVertexs()) {
			if (MMStringUtil.isEmpty(vertexModel.toString())) {
				continue;
			}
			if (vertexModel.toString().toUpperCase()
					.contains(content.toUpperCase())) {
				DefaultGraphCell cell = allVertex
						.get(vertexModel.getVertexId());
				getJGraph().setSelectionCell(cell);
				// ��λ������
				getJGraph().scrollCellToVisible(cell);
				break;
			}
		}
	}

	/**
	 * @param vertextDoubleClick
	 *            the vertextDoubleClick to set
	 */
	public void setVertextDoubleClick(ActionListener vertextDoubleClick) {
		this.vertextDoubleClick = vertextDoubleClick;
	}

	public void fireVertexDoubleClickEvent(ActionEvent e) {

		if (vertextDoubleClick != null) {
			vertextDoubleClick.actionPerformed(e);
		}
	}

	/**
	 * @return the vertextDoubleClick
	 */
	public ActionListener getVertextDoubleClick() {
		return vertextDoubleClick;
	}

	protected UIPanel createCmdPanel() {
		return null;
	}

	/**
	 * �Խڵ����Ӽ���
	 */
	public void addVertexListener(IVertexListener vl) {
		vertextListeners.add(vl);
	}

	/**
	 * ɾ���ڵ����
	 */
	public void removeVertexListener(IVertexListener vl) {
		vertextListeners.remove(vl);
	}

	/**
	 * �Ա����Ӽ���
	 */
	public void addEdgeListener(IEdgeListener el) {
		edgetListeners.add(el);
	}

	/**
	 * ɾ���߼���
	 */
	public void removeEdgeListener(IEdgeListener el) {
		edgetListeners.remove(el);
	}

	/**
	 * ��ģ�ͱ仯��ʱ����ô˷���,�����ģ��ָ����digraphModel
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		// ����ģ�ͼ�ͼ��
		if (arg == GraphOperator.Clear) {
			clearView();
			return;
		}

		// �жϱ仯���ǽڵ㻹�Ǳ�
		if (arg instanceof VertexEvent) {
			VertexEvent event = (VertexEvent) arg;
			onVertexChanged(event);
		} else if (arg instanceof EdgeEvent) {
			EdgeEvent event = (EdgeEvent) arg;
			onEdgeChanged(event);
		} else if (arg instanceof HierachBuildEvent) {
			// ����ʵ�ֵĿ��ǣ�Ϊ���ؽ�ͼ�ε�Ƕ�׹�ϵ
			List<AbstractVertexModel> model = ((HierachBuildEvent) arg)
					.getModel();
			if (model != null && !model.isEmpty()) {
				// �ؽ�Ƕ�׹�ϵ
				buildHierach(model);
			}
		}
	}

	// ����UIDigraph����������ͼ��ʹ�ã��ʲ��ܶ���ɳ��󷽷�
	protected void buildHierach(List<AbstractVertexModel> model) {

	}

	private void onEdgeChanged(EdgeEvent event) {
		// �������͸��»���
		if (event.getOperator() == GraphOperator.Add
				|| event.getOperator() == GraphOperator.Load) {
			addEdgeUI(event.getSource());
		} else if (event.getOperator() == GraphOperator.Delete) {
			removeEdgeUI(event.getSource());
		}
		if (event.getOperator() != GraphOperator.Load) {
			// ֪ͨ����
			for (IEdgeListener l : edgetListeners) {
				l.onEdgeChange(event);
			}
		}
	}

	private void onVertexChanged(VertexEvent event) {
		// �������͸��»���
		if (event.getOperator() == GraphOperator.Add
				|| event.getOperator() == GraphOperator.Load) {
			addVertexUI(event.getSource());

		} else if (event.getOperator() == GraphOperator.Delete) {
			removeVertexUI(event.getSource());
		} else if (event.getOperator() == GraphOperator.Update
				|| event.getOperator() == GraphOperator.Move) {
			updateVertexUI(event.getSource());
		}

		if (event.getOperator() != GraphOperator.Load) {
			// ֪ͨ����
			for (IVertexListener l : vertextListeners) {
				l.onVertexChange(event);
			}
		}
	}

	/**
	 * ��ջ���.
	 */
	private void clearView() {
		DigraphIdGenerator.clear();
		allEdge = new HashMap<String, DefaultEdge>();
		allVertex = new HashMap<String, DefaultGraphCell>();

		// wrr update �ݹ�ɾ�� 2012-03-16
		Object[] roots = DefaultGraphModel.getRoots(getJGraph().getModel());
		while (roots != null && roots.length != 0) {
			getJGraph().getModel().remove(roots);
			roots = DefaultGraphModel.getRoots(getJGraph().getModel());
		}
	}

	protected DigraphMarqueeHandler getMarqueeHandler() {
		return new DigraphMarqueeHandler(this);
	}

	public JGraph getJGraph() {

		if (graph != null) {
			return graph;
		}

		graph = createDefaultJGraph();
		graph.setMarqueeHandler(getMarqueeHandler());
		// graph.setMarqueeHandler(new BasicMarqueeHandler());
		// TODO
		GraphLayoutCache graphLayoutCache = new GraphLayoutCache(
				graph.getModel(), createCellViewFatory());
		graph.setGraphLayoutCache(graphLayoutCache);

		// GraphTransferable.dataFlavor = new DataFlavor();

		graph.setDisconnectable(false);
		graph.setDisconnectOnMove(false);
		graph.setBendable(false);
		graph.setConnectable(false);
		// graph.setXorEnabled(false);
		// xorEnabled

		// �ر�Cloneable
		graph.setCloneable(false);
		//
		graph.getGraphLayoutCache().setSelectsAllInsertedCells(true);
		graph.getGraphLayoutCache().setSelectsLocalInsertedCells(true);

		// �ҵ���ͼ����ק�ӳٵ�ԭ��
		// graph.setDoubleBuffered(false);

		graph.setGridVisible(true);
		// ����Snap
		graph.setGridEnabled(true);
		graph.setSizeable(true);
		graph.setMoveable(true);
		graph.setLockedHandleColor(Color.RED);
		graph.setHighlightColor(Color.RED);
		GraphConstants.SELECTION_STROKE = new BasicStroke(3f);
		// Ĭ���ǿ��Զ�ѡ
		graph.getSelectionModel().setSelectionMode(
				GraphSelectionModel.MULTIPLE_GRAPH_SELECTION);

		addPopMenu();

		graph.setToolTipText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0000")/*@res "����"*/); // ע��tooltipManager

		// TODO wrr add Ϊ�˽����������
		graph.setMoveIntoGroups(true);
		graph.setMoveOutOfGroups(true);
		graph.setMoveable(true);
		graph.setGroupsEditable(true);
		graph.setDragEnabled(true);
		graph.getGraphLayoutCache().setMovesChildrenOnExpand(true);
		graph.getGraphLayoutCache().setMovesParentsOnCollapse(true);
		graph.getGraphLayoutCache().setAutoSizeOnValueChange(false);
		return graph;
	}

	protected JGraph createDefaultJGraph() {
		return new DigraphChart(new DefaultGraphModel(), this);
	}

	/**
	 * ���ýڵ����ʾ��ʽ
	 */
	public void setVertexStyle(String type, VertexStyle style) {
		vertexStyles.put(type, style);
	}

	public VertexStyle getVertexStyle(String type) {
		return vertexStyles.get(type);
	}

	/**
	 * ���ñߵ���ʾ��ʽ
	 */
	public void setEdgeStyle(EdgeStyle style) {

		edgeStyle = style;
	}

	public EdgeStyle getEdgeStyle() {
		return edgeStyle;
	}

	public void setSelectionMode(int mode) {
		getJGraph().getSelectionModel().setSelectionMode(mode);
	}

	protected DefaultCellViewFactory createCellViewFatory() {
		return new DefaultCellViewFactory();
	}

	protected void addPopMenu() {

	}

	/**
	 * �ڻ��������ӽڵ㡣
	 *
	 * @param vertexData
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addVertexUI(AbstractVertexModel vertexData) {
		Map attrib = new Hashtable();
		DefaultGraphCell vertex = createVertexUI(vertexData, attrib);
		Map attributes = new Hashtable();
		attributes.put(vertex, attrib);

		getJGraph().getModel().insert(new Object[] { vertex }, attributes,
				null, null, null);

		setVertexToolTip(vertex);
	}

	/**
	 * @param vertex
	 */
	private void setVertexToolTip(DefaultGraphCell vertex) {
		CellView view = getJGraph().getGraphLayoutCache().getMapping(vertex,
				false);
		if (view != null) {
			Component c = view.getRendererComponent(getJGraph(), false, false,
					false);
			if (c instanceof JComponent) {
				String toolTip = ((AbstractCellModel) vertex.getUserObject())
						.getToolTipText();
				((JComponent) c).setToolTipText(toolTip);
			}
		}
	}

	/**
	 * �ڻ����ϸ��½ڵ㡣
	 *
	 * @param newVertexData
	 */
	private void updateVertexUI(AbstractVertexModel newVertexData) {
		DefaultGraphCell vertex = allVertex.get(newVertexData.getVertexId());
		AttributeMap attributes = getJGraph().getModel().getAttributes(null);
		Rectangle2D vertexBounds = new Rectangle2D.Double(newVertexData.getX(),
				newVertexData.getY(), newVertexData.getWidth(),
				newVertexData.getHeight());
		GraphConstants.setBounds(vertex.getAttributes(), vertexBounds);
		setVertexStyleAttrib(newVertexData.getType(), vertex.getAttributes());
		getJGraph().getModel().edit(attributes, null, null, null);
		vertex.setUserObject(newVertexData);

		setVertexToolTip(vertex);
		getJGraph().setSelectionCells(new DefaultGraphCell[] { vertex });
	}

	public DefaultGraphCell getVertexUI(String id) {
		return allVertex.get(id);
	}

	/**
	 * �ڻ�����ɾ���ڵ㡣
	 *
	 * @param vertexData
	 */
	private void removeVertexUI(AbstractVertexModel vertexData) {
		DefaultGraphCell vertex = allVertex.get(vertexData.getVertexId());
		getJGraph().getModel().remove(new Object[] { vertex });

		allVertex.remove(vertexData.getVertexId());
	}

	/**
	 * �ڻ��������ӱߡ�
	 *
	 * @param edgedata
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addEdgeUI(AbstractEdgeModel edgedata) {

		Map edgeAttrib = new Hashtable();
		DefaultEdge edge = createEdgeUI(edgedata, edgeAttrib);
		Map attributes = new Hashtable();
		attributes.put(edge, edgeAttrib);

		DefaultGraphCell fromcell = allVertex.get(edgedata
				.getSourceVertexModel().getVertexId());
		Object fromPort = fromcell.getChildAt(0);

		// ��Ϊ����ӽڵ���Ϣ��ʱ���Ѿ����������ŵ�map���ϢΪ(vertexID, DefaultGraphCell)
		DefaultGraphCell tocell = allVertex.get(edgedata.getTargetVertexModel()
				.getVertexId());
		Object toPort = tocell.getChildAt(0);

		ConnectionSet cs = new ConnectionSet(edge, fromPort, toPort);

		getJGraph().getModel().insert(new Object[] { edge }, attributes, cs,
				null, null);
	}

	/**
	 * �ڻ�����ɾ���ߡ�
	 *
	 * @param edgedata
	 */
	private void removeEdgeUI(AbstractEdgeModel edgedata) {
		DefaultEdge edge = allEdge.get(edgedata.getEdgeId());
		getJGraph().getModel().remove(new Object[] { edge });

		allEdge.remove(edgedata.getEdgeId());
	}

	@SuppressWarnings("rawtypes")
	protected DefaultGraphCell createDefaultGraphCell(
			AbstractVertexModel vertex, Map attrib) {

		DefaultGraphCell vertexCell = new DefaultGraphCell();
		// Set bounds
		Rectangle2D worldBounds = new Rectangle2D.Double(vertex.getX(),
				vertex.getY(), vertex.getWidth(), vertex.getHeight());

		GraphConstants.setBounds(attrib, worldBounds);

		return vertexCell;
	}

	/**
	 * �����ڵ�ؼ�.
	 *
	 * @param vertex
	 * @param attrib
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private DefaultGraphCell createVertexUI(AbstractVertexModel vertex,
			Map attrib) {

		DefaultGraphCell vertexCell = createDefaultGraphCell(vertex, attrib);

		// int u = GraphConstants.PERMILLE;
		//
		// vertexCell.addPort(new Point(0, 0),"TopLeft");
		// vertexCell.addPort(new Point(u/2, 0),"TopCenter");
		// vertexCell.addPort(new Point(u, 0),"TopRight");
		// vertexCell.addPort(new Point(0, u/2),"MiddleLeft");
		// vertexCell.addPort(new Point(u, u/2),"MiddleRight");
		// vertexCell.addPort(new Point(0, u),"BottomLeft");
		// vertexCell.addPort(new Point(u/2, u),"BottomCenter");
		// vertexCell.addPort(new Point(u, u),"BottomRight");

		setVertexStyleAttrib(vertex.getType(), attrib);
		vertexCell.setUserObject(vertex);
		allVertex.put(vertex.getVertexId(), vertexCell);

		vertexCell.addPort();
		return vertexCell;
	}

	/**
	 * �����߿ؼ�.
	 *
	 * @param edgeInfo
	 * @param attrib
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private DefaultEdge createEdgeUI(AbstractEdgeModel edgeInfo, Map attrib) {
		DefaultEdge edge = new DefaultEdge();
		edge.setUserObject(edgeInfo);

		setEdgStyleAttrib(attrib, edgeInfo.getStyle());
		if (customerLineWidth) {
			GraphConstants.setLineWidth(attrib,
					((Double) edgeInfo.getLineWidth()).floatValue());
		}

		allEdge.put(edgeInfo.getEdgeId(), edge);
		return edge;
	}

	/**
	 * ���ýڵ���ʽ.
	 *
	 * @param attrib
	 */
	@SuppressWarnings("rawtypes")
	private void setVertexStyleAttrib(String type, Map attrib) {
		VertexStyle style = vertexStyles.get(type);
		if (style == null) {
			return;
		}
		GraphConstants.setAutoSize(attrib, style.isAutoResize());
		GraphConstants.setBackground(attrib, style.getBackground());
		GraphConstants.setOpaque(attrib, style.isOpaque());
		GraphConstants.setBorder(attrib, style.getBorder());
	}

	/**
	 * ���ñ���ʽ.
	 *
	 * @param attrib
	 */
	@SuppressWarnings("rawtypes")
	private void setEdgStyleAttrib(Map attrib, int style) {
		GraphConstants.setLineEnd(attrib, edgeStyle.getLineEnd());
		GraphConstants.setEndFill(attrib, edgeStyle.isEndFill());
		// ����
		if (style == AbstractEdgeModel.STYLE_ROUTED) {
			GraphConstants.setRouting(attrib, RoutingConst.HORIZONTAL_ROUTING);
		}
	}

	public void selectNodes(NodeFilter filter) {
		List<DefaultGraphCell> cells = findCells(filter);
		if (cells.isEmpty()) {
			return;
		}
		getJGraph().setSelectionCells(cells.toArray(new DefaultGraphCell[0]));
		// ��λ������
		getJGraph().scrollCellToVisible(cells.get(0));
	}

	public List<DefaultGraphCell> findCells(NodeFilter filter) {
		List<DefaultGraphCell> result = new ArrayList<DefaultGraphCell>();
		for (AbstractVertexModel vertexModel : getDigraphModel()
				.getAllVertexs()) {
			if (filter.accept(vertexModel)) {
				DefaultGraphCell cell = allVertex
						.get(vertexModel.getVertexId());
				result.add(cell);
			}
		}
		return result;
	}

	@Override
	public void setEnabled(boolean enabled) {
		getJGraph().setSizeable(enabled);
		getJGraph().setMoveable(enabled);

		getJGraph().setEditable(enabled);
		super.setEnabled(enabled);
	}

	public boolean isCustomerLineWidth() {
		return customerLineWidth;
	}

	public void setCustomerLineWidth(boolean customerLineWidth) {
		this.customerLineWidth = customerLineWidth;
	}

	/**
	 * �Ƿ����ӽڵ� #���ս�ģ�Ѿ���ʹ�ã�����ͼʹ��
	 *
	 * @return
	 */
	public boolean isAddVertexMode() {
		return false;
	}

	/**
	 * @return
	 */
	public boolean isAddEdgeMode() {
		return false;
	}

	/**
	 * @return
	 */
	public boolean isSelectMode() {
		return true;
	}

	/**
	 * @return
	 */
	public boolean isAddRoutedEdgeMode() {
		return false;
	}

	/**
	 * @return
	 */
	public void resetMode() {

	}

	public AbstractVertexModel createDefaultVertexModel(double x, double y,
			double width, double height) {
		throw new RuntimeException("createDefaultVertexModel");
	}

	public AbstractEdgeModel createDefaultEdgeModel(
			AbstractVertexModel vertexStart, AbstractVertexModel vertexEnd) {
		throw new RuntimeException("createDefaultEdgeModel");
	}

	public void save() {
		throw new RuntimeException("save");
	}

	public Map<String, DefaultGraphCell> getAllVertex() {
		return allVertex;
	}
}