/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph.panel;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;
import nc.ui.pub.beans.UIPanel;

/**
 * <b> 属性面板抽象类 </b>
 * <p>
 * 属性面板抽象类
 * </p>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
@SuppressWarnings("serial")
public abstract class PropertiesPanel extends UIPanel {


    /**
     * 取得正在编辑的节点模型
     * @return
     */
    public abstract AbstractVertexModel getCurrentVertexModel();

    /**
     * 设置正在编辑的节点模型
     * @param currentVertexModel
     *        the currentVertexModel to set
     */
    public abstract void setCurrentVertexModel(AbstractVertexModel currentVertexModel);


    /**
     * 设置正在编辑的边模型
     * @param currentVertexModel
     *        the currentVertexModel to set
     */
    public abstract void setCurrentEdgeModel(AbstractEdgeModel currentEdgeModel);

}
