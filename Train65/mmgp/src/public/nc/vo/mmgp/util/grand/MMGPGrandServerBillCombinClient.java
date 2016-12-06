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
 * <b> ��Ҫ�������� </b>
 * <p>
 * ������ϲ�.
 * </p>
 * 
 * @See ServerBillCombinClient
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandServerBillCombinClient<E extends IBill> {

    private void combine(E bill,
                         E clientBill) {
        IBillMeta billMeta = bill.getMetaData();
        IVOMeta parentMeta = billMeta.getParent();
        // ��ʱ�ϲ���ͷ����Ҫ��GCVOTool.
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
                // ���ϲ�.
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
            // ǰ̨�����̬������
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
        // û�иı��VO
        if (changedIndex.size() == 0 && newList.size() == 0) {
            return false;
        }
        ISuperVO[] childrenVO = bill.getChildren(voMeta);
        // ԭʼ������û�е�ǰ��ʵ�壬��ǰ̨������������Щ��ʵ��
        if (childrenVO == null) {
            bill.setChildren(voMeta, clientBill.getChildren(voMeta));
            flag = false;
        }
        return flag;
    }

    /**
     * ��antherBill�е���Ϣ���Ƶ�bill�У�ֻ��Կ����л����ɳ������ֶ�
     * 
     * @param bills
     *        ԭʼȫ����
     * @param clientBills
     *        ���ݿ���
     */
    public void combine(E[] bills,
                        E[] clientBills) {
        int length = bills.length;
        for (int i = 0; i < length; i++) {
            this.combine(bills[i], clientBills[i]);
        }
    }

}
