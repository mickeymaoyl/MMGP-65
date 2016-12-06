package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.md.model.IAttribute;
import nc.md.model.IBusinessEntity;
import nc.md.model.access.javamap.BeanStyleEnum;
import nc.mddb.constant.ElementConstant;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;
import nc.vo.pubapp.pattern.model.tool.VOTool;
import nc.vo.pubapp.pattern.model.transfer.bill.ServerBillToClient;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;

/**
 * �������̨���ݲ��쵽ǰ̨.
 * 
 * @see ServerBillToClient
 * @since 6.3
 * @version 2012-6-15 ����04:57:55
 * @author zhaoshb
 */
public class MMGPGrandServerBillToClient<E extends IBill> {
    /**
     * ����ǰ̨�������������ݺ����ڵĵ������Ƚϣ���ȡ�ȽϺ�ĵ�����ͼ���ա�������ʱ����Լ�α�ж���Ĭ�����õ����յ�
     * 
     * @param fullClientBills ǰ̨�������������
     * @param bills ���ڵĵ���
     * @return �ȽϺ�ĵ��ݿ�����ͼ
     */
    @SuppressWarnings("unchecked")
    public E[] construct(E[] fullClientBills, E[] bills) {
        int length = bills.length;
        E[] newBills = (E[]) Constructor.construct(bills[0].getClass(), length);
        for (int i = 0; i < length; i++) {
            this.construct(fullClientBills[i], bills[i], newBills[i]);
        }
        return newBills;
    }

    private void appendMandatoryKey(Set<String> set, ISuperVO vo) {
        IAttributeMeta keyMeta = vo.getMetaData().getPrimaryAttribute();
        set.add(keyMeta.getName());
        set.add(ElementConstant.KEY_TS);
    }

    private void construct(E fullClientBill, E bill, E diffrentBill) {
        IBillMeta billMeta = bill.getMetaData();
        if (billMeta.getParent() != null) {
            ISuperVO originParent = fullClientBill.getParent();
            ISuperVO parent = bill.getParent();
            ISuperVO newParent = this.contruct(originParent, parent);
            diffrentBill.setParent(newParent);
        }

        IVOMeta[] childMetas = billMeta.getChildren();
        for (IVOMeta childMeta : childMetas) {
            ISuperVO[] vos = this.constructChildren(fullClientBill, bill, childMeta);
            if (vos == null) {
                continue;
            }
            diffrentBill.setChildren(childMeta, vos);
        }
    }

    private ISuperVO construct(ISuperVO clientVO, Map<Integer, ISuperVO> index) {
        if (clientVO.getStatus() == VOStatus.DELETED) {
            return null;
        }
        ISuperVO vo = this.getVO(clientVO, index);
        ISuperVO newVO = this.contruct(clientVO, vo);
        return newVO;
    }

    private ISuperVO getVO(ISuperVO clientVO, Map<Integer, ISuperVO> index) {
        String name = PseudoColumnAttribute.PSEUDOCOLUMN;
        Integer columnIndex = (Integer) clientVO.getAttributeValue(name);
        ISuperVO vo = index.get(columnIndex);
        return vo;

    }

    private ISuperVO[] constructChildren(E fullClientBill, E bill, IVOMeta voMeta) {
        ISuperVO[] clientVOs = fullClientBill.getChildren(voMeta);
        ISuperVO[] vos = bill.getChildren(voMeta);
        ISuperVO[] newVOs = null;
        if (clientVOs != null && clientVOs.length > 0) {
            // ����û�иĶ�
            if (vos == null || vos.length == 0) {
                newVOs = clientVOs;
            }
            else {
                newVOs = this.constructChildrenByHasChild(clientVOs, vos);
            }
        }
        return newVOs;
    }

    private ISuperVO[] constructChildrenByHasChild(ISuperVO[] clientVOs, ISuperVO[] vos) {
        if (clientVOs == null || vos == null) {
            return vos;
        }
        ISuperVO[] newVOs;
        boolean clientHasPseudoColumn = this.isHasPseudoCol(clientVOs);
        boolean serverHasPseudoColumn = this.isHasPseudoCol(vos);
        boolean clientHasPK = this.isHasPk(clientVOs);
        // ������VO��û��α�У�����ǰ̨VO����α�У�ֱ�ӷ���������VO
        if (!serverHasPseudoColumn && clientHasPseudoColumn) {
            newVOs = vos;
        }
        // ǰ̨��û��α�У���Ҫ����Ƚ�����
        else if (!serverHasPseudoColumn && !clientHasPseudoColumn) {
            // ��̨��ʽ�ű�����ʱ���ܻ���뵽����ط������磺���������Ƶ�����
            if (!clientHasPK) {
                newVOs = vos;
            }
            else {
                newVOs = this.constructNoColumnIndex(clientVOs, vos);
            }
        }
        else {
            newVOs = this.constructWithColumnIndex(clientVOs, vos);
        }
        return newVOs;
    }

    private boolean isHasPseudoCol(ISuperVO[] vos) {
        String name = PseudoColumnAttribute.PSEUDOCOLUMN;
        boolean isHasPseudoColumn = true;
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getAttributeValue(name) == null) {
                isHasPseudoColumn = false;
            }
        }
        return isHasPseudoColumn;
    }

    private boolean isHasPk(ISuperVO[] vos) {
        boolean isHasPK = true;
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getPrimaryKey() == null) {
                isHasPK = false;
            }
        }
        return isHasPK;
    }

    private ISuperVO[] constructNoColumnIndex(ISuperVO[] clientVOs, ISuperVO[] vos) {
        Map<String, ISuperVO> index = new HashMap<String, ISuperVO>();
        for (ISuperVO vo : clientVOs) {
            String pk = vo.getPrimaryKey();
            if (pk == null) {
                ExceptionUtils.unSupported();
            }
            index.put(pk, vo);
        }
        List<ISuperVO> list = new ArrayList<ISuperVO>();
        for (ISuperVO vo : vos) {
            String pk = vo.getPrimaryKey();
            if (pk == null) {
                ExceptionUtils.unSupported();
            }
            ISuperVO originVO = index.get(pk);
            if (originVO == null) {
                ExceptionUtils.unSupported();
            }
            ISuperVO newVO = this.contruct(originVO, vo);
            list.add(newVO);
        }
        ListToArrayTool<ISuperVO> tool = new ListToArrayTool<ISuperVO>();
        ISuperVO[] newVOs = tool.convertToArray(list);
        return newVOs;
    }

    private ISuperVO[] constructWithColumnIndex(ISuperVO[] clientVOs, ISuperVO[] vos) {
        String name = PseudoColumnAttribute.PSEUDOCOLUMN;
        Integer columnIndex;
        Map<Integer, ISuperVO> index = new HashMap<Integer, ISuperVO>();
        for (ISuperVO vo : vos) {
            columnIndex = (Integer) vo.getAttributeValue(name);
            if (columnIndex == null) {
                ExceptionUtils.unSupported();
            }
            index.put(columnIndex, vo);
        }
        List<ISuperVO> list = new ArrayList<ISuperVO>();
        for (ISuperVO vo : clientVOs) {
            ISuperVO newVO = this.construct(vo, index);
            if (newVO != null) {
                list.add(newVO);
            }
        }
        ListToArrayTool<ISuperVO> tool = new ListToArrayTool<ISuperVO>();
        ISuperVO[] newVOs = tool.convertToArray(list);
        return newVOs;
    }

    private ISuperVO contruct(ISuperVO clientVO, ISuperVO vo) {
        if (clientVO == null) {
            return null;
        }
        ISuperVO newVO = Constructor.construct(clientVO.getClass());
        VOTool tool = new VOTool();
        Set<String> set = tool.getDifferentFieldForDynamic(vo, clientVO);
        this.appendMandatoryKey(set, vo);
        for (String name : set) {
            Object value = vo.getAttributeValue(name);
            newVO.setAttributeValue(name, value);
        }
        String name = PseudoColumnAttribute.PSEUDOCOLUMN;
        Object value = vo.getAttributeValue(name);
        newVO.setAttributeValue(name, value);
        this.constructGCData(clientVO, vo, newVO);
        return newVO;
    }

    private void constructGCData(ISuperVO clientVO, ISuperVO vo, ISuperVO newVO) {
        if (!this.isCanConsChildData(clientVO)) {
            return;
        }
        List<IAttribute> childAttrList = this.getChildAttr(clientVO);
        for (IAttribute attrTemp : childAttrList) {
            ISuperVO[] clientChildData = this.getChildData(clientVO, attrTemp);
            ISuperVO[] newChildData = this.getChildData(vo, attrTemp);
            ISuperVO[] combineData = this.constructChildrenByHasChild(clientChildData, newChildData);
            this.setChildData(newVO, combineData, attrTemp);
        }
    }

    private List<IAttribute> getChildAttr(ISuperVO vo) {
        return MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(vo);
    }

    private ISuperVO[] getChildData(ISuperVO parentVO, IAttribute attr) {
        return (ISuperVO[]) attr.getAccessStrategy().getValue(parentVO, attr);
    }

    private void setChildData(ISuperVO parentVO, ISuperVO[] childData, IAttribute attr) {
        attr.getAccessStrategy().setValue(parentVO, attr, childData);
    }

    private boolean isCanConsChildData(ISuperVO clientVO) {
        IBusinessEntity busiEntity = MMGPGrandBusiEntityUtil.getInstance().queryBusiEntity(clientVO);
        return busiEntity.getBeanStyle().getStyle() != BeanStyleEnum.AGGVO_HEAD;

    }
}
