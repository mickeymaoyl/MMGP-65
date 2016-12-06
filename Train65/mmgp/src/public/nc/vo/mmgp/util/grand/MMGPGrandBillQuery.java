package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * <b>主子孙单据分层查询. </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillQuery<E extends IBill> {

    private Class<E> billClass = null;

    public MMGPGrandBillQuery(Class<E> billClass) {
        this.billClass = billClass;
    }

    public E[] query(String[] ids) {
        if (this.isNull(ids)) {
            return null;
        }
        E[] bills = this.qryBills(ids);
        if (this.isNull(bills)) {
            return null;
        }
        this.setGCVOs(bills);
        return bills;
    }

    private <T> boolean isNull(T[] datas) {
        if (datas == null || datas.length == 0) {
            return true;
        }
        return false;
    }

    private E[] qryBills(String[] ids) {
        BillQuery<E> query = new BillQuery<E>(this.billClass);
        return query.query(ids);
    }

    private void setGCVOs(E[] bills) {
        List<IAttribute> childAttrList = this.getChildAttr(bills[0]);
        for (IAttribute childAttr : childAttrList) {
            ISuperVO[] childVOs = this.getChildVOList(childAttr, bills);
            if (childVOs == null || childVOs.length == 0) {
                continue;
            }
            this.setGCVOs(childVOs);
        }
    }

    private ISuperVO[] getChildVOList(IAttribute childAttr,
                                      E[] bills) {
        List<ISuperVO> childVOList = new ArrayList<ISuperVO>();
        for (E bill : bills) {
            ISuperVO[] childVOs = (ISuperVO[]) childAttr.getAccessStrategy().getValue(bill, childAttr);
            childVOList.addAll(Arrays.asList(childVOs));
        }
        return childVOList.toArray(new ISuperVO[0]);
    }

    private void setGCVOs(ISuperVO[] parVOs) {
        List<IAttribute> childAttrList = this.getChildAttr(parVOs[0]);
        this.setGCVOs(childAttrList, parVOs);
    }

    private void setGCVOs(List<IAttribute> grandAttrList,
                          ISuperVO[] parVOs) {
        if (grandAttrList.isEmpty()) {
            return;
        }
        for (IAttribute grandAttr : grandAttrList) {
            ISuperVO[] grandVOs = this.qryGrandVOs(grandAttr, parVOs);
            if (grandVOs == null || grandVOs.length == 0) {
                continue;
            }
            this.setChildVOs(grandAttr, grandVOs, parVOs);
            List<IAttribute> curChildAttrList = this.getChildAttr(grandVOs[0]);
            this.setGCVOs(curChildAttrList, grandVOs);
        }
    }

    private List<IAttribute> getChildAttr(ISuperVO vo) {
        return MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(vo);
    }

    private List<IAttribute> getChildAttr(E bill) {
        ISuperVO parVO = bill.getParent();
        return this.getChildAttr(parVO);
    }

    @SuppressWarnings("unchecked")
    private ISuperVO[] qryGrandVOs(IAttribute childAttr,
                                   ISuperVO[] parVOs) {
        try {
            String qrySql = this.getQrySql(childAttr, parVOs);
            IBean bean = childAttr.getAssociation().getEndBean();
            Class< ? extends ISuperVO> childVOClass =
                    (Class< ? extends ISuperVO>) Class.forName(bean.getFullClassName());
            VOQuery<ISuperVO> query = new VOQuery<ISuperVO>((Class<ISuperVO>) childVOClass);
            return query.queryWithWhereKeyWord(qrySql, null);
        } catch (ClassNotFoundException ex) {
            ExceptionUtils.wrappException(ex);
        }
        return null;
    }

    private void setChildVOs(IAttribute grandAttr,
                             ISuperVO[] grandVOs,
                             ISuperVO[] parVOs) {
        Map<String, List<ISuperVO>> parChildMap = this.getParChildMap(grandAttr, grandVOs);
        for (ISuperVO parVO : parVOs) {
            String parId = parVO.getPrimaryKey();
            List<ISuperVO> childVOList = parChildMap.get(parId);
            if (childVOList == null || childVOList.isEmpty()) {
                continue;
            }
            grandAttr.getAccessStrategy().setValue(
                parVO,
                grandAttr,
                childVOList.toArray(Constructor.declareArray(childVOList.get(0).getClass(), childVOList.size())));
        }
    }

    private Map<String, List<ISuperVO>> getParChildMap(IAttribute childAttr,
                                                       ISuperVO[] grandVOs) {
        Map<String, List<ISuperVO>> parChildMap = new HashMap<String, List<ISuperVO>>();
        String field = childAttr.getColumn().getName();
        for (ISuperVO grandVO : grandVOs) {
            String parId = (String) grandVO.getAttributeValue(field);
            if (!parChildMap.containsKey(parId)) {
                parChildMap.put(parId, new ArrayList<ISuperVO>());
            }
            parChildMap.get(parId).add(grandVO);
        }
        return parChildMap;
    }

    private String getQrySql(IAttribute childAttr,
                             ISuperVO[] parVOs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" where ");
        Set<String> idList = this.getIdList(parVOs);
        String field = childAttr.getColumn().getName();
        sql.append(this.getInSql(field, idList));
        return sql.toString();
    }

    private String getInSql(String filedName,
                            Set<String> idList) {
        return new IDExQueryBuilder("MMGPGR").buildAnotherSQL(filedName, idList.toArray(new String[0]));
    }

    private Set<String> getIdList(ISuperVO[] vos) {
        Set<String> idList = new HashSet<String>();
        for (ISuperVO vo : vos) {
            idList.add(vo.getPrimaryKey());
        }
        return idList;
    }
}
