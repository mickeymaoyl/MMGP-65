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

import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.pub.beans.UIMultiLangCombox;
import nc.ui.pub.beans.UITextField;
import nc.vo.ml.MultiLangContext;
import nc.vo.pub.lang.MultiLangText;

import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.TreeTable;

/**
 * @author wangfan3
 * 
 * 多语文本编辑器
 *
 */
public class MMGPGantMultiLangEditor extends AbstractCellEditor implements ITreeTableCellEditor {
    protected JComponent editorComponent;

    protected EditorDelegate delegate;

    public Component getComponent() {
        return editorComponent;
    }

    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    public MMGPGantMultiLangEditor() {
        UIMultiLangCombox multiLangCombox = new UIMultiLangCombox();
        multiLangCombox.setEnabled(true);
        editorComponent = multiLangCombox;
        delegate = new EditorDelegate(multiLangCombox);
    }

    class EditorDelegate implements ActionListener, FocusListener {
        private UIMultiLangCombox langCombox;

        public EditorDelegate(UIMultiLangCombox langCombox) {
            this.langCombox = langCombox;
            langCombox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
            UITextField textField = (UITextField) langCombox.getEditor().getEditorComponent();
            textField.addActionListener(this);
            textField.addFocusListener(this);
        }

        public void setValue(Object value) {
            MultiLangText v = null;
            if (value != null) {
                v = new MultiLangText();
                v.setText(((MultiLangText) value).getText());
                v.setText2(((MultiLangText) value).getText2());
                v.setText3(((MultiLangText) value).getText3());
                v.setText4(((MultiLangText) value).getText4());
                v.setText5(((MultiLangText) value).getText5());
                v.setText6(((MultiLangText) value).getText6());
            }

            langCombox.setMultiLangText(v);
            int currLangIndex = -1;
            if (MultiLangContext.getInstance().getCurrentLangVO() != null) {
                currLangIndex = MultiLangContext.getInstance().getCurrentLangVO().getLangseq().intValue() - 1;
            }

            // 默认第一个Item选中
            langCombox.setSelectedIndex(currLangIndex);
            UITextField textField = (UITextField) langCombox.getEditor().getEditorComponent();
            if (!textField.hasFocus()) textField.selectAll();

        }

        public boolean shouldSelectCell(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                MouseEvent e = (MouseEvent) anEvent;
                return e.getID() != MouseEvent.MOUSE_DRAGGED;
            }
            return true;
        }

        public Object getCellEditorValue() {
            return langCombox.getMultiLangText();
        }

        public boolean stopCellEditing() {
            if (langCombox.isEditable()) {
                // Commit edited value.
                langCombox.actionPerformed(new ActionEvent(MMGPGantMultiLangEditor.this, 0, ""));
            }
            return true;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (editorComponent != null && editorComponent.getParent() instanceof MMGPGantTreetable) {
                MMGPGantMultiLangEditor.this.stopCellEditing();
            }
        }

        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!e.isTemporary()
                && editorComponent != null
                && editorComponent.getParent() instanceof MMGPGantTreetable) {
                MMGPGantMultiLangEditor.this.stopCellEditing();
            }
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
