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
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 *
 * @since �������� 2013-8-20
 * @author dongchx
 */
@SuppressWarnings("unchecked")
public class MMGPMarAssistantCaption {

    /**
     * �������Ա�������ֵ
     * <p>
     * ��Ҫ�滻����ĸ������Ա����Ǵ�1��10������ƽ̨�������Ҫ�滻�ؼ� �ĸ������Ա����Ǵ�6��15��������Ҫһ������ֵ
     */
    private static final int IPREFIX = 5;

    /** ���ϸ��������Զ����������� */
    private static final String MARASSISTANTRULECODE = "materialassistant";

    static Map<String, UserdefitemVO[]> userdefitemMap;

    static {
        // Ϊ�˼�����������ʹ����soft������ڴ滺�棬�����潻������
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
     * �����Զ������ѯ���������Զ�������������Ϣ���QCVO��
     */
    public String processDef(String paraKey) {
        String showname = NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0", "MarAssistantCaption-0000")/* �Զ��帨������ */;
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
     * ���ɸ�����������
     *
     * @param sequence
     * @return
     */
    private String getVfreeName(int sequence) {
        return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0067")/*@res "���ɸ�������"*/ + sequence;
    }

    private UserdefitemVO[] queryMarAsstDefitem(String pk_group) {
        UserdefitemVO[] defs = null;
        // ��ȡ�Զ�����
        IUserdefitemQryService service = NCLocator.getInstance().lookup(IUserdefitemQryService.class);
        try {
            defs = service.queryUserdefitemVOsByUserdefruleCode(MARASSISTANTRULECODE, pk_group);
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return defs;
    }

}