package nc.ui.mmgp.uif2.funcinit;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.uif2.IFuncNodeInitDataListener;
import nc.ui.uif2.model.IAppModelDataManagerEx;

public class TreeManageInitDataListener implements IFuncNodeInitDataListener {

	private IAppModelDataManagerEx manageDataManager;
	private IAppModelDataManagerEx treeDataManager;

	@Override
	public void initData(FuncletInitData data) {
		if (manageDataManager != null) {
			manageDataManager.initModel();
		}
		if (treeDataManager != null) {
			treeDataManager.initModel();
		}
	}

	public IAppModelDataManagerEx getManageDataManager() {
		return manageDataManager;
	}

	public void setManageDataManager(IAppModelDataManagerEx manageDataManager) {
		this.manageDataManager = manageDataManager;
	}

	public IAppModelDataManagerEx getTreeDataManager() {
		return treeDataManager;
	}

	public void setTreeDataManager(IAppModelDataManagerEx treeDataManager) {
		this.treeDataManager = treeDataManager;
	}

}
