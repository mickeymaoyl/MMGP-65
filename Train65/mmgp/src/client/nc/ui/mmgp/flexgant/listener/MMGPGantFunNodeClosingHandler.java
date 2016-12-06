package nc.ui.mmgp.flexgant.listener;

import java.util.List;

import com.dlsc.flexgantt.swing.AbstractGanttChart;

import nc.ui.uif2.FunNodeClosingHandler;

/**
 * @author wangfan3
 * 
 * 　所有甘特图　需要配置上此类，注入甘特图。防止内存泄漏
 *
 */
public class MMGPGantFunNodeClosingHandler extends FunNodeClosingHandler {

    List<AbstractGanttChart> gantchartList;

    protected void beforeClose() {
        if (gantchartList != null) {
            for (AbstractGanttChart gantchart : gantchartList) {
                gantchart.tearDown();
            }
        }
    }

    public List<AbstractGanttChart> getGantchartList() {
        return gantchartList;
    }

    public void setGantchartList(List<AbstractGanttChart> gantchartList) {
        this.gantchartList = gantchartList;
    }

}
