package nc.ui.mmgp.flexgant.view.treetable.userdefitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.bs.logging.Logger;
import nc.md.util.BizMDModelUtil;
import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.vo.bd.userdefrule.Disproperty;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.ml.MultiLangContext;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.IMetaDataProperty;
import nc.vo.pub.bill.MetaDataPropertyFactory;

/**
 * @Description: TODO
 *               <p>
 *               ��ϸ��������
 *               </p>
 * @data:2014-6-4����3:38:47
 * @author: tangxya
 */
public class MMGPDefaultUserDefGantItemsProcessor implements
		IMMGPUserDefGantItemsProcessor {

	@Override
	public List<MMGPGantItem> resetItems(MMGPGantChartModel chartModel,
			String prefix, UserdefitemVO[] userdefitems) {
		List<MMGPGantItem> result = new ArrayList<MMGPGantItem>();
		if (chartModel == null || MMArrayUtil.isEmpty(userdefitems))
			return result;

		BizMDModelUtil.preloadMetaInfosByUserdefitems(userdefitems);

		for (UserdefitemVO userdefitem : userdefitems) {
			String itemkey = prefix + userdefitem.getPropindex();
			MMGPGantItem item = (MMGPGantItem) chartModel.getContext()
					.getItemContainer().getItem(itemkey);
			if (item != null) {
				result.add(processGantItem(chartModel, item, userdefitem,
						prefix));
			}
		}
		return result;
	}

	protected MMGPGantItem processGantItem(MMGPGantChartModel chartModel,
			MMGPGantItem item, UserdefitemVO userdefitem, String prefix) {
		// ����ģ������Ԫ��������
		return processGantItemWithMD(chartModel, item, userdefitem);
	}

	private IMetaDataProperty createMetaDataProperty(MMGPGantItem billItem,
			UserdefitemVO userdefitem) {
		try {
			return MetaDataPropertyFactory
					.creatMetaDataUserDefPropertyByDefItemVO(
							billItem.getMetaDataProperty(), userdefitem);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			return null;
		}
	}

	private MMGPGantItem processGantItemWithMD(MMGPGantChartModel chartModel,
			MMGPGantItem billItem, UserdefitemVO userdefitem) {
		IMetaDataProperty prop = createMetaDataProperty(billItem, userdefitem);
		if (prop == null) {
			return billItem;
		}
		billItem.setIsDef(true);
		this.setBillItemShowname(userdefitem, billItem);
		billItem.setMetaDataProperty(prop);
		this.setItemShow(billItem, userdefitem);
		billItem.setDataType(billItem.getMetaDataProperty().getDataType());
		billItem.setValueClassType(billItem.getColumnClass(billItem
				.getDataType()));
		if (billItem.getRefTypeSet() != null) {
			if (userdefitem.getUsufruct() != null
					&& userdefitem.getUsufruct().booleanValue()
					&& !StringUtils.isEmpty(userdefitem.getUsufructgroup())) {
				billItem.getRefTypeSet().setDataPowerOperation_Code(
						userdefitem.getUsufructgroup());
			}
			billItem.getRefTypeSet().setReturnCode(false);
		}
		return billItem;
	}

	private void setBillItemShowname(UserdefitemVO defitemvo, MMGPGantItem item) {

		// �������
		Integer index = MultiLangContext.getInstance().getCurrentLangSeq();
		String showname = defitemvo.getShowname();
		if (index == null || index.intValue() == -1 || index.intValue() == 1) {
			item.setShowName(showname);
		} else {
			String langname = (String) defitemvo
					.getAttributeValue(UserdefitemVO.SHOWNAME
							+ index.intValue());
			if (StringUtils.isEmpty(langname)) {
				item.setShowName(showname);
			} else {
				item.setShowName(langname);
			}
		}
	}

	private void setItemShow(MMGPGantItem billItem, UserdefitemVO userdefitem) {
		if (userdefitem.getDisproperty() == null) {
			// Ĭ��Ϊ�ղ�������ģ�����ã�һ�㲻��ʾ,v60�������ű�
			// billItem.setShow(true);
		} else if (userdefitem.getDisproperty().equals(
				Disproperty.Template.toInt())) {
		} else if (userdefitem.getDisproperty()
				.equals(Disproperty.Hide.toInt())) {
			billItem.setShow(false);
		} else {
			// ������ʾ
			billItem.setShow(true);
		}
	}

}
