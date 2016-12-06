package nc.vo.mmgp.util;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.pubitf.org.cache.IOrgUnitPubService_C;
import nc.vo.org.util.OrgTypeManager;
import nc.vo.pub.BusinessException;

public class MMGPOrgUtil {
	
	public static String getOrgIDByVID(String pk_vid) {
		Map<String, String> map = OrgUnitPubService.getOrgIDSByVIDS(new String[] { pk_vid });
		return map.get(pk_vid);
	}
	
	/**
     * 方法功能描述：根据oid查询相应组织的最新vid。
     * 
     * @param pk_org
     * @return 最新vid。
     * @throws BusinessException
     * @since 6.0
     * @time 2010-7-20 下午03:02:06
     */
    public static String getNewVIDByOrgID(String pk_org) throws BusinessException {
        Map<String, String> omap =
                NCLocator.getInstance().lookup(IOrgUnitPubService_C.class).getNewVIDSByOrgIDS(new String[]{pk_org });
        return omap.get(pk_org);
    }

    /**
     * 方法功能描述：根据一组oid查询相应组织的最新vid。
     * 
     * @param pk_orgs
     * @return 组织的最新vid---MAP<OID,VID>
     * @throws BusinessException
     * @since 6.0
     * @time 2010-7-20 下午02:57:25
     */
    public static Map<String, String> getNewVIDSByOrgIDS(String[] pk_orgs) throws BusinessException {
        Map<String, String> omap =
                NCLocator.getInstance().lookup(IOrgUnitPubService_C.class).getNewVIDSByOrgIDS(pk_orgs);
        return omap;
    }

    /**
     * 判断某业务单元是否启用了工厂职能
     * 
     * @param pk_org
     *        业务单元
     * @return 是否启用工厂职能
     * @throws BusinessException
     *         业务异常
     */
    public static boolean orgEnableFactory(String pk_org) throws BusinessException {

        boolean isEnableFactory = OrgTypeManager.getInstance().isTypeOfByPk(pk_org, IOrgConst.FACTORYTYPE);
        return isEnableFactory;
        
        
    }

}
