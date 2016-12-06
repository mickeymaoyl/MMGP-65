package nc.ui.mmgp.uif2.actions;

import java.awt.event.ActionEvent;

import nc.ui.uif2.actions.ShowDisableDataAction;
import nc.ui.uif2.model.IAppModelDataManagerEx;
/**
 * @author���� 2013-4-19
 * �������ͽڵ㣬�����ˡ��£���ʾͣ�ð�ť����
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
			//���Ҳ����Manager���Ƿ���ʾͣ����Ϊtrue ѡ����ʾͣ�� 
			getManageDataManager().setShowSealDataFlag(true);
		}else
		{
			getDataManager().setShowSealDataFlag(false);
			//���Ҳ����Manager���Ƿ���ʾͣ����Ϊfalse δѡ�񣬲���ʾͣ��
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
