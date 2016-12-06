package nc.impl.mmgp;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pub.smart.QueryBillServiceImpl;
import nc.itf.mmgp.IMMGPGrandQueryBillService;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBillQuery;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jun 27, 2013
 * @author wangweir
 */
public class MMGPGrandQueryBillServiceImpl extends QueryBillServiceImpl implements IMMGPGrandQueryBillService {

    /*
     * (non-Javadoc)
     * @see nc.itf.pubapp.pub.smart.IQueryBillService#queryBill(java.lang.Class, java.lang.String, java.lang.String)
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    @Override
    public Object queryBill(Class< ? > clazz,
                            String primaryKey,
                            String nodeCode) {
        Object queryResult = super.queryBill(clazz, primaryKey, nodeCode);

        if (!AbstractBill.class.isAssignableFrom(queryResult.getClass())) {
            return queryResult;
        }

        AbstractBill bill = (AbstractBill) queryResult;
        String pk = bill.getPrimaryKey();
        MMGPGrandBillQuery query = new MMGPGrandBillQuery(queryResult.getClass());
        return query.query(new String[]{pk });
    }

    /*
     * (non-Javadoc)
     * @see nc.itf.pubapp.pub.smart.IQueryBillService#queryBills(java.lang.Class, java.lang.String[], java.lang.String)
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Object[] queryBills(Class< ? > clazz,
                               String[] pks,
                               String nodecode) {
        Object[] queryResults = super.queryBills(clazz, pks, nodecode);
        if (MMArrayUtil.isEmpty(queryResults) || !AbstractBill.class.isAssignableFrom(queryResults[0].getClass())) {
            return queryResults;
        }

        List<String> ids = new ArrayList<String>();
        for (Object bill : queryResults) {
            ids.add(((AbstractBill) bill).getPrimaryKey());
        }

        MMGPGrandBillQuery query = new MMGPGrandBillQuery(queryResults[0].getClass());
        return query.query(ids.toArray(new String[0]));
    }

}
