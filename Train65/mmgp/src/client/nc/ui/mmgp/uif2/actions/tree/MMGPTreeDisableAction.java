package nc.ui.mmgp.uif2.actions.tree;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnTreeService;
import nc.ui.bd.pub.actions.BDTreeDisableAction;
import nc.vo.pub.SuperVO;

@SuppressWarnings("serial")
public class MMGPTreeDisableAction extends BDTreeDisableAction {

	private IMMGPCmnTreeService sealService;

	public IMMGPCmnTreeService getManageService() {
		if (sealService == null)
			sealService = NCLocator.getInstance().lookup(
					IMMGPCmnTreeService.class);
		return sealService;
	}

	@Override
	public SuperVO doDisable(SuperVO selectedVO) throws Exception {
		return getManageService().cmnDisableTreeVO(selectedVO);
	}

}
