package nc.ui.mmgp.flexgant.model;

import java.util.Iterator;

import nc.ui.mmgp.flexgant.view.timelineobject.MMGPTimelineObject;
import nc.vo.mmgp.util.MMStringUtil;

import com.dlsc.flexgantt.model.gantt.ILayer;
import com.dlsc.flexgantt.model.gantt.Layer;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-19下午4:43:13
 * @author: tangxya
 */
public class MMGPDafaultGantLayerFactory implements IMMGPGantLayerFactory {

	private static IMMGPGantLayerFactory instance;

	protected MMGPDafaultGantLayerFactory() {
	}

	public static synchronized IMMGPGantLayerFactory getInstance() {
		if (instance == null) {
			instance = new MMGPDafaultGantLayerFactory();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.ui.mmgp.flexgant.model.IMMGPLayerFactory#createLayer(nc.ui.pubapp.
	 * gantt.model.AppTimelineObject)
	 */
	@Override
	public ILayer createLayer(MMGPTimelineObject timeline,
			MMGPGantChartModel charmodel) {
		ILayer layer = null;
		if (MMStringUtil.isEmpty(timeline.getLayername())) {// layername
															// 为空，当成只有一层
			layer = getFisrtGantLayer(charmodel);
			if (layer == null) {
				layer = new Layer("gant layer");
				charmodel.addLayer(layer);
			}
		} else {// 看layername是否存在 ，不存在创建新的
			layer = getLayerbyName(charmodel, timeline.getLayername());
			if (layer == null) {
				layer = new Layer(timeline.getLayername());
				charmodel.addLayer(layer);
			}
		}
		return layer;
	}

	/**
	 * 
	 * @Description:根据layer名称，从model中获取layer
	 * @param: @param charmodel
	 * @param: @param name
	 * @param: @return
	 * @return:ILayer
	 * @author: tangxya
	 * @data:2014-5-20下午4:55:57
	 * 
	 */
	public ILayer getLayerbyName(MMGPGantChartModel charmodel, String name) {
		if (MMStringUtil.isEmpty(name) || charmodel == null) {
			return null;
		}
		Iterator<ILayer> layers = charmodel.getLayers();
		while (layers.hasNext()) {
			ILayer layer = layers.next();
			if (name.equals(layer.getName())) {
				return layer;
			}
		}
		return null;
	}

	public ILayer getFisrtGantLayer(MMGPGantChartModel charmodel) {
		if (charmodel == null) {
			return null;
		}
		Iterator<ILayer> layers = charmodel.getLayers();
		while (layers.hasNext()) {
			return layers.next();
		}
		return null;

	}

}
