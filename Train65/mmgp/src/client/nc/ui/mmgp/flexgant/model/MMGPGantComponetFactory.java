package nc.ui.mmgp.flexgant.model;

import nc.ui.mmgp.flexgant.view.treetable.MMGPGantTreetable;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGroupableTreeTableHeader;
import nc.ui.pubapp.gantt.model.AppGantComponentFactory;

import com.dlsc.flexgantt.model.treetable.ITreeTableModel;
import com.dlsc.flexgantt.swing.AbstractGanttChart;
import com.dlsc.flexgantt.swing.treetable.TreeTable;
import com.dlsc.flexgantt.swing.treetable.TreeTableHeader;

public class MMGPGantComponetFactory extends AppGantComponentFactory{

	private static MMGPGantComponetFactory instance;
	

	public static synchronized MMGPGantComponetFactory getInstance() {
		if (instance == null) {
			instance = new MMGPGantComponetFactory();
		}

		return instance;
	}
	
	public TreeTableHeader createTreeTableHeader(AbstractGanttChart gc) {
		return new MMGPGroupableTreeTableHeader(gc);
	}
	
	public TreeTable createTreeTable(AbstractGanttChart gc,
	                                 ITreeTableModel model) {
	    MMGPGantTreetable table = new MMGPGantTreetable(gc, model);
	    return table;
	}
}
