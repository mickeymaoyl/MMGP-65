package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.pub.BeanHelper;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.model.tool.VOTool;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙合并.
 * </p>
 * 
 * @See ServerBillCombinClient
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandServerBillCombinClient<E extends IBill> {

    private void combine(E bill,
                         E clientBill) {
        IBillMeta billMeta = bill.getMetaData();
        IVOMeta parentMeta = billMeta.getParent();
        // 此时合并表头不需要用GCVOTool.
        VOTool tool = new VOTool();
        if (parentMeta != null) {
            tool.combine(bill.getParent(), clientBill.getParent());
        }
        IVOMeta[] children = billMeta.getChildren();
        if (children == null) {
            return;
        }
        for (IVOMeta child : children) {
            this.combine(bill, clientBill, child);
        }
    }

    private void combine(E bill,
                         E clientBill,
                         IVOMeta voMeta) {
        ISuperVO[] childrenVO = clientBill.getChildren(voMeta);
        if (childrenVO == null) {
            return;
        }
        Map<String, ISuperVO> changedIndex = new HashMap<String, ISuperVO>();
        List<ISuperVO> newList = new ArrayList<ISuperVO>();
        this.orgnizeData(childrenVO, changedIndex, newList);
        boolean flag = this.check(changedIndex, newList, bill, clientBill, voMeta);
        if (!flag) {
            return;
        }
        MMGPGrandVOTool tool = new MMGPGrandVOTool();
        List<ISuperVO> oldList = new ArrayList<ISuperVO>();
        childrenVO = bill.getChildren(voMeta);
        for (ISuperVO child : childrenVO) {
            String pk = child.getPrimaryKey();
            ISuperVO vo = changedIndex.get(pk);
            if (vo != null) {
                tool.combine(child, vo);
                // 孙表合并.
                this.combinGrandChildVO(child, vo);
            }
            oldList.add(child);
        }
        oldList.addAll(newList);
        ListToArrayTool<ISuperVO> arrayTool = new ListToArrayTool<ISuperVO>();
        ISuperVO[] vos = arrayTool.convertToArray(oldList);
        bill.setChildren(voMeta, vos);
    }

    private void combinGrandChildVO(ISuperVO serverVO,
                                    ISuperVO clientVO) {
        Set<String> childAttrNameSet = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(serverVO);
        MMGPGrandVOTool tool = new MMGPGrandVOTool();
        for (String attrTemp : childAttrNameSet) {
            SuperVO[] serverChildVO = (SuperVO[]) BeanHelper.getProperty(serverVO, attrTemp);
            SuperVO[] clientChildVO = (SuperVO[]) BeanHelper.getProperty(clientVO, attrTemp);
            if (clientChildVO == null && serverChildVO == null) {
                continue;
            }
            Map<String, ISuperVO> changedIndex = new HashMap<String, ISuperVO>();
            List<ISuperVO> newList = new ArrayList<ISuperVO>();
            List<ISuperVO> allNewList = new ArrayList<ISuperVO>();
            this.orgnizeData(clientChildVO, changedIndex, newList);
            if (serverChildVO != null) {
                for (SuperVO serverVOTemp : serverChildVO) {
                    String primaryKey = serverVOTemp.getPrimaryKey();
                    SuperVO clientVOTemp = (SuperVO) changedIndex.get(primaryKey);
                    if (clientVOTemp != null) {
                        tool.combine(serverVOTemp, clientVOTemp);
                    }
                    allNewList.add(serverVOTemp);
                    this.combinGrandChildVO(serverVOTemp, clientVOTemp);
                }
            }
            allNewList.addAll(newList);
            if (allNewList.size() != 0) {
                BeanHelper.setProperty(
                    serverVO,
                    attrTemp,
                    MMGPGrandVOTool.transfer(allNewList.get(0).getClass(), allNewList.toArray(new ISuperVO[0])));
            }

        }
    }

    private void orgnizeData(ISuperVO[] childrenVO,
                             Map<String, ISuperVO> changedIndex,
                             List<ISuperVO> newList) {
        if (childrenVO == null) {
            return;
        }
        for (ISuperVO vo : childrenVO) {
            // 前台界面变态，鄙视
            if (vo == null) {
                continue;
            }
            String pk = vo.getPrimaryKey();
            if (pk != null && vo.getStatus() != VOStatus.NEW) {
                changedIndex.put(pk, vo);
            } else {
                newList.add(vo);
            }
        }
    }

    private boolean check(Map<String, ISuperVO> changedIndex,
                          List<ISuperVO> newList,
                          E bill,
                          E clientBill,
                          IVOMeta voMeta) {
        boolean flag = true;
        // 没有改变的VO
        if (changedIndex.size() == 0 && newList.size() == 0) {
            return false;
        }
        ISuperVO[] childrenVO = bill.getChildren(voMeta);
        // 原始单据上没有当前子实体，而前台界面又新增了些子实体
        if (childrenVO == null) {
            bill.setChildren(voMeta, clientBill.getChildren(voMeta));
            flag = false;
        }
        return flag;
    }

    /**
     * 将antherBill中的信息复制到bill中，只针对可序列化、可持续化字段
     * 
     * @param bills
     *        原始全单据
     * @param clientBills
     *        单据快照
     */
    public void combine(E[] bills,
                        E[] clientBills) {
        int length = bills.length;
        for (int i = 0; i < length; i++) {
            this.combine(bills[i], clientBills[i]);
        }
    }

}
