package nc.vo.mmgp.util.grand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.impl.pubapp.pattern.database.DBTool;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * �����ﵥ�ݹ�����.
 * <p>
 * 1.��ȡ�����ӱ������.
 * <p>
 * 2.��ȡ�����������.
 * <p>
 * 2.�ݹ��ȡ�������������.
 * <p>
 * 3.�������Ǹ�Ϊ����.
 * <p>
 * 4.�����ס������ĵݹ鸳��������.
 * 
 * @since �������� May 20, 2013
 * @author wangweir
 */
public class MMGPGrandBillUtil {

    private static final MMGPGrandBillUtil instance = new MMGPGrandBillUtil();

    private MMGPGrandBillUtil() {
    }

    /**
     * ������ȡ������VO�����VO
     * @param bills ������voList
     * @return ���VOList
     */
    public List<ISuperVO> getAllGrandChildVOs(IBill[] bills) {
        List<ISuperVO> allGrandChildVOList = new ArrayList<ISuperVO>();
        for (IBill billTemp : bills) {
            allGrandChildVOList.addAll(this.getAllGrandChildVOs(billTemp));
        }
        return allGrandChildVOList;
    }

	/**
	 * ��ȡ������VO���������VO
	 * 
	 * @param bill
	 *            ������VO
	 * @return ���VOlist
	 */
    public List<ISuperVO> getAllGrandChildVOs(IBill bill) {
        List<ISuperVO> allGCVOList = new ArrayList<ISuperVO>();
        IVOMeta[] childMetas = bill.getMetaData().getChildren();
        for (IVOMeta voMetaTemp : childMetas) {
            SuperVO[] childVOs = (SuperVO[]) bill.getChildren(voMetaTemp);
            if (!MMArrayUtil.isEmpty(childVOs)) {
                for (SuperVO voTemp : childVOs) {
                    this.getAllChildVOs(voTemp, allGCVOList);
                }
            }
        }
        return allGCVOList;
    }
    /**
     * ��ȡ������VO����VO
     * @param vo ������VO
     * @return ��VOList
     */
    public List<SuperVO> getChildVOs(ISuperVO vo) {
        Set<String> childAttrSet = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(vo);
        List<SuperVO> childVOList = new ArrayList<SuperVO>();
        for (String childAttrTemp : childAttrSet) {
            SuperVO[] childVOs = (SuperVO[]) BeanHelper.getProperty(vo, childAttrTemp);
            if (childVOs != null && childVOs.length != 0) {
                childVOList.addAll(Arrays.asList(childVOs));
            }
        }
        return childVOList;
    }

    public ISuperVO[] getChildVOs(ISuperVO parent,
                                  String gcAttrName) {
        return (ISuperVO[]) BeanHelper.getProperty(parent, gcAttrName);
    }

    public void getAllChildVOs(ISuperVO[] vos,
                               List<ISuperVO> allVOList) {
        for (ISuperVO parentVOTemp : vos) {
            this.getAllChildVOs(parentVOTemp, allVOList);
        }
    }

    public void getAllChildVOs(List<ISuperVO> voList,
                               List<ISuperVO> allVOList) {
        for (ISuperVO parentVOTemp : voList) {
            this.getAllChildVOs(parentVOTemp, allVOList);
        }
    }

    public List<ISuperVO> getAllChildVOs(ISuperVO vo,
                                         List<ISuperVO> allVOList) {
        List<SuperVO> childVOList = this.getChildVOs(vo);
        allVOList.addAll(childVOList);
        for (SuperVO childVOTemp : childVOList) {
            this.getAllChildVOs(childVOTemp, allVOList);
        }
        return allVOList;
    }

    public Map<String, List<ISuperVO>> getAllChildVOMap(List<ISuperVO> voList) {
        Map<String, List<ISuperVO>> childVOMap = new HashMap<String, List<ISuperVO>>();
        if (voList == null || voList.isEmpty()) {
            return childVOMap;
        }
        ISuperVO metaVO = voList.get(0);
        Set<String> childAttrSet = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(metaVO);
        for (String childAttrTemp : childAttrSet) {
            for (ISuperVO voTemp : voList) {
                if (!childVOMap.containsKey(childAttrTemp)) {
                    childVOMap.put(childAttrTemp, new ArrayList<ISuperVO>());
                }
                List<ISuperVO> childVOList = childVOMap.get(childAttrTemp);
                ISuperVO[] childVOs = (ISuperVO[]) BeanHelper.getProperty(voTemp, childAttrTemp);
                if (childVOs != null && childVOs.length != 0) {
                    childVOList.addAll(Arrays.asList(childVOs));
                }
            }
        }
        return childVOMap;
    }

    /**
     * �趨������Bill�е�����VO������.
     * <p>
     * ��׼�����ӵ����ӱ��еĸ�������ڷ����л�ʱ�����.
     * 
     * @param bill
     */
    public void setGCBillPrimayKey(IBill bill) {
        // �趨������������.
        List<ISuperVO> newVOList = this.getChildrenNewVOs(bill);
        this.setPrimayKey(newVOList);
        // �趨���ӹ�ϵ.
        this.setChildParentId(bill);
    }

    /**
     * ������������VO������
     * @param bills
     */
    public void setGCBillPrimayKey(IBill[] bills) {
        for (IBill bill : bills) {
            this.setGCBillPrimayKey(bill);
        }
    }

    public void setChildParentId(IBill bill) {
        Set<ISuperVO[]> childrenVOSet = this.getChildrenVOSet(bill);
        ISuperVO parentVO = bill.getParent();
        String parentId = parentVO.getPrimaryKey();
        String parentField = parentVO.getMetaData().getPrimaryAttribute().getColumn().getName();
        for (ISuperVO[] childrenVO : childrenVOSet) {
            this.setChildParentId(childrenVO, parentField, parentId);
        }
    }

    public Set<ISuperVO[]> getChildrenVOSet(IBill bill) {
        Set<ISuperVO[]> childrenVOSet = new HashSet<ISuperVO[]>();
        IBillMeta billMeta = bill.getMetaData();
        IVOMeta[] childrenMeta = billMeta.getChildren();
        for (IVOMeta childMeta : childrenMeta) {
            ISuperVO[] childrenVOs = bill.getChildren(childMeta);
            if (childrenVOs != null && childrenVOs.length != 0) {
                childrenVOSet.add(childrenVOs);
            }
        }
        return childrenVOSet;
    }

    /**
     * ����VO��ID��ͬʱ��������ĸ���ID
     * @param vos ��VO
     * @param parentField ����ID�ֶ�
     * @param parentId ����ID
     */
    public void setChildParentId(ISuperVO[] vos,
                                 String parentField,
                                 String parentId) {
        for (ISuperVO voTemp : vos) {
            ((SuperVO) voTemp).setAttributeValue(parentField, parentId);
            Set<String> childrenAttr = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(voTemp);
            Map<String, String> childAttrToCol = MMGPGrandBusiEntityUtil.getInstance().querychildAttrToCol(voTemp);
            for (String childAttr : childrenAttr) {
                ISuperVO[] childrenVOs = (ISuperVO[]) BeanHelper.getProperty(voTemp, childAttr);
                if (childrenVOs != null && childrenVOs.length != 0) {
                    String field = childAttrToCol.get(childAttr);
                    String id = ((SuperVO) voTemp).getPrimaryKey();
                    this.setChildParentId(childrenVOs, field, id);
                }
            }
        }
    }

    public void setGCVOPrimayKey(ISuperVO[] childrenVO) {
        if (childrenVO == null || childrenVO.length == 0) {
            return;
        }
    }

    public List<ISuperVO> getChildrenNewVOs(IBill bill) {
        List<ISuperVO> newChildrenVOList = new ArrayList<ISuperVO>();
        IBillMeta billMeta = bill.getMetaData();
        IVOMeta[] childrenMeta = billMeta.getChildren();
        for (IVOMeta childMeta : childrenMeta) {
            ISuperVO[] childrenVOs = bill.getChildren(childMeta);
            this.getGCChildrenVOs(childrenVOs, newChildrenVOList);
        }
        return newChildrenVOList;
    }

    public void getGCChildrenVOs(ISuperVO[] childrenVOs,
                                 List<ISuperVO> newChildrenVOList) {
        if (null == childrenVOs || childrenVOs.length <= 0) {
            return;
        }
        for (ISuperVO voTemp : childrenVOs) {
            /*
             * modify by liwsh 2014 12.3 ����pk==null�ж�
             */
            if (voTemp.getStatus() == VOStatus.NEW && voTemp.getPrimaryKey() == null) {
                newChildrenVOList.add(voTemp);
            }
            Set<String> childrenAttr = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(voTemp);
            if (!childrenAttr.isEmpty()) {
                for (String childAttr : childrenAttr) {
                    ISuperVO[] gcVOs = (ISuperVO[]) BeanHelper.getProperty(voTemp, childAttr);
                    if (gcVOs != null && gcVOs.length != 0) {
                        this.getGCChildrenVOs(gcVOs, newChildrenVOList);
                    }
                }
            }
        }
    }

    public void setPrimayKey(List<ISuperVO> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }
        int idCount = voList.size();
        int count = 0;
        DBTool dao = new DBTool();
        String[] ids = dao.getOIDs(idCount);
        for (ISuperVO voTemp : voList) {
            ((SuperVO) voTemp).setPrimaryKey(ids[count++]);
        }
    }

    public static MMGPGrandBillUtil getInstance() {
        return MMGPGrandBillUtil.instance;
    }

}
