package nc.ui.mmgp.pub.beans.digraph;

import nc.bs.logging.Logger;
import nc.ui.mmgp.pub.beans.digraph.renderer.MMVertexRenderer;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class MMVertexView extends VertexView {
    /**
     * 
     */
    private static final long serialVersionUID = 369022750982530296L;

    /** Renderer for the class. */
    private static transient MMVertexRenderer mmrenderer;

    // Headless environment does not allow vertex renderer
    static {
        try {
            mmrenderer = new MMVertexRenderer();
        } catch (Error e) {
            // No vertex renderer
            Logger.debug(e.getMessage());
        }
    }

    public MMVertexView() {
        super();
        // try {
        // mmrenderer = new MMVertexRenderer();
        // } catch (Error e) {
        // // No vertex renderer
        // }
    }

    /**
     * Constructs a vertex view for the specified model object and the specified child views.
     * 
     * @param cell
     *        reference to the model object
     */
    public MMVertexView(Object cell) {
        super(cell);
    }

    @Override
    public CellViewRenderer getRenderer() {
        return mmrenderer;
    }

}
