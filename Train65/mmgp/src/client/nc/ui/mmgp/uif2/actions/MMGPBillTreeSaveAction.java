package nc.ui.mmgp.uif2.actions;

import nc.ui.uif2.UIState;
import nc.ui.uif2.model.HierachicalDataAppModel;

/**
 * <b> �����ӱ����ͽڵ㱣�水ť </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since: ��������:Sep 18, 2014
 * @author:liwsh
 */
public class MMGPBillTreeSaveAction extends MMGPSaveAction {

    /**
     * ����ģ��
     */
    private HierachicalDataAppModel treeModel = null;

    /**
     * ����
     */
    @Override
    protected void doAddSave(Object value) throws Exception {
        //����
        Object returnObj = getModel().add(value);
        //ͬ������ģ��
        treeModel.directlyAdd(returnObj);
        //�ı�״̬
        getModel().setUiState(UIState.NOT_EDIT);
        //������������Ϊѡ������
        if(treeModel instanceof HierachicalDataAppModel){
            treeModel.setSelectedData(returnObj);
        }
    }
    
    /**
     * �޸�
     */
    @Override
    protected void doEditSave(Object value) throws Exception {
        //�޸�����
        Object returnObj = getModel().update(value);
        getModel().setUiState(UIState.NOT_EDIT);
        //ͬ������ģ��
        treeModel.directlyUpdate(returnObj);
        
        
    }
    
    public HierachicalDataAppModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(HierachicalDataAppModel treeModel) {
        this.treeModel = treeModel;
    }

}
