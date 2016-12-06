package nc.ui.mmgp.flexgant.model;

import com.dlsc.flexgantt.model.dateline.TimeGranularity;

import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.pubapp.gantt.ui.treetable.GantItemContainer;
import nc.ui.pubapp.gantt.ui.treetable.IGantTableColumns;

public class MMGPGantContext extends AppGantContext {

	private GantItemContainer itemContainer ;

	public MMGPGantContext() {

	}

	private IGantTableColumns initGantItems;
	
	public void init() {
		
//		this.setValueAdapter(new nc.ui.flexgant.pm.gant.PlanGantItemsAdapter());
//		this.setNodeValueAdapter(new PlanGantItemsAdapter());
		this.setNeedRelationShip(true);
		this.setDatelineLeftRange(TimeGranularity.DAY_SHORT);
		this.setDatelineRightRange(TimeGranularity.YEAR);
		this.setCoordinateLineTimeGranularities(new TimeGranularity[] {
				TimeGranularity.DAY_SHORT, TimeGranularity.WEEK,
				TimeGranularity.MONTH_SHORT, TimeGranularity.YEAR });
		this.setEventlineTimeGranularities(new TimeGranularity[] {
				TimeGranularity.DAY_SHORT, TimeGranularity.WEEK,
				TimeGranularity.MONTH_SHORT, TimeGranularity.YEAR });
	}

	/**
	 * 甘特图表格式
	 */
	public void setGantTableColumns(IGantTableColumns gantTableColumns) {
		itemContainer.setGantTableColumns(gantTableColumns);

	}


	public GantItemContainer getItemContainer() {
		return itemContainer;
	}

	public void setItemContainer(GantItemContainer itemContainer) {
		this.itemContainer = itemContainer;
	}

    public IGantTableColumns getInitGantItems() {
        return initGantItems;
    }

    public void setInitGantItems(IGantTableColumns initGantItems) {
        this.initGantItems = initGantItems;
        this.setGantTableColumns(initGantItems);
    }

}
