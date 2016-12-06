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
 * 单据界面的辅助属性编码是1到10，但是平台定义的需要替换控件的辅助属性编码是从6到15 所以要做自定义项编码的修正，所以没有办法直接使用nc.ui.bill.tools.UserDefItemTools
 * <p>
 * 一部分方法是从UserDefItemTools类中copy过来的
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

    /** 物料辅助属性自定义项规则编码 */
    public static final String MARASSISTANTRULECODE = "materialassistant";

    /**
     * 辅助属性编码修正值
     * <p>
     * 需要替换界面的辅助属性编码是从1到10，但是平台定义的需要替换控件 的辅助属性编码是从6到15，所以需要一个修正值
     */
    private static final int IPREFIX = 5;

    private MMGPMarAssistantUtils() {
        super();
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
        UserdefitemVO[] defs = MMGPMarAssistantUtils.queryUserDefitem(pk_org);
        // 处理编码
        return MMGPMarAssistantUtils.prefixItems(defs);
    }

    public static UserdefitemVO[] getFixedAssistUserDefitem(UserDefItemContainer container) {
        UserdefitemVO[] defs =
                container.getUserdefitemVOsByUserdefruleCode(MMGPMarAssistantUtils.MARASSISTANTRULECODE);
        // 处理编码
        return MMGPMarAssistantUtils.prefixItems(defs);
    }

    /**
     * 根据集团主键，查询辅助属性数据类型的定义
     * 
     * @param pk_group
     * @throws BusinessException
     */
    public static UserdefitemVO[] queryMarAssistantDefine(String pk_group) throws BusinessException {
        return NCLocator.getInstance().lookup(IMarAssistantPubService.class).queryMarAssistantDefine(pk_group);
    }

    /**
     * 根据辅助属性结构ID数组，查询辅助属性结构包含的辅助属性序号
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
     * 根据实体关联的物料辅助属性规则编码, 更新表头用户定义属性字段的展现属性.
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
     * 根据表头实体关联的用户定义属性规则编码, 更新用户定义属性字段的展现属性.
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
        // 将获取到的物料辅助属性暂存起来，供查询时调用
        String pk_group = WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
        userdefitemMap.put(pk_group, userdefitemVOs);

        MMGPMarAssistantUtils.updateMarAssistantBillItem(new ListBillData(bd), prefix, userdefitemVOs);
    }

    /**
     * 根据表头实体关联的用户定义属性规则编码, 更新表头用户定义属性字段的展现属性.
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
        // 查询自定义项
        // UserdefitemVO[] defs = MarAssistantUtils.queryUserDefitem(pk_org);
        UserdefitemVO[] defs = userdefitemVOs;
        if (defs == null || defs.length == 0) {
            return;
        }
        // 处理编码（包括固定自定义项，第一次是从1开始，需要减去固定自定义项；第二次以后则不需要再减）
        if (defs[0].getPropindex().intValue() > 0) {
            defs = MMGPMarAssistantUtils.prefixItems(defs);
        }
        // 替换界面
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
        // 跟自定义项同步，只要设置就可以显示
        setItemShow(billItem, userdefitem);
        if (billItem.getRefTypeSet() != null) {
            billItem.getRefTypeSet().setReturnCode(false);
        }
        return billItem;
    }

    private static void setItemShow(BillItem billItem,
                                    UserdefitemVO userdefitem) {
        if (userdefitem.getDisproperty() == null) {
            // 默认为空不处理，走模板设置，一般不显示,v60走升级脚本
            // billItem.setShow(true);
        } else if (userdefitem.getDisproperty().equals(Disproperty.Template.toInt())) {
        } else if (userdefitem.getDisproperty().equals(Disproperty.Hide.toInt())) {
            billItem.setShow(false);
        } else {
            // 处理显示
            billItem.setShow(true);
        }
    }

    private static UserdefitemVO[] queryUserDefitem(String pk_org) {
        // 获取自定义项
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
     * 为参照类型的物料辅助属性添加_ID列的列号
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
