package nc.ui.mmgp.flexgant.view.treetable;

import java.util.ArrayList;
import java.util.List;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.pub.IGantTableData;

/**
 * @Description: TODO
 *               <p>
 *               ÏêÏ¸¹¦ÄÜÃèÊö
 *               </p>
 * @data:2014-5-30ÏÂÎç4:48:23
 * @author: tangxya
 */
public class CompositeGantTableDataPrepare implements IGantTableData {
	private List<IGantTableData> gantTableDataPrepares;

	@Override
	public void prepareGantTableData(MMGPGantChartModel charModel) {
		for (IGantTableData ibld : this.gantTableDataPrepares) {
			ibld.prepareGantTableData(charModel);
		}

	}

	public List<IGantTableData> getGantTableDataPrepares() {
		if (this.gantTableDataPrepares == null) {
			this.gantTableDataPrepares = new ArrayList<IGantTableData>();
		}
		return this.gantTableDataPrepares;
	}

	public void setGantTableDataPrepares(
			List<IGantTableData> billListDataPrepares) {
		this.gantTableDataPrepares = billListDataPrepares;
	}

}
