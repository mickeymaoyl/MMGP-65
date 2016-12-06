package nc.ui.mmgp.flexgant.view.treetable;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.tree.TreePath;

import nc.ui.mmgp.flexgant.actions.MMGPGantMenuAction;
import nc.ui.mmgp.flexgant.actions.MMGPGantSeparatorAction;
import nc.ui.pubapp.gantt.model.AppGantContext;

import com.dlsc.flexgantt.model.treetable.TreeTableColumn;
import com.dlsc.flexgantt.swing.action.treetable.AbstractTreeTableAction;
import com.dlsc.flexgantt.swing.treetable.DefaultTreeTableMenuProvider;
import com.dlsc.flexgantt.swing.treetable.TreeTable;
import com.dlsc.flexgantt.swing.util.MenuCleaner;


/**
 * @author wangfan3
 * 
 * @see nc.ui.pubapp.gantt.ui.treetable.AppGantTreeTableMenuProvider
 * 
 * ¸ÊÌØÍ¼×óÊ÷±í ÓÒ¼ü²Ëµ¥
 *
 */
public class MMGPGantTreeTableMenuProvider extends DefaultTreeTableMenuProvider {

	private AppGantContext context;

	public MMGPGantTreeTableMenuProvider(AppGantContext context) {
		super();
		this.context = context;
	}
	
	private Map<Integer, AbstractAction> numberToActionMap;
	
	private List<AbstractAction> actionList;
	
	public void initUI(){
	}
	
	@SuppressWarnings("rawtypes")
	public JPopupMenu getPopupMenu(TreeTable table, MouseEvent e,
			TreePath treePath, TreeTableColumn column) {
		removeAll();
		if (!context.isTreetablePopuMenuAllActionsEnable()) {
			for (AbstractAction action : numberToActionMap.values()) {
				if(!(action instanceof MMGPGantSeparatorAction)){
					action.setEnabled(false);
				}
			}
		} else {
			Map<Integer, Boolean> map = context
					.getTreetablePopuMenuActionsEnableInfoMap();
			if (map.size() > 0) {
				Set<Entry<Integer, Boolean>> entrySet = map.entrySet();
				for (Entry<Integer, Boolean> entry : entrySet) {
					Integer key = entry.getKey();
					AbstractAction action = numberToActionMap.get(key);
					if (action != null&&!(action instanceof MMGPGantSeparatorAction)) {
						action.setEnabled(map.get(key).booleanValue());
					}
				}
			}
		}
//		List<Integer>  Lists =new ArrayList<Integer>(numberToActionMap.keySet());
//		Collections.sort(Lists);
		for (AbstractAction action : actionList) {
			if(action instanceof MMGPGantSeparatorAction){
				add(new JSeparator());
			} else {
				
				if(action instanceof MMGPGantMenuAction){
					JMenu s = addMenu(action);
					for(AbstractTreeTableAction child :  ((MMGPGantMenuAction) action).getChild()){
						s.add(child);
					}
				}else {
					add(action);
				}
				
			}
		}
		this.updateUI();
		MenuCleaner.clean(this);
		return this;
	}

	public Map<Integer, AbstractAction> getNumberToActionMap() {
		return numberToActionMap;
	}

	public void setNumberToActionMap(Map<Integer, AbstractAction> numberToActionMap) {
		this.numberToActionMap = numberToActionMap;
	}

	protected JMenu createActionMenuComponent(Action a) {
		JMenu mi = new JMenu() {
			protected PropertyChangeListener createActionPropertyChangeListener(
					Action a) {
				PropertyChangeListener pcl = createActionChangeListener(this);
				if (pcl == null) {
					pcl = super.createActionPropertyChangeListener(a);
				}
				return pcl;
			}
		};
		mi.setHorizontalTextPosition(JButton.TRAILING);
		mi.setVerticalTextPosition(JButton.CENTER);
		return mi;
	}

	public JMenu addMenu(Action a) {
		JMenu mi = createActionMenuComponent(a);
		mi.setAction(a);
		add(mi);
		return mi;
	}

	public List<AbstractAction> getActionList() {
		return actionList;
	}

	public void setActionList(List<AbstractAction> actionList) {
		this.actionList = actionList;
	}

}
