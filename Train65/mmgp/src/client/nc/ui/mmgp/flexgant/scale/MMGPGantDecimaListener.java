package nc.ui.mmgp.flexgant.scale;

import java.util.Arrays;

import nc.ui.mmgp.flexgant.listener.ITreeTableModelDecimalListener;
import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.pubapp.gantt.model.AppGanttChartNode;
import nc.ui.pubapp.scale.BizDecimalParaVO;
import nc.vo.pubapp.scale.VarScaleObject;

/**
 * @Description: TODO
 *               <p>
 *               œÍœ∏π¶ƒ‹√Ë ˆ
 *               </p>
 * @data:2014-5-29œ¬ŒÁ3:39:50
 * @author: tangxya
 */
@SuppressWarnings("restriction")
public class MMGPGantDecimaListener implements ITreeTableModelDecimalListener {

	public MMGPGantChartModel chartModel;
	private String src;
	private String[] target;
	private VarScaleObject varso;

	public MMGPGantDecimaListener( BizDecimalParaVO paraVO,
			MMGPGantChartModel chartModel) {
		super();
		this.chartModel = chartModel;
		this.src = paraVO.getSrc();
		this.target = paraVO.getTargets();
		this.varso = paraVO.getVso();
		this.addListener();
	}

	@Override
	public String getSource() {
		return this.src;
	}

	@Override
	public int getDecimalFromSource(AppGanttChartNode node, Object pkValue) {
		/*
		 * if (this.varso instanceof OrgExchangeScaleObject) {
		 * ((OrgExchangeScaleObject) this.varso).setRow(row); }
		 */
		return this.varso.getDigit(this.getParam(node, pkValue));
	}

	protected Object getParam(AppGanttChartNode node, Object pkValue) {
		Object param = null;
		if (this.chartModel != null) {
			param = chartModel.getContext().getNodeValueAdapter()
					.getAttributeValue(node.getUserObject(), this.src);
		}
		if (param == null) {
			param = pkValue;
		}
		return param;
	}

	@Override
	public boolean isTarget(MMGPGantItem item) {
		if (item == null) {
			return false;
		}
		return Arrays.binarySearch(this.target, item.getKey()) > -1;
	}

	@Override
	public String[] getTarget() {
		return this.target;
	}

	protected void addListener() {
		if (this.target != null && this.target.length > 0) {
			if (this.chartModel != null) {
				this.chartModel.addDecimalListener(this);
			}
		}
	}

}
