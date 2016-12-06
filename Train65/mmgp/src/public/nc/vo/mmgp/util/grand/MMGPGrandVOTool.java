package nc.vo.mmgp.util.grand;

import java.lang.reflect.Array;
import java.util.Set;

import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.model.tool.VOTool;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙VO工具类,用于含子表的VO中的差异字段合并.
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandVOTool extends VOTool {
    @Override
    public void combine(ISuperVO vo,
                        ISuperVO anotherVO) {
        Set<String> set = this.getDifferentField(vo, anotherVO);
        Set<String> anotherSet = anotherVO.usedAttributeNames();
        this.filterGCAttr(anotherVO, anotherSet);
        for (String name : set) {
            // 在VO中根本没有设置值
            if (!anotherSet.contains(name)) {
                continue;
            }
            Object value = anotherVO.getAttributeValue(name);
            vo.setAttributeValue(name, value);
        }
        vo.setStatus(anotherVO.getStatus());
    }

    @SuppressWarnings("unchecked")
    public static <T extends ISuperVO> T[] transfer(Class<T> voClass,
                                                    ISuperVO[] vos) {
        T[] instance = (T[]) Array.newInstance(voClass, vos.length);
        for (int i = 0; i < instance.length; i++) {
            instance[i] = (T) vos[i];
        }
        return instance;
    }

    protected void filterGCAttr(ISuperVO anotherVO,
                                Set<String> anotherSet) {
        Set<String> childAttrSet = MMGPGrandBusiEntityUtil.getInstance().queryChildAttrName(anotherVO);
        if (childAttrSet.isEmpty()) {
            return;
        }
        anotherSet.removeAll(childAttrSet);
    }

}
