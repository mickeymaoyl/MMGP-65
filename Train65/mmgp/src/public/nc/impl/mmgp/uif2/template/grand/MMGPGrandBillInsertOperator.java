package nc.impl.mmgp.uif2.template.grand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.md.model.IAttribute;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.grand.MMGPGrandBusiEntityUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.tool.BillHelper;
import nc.vo.pubapp.pattern.model.tool.SynchronizePrimaryKey;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 单据插入业务处理实现
 * </p>
 * 
 * @since 创建日期 May 21, 2013
 * @author wangweir
 */
public class MMGPGrandBillInsertOperator<E extends IBill> implements IOperator<E> {

    @Override
    public E[] operate(E[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return vos;
        }
        BillHelper helper = new BillHelper(vos);
        List<ISuperVO> parentList = helper.getParentList();
        this.insertVO(parentList);
        SynchronizePrimaryKey bo = new SynchronizePrimaryKey();
        bo.setHeaderIDForChild(vos);
        Map<IVOMeta, List<ISuperVO>> item_index = helper.getItemIndex().toMap();
        for (List<ISuperVO> list : item_index.values()) {
            this.insertGcVO(list);
        }
        return vos;
    }

    private void insertGcVO(List<ISuperVO> voList) {
        if (MMCollectionUtil.isEmpty(voList)) {
            return;
        }
        // 插入子表.
        this.insertVO(voList);
        // 对孙表进行分组并设置相应外键.
        Map<String, List<ISuperVO>> attrNameToVOList = this.groupAndSetFk(voList);
        // 孙表持久化.
        for (List<ISuperVO> childVOList : attrNameToVOList.values()) {
            this.insertGcVO(childVOList);
        }
    }

    /**
     * 1.给孙表按照子表属性名进行分组，减少数据库持久化次数.
     * <p>
     * 2.设置孙表外键.
     * 
     * @param voList
     * @return
     */
    private Map<String, List<ISuperVO>> groupAndSetFk(List<ISuperVO> voList) {
        ISuperVO superVO = voList.get(0);
        List<IAttribute> childAttr = this.getChildAttr(superVO);
        Map<String, List<ISuperVO>> attrNameToVOList = new HashMap<String, List<ISuperVO>>();
        for (ISuperVO parentVOTemp : voList) {
            for (IAttribute attrTemp : childAttr) {
                List<SuperVO> hasFKVOList = this.setChildVOForeignKey(parentVOTemp, attrTemp);
                if (hasFKVOList != null && hasFKVOList.size() != 0) {
                    String attrName = attrTemp.getName();
                    if (!attrNameToVOList.containsKey(attrName)) {
                        attrNameToVOList.put(attrName, new ArrayList<ISuperVO>());
                    }
                    attrNameToVOList.get(attrName).addAll(hasFKVOList);
                }
            }
        }
        return attrNameToVOList;
    }

    private List<IAttribute> getChildAttr(ISuperVO vo) {
        return MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(vo);
    }

    private List<SuperVO> setChildVOForeignKey(ISuperVO parentVO,
                                               IAttribute childAttr) {
        String fatherAttrName = childAttr.getName();
        String fatherColName = childAttr.getColumn().getName();
        String primaryKey = parentVO.getPrimaryKey();
        Object attrValue = BeanHelper.getProperty(parentVO, fatherAttrName);
        SuperVO[] childVOs = (SuperVO[]) attrValue;
        if (childVOs == null || childVOs.length == 0) {
            return null;
        }
        int length = childVOs.length;
        for (int i = 0; i < length; i++) {
            childVOs[i].setAttributeValue(fatherColName, primaryKey);
        }
        return Arrays.asList((SuperVO[]) attrValue);
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

}
