package nc.ui.mmgp.uif2.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pubapp.uif2app.view.BatchBillTable;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AppEventConst;
import nc.vo.util.ManageModeUtil;

public class MMGPBatchBillTable extends BatchBillTable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6061921451422549995L;

    @Override
    public void initUI() {
        super.initUI();
    }

    @Override
    public void handleEvent(AppEvent event) {
        super.handleEvent(event);
        /* Jul 8, 2013 wangweir UE:����༭���л�����һ���ɱ༭�ֶ� Begin */
        if (event.getType() == AppEventConst.UISTATE_CHANGED) {
            // �Ǳ༭���༭̬
            if (this.getModel().getUiState() == UIState.EDIT) {
                // ���б༭������ѡ���пɱ༭
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        getBillCardPanel().transferFocusToFirstEditItem();
                        ((BillScrollPane) billCardPanel.getBodySelectedScrollPane()).getTable().requestFocus();
                    }
                });
            }
        }
    }

    @Override
    protected void onEdit() {
        super.onEdit();
    }

    @Override
    protected void synchornizeDataFromModel() {
        super.synchornizeDataFromModel();
        renderTable();
    }

    /**
     * �ܿ�ģʽ������ʾ���Ƽ��༭�Կ���
     */
    private void renderTable() {
        BillModel model = this.billCardPanel.getBillModel();
        int colCount = model.getColumnCount();
        List<Integer> rowList = new ArrayList<Integer>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object obj = model.getBodyValueRowVO(i, getVoClassName());
            if (!isEditable(obj)) {
                for (int j = 0; j < colCount; j++) {
                    model.setBackground(ManageModeUtil.NE_BG_COLOR, i, j);
                }
                rowList.add(Integer.valueOf(i));
            }
        }
        model.setNotEditAllowedRows(changeToIntArray(rowList));
    }

    /**
     * @param rowList
     * @return
     */
    private int[] changeToIntArray(List<Integer> rowList) {
        int[] unNecessaryRows = null;
        if (rowList.size() > 0) {
            unNecessaryRows = new int[rowList.size()];
            for (int i = 0; i < unNecessaryRows.length; i++) {
                unNecessaryRows[i] = rowList.get(i).intValue();
            }
        }
        return unNecessaryRows;
    }

    /**
     * �Ƿ�ɱ༭
     * 
     * @param obj
     * @return
     */
    protected boolean isEditable(Object obj) {
        return ManageModeUtil.manageable(obj, getModel().getContext());
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.editor.BatchBillTable#getVoClassName()
     */
    @Override
    public String getVoClassName() {
        if (super.getVoClassName() == null) {
            this.setVoClassName(MMGPMetaUtils.getClassFullName(this.getModel().getContext()));
        }
        return super.getVoClassName();
    }

}
