package nc.ui.mmgp.uif2.view;

import nc.bs.mmgp.common.CommonUtils;
import nc.ui.mmgp.uif2.model.MMGPTreeMangeModelDataManager;
import nc.ui.uif2.model.AbstractTreeManageQueryAndRefreshMrg;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.bd.access.tree.ITreeCreateStrategy;
import nc.vo.bd.meta.BDObjectTreeCreateStrategy;
import nc.vo.pub.BeanHelper;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class MMGPTreeManageForm extends MMGPShowUpableBillForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8273586184027442334L;
	
	private AbstractTreeManageQueryAndRefreshMrg treeAndListViewRefresh;

	private HierachicalDataAppModel treeAppModel;

	private MMGPTreeMangeModelDataManager datamanager;

	private String pkfieldname;

	@Override
	protected void setDefaultValue() {
		super.setDefaultValue();
		Object selectData = treeAppModel.getSelectedData();
		if (selectData != null) {
			ITreeCreateStrategy strategy = treeAppModel.getTreeCreateStrategy();
			if (strategy instanceof BDObjectTreeCreateStrategy) {
				String className = ((BDObjectTreeCreateStrategy) strategy)
						.getClassName();
				if (pkfieldname == null) {
					pkfieldname = CommonUtils.getPKFieldName(className);
				}
				
				//add by liwsh 2014-09-19 支持右边主子表
				if (selectData instanceof IBill) {
				    getBillCardPanel()
                    .getHeadItem(datamanager.getParentFieldName())
                    .setValue(
                            BeanHelper.getProperty(((IBill)selectData).getParent(), pkfieldname));
				} else { 
				    getBillCardPanel()
                    .getHeadItem(datamanager.getParentFieldName())
                    .setValue(
                            BeanHelper.getProperty(selectData, pkfieldname));
				}
				
				
			}

		}

	}

	public HierachicalDataAppModel getTreeAppModel() {
		return treeAppModel;
	}

	public void setTreeAppModel(HierachicalDataAppModel treeAppModel) {
		this.treeAppModel = treeAppModel;
	}

	public MMGPTreeMangeModelDataManager getDatamanager() {
		return datamanager;
	}

	public void setDatamanager(MMGPTreeMangeModelDataManager datamanager) {
		this.datamanager = datamanager;
	}

	public AbstractTreeManageQueryAndRefreshMrg getTreeAndListViewRefresh() {
		return treeAndListViewRefresh;
	}

	public void setTreeAndListViewRefresh(
			AbstractTreeManageQueryAndRefreshMrg treeAndListViewRefresh) {
		this.treeAndListViewRefresh = treeAndListViewRefresh;
	}

}
