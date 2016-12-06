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
 *               ����MarAssistantUtils��д�Ĵ������ͼ�����ϸ�������
 *               </p>
 * @data:2014-6-3����9:57:04
 * @author: tangxya
 */
public class MMGPGantMarAssistantUtils {

	/** ���ϸ��������Զ����������� */
	public static final String MARASSISTANTRULECODE = "materialassistant";

	private static Map<String, UserdefitemVO[]> userdefitemMap = new HashMap<String, UserdefitemVO[]>();

	/**
	 * �������Ա�������ֵ
	 * <p>
	 * ��Ҫ�滻����ĸ������Ա����Ǵ�1��10������ƽ̨�������Ҫ�滻�ؼ� �ĸ������Ա����Ǵ�6��15��������Ҫһ������ֵ
	 */
	private static final int IPREFIX = 5;

	private MMGPGantMarAssistantUtils() {
		super();
	}

	public static Map<String, UserdefitemVO[]> getUserdefitemMap() {
		return userdefitemMap;
	}

	public static void updateMarAssistantGantItem(UserdefitemVO[] userdefitemVOs) {
		// ����ȡ�������ϸ��������ݴ�����������ѯʱ����
		String pk_group = WorkbenchEnvironment.getInstance().getGroupVO()
				.getPk_group();
		userdefitemMap.put(pk_group, userdefitemVOs);
	}

	/**
	 * ��ȡ������ĸ��������Զ�����VO����<br>
	 * ���ڵ����ϵ����ɸ������Ա���Ǵ�1��ʼ����������ʱ�ı�Ŵ�6��ʼ�� ������Ҫ��һ����������������һ���������÷����������
	 * 
	 * @param pk_org
	 * @return ������ź�����ɸ��������Զ�����VO���飬���û���򷵻س���Ϊ0������
	 */
	public static UserdefitemVO[] getFixedAssistUserDefitem(String pk_org) {
		// ��ѯ�Զ�����
		UserdefitemVO[] defs = MMGPGantMarAssistantUtils
				.queryUserDefitem(pk_org);
		// �������
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
		// ��ȡ�Զ�����
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
		// ������루�����̶��Զ������һ���Ǵ�1��ʼ����Ҫ��ȥ�̶��Զ�����ڶ����Ժ�����Ҫ�ټ���
		if (defs[0].getPropindex().intValue() > 0) {
			defs = MMGPGantMarAssistantUtils.prefixItems(defs);
		}
		// �滻����
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
				//����ԭ����item,��������Ϊ�����������õ�����
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
