package nc.ui.mmgp.pub.beans.digraph.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.UIManager;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

/**
 * 从jgraph自带的renderer拷贝出来修改的
 */
public class MMVertexRenderer extends UIPanel implements CellViewRenderer, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 753589233381646166L;

    /** Cache the current shape for drawing. */
    protected transient VertexView view;

    /** Cached hasFocus and selected value. */
    protected transient boolean hasFocus;

    protected transient boolean selected;

    protected transient boolean preview;

    protected transient boolean childrenSelected;

    /** Cached default foreground and default background. */
    protected transient Color defaultForeground;

    protected transient Color defaultBackground;

    protected transient Color bordercolor;

    /** Cached borderwidth. */
    protected transient int borderWidth;

    /** Cached value of the double buffered state */
    protected transient boolean isDoubleBuffered = false;

    /** Cached value of whether the label is to be displayed */
    protected transient boolean labelEnabled;

    protected transient UILabel iconLabel;

    protected transient UILabel textLabel;

    /**
     * Caches values of the colors to be used for painting the cell. The values for gridColor, highlightColor and
     * lockedHandleColor are updated with the respective values from JGraph in getRendererComponent each time a vertex
     * is rendered. To render the selection border, the highlightColor or the lockedHandleColor are used depending on
     * the focused state of the vertex. The gridColor is used to draw the selection border if any child cells are
     * selected. To change these color values, please use the respective setters in JGraph.
     */
    protected transient Color gradientColor = null;

    protected transient Color gridColor = Color.black;

    protected transient Color highlightColor = Color.black;

    protected transient Color lockedHandleColor = Color.black;

    /**
     * Constructs a renderer that may be used to render vertices.
     */
    public MMVertexRenderer() {
        defaultForeground = UIManager.getColor("Tree.textForeground");
        defaultBackground = UIManager.getColor("Tree.textBackground");
        iconLabel = new ImageLabel();
        iconLabel.setSize(100, 40);
        textLabel = new UILabel();
        textLabel.setSize(100, 20);
        setLayout(new BorderLayout());
        add(iconLabel, BorderLayout.CENTER);
        add(textLabel, BorderLayout.SOUTH);
    }

    /**
     * Configure and return the renderer component based on the passed in cell. The value is typically set from
     * messaging the graph with <code>convertValueToString</code>. We recommend you check the value's class and throw an
     * illegal argument exception if it's not correct.
     * 
     * @param graph
     *        the graph that that defines the rendering context.
     * @param view
     *        the cell view that should be rendered.
     * @param sel
     *        whether the object is selected.
     * @param focus
     *        whether the object has the focus.
     * @param preview
     *        whether we are drawing a preview.
     * @return the component used to render the value.
     */
    public Component getRendererComponent(JGraph graph,
                                          CellView view,
                                          boolean sel,
                                          boolean focus,
                                          boolean preview) {
        gridColor = graph.getGridColor();
        highlightColor = graph.getHighlightColor();
        lockedHandleColor = graph.getLockedHandleColor();
        isDoubleBuffered = graph.isDoubleBuffered();
        if (view instanceof VertexView) {
            this.view = (VertexView) view;
            setComponentOrientation(graph.getComponentOrientation());
            if (graph.getEditingCell() != view.getCell()) {
                Object label = graph.convertValueToString(view);
                if (label != null) {
                    textLabel.setText(label.toString());
                } else {
                    textLabel.setText(null);
                }
            } else
                textLabel.setText(null);
            this.hasFocus = focus;
            this.childrenSelected = graph.getSelectionModel().isChildrenSelected(view.getCell());
            this.selected = sel;
            this.preview = preview;
            if (this.view.isLeaf() || GraphConstants.isGroupOpaque(view.getAllAttributes())) {
                installAttributes(view);
            } else {
                resetAttributes();
            }
            return this;
        }
        return null;
    }

    /**
     * Hook for subclassers that is invoked when the installAttributes is not called to reset all attributes to the
     * defaults. <br>
     * Subclassers must invoke the superclass implementation.
     */
    protected void resetAttributes() {
        textLabel.setText(null);
        setBorder(null);
        setOpaque(false);
        setGradientColor(null);
        iconLabel.setIcon(null);
    }

    /**
     * Install the attributes of specified cell in this renderer instance. This means, retrieve every published key from
     * the cells hashtable and set global variables or superclass properties accordingly.
     * 
     * @param view
     *        the cell view to retrieve the attribute values from.
     */

    @SuppressWarnings({ "rawtypes" })
    protected void installAttributes(CellView view) {
        Map map = view.getAllAttributes();
        iconLabel.setIcon(GraphConstants.getIcon(map));
        iconLabel.setOpaque(GraphConstants.isOpaque(map));
        setBorder(GraphConstants.getBorder(map));
        textLabel.setVerticalAlignment(GraphConstants.getVerticalAlignment(map));
        textLabel.setHorizontalAlignment(GraphConstants.getHorizontalAlignment(map));
        textLabel.setVerticalTextPosition(GraphConstants.getVerticalTextPosition(map));
        textLabel.setHorizontalTextPosition(GraphConstants.getHorizontalTextPosition(map));
        bordercolor = GraphConstants.getBorderColor(map);
        borderWidth = Math.max(1, Math.round(GraphConstants.getLineWidth(map)));
        if (getBorder() == null && bordercolor != null)
            setBorder(BorderFactory.createLineBorder(bordercolor, borderWidth));
        Color foreground = GraphConstants.getForeground(map);
        textLabel.setForeground((foreground != null) ? foreground : defaultForeground);
        Color gColor = GraphConstants.getGradientColor(map);
        setGradientColor(gColor);
        Color background = GraphConstants.getBackground(map);
        textLabel.setBackground((background != null) ? background : defaultBackground);
        // textLabel.setFont(GraphConstants.getFont(map));
        labelEnabled = GraphConstants.isLabelEnabled(map);
    }

    /**
     * Paint the renderer. Overrides superclass paint to add specific painting.
     */
    @Override
    public void paint(Graphics g) {
        try {
            // if (gradientColor != null && !preview && isOpaque()) {
            // setOpaque(false);
            // Graphics2D g2d = (Graphics2D) g;
            // g2d.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientColor, true));
            // g2d.fillRect(0, 0, getWidth(), getHeight());
            // }
            int width = getWidth();
            int iconHeight = getHeight() * 7 / 10;
            int textHeight = getHeight() - iconHeight;
            iconLabel.setSize(width, iconHeight);
            textLabel.setSize(width, textHeight);
            iconLabel.repaint();
            textLabel.repaint();
            super.paint(g);
            paintSelectionBorder(g);
        } catch (IllegalArgumentException e) {
            // JDK Bug: Zero length string passed to TextLayout constructor
            Logger.debug(e.getMessage());
        }
    }

    /**
     * Provided for subclassers to paint a selection border.
     */
    protected void paintSelectionBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke previousStroke = g2.getStroke();
        g2.setStroke(GraphConstants.SELECTION_STROKE);
        if (childrenSelected || selected) {
            if (childrenSelected) g.setColor(gridColor);
            else if (hasFocus && selected) g.setColor(lockedHandleColor);
            else if (selected) g.setColor(highlightColor);
            Dimension d = getSize();
            g.drawRect(0, 0, d.width - 1, d.height - 1);
        }
        g2.setStroke(previousStroke);
    }

    // /**
    // * Returns the intersection of the bounding rectangle and the straight line between the source and the specified
    // * point p. The specified point is expected not to intersect the bounds.
    // */
    // public Point2D getPerimeterPoint(VertexView view,
    // Point2D source,
    // Point2D p) {
    // Rectangle2D bounds = view.getBounds();
    // double x = bounds.getX();
    // double y = bounds.getY();
    // double width = bounds.getWidth();
    // double height = bounds.getHeight();
    // double xCenter = x + width / 2;
    // double yCenter = y + height / 2;
    // double dx = p.getX() - xCenter; // Compute Angle
    // double dy = p.getY() - yCenter;
    // double alpha = Math.atan2(dy, dx);
    // double xout = 0, yout = 0;
    // double pi = Math.PI;
    // double pi2 = Math.PI / 2.0;
    // double beta = pi2 - alpha;
    // double t = Math.atan2(height, width);
    // if (alpha < -pi + t || alpha > pi - t) { // Left edge
    // xout = x;
    // yout = yCenter - width * Math.tan(alpha) / 2;
    // } else if (alpha < -t) { // Top Edge
    // yout = y;
    // xout = xCenter - height * Math.tan(beta) / 2;
    // } else if (alpha < t) { // Right Edge
    // xout = x + width;
    // yout = yCenter + width * Math.tan(alpha) / 2;
    // } else { // Bottom Edge
    // yout = y + height;
    // xout = xCenter + height * Math.tan(beta) / 2;
    // }
    // return new Point2D.Double(xout, yout);
    // }

    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // public void validate() {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void revalidate() {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void repaint(long tm,
    // int x,
    // int y,
    // int width,
    // int height) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void repaint(Rectangle r) {
    // }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
     */
    @Override
    protected void firePropertyChange(String propertyName,
                                      Object oldValue,
                                      Object newValue) {
        // Strings get interned...
        if ("text".equals(propertyName)) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // byte oldValue,
    // byte newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // char oldValue,
    // char newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // short oldValue,
    // short newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // int oldValue,
    // int newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // long oldValue,
    // long newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // public void firePropertyChange(String propertyName,
    // float oldValue,
    // float newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // double oldValue,
    // double newValue) {
    // }
    //
    // /**
    // * Overridden for performance reasons. See the <a href="#override">Implementation Note </a> for more information.
    // */
    // @Override
    // public void firePropertyChange(String propertyName,
    // boolean oldValue,
    // boolean newValue) {
    // }

    /**
     * @return Returns the gradientColor.
     */
    public Color getGradientColor() {
        return gradientColor;
    }

    /**
     * @param gradientColor
     *        The gradientColor to set.
     */
    public void setGradientColor(Color gradientColor) {
        this.gradientColor = gradientColor;
    }
}
