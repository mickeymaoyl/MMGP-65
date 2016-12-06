package nc.ui.mmgp.uif2.ref.gridtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.vo.bd.ref.RefcolumnVO;

public class MMGPRefTableColumnModel extends DefaultTableColumnModel {

	public MMGPRefTableColumnModel(AbstractRefModel refModel) {
		super();
		this.refModel = refModel;
	}

	private AbstractRefModel refModel = null;

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-2-20</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception
	 * @since NC5.0
	 */

	// 要顺序显示的栏目
	private String[] columnNames = null;

	private HashMap hm_names = new HashMap();

	// 动态列
	private String[] dynamicColumnNames = null;

	// model中全部的列数据
	HashMap hm_AllColumns = null;

	/**
	 * @return 返回 columnVOs。
	 */
	public void adjustColumnShowSequence() {
		// 按照 columnNames数组的顺序调整列的顺序。
		if (columnNames != null) {
			// 第一次调用时，给hm_allColumns赋值，为全部的列数据
			if (hm_AllColumns == null) {
				init_Hm_allColumns();
			}
			// 去掉不显示的列,添加model中没有的列
			fitToColumnName(columnNames);
			// 调整列顺序
			for (int i = 0; i < columnNames.length; i++) {
				// 如果model中没有，不做调整。容错
				if (!hm_AllColumns.containsKey(columnNames[i])) {
					continue;
				}
				int newIndex = ((Integer) hm_names.get(columnNames[i]))
						.intValue();

				if (newIndex > getColumnCount() - 1) {
					continue;
				}
				int oldIndex = getColumnIndex(columnNames[i]);

				if (newIndex < getColumnCount() - 1 && oldIndex != newIndex) {
					moveColumn(oldIndex, newIndex);

				}

			}

			setCellRenderAlignment();
		}
	}

	// 调整Cell对齐方式, RefColumnDispConvertVO 方式
	// private void setCellRenderAlignment() {
	// if (refModel == null) {
	// return;
	// }
	// Object[][] columnDisps = refModel.getColumnDispConverter();
	// if (columnDisps == null) {
	// return;
	// }
	// for (int i = 0; i < columnDisps.length; i++) {
	// RefColumnDispConvertVO convertVO = (RefColumnDispConvertVO)
	// columnDisps[i][0];
	// if (convertVO == null) {
	// continue;
	// }
	// int[] aligns = convertVO.getAlignments();
	// if (aligns != null) {
	// for (int j = 0; j < aligns.length; j++) {
	// int fieldIndex = convertVO.getFieldIndex(convertVO
	// .getDispColNames()[j]);
	// TableColumn column = (TableColumn) hm_AllColumns
	// .get(refModel.getFieldName()[fieldIndex]);
	// setCellRenderAlign(aligns[j], column);
	// }
	// }
	// }
	// }
	private void setCellRenderAlignment() {
		if (refModel == null || refModel.getAlignMap() == null) {
			return;
		}

		Iterator<String> iterator = refModel.getAlignMap().keySet().iterator();
		String field = null;
		int alignment = SwingConstants.RIGHT;
		while (iterator.hasNext()) {
			field = iterator.next();
			alignment = refModel.getAlignMap().get(field).intValue();
			TableColumn column = (TableColumn) hm_AllColumns.get(refModel
					.getFieldName()[refModel.getFieldIndex(field)]);
			setCellRenderAlign(alignment, column);
		}

	}

	private void setCellRenderAlign(int align, TableColumn column) {
		javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(align);
		column.setCellRenderer(cellRenderer);
	}

	/**
	 * 删除、添加列，以匹配columnNames
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-2-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception
	 * @since NC5.0
	 */
	private void fitToColumnName(String[] columnNames) {

		// 删除所有的列
		while (getColumnCount() > 0) {
			removeColumn(getColumn(0));
		}

		// 添加列columnNames中存在而列Model中没有的列
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = (TableColumn) hm_AllColumns
					.get(columnNames[i]);
			if (column != null) {
				addColumn(column);
			}
		}

	}

	/**
	 * @return 返回 columnNames。
	 */
	protected String[] getColumnNames() {

		return columnNames;
	}

	/**
	 * @param columnNames
	 *            要设置的 columnNames。
	 */
	protected void setColumnVOs(RefcolumnVO[] vos) {

		if (vos != null) {
			columnNames = new String[vos.length];
			ArrayList al = new ArrayList();
			for (int i = 0; i < vos.length; i++) {
				if (vos[i].getIscolumnshow().booleanValue()) {
					al.add(vos[i].getFieldshowname());
				}
			}
			if (al.size() > 0) {
				columnNames = new String[al.size()];
				al.toArray(columnNames);
			}
		}
		// 处理动态列
		if (getDynamicColumnNames() != null) {
			columnNames = RefPubUtil.ArrayCombine(columnNames,
					getDynamicColumnNames());

		}
		init_Hm_names();
	}

	private void init_Hm_names() {
		hm_names.clear();

		if (columnNames != null) {
			for (int i = 0; i < columnNames.length; i++) {
				hm_names.put(columnNames[i], Integer.valueOf(i));
			}
		}

	}

	private void init_Hm_allColumns() {
		hm_AllColumns = new HashMap();
		java.util.Enumeration enumeration = getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn column = (TableColumn) enumeration.nextElement();
			String name = column.getHeaderValue().toString();
			hm_AllColumns.put(name, column);

		}
	}

	/**
	 * @return 返回 dynamicColumnNames。
	 */
	private String[] getDynamicColumnNames() {
		return dynamicColumnNames;
	}

	/**
	 * @param dynamicColumnNames
	 *            要设置的 dynamicColumnNames。
	 */
	public void setDynamicColumnNames(String[] dynamicColumnNames) {
		this.dynamicColumnNames = dynamicColumnNames;
	}

}
