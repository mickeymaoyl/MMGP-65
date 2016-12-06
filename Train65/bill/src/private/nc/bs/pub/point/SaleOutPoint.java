/**
 * 
 */
package nc.bs.pub.point;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * @author maoyulong
 *
 */
public enum SaleOutPoint implements IPluginPoint {

	
	INSERT,

	UPDATE,

	DELETE,

	SEND_APPROVE,

	UNSEND_APPROVE,

	APPROVE,

	UNAPPROVE;
	


	/* ���� Javadoc��
	 * @see nc.impl.pubapp.pattern.rule.plugin.IPluginPoint#getComponent()
	 */
	@Override
	public String getComponent() {
		// TODO �Զ����ɵķ������
		return "TR12";
	}

	/* ���� Javadoc��
	 * @see nc.impl.pubapp.pattern.rule.plugin.IPluginPoint#getModule()
	 */
	@Override
	public String getModule() {
		// TODO �Զ����ɵķ������
		return "train";
	}

	/* ���� Javadoc��
	 * @see nc.impl.pubapp.pattern.rule.plugin.IPluginPoint#getPoint()
	 */
	@Override
	public String getPoint() {
		// TODO �Զ����ɵķ������
		return this.getClass().getName() + "." + this.name();
	}

}
