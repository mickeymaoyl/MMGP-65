package nc.vo.mmgp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import nc.vo.pubapp.pattern.pub.MapList;
/**
 * 
 * 扩展水平产品的MapList，增加一些功能
 * @param <K> 键的类型
 * @param <V> 值的类型
 * @since: 
 * 创建日期:Oct 9, 2014
 * @author:liwsh
 */
public class MMGPMapList<K, V> extends MapList<K, V> {
    
    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return this.toMap().isEmpty();
    }
    
    /**
     * 清空MAP
     */
    public void clear() {
        this.toMap().clear();
    }
    
    /**
     * 获取所有的值
     * @return map中所有的值
     */
    public List<V> values() {
        
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        
        Set<Entry<K, List<V>>> entrySet = this.toMap().entrySet();
        Iterator<Entry<K, List<V>>> iterator = entrySet.iterator();
        
        List<V> resultList = new ArrayList<V>();
        while (iterator.hasNext()) {
            resultList.addAll(iterator.next().getValue());
        }
        
        return resultList;
    }
}
