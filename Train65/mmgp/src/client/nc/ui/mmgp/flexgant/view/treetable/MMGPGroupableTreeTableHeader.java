package nc.ui.mmgp.flexgant.view.treetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;

import com.dlsc.flexgantt.model.treetable.ColumnModelIterator;
import com.dlsc.flexgantt.model.treetable.SortDirection;
import com.dlsc.flexgantt.model.treetable.TreeTableColumn;
import com.dlsc.flexgantt.swing.AbstractGanttChart;
import com.dlsc.flexgantt.swing.treetable.IColumnHeaderRenderer;
import com.dlsc.flexgantt.swing.treetable.TreeTableHeader;



public class MMGPGroupableTreeTableHeader extends TreeTableHeader {

	public MMGPGroupableTreeTableHeader(AbstractGanttChart gc) {
		super(gc);
	}

	
	protected Vector columnGroups = null;
	
	
	
	/**
	 * Paints all column headers.
	 * 
	 * @see #paintColumnHeader(Graphics, TreeTableColumn, int, int, int, int)
	 * @param g
	 *            the graphics context into which to draw
	 * @since 1.0
	 */
	protected void paintColumnHeaders(Graphics g) {

		int x = 0;
		AbstractGanttChart ganttChart = getGanttChart();
		ColumnModelIterator iter = new ColumnModelIterator(this.getModel(),
				ganttChart.getKeyColumnPosition());
		ColumnModelIterator nextiter = new ColumnModelIterator(this.getModel(),
				ganttChart.getKeyColumnPosition());
		int groupWidth = 0;
		TreeTableColumn nextcolumn=null;
		if(nextiter.hasNext()){
			nextcolumn = nextiter.next();
		}
		while (iter.hasNext()) {
			TreeTableColumn column = iter.next();
			if(nextiter.hasNext()){
				nextcolumn = nextiter.next();
			} else {
				nextcolumn =null;
			}
			MMGPGantColumnGroup cGroup = this.getColumnGroupByCol(column);
			if (cGroup != null) {
				int width = column.getWidth();
				paintColumnHeader(g, column, x + 1, getHeight() / 2, width - 2,
						getHeight() / 2);
				g.setColor(this.getGridColor());
				g.drawLine(x - 1, getHeight()/2, x+width-1, getHeight()/2);
				x += width;
				g.drawLine(x - 1, getHeight() / 2, x - 1, getHeight());
				groupWidth += width;
				if(nextcolumn ==null||cGroup!=this.getColumnGroupByCol(nextcolumn)){
					g.drawLine(x - 1,0, x - 1, getHeight()/2);
					g.setColor(getForeground());
					FontMetrics fm = g.getFontMetrics();
					int strWidth = fm.stringWidth(cGroup.getText());
					if (groupWidth > strWidth) {
						g.drawString(cGroup.getText(), x 
								- (groupWidth / 2 + strWidth /2),
								getHeight() / 3);
					} else {
						int strdianWidth = fm.stringWidth("...");
						g.drawString("...", x 
								- (groupWidth / 2 + strdianWidth / 2),
								getHeight() / 3);
					}
					groupWidth = 0;
				}
			} else {
				int width = column.getWidth();
				paintColumnHeader(g, column, x + 1, 0, width - 2, getHeight());
				x += width;
				g.setColor(this.getGridColor());
				g.drawLine(x - 1, 0, x - 1, getHeight());
			}
		}

		if (this.isShowingFillerColumn()) {
			int width = getWidth() - this.getModel().getTotalColumnWidth();
			paintColumnHeader(g, this.getFillerColumn(), x, 0, width, getHeight());
		}
	}

	public void addColumnGroup(MMGPGantColumnGroup currentCG) {
		if (columnGroups == null) {
			columnGroups = new Vector();
		}
		columnGroups.addElement(currentCG);
		
	}
	
	/**
	 * 
	 * 创建日期:(2001-8-31 15:54:29)
	 */
	public void clearColumnGroups() {
		columnGroups = null;
	}
	
	public MMGPGantColumnGroup[] getColumnGroups() {
		MMGPGantColumnGroup[] retg = null;
		if (columnGroups != null && columnGroups.size() > 0) {
			retg = new MMGPGantColumnGroup[columnGroups.size()];
			columnGroups.copyInto(retg);
		}
		return retg;
	}
	
	public Enumeration getColumnGroups(TreeTableColumn col) {
		if (columnGroups == null)
			return null;
		Enumeration enums = columnGroups.elements();
		while (enums.hasMoreElements()) {
			MMGPGantColumnGroup cGroup = (MMGPGantColumnGroup) enums.nextElement();
			Vector v_ret = (Vector) cGroup.getColumnGroups(col, new Vector());
			if (v_ret != null) {
				return v_ret.elements();
			}
		}
		return null;
	}

	public MMGPGantColumnGroup getColumnGroupByCol(TreeTableColumn col) {
		if (columnGroups == null)
			return null;
		Enumeration enums = columnGroups.elements();
		while (enums.hasMoreElements()) {
			MMGPGantColumnGroup cGroup = (MMGPGantColumnGroup) enums.nextElement();
			Vector v_ret = (Vector) cGroup.getColumnGroups(col, new Vector());
			if (v_ret != null) {
				return (MMGPGantColumnGroup) v_ret.get(0);
			}
		}
		return null;
	}
}
