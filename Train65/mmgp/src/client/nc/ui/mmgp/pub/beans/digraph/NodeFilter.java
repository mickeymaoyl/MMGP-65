package nc.ui.mmgp.pub.beans.digraph;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;

public interface NodeFilter {

    /**
     * ����vertexModel
     * 
     * @param vertexModel
     *        �ڵ�ģ��
     * @return
     */
    boolean accept(AbstractVertexModel vertexModel);
}
