package nc.ui.mmgp.uif2.model;

import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.ui.pubapp.uif2app.components.grand.util.ArrayUtil;
import nc.ui.pubapp.uif2app.components.grand.util.MainGrandBusiUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 May 22, 2013
 * @author wangweir
 */
public class MMGPMainGrandModel extends MainGrandModel {

    /**
     * 
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    public List getData() {
        List data = this.getMainModel().getData();

        List grandData = new ArrayList();

        for (Object mainData : data) {
            if (mainData == null) {
                continue;
            }
            int aggVoStatus = ((AggregatedValueObject) mainData).getParentVO().getStatus();
            grandData.add(this.parseObject(aggVoStatus, mainData));
        }
        return grandData;
    }

    protected Object parseObject(int aggVoStatus,
                                 Object selectObj) {
        if (selectObj instanceof AbstractBill) {

            String[] tabCodes = ((AbstractBill) selectObj).getTableCodes();
            for (String tabCode : tabCodes) {

                this.setGrandFormTabCode(aggVoStatus, selectObj, tabCode);
            }
            if (aggVoStatus == VOStatus.DELETED) {
                ((AggregatedValueObject) selectObj).getParentVO().setStatus(VOStatus.DELETED);
            }
        }
        return selectObj;
    }

    /**
     * 根据某个页签将其孙表数据从缓存中取出来
     */
    protected void setGrandFormTabCode(int aggVoStatus,
                                       Object selectObj,
                                       String tabCode) {
        CircularlyAccessibleValueObject[] childrenVOs = ((AbstractBill) selectObj).getTableVO(tabCode);
        if (childrenVOs != null && childrenVOs.length != 0) {
            for (CircularlyAccessibleValueObject childVo : childrenVOs) {

                NCObject ncObject = NCObject.newInstance(childVo);
                IBean bean = ncObject.getRelatedBean();
                // 获取子表中对应孙实体的属性列表
                List<IAttribute> attrList = MainGrandBusiUtil.getInstance().queryChildAttr(bean);
                for (IAttribute grandattr : attrList) {
                    try {
                        String childPk = ((SuperVO) childVo).getPrimaryKey();
                        String uniqueCardKey = tabCode + childPk + grandattr.getName();
                        ArrayList<Object> grandVOList = new ArrayList<Object>();
                        if (childVo != null && childVo.getPrimaryKey() != null) {
                            childVo.setStatus(aggVoStatus);
                            grandVOList = this.getQueryDataMap().get(uniqueCardKey);
                            if (grandVOList != null && grandVOList.size() != 0) {
                                for (Object grandObj : grandVOList) {
                                    ((SuperVO) grandObj).setStatus(aggVoStatus);
                                }
                                Object obj = grandVOList.get(0);
                                grandattr.getAccessStrategy().setValue(
                                    childVo,
                                    grandattr,
                                    grandVOList.toArray(ArrayUtil.toArray(obj)));
                            }
                        }
                    } catch (BusinessException e) {
                        Logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel#getSelectedOperaDatas()
     */
    public Object[] getSelectedOperaDatas() {
        // 解决空指针问题
        Object[] selectObjArray = this.getMainModel().getSelectedOperaDatas();
        if (selectObjArray == null) {
            return null;
        }
        return super.getSelectedOperaDatas();
    }

}
