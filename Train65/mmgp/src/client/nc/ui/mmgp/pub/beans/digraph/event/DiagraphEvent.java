package nc.ui.mmgp.pub.beans.digraph.event;

import java.util.HashMap;
import java.util.Map;

import nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel;

/**
 * ��װ��ͼ���¼�����Ҫ�������ͼ�Σ���˫�������½�����·��
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
	 * ���ò���ֵ
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            ֵ
	 */
	public void setParameter(String key, Object value) {
		parameters.put(key, value);
	}

	/**
	 * ȡ�ò���ֵ
	 * 
	 * @param key
	 *            ����
	 * @return ����ֵ
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
