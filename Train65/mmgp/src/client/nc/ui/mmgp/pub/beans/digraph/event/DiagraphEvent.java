package nc.ui.mmgp.pub.beans.digraph.event;

import java.util.HashMap;
import java.util.Map;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel;

/**
 * 封装了图形事件，主要针对新增图形，如双击工序、新建工艺路线
 * @author wangrra
 *
 */
public class DiagraphEvent {
	private AbstractDigraphModel model = null;

	private GraphOperator operator = GraphOperator.Add;

	private Map<String, Object> parameters = new HashMap<String, Object>();

	public DiagraphEvent(AbstractDigraphModel newModel, GraphOperator op) {
		model = newModel;
		operator = op;
	}

	/**
	 * 设置参数值
	 * 
	 * @param key
	 *            主键
	 * @param value
	 *            值
	 */
	public void setParameter(String key, Object value) {
		parameters.put(key, value);
	}

	/**
	 * 取得参数值
	 * 
	 * @param key
	 *            主键
	 * @return 参数值
	 */
	public Object getParameter(String key) {
		return parameters.get(key);
	}

	public AbstractDigraphModel getModel() {
		return model;
	}

	/**
	 * @return the operator
	 */
	public GraphOperator getOperator() {
		return operator;
	}

}
