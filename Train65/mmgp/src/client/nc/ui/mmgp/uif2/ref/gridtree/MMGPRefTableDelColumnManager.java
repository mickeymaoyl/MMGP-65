package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.ref.event.NCEventObject;
import nc.ui.bd.ref.event.NCEventSource;
import nc.ui.bd.ref.event.RefEventConstant;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UILabel;
import nc.uitheme.ui.ThemeResourceCenter;

public class MMGPRefTableDelColumnManager {


    private JTable table;
    private int hyperlinkLabelColumn = 1;
    private UILabel headerHyperlinkLabel;
    private MouseListener nowMouseListen = null;
    
    private static final int DFAULT_MAX_WIDTH = 45;

    private static final int DFAULT_MIN_WIDTH = 45;

    private static final int DFAULT_PREFERRED_WIDTH = 45;

    private int maxWidth = DFAULT_MAX_WIDTH;

    private int minWidth = DFAULT_MIN_WIDTH;

    private int preferredWidth = DFAULT_PREFERRED_WIDTH;
    

    
    protected NCEventSource eventSource = new NCEventSource(this);
    
	
    public MMGPRefTableDelColumnManager(JTable table) {
        this.table = table;
        addTableMouseListener();
        setColumnAttributes();
        setHeaderShowHyperlinkLabel(1);
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


	
	
    private void setHeaderShowHyperlinkLabel(int col) {
    	hyperlinkLabelColumn = col;
        final JTableHeader jh = table.getTableHeader();
        TableColumnModel headerColumnMode = jh.getColumnModel();

        headerColumnMode.getColumn(hyperlinkLabelColumn).setHeaderRenderer(
                new TableHeaderRenderer(getHeaderHyperlinkLabel()));
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column;
                if ((column = jh.columnAtPoint(e.getPoint())) != -1) {
                    if (column == hyperlinkLabelColumn) {
						int len = table.getRowCount();
						int[] selectedRows = new int[len];
						for (int i = 0; i < len; i++) {
							selectedRows[i] = i;
						}
						clickHyperlinkLabel(selectedRows);
                        jh.repaint();
                    }
                }
            }
        	
			public void mouseMoved(MouseEvent e) {
				int columnIndex = jh.getTable().columnAtPoint(e.getPoint());
				if (columnIndex == hyperlinkLabelColumn) {
					Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
					if (cursor != null) {
						jh.setCursor(cursor);
						e.consume();
					}
				} else {
					jh.setCursor(null);
				}
			}
		};
        jh.addMouseListener(mouseAdapter);
        jh.addMouseMotionListener(mouseAdapter );
    }
	

    private void addTableMouseListener() {
        nowMouseListen = new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getButton() != java.awt.event.MouseEvent.BUTTON1)
                    return;
                if (e.getClickCount() == 1) {
                    int selectRow = MMGPRefTableDelColumnManager.this.table.getSelectedRow();
                    if (selectRow > -1) {
                        int col = MMGPRefTableDelColumnManager.this.table.columnAtPoint(e.getPoint());
                        if (col != -1 && col == hyperlinkLabelColumn) {
							clickHyperlinkLabel(new int[] { selectRow });
                            table.revalidate();
                            table.repaint();
                        }
                    }
                }
            }
        };
        table.addMouseListener(nowMouseListen);
    }
    
    private void clickHyperlinkLabel(int[] selectedRows){
		eventSource.fireEvent(new NCEventObject(RefEventConstant.DEL_SELECTED_ROWS, RefEventConstant.SELECTED_ROWS,selectedRows));
    }
    
    private void setColumnAttributes() {
        TableColumn tc = table.getColumnModel().getColumn(hyperlinkLabelColumn);
        tc.setPreferredWidth(preferredWidth);
        tc.setMaxWidth(maxWidth);
        tc.setMinWidth(minWidth);
        tc.setCellRenderer(getHyperlinkLabelCellRenderer());
    }
    


    private TableCellRenderer getHyperlinkLabelCellRenderer() {
		HyperlinkLabelRenderer renderer = new HyperlinkLabelRenderer();
        return renderer;
    }
    

    
    private UILabel getHeaderHyperlinkLabel() {
        if (headerHyperlinkLabel == null) {
        	headerHyperlinkLabel = new UILabel();
        	headerHyperlinkLabel.setHyperlinkLabel(true);
        	headerHyperlinkLabel.setText(NCLangRes.getInstance().getStrByID("ref", "RefTableDelColumnManager-0000")/*Çå¿Õ*/);
        	headerHyperlinkLabel.setForeground(Color.blue);
        	headerHyperlinkLabel.setHorizontalAlignment(JLabel.CENTER);
        }
        return headerHyperlinkLabel;
    }
    

	

    class HyperlinkLabelRenderer implements TableCellRenderer {

    	private UILabel editorHyperlinkLabel = null;
    	private Icon cutIcon = ThemeResourceCenter.getInstance().getImage("themeres/ui/cut.png");
    	private Icon cutHeighIcon = ThemeResourceCenter.getInstance().getImage("themeres/ui/cut_highlight.png");
		private int highlightRow = -1;
    	
        public HyperlinkLabelRenderer() {
			editorHyperlinkLabel = getHyperLinkLabel();

        }
        
    	private UILabel getHyperLinkLabel() {
    		if (editorHyperlinkLabel == null) {
    			editorHyperlinkLabel = new UILabel(cutIcon);
    			editorHyperlinkLabel.setHorizontalAlignment(JLabel.CENTER);
    		}
    		return editorHyperlinkLabel;
    	}

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
			if (row == getHighlightRow()) {
				editorHyperlinkLabel.setIcon(cutHeighIcon);
			} else {
				editorHyperlinkLabel.setIcon(cutIcon);
			}
        	return editorHyperlinkLabel;
        }

		public void setHighlightRow(int highlightRow) {
			this.highlightRow = highlightRow;
		}

		public int getHighlightRow() {
			return highlightRow;
		}
    }
    
    class TableHeaderRenderer implements TableCellRenderer {
        private UILabel renderLabel;

        public TableHeaderRenderer(UILabel renderLabel) {
        	this.renderLabel = renderLabel;
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            return this.renderLabel;
        }
    }
}
