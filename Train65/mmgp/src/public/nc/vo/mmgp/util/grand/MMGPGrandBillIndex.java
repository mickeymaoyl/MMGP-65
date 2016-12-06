package nc.vo.mmgp.util.grand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.tool.BillIndex;
import nc.vo.pubapp.pattern.pub.MapList;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙单据工具.
 * </p>
 * 
 * @see BillIndex
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillIndex {

    /**
     * 实体元数据、实体主键、实体的映射关系
     */
    private Map<IVOMeta, Map<String, ISuperVO>> index = new HashMap<IVOMeta, Map<String, ISuperVO>>();

    /**
     * 一主多子单据的各种索引结构构造函数
     * 
     * @param bills
     *        一主多子单据
     */
    public MMGPGrandBillIndex(IBill[] bills) {
        this.init(bills);
    }

    private Map<String, ISuperVO> get(IVOMeta voMeta) {
        if (this.index.containsKey(voMeta)) {
            return this.index.get(voMeta);
        }
        Map<String, ISuperVO> keyIndex = new HashMap<String, ISuperVO>();
        this.index.put(voMeta, keyIndex);
        return keyIndex;
    }

    /**
     * 根据实体元数据、实体主键获取实体
     * 
     * @param voMeta
     *        实体元数据
     * @param key
     *        实体主键
     * @return 实体
     */
    public ISuperVO get(IVOMeta voMeta,
                        String key) {
        if (this.index.containsKey(voMeta)) {
            return this.index.get(voMeta).get(key);
        }
        return null;
    }

    private void init(IBill[] bills) {
        MMGPGrandBillHelper helper = new MMGPGrandBillHelper(bills);
        List<ISuperVO> parentList = helper.getParentList();
        this.init(parentList);

        MapList<IVOMeta, ISuperVO> itemIndex = helper.getItemIndex();
        Set<Entry<IVOMeta, List<ISuperVO>>> entrySet = itemIndex.entrySet();
        for (Entry<IVOMeta, List<ISuperVO>> entry : entrySet) {
            this.init(entry.getValue());
        }
    }

    private void init(List<ISuperVO> list) {
        if (list.size() == 0) {
            return;
        }
        IVOMeta voMeta = list.get(0).getMetaData();
        Map<String, ISuperVO> keyIndex = this.get(voMeta);
        for (ISuperVO vo : list) {
            String key = vo.getPrimaryKey();
            keyIndex.put(key, vo);
        }
    }
}
