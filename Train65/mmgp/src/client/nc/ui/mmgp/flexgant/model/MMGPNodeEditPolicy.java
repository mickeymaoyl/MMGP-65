package nc.ui.mmgp.flexgant.model;

import com.dlsc.flexgantt.command.ICommand;
import com.dlsc.flexgantt.model.treetable.DefaultMutableTreeTableNode;
import com.dlsc.flexgantt.model.treetable.DefaultTreeTableModel;
import com.dlsc.flexgantt.model.treetable.ITreeTableModel;
import com.dlsc.flexgantt.model.treetable.ITreeTableNode;

import nc.ui.mmgp.flexgant.policy.MMGPNodeInsertCommand;
import nc.ui.pubapp.gantt.policy.edit.AppNodeEditPolicy;
import nc.ui.pubapp.gantt.ui.event.IAppGanttEditable;

public class MMGPNodeEditPolicy extends AppNodeEditPolicy {
    
    private IAppGanttEditable appGanttEditable;
    
    public ICommand getInsertNodeCommand(Object node,
                                         int childIndex,
                                         ITreeTableModel model) {
        assertClass("getInsertNodeCommand", "node", DefaultMutableTreeTableNode.class, node);
        assertClass("getInsertNodeCommand", "model", DefaultTreeTableModel.class, model);
        return new MMGPNodeInsertCommand((DefaultMutableTreeTableNode) node, childIndex, (DefaultTreeTableModel) model);
    }
    
    /* (non-Javadoc)
     * 
     * 修改BUG ，编辑判断中使用了静态方法，静态常量，导致项目管理 里的甘特图节点打开后，会影响项目计划甘特图的编辑性
     * @see nc.ui.pubapp.gantt.policy.AppNodeEditPolicy#isValueEditable(java.lang.Object, com.dlsc.flexgantt.model.treetable.ITreeTableModel, int)
     */
    @Override
    public boolean isValueEditable(Object node, ITreeTableModel model,
            int modelIndex) {
        
        boolean editable = ((ITreeTableNode) node).isValueEditable(modelIndex);
        if(editable == true && getAppGanttEditable() != null){
            return getAppGanttEditable().isCellEditable(node, model, modelIndex);
        } 
        return editable;
    }
    public IAppGanttEditable getAppGanttEditable() {
        return appGanttEditable;
    }
    public void setAppGanttEditable(IAppGanttEditable appGanttEditable) {
        this.appGanttEditable = appGanttEditable;
    }

}
