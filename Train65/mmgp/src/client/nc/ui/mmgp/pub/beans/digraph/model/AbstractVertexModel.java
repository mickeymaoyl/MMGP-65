/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph.model;

import nc.ui.mmgp.pub.beans.digraph.DigraphIdGenerator;

/**
 * <b>节点模型 </b> 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public abstract class AbstractVertexModel extends AbstractCellModel {
	private String pk = DigraphIdGenerator.nextId();
	protected String name;

	// wrr add code
	protected String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getVertexId() {
		return pk;
	}

	public final String getPk() {
		return pk;
	}

	public abstract double getX();

	public abstract double getY();

	public abstract double getWidth();

	public abstract double getHeight();

	public abstract void setX(double value);

	public abstract void setY(double value);

	public abstract void setWidth(double value);

	public abstract void setHeight(double value);

	/**
	 * 节点类型，用于不同的节点显示不同的样式
	 * 
	 * @return
	 */

	public abstract String getType();

	public abstract AbstractVertexModel getUpperModel();

	public abstract void setUpperModel(AbstractVertexModel parentModel);

}
