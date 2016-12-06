package nc.ui.mmgp.pub.beans.digraph.event;

import nc.ui.mmgp.pub.beans.digraph.model.IGraph;

public class GraphEvent {
	private IGraph graph = null;

	public GraphEvent(IGraph graphVO) {
		graph = graphVO;
	}

	public IGraph getGraph() {
		return graph;
	}
}
