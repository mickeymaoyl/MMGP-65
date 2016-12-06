package nc.ui.mmgp.flexgant.view.treetable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreePath;

import com.dlsc.flexgantt.command.CommandStackEvent;
import com.dlsc.flexgantt.command.ICommand;
import com.dlsc.flexgantt.command.ICommandStack;
import com.dlsc.flexgantt.command.ICommandStackListener;
import com.dlsc.flexgantt.model.treetable.ITreeTableModel;
import com.dlsc.flexgantt.model.treetable.TreeTableColumn;
import com.dlsc.flexgantt.policy.treetable.INodeEditPolicy;
import com.dlsc.flexgantt.swing.AbstractGanttChart;
import com.dlsc.flexgantt.swing.GanttChart;
import com.dlsc.flexgantt.swing.layer.LayerContainerScrollPane;
import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.TreeTable;
import com.dlsc.flexgantt.swing.treetable.TreeTableNode;
import com.dlsc.flexgantt.swing.treetable.TreeTableScrollPane;
import com.dlsc.flexgantt.swing.DefaultTreeTableCellEditor;

import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.flexgant.model.MMGPNodeEditPolicy;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.textfield.formatter.ParseException;
import nc.ui.pubapp.gantt.ui.treetable.AppGantTreeTable;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class MMGPGantTreetable extends AppGantTreeTable {

	@SuppressWarnings("rawtypes")
	public MMGPGantTreetable(AbstractGanttChart ganttChart,
			ITreeTableModel model) {
		super(ganttChart, model);
	}

	public void init() {
		super.init();
		setCellEditor(Object.class, new MMGPGenericEditor());
		// this.setCellEditor(Number.class, new MMGPNumberEditor());
		this.getPolicyProvider().setPolicy(INodeEditPolicy.class,
				new MMGPNodeEditPolicy());
	}

	/*
	 * Generic Editor
	 */
	/*
	 * @see MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
	 * 
	 * @since 1.0
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (this.getRowCount() >= this.getFirstVisibleRow())
			super.mouseMoved(e);
	}

	/**
	 * This method will walk down the table entry hierarchy and convert it into
	 * a TreeTableNode[] array. This array is used internally to store the
	 * table's state and to draw the table.
	 * 
	 * @param expandAll
	 *            will automatically expand all tree nodes
	 * @since 1.0
	 */
	public void updateNodes(boolean expandAll) {
		// LOGGER.fine("updating table entries"); //$NON-NLS-1$
		// nodes = new ArrayList<TreeTableNode>();
		/*****************************/
//		List<TreeTableNode> list = new ArrayList<TreeTableNode>();
//		setNodes(list);
//		// modelMap = new HashMap<TreePath, TreeTableNode>();
//		setModelMap(new HashMap<TreePath, TreeTableNode>());
//		if (isRootVisible()) {
//			getNodes().add(this.getRootNode());
//			getModelMap().put(this.getRootNode().getPath(), this.getRootNode());
//		}
//		updateNodes(this.getRootNode(), 0, expandAll);
//		setTotalHeight(0);
//		int row = 0;
//		for (TreeTableNode node : getNodes()) {
//			node.setRow(row++);
//			node.setY(getTotalHeight());
//			// totalHeight += node.getHeight(); // now height gets init
//			this.setTotalHeight(this.getTotalHeight() + node.getHeight());
//		}
//
//		if (this.getLc() != null) {
//			this.getLc().revalidate();
//			this.getLc().repaint();
//		}
		/************************************/
		// revalidate();
		// repaint();
		//
		// getRowHeader().revalidate();
		// getRowHeader().repaint();
		TreeTableScrollPane[] treeTableScrollPanes = getGanttChart()
				.getTreeTableScrollPanes();
		if (treeTableScrollPanes != null && treeTableScrollPanes.length > 0) {
			for (TreeTableScrollPane treeTableScrollPane : treeTableScrollPanes) {
				treeTableScrollPane.revalidate();
				treeTableScrollPane.repaint();
			}
		}

		LayerContainerScrollPane[] layerContainerScrollPanes = getGanttChart()
				.getLayerContainerScrollPanes();
		if (layerContainerScrollPanes != null
				&& layerContainerScrollPanes.length > 0) {
			for (LayerContainerScrollPane layContainerScrollPane : layerContainerScrollPanes) {
				layContainerScrollPane.revalidate();
				layContainerScrollPane.repaint();
				layContainerScrollPane.getVerticalScrollBar().revalidate();
				layContainerScrollPane.getVerticalScrollBar().repaint();
			}
		}
		// tangxya add
		if (!(getGanttChart() instanceof GanttChart)) {
			revalidate();
			repaint();
			return;
		}
		// tangxya ends
		((GanttChart) getGanttChart()).getSplitPane().revalidate();
		((GanttChart) getGanttChart()).getSplitPane().repaint();
	}

	@Override
	public void updateNodes() {
		if (getGanttChart() instanceof GanttChart
				&& ((GanttChart) getGanttChart()).getSplitPane() == null) {
			return;
		}
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						updateNodes(false);
					}
				});
			} catch (Exception ex) {
				ExceptionUtils.wrappException(ex);
			}
		} else {
			updateNodes(false);
		}
	}

	@Override
	protected void paintCell(Graphics g, TreeTableColumn column,
			TreeTableNode node, int x, int y, int width, int height, int row,
			int columnIndex, int modelIndex, Class columnClass,
			boolean fillerColumn) {
		if (this.isEditing() && this.getEditingRow() == row
				&& this.getEditingColumn() == columnIndex) {
			Component component = this.getEditorComponent();
			component.setBounds(new Rectangle(x, y, width, height));
			component.validate();
		} else {
			super.paintCell(g, column, node, x, y, width, height, row,
					columnIndex, modelIndex, columnClass, fillerColumn);
		}
	}
	
	/**
	 * @param path
	 * 
	 * 上移
	 */
	public void upMoveNode(TreePath path) {
		if (path != null) {
			if (isUpMoveableSelection(path)) {
				doUpMoveNode(path);
			} else {
				getGanttChart().showMessage(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0113")/*无法上移*/);
			}
		}
	}
	
	/**
	 * @param path
	 * 
	 * 上移
	 */
	private void doUpMoveNode(TreePath path) {
		int count = 1;
		int[] oldChildIndices = new int[count];
		int[] newChildIndices = new int[count];
		Object[] nodes = new Object[count];

		TreePath firstPath = path;
		Object draggedNode = firstPath.getLastPathComponent();
		Object parent = firstPath.getParentPath().getLastPathComponent();
		int childCount = getModel().getChildCount(parent);
		if (childCount > count) {
			int oldChildIndex = getModel().getIndexOfChild(parent, draggedNode);
			if (oldChildIndex>0) {
				/*
				 * The first child of a node can not be indented any further
				 * because there is no node that can be used as a new parent.
				 */
				final TreePath[] animatedPaths = new TreePath[count];
				for (int i = 0; i < count; i++) {
					draggedNode = path.getLastPathComponent();
					oldChildIndices[i] = getModel().getIndexOfChild(parent,
							draggedNode);
					newChildIndices[i] = oldChildIndices[i] - 1;
					nodes[i] = draggedNode;
					animatedPaths[i] = path.getParentPath().pathByAddingChild(
							draggedNode);
				}

				performUpMove(parent, oldChildIndices, parent,
						newChildIndices, nodes, animatedPaths);
			}
		}
	}
	
	private void performUpMove(Object oldParent, int[] oldChildIndices,
			Object newParent, int[] newChildIndices, Object[] nodes,
			final TreePath[] indentedPaths) {
		INodeEditPolicy ip = getPolicyProvider().getPolicy(
				INodeEditPolicy.class);
		final ICommand cmd = ip.getReassignmentCommand(nodes, oldParent,
				oldChildIndices, newParent, newChildIndices, getModel());
		final ICommand fCmd = cmd;
		final ICommandStack stack = getGanttChart().getCommandStack();
		stack.addCommandStackListener(new ICommandStackListener() {
			public void commandStackChanged(CommandStackEvent e) {
				if (e.getCommand() == fCmd
						&& e.getId().equals(
								CommandStackEvent.ID.COMMAND_EXECUTED)) {
					stack.removeCommandStackListener(this);
					try {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								setSelectionPaths(indentedPaths);
								expandPath(indentedPaths[0].getParentPath());
							}
						});
					} catch (Exception ex) {
						ExceptionUtils.wrappException(ex);
					}
				}
			}
		});
		getGanttChart().commandExecute(cmd);
	}
	
	protected boolean isUpMoveableSelection(TreePath path) {
		Object upMoveNode = path.getLastPathComponent();
		Object parent = path.getParentPath().getLastPathComponent();
		int upMoveNodeIndex = getModel()
				.getIndexOfChild(parent, upMoveNode);

		if (upMoveNodeIndex > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param path
	 * 
	 * 下移
	 */
	public void downMoveNode(TreePath path) {
		if (path != null) {
			if (isDownMoveableSelection(path)) {
				doDownMoveNode(path);
			} else {
				getGanttChart().showMessage(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0114")/*无法下移*/);
			}
		}
	}

	/**
	 * @param path
	 * 
	 * 下移
	 */
	private void doDownMoveNode(TreePath path) {
		int count = 1;
		int[] oldChildIndices = new int[count];
		int[] newChildIndices = new int[count];
		Object[] nodes = new Object[count];

		TreePath firstPath = path;
		Object draggedNode = firstPath.getLastPathComponent();
		Object parent = firstPath.getParentPath().getLastPathComponent();
		int childCount = getModel().getChildCount(parent);
		if (childCount > count) {
			int oldChildIndex = getModel().getIndexOfChild(parent, draggedNode);
			if (oldChildIndex+1<childCount) {
				/*
				 * The first child of a node can not be indented any further
				 * because there is no node that can be used as a new parent.
				 */
				final TreePath[] animatedPaths = new TreePath[count];
				for (int i = 0; i < count; i++) {
					draggedNode = path.getLastPathComponent();
					oldChildIndices[i] = getModel().getIndexOfChild(parent,
							draggedNode);
					newChildIndices[i] = oldChildIndices[i] + 1;
					nodes[i] = draggedNode;
					animatedPaths[i] = path.getParentPath().pathByAddingChild(
							draggedNode);
				}

				performDownMove(parent, oldChildIndices, parent,
						newChildIndices, nodes, animatedPaths);
			}
		}
	}

	private void performDownMove(Object oldParent, int[] oldChildIndices,
			Object newParent, int[] newChildIndices, Object[] nodes,
			final TreePath[] indentedPaths) {
		INodeEditPolicy ip = getPolicyProvider().getPolicy(
				INodeEditPolicy.class);
		final ICommand cmd = ip.getReassignmentCommand(nodes, oldParent,
				oldChildIndices, newParent, newChildIndices, getModel());
		final ICommand fCmd = cmd;
		final ICommandStack stack = getGanttChart().getCommandStack();
		stack.addCommandStackListener(new ICommandStackListener() {
			public void commandStackChanged(CommandStackEvent e) {
				if (e.getCommand() == fCmd
						&& e.getId().equals(
								CommandStackEvent.ID.COMMAND_EXECUTED)) {
					stack.removeCommandStackListener(this);
					try {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								setSelectionPaths(indentedPaths);
								expandPath(indentedPaths[0].getParentPath());
							}
						});
					} catch (Exception ex) {
						ExceptionUtils.wrappException(ex);
					}
				}
			}
		});
		getGanttChart().commandExecute(cmd);
	}

	protected boolean isDownMoveableSelection(TreePath path) {
		Object downMoveNode = path.getLastPathComponent();
		Object parent = path.getParentPath().getLastPathComponent();
		int downMoveNodeIndex = getModel()
				.getIndexOfChild(parent, downMoveNode);

		int count = getModel().getChildCount(parent);
		if (downMoveNodeIndex + 1 < count) {
			return true;
		} else {
			return false;
		}
	}

	protected static class MMGPGenericEditor extends DefaultTreeTableCellEditor {

		/*
		 * Argument type declaration. It is always a single argument method
		 * taking a String value.
		 */
		private final Class[] argTypes = new Class[] { String.class };

		/*
		 * The value's constructor.
		 */
		private Constructor constructor;

		/*
		 * The value that gets edited.
		 */
		private Object value;

		public MMGPGenericEditor() {
			this(new JTextField());
		}

		/**
		 * Constructs a new generic editor.
		 * 
		 * @since 1.0
		 */

		public MMGPGenericEditor(final JTextField textField) {
			super(textField);
			editorComponent = textField;
			this.clickCountToStart = 2;
			delegate = new MMGPEditorDelegate() {
				public void setValue(Object value) {
					textField.setText((value != null) ? value.toString() : "");
				}

				public Object getCellEditorValue() {
					return textField.getText();
				}

				@Override
				public boolean stopCellEditing() {
					// TODO Auto-generated method stub
					if (textField instanceof UITextField) {
						try {
							((UITextField) textField).stopEditing();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							return false;
						}
					}
					return super.stopCellEditing();
				}
			};

			textField.addActionListener(delegate);
			textField.addFocusListener((FocusListener) delegate);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see DefaultCellEditor#stopCellEditing()
		 */
		@Override
		public boolean stopCellEditing() {
			String s = (String) super.getCellEditorValue();
			// Here we are dealing with the case where a user
			// has deleted the string value in a cell, possibly
			// after a failed validation. Return null, so that
			// they have the option to replace the value with
			// null or use escape to restore the original.
			// For Strings, return "" for backward compatibility.
			if ("".equals(s)) { //$NON-NLS-1$
				if (constructor.getDeclaringClass() == String.class) {
					value = s;
				}
				super.stopCellEditing();
			}
			try {
				value = constructor.newInstance(new Object[] { s });
			} catch (Exception e) {
				((JComponent) getComponent()).setBorder(new LineBorder(
						Color.red));
				return false;
			}
			return super.stopCellEditing();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * DefaultTreeTableCellEditor#getTreeTableCellEditorComponent(TreeTable,
		 * Object, boolean, int, int)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public Component getTreeTableCellEditorComponent(TreeTable tree,
				Object value, boolean selected, int row, int column) {
			this.value = null;
			((JComponent) getComponent())
					.setBorder(new LineBorder(Color.black));
			Class type = tree.getColumnClass(column);
			if (value != null) {
				type = value.getClass();
			}
			// Since our obligation is to produce a value which is
			// assignable for the required type it is OK to use the
			// String constructor for columns which are declared
			// to contain Objects. A String is an Object.
			if (type == Object.class) {
				type = String.class;
			}
			try {
				constructor = type.getConstructor(argTypes);
			} catch (SecurityException ex) {
				ExceptionUtils.wrappException(ex);
			} catch (NoSuchMethodException ex) {
				ExceptionUtils.wrappException(ex);
			}
			return super.getTreeTableCellEditorComponent(tree, value, selected,
					row, column);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see DefaultCellEditor#getCellEditorValue()
		 */
		@Override
		public Object getCellEditorValue() {
			return value;
		}

		protected class MMGPEditorDelegate extends EditorDelegate implements
				FocusListener {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!e.isTemporary() && editorComponent != null
						&& editorComponent.getParent() instanceof TreeTable) {
					ITreeTableCellEditor editor = ((TreeTable) editorComponent
							.getParent()).getCellEditor();
					if (editor != null) {
						editor.stopCellEditing();
					}
				}
			}
		}
	}

	/**
	 * An editor for numbers (double, float, ...).
	 * 
	 * @since 1.0
	 */
	public static class MMGPNumberEditor extends MMGPGenericEditor {

		/**
		 * Construcs a new editor.
		 * 
		 * @since 1.0
		 */
		public MMGPNumberEditor() {
			((JTextField) getComponent())
					.setHorizontalAlignment(SwingConstants.RIGHT);

		}
	}

	public void setCellEditor(Class cl, ITreeTableCellEditor editor) {
		super.setCellEditor(cl, editor);

	}
}
