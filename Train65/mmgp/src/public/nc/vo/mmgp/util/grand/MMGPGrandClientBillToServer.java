package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.md.model.IAttribute;
import nc.md.model.IBusinessEntity;
import nc.md.model.access.javamap.BeanStyleEnum;
import nc.mddb.constant.ElementConstant;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;
import nc.vo.pubapp.pattern.model.tool.VOTool;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ������ǰ̨VO����������.
 * </p>
 * 
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandClientBillToServer<E extends IBill> {

    /**
     * ����ԭʼ���ݺ����ڵĵ��ݹ����޸���ͼ���ա�<br>
     * �ı�/δ�ı���ж������ÿ��յ�������ʱ������޸ĵ�VO�ὫVO��״̬����Ϊ����״̬
     * 
     * @param originBills
     *        ԭʼ����
     * @param bills
     *        ԭʼ����
     * @return �޸���ͼ����
     */
    public E[] construct(E[] originBills,
                         E[] bills) {
        int length = bills.length;
        E[] newBills = this.createBills(bills);
        for (int i = 0; i < length; i++) {
            this.constructUpdate(originBills[i], bills[i], newBills[i]);
        }
        return newBills;
    }

    /**
     * ����ԭʼ���ݹ����޸ı����ͼ���ա�<br>
     * δ�ı���ж������ÿ��յ�������ʱ�����
     * 
     * @param originBills
     *        ԭʼ����
     * @return �޸���ͼ����
     */
    public E[] construct(E[] originBills) {
        int length = originBills.length;
        E[] newBills = this.createBills(originBills);
        for (int i = 0; i < length; i++) {
            this.constructUpdate(originBills[i], originBills[i], newBills[i]);
        }
        return newBills;
    }

    /**
     * ���ݵ�ǰ���ݹ���ɾ����ͼ��ֻ�����������ʱ�����������VO��״̬Ϊɾ��״̬
     * 
     * @param bills
     *        ��ǰ����
     * @return ɾ����ͼ
     */
    public E[] constructDelete(E[] bills) {
        int length = bills.length;
        E[] newBills = this.createBills(bills);
        for (int i = 0; i < length; i++) {
            this.constructDelete(bills[i], newBills[i]);
        }
        return newBills;
    }

    /**
     * �����������ݵ���ͼ���ա�������VO��״̬Ϊ����״̬
     * 
     * @param bills
     *        Ҫ�����ĵ���
     * @return �������ݵ���ͼ����
     */
    public E[] constructInsert(E[] bills) {
        int length = bills.length;
        E[] newBills = this.createBills(bills);
        for (int i = 0; i < length; i++) {
            this.constructInsert(bills[i], newBills[i]);
        }
        return newBills;
    }

    private void appendMandatoryKey(Set<String> set,
                                    ISuperVO vo) {
        IAttributeMeta keyMeta = vo.getMetaData().getPrimaryAttribute();
        set.add(keyMeta.getName());
        set.add(ElementConstant.KEY_TS);
        set.add(PseudoColumnAttribute.PSEUDOCOLUMN);
    }

    private ISuperVO[] compareChildren(E originBill,
                                       E bill,
                                       IVOMeta voMeta) {
        ISuperVO[] originVOs = originBill.getChildren(voMeta);
        ISuperVO[] vos = bill.getChildren(voMeta);
        ISuperVO[] ret = null;
        // ԭʼ����û���ӱ�����
        if (this.hasNoChildren(originVOs)) {
            if (!this.hasNoChildren(vos)) {
                ret = this.createInsertChildren(bill, voMeta);
            }
        } else {
            if (this.hasNoChildren(vos)) {
                ret = this.createDeleteChildren(originBill, voMeta);
            }
            // ��Ҫ�Ƚ�ɾ�����޸ĵ�VO
            else {
                ret = this.compareChildren(originVOs, vos);
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private ISuperVO[] compareChildren(ISuperVO[] originVOs,
                                       ISuperVO[] vos) {
        Map<String, ISuperVO> index = new HashMap<String, ISuperVO>();
        for (ISuperVO vo : originVOs) {
            index.put(vo.getPrimaryKey(), vo);
        }
        List<ISuperVO> list = new ArrayList<ISuperVO>();
        for (ISuperVO vo : vos) {
            String key = vo.getPrimaryKey();
            if (key == null) {
                ISuperVO newVO = this.createInsertVO(vo);
                list.add(newVO);
            } else if (!index.containsKey(key)) {
                ISuperVO newVO = this.createInsertVO(vo);
                list.add(newVO);
            } else {
                ISuperVO originVO = index.get(key);
                ISuperVO diffVO = this.createDiffVO(originVO, vo);
                list.add(diffVO);
                index.remove(key);
            }
        }
        Set<Entry<String, ISuperVO>> entryset = index.entrySet();
        for (Entry<String, ISuperVO> entry : entryset) {
            ISuperVO originVO = entry.getValue();
            ISuperVO deleteVO = this.createDeleteVO(originVO);
            list.add(deleteVO);
        }
        Class<ISuperVO> clazz = null;
        if (originVOs.length > 0) {
            clazz = (Class<ISuperVO>) originVOs[0].getClass();
        } else if (vos.length > 0) {
            clazz = (Class<ISuperVO>) vos[0].getClass();
        }
        ListToArrayTool<ISuperVO> tool = new ListToArrayTool<ISuperVO>(clazz);
        return tool.convertToArray(list);
    }

//    /**
//     * @param vos
//     * @param voStatus
//     */
//    private void setVOStatus(ISuperVO[] vos,
//                             int voStatus) {
//        for (ISuperVO voTemp : vos) {
//            voTemp.setStatus(voStatus);
//        }
//    }

    private void constructDelete(E bill,
                                 E diffrentBill) {
        IBillMeta billMeta = bill.getMetaData();
        if (billMeta.getParent() != null) {
            ISuperVO originParent = bill.getParent();
            ISuperVO newParent = this.createDeleteVO(originParent);
            diffrentBill.setParent(newParent);
        }

        IVOMeta[] childMetas = billMeta.getChildren();
        for (IVOMeta childMeta : childMetas) {
            ISuperVO[] vos = bill.getChildren(childMeta);
            if (vos == null) {
                continue;
            }
            vos = this.createDeleteChildren(bill, childMeta);
            diffrentBill.setChildren(childMeta, vos);
        }
    }

    private void constructInsert(E bill,
                                 E diffrentBill) {
        IBillMeta billMeta = bill.getMetaData();
        if (billMeta.getParent() != null) {
            ISuperVO originParent = bill.getParent();
            ISuperVO newParent = this.createInsertVO(originParent);
            diffrentBill.setParent(newParent);
        }

        IVOMeta[] childMetas = billMeta.getChildren();
        for (IVOMeta childMeta : childMetas) {
            ISuperVO[] vos = bill.getChildren(childMeta);
            if (vos == null || vos.length == 0) {
                continue;
            }
            vos = this.createInsertChildren(bill, childMeta);
            diffrentBill.setChildren(childMeta, vos);
        }
    }

    private void constructUpdate(E originBills,
                                 E bill,
                                 E diffrentBill) {
        IBillMeta billMeta = bill.getMetaData();
        if (billMeta.getParent() != null) {
            ISuperVO originParent = originBills.getParent();
            ISuperVO parent = bill.getParent();
            ISuperVO newParent = this.createDiffVO(originParent, parent);
            diffrentBill.setParent(newParent);
        }

        IVOMeta[] childMetas = billMeta.getChildren();
        for (IVOMeta childMeta : childMetas) {
            ISuperVO[] vos = this.compareChildren(originBills, bill, childMeta);
            if (vos == null) {
                continue;
            }
            diffrentBill.setChildren(childMeta, vos);
        }
    }

    @SuppressWarnings("unchecked")
    private E[] createBills(E[] bills) {
        int length = bills.length;
        E[] newBills = (E[]) Constructor.construct(bills[0].getClass(), length);
        return newBills;
    }

    private ISuperVO[] createDeleteChildren(E bill,
                                            IVOMeta childMeta) {
        ISuperVO[] vos = bill.getChildren(childMeta);
        int length = vos.length;
        ISuperVO[] children = Constructor.declareArray(vos[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            children[i] = this.createDeleteVO(vos[i]);
        }
        return children;
    }

    private ISuperVO createDeleteVO(ISuperVO originVO) {
        ISuperVO newVO = Constructor.construct(originVO.getClass());
        Set<String> set = new HashSet<String>();
        this.appendMandatoryKey(set, originVO);

        for (String name : set) {
            Object value = originVO.getAttributeValue(name);
            newVO.setAttributeValue(name, value);
        }
        newVO.setStatus(VOStatus.DELETED);
        this.createGCDeleteVO(originVO, newVO);
        return newVO;
    }

    private void createGCDeleteVO(ISuperVO oldVO,
                                  ISuperVO newVO) {
        if (this.isNeedCreateRelVOs(oldVO)) {
            return;
        }
        List<IAttribute> childAttr = this.getChildAttr(oldVO);
        for (IAttribute attrTemp : childAttr) {
            ISuperVO[] childVOs = (ISuperVO[]) attrTemp.getAccessStrategy().getValue(oldVO, attrTemp);
            if (childVOs == null || childVOs.length == 0) {
                continue;
            }
            ISuperVO[] newChildVOs = this.batchCreateGCDeleteVO(childVOs);
            attrTemp.getAccessStrategy().setValue(newVO, attrTemp, newChildVOs);
        }
    }

    private ISuperVO[] batchCreateGCDeleteVO(ISuperVO[] vos) {
        int length = vos.length;
        ISuperVO[] newVOs = Constructor.declareArray(vos[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            newVOs[i] = this.createDeleteVO(vos[i]);
        }
        return newVOs;
    }

    private ISuperVO createDiffVO(ISuperVO originVO,
                                  ISuperVO vo) {
        ISuperVO newVO = Constructor.construct(originVO.getClass());
        VOTool tool = new VOTool();
        Set<String> set = tool.getDifferentFieldForDynamic(originVO, vo);
        // �Ƿ����޸�
        boolean flag = false;
        // ���ֻ��α�У����㱻�޸ģ��ӵ��ݽ�����ȡ���ݵ�ʱ��pubapp��ܻ��Զ�����α�У�
        // �˳��������ڣ����ݵ��޸İ�ť��ʲô������������������
        if (set.contains(PseudoColumnAttribute.PSEUDOCOLUMN)) {
            flag = set.size() > 1;
        } else {
            flag = set.size() > 0;
        }

        this.appendMandatoryKey(set, vo);
        Set<String> usedSet = vo.usedAttributeNames();
        for (String name : set) {
            // ����ģ����û�е�ǰitemkey�����Բ�������ֵ�������������ݱ���յĴ���
            if (!usedSet.contains(name)) {
                continue;
            }
            Object value = vo.getAttributeValue(name);
            newVO.setAttributeValue(name, value);
        }
        if (flag) {
            newVO.setStatus(VOStatus.UPDATED);
        } else {
            newVO.setStatus(VOStatus.UNCHANGED);
        }
        this.createGCDiffVO(originVO, vo, newVO);
        return newVO;
    }

    private void createGCDiffVO(ISuperVO originVO,
                                ISuperVO vo,
                                ISuperVO newVO) {
        if (!this.isNeedCreateRelVOs(originVO)) {
            return;
        }
        List<IAttribute> childAttr = this.getChildAttr(originVO);
        for (IAttribute attrTemp : childAttr) {
            ISuperVO[] originVOs = (ISuperVO[]) attrTemp.getAccessStrategy().getValue(originVO, attrTemp);
            ISuperVO[] vos = (ISuperVO[]) attrTemp.getAccessStrategy().getValue(vo, attrTemp);
            ISuperVO[] newChildVOs = null;
            // ISuperVO[] newChildVOs = this.compareChildren(oldChildVOs, childVOs);
            if (this.hasNoChildren(originVOs)) {
                if (!this.hasNoChildren(vos)) {
                    int length = vos.length;
                    ISuperVO[] children = Constructor.declareArray(vos[0].getClass(), length);
                    for (int i = 0; i < length; i++) {
                        children[i] = this.createInsertVO(vos[i]);
                    }
                    newChildVOs = children;
                }
            } else {
                if (this.hasNoChildren(vos)) {
                    int length = originVOs.length;
                    ISuperVO[] children = Constructor.declareArray(originVOs[0].getClass(), length);
                    for (int i = 0; i < length; i++) {
                        children[i] = this.createDeleteVO(originVOs[i]);
                    }
                    newChildVOs = children;
                }
                // ��Ҫ�Ƚ�ɾ�����޸ĵ�VO
                else {
                    newChildVOs = this.compareChildren(originVOs, vos);
                }
            }

            attrTemp.getAccessStrategy().setValue(newVO, attrTemp, newChildVOs);
        }
    }

    private ISuperVO[] createInsertChildren(E bill,
                                            IVOMeta childMeta) {
        ISuperVO[] vos = bill.getChildren(childMeta);
        int length = vos.length;
        ISuperVO[] children = Constructor.declareArray(vos[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            children[i] = this.createInsertVO(vos[i]);
        }
        return children;
    }

    private ISuperVO createInsertVO(ISuperVO vo) {
        ISuperVO newVO = Constructor.construct(vo.getClass());
        Set<String> set = vo.usedAttributeNames();
        for (String name : set) {
            Object value = vo.getAttributeValue(name);
            newVO.setAttributeValue(name, value);
        }
        newVO.setStatus(VOStatus.NEW);
        this.createGCInsertVO(vo, newVO);
        return newVO;
    }

    private void createGCInsertVO(ISuperVO oldVO,
                                  ISuperVO newVO) {
        if (!this.isNeedCreateRelVOs(oldVO)) {
            return;
        }
        List<IAttribute> childAttr = this.getChildAttr(oldVO);
        for (IAttribute attrTemp : childAttr) {
            ISuperVO[] childVOs = (ISuperVO[]) attrTemp.getAccessStrategy().getValue(oldVO, attrTemp);
            if (childVOs == null || childVOs.length == 0) {
                continue;
            }
            ISuperVO[] newChildVOs = this.batchCreateInsertVO(childVOs);
            attrTemp.getAccessStrategy().setValue(newVO, attrTemp, newChildVOs);
            for (int i = 0; i < newChildVOs.length; i++) {
                this.createGCInsertVO(childVOs[i], newChildVOs[i]);
            }
        }
    }

    private ISuperVO[] batchCreateInsertVO(ISuperVO[] childVOs) {
        int length = childVOs.length;
        ISuperVO[] newVOs = Constructor.construct(childVOs[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            newVOs[i] = this.createInsertVO(childVOs[i]);
        }
        return newVOs;
    }

    /**
     * �ж��Ƿ���Ҫ������ص�VO.<br>
     * �����beanStyle��BeanStyleEnum.AGGVO_HEAD<br>
     * �ں����ᴴ����������������Ҫ������ص���VO.
     * 
     * @param vo
     * @return
     */
    private boolean isNeedCreateRelVOs(ISuperVO vo) {
        IBusinessEntity busiEntity = MMGPGrandBusiEntityUtil.getInstance().queryBusiEntity(vo);
        BeanStyleEnum beanStyle = busiEntity.getBeanStyle().getStyle();
        return beanStyle != BeanStyleEnum.AGGVO_HEAD;
    }

    private List<IAttribute> getChildAttr(ISuperVO vo) {
        return MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(vo);
    }

    private boolean hasNoChildren(ISuperVO[] vos) {
        return vos == null || vos.length == 0;
    }

}
