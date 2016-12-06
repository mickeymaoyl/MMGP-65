/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.model;

import nc.ui.mmgp.pub.beans.digraph.DigraphIdGenerator;

/**
 * <b> ��ģ�� </b>
 * <p>
 * ���б�ģ�ͱ���̳д���
 * </p>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
public abstract class AbstractEdgeModel extends AbstractCellModel {

	public static final int STYLE_DEFAULT = 0;

	public static final int STYLE_ROUTED = 1;

	private double lineWidth;

	private String pk = DigraphIdGenerator.nextId();

	public final String getEdgeId() {
		return pk;
	}

	public double getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public abstract void setStyle(int style);

	public abstract int getStyle();

	public abstract AbstractVertexModel getSourceVertexModel();

	public abstract AbstractVertexModel getTargetVertexModel();

	public abstract void setSourceVertexModel(AbstractVertexModel src);

	/**
	 * ����Ŀ��ڵ�����ģ��
	 * 
	 * @param target
	 *            Ŀ��ڵ�����ģ��
	 */
	public abstract void setTargetVertexModel(AbstractVertexModel tgt);
}
