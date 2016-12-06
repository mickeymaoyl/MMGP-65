package nc.ui.mmgp.flexgant.treetable.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;

import nc.ui.bd.ref.IRefConst;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.textfield.IUITextFieldNegativeNumShowType;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.gantt.ui.treetable.AppTreeTableColumn;
import nc.vo.bill.pub.BillUtil;
import nc.vo.pub.lang.UFDouble;

import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

public class MMGPUFDoubleEditor extends AbstractCellEditor implements ITreeTableCellEditor {
    private JComponent editorComponent;

    private EditorDelegate delegate;

    public MMGPUFDoubleEditor() {
        final UIRefPane refPane = new UIRefPane();
        refPane.setAdjustHight(true);
        refPane.setFormat(false);
        refPane.setTextType(UITextType.TextDbl);
        refPane.setButtonVisible(false);
        refPane.setMaxLength(28);

        editorComponent = refPane;
        delegate = new EditorDelegate(refPane);
    }

    public MMGPUFDoubleEditor(MMGPGantItem gantitem) {
        this();
        UIRefPane refPane = (UIRefPane) editorComponent;
        if (gantitem.getNumberFormat() != null) {
            Double d = gantitem.getNumberFormat().getMinValue();
            if (d == null) {
                d =
                        new UFDouble(-1
                            * (Math.pow(10, (gantitem.getLength() - gantitem.getDecimalDigits())) - 0.00000001))
                            .doubleValue();
            }
            refPane.setMinValue(d.doubleValue());
            d = gantitem.getNumberFormat().getMaxValue();
            if (d == null) {
                d =
                        new UFDouble(Math.pow(10, (gantitem.getLength() - gantitem.getDecimalDigits())) - 0.00000001)
                            .doubleValue();
            }
            refPane.setMaxValue(d.doubleValue());
        }

        refPane.setNumPoint(gantitem.getDecimalDigits());
    }

    public Component getComponent() {
        return editorComponent;
    }

    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    class EditorDelegate implements ActionListener, FocusListener {

        private UIRefPane refPane;

        public EditorDelegate(UIRefPane refPane) {
            this.refPane = refPane;
            // refPane.getUITextField().addActionListener(this);
            refPane.getUITextField().addFocusListener(this);
            // refPane.addFocusListener(this);
        }

        public Object getCellEditorValue() {
            Object valueObj = refPane.getValueObj();
            return valueObj;
        }

        public void setValue(Object value) {
            refPane.setValueObj(value);
        }

        @Override
        public void focusGained(FocusEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!e.isTemporary()
                && editorComponent != null
                && editorComponent.getParent() instanceof MMGPGantTreetable) {
                MMGPUFDoubleEditor.this.stopCellEditing();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (editorComponent != null && editorComponent.getParent() instanceof MMGPGantTreetable) {
                MMGPUFDoubleEditor.this.stopCellEditing();
            }
        }

        public void stopCellEditing() {
            refPane.stopEditing();

        }

        public boolean shouldSelectCell(EventObject anEvent) {
            if (anEvent == null) editorComponent.requestFocus();
            return true;
        }

    }

    @Override
    public Component getTreeTableCellEditorComponent(TreeTable tree,
                                                     Object value,
                                                     boolean selected,
                                                     int row,
                                                     int column) {
        AppTreeTableColumn col = (AppTreeTableColumn) tree.getColumn(column);
        int digit = ((MMGPGantItem) col.getItem()).getDecimalDigits();
        ((UIRefPane) editorComponent).setNumPoint(digit);
        delegate.setValue(value);
        return editorComponent;
    }

    /**
     * Calls <code>fireEditingStopped</code> and returns true.
     * 
     * @return true
     */
    public boolean stopCellEditing() {

        delegate.stopCellEditing();
        fireEditingStopped();
        return true;
    }

    /**
     * Returns true.
     * 
     * @param anEvent
     *        an event object
     * @return true
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }
}
