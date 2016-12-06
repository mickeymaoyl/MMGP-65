package nc.ui.mmgp.flexgant.treetable.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillObjectRefPane;
import nc.ui.pub.bill.IBillObject;

import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

/**
 * <p>
 * <b>参照的编辑器</b>
 * </p>
 * <i>用户实现这个类，将参照的model传进来
 * 
 * @author wangfan3
 */
public class MMGPGanttUIRefEditor extends AbstractCellEditor implements ITreeTableCellEditor {

    private static final long serialVersionUID = -7436721383877603654L;

    private JComponent editorComponent;

    private UIRefPane pane;

    private EditorDelegate delegate;

    private Object oldValue = null;

    private boolean pk = false;

    public MMGPGanttUIRefEditor(AbstractRefModel model) {

        pane = new UIRefPane();
        pane.setRefModel(model);
        editorComponent = pane;
        delegate = new EditorDelegate(pane);
    }

    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    public Component getComponent() {
        return editorComponent;
    }

    class EditorDelegate implements ActionListener, FocusListener {

        private UIRefPane refPane;

        UITextField textField;

        public EditorDelegate(UIRefPane refPane) {
            this.refPane = refPane;
            textField = refPane.getUITextField();
            refPane.getUITextField().addFocusListener(this);
        }

        public Object getCellEditorValue() {
            Object value = refPane.getRefShowName();
            if (value instanceof String && ((String) value).length() != 0)
                value = new DefaultConstEnum(refPane.getRefPK(), (String) value);
            return value;
        }

        public void setValue(Object value) {
            if (value instanceof IConstEnum) value = ((IConstEnum) value).getName();

            Object pk = getOldValue();

            if (value == null) refPane.setPK(value);

            if (pk != null && !pk.equals(value)) {
                value = pk;
                refPane.setPK((value != null) ? value.toString() : "");
            } else if (refPane instanceof BillObjectRefPane) ((BillObjectRefPane) refPane)
                .setBillObject((IBillObject) value);
            else {
                refPane.setValueObj(value);
            }

            if (!textField.hasFocus()) textField.selectAll();
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
                MMGPGanttUIRefEditor.this.stopCellEditing();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (editorComponent != null && editorComponent.getParent() instanceof MMGPGantTreetable) {
                MMGPGanttUIRefEditor.this.stopCellEditing();
            }
        }

        public void stopCellEditing() {
            if (refPane.isEnabled() && refPane.isEditable()) {
                refPane.stopEditing();
            }

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
        setOldValue(value);
        setPk(false);
        if (value instanceof IConstEnum) {
            Object pkValue = ((IConstEnum) value).getValue();
            setOldValue((String) pkValue);
            setPk(true);
        }
        delegate.setValue(value);
        return editorComponent;
    }

    /**
     * @return Returns the oldValue.
     */
    private Object getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue
     *        The oldValue to set.
     */
    private void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    private boolean isPk() {
        return pk;
    }

    /**
     * @param pk
     *        The pk to set.
     */
    private void setPk(boolean pk) {
        this.pk = pk;
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
