package nc.ui.mmgp.pub.bill.treetable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import nc.bs.logging.Logger;
import nc.md.model.IBean;
import nc.ui.format.NCFormater;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIMultiLangCombox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.table.CellFont;
import nc.ui.pub.beans.table.ColoredCell;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemNumberFormat;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillModel.TotalTableModel;
import nc.ui.pub.bill.IBillItem;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.mmgp.util.MMMetaUtils;
import nc.vo.pub.format.FormatResult;
import nc.vo.pub.format.exception.FormatException;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * 创建日期:(01-2-28 14:49:01)
 */
@SuppressWarnings("serial")
public class MMGPTreeTableCellRenderer extends UILabel implements
		TableCellRenderer, Serializable {
	protected static Border noFocusBorder;

	// // We need a place to store the color the JLabel should be returned
	// // to after its foreground and background colors have been set
	// // to the selection background color.
	// // These ivars will be made protected when their names are finalized.
	// private Color unselectedForeground;
	//
	// private Color unselectedBackground;

	// private int m_iDataType = 0; // 数据类型

	// private int m_iDecimalDigits = 0; // 小数位数

	// private BillRendererVO paraVO = new BillRendererVO();

	// private BillItemNumberFormat numberFormat = null;

	// // {(row,col),Color}
	// private HashMap<String, Color> hashBackGround = new HashMap<String,
	// Color>();
	//
	// private HashMap<String, Color> hashForeGround = new HashMap<String,
	// Color>();

	private BillItem item = null;

	private boolean showWarning = false;

	String code = null;
	String name = null;
	String parentName = null;
	String parentCode = null;

	// private static BillTableCellRenderer cellrender = new
	// BillTableCellRenderer();

	/**
	 * Creates a default table cell renderer.
	 */
	public MMGPTreeTableCellRenderer(IBean bean) {
		super();
		noFocusBorder = new EmptyBorder(1, 2, 1, 2);
		setOpaque(true);
		setBorder(noFocusBorder);
		String pid = MMMetaUtils.getParentPKFieldName(bean);
		code = MMMetaUtils.getCodeFieldName(bean);
		name = MMMetaUtils.getNameFieldName(bean);
		parentCode = pid + "." + code;
		parentName = pid + "." + name;
	}

	// /**
	// * BillTableCellRenderer 构造子注解.
	// */
	// public BillTableCellRenderer(BillItem item) {
	// this();
	// this. item = item;
	// numberFormat = item.getNumberFormat();
	// }

	// /**
	// * BillTableCellRenderer 构造子注解.
	// */
	// public BillTableCellRenderer(BillItem item, BillRendererVO
	// newParameterVO) {
	// this(item.getDataType(), newParameterVO);
	// }
	//
	// /**
	// * BillTableCellRenderer 构造子注解.
	// */
	// public BillTableCellRenderer(int dataType, BillRendererVO newParameterVO)
	// {
	// this();
	// m_iDataType = dataType;
	// paraVO = newParameterVO;
	// }

	// /**
	// *
	// * 创建日期:(2003-6-19 16:29:41)
	// *
	// * @return java.awt.Color
	// */
	// public Color getBackGround(int row, int col) {
	// return hashBackGround.get(row + "," + col);
	// }

	// 获得数据类型
	public int getDataType() {
		if (item != null)
			return item.getDataType();
		return -1;
	}

	// // 获得小数位数
	// public int getDecimalDigits() {
	// return m_iDecimalDigits;
	// }

	// /**
	// *
	// * 创建日期:(2003-6-19 16:29:41)
	// *
	// * @return java.awt.Color
	// */
	// public Color getForeGround(int row, int col) {
	// return hashForeGround.get(row + "," + col);
	// }

	// // 负数是否显示符号
	// private boolean isNegativeSign() {
	//
	// if(getNumberFormat() != null)
	// return getNumberFormat().isNegativeSign();
	//
	// return paraVO.isNegativeSign();
	// }
	//
	// // 负数是否显示红字
	// private boolean isShowRed() {
	// if(getNumberFormat() != null)
	// return getNumberFormat().isShowRed();
	//
	// return paraVO.isShowRed();
	// }
	//
	// // 是否显示千分位
	// private boolean isShowThMark() {
	// if(getNumberFormat() != null)
	// return getNumberFormat().isShowThMark();
	// return paraVO.isShowThMark();
	// }

	// 是否将零显示为空串
	private boolean isShowZeroLikeNull() {
		if (getNumberFormat() != null)
			return getNumberFormat().isShowZeroLikeNull();
		// return paraVO.isShowZeroLikeNull();
		return false;
	}

	// 是否显示百分比
	private boolean isShowPercent() {
		// return paraVO.isShowPercent();
		return false;
	}

	// implements javax.swing.table.TableCellRenderer
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Color color = null;
		TableModel model = table.getModel();
		boolean ismaintable = false;

		item = null;
		boolean showWarning = false;
		boolean showMustInput = false;
		if (model instanceof BillModel) {
			ismaintable = true;

			int modelcolumn = table.convertColumnIndexToModel(column);
			item = ((BillModel) model).getBodyItems()[modelcolumn];

			showWarning = ((BillModel) model).isCellShowWarning(row,
					modelcolumn);

			// showMustInput = value == null && item.isNull();
		}

		if (model instanceof TotalTableModel) {
			int modelcolumn = table.convertColumnIndexToModel(column);
			item = ((TotalTableModel) model).getMainModel().getBodyItems()[modelcolumn];

		}
		if (model instanceof ColoredCell) {

			int modelcolumn = table.convertColumnIndexToModel(column);

			color = ((ColoredCell) model).getBackground(row, modelcolumn);
			if (isSelected) {
				super.setBackground(table.getSelectionBackground());
			} else {
				if (color != null)
					super.setBackground(color);
				else
					// super.setBackground((unselectedBackground != null) ?
					// unselectedBackground : table.getBackground());
					super.setBackground(table.getBackground());
			}

			color = ((ColoredCell) model).getForeground(row, modelcolumn);
			if (color != null)
				super.setForeground(color);
			else
				// super.setForeground((unselectedForeground != null) ?
				// unselectedForeground : table.getForeground());
				super.setForeground(table.getForeground());
		} else {

			if (isSelected) {
				super.setBackground(table.getSelectionBackground());
				super.setForeground(table.getSelectionForeground());
			} else {
				super.setBackground(table.getBackground());
				super.setForeground(table.getForeground());

			}
		}

		Font font = table.getFont();

		if (model instanceof CellFont) {
			font = ((CellFont) model).getFont(row, column);
			if (font == null)
				font = table.getFont();
		}

		setFont(font);
		// 不可编辑颜色
		Color diseditable = new Color(0XF6F7FA);
		// 编辑状态下 选择不可编辑颜色
		Color selectedDiseditable = new Color(0XEAEABB);

		Color rowAssEditable = new Color(0XF0F8FD);
		Color rowAssDiseditable = new Color(0XE3EAEE);

		if (model instanceof BillModel) {

			BillModel mdl = (BillModel) model;

			if (mdl.isEnabled()) {

				if (this.item.isNull()) {
					if (value instanceof DefaultConstEnum) {
						showMustInput = ((DefaultConstEnum) value).getValue() == null;
					} else {
						showMustInput = value == null;
					}

				}

				// 单元格不可编辑
				if (!table.isCellEditable(row, column)) {

					super.setBackground(diseditable);
				}

				if (isSelected) {
					if (!table.isCellEditable(row, column)) {
						super.setBackground(selectedDiseditable);
					}
				}

				// 有焦点
				if (hasFocus) {
					// 单元格可以编辑
					if (table.isCellEditable(row, column)) {
						super.setBackground(table.getBackground());
					} else {
						super.setBackground(selectedDiseditable);
					}

				} else {
					// 行辅助
					int leadRowIndex = table.getSelectionModel()
							.getLeadSelectionIndex();
					if (row == leadRowIndex) {

						if (table.isCellEditable(row, column)) {
							super.setBackground(rowAssEditable);
						} else {
							super.setBackground(rowAssDiseditable);
						}
					}
				}
				// setHyperlinkLabel(false);

			}

			// if (!mdl.isEnabled()) {

			if (item != null) {

				if (item.isEnabled()) {

					setHyperlinkLabel(false);
				} else {

					if (item.isCard()) {
						setHyperlinkLabel(item.isHyperlink());
					} else {
						setHyperlinkLabel(item.isListHyperlink());
					}
				}

			}
			// }

		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));

		} else {
			setBorder(noFocusBorder);

		}

		if (table.isRowSelected(row)) {

			if (table.getEditingRow() == row) {
				super.setBackground(table.getSelectionBackground());
			} else {
				// super.setBackground(new Color(0XF0F8FD));
				// super.setBackground(new Color(0XFFFFCC));
			}
		}

		// when datatype is integer or decimal,the foreground can be modified;
		setValue(value);

		/**
		 * add by lisyd 2015 1.14 begin
		 */
		if (model instanceof MMGPBillModelTreeTableAdapter) {

			MMGPBillModelTreeTableAdapter billmodel = (MMGPBillModelTreeTableAdapter) model;
			MMGPBillTreeTableModelNode selectNode = billmodel.getTreeNode(row);
			MMGPBillTreeTableModelNode parent = (MMGPBillTreeTableModelNode) selectNode
					.getParent();

			// 根节点界面上不显示
			if (parent != null && !parent.isRoot()) {
				// 获取父节点对应表体的行数字
				int parentRow = billmodel.getRow(parent);

				if (item.getKey().equals(parentCode)) {

					int i = billmodel.getBodyColByKey(code);

					Object code = parent.getValueAt(i);

					if (table.isEditing()) {

						int editModelcolumn = table
								.convertColumnIndexToModel(table
										.getEditingColumn());
						if (table.getEditingRow() == parentRow
								&& editModelcolumn == i) {
							UIRefPane panel = (UIRefPane) table
									.getEditorComponent();
							code = panel.getText();
						}
					}

					setValue(code);

				}

				if (item.getKey().equals(parentName)) {

					int i = billmodel.getBodyColByKey(name);

					Object name = parent.getValueAt(i);

					if (table.isEditing()) {

						int editModelcolumn = table
								.convertColumnIndexToModel(table
										.getEditingColumn());
						if (table.getEditingRow() == parentRow
								&& editModelcolumn == i) {
							UIMultiLangCombox panle = (UIMultiLangCombox) table
									.getEditorComponent();
							UITextField textField = ((UITextField) panle
									.getEditor().getEditorComponent());
							name = textField.getText();
						}
					}

					setValue(name);

				}

			}

		}
		/**
		 * add by lisyd 2015 1.14 end
		 */

		if (showMustInput) {
			super.setForeground(Color.RED);
			setText("*");
		}

		// reset foreground
		if (color != null)
			super.setForeground(color);

		if (model instanceof TotalTableModel) {
			setHyperlinkLabel(false);
		}

		setShowWarning(showWarning);

		setTooltipsIfNeed(table, column);

		return this;
	}

	/**
	 * 以label的最小宽度为准，计算是否显示tooltips。 不能设置为null而只能设置为“”，否则将被UILabel还原成label的text。
	 */
	private void setTooltipsIfNeed(JTable table, int column) {
		FontMetrics fm = getFontMetrics(getFont());
		if (getText() == null) {
			setToolTipText("");
			return;
		}
		int width = fm.stringWidth(getText());

		if (getMinimumSize() != null)
			width = getMinimumSize().width;
		TableColumn tc = table.getColumnModel().getColumn(column);
		int cwidth = tc.getWidth();
		if (cwidth < width)
			setToolTipText(getText());
		else
			setToolTipText("");
	}

	// public void resumeDefaultBackGround() {
	// hashBackGround.clear();
	// }
	//
	// public void resumeDefaultForeGround() {
	// hashForeGround.clear();
	// }
	//
	// /**
	// *
	// * 创建日期:(2003-6-19 16:29:41)
	// *
	// * @param newBackGround
	// * java.awt.Color
	// */
	// public void setBackGround(int row, int col, java.awt.Color color) {
	// if (color == null)
	// hashBackGround.remove(row + "," + col);
	// else
	// hashBackGround.put(row + "," + col, color);
	// }

	// /**
	// * Overrides <code>JComponent.setForeground</code> to specify the
	// * unselected-background color using the specified color.
	// */
	// public void setBackground(Color c) {
	// super.setBackground(c);
	// unselectedBackground = c;
	// }

	// // 设置数据类型
	// public void setDataType(int iDataType) {
	// m_iDataType = iDataType;
	// }

	// // 获得小数位数
	// public void setDecimalDigits(int iDecimalDigits) {
	// m_iDecimalDigits = iDecimalDigits;
	// }

	// /**
	// *
	// * 创建日期:(2003-6-19 16:29:41)
	// *
	// * @param newForeGround
	// * java.awt.Color
	// */
	// public void setForeGround(int row, int col, java.awt.Color color) {
	// if (color == null)
	// hashForeGround.remove(row + "," + col);
	// else
	// hashForeGround.put(row + "," + col, color);
	// }

	// /**
	// * Overrides <code>JComponent.setForeground</code> to specify the
	// * unselected-foreground color using the specified color.
	// */
	// public void setForeground(Color c) {
	// super.setForeground(c);
	// unselectedForeground = c;
	// }

	/**
	 * 加入千分位标志. 创建日期:(2002-01-29 14:51:16)
	 * 
	 * @param str
	 *            java.lang.String
	 * @return java.lang.String
	 */
	public static String setMark(String str) {
		if ((str == null) || (str.trim().length() < 3))
			return str;
		str = str.trim();
		String str0 = "";
		String str1 = str;
		if (str.startsWith("-")) {
			str0 = "-";
			str1 = str.substring(1);
		}
		int pointIndex = str1.indexOf(".");
		String str2 = "";
		String newStr = "";

		// 小数
		if (pointIndex != -1) {
			str2 = str1.substring(pointIndex);
			str1 = str1.substring(0, pointIndex);
		}
		int ii = (str1.length() - 1) % 3;
		for (int i = 0; i < str1.length(); i++) {
			newStr += str1.charAt(i);
			if (ii <= 0) {
				newStr += ",";
				ii += 2;
			} else
				ii--;
			ii = ii % 3;
		}
		if (newStr.length() > 0)
			newStr = newStr.substring(0, newStr.length() - 1) + str2;
		else
			newStr = str2;
		newStr = str0 + newStr;
		return newStr;
	}

	// // 设置负数是否显示符号
	// public void setNegativeSign(boolean newValue) {
	// paraVO.setNegativeSign(newValue);
	// }
	//
	// // 设置负数是否显示红字
	// public void setShowRed(boolean newValue) {
	// paraVO.setShowRed(newValue);
	// }
	//
	// // 设置是否显示千分位
	// public void setShowThMark(boolean newValue) {
	// paraVO.setShowThMark(newValue);
	// }
	//
	// 设置是否将零显示为空串
	public void setShowZeroLikeNull(boolean newValue) {
		if (getNumberFormat() != null)
			getNumberFormat().setShowZeroLikeNull(newValue);
		// paraVO.setShowZeroLikeNull(newValue);
	}

	@SuppressWarnings("unchecked")
	protected void setValue(Object value) {

		setHorizontalAlignment(SwingConstants.LEFT);

		if (value == null || value.equals("")) {
			setText("");
		} else {

			// 数据类型
			switch (getDataType()) {
			case IBillItem.INTEGER:
			case IBillItem.DECIMAL:
				if (getDataType() == IBillItem.INTEGER
						|| getDataType() == IBillItem.DECIMAL
						|| getDataType() == IBillItem.MONEY) {

					setHorizontalAlignment(SwingConstants.RIGHT);
					setForeground(java.awt.Color.black);

					// 辅助量小计合计特殊处理,其值为ArrayList
					if (value instanceof ArrayList) {
						StringBuffer valueBuf = new StringBuffer();
						ArrayList valueList = (ArrayList) value;
						int unitNum = valueList.size() / 2;
						for (int i = 0; i < unitNum; i++) {
							if (i > 0) {
								valueBuf.append("/ ");
							}
							valueBuf.append(valueList.get(i * 2));
							valueBuf.append(valueList.get(i * 2 + 1));
						}
						value = valueBuf.toString();
					} else {

						String v = value.toString();

						double dou = 0;

						if (value instanceof UFDouble)
							dou = ((UFDouble) value).doubleValue();
						else
							dou = Double.valueOf(v);

						if (dou == 0 && isShowZeroLikeNull()) {
							value = "";
						} else {
							FormatResult format = null;
							try {
								format = NCFormater.formatNumber(value);
								value = format.getValue();
							} catch (FormatException e) {
								Logger.error(e);
								value = "";
							}
							setForeground(format.getColor());

							// 负数
							if (dou < 0) {
								// if (isShowRed())
								// setForeground(java.awt.Color.red);
								// if (!isNegativeSign()) {
								// value = v.replaceFirst("-", "");
								// v = value.toString();
								// }
							} else if (isShowPercent()) {
								dou = dou * 100;
								value = dou + "%";
								v = value.toString();
							}
							// 显示千分位
							// if (isShowThMark()) {
							// value = setMark(v);
							// }
						}
					}
				}
				break;
			case IBillItem.MONEY:

				setHorizontalAlignment(SwingConstants.RIGHT);

				String v = value.toString();

				double dou = 0;

				if (value instanceof UFDouble)
					dou = ((UFDouble) value).doubleValue();
				else
					dou = Double.valueOf(v);

				if (dou == 0 && isShowZeroLikeNull()) {

					value = "";
				} else {
					FormatResult format = null;
					try {
						format = NCFormater.formatCurrency(value);
						value = format.getValue();
					} catch (FormatException e) {
						Logger.error(e);
						value = "";
					}
					setForeground(format.getColor());

					// 负数
					if (dou < 0) {
					} else if (isShowPercent()) {
						dou = dou * 100;
						value = dou + "%";
						v = value.toString();
					}
				}
				break;

			case IBillItem.DATE:
				FormatResult format = null;
				try {
					format = NCFormater.formatDate(value);
					value = format.getValue();
				} catch (FormatException e) {
					Logger.error(e);
					value = "";
				}

				break;

			case IBillItem.LITERALDATE:
				format = null;
				try {
					format = NCFormater.formatLiteralDateTime(value);
					value = format.getValue();
				} catch (FormatException e) {
					Logger.error(e);
					value = "";
				}

				break;

			case IBillItem.DATETIME:
				format = null;
				try {
					format = NCFormater.formatDateTime(value);
					value = format.getValue();
				} catch (FormatException e) {
					Logger.error(e);
					value = "";
				}
				break;

			case IBillItem.TIME:
				format = null;
				try {
					format = NCFormater.formatTime(value);
					value = format.getValue();
				} catch (FormatException e) {
					Logger.error(e);
					value = "";
				}
				break;

			case IBillItem.PASSWORDFIELD:
				value = "********";
				break;

			case IBillItem.FRACTION:

				value = dealWithFraction(value.toString());

				break;
			default:
				break;
			}
			value = dealWithTextAccountStr(value);
			setText(value.toString());
		}
	}

	// 处理4位空格分隔的字符串
	private Object dealWithTextAccountStr(Object value) {
		if (IBillItem.STRING == getDataType() && item != null) {
			if (item.getComponent() instanceof UIRefPane) {

				UITextField tf = ((UIRefPane) item.getComponent())
						.getUITextField();
				if (tf.getTextType() == UITextType.TextAccountStr) {

					String str = value.toString();
					char[] ch = str.toCharArray();
					StringBuffer st = new StringBuffer();
					for (int i = 0; i < ch.length; i++) {
						if (i % 4 == 3) {
							st.append(ch[i]);
							st.append(' ');
						} else {
							st.append(ch[i]);
						}
					}
					value = st.toString();

				}

			}

		}
		return value;
	}

	private String dealWithFraction(String fraction) {
		String value = fraction;
		String token = "/";
		int tokenIndex = value.indexOf(token);
		if (tokenIndex == -1) {

			return value;
		}

		String[] parts = value.split(token);
		if (parts == null || parts.length != 2) {
			return value;
		}
		UFDouble denominator = new UFDouble(parts[1]);
		// if (denominator.intValue() == 1) {
		// value = value.substring(0, tokenIndex);
		// setHorizontalAlignment(SwingConstants.RIGHT);
		// } else {
		setHorizontalAlignment(SwingConstants.CENTER);
		// }
		value = getRidofZero(token, parts);
		return value;
	}

	private String getRidofZero(String token, String[] parts) {
		String value;
		UFDouble numerator = new UFDouble(parts[0]);
		numerator.setTrimZero(true);

		UFDouble denominator = new UFDouble(parts[1]);
		denominator.setTrimZero(true);
		value = numerator + token + denominator;
		return value;
	}

	// /**
	// * Notification from the UIManager that the L&F has changed. Replaces the
	// * current UI object with the latest version from the UIManager.
	// *
	// * @see JComponent#updateUI
	// */
	// public void updateUI() {
	// super.updateUI();
	// setForeground(null);
	// setBackground(null);
	// }

	private void setShowWarning(boolean showWarning) {
		this.showWarning = showWarning;
	}

	public BillItemNumberFormat getNumberFormat() {

		if (item != null)
			return item.getNumberFormat();

		return null;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (showWarning) {
			// ImageIcon warningIcon = ImageToolKit.loadWarningImageIcon();
			String iconPath = "themeres\\login\\error.png";
			ImageIcon warningIcon = ThemeResourceCenter.getInstance().getImage(
					iconPath);
			if (warningIcon != null)
				g.drawImage(warningIcon.getImage(),
						getWidth() - warningIcon.getIconWidth() - 2, 3,// 0,
																		// 错误图标位置太偏上
						null);
		}
	}

	// public static BillTableCellRenderer getInstance() {
	// return cellrender;
	// }
}
