package nc.ui.mmgp.uif2.view.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.bd.userdefitem.IUserdefitemQryService;
import nc.pubitf.uapbd.assistant.IMarAssistantPubService;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.uif2.userdefitem.UserDefItemContainer;
import nc.vo.bd.userdefrule.Disproperty;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.IMetaDataProperty;
import nc.vo.pub.bill.MetaDataPropertyFactory;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ���ݽ���ĸ������Ա�����1��10������ƽ̨�������Ҫ�滻�ؼ��ĸ������Ա����Ǵ�6��15 ����Ҫ���Զ�������������������û�а취ֱ��ʹ��nc.ui.bill.tools.UserDefItemTools
 * <p>
 * һ���ַ����Ǵ�UserDefItemTools����copy������
 * 
 * @author puqh
 */
public class MMGPMarAssistantUtils {

    static class CardBillData implements MMGPMarAssistantUtils.IBaseBillData {
        private BillData bd;

        CardBillData(BillData bd) {
            this.bd = bd;
        }

        @Override
        public BillItem getBodyItem(String tabcode,
                                    String key) {
            return this.bd.getBodyItem(tabcode, key);
        }

        @Override
        public String[] getBodyTableCodes() {
            return this.bd.getTableCodes(IBillItem.BODY);
        }

        @Override
        public BillItem getHeadItem(String key) {
            return this.bd.getHeadItem(key);
        }

        @Override
        public BillModel getBodyBillModel(String tabcode) {
            return this.bd.getBillModel(tabcode);
        }

        @Override
        public BillModel getHeadBillModel() {
            return null;
        }

    }

    static interface IBaseBillData {
        BillItem getBodyItem(String tabcode,
                             String key);

        String[] getBodyTableCodes();

        BillItem getHeadItem(String key);

        BillModel getBodyBillModel(String tabcode);

        BillModel getHeadBillModel();
    }

    static class ListBillData implements MMGPMarAssistantUtils.IBaseBillData {
        private BillListData bd;

        ListBillData(BillListData bd) {
            this.bd = bd;
        }

        @Override
        public BillItem getBodyItem(String tabcode,
                                    String key) {
            return this.bd.getBodyItem(tabcode, key);
        }

        @Override
        public String[] getBodyTableCodes() {
            return this.bd.getBodyTableCodes();
        }

        @Override
        public BillItem getHeadItem(String key) {
            return this.bd.getHeadItem(key);
        }

        @Override
        public BillModel getBodyBillModel(String tabcode) {
            return this.bd.getBodyBillModel(tabcode);
        }

        @Override
        public BillModel getHeadBillModel() {
            return this.bd.getHeadBillModel();
        }

    }

    /** ���ϸ��������Զ����������� */
    public static final String MARASSISTANTRULECODE = "materialassistant";

    /**
     * �������Ա�������ֵ
     * <p>
     * ��Ҫ�滻����ĸ������Ա����Ǵ�1��10������ƽ̨�������Ҫ�滻�ؼ� �ĸ������Ա����Ǵ�6��15��������Ҫһ������ֵ
     */
    private static final int IPREFIX = 5;

    private MMGPMarAssistantUtils() {
        super();
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
        UserdefitemVO[] defs = MMGPMarAssistantUtils.queryUserDefitem(pk_org);
        // �������
        return MMGPMarAssistantUtils.prefixItems(defs);
    }

    public static UserdefitemVO[] getFixedAssistUserDefitem(UserDefItemContainer container) {
        UserdefitemVO[] defs =
                container.getUserdefitemVOsByUserdefruleCode(MMGPMarAssistantUtils.MARASSISTANTRULECODE);
        // �������
        return MMGPMarAssistantUtils.prefixItems(defs);
    }

    /**
     * ���ݼ�����������ѯ���������������͵Ķ���
     * 
     * @param pk_group
     * @throws BusinessException
     */
    public static UserdefitemVO[] queryMarAssistantDefine(String pk_group) throws BusinessException {
        return NCLocator.getInstance().lookup(IMarAssistantPubService.class).queryMarAssistantDefine(pk_group);
    }

    /**
     * ���ݸ������ԽṹID���飬��ѯ�������Խṹ�����ĸ����������
     * 
     * @param marAsstFrameIDs
     * @throws BusinessException
     */
    public static Map<String, List<Integer>> queryMarAsstFrameIncludeDefPropIndex(String[] marAsstFrameIDs)
            throws BusinessException {
        return NCLocator
            .getInstance()
            .lookup(IMarAssistantPubService.class)
            .queryMarAsstFrameIncludeDefPropIndex(marAsstFrameIDs);
    }

    /**
     * ����ʵ����������ϸ������Թ������, ���±�ͷ�û����������ֶε�չ������.
     * 
     * @param userdefruleCode
     * @param data
     * @param tabcode
     * @param prefix
     * @param pk_org
     */
    public static void updateMarAssistantBillItem(BillData bd,
                                                  String prefix,
                                                  UserdefitemVO[] userdefitemVOs) {
        MMGPMarAssistantUtils.updateMarAssistantBillItem(new CardBillData(bd), prefix, userdefitemVOs);
    }

    private static Map<String, UserdefitemVO[]> userdefitemMap = new HashMap<String, UserdefitemVO[]>();

    public static Map<String, UserdefitemVO[]> getUserdefitemMap() {
        return userdefitemMap;
    }

    /**
     * ���ݱ�ͷʵ��������û��������Թ������, �����û����������ֶε�չ������.
     * 
     * @param userdefruleCode
     * @param data
     * @param tabcode
     * @param prefix
     * @param pk_org
     */
    public static void updateMarAssistantBillItem(BillListData bd,
                                                  String prefix,
                                                  UserdefitemVO[] userdefitemVOs) {
        // ����ȡ�������ϸ��������ݴ�����������ѯʱ����
        String pk_group = WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
        userdefitemMap.put(pk_group, userdefitemVOs);

        MMGPMarAssistantUtils.updateMarAssistantBillItem(new ListBillData(bd), prefix, userdefitemVOs);
    }

    /**
     * ���ݱ�ͷʵ��������û��������Թ������, ���±�ͷ�û����������ֶε�չ������.
     * 
     * @param userdefruleCode
     * @param data
     * @param tabcode
     * @param prefix
     * @param pk_org
     */
    static void updateMarAssistantBillItem(IBaseBillData bd,
                                           String prefix,
                                           UserdefitemVO[] userdefitemVOs) {
        // ��ѯ�Զ�����
        // UserdefitemVO[] defs = MarAssistantUtils.queryUserDefitem(pk_org);
        UserdefitemVO[] defs = userdefitemVOs;
        if (defs == null || defs.length == 0) {
            return;
        }
        // ������루�����̶��Զ������һ���Ǵ�1��ʼ����Ҫ��ȥ�̶��Զ�����ڶ����Ժ�����Ҫ�ټ���
        if (defs[0].getPropindex().intValue() > 0) {
            defs = MMGPMarAssistantUtils.prefixItems(defs);
        }
        // �滻����
        MMGPMarAssistantUtils.resetHeadItems(bd, prefix, defs);
        if (null == bd.getBodyTableCodes()) {
            return;
        }

        for (String tableCode : bd.getBodyTableCodes()) {
            MMGPMarAssistantUtils.resetBodyItems(bd, tableCode, prefix, defs);
        }
    }

    private static UserdefitemVO[] prefixItems(UserdefitemVO[] items) {
        for (UserdefitemVO item : items) {
            int newIndex = item.getPropindex().intValue() - MMGPMarAssistantUtils.IPREFIX;
            item.setPropindex(Integer.valueOf(newIndex));
        }
        return items;
    }

    private static BillItem processBillItemWithMD(BillItem billItem,
                                                  UserdefitemVO userdefitem) {
        IMetaDataProperty prop = null;
        try {
            prop =
                    MetaDataPropertyFactory.creatMetaDataUserDefPropertyByDefItemVO(
                        billItem.getMetaDataProperty(),
                        userdefitem);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        if (prop == null) {
            return billItem;
        }
        billItem.setIsDef(true);
        billItem.setMetaDataProperty(prop);
        // ���Զ�����ͬ����ֻҪ���þͿ�����ʾ
        setItemShow(billItem, userdefitem);
        if (billItem.getRefTypeSet() != null) {
            billItem.getRefTypeSet().setReturnCode(false);
        }
        return billItem;
    }

    private static void setItemShow(BillItem billItem,
                                    UserdefitemVO userdefitem) {
        if (userdefitem.getDisproperty() == null) {
            // Ĭ��Ϊ�ղ�������ģ�����ã�һ�㲻��ʾ,v60�������ű�
            // billItem.setShow(true);
        } else if (userdefitem.getDisproperty().equals(Disproperty.Template.toInt())) {
        } else if (userdefitem.getDisproperty().equals(Disproperty.Hide.toInt())) {
            billItem.setShow(false);
        } else {
            // ������ʾ
            billItem.setShow(true);
        }
    }

    private static UserdefitemVO[] queryUserDefitem(String pk_org) {
        // ��ȡ�Զ�����
        IUserdefitemQryService service = NCLocator.getInstance().lookup(IUserdefitemQryService.class);

        UserdefitemVO[] defs = null;
        try {
            defs = service.queryUserdefitemVOsByUserdefruleCode(MMGPMarAssistantUtils.MARASSISTANTRULECODE, pk_org);
            if (null == defs) {
                defs = new UserdefitemVO[0];
            }
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return defs;
    }

    /**
     * Ϊ�������͵����ϸ����������_ID�е��к�
     * 
     * @param item
     * @param data
     * @param tabcode
     */
    private static void processBillItem(BillItem item,
                                        IBaseBillData data,
                                        String tabcode) {
        int dataType = item.getDataType();
        if (dataType != IBillItem.UFREF && dataType != IBillItem.COMBO) return;

        BillModel billModel = null;
        if (!StringUtil.isEmptyWithTrim(tabcode)) billModel = data.getBodyBillModel(tabcode);
        else
            billModel = data.getHeadBillModel();
        if (billModel == null) return;

        billModel.addBodyItemIndex(item.getKey() + IBillItem.ID_SUFFIX, billModel.getBodyColByKey(item.getKey()));
    }

    private static List<BillItem> resetBodyItems(IBaseBillData data,
                                                 String tabcode,
                                                 String prefix,
                                                 UserdefitemVO[] defs) {
        List<BillItem> result = new ArrayList<BillItem>();
        for (UserdefitemVO def : defs) {
            int index = def.getPropindex().intValue();
            String key = prefix + index;
            BillItem item = data.getBodyItem(tabcode, key);
            if (item != null && item.getMetaDataProperty() != null) {
                MMGPMarAssistantUtils.processBillItemWithMD(item, def);
                result.add(item);
                processBillItem(item, data, tabcode);
            }
        }
        return result;
    }

    private static List<BillItem> resetHeadItems(IBaseBillData data,
                                                 String prefix,
                                                 UserdefitemVO[] defs) {
        List<BillItem> result = new ArrayList<BillItem>();
        for (UserdefitemVO def : defs) {
            int index = def.getPropindex().intValue();
            String key = prefix + index;
            BillItem item = data.getHeadItem(key);
            if (item != null && item.getMetaDataProperty() != null) {
                MMGPMarAssistantUtils.processBillItemWithMD(item, def);
                result.add(item);
                processBillItem(item, data, null);
            }
        }
        return result;
    }
}
