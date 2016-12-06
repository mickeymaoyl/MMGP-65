package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.ref.event.NCEventObject;
import nc.ui.bd.ref.event.NCEventSource;
import nc.ui.bd.ref.event.RefEventConstant;
import nc.ui.bd.ref.event.NCEventSource.IEventListener;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UITable;
import nc.uitheme.ui.ThemeResourceCenter;



public class MMGPRefTableCheckboxManager {

	public static final int SELECT_PART = 0;//部份选标志位

	public static final int SELECT_ALL = 1;//全选标志位

	public static final int SELECT_NONE = 2;//全部不选标志位
	
	private static final int DFAULT_MAX_WIDTH = 45;

	private static final int DFAULT_MIN_WIDTH = 45;

	private static final int DFAULT_PREFERRED_WIDTH = 45;

	private byte[] tableCheck = new byte[0];//用来记录每行是否选中的数组，其中0表示没勾选，1表示勾选上了

	private IndexJCheckBox[] templetBox = new IndexJCheckBox[] {
			new IndexJCheckBox((String) null, false),
			new IndexJCheckBox((String) null, true) };

	private UITable table;

	private UICheckBox headerCheckBox;

	private int checkStata = -1;//整个表格的选择状态，是全选、或部份选及全不选，初始化时是无状态
	
	private boolean isMultiSelected = false;//多选参照有多选及单选两个状态，这个值用于标识该状态
	

	private Map<Integer, Object> selectedIndexMap = new HashMap<Integer, Object>();

	private int tableCheckedCount = 0;

	private boolean fireChanged = true;

	private int headerCheckBoxColumn = 1;

	private int checkBoxColumn = 1;

	private int maxWidth = DFAULT_MAX_WIDTH;

	private int minWidth = DFAULT_MIN_WIDTH;

	private int preferredWidth = DFAULT_PREFERRED_WIDTH;

	private IndexJCheckBox editorCheckBox = new IndexJCheckBox();

	private MouseListener nowMouseListen = null;
	
	private NCEventSource eventSource = new NCEventSource(this);

	private ActionListener actionChange = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			clickCheckBox(table.getEditingRow());
			checkedChanged2(table.getEditingRow());
		}
	};

	public MMGPRefTableCheckboxManager(UITable table) {
		this.table = table;
		initCheckBox();
		addTableMouseListener();
		setColumnAttributes();
		setHeaderShowCheckbox(1);
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				addCheckBoxs(((MMGPRefTablePane.RefNCtableModel) e.getSource())
						.getRowCount());
			}
		});
	}


	public void close() {
		clear();
		table = null;
	}

	public void setSelected(int[] indexs) {
		if (indexs != null) {
			for (int i = 0; i < getTableCheck().length; i++) {
				getTableCheck()[i] = 0;
			}
			for (int i = 0; i < indexs.length; i++) {
				if (!(getTableCheck().length < indexs[i])) {
					getTableCheck()[indexs[i]] = 1;
					selectedIndexMap.put(indexs[i], null);
				}
			}
		} else {
			clear();
		}
		setTableCheckedCount(selectedIndexMap.size());
		isMultiSelected = true;
		updateStata();
		table.revalidate();
		table.repaint();
	}
	
	public void setNotSelected(int[] indexs) {
		if (indexs == null)
			return;
		for (int i = 0; i < indexs.length; i++) {
			getTableCheck()[indexs[i]] = 0;
		}
		for (int i = indexs.length - 1; i >= 0; i--) {
			selectedIndexMap.remove(indexs[i]);
		}
		setTableCheckedCount(selectedIndexMap.size());
		updateStata();
		table.revalidate();
		table.repaint();
	}
	
	public void clickTableRow(int index, boolean isDrag) {
		if (getTableCheckedCount() == 0 && !isMultiSelected()) {
			clickCheckBoxAndCheckedChanged(index);
		} else if (getTableCheckedCount() == 1 && !isDrag && !isMultiSelected()) {
			Integer[] keys = selectedIndexMap.keySet().toArray(new Integer[0]);
			int oldRow = keys[0].intValue();
			clickCheckBoxAndCheckedChanged(oldRow);
			clickCheckBoxAndCheckedChanged(index);
		} else if ((getTableCheckedCount() == 0) && isMultiSelected()
				&& isDrag == false) {

		} else if (isDrag) {
			if (getTableCheck()[index] == 1)
				return;
			else
				clickCheckBoxAndCheckedChanged(index);
		}
	}
	
	public void clickTableRowWithCtrlDown(int index){
		clickCheckBoxAndCheckedChanged(index);
	}
	
	public void processKeyUpOrDown(boolean isDown, int rowIndex) {
		if (getTableCheckedCount() == 1 && !isMultiSelected()) {
			if ((rowIndex == 0 && !isDown)
					|| (rowIndex == getTableCheck().length - 1 && isDown))
				return;

			clickCheckBoxAndCheckedChanged(rowIndex);
			if (isDown)
				clickCheckBoxAndCheckedChanged(rowIndex + 1);
			else
				clickCheckBoxAndCheckedChanged(rowIndex - 1);
		}

	}


	public void clickCheckBoxAndCheckedChanged(int index) {
		clickCheckBox(index);
		checkedChanged(index);
		table.revalidate();
		table.repaint();
	}
	
	public boolean isChecked(int index) {
		if (index > getTableCheck().length)
			return false;
		return getTableCheck()[index] == 1 ? true : false;
	}
	
    public Set<Integer> getSelectedIndexSet() {
        switch (checkStata) {
        case SELECT_PART:
            return selectedIndexMap.keySet();
        case SELECT_ALL:
            HashSet<Integer> allItem = new HashSet<Integer>();
            for (int i = 0; i < getTableCheck().length; i++) {
                allItem.add(i);
            }
            return allItem;
        case SELECT_NONE:
            return new HashSet<Integer>();
        }
        return new HashSet<Integer>();
    }


	public void setTableCheckedCount(int tableCheckedCount) {
		this.tableCheckedCount = tableCheckedCount;
		if (tableCheckedCount == 0)
			this.isMultiSelected = false;
		if (tableCheckedCount > 1)
			this.isMultiSelected = true;
	}


	public int getTableCheckedCount() {
		return tableCheckedCount;
	}

	public void setTableCheck(byte[] tableCheck) {
		this.tableCheck = tableCheck;
	}


	public byte[] getTableCheck() {
		return tableCheck;
	}

	public void setMultiSelected(boolean isMultiSelected) {
		this.isMultiSelected = isMultiSelected;
	}


	public boolean isMultiSelected() {
		return isMultiSelected;
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
	
	private void clear() {


		checkStata = -1;

		selectedIndexMap = new HashMap<Integer, Object>();

		setTableCheckedCount(0);

		fireChanged = true;

		headerCheckBoxColumn = 1;

		checkBoxColumn = 1;

	}

	private void setColumnAttributes() {
		TableColumn tc = table.getColumnModel().getColumn(checkBoxColumn);
		tc.setPreferredWidth(preferredWidth);
		tc.setMaxWidth(maxWidth);
		tc.setMinWidth(minWidth);
		tc.setCellRenderer(getCheckBoxsCellRenderer());
		tc.setCellEditor(getCheckBoxsCellEditor());
	}

	private void addTableMouseListener() {
		nowMouseListen = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getButton() != java.awt.event.MouseEvent.BUTTON1)
					return;
				if(e.isShiftDown())
					return;
				if (e.getClickCount() == 1) {
					int selectRow = MMGPRefTableCheckboxManager.this.table.getSelectionModel().getAnchorSelectionIndex();
					if (selectRow > -1) {
						int col = MMGPRefTableCheckboxManager.this.table
								.columnAtPoint(e.getPoint());
						if (col != -1 && col == checkBoxColumn) {
							clickCheckBoxAndCheckedChanged(selectRow);
							if (getTableCheckedCount() > 0)
								isMultiSelected = true;
							table.revalidate();
							table.repaint();
						}
					}
				}
			}
		};
		table.addMouseListener(nowMouseListen);
	}

	private void saveCheckBoxListStata(List<UICheckBox> checkBoxs) {
		setTableCheck(new byte[checkBoxs.size()]);
		for (int i = 0; i < checkBoxs.size(); i++) {
			getTableCheck()[i] = (byte) (checkBoxs.get(i).isSelected() ? 1 : 0);
		}
	}



	private void removeSet() {
		if (table != null) {
			table.removeMouseListener(nowMouseListen);
			TableColumn tc = table.getColumnModel().getColumn(checkBoxColumn);
			tc.setCellRenderer(null);
			tc.setCellEditor(null);
		}
	}

	private void initCheckBox() {
		for (int i = 0; i < templetBox.length; i++) {
			templetBox[i].setVisible(true);
		}
		editorCheckBox.addActionListener(actionChange);
		editorCheckBox.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (table.getEditingRow() > 0
						&& table.getEditingRow() < getTableCheck().length) {
					editorCheckBox.setSelected(getTableCheck()[table.getEditingRow()] == 0 ? false
									: true);
				}
			}
		});
	}

	private void addCheckBoxs(int size) {
		setTableCheck(new byte[size]);
		if (size == 0) {
			clear();
			getHeaderCheckBox().setSelected(false);
			getHeaderCheckBox().setEnabled(false);
		}else{
			getHeaderCheckBox().setEnabled(true);
		}
	}

	private void setHeaderShowCheckbox(int col) {
		headerCheckBoxColumn = col;
		final JTableHeader jh = table.getTableHeader();
		TableColumnModel headerColumnMode = jh.getColumnModel();

		headerColumnMode.getColumn(headerCheckBoxColumn).setHeaderRenderer(
				new TableHeaderRenderer(getHeaderCheckBox()));
		
        MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int column;
				if ((column = jh.columnAtPoint(e.getPoint())) != -1) {
					TableColumn tc = table.getColumnModel().getColumn(
							checkBoxColumn);
					tc.getCellEditor().cancelCellEditing();
					if (column == headerCheckBoxColumn) {
						selectLogicCheckBoxChecked(true);
						clickHeadCheckBox();
						if (getTableCheckedCount() > 0)
							isMultiSelected = true;
						jh.repaint();
					}
				}
			}
            
			public void mouseMoved(MouseEvent e) {
				int columnIndex = jh.getTable().columnAtPoint(e.getPoint());
				if (columnIndex == headerCheckBoxColumn) {
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

	private void selectLogicCheckBoxChecked(boolean seleteSaveTable) {
		checkStata++;
		if (checkStata == 3) {
			checkStata = SELECT_PART;
		}

		switch (checkStata) {
		case SELECT_PART:
			getHeaderCheckBox().setSelected(true);
			getHeaderCheckBox().setEnabled(false);
			getHeaderCheckBox().setDisabledSelectedIcon(ThemeResourceCenter.getInstance().getImage("themeres/control/box/checkbox_mix.png"));
			if (seleteSaveTable) {
				seletedCheckBoxCheck();
			}
			break;
		case SELECT_ALL:
			getHeaderCheckBox().setSelected(true);
			getHeaderCheckBox().setEnabled(true);
			if (seleteSaveTable) {
				selectAllTable(true);
			}
			break;
		case SELECT_NONE:
			getHeaderCheckBox().setSelected(false);
			getHeaderCheckBox().setEnabled(true);
			isMultiSelected = false;
			if (seleteSaveTable) {
				selectAllTable(false);
			}
			break;
		}
		table.getTableHeader().repaint();
	}

	private void checkedChanged(int index) {
		if (fireChanged && !table.isEditing()) {
			changeStata(index);
		}
	}

	private void checkedChanged2(int index) {
		if (table.isEditing()) {
			changeStata(index);
		}
	}

	private void changeStata(int index) {

		if (checkStata == SELECT_NONE) {
			selectedIndexMap = new HashMap<Integer, Object>();
		}

		if (checkStata == SELECT_ALL) {
			selectedIndexMap = new HashMap<Integer, Object>();
			for (int i = 0; i < getTableCheck().length; i++) {
				selectedIndexMap.put(i, null);
			}
		}

		if (getTableCheck()[index] == 1) {
			setTableCheckedCount(getTableCheckedCount() + 1);
			selectedIndexMap.put(index, null);
		} else {
			setTableCheckedCount(getTableCheckedCount() - 1);
			selectedIndexMap.remove(index);
		}

		updateStata();
	}

	private void updateStata() {
		int changedStata = 0;
		if (getTableCheckedCount() != 0) {
			if (getTableCheckedCount() == getTableCheck().length) {
				changedStata = SELECT_ALL;
			} else {
				changedStata = SELECT_PART;
			}
		} else {
			changedStata = SELECT_NONE;
		}

		if (changedStata != checkStata) {
			changedStata--;
			checkStata = changedStata;
			selectLogicCheckBoxChecked(false);
			table.getTableHeader().repaint();
		}
	}

	private void selectAllTable(boolean check) {
		fireChanged = false;
		byte nowCheckStata = (byte) (check ? 1 : 0);
		for (int i = 0; i < getTableCheck().length; i++) {
			getTableCheck()[i] = nowCheckStata;
		}
		if (check) {
			setTableCheckedCount(getTableCheck().length);
		} else {
			setTableCheckedCount(0);
		}
		table.revalidate();
		table.repaint();
		fireChanged = true;
	}

	public UICheckBox getHeaderCheckBox() {
		if (headerCheckBox == null) {
			headerCheckBox = new UICheckBox();
			headerCheckBox.setBorderPainted(true);
		}
		return headerCheckBox;
	}
	

	private void clickCheckBox(int i) {
		if (getTableCheck() != null) {
			getTableCheck()[i] = (byte) (getTableCheck()[i] == 0 ? 1 : 0);

			eventSource.fireEvent(new NCEventObject(
					RefEventConstant.DATAROW_CHECKED_CHANGED,
					RefEventConstant.SELECTED_ROWS, new int[] { i },
					RefEventConstant.IS_ADD_ROWS, getTableCheck()[i] == 0 ? false
							: true));
		}
	}
	

	private void clickHeadCheckBox() {
		Boolean isAddRows = getHeaderCheckBox().isSelected();
		int len = table.getRowCount();
		int[] selectedRows = new int[len];
		if (checkStata != SELECT_PART) {
			for (int i = 0; i < len; i++) {
				selectedRows[i] = i;
			}
		} else {
			for (int i = 0; i < getTableCheck().length; i++) {
				if (getTableCheck()[i] == 1) {
					selectedRows[i] = i;
				} else {
					selectedRows[i] = -1;
				}
			}
		}
		if (checkStata == SELECT_NONE)
			this.isMultiSelected = false;
		else
			this.isMultiSelected = true;
		eventSource.fireEvent(new NCEventObject(
				RefEventConstant.DATAROW_CHECKED_CHANGED,
				RefEventConstant.SELECTED_ROWS, selectedRows,
				RefEventConstant.IS_ADD_ROWS, isAddRows));
	}
	
	private void seletedCheckBoxCheck() {
		fireChanged = false;
		setTableCheckedCount(0);

		Set<Integer> keys = selectedIndexMap.keySet();
		int checkIndex = 0;
		for (Iterator<Integer> keyItr = keys.iterator(); keyItr.hasNext();) {
			checkIndex = keyItr.next();
			if (getTableCheck().length > checkIndex) {
				getTableCheck()[checkIndex] = 1;
				setTableCheckedCount(getTableCheckedCount() + 1);
			}
		}

		if (getTableCheckedCount() == 0) {
			selectLogicCheckBoxChecked(true);
		} else if (getTableCheck().length > 0
				&& getTableCheckedCount() == getTableCheck().length) {
			selectLogicCheckBoxChecked(false);
		}
		table.revalidate();
		table.repaint();
		fireChanged = true;
	}




	private TableCellEditor getCheckBoxsCellEditor() {
		TableCellEditor tce = new CheckBoxsCellEditor();
		return tce;
	}

	private TableCellRenderer getCheckBoxsCellRenderer() {
		CheckBoxsRenderer checkBoxRenderer = new CheckBoxsRenderer();
		return checkBoxRenderer;
	}



	class TableHeaderRenderer implements TableCellRenderer {
		private UICheckBox renderCheckBoxs;

		public TableHeaderRenderer(UICheckBox tableCheck) {
			renderCheckBoxs = tableCheck;
			renderCheckBoxs.setHorizontalAlignment(SwingConstants.CENTER);
			renderCheckBoxs.setBackground(new Color(242, 242, 242));
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return renderCheckBoxs;
		}
	}



	class CheckBoxsCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = -1875319868682535006L;

		public CheckBoxsCellEditor() {
			super(new JTextField());
		}

		public Component getTableCellEditorComponent(UITable table,
				Object value, boolean isSelected, int row, int column) {
			return editorCheckBox;
		}
	}

	class CheckBoxsRenderer implements TableCellRenderer {

		private Color unselectedBackground = new Color(242, 242, 242);
		private Color selectedBackground = new Color(229, 243, 252);
		
		public CheckBoxsRenderer() {
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (row < getTableCheck().length) {
				if(getTableCheck()[row]==1){
					((Component) templetBox[getTableCheck()[row]]).setBackground(selectedBackground);
				}else{
					((Component) templetBox[getTableCheck()[row]]).setBackground(unselectedBackground);
				}
				return (Component) templetBox[getTableCheck()[row]];
			}
			return null;
		}
	}

	private class IndexJCheckBox extends UICheckBox {
		public IndexJCheckBox(String text, boolean selected) {
			super(text, selected);
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		public IndexJCheckBox() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		/**
         * 
         */
		private static final long serialVersionUID = 1L;

		private int index = 0;

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
