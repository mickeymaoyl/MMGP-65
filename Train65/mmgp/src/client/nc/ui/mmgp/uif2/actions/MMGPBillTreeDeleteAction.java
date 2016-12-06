package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.model.HierachicalDataAppModel;

/**
 * 
 * <b> �����ӱ����ͽڵ�ɾ����ť </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since: 
 * ��������:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeDeleteAction extends MMGPDeleteAction {

    /**
     * ����ģ��
     */
    private HierachicalDataAppModel treeModel = null;

    @Override
    public void doAction(ActionEvent e) throws Exception {
        super.doAction(e);
        deleteFromTree();
    }
    
    /**
     * ������ɾ��
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
