package nc.ui.mmgp.flexgant.treetable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.md.model.type.impl.EnumType;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.IBillItem;
import nc.vo.bill.pub.BillUtil;
import nc.vo.bill.pub.MiscUtil;
import nc.vo.mmgp.util.MMArrayUtil;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-9下午3:38:58
 * @author: tangxya
 */
public class MMGPGantMDEnumUtil {

	private MMGPGantItem item = null;

	public MMGPGantMDEnumUtil(MMGPGantItem gantitem) {
		this.item = gantitem;
	}

	private String getRefType() {
		return item.getRefType();
	}

	public DefaultConstEnum[] getGantMDEnumData() {
		
		EnumType enumtype=(EnumType)item.getMetaDataProperty().getAttribute().getDataType();
		IConstEnum[] enums=enumtype.getConstEnums();
		DefaultConstEnum[] items = new DefaultConstEnum[enums.length];
		if(enums instanceof DefaultConstEnum[] ){
			items = (DefaultConstEnum[]) enums;
		}else{
			for(int i=0;i<enums.length;i++){
				items[i]=new DefaultConstEnum(enums[i].getValue(),enums[i].getName());	
			}
		}
	
		return items;
		
		/*String comboitems = getRefType();
		if (comboitems != null && (comboitems = comboitems.trim()).length() > 0) {
			boolean isFromMeta = comboitems
					.startsWith(MetaDataPropertyAdpter.COMBOBOXMETADATATOKEN);
			comboitems = comboitems.replaceFirst(
					MetaDataPropertyAdpter.COMBOBOXMETADATATOKEN, "");
			Object enumdata= parseEnumData(comboitems, isFromMeta);
			if (enumdata instanceof DefaultConstEnum[])
				items = (DefaultConstEnum[]) enumdata;
			}
			if (enumdata instanceof IConstEnumFactory) {
				items = ((IConstEnumFactory) enumdata).getAllConstEnums();
			} else if (enumdata instanceof DefaultConstEnum[])
				items = (DefaultConstEnum[]) enumdata;
		}*/


		/*
		 * if (comboBoxData != null && comboBoxData != COMBO_INIT_DATA) {
		 * Object[] items = null; if (comboBoxData instanceof IConstEnumFactory)
		 * { items = ((IConstEnumFactory) comboBoxData).getAllConstEnums(); }
		 * else if (comboBoxData instanceof Object[]) items = (Object[])
		 * comboBoxData; if (items != null) { // if (cb instanceof UIComboBox) {
		 * // ((UIComboBox) cb).addItems(items); // } else if (!isNull()) { //
		 * cb.addItem(new DefaultConstEnum(null,"")); cb.addItem(""); } for (int
		 * i = 0; i < items.length; i++) cb.addItem(items[i]); } }
		 */
	}

	private Object parseEnumData(String comboitems, boolean isFromMeta) {
		Object comboBoxData = null;
		boolean isSX;
		ArrayList<String> list = new ArrayList<String>();

		String[] items = MiscUtil.getStringTokens(comboitems, ",");
		if (MMArrayUtil.isEmpty(items)) {
			return comboBoxData;
		}
		/*
		 * // 返回索引 String[] strArray = new String[] { IBillItem.COMBOTYPE_INDEX,
		 * IBillItem.COMBOTYPE_INDEX_DBFIELD };// , COMBOTYPE_INDEX_X // };
		 * 
		 * 
		 * setWithIndex(BillUtil.getStringIndexOfArray(strArray, items[0]) >=
		 * 0);
		 */

		// 获得下拉项目值
		String[] strArray = new String[] { IBillItem.COMBOTYPE_INDEX,
				IBillItem.COMBOTYPE_INDEX_X, IBillItem.COMBOTYPE_VALUE,
				IBillItem.COMBOTYPE_VALUE_X };

		isSX = IBillItem.COMBOTYPE_VALUE_X.equals(items[0]); // SX
		boolean isIX = IBillItem.COMBOTYPE_INDEX_X.equals(items[0]); // IX

		if (BillUtil.getStringIndexOfArray(strArray, items[0]) >= 0) {
			for (int i = 1; i < items.length; i++) {
				list.add(items[i].trim());
			}
		} else if (items.length == 3
				&& BillUtil.getStringIndexOfArray(new String[] {
						IBillItem.COMBOTYPE_INDEX_DBFIELD,
						IBillItem.COMBOTYPE_VALUE_DBFIELD }, items[0]) >= 0) {
			items = null;//new DataDictionaryReader(items[1], items[2]).getQzsm(); 65暂时还没找到数据字典的功能，等后续再维护  maoyl
			if (items != null) {
				for (int i = 0; i < items.length; i++) {
					list.add(items[i].trim());
				}
			}
		}

		// 解析值
		if (list.size() > 0) {
			String nodecode = getNodecode();
			String[] ss = list.toArray(new String[list.size()]);
			if (isSX || isIX) {
				DefaultConstEnum[] ces = new DefaultConstEnum[ss.length];
				Object value = null;
				for (int i = 0; i < ss.length; i++) {

					int pos = ss[i].indexOf('=');

					String name = pos >= 0 ? ss[i].substring(0, pos) : ss[i];
					name = getDecodeStr(name, isFromMeta);

					String x_name = null;
					try {
						if (nodecode != null)
							x_name = nc.ui.ml.NCLangRes.getInstance()
									.getStrByID(nodecode, name);
					} catch (Exception e) {
					}

					if (x_name != null)
						name = x_name;

					if (pos >= 0) {

						value = getDecodeStr(ss[i].substring(pos + 1),
								isFromMeta);

						if (isIX) {
							value = Integer.valueOf(value.toString());
							/* comboBoxReturnInteger = true; */
						}
					} else {

						if (isSX) {
							value = getDecodeStr(ss[i], isFromMeta);
						} else {
							value = Integer.valueOf(i);
							/* comboBoxReturnInteger = true; */
						}
					}

					ces[i] = new DefaultConstEnum(value, name);
				}
				comboBoxData = ces;
			} else {
				comboBoxData = ss;
			}
		}

		return comboBoxData;
	}

	private String getDecodeStr(String str, boolean isFromMeta) {

		if (isFromMeta) {
			try {
				return URLDecoder.decode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Logger.debug(e.getMessage());
			}
		}

		return str;
	}

	private String getNodecode() {
		return item.getNodecode();
	}

}
