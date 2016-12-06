package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.model.HierachicalDataAppModel;

/**
 * 
 * <b> �����ӱ����ͽڵ㱣��������ť  </b>
 * <p>
 *     ��ϸ��������
 * </p>
 * @since: 
 * ��������:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeSaveAddAction extends MMGPSaveAddAction {

    /**
     * ����ģ��
     */
    private HierachicalDataAppModel treeModel = null;
    
    
    @Override
    public void doAction(ActionEvent e) throws Exception {
        //��ȡ����
        Object value = getEditor().getValue();
        //У��
        validate(value);
        //����
        value = getModel().add(value);
        //��ʾ�ɹ�
        showSuccessInfo();
        //����
        getAddAction().doAction(e);
        //ͬ����ģ��
        treeModel.directlyAdd(value);
    }
    
    
    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
    }
}
