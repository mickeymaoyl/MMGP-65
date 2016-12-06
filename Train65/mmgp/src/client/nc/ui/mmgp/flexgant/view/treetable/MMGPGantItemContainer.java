package nc.ui.mmgp.flexgant.view.treetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import nc.ui.pubapp.gantt.ui.treetable.GantItem;
import nc.ui.pubapp.gantt.ui.treetable.GantItemContainer;
import nc.ui.pubapp.gantt.ui.treetable.IGantItem;
import nc.ui.pubapp.gantt.ui.treetable.IGantTableColumns;
/**
 * ¸ÊÌØÍ¼ gantitemÈÝÆ÷
 * @author tangxya
 *
 */
public class MMGPGantItemContainer extends GantItemContainer {
	private static class GantItemSort implements Comparator<IGantItem> {
		@Override
		public int compare(IGantItem o1, IGantItem o2) {
			return o1.getShowOrder().intValue() > o2.getShowOrder().intValue() ? 1
					: -1;
		}

	}

	protected List<IGantItem> extendGantItem = new ArrayList<IGantItem>();

	protected IGantTableColumns gantTableColumns;

	public IGantTableColumns getGantTableColumns() {
		return gantTableColumns;
	}

	public void setGantTableColumns(IGantTableColumns gantTableColumns) {
		this.gantTableColumns = gantTableColumns;
		this.extendGantItem = gantTableColumns.getGantColumns();
	}

	public void removeItem(IGantItem item) {
		this.extendGantItem.remove(item);
	}

	public void addItem(IGantItem item) {
		this.extendGantItem.add(item);
	}

	public IGantItem[] getAllItems() {
		if (this.extendGantItem == null) {
			return null;
		}
		List<IGantItem> itemsList = new ArrayList<IGantItem>();
		// itemsList.addAll(Arrays.asList(this.gantItems));
		itemsList.addAll(this.extendGantItem);
		IGantItem[] items = itemsList.toArray(new GantItem[0]);
		Arrays.sort(items, new GantItemSort());
		return items;

	}

	public void setGantItems(List<IGantItem> gantitems) {
		extendGantItem = gantitems;
	}

	public boolean containsItem(IGantItem item) {
		return extendGantItem.contains(item);
	}
}
