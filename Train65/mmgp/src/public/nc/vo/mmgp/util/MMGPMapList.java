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
 * ��չˮƽ��Ʒ��MapList������һЩ����
 * @param <K> ��������
 * @param <V> ֵ������
 * @since: 
 * ��������:Oct 9, 2014
 * @author:liwsh
 */
public class MMGPMapList<K, V> extends MapList<K, V> {
    
    /**
     * �Ƿ�Ϊ��
     */
    public boolean isEmpty() {
        return this.toMap().isEmpty();
    }
    
    /**
     * ���MAP
     */
    public void clear() {
        this.toMap().clear();
    }
    
    /**
     * ��ȡ���е�ֵ
     * @return map�����е�ֵ
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
