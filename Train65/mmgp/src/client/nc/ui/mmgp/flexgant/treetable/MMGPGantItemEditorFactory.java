package nc.ui.mmgp.flexgant.treetable;

import nc.ui.mmgp.flexgant.treetable.editor.DateEditor;
import nc.ui.mmgp.flexgant.treetable.editor.MMGPGantMultiLangEditor;
import nc.ui.mmgp.flexgant.treetable.editor.MMGPGanttUIRefEditor;
import nc.ui.mmgp.flexgant.treetable.editor.MMGPUFDoubleEditor;
import nc.ui.mmgp.flexgant.treetable.render.DateRender;
import nc.ui.mmgp.flexgant.treetable.render.MMGPGantMultiLangRender;
import nc.ui.mmgp.flexgant.treetable.render.MMGPUFDoubleRender;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.gantt.ui.treetable.editor.GanttMDEnumEditor;
import nc.ui.pubapp.gantt.ui.treetable.editor.GanttUICheckBoxEditor;
import nc.ui.pubapp.gantt.ui.treetable.editor.GanttUIRefEditor;
import nc.ui.pubapp.gantt.ui.treetable.render.GanttMDEnumRender;
import nc.ui.pubapp.gantt.ui.treetable.render.GanttUFCheckboxRender;
import nc.ui.pubapp.gantt.ui.treetable.render.GanttUIRefRender;

import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.ITreeTableCellRenderer;

/**
 * @Description: TODO
 *               <p>
 *               œÍœ∏π¶ƒ‹√Ë ˆ
 *               </p>
 * @data:2014-5-20…œŒÁ10:43:50
 * @author: tangxya
 */
public class MMGPGantItemEditorFactory {

	public static ITreeTableCellEditor creatTreeTableCellEditor(
			MMGPGantItem gantitem) {
		ITreeTableCellEditor editor = null;
		switch (gantitem.getDataType()) {
		case IBillItem.UFREF:
			editor = new MMGPGanttUIRefEditor(gantitem.getRefModel());
			break;
		case IBillItem.COMBO:
			MMGPGantMDEnumUtil enumutil = new MMGPGantMDEnumUtil(gantitem);
			editor = new GanttMDEnumEditor(enumutil.getGantMDEnumData());
			break;
		case IBillItem.DATE:
			editor = new DateEditor();
			break;
		case IBillItem.BOOLEAN:
		    editor = new GanttUICheckBoxEditor();
		    break;
		case IBillItem.MULTILANGTEXT:
            editor = new MMGPGantMultiLangEditor();
            break;
		case IBillItem.DECIMAL:
		    editor = new MMGPUFDoubleEditor(gantitem);
		    break;
		}
		return editor;
	}

	public static ITreeTableCellRenderer creatTreeTableCellRender(
			MMGPGantItem gantitem) {
		ITreeTableCellRenderer renderer = null;
		switch (gantitem.getDataType()) {
		case IBillItem.UFREF:
			renderer = new GanttUIRefRender(gantitem.getRefModel());
			break;
		case IBillItem.COMBO:
			MMGPGantMDEnumUtil enumutil = new MMGPGantMDEnumUtil(gantitem);
			renderer = new GanttMDEnumRender(enumutil.getGantMDEnumData());
			break;
		case IBillItem.DATE:
			renderer = new DateRender();
			break;
		case IBillItem.BOOLEAN:
		    renderer = new GanttUFCheckboxRender();
            break;
		case IBillItem.MULTILANGTEXT:
		    renderer = new MMGPGantMultiLangRender();
            break;
		case IBillItem.DECIMAL:
		    renderer = new MMGPUFDoubleRender();
            break;
		}
		return renderer;
	}

}
