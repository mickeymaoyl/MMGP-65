package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.model.HierachicalDataAppModel;

/**
 * 
 * <b> 树主子表档案型节点保存新增按钮  </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since: 
 * 创建日期:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeSaveAddAction extends MMGPSaveAddAction {

    /**
     * 左树模型
     */
    private HierachicalDataAppModel treeModel = null;
    
    
    @Override
    public void doAction(ActionEvent e) throws Exception {
        //获取数据
        Object value = getEditor().getValue();
        //校验
        validate(value);
        //保存
        value = getModel().add(value);
        //显示成功
        showSuccessInfo();
        //新增
        getAddAction().doAction(e);
        //同步树模型
        treeModel.directlyAdd(value);
    }
    
    
    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
    }
}
