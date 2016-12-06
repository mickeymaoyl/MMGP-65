package nc.ui.mmgp.pub.beans.digraph;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

public interface NodeFilter {

    /**
     * 过滤vertexModel
     * 
     * @param vertexModel
     *        节点模型
     * @return
     */
    boolean accept(AbstractVertexModel vertexModel);
}
