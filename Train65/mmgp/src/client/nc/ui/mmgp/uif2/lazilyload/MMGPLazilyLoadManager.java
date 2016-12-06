package nc.ui.mmgp.uif2.lazilyload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.md.model.IBean;
import nc.md.model.access.javamap.BeanStyleEnum;
import nc.ui.mmgp.uif2.model.MMGPBillManageModel;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.model.AppEventConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * <b> 表体懒加载处理器 </b>
 * <p>
 * 重写pubapp的懒加载处理器,主要目的为了处理表头多选事件，加载所有选中行的表体数据<br/>
 * 如果第一次点击查询，默认选中第一行数据，只加载第一行表头的表体数据。如果按住ctrl或者shift键然后多选，多选的数据行不会加载表体。
 * </p>
 * 
 * @since: 创建日期:Aug 9, 2014
 * @author:liwsh
 */
public class MMGPLazilyLoadManager extends LazilyLoadManager {

    private BillManageModel mmgpmodel;

    @Override
    public void setModel(BillManageModel billManageModel) {

        mmgpmodel = billManageModel;

        IBean bean = MMGPMetaUtils.getIBean(billManageModel.getContext());
        // 只有主子表才好使
        if (bean.getBeanStyle().getStyle() == BeanStyleEnum.AGGVO_HEAD
            || bean.getBeanStyle().getStyle() == BeanStyleEnum.EXTAGGVO_HEAD) {

            super.setModel(billManageModel);

            billManageModel.addAppEventListener(new mutiSelectedEventListener(billManageModel));
        }
    }

    /**
     * 所有表体都没有数据才懒加载，因为有些单据存在没有数据的表体
     */
    @Override
    public void changeChildren(List<Class< ? extends ISuperVO>> childrenClz,
                               IBill[] bills) {
        if (childrenClz == null || childrenClz.size() == 0) return;

        Map<IBill, List<Class< ? extends ISuperVO>>> needLoadChildrenMap =
                new HashMap<IBill, List<Class< ? extends ISuperVO>>>();

        if (bills == null || bills.length == 0) {
            return;
        }
        for (IBill bill : bills) {
            if (bill == null) {
                continue;
            }
            int i = mmgpmodel.findBusinessData(bill);
            //gaotx mmgpModel可能不是我们的model，所以不能直接这么处理
            if(isLazyLoadMMgpModel(mmgpmodel,i)){
                continue;
            }
            
            List<Class< ? extends ISuperVO>> needLoadChildren = needLoadChildrenMap.get(bill);
            if (needLoadChildren == null) {
                needLoadChildren = new ArrayList<Class< ? extends ISuperVO>>();
            }
            for (Class< ? extends ISuperVO> childCls : childrenClz) {
                if (null == bill.getChildren(childCls)) {
                    needLoadChildren.add(childCls);
                }
            }

            // 只改了这一个地方：所有表体都没有数据才懒加载，因为有些单据存在没有数据的表体
            if (needLoadChildren.size() == childrenClz.size()) {
                needLoadChildrenMap.put(bill, needLoadChildren);
            }
        }

        try {
            if (!needLoadChildrenMap.isEmpty()) {
                this.getLoader().loadChildrenByClass(needLoadChildrenMap);
                IBill[] loadBills = needLoadChildrenMap.keySet().toArray(new IBill[0]);
                Integer[] rows = this.mmgpmodel.getSelectedOperaRows();
                this.mmgpmodel.directlyUpdate(loadBills);

                if (rows != null && rows.length > 0) {
                    int[] rowsTemp = new int[rows.length];
                    for (int i = 0; i < rows.length; i++) {
                        rowsTemp[i] = rows[i];
                    }
                    this.mmgpmodel.setSelectedOperaRows(rowsTemp);
                }
            }
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
    }

    /**
     * <b> 处理表头多选事件 </b>
     * <p>
     * 主要用于加载所有选中行的表体数据，因为如果是懒加载，刚开始之后加载第一行的表体数据。
     * </p>
     * 
     * @since: 创建日期:Aug 9, 2014
     * @author:liwsh
     */
    class mutiSelectedEventListener implements AppEventListener {

        private BillManageModel billManageModel;

        public mutiSelectedEventListener(BillManageModel billManageModel) {
            this.billManageModel = billManageModel;
        }

        @Override
        public void handleEvent(AppEvent event) {
            if (event.getType() == AppEventConst.MULTI_SELECTION_CHANGED) {

                billManageModel.setLazLoadAllChildren(true);

                this.loadAllChildren(getAllChildrenClass(), getSelectedDatas());
            }

        }

        /**
         * 获取所有表体页签对应的 Class
         * 
         * @return
         */
        public List<Class< ? extends ISuperVO>> getAllChildrenClass() {

            List<Class< ? extends ISuperVO>> classList = new ArrayList<Class< ? extends ISuperVO>>();

            IBill aggVo = (IBill) this.billManageModel.getSelectedData();

            if (aggVo != null) {
                IVOMeta[] voMeta = aggVo.getMetaData().getChildren();
                for (IVOMeta childMeta : voMeta) {
                    Class< ? extends ISuperVO> childClass = aggVo.getMetaData().getVOClass(childMeta);
                    classList.add(childClass);
                }
            }
            return classList;
        }

        /**
         * 获取选中数据
         * 
         * @return
         */
        private IBill[] getSelectedDatas() {
            Object[] billObjs = this.billManageModel.getSelectedOperaDatas();

            if (MMArrayUtil.isEmpty(billObjs)) {
                return null;
            }

            // 如果只选择一行数据，从方法getSelectedData()获取数据，因为getSelectedOperaDatas()里面还有可能是旧数据。
            if (billObjs.length == 1) {

                Object selectData = this.billManageModel.getSelectedData();
                billObjs = new Object[]{selectData };
            }

            IBill[] bills = new IBill[billObjs.length];

            for (int i = 0; i < billObjs.length; i++) {
                bills[i] = (IBill) billObjs[i];
            }
            return bills;
        }

        /**
         * 加载所有选中数据的表体行数据
         * 
         * @param childrenClz
         *        表体VO class队列
         * @param bills
         *        表头选中数据
         */
        public void loadAllChildren(List<Class< ? extends ISuperVO>> childrenClz,
                                    IBill[] bills) {

            if (childrenClz == null || childrenClz.size() == 0) return;

            Map<IBill, List<Class< ? extends ISuperVO>>> needLoadChildrenMap =
                    new HashMap<IBill, List<Class< ? extends ISuperVO>>>();

            if (bills == null || bills.length == 0) {
                return;
            }

            // 有表体不需要懒加载表体的单据
            List<IBill> hasChildrenBillList = new ArrayList<IBill>();
            for (IBill bill : bills) {
                if (bill == null) {
                    continue;
                }
                int i = billManageModel.findBusinessData(bill);
                //gaotx mmgpModel可能不是我们的model，所以不能直接这么处理
                if (isLazyLoadMMgpModel(billManageModel, i)) {
                    continue;
                }
                List<Class< ? extends ISuperVO>> needLoadChildren = null;
                if (needLoadChildrenMap.get(bill) == null) {
                    needLoadChildren = new ArrayList<Class< ? extends ISuperVO>>();
                }
                for (Class< ? extends ISuperVO> childCls : childrenClz) {
                    if (null == bill.getChildren(childCls)) {
                        needLoadChildren.add(childCls);
                    }
                }

                // 所有表体都没有数据才懒加载，因为有些单据存在没有数据的表体
                if (needLoadChildren.size() == childrenClz.size()) {
                    needLoadChildrenMap.put(bill, needLoadChildren);
                } else {
                    hasChildrenBillList.add(bill);
                }
            }

            try {
                if (!needLoadChildrenMap.isEmpty()) {
                    MMGPLazilyLoadManager.this.getLoader().loadChildrenByClass(needLoadChildrenMap);
                    IBill[] loadBills = needLoadChildrenMap.keySet().toArray(new IBill[0]);

                    hasChildrenBillList.addAll(Arrays.asList(loadBills));

                    /**
                     * 将不是懒加载的单据也拿去更新模型。<br\>
                     * 原因如下：1. 用户选中第1行，2.用户按住ctrl选中第3行，3. 程序进入这里的时候，loadBills只有一条数据，因为只有第3行需要加载表体<br\>
                     * 4. 如果将loadBills直接传入directlyUpdate，会导致方法内部设置第3行为选中行，导致第一行高亮选中消失。
                     */
                    this.billManageModel.directlyUpdate(hasChildrenBillList.toArray(new IBill[0]));

                }
            } catch (Exception e) {
                ExceptionUtils.wrappException(e);
            }
        }

    }
    
    /**
     * 是否已经懒加载
     * @param model
     * @param i
     * @return
     */
    protected  boolean isLazyLoadMMgpModel(BillManageModel model, int i){
        if(model instanceof MMGPBillManageModel){
            Boolean islazyload = ((MMGPBillManageModel) model).getIsLazyLoad().get(i);
            if (islazyload == true) {
                return true;
            } else {
                ((MMGPBillManageModel) model).getIsLazyLoad().set(i, true);
                return false;
            }
        }
        return false;
    }
}

