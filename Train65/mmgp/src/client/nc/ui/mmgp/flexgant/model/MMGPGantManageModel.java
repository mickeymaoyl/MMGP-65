package nc.ui.mmgp.flexgant.model;

import java.util.Vector;

import javax.swing.tree.TreePath;

import nc.ui.mmgp.flexgant.event.MMGPGantRowSelectEvent;
import nc.ui.mmgp.flexgant.view.treetable.MMGPGanttChartNode;
import nc.ui.mmgp.uif2.model.MMGPBillManageModel;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.model.AppEventConst;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;

/**
 * @Description: 甘特图的管理型模型
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-26下午2:57:24
 * @author: tangxya
 */
public class MMGPGantManageModel extends MMGPBillManageModel {

	private Vector<Object> selectdatas = new Vector<Object>();

	public void setSelectData(Object obj) {
		addSelectData(obj);
	}

	public Object getSelectedData() {
		if (MMCollectionUtil.isNotEmpty(selectdatas)) {
			return selectdatas.get(0);
		}
		return null;

	}

	/**
	 * 处理事件
	 */
	public void fireEvent(AppEvent event) {
		if (event instanceof MMGPGantRowSelectEvent) {//行选中事件
			MMGPGantRowSelectEvent rowselect = (MMGPGantRowSelectEvent) event;
			TreePath[] oldpaths = rowselect.getOldrows();
			TreePath[] paths = rowselect.getRows();
			if (MMArrayUtil.isNotEmpty(oldpaths)) {
				for (TreePath deletepath : oldpaths) {
					removeSelectData(((MMGPGanttChartNode) deletepath
							.getLastPathComponent()).getTypedUserObject());
				}
			}
			if (MMArrayUtil.isNotEmpty(paths)) {
				for (TreePath deletepath : paths) {
					addSelectData(((MMGPGanttChartNode) deletepath
							.getLastPathComponent()).getTypedUserObject());
				}
			}
			super.fireEvent(new AppEvent(AppEventConst.SELECTION_CHANGED, this,
					null));
		} else {
			super.fireEvent(event);
		}
	}

	public Object[] getSelectedOperaDatas() {
		return selectdatas.toArray();
	}

	private void removeSelectData(Object data) {
		if (selectdatas.contains(data)) {
			selectdatas.remove(data);
		}
	}

	private void addSelectData(Object data) {
		if (!selectdatas.contains(data)) {
			selectdatas.add(data);
		}
	}
}
