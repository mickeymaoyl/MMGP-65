package nc.ui.mmgp.flexgant.view;

import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import nc.ui.mmgp.flexgant.event.MMGPGantRowSelectEvent;
import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.model.MMGPGantComponetFactory;
import nc.ui.mmgp.flexgant.model.MMGPNodeEditPolicy;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGantItemContainer;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.gantt.model.AppGanttChartModel;
import nc.ui.pubapp.gantt.policy.edit.AppTimelineObjEditPolicy;
import nc.ui.pubapp.gantt.ui.gantentry.AppGanttChart;
import nc.ui.pubapp.gantt.ui.treetable.AppTreeTableColumn;
import nc.ui.pubapp.gantt.ui.treetable.GantItem;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.mmgp.uif2.MMGPAbstractBill;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMListUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.MultiLangText;

import com.dlsc.flexgantt.model.dateline.TimeGranularity;
import com.dlsc.flexgantt.model.treetable.DefaultColumnModel;
import com.dlsc.flexgantt.policy.dateline.IZoomPolicy;
import com.dlsc.flexgantt.policy.dateline.TimeGranularityZoomPolicy;
import com.dlsc.flexgantt.policy.layer.IEditTimelineObjectPolicy;
import com.dlsc.flexgantt.policy.layer.IGridPolicy;
import com.dlsc.flexgantt.policy.layer.TimeGranularityGridPolicy;
import com.dlsc.flexgantt.policy.treetable.INodeEditPolicy;
import com.dlsc.flexgantt.swing.IComponentFactory;
import com.dlsc.flexgantt.swing.layer.timeline.ITimelineObjectRenderer;
import com.dlsc.flexgantt.swing.print.PrintContext;
import com.dlsc.flexgantt.swing.print.PrintGanttChart;
import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.ITreeTableMenuProvider;

/**
 * @author wangfan3
 * 
 * 甘特图 面板
 *
 */
@SuppressWarnings("rawtypes")
public class MMGPGantChart extends AppGanttChart implements
		TreeSelectionListener {

	private MMGPPrintGantChart printGantChart;
	private Integer chartwidth;

	/**
	 * @return
	 * 
	 *甘特图 左右 分隔像素
	 */
	public Integer getChartWidth() {
		if (chartwidth == null) {
			int screenwidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int treetablewidth = getTreeTableScrollPane().getPreferredSize().width + 1;
			if (screenwidth * 2 / 3 > treetablewidth) {
				chartwidth = treetablewidth;
			} else {
				chartwidth = screenwidth * 2 / 3;
			}
		}
		return chartwidth;
	}

	public AbstractAppModel manageModel;

	/**
	 * 
	 */
	private static final long serialVersionUID = -9087274225322872525L;

	public MMGPGantChart(AppGanttChartModel model) {
		this(model, MMGPGantComponetFactory.getInstance());
	}

	public MMGPGantChart(AppGanttChartModel model, IComponentFactory compFactory) {
		super(model, compFactory);
		this.initListener();
		initUI();
	}

	public MMGPGantChart(MMGPGantChartModel model,
			DefaultColumnModel columnModel) {
		super(model, columnModel, MMGPGantComponetFactory.getInstance());
		this.initListener();
		initUI();

		MouseWheelListener ml = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int rotation = e.getWheelRotation();
				JScrollBar bar = MMGPGantChart.this.getTreeTableScrollPane()
						.getVerticalScrollBar();
				MMGPGantChart.this.getTreeTableScrollPane()
						.getVerticalScrollBar()
						.setValue(bar.getValue() + 40 * rotation);
			}

		};
		this.getLayerContainer().addMouseWheelListener(ml);
		this.getTreeTable().addMouseWheelListener(ml);

	}

	public PrintGanttChart createPrintableGanttChart(PrintContext context) {
		if (printGantChart == null) {
			printGantChart = new MMGPPrintGantChart(context);
		}
		return printGantChart;
	}

	protected void initUI() {
		this.getTreeTable().setRootVisible(false);
		this.getLayerContainer().setDraggingEnabled(false);
		this.getLayerContainer().setLinkingEnabled(false);
		this.setTreeTableDragEnabled(false);

	}

	/* (non-Javadoc)
	 * @see nc.ui.pubapp.gantt.ui.gantentry.AppGanttChart#initAppPolicy()
	 * 将一些类 替换为MMGP的类
	 * 
	 */
	protected void initAppPolicy() {
		getPolicyProvider().setPolicy(INodeEditPolicy.class,
				new MMGPNodeEditPolicy());
		getPolicyProvider().setPolicy(IEditTimelineObjectPolicy.class,
				new AppTimelineObjEditPolicy());

		// 设置甘特图时间展示粒度
		if (getContext().getDatelineLeftRange() != null
				&& getContext().getDatelineRightRange() != null) {
			getDateline().getPolicyProvider().setPolicy(
					IZoomPolicy.class,
					new TimeGranularityZoomPolicy(TimeGranularity.getRange(
							getContext().getDatelineLeftRange(), getContext()
									.getDatelineRightRange())));
			getLayerContainer().getPolicyProvider().setPolicy(
					IZoomPolicy.class,
					new TimeGranularityZoomPolicy(TimeGranularity.getRange(
							getContext().getDatelineLeftRange(), getContext()
									.getDatelineRightRange())));
		}
		// 设置甘特图底部坐标时间粒度
		if (getContext().getCoordinateLineTimeGranularities() != null) {
			getLayerContainer().getPolicyProvider().setPolicy(
					IGridPolicy.class,
					new TimeGranularityGridPolicy(getContext()
							.getCoordinateLineTimeGranularities()));
		}

		// 设置甘特图底部事件线的坐标时间粒度控制
		if (getContext().getEventlineTimeGranularities() != null) {
			getEventline().getPolicyProvider().setPolicy(
					IGridPolicy.class,
					new TimeGranularityGridPolicy(getContext()
							.getEventlineTimeGranularities()));
		}

	}

	public void setGantRenderMap(Map<Class, ITimelineObjectRenderer> map) {
		this.getLayerContainer().setRendererMap(map);
	}

	public Map<Class, ITimelineObjectRenderer> getGantRenderMap() {
		return this.getLayerContainer().getRendererMap();
	}

	/**
	 * 支持甘特图编辑
	 */
	public void stopEditing() {
		ITreeTableCellEditor cellEditor = this.getTreeTable().getCellEditor();
		if (cellEditor != null) {
			cellEditor.stopCellEditing();
		}
	}

	/**
	 * 删除结点
	 */
	public void deleteSelNode() {
		TreePath[] treePaths = this.getTreeTable().getSelectionPaths();
		MMGPGanttChartNode[] nodes = new MMGPGanttChartNode[treePaths.length];
		if (!MMArrayUtil.isEmpty(treePaths)) {
			for (int i = 0; i < treePaths.length; i++) {
				nodes[i] = (MMGPGanttChartNode) treePaths[i]
						.getLastPathComponent();
			}
		}
		int minrow = -1;
		for (int row : this.getTreeTable().getRowsForPaths(treePaths)) {
			if (row < minrow || minrow < 0) {
				minrow = row;
			}
		}
		int nextSelRow = minrow - 1;

		getAppModel().deleteNodes(nodes);
		if (nextSelRow < 0) {
			return;
		}
		if (nextSelRow >= this.getTreeTable().getRowCount()) {
			return;
		}
		this.getTreeTable().setSelectionRow(nextSelRow);
	}

	public MMGPGanttChartNode addLine() {
		return addLine(null, true);

	}

	public MMGPGanttChartNode addLine(Object userobject) {
		return addLine(userobject, true);

	}

	/**
	 * 增行
	 * @param userobject
	 * @param changeselection
	 * @return
	 */
	public MMGPGanttChartNode addLine(Object userobject, boolean changeselection) {
		MMGPGanttChartNode parentNode = null;
		if (this.getSelectNode() == null
				|| this.getSelectNode().getParent() == null) {
			parentNode = (MMGPGanttChartNode) getAppModel().getRoot();

		} else {
			parentNode = (MMGPGanttChartNode) this.getSelectNode();
		}
		return addLine(parentNode, userobject, changeselection);
	}

	/**
	 * 增行
	 * @param parentNode
	 * @param userobject
	 * @param changeselection
	 * @return
	 */
	public MMGPGanttChartNode addLine(MMGPGanttChartNode parentNode,
			Object userobject, boolean changeselection) {
		if (parentNode == null) {
			parentNode = (MMGPGanttChartNode) getAppModel().getRoot();
		}
		MMGPGanttChartNode insertNode = ((MMGPGantChartModel) this
				.getAppModel()).addLine(parentNode, userobject);
		getTreeTable().updateNodes();
		getTreeTable().revalidate();
		if (changeselection) {
			MMGPGantChart.this.getTreeTable().getSelectionModel()
					.setSelectionPath(new TreePath(insertNode.getPath()));
		}
		String s = null;
		((MMGPGantChartModel) MMGPGantChart.this.getAppModel())
				.loadLoadRelationItemValue(s, insertNode);
		return insertNode;
	}

	/**
	 * 增行
	 * @param parentNode
	 * @param userobject
	 * @return
	 */
	public MMGPGanttChartNode addLine(MMGPGanttChartNode parentNode,
			Object userobject) {
		return addLine(parentNode, userobject, true);

	}

	/**
	 * 增行
	 * @param userobject
	 * @return
	 */
	public MMGPGanttChartNode addLineFromRoot(Object userobject) {
		return addLine(null, userobject, true);
	}

	/**
	 * 获得选择的数据(多选)
	 * 
	 * @return
	 */
	public List<MMGPGanttChartNode> getSelectNodes() {
		TreePath[] treePaths = getTreeTable().getSelectionModel()
				.getSelectionPaths();
		if (getTreeTable().getSelectionModel() == null
				|| MMArrayUtil.isEmpty(treePaths)) {
			return null;
		}

		List<MMGPGanttChartNode> list = new ArrayList<MMGPGanttChartNode>();
		for (TreePath treePath : treePaths) {
			list.add(((MMGPGanttChartNode) treePath.getLastPathComponent()));
		}
		return list;
	}

	/**
	 * 获得选择的Node(单选)
	 * 
	 * @return
	 */
	public MMGPGanttChartNode getSelectNode() {
		if (getTreeTable().getSelectionModel().getSelectionPath() == null) {
			return null;
		}
		Object appGanttChartNode = getTreeTable().getSelectionModel()
				.getSelectionPath().getLastPathComponent();
		if (null == appGanttChartNode) {
			return null;
		}
		return (MMGPGanttChartNode) appGanttChartNode;
	}

	public AbstractAppModel getManageModel() {
		return manageModel;
	}

	public void setManageModel(AbstractAppModel manageModel) {
		this.manageModel = manageModel;
	}

	public void initListener() {
		this.getTreeTable().getSelectionModel().addTreeSelectionListener(this);
	}

	TreePath oldpath;

	TreePath[] oldpaths;

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 * 
	 * 为发把选中行事件 转发到appModel中 走UI2的事件链
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {

		TreePath oldviewpath = null;
		if (oldpath != null) {
			oldviewpath = oldpath;
		}
		TreePath[] paths = this.getTreeTable().getSelectionPaths();
		TreePath path = this.getTreeTable().getSelectionPath();
		/* if (!isEqual(oldpaths, paths)) { */
		MMGPGantRowSelectEvent rowSelectEvent = new MMGPGantRowSelectEvent(
				this, oldviewpath, path);
		rowSelectEvent.setOldrows(oldpaths);
		rowSelectEvent.setRows(paths);
		this.getManageModel().fireEvent(rowSelectEvent);
		/* } */
		oldpath = path;
		oldpaths = paths;
	}

	// private boolean isEqual(TreePath[] oldPaths,
	// TreePath[] paths) {
	// if (ArrayUtils.isEmpty(oldPaths) && ArrayUtils.isEmpty(paths)) {
	// return true;
	// } else if (!ArrayUtils.isEmpty(oldPaths) && !ArrayUtils.isEmpty(paths)) {
	//
	// Arrays.sort(oldPaths);
	// Arrays.sort(paths);
	// return Arrays.equals(oldPaths, paths);
	// }
	// return false;
	// }

	/**
	 * 清除甘特图中数据
	 */
	public void clearViewData() {
		super.getAppModel().clear();
	}

	/**
	 * 设置选中 数据 value
	 * 
	 * @param voclass
	 * @param index
	 * @param key
	 * @param value
	 * @param datatype
	 */
	public void setSelNodeValue(Class voclass, int index, String key,
			Object value, int datatype) {
		MMGPGanttChartNode selNode = this.getSelectNode();
		setNodeValue(voclass, index, key, value, datatype, selNode);
	}

	/**
	 * 
	 * 设置值
	 * @param voclass
	 * @param index
	 * @param key
	 * @param value
	 * @param datatype
	 * @param selNode
	 */
	@SuppressWarnings("unchecked")
	public void setNodeValue(Class voclass, int index, String key,
			Object value, int datatype, MMGPGanttChartNode selNode) {
		if (selNode == null) {
			return;
		}
		Object aggvo = (Object) selNode.getTypedUserObject();
		CircularlyAccessibleValueObject operVO = null;
		if (aggvo instanceof MMGPAbstractBill) {
			if (((MMGPAbstractBill) aggvo).getParentVO().getClass()
					.equals(voclass)) {
				operVO = ((MMGPAbstractBill) aggvo).getParentVO();
			} else {
				operVO = (CircularlyAccessibleValueObject) ((MMGPAbstractBill) aggvo)
						.getChildren(voclass)[index];
			}
		}
		if (operVO != null) {
			if (datatype == IBillItem.MULTILANGTEXT) {
				MultiLangText mlt = (MultiLangText) value;

				operVO.setAttributeValue(key, mlt.getText());
				operVO.setAttributeValue(key + "2", mlt.getText2());
				operVO.setAttributeValue(key + "3", mlt.getText3());
				operVO.setAttributeValue(key + "4", mlt.getText4());
				operVO.setAttributeValue(key + "5", mlt.getText5());
				operVO.setAttributeValue(key + "6", mlt.getText6());
			} else {
				operVO.setAttributeValue(key, value);
			}
			this.getAppModel().updateNodeValue(aggvo);
		}
	}

	/**
	 * 在甘特图左侧表头左键->格式化全部列，点击后，会将隐藏的字段显示出来。 处理方法如下，可考虑加到MMGP中。
	 * 
	 * @author dongyshd
	 */
	@Override
	public void optimizeColumnWidth(boolean includeEditors) {
		List<GantItem> showItems = new ArrayList<GantItem>();
		MMGPGantItemContainer itemContainner = ((MMGPGantItemContainer) this
				.getAppModel().getContext().getItemContainer());
		GantItem[] gantItems = (GantItem[]) itemContainner.getAllItems();
		if (MMArrayUtil.isEmpty(gantItems)) {
			return;
		}
		for (GantItem gantitem : gantItems) {
			if (gantitem.isShow()) {
				showItems.add(gantitem);
			}
		}

		if (MMListUtil.isEmpty(showItems)) {
			return;
		}
		int count = getColumnCount();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			if (showItems.contains(((AppTreeTableColumn) getColumn(i))
					.getItem())) {
				list.add(i);
			}
		}
		if (MMListUtil.isEmpty(list)) {
			return;
		}
		for (Integer index : list) {
			optimizeColumnWidth(getColumn(index), includeEditors);
		}
		repaint();
	}

	@Override
	public void resetToPreferredSizes() {
		int divider = getSplitPane().getDividerLocation();
		if (divider == -1)
			getSplitPane().setDividerLocation(getChartWidth());
	}

	public void setGantTreeTableMenuProvider(ITreeTableMenuProvider provider) {
		getTreeTable().setMenuProvider(provider);
	}

}
