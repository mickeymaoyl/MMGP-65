package nc.ui.mmgp.flexgant.policy;

import com.dlsc.flexgantt.model.treetable.DefaultTreeTableModel;
import com.dlsc.flexgantt.model.treetable.IMutableTreeTableNode;

import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.pubapp.gantt.model.AppGanttChartModel;
import nc.ui.pubapp.gantt.policy.command.AppNodeInsertCommand;

/**
 * @author wangfan3
 *
 *
 *����ͼ�в�������
 *
 * @����
 */
public class MMGPNodeInsertCommand extends AppNodeInsertCommand {

    public MMGPNodeInsertCommand(IMutableTreeTableNode parent,
                                 int childIndex,
                                 DefaultTreeTableModel model) {
        super(parent, childIndex, model);
    }
    
    /* (non-Javadoc)
     * @see nc.ui.pubapp.gantt.policy.command.AppNodeInsertCommand#createNewNode()
     * ʹ����  ���ӵ���ΪMMGPGanttChartNode
     */
    protected IMutableTreeTableNode createNewNode() {
        AppGantContext context = ((AppGanttChartModel)getModel()).getContext();
         Object userObj=null;
         if(context.getValueAdapter()!=null){
             userObj=    context.getValueAdapter().createValueObj();
         }
         return new MMGPGanttChartNode(userObj,context);
 }
}
