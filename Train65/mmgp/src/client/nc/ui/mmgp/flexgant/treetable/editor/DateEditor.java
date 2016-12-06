package nc.ui.mmgp.flexgant.treetable.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;

import nc.ui.bd.ref.IRefConst;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.lang.UFDate;

import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

/**
 * Date数据类型编辑器
 * 
 * @author jingli
 */
public class DateEditor extends AbstractCellEditor implements ITreeTableCellEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JComponent editorComponent;

    private EditorDelegate delegate;

    public DateEditor() {
        final UIRefPane refPane = new UIRefPane(IRefConst.REFNODENAME_CALENDAR);
        editorComponent = refPane;
        delegate = new EditorDelegate(refPane);
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
            if (valueObj instanceof UFDate) {
                return valueObj;
            }
            return valueObj;
        }

        public void setValue(Object value) {
            if (value == null) {
                refPane.setText(null);
            } else {
                if (value instanceof Date) {
                    UFDate date = UFDate.getDate((Date) value);
                    refPane.setValueObj(date);
                } else if (value instanceof UFDate) {
                    refPane.setValueObj(value);
                }
            }
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
                DateEditor.this.stopCellEditing();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (editorComponent != null && editorComponent.getParent() instanceof MMGPGantTreetable) {
                DateEditor.this.stopCellEditing();
            }
        }

        /**
         * Calls <code>fireEditingStopped</code> and returns true.
         * 
         * @return true
         */
        public boolean stopCellEditing() {
            if (refPane.isEnabled() && refPane.isEditable()) {
                refPane.stopEditing();
            }
            return true;
        }
    }

    @Override
    public Component getTreeTableCellEditorComponent(TreeTable tree,
                                                     Object value,
                                                     boolean selected,
                                                     int row,
                                                     int column) {
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

}
