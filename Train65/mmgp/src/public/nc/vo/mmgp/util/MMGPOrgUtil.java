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
     * ������������������oid��ѯ��Ӧ��֯������vid��
     * 
     * @param pk_org
     * @return ����vid��
     * @throws BusinessException
     * @since 6.0
     * @time 2010-7-20 ����03:02:06
     */
    public static String getNewVIDByOrgID(String pk_org) throws BusinessException {
        Map<String, String> omap =
                NCLocator.getInstance().lookup(IOrgUnitPubService_C.class).getNewVIDSByOrgIDS(new String[]{pk_org });
        return omap.get(pk_org);
    }

    /**
     * ������������������һ��oid��ѯ��Ӧ��֯������vid��
     * 
     * @param pk_orgs
     * @return ��֯������vid---MAP<OID,VID>
     * @throws BusinessException
     * @since 6.0
     * @time 2010-7-20 ����02:57:25
     */
    public static Map<String, String> getNewVIDSByOrgIDS(String[] pk_orgs) throws BusinessException {
        Map<String, String> omap =
                NCLocator.getInstance().lookup(IOrgUnitPubService_C.class).getNewVIDSByOrgIDS(pk_orgs);
        return omap;
    }

    /**
     * �ж�ĳҵ��Ԫ�Ƿ������˹���ְ��
     * 
     * @param pk_org
     *        ҵ��Ԫ
     * @return �Ƿ����ù���ְ��
     * @throws BusinessException
     *         ҵ���쳣
     */
    public static boolean orgEnableFactory(String pk_org) throws BusinessException {

        boolean isEnableFactory = OrgTypeManager.getInstance().isTypeOfByPk(pk_org, IOrgConst.FACTORYTYPE);
        return isEnableFactory;
        
        
    }

}
