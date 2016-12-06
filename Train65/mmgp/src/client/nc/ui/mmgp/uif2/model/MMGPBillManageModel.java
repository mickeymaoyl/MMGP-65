package nc.ui.mmgp.uif2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;

/**
 * 扩展uap。
 * 
 * @author wangweiu
 */
public class MMGPBillManageModel extends BillManageModel {

    @Override
    public void addAppEventListener(AppEventListener l) {
        super.addAppEventListener(l);
    }

    @Override
    public AppUiState getAppUiState() {
        return super.getAppUiState();
    }

    @Override
    public UIState getUiState() {
        return super.getUiState();
    }

    @Override
    public void setAppUiState(AppUiState appUiState) {
        super.setAppUiState(appUiState);
    }

    @Override
    public void setSelectedOperaRows(int[] selectedRows) {
        super.setSelectedOperaRows(selectedRows);
    }

    @Override
    public void setSelectedRow(int selectedRow) {
        super.setSelectedRow(selectedRow);
    }

    @Override
    public void setSupportLazilyLoad(boolean supportLazilyLoad) {
        super.setSupportLazilyLoad(supportLazilyLoad);
    }

    @Override
    public void setUiState(UIState uiState) {
        super.setUiState(uiState);
    }

    private List<Boolean> isLazyLoad = new ArrayList<Boolean>();

    @Override
    public void directlyAdd(Object obj) {
        isLazyLoad.add(true);
        super.directlyAdd(obj);

    }

    public void directlyDelete(Object[] objs) throws Exception {
        if (objs == null || objs.length == 0) return;

        // modify by liwsh 2014-11-20 先降序排列，然后再删除，不然会数组越界
        List<Integer> indexList = new ArrayList<Integer>();
        for (int i = 0; i < objs.length; i++) {
            int index = findBusinessData(objs[i]);
            indexList.add(new Integer(index));
        }

        Collections.sort(indexList, new IntegerCompiler());
        for (int i = indexList.size() - 1; i >= 0; i--) {
            isLazyLoad.remove(indexList.get(i).intValue());
        }

        super.directlyDelete(objs);
    }

    class IntegerCompiler implements Comparator<Integer> {

        @Override
        public int compare(Integer o1,
                           Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public void initModel(Object data) {
        resetIsLazyLoad(data); 
        super.initModel(data);
    }

    /**
     * 重新设置isLazyLoad标志
     * 
     * @param data
     */
    protected void resetIsLazyLoad(Object data) {
        this.isLazyLoad.clear();
        if (data == null) {
        } else if (data.getClass().isArray()) {
            Object[] objs = (Object[]) data;
            if (objs.length == 1) {
                isLazyLoad.add(true);
            } else if (objs.length > 1) {
                isLazyLoad.add(true);
                for (int i = 1; i < objs.length; i++) {
                    isLazyLoad.add(false);
                }
            }
        } else {
            isLazyLoad.add(true);
        }
    }

    public List<Boolean> getIsLazyLoad() {
        return isLazyLoad;
    }

    public void setIsLazyLoad(List<Boolean> isLazyLoad) {
        this.isLazyLoad = isLazyLoad;
    }

    static class ModelDataRowNo {

        Boolean isLazyLoad;

        Object data;

        int rowNo;

        String datapk;

        public ModelDataRowNo(Object data,
                              int rowNo,
                              String datapk,
                              Boolean isLazyLoad) {
            super();
            this.isLazyLoad = isLazyLoad;
            this.data = data;
            this.rowNo = rowNo;
            this.datapk = datapk;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public int getRowNo() {
            return rowNo;
        }

        public void setRowNo(int rowNo) {
            this.rowNo = rowNo;
        }

        public String getDatapk() {
            return this.datapk;
        }

        public Boolean getIsLazyLoad() {
            return isLazyLoad;
        }
    }

    List<ModelDataRowNo> listToSort = new ArrayList<ModelDataRowNo>();

    @SuppressWarnings("rawtypes")
    public List getRelaSortObject() {
        listToSort.clear();
        for (int i = 0; i < getData().size(); i++) {
            listToSort.add(new ModelDataRowNo(getData().get(i), i, this.datapks.get(i), isLazyLoad.get(i)));
        }
        return listToSort;
    }

    @SuppressWarnings("unchecked")
    public void afterSort(String key) {
        HashSet<Integer> oldSelecteRowIndexes = new HashSet<Integer>();
        if (getSelectedOperaRows() != null) {
            oldSelecteRowIndexes.addAll(Arrays.asList(getSelectedOperaRows()));
        }
        List<Integer> newSelecteRowIndexes = new ArrayList<Integer>();

        getData().clear();
        datapks.clear();
        isLazyLoad.clear();
        boolean selectedRowAdjusted = false;
        for (int i = 0; i < listToSort.size(); i++) {
            getData().add(listToSort.get(i).getData());
            datapks.add(listToSort.get(i).getDatapk());
            isLazyLoad.add(listToSort.get(i).getIsLazyLoad());
            if (oldSelecteRowIndexes.contains(listToSort.get(i).getRowNo())) {
                newSelecteRowIndexes.add(i);
            }
            if (!selectedRowAdjusted && this.getSelectedRow() == listToSort.get(i).getRowNo()) {
                selectedRowAdjusted = true;
                this.setSelectedRowWithoutEvent(i);
            }
        }

        clearSelectedOperaRows();
        Collections.sort(newSelecteRowIndexes);
        int[] selectArray = new int[newSelecteRowIndexes.size()];
        for (int i = 0; i < newSelecteRowIndexes.size(); i++) {
            selectArray[i] = newSelecteRowIndexes.get(i);
        }
        this.setSelectedOperaRowsWithoutEvent(selectArray);
        // 应该发出排序后事件通知
    }

}
