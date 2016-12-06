/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.eventhandler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import nc.bs.logging.Logger;
import nc.ui.mmgp.pub.beans.digraph.UIDigraph;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-2-21
 * 
 * @author wangweiu
 */
public class DigraphMarqueeHandler extends BasicMarqueeHandler {

    protected Point2D start;

    /** Current point (where mouse was dragged). */
    protected Point2D current;

    /** Current port (when mouse was pressed). */
    protected PortView _port;

    /** First port (when mouse was pressed). */
    protected PortView firstPort;

    /** Last port (when mouse was draged). */
    protected PortView lastPort;

    private Color transitionColor = Color.darkGray;

    private UIDigraph sourceDigraph = null;

    /**
     * @param digraph
     */
    public DigraphMarqueeHandler(UIDigraph digraph) {
        sourceDigraph = digraph;
    }

    /**
     * @return the sourceDigraph
     */
    public UIDigraph getSourceDigraph() {
        return sourceDigraph;
    }

    @Override
    public boolean isForceMarqueeEvent(MouseEvent e) {
        return !sourceDigraph.isSelectMode() || super.isForceMarqueeEvent(e);
    }

    /**
     * ������¼�<br>
     * ��Select����ť��ѡ�У��ҷ��Ҽ�����������ͼԪ��
     */
    @Override
    public void mousePressed(MouseEvent ev) {
        if (SwingUtilities.isLeftMouseButton(ev)) {
            if (!sourceDigraph.isSelectMode()) {

                start = getJGraph().snap(ev.getPoint());
                firstPort = _port; // ��ǰ���λ�õ�PortView

                if (sourceDigraph.isAddVertexMode()) {
                    AbstractVertexModel vm =
                            sourceDigraph.createDefaultVertexModel(ev.getPoint().getX() / getJGraph().getScale(), ev
                                .getPoint()
                                .getY()
                                / getJGraph().getScale(), 80, 50);
                    sourceDigraph.getDigraphModel().addVertex(vm);
                } 
                
                else if (firstPort != null && isAddEdge()) {
                    Point2D point = firstPort.getLocation();
                    start = getScalePoint(point);
                }
            }
        } else if (SwingUtilities.isRightMouseButton(ev)) {
            sourceDigraph.resetMode();
        }

        if (!ev.isConsumed() && SwingUtilities.isLeftMouseButton(ev)) {
            super.mousePressed(ev);
        }
    }

    private boolean isAddEdge() {
        return (sourceDigraph.isAddEdgeMode() || sourceDigraph.isAddRoutedEdgeMode());
    }

    private Point2D getScalePoint(Point2D p) {
        return new Point2D.Double(getScaleValue(p.getX()), getScaleValue(p.getY()));
    }

    private double getScaleValue(double value) {
        return value * getJGraph().getScale();
    }

    /**
     * ����϶��¼�<br>
     */
    @Override
    public void mouseDragged(MouseEvent ev) {
        if (!ev.isConsumed() && isAddEdge() && (firstPort != null) && !SwingUtilities.isRightMouseButton(ev)) {
        	// ����
            Graphics g = getJGraph().getGraphics();
            Color bg = getJGraph().getBackground();
            Color fg = transitionColor;
            g.setColor(fg);
            g.setXORMode(bg);
            overlay(getJGraph(), g, true);
            current = getJGraph().snap(ev.getPoint());

            _port = getPortViewAt(ev.getX(), ev.getY());
            if (_port != null) {
                current = getScalePoint(_port.getLocation());
            }
            g.setColor(bg);
            g.setXORMode(fg);
            overlay(getJGraph(), g, false);
            ev.consume();
        }
        else{
        	//wrr �޸�  ֧�� ��ѡ������
        	if(!isAddEdge()){
        		// ֻ���ڷ����ӱ�״̬�²��ܶ�ѡ����
        		super.mouseDragged(ev);
        	}
        }
    }

    /**
     * ����ƶ��¼����� ��Ҫ˵��
     * 
     * @see org.jgraph.graph.BasicMarqueeHandler#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent ev) {
        super.mouseMoved(ev);
        if (!ev.isConsumed() && !sourceDigraph.isSelectMode()) {
            // �����ѡ�񡱰�ťû��ѡȡ���Ҳ�������֯��������ק״̬�������ٸ�����ƶ��¼�
            ev.consume();

            if (sourceDigraph.isAddEdgeMode() || sourceDigraph.isAddRoutedEdgeMode()) {
                // �����ǰѡ�С�ת�ơ���ť,������֯��������ק״̬
                PortView oldPort = _port;
                PortView newPort = getPortViewAt(ev.getX(), ev.getY());
                if (oldPort != newPort) {
                    Graphics g = getJGraph().getGraphics();
                    Color bg = getJGraph().getBackground();
                    Color fg = getJGraph().getMarqueeColor();
                    
                    g.setColor(fg);
                    g.setXORMode(bg);
                    overlay(getJGraph(), g, false);
                    _port = newPort;
                    g.setColor(bg);
                    g.setXORMode(fg);
                    overlay(getJGraph(), g, true);
                }
            }
        }
    }

    /**
     * ����ͷ��¼� <li>1.���"Transition"��ť��ѡ��,���������,�����"ת��"ͼԪ <li>2.���ѡȡ��Ϊ��,��ѡ��ѡȡ��������ͼԪ
     */
    public void mouseReleased(MouseEvent ev) {
    	// ���ӱ�
        if (ev != null && !ev.isConsumed() && isAddEdge() && !SwingUtilities.isRightMouseButton(ev)) {
            // �����ת�ơ���ť��ѡ�У�����롰ת�ơ�ͼԪ
            if (firstPort != null && _port != null && _port != firstPort) {
                DefaultGraphCell edgeStart = (DefaultGraphCell) ((DefaultPort) firstPort.getCell()).getParent();
                DefaultGraphCell edgeEnd = (DefaultGraphCell) ((DefaultPort) _port.getCell()).getParent();
                AbstractVertexModel vertexStart = (AbstractVertexModel) edgeStart.getUserObject();
                AbstractVertexModel vertexEnd = (AbstractVertexModel) edgeEnd.getUserObject();
                
                boolean canConn = checkCanConnect(vertexStart, vertexEnd);
                
                AbstractEdgeModel existEdgeSame =
                        sourceDigraph.getDigraphModel().getEdge(vertexStart.getVertexId(), vertexEnd.getVertexId());
                AbstractEdgeModel existEdgeOpposite =
                        sourceDigraph.getDigraphModel().getEdge(vertexEnd.getVertexId(), vertexStart.getVertexId());
                if (existEdgeSame == null && existEdgeOpposite == null && canConn) {
                    AbstractEdgeModel em = sourceDigraph.createDefaultEdgeModel(vertexStart, vertexEnd);
                    sourceDigraph.getDigraphModel().addEdge(em);
                }else{
 
                	// wrr update �������������Ѿ��������ģ��������õı�  20130326
    				if (!(ev.getSource() instanceof JGraph))
    					throw new IllegalArgumentException("MarqueeHandler cannot "
    							+ "handle event from unknown source: " + ev);
    				JGraph graph = (JGraph) ev.getSource();
    				
    				Point2D start = getScalePoint(firstPort.getLocation());
    				Point2D end = getScalePoint(_port.getLocation());
    				
    				int width = (int)end.getX() - (int)start.getX();
    				int height = (int)end.getY() - (int)start.getY();
    				
    				int absWidth = Math.abs(width);
    				int absHeight = Math.abs(height);
    				
    				// TODO ��ȷ�ػ�
    				Rectangle dirty = new Rectangle((int)start.getX() - absWidth - 5,
    						(int) start.getY()-absHeight - 5,2*absWidth + 5, 2*absHeight + 5);
    				dirty.width++;
    				dirty.height++;
    				graph.repaint(dirty);      
    			
                }
            } else {
                Graphics g = getJGraph().getGraphics();
                Color bg = getJGraph().getBackground();
                Color fg = transitionColor;
                g.setColor(fg);
                g.setXORMode(bg);
                _port = firstPort;
                overlay(getJGraph(), g, false);
            }
            ev.consume();
            
            // ѡ�С�ѡ�񡱰�ť���ָ����
            firstPort = null;
            _port = null;
            start = null;
            current = null;
            
        }
        else {
        	// wrr ���ӣ� Ϊ��ʹ��ס��������ק�������ķ�����û��ѡ���κνڵ��ߵ�����£���������
        	super.mouseReleased(ev);
        }
//        for(String id:sourceDigraph.get)
//        sourceDigraph.getVertexUI(id)
//        Rectangle2D bounds = GraphConstants.getBounds(vertext.getAttributes());


    }
    
    protected boolean checkCanConnect(AbstractVertexModel vertexStart,
			AbstractVertexModel vertexEnd) {
    	Logger.info("wrr-��DigraphMarqueeHandlerִ����checkCanConnect");
		return true;
	}

	/**
     * �õ���ǰ���λ�õ�PortView����<br>
     * _port ��ֵ
     */
    private PortView getPortViewAt(double x,
                                   double y) {
        Point2D sp = getJGraph().fromScreen(new Point2D.Double(x, y));
        PortView port = getJGraph().getPortViewAt(sp.getX(), sp.getY());

        if (port == null) {
            Object cell = getJGraph().getFirstCellForLocation(x, y);
            if (cell == null) {
                return null;
            }

            if (isVertex(cell)) {
                int childCount = getJGraph().getModel().getChildCount(cell);
                if (childCount <= 0) return null;
                Object firstChild = getJGraph().getModel().getChild(cell, 0);
                // ������Cell��View����
                CellView firstChildView = getJGraph().getGraphLayoutCache().getMapping(firstChild, false);
                if (firstChildView instanceof PortView) port = (PortView) firstChildView;
            }
        }
        return port;
    }

    /**
     * ���ǻ��Ʒ���<br>
     * ������������ƶ��¼�������ͷ��¼�������϶��¼�<br>
     * 1������ѡ���<br>
     * 2������Port<br>
     * 3������ǡ�edge����ѡȡ�������һ��ֱ��<br>
     */
    @Override
    public void overlay(JGraph graph,
                        Graphics g,
                        boolean clear) {
//    	super.overlay(graph, g, clear);
        boolean isDrawLine = start != null && isAddEdge() && current != null;
        if (!isDrawLine) {
            super.overlay(graph, g, clear);
        }

        // ���Ƶ�ǰCell�Ľ���߿�
        paintPort(graph.getGraphics());
        if (isDrawLine) {
            // ����ǡ�edge����ѡȡ�������һ��ֱ��
            g.drawLine((int) start.getX(), (int) start.getY(), (int) current.getX(), (int) current.getY());
        }
    }

    /**
     * ��ѡ�С�edge����������ƶ���ӵ��Port��Cell��ʱ�� ���Ƶ�ǰCell�Ľ���߿�
     * 
     * @param g
     */
	protected void paintPort(Graphics g) {
    	 if (_port != null) {
//             Map attr = _port.getAllAttributes();
//             Point2D p = GraphConstants.getOffset(attr);
//             boolean offset = (p != null);
  
             // r ����PortView�Ĵ�С��ParentCellView�Ĵ�С
             java.awt.geom.Rectangle2D r = _port.getBounds();
//             java.awt.geom.Rectangle2D r = (offset)
//                 ? _port.getBounds()
//                 : _port.getParentView().getBounds();
             
             r = getJGraph().toScreen(r);
             int s = 5;
             r.getBounds().translate( -s, -s);
             r.getBounds().setSize((int)r.getWidth() + 2 * s, (int)r.getHeight() + 2 * s);
             
//             getJGraph().getUI().paintCell(g, _port, r, true);
             getJGraph().getUI().paintCell(g, _port, r, true);
         }

//        if (_port != null) {
//            boolean offset = (GraphConstants.getOffset(_port.getAllAttributes()) != null);
//
//            // ע�⣺r ����PortView�Ĵ�С��ParentCellView�Ĵ�С
//            Rectangle2D r = (offset) ? _port.getBounds() : _port.getParentView().getBounds();
//            // Scale from Model to Screen
//            r = getJGraph().toScreen((Rectangle2D) r.clone());
//
//            int s = 3;
//            r.setFrame(r.getX() - s, r.getY() - s, r.getWidth() + 2 * s, r.getHeight() + 2 * s);
//
//            getJGraph().getUI().paintCell(g, _port, r, true);
//        }

    }

    /**
     * �ж�ͼԪ�Ƿ�Ϊ���㣨���Ƕ˿ڡ��Ǳߡ����飩
     * 
     * @param object
     *        Object
     * @return boolean
     */
    private boolean isVertex(Object object) {
        if (!(object instanceof Port) && !(object instanceof Edge)) {
            return !isGroup(object) && object != null;
        }
        return false;
    }

    /**
     * �ж�ͼԪ�Ƿ�Ϊ���ڵ�
     * 
     * @param cell
     *        Object
     * @return boolean
     */
    public boolean isGroup(Object cell) {
        CellView view = getJGraph().getGraphLayoutCache().getMapping(cell, false);
        if (view != null) {
            return !view.isLeaf();
        }
        return false;
    }

    private JGraph getJGraph() {
        return sourceDigraph.getJGraph();
    }

}
