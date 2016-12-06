package nc.ui.mmgp.uif2.view.value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.view.value.IBlankChildrenFilter;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * 可以根据设置判断表体行是否为空，是否要过滤掉。加的处理是，空行直接在界面上删除，validate空值得时候，从画面取值就不会取空行了 适用于表体有checkbox的情况，checkbox默认返回为N
 * 
 * @author lvyou
 */
public class MMGPMultiFieldsBlankChildrenFilter implements IBlankChildrenFilter {
    private Map<String, List<String>> filterMap;

    private boolean isNullAssertByOr = true;

    // private void filledShareCode(BillCardPanel cardPanel){
    // for (String curCode : getFilterMap().keySet()) {
    // String[] shareCodes=getShareCode(curCode,cardPanel);
    // if(shareCodes.length>0){
    // for(String sharecode:shareCodes){
    // getFilterMap().put(sharecode, getFilterMap().get(curCode));
    // }
    // }
    // }
    //
    // }
    /**
     * 过滤空子表VO，获取属于空行的子VO索引
     * 
     * @param 需要过滤空行的cardpanel
     *        ，待过滤空子表的VO
     * @return 过滤掉的子VO的序号Map
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Integer[]> filter(BillCardPanel cardPanel,
                                         Object obj) {
        Map<String, Integer[]> nullRowMap = new HashMap<String, Integer[]>();
        if (this.getFilterMap() == null || this.getFilterMap().size() == 0 || !(obj instanceof IBill)) {
            return nullRowMap;
        }

        for (String curCode : this.getFilterMap().keySet()) {
            // List<Integer> nullRowList = new ArrayList<Integer>();//这list好像返回值没有人用
            List<ISuperVO> newChildren = new ArrayList<ISuperVO>();
            BillTabVO tabVO = cardPanel.getBillData().getTabVO(IBillItem.BODY, curCode);
            if (tabVO.getBasetab() != null) {
                tabVO = cardPanel.getBillData().getTabVO(IBillItem.BODY, tabVO.getBasetab());
            }
            Class< ? extends ISuperVO> childClz = null;
            try {

                childClz =
                        (Class< ? extends ISuperVO>) Class.forName(tabVO
                            .getBillMetaDataBusinessEntity()
                            .getFullClassName());
            } catch (ClassNotFoundException e) {
                Logger.error(e.getMessage(), e);
            }
            ISuperVO[] bodyVos = ((IBill) obj).getChildren(childClz);
            if (bodyVos != null && bodyVos.length > 0) {
                List<Integer> deleteRows = new ArrayList<Integer>();

                for (int row = 0; row < bodyVos.length; row++) {
                    // 只要有一个值为空，就认为是空行
                    if (this.isNullAssertByOr()) {
                        boolean isRowNull = false;

                        for (String column : this.getFilterMap().get(curCode)) {
                            Object value = bodyVos[row].getAttributeValue(column);
                            if (null == value || "".equals(value.toString().trim())) {
                                isRowNull = true;
                                break;
                            }
                        }
                        if (isRowNull) {
                            deleteRows.add(row);
                            // nullRowList.add(Integer.valueOf(row));
                            continue;
                        }
                        newChildren.add(bodyVos[row]);
                    }
                    // 只要有一个值不为空，就认为不是空行
                    else {
                        boolean isRowNull = true;

                        for (String column : this.getFilterMap().get(curCode)) {
                            Object value = bodyVos[row].getAttributeValue(column);
                            if (!(null == value || "".equals(value.toString().trim()))) {
                                isRowNull = false;
                                break;
                            }
                        }
                        if (isRowNull) {
                            deleteRows.add(row);
                            // nullRowList.add(Integer.valueOf(row));
                            continue;
                        }
                        newChildren.add(bodyVos[row]);

                    }
                }
                ((IBill) obj).setChildren(
                    childClz,
                    newChildren.toArray((ISuperVO[]) Array.newInstance(bodyVos[0].getClass(), 0)));
                // nullRowMap.put(curCode, nullRowList.toArray(new Integer[0]));

                // 还要把画面上的空白行业给删了
                int[] delRows = new int[deleteRows.size()];
                for (int i = 0; i < deleteRows.size(); i++) {
                    delRows[i] = deleteRows.get(i);
                }
                
                /* May 23, 2013 wangweir 修改原因 Begin */
                if (delRows != null && delRows.length != 0) {
                    BillScrollPane bsp = cardPanel.getBodyPanel(curCode);
                    bsp.delLine(delRows);
                }
                /* May 23, 2013 wangweir End */

                // 因为现在校验用平台的方法(见BillForm的validateValue方法)，在过滤表体行时需要删除空行，
                // 所以不添加共享页签记录 by yinyxa 2011-07-11
                // String[] shareCodes = this.getShareCode(curCode, cardPanel);
                // if (shareCodes.length > 0) {
                // for (String sharecode : shareCodes) {
                // nullRowMap.put(sharecode, nullRowList.toArray(new Integer[0]));
                // }
                // }
            }
        }
        return nullRowMap;
    }

    public Map<String, List<String>> getFilterMap() {
        return this.filterMap;
    }

    public String[] getShareCode(String baseCode,
                                 BillCardPanel cardPanel) {
        List<String> shareCode = new ArrayList<String>();
        String[] tabCodes = cardPanel.getBillData().getBodyTableCodes();
        for (String code : tabCodes) {
            BillTabVO tabVO = cardPanel.getBillData().getTabVO(IBillItem.BODY, code);
            if (baseCode.equals(tabVO.getBasetab())) {
                shareCode.add(code);
            }
        }
        return shareCode.toArray(new String[0]);
    }

    public boolean isNullAssertByOr() {
        return this.isNullAssertByOr;
    }

    public void setFilterMap(Map<String, List<String>> filterMap) {
        this.filterMap = filterMap;
    }

    public void setIsNullAssertByOr(boolean nullAssertByOr) {
        this.isNullAssertByOr = nullAssertByOr;
    }
}
