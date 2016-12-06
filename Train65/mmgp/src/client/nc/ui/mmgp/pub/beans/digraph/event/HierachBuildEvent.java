package nc.ui.mmgp.pub.beans.digraph.event;

import java.util.List;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

public class HierachBuildEvent {
	// 需要重建嵌套关系的图元
	private List<AbstractVertexModel> vertexModel;
	
	public HierachBuildEvent(List<AbstractVertexModel> model){
		vertexModel = model;
	}

	public List<AbstractVertexModel> getModel() {
		return vertexModel;
	}
	
}
