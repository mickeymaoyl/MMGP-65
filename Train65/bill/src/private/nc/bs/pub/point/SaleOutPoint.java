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
	


	/* （非 Javadoc）
	 * @see nc.impl.pubapp.pattern.rule.plugin.IPluginPoint#getComponent()
	 */
	@Override
	public String getComponent() {
		// TODO 自动生成的方法存根
		return "TR12";
	}

	/* （非 Javadoc）
	 * @see nc.impl.pubapp.pattern.rule.plugin.IPluginPoint#getModule()
	 */
	@Override
	public String getModule() {
		// TODO 自动生成的方法存根
		return "train";
	}

	/* （非 Javadoc）
	 * @see nc.impl.pubapp.pattern.rule.plugin.IPluginPoint#getPoint()
	 */
	@Override
	public String getPoint() {
		// TODO 自动生成的方法存根
		return this.getClass().getName() + "." + this.name();
	}

}
