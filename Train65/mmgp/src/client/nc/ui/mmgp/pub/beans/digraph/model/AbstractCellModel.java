package nc.ui.mmgp.pub.beans.digraph.model;

import nc.ui.mmgp.pub.beans.digraph.UIDigraph;

/**
 * <b> �ڵ�ͱ�ģ�� </b>
 * ��������:2011-3-25
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
     * ����tooltips
     * @return
     */
    public String getToolTipText() {
        return null;
    }
    
    public abstract Object getDataVO();
    
    public abstract void updateDigraphModel(UIDigraph graph);
}
