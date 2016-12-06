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
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 *
 * @since �������� 2013-8-20
 * @author dongchx
 */
@SuppressWarnings("unchecked")
public class MMGPUserDefCaption {

    static Map<String, UserdefitemVO[]> userdefitemMap;

    static {
        // Ϊ�˼�����������ʹ����soft������ڴ滺�棬�����潻������
        CacheConfig configForBusi =
                CacheConfigFactory.getCacheConfig(
                    MMGPUserDefCaption.class.getName() + "_busi",
                    false,
                    5 * 60 * 1000,
                    -1,
                    CacheType.MEMORY,
                    MemoryCacheLevel.SOFT);
        ICache cacheForBusi = CacheManager.getInstance().getCache(configForBusi);
        MMGPUserDefCaption.userdefitemMap = cacheForBusi.toMap();
    }

    /**
     * �������������
     *
     * @param defcode
     * @return
     */
    public String processDef(String paraKey) {
        String showname = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004288")/* @res "�Զ�����" */;
        String[] strs = paraKey.substring(1, paraKey.length()).split("#");
        if (strs == null || strs.length != 3) {
            return showname;
        }
        String mdFullName = strs[1];
        String prop = strs[2].replaceAll("[a-z]", "");
        UserdefitemVO[] itemVOs = null;
        if (MMGPUserDefCaption.userdefitemMap.get(mdFullName) != null) {
            itemVOs = MMGPUserDefCaption.userdefitemMap.get(mdFullName);
        } else {
            itemVOs = this.queryMarAsstDefitem(mdFullName);
            if (itemVOs != null) {
                MMGPUserDefCaption.userdefitemMap.put(mdFullName, itemVOs);
            }
        }

        showname = this.getDefName(Integer.parseInt(prop));

        for (int i = 0; itemVOs != null && i < itemVOs.length; i++) {
            if (itemVOs[i].getPropindex().equals(Integer.parseInt(prop))) {
                showname = itemVOs[i].getShowname();
            }
        }
        return showname;
    }

    /**
     * �Զ���������
     *
     * @param sequence
     * @return
     */
    private String getDefName(int sequence) {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0004288")/*@res "�Զ�����"*/ + sequence;
    }

    private UserdefitemVO[] queryMarAsstDefitem(String mdFullName) {
        UserdefitemVO[] defs = null;
        // ��ȡ�Զ�����
        IUserdefitemQryService service = NCLocator.getInstance().lookup(IUserdefitemQryService.class);
        try {
            defs = service.qeuryUserdefitemVOsByMDClassFullName(mdFullName, AppContext.getInstance().getPkGroup());
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return defs;
    }

}