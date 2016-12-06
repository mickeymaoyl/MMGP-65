package nc.ui.mmgp.uif2.actions;

import nc.ui.uif2.UIState;
import nc.ui.uif2.model.HierachicalDataAppModel;

/**
 * <b> 树主子表档案型节点保存按钮 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeSaveAction extends MMGPSaveAction {

    /**
     * 左树模型
     */
    private HierachicalDataAppModel treeModel = null;

    /**
     * 新增
     */
    @Override
    protected void doAddSave(Object value) throws Exception {
        //新增
        Object returnObj = getModel().add(value);
        //同步到树模型
        treeModel.directlyAdd(returnObj);
        //改变状态
        getModel().setUiState(UIState.NOT_EDIT);
        //设置新增数据为选中数据
        if(treeModel instanceof HierachicalDataAppModel){
            treeModel.setSelectedData(returnObj);
        }
    }
    
    /**
     * 修改
     */
    @Override
    protected void doEditSave(Object value) throws Exception {
        //修改数据
        Object returnObj = getModel().update(value);
        getModel().setUiState(UIState.NOT_EDIT);
        //同步到树模型
        treeModel.directlyUpdate(returnObj);
        
        
    }
    
    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
    }

}
