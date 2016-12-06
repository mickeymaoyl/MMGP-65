/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.panel;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;
import nc.ui.pub.beans.UIPanel;

/**
 * <b> ������������ </b>
 * <p>
 * ������������
 * </p>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
@SuppressWarnings("serial")
public abstract class PropertiesPanel extends UIPanel {


    /**
     * ȡ�����ڱ༭�Ľڵ�ģ��
     * @return
     */
    public abstract AbstractVertexModel getCurrentVertexModel();

    /**
     * �������ڱ༭�Ľڵ�ģ��
     * @param currentVertexModel
     *        the currentVertexModel to set
     */
    public abstract void setCurrentVertexModel(AbstractVertexModel currentVertexModel);


    /**
     * �������ڱ༭�ı�ģ��
     * @param currentVertexModel
     *        the currentVertexModel to set
     */
    public abstract void setCurrentEdgeModel(AbstractEdgeModel currentEdgeModel);

}
