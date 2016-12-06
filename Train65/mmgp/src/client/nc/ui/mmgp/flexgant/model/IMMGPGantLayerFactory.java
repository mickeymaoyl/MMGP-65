package nc.ui.mmgp.flexgant.model;

import nc.ui.mmgp.flexgant.view.timelineobject.MMGPTimelineObject;

import com.dlsc.flexgantt.model.gantt.ILayer;

/**
 * @Description: 甘特图层级创建工厂
    <p>
          详细功能描述
    </p>
 * @data:2014-5-19下午4:29:20
 * @author: tangxya
 */
public interface IMMGPGantLayerFactory {
	
  public ILayer  createLayer(MMGPTimelineObject timeline,MMGPGantChartModel charmodel);

}
