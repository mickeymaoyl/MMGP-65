package nc.impl.mmgp.uif2.template.grand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.impl.pubapp.pattern.data.vo.VODelete;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.data.vo.VOUpdateTS;
import nc.impl.pubapp.pattern.rule.template.ICompareOperator;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBillHelper;
import nc.vo.mmgp.util.grand.MMGPGrandBillIndex;
import nc.vo.mmgp.util.grand.MMGPGrandBusiEntityUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 21, 2013
 * @author wangweir
 */
public class MMGPGrandBillUpdateOperator<E extends IBill> implements ICompareOperator<E> {

    /*
     * (non-Javadoc)
     * @see nc.impl.pubapp.pattern.rule.template.IOperator#operate(E[])
     */
    public E[] operate(E[] bills,
                       E[] originBills) {
        /* Jun 19, 2013 wangweir 如果为空，不持久化 Begin */
        if (MMArrayUtil.isEmpty(bills)) {
            return bills;
        }
        /* Jun 19, 2013 wangweir End */

        this.persistent(bills, originBills);
        return this.ognizeBill(bills);
    }

    protected void persistent(E[] bills,
                              E[] originBills) {
        Map<IVOMeta, List<ISuperVO>> originIndex = new HashMap<IVOMeta, List<ISuperVO>>();
        Map<IVOMeta, List<ISuperVO>> deleteIndex = new HashMap<IVOMeta, List<ISuperVO>>();
        Map<IVOMeta, List<ISuperVO>> newIndex = new HashMap<IVOMeta, List<ISuperVO>>();
        Map<IVOMeta, List<ISuperVO>> updateIndex = new HashMap<IVOMeta, List<ISuperVO>>();
        MMGPGrandBillIndex billIndex = new MMGPGrandBillIndex(originBills);
        MMGPGrandBillHelper helper = new MMGPGrandBillHelper(bills);
        List<ISuperVO> parentList = helper.getParentList();
        this.process(parentList, billIndex, originIndex, deleteIndex, newIndex, updateIndex);

        Map<IVOMeta, List<ISuperVO>> itemIndex = helper.getItemIndex().toMap();
        for (List<ISuperVO> list : itemIndex.values()) {
            this.process(list, billIndex, originIndex, deleteIndex, newIndex, updateIndex);
        }
        this.persistent(originIndex, deleteIndex, newIndex, updateIndex);

        // 对于表体有改动的表头VO，需要刷新数据库ts。供应链规范，不要问我为什么
        this.updateParentTS(parentList, updateIndex);
    }

    protected void process(List<ISuperVO> list,
                           MMGPGrandBillIndex billIndex,
                           Map<IVOMeta, List<ISuperVO>> originIndex,
                           Map<IVOMeta, List<ISuperVO>> deleteIndex,
                           Map<IVOMeta, List<ISuperVO>> newIndex,
                           Map<IVOMeta, List<ISuperVO>> updateIndex) {
        for (ISuperVO vo : list) {
            this.process(vo, billIndex, originIndex, deleteIndex, newIndex, updateIndex);
        }
    }

    private void process(ISuperVO vo,
                         MMGPGrandBillIndex billIndex,
                         Map<IVOMeta, List<ISuperVO>> originIndex,
                         Map<IVOMeta, List<ISuperVO>> deleteIndex,
                         Map<IVOMeta, List<ISuperVO>> newIndex,
                         Map<IVOMeta, List<ISuperVO>> updateIndex) {
        IVOMeta voMeta = vo.getMetaData();

        int status = vo.getStatus();
        if (status == VOStatus.NEW) {
            List<ISuperVO> list = this.get(voMeta, newIndex);
            list.add(vo);
        } else if (status == VOStatus.UPDATED) {
            List<ISuperVO> updateList = this.get(voMeta, updateIndex);
            updateList.add(vo);

            ISuperVO originVO = billIndex.get(vo.getMetaData(), vo.getPrimaryKey());
            List<ISuperVO> originList = this.get(voMeta, originIndex);
            originList.add(originVO);
        } else if (status == VOStatus.DELETED) {
            List<ISuperVO> list = this.get(voMeta, deleteIndex);
            list.add(vo);
        }
    }

    protected void persistent(Map<IVOMeta, List<ISuperVO>> originIndex,
                              Map<IVOMeta, List<ISuperVO>> deleteIndex,
                              Map<IVOMeta, List<ISuperVO>> newIndex,
                              Map<IVOMeta, List<ISuperVO>> updateIndex) {
        for (List<ISuperVO> list : deleteIndex.values()) {
            this.deleteVO(list);
        }
        for (List<ISuperVO> list : newIndex.values()) {
            this.insertVO(list);
        }
        for (Entry<IVOMeta, List<ISuperVO>> entry : updateIndex.entrySet()) {
            List<ISuperVO> list = entry.getValue();
            List<ISuperVO> originList = originIndex.get(entry.getKey());
            this.updateVO(list, originList);
        }
    }

    private void updateParentTS(List<ISuperVO> list,
                                Map<IVOMeta, List<ISuperVO>> updateIndex) {
        int size = list.size();
        if (size == 0) {
            return;
        }
        ISuperVO[] vos = null;
        IVOMeta meta = list.get(0).getMetaData();
        List<ISuperVO> updatedList = updateIndex.get(meta);
        if (updatedList != null) {

            Map<String, ISuperVO> index = new HashMap<String, ISuperVO>();
            for (ISuperVO vo : updatedList) {
                index.put(vo.getPrimaryKey(), vo);
            }
            List<ISuperVO> updateTSList = new ArrayList<ISuperVO>();
            for (ISuperVO vo : list) {
                if (index.containsKey(vo.getPrimaryKey())) {
                    continue;
                }
                updateTSList.add(vo);
            }
            size = updateTSList.size();
            if (size == 0) {
                return;
            }
            vos = new ISuperVO[size];
            vos = updateTSList.toArray(vos);
        } else {
            vos = new ISuperVO[size];
            vos = list.toArray(vos);
        }
        VOUpdateTS<ISuperVO> bo = new VOUpdateTS<ISuperVO>();
        bo.update(vos);
    }

    private List<ISuperVO> get(IVOMeta voMeta,
                               Map<IVOMeta, List<ISuperVO>> index) {
        if (index.containsKey(voMeta)) {
            return index.get(voMeta);
        }
        List<ISuperVO> list = new ArrayList<ISuperVO>();
        index.put(voMeta, list);
        return list;
    }

    private void insertVO(List<ISuperVO> list) {
        VOInsert<ISuperVO> bo = new VOInsert<ISuperVO>();
        int length = list.size();
        if (length > 0) {
            ISuperVO[] vos = new ISuperVO[length];
            vos = list.toArray(vos);
            bo.insert(vos);
        }
    }

    private void updateVO(List<ISuperVO> list,
                          List<ISuperVO> originList) {
        MMGPGrandVOUpdate<ISuperVO> bo = new MMGPGrandVOUpdate<ISuperVO>();
        int length = list.size();
        if (length > 0) {
            ISuperVO[] vos = new ISuperVO[length];
            vos = list.toArray(vos);

            ISuperVO[] originVOs = new ISuperVO[length];
            originVOs = originList.toArray(originVOs);

            bo.update(vos, originVOs);
        }
    }

    private void deleteVO(List<ISuperVO> list) {
        VODelete<ISuperVO> bo = new VODelete<ISuperVO>();
        int length = list.size();
        if (length > 0) {
            ISuperVO[] vos = new ISuperVO[length];
            vos = list.toArray(vos);
            bo.delete(vos);
        }
    }

    private E[] ognizeBill(E[] bills) {
        for (E bill : bills) {
            this.ognizeBill(bill);
        }
        return bills;
    }

    private void ognizeBill(E bill) {
        IBillMeta billMeta = bill.getMetaData();
        IVOMeta[] childMetas = billMeta.getChildren();
        for (IVOMeta childMeta : childMetas) {
            ISuperVO[] vos = bill.getChildren(childMeta);
            if (vos == null || vos.length == 0) {
                continue;
            }
            this.orgnize(bill, vos);
        }
    }

    private void orgnize(E bill,
                         ISuperVO[] vos) {
        if (vos == null || vos.length == 0) {
            return;
        }
        List<ISuperVO> list = new ArrayList<ISuperVO>();
        for (ISuperVO vo : vos) {
            if (vo.getStatus() == VOStatus.DELETED) {
                continue;
            }
            list.add(vo);
        }
        ListToArrayTool<ISuperVO> tool = new ListToArrayTool<ISuperVO>();
        if (list.size() == 0) {
            // 当前子表行已经全部被删除掉
            ISuperVO[] children = Constructor.construct(vos[0].getClass(), 0);
            bill.setChildren(vos[0].getMetaData(), children);
            return;
        }
        ISuperVO[] tempVOs = tool.convertToArray(list);
        bill.setChildren(vos[0].getMetaData(), tempVOs);
        // 组织孙表数据.
        this.orgnizeGCData(tempVOs);
    }

    private void orgnizeGCData(ISuperVO[] vos) {
        if (vos == null || vos.length == 0) {
            return;
        }
        for (ISuperVO voTemp : vos) {
            this.orgnizeGCData(voTemp);
        }
    }

    /**
     * 组织孙表数据.
     * 
     * @param parentVO
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    private void orgnizeGCData(ISuperVO parentVO) {
        Set<String> childAttrs = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(parentVO);
        if (childAttrs.isEmpty()) {
            return;
        }
        for (String propertyName : childAttrs) {
            SuperVO[] childVOs = (SuperVO[]) BeanHelper.getProperty(parentVO, propertyName);
            if (childVOs != null && childVOs.length != 0) {
                List voList = new ArrayList();
                for (SuperVO childVOTemp : childVOs) {
                    if (childVOTemp.getStatus() != VOStatus.DELETED) {
                        voList.add(childVOTemp);
                    }
                }

                Object[] setVal = MMArrayUtil.toArray(voList, childVOs[0].getClass());
                ISuperVO[] newVOs = MMArrayUtil.toArray(voList, ISuperVO.class);
                BeanHelper.setProperty(parentVO, propertyName, setVal);
                this.orgnizeGCData(newVOs);
            }
        }
    }

}
