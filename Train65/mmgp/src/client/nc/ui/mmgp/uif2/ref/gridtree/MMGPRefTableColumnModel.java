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
	 * <code>serialVersionUID</code> ��ע��
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-2-20</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception
	 * @since NC5.0
	 */

	// Ҫ˳����ʾ����Ŀ
	private String[] columnNames = null;

	private HashMap hm_names = new HashMap();

	// ��̬��
	private String[] dynamicColumnNames = null;

	// model��ȫ����������
	HashMap hm_AllColumns = null;

	/**
	 * @return ���� columnVOs��
	 */
	public void adjustColumnShowSequence() {
		// ���� columnNames�����˳������е�˳��
		if (columnNames != null) {
			// ��һ�ε���ʱ����hm_allColumns��ֵ��Ϊȫ����������
			if (hm_AllColumns == null) {
				init_Hm_allColumns();
			}
			// ȥ������ʾ����,���model��û�е���
			fitToColumnName(columnNames);
			// ������˳��
			for (int i = 0; i < columnNames.length; i++) {
				// ���model��û�У������������ݴ�
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

	// ����Cell���뷽ʽ, RefColumnDispConvertVO ��ʽ
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
	 * ɾ��������У���ƥ��columnNames
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-2-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception
	 * @since NC5.0
	 */
	private void fitToColumnName(String[] columnNames) {

		// ɾ�����е���
		while (getColumnCount() > 0) {
			removeColumn(getColumn(0));
		}

		// �����columnNames�д��ڶ���Model��û�е���
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = (TableColumn) hm_AllColumns
					.get(columnNames[i]);
			if (column != null) {
				addColumn(column);
			}
		}

	}

	/**
	 * @return ���� columnNames��
	 */
	protected String[] getColumnNames() {

		return columnNames;
	}

	/**
	 * @param columnNames
	 *            Ҫ���õ� columnNames��
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
		// ����̬��
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
	 * @return ���� dynamicColumnNames��
	 */
	private String[] getDynamicColumnNames() {
		return dynamicColumnNames;
	}

	/**
	 * @param dynamicColumnNames
	 *            Ҫ���õ� dynamicColumnNames��
	 */
	public void setDynamicColumnNames(String[] dynamicColumnNames) {
		this.dynamicColumnNames = dynamicColumnNames;
	}

}
