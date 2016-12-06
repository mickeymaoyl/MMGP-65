package nc.vo.mmgp.uif2;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * 一主多子单据元数据的创建工厂，保证元数据只会被创建一次。节省创建时间和内存开销
 * 
 * @since 6.0
 * @version 2008-7-24 下午04:30:42
 * @author
 */
public class MMGPBillMetaFactory {
    /**
     * 一主多子单据元数据的创建工厂实例
     */
    private static MMGPBillMetaFactory instance = new MMGPBillMetaFactory();

    /**
     * 单据元数据全路径名和单据元数据的映射
     */
    private Map<String, IBillMeta> nameIndex = new HashMap<String, IBillMeta>();

    private MMGPBillMetaFactory() {
        // 缺省构造方法
    }

    /**
     * 一主多子单据元数据的创建工厂方法
     * 
     * @return 一主多子单据元数据的创建工厂实例
     */
    public static MMGPBillMetaFactory getInstance() {
        return MMGPBillMetaFactory.instance;
    }

    /**
     * 根据单据元数据全路径名获取单据元数据
     * 
     * @param mainEntityName
     *        单据元数据全路径名
     * @return 单据元数据
     */
    public IBillMeta getBillMeta(String mainEntityName) {
        IBillMeta billMeta = null;
        synchronized (MMGPBillMetaFactory.instance) {
            billMeta = this.nameIndex.get(mainEntityName);
            if (billMeta == null) {
                billMeta = new MMGPAggBillMeta(mainEntityName);
                this.nameIndex.put(mainEntityName, billMeta);
            }
        }
        return billMeta;
    }
}
