package nc.ui.mmgp.flexgant.model;

import nc.ui.mmgp.flexgant.view.timelineobject.MMGPTimelineObject;

import com.dlsc.flexgantt.model.gantt.ILayer;

/**
 * @Description: ����ͼ�㼶��������
    <p>
          ��ϸ��������
    </p>
 * @data:2014-5-19����4:29:20
 * @author: tangxya
 */
public interface IMMGPGantLayerFactory {
	
  public ILayer  createLayer(MMGPTimelineObject timeline,MMGPGantChartModel charmodel);

}
