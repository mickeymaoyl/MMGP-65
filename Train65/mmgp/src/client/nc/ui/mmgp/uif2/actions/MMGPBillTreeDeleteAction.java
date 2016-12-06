package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.model.HierachicalDataAppModel;

/**
 * 
 * <b> 树主子表档案型节点删除按钮 </b>
 * <p>
 *     详细描述功能
 * </p>
 * @since: 
 * 创建日期:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeDeleteAction extends MMGPDeleteAction {

    /**
     * 左树模型
     */
    private HierachicalDataAppModel treeModel = null;

    @Override
    public void doAction(ActionEvent e) throws Exception {
        super.doAction(e);
        deleteFromTree();
    }
    
    /**
     * 从树上删除
     * @throws Exception 
     */
    private void deleteFromTree() throws Exception {
        this.getTreeModel().delete();
    }

    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
    }

    
}
