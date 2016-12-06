package nc.ui.mmgp.uif2.view.grand;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pubapp.uif2app.components.grand.util.MainGrandAssist;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValueObject;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Sep 27, 2013
 * @author wangweir
 */
public class MMGPMainGrandAssist extends MainGrandAssist {

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.components.grand.util.MainGrandAssist#setGrandToChild(java.lang.String,
     * nc.vo.pub.CircularlyAccessibleValueObject, java.util.List)
     */
    @Override
    public CircularlyAccessibleValueObject setGrandToChild(String tableCode,
                                                           CircularlyAccessibleValueObject childVO,
                                                           List< ? > grandVOList) {
        // ���������ֵ�ֱ�ǰ��Clone
        List<Object> clonedGrandVOList = new ArrayList<Object>();
        if (MMCollectionUtil.isNotEmpty(grandVOList)) {
            for (Object obj : grandVOList) {
                // ��ʱֻ����ValueObject����
                if (obj instanceof ValueObject) {
                    ValueObject valueObj = (ValueObject) obj;
                    clonedGrandVOList.add(valueObj.clone());
                } else {
                    clonedGrandVOList.add(obj);
                }
            }
        }
        return super.setGrandToChild(tableCode, childVO, clonedGrandVOList);
    }

}
