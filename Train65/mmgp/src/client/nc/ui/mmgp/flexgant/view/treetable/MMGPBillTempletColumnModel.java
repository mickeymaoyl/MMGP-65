package nc.ui.mmgp.flexgant.view.treetable;

import java.util.Map;

import nc.ui.ml.NCLangRes;
import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.pub.IGantTableData;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.mmgp.flexgant.treetable.MMGPGantItemEditorFactory;
import nc.ui.pubapp.gantt.model.AppGantContext;
import nc.ui.pubapp.gantt.ui.treetable.AppTreeTableColumn;
import nc.ui.pubapp.gantt.ui.treetable.GantItemContainer;
import nc.ui.pubapp.gantt.ui.treetable.IGantItem;

import com.dlsc.flexgantt.model.treetable.DefaultColumnModel;
import com.dlsc.flexgantt.model.treetable.KeyColumn;
import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.ITreeTableCellRenderer;

/**
 * @Description: 支持NC单据模板的甘特图树表模型
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-6-3下午4:25:59
 * @author: tangxya
 */
@SuppressWarnings("restriction")
public class MMGPBillTempletColumnModel extends DefaultColumnModel {

	private AppGantContext context;

	private IGantTableData userdefitemPreparator;

	private MMGPGantChartModel gantChartModel;

	public IGantTableData getUserdefitemPreparator() {
		return userdefitemPreparator;
	}

	public void setUserdefitemPreparator(
			IGantTableData userdefitemListPreparator) {
		this.userdefitemPreparator = userdefitemListPreparator;
	}

	public AppGantContext getContext() {
		return context;
	}

	public void setContext(AppGantContext context) {
		this.context = context;
	}

	public MMGPBillTempletColumnModel() {

	}

	public MMGPBillTempletColumnModel(AppGantContext context) {
		this.context = context;

	}

	protected void initTreeTableEditor() {
		IGantItem[] items = getContext().getItemContainer().getAllItems();
		Map<Class<?>, ITreeTableCellRenderer> renderMap = getContext()
				.getTypeMapRenderer();
		Map<Class<?>, ITreeTableCellEditor> coleditorMap = getContext()
				.getTypeEditorMap();

		for (IGantItem item : items) {
			MMGPGantItem gantitem = (MMGPGantItem) item;
			if (gantitem.getValueClassType() == null) {
				continue;
			}
			ITreeTableCellRenderer treerender = MMGPGantItemEditorFactory
					.creatTreeTableCellRender(gantitem);
			if (treerender != null) {
				renderMap.put(gantitem.getValueClassType(), treerender);
			}
			ITreeTableCellEditor treeeditor = MMGPGantItemEditorFactory
					.creatTreeTableCellEditor(gantitem);
			if (treeeditor != null) {
				coleditorMap.put(gantitem.getValueClassType(), treeeditor);
			}
		}

	}

	protected void processGantInfo() {
		if (getUserdefitemPreparator() != null)
			getUserdefitemPreparator().prepareGantTableData(
					this.getGantChartModel());
	}

	protected void init() {

		/*
		 * List<IGantItem> gantitems = loadTemplateData(); if
		 * (MMCollectionUtil.isEmpty(gantitems)) { return; }
		 * MMGPGantItemContainer itemContainer = (MMGPGantItemContainer) context
		 * .getItemContainer(); itemContainer.setGantItems(gantitems);
		 */

		GantItemContainer itemContainer = context.getItemContainer();
		IGantItem[] items = itemContainer.getAllItems();
		processGantInfo();
		initColumn(items);
		initTreeTableEditor();
	}

	public MMGPGantChartModel getGantChartModel() {
		return gantChartModel;
	}

	public void setGantChartModel(MMGPGantChartModel gantChartModel) {
		this.gantChartModel = gantChartModel;
	}

	public void initColumn(IGantItem[] items) {
		if (items == null || items.length == 0) {
			throw new IllegalArgumentException(NCLangRes.getInstance()
					.getStrByID("ganttchart", "GanttChart-000003")/* 列不能为空 */);
		}
		int index=0;
		for (IGantItem item : items) {
			MMGPGantItem  gantItem=(MMGPGantItem)item;
			gantItem.setShowOrder(index);
			if (item.isKeyValue()) {
				KeyColumn<String> keyColumn = new KeyColumn<String>(
						item.getShowName(), item.getValueClassType());
				if (!item.isShow()) {
					keyColumn.setMinimumWidth(0);
					keyColumn.setWidth(0);
				} else {
					keyColumn.setWidth(item.getWidth());
				}
				setKeyColumn(keyColumn);		

			} else {
				AppTreeTableColumn column = new AppTreeTableColumn(item);
				if (!item.isShow()) {
					column.setMinimumWidth(0);
					column.setWidth(0);
				} else {
					column.setWidth(item.getWidth());
				}
				addColumn(column);
				
			}
			index++;
		}

	}

}
