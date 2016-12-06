package nc.ui.mmgp.flexgant.view.treetable.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.bd.userdefitem.IUserdefitemQryService;
import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.IMetaDataProperty;
import nc.vo.pub.bill.MetaDataPropertyFactory;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * @Description: TODO
 *               <p>
 *               仿照MarAssistantUtils类写的处理甘特图的物料辅助属性
 *               </p>
 * @data:2014-6-3上午9:57:04
 * @author: tangxya
 */
public class MMGPGantMarAssistantUtils {

	/** 物料辅助属性自定义项规则编码 */
	public static final String MARASSISTANTRULECODE = "materialassistant";

	private static Map<String, UserdefitemVO[]> userdefitemMap = new HashMap<String, UserdefitemVO[]>();

	/**
	 * 辅助属性编码修正值
	 * <p>
	 * 需要替换界面的辅助属性编码是从1到10，但是平台定义的需要替换控件 的辅助属性编码是从6到15，所以需要一个修正值
	 */
	private static final int IPREFIX = 5;

	private MMGPGantMarAssistantUtils() {
		super();
	}

	public static Map<String, UserdefitemVO[]> getUserdefitemMap() {
		return userdefitemMap;
	}

	public static void updateMarAssistantGantItem(UserdefitemVO[] userdefitemVOs) {
		// 将获取到的物料辅助属性暂存起来，供查询时调用
		String pk_group = WorkbenchEnvironment.getInstance().getGroupVO()
				.getPk_group();
		userdefitemMap.put(pk_group, userdefitemVOs);
	}

	/**
	 * 获取修正后的辅助属性自定义项VO数据<br>
	 * 由于单据上的自由辅助属性编号是从1开始，而在设置时的编号从6开始， 所以需要有一个修正处理，让两者一致起来，好方便后续处理
	 * 
	 * @param pk_org
	 * @return 修正编号后的自由辅助属性自定义项VO数组，如果没有则返回长度为0的数组
	 */
	public static UserdefitemVO[] getFixedAssistUserDefitem(String pk_org) {
		// 查询自定义项
		UserdefitemVO[] defs = MMGPGantMarAssistantUtils
				.queryUserDefitem(pk_org);
		// 处理编码
		return MMGPGantMarAssistantUtils.prefixItems(defs);
	}

	private static UserdefitemVO[] prefixItems(UserdefitemVO[] items) {
		for (UserdefitemVO item : items) {
			int newIndex = item.getPropindex().intValue()
					- MMGPGantMarAssistantUtils.IPREFIX;
			item.setPropindex(Integer.valueOf(newIndex));
		}
		return items;
	}

	private static UserdefitemVO[] queryUserDefitem(String pk_org) {
		// 获取自定义项
		IUserdefitemQryService service = NCLocator.getInstance().lookup(
				IUserdefitemQryService.class);

		UserdefitemVO[] defs = null;
		try {
			defs = service.queryUserdefitemVOsByUserdefruleCode(
					MMGPGantMarAssistantUtils.MARASSISTANTRULECODE, pk_org);
			if (null == defs) {
				defs = new UserdefitemVO[0];
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return defs;
	}

	static void updateMarAssistantGantItem(MMGPGantChartModel model,
			String prefix, UserdefitemVO[] userdefitemVOs) {
		UserdefitemVO[] defs = userdefitemVOs;
		if (defs == null || defs.length == 0) {
			return;
		}
		// 处理编码（包括固定自定义项，第一次是从1开始，需要减去固定自定义项；第二次以后则不需要再减）
		if (defs[0].getPropindex().intValue() > 0) {
			defs = MMGPGantMarAssistantUtils.prefixItems(defs);
		}
		// 替换界面
		MMGPGantMarAssistantUtils.resetGantItems(model, prefix, defs);

	}

	private static List<MMGPGantItem> resetGantItems(MMGPGantChartModel data,
			String prefix, UserdefitemVO[] defs) {
		List<MMGPGantItem> result = new ArrayList<MMGPGantItem>();
		for (UserdefitemVO def : defs) {
			int index = def.getPropindex().intValue();
			String key = prefix + index;
			MMGPGantItem item = data.getItemByKey(key);
			if (item != null && item.getMetaDataProperty() != null) {
				//处理原来的item,类型设置为辅助属性引用的类型
				MMGPGantMarAssistantUtils.processGantItemWithMD(item, def);
				result.add(item);
				//
				/* processBillItem(item, data, null); */
			}
		}
		return result;
	}

	
	private static MMGPGantItem processGantItemWithMD(MMGPGantItem billItem,
			UserdefitemVO userdefitem) {
		IMetaDataProperty prop = null;
		try {
			prop = MetaDataPropertyFactory
					.creatMetaDataUserDefPropertyByDefItemVO(
							billItem.getMetaDataProperty(), userdefitem);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		if (prop == null) {
			return billItem;
		}
		billItem.setIsDef(true);
		billItem.setMetaDataProperty(prop);
		billItem.setDataType(billItem.getMetaDataProperty().getDataType()) ;
		billItem.setValueClassType(billItem.getColumnClass(billItem.getDataType()));
		if (billItem.getRefTypeSet() != null) {
			billItem.getRefTypeSet().setReturnCode(false);
		}
		return billItem;
	}

}
