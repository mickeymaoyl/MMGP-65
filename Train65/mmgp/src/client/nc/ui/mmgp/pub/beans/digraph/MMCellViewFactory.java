package nc.ui.mmgp.pub.beans.digraph;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

@SuppressWarnings("serial")
public class MMCellViewFactory extends DefaultCellViewFactory {
    protected VertexView createVertexView(Object cell) {
        return new MMVertexView(cell);
    }
}
