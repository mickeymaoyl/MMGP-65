package nc.ui.mmgp.pub.beans.digraph.eventhandler;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import nc.ui.mmgp.pub.beans.digraph.DigraphUI;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.VertexView.SizeHandle;

public class DefaultGraphRootHandler extends DigraphRootHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 774350075249589730L;

	public DefaultGraphRootHandler(GraphContext ctx, DigraphUI digraphUI) {
		super(ctx, digraphUI);
	}

	@Override
	protected void postprocessMouseRelease(MouseEvent event) {
		if (event == null) {
			return;
		}

		boolean isResize = activeHandle != null
				&& activeHandle instanceof SizeHandle;
		boolean isMove = isMoving && !event.getPoint().equals(start)
				&& graph.isMoveable();

		if (context.getCells() == null || context.getCells().length == 0) {
			return;
		}
		DefaultGraphCell vertext = (DefaultGraphCell) context.getCells()[0];
		if (!(vertext.getUserObject() instanceof AbstractVertexModel)) {
			return;
		}

		if (isMove || isResize) {

			// 通知画面，顶点移动/大小改变
			Rectangle2D bounds = GraphConstants.getBounds(vertext
					.getAttributes());
			AbstractVertexModel vertexModel = (AbstractVertexModel) vertext
					.getUserObject();
			vertexModel.setX(bounds.getX());
			vertexModel.setY(bounds.getY());
			vertexModel.setWidth(bounds.getWidth());
			vertexModel.setHeight(bounds.getHeight());

			// 通知模型
			graphUI.getUiDigraph().getDigraphModel().updateVertex(vertexModel);
		}
	}
}
