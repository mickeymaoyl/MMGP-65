package nc.vo.mmgp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.org.IGroupQryService;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.vo.org.GroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/**
 * <b>���������</b>
 * <p>����������ȡ�������Ż�IO���ܣ�����������������ʶ</p>
 * @author gaotx
 *
 */
public class PrimaryKeyCache {
    /**
     * ���������ݴ�С
     */
    private static final int CACHE_SIZE = 100;
    /**
     * ���漯��PK�뼯��VO,����ѭ������,��hashtable��֤�̰߳�ȫ��
     */
    private Map<String,GroupVO> groupCodeCache = new Hashtable<String,GroupVO>();

    /**
     * PoBillCodeCache
     */
    private static PrimaryKeyCache instance = new PrimaryKeyCache();

    /**
     * �ֲ߳̾� (thread-local) ����
     */
    private ThreadLocal<List<String>> local;

    private PrimaryKeyCache() {
        this.local = new ThreadLocal<List<String>>() {
            @Override
            protected List<String> initialValue() {
                return new ArrayList<String>();
            }
        };
    }

    /**
     * ��ȡPrimaryKeyCache
     * 
     * @return PrimaryKeyCache
     */
    public static PrimaryKeyCache getInstance() {
        return instance;
    }
    
    /**
     * �������
     */
    public void cleanCache(){
        this.groupCodeCache.clear();
    }
    
    /**
     * ����GroupPK��ȡPK
     * @param pk_group
     * @return
     * @throws BusinessException
     */
    public String getPrimaryKeyFormCahceByGrpPK(String pk_group){
        if(!MMStringUtil.isEmpty(pk_group)){
            try{
                GroupVO grpVO = null;
                if(this.groupCodeCache.containsKey(pk_group)){
                    grpVO = this.groupCodeCache.get(pk_group);
                }else{
                    IGroupQryService grpSrv = NCLocator.getInstance().lookup(IGroupQryService.class);
                    grpVO = grpSrv.queryGroupVOByGroupID(pk_group);
                    this.groupCodeCache.put(pk_group, grpVO);
                }
                return this.getPrimaryKeyFromCache(grpVO.getGroupno());
            }catch(Exception e){
                ExceptionUtils.wrappException(e);
            }
        }
        return this.getPrimaryKeyFromCache(null);
        
    }

    /**
     * ��ȡPK
     * @param groupNo ����NO������code����������
     * @return
     */
    public String getPrimaryKeyFromCache(String groupNo) {
        List<String> cache = this.local.get();
        if (MMCollectionUtil.isEmpty(cache)) {
            String groupNumber = groupNo;
            if (MMStringUtil.isEmpty(groupNumber)) {
                groupNumber = InvocationInfoProxy.getInstance().getGroupNumber();
            }
            SequenceGenerator gen = new SequenceGenerator();
            String[] pkArray = gen.generate(groupNumber, CACHE_SIZE);
            cache.addAll(Arrays.asList(pkArray));
        }

        // �������һ�������Ч��
        return cache.remove(cache.size() - 1);
    }

}
