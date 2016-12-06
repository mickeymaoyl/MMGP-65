package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.actions.ShowDisableDataAction;
import nc.ui.uif2.model.IAppModelDataManagerEx;
/**
 * @author赵利 2013-4-19
 * 树管理型节点，【过滤】下，显示停用按钮处理
 * 
 * */
public class MMGPTreeMangShowDisableDataAction extends ShowDisableDataAction{

	private static final long serialVersionUID = -7262458863539120940L;
	private IAppModelDataManagerEx manageDataManager;
	@Override
	public void doAction(ActionEvent e) throws Exception {
		if(isSelected())
		{
			getDataManager().setShowSealDataFlag(true);
			//将右侧管理Manager的是否显示停用置为true 选择，显示停用 
			getManageDataManager().setShowSealDataFlag(true);
		}else
		{
			getDataManager().setShowSealDataFlag(false);
			//将右侧管理Manager的是否显示停用置为false 未选择，不显示停用
			getManageDataManager().setShowSealDataFlag(false);
		} 
		
		doRefresh();
	}
	
	public IAppModelDataManagerEx getManageDataManager() {
		return manageDataManager;
	}
	public void setManageDataManager(IAppModelDataManagerEx manageDataManager) {
		this.manageDataManager = manageDataManager;
	}

	
	
}
