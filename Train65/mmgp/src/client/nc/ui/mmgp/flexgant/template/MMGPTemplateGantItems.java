package nc.ui.mmgp.flexgant.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.mmgp.flexgant.treetable.MMGPGantItem;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pubapp.gantt.ui.treetable.IGantItem;
import nc.ui.pubapp.gantt.ui.treetable.IGantTableColumns;
import nc.ui.uif2.editor.TemplateContainer;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;



import com.dlsc.flexgantt.swing.treetable.ITreeTableCellEditor;
import com.dlsc.flexgantt.swing.treetable.ITreeTableCellRenderer;

/**
 * @Description: 从单据模板读取item 转换为甘特图的静态item
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-20上午10:06:51
 * @author: tangxya
 */
@SuppressWarnings("restriction")
public class MMGPTemplateGantItems implements IGantTableColumns {

	private static String NODE_KEY = "gant";
	
	List<IGantItem> gantItemList = new ArrayList<IGantItem>();

	private BillCardPanel billcardpanel;

	private TemplateContainer templateContainer;

	/**
		 * 	 
		 */
	private String gantkey;
	
    private String nodekey;	

	protected Map<Class<?>, ITreeTableCellRenderer> renderMap = new HashMap<Class<?>, ITreeTableCellRenderer>();

	Map<Class<?>, ITreeTableCellEditor> coleditorMap = new HashMap<Class<?>, ITreeTableCellEditor>();

	public TemplateContainer getTemplateContainer() {
		return templateContainer;
	}

	public void setTemplateContainer(TemplateContainer templateContainer) {
		this.templateContainer = templateContainer;
	}

	public void init() {
		/* createBillcardpanel(); */
		loadTemplateData();
	}

	protected void loadTemplateData() {
		BillTempletVO template = templateContainer.getTemplate(NODE_KEY, null,
				null);
		BillTempletBodyVO[] bodyVOs = template.getBodyVO();

		for (int i = 0; i < bodyVOs.length; i++) {
			MMGPGantItem gantitem = new MMGPGantItem(bodyVOs[i]);
			if (gantitem.getKey().equals(getGantkey())) {
				gantitem.setKeyValue(true);
			}
			gantItemList.add(gantitem);
		}
		//设置关联字段
		initRelationItem();
	}
	

	protected void initRelationItem() {
		if (MMCollectionUtil.isEmpty(gantItemList)) {
			return;
		}
		Map<String, MMGPGantItem> htGantItems = new HashMap<String, MMGPGantItem>();
		for(IGantItem item:gantItemList){
			MMGPGantItem gantiem = (MMGPGantItem) item;
			htGantItems.put(gantiem.getKey(), gantiem);
		}
		for (IGantItem item : gantItemList) {
			MMGPGantItem gantiem = (MMGPGantItem) item;
			if (item != null) {
				if (gantiem.getIDColName() != null
						&& !gantiem.getKey().equals(gantiem.getIDColName())) {
					MMGPGantItem iditem = htGantItems.get(gantiem
							.getIDColName());
					if (iditem != null)
						iditem.addRelationItem(gantiem);
				}
			}
		}
	}


	@Override
	public List<IGantItem> getGantColumns() {
		return gantItemList;
	}

	/*
	 * private IGantItem changeToGantItem(BillItem item,int order){ String
	 * key=item.getKey(); String name=item.getName(); boolean
	 * isShow=item.isShow(); Class valueClassType=String.class; boolean
	 * isEdit=item.isEdit(); item.getIDColName(); order=item.getShowOrder();
	 * MMGPGantItem gantItem=new
	 * MMGPGantItem(key,name,isShow,order,valueClassType,false);
	 * gantItem.setEdit(isEdit); gantItem.setWidth(100); gantItem.setKeyValue();
	 * return gantItem; }
	 */

	protected void createBillcardpanel() {
		billcardpanel = new BillCardPanel();
		BillTempletVO template = templateContainer.getTemplate(getNodekey(), null,
				null);
		billcardpanel.setBillData(new BillData(template));

	}
	
	

	public String getNodekey() {
		if(MMStringUtil.isEmpty(nodekey)){
			nodekey=NODE_KEY;
		}
		return nodekey;
	}

	public void setNodekey(String nodekey) {
		
		this.nodekey = nodekey;
	}

	public String getGantkey() {
		return gantkey;
	}

	public void setGantkey(String gantkey) {
		this.gantkey = gantkey;
	}

}
