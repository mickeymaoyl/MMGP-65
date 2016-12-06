package nc.ui.mmgp.flexgant.model;

import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;

/**
 * @author wangfan3
 * 
 * 甘特图　新增行时　ＶＯ赋默认值接口
 *
 */
public interface IVOCreateStrategy {
    public Object genaralNewVO(MMGPGantChartModel model,MMGPGanttChartNode parent);
}
