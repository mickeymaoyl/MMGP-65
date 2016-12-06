package nc.ui.mmgp.pub.beans.digraph.model;

import nc.ui.mmgp.pub.beans.digraph.UIDigraph;

/**
 * <b> 节点和边模型 </b>
 * 创建日期:2011-3-25
 * 
 * @author wangweiu
 */
public abstract class AbstractCellModel {
    /**
     * @return int
     */
    public abstract int getStatus();

    /**
     * @return int
     */
    public abstract void setStatus(int status);

    /**
     * 返回tooltips
     * @return
     */
    public String getToolTipText() {
        return null;
    }
    
    public abstract Object getDataVO();
    
    public abstract void updateDigraphModel(UIDigraph graph);
}
