package nc.ui.mmgp.uif2.actions.batch;

import java.util.ArrayList;
import java.util.List;

import nc.ui.format.NCFormater;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.print.version55.directprint.PrintDirectSeperator;
import nc.ui.pub.print.version55.directprint.PrintDirectTable;
import nc.ui.pub.print.version55.print.template.cell.style.Style;
import nc.ui.uif2.actions.AbstractDirectPrintAction;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;

import org.apache.commons.lang.StringUtils;

public abstract class MMGPAbstractDirectPrintAction extends
		AbstractDirectPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void addTableSection(BillTable[] tables) throws Exception {
		if (tables == null || tables.length == 0)
			return;

		PrintDirectTable ptable = null;
		for (BillTable table : tables) {
			ptable = convertJTable2PrintTable(table);
			if (ptable != null) {
				getPrint().addSeperator(new PrintDirectSeperator());
				getPrint().addTableSection(ptable);
			}
		}
	}

	protected PrintDirectTable convertJTable2PrintTable(BillTable jtable)
			throws Exception {
		BillModel billModel = (BillModel) jtable.getModel();
		int rowCount = billModel.getRowCount();
		if (rowCount == 0) {
			return null;
		}

		PrintDirectTable pTable = new PrintDirectTable();
		BillItem[] items = billModel.getBodyItems();
		BillItem[] showItems = getShowItems(items);

		// 合并表头 CHENYL
		pTable.makeMergerHeadCells(jtable);

		String[] columnNames = new String[showItems.length];
		float[] columnWidth = new float[showItems.length];
		int[] aligns = new int[showItems.length];

		// 有合计行时
		if (billModel.isNeedCalculate()) {
			rowCount = rowCount + 2;
		}

		Object[][] data = new Object[rowCount][showItems.length];
		Object value = null;
		for (int i = 0; i < rowCount; i++) {
			int currColumnIndex = 0;
			for (int j = 0; j < items.length; j++) {
				if (!items[j].isShow())
					continue;
				else {
					if (!billModel.isNeedCalculate() || i < rowCount - 2) {
						value = billModel.getValueAt(i, j);
					} else if (billModel.isNeedCalculate() && i == rowCount - 2) {
						// 合计行情况下的倒数第二行
						if (currColumnIndex == 0) {
							value = nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("common", "UC000-0001146");
						} else {
							value = StringUtils.EMPTY;
						}
					} else if (billModel.isNeedCalculate() && i == rowCount - 1
							&& j != 0) {
						// 合计行情况下的倒数第一行
						value = billModel.getTotalTableModel().getValueAt(0, j);
					}

					// wangweiu update 解决锁定时的问题
					columnNames[currColumnIndex] = showItems[currColumnIndex]
							.getName();
					// jtable
					// .getColumnName(currColumnIndex);
					columnWidth[currColumnIndex] = showItems[currColumnIndex]
							.getWidth();
					// columnWidth[currColumnIndex] = jtable.getColumnModel()
					// .getColumn(currColumnIndex).getWidth();
					if (value == null) {
						data[i][currColumnIndex++] = null;
						continue;
					}

					if (value instanceof UFBoolean) {
						if (((UFBoolean) value).booleanValue())
							value = NCLangRes.getInstance().getStrByID("uif2",
									"AbstractDirectPrintAction-000000")/* 是 */;
						else
							value = NCLangRes.getInstance().getStrByID("uif2",
									"AbstractDirectPrintAction-000001")/* 否 */;
					} else if (value instanceof Boolean) {
						if (((Boolean) value).booleanValue())
							value = NCLangRes.getInstance().getStrByID("uif2",
									"AbstractDirectPrintAction-000000")/* 是 */;
						else
							value = NCLangRes.getInstance().getStrByID("uif2",
									"AbstractDirectPrintAction-000001")/* 否 */;
					}

					// 日期时间类型转换
					if (value instanceof UFDate) {
						value = NCFormater.formatDate(value).getValue();
					}
					if (value instanceof UFDateTime || value instanceof UFTime) {
						value = NCFormater.formatDateTime(value).getValue();
					}

					if (showItems[currColumnIndex].getDataType() == IBillItem.DECIMAL
							|| showItems[currColumnIndex].getDataType() == IBillItem.INTEGER
							|| showItems[currColumnIndex].getDataType() == IBillItem.MONEY
							|| showItems[currColumnIndex].getDataType() == IBillItem.FRACTION) {
						aligns[currColumnIndex] = Style.RIGHT;
						value = NCFormater.formatNumber(value).getValue();
					} else {
						aligns[currColumnIndex] = Style.LEFT;
					}

					data[i][currColumnIndex++] = value;
				}
			}
		}

		pTable.setColumnNames(columnNames, false);
		pTable.setColumnsWidth(columnWidth);
		pTable.setAlignment(aligns);
		pTable.setData(data);

		return pTable;
	}

	protected BillItem[] getShowItems(BillItem[] items) {
		List<BillItem> itemList = new ArrayList<BillItem>();
		for (BillItem item : items) {
			if (item.isShow())
				itemList.add(item);
		}
		return itemList.toArray(new BillItem[0]);
	}

}
