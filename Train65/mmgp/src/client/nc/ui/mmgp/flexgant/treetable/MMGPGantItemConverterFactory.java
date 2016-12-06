package nc.ui.mmgp.flexgant.treetable;

import java.util.HashMap;
import java.util.Map;

import nc.ui.mmgp.flexgant.treetable.itemconverters.MMGPGantUserDefConverter;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillItemConverter;
import nc.ui.pub.bill.itemconverters.BodyUFRefConvert;
import nc.ui.pub.bill.itemconverters.BodyUFRefPKConvert;
import nc.ui.pub.bill.itemconverters.IntegerConverter;
import nc.ui.pub.bill.itemconverters.MultiLangTextConverter;
import nc.ui.pub.bill.itemconverters.NotConverter;
import nc.ui.pub.bill.itemconverters.StringConverter;
import nc.ui.pub.bill.itemconverters.UFBooleanConverter;
import nc.ui.pub.bill.itemconverters.UFDateConverter;
import nc.ui.pub.bill.itemconverters.UFDateTimeConverter;
import nc.ui.pub.bill.itemconverters.UFDoubleConverter;
import nc.ui.pub.bill.itemconverters.UFLiteralDateConverter;
import nc.ui.pub.bill.itemconverters.UFTimeConverter;

public class MMGPGantItemConverterFactory {

	private static Map<Integer, IBillItemConverter> converts = new HashMap<Integer, IBillItemConverter>();

	private static IBillItemConverter noconvert = new NotConverter();

	private static IBillItemConverter bodyUFRefconvert = new BodyUFRefConvert();

	private static IBillItemConverter bodyUFRefPKconvert = new BodyUFRefPKConvert();

	private MMGPGantItemConverterFactory() {

	}

	static {

		IBillItemConverter stringconvert = new StringConverter();

		converts.put(IBillItem.STRING, stringconvert);
		converts.put(IBillItem.TEXTAREA, stringconvert);
		converts.put(IBillItem.USERDEF, stringconvert);
		converts.put(IBillItem.INTEGER, new IntegerConverter());
		converts.put(IBillItem.DECIMAL, new UFDoubleConverter());
		converts.put(IBillItem.MONEY, new UFDoubleConverter());
		converts.put(IBillItem.BOOLEAN, new UFBooleanConverter());
		converts.put(IBillItem.DATETIME, new UFDateTimeConverter());
		converts.put(IBillItem.DATE, new UFDateConverter());
		converts.put(IBillItem.LITERALDATE, new UFLiteralDateConverter());
		converts.put(IBillItem.TIME, new UFTimeConverter());
		converts.put(IBillItem.FRACTION, stringconvert);
		converts.put(IBillItem.MULTILANGTEXT, new MultiLangTextConverter());
	}

	private static IBillItemConverter datebegin = new UFDateConverter(
			IBillItem.DATE_FORMAT_BEGIN);
	private static IBillItemConverter dateend = new UFDateConverter(
			IBillItem.DATE_FORMAT_END);

	public static IBillItemConverter getItemConverter(MMGPGantItem item) {
		IBillItemConverter converter = getItemConverter(item, item.isIsDef());

		return converter;
	}

	public static IBillItemConverter getItemConverter(MMGPGantItem item,
			boolean userdef) {
		IBillItemConverter converter;

		if (userdef) {
			converter = new MMGPGantUserDefConverter(item);
		} else {
			converter = getBodyConverter(item);

			if (item.getDataType() == IBillItem.DATE
					&& item.getDateformat() != IBillItem.DATE_FORMAT_NORMAL) {
				switch (item.getDateformat()) {
				case IBillItem.DATE_FORMAT_BEGIN:
					converter = datebegin;
					break;
				case IBillItem.DATE_FORMAT_END:
					converter = dateend;
					break;

				default:
					converter = datebegin;
					break;
				}
			}

			if (converter == null) {
				converter = noconvert;
			}
		}

		return converter;
	}

	private static IBillItemConverter getHeadTailConvertere(MMGPGantItem item) {
		IBillItemConverter converter;
		switch (item.getDataType()) {
		case IBillItem.UFREF:
			converter = converts.get(IBillItem.STRING);
			break;
		case IBillItem.DECIMAL:
		case IBillItem.MONEY:
			if (item.getDecimalDigits() != IBillItem.DEFAULT_DECIMAL_DIGITS) {
				converter = new UFDoubleConverter(item.getDecimalDigits());
			} else {
				converter = converts.get(item.getDataType());
			}
			break;
		default:
			converter = converts.get(item.getDataType());
			break;
		}
		return converter;
	}

	private static IBillItemConverter getBodyConverter(MMGPGantItem item) {
		IBillItemConverter converter;
		switch (item.getDataType()) {
		case IBillItem.UFREF:
			if (item.getMetaDataProperty() != null) {
				converter = bodyUFRefPKconvert;
			} else {
				converter = bodyUFRefconvert;
			}
			break;
		/*
		 * case IBillItem.COMBO: converter = new BodyComboxConverter(item);
		 * break;
		 */
		case IBillItem.DECIMAL:
		case IBillItem.MONEY:
			if (item.getDecimalDigits() != IBillItem.DEFAULT_DECIMAL_DIGITS) {
				converter = new UFDoubleConverter(item.getDecimalDigits());
			} else {
				converter = converts.get(item.getDataType());
			}
			break;
		default:
			converter = converts.get(item.getDataType());
			break;
		}
		return converter;
	}

}
