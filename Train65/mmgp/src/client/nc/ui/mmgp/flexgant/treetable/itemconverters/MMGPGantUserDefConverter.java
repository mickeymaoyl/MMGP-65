package nc.ui.mmgp.flexgant.treetable.itemconverters;

import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItemConverterFactory;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillItemConverter;
import nc.ui.pub.bill.itemconverters.NotConverter;

/**
 * @Description: TODO
 *               <p>
 *               œÍœ∏π¶ƒ‹√Ë ˆ
 *               </p>
 * @data:2014-6-4…œŒÁ10:31:38
 * @author: tangxya
 */
public class MMGPGantUserDefConverter implements IBillItemConverter {

	private MMGPGantItem item = null;

	public MMGPGantUserDefConverter(MMGPGantItem item) {
		super();
		this.item = item;
	}

	public Object convertToBillItem(int type, Object value) {
		Object o = getConverter().convertToBillItem(type, value);

		return o;
	}

	public Object convertToObject(int type, Object value) {
		Object o = getConverter().convertToObject(type, value);

		if (o != null) {
			o = o.toString();
		}

		return o;
	}

	private IBillItemConverter getConverter() {
		IBillItemConverter converter = null;
		if (item.getDataType() != IBillItem.USERDEF) {
			converter = MMGPGantItemConverterFactory.getItemConverter(item,
					false);
		} else {
			converter = new NotConverter();
		}

		return converter;
	}

}
