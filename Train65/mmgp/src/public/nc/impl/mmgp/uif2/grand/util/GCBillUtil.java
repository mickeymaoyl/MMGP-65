package nc.impl.mmgp.uif2.grand.util;

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
 * 主子孙单据工具类.
 * <p>
 * 1.获取所有子表的数据.
 * <p>
 * 2.获取所有孙表数据.
 * <p>
 * 2.递归获取所有子孙表数据.
 * <p>
 * 3.后续考虑改为泛型.
 * <p>
 * 4.加入对住主子孙的递归赋主键方法.
 * 
 * @since 6.3
 * @version 2012-6-14 下午06:39:42
 * @author zhaoshb
 */
public class GCBillUtil {

    private static final GCBillUtil instance = new GCBillUtil();

    private GCBillUtil() {
    }

    public List<ISuperVO> getAllGrandChildVOs(IBill[] bills) {
        List<ISuperVO> allGrandChildVOList = new ArrayList<ISuperVO>();
        for (IBill billTemp : bills) {
            allGrandChildVOList.addAll(this.getAllGrandChildVOs(billTemp));
        }
        return allGrandChildVOList;
    }

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

    public List<SuperVO> getChildVOs(ISuperVO vo) {
        Set<String> childAttrSet = GCBusiEntityUtil.getInstance().queryChildAttrName(vo);
        List<SuperVO> childVOList = new ArrayList<SuperVO>();
        for (String childAttrTemp : childAttrSet) {
            SuperVO[] childVOs = (SuperVO[]) BeanHelper.getProperty(vo, childAttrTemp);
            if (childVOs != null && childVOs.length != 0) {
                childVOList.addAll(Arrays.asList(childVOs));
            }
        }
        return childVOList;
    }

    public ISuperVO[] getChildVOs(ISuperVO parent, String gcAttrName) {
        return (ISuperVO[]) BeanHelper.getProperty(parent, gcAttrName);
    }

    public void getAllChildVOs(ISuperVO[] vos, List<ISuperVO> allVOList) {
        for (ISuperVO parentVOTemp : vos) {
            this.getAllChildVOs(parentVOTemp, allVOList);
        }
    }

    public void getAllChildVOs(List<ISuperVO> voList, List<ISuperVO> allVOList) {
        for (ISuperVO parentVOTemp : voList) {
            this.getAllChildVOs(parentVOTemp, allVOList);
        }
    }

    public List<ISuperVO> getAllChildVOs(ISuperVO vo, List<ISuperVO> allVOList) {
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
        Set<String> childAttrSet = GCBusiEntityUtil.getInstance().queryChildAttrName(metaVO);
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
     * 设定主子孙Bill中的新增VO的主键.
     * <p>
     * 标准的主子单据子表中的父表外键在反序列化时加入的.
     * 
     * @param bill
     */
    public void setGCBillPrimayKey(IBill bill) {
        // 设定新增数据主键.
        List<ISuperVO> newVOList = this.getChildrenNewVOs(bill);
        this.setPrimayKey(newVOList);
        // 设定父子关系.
        this.setChildParentId(bill);
    }

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

    public void setChildParentId(ISuperVO[] vos, String parentField, String parentId) {
        for (ISuperVO voTemp : vos) {
            ((SuperVO) voTemp).setAttributeValue(parentField, parentId);
            Set<String> childrenAttr = GCBusiEntityUtil.getInstance().queryChildAttrName(voTemp);
            Map<String, String> childAttrToCol = GCBusiEntityUtil.getInstance().querychildAttrToCol(voTemp);
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

    public void getGCChildrenVOs(ISuperVO[] childrenVOs, List<ISuperVO> newChildrenVOList) {
        if (null == childrenVOs || childrenVOs.length <= 0) {
            return;
        }
        for (ISuperVO voTemp : childrenVOs) {
            if (voTemp.getStatus() == VOStatus.NEW) {
                newChildrenVOList.add(voTemp);
            }
            Set<String> childrenAttr = GCBusiEntityUtil.getInstance().queryChildAttrName(voTemp);
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

    public static GCBillUtil getInstance() {
        return GCBillUtil.instance;
    }

}
