/*
 * --------+-----------------+---------+---------------------------------------- DATE |NAME(Inc) |GUIDE |GUIDANCE
 * --------+-----------------+---------+---------------------------------------- XXXXXXXX AUTHOR G001.00.0 新建
 */
package nc.ui.mmgp.pub.beans.digraph.model;

import java.util.List;
import java.util.Observable;

/**
 * <b> 有向图模型 </b>
 * <p>
 * 继承Observable，当模型变化是通知注册类
 * </p>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public abstract class AbstractDigraphModel extends Observable {

	/**
	 * 初始化图形
	 * 
	 * @param vms
	 * @param ems
	 */
	public abstract void load(List<AbstractVertexModel> vms,
			List<AbstractEdgeModel> ems);

	/**
	 * 增加一个节点
	 */
	public abstract void addVertex(AbstractVertexModel vm);

	/**
	 * 删除一个节点
	 * 
	 * @param vm
	 */
	public abstract void deleteVertex(AbstractVertexModel vm);

	/**
	 * 修改一个节点
	 * 
	 * @param vm
	 */
	public abstract void updateVertex(AbstractVertexModel vm);

	// public abstract void updateVertexWithOutTriggerEvent(AbstractVertexModel
	// vm) ;

	/**
	 * 取得节点信息.
	 */
	public abstract AbstractVertexModel getVertex(String pk);

	/**
	 * 根据边信息增加边
	 * 
	 * @param em
	 */
	public abstract void addEdge(AbstractEdgeModel em);

	//
	// /**
	// * 在两个节点之间增加边
	// *
	// * @param em
	// */
	// void addEdge(String pkSource,
	// String pkTarge);

	/**
	 * 删除边
	 * 
	 * @param em
	 */
	public abstract void deleteEdge(AbstractEdgeModel em);

	/**
	 * 更新边
	 * 
	 * @param em
	 */
	public abstract void updateEdge(AbstractEdgeModel em);

	// /**
	// * 删除两个节点之间的边
	// *
	// * @param em
	// */
	// void deleteEdge(String pkSource,
	// String pkTarge);

	/**
	 * 根据主键取得边信息
	 */
	public abstract AbstractEdgeModel getEdge(String pk);

	/**
	 * 根据主键取得边信息
	 */
	public abstract AbstractEdgeModel getEdge(String pkSource, String pkTarge);

	/**
	 * 取得所有节点
	 * 
	 * @return
	 */
	public abstract List<AbstractVertexModel> getAllVertexs();

	/**
	 * 取得所有边
	 * 
	 * @return
	 */
	public abstract List<AbstractEdgeModel> getAllEdges();

	/**
	 * 清空
	 * 
	 * @return
	 */
	public abstract void clear();

	/**
	 * 取得变化的节点（包括删除的）
	 * 
	 * @return
	 */
	public abstract List<AbstractVertexModel> getChangedVertexs();

	/**
	 * 取得变化的边（包括删除的）
	 * 
	 * @return
	 */
	public abstract List<AbstractEdgeModel> getChangedEdges();

	/**
	 * 取得某个节点后面的节点
	 * 
	 * @param vertex
	 * @return
	 */
	public abstract List<AbstractVertexModel> getNextVertexs(
			AbstractVertexModel vertex);

	/**
	 * 成环状的节点
	 */
	public abstract AbstractVertexModel[] circleModel();

	public abstract void notifyUI(Object arg);

	// wrr 新增 -- begin 20120224
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
