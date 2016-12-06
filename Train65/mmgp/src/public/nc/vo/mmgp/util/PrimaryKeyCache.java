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
 * <b>主键缓冲池</b>
 * <p>用于批量获取主键，优化IO性能，用于批量操作，标识</p>
 * @author gaotx
 *
 */
public class PrimaryKeyCache {
    /**
     * 缓存中数据大小
     */
    private static final int CACHE_SIZE = 100;
    /**
     * 缓存集团PK与集团VO,避免循环调用,用hashtable保证线程安全吧
     */
    private Map<String,GroupVO> groupCodeCache = new Hashtable<String,GroupVO>();

    /**
     * PoBillCodeCache
     */
    private static PrimaryKeyCache instance = new PrimaryKeyCache();

    /**
     * 线程局部 (thread-local) 变量
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
     * 获取PrimaryKeyCache
     * 
     * @return PrimaryKeyCache
     */
    public static PrimaryKeyCache getInstance() {
        return instance;
    }
    
    /**
     * 清除缓存
     */
    public void cleanCache(){
        this.groupCodeCache.clear();
    }
    
    /**
     * 根据GroupPK获取PK
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
     * 获取PK
     * @param groupNo 集团NO，不是code，害死我了
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

        // 返回最后一个，提高效率
        return cache.remove(cache.size() - 1);
    }

}
