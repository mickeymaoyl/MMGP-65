package nc.ui.mmgp.flexgant.model;

import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;

/**
 * @author wangfan3
 * 
 * ����ͼ��������ʱ���֣ϸ�Ĭ��ֵ�ӿ�
 *
 */
public interface IVOCreateStrategy {
    public Object genaralNewVO(MMGPGantChartModel model,MMGPGanttChartNode parent);
}
