package nc.report.mmgp.marass;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.userdefitem.IUserdefitemQryService;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.cache.CacheManager;
import nc.vo.cache.ICache;
import nc.vo.cache.config.CacheConfig;
import nc.vo.cache.config.CacheConfig.CacheType;
import nc.vo.cache.config.CacheConfig.MemoryCacheLevel;
import nc.vo.cache.config.CacheConfigFactory;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 *
 * @since 创建日期 2013-8-20
 * @author dongchx
 */
@SuppressWarnings("unchecked")
public class MMGPMarAssistantCaption {

    /**
     * 辅助属性编码修正值
     * <p>
     * 需要替换界面的辅助属性编码是从1到10，但是平台定义的需要替换控件 的辅助属性编码是从6到15，所以需要一个修正值
     */
    private static final int IPREFIX = 5;

    /** 物料辅助属性自定义项规则编码 */
    private static final String MARASSISTANTRULECODE = "materialassistant";

    static Map<String, UserdefitemVO[]> userdefitemMap;

    static {
        // 为了减少连接数，使用了soft级别的内存缓存，来缓存交易类型
        CacheConfig configForBusi =
                CacheConfigFactory.getCacheConfig(
                    MMGPMarAssistantCaption.class.getName() + "_busi",
                    false,
                    5 * 60 * 1000,
                    -1,
                    CacheType.MEMORY,
                    MemoryCacheLevel.SOFT);
        ICache cacheForBusi = CacheManager.getInstance().getCache(configForBusi);
        MMGPMarAssistantCaption.userdefitemMap = cacheForBusi.toMap();
    }

    /**
     * 处理自定义项查询条件，将自定义项真正的信息设回QCVO中
     */
    public String processDef(String paraKey) {
        String showname = NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "MarAssistantCaption-0000")/* 自定义辅助属性 */;
        String[] strs = paraKey.substring(1, paraKey.length()).split("#");
        if (strs == null || strs.length != 2) {
            return showname;
        }
        String vfreecode = strs[1];

        String pk_group = AppContext.getInstance().getPkGroup();
        UserdefitemVO[] itemVOs = null;
        if (MMGPMarAssistantCaption.userdefitemMap.get(pk_group) != null) {
            itemVOs = MMGPMarAssistantCaption.userdefitemMap.get(pk_group);
        } else {
            itemVOs = this.queryMarAsstDefitem(pk_group);
            if (itemVOs != null) {
                MMGPMarAssistantCaption.userdefitemMap.put(pk_group, itemVOs);
            }
        }

        String prop = null;
        if (Character.isDigit(vfreecode.charAt(vfreecode.length() - 2))) {
            prop = vfreecode.substring(vfreecode.length() - 2, vfreecode.length());
        } else {
            prop = vfreecode.substring(vfreecode.length() - 1, vfreecode.length());
        }

        showname = this.getVfreeName(Integer.parseInt(prop));

        for (int i = 0; itemVOs != null && i < itemVOs.length; i++) {
            if (itemVOs[i].getPropindex().equals(Integer.parseInt(prop) + IPREFIX)) {
                showname = itemVOs[i].getShowname();
            }
        }
        return showname;
    }

    /**
     * 自由辅助属性名称
     *
     * @param sequence
     * @return
     */
    private String getVfreeName(int sequence) {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0067")/*@res "自由辅助属性"*/ + sequence;
    }

    private UserdefitemVO[] queryMarAsstDefitem(String pk_group) {
        UserdefitemVO[] defs = null;
        // 获取自定义项
        IUserdefitemQryService service = NCLocator.getInstance().lookup(IUserdefitemQryService.class);
        try {
            defs = service.queryUserdefitemVOsByUserdefruleCode(MARASSISTANTRULECODE, pk_group);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return defs;
    }

}