package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellRenderer;

import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.event.NCEventObject;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;
import nc.ui.bd.ref.event.RefEventConstant;
import nc.ui.mmgp.uif2.ref.gridtree.MMGPRefTableDelColumnManager.HyperlinkLabelRenderer;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;

public class MMGPRefGridSelectedData extends MMGPRefTablePane {

	private static final long serialVersionUID = 1L;
	
	private IEventListener delSelectedRowHandler = new IEventListener() {
		public void invoke(Object sender, NCEventObject evt) {
			if (evt.getProperty(RefEventConstant.SELECTED_ROWS) != null) {
				eventSource.fireEvent(new NCEventObject(RefEventConstant.DEL_SELECTED_ROWS, 
						RefEventConstant.SELECTED_ROWS,evt.getProperty("selectedRows")));
			}
		}
	};
	
	public HashMap<String, Integer> getTableColumnsWidth() {
        int colCount = getTable().getColumnModel().getColumnCount();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < colCount; i++) {
            String columnName = getTable().getColumnName(i);
            int columnWidth = getTable().getColumnModel().getColumn(i)
                    .getWidth();
            map.put(columnName, columnWidth);
        }
        return map;
    }
	
	public void initRowHeader() {
	       
		NCTableModel model = (NCTableModel) getTable().getModel();

		if (model == null) {
			return;
		}

		refNCtableModel.setShowCheckBoxColumn(true);

		UITable headerColumn = new UITable(refNCtableModel);
		headerColumn.setFocusable(false);

		headerColumn.createDefaultColumnsFromModel();

		headerColumn.setBackground(IRefConst.REFTABLEROWHEADER);
		headerColumn.setColumnSelectionAllowed(false);
		headerColumn.setCellSelectionEnabled(false);

		javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		headerColumn.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		

		MMGPRefTableDelColumnManager manager = new MMGPRefTableDelColumnManager(headerColumn);
		manager.addListener(RefEventConstant.DEL_SELECTED_ROWS, delSelectedRowHandler);


		JViewport jv = new JViewport();
		jv.setView(headerColumn);
		setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn.getTableHeader());

		jv.setPreferredSize(new Dimension(ROWHEADERWIDTH + 27, 0));

		setRowHeader(jv);
		
		initConn(headerColumn);
	}
	
	public void setTableColumnsWidth(HashMap<String, Integer> map) {
		if (map == null)
			return;
		int colCount = getTable().getColumnModel().getColumnCount();
		for (int i = 0; i < colCount; i++) {
			String columnName = getTable().getColumnName(i);
			if (map.get(columnName) != null) {
				int width = (int) map.get(columnName);
				getTable().getColumnModel().getColumn(i).setPreferredWidth(width);
			}
		}
		getTable().doLayout();
	}
	
	private void initConn(final JTable rowTable) {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				int columnIndex = rowTable.columnAtPoint(e.getPoint());
				int rowIndex =  rowTable.rowAtPoint(e.getPoint());
				TableCellRenderer renderer = rowTable.getCellRenderer(rowIndex,1);

				if (columnIndex == 1) {
					if(renderer instanceof HyperlinkLabelRenderer){
						((HyperlinkLabelRenderer)renderer).setHighlightRow(rowIndex);
						rowTable.repaint();
					}
					
					Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
					if (cursor != null) {
						rowTable.setCursor(cursor);
						e.consume();
					}
				} else {
					if(renderer instanceof HyperlinkLabelRenderer){
						((HyperlinkLabelRenderer) renderer).setHighlightRow(-1);
						rowTable.repaint();
					}
					rowTable.setCursor(null);
				}
			}
			public void mouseExited(MouseEvent e){
				int rowIndex =  rowTable.rowAtPoint(e.getPoint());
				TableCellRenderer renderer = rowTable.getCellRenderer(rowIndex,1);
	
				if (renderer instanceof HyperlinkLabelRenderer) {
					((HyperlinkLabelRenderer) renderer).setHighlightRow(-1);
					rowTable.repaint();
				}
				
			}
		};
		rowTable.addMouseListener(mouseAdapter);
		rowTable.addMouseMotionListener(mouseAdapter);
	}
}
