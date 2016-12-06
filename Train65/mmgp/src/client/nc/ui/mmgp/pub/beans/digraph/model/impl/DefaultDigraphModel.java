package nc.ui.mmgp.pub.beans.digraph.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.mmgp.pub.beans.digraph.event.EdgeEvent;
import nc.ui.mmgp.pub.beans.digraph.event.GraphEvent;
import nc.ui.mmgp.pub.beans.digraph.event.GraphOperator;
import nc.ui.mmgp.pub.beans.digraph.event.HierachBuildEvent;
import nc.ui.mmgp.pub.beans.digraph.event.VertexEvent;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractEdgeModel;
import nc.ui.mmgp.pub.beans.digraph.model.AbstractVertexModel;
import nc.ui.mmgp.pub.beans.digraph.model.IGraph;
import nc.ui.mmgp.pub.util.DigraphCheckUtil;
import nc.vo.mmgp.util.MMListUtil;
import nc.vo.pub.VOStatus;

/**
 * <b> 有向图模型默认实现类 </b>
 * <p>
 * 有向图模型默认实现类
 * </p>
 * 创建日期:2011-2-16
 * 
 * @author wangweiu
 */
public class DefaultDigraphModel extends AbstractDigraphModel {
	// 顶点信息
	private List<AbstractVertexModel> vertexList = new ArrayList<AbstractVertexModel>();

	/**
	 * 被删除的节点
	 */
	private List<AbstractVertexModel> removedVertexs = new ArrayList<AbstractVertexModel>();

	/**
	 * 被删除的边
	 */
	private List<AbstractEdgeModel> removedEdges = new ArrayList<AbstractEdgeModel>();

	// 边信息 key 为边的主键(PK)
	private Map<String, AbstractEdgeModel> edges = new HashMap<String, AbstractEdgeModel>();

	// 顶点关系
	private List<List<Boolean>> adjMat = null;

	public DefaultDigraphModel() {
		adjMat = new ArrayList<List<Boolean>>();
		vertexList = new ArrayList<AbstractVertexModel>();
		edges = new HashMap<String, AbstractEdgeModel>();
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#load(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void load(List<AbstractVertexModel> vms, List<AbstractEdgeModel> ems) {
		for (AbstractVertexModel vm : vms) {
			addVertexWithOutTrigger(vm);
			notifyUI(new VertexEvent(vm, GraphOperator.Load));
		}

		for (AbstractEdgeModel em : ems) {
			addEdgeWithOutTrigger(em);
			notifyUI(new EdgeEvent(em, GraphOperator.Load));
		}

		// 重建图形嵌套关系
		notifyUI(new HierachBuildEvent(vms));
	}

	/**
	 * 简要说明
	 */
	public void addEdge(AbstractEdgeModel em) {
		addEdgeWithOutTrigger(em);

		em.setStatus(VOStatus.NEW);

		notifyUI(new EdgeEvent(em, GraphOperator.Add));
	}

	/**
	 * @param em
	 */
	private void addEdgeWithOutTrigger(AbstractEdgeModel em) {
		edges.put(em.getEdgeId(), em);
		String pkSource = em.getSourceVertexModel().getVertexId();
		String pkTarge = em.getTargetVertexModel().getVertexId();

		notNull(pkSource);
		notNull(pkTarge);

		int start = getPos(pkSource);
		int end = getPos(pkTarge);
		adjMat.get(start).set(end, true);
	}

	/**
	 * 简要说明
	 */
	public void addVertex(AbstractVertexModel vm) {
		addVertexWithOutTrigger(vm);
		vm.setStatus(VOStatus.NEW);

		notifyUI(new VertexEvent(vm, GraphOperator.Add));
	}

	/**
	 * @param vm
	 */
	private void addVertexWithOutTrigger(AbstractVertexModel vm) {
		vertexList.add(vm);
		adjMat.add(new ArrayList<Boolean>());
		for (List<Boolean> row : adjMat) {
			int rowSize = row.size();
			for (int i = rowSize; i < adjMat.size(); i++) {
				row.add(false);
			}
		}
	}

	/**
	 * 简要说明
	 */
	public void deleteEdge(AbstractEdgeModel em) {
		String pkSource = em.getSourceVertexModel().getVertexId();
		String pkTarge = em.getTargetVertexModel().getVertexId();
		notNull(pkSource);
		notNull(pkTarge);
		int start = getPos(pkSource);
		int end = getPos(pkTarge);
		adjMat.get(start).set(end, false);
		String key = getEdge(pkSource, pkTarge).getEdgeId();
		edges.remove(key);

		// codesync_xg_v01 删除时，如果原有连线是新增，则不必把这个连线放到删除列表里 modify -begin
		if (em.getStatus() == VOStatus.NEW) {
			em.setStatus(VOStatus.UNCHANGED);
		} else {
			em.setStatus(VOStatus.DELETED);
			removedEdges.add(em);
		}

		// codesync_xg_v01 modify end

		notifyUI(new EdgeEvent(em, GraphOperator.Delete));
	}

	/**
	 * @param pkSource
	 * @param pkTarge
	 */
	private void notNull(String str) {
		if (str == null || str.trim().length() == 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 简要说明
	 */
	public void deleteVertex(AbstractVertexModel vm) {
		// 删除所有相关的边;删除节点、删除对应的矩阵的行和列、
		List<AbstractEdgeModel> removeEdges = new ArrayList<AbstractEdgeModel>();
		for (Map.Entry<String, AbstractEdgeModel> entity : edges.entrySet()) {
			AbstractEdgeModel edge = entity.getValue();
			if (edge.getSourceVertexModel().equals(vm)) {
				removeEdges.add(edge);
			} else if (edge.getTargetVertexModel().equals(vm)) {
				removeEdges.add(edge);
			}
		}

		deleteEdges(removeEdges);

		int index = getPos(vm.getVertexId());

		vertexList.remove(index);

		// 移除行
		adjMat.remove(index);

		// 移除列
		for (List<Boolean> row : adjMat) {
			row.remove(index);
		}
		// codesync_xg_v01 删除时，如果原有结点是新增，则不必把这个结点放到删除列表里 modify -begin
		if (vm.getStatus() == VOStatus.NEW) {
			vm.setStatus(VOStatus.UNCHANGED);
		} else {
			vm.setStatus(VOStatus.DELETED);
			removedVertexs.add(vm);
		}
		// codesync_xg_v01 modify end

		notifyUI(new VertexEvent(vm, GraphOperator.Delete));
	}

	private void deleteEdges(List<AbstractEdgeModel> deleteEdges) {
		for (AbstractEdgeModel em : deleteEdges) {
			deleteEdge(em);
		}
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getEdge(java.lang.String)
	 */
	public AbstractEdgeModel getEdge(String pk) {
		return edges.get(pk);
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getEdge(java.lang.String,
	 *      java.lang.String)
	 */
	public AbstractEdgeModel getEdge(String pkSource, String pkTarge) {
		for (Map.Entry<String, AbstractEdgeModel> entity : edges.entrySet()) {
			if (entity.getValue().getSourceVertexModel().getVertexId().equals(
					pkSource)
					&& entity.getValue().getTargetVertexModel().getVertexId()
							.equals(pkTarge)) {
				return entity.getValue();
			}
		}

		return null;
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getVertex(java.lang.String)
	 */
	public AbstractVertexModel getVertex(String pk) {
		int index = getPos(pk);
		return vertexList.get(index);
	}

	/**
	 * 简要说明
	 */
	public void updateVertex(AbstractVertexModel vm) {
		int index = getPos(vm.getVertexId());

		// wrr add to fix a bug  20120730
		if(index == -1){
			return;
		}
		
		vertexList.set(index, vm);
		if (vm.getStatus() != VOStatus.NEW) {
			vm.setStatus(VOStatus.UPDATED);
		}

		notifyUI(new VertexEvent(vm, GraphOperator.Update));
	}

	private int getPos(String pk) {
		for (int i = 0; i < vertexList.size(); i++) {
			AbstractVertexModel vm = vertexList.get(i);
			if (vm.getVertexId().equals(pk)) {
				return i;
			}
		}
		return -1;

	}

	public void notifyUI(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getAllEdges()
	 */
	@Override
	public List<AbstractEdgeModel> getAllEdges() {
		List<AbstractEdgeModel> lstEdges = new ArrayList<AbstractEdgeModel>();
		for (Map.Entry<String, AbstractEdgeModel> entity : edges.entrySet()) {
			lstEdges.add(entity.getValue());
		}
		return lstEdges;
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getAllVertexs()
	 */
	@Override
	public List<AbstractVertexModel> getAllVertexs() {
		return vertexList;
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#clear()
	 */
	@Override
	public void clear() {
		adjMat = new ArrayList<List<Boolean>>();
		vertexList = new ArrayList<AbstractVertexModel>();
		edges = new HashMap<String, AbstractEdgeModel>();
		removedVertexs = new ArrayList<AbstractVertexModel>();
		removedEdges = new ArrayList<AbstractEdgeModel>();
		notifyUI(GraphOperator.Clear);
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#circleModel()
	 */
	@Override
	public AbstractVertexModel[] circleModel() {
		int[] result = DigraphCheckUtil.circleModel(adjMat);
		if (result == null) {
			return null;
		}
		return new AbstractVertexModel[] { vertexList.get(result[0]),
				vertexList.get(result[1]) };
	}

	/**
	 * 简要说明
	 */
	@Override
	public void updateEdge(AbstractEdgeModel em) {

		String pkSource = em.getSourceVertexModel().getVertexId();
		String pkTarge = em.getTargetVertexModel().getVertexId();
		notNull(pkSource);
		notNull(pkTarge);
		int start = getPos(pkSource);
		int end = getPos(pkTarge);
		
		if(start == -1 || end == -1){
			// 起始/结束结点已删除
			return;
		}
		if(MMListUtil.isEmpty(adjMat) || adjMat.get(start) == null){
			return;
		}
		adjMat.get(start).set(end, true);
		String key = getEdge(pkSource, pkTarge).getEdgeId();

		edges.put(key, em);
		if (em.getStatus() != VOStatus.NEW) {
			em.setStatus(VOStatus.UPDATED);
		}
		notifyUI(new EdgeEvent(em, GraphOperator.Update));

	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getChangedEdges()
	 */
	@Override
	public List<AbstractEdgeModel> getChangedEdges() {
		List<AbstractEdgeModel> changed = new ArrayList<AbstractEdgeModel>();
		for (AbstractEdgeModel em : getAllEdges()) {
			if (em.getStatus() == VOStatus.UNCHANGED) {
				continue;
			}
			changed.add(em);
		}
		changed.addAll(removedEdges);
		return changed;
	}

	/**
	 * 简要说明
	 * 
	 * @see nc.ui.mmgp.pub.beans.digraph.model.AbstractDigraphModel#getChangedVertexs()
	 */
	@Override
	public List<AbstractVertexModel> getChangedVertexs() {
		List<AbstractVertexModel> changed = new ArrayList<AbstractVertexModel>();
		for (AbstractVertexModel vm : getAllVertexs()) {
			if (vm.getStatus() == VOStatus.UNCHANGED) {
				continue;
			}
			changed.add(vm);
		}
		changed.addAll(removedVertexs);
		return changed;
	}

	@Override
	public List<AbstractVertexModel> getNextVertexs(AbstractVertexModel vertex) {
		List<AbstractVertexModel> nextVertexs = new ArrayList<AbstractVertexModel>();
		for (AbstractEdgeModel edge : getAllEdges()) {
			if (edge.getSourceVertexModel().getVertexId().equals(
					vertex.getVertexId())) {
				nextVertexs.add(edge.getTargetVertexModel());
			}
		}

		return nextVertexs;
	}

	/**
	 * 加载显示这个model，但不需要加入model中，因为在model中已经存在
	 */
	public void loadWithoutChangeModel() {
		for (AbstractVertexModel vm : getAllVertexs()) {
			notifyUI(new VertexEvent(vm, GraphOperator.Load));
		}

		for (AbstractEdgeModel em : getAllEdges()) {
			notifyUI(new EdgeEvent(em, GraphOperator.Load));
		}
	}

	@Override
	public void addSubDiagraphModel(AbstractDigraphModel model) {
		throw new RuntimeException("addSubDiagraphModel");
	}

	@Override
	public AbstractVertexModel getSourceVertexModel() {
		throw new RuntimeException("getSourceVertexModel");
	}

	@Override
	public List<AbstractDigraphModel> getSubDiagraphModel() {
		throw new RuntimeException("getSubDiagraphModel");
	}

	@Override
	public void removeSubDiagraphModel(AbstractDigraphModel model) {
		throw new RuntimeException("removeSubDiagraphModel");
	}

	@Override
	public void removeSubDiagraphModel(int index) {
		throw new RuntimeException("removeSubDiagraphModel");
	}

	@Override
	public void setSourceVertexModel(AbstractVertexModel processModel) {
		throw new RuntimeException("setSourceVertexModel");
	}

	@Override
	public void setSubDiagraphModel(List<AbstractDigraphModel> subDiagraphModel) {
		throw new RuntimeException("setSubDiagraphModel");
	}

	@Override
	public IGraph getGraphVO() {
		throw new RuntimeException("getGraphVO");
	}

	@Override
	public void setGraphVO(IGraph graphVO) {
		throw new RuntimeException("setGraphVO");
	}

	@Override
	public void updateGraphVO(IGraph graphVO) {
		GraphEvent graphEvent = new GraphEvent(graphVO);
		setGraphVO(graphVO);
		notifyUI(graphEvent);
	}
}
