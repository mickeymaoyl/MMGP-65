package nc.impl.mmgp.uif2.rule.event.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMMapUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 只支持单表，未考虑元数据扩展及主子表
 * </p>
 * 
 * @since 创建日期 Nov 5, 2013
 * @author wangweir
 */
@SuppressWarnings("rawtypes")
public class CacheEventRule implements IRule {

    private IFireCacheEvent fireEvent;

    /**
     * 
     */
    public CacheEventRule(IFireCacheEvent fireEvent) {
        this.fireEvent = fireEvent;
    }

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.IRule#process(E[])
     */
    @Override
    public void process(Object[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }
        // 目前只支持SuperVO
        // 只支持单表，未考虑元数据扩展及主子表
        if (!(vos[0] instanceof SuperVO)) {
            ExceptionUtils.unSupported();
        }

        Map<String, List<String>> tableToPk = null;
        try {
            tableToPk = getTableNames(vos);
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }

        if (MMMapUtil.isEmpty(tableToPk)) {
            return;
        }

        Map<String, IBean> tableToID = this.getTableToID(vos);

        for (Entry<String, List<String>> oneEntry : tableToPk.entrySet()) {
            fireCahceEvent(tableToID.get(oneEntry.getKey()), oneEntry.getKey(), oneEntry.getValue());
        }
    }

    /**
     * @param vos
     */
    @SuppressWarnings("deprecation")
    private Map<String, IBean> getTableToID(Object[] vos) {
        IBean bean;
        try {
            bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(vos[0].getClass().getName());
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
            return Collections.emptyMap();
        }
        Map<String, IBean> tableToID = new HashMap<String, IBean>();
        tableToID.put(((SuperVO) vos[0]).getTableName(), bean);
        return tableToID;
    }

    protected void fireCahceEvent(IBean bean,
                                  String tableName,
                                  List<String> pks) {
        this.fireEvent.fireCahceEvent(bean, tableName, pks);
    }

    @SuppressWarnings("deprecation")
    protected Map<String, List<String>> getTableNames(Object[] vos) throws MetaDataException {
        SuperVO[] datas = (SuperVO[]) vos;
        Map<String, List<String>> tableToPk = new HashMap<String, List<String>>();
        for (SuperVO data : datas) {
            List<String> pks = tableToPk.get(data.getTableName());
            if (MMCollectionUtil.isEmpty(pks)) {
                pks = new ArrayList<String>();
                tableToPk.put(data.getTableName(), pks);
            }
            pks.add(data.getPrimaryKey());
        }
        return tableToPk;
    }
}
