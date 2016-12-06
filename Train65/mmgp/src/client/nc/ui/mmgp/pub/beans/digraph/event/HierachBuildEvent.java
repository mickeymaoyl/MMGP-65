package nc.ui.mmgp.pub.beans.digraph.event;

import java.util.List;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

public class HierachBuildEvent {
	// ��Ҫ�ؽ�Ƕ�׹�ϵ��ͼԪ
	private List<AbstractVertexModel> vertexModel;
	
	public HierachBuildEvent(List<AbstractVertexModel> model){
		vertexModel = model;
	}

	public List<AbstractVertexModel> getModel() {
		return vertexModel;
	}
	
}
