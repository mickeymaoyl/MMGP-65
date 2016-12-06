package nc.ui.mmgp.flexgant.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import nc.bs.logging.Logger;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.uif2.editor.value.AbstractComponentValueAdapter;

public class MMGPGantValueAdapter extends AbstractComponentValueAdapter {
    
    @Override
    public void setComponent(Component component) {
        if(component instanceof MMGPGantChart)      
            super.setComponent(component);
        else
            Logger.error("MMGPGantValueAdapter只能用于MMGPGantChart");
    }

    @Override
    public Object getValue() {
        MMGPGantChart gantchart = (MMGPGantChart) this.getComponent();
        MMGPGanttChartNode root = (MMGPGanttChartNode) gantchart.getAppModel().getRoot();
        Stack<MMGPGanttChartNode> stack = new Stack<MMGPGanttChartNode>();
        stack.push(root);
        List<Object> objList = new ArrayList<Object>();
        while (!stack.isEmpty()) {
            MMGPGanttChartNode curnode = stack.pop();
            if (root != curnode) {
                Object useObj = curnode.getTypedUserObject();
                objList.add(useObj);
            }
            if (!curnode.isLeaf()) {
                int count = curnode.getChildCount();
                for (int i = count; i > 0; i--) {
                    stack.push((MMGPGanttChartNode) curnode.getChildAt(i - 1));
                }
            }
        }
        return objList;
    }

    @Override
    public void setValue(Object object) {
        // TODO Auto-generated method stub
        
    }

}
