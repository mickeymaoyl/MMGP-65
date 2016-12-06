package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.event.NCEventSource;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.table.NCTableModel;

/**
 * 
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class MMGPRefTablePane extends UITablePane {
	protected RefNCtableModel refNCtableModel = new RefNCtableModel();
	public static int ROWHEADERWIDTH = 58;
	private int baseNumber = 0;
	private boolean isShowCheckBox = false;
	
    protected NCEventSource eventSource = new NCEventSource(this);
	
	class RefNCtableModel extends AbstractTableModel {
		int rowCount = 0;
		int colCount = 1;

		public RefNCtableModel() {

		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see nc.ui.pub.beans.table.NCTableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int column) {

			if (column == 0) {
				return Integer.toString(row + 1);
			}
			return null;

		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		public String getColumnName(int column) {
			// TODO 自动生成方法存根
			return "";
		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			// TODO 自动生成方法存根
			return colCount;
		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			// TODO 自动生成方法存根
			return rowCount;
		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see
		 * nc.ui.pub.beans.table.NCTableModel#setDataVector(java.util.Vector)
		 */
		public void setDataVector(Vector data) {
			// TODO 自动生成方法存根
			rowCount = data.size();
			fireTableDataChanged();
		}

		public void addRow() {
			rowCount += 1;
			fireTableDataChanged();
		}

		public void removeRow() {
			if (rowCount < 1) {
				return;
			}
			rowCount = rowCount - 1;
			fireTableDataChanged();
		}
		public void setShowCheckBoxColumn(boolean isShowCheckBox) {
			if (isShowCheckBox)
				colCount = 2;
			else
				colCount = 1;
		}
	}

	public void setDataVector(Vector data) {

		if (data == null) {
			return;
		}
		NCTableModel tableModel = (NCTableModel) getTable().getModel();
		tableModel.setDataVector(data);
		getRefNCtableModel().setDataVector(data);
		setTableColumn();
	}
	public void setDataVector(int baseNumber, Vector data) {
		this.baseNumber = baseNumber;
		NCTableModel tableModel = (NCTableModel) getTable().getModel();
		tableModel.setDataVector(data);
		getRefNCtableModel().setDataVector(data);
		setTableColumn();	
	}

	public void addRow(Vector rowData) {
		((NCTableModel) getTable().getModel()).addRow(rowData);
		getRefNCtableModel().addRow();
		setTableColumn();
	}

	public void removeRow(int row) {
		((NCTableModel) getTable().getModel()).removeRow(row);
		getRefNCtableModel().removeRow();
		setTableColumn();
	}
	public void initRowHeader() {
       
		NCTableModel model = (NCTableModel) getTable().getModel();

		if (model == null) {
			return;
		}

		UITable headerColumn = new UITable(refNCtableModel);

		//	
		headerColumn.createDefaultColumnsFromModel();

		headerColumn.setBackground(IRefConst.REFTABLEROWHEADER);
		headerColumn.setColumnSelectionAllowed(false);
		headerColumn.setCellSelectionEnabled(false);

		javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		headerColumn.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		
		JViewport jv = new JViewport();
		jv.setView(headerColumn);
		setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn
				.getTableHeader());

		jv.setPreferredSize(new Dimension(ROWHEADERWIDTH, 0));

		setRowHeader(jv);
	}

	/**
	 * @return 返回 refNCtableModel。
	 */
	public RefNCtableModel getRefNCtableModel() {
		return refNCtableModel;
	}
	@Override
	public JScrollBar createHorizontalScrollBar() {
		JScrollBar bar = new JScrollBar(JScrollBar.HORIZONTAL){
			
			@Override
			public void setBounds(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				super.setBounds(1, y, x+width-1, height);
			}
		};
		
		bar.setBorder(null);
		// TODO Auto-generated method stub
		return bar;
		
	}
	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}
	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	private void setTableColumn() {
//		if (getTable().getColumnCount() < 4) {
//			getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
//			getTable().doLayout();
//		}
	}
	
	public boolean isEventsEnabled() {
		return eventSource.isEventsEnabled();
	}


	public void setEventsEnabled(boolean eventsEnabled) {
		eventSource.setEventsEnabled(eventsEnabled);
	}

	public void addListener(String eventName, IEventListener listener) {
		eventSource.addListener(eventName, listener);
	}


	public void removeListener(IEventListener listener) {
		eventSource.removeListener(listener);
	}


	public void removeListener(IEventListener listener, String eventName) {
		eventSource.removeListener(listener, eventName);
	}
	
}
