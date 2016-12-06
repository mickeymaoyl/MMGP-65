package nc.ui.mmgp.flexgant.treetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.md.data.access.DASFacade;
import nc.md.data.access.DASInfoStruct;

import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-19下午5:02:33
 * @author: tangxya
 */
public class MMGPMetaDataGetGantModelRelationItemValue {
	private List<DASInfoStruct> ldis = null;

	private List<ArrayList<IConstEnum>> iosm = null;

	public MMGPMetaDataGetGantModelRelationItemValue() {
		super();
		ldis = null;
		iosm = null;
	}

	/*
	 * IConstEnum Value metadatapath:,Name:itemkey
	 */
	public IConstEnum[] getRelationItemValues() {
		IConstEnum[] os = null;

		if (ldis != null) {

			List<IConstEnum> los = new ArrayList<IConstEnum>();

			List<Map<String, Object[]>> valuess = DASFacade
					.getAttributeValuesList(ldis);

			for (int i = 0; i < ldis.size(); i++) {
				Map<String, Object[]> values = valuess.get(i);

				// IConstEnum Value Object[]:,Key:itemkey
				ArrayList<IConstEnum> ies = iosm.get(i);

				for (int j = 0; j < ies.size(); j++) {
					Object value = null;

					if (values != null) {
						value = values.get(ies.get(j).getValue().toString());
					}
					DefaultConstEnum ic = new DefaultConstEnum(value, ies
							.get(j).getName());

					los.add(ic);
				}
			}

			os = new IConstEnum[los.size()];
			los.toArray(os);
		}

		return os;
	}

	public void addRelationItem(MMGPGantItem item, ArrayList<IConstEnum> ies,
			String[] ids) {
		if (ldis == null)
			ldis = new ArrayList<DASInfoStruct>();

		if (iosm == null)
			iosm = new ArrayList<ArrayList<IConstEnum>>();

		if (ies != null && ies.size() > 0) {
			String[] path = new String[ies.size()];

			for (int i = 0; i < ies.size(); i++) {
				path[i] = ies.get(i).getValue().toString();

				Logger.debug("ItemKey:" + ies.get(i).getName() + " 加载属性："
						+ path[i]);
			}
			DASInfoStruct dis = new DASInfoStruct(item.getMetaDataProperty()
					.getRefBusinessEntity(), ids, path);
			ldis.add(dis);

			iosm.add(ies);
		}
	}

}
