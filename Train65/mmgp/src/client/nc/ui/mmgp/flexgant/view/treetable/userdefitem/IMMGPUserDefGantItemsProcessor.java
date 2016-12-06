package nc.ui.mmgp.flexgant.view.treetable.userdefitem;

import java.util.List;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.vo.bd.userdefrule.UserdefitemVO;

/**
 * @Description: TODO
    <p>
          ÏêÏ¸¹¦ÄÜÃèÊö
    </p>
 * @data:2014-6-4ÏÂÎç3:35:09
 * @author: tangxya
 */
public interface IMMGPUserDefGantItemsProcessor {
	public List<MMGPGantItem> resetItems(MMGPGantChartModel chartModel, String prefix, UserdefitemVO[] userdefitems);

}
