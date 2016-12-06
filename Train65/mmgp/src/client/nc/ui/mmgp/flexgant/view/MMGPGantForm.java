package nc.ui.mmgp.flexgant.view;

import java.awt.BorderLayout;

import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;
import nc.ui.uif2.components.BorderLayoutPanel;
import nc.ui.uif2.editor.IEditor;
import nc.ui.uif2.editor.value.IComponentValueStrategy;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.AppEventConst;

/**
 * @author wangfan3
 * 
 * Ϊ�˷�װGanttcahrt,���ڴ����м���AbstractAppModel
 *
 */
public class MMGPGantForm extends BorderLayoutPanel implements IEditor, AppEventListener {

    // ��ע��
    private MMGPGantChart gantChart = null;

    // ��ע��
    private AbstractAppModel model = null;
    
    protected IComponentValueStrategy componentValueManager =null;
    
    @Override
    public void initUI() {
        super.initUI();
        add(getGantChart(), BorderLayout.CENTER);
        this.setEditable(false);
    }
    
    public void stopEditing() {
        gantChart.stopEditing();
    }

    /**
     * �������ý����Ƿ�ɱ༭״̬
     */
    public void setEditable(boolean editable) {
        if (editable) {
            getGantChart().getTreeTable().setEditingEnabled(true);
           // getGantChart().getTreeTable().setCreationEnabled(true);
            getGantChart().setLayerContainerDragEnabled(true);
            getGantChart().getContext().setTreetablePopuMenuAllActionsEnable(true);

        } else {
            getGantChart().stopEditing();
            getGantChart().getContext().setTreetablePopuMenuAllActionsEnable(false);
            getGantChart().getTreeTable().setCreationEnabled(false);
            getGantChart().getTreeTable().setEditingEnabled(false);
            getGantChart().setLayerContainerDragEnabled(false);
        }
    }

    /* (non-Javadoc)
     * @see nc.ui.uif2.AppEventListener#handleEvent(nc.ui.uif2.AppEvent)
     * �������¼�����
     */
    @Override
    public void handleEvent(AppEvent event) {
        if (AppEventConst.UISTATE_CHANGED == event.getType()) {

            if (model.getUiState() == UIState.ADD) {
                onAdd();
            } else if (model.getUiState() == UIState.EDIT) {
                onEdit();
            } else {
                onNotEdit();
            }
        }

    }

    protected void onNotEdit() {
        setEditable(false);
        
    }

    protected void onEdit() {
        setEditable(true);
        
    }

    protected void onAdd() {

        setEditable(true);
       // billCardPanel.addNew();
       // setDefaultValue();

//        if (isRequestFocus()) {
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    billCardPanel.requestFocusInWindow();
//                    billCardPanel.transferFocusToFirstEditItem();
//                }
//            });
//        }
    }

    @Override
    public Object getValue() {
        this.getGantChart().stopEditing();
        beforeGetValue(); 
        if(componentValueManager != null)
            return componentValueManager.getValue();
        return null;
    }
    private void beforeGetValue() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(Object object) {
        if(componentValueManager != null) {
            componentValueManager.setValue(object);
//            if(isAutoExecLoadFormula())
//                execLoadFormula();
//            
//            if(isAutoExecLoadRelationItem())
//                this.getBillCardPanel().getBillData().loadLoadHeadRelation();
        }

    }

    public MMGPGantChart getGantChart() {
        return gantChart;
    }

    public void setGantChart(MMGPGantChart gantChart) {
        this.gantChart = gantChart;
    }

    public AbstractAppModel getModel() {
        return model;
    }

    public void setModel(AbstractAppModel model) {
        this.model = model;
        model.addAppEventListener(this);
    }

    public IComponentValueStrategy getComponentValueManager() {
        return componentValueManager;
    }

    public void setComponentValueManager(IComponentValueStrategy componentValueManager) {
        this.componentValueManager = componentValueManager;
    }

}
