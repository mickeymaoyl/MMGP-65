package nc.ui.mmgp.pub.beans.digraph.routed;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.PortView;
import org.jgraph.graph.DefaultEdge.LoopRouting;

@SuppressWarnings("unchecked")
public class VerticalRouting extends LoopRouting {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("rawtypes")
	protected List routeEdge(GraphLayoutCache cache,
                             EdgeView edge) {
        List newPoints = new ArrayList();
        int n = edge.getPointCount();
        Point2D from = edge.getPoint(0);
        newPoints.add(from);
        if (edge.getSource() instanceof PortView) {
            newPoints.set(0, edge.getSource());
            from = ((PortView) edge.getSource()).getLocation();
        } else if (edge.getSource() != null) {
            Rectangle2D b = edge.getSource().getBounds();
            from = edge.getAttributes().createPoint(b.getCenterX(), b.getCenterY());
        }
        Point2D to = edge.getPoint(n - 1);
        CellView target = edge.getTarget();
        if (target instanceof PortView) to = ((PortView) target).getLocation();
        else if (target != null) {
            Rectangle2D b = target.getBounds();
            to = edge.getAttributes().createPoint(b.getCenterX(), b.getCenterY());
        }
        if (from != null && to != null) {
            Point2D[] routed;
            // double dx = Math.abs(from.getX() - to.getX());
            // double dy = Math.abs(from.getY() - to.getY());
            double x2 = from.getX() + ((to.getX() - from.getX()) / 2);
            double y2 = from.getY() + ((to.getY() - from.getY()) / 2);
            routed = new Point2D[2];
            Rectangle2D targetBounds = null;
            Rectangle2D sourceBounds = null;
            if ((edge.getTarget() != null && edge.getTarget().getParentView() != null)
                && (edge.getSource() != null && edge.getSource().getParentView() != null)) {
                targetBounds = edge.getTarget().getParentView().getBounds();
                sourceBounds = edge.getSource().getParentView().getBounds();
            }
            if (targetBounds != null && sourceBounds != null) {
                routed[0] = edge.getAttributes().createPoint(from.getX(), y2);
                routed[1] = edge.getAttributes().createPoint(to.getX(), y2);
                if (targetBounds.contains(routed[0])
                    || (sourceBounds.contains(routed[0]))
                    || targetBounds.contains(routed[1])
                    || (sourceBounds.contains(routed[1]))) {
                    routed[0] = edge.getAttributes().createPoint(x2, from.getY());
                    routed[1] = edge.getAttributes().createPoint(x2, to.getY());
                }
            }
            // Set/Add Points
            for (int i = 0; i < routed.length; i++) {
                if (!targetBounds.contains(routed[i]) && (!sourceBounds.contains(routed[i]))) {
                    newPoints.add(routed[i]);
                }
            }

            // Add target point
            if (target != null) newPoints.add(target);
            else
                newPoints.add(to);
            return newPoints;
        }
        return null;
    }
}
