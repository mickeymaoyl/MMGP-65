package nc.ui.mmgp.flexgant.event;

import javax.swing.tree.TreePath;

import nc.ui.mmgp.flexgant.view.MMGPGantChart;
import nc.ui.pubapp.uif2app.event.PubAppEvent;

/**
 * @author wangfan3
 * 
 * 甘特图行选中事件
 *
 */
public class MMGPGantRowSelectEvent  extends PubAppEvent {
    private TreePath m_iOldrow = null; // 原行
    private TreePath m_iRow = null; // 现行

    private TreePath[] m_iOldrows = null; // 原行
    private TreePath[] m_iRows = null; // 现行

    public MMGPGantRowSelectEvent(MMGPGantChart mmgpGantChart, TreePath oldviewpath, TreePath path) {
        super(MMGPGantRowSelectEvent.class.getName());
        this.setSource(mmgpGantChart);
        this.m_iOldrow = oldviewpath;
        this.m_iRow = path;
    }
    public MMGPGantRowSelectEvent() {
        super(MMGPGantRowSelectEvent.class.getName());
    }
    
    public TreePath[] getOldrows() {
        return m_iOldrows;
    }

    public void setOldrows(TreePath[] oldrows) {
        m_iOldrows = oldrows;
    }

    public TreePath[] getRows() {
        return m_iRows;
    }

    public void setRows(TreePath[] rows) {
        m_iRows = rows;
    }
    public TreePath getOldrow() {
        return m_iOldrow;
    }
    public void setOldrow(TreePath m_iOldrow) {
        this.m_iOldrow = m_iOldrow;
    }
    public TreePath getRow() {
        return m_iRow;
    }
    public void setRow(TreePath m_iRow) {
        this.m_iRow = m_iRow;
    }
}
