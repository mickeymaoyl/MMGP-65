package nc.ui.mmgp.uif2.service;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.uif2.IMMGPCmnQueryService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.uif2.model.IPFAppModelService;
import nc.vo.uif2.LoginContext;

public class MMGPPFAppModelService implements IPFAppModelService {
	private LoginContext context;
	/**
	 * 默认只按主键查询
	 */
	@Override
	public Object queryBill(String billId, String billType) throws Exception {
		Class<?> clazz = Class.forName(MMGPMetaUtils.getClassFullName(context));
		IMMGPCmnQueryService service = NCLocator.getInstance().lookup(IMMGPCmnQueryService.class);
		return service.cmnQueryDatasByPk(clazz, billId);
	}
	public LoginContext getContext() {
		return context;
	}
	public void setContext(LoginContext context) {
		this.context = context;
	}
	

}
