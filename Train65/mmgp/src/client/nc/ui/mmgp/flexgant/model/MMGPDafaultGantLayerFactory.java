package nc.ui.mmgp.flexgant.model;

import java.util.Iterator;

import nc.ui.mmgp.flexgant.view.timelineobject.MMGPTimelineObject;
import nc.vo.mmgp.util.MMStringUtil;

import com.dlsc.flexgantt.model.gantt.ILayer;
import com.dlsc.flexgantt.model.gantt.Layer;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-5-19����4:43:13
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
															// Ϊ�գ�����ֻ��һ��
			layer = getFisrtGantLayer(charmodel);
			if (layer == null) {
				layer = new Layer("gant layer");
				charmodel.addLayer(layer);
			}
		} else {// ��layername�Ƿ���� �������ڴ����µ�
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
	 * @Description:����layer���ƣ���model�л�ȡlayer
	 * @param: @param charmodel
	 * @param: @param name
	 * @param: @return
	 * @return:ILayer
	 * @author: tangxya
	 * @data:2014-5-20����4:55:57
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
