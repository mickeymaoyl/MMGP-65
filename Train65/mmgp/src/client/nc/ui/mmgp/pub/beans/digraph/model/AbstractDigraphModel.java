/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 �½�
 */
package nc.ui.mmgp.pub.beans.digraph.model;

import java.util.List;
import java.util.Observable;

/**
 * <b> ����ͼģ�� </b>
 * <p>
 * �̳�Observable����ģ�ͱ仯��֪ͨע����
 * </p>
 * ��������:2011-2-16
 * 
 * @author wangweiu
 */
public abstract class AbstractDigraphModel extends Observable {

	/**
	 * ��ʼ��ͼ��
	 * 
	 * @param vms
	 * @param ems
	 */
	public abstract void load(List<AbstractVertexModel> vms,
			List<AbstractEdgeModel> ems);

	/**
	 * ����һ���ڵ�
	 */
	public abstract void addVertex(AbstractVertexModel vm);

	/**
	 * ɾ��һ���ڵ�
	 * 
	 * @param vm
	 */
	public abstract void deleteVertex(AbstractVertexModel vm);

	/**
	 * �޸�һ���ڵ�
	 * 
	 * @param vm
	 */
	public abstract void updateVertex(AbstractVertexModel vm);

	// public abstract void updateVertexWithOutTriggerEvent(AbstractVertexModel
	// vm) ;

	/**
	 * ȡ�ýڵ���Ϣ.
	 */
	public abstract AbstractVertexModel getVertex(String pk);

	/**
	 * ���ݱ���Ϣ���ӱ�
	 * 
	 * @param em
	 */
	public abstract void addEdge(AbstractEdgeModel em);

	//
	// /**
	// * �������ڵ�֮�����ӱ�
	// *
	// * @param em
	// */
	// void addEdge(String pkSource,
	// String pkTarge);

	/**
	 * ɾ����
	 * 
	 * @param em
	 */
	public abstract void deleteEdge(AbstractEdgeModel em);

	/**
	 * ���±�
	 * 
	 * @param em
	 */
	public abstract void updateEdge(AbstractEdgeModel em);

	// /**
	// * ɾ�������ڵ�֮��ı�
	// *
	// * @param em
	// */
	// void deleteEdge(String pkSource,
	// String pkTarge);

	/**
	 * ��������ȡ�ñ���Ϣ
	 */
	public abstract AbstractEdgeModel getEdge(String pk);

	/**
	 * ��������ȡ�ñ���Ϣ
	 */
	public abstract AbstractEdgeModel getEdge(String pkSource, String pkTarge);

	/**
	 * ȡ�����нڵ�
	 * 
	 * @return
	 */
	public abstract List<AbstractVertexModel> getAllVertexs();

	/**
	 * ȡ�����б�
	 * 
	 * @return
	 */
	public abstract List<AbstractEdgeModel> getAllEdges();

	/**
	 * ���
	 * 
	 * @return
	 */
	public abstract void clear();

	/**
	 * ȡ�ñ仯�Ľڵ㣨����ɾ���ģ�
	 * 
	 * @return
	 */
	public abstract List<AbstractVertexModel> getChangedVertexs();

	/**
	 * ȡ�ñ仯�ıߣ�����ɾ���ģ�
	 * 
	 * @return
	 */
	public abstract List<AbstractEdgeModel> getChangedEdges();

	/**
	 * ȡ��ĳ���ڵ����Ľڵ�
	 * 
	 * @param vertex
	 * @return
	 */
	public abstract List<AbstractVertexModel> getNextVertexs(
			AbstractVertexModel vertex);

	/**
	 * �ɻ�״�Ľڵ�
	 */
	public abstract AbstractVertexModel[] circleModel();

	public abstract void notifyUI(Object arg);

	// wrr ���� -- begin 20120224
	public abstract void loadWithoutChangeModel();

	public abstract AbstractVertexModel getSourceVertexModel();

	public abstract void setSourceVertexModel(AbstractVertexModel processModel);

	public abstract void addSubDiagraphModel(AbstractDigraphModel model);

	public abstract void removeSubDiagraphModel(AbstractDigraphModel model);

	public abstract void removeSubDiagraphModel(int index);

	public abstract List<AbstractDigraphModel> getSubDiagraphModel();

	public abstract void setSubDiagraphModel(
			List<AbstractDigraphModel> subDiagraphModel);
	
	public abstract IGraph getGraphVO();
	
	public abstract void setGraphVO(IGraph graphVO);
	
	public abstract void updateGraphVO(IGraph graphVO);
	// wrr -- end

}
