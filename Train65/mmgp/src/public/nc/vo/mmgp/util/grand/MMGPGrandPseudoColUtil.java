package nc.vo.mmgp.util.grand;

import java.util.List;

import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主子孙伪列工具.
 * </p>
 * 
 * @since 创建日期 May 20, 2013
 * @author wangweir
 */
public class MMGPGrandPseudoColUtil {

    public static void setPseudoColInfo(Object obj) {
        NCObject ncObj = NCObject.newInstance(obj);
        IBean bean = ncObj.getRelatedBean();

        List<IAttribute> childAttrList = MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(bean);
        setPseudoColInfo(obj, childAttrList);
    }

    private static void setPseudoColInfo(Object obj,
                                         List<IAttribute> childAttrList) {
        for (IAttribute attrTemp : childAttrList) {
            SuperVO[] childData = getChildData(obj, attrTemp);
            setGCPseudoColInfo(childData);
        }
    }

    private static void setGCPseudoColInfo(SuperVO[] childData) {
        if (childData == null || childData.length == 0) {
            return;
        }
        setPseudoColRowNo(childData);

        List<IAttribute> childAttrList = MMGPGrandBusiEntityUtil.getInstance().queryChildAttr(childData[0]);

        for (SuperVO voTemp : childData) {
            setPseudoColInfo(voTemp, childAttrList);
        }
    }

    private static void setPseudoColRowNo(SuperVO[] childData) {
        int i = 0;
        for (SuperVO voTemp : childData) {
            String pseudoColName = PseudoColumnAttribute.PSEUDOCOLUMN;
            Integer rowNo = Integer.valueOf(i++);
            voTemp.setAttributeValue(pseudoColName, rowNo);
        }
    }

    private static SuperVO[] getChildData(Object obj,
                                          IAttribute attr) {
        return (SuperVO[]) attr.getAccessStrategy().getValue(obj, attr);
    }

}
