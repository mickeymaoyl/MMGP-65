package nc.ui.mmgp.uif2.actions.tree;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.common.MMGPVoUtils;
import nc.itf.mmgp.uif2.IMMGPCmnTreeService;
import nc.ui.bd.pub.actions.BDEnableAction;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

public class MMGPTreeEnableAction extends BDEnableAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1470975058381071571L;

	@Override
	public Object doEnable(Object obj) throws Exception {
		SuperVO vo = (SuperVO) obj;
		MMGPVoUtils.setHeadVOStatus(obj, VOStatus.UPDATED);
		return NCLocator.getInstance().lookup(IMMGPCmnTreeService.class)
				.cmnEnableTreeVO(vo);
	}
}
