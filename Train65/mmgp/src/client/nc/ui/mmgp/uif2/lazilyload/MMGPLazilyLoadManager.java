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
 * <b> ���������ش����� </b>
 * <p>
 * ��дpubapp�������ش�����,��ҪĿ��Ϊ�˴����ͷ��ѡ�¼�����������ѡ���еı�������<br/>
 * �����һ�ε����ѯ��Ĭ��ѡ�е�һ�����ݣ�ֻ���ص�һ�б�ͷ�ı������ݡ������סctrl����shift��Ȼ���ѡ����ѡ�������в�����ر��塣
 * </p>
 * 
 * @since: ��������:Aug 9, 2014
 * @author:liwsh
 */
public class MMGPLazilyLoadManager extends LazilyLoadManager {

    private BillManageModel mmgpmodel;

    @Override
    public void setModel(BillManageModel billManageModel) {

        mmgpmodel = billManageModel;

        IBean bean = MMGPMetaUtils.getIBean(billManageModel.getContext());
        // ֻ�����ӱ�ź�ʹ
        if (bean.getBeanStyle().getStyle() == BeanStyleEnum.AGGVO_HEAD
            || bean.getBeanStyle().getStyle() == BeanStyleEnum.EXTAGGVO_HEAD) {

            super.setModel(billManageModel);

            billManageModel.addAppEventListener(new mutiSelectedEventListener(billManageModel));
        }
    }

    /**
     * ���б��嶼û�����ݲ������أ���Ϊ��Щ���ݴ���û�����ݵı���
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
            //gaotx mmgpModel���ܲ������ǵ�model�����Բ���ֱ����ô����
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

            // ֻ������һ���ط������б��嶼û�����ݲ������أ���Ϊ��Щ���ݴ���û�����ݵı���
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
     * <b> �����ͷ��ѡ�¼� </b>
     * <p>
     * ��Ҫ���ڼ�������ѡ���еı������ݣ���Ϊ����������أ��տ�ʼ֮����ص�һ�еı������ݡ�
     * </p>
     * 
     * @since: ��������:Aug 9, 2014
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
         * ��ȡ���б���ҳǩ��Ӧ�� Class
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
         * ��ȡѡ������
         * 
         * @return
         */
        private IBill[] getSelectedDatas() {
            Object[] billObjs = this.billManageModel.getSelectedOperaDatas();

            if (MMArrayUtil.isEmpty(billObjs)) {
                return null;
            }

            // ���ֻѡ��һ�����ݣ��ӷ���getSelectedData()��ȡ���ݣ���ΪgetSelectedOperaDatas()���滹�п����Ǿ����ݡ�
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
         * ��������ѡ�����ݵı���������
         * 
         * @param childrenClz
         *        ����VO class����
         * @param bills
         *        ��ͷѡ������
         */
        public void loadAllChildren(List<Class< ? extends ISuperVO>> childrenClz,
                                    IBill[] bills) {

            if (childrenClz == null || childrenClz.size() == 0) return;

            Map<IBill, List<Class< ? extends ISuperVO>>> needLoadChildrenMap =
                    new HashMap<IBill, List<Class< ? extends ISuperVO>>>();

            if (bills == null || bills.length == 0) {
                return;
            }

            // �б��岻��Ҫ�����ر���ĵ���
            List<IBill> hasChildrenBillList = new ArrayList<IBill>();
            for (IBill bill : bills) {
                if (bill == null) {
                    continue;
                }
                int i = billManageModel.findBusinessData(bill);
                //gaotx mmgpModel���ܲ������ǵ�model�����Բ���ֱ����ô����
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

                // ���б��嶼û�����ݲ������أ���Ϊ��Щ���ݴ���û�����ݵı���
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
                     * �����������صĵ���Ҳ��ȥ����ģ�͡�<br\>
                     * ԭ�����£�1. �û�ѡ�е�1�У�2.�û���סctrlѡ�е�3�У�3. ������������ʱ��loadBillsֻ��һ�����ݣ���Ϊֻ�е�3����Ҫ���ر���<br\>
                     * 4. �����loadBillsֱ�Ӵ���directlyUpdate���ᵼ�·����ڲ����õ�3��Ϊѡ���У����µ�һ�и���ѡ����ʧ��
                     */
                    this.billManageModel.directlyUpdate(hasChildrenBillList.toArray(new IBill[0]));

                }
            } catch (Exception e) {
                ExceptionUtils.wrappException(e);
            }
        }

    }
    
    /**
     * �Ƿ��Ѿ�������
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

